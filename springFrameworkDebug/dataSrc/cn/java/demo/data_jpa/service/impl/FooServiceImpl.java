package cn.java.demo.data_jpa.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import cn.java.demo.data_jpa.dao.repositorybyextend.FooOneDao;
import cn.java.demo.data_jpa.entity.FooOne;
import cn.java.demo.data_jpa.service.FooService;

public class FooServiceImpl implements FooService {
	
	private FooOneDao fooOneDao; 
	
	public void setFooDao(FooOneDao fooOneDao){
		this.fooOneDao = fooOneDao;
	}
	
	public void testMethod(){
		// org.springframework.data.jpa.repository.support.SimpleJpaRepository
		
		// 添加、修改
		{
			FooOne entity = new FooOne();
			fooOneDao.save(entity);
		}
		
		// 查询
		{
			// 单条
			FooOne entity = fooOneDao.getOne(1L);
			// 列表
			List<FooOne> entityList = null;
			entityList = fooOneDao.findAll();
			// 列表 - 排序
			entityList = fooOneDao.findAll(new Sort(new Order(Direction.ASC,"id"),new Order(Direction.DESC,"username")));
		}
		
		//　删除
		{
			fooOneDao.delete(1L);
		}
		
	}
}
