package cn.java.demo.contexttag.annotation;

import java.io.IOException;

import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * 
 * @author zhouzhian
 *
 */
public class FooTypeFilter extends AbstractTypeHierarchyTraversingFilterMock {

	protected FooTypeFilter(boolean considerInherited, boolean considerInterfaces) {
		super(considerInherited, considerInterfaces);
	}

	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
//		if(metadataReader instanceof org.springframework.core.type.classreading.SimpleMetadataReader){ // 类的元信息读取器
//		}
		if(metadataReaderFactory instanceof CachingMetadataReaderFactory){
			
		}
		metadataReader.getAnnotationMetadata(); // 获取注解信息
		return super.match(metadataReader, metadataReaderFactory);
	}
}
