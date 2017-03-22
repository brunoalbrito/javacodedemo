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


--------------------------------------------
		串行接口、并行接口、USB接口（驱动程序）
--------------------------------------------

-------串行接口(串口)---------
电信号：TTL、RS-232、RS-485指的是串口的电平标准
串行接口简称串口，也称串行通信接口或串行通讯接口（通常指COM接口），是采用串行通信方式的扩展接口。
串行接口 (Serial Interface) 是指数据一位一位地顺序传送，其特点是通信线路简单，只要一对传输线就可以实现双向通信（可以直接利用电话线作为传输线），从而大大降低了成本，特别适用于远距离通信，但传送速度较慢。一条信息的各位数据被逐位按顺序传送的通讯方式称为串行通讯。
串行通讯的特点是：数据位的传送，按位顺序进行，最少只需一根传输线即可完成；成本低但传送速度慢。串行通讯的距离可以从几米到几千米；根据信息的传送方向，串行通讯可以进一步分为单工、半双工和全双工三种。
串口（COM）不支持热插拔及传输速率较低，目前部分新主板和大部分便携电脑已开始取消该接口。
串口通信指串口按位（bit）发送和接收字节。尽管比按字节（byte）的并行通信慢，但是串口可以在使用一根线发送数据的同时用另一根线接收数据。

《串口驱动开发》
	串口驱动注册机制
		platform_driver_register
	
	串口驱动编写
	// ------ 串口(如 GPRS)驱动的代码骨架------ {{{
		数据结构
			struct uart_port {
				const struct uart_ops   *ops;  // 串口端口操作函数集
			}
			
			struct uart_ops {
				void         (*send_xchar)(struct uart_port *, char ch); //  发送xChar
				int          (*startup)(struct uart_port *); // 启动串口,应用程序打开串口设备文件时,该函数会被调用
				void         (*shutdown)(struct uart_port *); // 关闭串口,应用程序关闭串口设备文件时,该函数会被调用 
				void         (*set_termios)(struct uart_port *, struct ktermios *new, struct ktermios*old); //  设置串口参数
				int          (*ioctl)(struct uart_port *, unsigned int, unsigned long); // IO控制 
			}
			
			static struct uart_driver gprs_uart_driver = {
			    .owner = THIS_MODULE,                // Owner 
			    .driver_name = DEV_NAME,             // Driver name 
			    .dev_name = DEV_NAME,                // Device node name 
			    .major = GPRS_UART_MAJOR,            // Major number 
			    .minor = GPRS_UART_MINOR,            // Minor number start 
			    .nr = 1,                             // Number of UART ports 
			};
		
			static struct uart_ops gprs_uart_ops = {
				...
				.startup        = gprs_uart_startup,
				...
			}
			
			static struct uart_port gprs_uart_port = {
				.ops        = &gprs_uart_ops, 
			}
			static struct platform_driver gprs_plat_driver = {
				.probe = gprs_uart_probe,
			}
		
		函数
			// 探针函数，回调函数
			static int __init gprs_uart_probe(struct platform_device *dev)
			{
				// 初始化串口
				ret = gprs_uart_init_port(&gprs_uart_port, dev);
				// 添加串口
				ret = uart_add_one_port(&gprs_uart_driver, &gprs_uart_port);
				// 将串口uart_port结构体保存在platform_device->dev->driver_data中
				platform_set_drvdata(dev, &gprs_uart_port);
			}
			
			// 读 -- 获取串口接收到的数据,并将这些数据递交给行规则层
			static irqreturn_t gprs_uart_rx_chars(int irq, void *dev_id)
			{
				// 循环接收数据,最多一次中断接收64字节数据
				while (max_count-- > 0)
		    	{
		    		// 若Rx FIFO无数据了,跳出循环
			    	if (gprs_uart_rx_fifocnt(ufstat) == 0)
			            break;
			        // 读取Rx error状态寄存器
		            uerstat = rd_regl(port, S3C2410_UERSTAT);
		            // 读取已接受到的数据
		            ch = rd_regb(port, S3C2410_URXH);
		            // 先将tty标志设为正常
		            flag = TTY_NORMAL;
		            ....
		        }
		        // 刷新tty设备的flip缓冲,将接受到的数据传给行规则层
		        tty_flip_buffer_push(tty);
		        return IRQ_HANDLED;
			}
			
			// 写 -- 将用户空间的数据(保存在环形缓冲xmit里)发送出去
			static irqreturn_t gprs_uart_tx_chars(int irq, void *dev_id)
			{
				//  获取环线缓冲
				struct circ_buf *xmit = &port->info->xmit
				if (port->x_char) // 若设置了xChar字符
			    {
			    	wr_regb(port, S3C2410_UTXH, port->x_char); // 将该xChar发送出去
			    }
			    
			    // 如果没有更多的字符需要发送(环形缓冲为空)，或者uart Tx已停止，则停止uart并退出中断处理函数
			    if (uart_circ_empty(xmit) || uart_tx_stopped(port))
			    {
			    }
			    
			    // 循环发送数据,直到环形缓冲为空,最多一次中断发送256字节数据 
			    while (!uart_circ_empty(xmit) && count-- > 0)
			    {
			    	// 将要发送的数据写入Tx FIFO
			    	wr_regb(port, S3C2410_UTXH, xmit->buf[xmit->tail]);
			    }
			}
			
			static int gprs_uart_startup(struct uart_port *port)
			{
				// 设置串口为不可接受,也不可发送
				rx_enabled(port) = 0;
		    	tx_enabled(port) = 0;
		    	// 注册接收中断处理函数
				ret = request_irq(RX_IRQ(port), gprs_uart_rx_chars, 0, portname, port); // 其中gprs_uart_rx_chars为回调函数
				// 设置串口为允许接收
				rx_enabled(port) = 1;
				// 注册中断发送函数
				ret = request_irq(TX_IRQ(port), gprs_uart_tx_chars, 0, portname, port);
				// 设置串口为允许发送
				tx_enabled(port) = 1;
			}
		
		系统模块
			模块初始化函数
				static int __init gprs_init_module(void)
				{
					...
					// 将串口驱动uart_driver注册到内核(串口核心层)中
					retval = uart_register_driver(&gprs_uart_driver);
					
					// 注册驱动
					retval = platform_driver_register(&gprs_plat_driver);
					...
				}
				
			模块退出函数
				static void __exit gprs_exit_module(void)
				{
					...
					// 注销我们已注册的uart_driver
					platform_driver_unregister(&gprs_plat_driver);
				}
			注册到内核(以系统模块的方式注册)
				module_init(gprs_init_module);
				module_exit(gprs_exit_module);
				MODULE_AUTHOR("lingd");
				MODULE_LICENSE("Dual BSD/GPL");
	// ------ 串口驱动的代码骨架------ }}}

		
