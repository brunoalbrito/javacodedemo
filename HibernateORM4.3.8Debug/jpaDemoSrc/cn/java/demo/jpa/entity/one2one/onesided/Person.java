package cn.java.demo.jpa.entity.one2one.onesided;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 人
 */
@Entity(name="cn.java.demo.jpa.entity.one2one.onesided.Person")
@Table(name="jpa_one2one_onesided_person")
public class Person {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="person_id")
	private int personId;
	
	@Column(name="person_name")
	private String personName;
	
	// 删除Person一定要删除IdCard，所以要配置成cascade={CascadeType.ALL}
	// mappedBy在哪一端，在哪一端就不维护关系，它成为了关系的被管理端。至少要一端维护关系，所以Person和IdCard不能同时配置mappedBy
	@OneToOne(targetEntity=IdCard.class,cascade={CascadeType.ALL},fetch=FetchType.EAGER,optional=true) // 对象级别的关系：对应IdCard类的person属性存放本对象的信息
	@JoinColumn(name="idcard_id") // 表级别的关系：本表用idcard_id存放对IdCard的引用
	private IdCard idCard;

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public IdCard getIdCard() {
		return idCard;
	}

	public void setIdCard(IdCard idCard) {
		this.idCard = idCard;
	}

	@Override
	public String toString() {
		return "Person [personId=" + personId + ", personName=" + personName + "]";
	}

	
	
}
