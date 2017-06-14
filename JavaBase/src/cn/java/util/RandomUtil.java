package cn.java.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
	public static String randomUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	public static String randomUUIDWithoutDelimiter() {
		UUID uuid = UUID.randomUUID();
		String randomUUIDStr = uuid.toString();
		// 去掉"-"符号
		return randomUUIDStr.substring(0, 8) + randomUUIDStr.substring(9, 13) + randomUUIDStr.substring(14, 18) + randomUUIDStr.substring(19, 23) + randomUUIDStr.substring(24);
	}
	
	public static String getRandomString(int length){  
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
          
        for(int i = 0 ; i < length; ++i){  
            int number = random.nextInt(62);//[0,62)  
            sb.append(str.charAt(number));  
        }  
        return sb.toString();  
    }  
	
	/**
	 * 数字 + 字母
	 */
	public static String randomAlphanumeric(int minLengthInclusive, int maxLengthExclusive){  
		return RandomStringUtils.randomAlphanumeric(minLengthInclusive,maxLengthExclusive);
	}  
	
	/**
	 * 字母
	 */
	public static String randomAlphabetic(int minLengthInclusive, int maxLengthExclusive){  
		return RandomStringUtils.randomAlphabetic(minLengthInclusive,maxLengthExclusive);
	}  
	
	/**
	 * 数字
	 */
	public static String randomNumeric(int minLengthInclusive, int maxLengthExclusive){  
		return RandomStringUtils.randomNumeric(minLengthInclusive,maxLengthExclusive);
	}  
	
	/**
	 * ascii打印字符(32, 127)
	 */
	public static String randomAscii(int minLengthInclusive, int maxLengthExclusive){  
		return RandomStringUtils.randomAscii(minLengthInclusive,maxLengthExclusive);
	}  
	
	/**
	 * ascii打印字符(33, 126)
	 */
	public static String randomGraph(int minLengthInclusive, int maxLengthExclusive){  
		return RandomStringUtils.randomGraph(minLengthInclusive,maxLengthExclusive);
	}  
}
