package com.bonc.epm.paas.kubernetes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.controller.ServiceController;
import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.KeyValue;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.ServiceConfigmap;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;
import com.bonc.epm.paas.kubernetes.apis.KubernetesApisClient;
import com.bonc.epm.paas.kubernetes.model.ConfigMap;
import com.bonc.epm.paas.kubernetes.model.ConfigMapVolumeSource;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.ContainerStatus;
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
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaim;
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaimSpec;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodCondition;
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
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.StatefulSetSpec;
import com.bonc.epm.paas.kubernetes.model.Volume;
import com.bonc.epm.paas.kubernetes.model.VolumeMount;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.util.ConvertUtil;
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
	 * POD request cpu /limit cpu = 1/4
	 */
	@Value("${ratio.limittorequestcpu}")
	public int RATIO_LIMITTOREQUESTCPU;

	/**
	 * POD request memory /limit memory = 1/4
	 */
	@Value("${ratio.limittorequestmemory}")
	public int RATIO_LIMITTOREQUESTMEMORY;

	public static final Integer INITIAL_DELAY_SECONDS = 30 * 60;
	public static final Integer TIME_SECONDS = 5;

	public static final String adminNameSpace = "kube-system";

	@Autowired
	public ServiceController serviceController;

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

	public Long transMemory(String memory) {
		return (long) (ConvertUtil.parseMemory(memory) / Math.pow(2, 20));
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public boolean isRunning(Pod pod) {
		boolean podRunning = false;
		boolean containerRunning = true;
		if (pod.getStatus().getPhase().equals("Running")) {
			for(PodCondition podCondition : pod.getStatus().getConditions()){
				if (podCondition.getType().equals("Ready") && podCondition.getStatus().equals("True")) {
					podRunning = true;
					break;
				}
			}
		}
		if (podRunning) {
			for (ContainerStatus containerStatus : pod.getStatus().getContainerStatuses()) {
				if (containerStatus.getState().getRunning() == null) {
					containerRunning=false;
					break;
				}
			}
		}

		return podRunning && containerRunning;
	}

	public ReplicationController updateResource(String name, Double cpu, String ram) {
		ReplicationController replicationController = new ReplicationController();
		ObjectMeta meta = replicationController.getMetadata();
		meta.getName();
		return null;
	}

	public ReplicationController generateSimpleReplicationController(String name, int replicas, Integer initialDelay,
			Integer timeoutDetction, Integer periodDetction, String image, List<PortConfig> portConfigs, Double cpu,
			String ram, String nginxObj, String servicePath, String checkPath, List<EnvVariable> envVariables,
			List<String> command, List<String> args, List<ServiceConfigmap> serviceConfigmapList, boolean ispodmutex) {

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
		if (StringUtils.isNotBlank(nginxObj)) {
			String[] proxyArray = nginxObj.split(",");
			for (int i = 0; i < proxyArray.length; i++) {
				labels.put(proxyArray[i], proxyArray[i]);
			}
		}
		podMeta.setLabels(labels);
		if (ispodmutex) {
			Map<String, String> annotations = new HashMap<String, String>();
			String value = "{\"podAntiAffinity\": {\"requiredDuringSchedulingIgnoredDuringExecution\": [{ \"labelSelector\": {\"matchExpressions\": ["
					+ "{\"key\": \"app\",\"operator\": \"In\",\"values\": [\"" + name
					+ "\"]}]},\"topologyKey\": \"kubernetes.io/hostname\"}]}}";
			annotations.put("scheduler.alpha.kubernetes.io/affinity", value);
			podMeta.setAnnotations(annotations);
		}
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
		// float fcpu = cpu*1024; 页面资源 = 实际资源 * 资源系数
		def.put("cpu", cpu / RATIO_LIMITTOREQUESTCPU);
		def.put("memory", Double.parseDouble(ram) / RATIO_LIMITTOREQUESTMEMORY + "Mi");
		/*
		 * def.put("cpu", cpu / Integer.valueOf(RATIO_MEMTOCPU));
		 * def.put("memory", ram + "Mi");
		 */
		Map<String, Object> limit = new HashMap<String, Object>();
		// limit = getlimit(limit);
		limit.put("cpu", cpu);
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
		if (!CollectionUtils.isEmpty(serviceConfigmapList)) {
			ServiceConfigmap serviceConfigmap = serviceConfigmapList.get(0);
			List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
			VolumeMount volumeMount = new VolumeMount();
			volumeMount.setName(name);
			volumeMount.setMountPath(serviceConfigmap.getPath());
			volumeMounts.add(volumeMount);
			container.setVolumeMounts(volumeMounts);

			List<Volume> volumes = new ArrayList<Volume>();
			Volume volume = new Volume();
			ConfigMapVolumeSource configMapTemplate = new ConfigMapVolumeSource();
			configMapTemplate.setName(serviceConfigmap.getName());
			volume.setName(name);
			volume.setConfigMap(configMapTemplate);
			volumes.add(volume);
			podSpec.setVolumes(volumes);
		}
		containers.add(container);
		podSpec.setContainers(containers);
		template.setSpec(podSpec);
		spec.setTemplate(template);
		replicationController.setSpec(spec);
		return replicationController;
	}

	public ConfigMap generateConfigMap(String configMapName, List<KeyValue> keyValues) {
		ConfigMap configmap = new ConfigMap();
		Map<String, String> data = new HashMap<String, String>();

		if (!CollectionUtils.isEmpty(keyValues)) {
			for (KeyValue k : keyValues) {
				data.put(k.getKey(), k.getValue());
			}
		}

		ObjectMeta metadata = new ObjectMeta();
		metadata.setName(configMapName);
		configmap.setMetadata(metadata);
		configmap.setData(data);

		return configmap;
	}

	public ConfigMap generateConfigMap(String name, String namespace, Map<String, String> data) {
		ConfigMap configmap = new ConfigMap();

		ObjectMeta metadata = new ObjectMeta();
		metadata.setName(name);
		metadata.setNamespace(namespace);
		configmap.setMetadata(metadata );

		configmap.setData(data);

		return configmap;
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

	public ReplicationController generateSimpleReplicationController(String name, int replicas, String image,
			List<String> command, List<String> args) {
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

	public Service generateService(String appName, List<PortConfig> portConfigs, String proxyZone, String servicePath,
			String sessionAffinity) {
		Service service = new Service();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(appName);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", appName);
		if (StringUtils.isNotBlank(servicePath)) {
			labels.put("servicePath", servicePath.replaceAll("/", "LINE"));
		}

		if (StringUtils.isNotBlank(proxyZone)) {
			String[] proxyArray = proxyZone.split(",");
			for (int i = 0; i < proxyArray.length; i++) {
				labels.put(proxyArray[i], proxyArray[i]);
			}
		}
		meta.setLabels(labels);

		/*
		 * 兼容k8s 1.6 增加
		 * annotations:
		 *   service.beta.kubernetes.io/external-traffic:OnlyLocal
		 */
		Map<String, String> annotations = new HashMap<>();
		annotations.put("service.beta.kubernetes.io/external-traffic", "OnlyLocal");
		meta.setAnnotations(annotations);

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

	public Service generateService(Map<String, String> annotations, String name, String namespace,
			Map<String, String> labels, List<ServicePort> ports, String clusterIP, Map<String, String> selector) {
		Service service = new Service();
		// metadata
		ObjectMeta metadata = new ObjectMeta();
		metadata.setAnnotations(annotations);
		metadata.setName(name);
		metadata.setNamespace(namespace);
		metadata.setLabels(labels);
		service.setMetadata(metadata);

		//spec
		ServiceSpec spec = new ServiceSpec();
		spec.setPorts(ports);
		spec.setClusterIP(clusterIP);
		spec.setSelector(selector);
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
	 * @param refPort
	 * @param nameSpace
	 * @param serAddress
	 * @param refAddress
	 * @return
	 * @see
	 */
	public Service generateRefService(String serName, int refPort) {
		Service service = new Service();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(serName);
		service.setMetadata(meta);

		ServiceSpec spec = new ServiceSpec();
		List<ServicePort> ports = new ArrayList<ServicePort>();
		ServicePort portObj = new ServicePort();
		portObj.setProtocol("TCP");
		portObj.setPort(refPort);
		ports.add(portObj);
		spec.setPorts(ports);
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
	public Endpoints generateEndpoints(String serName, String refAddress, int refPort, String useProxy, String zone) {
		Endpoints endpoints = new Endpoints();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(serName);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", serName);
		if (StringUtils.isNotBlank(useProxy)) {
			labels.put("useProxy", useProxy);
			if (StringUtils.isNotBlank(zone)) {
				switch (zone) {
				case "0":
					labels.put("user", "user");
					break;
				case "1":
					labels.put("dmz", "dmz");
					break;
				case "2":
					labels.put("dmz1", "dmz1");
					break;
				case "3":
					labels.put("all", "all");
					break;
				default:
					labels.put("all", "all");
					break;
				}
			}
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
	 *
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
		// 璁剧疆metadata
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		hpa.setMetadata(meta);
		// 璁剧疆spec
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

	public StatefulSet generateStatefulSet(String name, String namespace, Integer replicas, PodTemplateSpec template,
			List<PersistentVolumeClaim> volumeClaimTemplates) {
		StatefulSet statefulSet = new StatefulSet();

		// 添加meta
		ObjectMeta metadata = new ObjectMeta();
		metadata.setName(name);
		metadata.setNamespace(namespace);
		statefulSet.setMetadata(metadata);

		// 添加spec
		StatefulSetSpec spec = new StatefulSetSpec();
		spec.setServiceName(name);
		spec.setReplicas(replicas);
		spec.setTemplate(template);
		spec.setVolumeClaimTemplates(volumeClaimTemplates);
		statefulSet.setSpec(spec);

		return statefulSet;
	}

	public PodTemplateSpec generatePodTemplateSpec(Map<String, String> labels, Integer terminationGracePeriodSeconds,
			List<Container> containers, List<Volume> volumes) {
		PodTemplateSpec template = new PodTemplateSpec();
		// metadata
		ObjectMeta metadata = new ObjectMeta();
		metadata.setLabels(labels);
		template.setMetadata(metadata);
		// spec
		PodSpec spec = new PodSpec();
		spec.setTerminationGracePeriodSeconds(terminationGracePeriodSeconds);
		spec.setContainers(containers);
		spec.setVolumes(volumes);
		template.setSpec(spec);
		return template;
	}

	public Container generateContainer(String name, String image, List<ContainerPort> ports, List<String> command,
			List<String> args, Probe readinessProbe, Probe livenessProbe, List<EnvVar> env, List<VolumeMount> volumeMounts) {
		Container container = new Container();
		container.setName(name);
		container.setImage(image);
		container.setPorts(ports);
		if (null != command) {
			container.setCommand(command);
		}
		if (null != args) {
			container.setArgs(args);
		}
		if (null != readinessProbe) {
			container.setReadinessProbe(readinessProbe);
		}
		if (null != livenessProbe) {
			container.setLivenessProbe(livenessProbe);
		}
		if (null != env) {
			container.setEnv(env);
		}
		if (null != volumeMounts) {
			container.setVolumeMounts(volumeMounts);
		}
		return container;
	}

	public PersistentVolumeClaim generatePersistentVolumeClaim(String name, String namespace, List<String> accessModes,
			ResourceRequirements resources, String storageClassName) {
		PersistentVolumeClaim persistentVolumeClaim = new PersistentVolumeClaim();

		ObjectMeta metadata = new ObjectMeta();
		metadata.setName(name);
		metadata.setNamespace(namespace);
		persistentVolumeClaim.setMetadata(metadata);

		PersistentVolumeClaimSpec spec = new PersistentVolumeClaimSpec();
		spec.setAccessModes(accessModes);
		spec.setResources(resources);
		spec.setStorageClassName(storageClassName);
		persistentVolumeClaim.setSpec(spec);

		return persistentVolumeClaim;
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
		}
		controller.getMetadata().getLabels().remove("dmz");
		controller.getMetadata().getLabels().remove("user");
		if (StringUtils.isNotBlank(service.getProxyZone())) {
			String ProxyZones[] = service.getProxyZone().split(",");
			for (int i = 0; i < ProxyZones.length; i++) {
				controller.getMetadata().getLabels().put(ProxyZones[i].toLowerCase(), ProxyZones[i]);
			}
			controller.getMetadata().getLabels().put("servicePath", service.getServicePath());
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
