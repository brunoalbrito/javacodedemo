package cn.java.cloud.docker.raw;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * http://blog.csdn.net/zhang__jiayu/article/details/43453369
 * https://github.com/docker-java/docker-java
 * http://www.open-open.com/lib/view/open1437363835818.html
 * @author zhouzhian
 *
 */
public class DockerApiTest {

	public static void main(String[] args) {

	}
	
	/**
	 * 非ssl协议调用
	 */
	public static void invokeApiWithHttp() {
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost("tcp://127.0.0.1:2375")
				.withDockerTlsVerify(false)
				.withDockerConfig("/home/user/.docker")
				.withApiVersion("1.23")
				.withRegistryUrl("https://index.docker.io/v1/")
				.withRegistryUsername("dockeruser")
				.withRegistryPassword("ilovedocker")
				.withRegistryEmail("dockeruser@github.com")
				.build();
		DockerClient docker = DockerClientBuilder.getInstance(config).build();
	}
	
	/**
	 * ssl协议调用
	 */
	public static void invokeApiWithHttps() {
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost("tcp://127.0.0.1:2376")
				.withDockerTlsVerify(true)
				.withDockerCertPath("/home/user/.docker/certs")
				.withDockerConfig("/home/user/.docker")
				.withApiVersion("1.23")
				.withRegistryUrl("https://index.docker.io/v1/")
				.withRegistryUsername("dockeruser")
				.withRegistryPassword("ilovedocker")
				.withRegistryEmail("dockeruser@github.com")
				.build();
		DockerClient docker = DockerClientBuilder.getInstance(config).build();
	}

}
