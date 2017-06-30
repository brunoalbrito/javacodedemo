package cn.java.demo.oxmtag;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import cn.java.demo.oxmtag.clazztobebound.FooOneClazz;

public class Test {
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/oxmtag/applicationContext.xml");
		
		{
			Jaxb2Marshaller jaxb2Marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller0");
			JAXBContext jaxbContext = jaxb2Marshaller.getJaxbContext(); // java对象和xml文档的转换器
			
			System.out.println("--------java对象转成xml文件----------------");
			String xmlStrTemp = "";
			{
				Writer writer = new StringWriter();
				Result result = new StreamResult(writer);
				Marshaller marshaller = jaxbContext.createMarshaller();
				FooOneClazz graph = new FooOneClazz();
				graph.setFooId(1);
				graph.setFooName("fooName1");
				marshaller.marshal(graph, result);
				xmlStrTemp = writer.toString();
				System.out.println(writer);
			}
			
			System.out.println("----------xml文件转成java对象---------------");
			{
				Source source = new StreamSource(new StringReader(xmlStrTemp));
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				Object object = unmarshaller.unmarshal(source);
				if(object instanceof FooOneClazz){
					FooOneClazz fooClazz = (FooOneClazz)object;
				}
				System.out.println(object);
			}
		}
		
		
		
	}
}
