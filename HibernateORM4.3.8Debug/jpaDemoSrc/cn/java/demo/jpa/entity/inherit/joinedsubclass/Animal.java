package cn.java.demo.jpa.entity.inherit.joinedsubclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.inherit.joinedsubclass.Animal")
@Table(name="jpa_inherit_joinedsubclass_animal")
@Inheritance(strategy=InheritanceType.JOINED) // 三张表
public class Animal {
	@Id
	@GeneratedValue
	@Column(name = "animal_id")
	private int animalId;
	
	@Column(name = "animal_name")
	private String animalName;
	
	@Column(name = "animal_sex")
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
