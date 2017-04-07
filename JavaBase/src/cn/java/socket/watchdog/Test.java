package cn.java.socket.watchdog;

import cn.java.codec.hex.HexUtil;

public class Test {

	
	public static void main(String[] args) {
		System.out.println(HexUtil.encode("12345678".getBytes())); // 31 32 33 34 35 36 37 38
		System.out.println(HexUtil.encode("2010041920200419".getBytes())); // 32 00 30 00 31 00 30 00 30 00 34 00 31 00 39 00 32 00 30 00 32 00 30 00 30 00 34 00 31 00 39 00
		System.out.println(HexUtil.encode("441581199009095712".getBytes())); // 34 00 34 00 31 00 35 00 38 00 31 00 31 00 39 00 39 00 30 00 30 00 39 00 30 00 39 00 35 00 37 00 31 00 32 00
	}

}
