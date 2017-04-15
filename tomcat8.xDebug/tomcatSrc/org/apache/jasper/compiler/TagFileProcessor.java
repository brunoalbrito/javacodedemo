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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagFileInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.jsp.tagext.TagVariableInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.runtime.JspSourceDependent;
import org.apache.jasper.servlet.JspServletWrapper;
import org.apache.tomcat.Jar;
import org.apache.tomcat.util.descriptor.tld.TldResourcePath;

/**
 * 1. Processes and extracts the directive info in a tag file. 2. Compiles and
 * loads tag files used in a JSP file.
 *
 * @author Kin-man Chung
 */

class TagFileProcessor {

    private Vector<Compiler> tempVector;

    /**
     * A visitor the tag file
     */
    private static class TagFileDirectiveVisitor extends Node.Visitor {

    	// display-name="" body-content="" dynamic-attributes="" small-icon="" large-icon="" 
    	//	description="" example="" pageEncoding="" language="" import="" deferredSyntaxAllowedAsLiteral=""
    	//  trimDirectiveWhitespaces="" isELIgnored=""
        private static final JspUtil.ValidAttribute[] tagDirectiveAttrs = {
                new JspUtil.ValidAttribute("display-name"),
                new JspUtil.ValidAttribute("body-content"),
                new JspUtil.ValidAttribute("dynamic-attributes"),
                new JspUtil.ValidAttribute("small-icon"),
                new JspUtil.ValidAttribute("large-icon"),
                new JspUtil.ValidAttribute("description"),
                new JspUtil.ValidAttribute("example"),
                new JspUtil.ValidAttribute("pageEncoding"),
                new JspUtil.ValidAttribute("language"),
                new JspUtil.ValidAttribute("import"),
                new JspUtil.ValidAttribute("deferredSyntaxAllowedAsLiteral"), // JSP 2.1
                new JspUtil.ValidAttribute("trimDirectiveWhitespaces"), // JSP 2.1
                new JspUtil.ValidAttribute("isELIgnored") };
//        	name=""  required=""  fragment=""  rtexprvalue="" type="" deferredValue="" 
//        		deferredValueType=""  deferredMethod=""  deferredMethodSignature=""  description=""
        private static final JspUtil.ValidAttribute[] attributeDirectiveAttrs = {
                new JspUtil.ValidAttribute("name", true),
                new JspUtil.ValidAttribute("required"),
                new JspUtil.ValidAttribute("fragment"),
                new JspUtil.ValidAttribute("rtexprvalue"),
                new JspUtil.ValidAttribute("type"),
                new JspUtil.ValidAttribute("deferredValue"),            // JSP 2.1
                new JspUtil.ValidAttribute("deferredValueType"),        // JSP 2.1
                new JspUtil.ValidAttribute("deferredMethod"),           // JSP 2
                new JspUtil.ValidAttribute("deferredMethodSignature"),  // JSP 21
                new JspUtil.ValidAttribute("description") };
//        name-given="" name-from-attribute="" alias="" variable-class="" scope="" declare="" description=""
        private static final JspUtil.ValidAttribute[] variableDirectiveAttrs = {
                new JspUtil.ValidAttribute("name-given"),
                new JspUtil.ValidAttribute("name-from-attribute"),
                new JspUtil.ValidAttribute("alias"),
                new JspUtil.ValidAttribute("variable-class"),
                new JspUtil.ValidAttribute("scope"),
                new JspUtil.ValidAttribute("declare"),
                new JspUtil.ValidAttribute("description") };

        private ErrorDispatcher err;

        private TagLibraryInfo tagLibInfo;

        private String name = null;

        private String path = null;

        private String bodycontent = null;

        private String description = null;

        private String displayName = null;

        private String smallIcon = null;

        private String largeIcon = null;

        private String dynamicAttrsMapName;

        private String example = null;

        private Vector<TagAttributeInfo> attributeVector;

        private Vector<TagVariableInfo> variableVector;

        private static final String ATTR_NAME = "the name attribute of the attribute directive";

        private static final String VAR_NAME_GIVEN = "the name-given attribute of the variable directive";

        private static final String VAR_NAME_FROM = "the name-from-attribute attribute of the variable directive";

