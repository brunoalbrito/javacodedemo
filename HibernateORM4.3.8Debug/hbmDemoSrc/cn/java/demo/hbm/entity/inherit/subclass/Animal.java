package cn.java.demo.hbm.entity.inherit.subclass;


public class Animal {
	
	private int animalId;
	
	private String animalName;
	
	private boolean animalSex;

	public int getAnimalId() {
		return animalId;
	}

	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}

	public String getAnimalName() {
		return animalName;
	}

	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}

	public boolean isAnimalSex() {
		return animalSex;
	}

	public void setAnimalSex(boolean animalSex) {
		this.animalSex = animalSex;
	}

	@Override
	public String toString() {
		return "Animal [animalId=" + animalId + ", animalName=" + animalName + ", animalSex=" + animalSex + "]";
	}
	
	
}
