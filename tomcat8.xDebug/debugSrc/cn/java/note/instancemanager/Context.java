package cn.java.note.instancemanager;

import java.util.Map;

public interface Context {
	public Loader getLoader();

	public boolean getPrivileged();

	public boolean getIgnoreAnnotations();

	public Map<String, String> findPostConstructMethods();

	public Map<String, String> findPreDestroyMethods();
	public void setPostConstructMethods(Map<String, String> postConstructMethods);
	public void setPreDestroyMethods(Map<String, String> preDestroyMethods);

}
