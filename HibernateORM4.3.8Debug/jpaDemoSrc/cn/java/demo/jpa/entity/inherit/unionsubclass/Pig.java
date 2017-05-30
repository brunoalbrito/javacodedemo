package cn.java.demo.jpa.entity.inherit.unionsubclass;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity(name="cn.java.demo.jpa.entity.inherit.unionsubclass.Pig")
@Table(name="jpa_inherit_unionsubclass_pig")
public class Pig extends Animal {
	
	private int weigth;

	public int getWeigth() {
		return weigth;
	}

	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}
	
}
