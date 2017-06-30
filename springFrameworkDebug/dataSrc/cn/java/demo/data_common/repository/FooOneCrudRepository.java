package cn.java.demo.data_common.repository;

import org.springframework.data.repository.CrudRepository;

import cn.java.demo.data_common.entity.FooOneEntity;

public interface FooOneCrudRepository extends CrudRepository<FooOneEntity, Integer> {

}
