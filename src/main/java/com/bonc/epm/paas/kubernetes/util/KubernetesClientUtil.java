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
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.kubernetes.api.RestFactory;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeItem;
import com.bonc.epm.paas.kubernetes.model.LimitRangeSpec;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ObjectMeta;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerSpec;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaSpec;
import com.bonc.epm.paas.kubernetes.model.ResourceRequirements;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.model.ServiceSpec;
import com.bonc.epm.paas.util.CurrentUserUtils;

public class KubernetesClientUtil {
	
	private static final Log log = LogFactory.getLog(KubernetesClientUtil.class);
	
	private static String endpoint;
    private static String username;
    private static String password;
    private static String startPort;
    static{
    	Properties k8sProperties = new Properties();
    	InputStream in = KubernetesClientUtil.class.getClassLoader().getResourceAsStream("kubernetes.api.properties");
    	try {
			k8sProperties.load(in);
			in.close();
			endpoint = k8sProperties.getProperty("kubernetes.api.endpoint");
			username = k8sProperties.getProperty("kubernetes.api.username");
			password = k8sProperties.getProperty("kubernetes.api.password");
			startPort = k8sProperties.getProperty("kubernetes.api.startport");
		} catch (IOException e) {
			log.error("KubernetesUtil.init:"+e.getMessage());
			e.printStackTrace();
		}
    }
    
	public static int getK8sStartPort(){
		return Integer.valueOf(startPort);
	}
	
	public static KubernetesAPIClientInterface getClient() {
		String namespace = CurrentUserUtils.getInstance().getUser().getUserName();
        return getClient(namespace);
    }
	
    public static KubernetesAPIClientInterface getClient(String namespace) {
        return new KubernetesApiClient(namespace,endpoint, username, password,new RestFactory());
    }
    
    /*public static void main(String[] args) {
    	
			KubernetesAPIClientInterface client = KubernetesClientUtil.getClient("admin");
			
			//获取特殊条件的pods
			Map<String,String> map = new HashMap<String,String>();
	    	map.put("app", "helloworld001");
	    	PodList podList = client.getLabelSelectorPods(map);
	    	System.out.println("podList:"+JSON.toJSONString(podList));
			
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
			
			Map<String,String> map = new HashMap<String,String>();
	    	map.put("memory", "1Gi");
	    	map.put("cpu", "20");
	    	map.put("pods", "10");
	    	map.put("services", "5");
	    	map.put("replicationcontrollers", "20");
	    	map.put("resourcequotas", "1");
	    	ResourceQuota quota = KubernetesClientUtil.generateSimpleResourceQuota("quota",map);
	    	System.out.println("quota1:"+JSON.toJSONString(quota));		
	    	quota = client.createResourceQuota(quota);
	    	System.out.println("quota:"+JSON.toJSONString(quota));
	    	
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("memory", "128Mi");
	    	map.put("cpu", "100m");
	    	LimitRange limitRange = KubernetesClientUtil.generateSimpleLimitRange("limits",map);
	    	System.out.println("limitRange1:"+JSON.toJSONString(limitRange));		
	    	limitRange = client.createLimitRange(limitRange);
	    	System.out.println("limitRange:"+JSON.toJSONString(limitRange));
	    	
	    	LimitRangeList limitRangeList = client.getAllLimitRanges();
    		System.out.println("limitRangeList:"+JSON.toJSONString(limitRangeList));
    		
    		LimitRange limitRange = client.getLimitRange("limits");
		System.out.println("limitRange:"+JSON.toJSONString(limitRange));
		
		
			//删除
			 PodList podList = client.getAllPods();
    			System.out.println("podList:"+JSON.toJSONString(podList));
    			if(podList.getItems().size()>0){
    				for(Pod pod:podList.getItems()){
    					client.deletePod(pod.getMetadata().getName());
    					System.out.println("pod:"+JSON.toJSONString(pod));
    				}
    			}
    			ReplicationControllerList list = client.getAllReplicationControllers();
    			if(list.getItems().size()>0){
    				for(ReplicationController controller:list.getItems()){
    					client.deleteReplicationController(controller.getMetadata().getName());
    				}
    			}
    			ServiceList serviceList = client.getAllServices();
    			if(serviceList.getItems().size()>0){
    				for(Service service:serviceList.getItems()){
    					client.deleteService(service.getMetadata().getName());
    				}
    			}
    			ResourceQuotaList quotaList = client.getAllResourceQuotas();
    			if(quotaList!=null&&quotaList.getItems().size()>0){
    				for(ResourceQuota quota:quotaList.getItems()){
    					client.deleteResourceQuota(quota.getMetadata().getName());
    				}
    			}
    			LimitRangeList limitRangeList = client.getAllLimitRanges();
    			if(limitRangeList!=null&&limitRangeList.getItems().size()>0){
    				for(LimitRange limitRange:limitRangeList.getItems()){
    					client.deleteLimitRange(limitRange.getMetadata().getName());
    				}
    			}
		
		
		}
		*/
    
//    public static void main(String[] args) {
//    	//创建容器
//    	KubernetesAPIClientInterface client = KubernetesClientUtil.getClient("feng");
//		ReplicationController controller = KubernetesClientUtil.generateSimpleReplicationController("bonctest1",3,"10.0.93.25:5000/bonc/hw8:latest",8080,1.0f,"512");
//		controller = client.createReplicationController(controller);
//		System.out.println("controller:"+JSON.toJSONString(controller));
//	}
	public static Namespace generateSimpleNamespace(String name){
		Namespace namespace = new Namespace();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		namespace.setMetadata(meta);
		return namespace;
	}
	
