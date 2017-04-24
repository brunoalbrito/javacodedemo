package cn.java.core.helper.image.jmagick;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;

import magick.CompositeOperator;
import magick.CompressionType;
import magick.DrawInfo;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;
import magick.PreviewType;

public class JMagicTemp {

	public static void main(String[] args) throws MagickException {

		//test for function imageResize   

		//        JMagicjWrapper.imageResize("pics.jpg", "reSize20x30.png", 20, 30);  
		//        JMagicjWrapper.imageResize("pics.jpg", "reSize250x200.jpeg", 250, 200);  
		//        JMagicjWrapper.imageResize("pics.jpg", "reSize50x50.jpg", 50, 50);  
		//        JMagicjWrapper.imageResize("pics.jpg", "reSize120x120.bmp", 120, 120);  
		//        JMagicjWrapper.imageResize("pics.jpg", "reSize.tif", 20, 30);//not create file   
		//          

		//test for function createWaterPrintByImg   
		JMagicTemp.createWaterPrintByImg("f://1.jpg", "f://4.jpg", "f://watermark.gif", new Point(0, 0));
		//JMagicjWrapper.imageResize("wpl.gif", "logo250x200.gif", 250, 200);  
		//Because file "wpl.gif" may not be release, the later function cause a error, can not open file handle.   
		//JMagicjWrapper.createWaterPrintByImg("pics.jpg", "wpl.gif", "logoFull.jpg", new Point(1680,1050));//not create file   
		//JMagicjWrapper.createWaterPrintByImg("pics.jpg", "wpl.gif", "logoExt.jpg", new Point(2000,1000));//not create file   

		//test for function createWaterPrintByText   
		//This function can not handle Chinese Character, I'll continue to takle the issue    
		//JMagicjWrapper.createWaterPrintByText("pics1.jpg", "wpt.gif", "For Test", new Point(300,300), 100);  
	}

	private static final String[] Type = { ".JPG", ".JPEG", ".BMP", ".GIF", ".PNG" };

