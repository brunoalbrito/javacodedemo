package cn.java.demo.main;

import com.jfinal.server.IServer;
import com.jfinal.server.ServerFactory;

public class ServerMain {

	public static void main(String[] args) {
		IServer server = ServerFactory.getServer(8080);
		server.start();
	}
}
