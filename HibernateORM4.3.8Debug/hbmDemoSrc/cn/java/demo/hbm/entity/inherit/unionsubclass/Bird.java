package cn.java.demo.hbm.entity.inherit.unionsubclass;


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
