package cn.java.aopnote;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aspectj.util.PartialOrder;
import org.aspectj.util.PartialOrder.PartialComparable;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAopUtils;
import org.springframework.aop.aspectj.AspectJPrecedenceInformation;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class SortAdvisorsTest {

	public static void main(String[] args) {
		/*
			org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator.sortAdvisors(...)
		 */
	}
	
	private static final Comparator<Advisor> DEFAULT_PRECEDENCE_COMPARATOR = new AspectJPrecedenceComparator();
	protected List<Advisor> sortAdvisors(List<Advisor> advisors) {
		List<PartiallyComparableAdvisorHolder> partiallyComparableAdvisors =
				new ArrayList<PartiallyComparableAdvisorHolder>(advisors.size());
		for (Advisor element : advisors) {
			partiallyComparableAdvisors.add(
					new PartiallyComparableAdvisorHolder(element, DEFAULT_PRECEDENCE_COMPARATOR));
		}
		List<PartiallyComparableAdvisorHolder> sorted =
				PartialOrder.sort(partiallyComparableAdvisors); // 会构造一个图
		if (sorted != null) {
			List<Advisor> result = new ArrayList<Advisor>(advisors.size());
			for (PartiallyComparableAdvisorHolder pcAdvisor : sorted) {
				result.add(pcAdvisor.getAdvisor());
			}
			return result;
		}
		else {
			AnnotationAwareOrderComparator.sort(advisors);
			return advisors;
		}
	}
	
	
	/**
	 * Implements AspectJ PartialComparable interface for defining partial orderings.
	 */
	private static class PartiallyComparableAdvisorHolder implements PartialComparable {

		private final Advisor advisor;

		private final Comparator<Advisor> comparator;

		public PartiallyComparableAdvisorHolder(Advisor advisor, Comparator<Advisor> comparator) {
			this.advisor = advisor;
			this.comparator = comparator;
		}

		@Override
		public int compareTo(Object obj) {
			Advisor otherAdvisor = ((PartiallyComparableAdvisorHolder) obj).advisor;
			return this.comparator.compare(this.advisor, otherAdvisor);
		}

		@Override
		public int fallbackCompareTo(Object obj) {
			return 0;
		}

		public Advisor getAdvisor() {
			return this.advisor;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			Advice advice = this.advisor.getAdvice();
			sb.append(ClassUtils.getShortName(advice.getClass()));
			sb.append(": ");
			if (this.advisor instanceof Ordered) {
				sb.append("order ").append(((Ordered) this.advisor).getOrder()).append(", ");
			}
			if (advice instanceof AbstractAspectJAdvice) {
				AbstractAspectJAdvice ajAdvice = (AbstractAspectJAdvice) advice;
				sb.append(ajAdvice.getAspectName());
				sb.append(", declaration order ");
				sb.append(ajAdvice.getDeclarationOrder());
			}
			return sb.toString();
		}
	}
	
	private static class AspectJPrecedenceComparator implements Comparator<Advisor> {

		private static final int HIGHER_PRECEDENCE = -1;

		private static final int SAME_PRECEDENCE = 0;

		private static final int LOWER_PRECEDENCE = 1;


		private final Comparator<? super Advisor> advisorComparator;


		/**
		 * Create a default AspectJPrecedenceComparator.
		 */
		public AspectJPrecedenceComparator() {
			this.advisorComparator = AnnotationAwareOrderComparator.INSTANCE;
		}

		/**
		 * Create a AspectJPrecedenceComparator, using the given Comparator
		 * for comparing {@link org.springframework.aop.Advisor} instances.
		 * @param advisorComparator the Comparator to use for Advisors
		 */
		public AspectJPrecedenceComparator(Comparator<? super Advisor> advisorComparator) {
			Assert.notNull(advisorComparator, "Advisor comparator must not be null");
			this.advisorComparator = advisorComparator;
		}


		@Override
		public int compare(Advisor o1, Advisor o2) {
			int advisorPrecedence = this.advisorComparator.compare(o1, o2);
			if (advisorPrecedence == SAME_PRECEDENCE && declaredInSameAspect(o1, o2)) {
				advisorPrecedence = comparePrecedenceWithinAspect(o1, o2);
			}
			return advisorPrecedence;
		}

		private int comparePrecedenceWithinAspect(Advisor advisor1, Advisor advisor2) {
			boolean oneOrOtherIsAfterAdvice =
					(AspectJAopUtils.isAfterAdvice(advisor1) || AspectJAopUtils.isAfterAdvice(advisor2));
			int adviceDeclarationOrderDelta = getAspectDeclarationOrder(advisor1) - getAspectDeclarationOrder(advisor2);

			if (oneOrOtherIsAfterAdvice) {
				// the advice declared last has higher precedence
				if (adviceDeclarationOrderDelta < 0) {
					// advice1 was declared before advice2
					// so advice1 has lower precedence
					return LOWER_PRECEDENCE;
				}
				else if (adviceDeclarationOrderDelta == 0) {
					return SAME_PRECEDENCE;
				}
				else {
					return HIGHER_PRECEDENCE;
				}
			}
			else {
				// the advice declared first has higher precedence
				if (adviceDeclarationOrderDelta < 0) {
					// advice1 was declared before advice2
					// so advice1 has higher precedence
					return HIGHER_PRECEDENCE;
				}
				else if (adviceDeclarationOrderDelta == 0) {
					return SAME_PRECEDENCE;
				}
				else {
					return LOWER_PRECEDENCE;
				}
			}
		}

		private boolean declaredInSameAspect(Advisor advisor1, Advisor advisor2) {
			return (hasAspectName(advisor1) && hasAspectName(advisor2) &&
					getAspectName(advisor1).equals(getAspectName(advisor2)));
		}

		private boolean hasAspectName(Advisor anAdvisor) {
			return (anAdvisor instanceof AspectJPrecedenceInformation ||
					anAdvisor.getAdvice() instanceof AspectJPrecedenceInformation);
		}

		// pre-condition is that hasAspectName returned true
		private String getAspectName(Advisor anAdvisor) {
			return AspectJAopUtils.getAspectJPrecedenceInformationFor(anAdvisor).getAspectName();
		}

		private int getAspectDeclarationOrder(Advisor anAdvisor) {
			AspectJPrecedenceInformation precedenceInfo =
				AspectJAopUtils.getAspectJPrecedenceInformationFor(anAdvisor);
			if (precedenceInfo != null) {
				return precedenceInfo.getDeclarationOrder();
			}
			else {
				return 0;
			}
		}

	}
}
