package cn.java.demo.jpa.entity.inherit.subclass;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name="cn.java.demo.jpa.entity.inherit.subclass.Pig")
@DiscriminatorValue(value="pig") // 鉴别值是：pig
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
