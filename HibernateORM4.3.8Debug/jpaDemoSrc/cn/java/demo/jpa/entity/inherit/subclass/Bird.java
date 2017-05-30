package cn.java.demo.jpa.entity.inherit.subclass;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name="cn.java.demo.jpa.entity.inherit.subclass.Bird")
@DiscriminatorValue(value="bird")//鉴别值是：B
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
