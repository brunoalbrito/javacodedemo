package cn.java.note.beans.beanwrapper;

import java.beans.PropertyDescriptor;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

public class BeanWrapperTest {

	public static void main(String[] args) {
		Object beanInstance = new Bean1();
		BeanWrapper beanWrapper = new BeanWrapperImpl(beanInstance);
		
		{
			Set<String> result = new TreeSet<String>();
			PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors(); // 对象的属性
			for (PropertyDescriptor pd : pds) {
				if (pd.getWriteMethod() != null && !BeanUtils.isSimpleProperty(pd.getPropertyType())) {
					result.add(pd.getName());
				}
			}
			System.out.println(StringUtils.toStringArray(result));
		}
		
	}

	private static class Bean1{
		private int userid;
		private String username;
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
}
