package cn.java.dubbo.note.myext;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;

public interface MyExtensionLoaderInterface   {
	@Adaptive
	public void method1();
	public void method2();
	public void method3(URL arg0);
	public void method4(MyExtension1 arg0,URL arg1);
	public Object method5(MyExtension1 arg0,URL arg1,com.alibaba.dubbo.rpc.Invocation arg2);
}
