package cn.java.note.all;

import java.util.Set;

import org.springframework.util.Assert;

/**
 * http://blog.csdn.net/lufeng20/article/details/24314381
 * @author zhouzhian
 *
 */
public class ThreadLocalTest {

	public static void main(String[] args){
		(new NamedThreadLocalService()).test("beanName1");
	}

	public static class NamedThreadLocalService {
		private final ThreadLocal<Object> prototypesCurrentlyInCreation =
				new NamedThreadLocal<Object>("Prototype beans currently in creation");
		public void test(String beanName) {
			// 设置
			Object curVal = this.prototypesCurrentlyInCreation.get();
			if (curVal == null) {
				this.prototypesCurrentlyInCreation.set(beanName);
			}
			else {
				Set<String> beanNameSet = (Set<String>) curVal;
				beanNameSet.add(beanName);
			}
			
			
			// 删除
			curVal = this.prototypesCurrentlyInCreation.get();
			if (curVal instanceof Set) {
				Set<String> beanNameSet = (Set<String>) curVal;
				beanNameSet.remove(beanName);
				if (beanNameSet.isEmpty()) {
					this.prototypesCurrentlyInCreation.remove();
				}
			}
		}
	}
	public static class NamedThreadLocal<T> extends ThreadLocal<T> {

		private final String name;


		/**
		 * Create a new NamedThreadLocal with the given name.
		 * @param name a descriptive name for this ThreadLocal
		 */
		public NamedThreadLocal(String name) {
			Assert.hasText(name, "Name must not be empty");
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

	}
}
