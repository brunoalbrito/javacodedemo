package cn.java.demo.data_jpa.internal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class SimpleJpaRepositoryTest {

	public static void main(String[] args) {
		EntityManager entityManager = null;
		if(entityManager==null){
			throw new RuntimeException("entityManager is null.");
		}
		Class domainClass = FooEntity.class;
		
		JpaEntityInformation<?, Serializable> entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
		Object target = new SimpleJpaRepository(entityInformation,entityManager);
		if(target instanceof SimpleJpaRepository){
			SimpleJpaRepository simpleJpaRepository = (SimpleJpaRepository)target;
			
			{
				// 查询
				FooEntity entity = (FooEntity) simpleJpaRepository.findOne(1);
				List<FooEntity> fooEntityList = simpleJpaRepository.findAll();
				// 删除
				simpleJpaRepository.delete(1);
				// 修改、添加
				simpleJpaRepository.save(entity);
			}
		}
	}

	public static class FooEntity{
		private Integer fooId;
		private String fooName;
		public Integer getFooId() {
			return fooId;
		}
		public void setFooId(Integer fooId) {
			this.fooId = fooId;
		}
		public String getFooName() {
			return fooName;
		}
		public void setFooName(String fooName) {
			this.fooName = fooName;
		}
		
	}
}
