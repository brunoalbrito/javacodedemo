package cn.java.codec.qrcode.jp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;

public class QRCodeUtils {
	private static String CHARSET_NAME = "UTF-8";
	private static int GRAPHICS2D_WIDTH = 139;
	private static int GRAPHICS2D_HEIGHT = 139;
	
	/**
	 * 编码字符串内容到目标File对象中
	 * 
	 * @param content 编码的内容
	 * @param destFile	生成file文件  1381090722   5029067275903
	 * @throws IOException
	 */
	private static BufferedImage renderQrCodeBufferedImage(String content)  {
		Qrcode qrcode = new Qrcode();
		/*
		  	表示的字符串长度： 容错率(ECC) 显示编码模式(EncodeMode)及版本(Version)有关
		  	二维码的纠错级别(排错率)，共有四级：可选L(7%)、M(15%)、Q(25%)、H(30%)(最高H)。
		 	纠错信息同样存储在二维码中，纠错级别越高，纠错信息占用的空间越多，那么能存储的有用信息就越少,对二维码清晰度的要求越小 
	  	*/
		qrcode.setQrcodeErrorCorrect('M');
		// 编码模式：Numeric 数字, Alphanumeric 英文字母,Binary 二进制,Kanji 汉字(第一个大写字母表示)
		qrcode.setQrcodeEncodeMode('B');	
		/*
		 	二维码的版本号：也象征着二维码的信息容量；二维码可以看成一个黑白方格矩阵，版本不同，矩阵长宽方向方格的总数量分别不同。
		 	1-40总共40个版本，版本1为21*21矩阵，版本每增1，二维码的两个边长都增4；
			版本2 为25x25模块，最高版本为是40，是177*177的矩阵；
		 */
		qrcode.setQrcodeVersion(7);
		
		byte[] contentByteArray;
		try {
			contentByteArray = content.getBytes(CHARSET_NAME); // 字符集
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}	
		
		// 图片尺寸,会根据version的变大，而变大，自己需要计算
		BufferedImage bufferedImage = new BufferedImage(GRAPHICS2D_WIDTH, GRAPHICS2D_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = bufferedImage.createGraphics(); // 创建图层
		graphics2D.setBackground(Color.WHITE);	// 设置背景颜色（白色）
		graphics2D.clearRect(0, 0, GRAPHICS2D_WIDTH, GRAPHICS2D_HEIGHT);	// 矩形 X、Y、width、height
		graphics2D.setColor(Color.BLACK);	// 设置图像颜色（黑色）
		
		// 设置偏移量，不设置可能导致二维码生产错误(解析失败出错)
		int pixoff = 2;
		
		// 二维码输出
		if (contentByteArray.length > 0 && contentByteArray.length < 123) {
			boolean[][] b = qrcode.calQrcode(contentByteArray);
			for (int i = 0; i < b.length; i++) {
				for (int j = 0; j < b.length; j++) {
					if (b[j][i]) {
						graphics2D.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
					}
				}
			}
		}
		else{
			throw new RuntimeException("the content bytes length not in [0,123].");
		}
//		Image img = ImageIO.read(new File("D:/tt.png"));  logo
//		g.drawImage(img, 25, 55,60,50, null);
				
		graphics2D.dispose(); // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象
		bufferedImage.flush(); // 刷新此 Image 对象正在使用的所有可重构的资源

		return bufferedImage;
		
	}
	
	public static void renderQrCodeToFileWithPngFormat(String content, File destFile)  {
		renderQrCodeToFile(content,"png",destFile);
	}
	
	/**
	 * 生成二维码带LOGO
	 * @param content
	 * @param logoFile
	 * @param destFile
	 */
	public static void renderQrCodeToFileMarkLogoWithPngFormat(String content,File logoFile, File destFile)  {
		BufferedImage bufferedImage = renderQrCodeBufferedImage(content);
		markLogoWithPngFormat(bufferedImage,logoFile,destFile);
	}
	
	/**
	 * 给图片打上logo
	 */
	public static void markLogoWithPngFormat(BufferedImage bufferedImage,File logoFile, File destFile)  {
		if(!logoFile.exists()){
			throw new RuntimeException("logoFile is not exists.");
		}
		try {
			BufferedImage bufferedImageTmp = new BufferedImage(bufferedImage.getWidth(null), bufferedImage.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = bufferedImageTmp.createGraphics(); // 创建画布
			
			// 画二维码
			graphics2D.drawImage(bufferedImage.getScaledInstance(bufferedImage.getWidth(null), bufferedImage.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);
			
			// 画logo
			BufferedImage bufferedImageLogo = ImageIO.read(logoFile);
			float logoDesWidth = (float) (GRAPHICS2D_WIDTH / 5.0);
			float scale = bufferedImageLogo.getWidth() / logoDesWidth;
			float logoDesHeigth = bufferedImageLogo.getHeight() / scale;
			float desX = (bufferedImage.getWidth() - logoDesWidth) / 2;
			graphics2D.drawImage(bufferedImageLogo,(int)desX, (int)desX,(int)logoDesWidth,(int)logoDesHeigth, null);
//			System.out.println(
//					"GRAPHICS2D_WIDTH = " + GRAPHICS2D_WIDTH
//					+ ",bufferedImageLogo.getWidth() = " + bufferedImageLogo.getWidth(null)	+",bufferedImageLogo.getHeight() = " + bufferedImageLogo.getHeight(null) 
//					+ ",logoDesWidth = " + logoDesWidth+",logoDesHeigth = "+ logoDesHeigth + ",scale = " + scale + ",desX = " + desX);
			graphics2D.dispose();
			bufferedImage.flush();
			bufferedImageTmp.flush();
			bufferedImageLogo.flush();
			ImageIO.write(bufferedImageTmp, "png",destFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 给图片打上logo
	 */
	public static void markLogoWithPngFormat(File srcFile,File logoFile, File destFile)  {
		if(!srcFile.exists()){
			throw new RuntimeException("srcFile is not exists.");
		}
		if(!logoFile.exists()){
			throw new RuntimeException("logoFile is not exists.");
		}
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(srcFile);
			markLogoWithPngFormat(bufferedImage,logoFile,destFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void renderQrCodeToStreamWithPngFormat(String content, OutputStream stream)  {
		renderQrCodeToStream(content,"png",stream);
	}
	
	public static void renderQrCodeToFile(String content,String formatName, File destFile)  {
		BufferedImage bufferedImage = renderQrCodeBufferedImage(content);
		try {
			ImageIO.write(bufferedImage, formatName, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void renderQrCodeToStream(String content,String formatName, OutputStream stream)  {
		BufferedImage bufferedImage = renderQrCodeBufferedImage(content);
		try {
			ImageIO.write(bufferedImage,formatName, stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解析二维码，返回解析内容
	 * 
	 * @param imageFile
	 * @return
	 */
	public static String qrCodeDecode(File imageFile) {
		String decodedData = null;
		QRCodeDecoder decoder = new QRCodeDecoder();
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

		try {
			decodedData = new String(decoder.decode(new J2SEImage(bufferedImage)), CHARSET_NAME);
		} catch (DecodingFailedException dfe) {
			throw new RuntimeException(dfe);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return decodedData;
	}
	
	static class J2SEImage implements QRCodeImage {
		BufferedImage image;

		public J2SEImage(BufferedImage image) {
			this.image = image;
		}

		public int getWidth() {
			return image.getWidth();
		}

		public int getHeight() {
			return image.getHeight();
		}

		public int getPixel(int x, int y) {
			return image.getRGB(x, y);
		}
	}
}

