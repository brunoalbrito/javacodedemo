package cn.java.codec.qrcode.google;

import java.io.File;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class Test {
	public static void main(String[] args) throws Exception {
		
		String text = "hello, qrcode google."; // 二维码内容
		int width = 300; // 二维码图片宽度
		int height = 300; // 二维码图片高度
		String format = "gif";// 二维码的图片格式
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用字符集编码
		
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		
		
		// 生成二维码
		String filePath = Test.class.getResource("/cn/java/codec/qrcode/jp").getFile()+"/qrcode_google.png";
		System.out.println("filePath = " + filePath);
		File outputFile = new File(filePath);
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
	}
}