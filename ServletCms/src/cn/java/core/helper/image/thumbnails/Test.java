package cn.java.core.helper.image.thumbnails;

public class Test {

	
	public static String srcFilePath = "E:/Repository/EclipseRepository/ServletCms/WebRoot/upload/images/138257932533.jpg";
	public static String desFilePath = "E:/Repository/EclipseRepository/ServletCms/WebRoot/upload/images/138257932533_thumb.jpg";
	public static String logoFilePath = "E:/Repository/EclipseRepository/ServletCms/WebRoot/upload/images/logo.png";
	
	@org.junit.Test
	public void createThumbnail() {
		ThumbnailsHelper.createThumbnail(srcFilePath, desFilePath, 300, 300);
	}
	
	@org.junit.Test
	public void createWaterPrintByImg() {
		ThumbnailsHelper.createWaterPrintByImg(srcFilePath, desFilePath, logoFilePath, 300, 300);
	}

}
