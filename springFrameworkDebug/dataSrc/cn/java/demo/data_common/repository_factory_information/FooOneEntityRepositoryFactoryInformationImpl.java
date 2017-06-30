package cn.java.demo.data_common.repository_factory_information;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultCrudMethods;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.query.QueryMethod;

import cn.java.demo.data_common.entity.FooOneEntity;
import cn.java.demo.data_common.repository.FooOneRepository;
import cn.java.demo.data_common.repository.impl.FooOneRepositoryImpl;

public class FooOneEntityRepositoryFactoryInformationImpl implements RepositoryFactoryInformation<FooOneEntity, Integer> {

	@Override
	public EntityInformation<FooOneEntity, Integer> getEntityInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public RepositoryInformation getRepositoryInformation() {
		Class<?> repositoryInterface = FooOneRepository.class;
		Class<?> customImplementationClass = FooOneRepositoryImpl.class;
		
		{
			RepositoryMetadata metadata = AbstractRepositoryMetadata.getMetadata(repositoryInterface);
			Class<?> repositoryBaseClass = SimpleJpaRepository.class;
			RepositoryInformation repositoryInformation = new DefaultRepositoryInformation(metadata,repositoryBaseClass,customImplementationClass);
			
			{
				CrudMethods methods = repositoryInformation.getCrudMethods(); // crud方法列表
				
				{
					if(methods instanceof DefaultCrudMethods);
					
					if(false){
						
						// 添加、修改
						System.out.println("methods.getSaveMethod() = " + methods.getSaveMethod());
						
						// 删除
						System.out.println("methods.getDeleteMethod() = " + methods.getDeleteMethod());
						
						// 查询
						System.out.println("methods.getFindOneMethod() = " + methods.getFindOneMethod());
						System.out.println("methods.getFindAllMethod() = " + methods.getFindAllMethod());
					}
				}
				Class<? extends Serializable> idType = repositoryInformation.getIdType(); // 主键类型
			}
			return repositoryInformation;
		}
	}

	@Override
	public PersistentEntity<?, ?> getPersistentEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QueryMethod> getQueryMethods() {
		// TODO Auto-generated method stub
		return null;
	}

}