        private static final String VAR_ALIAS = "the alias attribute of the variable directive";

        private static final String TAG_DYNAMIC = "the dynamic-attributes attribute of the tag directive";

        private HashMap<String,NameEntry> nameTable = new HashMap<>();

        private HashMap<String,NameEntry> nameFromTable = new HashMap<>();

        public TagFileDirectiveVisitor(Compiler compiler,
                TagLibraryInfo tagLibInfo, String name, String path) {
//        	*.tld文件
//        	<tag-file>
//    		        <name>tag1</name>
//    		        <path>/webapps/examples/WEB-INF/tags/dir1/tag1.tag</path>
//    		</tag-file>
            err = compiler.getErrorDispatcher();
            this.tagLibInfo = tagLibInfo; // tagLibInfo == org.apache.jasper.compiler.TagLibraryInfoImpl
            this.name = name;  // displayProducts
            this.path = path; // /webapps/examples/WEB-INF/tags/dir1/tag1.tag
            attributeVector = new Vector<>();
            variableVector = new Vector<>();
        }
        
//		<%-- 注释  --%>
		
	 // 可在标签页面(*.tag)页面使用的指令
	//  <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
	//  <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>  
	//  <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
	//  <%@ attribute attr1="value1" attr2="value2" %>
	//  <%@ variable attr1="value1" attr2="value2" %>
		
	  // 可在标签页面(*.tag)页面使用的指令
	//  <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
	//  <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
	//  <jsp:directive.attribute attr1="value1" attr2="value2" />
	//  <jsp:directive.variable attr1="value1" attr2="value2" />
        
        @Override
        public void visit(Node.TagDirective n) throws JasperException { // 只能有一个
        	//<%@ tag display-name="" body-content="" dynamic-attributes="" small-icon="" large-icon="" 
        	//	description="" example="" pageEncoding="" language="" import="cn.java.test2.*,cn.java.test.Hello" deferredSyntaxAllowedAsLiteral=""
        	//  trimDirectiveWhitespaces="" isELIgnored="" %>
        	//<jsp:directive.tag display-name="" body-content="" dynamic-attributes="" small-icon="" large-icon="" 
        	//	description="" example="" pageEncoding="" language="" import="cn.java.test2.*,cn.java.test.Hello" deferredSyntaxAllowedAsLiteral=""
        	//  trimDirectiveWhitespaces="" isELIgnored="" />
            JspUtil.checkAttributes("Tag directive", n, tagDirectiveAttrs, err);

            bodycontent = checkConflict(n, bodycontent, "body-content");
            if (bodycontent != null
                    && !bodycontent
                            .equalsIgnoreCase(TagInfo.BODY_CONTENT_EMPTY)
                    && !bodycontent
                            .equalsIgnoreCase(TagInfo.BODY_CONTENT_TAG_DEPENDENT)
                    && !bodycontent
                            .equalsIgnoreCase(TagInfo.BODY_CONTENT_SCRIPTLESS)) {
                err.jspError(n, "jsp.error.tagdirective.badbodycontent",
                        bodycontent);
            }
            dynamicAttrsMapName = checkConflict(n, dynamicAttrsMapName,
                    "dynamic-attributes");
            if (dynamicAttrsMapName != null) {
                checkUniqueName(dynamicAttrsMapName, TAG_DYNAMIC, n);
            }
            smallIcon = checkConflict(n, smallIcon, "small-icon");
            largeIcon = checkConflict(n, largeIcon, "large-icon");
            description = checkConflict(n, description, "description");
            displayName = checkConflict(n, displayName, "display-name");
            example = checkConflict(n, example, "example");
        }

        private String checkConflict(Node n, String oldAttrValue, String attr)
                throws JasperException {

            String result = oldAttrValue;
            String attrValue = n.getAttributeValue(attr);
            if (attrValue != null) {
                if (oldAttrValue != null && !oldAttrValue.equals(attrValue)) {
                    err.jspError(n, "jsp.error.tag.conflict.attr", attr,
                            oldAttrValue, attrValue);
                }
                result = attrValue;
            }
            return result;
        }
      
