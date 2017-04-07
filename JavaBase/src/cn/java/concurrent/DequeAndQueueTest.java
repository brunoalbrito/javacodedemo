package cn.java.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class DequeAndQueueTest {
	
	public static void main(String[] args) {
		DequeTest.testConcurrentLinkedDeque();
		DequeTest.testLinkedBlockingDeque();
		
		QueueTest.testArrayBlockingQueue();
		QueueTest.testConcurrentLinkedQueue();
		QueueTest.testDelayQueue();
		QueueTest.testLinkedBlockingQueue();
		QueueTest.testLinkedTransferQueue();
		QueueTest.testPriorityBlockingQueue();
		QueueTest.testSynchronousQueue();
	}
	
	private static class DequeTest {
		public static void testConcurrentLinkedDeque() {
			System.out.println("-----------ConcurrentLinkedDeque---------------");
			ConcurrentLinkedDeque<String> queue = new ConcurrentLinkedDeque<String>();
			queue.addFirst("element1 inserts at front of ConcurrentLinkedDeque");
			System.out.println(queue.peekFirst());
			System.out.println(queue.pollFirst());
			
			queue.addLast("element1 inserts at end of ConcurrentLinkedDeque");
			System.out.println(queue.peekLast());
			System.out.println(queue.pollLast());
		}
		
		public static void testLinkedBlockingDeque() {
			System.out.println("-----------LinkedBlockingDeque---------------");
			LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<String>();
			queue.addFirst("element1 inserts at front of LinkedBlockingDeque");
			System.out.println(queue.peekFirst());
			System.out.println(queue.pollFirst());
			
			queue.addLast("element1 inserts at end of LinkedBlockingDeque");
			System.out.println(queue.peekLast());
			System.out.println(queue.pollLast());
		}
	}
	
	private static class QueueTest {
		
		public static void testArrayBlockingQueue() {
			System.out.println("-----------ArrayBlockingQueue---------------");
			ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(50);
			queue.add("element1 inserts at tail of ArrayBlockingQueue"); // 队列满了，会抛异常
			try {
				queue.put("element2 inserts at tail of ArrayBlockingQueue"); // 队列满了，会等待
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testConcurrentLinkedQueue() {
			System.out.println("-----------ConcurrentLinkedQueue---------------");
			ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
			queue.add("element1 inserts at tail of ConcurrentLinkedQueue"); // 队列没有边界、插入元素不能为null
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testLinkedBlockingQueue() {
			System.out.println("-----------LinkedBlockingQueue---------------");
			LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
			queue.add("element1 inserts at tail of LinkedBlockingQueue"); // 有容量限制
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testLinkedTransferQueue() {
			System.out.println("-----------LinkedTransferQueue---------------");
			LinkedTransferQueue<String> queue = new LinkedTransferQueue<String>();
			queue.add("element1 inserts at tail of LinkedTransferQueue"); // 队列没有边界、插入元素不能为null
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testPriorityBlockingQueue() {
			System.out.println("-----------PriorityBlockingQueue---------------");
			PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>();
			queue.add("element1 inserts at tail of PriorityBlockingQueue"); // 元素要能比较，插入元素不能为null
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testSynchronousQueue() {
			System.out.println("-----------SynchronousQueue---------------");
			SynchronousQueue<String> queue = new SynchronousQueue<String>();
			queue.add("element1 inserts at tail of SynchronousQueue"); // 有容量限制
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		/**
		 * 指定过期时间
		 */
		public static void testDelayQueue() {
			System.out.println("-----------DelayQueue---------------");
			DelayQueue delayQueue = new DelayQueue();
			long currentTimeMillis = System.currentTimeMillis();
			delayQueue.add(new DelayedElement("DelayQueueElement1",currentTimeMillis + 3 * 1000));
			delayQueue.add(new DelayedElement("DelayQueueElement2",currentTimeMillis + 4 * 1000));
			delayQueue.add(new DelayedElement("DelayQueueElement3",currentTimeMillis + 5 * 1000));
			
			while(delayQueue.size()>0){
				System.out.println(delayQueue.poll());
				System.out.println(delayQueue.poll());
				System.out.println(delayQueue.poll());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private static class DelayedElement implements Delayed{
			private long expireTimeMillis = 0;
			private String value;
			public DelayedElement(String value,long expireTimeMillis){
				this.expireTimeMillis = expireTimeMillis;
				this.value = value;
			}
			
			public String getValue() {
				return value;
			}

			@Override
			public int compareTo(Delayed o) {
				return this.value.compareTo(((DelayedElement) o).getValue());
			}

			@Override
			public long getDelay(TimeUnit unit) {
				return (expireTimeMillis - System.currentTimeMillis());
			}

			@Override
			public String toString() {
				return "DelayedElement [expireTimeMillis=" + expireTimeMillis + ", value=" + value + "]";
			}
			
		}
	}
	
	
	
}
