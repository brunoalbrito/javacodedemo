package cn.java.demo.hbm.entity.inherit.unionsubclass;


public class Animal {
	
	private String animalId;
	
	private String animalName;
	
	private boolean animalSex;
	
	public String getAnimalId() {
		return animalId;
	}

	public void setAnimalId(String animalId) {
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
