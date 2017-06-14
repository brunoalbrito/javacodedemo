package cn.java.demo.beantag.internal;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.util.ClassUtils;

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
	}
	public static class FooService {
		public void method0(){
			
		}
	}
}
