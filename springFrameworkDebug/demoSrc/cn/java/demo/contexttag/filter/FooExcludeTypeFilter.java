package cn.java.demo.contexttag.filter;

import java.io.IOException;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * 
 * @author zhouzhian
 *
 */
public class FooExcludeTypeFilter extends AbstractTypeHierarchyTraversingFilterMock {

	protected FooExcludeTypeFilter() {
		this(false,false);
	}
	
	protected FooExcludeTypeFilter(boolean considerInherited, boolean considerInterfaces) {
		super(considerInherited, considerInterfaces);
	}

	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		
		if(true){
			return false;
		}
		
		
//		if(metadataReader instanceof org.springframework.core.type.classreading.SimpleMetadataReader){ // 类的元信息读取器
			ClassMetadata metadata = metadataReader.getClassMetadata();
			metadata.getClassName();
			if (metadata.hasSuperClass()) {
				metadata.getSuperClassName();
			}
			for (String ifc : metadata.getInterfaceNames()) {
				
			}
//		}
		if(metadataReaderFactory instanceof CachingMetadataReaderFactory){
			
		}
		metadataReader.getAnnotationMetadata(); // 获取注解信息
		return super.match(metadataReader, metadataReaderFactory);
	}
}
