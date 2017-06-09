package cn.java.reports.jasperreports;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class Test {
	
//	1. 扩展名为.jrxml的文件为标准的xml文件，该文件定义了报表的格式和数据构成。可以通过jasperReports的“黄金搭档”iReport以可视化的方式生成并编辑这个文件。  
//	2. jrxml文件经过JasperReports API 编译后将生成扩展名为.jasper的二进制文件。  
//	3. 可以调用JasperReports API针对jasper文件进行数据和参数的填充，生成扩展名为.jrprint的文件。  
//	4. 调用JasperReports API可以将jrprint文件最终导出成PDF、Excel、Html等各种格式的文件。      
	
	/**
	 * 官网：
	 * http://community.jaspersoft.com/project/jasperreports-library
	 * http://www.programcreek.com/java-api-examples/index.php?api=net.sf.jasperreports.engine.JRExporter
	 * http://www.cnblogs.com/mingforyou/p/4568521.html
	 * https://my.oschina.net/zzq350623/blog/519542
	 * http://blog.csdn.net/qq_26906345/article/details/51786910
	 */
	public static void main(String[] args) throws Exception {
		
		String fileNameJrxml = ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/test.jrxml";
		String fileNameJasper = ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/test.jasper";
		String fileNameJrprint = ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/test.jrprint";
		String fileNameHtml = ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/test.html";
		String fileNamePdf = ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/test.pdf";
		
		
		// ---1--- 编译模板（.jrxml -> .jasper）
		JasperCompileManager.compileReportToFile(fileNameJrxml,fileNameJasper);
		
		JasperPrint jasperPrint = null;
		boolean useJdbcConnection = false;
		Connection conn = null;
		Map parameters = new HashMap();
		parameters.put("parameter0", "parameter0value");
		parameters.put("parameter1", "parameter1value");
		
		// 方式1 （.jasper -> .jrprint）
		boolean useTypeOne = true;
		if(useTypeOne){ // 这种方式不可以
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(fileNameJasper));
			if(useJdbcConnection){
				Class.forName("com.MySQL.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db001", "root", "123456");
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			}
			else{
				// JREmptyDataSource参数必须指定
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new JREmptyDataSource());
			}
		}
		// 方式二 （.jasper -> .jrprint）
		if(true){ // 这种方式可以
			JasperFillManager.fillReportToFile(fileNameJasper,fileNameJrprint, parameters, new JREmptyDataSource());
			jasperPrint = (JasperPrint) JRLoader.loadObject(new File(fileNameJrprint));
		}
		
		// ---3--- 生成报表
		boolean isExportReportToHtmlFile = true;
		if(isExportReportToHtmlFile){
			JasperExportManager.exportReportToHtmlFile(jasperPrint,fileNameHtml);
		}
		boolean isExportReportToPdfFile = false;
		if(isExportReportToPdfFile){
			JasperExportManager.exportReportToPdfFile(jasperPrint,fileNamePdf);
		}
		if(useJdbcConnection){
			conn.close();
		}
		System.out.println("saveDir = "+ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/");
		System.out.println("-----------ok----------");
	}
}
