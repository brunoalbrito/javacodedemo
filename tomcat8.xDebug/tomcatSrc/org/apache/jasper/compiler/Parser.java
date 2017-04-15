/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jasper.compiler;

import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagFileInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.util.UniqueAttributesImpl;
import org.apache.tomcat.Jar;
import org.apache.tomcat.util.descriptor.tld.TldResourcePath;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * This class implements a parser for a JSP page (non-xml view). JSP page
 * grammar is included here for reference. The token '#' that appears in the
 * production indicates the current input token location in the production.
 *
 * @author Kin-man Chung
 * @author Shawn Bayern
 * @author Mark Roth
 */

class Parser implements TagConstants {

    private final ParserController parserController;

    private final JspCompilationContext ctxt;

    private final JspReader reader;

    private Mark start;

    private final ErrorDispatcher err;

    private int scriptlessCount;

    private final boolean isTagFile;

    private final boolean directivesOnly;

    private final Jar jar;

    private final PageInfo pageInfo;

    // Virtual body content types, to make parsing a little easier.
    // These are not accessible from outside the parser.
    private static final String JAVAX_BODY_CONTENT_PARAM =
        "JAVAX_BODY_CONTENT_PARAM";

    private static final String JAVAX_BODY_CONTENT_PLUGIN =
        "JAVAX_BODY_CONTENT_PLUGIN";

    private static final String JAVAX_BODY_CONTENT_TEMPLATE_TEXT =
        "JAVAX_BODY_CONTENT_TEMPLATE_TEXT";

    /* System property that controls if the strict white space rules are
     * applied.
     */
    private static final boolean STRICT_WHITESPACE = Boolean.parseBoolean(
            System.getProperty(
                    "org.apache.jasper.compiler.Parser.STRICT_WHITESPACE",
                    "true"));
    /**
     * The constructor
     */
    private Parser(ParserController pc, JspReader reader, boolean isTagFile,
            boolean directivesOnly, Jar jar) {
        this.parserController = pc; // org.apache.jasper.compiler.ParserController
        this.ctxt = pc.getJspCompilationContext();
        this.pageInfo = pc.getCompiler().getPageInfo(); // org.apache.jasper.compiler.PageInfo
        this.err = pc.getCompiler().getErrorDispatcher();
        this.reader = reader;
        this.scriptlessCount = 0;
        this.isTagFile = isTagFile;
        this.directivesOnly = directivesOnly;
        this.jar = jar;
        start = reader.mark();
    }

    /**
     * The main entry for Parser
     *
     * @param pc  The ParseController, use for getting other objects in compiler
     *            and for parsing included pages
     * @param reader To read the page
     * @param parent The parent node to this page, null for top level page
     * @param isTagFile Is the page being parsed a tag file?
     * @param directivesOnly Should only directives be parsed?
     * @param jar JAR, if any, that this page was loaded from
     * @param pageEnc The encoding of the source
     * @param jspConfigPageEnc The encoding for the page
     * @param isDefaultPageEncoding Is the page encoding the default?
     * @param isBomPresent Is a BOM present in the source
     * @return list of nodes representing the parsed page
     *
     * @throws JasperException If an error occurs during parsing
     */
    public static Node.Nodes parse(ParserController pc, JspReader reader,
            Node parent, boolean isTagFile, boolean directivesOnly,
            Jar jar, String pageEnc, String jspConfigPageEnc,
            boolean isDefaultPageEncoding, boolean isBomPresent)
            throws JasperException {

    	// pc == org.apache.jasper.compiler.ParserController
    	// reader == org.apache.jasper.compiler.JspReader
    	// parent == null

        Parser parser = new Parser(pc, reader, isTagFile, directivesOnly, jar); // 创建解析对象

        Node.Root root = new Node.Root(reader.mark(), parent, false);
        root.setPageEncoding(pageEnc);
        root.setJspConfigPageEncoding(jspConfigPageEnc);
        root.setIsDefaultPageEncoding(isDefaultPageEncoding);
        root.setIsBomPresent(isBomPresent);

        // For the Top level page, add include-prelude and include-coda
        // pc.getCompiler() === org.apache.jasper.compiler.JDTCompiler
        PageInfo pageInfo = pc.getCompiler().getPageInfo(); // JSP解析规则的配置信息
        if (parent == null && !isTagFile) {
        	// 取得web.xml中<include-coda></include-coda>的配置
            parser.addInclude(root, pageInfo.getIncludePrelude()); // 
        }
        if (directivesOnly) { // 只解析指令
//      	<%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//          <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>  // 这个标签会被跳过
//          <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ attribute attr1="value1" attr2="value2" %>
//          <%@ variable attr1="value1" attr2="value2" %>
//      	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//          <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.attribute attr1="value1" attr2="value2" />
//          <jsp:directive.variable attr1="value1" attr2="value2" />
            parser.parseFileDirectives(root); //!!!! 解析页面指令
        } else { // 解析页面的其他元素
//        	***注释***
//        	<%-- 注释  --%>
//        	***页面指令***
//      	<%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//          <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
//          <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ attribute attr1="value1" attr2="value2" %>
//          <%@ variable attr1="value1" attr2="value2" %>
//      	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//          <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.attribute attr1="value1" attr2="value2" />
//          <jsp:directive.variable attr1="value1" attr2="value2" />
//        	***声明***
//        	<%!  声明    %>
//        	<jsp:declaration></jsp:declaration>
//        	<jsp:declaration><![CDATA[  ...  ]]></jsp:declaration>
//        	<jsp:declaration> ...  <![CDATA[  ...  ]]> ...  </jsp:declaration>
//        	***表达式***
//        	<%= 表达式  %>
//        	<jsp:expression></jsp:expression>
//        	<jsp:expression><![CDATA[  ...  ]]></jsp:expression>
//        	<jsp:expression> ...  <![CDATA[  ...  ]]> ...  </jsp:expression>
//        	***脚本***
//        	<% 脚本  %>
//        	<jsp:scriptlet></jsp:scriptlet>
//        	<jsp:scriptlet><![CDATA[  ...  ]]></jsp:scriptlet>
//        	<jsp:scriptlet> ...  <![CDATA[  ...  ]]> ...  </jsp:scriptlet>
//        	***EL表达式***
//        	${param.username}   --->  <%=request.getParameter("username") %>
//        	${param['username']}
//        	${param["username"]}
//        	${param{param["username"]}}  --?-> 嵌套   <%=request.getParameter(request.getParameter("username")) %>
//        	***EL迭代表达式***
//        	#{param['username']}
//        	***行为标签***
//          <jsp:include page="/test/jsp/header.jsp">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:include>
//          <jsp:forward page="/test/jsp/header.jsp">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:forward>       
//          <jsp:invoke attr1="value1">
//        		<jsp:attribute name="param1" value="value1" />
//          </jsp:invoke> 
//          <jsp:doBody attr1="value1">
//        		<jsp:attribute name="param1" value="value1" />
//          </jsp:doBody>
//          <jsp:getProperty attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:getProperty>
//          <jsp:setProperty attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:setProperty>
//          <jsp:useBean attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:useBean>
//          <jsp:plugin attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:plugin>
//          <jsp:element attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:element>
//        	***自定义标签***
//          <myprefix:tagname  attr1="value1" attr2="value2" />
//          或者
//  	    <myprefix:tagname>
//  	    	<jsp:attribute name="" value="" />
//  			<jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
//  	        <jsp:body>body content...</jsp:body>
//  	      </myprefix:tagname>
//          或者
//        <myprefix:tagname>
//  			body content...
//        </myprefix:tagname>
//        	***解析普通文本***
//        	...
            while (reader.hasMoreInput()) {
                parser.parseElements(root);
            }
        }
        if (parent == null && !isTagFile) {
            parser.addInclude(root, pageInfo.getIncludeCoda()); // 添加include节点
        }

        Node.Nodes page = new Node.Nodes(root);
        return page;
    }

    /**
     * Attributes ::= (S Attribute)* S?
     */
    Attributes parseAttributes() throws JasperException {
        return parseAttributes(false);
    }
    Attributes parseAttributes(boolean pageDirective) throws JasperException {
        UniqueAttributesImpl attrs = new UniqueAttributesImpl(pageDirective);

        reader.skipSpaces();
        int ws = 1;

        try {
            while (parseAttribute(attrs)) { // 解析标签属性
                if (ws == 0 && STRICT_WHITESPACE) {
                    err.jspError(reader.mark(),
                            "jsp.error.attribute.nowhitespace");
                }
                ws = reader.skipSpaces();
            }
        } catch (IllegalArgumentException iae) {
            // Duplicate attribute
            err.jspError(reader.mark(), "jsp.error.attribute.duplicate");
        }

        return attrs;
    }

    /**
     * Parse Attributes for a reader, provided for external use
     *
     * @param pc The parser
     * @param reader The source
     *
     * @return The parsed attributes
     *
     * @throws JasperException If an error occurs during parsing
     */
    public static Attributes parseAttributes(ParserController pc,
            JspReader reader) throws JasperException {
        Parser tmpParser = new Parser(pc, reader, false, false, null);
        return tmpParser.parseAttributes(true);
    }

    /**
     * Attribute ::= Name S? Eq S? ( '"<%=' RTAttributeValueDouble | '"'
     * AttributeValueDouble | "'<%=" RTAttributeValueSingle | "'"
     * AttributeValueSingle } Note: JSP and XML spec does not allow while spaces
     * around Eq. It is added to be backward compatible with Tomcat, and with
     * other xml parsers.
     */
    private boolean parseAttribute(AttributesImpl attrs)
            throws JasperException {

    	// 如：<%@ page pageEncoding="UTF-8" %>
    	// 如：<%@ page import="java.uti1.*, cn.java.*" %>
        // Get the qualified name
        String qName = parseName(); // 如： "attr1"
        if (qName == null)
            return false;

        boolean ignoreEL = pageInfo.isELIgnored();

        // Determine prefix and local name components
        String localName = qName;
        String uri = "";
        int index = qName.indexOf(':');
        if (index != -1) {
            String prefix = qName.substring(0, index); // 前缀 jsp
            uri = pageInfo.getURI(prefix); // 地址
            if (uri == null) {
                err.jspError(reader.mark(),
                        "jsp.error.attribute.invalidPrefix", prefix);
            }
            localName = qName.substring(index + 1); // directive.include
        }

        reader.skipSpaces();
        if (!reader.matches("="))
            err.jspError(reader.mark(), "jsp.error.attribute.noequal");

        reader.skipSpaces();
        char quote = (char) reader.nextChar();
        if (quote != '\'' && quote != '"') // 不是以引号开头，就出错
            err.jspError(reader.mark(), "jsp.error.attribute.noquote");

        String watchString = "";
        if (reader.matches("<%=")) {
            watchString = "%>";
            // Can't embed EL in a script expression
            ignoreEL = true;
        }
        watchString = watchString + quote;   //  %>" 或者 "

        //  <%@ page attr1="<%=xxx%>" %>
        
        //  <%@ page attr1="value" %>
        // qName == 'attr1'
        // parseAttributeValue("attr1", "\"", true)
        String attrValue = parseAttributeValue(qName, watchString, ignoreEL); // 解析出属性值
        attrs.addAttribute(uri, localName, qName, "CDATA", attrValue);
        return true;
    }

