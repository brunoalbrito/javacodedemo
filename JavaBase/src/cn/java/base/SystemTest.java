package cn.java.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SystemTest {

	public static void main(String[] args) {
		System.out.println("---------System.getProperties()--------------");
		{
			// 通过命令行参数的"-D"选项
			Properties properties = System.getProperties();
			Set keySet = properties.keySet();
			for(Iterator iterator = keySet.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				System.out.println(key+" = " + properties.getProperty(key));
			}
		}
           
        System.out.println("\n---------System.getenv()--------------");
        {
        	// 系统环境变量值
        	Map map = System.getenv();
        	Set keySet =  map.keySet();
        	for (Object key : keySet) {
        		System.out.println(key+" = " + map.get(key));
        	}
        }
	}

}
