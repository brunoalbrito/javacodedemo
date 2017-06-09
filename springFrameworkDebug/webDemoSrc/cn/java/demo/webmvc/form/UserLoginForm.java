package cn.java.demo.webmvc.form;

public class UserLoginForm {
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "UserLoginForm [username=" + username + ", password=" + password + "]";
	}
	
	
}