    /**
     * Name ::= (Letter | '_' | ':') (Letter | Digit | '.' | '_' | '-' | ':')*
     */
    private String parseName() {
        char ch = (char) reader.peekChar();
        if (Character.isLetter(ch) || ch == '_' || ch == ':') {
            StringBuilder buf = new StringBuilder();
            buf.append(ch);
            reader.nextChar();
            ch = (char) reader.peekChar();
            while (Character.isLetter(ch) || Character.isDigit(ch) || ch == '.'
                    || ch == '_' || ch == '-' || ch == ':') {
                buf.append(ch);
                reader.nextChar();
                ch = (char) reader.peekChar();
            }
            return buf.toString();
        }
        return null;
    }

    /**
     * AttributeValueDouble ::= (QuotedChar - '"')* ('"' | <TRANSLATION_ERROR>)
     * RTAttributeValueDouble ::= ((QuotedChar - '"')* - ((QuotedChar-'"')'%>"')
     * ('%>"' | TRANSLATION_ERROR)
     */
    private String parseAttributeValue(String qName, String watch, boolean ignoreEL) throws JasperException {
        boolean quoteAttributeEL = ctxt.getOptions().getQuoteAttributeEL();
        // 如:
        //  <%@ page attr1="value" %>
        // parseAttributeValue("attr1", "\"", true)
        // qName == 'attr1'
        // watch == '"'
        // ignoreEL == true
        
        Mark start = reader.mark();
        // In terms of finding the end of the value, quoting EL is equivalent to
        // ignoring it.
        Mark stop = reader.skipUntilIgnoreEsc(watch, ignoreEL || quoteAttributeEL);
        if (stop == null) {
            err.jspError(start, "jsp.error.attribute.unterminated", qName);
        }

        String ret = null;
        try {
            char quote = watch.charAt(watch.length() - 1);

            // If watch is longer than 1 character this is a scripting
            // expression and EL is always ignored
            boolean isElIgnored =
                pageInfo.isELIgnored() || watch.length() > 1;

            ret = AttributeParser.getUnquoted(reader.getText(start, stop),
                    quote, isElIgnored,
                    pageInfo.isDeferredSyntaxAllowedAsLiteral(),
                    ctxt.getOptions().getStrictQuoteEscaping(),
                    quoteAttributeEL);
        } catch (IllegalArgumentException iae) {
            err.jspError(start, iae.getMessage());
        }
        if (watch.length() == 1) // quote
            return ret;

        // Put back delimiter '<%=' and '%>', since they are needed if the
        // attribute does not allow RTexpression.
        return "<%=" + ret + "%>";
    }

    private String parseScriptText(String tx) {
        CharArrayWriter cw = new CharArrayWriter();
        int size = tx.length();
        int i = 0;
        while (i < size) {
            char ch = tx.charAt(i);
            if (i + 2 < size && ch == '%' && tx.charAt(i + 1) == '\\'
                    && tx.charAt(i + 2) == '>') { // 如果遇到%\>,就转成  %>
                cw.write('%');
                cw.write('>');
                i += 3;
            } else {
                cw.write(ch);
                ++i;
            }
        }
        cw.close();
        return cw.toString();
    }

    /*
     * Invokes parserController to parse the included page
     */
    private void processIncludeDirective(String file, Node parent)
            throws JasperException {
        if (file == null) {
            return;
        }

        try {
            parserController.parse(file, parent, jar);
        } catch (FileNotFoundException ex) {
            err.jspError(start, "jsp.error.file.not.found", file);
        } catch (Exception ex) {
            err.jspError(start, ex.getMessage());
        }
    }

    /*
     * Parses a page directive with the following syntax: PageDirective ::= ( S
     * Attribute)*
     */
    private void parsePageDirective(Node parent) throws JasperException {
        Attributes attrs = parseAttributes(true); // 解析标签属性
        Node.PageDirective n = new Node.PageDirective(attrs, start, parent);

        /*
         * A page directive may contain multiple 'import' attributes, each of
         * which consists of a comma-separated list of package names. Store each
         * list with the node, where it is parsed.
         */
        //　<%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
        for (int i = 0; i < attrs.getLength(); i++) {
        	// attrs == org.apache.jasper.util.UniqueAttributesImpl
            if ("import".equals(attrs.getQName(i))) {
                n.addImport(attrs.getValue(i));
            }
        }
    }

    /*
     * Parses an include directive with the following syntax: IncludeDirective
     * ::= ( S Attribute)*
     */
    private void parseIncludeDirective(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();

        // Included file expanded here
        Node includeNode = new Node.IncludeDirective(attrs, start, parent);
        processIncludeDirective(attrs.getValue("file"), includeNode); // 继续解析要引入的文件
    }

    /**
     * Add a list of files. This is used for implementing include-prelude and
     * include-coda of jsp-config element in web.xml
     */
    private void addInclude(Node parent, Collection<String> files) throws JasperException {
        if (files != null) {
            Iterator<String> iter = files.iterator();
            while (iter.hasNext()) {
                String file = iter.next();
                AttributesImpl attrs = new AttributesImpl();
                attrs.addAttribute("", "file", "file", "CDATA", file);

                // Create a dummy Include directive node
                Node includeNode = new Node.IncludeDirective(attrs, reader
                        .mark(), parent);
                processIncludeDirective(file, includeNode); // 解析include的文件内容
            }
        }
    }

    /*
     * Parses a taglib directive with the following syntax: Directive ::= ( S
     * Attribute)*
     */
    private void parseTaglibDirective(Node parent) throws JasperException {
    	//  <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
        Attributes attrs = parseAttributes();
        String uri = attrs.getValue("uri");
        String prefix = attrs.getValue("prefix");
        if (prefix != null) {
        	// org.apache.jasper.compiler.PageInfo
            Mark prevMark = pageInfo.getNonCustomTagPrefix(prefix); // 取得不能自定义的前缀
            if (prevMark != null) { // 这个是不能自定义的前缀，就报错
                err.jspError(reader.mark(), "jsp.error.prefix.use_before_dcl",
                        prefix, prevMark.getFile(), ""
                                + prevMark.getLineNumber());
            }
            if (uri != null) {
            	// org.apache.jasper.compiler.PageInfo
            	// uriPrev = xmlPrefixMapper.get(prefix) || jspPrefixMapper.get(prefix);
                String uriPrev = pageInfo.getURI(prefix);  // 这个前缀是否已经定义过 pageInfo.getURI("myprefix");
                if (uriPrev != null && !uriPrev.equals(uri)) {
                    err.jspError(reader.mark(), "jsp.error.prefix.refined",
                            prefix, uri, uriPrev);
                }
                // pageInfo.taglibsMap.get("http://cn.java/jsp/core");
                if (pageInfo.getTaglib(uri) == null) { // .... pageInfo.getURI("http://cn.java/jsp/core");
                    TagLibraryInfoImpl impl = null;
                    // org.apache.jasper.JspCompilationContext.getOptions().isCaching()
                    // org.apache.jasper.EmbeddedServletOptions.isCaching()
                    if (ctxt.getOptions().isCaching()) {
                        impl = (TagLibraryInfoImpl) ctxt.getOptions()
                                .getCache().get(uri);
                    }
                    if (impl == null) {
                    	// !!! 《解析 .tld文件，解析成对象树》
                    	// ctxt === org.apache.jasper.JspCompilationContext
                    	// tldResourcePath === org.apache.tomcat.util.descriptor.tld.TldResourcePath  标签的.tld文件的路径信息
                    	// tldResourcePath == org.apache.jasper.compiler.TldCache.getInstance(ApplicationContextFacade).getTldResourcePath(uri);
                    	// tldResourcePath == org.apache.jasper.compiler.TldCache().getTldResourcePath(uri);
                    	// tldResourcePath == org.apache.jasper.compiler.TldCache().uriTldResourcePathMap.get(uri);
                    	// 上下文中web.xml中的<jsp-config>配置，或上下文中扫描.tld文件提取的信息列表
	                    // <taglib>
		                //      <taglib-location>/WEB-INF/taglib/tag2.tld</taglib-location>
		                //      <taglib-uri>http://cn.java/jsp/core2</taglib-uri>
		                // </taglib>
                    	TldResourcePath tldResourcePath = ctxt.getTldResourcePath(uri); // 取得.tld文件的信息
                        // 根据URI取得解析.tld文件形成的对象树TaglibXml，重新提取TaglibXml的信息放入TagLibraryInfoImpl
                    	impl = new TagLibraryInfoImpl(ctxt, parserController,
                                pageInfo, prefix, uri, tldResourcePath, err); // 创建TagLibraryInfoImpl对象
                        if (ctxt.getOptions().isCaching()) {
                            ctxt.getOptions().getCache().put(uri, impl);
                        }
                    }
                    // pageInfo.addTaglib("http://cn.java/jsp/core", TagLibraryInfoImpl); // 添加到pageInfo对象中
                    // pageInfo.taglibsMap.put("http://cn.java/jsp/core", TagLibraryInfoImpl);
                    pageInfo.addTaglib(uri, impl); // 添加到pageInfo对象中
                }
                // pageInfo.addPrefixMapping("myprefix", "http://cn.java/jsp/core"); // 添加到pageInfo对象中
                // pageInfo.jspPrefixMapper.put("myprefix", "http://cn.java/jsp/core");
                pageInfo.addPrefixMapping(prefix, uri); // 添加到pageInfo对象中
            } else {
                String tagdir = attrs.getValue("tagdir");
                if (tagdir != null) {
                    String urnTagdir = URN_JSPTAGDIR + tagdir;
                    if (pageInfo.getTaglib(urnTagdir) == null) {
                        pageInfo.addTaglib(urnTagdir,
                                new ImplicitTagLibraryInfo(ctxt,
                                        parserController, pageInfo, prefix,
                                        tagdir, err));
                    }
                    pageInfo.addPrefixMapping(prefix, urnTagdir);
                }
            }
        }

        @SuppressWarnings("unused")
        Node unused = new Node.TaglibDirective(attrs, start, parent);
    }