-------并行接口(并口)---------
并行接口，指采用并行传输方式来传输数据的接口标准。
一个并行接口的接口特性可以从两个方面加以描述：1. 以并行方式传输的数据通道的宽度，也称接口传输的位数；2. 用于协调并行数据传输的额外接口控制线或称交互信号的特性。 数据的宽度可以从1～128位或者更宽，最常用的是8位，可通过接口一次传送8个数据位。
《并口驱动开发》
	并口驱动注册机制
		register_chrdev
		
	并口驱动编写
		结构体
			struct file_operations parport_fops = {
				.owner	=	THIS_MODULE,
				.write	=	parport_write, // 读
				.read	=	parport_read, // 写
				.open	=	parport_open,
				.unlocked_ioctl	=	parport_ioctl,
				.release=	parport_release,
			};
			
		函数
			int parport_open(struct inode *inode, struct file *filp)
			{
				printk(KERN_ALERT "open the parport_dev\n");
				return 0;
			}
			// 写
			ssize_t parport_write(struct file *filp, const char *buf, size_t count, loff_t *f_ops)
			{
				unsigned char status;
				int loop;
				for(loop = 0; loop < count; loop++)
				{
					get_user(status, (char *)buf);
					outb(status, Drv_write_addr);
				}
				return count;
			}
			// 读
			ssize_t parport_read(struct file *filp, char *buf, size_t count, loff_t *f_ops)
			{
				unsigned char status;
				int loop;
				for(loop = 0; loop < count; loop++)
				{
					status = inb(Drv_read_addr);
					put_user(status, (char *) &buf[loop]);
				}
				return count;
			}
			
			long parport_ioctl(struct file *filp, unsigned int cmd, unsigned long arg)
			{
				int loop;
				struct dat data;
				switch(cmd)
				{
					case PARPORT_WRITE:
			//			outb(status, Drv_write_addr);
						copy_from_user(&data, (struct dat *)arg, sizeof(data));			
						printk(KERN_ALERT "out put %d\n",data.loop);
						for(loop = 0; loop < data.loop; loop ++)
						{
							printk(KERN_ALERT "the %dth loop, write %d\n",loop,data.buf[loop]);
							outb(data.buf[loop], Drv_write_addr);
							wmb();
						}
						break;
			
					case PARPORT_CLOSE:
						outb(0x00, Drv_write_addr);
						wmb();
						break;
				}
				return 0;
			}
			
			int parport_release(struct inode *inode, struct file *filp)
			{
				printk(KERN_ALERT "close the module parport_dev\n");
				return 0;
			}
				
		系统模块
			模块初始化函数
				int parport_init(void)
				{
					int result;
				
					result = register_chrdev(Drv_major, Drv_name, &parport_fops);
					if(result < 0)
						return result;
					printk(KERN_ALERT "hello the module parport_dev\n");
				
					return 0;
				}
			模块退出函数
				void parport_exit(void)
				{
					printk(KERN_ALERT "exit the module parport_drv\n");
					unregister_chrdev(Drv_major, Drv_name);
					
				}
			注册到内核(以系统模块的方式注册)	
				module_init(parport_init);
				module_exit(parport_exit);

