package cn.java.configuration.mock;

import java.util.HashMap;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.hibernate.engine.spi.FilterDefinition;

public class ConfigurationMock extends Configuration {
	public static void main(String[] args) {

	}
	
	public void test() {
		Mappings mappings = createMappings();
		
		{
			/*
			 	<filter-def name="name" condition="defaultCondition">
					<filter-param name="paramName0" type="paramType0" />
					<filter-param name="paramName1" type="paramType1" />
				</filter-def>
			 */
			HashMap paramMappings = new HashMap();
			paramMappings.put( "paramName0", "paramType0" );
			FilterDefinition def = new FilterDefinition( "filter0", "defaultCondition", paramMappings );
			mappings.addFilterDefinition( def );
		}
	}
}
