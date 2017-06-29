package cn.java.demo.data_common.dao_alias_repository;

import org.springframework.data.repository.Repository;

import cn.java.demo.data_common.entity.FooEntity;

public interface FooEntityDao extends Repository<FooEntity, Long> {
	public FooEntity save(FooEntity foo); 
	public FooEntity findByFooId(Long fooId); 
}