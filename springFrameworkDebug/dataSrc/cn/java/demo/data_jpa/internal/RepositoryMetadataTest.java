package cn.java.demo.data_jpa.internal;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;

public class RepositoryMetadataTest {
	public static class FooEntity {

	}

	public static interface UserRepository extends Repository<FooEntity, Integer> {

	}

	public static interface PersonRepository {

	}

	public static void main(String[] args) {
		printMetadataInfo(UserRepository.class);
		printMetadataInfo(PersonRepository.class);
	}
	
	public static void printMetadataInfo(Class repositoryInterface) {
		{
			RepositoryMetadata metadata = AbstractRepositoryMetadata.getMetadata(repositoryInterface);

			Class<?> domainType = metadata.getDomainType();
			System.out.println("domainType = " + domainType);

			{
				Class<? extends Annotation> annotationType = Entity.class;
				if (AnnotationUtils.findAnnotation(domainType, annotationType) != null) {
				}
			}
		}
	}

	

}
