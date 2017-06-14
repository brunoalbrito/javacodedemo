package cn.java.demo.webmvc.form;

import java.util.List;

public class SpringformTagForm {
	private String username;
	private String password;
	private String csrfToken;
	private String city;
	private String sex;
	private List<String> likes;
	private String description;
	
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
	
	public String getCsrfToken() {
		return csrfToken;
	}
	
	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public List getLikes() {
		return likes;
	}
	public void setLikes(List likes) {
		this.likes = likes;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "SpringformTagForm [username=" + username + ", password=" + password + ", csrfToken=" + csrfToken
				+ ", city=" + city + ", sex=" + sex + ", likes=" + likes + ", description=" + description + "]";
	}
	
	
	
}
