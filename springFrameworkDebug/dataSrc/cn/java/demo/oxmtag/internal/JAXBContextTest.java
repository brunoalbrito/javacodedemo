package cn.java.demo.oxmtag.internal;

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

import cn.java.demo.oxmtag.clazztobebound.FooOneClazz;
import cn.java.demo.oxmtag.clazztobebound.FooTwoClazz;

public class JAXBContextTest {

	public static void main(String[] args) {
		Class<?>[] classesToBeBound = {FooOneClazz.class,FooTwoClazz.class};
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
			
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
