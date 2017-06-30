package cn.java.demo.data_jpa.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Entity;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.stereotype.Component;

public class RepositoryMetadataTest {
	public static class UserEntity {

	}
	public static class PersonEntity {
		
	}

	/**
	 * 使用继承的方式定义Repository
	 * @author zhouzhian
	 *
	 */
	public static interface UserRepository<T extends UserRepository> extends Repository<UserEntity, Integer> {

	}

	/**
	 * 使用注解的方式定义Repository
	 * @author zhouzhian
	 *
	 */
	@Component(value="alphaDao") // beanName="alphaDao"
	@Entity // 代表本类是repository类
	@RepositoryDefinition(domainClass=PersonEntity.class,idClass=Integer.class) // domainClass描述实体类型、idClass主键类型
	public static interface PersonRepository {

	}

	public static void main(String[] args) {
		printMetadataInfo(UserRepository.class);
		printMetadataInfo(PersonRepository.class);
	}
	
	public static void printMetadataInfo(Class repositoryInterface) {
		{
			RepositoryMetadata metadata = AbstractRepositoryMetadata.getMetadata(repositoryInterface);
			
			System.out.println("metadata.getDomainType() = " + metadata.getDomainType());
			System.out.println("metadata.getAlternativeDomainTypes() = " + metadata.getAlternativeDomainTypes());

			{
				Class<?> domainType = metadata.getDomainType();
				Class<? extends Annotation> annotationType = Entity.class;
				if (AnnotationUtils.findAnnotation(domainType, annotationType) != null) {
				}
				{
					CrudMethods methods = metadata.getCrudMethods();
					// 添加、修改
					System.out.println("methods.getSaveMethod().getName() = " + methods.getSaveMethod().getName());
					
					// 删除
					System.out.println("methods.getDeleteMethod().getName() = " + methods.getDeleteMethod().getName());
					
					// 查询
					System.out.println("methods.getFindOneMethod().getName() = " + methods.getFindOneMethod().getName());
					System.out.println("methods.getFindAllMethod().getName() = " + methods.getFindAllMethod().getName());
				}
			}
		}
	}

	

}