        @Override
        public void visit(Node.AttributeDirective n) throws JasperException {

            JspUtil.checkAttributes("Attribute directive", n,
                    attributeDirectiveAttrs, err);

//            <%@ attribute name=""  required=""  fragment=""  rtexprvalue="" type="" deferredValue="" 
//            		deferredValueType=""  deferredMethod=""  deferredMethodSignature=""  description="" %>
//           <jsp:directive.attribute name=""  required=""  fragment=""  rtexprvalue="" type="" deferredValue="" 
//            		deferredValueType=""  deferredMethod=""  deferredMethodSignature=""  description="" />
            
            // 属性例子
            // deferredValue="true" deferredValueType="cn.java.Test"
            // deferredValue="true" deferredValueType="java.lang.Object"
            // deferredValue="false"
            // JSP 2.1 Table JSP.8-3
            // handle deferredValue and deferredValueType
            boolean deferredValue = false;
            boolean deferredValueSpecified = false;
            String deferredValueString = n.getAttributeValue("deferredValue");
            if (deferredValueString != null) {
                deferredValueSpecified = true;
                deferredValue = JspUtil.booleanValue(deferredValueString);
            }
            String deferredValueType = n.getAttributeValue("deferredValueType");
            if (deferredValueType != null) {
                if (deferredValueSpecified && !deferredValue) {
                    err.jspError(n, "jsp.error.deferredvaluetypewithoutdeferredvalue");
                } else {
                    deferredValue = true;
                }
            } else if (deferredValue) {
                deferredValueType = "java.lang.Object";
            } else {
                deferredValueType = "java.lang.String";
            }

            // JSP 2.1 Table JSP.8-3
            // handle deferredMethod and deferredMethodSignature
            // 属性例子
            // deferredMethod="true" deferredMethodSignature="void methodname1()"
            // deferredMethod="true"
            // deferredMethod="false"
            boolean deferredMethod = false;
            boolean deferredMethodSpecified = false;
            String deferredMethodString = n.getAttributeValue("deferredMethod");
            if (deferredMethodString != null) {
                deferredMethodSpecified = true;
                deferredMethod = JspUtil.booleanValue(deferredMethodString);
            }
            String deferredMethodSignature = n
                    .getAttributeValue("deferredMethodSignature");
            if (deferredMethodSignature != null) { // 有定义签名
                if (deferredMethodSpecified && !deferredMethod) { 
                    err.jspError(n, "jsp.error.deferredmethodsignaturewithoutdeferredmethod");
                } else {
                    deferredMethod = true;
                }
            } else if (deferredMethod) {
                deferredMethodSignature = "void methodname()";
            }

            if (deferredMethod && deferredValue) {
                err.jspError(n, "jsp.error.deferredmethodandvalue");
            }

            
            String attrName = n.getAttributeValue("name"); // 属性名
            boolean required = JspUtil.booleanValue(n
                    .getAttributeValue("required"));
            boolean rtexprvalue = true;
            String rtexprvalueString = n.getAttributeValue("rtexprvalue");
            if (rtexprvalueString != null) {
                rtexprvalue = JspUtil.booleanValue(rtexprvalueString);
            }
            boolean fragment = JspUtil.booleanValue(n
                    .getAttributeValue("fragment"));
            String type = n.getAttributeValue("type");
            if (fragment) { //  fragment="true" name="" required="true"
                // type is fixed to "JspFragment" and a translation error
                // must occur if specified.
                if (type != null) {
                    err.jspError(n, "jsp.error.fragmentwithtype");
                }
                // rtexprvalue is fixed to "true" and a translation error
                // must occur if specified.
                rtexprvalue = true;
                if (rtexprvalueString != null) {
                    err.jspError(n, "jsp.error.frgmentwithrtexprvalue");
                }
            } else { //  fragment="false" type="java.lang.String" name="" required="true"
                if (type == null)
                    type = "java.lang.String";

                if (deferredValue) {
                    type = ValueExpression.class.getName();
                } else if (deferredMethod) {
                    type = MethodExpression.class.getName();
                }
            }

            if (("2.0".equals(tagLibInfo.getRequiredVersion()) || ("1.2".equals(tagLibInfo.getRequiredVersion())))
                    && (deferredMethodSpecified || deferredMethod
                            || deferredValueSpecified || deferredValue)) {
                err.jspError("jsp.error.invalid.version", path);
            }

            TagAttributeInfo tagAttributeInfo = new TagAttributeInfo(attrName,
                    required, type, rtexprvalue, fragment, null, deferredValue,
                    deferredMethod, deferredValueType, deferredMethodSignature);
            attributeVector.addElement(tagAttributeInfo);
            checkUniqueName(attrName, ATTR_NAME, n, tagAttributeInfo);
        }

