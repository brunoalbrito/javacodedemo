package cn.java.reports.jasperreports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ScrapeFromSpringTest {

	public static void main(String[] args) throws Exception {
		testByWriter();
//		testByStream();
	}

	/**
	 * 使用writer对象写数据
	 */
	public static void testByWriter() throws Exception {
		JRExporter exporter = new JRCsvExporter();
		Writer writer = new StringWriter();
		exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, getFilledReport());
		exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_WRITER, writer);
		exporter.exportReport();
		System.out.println(writer.toString());
	}

	/**
	 * 使用outputStream对象写数据
	 */
	public static void testByStream() throws Exception {
		JRExporter exporter = new JRCsvExporter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
		exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, getFilledReport());
		exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_STREAM, outputStream);
		exporter.exportReport();

	}
	
	public static JasperPrint getFilledReport() throws Exception {
		JasperReport report =  null;
		
		// 加载模板
		{
			String fileNameJrxml = ScrapeFromSpringTest.class.getResource("/").getFile() + "cn/java/reports/jasperreports/scrapeFromSpringTest.jrxml";
			report = loadReport(fileNameJrxml);
		}
		
		// 填充数据
		{
			
			JasperPrint jasperPrint = null;
			Map<String, Object> parameters = new HashMap<>(); // 参数
			parameters.put("parameter0", "parameter0value");
			parameters.put("parameter1", "parameter1value");
			
			// 使用jdbc的数据源
			boolean useJdbcDataSource = false;
			if(useJdbcDataSource)
			{
				DataSource jdbcDataSourceToUse = null;
				Connection con = jdbcDataSourceToUse.getConnection();
				try {
					jasperPrint = JasperFillManager.fillReport(report, parameters, con);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			// 使用jr的数据源
			boolean useJrDataSource = false;
			if(useJdbcDataSource)
			{
				JRDataSource jrDataSource = null;
				{
					Object reportDataValue = null; // 返回的数据
					if (reportDataValue instanceof JRDataSourceProvider) {
						JRDataSourceProvider provider = (JRDataSourceProvider) reportDataValue;
						jrDataSource = provider.create(report);
					}
					else {
						if (reportDataValue instanceof JRDataSource) {
							jrDataSource = (JRDataSource) reportDataValue;
						}
						else if (reportDataValue instanceof Collection) {
							jrDataSource = new JRBeanCollectionDataSource((Collection<?>) reportDataValue);
						}
						else if (reportDataValue instanceof Object[]) {
							jrDataSource = new JRBeanArrayDataSource((Object[]) reportDataValue);
						}
						else {
							throw new IllegalArgumentException("Value [" + reportDataValue + "] cannot be converted to a JRDataSource");
						}
					}
				}
				jasperPrint = JasperFillManager.fillReport(report, parameters, jrDataSource);
			}
			
			// 不使用数据源
			boolean useModelOnly = true;
			if(useModelOnly)
			{
				// JREmptyDataSource参数必须指定
				jasperPrint = JasperFillManager.fillReport(report, parameters,new JREmptyDataSource());
			}
			
			if(jasperPrint==null){
				throw new RuntimeException(" not jasperPrint instance.");
			}
			return jasperPrint;
		}
	}
	
	protected static final JasperReport loadReport(String filename) throws Exception {
		
		if (filename != null) {
			File file = new File(filename);
			if (!file.exists()) {
				throw new RuntimeException(filename + " is not exist.");
			}
			
			if (filename.endsWith(".jasper")) {
				// Load pre-compiled report.
				InputStream is = new FileInputStream(file);
				try {
					return (JasperReport) JRLoader.loadObject(is); // 加载编译后的文件
				} finally {
					is.close();
				}
			} else if (filename.endsWith(".jrxml")) {
				// Compile report on-the-fly.
				InputStream is = new FileInputStream(file);
				try {
					JasperDesign design = JRXmlLoader.load(is); // 加载xml文件
					return JasperCompileManager.compileReport(design);  // 编译
				} finally {
					is.close();
				}
			}
		}
		throw new IllegalArgumentException(
				"Report filename [" + filename + "] must end in either .jasper or .jrxml");
	}

}
