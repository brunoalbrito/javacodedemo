package cn.java.demo.beantag.bean.applicationlistener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class ApplicationListenerWithApplicationEventParamBean implements ApplicationListener<ApplicationEvent> {

	/**
	 * ApplicationEvent 是用户实现的的，是被原样传递过来的
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ApplicationEventImpl){
			ApplicationEventImpl applicationEventImpl = (ApplicationEventImpl)event;
			Object source = applicationEventImpl.getSource();
			if(source instanceof FooEventTriggerBean){
				FooEventTriggerBean fooEventTriggerBean = (FooEventTriggerBean)source;
				System.out.println(fooEventTriggerBean);
			}
		}
	}

}
