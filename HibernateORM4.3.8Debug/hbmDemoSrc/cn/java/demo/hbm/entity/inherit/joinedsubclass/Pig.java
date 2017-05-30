package cn.java.demo.hbm.entity.inherit.joinedsubclass;



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
