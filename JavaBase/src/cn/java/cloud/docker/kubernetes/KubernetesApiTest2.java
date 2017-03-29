package cn.java.cloud.docker.kubernetes;

import java.util.ArrayList;
import java.util.List;


/**
 *   org.apache.stratos.kubernetes.client 包已经不被维护了
 * http://mvnrepository.com/artifact/org.apache.stratos/org.apache.stratos.kubernetes.client/4.1.6
 * 
 * @author zhouzhian
 *
 */
public class KubernetesApiTest2 {

	/*
	public static void main(String[] args) {

		
        KubernetesApiClient client = new KubernetesApiClient("http://54.255.46.34:8080/api/v1beta1/","namespace1");
        
        // test get pod
        System.out.println("Test GET POD");
        System.out.println(client.getPod("redis-master-2"));
        
        // test get all Pods
        System.out.println("Test GET PODS");
        PodList podList = client.getAllPods();
        printPods(podList);
        
        // test create pod
        System.out.println("Test POST POD");
        Pod pod = new Pod();
        pod.setApiVersion("v1beta1");
        pod.setId("nirmal-test-pod");
        pod.setKind("Pod");
        Label l = new Label();
        l.setName("nirmal");
        pod.setLabels(l);
        State desiredState = new State();
        Manifest m = new Manifest();
        m.setId("nirmal-test-pod");
        m.setVersion("v1beta1");
        Container c = new Container();
        c.setName("master");
        c.setImage("dockerfile/redis");
        Port p = new Port();
        p.setContainerPort(8379);
        p.setHostPort(8379);
        c.setPorts(new Port[]{p});
        m.setContainers(new Container[]{c});
        desiredState.setManifest(m);
        pod.setDesiredState(desiredState);
        client.createPod(pod);
        
        podList = client.getAllPods();
        printPods(podList);
        
        // test delete Pod
        System.out.println("Test DELETE POD");
        client.deletePod("nirmal-test-pod");
        
        podList = client.getAllPods();
        printPods(podList);
        
        // Replication Controllers 
        // test get controller
        System.out.println("Test GET ReplicationController");
        ReplicationController controller = client.getReplicationController("frontendController");
        System.out.println(controller);
        
        // test get all controllers
        System.out.println("Test GET ReplicationControllers");
        ReplicationController[] controllers = client.getAllReplicationControllers();
        printControllers(controllers);
        
        // test create controller
        System.out.println("Test POST ReplicationController");
        ReplicationController contr = new ReplicationController();
        contr.setId("nirmalController");
        contr.setKind("ReplicationController");
        contr.setApiVersion("v1beta1");
        desiredState = new State();
        desiredState.setReplicas(3);
        Selector selector = new Selector();
        selector.setName("frontend");
        desiredState.setReplicaSelector(selector);
        
        Pod podTemplate = new Pod();
        State podState = new State();
        Manifest manifest = new Manifest();
        manifest.setVersion("v1beta1");
        manifest.setId("nirmalfrontendController");
        Container container = new Container();
        container.setName("nirmal-php-redis");
        container.setImage("brendanburns/php-redis");
        p = new Port();
        p.setContainerPort(81);
        p.setHostPort(8001);
        container.setPorts(new Port[]{p});
        manifest.setContainers(new Container[]{container});
        podState.setManifest(manifest);
        podTemplate.setDesiredState(podState);
        Label l1 = new  Label();
        l1.setName("frontend");
        podTemplate.setLabels(l1);
        
        desiredState.setPodTemplate(podTemplate);
        contr.setDesiredState(desiredState);
        Label l2 = new Label();
        l2.setName("frontend");
        contr.setLabels(l2);
        
        client.createReplicationController(contr);
        
        controllers = client.getAllReplicationControllers();
        printControllers(controllers);
        
        // Test delete controller
        System.out.println("Test DELETE ReplicationController");
        client.deleteReplicationController("nirmalController");
        
        controllers = client.getAllReplicationControllers();
        printControllers(controllers);
        
        // test get service
        System.out.println("Test GET Service");
        System.out.println(client.getService("redisslave"));
        
        // test get all services
        System.out.println("Test GET All Services");
        ServiceList services = client.getAllServices();
        printServices(services);
        
        // test create service
        System.out.println("Test POST Service");
        Service service = new Service();
        service.setApiVersion("v1beta1");
        service.setId("nirmalfrontend");
        service.setKind("Service");
        service.setPort(9999);
        selector = new Selector();
        selector.setName("frontend");
        service.setSelector(selector);
        
        client.createService(service);
        
        services = client.getAllServices();
        printServices(services);
        
        // Test delete Service
        System.out.println("Test DELETE Service");
        client.deleteService("nirmalfrontend");
        
        services = client.getAllServices();
        printServices(services);
        
	}

	private static void printControllers(ReplicationController[] controllers) {
		for (ReplicationController replicationController : controllers) {
			System.out.println("Replication Controller: "+replicationController.getId());
		}
	}

	private static void printPods(PodList podList) {
		for (Pod pod2 : podList.getItems()) {
			System.out.println("Pod : "+pod2.getId());
		}
	}

	private static void printServices(ServiceList services) {
		for (Service service : services.getItems()) {
			
        	System.out.println("Service : "+service.getId());
		}
	}
	*/
}
