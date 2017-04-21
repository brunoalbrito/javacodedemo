package cn.java.demo.beantag.customscope;

import org.springframework.beans.factory.Aware;

public interface InstancePoolScopeAware extends Aware {
	public void setInstancePoolScope(InstancePoolScope scope);
	public void setInstanceInfo(String instanceInfo);
}
