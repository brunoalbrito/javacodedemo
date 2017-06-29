package cn.java.demo.beantag.internal;

import java.util.List;

import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class SpringFactoriesLoaderTest {

	public static void main(String[] args) {
		List<String> springFactories = SpringFactoriesLoader.loadFactoryNames(RepositoryFactorySupport.class, SpringFactoriesLoaderTest.class.getClassLoader());
		System.out.println(springFactories);
	}

}
