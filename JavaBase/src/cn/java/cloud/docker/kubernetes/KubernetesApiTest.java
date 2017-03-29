package cn.java.cloud.docker.kubernetes;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;


/**
 * Kubernetes 简介
 * http://blog.csdn.net/zhangjun2915/article/details/40598151
 * http://www.csdn.net/article/2014-12-24/2823292-Docker-Kubernetes
 * 
 * 关于Kubernetes Api(kubernetes-client)
 * http://geek.csdn.net/news/detail/57265
 * http://www.tuicool.com/articles/Mvyqueu
 * 
 * https://github.com/fabric8io/kubernetes-client
 * https://github.com/fabric8io/kubernetes-client/tree/master/kubernetes-examples/src/main/java/io/fabric8/kubernetes/examples
 * http://mvnrepository.com/artifact/io.fabric8/kubernetes-client
 * 
 * @author zhouzhian
 *
 */
public class KubernetesApiTest {

	// <dependency>
	// <groupId>io.fabric8</groupId>
	// <artifactId>kubernetes-api</artifactId>
	// <version>2.0.37</version>
	// </dependency>

	public static void main(String[] args) {
		Config config = new ConfigBuilder().withMasterUrl("https://localhost:8443/").build();
		KubernetesClient kubernetesClient = new DefaultKubernetesClient(config);
	}

	public static class NamespaceApi {
		public void getNamespaces(KubernetesClient client) throws Exception {
			NamespaceList myNs = client.namespaces().list();
		}
		
		public void getNamespace(KubernetesClient client) throws Exception {
			Namespace myns = client.namespaces().withName("myns").get();
		}
		
		public void deleteNamespace(KubernetesClient client) throws Exception {
			client.namespaces().withName("myns").delete();
		}
		
		public void createNamespace(KubernetesClient client) throws Exception {
			Namespace ns = new NamespaceBuilder()
					.withNewMetadata()
					.withName("thisisatest")
					.addToLabels("this", "rocks")
					.endMetadata()
					.build();
		}
		
		public void updateNamespace(KubernetesClient client) throws Exception {
			Namespace myns = client.namespaces().withName("myns").edit()
					.editMetadata()
					.addToLabels("a", "label")
					.endMetadata()
					.done();
		}
	}

	public static class ServicesApi {

		public void getServices(KubernetesClient client) throws Exception {
			ServiceList serviceList = client.services().list();
			for (Service service : serviceList.getItems()) {
				System.out.println(service.getApiVersion());
			}
		}
		
		public void getService(KubernetesClient client) throws Exception {
			Service service = client.services().inNamespace("default").withName("myservice").get();
		}
		
		public void createService(KubernetesClient client) throws Exception {
			Service service = client.services().inNamespace("default").createNew()
					.withNewMetadata()
					.withName("myservice")
					.withName("testpod")
					.addToLabels("server", "nginx")
					.endMetadata()
					.withNewSpec()
					.addNewPort()
					.withPort(800)
					.endPort()
					.endSpec().done();
		}
		
		public void deleteService(KubernetesClient client) throws Exception {
			client.services().inNamespace("default").withName("myservice").delete();
		}
		
		public void updateService(KubernetesClient client) throws Exception {
			Service service = client.services().inNamespace("default").withName("myservice").edit()
					.editMetadata()
					.addToLabels("another", "label")
					.endMetadata()
					.done();
		}

	}

	public static class PodApi {

		public void getPods(KubernetesClient client) throws Exception {
			client.pods().withLabel("this", "works").list();
			client.pods().inNamespace("test").withLabel("this", "works").list();
		}
		
		public void getPod(KubernetesClient client) throws Exception {
			client.pods().inNamespace("test").withName("testing").get();
		}

		public void createPod(KubernetesClient client) throws Exception {
			Pod createdPod = client.pods().inNamespace("thisisatest").createNew()
					.withNewMetadata()
					.withName("testpod")
					.addToLabels("server", "nginx")
					.endMetadata()
					.withNewSpec()
					.addNewContainer().withName("nginx").withImage("nginx")
					.addNewPort().withContainerPort(80).endPort()
					.endContainer()
					.endSpec().done();
		}

		public void updatedPod(KubernetesClient client) throws Exception {
			Pod updatedPod = client.pods().inNamespace("thisisatest").withName("testpod").edit()
					.editMetadata()
					.addToLabels("server2", "nginx2")
					.and().done();
		}

		public void deletePod(KubernetesClient client) throws Exception {
			client.pods().inNamespace("test").withName("testing").delete();
		}
	}

	public static class ReplicationControllerApi {

		/**
		 * 获取列表
		 * @param client
		 * @throws Exception
		 */
		public void getReplicationControllers(KubernetesClient client) throws Exception {
			client.replicationControllers().withLabel("server", "nginx").list();
		}

		/**
		 * 获取单项
		 * @param client
		 * @throws Exception
		 */
		public void getReplicationController(KubernetesClient client) throws Exception {
			ReplicationController gotRc = client.replicationControllers().inNamespace("thisisatest").withName("nginx-controller").get();
//			SerializationUtils.dumpAsYaml(gotRc)
		}
		/**
		 * 修改
		 * @param client
		 * @throws Exception
		 */
		public void updateReplicationController(KubernetesClient client) throws Exception {
			client.replicationControllers().inNamespace("thisisatest").withName("nginx-controller").edit().editSpec().editTemplate().withNewSpec()
			.addNewContainer().withName("nginx").withImage("httpd")
			.addNewPort().withContainerPort(80).endPort()
			.endContainer()
			.endSpec()
			.endTemplate()
			.endSpec().done();
		}

		/**
		 * 添加
		 * @param client
		 * @throws Exception
		 */
		public void createReplicationController(KubernetesClient client) throws Exception {
			ReplicationController rc = new ReplicationControllerBuilder()
					.withNewMetadata().withName("nginx-controller").addToLabels("server", "nginx").endMetadata()
					.withNewSpec().withReplicas(3)
					.withNewTemplate()
					.withNewMetadata().addToLabels("server", "nginx").endMetadata()
					.withNewSpec()
					.addNewContainer().withName("nginx").withImage("nginx")
					.addNewPort().withContainerPort(80).endPort()
					.endContainer()
					.endSpec()
					.endTemplate()
					.endSpec().build();
			client.replicationControllers().inNamespace("thisisatest").create(rc);
		}

		/**
		 * 删除
		 * @param client
		 * @throws Exception
		 */
		public void deleteReplicationController(KubernetesClient client) throws Exception {
			client.replicationControllers().inNamespace("thisisatest").withName("nginx-controller").delete();
		}
	}
}
