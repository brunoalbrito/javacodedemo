package cn.java.security.mock;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Encrypter {
	private static String sSkey = "19800820";

	public static synchronized String encryptByMD5(String data) {
		try {
			MessageDigest nsae = MessageDigest.getInstance("MD5");
			nsae.update(data.getBytes());
			return encodeHex(nsae.digest());
		} catch (NoSuchAlgorithmException arg1) {
			arg1.printStackTrace();
			return null;
		}
	}

	public static String encryptByDES(String toEncryptText) {
		return encryptByDES(sSkey, toEncryptText);
	}

	public static String encryptByDES(String key, String toEncryptText) {
		String s2 = "";
		if (toEncryptText == "") {
			return s2;
		} else if (key == "") {
			return s2;
		} else {
			int i = toEncryptText.length();
			String s3 = encryptkey(key, i);

			for (int k = 0; k < i; ++k) {
				char c = toEncryptText.charAt(k);
				char c1 = s3.charAt(k);
				int j = c ^ c1;
				j += 29;
				j = 1000 + j / 10 % 10 * 100 + j / 100 * 10 + j % 10;
				s2 = s2 + Integer.toString(j).substring(1);
			}

			return s2;
		}
	}

	public static String decodeByDES(String toDecodeText) {
		return decodeByDES(sSkey, toDecodeText);
	}

	public static String decodeByDES(String key, String toDecodeText) {
		try {
			String nfe = "";
			if (toDecodeText == "") {
				return nfe;
			} else if (key == "") {
				return nfe;
			} else {
				int i = toDecodeText.length() / 3;
				String s3 = encryptkey(key, i);

				for (int l = 0; l < i; ++l) {
					String s4 = toDecodeText.substring(l * 3, (l + 1) * 3);
					char c1 = s3.charAt(l);
					int k = Integer.parseInt(s4);
					k = k / 10 % 10 * 100 + k / 100 * 10 + k % 10;
					int j = k - 29 ^ c1;
					char c = (char) j;
					nfe = nfe + c;
				}

				return nfe;
			}
		} catch (NumberFormatException arg11) {
			return "N/A";
		}
	}

	private static String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; ++i) {
			if ((bytes[i] & 255) < 16) {
				buf.append("0");
			}

			buf.append(Long.toString((long) (bytes[i] & 255), 16));
		}

		return buf.toString();
	}

	private static String encryptkey(String s, int i) {
		String s1 = s;
		int j = s.length();
		if (i <= j) {
			s1 = s.substring(0, i);
		} else {
			for (; j < i; j = s1.length()) {
				if (j + s.length() >= i) {
					s1 = s1 + s.substring(0, i - j);
				} else {
					s1 = s1 + s;
				}
			}
		}

		return s1;
	}
}

