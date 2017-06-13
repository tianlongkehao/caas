package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
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
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.dao.DNSServiceDao;
import com.bonc.epm.paas.dao.PingResultDao;
import com.bonc.epm.paas.docker.model.ExecStartStringResultCallback;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.DNSService;
import com.bonc.epm.paas.entity.PingResult;
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
public class DNSController {
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

	@Autowired
	PingResultDao pingResultDao;

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

//	/**
//	 * modifyDnsService:修改DNSService. <br/>
//	 *
//	 * @param dnsService
//	 * @return String
//	 */
//	@RequestMapping(value="/modifyDnsService.do", method=RequestMethod.POST)
//	@ResponseBody
//	public String modifyDnsService(DNSService dnsService) {
//		Map<String, Object> map = new HashMap<>();
//		List<String> messages = new ArrayList<>();
//		User user = CurrentUserUtils.getInstance().getUser();
//		DNSService service = dnsServiceDao.findOne(dnsService.getId());
//		// 查找不到的时候返回异常
//		if (service == null) {
//			messages.add("查找监控失败：[id:" + dnsService.getId() + "]");
//			LOG.error("查找监控失败：[id:" + dnsService.getId() + "]");
//			map.put("status", "400");
//			map.put("messages", messages);
//			return JSON.toJSONString(map);
//		}
//		service.setAddress(dnsService.getAddress());
//		service.setIsMonitor(dnsService.getIsMonitor());
//		service.setCreateBy(user.getId());
//		service.setCreateDate(new Date());
//		service = dnsServiceDao.save(service);
//
//		if (CollectionUtils.isEmpty(messages)) {
//			map.put("status", "200");
//		} else {
//			map.put("status", "400");
//			map.put("messages", messages);
//		}
//		return JSON.toJSONString(map);
//	}

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

//	/**
//	 * modifySleepTime:修改监控循环的时间. <br/>
//	 *
//	 * @param sleeptime
//	 * @return String
//	 */
//	@RequestMapping(value="/modifySleepTime.do", method=RequestMethod.POST)
//	@ResponseBody
//	public String modifySleepTime(Integer sleeptime) {
//		Map<String, String> map = new HashMap<>();
//		Iterable<DNSService> all = dnsServiceDao.findAll();
//		for (DNSService dnsService2 : all) {
//			dnsService2.setSleepTime(sleeptime);
//		}
//		dnsServiceDao.save(all);
//		map.put("status", "200");
//		return JSON.toJSONString(map);
//	}

