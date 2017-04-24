package cn.java.core.helper.image.jmagick;

import java.awt.Dimension;
import java.awt.Rectangle;

import magick.CompositeOperator;
import magick.CompressionType;
import magick.DrawInfo;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;
import magick.PreviewType;

/*
 jar包地址  http://downloads.jmagick.org/6.3.9/      
 我在window下使用的是    jmagick-win-6.3.9-Q8.zip 和  ImageMagick-6.3.9-0-Q8-windows-dll.exe
 linux下使用的是         JMagick-6.2.6-0.tar.gz    和 ImageMagick-x86_64-pc-linux-gnu.tar.gz             
 doc地址   http://downloads.jmagick.org/jmagick-doc/
 ----------
 windows
 ----------
 一 .  安装 ImageMagick-6.3.9-0-Q8-windows-dll.exe
 二 .  将 安装目录下 “C:/Program Files/ImageMagick-6.3.9-Q8/ ”(按自己所安装的目录找) 下的所有dll文件 copy 到系统盘下的 “C:/WINDOWS/system32/”文件夹里
 三 .  配置环境变量      再环境变量path里添加新的值 “C:/Program Files/ImageMagick-6.3.9-Q8”
 四 .  解压jmagick-win-6.3.9-Q8.zip 
 将 jmagick.dll 复制到系统盘下的 “C:/WINDOWS/system32/”文件夹里 和 复制到jdk的bin(例“E:/Java/jdk1.5.0_10/bin”)文件里各一份 
 将 jmagick.jar 复制到Tomcat下的lib文件夹里 和 所使用项目的WEB-INF下lib文件里 各一份
 ----------	        
 linux
 ----------
 */
public class JmagickHelper {
	static {
		//不能漏掉这个，不然jmagick.jar的路径找不到
		System.setProperty("jmagick.systemclassloader", "no");
	}

	/**
	 * 压缩图片
	 * 
	 * @param filePath
	 * @param toPath
	 * @throws MagickException
	 */
	public static void createThumbnail(String filePath, String toPath) throws MagickException {
		createThumbnail(filePath, toPath, 660);
	}

	/**
	 * 压缩图片
	 * 
	 * @param filePath 源文件路径
	 * @param toPath 缩略图路径
	 */
	public static void createThumbnail(String filePath, String toPath, int desWidth) throws MagickException {
		ImageInfo info = null;
		MagickImage srcMagickImage = null;
		Dimension imageDim = null;
		MagickImage newMagickImage = null;
		try {
			info = new ImageInfo(filePath);
			srcMagickImage = new MagickImage(info);
			imageDim = srcMagickImage.getDimension();
			int width = imageDim.width;
			int height = imageDim.height;
			if (width > height) {
				height = desWidth * height / width;
				width = desWidth;
			}
			//小图片
			newMagickImage = srcMagickImage.scaleImage(width, height);//小图片文件的大小.
			newMagickImage.setFileName(toPath);
			newMagickImage.writeImage(info);
		} finally {
			if (srcMagickImage != null) {
				srcMagickImage.destroyImages();
			}
			if (newMagickImage != null) {
				newMagickImage.destroyImages();
			}
		}
	}

	/**
	 * 水印(图片logo)
	 * 
	 * @param filePath
	 * @param toImg
	 * @param logoPath
	 * @throws MagickException
	 */
	public static void createWaterPrintByImg(String filePath, String toImg, String logoPath) throws MagickException {
		createWaterPrintByImg(filePath, toImg, logoPath, 660);
	}

	/**
	 * 水印(图片logo)
	 * 
	 * @param filePath 源文件路径
	 * @param toImg 修改图路径
	 * @param logoPath logo图路径
	 * @throws MagickException
	 */
	public static void createWaterPrintByImg(String filePath, String toImg, String logoPath, int desWidth) throws MagickException {
		ImageInfo info = new ImageInfo();
		MagickImage srcMagickImage = null;
		MagickImage newMagickImage = null;
		MagickImage srcLogoMagickImage = null;
		MagickImage newLogoMagickImage = null;
		Dimension srcDimension = null;
		Dimension srcLogoDimension = null;
		try {
			srcMagickImage = new MagickImage(new ImageInfo(filePath));
			srcDimension = srcMagickImage.getDimension();
			int width = srcDimension.width;
			int height = srcDimension.height;
			if (width > desWidth) {
				height = desWidth * height / width;
				width = desWidth;
			}
			newMagickImage = srcMagickImage.scaleImage(width, height);

			srcLogoMagickImage = new MagickImage(new ImageInfo(logoPath));
			srcLogoDimension = srcLogoMagickImage.getDimension();
			int logoWidth = width / 8;
			int logoHeight = srcLogoDimension.height * logoWidth / srcLogoDimension.width;
			newLogoMagickImage = srcLogoMagickImage.scaleImage(logoWidth, logoHeight);

			newMagickImage.compositeImage(CompositeOperator.AtopCompositeOp, newLogoMagickImage, width - (logoWidth + logoHeight / 10), height - (logoHeight + logoHeight / 10));
			newMagickImage.setFileName(toImg);
			newMagickImage.writeImage(info);
		} finally {
			if (srcMagickImage != null) {
				srcMagickImage.destroyImages();
			}
			if (newMagickImage != null) {
				newMagickImage.destroyImages();
			}
			if (srcLogoMagickImage != null) {
				srcLogoMagickImage.destroyImages();
			}
			if (newLogoMagickImage != null) {
				newLogoMagickImage.destroyImages();
			}
		}
	}

