package cn.java.demo.jpa.entity.inherit.subclass;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.inherit.subclass.Animal")
@Table(name="jpa_inherit_subclass_animal")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE) // 整个继承树就只有一张表
@DiscriminatorColumn(name="animal_type",discriminatorType=DiscriminatorType.STRING) // 鉴别字段
public class Animal {
	@Id
	@GeneratedValue()
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
