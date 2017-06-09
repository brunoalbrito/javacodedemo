package cn.java.demo.webmvc.servlet.view.jasperreports;

import java.net.URL;
import java.util.Locale;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import net.sf.jasperreports.engine.JasperReport;

public class JasperReportsPdfViewExtend extends JasperReportsPdfView {

	/**
	 * 加载报表 - 增加检查存在性的功能
	 */
	@Override
	protected JasperReport loadReport() {
		try {
			if (!checkResourceInternal(null)) {
				return null;
			}
			return super.loadReport();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 支持其仅支持 ".jasper" 和 ".jrxml"
	 */
	private boolean checkResourceInternal(Locale locale) throws Exception {
		String url = getUrl();
		if (url == null) {
			return false;
		}
		Resource resource = getApplicationContext().getResource(url);
		
		// 文件必须存在
		if(!resource.exists()){
			return false;
		}
		
		// 只能指定后缀
		String filename = resource.getFilename();
		if (filename != null) {
			if (filename.endsWith(".jasper")) {
				return true;
			}
			else if (filename.endsWith(".jrxml")) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * 检查存在性 - 增加检查存在性的功能
	 */
	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return checkResourceInternal(locale);
	}
}