    /*
     * Parses a directive with the following syntax: Directive ::= S? ( 'page'
     * PageDirective | 'include' IncludeDirective | 'taglib' TagLibDirective) S?
     * '%>'
     *
     * TagDirective ::= S? ('tag' PageDirective | 'include' IncludeDirective |
     * 'taglib' TagLibDirective) | 'attribute AttributeDirective | 'variable
     * VariableDirective S? '%>'
     */
    private void parseDirective(Node parent) throws JasperException {
        reader.skipSpaces(); // 跳过空格
//      以  <%@ 开头
//        <%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//        <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//        <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
//        <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//        <%@ attribute attr1="value1" attr2="value2" %>
//        <%@ variable attr1="value1" attr2="value2" %>
        
        // 可在JSP页面使用的指令
//      <%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//      <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//      <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
        
        // 可在标签页面(*.tag)页面使用的指令
//      <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//      <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>  
//      <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//      <%@ attribute attr1="value1" attr2="value2" %>
//      <%@ variable attr1="value1" attr2="value2" %>
        
        String directive = null;
        if (reader.matches("page")) { // 如： <%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
            directive = "&lt;%@ page";
            if (isTagFile) { // 只支持jsp文件使用本指令
                err.jspError(reader.mark(), "jsp.error.directive.istagfile",
                        directive);
            }
            parsePageDirective(parent);
        } else if (reader.matches("include")) { // 如： <%@ include file="/a/b/c/file.jsp" %>
            directive = "&lt;%@ include";
            parseIncludeDirective(parent);
        } else if (reader.matches("taglib")) { // 如： <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
            if (directivesOnly) { // 如果是 只解析指令的情况，不需要解析这个标签
                // No need to get the tagLibInfo objects. This alos suppresses
                // parsing of any tag files used in this tag file.
                return;
            }
            directive = "&lt;%@ taglib";
            // 提取出的tld文件的对象TaglibXml,放入TagLibraryInfoImpl，并把对象TagLibraryInfoImpl放入pageInfo
//            pageInfo.taglibsMap.put("http://cn.java/jsp/core", TagLibraryInfoImpl);
//            pageInfo.jspPrefixMapper.put("myprefix", "http://cn.java/jsp/core");
            parseTaglibDirective(parent); 
        } else if (reader.matches("tag")) {  // <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
            directive = "&lt;%@ tag";
            if (!isTagFile) { // 只支持tag文件使用本指令
                err.jspError(reader.mark(), "jsp.error.directive.isnottagfile",
                        directive);
            }
            parseTagDirective(parent);
        } else if (reader.matches("attribute")) { // <%@ attribute attr1="value1" attr2="value2" %>
            directive = "&lt;%@ attribute";
            if (!isTagFile) { // 只支持tag文件使用本指令
                err.jspError(reader.mark(), "jsp.error.directive.isnottagfile",
                        directive);
            }
            parseAttributeDirective(parent);
        } else if (reader.matches("variable")) { // <%@ variable attr1="value1" attr2="value2" %>
            directive = "&lt;%@ variable";
            if (!isTagFile) { // 只支持tag文件使用本指令
                err.jspError(reader.mark(), "jsp.error.directive.isnottagfile",
                        directive);
            }
            parseVariableDirective(parent);
        } else {
            err.jspError(reader.mark(), "jsp.error.invalid.directive");
        }

        reader.skipSpaces();
        if (!reader.matches("%>")) {
            err.jspError(start, "jsp.error.unterminated", directive);
        }
    }

    /*
     * Parses a directive with the following syntax:
     *
     * XMLJSPDirectiveBody ::= S? ( ( 'page' PageDirectiveAttrList S? ( '/>' | (
     * '>' S? ETag ) ) | ( 'include' IncludeDirectiveAttrList S? ( '/>' | ( '>'
     * S? ETag ) ) | <TRANSLATION_ERROR>
     *
     * XMLTagDefDirectiveBody ::= ( ( 'tag' TagDirectiveAttrList S? ( '/>' | (
     * '>' S? ETag ) ) | ( 'include' IncludeDirectiveAttrList S? ( '/>' | ( '>'
     * S? ETag ) ) | ( 'attribute' AttributeDirectiveAttrList S? ( '/>' | ( '>'
     * S? ETag ) ) | ( 'variable' VariableDirectiveAttrList S? ( '/>' | ( '>' S?
     * ETag ) ) ) | <TRANSLATION_ERROR>
     */
    private void parseXMLDirective(Node parent) throws JasperException {
        reader.skipSpaces();
        
        // 可在JSP页面使用的指令
//  	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//      <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
        
        // 可在标签页面(*.tag)页面使用的指令
//      <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//      <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//      <jsp:directive.attribute attr1="value1" attr2="value2" />
//      <jsp:directive.variable attr1="value1" attr2="value2" />
        
        String eTag = null;
        if (reader.matches("page")) {
            eTag = "jsp:directive.page";
            if (isTagFile) {
                err.jspError(reader.mark(), "jsp.error.directive.istagfile",
                        "&lt;" + eTag);
            }
            parsePageDirective(parent);
        } else if (reader.matches("include")) {
            eTag = "jsp:directive.include";
            parseIncludeDirective(parent);
        } else if (reader.matches("tag")) {
            eTag = "jsp:directive.tag";
            if (!isTagFile) {
                err.jspError(reader.mark(), "jsp.error.directive.isnottagfile",
                        "&lt;" + eTag);
            }
            parseTagDirective(parent);
        } else if (reader.matches("attribute")) {
            eTag = "jsp:directive.attribute";
            if (!isTagFile) {
                err.jspError(reader.mark(), "jsp.error.directive.isnottagfile",
                        "&lt;" + eTag);
            }
            parseAttributeDirective(parent);
        } else if (reader.matches("variable")) {
            eTag = "jsp:directive.variable";
            if (!isTagFile) {
                err.jspError(reader.mark(), "jsp.error.directive.isnottagfile",
                        "&lt;" + eTag);
            }
            parseVariableDirective(parent);
        } else {
            err.jspError(reader.mark(), "jsp.error.invalid.directive");
        }

        reader.skipSpaces();
        if (reader.matches(">")) {
            reader.skipSpaces();
            if (!reader.matchesETag(eTag)) {
                err.jspError(start, "jsp.error.unterminated", "&lt;" + eTag);
            }
        } else if (!reader.matches("/>")) {
            err.jspError(start, "jsp.error.unterminated", "&lt;" + eTag);
        }
    }

    /*
     * Parses a tag directive with the following syntax: PageDirective ::= ( S
     * Attribute)*
     */
    private void parseTagDirective(Node parent) throws JasperException {
        Attributes attrs = parseAttributes(true);
        Node.TagDirective n = new Node.TagDirective(attrs, start, parent);

        /*
         * A page directive may contain multiple 'import' attributes, each of
         * which consists of a comma-separated list of package names. Store each
         * list with the node, where it is parsed.
         */
        for (int i = 0; i < attrs.getLength(); i++) {
            if ("import".equals(attrs.getQName(i))) {
                n.addImport(attrs.getValue(i));
            }
        }
    }

    /*
     * Parses a attribute directive with the following syntax:
     * AttributeDirective ::= ( S Attribute)*
     */
    private void parseAttributeDirective(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        @SuppressWarnings("unused")
        Node unused = new Node.AttributeDirective(attrs, start, parent);
    }

    /*
     * Parses a variable directive with the following syntax:
     * PageDirective ::= ( S Attribute)*
     */
    private void parseVariableDirective(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        @SuppressWarnings("unused")
        Node unused = new Node.VariableDirective(attrs, start, parent);
    }

    /*
     * JSPCommentBody ::= (Char* - (Char* '--%>')) '--%>'
     */
    private void parseComment(Node parent) throws JasperException {
        start = reader.mark();
        Mark stop = reader.skipUntil("--%>");
        if (stop == null) {
            err.jspError(start, "jsp.error.unterminated", "&lt;%--");
        }

        @SuppressWarnings("unused")
        Node unused =
                new Node.Comment(reader.getText(start, stop), start, parent);
    }

    /*
     * DeclarationBody ::= (Char* - (char* '%>')) '%>'
     */
    private void parseDeclaration(Node parent) throws JasperException {
        start = reader.mark();
        Mark stop = reader.skipUntil("%>");
        if (stop == null) {
            err.jspError(start, "jsp.error.unterminated", "&lt;%!");
        }

        @SuppressWarnings("unused")
        Node unused = new Node.Declaration(
                parseScriptText(reader.getText(start, stop)), start, parent);
    }

    /*
     * XMLDeclarationBody ::= ( S? '/>' ) | ( S? '>' (Char* - (char* '<'))
     * CDSect?)* ETag | <TRANSLATION_ERROR> CDSect ::= CDStart CData CDEnd
     * CDStart ::= '<![CDATA[' CData ::= (Char* - (Char* ']]>' Char*)) CDEnd
     * ::= ']]>'
     */
    private void parseXMLDeclaration(Node parent) throws JasperException {
    	
//    	<jsp:declaration></jsp:declaration>
//    	<jsp:declaration><![CDATA[    ]]></jsp:declaration>
//    	<jsp:declaration>   <![CDATA[    ]]>   </jsp:declaration>
        reader.skipSpaces();
        if (!reader.matches("/>")) {
            if (!reader.matches(">")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:declaration&gt;");
            }
            Mark stop;
            String text;
            while (true) {
                start = reader.mark();
                stop = reader.skipUntil("<");
                if (stop == null) {
                    err.jspError(start, "jsp.error.unterminated",
                            "&lt;jsp:declaration&gt;");
                }
                text = parseScriptText(reader.getText(start, stop)); // 文本内容
                @SuppressWarnings("unused")
                Node unused = new Node.Declaration(text, start, parent);
                if (reader.matches("![CDATA[")) {
                    start = reader.mark();
                    stop = reader.skipUntil("]]>");
                    if (stop == null) {
                        err.jspError(start, "jsp.error.unterminated", "CDATA");
                    }
                    text = parseScriptText(reader.getText(start, stop));
                    @SuppressWarnings("unused")
                    Node unused2 = new Node.Declaration(text, start, parent);
                } else {
                    break;
                }
            }

            if (!reader.matchesETagWithoutLessThan("jsp:declaration")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:declaration&gt;");
            }
        }
    }

    /*
     * ExpressionBody ::= (Char* - (char* '%>')) '%>'
     */
    private void parseExpression(Node parent) throws JasperException {
        start = reader.mark();
        Mark stop = reader.skipUntil("%>");
        if (stop == null) {
            err.jspError(start, "jsp.error.unterminated", "&lt;%=");
        }

        @SuppressWarnings("unused")
        Node unused = new Node.Expression(
                parseScriptText(reader.getText(start, stop)), start, parent);
    }

    /*
     * XMLExpressionBody ::= ( S? '/>' ) | ( S? '>' (Char* - (char* '<'))
     * CDSect?)* ETag ) | <TRANSLATION_ERROR>
     */
    private void parseXMLExpression(Node parent) throws JasperException {
        reader.skipSpaces();
        if (!reader.matches("/>")) {
            if (!reader.matches(">")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:expression&gt;");
            }
            Mark stop;
            String text;
            while (true) {
                start = reader.mark();
                stop = reader.skipUntil("<");
                if (stop == null) {
                    err.jspError(start, "jsp.error.unterminated",
                            "&lt;jsp:expression&gt;");
                }
                text = parseScriptText(reader.getText(start, stop));
                @SuppressWarnings("unused")
                Node unused = new Node.Expression(text, start, parent);
                if (reader.matches("![CDATA[")) {
                    start = reader.mark();
                    stop = reader.skipUntil("]]>");
                    if (stop == null) {
                        err.jspError(start, "jsp.error.unterminated", "CDATA");
                    }
                    text = parseScriptText(reader.getText(start, stop));
                    @SuppressWarnings("unused")
                    Node unused2 = new Node.Expression(text, start, parent);
                } else {
                    break;
                }
            }
            if (!reader.matchesETagWithoutLessThan("jsp:expression")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:expression&gt;");
            }
        }
    }

    /*
     * ELExpressionBody. Starts with "#{" or "${".  Ends with "}".
     * See JspReader.skipELExpression().
     */
    private void parseELExpression(Node parent, char type)
            throws JasperException {
    	// reader === org.apache.jasper.compiler.JspReader
        start = reader.mark();
        Mark last = reader.skipELExpression();
        if (last == null) {
            err.jspError(start, "jsp.error.unterminated", type + "{");
        }

        @SuppressWarnings("unused")
        Node unused = new Node.ELExpression(type, reader.getText(start, last),
                start, parent);
    }

