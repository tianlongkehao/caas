package com.bonc.epm.paas.kubernetes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.EnvVar;
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
import com.bonc.epm.paas.kubernetes.model.Secret;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.model.ServiceSpec;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.util.CurrentUserUtils;

@org.springframework.stereotype.Service
public class KubernetesClientService {
	
	private static final Log log = LogFactory.getLog(KubernetesClientService.class);
	
	@Value("${kubernetes.api.endpoint}")
	private String endpoint;
	@Value("${kubernetes.api.username}")
    private String username;
	@Value("${kubernetes.api.password}")
    private String password;
	@Value("${kubernetes.api.startport}")
    private String startPort;
	@Value("${kubernetes.api.endport}")
    private String endPort;
	@Value("${kubernetes.api.address}")
	private String address;
	
	/**
     * 内存和cpu的比例大小
     */
    @Value("${ratio.memtocpu}")
    public String RATIO_MEMTOCPU = "4";
	
	public int getK8sEndPort() {
		return Integer.valueOf(endPort);
	}
    public String getK8sEndpoint(){
		return endpoint;
	}
	public String getK8sUsername(){
		return username;
	}
	public String getK8sPasswrod(){
		return password;
	}
	public String getK8sAddress(){
		return address;
	}
	public int getK8sStartPort(){
		return Integer.valueOf(startPort);
	}
	
	public KubernetesAPIClientInterface getClient() {
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        return getClient(namespace);
    }
	
    public KubernetesAPIClientInterface getClient(String namespace) {
        return new KubernetesApiClient(namespace,endpoint, username, password,new RestFactory());
    }

    /*public static void main(String[] args) {

			KubernetesAPIClientInterface client = KubernetesClientUtil.getClient("admin");
			
	    	try{
	    		client.updateReplicationController("bonctest1", 1);
				ReplicationControllerList list = client.getAllReplicationControllers();
				System.out.println("ReplicationControllerList:"+JSON.toJSONString(list));
	    	}catch(KubernetesClientException e){
	    		System.out.println(e.getMessage()+":"+JSON.toJSONString(e.getStatus()));
	    	}
			
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
    
//    public void main(String[] args) {
//    	//创建容器
//    	KubernetesAPIClientInterface client = KubernetesClientUtil.getClient("feng");
//		ReplicationController controller = KubernetesClientUtil.generateSimpleReplicationController("bonctest1",3,"10.0.93.25:5000/bonc/hw8:latest",8080,1.0f,"512");
//		controller = client.createReplicationController(controller);
//		System.out.println("controller:"+JSON.toJSONString(controller));
//	}
	public Namespace generateSimpleNamespace(String name){
		Namespace namespace = new Namespace();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		namespace.setMetadata(meta);
		return namespace;
	}
	
	public ResourceQuota generateSimpleResourceQuota(String name,Map<String,String> hard){
		ResourceQuota resourceQuota = new ResourceQuota();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		resourceQuota.setMetadata(meta);
		ResourceQuotaSpec spec = new ResourceQuotaSpec();
		spec.setHard(hard);
		resourceQuota.setSpec(spec);
		return resourceQuota;
	}
	
	public LimitRange generateSimpleLimitRange(String name,Map<String,String> defaultVal){
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
	
	/*public Map<String,Object> getlimit(Map<String,Object> limit){
		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = this.getClient();
		LimitRange limitRange = client.getLimitRange(currentUser.getUserName());
	    LimitRangeItem limitRangeItem = limitRange.getSpec().getLimits().get(0);
	    double icpuMax = transCpu(limitRangeItem.getMax().get("cpu"));
	    Integer imemoryMax = transMemory(limitRangeItem.getMax().get("memory"));
	    limit.put("cpu", icpuMax);
		limit.put("memory", imemoryMax+"Mi");
		return limit;
	    
	}*/
	
	public Long transMemory(String memory){
		if(memory.endsWith("M")){
			memory = memory.replace("M", "");
		}else if(memory.endsWith("Mi")) {
			memory = memory.replace("Mi", "");
		}else if(memory.endsWith("G")){
			memory = memory.replace("G", "");
			 long memoryG = Long.valueOf(memory)*1024;
			 return memoryG;
		}else if(memory.endsWith("Gi")){
			memory = memory.replace("Gi", "");
			long memoryG = Long.valueOf(memory)*1024;
			return memoryG;
		}else if (isNumeric(memory)) {
			long memoryBit = Long.valueOf(memory)/(1024*1024);
			return memoryBit;
		}else if (memory.endsWith("k")) {
			memory = memory.replace("k", "");
			long memoryk = Long.valueOf(memory)/1024;
			return memoryk;
		}
		return Long.valueOf(memory);
	}
	
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	public Double transCpu(String cpu) {
		if(cpu.endsWith("m")){
			cpu = cpu.replace("m", "");
			double icpu = Double.valueOf(cpu)/1000;
			return icpu;
		}
		return Double.valueOf(cpu);
	}
	
