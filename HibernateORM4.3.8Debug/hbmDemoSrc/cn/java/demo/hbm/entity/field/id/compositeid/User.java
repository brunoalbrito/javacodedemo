package cn.java.demo.hbm.entity.field.id.compositeid;

import java.util.Date;

public class User {
	private CompositeId compositeId;
	private String nickName;
	private Date birthday;
	
	public CompositeId getCompositeId() {
		return compositeId;
	}
	public void setCompositeId(CompositeId compositeId) {
		this.compositeId = compositeId;
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
		return "User [compositeId=" + compositeId + ", nickName=" + nickName + ", birthday=" + birthday + "]";
	}

	
	
	
	
}
