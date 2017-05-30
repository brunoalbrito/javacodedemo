package cn.java.demo.hbm.entity.field.version;

import java.util.Date;

public class User {
	private int id;
	private String nickName;
	private Date birthday;
	private int ver;

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
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
}
