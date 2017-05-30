package cn.java.demo.hbm.entity.field.id.compositeid;

import java.io.Serializable;

/**
 * 实现系列化接口 覆盖hascode、equals方法
 */
public class CompositeId implements Serializable {
	private int firstPartId;
	private int secondPartId;

	public int getFirstPartId() {
		return firstPartId;
	}

	public void setFirstPartId(int firstPartId) {
		this.firstPartId = firstPartId;
	}

	public int getSecondPartId() {
		return secondPartId;
	}

	public void setSecondPartId(int secondPartId) {
		this.secondPartId = secondPartId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + secondPartId;
		result = prime * result + firstPartId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeId other = (CompositeId) obj;
		if (secondPartId != other.secondPartId)
			return false;
		if (firstPartId != other.firstPartId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompositeId [firstPartId=" + firstPartId + ", secondPartId=" + secondPartId + "]";
	}
	
	

}
