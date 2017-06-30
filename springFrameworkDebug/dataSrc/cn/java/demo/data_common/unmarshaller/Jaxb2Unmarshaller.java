package cn.java.demo.data_common.unmarshaller;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.Source;

import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import cn.java.demo.oxmtag.clazztobebound.FooOneClazz;

public class Jaxb2Unmarshaller implements Unmarshaller {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public Object unmarshal(Source source) throws IOException, XmlMappingException {
		Class<?>[] classesToBeBound = {FooOneClazz.class};
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
			javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Object object = unmarshaller.unmarshal(source);
			return unmarshaller.unmarshal(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
