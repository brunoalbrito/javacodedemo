package cn.java.debug.aopnote;

public class 五种通知接受器_三种通知接收器的适配器 {

	public static void main(String[] args) {
		
		/*
		 	通知接受者Advice = {
			 	<before ...> == org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
				<after ...> === org.springframework.aop.aspectj.AspectJAfterAdvice
				<after-returning ...> === org.springframework.aop.aspectj.AspectJAfterReturningAdvice
				<after-throwing ...> === org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
				<around ...> === org.springframework.aop.aspectj.AspectJAroundAdvice
		 	}
		 	
		 	如下三种适配器：
			 	adapters = {
			 		org.springframework.aop.framework.adapter.MethodBeforeAdviceAdapter,
			 		org.springframework.aop.framework.adapter.AfterReturningAdviceAdapter,
			 		org.springframework.aop.framework.adapter.ThrowsAdviceAdapter,
			 	}
		 	
		 	适配完的拦截器是：
	 			Interceptor = {
		 			<before ...> == org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor
		 			<after ...> === ...没被适配org.springframework.aop.aspectj.AspectJAfterAdvice
					<after-returning ...> === org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor
					<after-throwing ...> === ...没被适配org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
					<around ...> === ...没被适配org.springframework.aop.aspectj.AspectJAroundAdvice
	 			}
	 			
		 */
		
		/*
		 	<aop:config proxy-target-class="false" expose-proxy="false">
		 		<aop:pointcut id="" expression="" />
		 		<aop:advisor id="" advice-ref="" pointcut="" pointcut-ref="" order="" />
		 		<aop:aspect id="" ref="" order="">
		 			<aop:pointcut id="" expression=""></aop:pointcut>
		 			<aop:declare-parents types-matching="" implement-interface="" default-impl="" delegate-ref="" />
		 			<aop:before pointcut="" pointcut-ref="" method="" arg-names="" />
		 			<aop:after pointcut="" pointcut-ref="" method="" arg-names="" />
		 			<aop:after-returning pointcut="" pointcut-ref="" method="" arg-names="" />
		 			<aop:after-throwing   pointcut="" pointcut-ref="" method="" arg-names="" />
		 			<aop:around pointcut="" pointcut-ref="" method="" arg-names="" ></aop:around>
		 		</aop:aspect>
		 	</aop:config>
		 */
		
	}

}
