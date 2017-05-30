package cn.java.demo.jpa.createtables;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ExportDB {
	public static void main(String[]args ){
//		Configuration cfg = new Configuration().configure();//解析*.hbm.xml的映射文件
		Configuration cfg = new AnnotationConfiguration().configure();//解析@Entity注解方式的映射
	
		SchemaExport export = new SchemaExport(cfg);
		export.create(true, true);
	}
}





































