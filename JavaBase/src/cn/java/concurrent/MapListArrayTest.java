package cn.java.concurrent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class MapListArrayTest {

	public static void main(String[] args) {
	}


	private static class ListTest {
		
		public static void testCopyOnWriteArrayList() {
			CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
			copyOnWriteArrayList.add("element1");
			copyOnWriteArrayList.add("element2");
			for(String element : copyOnWriteArrayList){
				System.out.println(element);
			}
			copyOnWriteArrayList.remove("element2");
		}
		
	}
	
	private static class SetTest {
		
		/**
		 * 使用SkipList算法（跳表算法）
		 */
		public static void testConcurrentSkipListSet() {
			ConcurrentSkipListSet<String> concurrentSkipListSet = new ConcurrentSkipListSet();
			concurrentSkipListSet.add("element1");
			concurrentSkipListSet.add("element2");
			for(String element : concurrentSkipListSet){
				System.out.println(element);
			}
			concurrentSkipListSet.remove("element2");
		}
		
		public static void testCopyOnWriteArraySet() {
			CopyOnWriteArraySet<String> copyOnWriteArraySet = new CopyOnWriteArraySet();
			copyOnWriteArraySet.add("element1");
			copyOnWriteArraySet.add("element2");
			for(String element : copyOnWriteArraySet){
				System.out.println(element);
			}
			copyOnWriteArraySet.remove("element2");
		}
	}
	
	private static class MapTest {

		public static void testConcurrentHashMap() {
			ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
			concurrentHashMap.put("key1", "key1Value");
			concurrentHashMap.get("key1");
			for (Entry<String,String> entry : (Set<Map.Entry<String,String>>)concurrentHashMap.entrySet()) {
				System.out.println("Key = "+entry.getKey()+",Value = "+entry.getValue());
			}
			concurrentHashMap.remove("key1");
		}

		/**
		 * 使用SkipList算法（跳表算法）
		 */
		public static void testConcurrentSkipListMap() {
			ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap();
			concurrentSkipListMap.put("key1", "key1Value");
			concurrentSkipListMap.get("key1");
			for (Entry<String,String> entry : (Set<Map.Entry<String,String>>)concurrentSkipListMap.entrySet()) {
				System.out.println("Key = "+entry.getKey()+",Value = "+entry.getValue());
			}
			concurrentSkipListMap.remove("key1");
		}
	}


}
