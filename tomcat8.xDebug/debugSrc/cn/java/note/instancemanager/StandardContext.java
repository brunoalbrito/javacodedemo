package cn.java.note.instancemanager;

import java.util.HashMap;
import java.util.Map;

public class StandardContext implements Context {

	private Map<String, String> postConstructMethods = new HashMap<>();
	private Map<String, String> preDestroyMethods = new HashMap<>();

	public Loader getLoader() {
		return null;
	}

	public boolean getPrivileged() {
		return false;
	}

	public boolean getIgnoreAnnotations() {
		return false;
	}

	public Map<String, String> findPostConstructMethods() {
		return postConstructMethods;
	}

	public Map<String, String> findPreDestroyMethods() {
		return preDestroyMethods;
	}

	
	public void setPostConstructMethods(Map<String, String> postConstructMethods) {
		this.postConstructMethods = postConstructMethods;
	}

	public void setPreDestroyMethods(Map<String, String> preDestroyMethods) {
		this.preDestroyMethods = preDestroyMethods;
	}
	
	
	

}
