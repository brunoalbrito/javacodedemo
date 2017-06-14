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
		{
			DequeTest.testConcurrentLinkedDeque();
			DequeTest.testLinkedBlockingDeque();
		}
		
		{
			// BlockingQueue是对 读取 或者 写入 锁定“整个队列”，所以在比较繁忙的时候，各种锁比较耗时 
			// java.util.concurrent.locks.ReentrantLock
			{
				QueueTest.testArrayBlockingQueue();
				QueueTest.testLinkedBlockingQueue();
				QueueTest.testPriorityBlockingQueue();
				QueueTest.testDelayQueue(); // 指定时间有效（指定时间后队列的元素才能被获取）
			}
			
			// 使用的锁机制是：sun.misc.Unsafe.compareAndSwapObject --- CAS
			{
				QueueTest.testConcurrentLinkedQueue(); 
				QueueTest.testLinkedTransferQueue(); // 
				QueueTest.testSynchronousQueue(); // 没有容量（或者说容量为1），压入一个元素，必须等到元素被取走，才能继续压入
			}
		}
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
			/*
			 	 如果看了源码的可以看出ArrayBlockingQueue并没有用List作为容器，而是用了数组。
			 	 如果知道队列的大小，那么使用ArrayBlockIngQueue就比较合适了，因为它使用循环数组实现。
			 	 但是如果不知道队列未来的大小，那么使用ArrayBlockingQueue就必然会导致数组的来回复制，降低效率。
			 	 外在ArrayBlockingQueue中有这个poll(long timeout, TimeUnit unit)这个方法，这个方法是传入一个参数就是等待timeout ms后返回。实际应用中会发现这个等待时间是不准确的。
			 
			 	不能放入null元素
			 	如果队列满了，返回false
			 	
			 	使用的锁机制是：java.util.concurrent.locks.ReentrantLock
			 	数据结构是：数组
			 */
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
		
		public static void testLinkedBlockingQueue() {
			/*
			 	LinkedBlockingQueue内部使用ReentrantLock实现插入锁(putLock)和取出锁(takeLock)。
			 	putLock上的条件变量是notFull，即可以用notFull唤醒阻塞在putLock上的线程。
			 	takeLock上的条件变量是notEmtpy，即可用notEmpty唤醒阻塞在takeLock上的线程。
			 	
			 	使用的锁机制是：java.util.concurrent.locks.ReentrantLock
			 	数据结构是：链表
			 */
			System.out.println("-----------LinkedBlockingQueue---------------");
			LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
			queue.add("element1 inserts at tail of LinkedBlockingQueue"); // 有容量限制
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testPriorityBlockingQueue() {
			/*
			 	使用的锁机制是：java.util.concurrent.locks.ReentrantLock
			 	数据结构是：数组 Array
			 */
			System.out.println("-----------PriorityBlockingQueue---------------");
			PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>();
			queue.add("element1 inserts at tail of PriorityBlockingQueue"); // 元素要能比较，插入元素不能为null
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}

		/**
		 * 指定过期时间
		 */
		public static void testDelayQueue() {
			/**
			 	使用的锁机制是：java.util.concurrent.locks.ReentrantLock
			 	数据结构是：数组
			 */
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
		
		public static void testConcurrentLinkedQueue() {
			/*
			 	ConcurrentLinkedQueue是Queue的一个安全实现。Queue中元素按FIFO原则进行排序。采用CAS操作，来保证元素的一致性。
			 	ConcurrentLinkedQueue是一个基于链接节点的无界线程安全队列，它采用先进先出的规则对节点进行排序，当我们添加一个元素的时候，
			 	它会添加到队列的尾部，当我们获取一个元素时，它会返回队列头部的元素。它采用了“wait－free”算法来实现。
			 	
			 	使用的锁机制是：sun.misc.Unsafe.compareAndSwapObject --- CAS
			 	数据结构是：链表
			 */
			System.out.println("-----------ConcurrentLinkedQueue---------------");
			ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
			queue.add("element1 inserts at tail of ConcurrentLinkedQueue"); // 队列没有边界、插入元素不能为null
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
		
		public static void testLinkedTransferQueue() {
			/*
			 	使用的锁机制是：sun.misc.Unsafe.compareAndSwapObject --- CAS
			 	数据结构是：链表
			 */
			System.out.println("-----------LinkedTransferQueue---------------");
			LinkedTransferQueue<String> queue = new LinkedTransferQueue<String>();
			queue.add("element1 inserts at tail of LinkedTransferQueue"); // 队列没有边界、插入元素不能为null
			System.out.println(queue.peek());
			System.out.println(queue.poll());
		}
	
		
		public static void testSynchronousQueue() {
			/*
				SynchronousQueue是这样 一种阻塞队列，其中每个 put 必须等待一个 take，反之亦然。
				同步队列没有任何内部容量，甚至连一个队列的容量都没有。 不能在同步队列上进行 peek，因为仅在试图要取得元素时，该元素才存在
				
				不像ArrayBlockingQueue或LinkedListBlockingQueue，SynchronousQueue内部并没有数据缓存空间，
				你不能调用peek()方法来看队列中是否有数据元素，因为数据元素只有当你试着取走的时候才可能存在，
				不取走而只想偷窥一下是不行的，当然遍历这个队列的操作也是不允许的。队列头元素是第一个排队要插
				入数据的线程，而不是要交换的数据。数据是在配对的生产者和消费者线程之间直接传递的，并不会将数
				据缓冲数据到队列中。可以这样来理解：生产者和消费者互相等待对方
				
			 	使用的锁机制是：sun.misc.Unsafe.compareAndSwapObject --- CAS
			 	数据结构是：链表
			 */
			System.out.println("-----------SynchronousQueue---------------");
			SynchronousQueue<String> queue = new SynchronousQueue<String>();
			queue.add("element1 inserts at tail of SynchronousQueue"); // 有容量限制
			System.out.println(queue.peek());
			System.out.println(queue.poll());
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
