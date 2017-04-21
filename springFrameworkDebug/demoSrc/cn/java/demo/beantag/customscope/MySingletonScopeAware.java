package cn.java.demo.beantag.customscope;

import org.springframework.beans.factory.Aware;

public interface MySingletonScopeAware extends Aware {
	public void setMySingletonScope(MySingletonScope scope);
}