	public ReplicationController updateResource(String name,Double cpu,String ram){
		ReplicationController replicationController = new ReplicationController();
		ObjectMeta meta =  replicationController.getMetadata();
		meta.getName();
		return null;
	}
	

	
	public ReplicationController generateSimpleReplicationController(String name,int replicas,
	                                                                     String image,List<PortConfig> portConfigs,
	                                                                         Double cpu,String ram,String nginxObj, 
	                                                                             String servicePath, String proxyPath,String checkPath,
	                                                                                 List<EnvVariable> envVariables, List<String> command, List<String> args){
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
		if (StringUtils.isNotBlank(servicePath)) {
		    labels.put("servicePath", servicePath.replaceAll("/", "---")); 
		}
		if (StringUtils.isNotBlank(proxyPath)) {
		    labels.put("proxyPath", proxyPath.replaceAll("/", "---"));
		}
		// 添加服务检查路径
		if (StringUtils.isNotBlank(checkPath)) {
		    labels.put("healthcheck", checkPath.replaceAll("/", "---"));  
		}
		if (StringUtils.isNotBlank(nginxObj)) {
			String[] proxyArray = nginxObj.split(",");
			for (int i = 0; i < proxyArray.length;i++) {
				labels.put(proxyArray[i], proxyArray[i]);
			}
		}
		podMeta.setLabels(labels);
		template.setMetadata(podMeta);
		PodSpec podSpec = new PodSpec();
		List<Container> containers = new ArrayList<Container>();
		Container container = new Container();
		container.setName(name);
		container.setImage(image);
		container.setImagePullPolicy("Always");
		
		if (null != envVariables && envVariables.size() > 0) {
		    List<EnvVar> envVars = new ArrayList<EnvVar>();
		    for (EnvVariable oneRow : envVariables) {
		        EnvVar envVar = new EnvVar();
		        envVar.setName(oneRow.getEnvKey());
		        envVar.setValue(oneRow.getEnvValue());
		        envVars.add(envVar);
		    }
		    container.setEnv(envVars);
		}


		ResourceRequirements requirements = new ResourceRequirements();
		requirements.getLimits();
		Map<String,Object> def = new HashMap<String,Object>();
		//float fcpu = cpu*1024;
		def.put("cpu", cpu / Integer.valueOf(RATIO_MEMTOCPU));
		def.put("memory", ram+"Mi");
		Map<String,Object> limit = new HashMap<String,Object>();
		//limit = getlimit(limit);
		limit.put("cpu", cpu / Integer.valueOf(RATIO_MEMTOCPU));
		limit.put("memory", ram+"Mi");
		requirements.setRequests(def);
		requirements.setLimits(limit);
		container.setResources(requirements);
		
		if (CollectionUtils.isNotEmpty(portConfigs)) {
		      List<ContainerPort> ports = new ArrayList<ContainerPort>();
		      for (PortConfig oneRow : portConfigs) {
		          ContainerPort port = new ContainerPort();
		          port.setContainerPort(Integer.valueOf(oneRow.getContainerPort().trim()));
		          ports.add(port); 
		      }
		      container.setPorts(ports);
		}

		if (CollectionUtils.isNotEmpty(command)) {
		      container.setCommand(command);
		}
		if (CollectionUtils.isNotEmpty(args)) {
		      container.setArgs(args); 
		}
		containers.add(container);
		podSpec.setContainers(containers);
		template.setSpec(podSpec);
		spec.setTemplate(template);
		replicationController.setSpec(spec);
		return replicationController;
	}
	
	public ReplicationController generateSimpleReplicationController(String name,int replicas,String image,int containerPort ){
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
	
	public Service generateService(String appName,List<PortConfig> portConfigs, 
	                                       String proxyZone, String servicePath, String proxyPath){
	    Service service = new Service();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(appName);
		Map<String,String> labels = new HashMap<String,String>();
		labels.put("app", appName);
		if (StringUtils.isNotBlank(servicePath)) {
		    labels.put("servicePath", servicePath.replaceAll("/", "---"));
		}
		if (StringUtils.isNotBlank(proxyPath)) {
		    labels.put("proxyPath", proxyPath.replaceAll("/", "---"));
		}
		
		if (StringUtils.isNotBlank(proxyZone)) {
		    String[] proxyArray = proxyZone.split(",");
		    for (int i = 0; i < proxyArray.length;i++) {
		        labels.put(proxyArray[i], proxyArray[i]);
		    }
		}
		meta.setLabels(labels);
		service.setMetadata(meta);
		ServiceSpec spec = new ServiceSpec();
		spec.setType("NodePort");
		spec.setSessionAffinity("ClientIP");
		
		Map<String,String> selector = new HashMap<String,String>();
		selector.put("app", appName);
		spec.setSelector(selector);
		if (CollectionUtils.isNotEmpty(portConfigs)) {
		    List<ServicePort> ports = new ArrayList<ServicePort>();
		    for (PortConfig oneRow : portConfigs) {
		        ServicePort portObj = new ServicePort();
		        portObj.setName("http"+portConfigs.indexOf(oneRow));
		        portObj.setProtocol("TCP");
		        portObj.setPort(Integer.valueOf(oneRow.getContainerPort().trim()));
		        portObj.setTargetPort(Integer.valueOf(oneRow.getContainerPort().trim()));
		        portObj.setNodePort(Integer.valueOf(oneRow.getMapPort().trim()));
		        ports.add(portObj);
		    }
		    spec.setPorts(ports);
		}
		service.setSpec(spec);
		return service;
	}

	
	public Secret generateSecret(String name, String namespace, String key){
		Secret secret = new Secret();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		meta.setNamespace(namespace);
		secret.setMetadata(meta);
		Map<String, String> data = new HashMap<String, String>();
		data.put("key", key);
		secret.setData(data);
		return secret;
	}
}

