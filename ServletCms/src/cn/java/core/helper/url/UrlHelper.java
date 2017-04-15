package cn.java.core.helper.url;

public class UrlHelper {
	/**
	 * 生成URL地址
	 * 
	 * @param module
	 * @param controller
	 * @param action
	 * @param paramStr
	 * @return
	 */
	public static String url(String module, String controller, String action, String paramStr) {
		String url = "/" + module + "/" + controller + "/?act=" + action;
		if (paramStr != null && (!paramStr.equals(""))) {
			return url + "&" + paramStr;
		}
		return url;
	}

	public static String url(String module, String controller, String action) {
		return url(module, controller, action, "");
	}

	public static String baseUrl() {
		return "";
	}
}