	public static ResourceQuota generateSimpleResourceQuota(String name,Map<String,String> hard){
		ResourceQuota resourceQuota = new ResourceQuota();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		resourceQuota.setMetadata(meta);
		ResourceQuotaSpec spec = new ResourceQuotaSpec();
		spec.setHard(hard);
		resourceQuota.setSpec(spec);
		return resourceQuota;
	}
	
	public static LimitRange generateSimpleLimitRange(String name,Map<String,String> defaultVal){
		LimitRange limitRange = new LimitRange();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		limitRange.setMetadata(meta);
		
		LimitRangeSpec spec = new LimitRangeSpec();
		List<LimitRangeItem> limits = new ArrayList<LimitRangeItem>();
		LimitRangeItem limitRangeItem = new LimitRangeItem();
		limitRangeItem.setDefaultVal(defaultVal);
		limits.add(limitRangeItem);
		spec.setLimits(limits);
		limitRange.setSpec(spec);
		return limitRange;
	}
	
	public static Map<String,Object> getlimit(Map<String,Object> limit){
		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
		
		LimitRange limitRange = client.getLimitRange(currentUser.getUserName());
	    LimitRangeItem limitRangeItem = limitRange.getSpec().getLimits().get(0);
	    String cpuMax = limitRangeItem.getMax().get("cpu").replace("m", "");
	    String memoryMax = limitRangeItem.getMax().get("memory").replace("M", "");
	    double icpuMax = Double.valueOf(cpuMax)/1024;
	    limit.put("cpu", icpuMax);
		limit.put("memory", memoryMax+"Mi");
		return limit;
	    
	}
	
	public static ReplicationController generateSimpleReplicationController(String name,int replicas,String image,int containerPort,Double cpu,String ram){
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
		
		
	
		ResourceRequirements requirements = new ResourceRequirements();
		requirements.getLimits();
		Map<String,Object> def = new HashMap<String,Object>();
		//float fcpu = cpu*1024;
		def.put("cpu", cpu);
		def.put("memory", ram+"Mi");
		Map<String,Object> limit = new HashMap<String,Object>();
		limit = getlimit(limit);
		requirements.setRequests(def);
		requirements.setLimits(limit);
		container.setResources(requirements);
		
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
		
//		ResourceRequirements requirements = new ResourceRequirements();
//		requirements.getLimits();
//		Map<String,String> def = new HashMap<String,String>();
//		//float fcpu = cpu*1024;
//		def.put("cpu", String.valueOf(cpu));
//		def.put("memory", ram+"Mi");
//		requirements.setRequests(def);
//		container.setResources(requirements);
		
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
		spec.setSessionAffinity("ClientIP");
		
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
