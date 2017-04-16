package cn.java.data_struct;

import java.util.Arrays;

public class ArrayTest {
	private static final String[] SCOPE_NAMES = new String[] {
            "applicationScope", "cookie", "header", "headerValues",
            "initParam", "pageContext", "pageScope", "param", "paramValues",
            "requestScope", "sessionScope" };
	
	public static void main(String[] args) {
		int idx = Arrays.binarySearch(SCOPE_NAMES, "applicationScope"); // 搜索指定值的索引
		System.out.println(idx); // idx === 0
	}

}
