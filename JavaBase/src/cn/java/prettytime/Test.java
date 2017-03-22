package cn.java.prettytime;

import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

/**
 * 时间间隔
 * @author zhouzhian
 *
 */
public class Test {

	public static void main(String[] args) {
		test1();
		test2();
		test3();
		test4();
	}

	public static void test1() {
//		PrettyTime p = new PrettyTime();
		PrettyTime p = new PrettyTime(Locale.CHINESE);
		System.out.println(p.format(new Date()));
	}

	public static void test2() {
		PrettyTime p = new PrettyTime(Locale.CHINESE);
		Date mDate = new Date();
		long milliseconds = mDate.getTime();
		milliseconds = milliseconds - 3600*1000;
		System.out.println(p.format(new Date(milliseconds)));
	}

	public static void test3() {
		PrettyTime p = new PrettyTime(Locale.CHINESE);
		Date mDate = new Date();
		long milliseconds = mDate.getTime();
		milliseconds = milliseconds - 3600 * 24 *1000;
		System.out.println(p.format(new Date(milliseconds)));
	}

	public static void test4() {
		PrettyTime p = new PrettyTime(Locale.CHINESE);
		Date mDate = new Date();
		long milliseconds = mDate.getTime();
		milliseconds = milliseconds + 3600*1000;
		System.out.println(p.format(new Date(milliseconds)));
	}
}
