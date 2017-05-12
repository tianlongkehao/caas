package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bonc.epm.paas.cluster.api.ClusterHealthyClient;
import com.bonc.epm.paas.cluster.api.LocalHealthyClient;
import com.bonc.epm.paas.cluster.entity.CatalogResource;
import com.bonc.epm.paas.cluster.entity.ClusterResources;
import com.bonc.epm.paas.cluster.entity.NodeInfo;
import com.bonc.epm.paas.cluster.entity.NodeTestInfo;
import com.bonc.epm.paas.cluster.entity.Response;
import com.bonc.epm.paas.cluster.util.InfluxdbSearchService;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.dao.NodeInfoDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Cluster;
import com.bonc.epm.paas.entity.ClusterUse;
import com.bonc.epm.paas.entity.PodTopo;
import com.bonc.epm.paas.entity.ServiceTopo;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
import com.bonc.epm.paas.kubernetes.model.ObjectMeta;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.model.ServiceSpec;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.net.api.NetAPIClientInterface;
import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.Diff;
import com.bonc.epm.paas.net.model.Iptable;
import com.bonc.epm.paas.net.model.RecoverResult;
import com.bonc.epm.paas.net.model.RouteTable;
import com.bonc.epm.paas.net.model.ServiceProblems;
import com.bonc.epm.paas.net.util.NetClientService;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.SshConnect;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * ClusterController 集群监控控制器
 *
 * @author zhoutao
 * @version 2016年9月5日
 * @since
 */
