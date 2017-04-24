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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.servlet.JspServletWrapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.Jar;
import org.apache.tomcat.util.scan.JarFactory;

/**
 * Main JSP compiler class. This class uses Ant for compiling.
 *
 * @author Anil K. Vijendran
 * @author Mandar Raje
 * @author Pierre Delisle
 * @author Kin-man Chung
 * @author Remy Maucherat
 * @author Mark Roth
 */
public abstract class Compiler {

    private final Log log = LogFactory.getLog(Compiler.class); // must not be static

    // ----------------------------------------------------- Instance Variables

    protected JspCompilationContext ctxt;

    protected ErrorDispatcher errDispatcher;

    protected PageInfo pageInfo;

    protected JspServletWrapper jsw;

    protected TagFileProcessor tfp;

    protected Options options;

    protected Node.Nodes pageNodes;

    // ------------------------------------------------------------ Constructor

    public void init(JspCompilationContext ctxt, JspServletWrapper jsw) {
        this.jsw = jsw;
        this.ctxt = ctxt;
        this.options = ctxt.getOptions();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * <p>
     * Retrieves the parsed nodes of the JSP page, if they are available. May
     * return null. Used in development mode for generating detailed error
     * messages. http://bz.apache.org/bugzilla/show_bug.cgi?id=37062.
     * </p>
     * @return the page nodes
     */
    public Node.Nodes getPageNodes() {
        return this.pageNodes;
    }

    /**
     * Compile the jsp file into equivalent servlet in .java file
     *
     * @return a smap for the current JSP page, if one is generated, null
     *         otherwise
     * @throws Exception Error generating Java source
     */
    protected String[] generateJava() throws Exception {

        String[] smapStr = null;

        long t1, t2, t3, t4;

        t1 = t2 = t3 = t4 = 0;

        if (log.isDebugEnabled()) {
            t1 = System.currentTimeMillis();
        }

        // ctxt === org.apache.jasper.JspCompilationContext
        // Setup page info area
        pageInfo = new PageInfo(new BeanRepository(ctxt.getClassLoader(),
                errDispatcher), ctxt.getJspFile(), ctxt.isTagFile());

        // options === org.apache.jasper.EmbeddedServletOptions
        JspConfig jspConfig = options.getJspConfig();
        // jspConfig === org.apache.jasper.compiler.JspConfig
        // !!!!! 匹配  ， 根据访问路径，取得配置org.apache.jasper.compiler.JspConfig.JspProperty
        // <jsp-property-group>
        //  ctxt.getJspFile() === "/a/b/c/file.jsp"
        JspConfig.JspProperty jspProperty = jspConfig.findJspProperty(ctxt
                .getJspFile());

        /*
         * If the current uri is matched by a pattern specified in a
         * jsp-property-group in web.xml, initialize pageInfo with those
         * properties.
         */
        if (jspProperty.isELIgnored() != null) {
            pageInfo.setELIgnored(JspUtil.booleanValue(jspProperty
                    .isELIgnored()));
        }
        if (jspProperty.isScriptingInvalid() != null) {
            pageInfo.setScriptingInvalid(JspUtil.booleanValue(jspProperty
                    .isScriptingInvalid()));
        }
        if (jspProperty.getIncludePrelude() != null) {
            pageInfo.setIncludePrelude(jspProperty.getIncludePrelude());
        }
        if (jspProperty.getIncludeCoda() != null) {
            pageInfo.setIncludeCoda(jspProperty.getIncludeCoda());
        }
        if (jspProperty.isDeferedSyntaxAllowedAsLiteral() != null) {
            pageInfo.setDeferredSyntaxAllowedAsLiteral(JspUtil.booleanValue(jspProperty
                    .isDeferedSyntaxAllowedAsLiteral()));
        }
        if (jspProperty.isTrimDirectiveWhitespaces() != null) {
            pageInfo.setTrimDirectiveWhitespaces(JspUtil.booleanValue(jspProperty
                    .isTrimDirectiveWhitespaces()));
        }
        // Default ContentType processing is deferred until after the page has
        // been parsed
        if (jspProperty.getBuffer() != null) {
            pageInfo.setBufferValue(jspProperty.getBuffer(), null,
                    errDispatcher); // 缓冲区
        }
        if (jspProperty.isErrorOnUndeclaredNamespace() != null) {
            pageInfo.setErrorOnUndeclaredNamespace(
                    JspUtil.booleanValue(
                            jspProperty.isErrorOnUndeclaredNamespace()));
        }
        if (ctxt.isTagFile()) { // 如果是标签
            try {
                double libraryVersion = Double.parseDouble(ctxt.getTagInfo()
                        .getTagLibrary().getRequiredVersion());
                if (libraryVersion < 2.0) {
                    pageInfo.setIsELIgnored("true", null, errDispatcher, true);
                }
                if (libraryVersion < 2.1) {
                    pageInfo.setDeferredSyntaxAllowedAsLiteral("true", null,
                            errDispatcher, true);
                }
            } catch (NumberFormatException ex) {
                errDispatcher.jspError(ex);
            }
        }

        //ctxt ==  org.apache.jasper.JspCompilationContext
        ctxt.checkOutputDir();  // 检查存放java的目录是否存在，outputDir = == d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp
        String javaFileName = ctxt.getServletJavaFileName(); // d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp/index_jsp.java

        try {
            /*
             * The setting of isELIgnored changes the behaviour of the parser
             * in subtle ways. To add to the 'fun', isELIgnored can be set in
             * any file that forms part of the translation unit so setting it
             * in a file included towards the end of the translation unit can
             * change how the parser should have behaved when parsing content
             * up to the point where isELIgnored was set. Arghh!
             * Previous attempts to hack around this have only provided partial
             * solutions. We now use two passes to parse the translation unit.
             * The first just parses the directives and the second parses the
             * whole translation unit once we know how isELIgnored has been set.
             * TODO There are some possible optimisations of this process.
             */
            // Parse the file
            ParserController parserCtl = new ParserController(ctxt, this);

            // Pass 1 - the directives
            // ctxt === org.apache.jasper.JspCompilationContext
            // ctxt.getJspFile() === jspUri === "/a/b/c/file.jsp"    "/index.jsp"
            Node.Nodes directives =
                parserCtl.parseDirectives(ctxt.getJspFile()); // 解析页面指令标签
//      	<%@ page import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
//          <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %> //会跳过这个指令的解析
//          <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
//          <%@ attribute attr1="value1" attr2="value2" %>
//          <%@ variable attr1="value1" attr2="value2" %>
//      	<jsp:directive.page import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
//          <jsp:directive.taglib uri="http://cn.java/jsp/core" prefix="myprefix" />
//          <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
//          <jsp:directive.attribute attr1="value1" attr2="value2" />
//          <jsp:directive.variable attr1="value1" attr2="value2" />
            
            Validator.validateDirectives(this, directives); // 页面指定中配置的相关信息设置到org.apache.jasper.compiler.PageInfo中

            // Pass 2 - the whole translation unit
            pageNodes = parserCtl.parse(ctxt.getJspFile()); // 解析非指令的标签

            // Leave this until now since it can only be set once - bug 49726
            if (pageInfo.getContentType() == null &&
                    jspProperty.getDefaultContentType() != null) {
                pageInfo.setContentType(jspProperty.getDefaultContentType());
            }

            // ctxt === org.apache.jasper.JspCompilationContext
            if (ctxt.isPrototypeMode()) {
                // generate prototype .java file for the tag file
                try (ServletWriter writer = setupContextWriter(javaFileName)) {
                    Generator.generate(writer, this, pageNodes);
                    return null;
                }
            }

            // Validate and process attributes - don't re-validate the
            // directives we validated in pass 1
            Validator.validateExDirectives(this, pageNodes); // 校验其他节点，不再校验在第一步已经校验的

            if (log.isDebugEnabled()) {
                t2 = System.currentTimeMillis();
            }

            // Collect page info
            Collector.collect(this, pageNodes);

            // 把标签对应的tag文件转成class文件，并设置到node的tagHandlerClass属性中，如： *.tag --->  *.java ---> *.class
            // 如"org.apache.jsp.tag.web.dir1.tag1_tag"
            // 如: tagClazz = "org.apache.jsp.tag.web.dir1.tag1_tag.class"
            // 在pageInfo 添加依赖的 .tag文件 "/WEB-INFO/tags/dir1/tag1.tag"
            // Compile (if necessary) and load the tag files referenced in
            // this compilation unit.
            tfp = new TagFileProcessor();
            tfp.loadTagFiles(this, pageNodes); // 取出标签对应的“标签文件 *.tag”，并解析“标签文件 *.tag”成class文件，设置到node的tagHandlerClass属性中

            if (log.isDebugEnabled()) {
                t3 = System.currentTimeMillis();
            }

            // Determine which custom tag needs to declare which scripting vars
            ScriptingVariabler.set(pageNodes, errDispatcher);

            // Optimizations by Tag Plugins   标签插件管理器
            // options === org.apache.jasper.EmbeddedServletOptions
            // tagPluginManager === org.apache.jasper.compiler.TagPluginManager
            TagPluginManager tagPluginManager = options.getTagPluginManager();  // 取出标签对应的“标签插件”，并调用“标签插件”的doTag
            tagPluginManager.apply(pageNodes, errDispatcher, pageInfo);

            // Optimization: concatenate contiguous template texts.
            TextOptimizer.concatenate(this, pageNodes);

            // Generate static function mapper codes.
            ELFunctionMapper.map(pageNodes); // EL表达式处理代码，如果有使用EL函数，要声明静态代码块 static{ ... } 

            // generate servlet .java file 开始迭代“解析出来的节点树”变成“java代码”写入 java文件
            try (ServletWriter writer = setupContextWriter(javaFileName)) {  // javaFileName ===  d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp/index_jsp.java
                Generator.generate(writer, this, pageNodes);  // 生成java的Servlet.java文件
            }

            // The writer is only used during the compile, dereference
            // it in the JspCompilationContext when done to allow it
            // to be GC'd and save memory.
            ctxt.setWriter(null);

            if (log.isDebugEnabled()) {
                t4 = System.currentTimeMillis();
                log.debug("Generated " + javaFileName + " total=" + (t4 - t1)
                        + " generate=" + (t4 - t3) + " validate=" + (t2 - t1));
            }

        } catch (Exception e) {
            // Remove the generated .java file
            File file = new File(javaFileName);
            if (file.exists()) {
                if (!file.delete()) {
                    log.warn(Localizer.getMessage(
                            "jsp.warning.compiler.javafile.delete.fail",
                            file.getAbsolutePath()));
                }
            }
            throw e;
        }

        // JSR45 Support
        if (!options.isSmapSuppressed()) {
            smapStr = SmapUtil.generateSmap(ctxt, pageNodes);
        }

        // If any proto type .java and .class files was generated,
        // the prototype .java may have been replaced by the current
        // compilation (if the tag file is self referencing), but the
        // .class file need to be removed, to make sure that javac would
        // generate .class again from the new .java file just generated.
        tfp.removeProtoTypeFiles(ctxt.getClassFileName());

        return smapStr;
    }

    private ServletWriter setupContextWriter(String javaFileName)
            throws FileNotFoundException, JasperException {
        ServletWriter writer;
        // Setup the ServletWriter
        String javaEncoding = ctxt.getOptions().getJavaEncoding();
        OutputStreamWriter osw = null;

        try {
            osw = new OutputStreamWriter(
                    new FileOutputStream(javaFileName), javaEncoding);
        } catch (UnsupportedEncodingException ex) {
            errDispatcher.jspError("jsp.error.needAlternateJavaEncoding",
                    javaEncoding);
        }

        writer = new ServletWriter(new PrintWriter(osw));
        // org.apache.jasper.JspCompilationContext
        ctxt.setWriter(writer);
        return writer;
    }

    /**
     * Servlet compilation. This compiles the generated sources into
     * Servlets.
     * @param smap The SMAP files for source debugging
     * @throws FileNotFoundException Source files not found
     * @throws JasperException Compilation error
     * @throws Exception Some other error
     */
    protected abstract void generateClass(String[] smap)
            throws FileNotFoundException, JasperException, Exception;

    /**
     * Compile the jsp file from the current engine context.
     * @throws FileNotFoundException Source files not found
     * @throws JasperException Compilation error
     * @throws Exception Some other error
     */
    public void compile() throws FileNotFoundException, JasperException,
            Exception {
        compile(true);//!!!
    }

    /**
     * Compile the jsp file from the current engine context. As an side- effect,
     * tag files that are referenced by this page are also compiled.
     *
     * @param compileClass
     *            If true, generate both .java and .class file If false,
     *            generate only .java file
     * @throws FileNotFoundException Source files not found
     * @throws JasperException Compilation error
     * @throws Exception Some other error
     */
    public void compile(boolean compileClass) throws FileNotFoundException,
            JasperException, Exception {
        compile(compileClass, false);//!!!
    }

    /**
     * Compile the jsp file from the current engine context. As an side- effect,
     * tag files that are referenced by this page are also compiled.
     *
     * @param compileClass
     *            If true, generate both .java and .class file If false,
     *            generate only .java file
     * @param jspcMode
     *            true if invoked from JspC, false otherwise
     * @throws FileNotFoundException Source files not found
     * @throws JasperException Compilation error
     * @throws Exception Some other error
     */
    public void compile(boolean compileClass, boolean jspcMode)
            throws FileNotFoundException, JasperException, Exception {
    	// compileClass == true 
    	// jspcMode == false 
        if (errDispatcher == null) {
            this.errDispatcher = new ErrorDispatcher(jspcMode);
        }

        try {
        	// ------生成java文件---------
            String[] smap = generateJava(); // 生成java文件!!!!
            File javaFile = new File(ctxt.getServletJavaFileName());
            Long jspLastModified = ctxt.getLastModified(ctxt.getJspFile());
            javaFile.setLastModified(jspLastModified.longValue());
            // ------编译成class文件-------
            if (compileClass) { // 编译成class文件
                generateClass(smap);
                // Fix for bugzilla 41606
                // Set JspServletWrapper.servletClassLastModifiedTime after successful compile
                File targetFile = new File(ctxt.getClassFileName());
                if (targetFile.exists()) {
                    targetFile.setLastModified(jspLastModified.longValue());
                    if (jsw != null) {
                        jsw.setServletClassLastModifiedTime(
                                jspLastModified.longValue());
                    }
                }
            }
        } finally {
            if (tfp != null && ctxt.isPrototypeMode()) {
                tfp.removeProtoTypeFiles(null);
            }
            // Make sure these object which are only used during the
            // generation and compilation of the JSP page get
            // dereferenced so that they can be GC'd and reduce the
            // memory footprint.
            tfp = null;
            errDispatcher = null;
            pageInfo = null;

            // Only get rid of the pageNodes if in production.
            // In development mode, they are used for detailed
            // error messages.
            // http://bz.apache.org/bugzilla/show_bug.cgi?id=37062
            if (!this.options.getDevelopment()) {
                pageNodes = null;
            }

            if (ctxt.getWriter() != null) {
                ctxt.getWriter().close();
                ctxt.setWriter(null);
            }
        }
    }

    /**
     * This is a protected method intended to be overridden by subclasses of
     * Compiler. This is used by the compile method to do all the compilation.
     * @return <code>true</code> if the source generation and compilation
     *  should occur
     */
    public boolean isOutDated() {
        return isOutDated(true);
    }

    /**
     * Determine if a compilation is necessary by checking the time stamp of the
     * JSP page with that of the corresponding .class or .java file. If the page
     * has dependencies, the check is also extended to its dependents, and so
     * on. This method can by overridden by a subclasses of Compiler.
     *
     * @param checkClass
     *            If true, check against .class file, if false, check against
     *            .java file.
     * @return <code>true</code> if the source generation and compilation
     *  should occur
     */
    public boolean isOutDated(boolean checkClass) {

    	// jsw === org.apache.jasper.servlet.JspServletWrapper
    	// ctxt.getOptions() == org.apache.jasper.EmbeddedServletOptions
    	// ctxt === org.apache.jasper.JspCompilationContext
        if (jsw != null
                && (ctxt.getOptions().getModificationTestInterval() > 0)) { // getModificationTestInterval() === 4 
        	// 0+4 > System.currentTimeMillis()
            if (jsw.getLastModificationTest()
                    + (ctxt.getOptions().getModificationTestInterval() * 1000) > System 
                    .currentTimeMillis()) {
                return false;
            }
            // org.apache.jasper.servlet.JspServletWrapper
            jsw.setLastModificationTest(System.currentTimeMillis()); // 设置修改时间
        }

        // Test the target file first. Unless there is an error checking the
        // last modified time of the source (unlikely) the target is going to
        // have to be checked anyway. If the target doesn't exist (likely during
        // startup) this saves an unnecessary check of the source.
        File targetFile;
        if (checkClass) {
        	// ctxt === org.apache.jasper.JspCompilationContext
        	// ctxt.getClassFileName() === d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp/index_jsp.class
            targetFile = new File(ctxt.getClassFileName()); //!!!!
        } else {
        	// ctxt.getServletJavaFileName() === d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp/index_jsp.java
            targetFile = new File(ctxt.getServletJavaFileName());
        }
        if (!targetFile.exists()) { // 类文件不存在
            return true; //!!!!
        }
        long targetLastModified = targetFile.lastModified(); // class文件的修改时间
        if (checkClass && jsw != null) { 
            jsw.setServletClassLastModifiedTime(targetLastModified);
        }

        Long jspRealLastModified = ctxt.getLastModified(ctxt.getJspFile()); // jsp文件的修改时间
        if (jspRealLastModified.longValue() < 0) {
            // Something went wrong - assume modification
            return true;
        }

        if (targetLastModified != jspRealLastModified.longValue()) {
            if (log.isDebugEnabled()) {
                log.debug("Compiler: outdated: " + targetFile + " "
                        + targetLastModified);
            }
            return true;
        }

        // determine if source dependent files (e.g. includes using include
        // directives) have been changed.
        if (jsw == null) {
            return false;
        }

        Map<String,Long> depends = jsw.getDependants();
        if (depends == null) {
            return false;
        }

        Iterator<Entry<String,Long>> it = depends.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String,Long> include = it.next();
            try {
                String key = include.getKey();
                URL includeUrl;
                long includeLastModified = 0;
                if (key.startsWith("jar:jar:")) {
                    // Assume we constructed this correctly
                    int entryStart = key.lastIndexOf("!/");
                    String entry = key.substring(entryStart + 2);
                    try (Jar jar = JarFactory.newInstance(new URL(key.substring(4, entryStart)))) {
                        includeLastModified = jar.getLastModified(entry);
                    }
                } else {
                    if (key.startsWith("jar:") || key.startsWith("file:")) {
                        includeUrl = new URL(key);
                    } else {
                        includeUrl = ctxt.getResource(include.getKey());
                    }
                    if (includeUrl == null) {
                        return true;
                    }
                    URLConnection iuc = includeUrl.openConnection();
                    if (iuc instanceof JarURLConnection) {
                        includeLastModified =
                            ((JarURLConnection) iuc).getJarEntry().getTime();
                    } else {
                        includeLastModified = iuc.getLastModified();
                    }
                    iuc.getInputStream().close();
                }

                if (includeLastModified != include.getValue().longValue()) {
                    return true;
                }
            } catch (Exception e) {
                if (log.isDebugEnabled())
                    log.debug("Problem accessing resource. Treat as outdated.",
                            e);
                return true;
            }
        }

        return false;

    }

