package cn.java.demo.mock.mytag.schemas;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleSaxErrorHandler implements ErrorHandler {

	/**
	 */
	public SimpleSaxErrorHandler() {
	}

	@Override
	public void warning(SAXParseException ex) throws SAXException {
		System.out.println("Ignored XML validation warning" + ex.toString());
	}

	@Override
	public void error(SAXParseException ex) throws SAXException {
		throw ex;
	}

	@Override
	public void fatalError(SAXParseException ex) throws SAXException {
		throw ex;
	}

}
