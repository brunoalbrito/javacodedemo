package cn.java.security.bc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.x509.X509V3CertificateGenerator;
/**
 * @author zhouzhian
 *
 */
public class SecurityUtil {
	/**
	 * 消息摘要
	 * @author zhouzhian
	 *
	 */
	public static class MessageDigestUtil {

		static{
			// 只需要 bcprov-jdk15on-156.jar 包
			// 添加 bouncycastle 的扩展包，支持md4算法
			Security.addProvider(new BouncyCastleProvider());
		}

		public static byte[] digestMd4(String content) throws Exception {
			// 需要 bcprov-jdk15on-156.jar 包的支持
			MessageDigest messageDigest = MessageDigest.getInstance("MD4");
			messageDigest.update(content.getBytes());
			return messageDigest.digest();
		}

		public static byte[] digestMd5(String content, boolean isMd5) throws Exception {
			MessageDigest messageDigest = null;
			messageDigest = MessageDigest.getInstance("MD5");
			return messageDigest.digest(content.getBytes());
		}
		public static byte[] digestSha(String content, boolean isMd5) throws Exception {
			MessageDigest messageDigest = null;
			messageDigest = MessageDigest.getInstance("SHA");
			return messageDigest.digest(content.getBytes());
		}
	}

	public static class CertificateUtil {
		private static final String ALGORITHM = "RSA";
		private static final String ALGORITHMS_SHA1WithRSA = "SHA1WithRSA";
		private static final String ALGORITHMS_SHA256WithRSA = "SHA256WithRSA";
		private static String getAlgorithms(boolean isRsa2) {
			return isRsa2 ? ALGORITHMS_SHA256WithRSA : ALGORITHMS_SHA1WithRSA;
		}

		static {
			// 需要 bcprov-jdk15on-156.jar/bcpkix-jdk15on-156.jar 包
			Security.addProvider(new BouncyCastleProvider()); 
		}
		
		public static KeyPair genKeyPair() throws NoSuchAlgorithmException{
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.genKeyPair();
			return keyPair;
		}
		
		/**
		 * 生成证书和私钥
		 * openssl req -new -nodes -sha256 -newkey rsa:2048 -keyout myprivate.key -out mydomain.csr
		 */
		public static void genX509v3Certificate(boolean isSha256WithRSAEncryption) throws Exception{
			
			// 设置开始日期和结束日期
			long year = 1; // 一年
			Date notBefore = new Date();
			Date notAfter = new Date(notBefore.getTime() + (year * 360 * 24 * 60 * 60 * 1000));

			// 设置颁发者和主题
			String issuerString = "CN=root,OU=单位,O=组织"; // 证书颁发者
			X500Name issueDn = new X500Name(issuerString);
			String subject = "CN=root,OU=单位,O=组织"; // 证书主题
			X500Name subjectDn = new X500Name(subject);

			// 证书序列号
			BigInteger serail = BigInteger.probablePrime(32, new Random());

			// 产生RSA算法密钥对
			KeyPair keyPair = genKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			// 公钥信息
			SubjectPublicKeyInfo subjectPublicKeyInfo = null;
			subjectPublicKeyInfo = SubjectPublicKeyInfo
					.getInstance(new ASN1InputStream(publicKey.getEncoded())
							.readObject());


			// 对公钥进行签名
			final byte[] signatureData ;
			java.security.Signature signature = Signature.getInstance(getAlgorithms(isSha256WithRSAEncryption));
			signature.initSign(privateKey); // 私钥
			signature.update(publicKey.getEncoded()); // 进行签名
			signatureData = signature.sign();

			// 证书 builder
			X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
					issueDn, serail, notBefore, notAfter, subjectDn,
					subjectPublicKeyInfo);

			// 创建证书
			X509CertificateHolder holder = builder.build(new ContentSigner() {
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				@Override
				public byte[] getSignature() {
					try {
						buf.write(signatureData);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return signatureData;
				}

				@Override
				public OutputStream getOutputStream() {
					return buf;
				}

				@Override
				public AlgorithmIdentifier getAlgorithmIdentifier() {
					if(isSha256WithRSAEncryption){
						return AlgorithmIdentifier.getInstance(PKCSObjectIdentifiers.sha256WithRSAEncryption.toString()); // 加密算法ID
					}
					else{
						return AlgorithmIdentifier.getInstance(PKCSObjectIdentifiers.sha1WithRSAEncryption.toString()); // 加密算法ID
					}
				}
			});
			
			byte[] certBuf = holder.getEncoded();
			
			// 生成证书
			X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X509").
					generateCertificate(new ByteArrayInputStream(certBuf));
			
			System.out.println(certificate);
			byte[] certBytes = certificate.getEncoded();
					
			// 证书base64编码字符串
			System.out.println(Base64.getEncoder().encodeToString(certBytes));
		}
	}

}
