package cn.java.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String srcImgPath = "c:/1111.png";
		String iconPath = "c:/0439.jpg";
		String targerPath = "c:/c.png";
		// 给图片添加水印
		markImageByIcon(iconPath, srcImgPath, targerPath, -45);
	}

	/**
	 * 给图片添加水印
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		markImageByIcon(iconPath, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加水印、可设置水印图片旋转角度
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			// Graphics g= buffImg.getGraphics();
			Graphics2D graphics2D = buffImg.createGraphics(); // 创建画布

			// 设置对线段的锯齿状边缘处理
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);

			if (null != degree) {
				// 设置水印旋转
				graphics2D.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}
			// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);
			// 得到Image对象。
			Image img = imgIcon.getImage();
			float alpha = 0.2f; // 透明度
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 表示水印图片的位置
			graphics2D.drawImage(img, 150, 300, null);
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			graphics2D.dispose();
			os = new FileOutputStream(targerPath);
			// 生成图片
			ImageIO.write(buffImg, "JPG", os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
