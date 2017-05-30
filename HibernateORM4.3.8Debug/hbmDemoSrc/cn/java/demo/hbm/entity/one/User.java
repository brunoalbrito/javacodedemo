package cn.java.demo.hbm.entity.one;

import java.util.Date;

public class User {
	private int id;
	private UserName userName;
	private String nickName;
	private Date birthday;

	private int ver;

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public UserName getUserName() {
		return userName;
	}

	public void setUserName(UserName userName) {
		this.userName = userName;
	}

	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", birthday=" + birthday + ", ver=" + ver + "]";
	}
	
	
}