        @Override
        public void visit(Node.VariableDirective n) throws JasperException {

            JspUtil.checkAttributes("Variable directive", n,
                    variableDirectiveAttrs, err);
//           <%@ variable  name-given="" name-from-attribute="" alias="" variable-class="" scope="" declare="" description="" %>
//           <jsp:directive.variable name-given="" name-from-attribute="" alias="" variable-class="" scope="" declare="" description="" />
            String nameGiven = n.getAttributeValue("name-given");
            String nameFromAttribute = n
                    .getAttributeValue("name-from-attribute");
            if (nameGiven == null && nameFromAttribute == null) {
                err.jspError("jsp.error.variable.either.name");
            }

            if (nameGiven != null && nameFromAttribute != null) {
                err.jspError("jsp.error.variable.both.name");
            }

            String alias = n.getAttributeValue("alias");
            if (nameFromAttribute != null && alias == null
                    || nameFromAttribute == null && alias != null) {
                err.jspError("jsp.error.variable.alias");
            }

            String className = n.getAttributeValue("variable-class");
            if (className == null)
                className = "java.lang.String";

            String declareStr = n.getAttributeValue("declare");
            boolean declare = true;
            if (declareStr != null)
                declare = JspUtil.booleanValue(declareStr);

            int scope = VariableInfo.NESTED;
            String scopeStr = n.getAttributeValue("scope");
            if (scopeStr != null) {
                if ("NESTED".equals(scopeStr)) {
                    // Already the default
                } else if ("AT_BEGIN".equals(scopeStr)) {
                    scope = VariableInfo.AT_BEGIN;
                } else if ("AT_END".equals(scopeStr)) {
                    scope = VariableInfo.AT_END;
                }
            }

            if (nameFromAttribute != null) {
                /*
                 * An alias has been specified. We use 'nameGiven' to hold the
                 * value of the alias, and 'nameFromAttribute' to hold the name
                 * of the attribute whose value (at invocation-time) denotes the
                 * name of the variable that is being aliased
                 */
                nameGiven = alias;
                checkUniqueName(nameFromAttribute, VAR_NAME_FROM, n);
                checkUniqueName(alias, VAR_ALIAS, n);
            } else {
                // name-given specified
                checkUniqueName(nameGiven, VAR_NAME_GIVEN, n);
            }

            variableVector.addElement(new TagVariableInfo(nameGiven,
                    nameFromAttribute, className, declare, scope));
        }

        public TagInfo getTagInfo() throws JasperException {

            if (name == null) {
                // XXX Get it from tag file name
            }

            if (bodycontent == null) {
                bodycontent = TagInfo.BODY_CONTENT_SCRIPTLESS;
            }

            // String tagClassName = "org.apache.jsp.tag.web.dir1.tag1_tag";
            String tagClassName = JspUtil.getTagHandlerClassName(
                    path, tagLibInfo.getReliableURN(), err);

            // <%@ variable name-given="name" %>
            TagVariableInfo[] tagVariableInfos = new TagVariableInfo[variableVector
                    .size()];
            variableVector.copyInto(tagVariableInfos);

            // <%@ attribute name="normalPrice" fragment="true" %>
            TagAttributeInfo[] tagAttributeInfo = new TagAttributeInfo[attributeVector
                    .size()];
            attributeVector.copyInto(tagAttributeInfo);
//            return new JasperTagInfo("displayProducts", tagClassName, TagInfo.BODY_CONTENT_SCRIPTLESS,
//                    description, TagLibraryInfoImpl, null, tagAttributeInfo,
//                    displayName, smallIcon, largeIcon, tagVariableInfos,
//                    dynamicAttrsMapName);
            return new JasperTagInfo(name, tagClassName, bodycontent,
            		description, tagLibInfo, null, tagAttributeInfo,
            		displayName, smallIcon, largeIcon, tagVariableInfos,
            		dynamicAttrsMapName);
        }

