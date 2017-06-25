package cn.java.demo.data_common.dao_alias_repository;

import org.springframework.data.repository.Repository;

import cn.java.demo.data_common.entity.Foo;

public interface FooDao extends Repository<Foo, Long> {
	public Foo save(Foo foo); 
	public Foo findByFooId(Long fooId); 
}