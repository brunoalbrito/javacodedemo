package cn.java.core.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jasper.runtime.ServletResponseWrapperInclude;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 */
public class CoreSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected String tplRootDir = "/WEB-INF/servletView/";
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	protected void note() {
		//JspRuntimeLibrary.include(request, response, "date.jsp", out, true); 
	}

	protected void initial() {

	}

	public CoreSerlvet() {
		this.initial();
	}

	/**
	 * get请求
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * post请求
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		String actionName = request.getParameter("act");
		if (actionName == null || "".equals(actionName)) {
			actionName = "index";
		}
		actionName = actionName + "Action";
		String denyString = "service,log,init,destroy,getServletContext,getInitParameter,getInitParameterNames,getServletConfig,getServletInfo,getServletName,wait,equals,toString,hashCode,getClass,notify,notifyAll";
		String[] denyStrArray = denyString.toLowerCase().split(",");
		List denyList = Arrays.asList(denyStrArray);
		if (denyList.contains(actionName.toLowerCase())) {
			response.sendError(404);//禁止访问的方法
			response.getWriter().write("miss action...");
		} else {
			Method[] methods = this.getClass().getMethods();
			boolean isHit = false;
			for (Method method : methods) {
				//System.out.println(method.getName());
				if (method.getName().equals(actionName)) {
					//System.out.println(method.getName() + "---" + actionName + "\n");
					if (method.getModifiers() == Modifier.PUBLIC) {
						try {
							method.invoke(this, null);
							isHit = true;
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (!isHit) {
				response.sendError(404);//禁止访问的方法
				response.getWriter().write("miss action...");
			}
		}
	}

	/**
	 * 赋值
	 * 
	 * @param key
	 * @param object
	 */
	protected void assign(String key, Object object) {
		request.setAttribute(key, object);
	}

	/**
	 * 输出内容
	 * 
	 * @param tplName
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void display(String tplName) throws ServletException, IOException {
		String tplName1 = "";
		if (tplName != null && (!"".equals(tplName))) {
			if (!tplName.startsWith("/"))
				tplName1 = tplRootDir + tplName;//tplName = "test2Servlet-doPost.jsp"
			else
				tplName1 = tplName;
		}
		request.getRequestDispatcher(tplName1).forward(request, response);//内部转发
	}

	/**
	 * 抓取模板内容
	 * 
	 * @param tplName
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	protected String fetch(String tplName) throws IOException, ServletException {
		String tplName1 = "";
		if (tplName != null && (!"".equals(tplName))) {
			if (!tplName.startsWith("/"))
				tplName1 = tplRootDir + tplName;//tplName = "test2Servlet-testFetch.jsp"
			else
				tplName1 = tplName;
		}
		MyJspWriterImpl mMyJspWriterImpl = new MyJspWriterImpl();
		org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, tplName1, mMyJspWriterImpl, false);
		return mMyJspWriterImpl.getHtmlStrTemp().trim();
	}

	/**
	 * 抓取模板内容
	 * 
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	protected String fetch2(String resourcePath) throws IOException, ServletException {
		MyJspWriterImpl mMyJspWriterImpl = new MyJspWriterImpl();
		RequestDispatcher mRequestDispatcher = request.getRequestDispatcher(resourcePath);//org.apache.catalina.core.ApplicationDispatcher
		mRequestDispatcher.include(request, new ServletResponseWrapperInclude(response, mMyJspWriterImpl));
		return mMyJspWriterImpl.getHtmlStrTemp().trim();
	}

	/**
	 * 重定向
	 * 
	 * @param url
	 */
	protected void redirect(String url) {
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分页
	 * @param path
	 * @param currPage
	 * @param totalCount
	 * @param perPageShowCount 每页显示数量
	 * @param showRangCount  过渡页数量
	 * @return
	 */
	protected String pagination(String path, int currPage, int totalCount, int perPageShowCount, int showRangCount) {
		if (totalCount > perPageShowCount) {
			String strTemp = "";
			String pageTag = "&page=";
			path = path.trim();
			path = path.replaceAll("[/]{1}$", "");
			int pageCount = totalCount / perPageShowCount;//6
			if(totalCount % perPageShowCount >0){
				pageCount = pageCount + 1;
			}
//			System.out.println("pageCount="+pageCount+",totalCount="+totalCount+",perPageShowCount="+perPageShowCount+",showRangCount="+showRangCount);
//			strTemp += "<li><a href=\"" + path + pageTag + "1\" class=\"prev\"></a>首页</li>";
			if (currPage > 1){
//				strTemp += "<li><a href=\"" + path + pageTag + (currPage - 1) + "\" class=\"prev\">上一页</a></li>";
				strTemp += "<li><a href=\"" + path + pageTag + (currPage - 1) + "\" class=\"prev\"></a></li>";
			}
			if (showRangCount > 0) {
				if (showRangCount < pageCount) { // 小于总页数
					int beforeShowCount = showRangCount / 2;
					int afterShowCount = beforeShowCount;
					if ((currPage + afterShowCount) > pageCount) {//后越界
						afterShowCount = pageCount - currPage;
						beforeShowCount = showRangCount - afterShowCount - 1;
					} else if ((currPage - beforeShowCount) <= 0) {//前越界
						beforeShowCount = currPage - 1;
						afterShowCount = showRangCount - beforeShowCount - 1;
					}

					//前一半
					for (int i = currPage - beforeShowCount; i < currPage; i++) {
						strTemp += "<li><a href=\"" + path + pageTag + i + "\" class=\"num\">" + i + "</a></li>";
					}
					//当前页
					strTemp += "<li class=\"active\"><a href=\"javascript:void(0);\">" + currPage + "</a></li>";
					//后一半
					for (int i = currPage + 1; i <= currPage + afterShowCount; i++) {
						strTemp += "<li><a href=\"" + path + pageTag + i + "\"  class=\"num\">" + i + "</a></li>";
					}
				} else {
					//全部显示
					for (int i = 1; i <= pageCount; i++) {
						if(i==currPage){
							strTemp += "<li class=\"active\"><a href=\"javascript:void(0);\">" + currPage + "</a></li>";
						}
						else{
							strTemp += "<li><a href=\"" + path + pageTag + i + "\">" + i + "</a></li>";
						}
					}
				}
			}

			if ((currPage >= 0) && (currPage < pageCount)){
//				strTemp += "<li><a href=\"" + path + pageTag + (currPage + 1) + "\" class=\"next\">下一页</a></li>";
				strTemp += "<li><a href=\"" + path + pageTag + (currPage + 1) + "\" class=\"next\"></a></li>";
			}
//			strTemp += "<li><a href=\"" + path + pageTag + pageCount + "\">尾页</a></li>";
			strTemp = "<ul class=\"pagination\">" + strTemp + "</ul>";
			return strTemp;
		}
		return "";
	}

	protected void ajaxHtmlReturn(int status,String message,String html) throws IOException {
		JSONObject mJSONObject = new JSONObject();
		mJSONObject.put("status", status);
		mJSONObject.put("info", message);
		
		JSONObject mJSONObject2 = new JSONObject();
		mJSONObject2.put("html", html);
		mJSONObject.put("data", mJSONObject2);
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-type", "text/json");
		response.getWriter().write(mJSONObject.toJSONString());
		return;
	}
	
	protected void ajaxReturn(HashMap<String, String> hashMap) throws IOException {
		Set<String> set = hashMap.keySet();
		Iterator<String> iterator = set.iterator();
		JSONObject mJSONObject = new JSONObject();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			mJSONObject.put(key,hashMap.get(key));
		}
		this.jsonHeader();
		response.getWriter().write(mJSONObject.toJSONString());
		return;
	}
	
	/**
	 * 发送JSON的头部
	 */
	protected void jsonHeader() {
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-type", "text/json");
	}
	

	/**
	 * Json转换成字符串
	 */
	protected void jsonEncode() {
		JSONObject mJSONObject = new JSONObject();
		JSONArray mJSONArray = new JSONArray();
		mJSONArray.add("1");
		mJSONArray.add(2);
		mJSONArray.add("3");
		mJSONObject.put("status", 1);
		mJSONObject.put("info", "成功！");
		mJSONObject.put("data", mJSONArray);
		System.out.println(mJSONObject.toJSONString());
	}

	/**
	 * 字符串转成为Json
	 */
	protected void jsonDecode() {
		String jsonStr = "{\"status\":1,\"data\":[\"1\",\"2\",\"3\"],\"info\":\"成功！\"}";
		JSONObject mJSONObject = (JSONObject) JSON.parse(jsonStr);
		System.out.println(mJSONObject.get("status"));
		System.out.println(mJSONObject.get("info"));
		JSONArray mJSONArray = (JSONArray) mJSONObject.get("data");
		for (Object str : mJSONArray) {
			System.out.println(str);
		}
	}

	/**
	 * 编码码JavaScript的标志位
	 * 
	 * @param str
	 * @return
	 */
	protected String escapeJavaScript(String str) {
		return StringEscapeUtils.escapeJavaScript(str);
	}

	/**
	 * 解码JavaScript的标志位
	 * 
	 * @param str
	 * @return
	 */
	protected String unescapeJavaScript(String str) {
		return StringEscapeUtils.unescapeJavaScript(str);
	}

}