        static class NameEntry {
            private String type;

            private Node node;

            private TagAttributeInfo attr;

            NameEntry(String type, Node node, TagAttributeInfo attr) {
                this.type = type;
                this.node = node;
                this.attr = attr;
            }

            String getType() {
                return type;
            }

            Node getNode() {
                return node;
            }

            TagAttributeInfo getTagAttributeInfo() {
                return attr;
            }
        }

        /**
         * Reports a translation error if names specified in attributes of
         * directives are not unique in this translation unit.
         *
         * The value of the following attributes must be unique. 1. 'name'
         * attribute of an attribute directive 2. 'name-given' attribute of a
         * variable directive 3. 'alias' attribute of variable directive 4.
         * 'dynamic-attributes' of a tag directive except that
         * 'dynamic-attributes' can (and must) have the same value when it
         * appears in multiple tag directives.
         *
         * Also, 'name-from' attribute of a variable directive cannot have the
         * same value as that from another variable directive.
         */
        private void checkUniqueName(String name, String type, Node n)
                throws JasperException {
            checkUniqueName(name, type, n, null);
        }

        private void checkUniqueName(String name, String type, Node n,
                TagAttributeInfo attr) throws JasperException {

            HashMap<String, NameEntry> table = (VAR_NAME_FROM.equals(type)) ? nameFromTable : nameTable;
            NameEntry nameEntry = table.get(name);
            if (nameEntry != null) {
                if (!TAG_DYNAMIC.equals(type) ||
                        !TAG_DYNAMIC.equals(nameEntry.getType())) {
                    int line = nameEntry.getNode().getStart().getLineNumber();
                    err.jspError(n, "jsp.error.tagfile.nameNotUnique", type,
                            nameEntry.getType(), Integer.toString(line));
                }
            } else {
                table.put(name, new NameEntry(type, n, attr));
            }
        }

        /**
         * Perform miscellaneous checks after the nodes are visited.
         */
        void postCheck() throws JasperException {
            // Check that var.name-from-attributes has valid values.
            for (Entry<String, NameEntry> entry : nameFromTable.entrySet()) {
                String key = entry.getKey();
                NameEntry nameEntry = nameTable.get(key);
                NameEntry nameFromEntry = entry.getValue();
                Node nameFromNode = nameFromEntry.getNode();
                if (nameEntry == null) {
                    err.jspError(nameFromNode,
                            "jsp.error.tagfile.nameFrom.noAttribute", key);
                } else {
                    Node node = nameEntry.getNode();
                    TagAttributeInfo tagAttr = nameEntry.getTagAttributeInfo();
                    if (!"java.lang.String".equals(tagAttr.getTypeName())
                            || !tagAttr.isRequired()
                            || tagAttr.canBeRequestTime()) {
                        err.jspError(nameFromNode,
                                "jsp.error.tagfile.nameFrom.badAttribute",
                                key, Integer.toString(node.getStart()
                                        .getLineNumber()));
                    }
                }
            }
        }
    }

