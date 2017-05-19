package cn.java.demo.beantag.bean.applicationlistener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationListenerWithPayloadApplicationEventParamBean implements ApplicationListener<PayloadApplicationEvent> {

	/**
	 * PayloadApplicationEvent 是系统对象,使用数据的包裹对象
	 */
	@Override
	public void onApplicationEvent(PayloadApplicationEvent event) {
		Object source = event.getSource();
		if(source instanceof ClassPathXmlApplicationContext){
			ClassPathXmlApplicationContext classPathXmlApplicationContext = (ClassPathXmlApplicationContext)source;
			System.out.println("classPathXmlApplicationContext.getId() ---> "+classPathXmlApplicationContext.getId());
			System.out.println("event.getPayload() ---> "+event.getPayload());
		}
	}

}
