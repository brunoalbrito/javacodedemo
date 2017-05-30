package cn.java.demo.jpa.entity.inherit.joinedsubclass;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.inherit.joinedsubclass.Pig")
@Table(name="jpa_inherit_joinedsubclass_pig")
public class Pig extends Animal {
	
	private int weigth;

	public int getWeigth() {
		return weigth;
	}

	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}

	@Override
	public String toString() {
		return "Pig [weigth=" + weigth + ", toString()=" + super.toString() + "]";
	}
	
}
