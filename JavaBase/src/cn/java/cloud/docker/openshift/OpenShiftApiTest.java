package cn.java.cloud.docker.openshift;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

/**
 * 
 * 关于OpenShift Api(openshift-client)
 * https://github.com/fabric8io/kubernetes-client
 * 
 * @author zhouzhian
 *
 */
public class OpenShiftApiTest {
	public static void main(String[] args) {
		OpenShiftClient osClient = new DefaultOpenShiftClient();
	}
	

	public static class NamespaceApi {
		public void getNamespaces(OpenShiftClient client) throws Exception {
			NamespaceList myNs = client.namespaces().list();
		}
		
		public void getNamespace(OpenShiftClient client) throws Exception {
			Namespace myns = client.namespaces().withName("myns").get();
		}
		
		public void deleteNamespace(OpenShiftClient client) throws Exception {
			client.namespaces().withName("myns").delete();
		}
		
		public void createNamespace(OpenShiftClient client) throws Exception {
			Namespace ns = new NamespaceBuilder()
					.withNewMetadata()
					.withName("thisisatest")
					.addToLabels("this", "rocks")
					.endMetadata()
					.build();
		}
		
		public void updateNamespace(OpenShiftClient client) throws Exception {
			Namespace myns = client.namespaces().withName("myns").edit()
					.editMetadata()
					.addToLabels("a", "label")
					.endMetadata()
					.done();
		}
	}

	public static class ServicesApi {

		public void getServices(OpenShiftClient client) throws Exception {
			ServiceList serviceList = client.services().list();
			for (Service service : serviceList.getItems()) {
				System.out.println(service.getApiVersion());
			}
		}
		
		public void getService(OpenShiftClient client) throws Exception {
			Service service = client.services().inNamespace("default").withName("myservice").get();
		}
		
		public void createService(OpenShiftClient client) throws Exception {
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
		
		public void deleteService(OpenShiftClient client) throws Exception {
			client.services().inNamespace("default").withName("myservice").delete();
		}
		
		public void updateService(OpenShiftClient client) throws Exception {
			Service service = client.services().inNamespace("default").withName("myservice").edit()
					.editMetadata()
					.addToLabels("another", "label")
					.endMetadata()
					.done();
		}

	}

	public static class PodApi {

		public void getPods(OpenShiftClient client) throws Exception {
			client.pods().withLabel("this", "works").list();
			client.pods().inNamespace("test").withLabel("this", "works").list();
		}
		
		public void getPod(OpenShiftClient client) throws Exception {
			client.pods().inNamespace("test").withName("testing").get();
		}

		public void createPod(OpenShiftClient client) throws Exception {
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

		public void updatedPod(OpenShiftClient client) throws Exception {
			Pod updatedPod = client.pods().inNamespace("thisisatest").withName("testpod").edit()
					.editMetadata()
					.addToLabels("server2", "nginx2")
					.and().done();
		}

		public void deletePod(OpenShiftClient client) throws Exception {
			client.pods().inNamespace("test").withName("testing").delete();
		}
	}

	public static class ReplicationControllerApi {

		/**
		 * 获取列表
		 * @param client
		 * @throws Exception
		 */
		public void getReplicationControllers(OpenShiftClient client) throws Exception {
			client.replicationControllers().withLabel("server", "nginx").list();
		}

		/**
		 * 获取单项
		 * @param client
		 * @throws Exception
		 */
		public void getReplicationController(OpenShiftClient client) throws Exception {
			ReplicationController gotRc = client.replicationControllers().inNamespace("thisisatest").withName("nginx-controller").get();
			//			SerializationUtils.dumpAsYaml(gotRc)
		}
		/**
		 * 修改
		 * @param client
		 * @throws Exception
		 */
		public void updateReplicationController(OpenShiftClient client) throws Exception {
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
		public void createReplicationController(OpenShiftClient client) throws Exception {
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
		public void deleteReplicationController(OpenShiftClient client) throws Exception {
			client.replicationControllers().inNamespace("thisisatest").withName("nginx-controller").delete();
		}
	}
}
