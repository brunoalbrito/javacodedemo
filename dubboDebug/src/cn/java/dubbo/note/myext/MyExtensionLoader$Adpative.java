package cn.java.dubbo.note.myext;
import java.util.Arrays;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.compiler.Compiler;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
/**
 * 生成的代码
 * @author zhouzhian
 *
 */
public class MyExtensionLoader$Adpative  implements MyExtensionLoaderInterface{

	public void method1() {
		ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
	}

	public void method2() {
		throw new UnsupportedOperationException("method method2 of interface MyExtensionLoaderInterface is not adaptive method!");
	}
	
	public void method3(URL arg0) {
		if (arg0 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
	}
	
	public void method4(MyExtension1 arg0,URL arg1) {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		
		if (arg0 == null) throw new IllegalArgumentException("MyExtension1 argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("MyExtension1 argument getMethod1() == null");
		URL url = arg0.getUrl();
	}
	
	public Object method5(MyExtension1 arg0,URL arg1,com.alibaba.dubbo.rpc.Invocation arg2) {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		
		if (arg0 == null) throw new IllegalArgumentException("MyExtension1 argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("MyExtension1 argument getMethod1() == null");
		URL url = arg0.getUrl();
		
		if (arg2 == null) throw new IllegalArgumentException("invocation == null");
		String methodName = arg2.getMethodName();
		String extName = url.getMethodParameter(methodName, "protocolx", null);
		if(extName == null) 
			throw new IllegalStateException("Fail to get extension(MyExtensionLoaderInterface) name from url(" + url.toString() + ") use keys(Arrays.toString(value))");
		
		MyExtensionLoaderInterface extension = (MyExtensionLoaderInterface)ExtensionLoader.getExtensionLoader(MyExtensionLoaderInterface.class).getExtension(extName);
		
		return extension.method5(arg0,arg1,arg2);
	}

}
