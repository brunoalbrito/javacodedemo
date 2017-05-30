package cn.java.demo.jpa.entity.inherit.unionsubclass;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.inherit.unionsubclass.Bird")
@Table(name="jpa_inherit_unionsubclass_bird")
public class Bird extends Animal {
	private int height;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
