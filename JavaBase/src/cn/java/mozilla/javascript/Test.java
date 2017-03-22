package cn.java.mozilla.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class Test {

	public static void main(String[] args) {
		test4JsNormalCode();
		test4JsObj();
		test4JsFunc();
		test4JsFuncWithParam();
	}
	
	protected static void test4JsNormalCode() {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();

		String jsStr = "100*20/10";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}
	
	protected static void test4JsObj() {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();
		
		String jsStr = "var objTest = { 'key1':'value1' }; objTest.key1;";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}
	
	protected static void test4JsFunc() {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();
		String jsStr = "var testFunc = function(){ return 'testFunc...' }; testFunc();";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}
	
	protected static void test4JsFuncWithParam() {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();
		scope.put("param1", scope, "value1");
		String jsStr = "var testFunc = function(param){ return 'testFunc...'+'param='+param }; testFunc(param1);";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}

}
