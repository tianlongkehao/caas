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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.ConfigMap;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;

/**
 * ClassName:RedisController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年6月21日 下午4:03:17 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
/**
 * ClassName: RedisController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年6月22日 上午9:36:55 <br/>
 *
 * @author longkaixiang
 * @version
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

	public String createRedis(String name) {
		Map<String, Object> map = new HashMap<>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		//查询service是否已经存在
		Service service = null;
		try {
			service = client.getService(name);
		} catch (KubernetesClientException e) {
			service = null;
		}
		if (null != service) {
			map.put("message", "存在同名service：["+name+"]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		//查询configMap是否已经存在
		ConfigMap configMap = null;
		try {
			configMap = client.getConfigMap(name);
		} catch (KubernetesClientException e) {
			configMap = null;
		}
		if (null != configMap) {
			map.put("message", "存在同名configMap：["+name+"]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		//创建service
		User user = CurrentUserUtils.getInstance().getUser();
		service = generateRedisService(name, user.getNamespace());
		try {
			service = client.createService(service);
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("message", "创建service失败：["+e.getStatus().getReason()+"]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		//创建configMap
		try {
			 configMap = generateRedisConfigMap(name);
		} catch (IOException e) {
			e.printStackTrace();
			map.put("message", "初始化ConfigMap失败：["+e.getMessage()+"]");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}
		if (null != configMap) {
			try {
				configMap = client.createConfigMap(configMap);
			} catch (KubernetesClientException e) {
				e.printStackTrace();
				map.put("message", "创建ConfigMap失败：["+e.getStatus().getReason()+"]");
				map.put("status", "300");
				return JSON.toJSONString(map);
			}

		}

		return JSON.toJSONString(map);
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

		return kubernetesClientService.generateService(annotations, name, namespace, labels, ports, clusterIP, selector);
	}

	/**
	 * generateRedisConfigMap:初始化redis的configmap. <br/>
	 *
	 * @param name
	 * @return
	 * @throws IOException ConfigMap
	 */
	private ConfigMap generateRedisConfigMap(String name) throws IOException {
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
		String meetClusterFile = FileUtils.class.getClassLoader().getResource(MEET_CLUSTER_FILE).getPath();
		String meetCluster = FileUtils.readFileByLines(meetClusterFile);
		data.put(MEET_CLUSTER_FILE, meetCluster);

		return kubernetesClientService.generateConfigMap(name, namespace, data);
	}
}
