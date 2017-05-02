package cn.java.debug;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Locale;

import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.eclipse.jetty.xml.XmlParser;
import org.xml.sax.InputSource;

public class XmlParserDebug {

	public static void main(String[] args) throws Exception {
		new XmlParserDebug().test();
	}
	
	public void test() throws Exception {
		File file = new File("debugSrc/cn/java/debug/etc/jetty.xml");
		if(!file.exists()){
			throw new RuntimeException("file is not exist.");
		}
		XmlParser parser = getXmlParser();
		InputSource source = new InputSource(new FileInputStream(file));
		XmlParser.Node root = parser.parse(source);
		String dtd = parser.getDTD();
		System.out.println("dtd = " + dtd);
		
		nodeClass(root);
		
		{
			XmlParser.Node cfg = root;
			
			int i = 0;
		    // Object already constructed so skip any arguments
            for (; i < cfg.size(); i++)
            {
                Object o = cfg.get(i);
                if (o instanceof String)
                    continue;
                XmlParser.Node node = (XmlParser.Node)o;
                if ("Arg".equals(node.getTag())) // 参数标签
                {
                    continue;
                }
                break;
            }
            
            // Process real arguments
            for (; i < cfg.size(); i++)
            {
                Object o = cfg.get(i);
                if (o instanceof String)
                    continue;
                XmlParser.Node node = (XmlParser.Node)o;
                String tag = node.getTag(); // 子标签
                switch (tag)
                {
                    case "Set":
                        set(/*obj, */node);
                        break;
                    case "Put":
                        put(/*obj, */node);
                        break;
                    case "Call":
                        call(/*obj, */node);
                    default:
                       System.out.println("tag : " + tag );
                }
            }
		}
	}
	
	private void set(/*Object obj, */XmlParser.Node node) throws Exception
    {
		String attr = node.getAttribute("name");
        String name = "set" + attr.substring(0,1).toUpperCase(Locale.ENGLISH) + attr.substring(1);
        System.out.println(name);
    }
	
	private void put(/*Object obj, */XmlParser.Node node) throws Exception
    {
		
    }

	private Object call(/*Object obj,*/ XmlParser.Node node) throws Exception
    {
		return null;
    }
	
	private static Class<?> nodeClass(XmlParser.Node node) 
	{
		String className = node.getAttribute("class");
		if (className == null)
			System.out.println("className = " + className);
		return null;
	}

	

	public static XmlParser getXmlParser() {
		XmlParser parser = new XmlParser();
		URL config60 = Loader.getResource(XmlConfiguration.class, "org/eclipse/jetty/xml/configure_6_0.dtd");
		URL config76 = Loader.getResource(XmlConfiguration.class,"org/eclipse/jetty/xml/configure_7_6.dtd");
		URL config90 = Loader.getResource(XmlConfiguration.class,"org/eclipse/jetty/xml/configure_9_0.dtd");
		parser.redirectEntity("configure.dtd",config90);
		parser.redirectEntity("configure_1_0.dtd",config60);
		parser.redirectEntity("configure_1_1.dtd",config60);
		parser.redirectEntity("configure_1_2.dtd",config60);
		parser.redirectEntity("configure_1_3.dtd",config60);
		parser.redirectEntity("configure_6_0.dtd",config60);
		parser.redirectEntity("configure_7_6.dtd",config76);
		parser.redirectEntity("configure_9_0.dtd",config90);

		parser.redirectEntity("http://jetty.mortbay.org/configure.dtd",config90);
		parser.redirectEntity("http://jetty.eclipse.org/configure.dtd",config90);
		parser.redirectEntity("http://www.eclipse.org/jetty/configure.dtd",config90);

		parser.redirectEntity("-//Mort Bay Consulting//DTD Configure//EN",config90);
		parser.redirectEntity("-//Jetty//Configure//EN",config90);
		return parser;
	}

}
