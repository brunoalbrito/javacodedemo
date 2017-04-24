<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="java.io.*,java.util.*,java.net.*"%>
<%!String tblWidth = "700";
	String tblAlign = "center";
	String strOS = System.getProperty("os.name");

	private String getIp() {
		String strTmp = "";
		try {
			strTmp = InetAddress.getLocalHost().getHostAddress();
			return strTmp;
		} catch (Exception e) {
			return strTmp;
		}
	}

	private String getSystemEnv() throws Exception {
		String OS = System.getProperty("os.name").toLowerCase();
		StringBuffer sb = new StringBuffer("");
		Process p = null;
		if (OS.indexOf("windows") > -1) {
			p = Runtime.getRuntime().exec("cmd /c set");
		} else {
			p = Runtime.getRuntime().exec("/bin/sh -c set");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line + "<br>");
		}
		return sb.toString();
	}

	private String getDrivers() {
		StringBuffer sb = new StringBuffer("");
		File roots[] = File.listRoots();
		for (int i = 0; i < roots.length; i++) {
			sb.append(roots[i] + " ");
		}
		return sb.toString();
	}%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jsp探针</title>
<style type="text/css">
<!--
body, td, th {
	font-size: 9pt;
}

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

a:link {
	text-decoration: none;
}

a:visited {
	text-decoration: none;
}

a:hover {
	text-decoration: none;
}

a:active {
	text-decoration: none;
}

.STYLE4 {
	color: #000000
}

.STYLE5 {
	font-weight: bold;
	font-size: 10pt;
}

.STYLE7 {
	font-size: 10
}
-->
</style>
</head>

<body>
	<table align="center" width="277" border="0">
		<tr>
			<td width="90" align="center">JSP探针 v1.1</td>
			<td width="85" align="center"><a href="http://www.ha97.com">服务器运维与架构</a></td>
		</tr>
	</table>
	<div align="center">
		<br>
		<table width="<%=tblWidth%>" align="<%=tblAlign%>" border="0"
			cellspacing="0" cellpadding="1">
			<tr>
				<td colspan="2" height="22" bgcolor="#E0E0E0"><span
					class="STYLE5">服务器基本信息 </span></td>
			</tr>
			<tr>
				<td height="23" width="20%">服务器名称</td>
				<td><%=request.getServerName()%></td>
			</tr>
			<tr>
				<td height="23" width="20%">域名/IP</td>
				<td><%=request.getServerName()%> <%=getIp()%></td>
			</tr>
			<tr>
				<td height="23">服务器端口</td>
				<td><%=request.getServerPort()%></td>
			</tr>
			<tr>
				<td height="23">客户端端口</td>
				<td><%=request.getRemotePort()%></td>
			</tr>
			<tr>
				<td height="23">客户端IP</td>
				<td><%=request.getRemoteAddr()%></td>
			</tr>
			<tr>
				<td height="23">Web 服务器</td>
				<td><%=application.getServerInfo()%></td>
			</tr>

			<tr>
				<td height="23">操作系统</td>
				<td><%=strOS + " " + System.getProperty("sun.os.patch.level")
					+ " Ver:" + System.getProperty("os.version")%>
				</td>
			</tr>
			<tr>
				<td height="23">服务器时间</td>
				<td><%=new Date().toLocaleString()%></td>
			</tr>
			<tr>
				<td height="23" width="20%">CPU 信息</td>
				<td><%=System.getProperty("os.arch")%></td>
			</tr>
			<tr>
				<td height="23" width="20%">磁盘分区</td>
				<td><%=getDrivers()%></td>
			</tr>
			<tr>
				<td height="23" width="20%">用户当前工作目录</td>
				<td><%=System.getProperty("user.dir")%></td>
			</tr>
			<tr>
				<td height="23">本文件路径</td>
				<td><%=application.getRealPath(request.getRequestURI())%></td>
			</tr>
		</table>
		<br>
		<table width="<%=tblWidth%>" align="<%=tblAlign%>" border="0"
			cellspacing="0" cellpadding="1">
			<tr>
				<td colspan="2" height="22" bgcolor="#E0E0E0"><span
					class="STYLE4"><span class="STYLE5">Java 相关信息</span></span></td>
			</tr>
			<tr>
				<td height="23" width="20%"><span class="STYLE7">JDK 版本</span>
				</td>
				<td><%=System.getProperty("java.version")%></td>
			</tr>
			<tr>
				<td height="23" width="20%">Servlet 版本</td>
				<td><%=application.getMajorVersion() + "."
					+ application.getMinorVersion()%>
				</td>
			</tr>
			<tr>
				<td height="23" width="20%">JDK 安装路径</td>
				<td><%=System.getProperty("java.home")%></td>
			</tr>
			<tr>
				<td height="23" width="20%">编码</td>
				<td><%=System.getProperty("file.encoding")%></td>
			</tr>
			<tr>
				<td height="23" width="20%">JAVA类路径</td>
				<td><%=System.getProperty("java.class.path")%></td>
			</tr>
		</table>
		<br>
		<table width="<%=tblWidth%>" align="<%=tblAlign%>" border="0"
			cellspacing="0" cellpadding="1">
			<tr>
				<td colspan="2" height="22" bgcolor="#E0E0E0">服务器环境变量</td>
			</tr>
			<tr>
				<td colspan="2"><%= getSystemEnv() %></td>
			</tr>
		</table>
		<br>

	</div>
</body>
</html>