@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {

	/**
	 * ClusterController日志实例
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ClusterController.class);

	/**
	 * KubernetesClientService 服务接口
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	/**
	 * ClusterDao 数据库操作Dao接口
	 */
	@Autowired
	private ClusterDao clusterDao;

	/**
	 * NodeInfoDao 集群检测NodeInfoDao接口
	 */
	@Autowired
	private NodeInfoDao nodeInfoDao;

	/**
	 * userDao 数据库操作接口
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * 获取配置文件中的 yumConf.io.address 的数据信息；
	 */
	@Value("${yumConf.io.address}")
	private String yumSource;
	/**
	 * master主节点地址信息
	 */
	@Value("${kubernetes.api.address}")
	private String masterAddress;

	// 镜像仓库地址
	@Value("${docker.io.username}")
	private String DOCKER_IO_USERNAME;

	@Autowired
	InfluxdbSearchService influxdbSearchService;

	@Autowired
	NetClientService netClientService;

	/**
	 * DockerClientService接口
	 */
	@Autowired
	private DockerClientService dockerClientService;

	/**
	 *
	 * Description: <br>
	 * 跳转进入cluster.jsp页面
	 *
	 * @param model
	 *            添加返回页面数据
	 * @return String
	 */
	@RequestMapping(value = { "/resource" }, method = RequestMethod.GET)
	public String resourceCluster(Model model) {
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "cluster");
		return "cluster/cluster.jsp";
	}

	/**
	 *
	 * Description: <br>
	 * 跳转进入containers.jsp页面；
	 *
	 * @param model
	 *            添加返回页面数据；
	 * @return String
	 */
	@RequestMapping(value = { "/containers" }, method = RequestMethod.GET)
	public String resourceContainers(Model model) {
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "container");
		return "cluster/containers.jsp";
	}

	/**
	 *
	 * Description: <br>
	 * 跳转进入集群管理页面 cluster-management.jsp
	 *
	 * @param model
	 *            添加返回页面的数据；
	 * @return String
	 */
	@RequestMapping(value = { "/management" }, method = RequestMethod.GET)
	public String clusterList(Model model) {
		List<Cluster> lstClusters = new ArrayList<>();
		for (Cluster cluster : clusterDao.findAll()) {
			lstClusters.add(cluster);
		}
		model.addAttribute("lstClusters", lstClusters);
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "management");
		return "cluster/cluster-management.jsp";
	}

	/**
	 * Description: <br>
	 * 进入cluster-topo.jsp
	 *
	 * @param model
	 *            添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = { "/topo" }, method = RequestMethod.GET)
	public String clusterTopo(Model model) {
		User currentUser = CurrentUserUtils.getInstance().getUser();
		// 判断是否是超级用户
		if (!currentUser.getUser_autority().equals("1")) {
			List<ServiceTopo> serviceTopoList = new ArrayList<>();
			addServiceTopo(currentUser.getNamespace(), serviceTopoList);
			model.addAttribute("serviceTopo", serviceTopoList);
			model.addAttribute("user", "user");
		} else {
			List<User> userList = userDao.checkUser(currentUser.getId());
			model.addAttribute("userList", userList);
			model.addAttribute("user", "root");
		}
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "topo");
		return "cluster/cluster-topo.jsp";
	}

	/**
	 * Description: <br>
	 * 进入cluster-route.jsp
	 *
	 * @param model
	 *            添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = { "/route" }, method = RequestMethod.GET)
	public String clusterRoute(Model model) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		NodeList allNodes = client.getAllNodes();
		List<Object> nodeList = new ArrayList<>();
		if (allNodes != null) {
			for (Node node : allNodes.getItems()) {
				Map<String, String> nodeMap = new HashMap<>();
				nodeList.add(nodeMap);
				nodeMap.put("nodeName", node.getMetadata().getName());
				nodeMap.put("nodeIp", node.getStatus().getAddresses().get(0).getAddress());
				NetAPIClientInterface netAPIClient = netClientService.getSpecifiedClient(nodeMap.get("nodeIp"));
				try {
					RouteTable checkRoutetable = netAPIClient.checkRoutetable();
					nodeMap.put("problem", String.valueOf(checkRoutetable.isProblem()));
				} catch (Exception e) {
					nodeMap.put("problem", "unknown");
				}

			}
		}
		model.addAttribute("nodeList", nodeList);
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "route");
		return "cluster/cluster-route.jsp";
	}

	/**
	 * Description: <br>
	 * 进入cluster-iptables.jsp
	 *
	 * @param model
	 *            添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = { "/iptables" }, method = RequestMethod.GET)
	public String clusterIptables(Model model) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		if (1 == CurrentUserUtils.getInstance().getUser().getId()) {
			List<User> users = userDao.findAllTenant();
			model.addAttribute("users", users);
		} else {
			ServiceList allServices = client.getAllServices();
			List<Service> services = allServices.getItems();
			model.addAttribute("services", services);
		}

		NetAPIClientInterface netClient = netClientService.getClient();
		List<Iptable> checkIptable = null;
		try {
			checkIptable = netClient.checkIptable();
		} catch (NetClientException e) {
			LOG.error(e.getMessage());
		}
		model.addAttribute("checkIptable", checkIptable);
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "iptables");
		return "cluster/cluster-iptables.jsp";
	}

	@RequestMapping(value = { "/topo/data.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String clusterTopoData(String nameSpace) {
		// 创建client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
		// 以node节点名称为key，node节点中包含的pod信息为value；
		Map<String, Object> jsonData = new HashMap<String, Object>();
		Map<String, List<PodTopo>> nodeMap = new HashMap<>();
		List<ServiceTopo> serviceTopoList = new ArrayList<>();

		// 取得所有node
		NodeList nodeList = client.getAllNodes();
		for (Node node : nodeList.getItems()) {
			String minionName = node.getMetadata().getName();
			// String hostIp =
			// node.getStatus().getAddresses().get(0).getAddress();
			List<PodTopo> podTopoList = new ArrayList<PodTopo>();
			nodeMap.put(minionName, podTopoList);
		}
		if (nameSpace != null) {
			// 添加pod数据
			addPodTopo(nameSpace, nodeMap);
			// 添加service数据
			addServiceTopo(nameSpace, serviceTopoList);
		} else {
			User currentUser = CurrentUserUtils.getInstance().getUser();
			// 判断当前用户是否是超级管理员权限
			if (currentUser.getUser_autority().equals("1")) {
				// 取得所有NAMESPACE
				NamespaceList namespaceList = client.getAllNamespaces();
				for (Namespace namespace : namespaceList.getItems()) {
					String name = namespace.getMetadata().getName();
					// 添加pod数据
					addPodTopo(name, nodeMap);
					// 添加service数据
					addServiceTopo(name, serviceTopoList);
				}
			} else {
				// 添加pod数据
				addPodTopo(currentUser.getNamespace(), nodeMap);
				// 添加service数据
				addServiceTopo(currentUser.getNamespace(), serviceTopoList);
			}
		}

		System.out.println(nodeMap.toString());
		System.out.println(serviceTopoList.toString());
		jsonData.put("master", "master/" + masterAddress);
		jsonData.put("nodes", nodeMap);
		jsonData.put("services", serviceTopoList);
		return JSON.toJSONString(jsonData);
	}

	/**
	 * Description: <br>
	 * 通过服务名称查询当前服务的pod，展示当前的pod拓扑图；
	 *
	 * @param serviceName
	 *            服务名称；
	 * @return String
	 */
	@RequestMapping(value = { "/topo/findPod.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String findPodByService(String serviceName, String nameSpace) {
		KubernetesAPIClientInterface client = null;
		if (StringUtils.isNotEmpty(nameSpace)) {
			client = kubernetesClientService.getClient(nameSpace);
		} else {
			client = kubernetesClientService.getClient();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<PodTopo> podTopoList = new ArrayList<>();
		serviceName = serviceName.substring(serviceName.indexOf("/") + 1);
		Service service = client.getService(serviceName);
		Map<String, String> labelSelector = service.getSpec().getSelector();
		PodList podList = client.getLabelSelectorPods(labelSelector);
		for (Pod pod : podList.getItems()) {
			PodTopo podTopo = new PodTopo();
			String podName = pod.getMetadata().getName();
			if (podName.length() > 32) {
				podName = podName.substring(0, podName.indexOf("-") + 6);
			}
			podTopo.setPodName(podName);
			podTopo.setNodeName(pod.getSpec().getNodeName());
			podTopo.setHostIp(pod.getStatus().getHostIP());
			podTopo.setServiceName(nameSpace + "/" + serviceName);
			podTopoList.add(podTopo);
		}
		map.put("master", "master/" + masterAddress);
		map.put("podTopoList", podTopoList);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 查询当前namespace中的pod数据，添加到相关联的Node集合中
	 *
	 * @param namespace
	 *            命名空间；
	 * @param nodeMap
	 *            Node
	 * @see
	 */
	public void addPodTopo(String namespace, Map<String, List<PodTopo>> nodeMap) {
		try {
			KubernetesAPIClientInterface clientName = kubernetesClientService.getClient(namespace);
			// 取得所有此NAMESPACE下的POD
			PodList podList = clientName.getAllPods();
			if (podList != null) {
				for (Pod pod : podList.getItems()) {
					try {
						PodTopo podTopo = new PodTopo();
						podTopo.setNamespace(namespace);
						String podName = pod.getMetadata().getName();
						if (podName.length() > 32) {
							podName = podName.substring(0, podName.indexOf("-") + 6);
						}
						podTopo.setPodName(podName);
						podTopo.setNodeName(pod.getSpec().getNodeName());
						podTopo.setHostIp(pod.getStatus().getHostIP());
						// podTopo.setServiceName(namespace +"/"+
						// pod.getMetadata().getLabels().get("app"));
						String serviceName = "";
						Iterator<Map.Entry<String, String>> it = pod.getMetadata().getLabels().entrySet().iterator();
						while (it.hasNext() && StringUtils.isBlank(serviceName)) {
							Map.Entry<String, String> entry = it.next();
							Service service = clientName.getService(entry.getValue());
							if (null != service) {
								serviceName = service.getMetadata().getName();
							}
						}
						podTopo.setServiceName(serviceName);
						String key = podTopo.getNodeName();
						List<PodTopo> podTopoList = nodeMap.get(key);
						podTopoList.add(podTopo);
						nodeMap.replace(key, podTopoList);
					} catch (Exception e) {
						LOG.debug(e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
	}

	/**
	 * Description: <br>
	 * 将当前命名空间中的所有服务名称添加到集合中；
	 *
	 * @param namespace
	 * @param serviceTopoList
	 * @see
	 */
	public void addServiceTopo(String namespace, List<ServiceTopo> serviceTopoList) {
		try {
			KubernetesAPIClientInterface clientName = kubernetesClientService.getClient(namespace);
			// 取得所有此NAMESPACE下的service
			ServiceList serviceList = clientName.getAllServices();
			for (Service service : serviceList.getItems()) {
				try {
					ServiceTopo serviceTopo = new ServiceTopo();
					List<String> podName = new ArrayList<>();
					Map<String, String> labelSelector = service.getSpec().getSelector();
					PodList podList = clientName.getLabelSelectorPods(labelSelector);
					if (podList != null) {
						for (Pod pod : podList.getItems()) {
							podName.add(pod.getMetadata().getName());
						}
					}
					serviceTopo.setPodName(podName);
					serviceTopo.setNamespace(service.getMetadata().getNamespace());
					// serviceTopo.setServiceName(namespace
					// +"/"+service.getMetadata().getName());
					serviceTopo.setServiceName(service.getMetadata().getName());
					serviceTopoList.add(serviceTopo);
				} catch (Exception e) {
					LOG.debug(e.getMessage());
				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
	}

	/**
	 * Description: <br>
	 * 跳转进入cluster-deltail.jsp页面
	 *
	 * @param hostIps
	 *            hostIps
	 * @param model
	 *            添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = { "/detail" }, method = RequestMethod.GET)
	public String clusterDetail(@RequestParam String hostIps, Model model) {
		List<ClusterUse> lstClustersUse = new ArrayList<>();
		String[] strHostIps = hostIps.split(",");
		for (String hostIp : strHostIps) {
			ClusterUse clusterUse = getClustersUse("");
			clusterUse.setHost(hostIp);
			lstClustersUse.add(clusterUse);
		}
		model.addAttribute("lstClustersUse", lstClustersUse);
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "management");
		return "cluster/cluster-detail.jsp";
	}

	/**
	 * Description: <br>
	 * 取得单一主机资源使用情况
	 *
	 * @param timePeriod
	 *            String
	 * @deprecated
	 * @return ClusterUse
	 */
	private ClusterUse getClustersUse(String timePeriod) {
		try {
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
		return null;
	}

	/**
	 * Description: 取得集群监控数据 包含master节点和node节点 包含的信息内容有cpu、mem、disk、network等
	 *
	 * @param timePeriod
	 *            String
	 * @return clusterResources JOSNString
	 */
	@RequestMapping(value = { "/getClusterMonitor" }, method = RequestMethod.GET)
	@ResponseBody
	public String getClusterMonitor(String timePeriod) {
		ClusterResources clusterResources = new ClusterResources();
		try {
			InfluxDB influxDB = influxdbSearchService.getInfluxdbClient();
			List<String> xValue = influxdbSearchService.generateXValue(influxDB, timePeriod);
			clusterResources.setxValue(xValue);

			List<CatalogResource> yValue = new ArrayList<CatalogResource>();
			yValue.add(influxdbSearchService.generateYValueOfCluster(influxDB, timePeriod));
			yValue.add(influxdbSearchService.generateYValueOfMinmon(influxDB, timePeriod));
			clusterResources.setyValue(yValue);
		} catch (Exception e) {
			LOG.error("get cluster monitor data failed. error message:-" + e.getMessage());
			e.printStackTrace();
		}
		return JSON.toJSONString(clusterResources);
	}

	/**
	 *
	 * Description: 取得所有的namespace
	 *
	 * @return rtnValue String
	 */
	@RequestMapping(value = { "/getAllNamespace" }, method = RequestMethod.GET)
	@ResponseBody
	public String getAllNamespace() {
		// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 NAMESPACE 资源详情
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		// 取得所有NAMESPACE
		NamespaceList namespaceLst = client.getAllNamespaces();
		List<String> namespaceArray = new ArrayList<String>();
		if (namespaceLst != null) {
			for (int i = 0; i < namespaceLst.size(); i++) {
				namespaceArray.add(namespaceLst.get(i).getMetadata().getName());
			}
		} else {
			LOG.error("Get all Namespaces failed");
		}
		return JSON.toJSONString(namespaceArray);
	}

	/**
	 *
	 * Description: <br>
	 * 获取Pod资源的使用情况
	 *
	 * @param nameSpace
	 *            String
	 * @param podName
	 *            String
	 * @param timePeriod
	 *            String
	 * @return String
	 */
	@RequestMapping(value = { "/getContainerMonitor" }, method = RequestMethod.GET)
	@ResponseBody
	public String getContainerMonitor(String nameSpace, String podName, String timePeriod) {
		ClusterResources clusterResources = new ClusterResources();
		// 当前登陆用户是租户
		User curUser = CurrentUserUtils.getInstance().getUser();
		if (UserConstant.AUTORITY_TENANT.equals(curUser.getUser_autority())) {
			nameSpace = curUser.getNamespace();
		}
		try {
			InfluxDB influxDB = influxdbSearchService.getInfluxdbClient();
			List<String> xValue = influxdbSearchService.generateXValue(influxDB, timePeriod);
			List<CatalogResource> yValue = influxdbSearchService.generateContainerMonitorYValue(influxDB, timePeriod,
					nameSpace, podName);
			clusterResources.setxValue(xValue);
			clusterResources.setyValue(yValue);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		return JSON.toJSONString(clusterResources);
	}

	/**
	 * Description: <br>
	 * 跳转进入cluster-create.jsp页面；
	 *
	 * @return String
	 */
	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String clusterAdd(Model model) {
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "management");
		return "cluster/cluster-create.jsp";
	}

	/**
	 *
	 * Description: <br>
	 * 跳转进入cluster-management.jsp页面
	 *
	 * @param searchIP
	 *            serarchIP
	 * @param model
	 *            添加返回页面数据
	 * @return String
	 */
	@RequestMapping(value = { "/searchCluster" }, method = RequestMethod.POST)
	public String searchCluster(@RequestParam String searchIP, Model model) {

		List<Cluster> lstClusters = clusterDao.findByHostLike(searchIP);
		model.addAttribute("lstClusters", lstClusters);
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "management");
		return "cluster/cluster-management.jsp";
	}

	/**
	 *
	 * Description: <br>
	 * 跳转进入cluster-create.jsp
	 *
	 * @param ipRange
	 *            ipRange
	 * @param model
	 *            添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = { "/getClusters" }, method = RequestMethod.POST)
	public String getClusters(@RequestParam String ipRange, Model model) {
		List<String> lstIps = new ArrayList<>();
		List<Cluster> lstClusters = new ArrayList<>();
		List<String> existIps = new ArrayList<>();
		int index = ipRange.indexOf("[");
		if (index != -1) {
			String ipHalf = ipRange.substring(0, index);
			String ipSect = ipRange.substring(index + 1, ipRange.length() - 1);
			if (ipSect.contains("-")) {
				String[] ipsArray = ipSect.split("-");
				int ipStart = Integer.valueOf(ipsArray[0]);
				int ipEnd = Integer.valueOf(ipsArray[1]);
				for (int i = ipStart; i < ipEnd + 1; i++) {
					lstIps.add(ipHalf + i);
				}
			} else {
				String[] ipsArray = ipSect.split(",");
				for (String ipSon : ipsArray) {
					lstIps.add(ipHalf + ipSon);
				}
			}
		} else {
			String[] ipsArray = ipRange.split(",");
			Collections.addAll(lstIps, ipsArray);
		}
		Iterable<Cluster> a = clusterDao.findAll();
		for (Cluster b : a) {
			existIps.add(b.getHost());
		}
		for (String ipSon : lstIps) {
			// 增加数据库中是否存在的验证
			if (!existIps.contains(ipSon)) {
				try {
					Socket socket = new Socket(ipSon, 22);
					if (socket.isConnected()) {
						Cluster conCluster = new Cluster();
						conCluster.setHost(ipSon);
						conCluster.setPort(22);
						lstClusters.add(conCluster);
						socket.close();
					}
				} catch (NoRouteToHostException e) {
					LOG.error("无法SSH到目标主机:" + ipSon);
				} catch (UnknownHostException e) {
					LOG.error("未知主机:" + ipSon);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		model.addAttribute("lstClusters", lstClusters);
		model.addAttribute("ipRange", ipRange);
		return "cluster/cluster-create.jsp";
	}

	/**
	 *
	 * Description: <br>
	 * installCluster
	 *
	 * @param user
	 * @param pass
	 * @param ip
	 * @param port
	 * @param type
	 * @return String
	 * @see
	 */
	@RequestMapping(value = { "/installCluster" }, method = RequestMethod.GET)
	@ResponseBody
	public String installCluster(@RequestParam String user, @RequestParam String pass, @RequestParam String ip,
			@RequestParam Integer port, @RequestParam String type) {

		try {
			// 拷贝安装脚本
			copyFile(user, pass, ip, port);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return "拷贝安装脚本失败";
		}
		// 读取私有仓库地址
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
		String imageHostPort = config.getRegistryUsername();
		// ssh连接
		try {
			SshConnect.connect(user, pass, ip, port);
			// 获取主机的内存大小
			/*
			 * Integer memLimit = 1000000; String memCmd =
			 * "cat /proc/meminfo | grep MemTotal | awk -F ':' '{print $2}' | awk '{print $1}'"
			 * ; String memRtn = SshConnect.exec(memCmd, 1000); String[] b =
			 * memRtn.split("\n"); memLimit = Integer.valueOf(b[b.length -
			 * 2].trim());
			 */
			// 安装环境
			String masterName = "master";
			String hostName = "minion" + ip.split("\\.")[3];
			String cmd = "cd /opt/;chmod +x ./envInstall.sh;nohup ./envInstall.sh " + imageHostPort + " " + yumSource
					+ " " + type + " " + masterName + " " + hostName;
			Boolean endFlg = false;
			SshConnect.exec(cmd, 10000);
			while (!endFlg) {
				String strRtn = SshConnect.exec("echo $?", 1000);
				endFlg = strRtn.endsWith("#");
			}
			// 插入主机数据
			Cluster cluster = clusterDao.findByHost(ip);
			if (cluster == null) {
				Cluster newCluster = new Cluster();
				newCluster.setUsername(user);
				newCluster.setPassword(pass);
				newCluster.setHost(ip);
				newCluster.setPort(port);
				newCluster.setHostType(type);
				clusterDao.save(newCluster);
			}
			String ins_detail = SshConnect.exec("tail -n 5 /opt/nohup.out", 1000);
			return ins_detail.split("nohup.out")[1].trim().split("\\[root")[0].trim();
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return "执行command失败";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return "ssh连接失败";
		} finally {
			// 关闭SSH连接
			SshConnect.disconnect();
		}
	}

	/**
	 *
	 * Description: <br>
	 * copyFile
	 *
	 * @param user
	 *            user
	 * @param pass
	 *            pass
	 * @param ip
	 *            ip
	 * @param port
	 *            port
	 * @throws IOException
	 *             IOException
	 * @throws JSchException
	 *             JSchException
	 * @throws InterruptedException
	 *             InterruptedException
	 */
	private void copyFile(String user, String pass, String ip, Integer port)
			throws IOException, JSchException, InterruptedException {
		JSch jsch = new JSch();
		System.out.print("++++++++++++++++" + user + "+++++" + ip + "+++++" + port + "++" + pass);
		Session session = jsch.getSession(user, ip, port);
		session.setPassword(pass);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		session.connect(30000);
		ChannelSftp sftpConn = (ChannelSftp) session.openChannel("sftp");
		sftpConn.connect(1000);
		try {
			String lpwdPath = sftpConn.lpwd();
			// 创建目录并拷贝文件
			sftpConn.put(lpwdPath + "/src/main/resources/static/bin/envInstall.sh", "/opt/");
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param ip
	 * @return
	 */
	@RequestMapping(value = { "/getRouteTable.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getRouteTable(String ip) {
		Map<String, Object> map = new HashMap<>();
		NetAPIClientInterface client = netClientService.getSpecifiedClient(ip);
		RouteTable checkRoutetable;
		try {
			checkRoutetable = client.checkRoutetable();
			map.put("status", "200");
			map.put("checkRoutetable", checkRoutetable);
		} catch (Exception e) {
			map.put("status", "400");
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * @param ip
	 * @return
	 */
	@RequestMapping(value = { "/getDiff.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getDiff(String namespace, String serviceName) {
		NetAPIClientInterface client = netClientService.getClient();
		com.bonc.epm.paas.net.model.Service service = new com.bonc.epm.paas.net.model.Service();
		service.setNamespace(namespace);
		service.setService(serviceName);
		Diff diff = client.getDiff(service);
		return JSON.toJSONString(diff);
	}

	/**
	 * @param namespace
	 * @return
	 */
	@RequestMapping(value = { "/getServices.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getServicesByNamespace(String namespace) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
		ServiceList allServices = client.getAllServices();
		return JSON.toJSONString(allServices.getItems());
	}

	/**
	 * @param namespace
	 * @return
	 */
	@RequestMapping(value = { "/node" }, method = RequestMethod.GET)
	public String getAllNode(Model model) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		NodeList nodes = client.getAllNodes();
		List<Node> nodeList = nodes.getItems();
		model.addAttribute("nodeList", nodeList);
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "node");
		return "cluster/node.jsp";
	}

	/**
	 * @param 根据查询条件，返回node信息
	 * @return
	 * @see
	 */
	@RequestMapping(value = { "/nodeinfo" }, method = RequestMethod.GET)
	@ResponseBody
	public String getNodeInfo(String clusterstatus, String nodestatus, String ip) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		NodeList nodes = client.getAllNodes();
		List<Node> tempList = nodes.getItems();
		List<Node> nodeList1 = new ArrayList<Node>();

		if (clusterstatus.equals("1")) {
			for (Node node : tempList) {
				// if(node.getSpec().getunschedulable()==false){\
				if (node.getSpec().getUnschedulable() == null) {
					nodeList1.add(node);
				}
			}
		} else if (clusterstatus.equals("2")) {
			for (Node node : tempList) {
				if (node.getSpec().getUnschedulable() != null) {
					nodeList1.add(node);
				}
			}
		} else {
			for (Node node : tempList) {
				nodeList1.add(node);
			}
		}

		tempList.clear();
		tempList = null;
		List<Node> nodeList2 = new ArrayList<Node>();

		if (nodestatus.equals("1")) {
			for (Node node : nodeList1) {
				if (node.getStatus().getConditions().get(1).getStatus().equals("True")) {
					nodeList2.add(node);
				}
			}
		} else if (nodestatus.equals("2")) {
			for (Node node : nodeList1) {
				if (!node.getStatus().getConditions().get(1).getStatus().equals("True")) {
					nodeList2.add(node);
				}
			}
		} else {
			for (Node node : nodeList1) {
				nodeList2.add(node);
			}
		}

		nodeList1.clear();
		nodeList1 = null;
		List<Node> nodeList3 = new ArrayList<Node>();

		if (!ip.equals("")) {
			for (Node node : nodeList2) {
				if (node.getStatus().getAddresses().get(0).getAddress().equals(ip)) {
					nodeList3.add(node);
				}
			}
		} else {
			for (Node node : nodeList2) {
				nodeList3.add(node);
			}
		}

		nodeList2.clear();
		nodeList2 = null;

		map.put("status", "200");
		map.put("nodelist", nodeList3);
		// String result = JSON.toJSONString(map);
		return JSON.toJSONString(map);
	}

	/**
	 * @param namespace
	 * @return
	 */
	@RequestMapping(value = { "/addnode" }, method = RequestMethod.GET)
	@ResponseBody
	public String getAddNode(String nodename) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		Node node = client.getSpecifiedNode(nodename);
		node.getSpec().setUnschedulable(false);
		// node.getSpec().setunschedulable("false");
		client.updateNode(nodename, node);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * @param namespace
	 * @return
	 */
	@RequestMapping(value = { "/deletenode" }, method = RequestMethod.GET)
	@ResponseBody
	public String deleteNode(String nodename) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		Node node = client.getSpecifiedNode(nodename);
		// node.getSpec().setunschedulable("true");
		node.getSpec().setUnschedulable(true);
		client.updateNode(nodename, node);
		deletePodsOfNode(nodename);// 隔离节点之后，将节点上的pod全部删除掉
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * @param namespace
	 * @return
	 */
	@RequestMapping(value = { "/partdeletenode" }, method = RequestMethod.GET)
	@ResponseBody
	public String partDeleteNode(String nodename) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		Node node = client.getSpecifiedNode(nodename);
		// node.getSpec().setunschedulable("true");
		node.getSpec().setUnschedulable(true);
		client.updateNode(nodename, node);
		// deletePodsOfNode(nodename);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/*
	 * 删除指定Node上的所有pod
	 */
	private void deletePodsOfNode(String name) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<Pod> pods = client.getPods().getItems();
		if (!CollectionUtils.isEmpty(pods)) {
			for (Pod pod : pods) {
				if (pod.getSpec().getNodeName().equals(name)) {
					client.deletePodOfNamespace(pod.getMetadata().getNamespace(), pod.getMetadata().getName());
				}
			}
		}
	}

	/**
	 * @param namespace
	 * @return
	 */
	@RequestMapping(value = { "/nodedetail" }, method = RequestMethod.GET)
	@ResponseBody
	public String getNode(String nodename) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		Node node = client.getSpecifiedNode(nodename);
		List<Pod> tempPods = client.getPods().getItems();
		List<Pod> pods = new ArrayList<Pod>();

		if (!CollectionUtils.isEmpty(tempPods)) {
			for (Pod pod : tempPods) {
				if (pod.getSpec().getNodeName().equals(nodename)) {
					pods.add(pod);
				}
			}
		}
		Boolean status = node.getSpec().getUnschedulable();

		map.put("status", status);
		map.put("node", node);
		map.put("pods", pods);
		return JSON.toJSONString(map);
	}

	/**
	 * 集群测试，获得集群中所有的node
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = { "/test" }, method = RequestMethod.GET)
	public String getAllNodeForTest(Model model) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		NodeList nodes = client.getAllNodes();
		List<Node> nodeList = nodes.getItems();

		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();

		List<Pod> pods = client.getPods().getItems();

		// 讲节点状态为Ready的节点返回，用于测试
		for (Node node : nodeList) {

			if (node.getStatus().getConditions().get(1).getStatus().equals("True")) {
				NodeInfo nodeInfo = new NodeInfo();
				nodeInfo.setNodename(node.getMetadata().getName());
				for (Pod pod : pods) {
					if (node.getMetadata().getName().equals(pod.getMetadata().getName())) {
						nodeInfo.setDeploystatus(true);
						break;
					}
				}
				List<NodeTestInfo> nodeTestInfos = nodeInfoDao.findByNodename(node.getMetadata().getName());
				if (!CollectionUtils.isEmpty(nodeTestInfos)) {
					nodeInfo.setTeststatus(true);
					nodeInfo.setNodeTestInfo(nodeTestInfos.get(0));
				}
				nodeInfos.add(nodeInfo);
			}
		}

		List<NodeTestInfo> nodeTestInfos = nodeInfoDao.findByNodename("all");
		if (!CollectionUtils.isEmpty(nodeTestInfos)) {
			model.addAttribute("testparam", nodeTestInfos.get(0));// 执行过批量测试，则返回测试参数信息
		}

		model.addAttribute("nodeList", nodeInfos);// 节点信息list
		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "test");
		return "cluster/test.jsp";
	}

	/**
	 * nodenames：节点名字符串,逗号隔开 例："node1,node2,...."
	 *
	 * @return map： status(200 :成功 , 500 :失败 ) msg(错误消息)
	 */
	@RequestMapping(value = { "/deploypodfortest" }, method = RequestMethod.GET)
	@ResponseBody
	public String generateServiceAndPodForClusterTest(String nodenames) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();

		// 清空集群中所有node上的测试pod,pod的名称与node的名称相同
		/*
		 * NodeList nodes = client.getAllNodes(); List<Node> nodeList =
		 * nodes.getItems();
		 */

		PodList podList = client.getPods();
		List<Pod> pods = podList.getItems();

		/*
		 * for (Node node : nodeList) { for (Pod pod : pods) { if
		 * (pod.getMetadata().getName().equals(node.getMetadata().getName())) {
		 * client.deletePodOfNamespace("kube-system",
		 * node.getMetadata().getName()); System.out.println("pod:" +
		 * node.getMetadata().getName() + "被删除！"); break; } } }
		 */
		LOG.info("**********************开始部署：" + nodenames + "**************************");

		String[] names = nodenames.split(",");
		try {
			// 为localhealthy服务创建pod,在所有的每个node中创建一个pod,pod名称与container名称与node名称相同
			for (int i = 0; i < names.length; i++) {
				try {
					Pod temppod = client.getPodOfNamespace("kube-system", names[i]);
					// 没有出现异常，则已经部署过
					LOG.info("节点：" + names[i] + "已经部署过...");
					continue;
				} catch (KubernetesClientException e) {
					// 如果没有部署过，则新部署此Pod
					Pod pod = new Pod();
					ObjectMeta objectMeta = new ObjectMeta();
					Map<String, String> labels = new HashMap<String, String>();
					labels.put("name", "localhealthy");
					objectMeta.setName(names[i]);
					objectMeta.setLabels(labels);
					objectMeta.setNamespace("kube-system");
					pod.setMetadata(objectMeta);
					PodSpec podSpec = new PodSpec();
					List<Container> containers = new ArrayList<Container>();
					List<ContainerPort> ports = new ArrayList<ContainerPort>();
					ContainerPort port = new ContainerPort();
					port.setContainerPort(8011);
					port.setProtocol("TCP");
					ports.add(port);
					Container container = new Container();
					container.setName(names[i]);
					container.setImage(DOCKER_IO_USERNAME + "/localhealthy");
					container.setPorts(ports);
					containers.add(container);
					podSpec.setContainers(containers);
					podSpec.setNodeName(names[i]);
					pod.setSpec(podSpec);
					client.createPodOfNamespace("kube-system", pod);
					LOG.info("Pod:" + names[i] + "被创建！");
				}
			}

			boolean exist = false;
			for (Pod pod : pods) {
				if (pod.getMetadata().getName().equals("clusterhealthy")) {
					exist = true;
					break;
				}
			}
			// 为clusterhealthy服务创建一个pod
			if (!exist) {
				Pod pod = new Pod();
				ObjectMeta objectMeta = new ObjectMeta();
				Map<String, String> labels = new HashMap<String, String>();
				labels.put("name", "clusterhealthy");
				objectMeta.setName("clusterhealthy");
				objectMeta.setLabels(labels);
				objectMeta.setNamespace("kube-system");
				pod.setMetadata(objectMeta);
				PodSpec podSpec = new PodSpec();
				List<Container> containers = new ArrayList<Container>();
				List<ContainerPort> ports = new ArrayList<ContainerPort>();
				ContainerPort port = new ContainerPort();
				port.setContainerPort(8011);
				port.setProtocol("TCP");
				ports.add(port);
				Container container = new Container();
				container.setName("clusterhealthy");
				container.setImage(DOCKER_IO_USERNAME + "/clusterhealthy");
				container.setPorts(ports);
				containers.add(container);
				podSpec.setContainers(containers);
				pod.setSpec(podSpec);
				client.createPodOfNamespace("kube-system", pod);
				LOG.info("pod:clusterhealthy被创建！");
			}

			boolean localhealthy = false;
			boolean clusterhealthy = false;

			List<Service> services = client.getAllServicesOfNamespace("kube-system").getItems();
			for (Service service : services) {
				if (service.getMetadata().getName().equals("localhealthy")) {
					localhealthy = true;
					continue;
				} else if (service.getMetadata().getName().equals("clusterhealthy")) {
					clusterhealthy = true;
					continue;
				}
			}

			// 创建两个服务localhealthy与clusterhealthy
			if (!localhealthy) {
				Service service1 = new Service();
				ObjectMeta serviceObjectMeta = new ObjectMeta();
				serviceObjectMeta.setName("localhealthy");
				serviceObjectMeta.setNamespace("kube-system");
				service1.setMetadata(serviceObjectMeta);
				ServiceSpec serviceSpec = new ServiceSpec();

				List<ServicePort> servicePorts = new ArrayList<ServicePort>();
				ServicePort servicePort = new ServicePort();
				servicePort.setPort(8011);
				servicePort.setProtocol("TCP");
				servicePort.setTargetPort(8011);
				servicePorts.add(servicePort);
				serviceSpec.setPorts(servicePorts);
				serviceSpec.setType("NodePort");
				Map<String, String> selector1 = new HashMap<String, String>();
				selector1.put("name", "localhealthy");
				serviceSpec.setSelector(selector1);

				service1.setSpec(serviceSpec);
				client.createServiceOfNamespace("kube-system", service1);
				LOG.info("service:localhealthy被创建！");
			}

			if (!clusterhealthy) {
				Service service2 = new Service();
				ObjectMeta serviceObjectMeta2 = new ObjectMeta();
				serviceObjectMeta2.setName("clusterhealthy");
				serviceObjectMeta2.setNamespace("kube-system");
				service2.setMetadata(serviceObjectMeta2);
				ServiceSpec serviceSpec2 = new ServiceSpec();

				List<ServicePort> servicePorts2 = new ArrayList<ServicePort>();
				ServicePort servicePort2 = new ServicePort();
				servicePort2.setPort(8022);
				servicePort2.setProtocol("TCP");
				servicePort2.setTargetPort(8022);
				servicePorts2.add(servicePort2);
				serviceSpec2.setPorts(servicePorts2);
				serviceSpec2.setType("NodePort");
				Map<String, String> selector2 = new HashMap<String, String>();
				selector2.put("name", "clusterhealthy");
				serviceSpec2.setSelector(selector2);

				service2.setSpec(serviceSpec2);
				client.createServiceOfNamespace("kube-system", service2);
				LOG.info("service:clusterhealthy被创建！");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LOG.info("******************************部署失败************************************");
			deletePodsForTest();
			map.put("msg", "部署失败！");
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		long start = System.currentTimeMillis();
		while (true) {
			try {
				Thread.sleep(2000);
				boolean flag = true;
				for (String name : names) {
					Pod pod = client.getPodOfNamespace("kube-system", name);
					if (!pod.getStatus().getPhase().equals("Running")) {
						flag = false;
						break;
					}
				}
				if (flag) {// pod状态都是Running
					break;
				}
				long end = System.currentTimeMillis();
				if ((end - start) > 60000) {// 1分钟即为超时
					LOG.info("******************************部署超时************************************");
					deletePodsForTest();
					map.put("msg", "部署超时！");
					map.put("status", 500);
					return JSON.toJSONString(map);
				}
				System.out.println("正在准备....已经用时：" + (end - start) + "ms");

			} catch (InterruptedException e) {
				LOG.info("******************************部署失败************************************");
				deletePodsForTest();
				// e.printStackTrace();
				map.put("msg", "部署失败！");
				map.put("status", 500);
				return JSON.toJSONString(map);
			}
		}

		map.put("status", 200);
		return JSON.toJSONString(map);
	}

	/**
	 * 清除指定的集群测试Pods nodenames ：节点名字符串,逗号隔开 例："node1,node2,...."
	 *
	 * @return map： status(200 :成功 , 500 :失败 ) msg(错误消息)
	 */
	@RequestMapping(value = { "/clearspecifiedpod" }, method = RequestMethod.GET)
	@ResponseBody
	public String deleteSpecifiedPodsForTest(String nodenames) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		map.put("status", 200);

		String msg = "";
		if (StringUtils.isEmpty(nodenames)) {
			msg = "没有选择节点！";
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		// 清空指定的Pods,并不删除clusterhealthy Pod，以及测试服务
		PodList podList = client.getPods();
		List<Pod> pods = podList.getItems();
		String[] nodenamesarray = nodenames.split(",");

		LOG.info("********************************清理开始***************************************");

		for (String name : nodenamesarray) {
			for (Pod pod : pods) {
				if (pod.getMetadata().getName().equals(name)) {
					try {
						client.deletePodOfNamespace("kube-system", name);
						LOG.info("Pod：" + name + "被删除！");
					} catch (KubernetesClientException e) {
						LOG.info("Pod：" + name + "删除失败！");
						msg = msg + "Pod：" + name + "删除失败！";
						map.put("status", 500);
					}

					// ************************数据库中删除记录************
					nodeInfoDao.deleteByNodename(name);
					break;
				}
			}
		}

		if (!StringUtils.isEmpty(msg)) {
			map.put("msg", msg);
			LOG.info("********************************清理异常***************************************");
		} else {
			LOG.info("********************************清理完成***************************************");
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 清除用于集群测试的所有pod和service 返回码： status:200 - 成功 500 - 失败 msg: 错误消息
	 */
	@RequestMapping(value = { "/deployclear" }, method = RequestMethod.GET)
	@ResponseBody
	public String deletePodsForTest() {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		map.put("status", 200);

		// 清空集群中所有用于测试的Pod和Service
		NodeList nodes = client.getAllNodes();
		List<Node> nodeList = nodes.getItems();

		PodList podList = client.getPods();
		List<Pod> pods = podList.getItems();

		boolean clear = true;// 是否清楚干净标记
		String msg = "";

		for (Node node : nodeList) {
			for (Pod pod : pods) {
				if (pod.getMetadata().getName().equals(node.getMetadata().getName())) {
					try {
						client.deletePodOfNamespace("kube-system", node.getMetadata().getName());
						LOG.info("pod:" + node.getMetadata().getName() + "被删除！");
					} catch (KubernetesClientException e) {
						LOG.info("pod:" + node.getMetadata().getName() + "删除失败！");
						msg = msg + "pod:" + node.getMetadata().getName() + "删除失败！";
						clear = false;
					}
					// ************************数据库中删除检测记录************
					nodeInfoDao.deleteByNodename(node.getMetadata().getName());
					break;
				}
			}
		}

		for (Pod pod : pods) {
			if (pod.getMetadata().getName().equals("clusterhealthy")) {
				try {
					client.deletePodOfNamespace("kube-system", "clusterhealthy");
					LOG.info("pod:clusterhealthy被删除！");
				} catch (KubernetesClientException e) {
					LOG.info("pod:clusterhealthy删除失败！");
					msg = msg + "pod:clusterhealthy删除失败！";
					clear = false;
				}
				break;
			}
		}

		List<Service> services = client.getAllServicesOfNamespace("kube-system").getItems();
		for (Service service : services) {
			if (service.getMetadata().getName().equals("localhealthy")) {
				try {
					client.deleteServiceOfNamespace("kube-system", "localhealthy");
					LOG.info("Service:localhealthy被删除！");
				} catch (KubernetesClientException e) {
					LOG.info("Service:localhealthy删除失败！");
					msg = msg + "Service:localhealthy删除失败！";
					clear = false;
				}
				continue;
			} else if (service.getMetadata().getName().equals("clusterhealthy")) {
				try {
					client.deleteServiceOfNamespace("kube-system", "clusterhealthy");
					LOG.info("Service:clusterhealthy被删除！");
				} catch (Exception e) {
					LOG.info("Service:clusterhealthy删除失败！");
					msg = msg + "Service:clusterhealthy删除失败！";
					clear = false;
				}
				continue;
			}
		}

		nodeInfoDao.deleteByNodename("all");// 删除批量测试的测试参数记录

		if (clear) {
			LOG.info("Service:clusterhealthy删除失败！");
		} else {
			LOG.info("Service:clusterhealthy删除失败！");
			map.put("msg", msg);
			map.put("status", 500);
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 执行测试 nodenames : 节点名字符串,逗号隔开 例："node1,node2,...." items: 检测项目字符串，逗号隔开
	 * 例："item1,item2,....."
	 *
	 * @return map: status:200 - 成功 500 - 失败 msg:错误信息
	 */
	@RequestMapping(value = { "/excutetest" }, method = RequestMethod.GET)
	@ResponseBody
	public String excuteTest(String nodenames, String items, String pingIp, int pingtime, String tracepathIp,
			int tracetime, int curltime, int speed, int latency, int memory) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();

		String msg = "";

		if (StringUtils.isEmpty(nodenames)) {
			msg = "没有选择要检测的节点！";
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}
		if (StringUtils.isEmpty(items)) {
			msg = "没有选择要检测的项目！";
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		LOG.info("***************************检测开始**********************************");

		Service localservice = null;
		try {
			localservice = client.getServiceOfNamespace("kube-system", "localhealthy");
		} catch (KubernetesClientException e) {
			msg = "找不到localhealthy服务！";
			LOG.info(msg);
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		Service clusterservice = null;
		try {
			clusterservice = client.getServiceOfNamespace("kube-system", "clusterhealthy");
		} catch (KubernetesClientException e) {
			msg = "找不到clusterhealthy服务！";
			LOG.info(msg);
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		Pod clusterpod = null;
		try {
			clusterpod = client.getPodOfNamespace("kube-system", "clusterhealthy");
		} catch (KubernetesClientException e) {
			msg = "找不到clusterhealthy Pod！";
			LOG.info(msg);
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		ClusterHealthyClient clusterHealthyClient;
		try {
			clusterHealthyClient = new ClusterHealthyClient(clusterpod.getStatus().getHostIP(),
					clusterservice.getSpec().getPorts().get(0).getNodePort().toString(), new RestFactory());
		} catch (Exception e) {
			msg = "ClusterhealthyClient创建失败!";
			LOG.info(msg);
			map.put("msg", msg);
			map.put("status", 500);
			return JSON.toJSONString(map);
		}

		String[] names = nodenames.split(",");
		List<NodeTestInfo> nodeInfos = new ArrayList<NodeTestInfo>();

		boolean pingitem = false;
		boolean traceitem = false;
		boolean curlitem = false;
		boolean qperfitem = false;
		boolean dockeritem = false;
		boolean dnsitem = false;
		String[] chkitems = items.split(",");
		for (String item : chkitems) {
			if (item.equals("pingitem")) {
				pingitem = true;
				continue;
			}
			if (item.equals("traceitem")) {
				traceitem = true;
				continue;
			}
			if (item.equals("curlitem")) {
				curlitem = true;
				continue;
			}
			if (item.equals("dnsitem")) {
				dnsitem = true;
				continue;
			}
			if (item.equals("qperfitem")) {
				qperfitem = true;
				continue;
			}
			if (item.equals("dockeritem")) {
				dockeritem = true;
				continue;
			}
		}

		for (String name : names) {
			Pod pod = client.getPodOfNamespace("kube-system", name);
			LocalHealthyClient localHealthyClient;
			try {
				localHealthyClient = new LocalHealthyClient(pod.getStatus().getHostIP(),
						localservice.getSpec().getPorts().get(0).getNodePort().toString(), new RestFactory());
				NodeTestInfo nodeInfo = new NodeTestInfo();
				nodeInfo.setNodename(name);
				boolean allpass = true;
				if (pingitem) {
					Response pingresponse = localHealthyClient.ping(pingIp);
					nodeInfo.setPingoutmsg(pingresponse.getOutmsg());
					// nodeInfo.setPing(pingresponse.getOutmsg().contains("Unreachable")
					// ? false : true);
					nodeInfo.setPing(pingitem);
					if (!pingresponse.getOutmsg().contains("Unreachable")) {
						String[] outmsg = pingresponse.getOutmsg().split("/");
						nodeInfo.setPingtime(Double.parseDouble(outmsg[outmsg.length - 3]));
						nodeInfo.setPingpass(nodeInfo.getPingtime() <= pingtime);// ping通过
					}
					allpass = allpass && nodeInfo.isPingpass();
				}

				if (traceitem) {
					Response traceresponse = localHealthyClient.tracepath(tracepathIp);
					nodeInfo.setTracepathoutmsg(traceresponse.getOutmsg());
					String tracemsg = traceresponse.getOutmsg();
					// nodeInfo.setTracepath(tracemsg.contains("hops") ? true :
					// false);
					nodeInfo.setTracepath(traceitem);
					tracemsg = tracemsg.subSequence(tracemsg.indexOf("real"), tracemsg.indexOf("user")).toString()
							.trim();
					tracemsg = tracemsg.split("0m")[1].split("s")[0];
					nodeInfo.setTracetime(Double.parseDouble(tracemsg));
					if (tracemsg.contains("hops")) {
						nodeInfo.setTracepass(nodeInfo.getTracetime() <= tracetime);// trace通过
					}
					allpass = allpass && nodeInfo.isTracepass();
				}

				if (qperfitem) {
					Response qperfresponse = clusterHealthyClient.qperf(pod.getStatus().getPodIP());
					String qperfmsg = qperfresponse.getOutmsg();
					nodeInfo.setQperfoutmsg(qperfmsg);
					// nodeInfo.setQperf(qperfmsg.contains("failed") ? false :
					// true);
					nodeInfo.setQperf(qperfitem);
					if (!qperfmsg.contains("failed")) {
						String unit = "";
						double sp;
						double lcy;
						qperfmsg = qperfmsg.substring(0, qperfmsg.indexOf("/sec")).trim();
						qperfmsg = qperfmsg.split("=")[1].trim();
						unit = qperfmsg.substring(qperfmsg.length() - 3, qperfmsg.length()).trim();
						sp = Double.parseDouble(qperfmsg.substring(0, qperfmsg.length() - 3).trim());
						if (unit.equals("GB")) {// 前台单位为MB
							sp = sp * 1024;
						} else if (unit.equals("KB")) {
							sp = sp / 1024;
						} else if (unit.equals("TB")) {
							sp = sp * 1024;
						}
						nodeInfo.setSpeed(sp);
						qperfmsg = qperfresponse.getOutmsg();
						qperfmsg = qperfmsg.split("latency")[1].split("conf")[0].split("=")[1].trim();
						unit = qperfmsg.substring(qperfmsg.length() - 3, qperfmsg.length()).trim();
						lcy = Double.parseDouble(qperfmsg.substring(0, qperfmsg.length() - 3).trim());
						if (unit.equals("us")) {// 前台单位为ms
							lcy = lcy / 1000;
						} else if (unit.equals("s")) {
							lcy = lcy * 1000;
						}
						nodeInfo.setLatency(lcy);
						nodeInfo.setQperfpass(sp >= speed && lcy <= latency);// qperf通过
					}
					allpass = allpass && nodeInfo.isQperfpass();
				}

				if (curlitem) {
					Response curlresponse = clusterHealthyClient.curl(pod.getStatus().getPodIP() + ":8011");
					String curlmsg = curlresponse.getOutmsg();
					nodeInfo.setCurloutmsg(curlmsg);
					// nodeInfo.setCurl(curlmsg.contains("Failed") ? false :
					// true);
					nodeInfo.setCurl(curlitem);
					curlmsg = curlmsg.split("real")[1].split("user")[0].trim().split("0m")[1].split("s")[0].trim();
					nodeInfo.setCurltime(Double.parseDouble(curlmsg));
					if (!curlmsg.contains("Failed")) {
						nodeInfo.setCurlpass(nodeInfo.getCurltime() <= curltime);// curl通过
					}
					allpass = allpass && nodeInfo.isCurlpass();
				}

				if (dockeritem) {
					DockerClient dockerClient = dockerClientService
							.getSpecifiedDockerClientInstance(pod.getStatus().getHostIP());
					InfoCmd infoCmd = dockerClient.infoCmd();
					Info info = infoCmd.exec();

					long mem = info.getMemTotal() / (1024 * 1024 * 1024);// 转换成GiB
					nodeInfo.setDocker(dockeritem);
					nodeInfo.setCpu(info.getNCPU());
					nodeInfo.setMemory(mem);
					nodeInfo.setDockerpass(mem == memory);// docker通过
					allpass = allpass && nodeInfo.isDockerpass();
				}

				if (dnsitem) {
					Response masterdnsresponse = localHealthyClient.dns("master");
					Response standbydnsresponse = localHealthyClient.dns("standby");

					nodeInfo.setDns(dnsitem);
					String dnString = masterdnsresponse.getOutmsg();
					nodeInfo.setMasterdnsoutmsg(dnString);
					nodeInfo.setMasterdns(dnString.contains("timed out") || dnString.contains("failed")
							|| dnString.contains("resolved") ? false : true);
					dnString = standbydnsresponse.getOutmsg();
					nodeInfo.setStandbydnsoutmsg(dnString);
					nodeInfo.setStandbydns(dnString.contains("timed out") || dnString.contains("failed")
							|| dnString.contains("resolved") ? false : true);
					nodeInfo.setDnspass(nodeInfo.isMasterdns() && nodeInfo.isStandbydns());
					allpass = allpass && nodeInfo.isDnspass();
				}

				nodeInfo.setAllpass(allpass);
				nodeInfos.add(nodeInfo);

				nodeInfoDao.deleteByNodename(name);
				nodeInfoDao.save(nodeInfo);
			} catch (Exception e) {
				LOG.info("节点：" + name + "检测故障！");
				nodeInfoDao.deleteByNodename(name);
				msg = msg + "节点：" + name + "故障!";
				map.put("status", 500);
				// return JSON.toJSONString(map);
				continue;
			}
		}

		// 批量测试，保存测试参数
		if (names.length > 1) {
			NodeTestInfo nodeTestInfo = new NodeTestInfo();
			nodeTestInfo.setNodename("all");

			nodeTestInfo.setPing(pingitem);
			nodeTestInfo.setPingIp(pingIp);
			nodeTestInfo.setPingtimetarget(pingtime);

			nodeTestInfo.setCurl(curlitem);
			nodeTestInfo.setCurltimetarget(curltime);

			nodeTestInfo.setTracepath(traceitem);
			nodeTestInfo.setTraceIp(tracepathIp);
			nodeTestInfo.setTracetimetarget(tracetime);

			nodeTestInfo.setQperf(qperfitem);
			nodeTestInfo.setSpeedtarget(speed);
			nodeTestInfo.setLatencytarget(latency);

			nodeTestInfo.setDocker(dockeritem);
			nodeTestInfo.setMemorytarget(memory);

			nodeTestInfo.setDns(dnsitem);

			nodeInfoDao.deleteByNodename("all");
			nodeInfoDao.save(nodeTestInfo);
		}

		if (!StringUtils.isEmpty(msg)) {
			LOG.info("***************************检测异常**********************************");
		} else {
			LOG.info("***************************检测完成**********************************");
		}

		map.put("nodeInfos", nodeInfos);
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * @param nodename 节点名称
	 * @return 节点测试结果
	 *        status：500 错误
	 *                404 没有测试结果
	 *                200 成功
	 */
	public String getClusterTestResult(String nodename) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		String msg = "";

		if (StringUtils.isEmpty(nodename)) {
			msg="节点名称为空！";
			map.put("status", 500);
			map.put("msg", msg);
			return JSON.toJSONString(map);
		}
        List<NodeTestInfo> nodeTestInfos = nodeInfoDao.findByNodename(nodename);
        if(CollectionUtils.isEmpty(nodeTestInfos)){
        	msg="节点"+nodename+"没有测试结果！";
			map.put("status", 404);
			map.put("msg", msg);
			return JSON.toJSONString(map);
        }

        map.put("nodetestresult", nodeTestInfos.get(0));
		return JSON.toJSONString(map);
	}

	/**
	 * 集群测试，获得集群中所有的node
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = { "/dns" }, method = RequestMethod.GET)
	public String clusterDns(Model model) {

		model.addAttribute("menu_flag", "cluster");
		model.addAttribute("li_flag", "dns");
		return "cluster/cluster-dns.jsp";
	}

	/**
	 * checkIptables:检查iptable. <br/>
	 *
	 * @author longkaixiang
	 * @return String
	 */
	@RequestMapping(value = { "/checkIptables.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkIptables() {
		Map<String, Object> map = new HashMap<>();
		NetAPIClientInterface client = netClientService.getClient();
		List<Iptable> checkIptable = null;
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			checkIptable = client.checkIptable();
			for (Iptable iptable : checkIptable) {
				// 创建用于存放节点所有信息的map
				Map<String, Object> nodeMap = new HashMap<>();
				// 节点名
				nodeMap.put("nodeName", iptable.getNodeName());
				// 节点的所有服务
				nodeMap.put("allServices", iptable.getProblems());
				// 创建用于存放异常服务的map
				Map<String, Object> problemServices = new HashMap<>();
				// 遍历所有的服务
				for (String serviceName : iptable.getProblems().keySet()) {
					ServiceProblems serviceProblems = iptable.getProblems().get(serviceName);
					if (CollectionUtils.isNotEmpty(serviceProblems.getExternalAccess())
							|| CollectionUtils.isNotEmpty(serviceProblems.getInternalAccess())
							|| CollectionUtils.isNotEmpty(serviceProblems.getOthers())) {
						problemServices.put(serviceName, serviceProblems);
					}
				}
				nodeMap.put("problemServices", problemServices);
				// 节点是否有问题 true正常
				if (MapUtils.isEmpty(problemServices)) {
					nodeMap.put("problem", true);
				} else {
					nodeMap.put("problem", false);
				}
				resultList.add(nodeMap);
			}
		} catch (NetClientException e) {
			LOG.error(e.getMessage());
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		map.put("resultList", resultList);
		return JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * recoverRoutetable:恢复Routetable. <br/>
	 *
	 * @author longkaixiang
	 * @param nodeListString
	 * @return String
	 */
	@RequestMapping(value = { "/recoverRoutetable.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String recoverRoutetable(String nodeListString) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		List<com.bonc.epm.paas.net.model.NodeInfo> nodeList;
		try {
			nodeList = JSON.parseArray(nodeListString, com.bonc.epm.paas.net.model.NodeInfo.class);
		} catch (Exception e) {
			map.put("status", "400");
			messages.add("解析错误：[Message:" + e.getMessage() + "nodeListString:" + nodeListString + "]");
			map.put("messages", e.getMessage());
			return JSON.toJSONString(map);
		}
		for (com.bonc.epm.paas.net.model.NodeInfo nodeInfo : nodeList) {
			NetAPIClientInterface client = netClientService.getSpecifiedClient(nodeInfo.getIp());
			try {
				RecoverResult recoverResult = client.recoverRoutetable(nodeInfo);
				if (!recoverResult.isCordon() || !recoverResult.isDrain() || !recoverResult.isRestart()
						|| !recoverResult.isUncordon()) {
					messages.add(nodeInfo.getIp() + "修复异常");
				}
			} catch (NetClientException e) {
				LOG.error(e.getMessage());
				messages.add(nodeInfo.getIp() + "修复异常");
			}
		}
		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "300");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * recoverRoutetable:修复Iptables. <br/>
	 *
	 * @author longkaixiang
	 * @param nodeNameListString
	 * @return String
	 */
	@RequestMapping(value = { "/recoverIptables.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String recoverIptables(String nodeNameListString) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		List<String> nodeNameList;
		try {
			nodeNameList = JSON.parseArray(nodeNameListString, String.class);
		} catch (Exception e) {
			map.put("status", "400");
			messages.add("解析错误：[Message:" + e.getMessage() + "nodeNameListString:" + nodeNameListString + "]");
			map.put("messages", e.getMessage());
			return JSON.toJSONString(map);
		}
		KubernetesAPIClientInterface k8sclient = kubernetesClientService.getClient();

		for (String nodeName : nodeNameList) {
			String nodeIp = "";
			try {
				// 依据nodename获取nodeip
				Node node = k8sclient.getSpecifiedNode(nodeName);
				nodeIp = node.getStatus().getAddresses().get(0).getAddress();
			} catch (Exception e) {
				LOG.error("查找对应ip失败：[nodeName:" + nodeName + "]" + e.getMessage());
				messages.add("查找对应ip失败：[nodeName:" + nodeName + "]" + e.getMessage());
				map.put("status", "400");
				map.put("messages", e.getMessage());
				return JSON.toJSONString(map);
			}
			NetAPIClientInterface client = netClientService.getSpecifiedClient(nodeIp);
			try {
				RecoverResult recoverResult = client.recoverIptables();
				if (!recoverResult.isRestart()) {
					messages.add(nodeName + "修复异常");
				}
			} catch (NetClientException e) {
				LOG.error(e.getMessage());
				messages.add(nodeName + "修复异常");
			}

		}
		if (CollectionUtils.isEmpty(messages)) {
			map.put("status", "200");
		} else {
			map.put("status", "300");
			map.put("messages", messages);
		}
		return JSON.toJSONString(map);
	}

}
