package cn.java.demo.hbm.entity.one2one;

import java.util.Date;

public class IdCard {

	private int id;
	private Date usefulLife;
	private Person person;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getUsefulLife() {
		return usefulLife;
	}

	public void setUsefulLife(Date usefulLife) {
		this.usefulLife = usefulLife;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "IdCard [id=" + id + ", usefulLife=" + usefulLife + "]";
	}

	
}
