package cn.java.demo.data_jpa.service.impl;

import cn.java.demo.data_jpa.dao_alias_repository.FooDao;
import cn.java.demo.data_jpa.entity.Foo;
import cn.java.demo.data_jpa.service.FooService;

public class FooServiceImpl implements FooService {
	private FooDao fooDao; 
	public void setFooDao(FooDao fooDao){
		this.fooDao = fooDao;
	}
	public void testMethod(){
		Foo foo = fooDao.findByFooId(1L);
		System.out.println(foo);
	}
}
