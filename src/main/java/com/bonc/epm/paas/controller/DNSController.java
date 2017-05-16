package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.DNSServiceDao;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.entity.DNSService;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
public class DNSController {
	/**
	 * 输出日志
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DNSController.class);

	// 镜像名
	@Value("${monitor.image.name}")
	public String MONITOR_IMAGE_NAME;

	@Value("${monitor.command}")
	public String MONITOR_COMMAND;

	// ping的次数
	@Value("${monitor.num}")
	public String MONITOR_NUM;

	// 频率
	@Value("${monitor.frequency}")
	public String MONITOR_FREQUENCY;

	@Value("${mysql.host}")
	public String MYSQL_HOST;

	@Value("${mysql.port}")
	public String MYSQL_PORT;

	@Value("${mysql.user}")
	public String MYSQL_USER;

	@Value("${mysql.password}")
	public String MYSQL_PASSWORD;

	@Value("${mysql.dbname}")
	public String MYSQL_DBNAME;

	@Autowired
	KubernetesClientService kubernetesClientService;

	@Autowired
	DNSServiceDao dnsServiceDao;

	/**
	 * 调用服务controller
	 */
	@Autowired
	private ServiceController serviceController;

	@Autowired
	private PortConfigDao portConfigDao;

	/**
	 * createDNSMonitor:创建一个监控. <br/>
	 *
	 * @author longkaixiang
	 * @param serviceName
	 * @param address
	 * @return String
	 */
	@RequestMapping(value = ("createDNSMonitor.do"), method = RequestMethod.POST)
	@ResponseBody
	public String createDNSMonitor(String serviceName, String address) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		User user = CurrentUserUtils.getInstance().getUser();
		if (StringUtils.isBlank(serviceName)) {
			LOG.info("创建监控失败，服务名不能为空");
			messages.add("创建监控失败，服务名不能为空");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		List<DNSService> findByServiceName = dnsServiceDao.findByServiceName(serviceName);
		if (CollectionUtils.isNotEmpty(findByServiceName)) {
			LOG.info("创建监控失败，已存在的服务名");
			messages.add("创建监控失败，已存在的服务名");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		if (StringUtils.isBlank(address)) {
			LOG.info("创建监控失败，地址不能为空");
			messages.add("创建监控失败，地址不能为空");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		List<DNSService> findByAddress = dnsServiceDao.findByAddress(address);
		if (CollectionUtils.isNotEmpty(findByAddress)) {
			LOG.info("创建监控失败，监控地址已存在");
			messages.add("创建监控失败，监控地址已存在");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		// 命令格式：monitor.sh www.baidu.com 1 192.168.0.76 3306 root 123456
		// epm_paas 15
		List<String> command = new ArrayList<>();
		command.add(MONITOR_COMMAND);

		List<String> args = new ArrayList<>();
		args.add(address);
		args.add(MONITOR_NUM);
		args.add(MYSQL_HOST);
		args.add(MYSQL_PORT);
		args.add(MYSQL_USER);
		args.add(MYSQL_PASSWORD);
		args.add(MYSQL_DBNAME);
		args.add(MONITOR_FREQUENCY);

		int nodePort = serviceController.vailPortSet();
		List<PortConfig> portConfigs = new ArrayList<>();
		PortConfig portCon = new PortConfig();
		portCon.setContainerPort(String.valueOf(8080));
		portCon.setMapPort(String.valueOf(nodePort));
		portCon.setProtocol("TCP");
		portConfigs.add(portCon);

		KubernetesAPIClientInterface client = kubernetesClientService.getClient("default");
		// 创建Service
		Service k8sService = kubernetesClientService.generateService(serviceName, portConfigs, null, serviceName, serviceName, null, null);
		try {
			k8sService = client.createService(k8sService);
		} catch (KubernetesClientException e) {
			client.deleteReplicationController(serviceName);
			LOG.error("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			messages.add("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			e.printStackTrace();
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		// 创建ReplicationController
		ReplicationController replicationController = kubernetesClientService
				.generateSimpleReplicationController(serviceName, 1, null, null, null, MONITOR_IMAGE_NAME, portConfigs, 1.0, "2048.0", null, serviceName, serviceName, null, null, command, args, null, false);
		try {
			replicationController = client.createReplicationController(replicationController);
		} catch (KubernetesClientException e) {
			LOG.error("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			messages.add("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			e.printStackTrace();
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}

		// 持久化
		DNSService service = new DNSService();
		service.setServiceName(serviceName);
		service.setCreateBy(user.getId());
		service.setCreateDate(new Date());
		service.setAddress(address);
		service = dnsServiceDao.save(service);

		portCon.setDnsServiceId(service.getId());
		portConfigDao.save(portCon);

		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * deleteDNSMonitor:删除一个监控. <br/>
	 *
	 * @author longkaixiang
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = ("deleteDNSMonitor.do"), method = RequestMethod.GET)
	@ResponseBody
	public String deleteDNSMonitor(long id) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		DNSService service = dnsServiceDao.findOne(id);
		// 查找不到的时候返回异常
		if (service == null) {
			messages.add("查找监控失败：[id:" + id + "]");
			LOG.error("查找监控失败：[id:" + id + "]");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}

		KubernetesAPIClientInterface client = kubernetesClientService.getClient(KubernetesClientService.adminNameSpace);
		// 删除rc
		ReplicationController controller = null;
		try {
			//查询ReplicationController是否已经创建
			controller = client.getReplicationController(service.getServiceName());
		} catch (Exception e1) {
			controller = null;
		}
		if (controller != null) {
			controller = client.updateReplicationController(service.getServiceName(), 0);
			if (controller != null && controller.getSpec().getReplicas() == 0) {
				Status status = client.deleteReplicationController(service.getServiceName());
				if (!status.getStatus().equals("Success")) {
					LOG.error(
							"Delete a Replication Controller failed:DNSServiceName[" + service.getServiceName() + "]");
					messages.add(
							"Delete a Replication Controller failed:DNSServiceName[" + service.getServiceName() + "]");
					map.put("status", "400");
					map.put("messages", messages);
					return JSON.toJSONString(map);
				}
			} else {
				LOG.error("Update a Replication Controller (update the number of replicas) failed:DNSServiceName["
						+ service.getServiceName() + "]");
				messages.add("Update a Replication Controller (update the number of replicas) failed:DNSServiceName["
						+ service.getServiceName() + "]");
				map.put("status", "400");
				map.put("messages", messages);
				return JSON.toJSONString(map);
			}
		}

		// 删除svc
		Service k8sService = null;
		try {
			// 查询svc是否已经创建
			k8sService = client.getService(service.getServiceName());
		} catch (KubernetesClientException e) {
			k8sService = null;
		}
		if (null != k8sService) {
			Status status = client.deleteService(service.getServiceName());
			if (!status.getStatus().equals("Success")) {
				LOG.error("Delete a Service failed:ServiceName[" + service.getServiceName() + "]");
				messages.add("Delete a Service failed:ServiceName[" + service.getServiceName() + "]");
				map.put("status", "400");
				map.put("messages", messages);
				return JSON.toJSONString(map);
			}
		}
		//持久化
		dnsServiceDao.delete(service);
		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);

	}

}
