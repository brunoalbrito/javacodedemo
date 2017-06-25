package cn.java.demo.data_cassandra.repository;

import java.util.List;

import cn.java.demo.data_cassandra.entity.Person;
import cn.java.demo.data_cassandra.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	// 统计
	Long countByLastname(String lastname);

	// 删除
	Long deleteByLastname(String lastname);

	List<User> removeByLastname(String lastname);

	// 查询
	List<Person> findByLastname(String lastname);
}