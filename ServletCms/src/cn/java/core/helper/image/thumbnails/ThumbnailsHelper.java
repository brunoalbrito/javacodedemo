package cn.java.core.helper.image.thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import magick.MagickException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * http://search.maven.org/#artifactdetails|net.coobird|thumbnailator|0.4.8|jar
 * http://rensanning.iteye.com/blog/1545708
 * 
 * @author Administrator
 *
 */
public class ThumbnailsHelper {
	public static void main(String[] args) {
		try {

			OutputStream os = new FileOutputStream("E:/Repository/EclipseRepository/ServletCms/WebRoot/upload/images/138257932533_thumb.jpg");
			Thumbnails.of("E:/Repository/EclipseRepository/ServletCms/WebRoot/upload/images/138257932533.jpg").size(200, 200).toOutputStream(os);

			//asBufferedImage() 返回BufferedImage  
			BufferedImage thumbnail = Thumbnails.of("images/a380_1280x1024.jpg").size(1280, 1024).asBufferedImage();
			ImageIO.write(thumbnail, "jpg", new File("c:/a380_1280x1024_BufferedImage.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 生成缩略图
	 * 
	 * @param filePath
	 * @param toPath
	 * @param desWidth
	 * @throws MagickException
	 */
	public static void createThumbnail(String filePath, String toPath, int desWidth, int desHeigth) {
		try {
			Thumbnails.of(filePath).size(desWidth, desHeigth).toFile(toPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 水印（图片）
	 * 
	 * @param filePath
	 * @param toImg
	 * @param logoPath
	 * @param desWidth
	 * @throws MagickException
	 */
	public static void createWaterPrintByImg(String filePath, String toImg, String logoPath, int desWidth, int desHeigth) {
		try {
			Thumbnails.of(filePath).size(desWidth, desHeigth).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(logoPath)), 0.5f).outputQuality(0.8f).toFile(toImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切割图片
	 * 
	 * @param imgPath
	 * @param toPath
	 * @param w
	 * @param h
	 * @param x
	 * @param y
	 * @throws MagickException
	 */
	public static void cutImg(String imgPath, String toPath, int width, int heigth, int desWidth, int desHeigth) {
		//图片中心400*400的区域  
		try {
			Thumbnails.of(imgPath).sourceRegion(Positions.CENTER, width, heigth).size(desWidth, desHeigth).keepAspectRatio(false).toFile(toPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
