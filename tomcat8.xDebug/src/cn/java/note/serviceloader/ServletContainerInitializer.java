package cn.java.note.serviceloader;

import java.util.Set;

import javax.servlet.ServletContext;

public interface ServletContainerInitializer {
	void onStartup(Set<Class<?>> c, ServletContext ctx);
}