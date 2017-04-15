package cn.java.admin.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.jasper.runtime.ServletResponseWrapperInclude;

import com.alibaba.fastjson.JSONObject;

/**
 * com.jfinal.core.JFinal
 */
public class TestSerlvet extends AdminCommonSerlvet {

	public void testStartServerAction(){
//		com.jfinal.core.JFinal 
	}
	
	public void testHashMapAction(){
		HashMap mHashMap = new HashMap();
		mHashMap.containsKey("111");
	}
	/**
	 * 跳转
	 * @throws ServletException
	 * @throws IOException
	 */
	public void testDispatcherAction() throws ServletException, IOException{
		String fileFullPath = "/WEB-INF/servletView/admin/Common-footer.jsp";
		RequestDispatcher mRequestDispatcher = request.getRequestDispatcher(fileFullPath);
		mRequestDispatcher.forward(request, response);
	}

	public void testFetchAction() throws ServletException, IOException {
		//response.addHeader("Content-type", "text/json");
		String htmlStr = this.fetch("/WEB-INF/servletView/common/testFetch.jsp");
		JSONObject mJSONObject = new JSONObject();
		mJSONObject.put("status", 1);
		mJSONObject.put("info", "成功！");
		mJSONObject.put("data", this.escapeJavaScript(htmlStr));
		System.out.println(mJSONObject.toJSONString());
		
	}

	public void testHtmlAddsladesAction() {
		String str = this.escapeJavaScript("<html></html>");
		System.out.println(str);
		this.unescapeJavaScript(str);
	}

	public void testJsonAction() {
		this.jsonEncode();
		this.jsonDecode();
	}

	/**
	 * /admin/test/?act=test
	 */
	public void testAction() throws ServletException, IOException {

		String page = request.getParameter("page");
		if (page == null || page.equals("")) {
			page = "1";
		}
		int currentPage = Integer.valueOf(page);
		if (request.getMethod().equals("GET")) {
			this.assign("pagination", this.pagination("/admin/test/?act=test", currentPage, 20));
			this.display("Test-test.jsp");
		} else {
			// -------------text---------
			System.out.println("username:" + request.getParameter("username"));
			System.out.println("sex:" + request.getParameter("sex"));
			System.out.println("age:" + request.getParameter("age"));
			// -------------radio---------
			String[] values = request.getParameterValues("sex");
			for (String string : values) {
				System.out.println("sex:" + string);
			}
			// -------------select---------
			values = request.getParameterValues("age");
			for (String string : values) {
				System.out.println("age:" + string);
			}
			// -------------text---------
			values = request.getParameterValues("username");
			for (String string : values) {
				System.out.println("username:" + string);
			}
			// -------------text list---------
			values = request.getParameterValues("list");
			for (String string : values) {
				System.out.println("text list:" + string);
			}
			// -------------checkbox---------
			values = request.getParameterValues("like1");
			for (String string : values) {
				System.out.println("checkbox:" + string);
			}
			Enumeration  mEnumeration= request.getParameterNames();
			HashMap configHashMap = new HashMap(); 
			while(mEnumeration.hasMoreElements()){
				String filedName = (String) mEnumeration.nextElement();
				if(filedName.indexOf("config.")!=-1){
					String[] filedNameSplit = filedName.split("\\.");
					String valueTemp = request.getParameter(filedName);
					if(filedNameSplit.length==2){
						configHashMap.put(filedNameSplit[1], valueTemp);
					}
					else if(filedNameSplit.length==3){
						HashMap configHashMapSub = null;
						if(!configHashMap.containsKey(filedNameSplit[1])){
							configHashMapSub = new HashMap(); 
						}
						else{
							configHashMapSub = (HashMap) configHashMap.get(filedNameSplit[1]);
						}
						configHashMapSub.put(filedNameSplit[2],valueTemp);
						configHashMap.put(filedNameSplit[1], configHashMapSub);
					}
				}
			}
			System.out.println(configHashMap);
		}
	}
}