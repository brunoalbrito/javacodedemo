package cn.java.demo.data_common.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.java.demo.data_common.entity.FooOneEntity;

public interface FooOnePagingAndSortingRepository extends PagingAndSortingRepository<FooOneEntity, Integer> {

}
