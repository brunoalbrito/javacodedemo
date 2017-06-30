package cn.java.demo.data_common.unmarshaller;

import java.io.IOException;

import javax.xml.transform.Source;

import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

public class XmlbeansUnmarshaller implements Unmarshaller {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public Object unmarshal(Source source) throws IOException, XmlMappingException {
		return null;
	}

}
