package cn.java.util.function;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Test {
	
	/**
	 * Java8中java.util.function包下的函数式接口
	 * Function, Supplier, Consumer, Predicate
	 */
	public static void main(String[] args) {
		// ---------testFunction-------------
		int incr = 20;
		int myNumber = 10;
		testFunction(myNumber, val -> val + incr);

		// ---------testPredicate-------------
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		System.out.println("输出所有偶数:");
		testPredicate(list, n-> n%2 == 0 );
	}

	public static void testFunction(int value, Function<Integer, Integer> function) {
		int newValue = function.apply(value);
		System.out.println(newValue);
	}


	public static void testPredicate(List<Integer> list, Predicate<Integer> predicate) {
		for(Integer n: list) {
			if(predicate.test(n)) {
				System.out.println(n + " ");
			}
		}
	}
}
