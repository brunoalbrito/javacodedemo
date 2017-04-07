package cn.java.socket.minshengbank;

import org.bouncycastle.util.Arrays;

public class Message {
	private String encodedType = "";
	private String tradeCode = "";
	private String xmlSignStr = "";
	private String xmlEncryptStr = "";

	public String getEncodedType() {
		return encodedType;
	}

	public void setEncodedType(String encodedType) {
		this.encodedType = encodedType;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getXmlSignStr() {
		return xmlSignStr;
	}

	public void setXmlSignStr(String xmlSignStr) {
		this.xmlSignStr = xmlSignStr;
	}

	public String getXmlEncryptStr() {
		return xmlEncryptStr;
	}

	public void setXmlEncryptStr(String xmlEncryptStr) {
		this.xmlEncryptStr = xmlEncryptStr;
	}

	/**
	 * 转成消息对象
	 * @param bytesSrc
	 * @return
	 */
	public static Message toMessage(byte[] bytesSrc){
		byte[] totalLengthBytesDest = new byte[8]; // 总长度
		System.arraycopy(bytesSrc, 0, totalLengthBytesDest, 0, totalLengthBytesDest.length);
		long totalLength = ConvertUtil.bytesToLong(totalLengthBytesDest); // 总长度
		
		byte[] encodedTypeBytesDest = new byte[15]; // 合作方编码
		System.arraycopy(bytesSrc, totalLengthBytesDest.length, encodedTypeBytesDest, 0, encodedTypeBytesDest.length);
		String encodedType =  ConvertUtil.bytesToString(encodedTypeBytesDest); 
		
		byte[] tradeCodeBytesDest = new byte[8]; // 交易服务码
		System.arraycopy(bytesSrc, totalLengthBytesDest.length + encodedTypeBytesDest.length, tradeCodeBytesDest, 0, tradeCodeBytesDest.length);
		String tradeCode =  ConvertUtil.bytesToString(encodedTypeBytesDest); 
	
		byte[] xmlSignStrLengthBytesDest = new byte[8]; // 交易服务码
		System.arraycopy(bytesSrc, totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length, xmlSignStrLengthBytesDest, 0, xmlSignStrLengthBytesDest.length);
		int xmlSignStrLength =  ConvertUtil.bytesToInt(xmlSignStrLengthBytesDest); 
		
		byte[] xmlSignStrBytesDest = new byte[xmlSignStrLength]; //  该域是整个XML报文明文参与签名后的值
		System.arraycopy(bytesSrc, totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length + xmlSignStrLengthBytesDest.length, xmlSignStrBytesDest, 0, xmlSignStrBytesDest.length);
		String xmlSignStr =  ConvertUtil.bytesToString(xmlSignStrLengthBytesDest); 
		
		long remainLength = totalLength  - totalLengthBytesDest.length - encodedTypeBytesDest.length - tradeCodeBytesDest.length - xmlSignStrLengthBytesDest.length;
		byte[] xmlEncryptStrBytesDest = new byte[(int) remainLength]; //  该域是整个XML报文明文参与加密后的值
		System.arraycopy(bytesSrc, totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length + xmlSignStrLengthBytesDest.length + xmlSignStrBytesDest.length, xmlEncryptStrBytesDest, 0, xmlEncryptStrBytesDest.length);
		String xmlEncryptStr =  ConvertUtil.bytesToString(xmlSignStrLengthBytesDest);
				
		Message message =  new Message();
		message.setEncodedType(encodedType);
		message.setTradeCode(tradeCode);
		message.setXmlEncryptStr(xmlEncryptStr);
		message.setXmlSignStr(xmlSignStr);
		return message;
	}
	
	/**
	 * 转成字节数组
	 * @param message
	 * @return
	 */
	public static byte[] toBytes(Message message){
		// 结构定义
		byte[] totalLengthBytesDest = new byte[8]; // 报文总长度，固定8个字节,位数不足左补“0”。
		byte[] encodedTypeBytesDest = new byte[15]; // 合作方编码，固定15个字节，位数不足左补空格。
		byte[] tradeCodeBytesDest = new byte[8]; // 交易服务码，固定8个字节，位数不足左补空格。
		byte[] xmlSignStrLengthBytesDest = new byte[4]; // 固定4个字节，位数不足左补“0”。
		byte[] xmlSignStrBytesDest = message.getXmlSignStr().getBytes(); //  该域是整个XML报文明文参与签名后的值
		byte[] xmlEncryptStrBytesDest = message.getXmlEncryptStr().getBytes(); //  该域是整个XML报文明文参与加密后的值

		// 初始化
		Arrays.fill(totalLengthBytesDest,(byte) 0);	
		Arrays.fill(encodedTypeBytesDest,(byte) ' ');	
		Arrays.fill(tradeCodeBytesDest,(byte) ' ');	
		Arrays.fill(xmlSignStrLengthBytesDest,(byte) 0);

		// 填充实际值
		long totalLength = 0L; // 数据总长度
		totalLength = totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length + xmlSignStrLengthBytesDest.length + xmlSignStrBytesDest.length + xmlEncryptStrBytesDest.length;
		byte[] totalLengthBytesSrc = ConvertUtil.longToBytes(totalLength); // 总长度
		System.arraycopy(totalLengthBytesSrc, 0, totalLengthBytesDest, totalLengthBytesDest.length - totalLengthBytesSrc.length, totalLengthBytesSrc.length);
		byte[] encodedTypeBytesSrc = message.getEncodedType().getBytes(); // 编码类型
		System.arraycopy(encodedTypeBytesSrc, 0, encodedTypeBytesDest, encodedTypeBytesDest.length - encodedTypeBytesSrc.length, encodedTypeBytesSrc.length);
		byte[] tradeCodeBytesSrc = message.getTradeCode().getBytes(); // 交易类型
		System.arraycopy(tradeCodeBytesSrc, 0, tradeCodeBytesDest, tradeCodeBytesDest.length - tradeCodeBytesSrc.length, tradeCodeBytesSrc.length);
		byte[] xmlSignStrLengthBytesSrc = ConvertUtil.intToBytes(xmlSignStrBytesDest.length); // 签名长度
		System.arraycopy(xmlSignStrLengthBytesSrc, 0, xmlSignStrLengthBytesDest, xmlSignStrLengthBytesDest.length - xmlSignStrLengthBytesSrc.length, xmlSignStrLengthBytesSrc.length);

		// 合并所有数据
		byte[] allBytesDest = new byte[(int) totalLength];
		System.arraycopy(totalLengthBytesDest, 0, allBytesDest,0, totalLengthBytesDest.length);
		System.arraycopy(encodedTypeBytesDest, 0, allBytesDest,totalLengthBytesDest.length, encodedTypeBytesDest.length);
		System.arraycopy(tradeCodeBytesDest, 0, allBytesDest,totalLengthBytesDest.length + encodedTypeBytesDest.length, tradeCodeBytesDest.length);
		System.arraycopy(xmlSignStrLengthBytesDest, 0, allBytesDest,totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length, xmlSignStrLengthBytesDest.length);
		System.arraycopy(xmlSignStrBytesDest, 0, allBytesDest,totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length + xmlSignStrLengthBytesDest.length, xmlSignStrBytesDest.length);
		System.arraycopy(xmlEncryptStrBytesDest, 0, allBytesDest,totalLengthBytesDest.length + encodedTypeBytesDest.length + tradeCodeBytesDest.length + xmlSignStrLengthBytesDest.length + xmlSignStrBytesDest.length, xmlEncryptStrBytesDest.length);
		return allBytesDest;
	}

}
