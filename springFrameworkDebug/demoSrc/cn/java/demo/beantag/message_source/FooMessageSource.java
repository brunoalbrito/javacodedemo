package cn.java.demo.beantag.message_source;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DelegatingMessageSource;

public class FooMessageSource extends DelegatingMessageSource implements MessageSource {

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		
//		  code === "module0.ctrl0.action0"
//		  args === { new Long(1273), "DiskOne" };
//		  defaultMessage === "The disk \"{1}\" contains {0} file(s)."
//		  locale === Locale.ENGLISH
		
		return super.getMessage(code, args,defaultMessage,locale);
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return super.getMessage(code, args, locale);
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return super.getMessage(resolvable, locale);
	}

}
