package cn.java.note.beans.propertychange;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 属性改变监听器
 * @author Administrator
 *
 */
class UserPropertyChangeListener implements PropertyChangeListener {

	private User user;

	public UserPropertyChangeListener(User user) {
		super();
		this.user = user;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getPropertyName());
		if ("username".equals(evt.getPropertyName())) {
			System.out.println(this.user.getUsername()==evt.getOldValue());
			System.out.println(this.user.getUsername()==evt.getNewValue());
		}
	}

}