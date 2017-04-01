package cn.java.note.web;

import org.springframework.core.ResolvableType;

public class ResolvableTypeTest {

	public static void main(String[] args) {
		ResolvableType resolvableType = ResolvableType.forRawClass(Object.class);
		resolvableType.isAssignableFrom(String.class);
	}

}
