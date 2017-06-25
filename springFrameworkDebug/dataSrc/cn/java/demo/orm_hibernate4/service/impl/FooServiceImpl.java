package cn.java.demo.orm_hibernate4.service.impl;
import cn.java.demo.orm_hibernate4.dao.FooDao;
import cn.java.demo.orm_hibernate4.service.FooService;
public class FooServiceImpl implements FooService{
	
	private FooDao fooDao;
	
	public FooDao getFooDao() {
		return fooDao;
	}

	public void setFooDao(FooDao fooDao) {
		this.fooDao = fooDao;
	}

	public void testMethod() {
		int affectRawCount = fooDao.insert();
	}
	
}
