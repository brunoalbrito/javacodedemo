package cn.java.note.beans.propertyeditor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class UserBeanInfo extends SimpleBeanInfo {  

	@Override  
	public PropertyDescriptor[] getPropertyDescriptors() {  
		try {  
			// 1. 将 SexCnNameEditor 绑定到 User 的 sex 属性中      
			ArrayList<PropertyDescriptor> arrayList = new ArrayList();
			Field[] fields = User.class.getDeclaredFields();
			for(Field field:fields){
				PropertyDescriptor pd = null;
				if(field.getName() == "sex"){ // 自定义属性描述器
					pd = new PropertyDescriptor("sex", User.class);  
					pd.setPropertyEditorClass(UserSexEditor.class); 
				}
				else{
					pd = new PropertyDescriptor(field.getName(), User.class);  
				}
				arrayList.add(pd);
			}
			return (PropertyDescriptor[]) arrayList.toArray(new PropertyDescriptor[arrayList.size()]);
		} catch (IntrospectionException e) {  
			e.printStackTrace();  
			return null;  
		}  
	}  
}  