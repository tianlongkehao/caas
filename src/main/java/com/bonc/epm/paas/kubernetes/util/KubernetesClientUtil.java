package com.bonc.epm.paas.kubernetes.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.kubernetes.api.RestFactory;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ObjectMeta;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerSpec;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.model.ServiceSpec;
import com.bonc.epm.paas.util.CurrentUserUtils;

public class KubernetesClientUtil {
	
	private static final Log log = LogFactory.getLog(KubernetesClientUtil.class);
	
	private static String endpoint;
    private static String username;
    private static String password;
    static{
    	Properties k8sProperties = new Properties();
    	InputStream in = KubernetesClientUtil.class.getClassLoader().getResourceAsStream("kubernetes.api.properties");
    	try {
			k8sProperties.load(in);
			in.close();
			endpoint = k8sProperties.getProperty("kubernetes.api.endpoint");
			username = k8sProperties.getProperty("kubernetes.api.username");
			password = k8sProperties.getProperty("kubernetes.api.password");
		} catch (IOException e) {
			log.error("KubernetesUtil.init:"+e.getMessage());
			e.printStackTrace();
		}
    }
    
	private static KubernetesAPIClientInterface client;
	
	public static KubernetesAPIClientInterface getClient() {
		String namespace = CurrentUserUtils.getInstance().getUser().getUserName();
        return getClient(namespace);
    }
	
    public static KubernetesAPIClientInterface getClient(String namespace) {
        if (client == null) {
            client = new KubernetesApiClient(namespace,endpoint, username, password,new RestFactory());
        }
        return client;
    }
    /*public static void main(String[] args) {
    	
			KubernetesAPIClientInterface client = KubernetesClientUtil.getClient("bonc");
			
			//更新容器
			client.updateReplicationController("bonctest1", 1);
			ReplicationControllerList list = client.getAllReplicationControllers();
			System.out.println("ReplicationControllerList:"+JSON.toJSONString(list));
			
			//创建命名空间
			Namespace namespace = KubernetesClientUtil.generateSimpleNamespace("bonc");
			namespace = client.createNamespace(namespace);
			System.out.println("namespace:"+JSON.toJSONString(namespace));
			//查询命名空间
			Namespace namespace = client.getNamespace("bonc");
			System.out.println("namespace:"+JSON.toJSONString(namespace));
			
			//创建容器
			ReplicationController controller = KubernetesClientUtil.generateSimpleReplicationController("bonctest1",3,"10.0.93.25:5000/bonc/hw8:latest",8080);
			controller = client.createReplicationController(controller);
			System.out.println("controller:"+JSON.toJSONString(controller));
			
			ReplicationControllerList list = client.getAllReplicationControllers();
			System.out.println("ReplicationControllerList:"+JSON.toJSONString(list));
			
			
			PodList podList = client.getAllPods();
			System.out.println("podList:"+JSON.toJSONString(podList));

			Service service = KubernetesClientUtil.generateService("bonctest1",80,8080);
			service = client.createService(service);
			System.out.println("service:"+JSON.toJSONString(service));
			
			ServiceList serviceList = client.getAllServices();
			System.out.println("serviceList:"+JSON.toJSONString(serviceList));
			
			
		}
		*/
	public static Namespace generateSimpleNamespace(String name){
		Namespace namespace = new Namespace();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		namespace.setMetadata(meta);
		return namespace;
	}
	
	public static ReplicationController generateSimpleReplicationController(String name,int replicas,String image,int containerPort){
		ReplicationController replicationController = new ReplicationController();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		replicationController.setMetadata(meta);
		ReplicationControllerSpec spec = new ReplicationControllerSpec();
		spec.setReplicas(replicas);
		
		PodTemplateSpec template = new PodTemplateSpec();
		ObjectMeta podMeta = new ObjectMeta();
		podMeta.setName(name);
		Map<String,String> labels = new HashMap<String,String>();
		labels.put("app", name);
		podMeta.setLabels(labels);
		template.setMetadata(podMeta);
		PodSpec podSpec = new PodSpec();
		List<Container> containers = new ArrayList<Container>();
		Container container = new Container();
		container.setName(name);
		container.setImage(image);
		List<ContainerPort> ports = new ArrayList<ContainerPort>();
		ContainerPort port = new ContainerPort();
		port.setContainerPort(containerPort);
		ports.add(port);
		container.setPorts(ports);
		containers.add(container);
		podSpec.setContainers(containers);
		template.setSpec(podSpec);
		spec.setTemplate(template);
		replicationController.setSpec(spec);
		return replicationController;
	}
	
	public static Service generateService(String appName,Integer port,Integer targetPort,Integer nodePort){
		Service service = new Service();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(appName);
		service.setMetadata(meta);
		ServiceSpec spec = new ServiceSpec();
		spec.setType("NodePort");
		
		Map<String,String> selector = new HashMap<String,String>();
		selector.put("app", appName);
		spec.setSelector(selector);
		List<ServicePort> ports = new ArrayList<ServicePort>();
		ServicePort portObj = new ServicePort();
		portObj.setName("http");
		portObj.setProtocol("TCP");
		portObj.setPort(port);
		portObj.setTargetPort(targetPort);
		portObj.setNodePort(nodePort);
		ports.add(portObj);
		spec.setPorts(ports);
		
		service.setSpec(spec);
		return service;
	}
}
