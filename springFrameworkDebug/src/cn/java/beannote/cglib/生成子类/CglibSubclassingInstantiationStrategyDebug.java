package cn.java.beannote.cglib.生成子类;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class CglibSubclassingInstantiationStrategyDebug extends CglibSubclassingInstantiationStrategy {
	
	@Override
	protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner) {
		return instantiateWithMethodInjection(bd, beanName, owner, null);
	}

}
