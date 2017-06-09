package cn.java.demo.core.env;

import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

public class MutablePropertySourcesExtend extends MutablePropertySources {
	
	public MutablePropertySourcesExtend(PropertySource[] propertySources) {
		super();
		for (PropertySource<?> propertySource : propertySources) {
			addLast(propertySource);
		}
	}

}
