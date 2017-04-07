package cn.java.socket.watchdog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpServer {
	public static void main(String[] args) {
		ServerSocket server;// 定义ServerSocket变量
		try {
			server = new ServerSocket(Integer.parseInt("18887"));// ServerSocket建立在18887端口，以及本机IP地址上
			while (true) {
				Socket client = server.accept();// 等待客户端连接
				new Thread(new SocketServer(client)).start();// 有客户端连接进来，则开启SocketServer线程，并处理这个TCP连接
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static class SocketServer implements Runnable {
		private Socket client = null;

		public SocketServer(Socket client) {
			this.client = client;
		}

		/**
		 * 得到二维码数据5506
		 * @param buffer
		 * @param bufferLen
		 */
		public void getQRCodeData(byte buffer[], int bufferLen) {
			byte[] sendCommand = new byte[10];
			byte pos = 0;
			byte readerNo = 0;
			int nDataLength = bufferLen - 16;
			byte[] QRData = new byte[nDataLength];

			pos = 14;// 从得到数据的第15字节开始取二维码数据
			readerNo = buffer[13];// 读头号，一个设备可以带二个二维码扫描头，从第14字节取扫描头号
			System.arraycopy(buffer, pos, QRData, 0, nDataLength);// 将二维码数据复制到QRData数组

			String QrDataStr = new String(QRData);// 将QRData数组数据赋给字符串变量QrDataStr
			String sReader = (Integer.toHexString(readerNo & 0XFF)); // 将读头号从整形转成字符型

			Date nowTime = new Date();
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 取系统时间
			// 显示得到的二维码字符串，以及系统时间
			System.out.println(
					time.format(nowTime) + " QRcode data:\r\nReader:" + sReader + "\r\nQR Date:" + QrDataStr + "\r\n");
		}

		public void getDeviceNetParameter(byte recieve[], int len) {

		}

		public void sendCommandtoDevice(DataOutputStream output, byte[] sendCommand, int code) {

		}

		public void getReturnCommand(byte recieve[], int len) {

		}

		public static final int DEVICE_NET_PARAMETER_UPLOAD = 5502;
		public static final int QRCODE_UPLOAD = 5506;
		public static final int DISTANCE_OPEN_DOOR_CODE = 0x5000;
		public static final int HHEART_BEEP_CODE_FROM_SERVER = 4508;
		public static final int HHEART_BEEP_CODE_FROM_DEVICE = 4518;

		public void run() {
			DataInputStream input;
			DataOutputStream output;
			int commandCode = 0;// 协议指令代码
			byte[] sendCommand = new byte[10];
			String clientAddress = client.getLocalSocketAddress() + ">"; // 得到TCP
																			// SOCKET设备IP地址和端口信息
			try {
				client.setSoTimeout(300 * 1000);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			try {
				input = new DataInputStream(client.getInputStream());
				output = new DataOutputStream(client.getOutputStream());
				System.out.println(clientAddress + "#login successfully##");
				byte[] buffer = new byte[1024];
				int bufferLen = -1;
				while (true) {
					try {
						bufferLen = input.read(buffer);// 等待读设备数据，
						if (bufferLen == -1)
							break;

						// TCP客户端的数据，如果满足 数据位0=0x02
						// 数据位9=0x11,长度大于0x0F,数据最后一位=0x03则表示是二维码设备传过来的数据
						// 因为服务器在广域网，不要以为接收到数据，那就是二维码数据发过来的，有很多广播或其它途径过来的网络数据，也会接收到
						if ((buffer[0] == 0x02) && (buffer[9] == 0x11) && (bufferLen >= 0x0f)
								&& (buffer[bufferLen - 1] == 0x03)) {
							commandCode = 0x0000;
							commandCode = buffer[10];
							commandCode <<= 8;
							commandCode |= buffer[11];
							switch (commandCode)// 得到指令代码
							{
							case DEVICE_NET_PARAMETER_UPLOAD:// 5502设备TCP连接时，设备首先发出来的设备网络参数，不要再发给设备
								getDeviceNetParameter(buffer, bufferLen);// 得到设备的网络参数以及MAC地址
								break;
							case QRCODE_UPLOAD:// 5506二维码数据，本指令不要再发给设备
								getQRCodeData(buffer, bufferLen);
								// 远程开门，判断合法后远程开门
								sendCommand[0] = (byte) 0x50;
								sendCommand[1] = (byte) 0x00;
								sendCommand[2] = (byte) 0xFF;
								sendCommand[3] = (byte) 0x01;
								sendCommandtoDevice(output, sendCommand, 4);
								break;
							case DISTANCE_OPEN_DOOR_CODE:// 0x5000
								// //服务器向设备发的远程开门，设备应答的返回指令，不要再发给设备
								getReturnCommand(buffer, bufferLen);
								break;
							case HHEART_BEEP_CODE_FROM_SERVER:// 4508
								// 服务器向设备向发送的心跳指令后，设备应答的4508成功的指令，不要再发给设备
								break;
							case HHEART_BEEP_CODE_FROM_DEVICE:// 4518
								// 设备向服务器发送的心跳指令，要回传4508到设备
								getReturnCommand(buffer, bufferLen);
								sendCommand[0] = (byte) 0x45;
								sendCommand[1] = (byte) 0x08;
								sendCommand[2] = (byte) 0x00;
								sendCommandtoDevice(output, sendCommand, 3);
								break;

							default:
								break;
							}
						}
					} catch (SocketTimeoutException e)// read time out
					{
						e.printStackTrace();
						break;
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (client != null) {
					try {
						client.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}