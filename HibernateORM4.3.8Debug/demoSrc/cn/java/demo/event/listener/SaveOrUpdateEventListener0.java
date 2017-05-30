package cn.java.demo.event.listener;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

import cn.java.curd.entity.User;

/**
 */
public class SaveOrUpdateEventListener0 implements SaveOrUpdateEventListener {

	private static final long serialVersionUID = 1L;
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		if (event.getObject() instanceof User) {
			User user = (User) event.getObject();
			System.out.println("------" + user.getUsername());
		}
	}

}
