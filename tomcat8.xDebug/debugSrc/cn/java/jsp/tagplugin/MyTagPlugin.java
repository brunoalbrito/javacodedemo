package cn.java.jsp.tagplugin;

import org.apache.jasper.compiler.tagplugin.TagPlugin;
import org.apache.jasper.compiler.tagplugin.TagPluginContext;

public class MyTagPlugin implements TagPlugin {
	
//	<tag-plugins>
//		<tag-class></tag-class>
//		<plugin-class>cn.java.jsp.tagplugin.MyTagPlugin</plugin-class>
//	<tag-plugins>
	
	@Override
	public void doTag(TagPluginContext tagPluginContext) {
		// tagPluginContext === org.apache.jasper.compiler.TagPluginManager.TagPluginContextImpl
//		tagPluginContext.getConstantAttribute("attr1");
	}

}
