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
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.dao.DNSServiceDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.DNSService;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;

/**
 * ClassName: DNSController <br/>
 * date: 2017年6月9日 下午3:22:33 <br/>
 *
 * @author longkaixiang
 * @version
 */
@Controller
@RequestMapping(value="/DNSController")
public class DNSController2 {
	/**
	 * 输出日志
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DNSController.class);

	@Autowired
	KubernetesClientService kubernetesClientService;

	@Autowired
	DockerClientService dockerClientService;

	/**
	 * 调用服务controller
	 */
	@Autowired
	private ServiceController serviceController;

	@Autowired
	DNSServiceDao dnsServiceDao;

	// 镜像名
	@Value("${monitor.image.name}")
	public String MONITOR_IMAGE_NAME;

	@Value("${monitor.name}")
	public String DNS_MONITOR_NAME;

	/**
	 * createDnsService:创建DNSService. <br/>
	 *
	 * @param address
	 * @return String
	 */
	@RequestMapping(value="/createDnsService.do", method=RequestMethod.POST)
	@ResponseBody
	public String createDnsService(String address) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		User user = CurrentUserUtils.getInstance().getUser();
		if (StringUtils.isBlank(address)) {
			LOG.info("创建监控地址失败，地址不能为空");
			messages.add("创建监控地址失败，地址不能为空");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		List<DNSService> findByAddress = dnsServiceDao.findByAddress(address);
		if (CollectionUtils.isNotEmpty(findByAddress)) {
			LOG.info("创建监控地址失败，监控地址已存在");
			messages.add("创建监控地址失败，监控地址已存在");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		// 持久化
		DNSService service = new DNSService();
		service.setIsMonitor(CommConstant.TYPE_NO_VALUE);
		service.setAddress(address);
		service.setCreateBy(user.getId());
		service.setCreateDate(new Date());
		service = dnsServiceDao.save(service);

		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * modifyDnsService:修改DNSService. <br/>
	 *
	 * @param dnsService
	 * @return String
	 */
	@RequestMapping(value="/modifyDnsService.do", method=RequestMethod.POST)
	@ResponseBody
	public String modifyDnsService(DNSService dnsService) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		User user = CurrentUserUtils.getInstance().getUser();
		DNSService service = dnsServiceDao.findOne(dnsService.getId());
		// 查找不到的时候返回异常
		if (service == null) {
			messages.add("查找监控失败：[id:" + dnsService.getId() + "]");
			LOG.error("查找监控失败：[id:" + dnsService.getId() + "]");
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		service.setAddress(dnsService.getAddress());
		service.setIsMonitor(dnsService.getIsMonitor());
		service.setCreateBy(user.getId());
		service.setCreateDate(new Date());
		service = dnsServiceDao.save(service);
		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * deleteDnsService:删除DNSService. <br/>
	 *
	 * @param id
	 * @return String
	 */
	@RequestMapping(value="/deleteDnsService.do", method=RequestMethod.POST)
	@ResponseBody
	public String deleteDnsService(long id) {
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
		dnsServiceDao.delete(service);
		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

	public void startMonitor() {
		new Thread(){
			public void run() {
				dnsServiceDao.deleteAll();
				while(true){
					KubernetesAPIClientInterface client = kubernetesClientService.getClient(KubernetesClientService.adminNameSpace);
					Service service = null;
					try {
						//查找对应的监控service 若没有找到则创建服务
						service = client.getService(DNS_MONITOR_NAME);
						if (null == service) {
							createDNSMonitor();
						}
					} catch (KubernetesClientException e) {
						e.printStackTrace();
						createDNSMonitor();
						continue;
					}
					if (null != service) {
						Map<String, String> selector = service.getSpec().getSelector();
						PodList labelSelectorPods;
						try {
							labelSelectorPods = client.getLabelSelectorPods(selector);
						} catch (KubernetesClientException e) {
							LOG.error(e.getStatus().getReason());
							continue;
						}
						if (null != labelSelectorPods && CollectionUtils.isNotEmpty(labelSelectorPods.getItems())) {
							for (Pod pod : labelSelectorPods.getItems()) {
								if (kubernetesClientService.isRunning(pod)) {
									String containerID = pod.getStatus().getContainerStatuses().get(0).getContainerID().replace("docker://",
											"");
									String hostIP = pod.getStatus().getHostIP();
									DockerClient dockerClient = dockerClientService.getSpecifiedDockerClientInstance(hostIP);
									ExecCreateCmdResponse exec = dockerClient.execCreateCmd(containerID).withCmd("nslookup","www.baidu.com").exec();
									dockerClient.execStartCmd(exec.getId()).exec(new )
								}
							}
						}
					}
				}
			}
		}.start();
	}

	private String createDNSMonitor() {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();

		List<String> command = new ArrayList<>();
		command.add("sleep");

		List<String> args = new ArrayList<>();
		args.add("9999999999");

		int nodePort = serviceController.vailPortSet();
		List<PortConfig> portConfigs = new ArrayList<>();
		PortConfig portCon = new PortConfig();
		portCon.setContainerPort(String.valueOf(8080));
		portCon.setMapPort(String.valueOf(nodePort));
		portCon.setProtocol("TCP");
		portConfigs.add(portCon);

		KubernetesAPIClientInterface client = kubernetesClientService.getClient(KubernetesClientService.adminNameSpace);
		// 创建Service
		Service k8sService = kubernetesClientService.generateService(DNS_MONITOR_NAME, portConfigs, null, DNS_MONITOR_NAME,
				"");
		try {
			k8sService = client.createService(k8sService);
		} catch (KubernetesClientException e) {
			LOG.error("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			messages.add("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			e.printStackTrace();
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}
		// 创建ReplicationController
		ReplicationController replicationController = kubernetesClientService.generateSimpleReplicationController(
				DNS_MONITOR_NAME, 1, null, null, null, MONITOR_IMAGE_NAME, portConfigs, 1.0, "1024.0", null, DNS_MONITOR_NAME,
				"", new ArrayList<>(), command, args, new ArrayList<>(), false);
		try {
			replicationController = client.createReplicationController(replicationController);
		} catch (KubernetesClientException e) {
			client.deleteService(DNS_MONITOR_NAME);
			LOG.error("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			messages.add("创建监控失败：[Reason:" + e.getStatus().getReason() + "]");
			e.printStackTrace();
			map.put("status", "400");
			map.put("messages", messages);
			return JSON.toJSONString(map);
		}

		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}


}
