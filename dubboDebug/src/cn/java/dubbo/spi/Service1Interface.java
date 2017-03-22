package cn.java.dubbo.spi;

import com.alibaba.dubbo.common.extension.SPI;

/**
	 1、定义接口文件，加上注解 @SPI
	 2、定义配置文件
	  		"META-INF/dubbo/internal/cn.java.dubbo.spi.Service1Interface"、
	 		"META-INF/dubbo/cn.java.dubbo.spi.Service1Interface"、
	 		"META-INF/services/cn.java.dubbo.spi.Service1Interface"
	 3、编写业务类，要求实现本接口
 */
@SPI
public class Service1Interface {

}
