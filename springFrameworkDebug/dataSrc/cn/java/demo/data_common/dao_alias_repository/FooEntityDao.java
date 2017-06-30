package cn.java.demo.data_common.dao_alias_repository;

import org.springframework.data.repository.Repository;

import cn.java.demo.data_common.entity.FooOneEntity;

public interface FooEntityDao extends Repository<FooOneEntity, Long> {
	public FooOneEntity save(FooOneEntity foo); 
	public FooOneEntity findByFooId(Long fooId); 
}