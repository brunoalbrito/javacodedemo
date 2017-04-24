package cn.java.core.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 表单项目列表
 * 
 * @author Administrator
 *
 */
public class FieldHashMap {
	private HashMap hashMap = new HashMap();

	/**
	 * 取得表单值
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		FormValue formValue = (FormValue) hashMap.get(key);
		return formValue.getValue();
	}

	/**
	 * 取得值类型
	 * @param key
	 * @return
	 */
	public String getValueType(String key) {
		FormValue formValue = (FormValue) hashMap.get(key);
		return formValue.getValueType();
	}

	/**
	 * 添加表单值
	 * 
	 * @param key
	 * @param object
	 */
	public void put(String key, Object object) {
		if (hashMap.get(key) == null) {
			FormValue formValue = new FormValue("String", object);
			hashMap.put(key, formValue);
		} else {
			FormValue formValue = (FormValue) hashMap.get(key);
			if (formValue.getValueType() == "String") {
				String strTemp = (String) formValue.getValue();
				ArrayList arrayList = new ArrayList();
				arrayList.add(strTemp);
				arrayList.add(object);
				formValue = new FormValue("ArrayList", arrayList);
				hashMap.put(key, formValue);
			} else if (formValue.getValueType() == "ArrayList") {
				ArrayList arrayList = (ArrayList) formValue.getValue();
				arrayList.add(object);
				hashMap.put(key, formValue);
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		Set set = hashMap.keySet();
		Iterator iterator = set.iterator();
		String str = "";
		while (iterator.hasNext()) {
			String keyName = (String) iterator.next();
			FormValue formValue = (FormValue) hashMap.get(keyName);
			if (formValue.getValueType() == "String") {
				str = str + keyName + ":" + formValue.getValue();
			} else if (formValue.getValueType() == "ArrayList") {
				ArrayList arrayList = (ArrayList) formValue.getValue();
				Iterator iteratorTemp = arrayList.iterator();
				String valueStr = "";
				while (iteratorTemp.hasNext()) {
					String valueTemp = (String) iteratorTemp.next();
					valueStr = valueStr + valueTemp + ",";
				}
				valueStr = valueStr.replaceAll("[,]{1}$", "");
				str = str + keyName + ":" + valueStr;
			}
			str = str + "\n";
		}
		return str;
	}
}
