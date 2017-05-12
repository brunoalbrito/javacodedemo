package cn.java.note.jndi.debug;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Test {

	public static void main(String[] args) throws Exception {
		/*
		  javax.naming.Context.STATE_FACTORIES
		 */
		testCompositeName();
	}

	public static void testInitialContext() throws Exception {
		Context ctx = new InitialContext();  
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mybatis");
	}
	
	public static void testCompositeName() throws Exception {
		Name name = new CompositeName("comp");
		while ((!name.isEmpty()) && (name.get(0).length() == 0))
			name = name.getSuffix(1);
		if (name.isEmpty())
			throw new NamingException("namingContext.invalidName");
		System.out.println(name);
	}

}