    /**
     * @return the error dispatcher.
     */
    public ErrorDispatcher getErrorDispatcher() {
        return errDispatcher;
    }

    /**
     * @return the info about the page under compilation
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public JspCompilationContext getCompilationContext() {
        return ctxt;
    }

    /**
     * Remove generated files
     */
    public void removeGeneratedFiles() {
    	// d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp/index_jsp.class
        removeGeneratedClassFiles(); // 删除 .class 类文件

        try {
        	// 删除java文件
        	// d:/a/b/tomcat/work/Catalina/localhost/test/org/apache/jsp/index_jsp.java
            File javaFile = new File(ctxt.getServletJavaFileName());
            if (log.isDebugEnabled())
                log.debug("Deleting " + javaFile);
            if (javaFile.exists()) {
                if (!javaFile.delete()) {
                    log.warn(Localizer.getMessage(
                            "jsp.warning.compiler.javafile.delete.fail",
                            javaFile.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            // Remove as much as possible, log possible exceptions
            log.warn(Localizer.getMessage("jsp.warning.compiler.classfile.delete.fail.unknown"),
                     e);
        }
    }

    public void removeGeneratedClassFiles() {
        try {
            File classFile = new File(ctxt.getClassFileName());
            if (log.isDebugEnabled())
                log.debug("Deleting " + classFile);
            if (classFile.exists()) {
                if (!classFile.delete()) {
                    log.warn(Localizer.getMessage(
                            "jsp.warning.compiler.classfile.delete.fail",
                            classFile.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            // Remove as much as possible, log possible exceptions
            log.warn(Localizer.getMessage("jsp.warning.compiler.classfile.delete.fail.unknown"),
                     e);
        }
    }
}
