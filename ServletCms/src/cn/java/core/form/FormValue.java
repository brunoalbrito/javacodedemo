package cn.java.core.form;

public class FormValue {
	/**
	 * 值的类型
	 */
	private String valueType = "";
	/**
	 * 值
	 */
	private Object value = null;

	public FormValue(String valueType, Object value) {
		this.valueType = valueType;
		this.value = value;
	}

	public FormValue() {
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
