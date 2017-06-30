package cn.java.demo.data_common.unmarshaller;

import java.io.IOException;

import javax.xml.transform.Source;

import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

public class JibxUnmarshaller implements Unmarshaller {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object unmarshal(Source source) throws IOException, XmlMappingException {
		// TODO Auto-generated method stub
		return null;
	}

}
