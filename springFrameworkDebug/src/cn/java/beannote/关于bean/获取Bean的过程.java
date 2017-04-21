package cn.java.beannote.关于bean;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

public class 获取Bean的过程 {

	public static void main(String[] args) {
		/**
		 	
		 	------注意点------
		 		<bean>没有“带参数的构造函数”，必须设置scope="prototype"，不然spring对没有配置scope="prototype"的bean，默认为是单例的bean，在预创建的时候，会使用“不带参数的构造函数”实例化，会出错
		 	
		 	------实例化过程------
		 	1、实例化bean （自动装配在这个步骤也有使用，即：根据构造函数传递参数信息进行装配）
		 	{
		 		- 不需要生成子类
		 		
		 		- 需要生成子类（有配置 <lookup-method> 或者 <replaced-method>）
		 			org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy.instantiate(mbd, beanName, parent);
		  			beanInstance = this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, this.beanFactory, factoryBean, factoryMethodToUse, argsToUse);
		 	}
		 	2、填充bean的属性（自动装配）
		 	{
		 	
		 		根据setter的属性名称进行装配
		 		{
		 		
		 		}
		 		
		 		根据setter的参数类型进行装配
		 		{
			 		参数类型是 Optional
			 		{
			 			 当根据参数类型查找到的bean有多个的时候，决策规则是：
					  		1、检查有配置primary的bean，有且只能有一个设置为primary的bean； 
					  		2、根据bean优先权，不能有两个一样优先权的bean   
					  		3、根据参数名获取到bean
			 		}
			 		
			 		参数类型是 ObjectFactory 或者 参数类型是 ObjectProvider
			 		{
			 			 当根据参数类型查找到的bean有多个的时候，决策规则是：
					  		1、检查有配置primary的bean，有且只能有一个设置为primary的bean； 
					  		2、根据bean优先权，不能有两个一样优先权的bean   
					  		3、根据参数名获取到bean
			 		}
			 		
			 		参数类型 javax.inject.Provider
			 		{
			 			 当根据参数类型查找到的bean有多个的时候，决策规则是：
					  		1、检查有配置primary的bean，有且只能有一个设置为primary的bean； 
					  		2、根据bean优先权，不能有两个一样优先权的bean   
					  		3、根据参数名获取到bean
			 		}
			 		
			 		自定义类型
			 		{
			 			 当根据参数类型查找到的bean有多个的时候，决策规则是：
					  		1、检查有配置primary的bean，有且只能有一个设置为primary的bean； 
					  		2、根据bean优先权，不能有两个一样优先权的bean   
					  		3、根据参数名获取到bean
			 		}
		 		}
		 		
		 	}
		 	3、检查bean的实现的感知接口（ BeanNameAware、BeanClassLoaderAware、BeanFactoryAware ），注入感知
			4、根据<bean init-method="initMethod">配置，调用bean的初始化方法
		 */
		
	}

}