	/**
	 * 水印(文字)
	 * @param filePath
	 * @param toImg
	 * @param text
	 * @throws MagickException
	 */
	public static void createWaterPrintByText(String filePath, String toImg, String text) throws MagickException {
		createWaterPrintByText(filePath, toImg, text, 660);
	}

	/**
	 * 水印(文字)
	 * 
	 * @param filePath 源文件路径
	 * @param toImg 修改图路径
	 * @param text 名字(文字内容自己随意)
	 * @throws MagickException
	 */
	public static void createWaterPrintByText(String filePath, String toImg, String text, int desWidth) throws MagickException {
		ImageInfo info = new ImageInfo(filePath);
		if (filePath.toUpperCase().endsWith("JPG") || filePath.toUpperCase().endsWith("JPEG")) {
			info.setCompression(CompressionType.JPEGCompression); //压缩类别为JPEG格式
			info.setPreviewType(PreviewType.JPEGPreview); //预览格式为JPEG格式
			info.setQuality(95);
		}
		MagickImage srcMagickImage = new MagickImage(info);
		Dimension srcDimension = srcMagickImage.getDimension();
		int width = srcDimension.width;
		int height = srcDimension.height;
		if (width > desWidth) {
			height = desWidth * height / width;
			width = desWidth;
		}
		int a = 0;
		int b = 0;
		String[] as = text.split("");
		for (String string : as) {
			if (string.matches("[\u4E00-\u9FA5]")) {
				a++;
			}
			if (string.matches("[a-zA-Z0-9]")) {
				b++;
			}
		}
		int tl = a * 12 + b * 6 + 300;
		MagickImage newMagickImage = srcMagickImage.scaleImage(width, height);
		if (width > tl && height > 5) {
			DrawInfo drawInfoTemp = new DrawInfo(info);
			drawInfoTemp.setFill(PixelPacket.queryColorDatabase("white"));
			drawInfoTemp.setUnderColor(new PixelPacket(0, 0, 0, 100));
			drawInfoTemp.setPointsize(12);
			//解决中文乱码问题,自己可以去随意定义个自己喜欢字体，我在这用的微软雅黑
			String fontPath = "C:/WINDOWS/Fonts/MSYH.TTF";
			// String fontPath = "/usr/maindata/MSYH.TTF";
			drawInfoTemp.setFont(fontPath);
			drawInfoTemp.setTextAntialias(true);
			drawInfoTemp.setOpacity(0);
			drawInfoTemp.setText(text);//写入文章
			//	drawInfoTemp.setText("　" + text + "于　" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "　上传于XXXX网，版权归作者所有！");
			drawInfoTemp.setGeometry("+" + (width - tl) + "+" + (height - 5));//设置坐标
			newMagickImage.annotateImage(drawInfoTemp);
		}
		newMagickImage.setFileName(toImg);
		newMagickImage.writeImage(info);
		newMagickImage.destroyImages();
	}

	/**
	 * 切图
	 * 
	 * @param imgPath 源图路径
	 * @param toPath 修改图路径
	 * @param width
	 * @param heigth
	 * @param posX
	 * @param posY
	 * @throws MagickException
	 */
	public static void cutImg(String imgPath, String toPath, int width, int heigth, int posX, int posY) throws MagickException {
		ImageInfo imageInfo = null;
		MagickImage srcMagickImage = null;
		MagickImage newMagickImage = null;
		Rectangle rect = null;
		try {
			imageInfo = new ImageInfo(imgPath);
			srcMagickImage = new MagickImage(imageInfo);
			rect = new Rectangle(posX, posY, width, heigth);
			newMagickImage = srcMagickImage.cropImage(rect);
			newMagickImage.setFileName(toPath);
			newMagickImage.writeImage(imageInfo);
		} finally {
			if (newMagickImage != null) {
				newMagickImage.destroyImages();
			}
		}
	}
}