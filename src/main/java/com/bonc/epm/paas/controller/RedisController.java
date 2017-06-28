/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:RedisController.java
 * Package Name:com.bonc.epm.paas.controller
 * Date:2017年6月21日下午4:03:17
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.RedisConstant;
import com.bonc.epm.paas.dao.RedisDao;
import com.bonc.epm.paas.entity.Redis;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.ConfigMap;
import com.bonc.epm.paas.kubernetes.model.ConfigMapVolumeSource;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.DownwardAPIVolumeFile;
import com.bonc.epm.paas.kubernetes.model.DownwardAPIVolumeSource;
import com.bonc.epm.paas.kubernetes.model.EnvVar;
import com.bonc.epm.paas.kubernetes.model.EnvVarSource;
import com.bonc.epm.paas.kubernetes.model.ExecAction;
import com.bonc.epm.paas.kubernetes.model.KeyToPath;
import com.bonc.epm.paas.kubernetes.model.ObjectFieldSelector;
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaim;
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaimVolumeSource;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.Probe;
import com.bonc.epm.paas.kubernetes.model.ResourceRequirements;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.Volume;
import com.bonc.epm.paas.kubernetes.model.VolumeMount;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;

/**
 * ClassName:RedisController <br/>
 * Date: 2017年6月21日 下午4:03:17 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
@Controller
@RequestMapping(value = "/RedisController")
public class RedisController {

	private static final String REDIS_CONF = "redis.conf";
	private static final String REDIS_CONF_FILE = "REDISCONF";
	private static final String BOOTSTRAP_POD = "bootstrap-pod.sh";
	private static final String BOOTSTRAP_POD_FILE = "BOOTSTRAPPOD";
	private static final String MEET_CLUSTER = "meet-cluster.sh";
	private static final String MEET_CLUSTER_FILE = "MEETCLUSTER";

	@Autowired
	KubernetesClientService kubernetesClientService;

	@Autowired
	RedisDao redisDao;

	/**
	 * 帮助文档
	 *
	 * @param model
	 *            添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	public String indexRedis(Model model) {
		List<Redis> redisList = redisDao.findByCreateBy(CurrentUserUtils.getInstance().getUser().getId());
		model.addAttribute("redisList", redisList);
		model.addAttribute("menu_flag", "database");
		model.addAttribute("li_flag", "redis");
		return "database/redis.jsp";
	}

	@RequestMapping(value = "/redis/detail", method = RequestMethod.GET)
	public String redisDetail(Long id, Model model) {
		Redis redis = redisDao.findOne(id);
		model.addAttribute("redis", redis);
		model.addAttribute("menu_flag", "database");
		model.addAttribute("li_flag", "redis");
		return "database/redis-detail.jsp";
	}

	@RequestMapping(value = "/redis/create", method = RequestMethod.GET)
	public String redisCreate(Model model) {
		model.addAttribute("menu_flag", "database");
		model.addAttribute("li_flag", "redis");
		return "database/redis-create.jsp";
	}

	/**
	 * createRedisService:创建RedisService. <br/>
	 *
	 * @param redis
	 * @return String
	 */
	@RequestMapping(value = "createRedisService.do", method = RequestMethod.POST)
	@ResponseBody
	public String createRedisService(Redis redis) {
		Map<String, Object> map = new HashMap<>();
		// 查询是否有同名服务
		if (CollectionUtils.isNotEmpty(redisDao.findByName(redis.getName()))) {
			map.put("message", "存在同名的redis服务：[" + redis.getName() + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}
		redis.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
		redis.setCreateDate(new Date());
		redis.setStatus(RedisConstant.REDIS_STATUS_STOP);
		redisDao.save(redis);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * modifyRedisService:修改服务配置. <br/>
	 *
	 * @param redis
	 * @return String
	 */
	@RequestMapping(value = "modifyRedisService.do", method = RequestMethod.POST)
	@ResponseBody
	public String modifyRedisService(Redis redis) {
		Map<String, Object> map = new HashMap<>();
		Redis service = redisDao.findOne(redis.getId());
		if (service == null) {
			map.put("message", "找不到redis服务：[" + redis.getId() + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}
		redis.setStatus(service.getStatus());
		redis.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
		redis.setCreateDate(new Date());
		redisDao.save(redis);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * deleteRedisService:删除RedisService. <br/>
	 *
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = "deleteRedisService.do", method = RequestMethod.GET)
	@ResponseBody
	public String deleteRedisService(long id) {
		Map<String, Object> map = new HashMap<>();
		Redis redis = redisDao.findOne(id);
		if (null == redis) {
			map.put("message", "找不到对应的服务：[" + id + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		if (redis.getStatus().equals(RedisConstant.REDIS_STATUS_RUNNING)) {
			map = deleteRedis(redis.getName());
			if (map.get("status").equals("200")) {
				redisDao.delete(redis);
			}
		} else {
			redisDao.delete(redis);
			map.put("status", "200");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * startRedisService:启动redis服务. <br/>
	 *
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = "startRedisService.do", method = RequestMethod.GET)
	@ResponseBody
	public String startRedisService(long id) {
		Map<String, Object> map = new HashMap<>();
		Redis redis = redisDao.findOne(id);
		if (null == redis) {
			map.put("message", "找不到对应的服务：[" + id + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		if (!redis.getStatus().equals(RedisConstant.REDIS_STATUS_RUNNING)) {
			map = createRedis(redis.getName());
			if (map.get("status").equals("200")) {
				redis.setStatus(RedisConstant.REDIS_STATUS_RUNNING);
				redisDao.save(redis);
				return JSON.toJSONString(map);
			}
		} else {
			map.put("status", "300");
			map.put("message", "该服务当前是运行状态！["+redis.getName()+"]");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * stopRedisService:停止redis服务. <br/>
	 *
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = "stopRedisService.do", method = RequestMethod.GET)
	@ResponseBody
	public String stopRedisService(long id) {
		Map<String, Object> map = new HashMap<>();
		Redis redis = redisDao.findOne(id);
		if (null == redis) {
			map.put("message", "找不到对应的服务：[" + id + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		if (redis.getStatus().equals(RedisConstant.REDIS_STATUS_RUNNING)) {
			map = deleteRedis(redis.getName());
			if (map.get("status").equals("200")) {
				redis.setStatus(RedisConstant.REDIS_STATUS_STOP);
				redisDao.save(redis);
				return JSON.toJSONString(map);
			}
		} else {
			map.put("status", "300");
			map.put("message", "该服务当前是停止状态！["+redis.getName()+"]");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * getPodList:获取redis服务的podlist. <br/>
	 *
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = "getPodList.do", method = RequestMethod.GET)
	@ResponseBody
	public String getPodList(long id) {
		Map<String, Object> map = new HashMap<>();
		//判断服务是否存在
		Redis redis = redisDao.findOne(id);
		if (null == redis) {
			map.put("message", "找不到对应的服务：[" + id + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		KubernetesAPIClientInterface apiClient = kubernetesClientService.getClient();
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();
		//获取StatefulSet
		StatefulSet statefulSet;
		try {
			statefulSet = apisClient.getStatefulSet(redis.getName());
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("message", "找不到对应的StatefulSet：[" + e.getStatus().getReason() + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}
		//获取PodList
		PodList pods;
		try {
			pods = apiClient.getLabelSelectorPods(statefulSet.getSpec().getSelector().getMatchLabels());
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("message", "找不到对应的PodList：[" + e.getStatus().getReason() + "]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		map.put("podList", pods.getItems());
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * createRedis:创建redis. <br/>
	 *
	 * @param name
	 * @return Map<String,Object>
	 */
	private Map<String, Object> createRedis(String name) {
		Map<String, Object> map = new HashMap<>();
		KubernetesAPIClientInterface apiClient = kubernetesClientService.getClient();
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();
		// 查询service是否已经存在
		Service service = null;
		try {
			service = apiClient.getService(name);
		} catch (KubernetesClientException e) {
			service = null;
		}
		if (null != service) {
			map.put("message", "存在同名service：[" + name + "]");
			map.put("status", "300");
			return map;
		}

		// 查询configMap是否已经存在
		ConfigMap configMap = null;
		try {
			configMap = apiClient.getConfigMap(name);
		} catch (KubernetesClientException e) {
			configMap = null;
		}
		if (null != configMap) {
			map.put("message", "存在同名configMap：[" + name + "]");
			map.put("status", "300");
			return map;
		}

		// 查询statefulSet是否已经存在
		StatefulSet statefulSet = null;
		try {
			statefulSet = apisClient.getStatefulSet(name);
		} catch (KubernetesClientException e1) {
			statefulSet = null;
		}
		if (null != statefulSet) {
			map.put("message", "存在同名statefulSet：[" + name + "]");
			map.put("status", "300");
			return map;
		}

		// 创建service
		User user = CurrentUserUtils.getInstance().getUser();
		service = generateRedisService(name, user.getNamespace());
		try {
			service = apiClient.createService(service);
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("message", "创建service失败：[" + e.getStatus().getReason() + "]");
			map.put("status", "300");
			return map;
		}

		// 创建configMap
		try {
			configMap = generateRedisConfigMap(name, user.getNamespace());
		} catch (IOException e) {
			e.printStackTrace();
			map.put("message", "初始化ConfigMap失败：[" + e.getMessage() + "]");
			map.put("status", "300");
			return map;
		}
		if (null != configMap) {
			try {
				configMap = apiClient.createConfigMap(configMap);
			} catch (KubernetesClientException e) {
				e.printStackTrace();
				apiClient.deleteService(name);
				map.put("message", "创建ConfigMap失败：[" + e.getStatus().getReason() + "]");
				map.put("status", "300");
				return map;
			}
		}

		// 创建statefulSet
		statefulSet = generateRedisStatefulSet(name);
		try {
			apisClient.createStatefulSet(statefulSet);
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			apiClient.deleteService(name);
			apiClient.deleteConfigMap(name);
			map.put("message", "创建StatefulSet失败：[" + e.getStatus().getReason() + "]");
			map.put("status", "300");
			return map;
		}

		map.put("service", service);
		map.put("configMap", configMap);
		map.put("statefulSet", statefulSet);
		map.put("status", "200");
		return map;
	}

	private Map<String, Object> deleteRedis(String name){
		Map<String, Object> map = new HashMap<>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();

		try {
			client.deleteConfigMap(name);
			client.deleteService(name);
			apisClient.deleteStatefulSet(name);
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("status", "300");
			map.put("message", "删除失败：" + e.getStatus().getReason());
			return map;
		}
		map.put("status", "200");
		return map;
	}

	/**
	 * generateRedisService:初始化redis的service. <br/>
	 *
	 * @param name
	 * @param namespace
	 * @return Service
	 */
	private Service generateRedisService(String name, String namespace) {
		Map<String, String> annotations = new HashMap<>();
		annotations.put("service.alpha.kubernetes.io/tolerate-unready-endpoints", "true");

		Map<String, String> labels = new HashMap<>();
		labels.put("app", name);

		List<ServicePort> ports = new ArrayList<>();
		ServicePort servicePort1 = new ServicePort();
		servicePort1.setName("client");
		servicePort1.setPort(6379);
		servicePort1.setTargetPort(6379);
		ports.add(servicePort1);

		ServicePort servicePort2 = new ServicePort();
		servicePort2.setName("gossip");
		servicePort2.setPort(16379);
		servicePort2.setTargetPort(16379);
		ports.add(servicePort2);

		ServicePort servicePort3 = new ServicePort();
		servicePort3.setName("redis-exporter");
		servicePort3.setPort(9121);
		servicePort3.setTargetPort(9121);
		ports.add(servicePort3);

		String clusterIP = "None";

		Map<String, String> selector = new HashMap<>();
		selector.put("app", name);

		return kubernetesClientService.generateService(annotations, name, namespace, labels, ports, clusterIP,
				selector);
	}

	/**
	 * generateRedisConfigMap:初始化redis的configmap. <br/>
	 *
	 * @param name
	 * @param nameSpace
	 * @return
	 * @throws IOException
	 *             ConfigMap
	 */
	private ConfigMap generateRedisConfigMap(String name, String nameSpace) throws IOException {
		User user = CurrentUserUtils.getInstance().getUser();
		String namespace = user.getNamespace();
		Map<String, String> data = new HashMap<>();

		// redis.conf文件的配置
		Map<String, String> redisConfReplaceMap = new HashMap<>();
		// TODO 替换文字
		String redisConfFile = FileUtils.class.getClassLoader().getResource(REDIS_CONF_FILE).getPath();
		String redisConf = FileUtils.readFileByLines(redisConfFile, redisConfReplaceMap);
		data.put(REDIS_CONF, redisConf);

		// bootstrap-pod.sh文件的配置
		String bootstrapPodFile = FileUtils.class.getClassLoader().getResource(BOOTSTRAP_POD_FILE).getPath();
		String bootstrapPod = FileUtils.readFileByLines(bootstrapPodFile);
		data.put(BOOTSTRAP_POD, bootstrapPod);

		// meet-cluster.sh文件的配置
		Map<String, String> meetClusterReplaceMap = new HashMap<>();
		meetClusterReplaceMap.put("${serviceName}", name);
		meetClusterReplaceMap.put("${nameSpace}", nameSpace);
		String meetClusterFile = FileUtils.class.getClassLoader().getResource(MEET_CLUSTER_FILE).getPath();
		String meetCluster = FileUtils.readFileByLines(meetClusterFile, meetClusterReplaceMap);
		data.put(MEET_CLUSTER, meetCluster);

		return kubernetesClientService.generateConfigMap(name, namespace, data);
	}

	/**
	 * generateRedisStatefulSet:初始化redis的StatefulSet. <br/>
	 *
	 * @param name
	 * @return StatefulSet
	 */
	public StatefulSet generateRedisStatefulSet(String name) {

		/*
		 * 1.创建statefulSet的template
		 */

		// 1.1创建template的labels
		Map<String, String> labels = new HashMap<>();
		labels.put("app", name);

		// 1.2创建template的containers
		List<Container> containers = new ArrayList<>();
		// 1.2.1创建container redis-cluster
		List<ContainerPort> ports1 = new ArrayList<>();
		ContainerPort port11 = new ContainerPort();
		port11.setContainerPort(6379);
		port11.setName("client");
		ports1.add(port11);
		ContainerPort port12 = new ContainerPort();
		port12.setContainerPort(16379);
		port12.setName("gossip");
		ports1.add(port12);

		List<String> command = new ArrayList<>();
		command.add("sh");

		List<String> args = new ArrayList<>();
		args.add("/conf/bootstrap-pod.sh");

		Probe readinessProbe = new Probe();
		ExecAction readinessProbeExecAction = new ExecAction();
		List<String> readinessProbeExec = new ArrayList<>();
		readinessProbeExec.add("sh");
		readinessProbeExec.add("-c");
		readinessProbeExec.add("redis-cli -h $(hostname) ping");
		readinessProbeExecAction.setCommand(readinessProbeExec);
		readinessProbe.setExec(readinessProbeExecAction);
		readinessProbe.setInitialDelaySeconds(15);
		readinessProbe.setTimeoutSeconds(5);

		Probe livenessProbe = new Probe();
		ExecAction livenessProbeExecAction = new ExecAction();
		List<String> livenessProbeExec = new ArrayList<>();
		livenessProbeExec.add("sh");
		livenessProbeExec.add("-c");
		livenessProbeExec.add("redis-cli -h $(hostname) ping");
		livenessProbeExecAction.setCommand(livenessProbeExec);
		livenessProbe.setExec(livenessProbeExecAction);
		livenessProbe.setInitialDelaySeconds(20);
		livenessProbe.setTimeoutSeconds(3);

		List<EnvVar> env = new ArrayList<>();
		EnvVar envVar = new EnvVar();
		envVar.setName("POD_NAMESPACE");
		EnvVarSource valueFrom = new EnvVarSource();
		ObjectFieldSelector fieldRef = new ObjectFieldSelector();
		fieldRef.setFieldPath("metadata.namespace");
		valueFrom.setFieldRef(fieldRef);
		envVar.setValueFrom(valueFrom);
		env.add(envVar);

		List<VolumeMount> volumeMounts = new ArrayList<>();
		VolumeMount volumeMount1 = new VolumeMount();
		volumeMount1.setName("data");
		volumeMount1.setMountPath("/var/lib/redis");
		volumeMount1.setReadOnly(false);
		volumeMounts.add(volumeMount1);

		VolumeMount volumeMount2 = new VolumeMount();
		volumeMount2.setName("conf");
		volumeMount2.setMountPath("/conf");
		volumeMount2.setReadOnly(false);
		volumeMounts.add(volumeMount2);

		VolumeMount volumeMount3 = new VolumeMount();
		volumeMount3.setName("podinfo");
		volumeMount3.setMountPath("/etc/podinfo");
		volumeMount3.setReadOnly(false);
		volumeMounts.add(volumeMount3);

		Container container1 = kubernetesClientService.generateContainer(name, "dipperroy/redis:3.2.8-rb", ports1,
				command, args, readinessProbe, livenessProbe, env, volumeMounts);
		containers.add(container1);

		// 1.2.2创建container redis-exporter
		List<ContainerPort> ports2 = new ArrayList<>();
		ContainerPort port21 = new ContainerPort();
		port21.setName("redis-exporter");
		port21.setContainerPort(9291);
		ports2.add(port21);

		Container container2 = kubernetesClientService.generateContainer("redis-exporter",
				"docker.io/oliver006/redis_exporter", ports2, null, null, null, null, null, null);
		containers.add(container2);

		// 1.3创建template的volumes
		List<Volume> volumes = new ArrayList<>();
		// 1.3.1创建volumes data
		Volume volume1 = new Volume();
		volume1.setName("data");
		PersistentVolumeClaimVolumeSource persistentVolumeClaimVolumeSource = new PersistentVolumeClaimVolumeSource();
		persistentVolumeClaimVolumeSource.setClaimName("data");
		volume1.setPersistentVolumeClaim(persistentVolumeClaimVolumeSource);
		volumes.add(volume1);
		// 1.3.2创建volumes conf
		Volume volume2 = new Volume();
		volume2.setName("conf");
		ConfigMapVolumeSource configMap = new ConfigMapVolumeSource();
		configMap.setName(name);
		List<KeyToPath> items = new ArrayList<>();
		KeyToPath keyToPath1 = new KeyToPath();
		keyToPath1.setKey(REDIS_CONF);
		keyToPath1.setPath(REDIS_CONF);
		items.add(keyToPath1);
		KeyToPath keyToPath2 = new KeyToPath();
		keyToPath2.setKey(BOOTSTRAP_POD);
		keyToPath2.setPath(BOOTSTRAP_POD);
		items.add(keyToPath2);
		KeyToPath keyToPath3 = new KeyToPath();
		keyToPath3.setKey(MEET_CLUSTER);
		keyToPath3.setPath(MEET_CLUSTER);
		items.add(keyToPath3);
		configMap.setItems(items);
		volume2.setConfigMap(configMap);
		volumes.add(volume2);
		// 1.3.3创建volumes podinfo
		Volume volume3 = new Volume();
		volume3.setName("podinfo");
		DownwardAPIVolumeSource downwardAPI = new DownwardAPIVolumeSource();
		List<DownwardAPIVolumeFile> downwardAPIItems = new ArrayList<>();

		DownwardAPIVolumeFile downwardAPIVolumeFile1 = new DownwardAPIVolumeFile();
		downwardAPIVolumeFile1.setPath("labels");
		ObjectFieldSelector fieldRef1 = new ObjectFieldSelector();
		fieldRef1.setFieldPath("metadata.labels");
		downwardAPIVolumeFile1.setFieldRef(fieldRef1);
		downwardAPIItems.add(downwardAPIVolumeFile1);

		DownwardAPIVolumeFile downwardAPIVolumeFile2 = new DownwardAPIVolumeFile();
		downwardAPIVolumeFile2.setPath("annotations");
		ObjectFieldSelector fieldRef2 = new ObjectFieldSelector();
		fieldRef2.setFieldPath("metadata.annotations");
		downwardAPIVolumeFile2.setFieldRef(fieldRef2);
		downwardAPIItems.add(downwardAPIVolumeFile2);

		DownwardAPIVolumeFile downwardAPIVolumeFile3 = new DownwardAPIVolumeFile();
		downwardAPIVolumeFile3.setPath("pod_name");
		ObjectFieldSelector fieldRef3 = new ObjectFieldSelector();
		fieldRef3.setFieldPath("metadata.name");
		downwardAPIVolumeFile3.setFieldRef(fieldRef3);
		downwardAPIItems.add(downwardAPIVolumeFile3);

		DownwardAPIVolumeFile downwardAPIVolumeFile4 = new DownwardAPIVolumeFile();
		downwardAPIVolumeFile4.setPath("pod_namespace");
		ObjectFieldSelector fieldRef4 = new ObjectFieldSelector();
		fieldRef4.setFieldPath("metadata.namespace");
		downwardAPIVolumeFile4.setFieldRef(fieldRef4);
		downwardAPIItems.add(downwardAPIVolumeFile4);

		downwardAPI.setItems(downwardAPIItems);
		volume3.setDownwardAPI(downwardAPI);
		volumes.add(volume3);
		// 1.4
		PodTemplateSpec template = kubernetesClientService.generatePodTemplateSpec(labels, 10, containers, volumes);

		/*
		 * 2.创建statefulSet的volumeClaimTemplates
		 */
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();

		List<PersistentVolumeClaim> volumeClaimTemplates = new ArrayList<>();
		List<String> accessModes = new ArrayList<>();
		accessModes.add("ReadWriteOnce");
		accessModes.add("ReadWriteMany");
		ResourceRequirements resources = new ResourceRequirements();
		Map<String, Object> requests = new HashMap<>();
		requests.put("storage", "1Gi");
		resources.setRequests(requests);

		String storageClassName = "ceph-rbd";
		PersistentVolumeClaim persistentVolumeClaim = kubernetesClientService.generatePersistentVolumeClaim("data",
				namespace, accessModes, resources, storageClassName);
		volumeClaimTemplates.add(persistentVolumeClaim);
		/*
		 * 3.创建statefulSet
		 */
		StatefulSet statefulSet = kubernetesClientService.generateStatefulSet(name, namespace, 8, template,
				volumeClaimTemplates);

		return statefulSet;
	}
}
