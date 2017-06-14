<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is "theme-handler/method0.jsp" file.<br />

<!-- *******************************jstl********************************** -->
<!-- JSTL标准库，放到类路径，会被自动扫描到 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:out value="Hello JSTL." /><br />
${fn:toUpperCase('abc')}<br />


<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>