    /*
     * ScriptletBody ::= (Char* - (char* '%>')) '%>'
     */
    private void parseScriptlet(Node parent) throws JasperException {
        start = reader.mark();
        Mark stop = reader.skipUntil("%>");
        if (stop == null) {
            err.jspError(start, "jsp.error.unterminated", "&lt;%");
        }

        @SuppressWarnings("unused")
        Node unused = new Node.Scriptlet(
                parseScriptText(reader.getText(start, stop)), start, parent);
    }

    /*
     * XMLScriptletBody ::= ( S? '/>' ) | ( S? '>' (Char* - (char* '<'))
     * CDSect?)* ETag ) | <TRANSLATION_ERROR>
     */
    private void parseXMLScriptlet(Node parent) throws JasperException {
        reader.skipSpaces();
        if (!reader.matches("/>")) {
            if (!reader.matches(">")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:scriptlet&gt;");
            }
            Mark stop;
            String text;
            while (true) {
                start = reader.mark();
                stop = reader.skipUntil("<");
                if (stop == null) {
                    err.jspError(start, "jsp.error.unterminated",
                            "&lt;jsp:scriptlet&gt;");
                }
                text = parseScriptText(reader.getText(start, stop));
                @SuppressWarnings("unused")
                Node unused = new Node.Scriptlet(text, start, parent);
                if (reader.matches("![CDATA[")) {
                    start = reader.mark();
                    stop = reader.skipUntil("]]>");
                    if (stop == null) {
                        err.jspError(start, "jsp.error.unterminated", "CDATA");
                    }
                    text = parseScriptText(reader.getText(start, stop));
                    @SuppressWarnings("unused")
                    Node unused2 = new Node.Scriptlet(text, start, parent);
                } else {
                    break;
                }
            }

            if (!reader.matchesETagWithoutLessThan("jsp:scriptlet")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:scriptlet&gt;");
            }
        }
    }

    /**
     * Param ::= '<jsp:param' S Attributes S? EmptyBody S?
     */
    private void parseParam(Node parent) throws JasperException {
        if (!reader.matches("<jsp:param")) {
            err.jspError(reader.mark(), "jsp.error.paramexpected");
        }
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node paramActionNode = new Node.ParamAction(attrs, start, parent);

//        <jsp:param attr0="value1">
//	        <jsp:attribute name="attr1" value="" />
//	        <jsp:attribute name="attr2" value="" />value
//          <jsp:attribute name="value">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        </jsp:param>
        parseEmptyBody(paramActionNode, "jsp:param");

        reader.skipSpaces();
    }

    /*
     * For Include: StdActionContent ::= Attributes ParamBody
     *
     * ParamBody ::= EmptyBody | ( '>' S? ( '<jsp:attribute' NamedAttributes )? '<jsp:body'
     * (JspBodyParam | <TRANSLATION_ERROR> ) S? ETag ) | ( '>' S? Param* ETag )
     *
     * EmptyBody ::= '/>' | ( '>' ETag ) | ( '>' S? '<jsp:attribute'
     * NamedAttributes ETag )
     *
     * JspBodyParam ::= S? '>' Param* '</jsp:body>'
     */
    private void parseInclude(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node includeNode = new Node.IncludeAction(attrs, start, parent);
//        ****写法1
//        <jsp:include page="/test/jsp/header.jsp" />
//        ****写法2        
//        <jsp:include page="/test/jsp/header.jsp">
//        	<jsp:param attr0="value0" attr1="value1" />
//        </jsp:include>
//        ****写法3        
//        <jsp:include>
//	        <jsp:attribute name="page" value="/test/jsp/header.jsp" />
//	        <jsp:body>
//        		<jsp:param attr0="value0" attr1="value1" />
//        	</jsp:body>
//        </jsp:include>
//        ****写法4        
//        <jsp:include>
//	        <jsp:attribute name="page">/test/jsp/header.jsp</jsp:attribute>
//        	<jsp:body>
//				<jsp:param attr0="value0" attr1="value1" attr2="value2" />
//			</jsp:body>
//        </jsp:include>
//        ****写法5       
//        <jsp:include>
//	        <jsp:attribute name="page">/test/jsp/header.jsp</jsp:attribute>
//        	<jsp:body>
//				<jsp:param attr0="javabean1">
//        			<jsp:attribute name="attr1" value="value1" />
//        			<jsp:attribute name="attr2">value2</jsp:attribute>
//        		</jsp:param>
//			</jsp:body>
//        </jsp:include>
        parseOptionalBody(includeNode, "jsp:include", JAVAX_BODY_CONTENT_PARAM); // <body> 只能是<param>标签
    }

    /*
     * For Forward: StdActionContent ::= Attributes ParamBody
     */
    private void parseForward(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node forwardNode = new Node.ForwardAction(attrs, start, parent);
//      <jsp:forward attr0="value1">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="" />
//    		<jsp:attribute name="attr2" value="" />
//        	<jsp:body attr0="value1">
//        		<jsp:param attr0="javabean1">
//    				<jsp:attribute name="attr1" value="value1" />
//    				<jsp:attribute name="attr2" value="value2" />
//        		</jsp:param>
//          </jsp:body>
//  	</jsp:forward>
        parseOptionalBody(forwardNode, "jsp:forward", JAVAX_BODY_CONTENT_PARAM); // <body> 只能是<param>标签
    }

    private void parseInvoke(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node invokeNode = new Node.InvokeAction(attrs, start, parent);
//      <jsp:invoke attr0="value0">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="value1" />
//  	</jsp:invoke>
        parseEmptyBody(invokeNode, "jsp:invoke");
    }

    private void parseDoBody(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node doBodyNode = new Node.DoBodyAction(attrs, start, parent);

//      <jsp:doBody attr0="value0">
//        	<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        	<jsp:attribute name="attr1" value="value1" />
//      </jsp:doBody>
        parseEmptyBody(doBodyNode, "jsp:doBody");
    }

    private void parseElement(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node elementNode = new Node.JspElement(attrs, start, parent);
//      <jsp:element attr0="value1">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="" />
//    		<jsp:attribute name="attr2" value="" />
//        	<jsp:body attr0="value1">
//        		JSP页面支持的全部标签，包括如  ${user.info}
//          </jsp:body>
//  	</jsp:element>
        parseOptionalBody(elementNode, "jsp:element", TagInfo.BODY_CONTENT_JSP);
    }

    /*
     * For GetProperty: StdActionContent ::= Attributes EmptyBody
     */
    private void parseGetProperty(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node getPropertyNode = new Node.GetProperty(attrs, start, parent);
//      <jsp:getProperty attr0="value1">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="" />
//    		<jsp:attribute name="attr2" value="" />
//  	</jsp:getProperty>
        parseOptionalBody(getPropertyNode, "jsp:getProperty",
                TagInfo.BODY_CONTENT_EMPTY);
    }

    /*
     * For SetProperty: StdActionContent ::= Attributes EmptyBody
     */
    private void parseSetProperty(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node setPropertyNode = new Node.SetProperty(attrs, start, parent);
//      <jsp:setProperty attr0="value1">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="" />
//    		<jsp:attribute name="attr2" value="" />
//  	</jsp:setProperty>
        parseOptionalBody(setPropertyNode, "jsp:setProperty",
                TagInfo.BODY_CONTENT_EMPTY);
    }

    /*
     * EmptyBody ::= '/>' | ( '>' ETag ) | ( '>' S? '<jsp:attribute'
     * NamedAttributes ETag )
     */
    private void parseEmptyBody(Node parent, String tag) throws JasperException {
        if (reader.matches("/>")) {
            // Done
        } else if (reader.matches(">")) {
            if (reader.matchesETag(tag)) {
                // Done
            } else if (reader.matchesOptionalSpacesFollowedBy("<jsp:attribute")) {
                // Parse the one or more named attribute nodes
                parseNamedAttributes(parent);
                if (!reader.matchesETag(tag)) {
                    // Body not allowed
                    err.jspError(reader.mark(),
                            "jsp.error.jspbody.emptybody.only", "&lt;" + tag);
                }
            } else {
                err.jspError(reader.mark(), "jsp.error.jspbody.emptybody.only",
                        "&lt;" + tag);
            }
        } else {
            err.jspError(reader.mark(), "jsp.error.unterminated", "&lt;" + tag);
        }
    }

    /*
     * For UseBean: StdActionContent ::= Attributes OptionalBody
     */
    private void parseUseBean(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node useBeanNode = new Node.UseBean(attrs, start, parent);
//      <jsp:useBean attr0="value1">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="" />
//    		<jsp:attribute name="attr2" value="" />
//        	<jsp:body attr0="value1">
//        		JSP页面支持的全部标签，包括如  ${user.info}
//          </jsp:body>
//  	</jsp:useBean>
        parseOptionalBody(useBeanNode, "jsp:useBean", TagInfo.BODY_CONTENT_JSP);
    }

    /*
     * Parses OptionalBody, but also reused to parse bodies for plugin and param
     * since the syntax is identical (the only thing that differs substantially
     * is how to process the body, and thus we accept the body type as a
     * parameter).
     *
     * OptionalBody ::= EmptyBody | ActionBody
     *
     * ScriptlessOptionalBody ::= EmptyBody | ScriptlessActionBody
     *
     * TagDependentOptionalBody ::= EmptyBody | TagDependentActionBody
     *
     * EmptyBody ::= '/>' | ( '>' ETag ) | ( '>' S? '<jsp:attribute'
     * NamedAttributes ETag )
     *
     * ActionBody ::= JspAttributeAndBody | ( '>' Body ETag )
     *
     * ScriptlessActionBody ::= JspAttributeAndBody | ( '>' ScriptlessBody ETag )
     *
     * TagDependentActionBody ::= JspAttributeAndBody | ( '>' TagDependentBody
     * ETag )
     *
     */
    private void parseOptionalBody(Node parent, String tag, String bodyType)
            throws JasperException {
    	
//    	parseOptionalBody(tagNode, "myprefix:tagname", "scriptless"); // 解析body内容
    	
        if (reader.matches("/>")) {
            // EmptyBody
            return;
        }

        if (!reader.matches(">")) {
            err.jspError(reader.mark(), "jsp.error.unterminated", "&lt;" + tag);
        }
        
        if (reader.matchesETag(tag)) {
            // EmptyBody
            return;
        }
        
//	      <myprefix:tagname>
//	    	<jsp:attribute name="" value="" />
//			<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//          <jsp:body>body content...</jsp:body>
//	      </myprefix:tagname>
//        parseJspAttributeAndBody(parent, "myprefix:tagname", "scriptless");
        if (!parseJspAttributeAndBody(parent, tag, bodyType)) { // 查看是否是否子标签<jsp:attribute>或者<jsp:body>,如果有,就进行解析
            // Must be ( '>' # Body ETag )
//  	      <myprefix:tagname>
//        		body content...
//  	      </myprefix:tagname>
        	// 解析body内容
            parseBody(parent, tag, bodyType); // 解析body
        }
    }

