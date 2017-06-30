package cn.java.demo.data_common.repository_factory_information;

import java.util.List;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.query.QueryMethod;

import cn.java.demo.data_common.entity.FooTwoEntity;
import cn.java.demo.data_common.repository.FooTwoRepository;

public class FooTwoEntityRepositoryFactoryInformationImpl implements RepositoryFactoryInformation<FooTwoEntity, Integer> {

	@Override
	public EntityInformation<FooTwoEntity, Integer> getEntityInformation() {
		return null;
	}

	/**
	 * 工厂信息
	 */
	@Override
	public RepositoryInformation getRepositoryInformation() {
		Class<?> repositoryInterface = FooTwoRepository.class;
		Class<?> customImplementationClass = null;
		
		{
			RepositoryMetadata metadata = AbstractRepositoryMetadata.getMetadata(repositoryInterface);
			Class<?> repositoryBaseClass = SimpleJpaRepository.class;
			RepositoryInformation repositoryInformation = new DefaultRepositoryInformation(metadata,repositoryBaseClass,customImplementationClass);
			return repositoryInformation;
		}
	}

	@Override
	public PersistentEntity<?, ?> getPersistentEntity() {
		return null;
	}

	@Override
	public List<QueryMethod> getQueryMethods() {
		return null;
	}

}
