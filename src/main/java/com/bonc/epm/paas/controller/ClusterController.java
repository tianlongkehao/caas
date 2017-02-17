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
import com.bonc.epm.paas.cluster.entity.CatalogResource;
import com.bonc.epm.paas.cluster.entity.ClusterResources;
import com.bonc.epm.paas.cluster.util.InfluxdbSearchService;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.Cluster;
import com.bonc.epm.paas.entity.ClusterUse;
import com.bonc.epm.paas.entity.PodTopo;
import com.bonc.epm.paas.entity.ServiceTopo;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.net.api.NetAPIClientInterface;
import com.bonc.epm.paas.net.model.Nodes;
import com.bonc.epm.paas.net.model.RouteTable;
import com.bonc.epm.paas.net.util.NetClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.RouteTableUtils;
import com.bonc.epm.paas.util.SshConnect;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * ClusterController
 * 集群监控控制器
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

    @Autowired
    InfluxdbSearchService influxdbSearchService;

    @Autowired
    NetClientService netClientService;

    /**
     *
     * Description: <br>
     * 跳转进入cluster.jsp页面
     * @param model  添加返回页面数据
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
     *  跳转进入containers.jsp页面；
     * @param model  添加返回页面数据；
     * @return  String
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
     *  跳转进入集群管理页面 cluster-management.jsp
     * @param model 添加返回页面的数据；
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
     * @param model 添加返回页面的数据
     * @return String
     */
    @RequestMapping(value = { "/topo" }, method = RequestMethod.GET)
	public String clusterTopo(Model model) {
        User currentUser = CurrentUserUtils.getInstance().getUser();
        //判断是否是超级用户
        if (!currentUser.getUser_autority().equals("1")) {
            List<ServiceTopo> serviceTopoList = new ArrayList<>();
            addServiceTopo(currentUser.getNamespace(),serviceTopoList);
            model.addAttribute("serviceTopo",serviceTopoList);
            model.addAttribute("user", "user");
        }
        else {
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
     * @param model 添加返回页面的数据
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
     * @param model 添加返回页面的数据
     * @return String
     */
    @RequestMapping(value = { "/iptables" }, method = RequestMethod.GET)
	public String clusterIptables(Model model) {

        model.addAttribute("menu_flag", "cluster");
        model.addAttribute("li_flag", "iptables");
        return "cluster/cluster-iptables.jsp";
    }

    @RequestMapping(value = { "/topo/data.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String clusterTopoData(String nameSpace){
        // 创建client
        KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
        //以node节点名称为key，node节点中包含的pod信息为value；
        Map<String,Object> jsonData = new HashMap<String,Object>();
        Map<String, List> nodeMap = new HashMap<String, List>();
        List<ServiceTopo> serviceTopoList = new ArrayList<>();

        // 取得所有node
        NodeList nodeList = client.getAllNodes();
        for (Node node : nodeList.getItems()) {
            String minionName = node.getMetadata().getName();
            //String hostIp = node.getStatus().getAddresses().get(0).getAddress();
            List<PodTopo> podTopoList = new ArrayList<PodTopo>();
            nodeMap.put(minionName, podTopoList);
        }
        if (nameSpace != null) {
            //添加pod数据
            addPodTopo(nameSpace,nodeMap);
            //添加service数据
            addServiceTopo(nameSpace,serviceTopoList);
        }
        else {
            User currentUser = CurrentUserUtils.getInstance().getUser();
            //判断当前用户是否是超级管理员权限
            if (currentUser.getUser_autority().equals("1")) {
                // 取得所有NAMESPACE
                NamespaceList namespaceList = client.getAllNamespaces();
                for (Namespace namespace : namespaceList.getItems()) {
                    String name = namespace.getMetadata().getName();
                    //添加pod数据
                    addPodTopo(name,nodeMap);
                    //添加service数据
                    addServiceTopo(name,serviceTopoList);
                }
            } else {
                //添加pod数据
                addPodTopo(currentUser.getNamespace(),nodeMap);
                //添加service数据
                addServiceTopo(currentUser.getNamespace(),serviceTopoList);
            }
        }

        System.out.println(nodeMap.toString());
        System.out.println(serviceTopoList.toString());
        jsonData.put("master", "master/"+masterAddress);
        jsonData.put("nodes", nodeMap);
        jsonData.put("services", serviceTopoList);
        return JSON.toJSONString(jsonData);
    }

    /**
     * Description: <br>
     * 通过服务名称查询当前服务的pod，展示当前的pod拓扑图；
     * @param serviceName 服务名称；
     * @return String
     */
    @RequestMapping(value = { "/topo/findPod.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String findPodByService(String serviceName,String nameSpace) {
        KubernetesAPIClientInterface client = null;
        if (StringUtils.isNotEmpty(nameSpace)) {
            client = kubernetesClientService.getClient(nameSpace);
        } else {
            client = kubernetesClientService.getClient();
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<PodTopo> podTopoList = new ArrayList<>();
        serviceName = serviceName.substring(serviceName.indexOf("/")+1);
        Service service = client.getService(serviceName);
        Map<String,String> labelSelector = service.getSpec().getSelector();
        PodList podList = client.getLabelSelectorPods(labelSelector);
        for (Pod pod : podList.getItems()) {
            PodTopo podTopo = new PodTopo();
            String podName = pod.getMetadata().getName();
            if (podName.length() > 32) {
                podName = podName.substring(0, podName.indexOf("-")+6);
            }
            podTopo.setPodName(podName);
            podTopo.setNodeName(pod.getSpec().getNodeName());
            podTopo.setHostIp(pod.getStatus().getHostIP());
            podTopo.setServiceName(nameSpace +"/"+serviceName);
            podTopoList.add(podTopo);
        }
        map.put("master", "master/"+masterAddress);
        map.put("podTopoList", podTopoList);
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 查询当前namespace中的pod数据，添加到相关联的Node集合中
     * @param namespace 命名空间；
     * @param nodeMap Node
     * @see
     */
    @SuppressWarnings("unchecked")
    public void addPodTopo(String namespace,Map<String, List> nodeMap) {
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
                            podName = podName.substring(0, podName.indexOf("-")+6);
                        }
                        podTopo.setPodName(podName);
                        podTopo.setNodeName(pod.getSpec().getNodeName());
                        podTopo.setHostIp(pod.getStatus().getHostIP());
//                      podTopo.setServiceName(namespace +"/"+ pod.getMetadata().getLabels().get("app"));
                        String serviceName = "";
                        Iterator<Map.Entry<String, String>> it = pod.getMetadata().getLabels().entrySet().iterator();
                        while (it.hasNext() && StringUtils.isBlank(serviceName)) {
                            Map.Entry<String, String> entry = it.next();
                            Service service = clientName.getService(entry.getValue());
                            if (null != service) {
                                serviceName =  service.getMetadata().getName();
                            }
                        }
                        podTopo.setServiceName(serviceName);
                        String key = podTopo.getNodeName();
                        List<PodTopo> podTopoList = nodeMap.get(key);
                        podTopoList.add(podTopo);
                        nodeMap.replace(key, podTopoList);
                    }
                    catch (Exception e) {
                        LOG.debug(e.getMessage());
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.debug(e.getMessage());
        }
    }

    /**
     * Description: <br>
     *  将当前命名空间中的所有服务名称添加到集合中；
     * @param namespace
     * @param serviceTopoList
     * @see
     */
    public void addServiceTopo(String namespace, List<ServiceTopo> serviceTopoList){
        try {
            KubernetesAPIClientInterface clientName = kubernetesClientService.getClient(namespace);
            // 取得所有此NAMESPACE下的service
            ServiceList serviceList = clientName.getAllServices();
            for (Service service : serviceList.getItems()) {
                try {
                    ServiceTopo serviceTopo = new ServiceTopo();
                    List<String> podName = new ArrayList<>();
                    Map<String,String> labelSelector = service.getSpec().getSelector();
                    PodList podList = clientName.getLabelSelectorPods(labelSelector);
                    if (podList != null) {
                        for (Pod pod : podList.getItems()) {
                            podName.add(pod.getMetadata().getName());
                        }
                    }
                    serviceTopo.setPodName(podName);
                    serviceTopo.setNamespace(service.getMetadata().getNamespace());
//                  serviceTopo.setServiceName(namespace +"/"+service.getMetadata().getName());
                    serviceTopo.setServiceName(service.getMetadata().getName());
                    serviceTopoList.add(serviceTopo);
                } catch (Exception e) {
                    LOG.debug(e.getMessage());
                }
            }
        }
        catch (Exception e) {
            LOG.debug(e.getMessage());
        }
    }

    /**
     * Description: <br>
     *  跳转进入cluster-deltail.jsp页面
     * @param hostIps  hostIps
     * @param model 添加返回页面的数据
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
     *  取得单一主机资源使用情况
     * @param timePeriod String
     * @deprecated
     * @return ClusterUse
     */
    private ClusterUse getClustersUse(String timePeriod) {
        try {
        }
        catch (Exception e) {
            LOG.debug(e.getMessage());
        }
        return null;
    }

    /**
     * Description:
     * 取得集群监控数据 包含master节点和node节点
     * 包含的信息内容有cpu、mem、disk、network等
     * @param timePeriod String
     * @return clusterResources JOSNString
     */
    @RequestMapping(value={ "/getClusterMonitor" }, method = RequestMethod.GET)
	@ResponseBody
	public String getClusterMonitor(String timePeriod) {
        ClusterResources clusterResources =  new ClusterResources();
        try {
            InfluxDB influxDB = influxdbSearchService.getInfluxdbClient();
            List<String> xValue = influxdbSearchService.generateXValue(influxDB, timePeriod);
            clusterResources.setxValue(xValue);

            List<CatalogResource> yValue = new ArrayList<CatalogResource>();
            yValue.add(influxdbSearchService.generateYValueOfCluster(influxDB, timePeriod));
            yValue.add(influxdbSearchService.generateYValueOfMinmon(influxDB, timePeriod));
            clusterResources.setyValue(yValue);
        }
        catch (Exception e) {
            LOG.error("get cluster monitor data failed. error message:-" + e.getMessage());
            e.printStackTrace();
        }
        return JSON.toJSONString(clusterResources);
    }

    /**
     *
     * Description:
     * 取得所有的namespace
     * @return rtnValue String
     */
    @RequestMapping(value={"/getAllNamespace"}, method=RequestMethod.GET)
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
     * @param nameSpace String
     * @param podName String
     * @param timePeriod String
     * @return String
     */
    @RequestMapping(value = { "/getContainerMonitor" }, method = RequestMethod.GET)
	@ResponseBody
	public String getContainerMonitor(String nameSpace, String podName, String timePeriod) {
        ClusterResources clusterResources =  new ClusterResources();
		// 当前登陆用户是租户
        User curUser = CurrentUserUtils.getInstance().getUser();
        if (UserConstant.AUTORITY_TENANT.equals(curUser.getUser_autority())) {
            nameSpace = curUser.getNamespace();
        }
        try {
            InfluxDB influxDB = influxdbSearchService.getInfluxdbClient();
            List<String> xValue = influxdbSearchService.generateXValue(influxDB, timePeriod);
            List<CatalogResource> yValue = influxdbSearchService.generateContainerMonitorYValue(influxDB, timePeriod,nameSpace,podName);
            clusterResources.setxValue(xValue);
            clusterResources.setyValue(yValue);
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return JSON.toJSONString(clusterResources);
    }


    /**
     * Description: <br>
     * 跳转进入cluster-create.jsp页面；
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
     * @param searchIP  serarchIP
     * @param model  添加返回页面数据
     * @return  String
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
     * @param ipRange ipRange
     * @param model  添加返回页面的数据
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
            }
            else {
                String[] ipsArray = ipSect.split(",");
                for (String ipSon : ipsArray) {
                    lstIps.add(ipHalf + ipSon);
                }
            }
        }
        else {
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
                }
                catch (NoRouteToHostException e) {
                    LOG.error("无法SSH到目标主机:" + ipSon);
                }
                catch (UnknownHostException e) {
                    LOG.error("未知主机:" + ipSon);
                }
                catch (IOException e) {
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
     * @param user
     * @param pass
     * @param ip
     * @param port
     * @param type
     * @return  String
     * @see
     */
    @RequestMapping(value = { "/installCluster" }, method = RequestMethod.GET)
	@ResponseBody
	public String installCluster(@RequestParam String user, @RequestParam String pass, @RequestParam String ip,
			@RequestParam Integer port, @RequestParam String type) {

        try {
			// 拷贝安装脚本
            copyFile(user, pass, ip, port);
        }
        catch (Exception e) {
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
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return "执行command失败";
        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return "ssh连接失败";
        }
        finally {
			// 关闭SSH连接
            SshConnect.disconnect();
        }
    }

    /**
     *
     * Description: <br>
     * copyFile
     * @param user user
     * @param pass pass
     * @param ip ip
     * @param port port
     * @throws IOException IOException
     * @throws JSchException JSchException
     * @throws InterruptedException  InterruptedException
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
        }
        catch (SftpException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = { "/getRouteTable.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String getRouteTable(String ip){
    	NetAPIClientInterface client = netClientService.getSpecifiedClient(ip);
    	RouteTable checkRoutetable = client.checkRoutetable();
        return JSON.toJSONString(checkRoutetable);
    }
}