    /**
     * Parses the tag file, and collects information on the directives included
     * in it. The method is used to obtain the info on the tag file, when the
     * handler that it represents is referenced. The tag file is not compiled
     * here.
     *
     * @param pc
     *            the current ParserController used in this compilation
     * @param name
     *            the tag name as specified in the TLD
     * @param path
     *            the path for the tagfile
     * @param jar
     *            the Jar resource containing the tag file
     * @param tagLibInfo
     *            the TagLibraryInfo object associated with this TagInfo
     * @return a TagInfo object assembled from the directives in the tag file.
     *
     * @throws JasperException If an error occurs during parsing
     */
    @SuppressWarnings("null") // page can't be null
    public static TagInfo parseTagFileDirectives(ParserController pc,
            String name, String path, Jar jar, TagLibraryInfo tagLibInfo)
            throws JasperException {


        ErrorDispatcher err = pc.getCompiler().getErrorDispatcher();

        Node.Nodes page = null;
        try {
        	// 解析标签中的信息 ，如 path === /webapps/examples/WEB-INF/tags/displayProducts.tag
        	// 如displayProducts.tag文件内容如下：
//        	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
//        	<%@ attribute name="normalPrice" fragment="true" %>
//        	<%@ attribute name="onSale" fragment="true" %>
//        	<%@ variable name-given="name" %>
//        	<%@ variable name-given="price" %>
//        	<%@ variable name-given="origPrice" %>
//        	<%@ variable name-given="salePrice" %>
//
//        	<table border="1">
//        	  <tr>
//        	    <td>
//        	      <c:set var="name" value="Hand-held Color PDA"/>
//        	      <c:set var="price" value="$298.86"/>
//        	      <jsp:invoke fragment="normalPrice"/>
//        	    </td>
//        	    <td>
//        	      <c:set var="name" value="4-Pack 150 Watt Light Bulbs"/>
//        	      <c:set var="origPrice" value="$2.98"/>
//        	      <c:set var="salePrice" value="$2.32"/>
//        	      <jsp:invoke fragment="onSale"/>
//        	    </td>
//        	    <td>
//        	      <c:set var="name" value="Digital Cellular Phone"/>
//        	      <c:set var="price" value="$68.74"/>
//        	      <jsp:invoke fragment="normalPrice"/>
//        	    </td>
//        	    <td>
//        	      <c:set var="name" value="Baby Grand Piano"/>
//        	      <c:set var="price" value="$10,800.00"/>
//        	      <jsp:invoke fragment="normalPrice"/>
//        	    </td>
//        	    <td>
//        	      <c:set var="name" value="Luxury Car w/ Leather Seats"/>
//        	      <c:set var="origPrice" value="$23,980.00"/>
//        	      <c:set var="salePrice" value="$21,070.00"/>
//        	      <jsp:invoke fragment="onSale"/>
//        	    </td>
//        	  </tr>
//        	</table>
            page = pc.parseTagFileDirectives(path, jar); // 解析 displayProducts.tag 文件中标签
        } catch (IOException e) {
            err.jspError("jsp.error.file.not.found", path);
        }
//        <%@ tag display-name="" body-content="" dynamic-attributes="" small-icon="" large-icon="" 
//      		description="" example="" pageEncoding="" language="" import="cn.java.test2.*,cn.java.test.Hello" deferredSyntaxAllowedAsLiteral=""
//      		trimDirectiveWhitespaces="" isELIgnored="" %>
//      	<jsp:directive.tag display-name="" body-content="" dynamic-attributes="" small-icon="" large-icon="" 
//      		description="" example="" pageEncoding="" language="" import="cn.java.test2.*,cn.java.test.Hello" deferredSyntaxAllowedAsLiteral=""
//      		trimDirectiveWhitespaces="" isELIgnored="" />
//        <%@ attribute name=""  required=""  fragment=""  rtexprvalue="" type="" deferredValue="" 
//  			deferredValueType=""  deferredMethod=""  deferredMethodSignature=""  description="" %>
//        <jsp:directive.attribute name=""  required=""  fragment=""  rtexprvalue="" type="" deferredValue="" 
//  			deferredValueType=""  deferredMethod=""  deferredMethodSignature=""  description="" />
//        <%@ variable  name-given="" name-from-attribute="" alias="" variable-class="" scope="" declare="" description="" %>
//        <jsp:directive.variable name-given="" name-from-attribute="" alias="" variable-class="" scope="" declare="" description="" />

        // tagLibInfo == org.apache.jasper.compiler.TagLibraryInfoImpl
        TagFileDirectiveVisitor tagFileVisitor = new TagFileDirectiveVisitor(pc
                .getCompiler(), tagLibInfo, name, path);
        page.visit(tagFileVisitor);
        tagFileVisitor.postCheck();

        return tagFileVisitor.getTagInfo(); // org.apache.jasper.compiler.JasperTagInfo  *.tag文件解析处理的对象树
    }

