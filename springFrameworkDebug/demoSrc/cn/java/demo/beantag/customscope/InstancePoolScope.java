package cn.java.demo.beantag.customscope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.Assert;

/**
 * 自定义作用域
 * @author zhouzhian
 */
public class InstancePoolScope implements Scope {
	protected final Log logger = LogFactory.getLog(getClass());
	
	/** 配置的名称 */
	private String scopeNameX = "Unknow";
	public InstancePoolScope(){
	}
	public InstancePoolScope(String scopeNameX){
		this.scopeNameX = scopeNameX;
	}
	public String getScopeNameX(){
		return scopeNameX;
	}
	public int getInstanceCounter(String beanName){
		synchronized (this.poolObjects) {
			AtomicInteger atomicInteger = this.poolObjectsCounter.get(beanName);
			return atomicInteger.get();
		}
	}
	private int maxObjectsCountPerBeanName = 3;
	
	
	public int getMaxObjectsCountPerBeanName() {
		return maxObjectsCountPerBeanName;
	}
	public void setMaxObjectsCountPerBeanName(int maxObjectsCountPerBeanName) {
		this.maxObjectsCountPerBeanName = maxObjectsCountPerBeanName;
	}
	
	/** Cache of singleton objects: bean name --> bean instance */
	private final Map<String, List> poolObjects = new ConcurrentHashMap<String, List>(256);
	
	private final Map<String, AtomicInteger>  poolObjectsCounter = new ConcurrentHashMap<String, AtomicInteger>(256);
	/**
	 * Internal marker for a null singleton object:
	 * used as marker value for concurrent Maps (which don't support null values).
	 */
	protected static final Object NULL_OBJECT = new Object();
	
	@Override
	public Object get(String beanName, ObjectFactory<?> objectFactory) {
		Assert.notNull(beanName, "'beanName' must not be null");
		synchronized (this.poolObjects) {
			List beanList = this.poolObjects.get(beanName);
			AtomicInteger atomicInteger = this.poolObjectsCounter.get(beanName);
			Object beanObject = null;
			if (beanList == null) {
				beanList = new ArrayList<>();
				atomicInteger = new AtomicInteger(0);
				this.poolObjects.put(beanName, beanList);
				this.poolObjectsCounter.put(beanName, atomicInteger);
			}
			if ((beanList.size()<=0 ) || (beanList.size()<this.maxObjectsCountPerBeanName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
				}
				boolean newBeanObject = false;
				try {
					beanObject = objectFactory.getObject();
					if(beanObject instanceof InstancePoolScopeAware){ // 如果实现了感知接口，注入自己
						((InstancePoolScopeAware)beanObject).setInstancePoolScope(this);
						((InstancePoolScopeAware)beanObject).setInstanceInfo(System.currentTimeMillis() + " - " + atomicInteger.get());
					}
					newBeanObject = true;
				}
				catch (IllegalStateException ex) {
					// Has the singleton object implicitly appeared in the meantime ->
					// if yes, proceed with it since the exception indicates that state.
					if (beanList.size()<=0) {
						throw ex;
					}
				}
				catch (BeanCreationException ex) {
					throw ex;
				}
				finally {
				}
				if (newBeanObject) {
					atomicInteger.addAndGet(1);
					beanList.add(beanObject);
				}
			}
			else{
				beanObject = beanList.get((atomicInteger.get() % beanList.size()));
			}
			return (beanObject != NULL_OBJECT ? beanObject : null);
		}
	}

	@Override
	public Object remove(String beanName) {
		return null;
	}

	@Override
	public void registerDestructionCallback(String beanName, Runnable callback) {
		
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}
	
	@Override
	public String getConversationId() {
		return null;
	}
	

}