	/**
	 * startMonitor:初始化监控. <br/>
	 * void
	 */
	public void startMonitor() {
		new Thread() {
			public void run() {
				while (true) {
					String checkDnsResult = getCheckDnsResult(true);
					JSONObject parseObject = JSONObject.parseObject(checkDnsResult);
					if (parseObject.get("status").equals("200")) {
						List<PingResult> pingResults = JSONObject
								.parseArray(parseObject.get("pingResultList").toString(), PingResult.class);
						pingResultDao.save(pingResults);
					}
					int sleepTime = 60000;
					Iterable<DNSService> allService = dnsServiceDao.findAll();
					Iterator<DNSService> iterator = allService.iterator();
					while (iterator.hasNext()) {
						DNSService dnsService2 = iterator.next();
						if (dnsService2.getSleepTime() != null) {
							sleepTime = dnsService2.getSleepTime();
							break;
						}
					}
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * createDNSMonitor:创建监控用的服务. <br/>
	 *
	 * @return String
	 */
	public String createDNSMonitor() {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();

		List<String> command = new ArrayList<>();
		command.add("top");

		int nodePort = serviceController.vailPortSet();
		List<PortConfig> portConfigs = new ArrayList<>();
		PortConfig portCon = new PortConfig();
		portCon.setContainerPort(String.valueOf(8080));
		portCon.setMapPort(String.valueOf(nodePort));
		portCon.setProtocol("TCP");
		portConfigs.add(portCon);

		KubernetesAPIClientInterface client = kubernetesClientService.getClient(KubernetesClientService.adminNameSpace);
		// 创建Service
		Service service = null;
		try {
			service = client.getService(DNS_MONITOR_NAME);
		} catch (KubernetesClientException e1) {
			service = null;
		}
		Service k8sService = kubernetesClientService.generateService(DNS_MONITOR_NAME, portConfigs, null, DNS_MONITOR_NAME,
				"");
		if (service == null) {
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
		}

		// 创建ReplicationController
		ReplicationController rc = null;
		try {
			rc = client.getReplicationController(DNS_MONITOR_NAME);
		} catch (KubernetesClientException e1) {
			rc = null;
		}
		if (rc == null) {
			ReplicationController replicationController = kubernetesClientService.generateSimpleReplicationController(
					DNS_MONITOR_NAME, 1, null, null, null, MONITOR_IMAGE_NAME, portConfigs, 1.0, "1024.0", null, DNS_MONITOR_NAME,
					"", new ArrayList<>(), command, null, new ArrayList<>(), false);
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
		}

		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * getCheckDnsResult:获取dns解析结果. <br/>
	 *
	 * @return String
	 */
	@RequestMapping(value="getCheckDnsResult.do", method=RequestMethod.GET)
	@ResponseBody
	private String getCheckDnsResult(Boolean isMonitorCheck) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		List<PingResult> pingResultList = new ArrayList<>();
		map.put("messages", messages);
		map.put("pingResultList", pingResultList);

		// 检查是否有地址需要解析
		List<DNSService> dnsServices = null;
		if (BooleanUtils.isTrue(isMonitorCheck)) {
			dnsServices = dnsServiceDao.findByIsMonitor(CommConstant.TYPE_YES_VALUE);
		} else {
			dnsServices = (List<DNSService>) dnsServiceDao.findAll();
		}
		if (CollectionUtils.isEmpty(dnsServices)) {
			messages.add("没有需要检测的服务。");
			map.put("status", 300);
			return JSON.toJSONString(map);
		}

		// 检查是否已经创建服务
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(KubernetesClientService.adminNameSpace);
		Service service = null;
		while(true){
			try {
				// 查找对应的监控service 若没有找到则创建服务
				service = client.getService(DNS_MONITOR_NAME);
				if (null == service) {
					createDNSMonitor();
					continue;
				}
			} catch (KubernetesClientException e) {
				createDNSMonitor();
				continue;
			}

			if (null != service) {
				Map<String, String> selector = service.getSpec().getSelector();
				PodList labelSelectorPods;
				try {
					labelSelectorPods = client.getLabelSelectorPods(selector);
				} catch (KubernetesClientException e) {
					continue;
				}
				if (null != labelSelectorPods && CollectionUtils.isNotEmpty(labelSelectorPods.getItems())) {
					for (Pod pod : labelSelectorPods.getItems()) {
						if (kubernetesClientService.isRunning(pod)) {
							for (DNSService dnsService : dnsServices) {
								String checkResult = checkDns(pod, dnsService.getAddress());
								PingResult pingResult = parsePingResult(dnsService.getAddress(), checkResult);
								pingResultList.add(pingResult);
							}
						}
					}
				}
			}
			if (CollectionUtils.isNotEmpty(pingResultList)) {
				break;
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * checkDns:在指定的pod中运行nslookup address命令并返回结果. <br/>
	 *
	 * @param pod
	 * @param address
	 * @return String
	 */
	private String checkDns(Pod pod, String address) {
		String containerID = pod.getStatus().getContainerStatuses().get(0).getContainerID().replace("docker://", "");
		String hostIP = pod.getStatus().getHostIP();
		DockerClient dockerClient = dockerClientService.getSpecifiedDockerClientInstance(hostIP);
		ExecCreateCmdResponse exec = dockerClient.execCreateCmd(containerID).withAttachStdout(true).withAttachStderr(true).withCmd("nslookup", address).exec();
		ExecStartStringResultCallback execStartStringResultCallback = new ExecStartStringResultCallback();
		try {
			dockerClient.execStartCmd(exec.getId()).withDetach(false).exec(execStartStringResultCallback).awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "";
		}
		return execStartStringResultCallback.toString();
	}

	/**
	 * parsePingResult:解析nslookup address命令的返回结果，转换为PingResult对象. <br/>
	 *
	 * @param checkResult
	 * @return PingResult
	 */
	private PingResult parsePingResult(String address, String checkResult) {
		PingResult pingResult = new PingResult();
		pingResult.setHost(address);
		pingResult.setPingResult(checkResult);
		pingResult.setCreateDate(new Date());
		pingResult.setCreateBy(1);
		String[] resultList = checkResult.split("\n");
		for (int i = 0; i < resultList.length; i++) {
			if (resultList[i].contains(address) && i+1 < resultList.length) {
				if (resultList[i+1].contains("Address 1: ")) {
					int addressIndex = resultList[i+1].lastIndexOf("Address 1: ");
					pingResult.setIp(resultList[i+1].substring(addressIndex + 11).replace("\n", ""));
				}
				break;
			}
		}
		return pingResult;
	}

	/**
	 * modifyDnsMonitorConfig:修改勾选的地址和时间间隔. <br/>
	 *
	 * @param addressString
	 * @param sleepTime
	 * @return String
	 */
	@RequestMapping(value="/modifyDnsMonitorConfig.do", method=RequestMethod.POST)
	@ResponseBody
	public String modifyDnsMonitorConfig(String addressString, Integer sleepTime) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		map.put("messages", messages);
		if (addressString == null || sleepTime == null) {
			messages.add("参数不能为空。");
			map.put("status", "300");
			return JSON.toJSONString(map);
		}
		List<String> addressList = JSONObject.parseArray(addressString, String.class);
		if (addressList.size() > 5) {
			messages.add("最多只能检查五个地址。");
			map.put("status", "301");
			return JSON.toJSONString(map);
		}
		Iterable<DNSService> all = dnsServiceDao.findAll();
		Iterator<DNSService> iterator = all.iterator();
		while (iterator.hasNext()) {
			DNSService dnsService = iterator.next();
			if (addressList.contains(dnsService.getAddress())) {
				dnsService.setIsMonitor(CommConstant.TYPE_YES_VALUE);
			} else {
				dnsService.setIsMonitor(CommConstant.TYPE_NO_VALUE);
			}
			dnsService.setSleepTime(sleepTime);
			dnsServiceDao.save(dnsService);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * getMonitorLog:获取监控日志. <br/>
	 *
	 * @return String
	 */
	@RequestMapping(value="/getMonitorLog.do", method=RequestMethod.GET)
	@ResponseBody
	public String getMonitorLog() {
		Iterable<PingResult> all = pingResultDao.findAllOrderByCreateDate();
		return JSON.toJSONString(all);
	}

	/**
	 * getAllDnsService:获取所有的监控域名. <br/>
	 *
	 * @return String
	 */
	@RequestMapping(value="/getAllDnsService.do", method=RequestMethod.GET)
	@ResponseBody
	public String getAllDnsService() {
		Iterable<DNSService> all = dnsServiceDao.findAll();
		return JSON.toJSONString(all);
	}
}
