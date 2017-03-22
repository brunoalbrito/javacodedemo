package cn.java.note.util.parameterexpression;

public class Test {

	public static void main(String[] args) throws Exception {
		// #{((username))}
		String expression = "((username))";
		ParameterExpression mParameterExpression = new ParameterExpression(expression);
	}

}