    /**
     * Attempts to parse 'JspAttributeAndBody' production. Returns true if it
     * matched, or false if not. Assumes EmptyBody is okay as well.
     *
     * JspAttributeAndBody ::= ( '>' # S? ( '<jsp:attribute' NamedAttributes )? '<jsp:body' (
     * JspBodyBody | <TRANSLATION_ERROR> ) S? ETag )
     */
    private boolean parseJspAttributeAndBody(Node parent, String tag,
            String bodyType) throws JasperException {
        boolean result = false;

        if (reader.matchesOptionalSpacesFollowedBy("<jsp:attribute")) {
            // May be an EmptyBody, depending on whether
            // There's a "<jsp:body" before the ETag
        	
            // First, parse <jsp:attribute> elements:
//            <myprefix:tagname>
//            	<jsp:attribute name="" value="" />
//        		<jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
//            </myprefix:tagname>
        	// 解析标签 <jsp:attribute name="" value="" />
        	parseNamedAttributes(parent);

            result = true;
        }
//	      <myprefix:tagname>
//          <jsp:attribute name="" value="" />
//		    <jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
//	        <jsp:body>.....</jsp:body>
//	      </myprefix:tagname>
        if (reader.matchesOptionalSpacesFollowedBy("<jsp:body")) {
            // ActionBody
            parseJspBody(parent, bodyType);
            reader.skipSpaces();
            if (!reader.matchesETag(tag)) {
                err.jspError(reader.mark(), "jsp.error.unterminated", "&lt;"
                        + tag);
            }

            result = true;
        } else if (result && !reader.matchesETag(tag)) {
            // If we have <jsp:attribute> but something other than
            // <jsp:body> or the end tag, translation error.
            err.jspError(reader.mark(), "jsp.error.jspbody.required", "&lt;"
                    + tag);
        }

        return result;
    }

    /*
     * Params ::= `>' S? ( ( `<jsp:body>' ( ( S? Param+ S? `</jsp:body>' ) |
     * <TRANSLATION_ERROR> ) ) | Param+ ) '</jsp:params>'
     */
    private void parseJspParams(Node parent) throws JasperException {
        Node jspParamsNode = new Node.ParamsAction(start, parent);
        parseOptionalBody(jspParamsNode, "jsp:params", JAVAX_BODY_CONTENT_PARAM);
    }

    /*
     * Fallback ::= '/>' | ( `>' S? `<jsp:body>' ( ( S? ( Char* - ( Char* `</jsp:body>' ) ) `</jsp:body>'
     * S? ) | <TRANSLATION_ERROR> ) `</jsp:fallback>' ) | ( '>' ( Char* - (
     * Char* '</jsp:fallback>' ) ) '</jsp:fallback>' )
     */
    private void parseFallBack(Node parent) throws JasperException {
        Node fallBackNode = new Node.FallBackAction(start, parent);
        parseOptionalBody(fallBackNode, "jsp:fallback",
                JAVAX_BODY_CONTENT_TEMPLATE_TEXT);
    }

    /*
     * For Plugin: StdActionContent ::= Attributes PluginBody
     *
     * PluginBody ::= EmptyBody | ( '>' S? ( '<jsp:attribute' NamedAttributes )? '<jsp:body' (
     * JspBodyPluginTags | <TRANSLATION_ERROR> ) S? ETag ) | ( '>' S? PluginTags
     * ETag )
     *
     * EmptyBody ::= '/>' | ( '>' ETag ) | ( '>' S? '<jsp:attribute'
     * NamedAttributes ETag )
     *
     */
    private void parsePlugin(Node parent) throws JasperException {
        Attributes attrs = parseAttributes();
        reader.skipSpaces();

        Node pluginNode = new Node.PlugIn(attrs, start, parent);
//      <jsp:plugin attr0="value0">
//    		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//    		<jsp:attribute name="attr1" value="" />
//    		<jsp:attribute name="attr2" value="" />
//        	<jsp:body attr0="value0">
//        		<jsp:params attr0="value0">
//        			<jsp:param name="attr1" value="" />
//        			<jsp:param attr0="value0">
//				        <jsp:attribute name="attr1" value="" />
//				    	<jsp:attribute name="attr2" value="" />
//			        </jsp:param>
//        		</jsp:params>
//        		<jsp:fallback></jsp:fallback>
//          </jsp:body>
//  	 </jsp:plugin>
        parseOptionalBody(pluginNode, "jsp:plugin", JAVAX_BODY_CONTENT_PLUGIN);
    }

    /*
     * PluginTags ::= ( '<jsp:params' Params S? )? ( '<jsp:fallback' Fallback?
     * S? )?
     */
    private void parsePluginTags(Node parent) throws JasperException {
        reader.skipSpaces();

        if (reader.matches("<jsp:params")) {
            parseJspParams(parent);
            reader.skipSpaces();
        }

        if (reader.matches("<jsp:fallback")) {
            parseFallBack(parent);
            reader.skipSpaces();
        }
    }

    /*
     * StandardAction ::= 'include' StdActionContent | 'forward'
     * StdActionContent | 'invoke' StdActionContent | 'doBody' StdActionContent |
     * 'getProperty' StdActionContent | 'setProperty' StdActionContent |
     * 'useBean' StdActionContent | 'plugin' StdActionContent | 'element'
     * StdActionContent
     */
    private void parseStandardAction(Node parent) throws JasperException {
        Mark start = reader.mark();
        if (reader.matches(INCLUDE_ACTION)) {
//          <jsp:include page="/test/jsp/header.jsp">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:include>
            parseInclude(parent);
        } else if (reader.matches(FORWARD_ACTION)) {
//          <jsp:forward page="/test/jsp/header.jsp">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:forward>
            parseForward(parent);
        } else if (reader.matches(INVOKE_ACTION)) {
            if (!isTagFile) {
                err.jspError(reader.mark(), "jsp.error.action.isnottagfile",
                        "&lt;jsp:invoke");
            }
//          <jsp:invoke attr1="value1">
//        		<jsp:attribute name="param1" value="value1" />
//          </jsp:invoke>
            parseInvoke(parent);
        } else if (reader.matches(DOBODY_ACTION)) {
            if (!isTagFile) {
                err.jspError(reader.mark(), "jsp.error.action.isnottagfile",
                        "&lt;jsp:doBody");
            }
//          <jsp:doBody attr1="value1">
//        		<jsp:attribute name="param1" value="value1" />
//          </jsp:doBody>
            parseDoBody(parent);
        } else if (reader.matches(GET_PROPERTY_ACTION)) {
//          <jsp:getProperty attr0="value1">
//        		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        		<jsp:attribute name="attr1" value="" />
//        		<jsp:attribute name="attr2" value="" />
//      	</jsp:getProperty>
            parseGetProperty(parent);
        } else if (reader.matches(SET_PROPERTY_ACTION)) {
//          <jsp:setProperty attr0="value1">
//        		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        		<jsp:attribute name="attr1" value="" />
//        		<jsp:attribute name="attr2" value="" />
//      	</jsp:setProperty>
            parseSetProperty(parent);
        } else if (reader.matches(USE_BEAN_ACTION)) {
//          <jsp:useBean attr0="value1">
//        		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        		<jsp:attribute name="attr1" value="" />
//        		<jsp:attribute name="attr2" value="" />
//            	<jsp:body attr0="value1">
//            		JSP页面支持的全部标签，包括如  ${user.info}
//              </jsp:body>
//      	</jsp:useBean>
            parseUseBean(parent);
        } else if (reader.matches(PLUGIN_ACTION)) {
//          <jsp:plugin attr1="value1">
//        		<jsp:params attr0="value0">
//        			<jsp:param name="param1" value="value1" />
//        		</jsp:params>
//        		<jsp:fallback></jsp:fallback>
//          </jsp:plugin>
            parsePlugin(parent);
        } else if (reader.matches(ELEMENT_ACTION)) {
//          <jsp:element attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:element>
            parseElement(parent);
        } else if (reader.matches(ATTRIBUTE_ACTION)) {
            err.jspError(start, "jsp.error.namedAttribute.invalidUse");
        } else if (reader.matches(BODY_ACTION)) {
            err.jspError(start, "jsp.error.jspbody.invalidUse");
        } else if (reader.matches(FALLBACK_ACTION)) {
            err.jspError(start, "jsp.error.fallback.invalidUse");
        } else if (reader.matches(PARAMS_ACTION)) {
            err.jspError(start, "jsp.error.params.invalidUse");
        } else if (reader.matches(PARAM_ACTION)) {
            err.jspError(start, "jsp.error.param.invalidUse");
        } else if (reader.matches(OUTPUT_ACTION)) {
            err.jspError(start, "jsp.error.jspoutput.invalidUse");
        } else {
            err.jspError(start, "jsp.error.badStandardAction");
        }
    }

