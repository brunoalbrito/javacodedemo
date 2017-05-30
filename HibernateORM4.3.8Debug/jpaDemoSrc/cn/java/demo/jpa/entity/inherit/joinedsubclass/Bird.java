package cn.java.demo.jpa.entity.inherit.joinedsubclass;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.inherit.joinedsubclass.Bird")
@Table(name="jpa_inherit_joinedsubclass_bird")
public class Bird extends Animal{
	private int height;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Bird [height=" + height + ", toString()=" + super.toString() + "]";
	}
	
	
}
