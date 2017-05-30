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
 * 身份证
 */
@Entity(name="cn.java.demo.jpa.entity.one2one.onesided.IdCard")
@Table(name="jpa_one2one_onesided_idcard")
public class IdCard {
	
	@Id
	@Column(name="idcard_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idcardId;
	
	@Column(name="idcard_name")
	private String idcardName;
	
	// 删除IdCard一定要删除Person，所以要配置成cascade={CascadeType.ALL}
	@OneToOne(targetEntity=Person.class,mappedBy="idCard",cascade={CascadeType.ALL},fetch=FetchType.EAGER,optional=false) // 对象级别的关系：对应Person类的idCard属性存放本对象的信息
	@JoinColumn(name="person_id") // 表级别的关系：本表用person_id存放对Person的引用
	private Person person;

	public int getIdcardId() {
		return idcardId;
	}

	public void setIdcardId(int idcardId) {
		this.idcardId = idcardId;
	}

	public String getIdcardName() {
		return idcardName;
	}

	public void setIdcardName(String idcardName) {
		this.idcardName = idcardName;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "IdCard [idcardId=" + idcardId + ", idcardName=" + idcardName + "]";
	}

	
}