-------USB接口---------
《USB驱动开发》
	USB驱动注册机制
		usb_register
	
	USB硬件触发机制
		http://blog.csdn.net/qianguozheng/article/details/6700274
		http://www.cnblogs.com/hoys/archive/2011/04/01/2002299.html
		在最后会走入两条路：
			1、通用探针（generic_probe 适合通用硬件，如鼠标、键盘）  [ 键盘点击  ---> generic_probe ]
			2、usb接口探针（usb_probe_interface 硬件商自主开发的驱动程序） [ 硬件事件  ---> usb_probe_interface ]
	USB驱动编写
		http://blog.csdn.net/zqixiao_09/article/details/50986965
		http://blog.csdn.net/terry_yuan2011/article/details/7334074
		编写一个USB设备驱动程序的方法和其他总线驱动方式类似，驱动程序把驱动程序对象注册到USB子系统中，稍后再使用制造商和设备标识来判断是否安装了硬件。
		Linux的设备驱动，会被编译成模块（即：驱动程序开发是以系统模块开发的方式开发），然后在需要时挂在到内核。
		usb设备可以有多个interface，每个interface所定义的IO操作可能不一样，所以向系统注册的usb_class_driver要求注册到某一个interface，而不是device，因此，usb_register_dev的第一个参数才是interface，而第二个参数就是某一个usb_class_driver。
		linux系统用主设备号来识别某类设备的驱动程序，用次设备号管理识别具体的设备，驱动程序可以依照次设备号来区分不同的设备
		urb 以一种异步的方式同一个特定USB设备的特定端点发送或接受数据。一个 USB 设备驱动可根据驱动的需要，分配多个 urb 给一个端点或重用单个 urb 给多个不同的端点。设备中的每个端点都处理一个 urb 队列, 所以多个 urb 可在队列清空之前被发送到相同的端点。
		
		// ------ USB驱动的代码骨架------ {{{
			IO 函数：
				static int skel_open(struct inode *inode, struct file *file) 
				{
					...		
				}
				static int skel_release(struct inode *inode, struct file *file) 
				{
					...		
				}
				// 读
				static ssize_t skel_read(struct file *file, char *buffer, size_t count, loff_t *ppos)
				{	
					// 进行阻塞的批量读以从设备获取数据
					retval = usb_bulk_msg(dev->udev, usb_rcvbulkpipe(dev->udev, dev->bulk_in_endpointAddr),  dev->bulk_in_buffer, min(dev->bulk_in_size, count), &bytes_read, 10000);
					// 如果读成功，复制到用户空间
					if (!retval) {
				        if (copy_to_user(buffer, dev->bulk_in_buffer, bytes_read))
				        	retval = -EFAULT;
				        else
				        	retval = bytes_read;
					}
					...	
				}
				// 回调函数，当urb被成功传输到USB设备之后，urb回调函数将被USB核心调用，在我们的例子中，我们初始化urb，使它指向skel_write_bulk_callback函数
				static void skel_write_bulk_callback(struct urb *urb){
					...	
				}
				// 写
				static ssize_t skel_write(struct file *file, const char *user_buffer, size_t count, loff_t *ppos)
				{
					// urb 以一种异步的方式同一个特定USB设备的特定端点发送或接受数据
					struct urb *urb = NULL;
				 	// 创建一个urb,并且给它分配一个缓存
					urb = usb_alloc_urb(0, GFP_KERNEL);
					// 当urb被成功分配后，还要创建一个DMA缓冲区来以高效的方式发送数据到设备，传递给驱动程序的数据要复制到这块缓冲中去
					buf = usb_buffer_alloc(dev->udev, writesize, GFP_KERNEL, &urb->transfer_dma);
					// 当数据从用户空间正确复制到局部缓冲区后，urb必须在可以被提交给USB核心之前被正确初始化，指定了回调函数skel_write_bulk_callback
					usb_fill_bulk_urb(urb, dev->udev,  usb_sndbulkpipe(dev->udev, dev->bulk_out_endpointAddr),  buf, writesize, skel_write_bulk_callback, dev);
					// 把数据从批量OUT端口发出
					retval = usb_submit_urb(urb, GFP_KERNEL);
					....
				}
				
			数据结构：（声明了static限制，即内部变量）
				static const struct file_operations skel_fops = {
					.owner = THIS_MODULE,
					.read = skel_read,
					.write = skel_write,
					.open = skel_open,
					.release = skel_release,
				};
				static struct usb_class_driver skel_class = {     
					.name =       "skel%d",     
					.fops =       &skel_fops, // 真正完成对设备IO操作的函数
					.minor_base = USB_SKEL_MINOR_BASE,     
				};
				
				static struct usb_driver skel_driver = {     
					.owner = THIS_MODULE,  // 指向该驱动程序的模块所有者的批针。USB核心使用它来正确地对该USB驱动程序进行引用计数，使它不会在不合适的时刻被卸载掉，这个变量应该被设置为THIS_MODULE宏。
					.name = "skeleton",  // 指向驱动程序名字的指针，在内核的所有USB驱动程序中它必须是唯一的，通常被设置为和驱动程序模块名相同的名字。
					.id_table = skel_table, // 指向ID设备表的指针，这个表包含了一列该驱动程序可以支持的USB设备，如果没有设置这个变量，USB驱动程序中的探测回调函数就不会被调用。
					.probe = skel_probe, //　驱动的处理函数（探针函数），当硬件触发的时候，会调用本函数。这个是指向USB驱动程序中的探测函数的指针。当USB核心认为它有一个接口（usb_interface）可以由该驱动程序处理时，这个函数被调用。
					.disconnect = skel_disconnect,    // 断开函数,指向USB驱动程序中的断开函数的指针，当一个USB接口（usb_interface）被从系统中移除或者驱动程序正在从USB核心中卸载时，USB核心将调用这个函数。
				};   
			
			驱动的处理函数（探针函数）：
				函数应该检查传递给他的设备信息，确定驱动程序是否真的适合该设备。
				static int skel_probe(struct usb_interface *interface, const struct usb_device_id *id)
				{
					struct usb_skel *dev;     //usb_skel设备
					dev = kzalloc(sizeof(*dev), GFP_KERNEL);  //分配设备状态的内存并初始化
					...
					dev->interface = interface;
					...
					// 把数据指针保存到接口设备中,以后用usb_get_intfdata来得到
					usb_set_intfdata(interface, dev);
					// 注册设备到USB核心
					retval = usb_register_dev(interface, &skel_class);
				}
			断开函数：
				当设备被拔出集线器时，usb子系统会自动地调用disconnect
				static void skel_disconnect(struct usb_interface *interface)
				{
				
				}
		 	
			系统模块
				------ {{{ 系统模块  --->  usb_skel_init ---> usb_register 注册驱动 }}} ------
				模块初始化函数	
					static int __init usb_skel_init(void)
					{
						// 驱动程序注册到USB子系统中
						result = usb_register(&skel_driver);
					}
				
				模块退出函数
					static void __exit usb_skel_exit(void)
					{
						// 从子系统注销驱动程序
						usb_deregister(&skel_driver);
					}
				
				注册到内核(以系统模块的方式注册)
					module_init(usb_skel_init);
					module_exit(usb_skel_exit);
					MODULE_LICENSE("GPL");
			
		// ------USB驱动的代码骨架------ }}}
	
		------------模拟给本机电脑发送“串口数据”（即：给本机电脑发送“串口中断信号”）------------------
		SSH 的机制：
			1、客户端使用tcp连接
			2、客户端使用SSL协议发送数据给服务端
			3、服务端接受到数据，服务端模拟发送串口数据（tty？）给Linux系统
			4、操作系统接受串口数据，识别、调用串口驱动的回调函数（探针函数）
			
		http://blog.sina.com.cn/s/blog_6cb543ef0100x90j.html
		
		查看输入设备
			cat /proc/bus/input/devices
			
		linux获得键盘鼠标事件，模拟键盘鼠标按键
			http://blog.chinaunix.net/uid-20666855-id-4905405.html	
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
