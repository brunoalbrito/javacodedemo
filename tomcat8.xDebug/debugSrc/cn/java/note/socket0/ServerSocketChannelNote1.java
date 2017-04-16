package cn.java.note.socket0;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CountDownLatch;

import org.apache.tomcat.util.net.NioSelectorPool;

public class ServerSocketChannelNote1 {
	private volatile CountDownLatch stopLatch = null;
	private NioSelectorPool selectorPool = new NioSelectorPool();
	public  void note1() throws IOException {
		ServerSocketChannel serverSock = ServerSocketChannel.open();
//		serverSock.setReceiveBufferSize(rxBufSize.intValue());
//		serverSock.setPerformancePreferences(
//                performanceConnectionTime.intValue(),
//                performanceLatency.intValue(),
//                performanceBandwidth.intValue());
//		socket.setReuseAddress(soReuseAddress.booleanValue());
//		socket.setSoTimeout(soTimeout.intValue());
        // 端口
        InetSocketAddress addr = (false ? new InetSocketAddress(InetAddress.getByName("127.0.0.1"),8080):new InetSocketAddress(8080));
        serverSock.socket().bind(addr,100);
        serverSock.configureBlocking(true); //mimic APR behavior
//        serverSock.socket().setSoTimeout(getSocketProperties().getSoTimeout());

        stopLatch = new CountDownLatch(Math.min(2,Runtime.getRuntime().availableProcessors()));

        // Initialize SSL if needed
//        initialiseSsl();
        
        selectorPool.open();
	}
	
//	@Override 使用证书
//    protected void createSSLContext(SSLHostConfig sslHostConfig) throws IllegalArgumentException {
//        boolean firstCertificate = true;
//        for (SSLHostConfigCertificate certificate : sslHostConfig.getCertificates(true)) {
//            SSLUtil sslUtil = sslImplementation.getSSLUtil(certificate);
//            if (firstCertificate) {
//                firstCertificate = false;
//                sslHostConfig.setEnabledProtocols(sslUtil.getEnabledProtocols());
//                sslHostConfig.setEnabledCiphers(sslUtil.getEnabledCiphers());
//            }
//
//            SSLContext sslContext;
//            try {
//                sslContext = sslUtil.createSSLContext(negotiableProtocols);
//                sslContext.init(sslUtil.getKeyManagers(), sslUtil.getTrustManagers(), null);
//            } catch (Exception e) {
//                throw new IllegalArgumentException(e);
//            }
//
//            SSLSessionContext sessionContext = sslContext.getServerSessionContext();
//            if (sessionContext != null) {
//                sslUtil.configureSessionContext(sessionContext);
//            }
//            certificate.setSslContext(sslContext);
//        }
//    }
	
	public static void main(String[] args) {
		
	}

}
