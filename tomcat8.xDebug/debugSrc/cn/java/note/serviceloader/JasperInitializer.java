package cn.java.note.serviceloader;

import java.util.Set;

import javax.servlet.ServletContext;

/**
 * org.apache.jasper.servlet.JasperInitializer  jasper.jar
 */
public class JasperInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) {
		
	}

}