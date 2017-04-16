package cn.java.note.beans.propertychange;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
public class User {
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	private int id;
	private String username;
	private String password;

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		String oldUsername = this.username;
		this.username = username;
		support.firePropertyChange("username", oldUsername, this.username); // 触发属性改变事件
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String oldPassword = this.password;
		this.password = password;
		support.firePropertyChange("password", oldPassword, this.password);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

}