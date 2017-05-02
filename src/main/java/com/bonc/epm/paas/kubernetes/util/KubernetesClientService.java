package com.bonc.epm.paas.kubernetes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;
import com.bonc.epm.paas.kubernetes.apis.KubernetesApisClient;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.CrossVersionObjectReference;
import com.bonc.epm.paas.kubernetes.model.EndpointAddress;
import com.bonc.epm.paas.kubernetes.model.EndpointPort;
import com.bonc.epm.paas.kubernetes.model.EndpointSubset;
import com.bonc.epm.paas.kubernetes.model.Endpoints;
import com.bonc.epm.paas.kubernetes.model.EnvVar;
import com.bonc.epm.paas.kubernetes.model.HTTPGetAction;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscalerSpec;
import com.bonc.epm.paas.kubernetes.model.Kind;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeItem;
import com.bonc.epm.paas.kubernetes.model.LimitRangeSpec;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ObjectMeta;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.Probe;
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

	public static final Integer INITIAL_DELAY_SECONDS = 30 * 60;
	public static final Integer TIME_SECONDS = 5;

	public static final String adminNameSpace = "kube-system";

	public int getK8sEndPort() {
		return Integer.valueOf(endPort);
	}

	public String getK8sEndpoint() {
		return endpoint;
	}

	public String getK8sUsername() {
		return username;
	}

	public String getK8sPasswrod() {
		return password;
	}

	public String getK8sAddress() {
		return address;
	}

	public int getK8sStartPort() {
		return Integer.valueOf(startPort);
	}

	public KubernetesAPIClientInterface getClient() {
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		return getClient(namespace);
	}

	public KubernetesAPIClientInterface getClient(String namespace) {
		return new KubernetesApiClient(namespace, endpoint, username, password, new RestFactory());
	}

	public KubernetesAPISClientInterface getApisClient() {
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		return getApisClient(namespace);
	}

	public KubernetesAPISClientInterface getApisClient(String namespace) {
		return new KubernetesApisClient(namespace, endpoint, username, password, new RestFactory());
	}

	public Namespace generateSimpleNamespace(String name) {
		Namespace namespace = new Namespace();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		namespace.setMetadata(meta);
		return namespace;
	}

	public ResourceQuota generateSimpleResourceQuota(String name, Map<String, String> hard) {
		ResourceQuota resourceQuota = new ResourceQuota();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		resourceQuota.setMetadata(meta);
		ResourceQuotaSpec spec = new ResourceQuotaSpec();
		spec.setHard(hard);
		resourceQuota.setSpec(spec);
		return resourceQuota;
	}

	public LimitRange generateSimpleLimitRange(String name, Map<String, String> defaultVal) {
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

	/*
	 * public Map<String,Object> getlimit(Map<String,Object> limit){ User
	 * currentUser = CurrentUserUtils.getInstance().getUser();
	 * KubernetesAPIClientInterface client = this.getClient(); LimitRange
	 * limitRange = client.getLimitRange(currentUser.getUserName());
	 * LimitRangeItem limitRangeItem = limitRange.getSpec().getLimits().get(0);
	 * double icpuMax = transCpu(limitRangeItem.getMax().get("cpu")); Integer
	 * imemoryMax = transMemory(limitRangeItem.getMax().get("memory"));
	 * limit.put("cpu", icpuMax); limit.put("memory", imemoryMax+"Mi"); return
	 * limit;
	 *
	 * }
	 */

	public Long transMemory(String memory) {
		if (memory.endsWith("M")) {
			memory = memory.replace("M", "");
		} else if (memory.endsWith("Mi")) {
			memory = memory.replace("Mi", "");
		} else if (memory.endsWith("G")) {
			memory = memory.replace("G", "");
			long memoryG = Long.valueOf(memory) * 1024;
			return memoryG;
		} else if (memory.endsWith("Gi")) {
			memory = memory.replace("Gi", "");
			long memoryG = Long.valueOf(memory) * 1024;
			return memoryG;
		} else if (isNumeric(memory)) {
			long memoryBit = Long.valueOf(memory) / (1024 * 1024);
			return memoryBit;
		} else if (memory.endsWith("k")) {
			memory = memory.replace("k", "");
			long memoryk = Long.valueOf(memory) / 1024;
			return memoryk;
		}
		return Long.valueOf(memory);
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public Double transCpu(String cpu) {
		if (cpu.endsWith("m")) {
			cpu = cpu.replace("m", "");
			double icpu = Double.valueOf(cpu) / 1000;
			return icpu;
		}
		return Double.valueOf(cpu);
	}

	/**
	 * Description: computeMemoryOut
	 *
	 * @param val
	 *            Map<String, String>
	 * @return memVal String
	 * @see
	 */
	public String computeMemoryOut(String mem) {
		if (mem.contains("Mi")) {
			Float a1 = Float.valueOf(mem.replace("Mi", "")) / 1024;
			return a1.toString();
		} else {
			return mem.replace("G", "").replace("i", "");
		}
	}

	public ReplicationController updateResource(String name, Double cpu, String ram) {
		ReplicationController replicationController = new ReplicationController();
		ObjectMeta meta = replicationController.getMetadata();
		meta.getName();
		return null;
	}

	public ReplicationController generateSimpleReplicationController(String name, int replicas, Integer initialDelay,
			Integer timeoutDetction, Integer periodDetction, String image, List<PortConfig> portConfigs, Double cpu,
			String ram, String nginxObj, String servicePath, String proxyPath, String checkPath,
			List<EnvVariable> envVariables, List<String> command, List<String> args) {

		ReplicationController replicationController = new ReplicationController();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		replicationController.setMetadata(meta);
		ReplicationControllerSpec spec = new ReplicationControllerSpec();
		spec.setReplicas(replicas);

		PodTemplateSpec template = new PodTemplateSpec();
		ObjectMeta podMeta = new ObjectMeta();
		podMeta.setName(name);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", name);
		if (StringUtils.isNotBlank(servicePath)) {
			labels.put("servicePath", servicePath.replaceAll("/", "LINE"));
		}
		if (StringUtils.isNotBlank(proxyPath)) {
			labels.put("proxyPath", proxyPath.replaceAll("/", "LINE"));
		}
		// 添加服务检查路径
		/*
		 * if (StringUtils.isNotBlank(checkPath)) { labels.put("healthcheck",
		 * checkPath.replaceAll("/", "---")); }
		 */
		if (StringUtils.isNotBlank(nginxObj)) {
			String[] proxyArray = nginxObj.split(",");
			for (int i = 0; i < proxyArray.length; i++) {
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
		if (StringUtils.isNotBlank(checkPath)) {
			Probe livenessProbe = new Probe();
			if (initialDelay == null || initialDelay == 0) {
				livenessProbe.setInitialDelaySeconds(600);
			} else {
				livenessProbe.setInitialDelaySeconds(initialDelay);
			}
			if (timeoutDetction == null || timeoutDetction == 0) {
				livenessProbe.setTimeoutSeconds(5);
			} else {
				livenessProbe.setTimeoutSeconds(timeoutDetction);
			}
			if (periodDetction == null || periodDetction == 0) {
				livenessProbe.setPeriodSeconds(10);
			} else {
				livenessProbe.setPeriodSeconds(periodDetction);
			}
			HTTPGetAction httpGet = new HTTPGetAction();
			httpGet.setPath(checkPath);
			httpGet.setPort(8080); // 修改了HTTPGetAction的port字段类型定义
			httpGet.setScheme("HTTP");
			livenessProbe.setHttpGet(httpGet);
			container.setLivenessProbe(livenessProbe);
		}

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
		Map<String, Object> def = new HashMap<String, Object>();
		// float fcpu = cpu*1024;
		def.put("cpu", cpu / Integer.valueOf(RATIO_MEMTOCPU));
		def.put("memory", ram + "Mi");
		Map<String, Object> limit = new HashMap<String, Object>();
		// limit = getlimit(limit);
		limit.put("cpu", cpu / Integer.valueOf(RATIO_MEMTOCPU));
		limit.put("memory", ram + "Mi");
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

	public ReplicationController generateSimpleReplicationController(String name, int replicas, String image,
			int containerPort) {
		ReplicationController replicationController = new ReplicationController();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		replicationController.setMetadata(meta);
		ReplicationControllerSpec spec = new ReplicationControllerSpec();
		spec.setReplicas(replicas);

		PodTemplateSpec template = new PodTemplateSpec();
		ObjectMeta podMeta = new ObjectMeta();
		podMeta.setName(name);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", name);
		podMeta.setLabels(labels);
		template.setMetadata(podMeta);
		PodSpec podSpec = new PodSpec();
		List<Container> containers = new ArrayList<Container>();
		Container container = new Container();
		container.setName(name);
		container.setImage(image);

		// ResourceRequirements requirements = new ResourceRequirements();
		// requirements.getLimits();
		// Map<String,String> def = new HashMap<String,String>();
		// //float fcpu = cpu*1024;
		// def.put("cpu", String.valueOf(cpu));
		// def.put("memory", ram+"Mi");
		// requirements.setRequests(def);
		// container.setResources(requirements);

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

	public Service generateService(String appName, List<PortConfig> portConfigs, String proxyZone, String servicePath,
			String proxyPath, String sessionAffinity, String nodeIpAffinity) {
		Service service = new Service();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(appName);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", appName);
		if (StringUtils.isNotBlank(servicePath)) {
			labels.put("servicePath", servicePath.replaceAll("/", "LINE"));
		}
		if (StringUtils.isNotBlank(proxyPath)) {
			labels.put("proxyPath", proxyPath.replaceAll("/", "LINE"));
		}

		if (StringUtils.isNotBlank(proxyZone)) {
			String[] proxyArray = proxyZone.split(",");
			for (int i = 0; i < proxyArray.length; i++) {
				labels.put(proxyArray[i], proxyArray[i]);
			}
		}
		if (StringUtils.isNotBlank(nodeIpAffinity)) {
			labels.put("nodeIpAffinity", nodeIpAffinity);
		}
		meta.setLabels(labels);
		service.setMetadata(meta);
		ServiceSpec spec = new ServiceSpec();
		spec.setType("NodePort");
		if (StringUtils.isNotBlank(sessionAffinity)) {
			spec.setSessionAffinity(sessionAffinity);
		}

		Map<String, String> selector = new HashMap<String, String>();
		selector.put("app", appName);
		spec.setSelector(selector);
		if (CollectionUtils.isNotEmpty(portConfigs)) {
			List<ServicePort> ports = new ArrayList<ServicePort>();
			for (PortConfig oneRow : portConfigs) {
				ServicePort portObj = new ServicePort();
				portObj.setName("http" + portConfigs.indexOf(oneRow));
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

	public Secret generateSecret(String name, String namespace, String key) {
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

	/**
	 * Description: 创建k8s Service
	 *
	 * @param serName
	 * @param refPort
	 * @return
	 * @see
	 */
	public Service generateRefService(String serName, int refPort) {
		Service service = new Service();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(serName);
		/*
		 * Map<String,String> labels = new HashMap<String,String>();
		 * labels.put("app", serName); meta.setLabels(labels);
		 */
		service.setMetadata(meta);

		ServiceSpec spec = new ServiceSpec();
		// spec.setType("NodePort");
		List<ServicePort> ports = new ArrayList<ServicePort>();
		ServicePort portObj = new ServicePort();
		// portObj.setName("http");
		portObj.setProtocol("TCP");
		portObj.setPort(refPort);
		// portObj.setNodePort(serviceController.vailPortSet());
		ports.add(portObj);
		spec.setPorts(ports);
		/*
		 * Map<String,String> selector = new HashMap<String,String>();
		 * selector.put("app", serName); spec.setSelector(selector);
		 */
		service.setSpec(spec);
		return service;
	}

	/**
	 * Description: create a new Endpoint Object
	 *
	 * @param serAddress
	 *            String
	 * @param refAddress
	 *            String
	 * @param refPort
	 * @return
	 * @see
	 */
	public Endpoints generateEndpoints(String serName, String refAddress, int refPort, String useProxy) {
		Endpoints endpoints = new Endpoints();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(serName);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", serName);
		if (StringUtils.isNotBlank(useProxy)) {
			labels.put("useProxy", useProxy);
		}
		meta.setLabels(labels);
		endpoints.setMetadata(meta);

		List<EndpointSubset> subsets = new ArrayList<EndpointSubset>();
		EndpointSubset subset = new EndpointSubset();
		List<EndpointAddress> addresses = new ArrayList<EndpointAddress>();
		EndpointAddress address = new EndpointAddress();
		address.setIp(refAddress);
		addresses.add(address);
		subset.setAddresses(addresses);
		// List<EndpointAddress> notReadyAddresses = new
		// ArrayList<EndpointAddress>();
		List<EndpointPort> ports = new ArrayList<EndpointPort>();
		EndpointPort port = new EndpointPort();
		port.setPort(refPort);
		ports.add(port);
		subset.setPorts(ports);
		subsets.add(subset);
		endpoints.setSubsets(subsets);
		return endpoints;
	}

	/**
	 * Description: create a new HorizontalPodAutoscaler Object
	 * @param maxReplicas
	 * @param minReplicas
	 * @param kind
	 * @param targetCPUUtilizationPercentage
	 *
	 * @param serAddress
	 *            String
	 * @param refAddress
	 *            String
	 * @param refPort
	 * @return
	 * @see
	 */
	public HorizontalPodAutoscaler generateHorizontalPodAutoscaler(String name, Integer maxReplicas,
			Integer minReplicas, Kind kind, Integer targetCPUUtilizationPercentage) {
		HorizontalPodAutoscaler hpa = new HorizontalPodAutoscaler();
		// 设置metadata
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		hpa.setMetadata(meta);
		// 设置spec
		HorizontalPodAutoscalerSpec spec = new HorizontalPodAutoscalerSpec();
		spec.setMaxReplicas(maxReplicas);
		spec.setMinReplicas(minReplicas);
		CrossVersionObjectReference scaleTargetRef = new CrossVersionObjectReference();
		scaleTargetRef.setKind(kind);
		scaleTargetRef.setName(name);
		spec.setScaleTargetRef(scaleTargetRef);
		spec.setTargetCPUUtilizationPercentage(targetCPUUtilizationPercentage);
		hpa.setSpec(spec);
		return hpa;
	}

	/**
	 * Description: update a rc
	 *
	 *
	 * @param controller
	 * @param service
	 * @return
	 * @see
	 */
	public ReplicationController updateSimpleReplicationController(ReplicationController controller,
			com.bonc.epm.paas.entity.Service service, List<String> command, int flag) {

		if (flag == 0) {
			controller.getMetadata().setName(service.getServiceName());
			controller.getMetadata().getLabels().put("app", service.getServiceName());
			controller.getSpec().getTemplate().getSpec().getContainers().get(0).setCommand(command);
			// if(!StringUtils.isNotBlank(service.getVolName())){
			// controller.getSpec().getTemplate().getSpec().getContainers().get(0).setVolumeMounts(null);
			// }
		}
		controller.getMetadata().getLabels().remove("dmz");
		controller.getMetadata().getLabels().remove("user");
		if (StringUtils.isNotBlank(service.getProxyZone())) {
			String ProxyZones[] = service.getProxyZone().split(",");
			for (int i = 0; i < ProxyZones.length; i++) {
				controller.getMetadata().getLabels().put(ProxyZones[i].toLowerCase(), ProxyZones[i]);
			}
			controller.getMetadata().getLabels().put("proxyPath", service.getProxyPath());
			controller.getMetadata().getLabels().put("servicePath", service.getServicePath());
			// controller.getSpec().setSelector(controller.getMetadata().getLabels());
			// controller.getSpec().getTemplate().getMetadata().setLabels(controller.getMetadata().getLabels());
		}
		if (StringUtils.isNotBlank(service.getCheckPath())) {
			Probe livenessProbe = new Probe();
			if (service.getInitialDelay() == null || service.getInitialDelay() == 0) {
				livenessProbe.setInitialDelaySeconds(600);
			} else {
				livenessProbe.setInitialDelaySeconds(service.getInitialDelay());
			}
			if (service.getTimeoutDetction() == null || service.getTimeoutDetction() == 0) {
				livenessProbe.setTimeoutSeconds(5);
			} else {
				livenessProbe.setTimeoutSeconds(service.getTimeoutDetction());
			}
			if (service.getPeriodDetction() == null || service.getPeriodDetction() == 0) {
				livenessProbe.setPeriodSeconds(10);
			} else {
				livenessProbe.setPeriodSeconds(service.getPeriodDetction());
			}

			HTTPGetAction httpGet = new HTTPGetAction();
			httpGet.setPath(service.getCheckPath());
			httpGet.setPort(8080); // 修改了HTTPGetAction的port字段类型定义
			httpGet.setScheme("HTTP");
			livenessProbe.setHttpGet(httpGet);
			controller.getSpec().getTemplate().getSpec().getContainers().get(0).setLivenessProbe(livenessProbe);
			// ---------------------
		}

		return controller;

	}

	/**
	 *
	 * Description: update a service
	 *
	 * @param k8sService
	 * @param service
	 * @return service
	 * @see
	 */
	public Service updateService(Service k8sService, com.bonc.epm.paas.entity.Service service) {
		k8sService.getMetadata().setName(service.getServiceName());
		k8sService.getMetadata().getLabels().remove("dmz");
		k8sService.getMetadata().getLabels().remove("user");
		k8sService.getMetadata().getLabels().put("app", service.getServiceName());
		if (StringUtils.isNotBlank(service.getProxyZone())) {
			String ProxyZones[] = service.getProxyZone().split(",");
			for (int i = 0; i < ProxyZones.length; i++) {
				k8sService.getMetadata().getLabels().put(ProxyZones[i].toLowerCase(), ProxyZones[i]);
			}
			if (StringUtils.isNotBlank(service.getServicePath())) {
				k8sService.getMetadata().getLabels().put("servicePath",
						service.getServicePath().replaceAll("/", "LINE"));
			}
			if (StringUtils.isNotBlank(service.getProxyPath())) {
				k8sService.getMetadata().getLabels().put("proxyPath", service.getProxyPath().replaceAll("/", "LINE"));
			}
			if (StringUtils.isNotBlank(service.getNodeIpAffinity())) {
				k8sService.getMetadata().getLabels().put("nodeIpAffinity", service.getNodeIpAffinity());
			} else {
				k8sService.getMetadata().getLabels().remove("nodeIpAffinity");
			}
			ServiceSpec spec = new ServiceSpec();
			spec.setType("NodePort");
			if (StringUtils.isNotBlank(service.getSessionAffinity())) {
				k8sService.getSpec().setSessionAffinity(service.getSessionAffinity());
			} else {
				k8sService.getSpec().setSessionAffinity(null);
			}
		}
		return k8sService;
	}

	/**
	 * Description: 更新容器端口
	 *
	 * @param controller
	 * @param containerPort
	 * @return
	 * @see
	 */
	public ReplicationController updateRcContainPort(ReplicationController controller, List<PortConfig> portCfg) {
		if (CollectionUtils.isNotEmpty(portCfg)) {
			List<ContainerPort> ports = new ArrayList<ContainerPort>();
			for (PortConfig oneRow : portCfg) {
				ContainerPort port = new ContainerPort();
				port.setContainerPort(Integer.valueOf(oneRow.getContainerPort().trim()));
				ports.add(port);
			}
			controller.getSpec().getTemplate().getSpec().getContainers().get(0).setPorts(ports);
		}
		return controller;
	}

	/**
	 * Description: 更新service的容器端口信息<br>
	 *
	 * @param k8sService
	 * @param portCfgs
	 * @return
	 * @see
	 */
	public com.bonc.epm.paas.kubernetes.model.Service updateSvcContainPort(
			com.bonc.epm.paas.kubernetes.model.Service k8sService, List<PortConfig> portCfg) {
		if (CollectionUtils.isNotEmpty(portCfg)) {
			List<ServicePort> ports = new ArrayList<ServicePort>();
			for (PortConfig oneRow : portCfg) {
				ServicePort portObj = new ServicePort();
				portObj.setName("http" + portCfg.indexOf(oneRow));
				portObj.setProtocol("TCP");
				portObj.setPort(Integer.valueOf(oneRow.getContainerPort().trim()));
				portObj.setTargetPort(Integer.valueOf(oneRow.getContainerPort().trim()));
				portObj.setNodePort(Integer.valueOf(oneRow.getMapPort().trim()));
				ports.add(portObj);
			}
			k8sService.getSpec().setPorts(ports);
		}
		return k8sService;
	}

	/**
	 * Description: <br>
	 *
	 * @param controller
	 * @param envVars
	 * @return
	 * @see
	 */
	public ReplicationController updateRcEnv(ReplicationController controller, List<EnvVariable> envVariables) {
		if (null != envVariables && envVariables.size() > 0) {
			List<EnvVar> envVars = new ArrayList<EnvVar>();
			for (EnvVariable oneRow : envVariables) {
				EnvVar envVar = new EnvVar();
				envVar.setName(oneRow.getEnvKey());
				envVar.setValue(oneRow.getEnvValue());
				envVars.add(envVar);
			}
			controller.getSpec().getTemplate().getSpec().getContainers().get(0).setEnv(envVars);
		}
		return controller;
	}

}
