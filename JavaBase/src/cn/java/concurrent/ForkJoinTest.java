package cn.java.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
/**
 * http://www.infoq.com/cn/articles/fork-join-introduction
 * @author zhouzhian
 *
 */
public class ForkJoinTest {

	public static void main(String[] args) throws Exception {
		ForkJoinTest.testForkJoinTaskImpl();
		ForkJoinTest.testRecursiveTaskImpl();

	}

	public static void testForkJoinTaskImpl() throws Exception{
		System.out.println("------------ForkJoinTaskImpl-------------");
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		ForkJoinTaskImpl forkJoinTaskImpl = new ForkJoinTaskImpl(1,4);
		Future<Integer> result = forkJoinPool.submit(forkJoinTaskImpl);
		System.out.println(result.get());
	}
	
	public static void testRecursiveTaskImpl() throws Exception{
		System.out.println("------------RecursiveTaskImpl-------------");
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		RecursiveTaskImpl recursiveTaskImpl = new RecursiveTaskImpl(1,4);
		Future<Integer> result = forkJoinPool.submit(recursiveTaskImpl);
		System.out.println(result.get());
	}

	public static class RecursiveTaskImpl extends RecursiveTask {
		private static final int THRESHOLD = 2;// 阈值
		private int start;
		private int end;

		public RecursiveTaskImpl(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			int sum = 0;
			boolean canCompute = (end - start) <= THRESHOLD;
			if (canCompute) {
				for (int i = start; i <= end; i++) {
					sum += i;
				}
			} else {
				int middle = (start + end) / 2;
				// 分裂成两个子任务
				RecursiveTaskImpl child1 = new RecursiveTaskImpl(start, middle);
				RecursiveTaskImpl child2 = new RecursiveTaskImpl(middle + 1, end);

				// 执行子任务
				child1.fork();
				child2.fork();

				// 等待子任务执行完成
				int child1Result = (int) child1.join();
				int child2Result = (int) child2.join();

				// 合并子任务
				sum = child1Result + child2Result;
			}
			return sum;
		}

	}

	public static class ForkJoinTaskImpl extends ForkJoinTask {
		private static final int THRESHOLD = 2;// 阈值
		private int start;
		private int end;
		private int result = 0;

		public ForkJoinTaskImpl(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public Object getRawResult() {
			return result;
		}

		@Override
		protected void setRawResult(Object value) {
			result = (int) value;
		}

		@Override
		protected boolean exec() {
			boolean canCompute = (end - start) <= THRESHOLD;
			if (canCompute) {
				for (int i = start; i <= end; i++) {
					result += i;
				}
			} else {
				int middle = (start + end) / 2;
				
				// 分裂成两个子任务
				ForkJoinTaskImpl child1 = new ForkJoinTaskImpl(start, middle);
				ForkJoinTaskImpl child2 = new ForkJoinTaskImpl(middle + 1, end);

				// 执行子任务
				child1.fork();
				child2.fork();

				// 等待子任务执行完成
				int child1Result = (int) child1.join();
				int child2Result = (int) child2.join();
				// 合并子任务
				result = child1Result + child2Result;
			}
			return true;
		}

	}

}