    /**
     * Compiles and loads a tagfile.
     */
    private Class<?> loadTagFile(Compiler compiler, String tagFilePath,
            TagInfo tagInfo, PageInfo parentPageInfo) throws JasperException {

        Jar tagJar = null;
        Jar tagJarOriginal = null;
        try {
            if (tagFilePath.startsWith("/META-INF/")) {
                try {
                    tagJar = compiler.getCompilationContext().getTldResourcePath(
                                tagInfo.getTagLibrary().getURI()).openJar();
                } catch (IOException ioe) {
                    throw new JasperException(ioe);
                }
            }
            String wrapperUri;
            if (tagJar == null) {
                wrapperUri = tagFilePath; // 如: "/WEB-INFO/tags/dir1/tag1.tag"
            } else {
                wrapperUri = tagJar.getURL(tagFilePath);
            }

            JspCompilationContext ctxt = compiler.getCompilationContext();
            JspRuntimeContext rctxt = ctxt.getRuntimeContext();
            // rctxt === org.apache.jasper.compiler.JspRuntimeContext
            synchronized (rctxt) {
                JspServletWrapper wrapper = null;
                try {
                    wrapper = rctxt.getWrapper(wrapperUri);
                    if (wrapper == null) {
                    	// !!!!!
                        wrapper = new JspServletWrapper(ctxt.getServletContext(), ctxt
                                .getOptions(), tagFilePath, tagInfo, ctxt
                                .getRuntimeContext(), tagJar);
                        // rctxt.addWrapper("/WEB-INFO/tags/dir1/tag1.tag", wrapper); //!!!!
                        rctxt.addWrapper(wrapperUri, wrapper); //!!!!

                        // Use same classloader and classpath for compiling tag files
                        wrapper.getJspEngineContext().setClassLoader(
                                ctxt.getClassLoader());
                        wrapper.getJspEngineContext().setClassPath(ctxt.getClassPath());
                    } else {
                        // Make sure that JspCompilationContext gets the latest TagInfo
                        // for the tag file. TagInfo instance was created the last
                        // time the tag file was scanned for directives, and the tag
                        // file may have been modified since then.
                        wrapper.getJspEngineContext().setTagInfo(tagInfo);
                        // This compilation needs to use the current tagJar.
                        // Compilation may be nested in which case the old tagJar
                        // will need to be restored
                        tagJarOriginal = wrapper.getJspEngineContext().getTagFileJar();
                        wrapper.getJspEngineContext().setTagFileJar(tagJar);
                    }

                    Class<?> tagClazz;
                    int tripCount = wrapper.incTripCount();
                    try {
                        if (tripCount > 0) {
                            // When tripCount is greater than zero, a circular
                            // dependency exists. The circularly dependent tag
                            // file is compiled in prototype mode, to avoid infinite
                            // recursion.

                            JspServletWrapper tempWrapper = new JspServletWrapper(ctxt
                                    .getServletContext(), ctxt.getOptions(),
                                    tagFilePath, tagInfo, ctxt.getRuntimeContext(),
                                    tagJar);
                            // Use same classloader and classpath for compiling tag files
                            tempWrapper.getJspEngineContext().setClassLoader(
                                    ctxt.getClassLoader());
                            tempWrapper.getJspEngineContext().setClassPath(ctxt.getClassPath());
                            // 如: tagClazz = "org.apache.jsp.tag.web.dir1.tag1_tag"
                            tagClazz = tempWrapper.loadTagFilePrototype(); // 编译"/WEB-INFO/tags/dir1/tag1.tag"成 dir1_tag1.class文件(Servlet)，并加载进内存
                            tempVector.add(tempWrapper.getJspEngineContext()
                                    .getCompiler());
                        } else {
                            tagClazz = wrapper.loadTagFile();
                        }
                    } finally {
                        wrapper.decTripCount();
                    }

                    // Add the dependents for this tag file to its parent's
                    // Dependent list. The only reliable dependency information
                    // can only be obtained from the tag instance.
                    try {
                    	// 如: tagClazz = "org.apache.jsp.tag.web.dir1.tag1_tag"
                        Object tagIns = tagClazz.newInstance();  // new dir1_tag1();
                        if (tagIns instanceof JspSourceDependent) {
                            Iterator<Entry<String,Long>> iter = ((JspSourceDependent)
                                    tagIns).getDependants().entrySet().iterator();
                            while (iter.hasNext()) {
                                Entry<String,Long> entry = iter.next();
                                parentPageInfo.addDependant(entry.getKey(),
                                        entry.getValue());
                            }
                        }
                    } catch (Exception e) {
                        // ignore errors
                    }

                    return tagClazz;
                } finally {
                    if (wrapper != null && tagJarOriginal != null) {
                        wrapper.getJspEngineContext().setTagFileJar(tagJarOriginal);
                    }
                }
            }
        } finally {
            if (tagJar != null) {
                tagJar.close();
            }
        }
    }

