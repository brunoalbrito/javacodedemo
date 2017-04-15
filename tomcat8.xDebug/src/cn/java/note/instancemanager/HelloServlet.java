package cn.java.note.instancemanager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.xml.ws.WebServiceRef;

public class HelloServlet {
	
	@Resource(name="java:comp/env/name1")
	private String field1;
	
	@EJB(name="java:comp/env/name2")
	private String field2;
	
	@WebServiceRef(name="java:comp/env/name3")
	private String field3;
	
	@PersistenceContext(name="java:comp/env/name4")
	private String field4;
	
	@PersistenceUnit(name="java:comp/env/name5")
	private String field5;
	
	public void doPost(){
		
	}
	
	@PostConstruct
	public void postConstruct(){
		
	}
	
	@PreDestroy
	public void preDestroy(){
		
	}
}
