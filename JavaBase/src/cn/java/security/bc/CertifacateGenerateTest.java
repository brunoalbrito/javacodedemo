package cn.java.security.bc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 * http://blog.csdn.net/xtayfjpk/article/details/46402061
 * @author zhouzhian
 *
 */
public class CertifacateGenerateTest {
	private static Map<String, String> algorithmMap = new HashMap<>();
	private static final String ALGORITHM = "RSA";
	private static final String ALGORITHMS_SHA1WithRSA = "SHA1WithRSA";
	//	private static final String ALGORITHMS_SHA256WithRSA = "SHA256WithRSA";
	private static final String DN_USER = "CN=User,OU=development,O=Huawei,L=ShenZhen,ST=GuangDong,C=CN";  
    private static final String DN_CA = "CN=Kingyea,OU=Kingyea,O=Kingyea,L=GuangZou,ST=GuangDong,C=CN";  

	static {  
		// 算法名称与算法标识符映射   
		algorithmMap.put("1.2.840.113549.1.1.5", ALGORITHMS_SHA1WithRSA);  
		algorithmMap.put("1.2.840.113549.1.1.1", ALGORITHM);  
	}

	/** 
	 * 生成根证书公钥与私钥对 
	 */  
	public void testGenRootKeyPair() throws Exception {  
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);  
		keyPairGenerator.initialize(2048);  
		KeyPair keyPair = keyPairGenerator.generateKeyPair();  
		writeObject("d:/certtest/root.public", keyPair.getPublic());  
		writeObject("d:/certtest/root.private", keyPair.getPrivate());  
	}  


	/** 
	 * 生成用户证书公钥与私钥对 
	 * @throws Exception 
	 */  
	public void testUserKeyPair() throws Exception {  
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);  
		keyPairGenerator.initialize(2048);  
		KeyPair keyPair = keyPairGenerator.generateKeyPair();  
		writeObject("d:/certtest/user.public", keyPair.getPublic());  
		writeObject("d:/certtest/user.private", keyPair.getPrivate());  
	}  
	
	/**
	 * 生成证书序列号
	 */
	public static BigInteger genSerialNumber(){
		 return BigInteger.probablePrime(32, new Random());
	}
	
	/** 
     * 生成根证书(被BC废弃，但可以使用) 
     */  
    public void testGenRootCert() throws Exception {  
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();  
        //设置证书颁发者  
        certGen.setIssuerDN(new X500Principal(DN_CA));  
        //设置证书有效期  
        certGen.setNotAfter(new Date(System.currentTimeMillis()+ 100 * 24 * 60 * 60 * 1000));  
        certGen.setNotBefore(new Date());  
        //设置证书公钥  
        certGen.setPublicKey(getRootPublicKey());  
        //设置证书序列号  
        certGen.setSerialNumber(genSerialNumber());  
        //设置签名算法  
        certGen.setSignatureAlgorithm(ALGORITHMS_SHA1WithRSA);  
        //设置证书使用者  
        certGen.setSubjectDN(new X500Principal(DN_CA));  
        //使用私钥生成证书，主要是为了进行签名操作  
        X509Certificate certificate = certGen.generate(getRootPrivateKey());  
        PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier)certificate;  
        bagAttr.setBagAttribute(  
                PKCSObjectIdentifiers.pkcs_9_at_friendlyName,  
                new DERBMPString("Root Coperation Certificate"));  
        writeFile("d:/certtest/ca.cer", certificate.getEncoded());  
    }
    
    /** 
     * 生成根证书的另外一种方式 
     * @throws Exception 
     */  
    public void testGenRootCertWithBuilder() throws Exception {  
        final AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(ALGORITHMS_SHA1WithRSA);  
        final AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);  
          
        PublicKey publicKey = getRootPublicKey();  
        PrivateKey privateKey = getRootPrivateKey();  
          
          
        X500Name issuer = new X500Name(DN_CA);  
        BigInteger serial = genSerialNumber();  
        Date notBefore = new Date();  
        Date notAfter = new Date(System.currentTimeMillis()+ 100 * 24 * 60 * 60 * 1000);  
        X500Name subject = new X500Name(DN_CA);  
          
          
        AlgorithmIdentifier algId = AlgorithmIdentifier.getInstance(PKCSObjectIdentifiers.rsaEncryption.toString());  
        System.out.println(algId.getAlgorithm());  
        AsymmetricKeyParameter publicKeyParameter = PublicKeyFactory.createKey(publicKey.getEncoded());  
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKeyParameter);  
        //此种方式不行，生成证书不完整  
        //SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(algId, publicKey.getEncoded());  
        X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKeyInfo);  
          
        BcRSAContentSignerBuilder contentSignerBuilder = new BcRSAContentSignerBuilder(sigAlgId, digAlgId);  
        AsymmetricKeyParameter privateKeyParameter = PrivateKeyFactory.createKey(privateKey.getEncoded());  
        ContentSigner contentSigner = contentSignerBuilder.build(privateKeyParameter);  
          
        X509CertificateHolder certificateHolder = x509v3CertificateBuilder.build(contentSigner);  
        Certificate certificate = certificateHolder.toASN1Structure();  
        writeFile("H:/certtest/ca.cer", certificate.getEncoded());  
    }  
    
    /** 
     * 生成用户证书 
     */  
    public void testGenUserCert() throws Exception {  
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();  
        certGen.setIssuerDN(new X500Principal(DN_CA));  
        certGen.setNotAfter(new Date(System.currentTimeMillis()+ 100 * 24 * 60 * 60 * 1000));  
        certGen.setNotBefore(new Date());  
        certGen.setPublicKey(getUserPublicKey());  
        certGen.setSerialNumber(genSerialNumber());  
        certGen.setSignatureAlgorithm(ALGORITHMS_SHA1WithRSA);  
        certGen.setSubjectDN(new X500Principal(DN_USER));  
        X509Certificate certificate = certGen.generate(getRootPrivateKey());  
          
        writeFile("H:/certtest/User.cer", certificate.getEncoded());  
    }  
      
      
    /** 
     * 验证根证书签名 
     */  
    public void testVerifyRootCert() throws Exception {  
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");  
        FileInputStream inStream = new FileInputStream("d:/certtest/ca.cer");  
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inStream);  
        System.out.println(certificate);  
        Signature signature = Signature.getInstance(certificate.getSigAlgName());  
        signature.initVerify(certificate);  
        signature.update(certificate.getTBSCertificate());  
        boolean legal = signature.verify(certificate.getSignature());  
        System.out.println(legal);  
    }  
      
    /** 
     * 验证用户证书签名 
     */  
    public void testVerifyUserCert() throws Exception {  
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");  
        FileInputStream inStream = new FileInputStream("H:/certtest/User.cer");  
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inStream);  
        System.out.println(certificate.getPublicKey().getClass());  
        Signature signature = Signature.getInstance(certificate.getSigAlgName());  
        signature.initVerify(getRootPublicKey());  
        signature.update(certificate.getTBSCertificate());  
        boolean legal = signature.verify(certificate.getSignature());  
        System.out.println(legal);  
    }  
      
      
    /** 
     * 生成证书请求文件 
     */  
    public void testGenCSR() throws Exception {  
        X500Name subject = new X500Name(DN_USER);  
        AsymmetricKeyParameter keyParameter = PrivateKeyFactory.createKey(getUserPrivateKey().getEncoded());  
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(keyParameter);  
        PKCS10CertificationRequestBuilder certificationRequestBuilder = new PKCS10CertificationRequestBuilder(subject, publicKeyInfo);  
        final AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(ALGORITHMS_SHA1WithRSA);  
        final AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);  
        BcRSAContentSignerBuilder contentSignerBuilder = new BcRSAContentSignerBuilder(sigAlgId, digAlgId);  
        PKCS10CertificationRequest certificationRequest = certificationRequestBuilder.build(contentSignerBuilder.build(keyParameter));  
        System.out.println(certificationRequest);  
        writeFile("d:/certtest/User.csr", certificationRequest.getEncoded());  
    }  
      
    /** 
     * 根据证书请求文件生成用户证书，其实主要是使用根证书私钥为其签名 
     */  
    public void testUserCertWithCSR() throws Exception {  
        byte[] encoded = readFile("d:/certtest/User.csr");  
        PKCS10CertificationRequest certificationRequest = new PKCS10CertificationRequest(encoded);  
          
          
        RSAKeyParameters parameter = (RSAKeyParameters) PublicKeyFactory.createKey(certificationRequest.getSubjectPublicKeyInfo());  
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(parameter.getModulus(), parameter.getExponent());  
        String algorithm = algorithmMap.get(certificationRequest.getSubjectPublicKeyInfo().getAlgorithm().getAlgorithm().toString());  
        PublicKey publicKey = KeyFactory.getInstance(algorithm).generatePublic(keySpec);  
        System.out.println(certificationRequest.getSubject());  
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();  
        certGen.setIssuerDN(new X500Principal(DN_CA));  
        certGen.setNotAfter(new Date(System.currentTimeMillis()+ 100 * 24 * 60 * 60 * 1000));  
        certGen.setNotBefore(new Date());  
          
        certGen.setPublicKey(publicKey);  
        certGen.setSerialNumber(genSerialNumber());  
        certGen.setSignatureAlgorithm(algorithmMap.get(certificationRequest.getSignatureAlgorithm().getAlgorithm().toString()));  
        certGen.setSubjectDN(new X500Principal(certificationRequest.getSubject().toString()));  
        X509Certificate certificate = certGen.generate(getRootPrivateKey());  
          
        writeFile("d:/certtest/User.cer", certificate.getEncoded());  
          
    }  
      
    public PrivateKey getRootPrivateKey() throws Exception {  
        return PrivateKey.class.cast(readKey("d:/certtest/root.private"));  
    }  
    public PublicKey getRootPublicKey() throws Exception {  
        return PublicKey.class.cast(readKey("d:/certtest/root.public"));  
    }  
      
    public PrivateKey getUserPrivateKey() throws Exception {  
        return PrivateKey.class.cast(readKey("d:/certtest/user.private"));  
    }  
    public PublicKey getUserPublicKey() throws Exception {  
        return PublicKey.class.cast(readKey("d:/certtest/user.public"));  
    }  
      
      
    public byte[] readFile(String path) throws Exception {  
        FileInputStream cntInput = new FileInputStream(path);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        int b = -1;  
        while((b=cntInput.read())!=-1) {  
            baos.write(b);  
        }  
        cntInput.close();  
        byte[] contents = baos.toByteArray();  
        baos.close();  
        return contents;  
    }  
      
    public void writeFile(String path, byte[] content) throws Exception {  
        FileOutputStream fos = new FileOutputStream(path);  
        fos.write(content);  
        fos.close();  
    }  
      
    public void writeObject(String path, Object object) throws Exception {  
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));  
        oos.writeObject(object);  
        oos.close();  
    }
      
    public Object readObject(String path) throws Exception {  
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));  
        Object obj = ois.readObject();  
        ois.close();  
        return obj;  
    }  
      
    public Key readKey(String path) throws Exception {  
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));  
        Key key = Key.class.cast(ois.readObject());  
        ois.close();  
        return key;  
    }  
}

