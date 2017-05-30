package cn.java.demo.jpa.entity.inherit.unionsubclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="cn.java.demo.jpa.entity.inherit.unionsubclass.Animal")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Animal {
	@Id
	@GenericGenerator(name="myIdGenerator",strategy="uuid")
	@GeneratedValue(generator="myIdGenerator")
	@Column(name = "animal_id")
	private String animalId;
	
	@Column(name = "animal_name")
	private String animalName;
	
	@Column(name = "animal_sex")
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