    /*
     * # '<' CustomAction CustomActionBody
     *
     * CustomAction ::= TagPrefix ':' CustomActionName
     *
     * TagPrefix ::= Name
     *
     * CustomActionName ::= Name
     *
     * CustomActionBody ::= ( Attributes CustomActionEnd ) | <TRANSLATION_ERROR>
     *
     * Attributes ::= ( S Attribute )* S?
     *
     * CustomActionEnd ::= CustomActionTagDependent | CustomActionJSPContent |
     * CustomActionScriptlessContent
     *
     * CustomActionTagDependent ::= TagDependentOptionalBody
     *
     * CustomActionJSPContent ::= OptionalBody
     *
     * CustomActionScriptlessContent ::= ScriptlessOptionalBody
     */
    @SuppressWarnings("null") // tagFileInfo can't be null after initial test
    private boolean parseCustomTag(Node parent) throws JasperException {
    	// 如：
    	// <myprefix:out value="${value1}" default="default_value1" escapeXml="true"/> ---> echo $value1
    	// <myprefix:set var="key1" scope="session" value="${value1}"/>   ---> $_SESSION['key1'] = $value1;
        if (reader.peekChar() != '<') {
            return false;
        }

        // Parse 'CustomAction' production (tag prefix and custom action name)
        reader.nextChar(); // skip '<'
        String tagName = reader.parseToken(false); // tagName = "myprefix:tag1"
        int i = tagName.indexOf(':');
        if (i == -1) {
            reader.reset(start);
            return false;
        }
        // 
        String prefix = tagName.substring(0, i); // 前缀   myprefix
        String shortTagName = tagName.substring(i + 1);  // out
//        pageInfo.taglibsMap = {
//        		"http://cn.java/jsp/core" : TagLibraryInfoImpl
//        };
//        pageInfo.jspPrefixMapper= {
//        	"myprefix" : "http://cn.java/jsp/core"
//    	  };
        // Check if this is a user-defined tag.
        // !!! pageInfo.jspPrefixMapper.get(prefix);
        String uri = pageInfo.getURI(prefix); // 根据前缀，取得当前页面声明的  uri信息
        // 如: uri="http://cn.java/jsp/core"   prefix="myprefix"
        if (uri == null) { // 查看是否有定义这个前缀
            if (pageInfo.isErrorOnUndeclaredNamespace()) { // 报错误
                err.jspError(start, "jsp.error.undeclared_namespace", prefix);
            } else {
                reader.reset(start);
                // Remember the prefix for later error checking
                pageInfo.putNonCustomTagPrefix(prefix, reader.mark());
                return false;
            }
        }
//        org.apache.tomcat.util.descriptor.tld.TldRuleSet 规则定义  
//        <taglib>
//		        <tlibversion></tlibversion>
//		        <tlib-version></tlib-version>
//		        <jspversion></jspversion>
//		        <jsp-version></jsp-version>
//		        <shortname></shortname>
//		        <short-name></short-name>
//		        <uri></uri>
//		        <info></info>
//		        <description></description>
//		        <listener>
//		                <listener-class></listener-class>
//		        </listener>
//		        <validator>
//		                <validator-class></validator-class>
//		                <init-param>
//		                        <param-name></param-name>
//		                        <param-value></param-value>
//		                </init-param>
//		        </validator>
//		        <tag>
//		                <info></info>
//		                <small-icon></small-icon>
//		                <large-icon></large-icon>
//		                <description></description>
//		                <display-name></display-name>
//		                <icon>
//		                        <small-icon></small-icon>
//		                        <large-icon></large-icon>
//		                </icon>
//		                <name></name>
//		                <tagclass></tagclass>
//		                <tag-class></tag-class>
//		                <teiclass></teiclass>
//		                <tei-class></tei-class>
//		                <bodycontent></bodycontent>
//		                <body-content></body-content>
//		                <variable>
//		                        <name-given></name-given>
//		                        <name-from-attribute></name-from-attribute>
//		                        <variable-class></variable-class>
//		                        <declare></declare>
//		                </variable>
//		                <attribute>
//		                    <description></description>
//		                    <name></name>
//		                    <required></required>
//		                    <rtexprvalue></rtexprvalue>
//		                    <type></type>
//		                    <deferred-value>
//		                            <type></type>
//		                    </deferred-value>
//		                    <deferred-method>
//		                            <method-signature></method-signature>
//		                    </deferred-method>
//		                    <fragment></fragment>
//		            </attribute>
//		            <dynamic-attributes></dynamic-attributes>
//		        </tag>
//		        <tag-file>
//		                <name></name>
//		                <path></path>
//		        </tag-file>
//		        <function>
//		                <name></name>
//		                <function-class></function-class>
//		                <function-signature></function-signature>
//		        </function>
//		</taglib>

        // 如： uri="http://cn.java/jsp/core" prefix="myprefix"
        // tagLibInfo === org.apache.jasper.compiler.TagLibraryInfoImpl  
        // TagLibraryInfoImpl(解析jsp头部声明，根据声明中的uri，匹配web.xml文件中配置的uri-->.tld信息，tld对象树TaglibXml的信息被再次提取出来放入TagLibraryInfoImpl)
        
//      pageInfo.taglibsMap = {
//			"http://cn.java/jsp/core" : TagLibraryInfoImpl
//		};
//		pageInfo.jspPrefixMapper= {
//			"myprefix" : "http://cn.java/jsp/core"
//	    };
//      pageInfo.taglibsMap.get(uri);
//      TagLibraryInfo ： tld文件的对象TaglibXml提取出的资料，重新放入TagLibraryInfoImpl
        TagLibraryInfo tagLibInfo = pageInfo.getTaglib(uri);  //
        
//      <tag>
//	        <description>Outputs Hello, World</description>
//	        <name>helloWorld</name>
//	        <tag-class>jsp2.examples.simpletag.HelloWorldSimpleTag</tag-class>
//	        <body-content>empty</body-content>
//	    </tag>
        // 匹配.tld文件中配置 <tag><name>shortTagName</name><tag-class>cn.java.Class4ShortTagName</tag-class></tag>
        TagInfo tagInfo = tagLibInfo.getTag(shortTagName); // 标签信息
        // 匹配.tld文件中的配置 <tag-file><name>shortTagName</name><path>/WEB-INF/tags/shortTagName.tag</path></tag-file>
        TagFileInfo tagFileInfo = tagLibInfo.getTagFile(shortTagName); // 标签文件信息
        if (tagInfo == null && tagFileInfo == null) {
            err.jspError(start, "jsp.error.bad_tag", shortTagName, prefix);
        }
        Class<?> tagHandlerClass = null;
        if (tagInfo != null) {
        	// 如果tag的配置如下
//        	<tag>
//	            <description>Outputs Hello, World</description>
//	            <name>helloWorld</name>
//	            <tag-class>jsp2.examples.simpletag.HelloWorldSimpleTag</tag-class>
//	            <body-content>empty</body-content>
//	        </tag>
            // Must be a classic tag, load it here.
            // tag files will be loaded later, in TagFileProcessor
            String handlerClassName = tagInfo.getTagClassName();
            try {
                tagHandlerClass = ctxt.getClassLoader().loadClass(
                        handlerClassName); // 加载标签处理类
            } catch (Exception e) {
                err.jspError(start, "jsp.error.loadclass.taghandler",
                        handlerClassName, tagName);
            }
        }

        // Parse 'CustomActionBody' production:
        // At this point we are committed - if anything fails, we produce
        // a translation error.

        // Parse 'Attributes' production:
        Attributes attrs = parseAttributes(); // 解析标签的属性
        reader.skipSpaces();

        // Parse 'CustomActionEnd' production:
        if (reader.matches("/>")) { // 如果没有body内容  <myprefix:tagname  attr1="value1" attr2="value2" />
            if (tagInfo != null) { // 使用 class类渲染
            	// Node.CustomTag("myprefix:tagname1", "myprefix", "tagname1","http://cn.java/jsp/core", 
            	//	{attr1:"value1" , attr2:"value2"}, start, parent, TagInfo, "cn.java.tag.TagName1Handler")
                @SuppressWarnings("unused")
                Node unused = new Node.CustomTag(tagName, prefix, shortTagName,
                        uri, attrs, start, parent, tagInfo, tagHandlerClass);
            } else {  // 使用　*.tag文件渲染
                @SuppressWarnings("unused")
                Node unused = new Node.CustomTag(tagName, prefix, shortTagName,
                        uri, attrs, start, parent, tagFileInfo);
            }
            return true;
        }

        // Now we parse one of 'CustomActionTagDependent',
        // 'CustomActionJSPContent', or 'CustomActionScriptlessContent'.
        // depending on body-content in TLD.

        // Looking for a body, it still can be empty; but if there is a
        // a tag body, its syntax would be dependent on the type of
        // body content declared in the TLD.
        String bc;
        if (tagInfo != null) {
//        	<tag>
//	        	<name></name>
//	            <tagclass></tagclass>
//	            <body-content>bodyType,如:JSP,scriptless</body-content>
//            </tag>
            bc = tagInfo.getBodyContent(); // 取得内容
        } else {
        	// (解析 /webapps/examples/WEB-INF/tags/displayProducts.tag 文件的对象树).getBodyContent();
        	// 在 org.apache.jasper.compiler.TagLibraryInfoImpl.createTagFileInfo() 中创建的 tagFileInfo对象和 TagInfo 对象
        	// TagFileProcessor.parseTagFileDirectives(...)
        	// tagFileInfo === javax.servlet.jsp.tagext.TagFileInfo.TagFileInfo
        	// tagFileInfo.getTagInfo() === org.apache.jasper.compiler.JasperTagInfo
        	// bc === TagInfo.BODY_CONTENT_SCRIPTLESS
            bc = tagFileInfo.getTagInfo().getBodyContent(); // *.tag 
        }

        Node tagNode = null;
        if (tagInfo != null) { // 使用 class类渲染
        	// Node.CustomTag("myprefix:tagname1", "myprefix", "tagname1","http://cn.java/jsp/core", 
        	//	{attr1:"value1" , attr2:"value2"}, start, parent, TagInfo, "cn.java.tag.TagName1Handler")
            tagNode = new Node.CustomTag(tagName, prefix, shortTagName, uri,
            		attrs, start, parent, tagInfo, tagHandlerClass); // tagHandlerClass是标签对应的处理类信息，即标签对应调用类
        } else { // 使用　*.tag文件渲染
            tagNode = new Node.CustomTag(tagName, prefix, shortTagName, uri,
                    attrs, start, parent, tagFileInfo);// tagFileInfo是标签对应的处理文件信息，即标签对应调用文件
        }
//	      <myprefix:tagname>
//	    	<jsp:attribute name="" value="" />
//			<jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
//	        <jsp:body>body content...</jsp:body>
//	      </myprefix:tagname>
//        或者
//      <myprefix:tagname>
//			body content...
//      </myprefix:tagname>
        
//        parseOptionalBody(tagNode, "myprefix:tagname", "scriptless"); // 解析body内容
        parseOptionalBody(tagNode, tagName, bc); // 解析body内容

        return true;
    }

    /*
     * Parse for a template text string until '<' or "${" or "#{" is encountered,
     * recognizing escape sequences "<\%", "\$", and "\#".
     *
     * Note: JSP uses '\$' as an escape for '$' and '\#' for '#' whereas EL uses
     *       '\${' for '${' and '\#{' for '#{'. We are processing JSP template
     *       test here so the JSP escapes apply.
     */
    private void parseTemplateText(Node parent) {

        if (!reader.hasMoreInput())
            return;

        CharArrayWriter ttext = new CharArrayWriter();

        int ch = reader.nextChar();
        while (ch != -1) {
            if (ch == '<') {
                // Check for "<\%"
                if (reader.peekChar(0) == '\\' && reader.peekChar(1) == '%') {
                    ttext.write(ch);
                    // Swallow the \
                    reader.nextChar();
                    ttext.write(reader.nextChar());
                } else {
                    if (ttext.size() == 0) {
                        ttext.write(ch);
                    } else {
                        reader.pushChar();
                        break;
                    }
                }
            } else if (ch == '\\' && !pageInfo.isELIgnored()) {
                int next = reader.peekChar(0);
                if (next == '$' || next == '#') {
                    ttext.write(reader.nextChar());
                } else {
                    ttext.write(ch);
                }
            } else if ((ch == '$' || ch == '#' && !pageInfo.isDeferredSyntaxAllowedAsLiteral()) &&
                    !pageInfo.isELIgnored()) {
                if (reader.peekChar(0) == '{') {
                    reader.pushChar();
                    break;
                } else {
                    ttext.write(ch);
                }
            } else {
                ttext.write(ch);
            }
            ch = reader.nextChar();
        }

        @SuppressWarnings("unused")
        Node unused = new Node.TemplateText(ttext.toString(), start, parent);
    }

