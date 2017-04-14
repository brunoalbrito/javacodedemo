package cn.java.thread.data_access_controller;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

public class MyMockTest {

	public static void main(String[] args) {

	}

	public static class Threadx {
		ThreadLocalx.ThreadLocalMapx threadLocals = null;
		
		// 本地方法
		public static native Threadx currentThread();
	}


	public static class ThreadLocalx {

		private final int threadLocalHashCode = nextHashCode();
		private static AtomicInteger nextHashCode =
				new AtomicInteger();
		private static final int HASH_INCREMENT = 0x61c88647;
		private static int nextHashCode() {
			return nextHashCode.getAndAdd(HASH_INCREMENT);
		}

		/**
		 * 设置值
		 * @param value
		 */
		public void set(String value) {
			Threadx t = Threadx.currentThread();
			ThreadLocalMapx map = getMap(t);
			if (map != null)
				map.set(this, value);
			else
				createMap(t, value);
		}

		public String get() {
			Threadx t = Threadx.currentThread();
	        ThreadLocalMapx map = getMap(t);
	        if (map != null) {
	            ThreadLocalMapx.Entryx e = map.getEntry(this);
	            if (e != null) {
	                @SuppressWarnings("unchecked")
	                String result = (String)e.value;
	                return result;
	            }
	        }
	        return null;
	    }
		
		ThreadLocalMapx getMap(Threadx t) {
			return t.threadLocals;
		}

		void createMap(Threadx t, String firstValue) {
			t.threadLocals = new ThreadLocalMapx(this, firstValue);
		}

		public static class ThreadLocalMapx {
			private Entryx[] table;
			private static final int INITIAL_CAPACITY = 16;

			ThreadLocalMapx(ThreadLocalx firstKey, Object firstValue) {
				table = new Entryx[INITIAL_CAPACITY];
				int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
				table[i] = new Entryx(firstKey, firstValue);
			}

			/**
			 * 设置值
			 * @param value
			 */
			private void set(ThreadLocalx key, Object value) {
				Entryx[] tab = table;
				int len = tab.length;
	            int i = key.threadLocalHashCode & (len-1); // 键名由ThreadLocalx对象决定
				Entryx e = tab[i]; // 存入的是一个map结构
				e.value = value; 
			}

			/**
			 * 获取值
			 * @param key
			 * @return
			 */
			private Entryx getEntry(ThreadLocalx key) {
	            int i = key.threadLocalHashCode & (table.length - 1);
	            Entryx e = table[i];
	            return e;
			}
			
			/**
			 * 条目
			 */
			static class Entryx extends WeakReference<ThreadLocalx> {
				/** The value associated with this ThreadLocal. */
				Object value;

				Entryx(ThreadLocalx k, Object v) {
					super(k);
					value = v;
				}
			}
		}
	}

}
