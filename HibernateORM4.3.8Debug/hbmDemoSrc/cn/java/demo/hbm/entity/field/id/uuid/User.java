package cn.java.demo.hbm.entity.field.id.uuid;

import java.util.Date;

public class User {
	private String uuidStr;
	private String nickName;
	private Date birthday;
	
	public String getUuidStr() {
		return uuidStr;
	}
	public void setUuidStr(String uuidStr) {
		this.uuidStr = uuidStr;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Override
	public String toString() {
		return "User [uuidStr=" + uuidStr + ", nickName=" + nickName + ", birthday=" + birthday + "]";
	}

	
	
}