    /*
     * XMLTemplateText ::= ( S? '/>' ) | ( S? '>' ( ( Char* - ( Char* ( '<' |
     * '${' ) ) ) ( '${' ELExpressionBody )? CDSect? )* ETag ) |
     * <TRANSLATION_ERROR>
     */
    private void parseXMLTemplateText(Node parent) throws JasperException {
        reader.skipSpaces();
        if (!reader.matches("/>")) {
            if (!reader.matches(">")) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:text&gt;");
            }
            // <jsp:text></jsp:text>
            // <jsp:text><![CDATA[  ...  ]]></jsp:text>
            // <jsp:text>$</jsp:text>
            // <jsp:text>#</jsp:text>
            CharArrayWriter ttext = new CharArrayWriter();
            int ch = reader.nextChar();
            while (ch != -1) {
                if (ch == '<') {
                    // Check for <![CDATA[
                    if (!reader.matches("![CDATA[")) {
                        break;
                    }
                    start = reader.mark();
                    Mark stop = reader.skipUntil("]]>");
                    if (stop == null) {
                        err.jspError(start, "jsp.error.unterminated", "CDATA");
                    }
                    String text = reader.getText(start, stop); //文本内容
                    ttext.write(text, 0, text.length());
                } else if (ch == '\\') {
                    int next = reader.peekChar(0);
                    if (next == '$' || next =='#') { // 如果下一个字符是 $ 或者 # ,就排除掉 \
                        ttext.write(reader.nextChar());
                    } else {
                        ttext.write('\\');
                    }
                } else if (ch == '$' || ch == '#') {  // ${   或者  #{
                    if (reader.peekChar(0) == '{') {
                        // Swallow the '{'
                        reader.nextChar();

                        // Create a template text node
                        @SuppressWarnings("unused")
                        Node unused = new Node.TemplateText(
                                ttext.toString(), start, parent);

                        // Mark and parse the EL expression and create its node:
                        parseELExpression(parent, (char) ch);  // 是EL表达式

                        start = reader.mark();
                        ttext.reset();
                    } else {
                        ttext.write(ch);
                    }
                } else {
                    ttext.write(ch);
                }
                ch = reader.nextChar();
            }

            @SuppressWarnings("unused")
            Node unused =
                    new Node.TemplateText(ttext.toString(), start, parent);

            if (!reader.hasMoreInput()) {
                err.jspError(start, "jsp.error.unterminated",
                        "&lt;jsp:text&gt;");
            } else if (!reader.matchesETagWithoutLessThan("jsp:text")) {
                err.jspError(start, "jsp.error.jsptext.badcontent");
            }
        }
    }

    /*
     * AllBody ::= ( '<%--' JSPCommentBody ) | ( '<%@' DirectiveBody ) | ( '<jsp:directive.'
     * XMLDirectiveBody ) | ( '<%!' DeclarationBody ) | ( '<jsp:declaration'
     * XMLDeclarationBody ) | ( '<%=' ExpressionBody ) | ( '<jsp:expression'
     * XMLExpressionBody ) | ( '${' ELExpressionBody ) | ( '<%' ScriptletBody ) | ( '<jsp:scriptlet'
     * XMLScriptletBody ) | ( '<jsp:text' XMLTemplateText ) | ( '<jsp:'
     * StandardAction ) | ( '<' CustomAction CustomActionBody ) | TemplateText
     */
    private void parseElements(Node parent) throws JasperException {
        if (scriptlessCount > 0) {
            // vc: ScriptlessBody
            // We must follow the ScriptlessBody production if one of
            // our parents is ScriptlessBody.
            parseElementsScriptless(parent);
            return;
        }

        start = reader.mark();
        if (reader.matches("<%--")) {
            parseComment(parent); // 注释
        } else if (reader.matches("<%@")) {
//          <%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//          <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
//          <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ attribute attr1="value1" attr2="value2" %>
//          <%@ variable attr1="value1" attr2="value2" %>
            parseDirective(parent); // 普通指令
        } else if (reader.matches("<jsp:directive.")) {
//      	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//          <jsp:directive.taglib uri="http://cn.java/jsp/core" prefix="myprefix" />
//          <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.attribute attr1="value1" attr2="value2" />
//          <jsp:directive.variable attr1="value1" attr2="value2" />
            parseXMLDirective(parent);  // xml指令
        } else if (reader.matches("<%!")) {
//        	<%!   %>
            parseDeclaration(parent); // 声明
        } else if (reader.matches("<jsp:declaration")) {
//        	<jsp:declaration></jsp:declaration>
//        	<jsp:declaration><![CDATA[  ...  ]]></jsp:declaration>
//        	<jsp:declaration> ...  <![CDATA[  ...  ]]> ...  </jsp:declaration>
            parseXMLDeclaration(parent);
        } else if (reader.matches("<%=")) {
//        	<%=  >
            parseExpression(parent); // 表达式
        } else if (reader.matches("<jsp:expression")) {
//        	<jsp:expression></jsp:expression>
//        	<jsp:expression><![CDATA[  ...  ]]></jsp:expression>
//        	<jsp:expression> ...  <![CDATA[  ...  ]]> ...  </jsp:expression>
            parseXMLExpression(parent);
        } else if (reader.matches("<%")) { // 脚本
//        	<%   %>
            parseScriptlet(parent);
        } else if (reader.matches("<jsp:scriptlet")) { // 脚本
//        	<jsp:scriptlet></jsp:scriptlet>
//        	<jsp:scriptlet><![CDATA[  ...  ]]></jsp:scriptlet>
//        	<jsp:scriptlet> ...  <![CDATA[  ...  ]]> ...  </jsp:scriptlet>
            parseXMLScriptlet(parent);
        } else if (reader.matches("<jsp:text")) {
//        	<jsp:text></jsp:text>
//          <jsp:text><![CDATA[  ...  ]]></jsp:text>
            parseXMLTemplateText(parent);
        } else if (!pageInfo.isELIgnored() && reader.matches("${")) {  // EL 表达式
//        	${param.username}   --->  <%=request.getParameter("username") %>
//        	${param['username']}
//        	${param["username"]}
//        	${param{param["username"]}}  --?-> 嵌套   <%=request.getParameter(request.getParameter("username")) %>
            parseELExpression(parent, '$');
        } else if (!pageInfo.isELIgnored()
                && !pageInfo.isDeferredSyntaxAllowedAsLiteral()
                && reader.matches("#{")) { // EL 表达式
//        	#{param['username']}
            parseELExpression(parent, '#');
        } else if (reader.matches("<jsp:")) {
//          <jsp:include page="/test/jsp/header.jsp">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:include>
//          <jsp:forward page="/test/jsp/header.jsp">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:forward>
//          <jsp:invoke attr1="value1">
//        		<jsp:attribute name="param1" value="value1" />
//          </jsp:invoke>
//          <jsp:doBody attr1="value1">
//        		<jsp:attribute name="param1" value="value1" />
//          </jsp:doBody>
//          <jsp:getProperty attr0="value1">
//        		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        		<jsp:attribute name="attr1" value="" />
//        		<jsp:attribute name="attr2" value="" />
//      	</jsp:getProperty>
//          <jsp:setProperty attr0="value1">
//        		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        		<jsp:attribute name="attr1" value="" />
//        		<jsp:attribute name="attr2" value="" />
//      	</jsp:setProperty>
//          <jsp:useBean attr0="value1">
//        		<jsp:attribute name="page">JSP页面支持的全部标签，包括如  ${user.info}</jsp:attribute>
//        		<jsp:attribute name="attr1" value="" />
//        		<jsp:attribute name="attr2" value="" />
//            	<jsp:body attr0="value1">
//            		JSP页面支持的全部标签，包括如  ${user.info}
//              </jsp:body>
//      	</jsp:useBean>
//          <jsp:plugin attr1="value1">
//        		<jsp:params attr0="value0">
//        			<jsp:param name="param1" value="value1" />
//        		</jsp:params>
//        		<jsp:fallback></jsp:fallback>
//          </jsp:plugin>
//          <jsp:element attr1="value1">
//        		<jsp:param name="param1" value="value1" />
//          </jsp:element>
            parseStandardAction(parent);
        } else if (!parseCustomTag(parent)) { // 解析自定义标签
//          <myprefix:tagname  attr1="value1" attr2="value2" />
//          或者
//  	    <myprefix:tagname>
//  	    	<jsp:attribute name="" value="" />
//  			<jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
//  	        <jsp:body>body content...</jsp:body>
//  	      </myprefix:tagname>
//          或者
//        <myprefix:tagname>
//  			body content...
//        </myprefix:tagname>
            checkUnbalancedEndTag();
            parseTemplateText(parent); // 普通文本信息
        }
    }

    /*
     * ScriptlessBody ::= ( '<%--' JSPCommentBody ) | ( '<%@' DirectiveBody ) | ( '<jsp:directive.'
     * XMLDirectiveBody ) | ( '<%!' <TRANSLATION_ERROR> ) | ( '<jsp:declaration'
     * <TRANSLATION_ERROR> ) | ( '<%=' <TRANSLATION_ERROR> ) | ( '<jsp:expression'
     * <TRANSLATION_ERROR> ) | ( '<%' <TRANSLATION_ERROR> ) | ( '<jsp:scriptlet'
     * <TRANSLATION_ERROR> ) | ( '<jsp:text' XMLTemplateText ) | ( '${'
     * ELExpressionBody ) | ( '<jsp:' StandardAction ) | ( '<' CustomAction
     * CustomActionBody ) | TemplateText
     */
    private void parseElementsScriptless(Node parent) throws JasperException {
        // Keep track of how many scriptless nodes we've encountered
        // so we know whether our child nodes are forced scriptless
        scriptlessCount++;

        start = reader.mark();
        if (reader.matches("<%--")) {
            parseComment(parent);
        } else if (reader.matches("<%@")) {
            parseDirective(parent);
        } else if (reader.matches("<jsp:directive.")) {
            parseXMLDirective(parent);
        } else if (reader.matches("<%!")) {
            err.jspError(reader.mark(), "jsp.error.no.scriptlets");
        } else if (reader.matches("<jsp:declaration")) {
            err.jspError(reader.mark(), "jsp.error.no.scriptlets");
        } else if (reader.matches("<%=")) {
            err.jspError(reader.mark(), "jsp.error.no.scriptlets");
        } else if (reader.matches("<jsp:expression")) {
            err.jspError(reader.mark(), "jsp.error.no.scriptlets");
        } else if (reader.matches("<%")) {
            err.jspError(reader.mark(), "jsp.error.no.scriptlets");
        } else if (reader.matches("<jsp:scriptlet")) {
            err.jspError(reader.mark(), "jsp.error.no.scriptlets");
        } else if (reader.matches("<jsp:text")) {
            parseXMLTemplateText(parent);
        } else if (!pageInfo.isELIgnored() && reader.matches("${")) {
            parseELExpression(parent, '$');
        } else if (!pageInfo.isELIgnored()
                && !pageInfo.isDeferredSyntaxAllowedAsLiteral()
                && reader.matches("#{")) {
            parseELExpression(parent, '#');
        } else if (reader.matches("<jsp:")) {
            parseStandardAction(parent);
        } else if (!parseCustomTag(parent)) {
            checkUnbalancedEndTag();
            parseTemplateText(parent);
        }

        scriptlessCount--;
    }

    /*
     * TemplateTextBody ::= ( '<%--' JSPCommentBody ) | ( '<%@' DirectiveBody ) | ( '<jsp:directive.'
     * XMLDirectiveBody ) | ( '<%!' <TRANSLATION_ERROR> ) | ( '<jsp:declaration'
     * <TRANSLATION_ERROR> ) | ( '<%=' <TRANSLATION_ERROR> ) | ( '<jsp:expression'
     * <TRANSLATION_ERROR> ) | ( '<%' <TRANSLATION_ERROR> ) | ( '<jsp:scriptlet'
     * <TRANSLATION_ERROR> ) | ( '<jsp:text' <TRANSLATION_ERROR> ) | ( '${'
     * <TRANSLATION_ERROR> ) | ( '<jsp:' <TRANSLATION_ERROR> ) | TemplateText
     */
    private void parseElementsTemplateText(Node parent) throws JasperException {
        start = reader.mark();
        if (reader.matches("<%--")) {
            parseComment(parent);
        } else if (reader.matches("<%@")) {
            parseDirective(parent);
        } else if (reader.matches("<jsp:directive.")) {
            parseXMLDirective(parent);
        } else if (reader.matches("<%!")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Declarations");
        } else if (reader.matches("<jsp:declaration")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Declarations");
        } else if (reader.matches("<%=")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Expressions");
        } else if (reader.matches("<jsp:expression")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Expressions");
        } else if (reader.matches("<%")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Scriptlets");
        } else if (reader.matches("<jsp:scriptlet")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Scriptlets");
        } else if (reader.matches("<jsp:text")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "&lt;jsp:text");
        } else if (!pageInfo.isELIgnored() && reader.matches("${")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Expression language");
        } else if (!pageInfo.isELIgnored()
                && !pageInfo.isDeferredSyntaxAllowedAsLiteral()
                && reader.matches("#{")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Expression language");
        } else if (reader.matches("<jsp:")) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Standard actions");
        } else if (parseCustomTag(parent)) {
            err.jspError(reader.mark(), "jsp.error.not.in.template",
                    "Custom actions");
        } else {
            checkUnbalancedEndTag();
            parseTemplateText(parent);
        }
    }

    /*
     * Flag as error if an unbalanced end tag appears by itself.
     */
    private void checkUnbalancedEndTag() throws JasperException {

        if (!reader.matches("</")) {
            return;
        }

        // Check for unbalanced standard actions
        if (reader.matches("jsp:")) {
            err.jspError(start, "jsp.error.unbalanced.endtag", "jsp:");
        }

        // Check for unbalanced custom actions
        String tagName = reader.parseToken(false);
        int i = tagName.indexOf(':');
        if (i == -1 || pageInfo.getURI(tagName.substring(0, i)) == null) { // 通过前缀取得uri信息
            reader.reset(start);
            return;
        }

        err.jspError(start, "jsp.error.unbalanced.endtag", tagName);
    }

    /**
     * TagDependentBody :=
     */
    private void parseTagDependentBody(Node parent, String tag)
            throws JasperException {
        Mark bodyStart = reader.mark();
        Mark bodyEnd = reader.skipUntilETag(tag);
        if (bodyEnd == null) {
            err.jspError(start, "jsp.error.unterminated", "&lt;" + tag);
        }
        @SuppressWarnings("unused")
        Node unused = new Node.TemplateText(reader.getText(bodyStart, bodyEnd),
                bodyStart, parent);
    }

    /*
     * Parses jsp:body action.
     */
    private void parseJspBody(Node parent, String bodyType)
            throws JasperException {
        Mark start = reader.mark();
        Node bodyNode = new Node.JspBody(start, parent);

        reader.skipSpaces();
        if (!reader.matches("/>")) {
            if (!reader.matches(">")) {
                err.jspError(start, "jsp.error.unterminated", "&lt;jsp:body");
            }
            parseBody(bodyNode, "jsp:body", bodyType);
        }
    }

    /*
     * Parse the body as JSP content. @param tag The name of the tag whose end
     * tag would terminate the body @param bodyType One of the TagInfo body
     * types
     */
    private void parseBody(Node parent, String tag, String bodyType)
            throws JasperException {
//    	bodyType  body被允许的类型
//    	parseBody(namedAttributeNode, "jsp:attribute",TagInfo.BODY_CONTENT_JSP));
    	// 如：bodyType == TagInfo.BODY_CONTENT_JSP
        if (bodyType.equalsIgnoreCase(TagInfo.BODY_CONTENT_TAG_DEPENDENT)) { // body是普通文本
            parseTagDependentBody(parent, tag);
        } else if (bodyType.equalsIgnoreCase(TagInfo.BODY_CONTENT_EMPTY)) { // 不能有body
            if (!reader.matchesETag(tag)) {
                err.jspError(start, "jasper.error.emptybodycontent.nonempty",
                        tag);
            }
        } else if (bodyType == JAVAX_BODY_CONTENT_PLUGIN) { // 是<jsp:params 或者 <jsp:fallback
            // (note the == since we won't recognize JAVAX_*
            // from outside this module).
            parsePluginTags(parent);
            if (!reader.matchesETag(tag)) {
                err.jspError(reader.mark(), "jsp.error.unterminated", "&lt;"
                        + tag);
            }
        } else if (bodyType.equalsIgnoreCase(TagInfo.BODY_CONTENT_JSP)
                || bodyType.equalsIgnoreCase(TagInfo.BODY_CONTENT_SCRIPTLESS)
                || (bodyType == JAVAX_BODY_CONTENT_PARAM)
                || (bodyType == JAVAX_BODY_CONTENT_TEMPLATE_TEXT)) {
            while (reader.hasMoreInput()) {
                if (reader.matchesETag(tag)) {
                    return;
                }

                // Check for nested jsp:body or jsp:attribute
                if (tag.equals("jsp:body") || tag.equals("jsp:attribute")) {
                    if (reader.matches("<jsp:attribute")) {  // 多级嵌套<jsp:attribute，就报错
                        err.jspError(reader.mark(),
                                "jsp.error.nested.jspattribute");
                    } else if (reader.matches("<jsp:body")) { // 多级嵌套<jsp:body，就报错
                        err.jspError(reader.mark(), "jsp.error.nested.jspbody");
                    }
                }

                if (bodyType.equalsIgnoreCase(TagInfo.BODY_CONTENT_JSP)) { // body可以是JSP页面支持的全部标签，包括如  ${user.info}
//                	<jsp:attribute name="page">JSP页面支持的全部标签，包括如 ${user.info}</jsp:attribute>
                    parseElements(parent);
                } else if (bodyType
                        .equalsIgnoreCase(TagInfo.BODY_CONTENT_SCRIPTLESS)) { // body只能是脚本标签
                    parseElementsScriptless(parent);
                } else if (bodyType == JAVAX_BODY_CONTENT_PARAM) { // body只能是<jsp:param>标签
                    // (note the == since we won't recognize JAVAX_*
                    // from outside this module).
                    reader.skipSpaces();
//                  <jsp:param attr0="value1">
//        	        	<jsp:attribute name="attr1" value="" />
//        	        	<jsp:attribute name="attr2" value="" />
//                  </jsp:param>
                    parseParam(parent);
                } else if (bodyType == JAVAX_BODY_CONTENT_TEMPLATE_TEXT) { // body只能是文本
                    parseElementsTemplateText(parent);
                }
            }
            err.jspError(start, "jsp.error.unterminated", "&lt;" + tag);
        } else {
            err.jspError(start, "jasper.error.bad.bodycontent.type");
        }
    }

    /*
     * Parses named attributes.
     */
    private void parseNamedAttributes(Node parent) throws JasperException {
        do {
        	// 解析标签 <jsp:attribute name="page" value="" />
        	// 解析标签 <jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
//          <myprefix:tagname>
//        		<jsp:attribute name="" value="" />
//          </myprefix:tagname>
            Mark start = reader.mark();
            Attributes attrs = parseAttributes(); // 解析标签的属性
            Node.NamedAttribute namedAttributeNode = new Node.NamedAttribute(
                    attrs, start, parent);

            reader.skipSpaces();
            if (!reader.matches("/>")) {
                if (!reader.matches(">")) {
                    err.jspError(start, "jsp.error.unterminated",
                            "&lt;jsp:attribute");
                }
                if (namedAttributeNode.isTrim()) {
                    reader.skipSpaces();
                }
//                解析标签<jsp:attribute></jsp:attribute>的body内容
//                <jsp:attribute name="page" value="" />
//                <jsp:attribute name="page">JSP页面支持的标签，如 ${user.info}</jsp:attribute>
                parseBody(namedAttributeNode, "jsp:attribute",
                        getAttributeBodyType(parent, attrs.getValue("name")));
                if (namedAttributeNode.isTrim()) {
                    Node.Nodes subElems = namedAttributeNode.getBody();
                    if (subElems != null) {
                        Node lastNode = subElems.getNode(subElems.size() - 1);
                        if (lastNode instanceof Node.TemplateText) {
                            ((Node.TemplateText) lastNode).rtrim();
                        }
                    }
                }
            }
            reader.skipSpaces();
        } while (reader.matches("<jsp:attribute"));
    }

    /**
     * Determine the body type of <jsp:attribute> from the enclosing node
     */
    private String getAttributeBodyType(Node n, String name) {

        if (n instanceof Node.CustomTag) { //  如果当前是自定义标签的属性
//          <myprefix:tagname>
//        		<jsp:attribute name="" value="" />
//          </myprefix:tagname>
            TagInfo tagInfo = ((Node.CustomTag) n).getTagInfo();
            TagAttributeInfo[] tldAttrs = tagInfo.getAttributes(); // 标签支持的属性列表
            for (int i = 0; i < tldAttrs.length; i++) {
                if (name.equals(tldAttrs[i].getName())) {
                    if (tldAttrs[i].isFragment()) {
                        return TagInfo.BODY_CONTENT_SCRIPTLESS;
                    }
                    if (tldAttrs[i].canBeRequestTime()) {
                        return TagInfo.BODY_CONTENT_JSP;
                    }
                }
            }
            if (tagInfo.hasDynamicAttributes()) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.IncludeAction) {
//          <jsp:include>
//        		<jsp:attribute name="page" value="" />
//          </jsp:include>
            if ("page".equals(name)) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.ForwardAction) {
            if ("page".equals(name)) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.SetProperty) {
            if ("value".equals(name)) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.UseBean) {
            if ("beanName".equals(name)) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.PlugIn) {
            if ("width".equals(name) || "height".equals(name)) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.ParamAction) {
            if ("value".equals(name)) {
                return TagInfo.BODY_CONTENT_JSP;
            }
        } else if (n instanceof Node.JspElement) {
            return TagInfo.BODY_CONTENT_JSP;
        }

        return JAVAX_BODY_CONTENT_TEMPLATE_TEXT;
    }

    private void parseFileDirectives(Node parent) throws JasperException {
//  	<%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//      <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//      <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
//      <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//      <%@ attribute attr1="value1" attr2="value2" %>
//      <%@ variable attr1="value1" attr2="value2" %>
//  	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//      <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//      <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//      <jsp:directive.attribute attr1="value1" attr2="value2" />
//      <jsp:directive.variable attr1="value1" attr2="value2" />
//    	<%-- 注释  --%>
//    	<%!  声明    %>
//    	<%= 表达式  %>
//    	<% 脚本  %>
        reader.skipUntil("<");
        while (reader.hasMoreInput()) {
            start = reader.mark();
            if (reader.matches("%--")) { 
            	// <%-- 注释  --%>
                // Comment
                reader.skipUntil("--%>");
            } else if (reader.matches("%@"))  {// <%@ 
//            	  <%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//                <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//                <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
//                <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//                <%@ attribute attr1="value1" attr2="value2" %>
//                <%@ variable attr1="value1" attr2="value2" %>
            	
                // 可在JSP页面使用的指令
//              <%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//              <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//              <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>  
                
                // 可在标签页面(*.tag)页面使用的指令
//              <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//              <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>  
//              <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//              <%@ attribute attr1="value1" attr2="value2" %>
//              <%@ variable attr1="value1" attr2="value2" %>
            	
                parseDirective(parent);
            } else if (reader.matches("jsp:directive.")) {
//          	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//              <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//              <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//              <jsp:directive.attribute attr1="value1" attr2="value2" />
//              <jsp:directive.variable attr1="value1" attr2="value2" />
            	
                // 可在JSP页面使用的指令
//          	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//              <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
                
                // 可在标签页面(*.tag)页面使用的指令
//              <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//              <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//              <jsp:directive.attribute attr1="value1" attr2="value2" />
//              <jsp:directive.variable attr1="value1" attr2="value2" />
                parseXMLDirective(parent);
            } else if (reader.matches("%!")) { // 跳过声明
            	// <%!  声明    %>
                // Declaration
                reader.skipUntil("%>");
            } else if (reader.matches("%=")) { // 跳过表达式
            	// <%= 表达式  %>
                // Expression
                reader.skipUntil("%>");
            } else if (reader.matches("%")) { // 跳过脚本
            	// <% 脚本  %>
                // Scriptlet
                reader.skipUntil("%>");
            }
            reader.skipUntil("<");
        }
    }
}