    /*
     * Visitor which scans the page and looks for tag handlers that are tag
     * files, compiling (if necessary) and loading them.
     */
    private class TagFileLoaderVisitor extends Node.Visitor {

        private Compiler compiler;

        private PageInfo pageInfo;

        TagFileLoaderVisitor(Compiler compiler) {

            this.compiler = compiler;
            this.pageInfo = compiler.getPageInfo();
        }

        @Override
        public void visit(Node.CustomTag n) throws JasperException {
            TagFileInfo tagFileInfo = n.getTagFileInfo();  // 标签对应的调用文件
            if (tagFileInfo != null) {
                String tagFilePath = tagFileInfo.getPath(); // 标签文件路径
                if (tagFilePath.startsWith("/META-INF/")) {
                    // For tags in JARs, add the TLD and the tag as a dependency
                    TldResourcePath tldResourcePath =
                        compiler.getCompilationContext().getTldResourcePath(
                            tagFileInfo.getTagInfo().getTagLibrary().getURI());

                    try (Jar jar = tldResourcePath.openJar()) {

                        if (jar != null) {
                            // Add TLD
                            pageInfo.addDependant(jar.getURL(tldResourcePath.getEntryName()),
                                                  Long.valueOf(jar.getLastModified(tldResourcePath.getEntryName())));
                            // Add Tag
                            pageInfo.addDependant(jar.getURL(tagFilePath.substring(1)),
                                                  Long.valueOf(jar.getLastModified(tagFilePath.substring(1))));
                        } else {
                            pageInfo.addDependant(tagFilePath,
                                                  compiler.getCompilationContext().getLastModified(tagFilePath));
                        }
                    } catch (IOException ioe) {
                        throw new JasperException(ioe);
                    }
                } else {
                    pageInfo.addDependant(tagFilePath,
                            compiler.getCompilationContext().getLastModified(tagFilePath));
                }
                
                // 加载标签文件,如:"/WEB-INFO/tags/dir1/tag1.tag"
                Class<?> c = loadTagFile(compiler, tagFilePath, n.getTagInfo(),
                        pageInfo); // 把标签文件编译成class文件，并加载类
                n.setTagHandlerClass(c); // dir1_tag1 
            }
            visitBody(n);
        }
    }

    /**
     * Implements a phase of the translation that compiles (if necessary) the
     * tag files used in a JSP files. The directives in the tag files are
     * assumed to have been processed and encapsulated as TagFileInfo in the
     * CustomTag nodes.
     *
     * @param compiler Compiler to use to compile tag files
     * @param page     The page from to scan for tag files to compile
     *
     * @throws JasperException If an error occurs during the scan or compilation
     */
    public void loadTagFiles(Compiler compiler, Node.Nodes page)
            throws JasperException {

        tempVector = new Vector<>();
        page.visit(new TagFileLoaderVisitor(compiler));
    }

    /**
     * Removed the java and class files for the tag prototype generated from the
     * current compilation.
     *
     * @param classFileName
     *            If non-null, remove only the class file with with this name.
     */
    public void removeProtoTypeFiles(String classFileName) {
        Iterator<Compiler> iter = tempVector.iterator();
        while (iter.hasNext()) {
            Compiler c = iter.next();
            if (classFileName == null) {
                c.removeGeneratedClassFiles();
            } else if (classFileName.equals(c.getCompilationContext()
                    .getClassFileName())) {
                c.removeGeneratedClassFiles();
                tempVector.remove(c);
                return;
            }
        }
    }
}
