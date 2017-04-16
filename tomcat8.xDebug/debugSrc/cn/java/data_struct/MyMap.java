package cn.java.data_struct;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MyMap extends AbstractMap<String,String> {

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		Set<Map.Entry<String,String>> set = new HashSet<>();
		return set;
	}

}
