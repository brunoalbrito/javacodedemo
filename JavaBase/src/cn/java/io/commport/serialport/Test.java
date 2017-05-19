package cn.java.io.commport.serialport;

import cn.java.io.commport.CommPortsUtil;

/**
 * 主板arduino
 * 树莓派
 * 
 * http://blog.csdn.net/bhq2010/article/details/8674192
 * http://rxtx.qbang.org/wiki/index.php/Download
 * http://baike.baidu.com/link?url=p0mjfmiHo642uJlEsJytWlFJ-Syswxp5SYKwhutfg-aHQXTjpQ-WMycoggyU_EoT6zv4JdveZx-ZL_J_sxrLL_
 * http://rxtx.qbang.org/wiki/index.php/Examples
 * 
 * @author zhouzhian
 *
 */
/*
linux下查看串口 sudo dmesg | grep tty
*/
public class Test {

	public static void printJavaLibraryPath(){
		String javaLibraryPath = System.getProperty("java.library.path");
		String[] paths = javaLibraryPath.split(";");
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i]);
		}
	}
	
	public static void main ( String[] args )
	{
		printJavaLibraryPath();
		CommPortsUtil.listPorts();
		try
		{
//			(new TwoWaySerialComm()).connect("/dev/ttyACM0");
//			(new TwoWaySerialComm()).connect("COM3");
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