	public static boolean checkType(String path) {
		for (int i = 0; i < Type.length; i++) {
			if (path.toUpperCase().endsWith(Type[i])) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * 改变图片大小
	 * 
	 * @param filePath 原图片位置 toPath 新图片位置 width 新图片的宽度 height 新图片的高度
	 * @return
	 * @throw
	 * @author sulliy@sina.com 2010-8-11
	 */
	public static void imageResize(String filePath, String toPath, int width, int height) throws MagickException {
		ImageInfo info = null;
		MagickImage image = null;
		Dimension imageDim = null;
		MagickImage scaled = null;

		if (!checkType(filePath) || !checkType(toPath)) {
			return;
		}

		try {
			info = new ImageInfo();
			image = new MagickImage(info);
			imageDim = image.getDimension();
			if (width <= 0 || height <= 0) {
				height = 120;
				width = 120;
			}
			scaled = image.scaleImage(width, height);
			scaled.setFileName(toPath);
			scaled.writeImage(info);
		} finally {
			if (scaled != null) {
				scaled.destroyImages();
			}
		}
	}

	/**
	 * 创建图片水印
	 * 
	 * @param filePath 源文件路径 toImg 生成文件位置 logoPath logo路径 pos
	 *            logo在源图片中的相对位置，以像素点为单位
	 * @return
	 * @throw MagickException
	 * @author sulliy@sina.com 2010-8-11
	 */
	public static void createWaterPrintByImg(String filePath, String toImg, String logoPath, Point pos) throws MagickException {
		if (!checkType(filePath) || !checkType(toImg) || !checkType(logoPath)) {
			return;
		}

		ImageInfo info = new ImageInfo();
		MagickImage fImage = null;
		MagickImage sImage = null;
		MagickImage fLogo = null;
		MagickImage sLogo = null;
		Dimension imageDim = null;
		Dimension logoDim = null;
		try {
			//原来图片
			fImage = new MagickImage(new ImageInfo(filePath));
			imageDim = fImage.getDimension();
			int width = imageDim.width;
			int height = imageDim.height;
			sImage = fImage.scaleImage(width, height);

			fLogo = new MagickImage(new ImageInfo(logoPath));
			logoDim = fLogo.getDimension();
			int lw = logoDim.width;
			int lh = logoDim.height;
			sLogo = fLogo.scaleImage(lw, lh);

			//开始打水印，从左上角开始;如果到右边界则重新开始一行的打印(x=0,y=y+h)
			int startX = 0;
			int startY = 0;
			do {
				sImage.compositeImage(CompositeOperator.AtopCompositeOp, sLogo, startX, startY);
				startX += (logoDim.width + 60);
				if (startX >= width) {
					startY += logoDim.height * 2;
					startX = 0;
				}
			} while (startY <= height);

			sImage.setFileName(toImg);
			sImage.writeImage(info);
		} finally {
			if (fImage != null) {
				fImage.destroyImages();
			}
			if (sImage != null) {
				sImage.destroyImages();
			}
			if (fLogo != null) {
				fLogo.destroyImages();
			}
			if (sLogo != null) {
				sLogo.destroyImages();
			}
		}
	}

	/**
	 * 创建文字水印
	 * 
	 * @param filePath 源文件路径 toImg 生成文件位置 text 水印文本 pos logo在源图片中的相对位置，以像素点为单位
	 *            pointSize 用于设置点阵大小
	 * @return
	 * @throw MagickException
	 * @author sulliy@sina.com 2010-8-11
	 */
	public static void createWaterPrintByText(String filePath, String toImg, String text, Point pos, int pointSize) throws MagickException {
		if (!checkType(filePath) || !checkType(toImg)) {
			return;
		}

		if (null == text || "".equals(text)) {
			text = "sulliy@sina.com";
		}

		ImageInfo info = new ImageInfo(filePath);
		if (filePath.toUpperCase().endsWith("JPG") || filePath.toUpperCase().endsWith("JPEG")) {
			info.setCompression(CompressionType.JPEGCompression); // 压缩类别为JPEG格式   
			info.setPreviewType(PreviewType.JPEGPreview); // 预览格式为JPEG格式   
			info.setQuality(95);
		}
		MagickImage aImage = new MagickImage(info);
		Dimension imageDim = aImage.getDimension();
		int width = imageDim.width;
		int height = imageDim.height;

		if (width <= (int) pos.getX() || height <= (int) pos.getY()) {
			pos.setLocation(0, 0);
		}

		int a = 0;
		int b = 0;
		String[] as = text.split("");
		for (String string : as) {
			if (string.matches("[/u4E00-/u9FA5]")) {
				a++;
			}
			if (string.matches("[a-zA-Z0-9]")) {
				b++;
			}
		}
		int tl = a * 12 + b * 6;//字符长度   
		MagickImage scaled = aImage.scaleImage(width, height);
		if (width > tl && height > 5) {
			DrawInfo aInfo = new DrawInfo(info);
			aInfo.setFill(PixelPacket.queryColorDatabase("white"));
			aInfo.setUnderColor(new PixelPacket(65535, 65535, 65535, 65535));//设置为透明颜色   
			aInfo.setPointsize(pointSize);
			// 解决中文乱码问题,自己可以去随意定义个自己喜欢字体，我在这用的微软雅黑   
			String fontPath = "C:/WINDOWS/Fonts/MSIMHEI.TTF";
			// String fontPath = "/usr/maindata/MSYH.TTF";   
			aInfo.setFont(fontPath);
			aInfo.setTextAntialias(true);
			aInfo.setOpacity(0);//透明度   
			aInfo.setText(text);
			aInfo.setGeometry("+" + ((int) pos.getX() + "+" + (int) pos.getY()));
			scaled.annotateImage(aInfo);
		}
		scaled.setFileName(toImg);
		scaled.writeImage(info);
		scaled.destroyImages();
	}

	/**
	 * 切取图片
	 * 
	 * @param imgPath 原图路径 toPath 生成文件位置 w 左上位置横坐标 h 左上位置竖坐标 x 右下位置横坐标 y 右下位置竖坐标
	 * @return
	 * @throw MagickException
	 * @author sulliy@sina.com 2010-8-11
	 */
	public static void cutImg(String imgPath, String toPath, int w, int h, int x, int y) throws MagickException {
		ImageInfo infoS = null;
		MagickImage image = null;
		MagickImage cropped = null;
		Rectangle rect = null;
		try {
			infoS = new ImageInfo(imgPath);
			image = new MagickImage(infoS);
			rect = new Rectangle(x, y, w, h);
			cropped = image.cropImage(rect);
			cropped.setFileName(toPath);
			cropped.writeImage(infoS);

		} finally {
			if (cropped != null) {
				cropped.destroyImages();
			}
		}
	}

	/**
	 * 删除图片文件
	 * 
	 * @param src 图片位置
	 * @return
	 * @throw
	 * @author sulliy@sina.com 2010-8-11
	 */
	public static boolean removeFile(String src) throws SecurityException {
		try {
			if (!checkType(src)) {
				return false;
			}

			File file = new File(src);
			return file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
