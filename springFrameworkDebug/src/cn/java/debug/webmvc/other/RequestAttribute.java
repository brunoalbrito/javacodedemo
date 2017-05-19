package cn.java.debug.webmvc.other;

import java.util.Collections;

import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.servlet.FlashMap;

public class RequestAttribute {

	/*
		org.springframework.web.servlet.DispatcherServlet.doService(...)
		{
			request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext()); // !!!!  getWebApplicationContext() === org.springframework.web.context.support.XmlWebApplicationContext
			request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver); // org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
			request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver); // org.springframework.web.servlet.theme.FixedThemeResolver
			request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource()); // !!!!  getThemeSource() === org.springframework.web.context.support.XmlWebApplicationContext
	
			FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response); // org.springframework.web.servlet.support.SessionFlashMapManager
			if (inputFlashMap != null) {
				request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
			}
			request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
			request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
		}
		
		org.springframework.web.servlet.DispatcherServlet.doDispatch(...)
		{
			asyncManager = new WebAsyncManager()
			servletRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager);
		}
	 */
}
