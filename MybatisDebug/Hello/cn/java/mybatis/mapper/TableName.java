package cn.java.mybatis.mapper;

import java.util.HashMap;
import java.util.Map;

import cn.java.note.收集的代码.JdbcType;

public enum TableName {
	FOO("foo",""),
	USER("user","用户表");

	private String tableName;
	private String tableComment;

	private TableName(String tableName,String tableComment){
		this.tableName = tableName;
		this.tableComment = tableComment;
	}

	private TableName(String tableName){
		this.tableName = tableName;
	}

	// ----------------------------
	private static Map<String,TableName> codeLookup = new HashMap<String,TableName>();
	static {
		for (TableName type : TableName.values()) {
			codeLookup.put(type.tableName, type);
		}
	}
	
	public static TableName forCode(String tableName)  {
		return codeLookup.get(tableName);
	}
}
