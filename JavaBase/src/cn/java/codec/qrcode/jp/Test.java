package cn.java.codec.qrcode.jp;

import java.io.File;

public class Test {


	public static void main(String[] args) {
		String filePath = Test.class.getResource("/cn/java/codec/qrcode/jp").getFile()+"/qrcode_jp.png";
		System.out.println("filePath = " + filePath);
		
		File qrFile = new File(filePath);

		// 二维码内容
		String encodeddata = "hello, qrcode jp.";
		try {
			QRCodeUtils.renderQrCodeToFileWithPngFormat(encodeddata, qrFile);
			System.out.println("Input Encoded data is：" + encodeddata);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		// 解码
		try {
			String reText = QRCodeUtils.qrCodeDecode(qrFile);
			System.out.println("Output Decoded Data is：" + reText);
			System.out.println(reText);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		System.out.println("----------带logo的二维码图片------------");
		// 带logo的二维码图片，解码器不准确
		{
			encodeddata = "test.test...";
			File qrFileWithLogo = new File(Test.class.getResource("/cn/java/codec/qrcode/jp").getFile()+"/qrcode_jp_withlogo.png");
			{
				File logoFile = new File(Test.class.getResource("/cn/java/codec/qrcode/jp").getFile()+"/qrcode_logo.png");
				QRCodeUtils.renderQrCodeToFileMarkLogoWithPngFormat(encodeddata,logoFile,qrFileWithLogo);
			}
			
			// 解码
			try {
				String reText = QRCodeUtils.qrCodeDecode(qrFileWithLogo);
				System.out.println("Output Decoded Data is：" + reText);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		
	}

}
