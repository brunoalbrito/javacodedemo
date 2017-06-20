package cn.java.demo.beantag.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.util.ClassUtils;

import cn.java.beannote.all.ClassUtilsTest;

public class InternalUtils_ClassUtils {
	
	public static void testClassUtils(AbstractRefreshableConfigApplicationContext context) {
		
		// 获取类缩略名、获取类的包名
		{
			System.out.println(ClassUtils.getShortName(FooService.class));
			System.out.println(ClassUtils.getPackageName(FooService.class));
		}
		
		
		// 检查是否支持接口“javax.transaction.Transactional”
		{
			boolean jta12Present = ClassUtils.isPresent("javax.transaction.Transactional", AnnotationTransactionAttributeSource.class.getClassLoader());
		}
		
		// 加载类
		{
			try {
				ClassUtils.forName("cn.java.demo.beantag.internal.InternalUtils_ClassUtils", null);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (LinkageError e) {
				e.printStackTrace();
			}
		}
		
		// 获取所有接口
		{
			Class<?> targetClass = FooService.class;
			Set<Class<?>> classes = new LinkedHashSet<Class<?>>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
			classes.add(targetClass);	
		}
	}
	
	public static class FooService {
		public void method0(){
			
		}
	}
}
