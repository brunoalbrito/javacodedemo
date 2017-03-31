package cn.java.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorCompletionServiceTest {

	public static void main(String[] args) throws Exception {
		ExecutorService executors = Executors.newCachedThreadPool();
		ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(executors);
		executorCompletionService.submit(new CallableImpl("callableName1"));
		executorCompletionService.submit(new CallableImpl("callableName2"));
		
		Future future1 = executorCompletionService.poll();
		System.out.println(future1.get());
		
		Future future2 = executorCompletionService.poll();
		System.out.println(future2.get());
	}

	public static class CallableImpl implements Callable {
		private String callableName;

		public CallableImpl(String callableName) {
			this.callableName = callableName;
		}

		@Override
		public Object call() throws Exception {
			return this.callableName;
		}
	}

}
