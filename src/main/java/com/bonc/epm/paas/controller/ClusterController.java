package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
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
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.DateFormatUtils;
import com.bonc.epm.paas.util.SshConnect;
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

    private static final int ArrayList = 0;

    private static final int PodTopo = 0;

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
     * 获取配置文件中的监控器的url地址信息
     */
    @Value("${monitor.url}")
	private String url;
    
    /**
     * 获取监控器的username数据信息；
     */
    @Value("${monitor.username}")
	private String username;
    
    /**
     * 获取监控器的密码；
     */
    @Value("${monitor.password}")
	private String password;
    
    /**
     * 获取监控器的dbName;
     */
    @Value("${monitor.dbName}")
	private String dbName;
    
    /**
     * master主节点地址信息
     */
    @Value("${kubernetes.api.address}")
    private String masterAddress;
    /**
     * 获取InfluxDB；
     */
    private InfluxDB influxDB;

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
        return "cluster/cluster-topo.jsp";
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
                    PodTopo podTopo = new PodTopo();
                    podTopo.setNamespace(namespace);
                    String podName = pod.getMetadata().getName();
                    if (podName.length() > 32) {
                        podName = podName.substring(0, podName.indexOf("-")+6);
                    }
                    podTopo.setPodName(podName);
                    podTopo.setNodeName(pod.getSpec().getNodeName());
                    podTopo.setHostIp(pod.getStatus().getHostIP());
//                    podTopo.setServiceName(namespace +"/"+ pod.getMetadata().getLabels().get("app"));
                    podTopo.setServiceName(pod.getMetadata().getLabels().get("app"));
                    String key = podTopo.getNodeName();
                    List<PodTopo> podTopoList = nodeMap.get(key);
                    podTopoList.add(podTopo);
                    nodeMap.replace(key, podTopoList);
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
//                serviceTopo.setServiceName(namespace +"/"+service.getMetadata().getName());
                serviceTopo.setServiceName(service.getMetadata().getName());
                serviceTopoList.add(serviceTopo);
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
     * 
     * Description: <br>
     * 取得集群监控数据
     * @param timePeriod  timePeriod
     * @return String
     */
    @RequestMapping(value = { "/getClusterMonitor" }, method = RequestMethod.GET)
	@ResponseBody
	public String getClusterMonitor(String timePeriod) {
        StringBuilder xValue = new StringBuilder();
        StringBuilder yValue = new StringBuilder();
        try {
            influxDB = InfluxDBFactory.connect(url, username, password);
            xValue.append("\"xValue\": [");
            
			// xValue
            xValue = joinXValue(xValue, timePeriod);
            
			// yValue
            yValue.append("\"yValue\": [");
            
			// cluster
            yValue.append("{\"name\": \"cluster\",\"val\": [");
            
			// memory
            yValue.append("{\"titleText\": \"memory\",\"val\": [");
            
			// OVERALL CLUSTER MEMORY USAGE
            yValue.append("{\"title\": \"OVERALL CLUSTER MEMORY USAGE\",\"val\": [");
            
			// overall cluster memory usage:mem_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getMemLimitOverAll", "");
			
            // overall cluster memory usage:mem_use
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getMemUseOverAll", "");
			
            // overall cluster memory usage:mem_workingSet
            yValue = joinClusterYValue(yValue, "WorkingSetCurrent", timePeriod, "getMemSetOverAll", "");
			
            // 去掉最后一个逗号
            yValue = this.deleteLastStr(yValue);
            
            yValue.append("]},");
			
            // MEMORY USAGE GROUP BY NODE
            yValue.append("{\"title\": \"MEMORY USAGE GROUP BY NODE\",\"val\": [");
			
            // MEM NODE结束,MEM结束
            yValue.append("]}]},");
			
            // CPU
            yValue.append("{\"titleText\": \"CPU\",\"val\": [");
			
            // CPU USAGE GROUP BY NODE
            yValue.append("{\"title\": \"CPU USAGE GROUP BY NODE\",\"val\": [");
            
			// CPU结束
            yValue.append("]}]},");
            
			// DISK
            yValue.append("{\"titleText\": \"DISK\",\"val\": [");
            
			// OVERALL CLUSTER DISK USAGE
            yValue.append("{\"title\": \"OVERALL CLUSTER DISK USAGE\",\"val\": [");
            
			// overall cluster disk usage:disk_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getDiskLimitOverAll", "");
            
			// overall cluster disk usage:disk_use
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getDiskUseOverAll", "");
            
			// 去掉最后一个逗号
            yValue = this.deleteLastStr(yValue);
            
            yValue.append("]},");
			
            // DISK USAGE GROUP BY NODE
            yValue.append("{\"title\": \"DISK USAGE GROUP BY NODE\",\"val\": [");
			
            // DISK结束
            yValue.append("]}]},");
			
            // NETWORK
            yValue.append("{\"titleText\": \"NETWORK\",\"val\": [");
			
            // NETWORK USAGE GROUP BY NODE
            yValue.append("{\"title\": \"NETWORK USAGE GROUP BY NODE\",\"val\": [");
			
            // NET结束
            yValue.append("]}]}]},");
			
            // minion
            yValue.append("{\"name\": \"minmon\",\"val\": [");
			
            // 创建client
            KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			
            // 取得所有node
            NodeList nodeLst = client.getAllNodes();
            
            if (nodeLst != null) {
				
            	// 循环处理minion的监控信息
            	for (int i = 0; i < nodeLst.size(); i++) {
            		
            		Node minionItem = nodeLst.getItems().get(i);
            		// 子节点名称
            		String minionName = minionItem.getMetadata().getName();
            		// 子节点类型
            		String minionType= "";
            		String minionType0 = minionItem.getStatus().getConditions().get(0).getType();
            		String minionType1 = minionItem.getStatus().getConditions().get(1).getType();
            		if ("Ready".equals(minionType0) || "Ready".equals(minionType1)){
            			minionType= "Ready";
            		}
            		// 子节点状态
            		String minionStatus = "";
            		String minionStatus0 = minionItem.getStatus().getConditions().get(0).getStatus();
            		String minionStatus1 = minionItem.getStatus().getConditions().get(1).getStatus();
            		if ("True".equals(minionStatus0) || "True".equals(minionStatus1)){
            			minionStatus= "True";
            		}
            		
            		// 判斷节点非master,type为Ready,status为True
            		if (!"127.0.0.1".equals(minionName) && "Ready".equals(minionType) && "True".equals(minionStatus)) {
            			
            			// memory
            			yValue.append("{\"titleText\": \"").append(minionName).append("\",\"val\": [");
            			
            			// memory
            			yValue.append("{\"title\": \"memory\",\"val\": [");
            			
            			// individual node memory usage： mem_limit
            			yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getMemLimitMinion", minionName);
            			
            			// individual node memory usage:memUse
            			yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getMemUseMinion", minionName);
            			
            			// individual node memory usage:memory_working_set
            			yValue = joinClusterYValue(yValue, "WorkingSetCurrent", timePeriod, "getMemSetMinion", minionName);
            			
            			// 去掉最后一个逗号
            			yValue = this.deleteLastStr(yValue);
            			
            			// memory结束
            			yValue.append("]},");
            			
            			// CPU
            			yValue.append("{\"title\": \"cpu\",\"val\": [");
            			
            			// individual node CPU usage:cpu_limit
            			yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getCpuLimitMinion", minionName);
            			
            			// individual node CPU usage:cpu_use
            			yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getCpuUseMinion", minionName);
            			
            			// 去掉最后一个逗号
            			yValue = this.deleteLastStr(yValue);
            			
            			// CPU结束
            			yValue.append("]},");
            			
            			// disk
            			yValue.append("{\"title\": \"disk\",\"val\": [");
            			
            			// individual node disk usage:disk_limit
            			yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getDiskLimitMinion", minionName);
            			
            			// individual node disk usage:disk_use
            			yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getDiskUseMinion", minionName);
            			
            			// 去掉最后一个逗号
            			yValue = this.deleteLastStr(yValue);
            			
            			// disk结束
            			yValue.append("]},");
            			
            			// network
            			yValue.append("{\"title\": \"network\",\"val\": [");
            			
            			// individual node network usage:tx
            			yValue = joinClusterYValue(yValue, "TxCurrent", timePeriod, "getTxMinion", minionName);
            			
            			// individual node network usage:rx
            			yValue = joinClusterYValue(yValue, "RxCurrent", timePeriod, "getRxMinion", minionName);
            			
            			// 去掉最后一个逗号
            			yValue = this.deleteLastStr(yValue);
            			
            			// network结束
            			yValue.append("]},");
            			
            			// 去掉最后一个逗号
            			yValue = this.deleteLastStr(yValue);
            			
            			// minion节点串结束
            			yValue.append("]},");
            		}
            	}
			} else {
				LOG.error("Get all Nodes failed");
			}
			// 去掉最后一个逗号
            yValue = this.deleteLastStr(yValue);
			// 所有minion结束
            yValue.append("]}");

			// yValue结束
            yValue.append("]");
        } 
        catch (Exception e) {
            LOG.debug(e.getMessage());
            System.out.println(e.getMessage());
        }
		// 拼接总串
        return "{" + xValue.toString() + yValue.toString() + "}";
    }

    /**
     * 
     * Description: <br>
     * 取得所有的namespace
     * @return rtnValue String
     */
    @RequestMapping(value = { "/getAllNamespace" }, method = RequestMethod.GET)
	@ResponseBody
	public String getAllNamespace() {
        StringBuilder rtnValue = new StringBuilder();

		// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 NAMESPACE 资源详情
        KubernetesAPIClientInterface client = kubernetesClientService.getClient();

		// 取得所有NAMESPACE
        NamespaceList namespaceLst = client.getAllNamespaces();
        
        if (namespaceLst != null) {
        	// 循环处理每个NAMESPACE
        	for (int i = 0; i < namespaceLst.size(); i++) {
        		// 命名空间名称
        		String namespaceName = namespaceLst.get(i).getMetadata().getName();
        		rtnValue.append("\"").append(namespaceName).append("\"").append(",");
        	}
		} else {
			LOG.error("Get all Namespaces failed");
		}

		// 去掉最后一个逗号
        rtnValue = this.deleteLastStr(rtnValue);

		// 拼接总串
        return "[" + rtnValue.toString() + "]";
    }
    
    /**
     * 
     * Description: <br>
     * 取得容器资源的使用情况
     * @param nameSpace 
     * @param podName 
     * @param timePeriod  
     * @return String
     */
    @RequestMapping(value = { "/getContainerMonitor" }, method = RequestMethod.GET)
	@ResponseBody
	public String getContainerMonitor(String nameSpace, String podName, String timePeriod) {

        influxDB = InfluxDBFactory.connect(url, username, password);
		// 当前登陆用户
        User curUser = CurrentUserUtils.getInstance().getUser();
		// 当前用户为租户
        if (UserConstant.AUTORITY_TENANT.equals(curUser.getUser_autority())) {
            nameSpace = curUser.getNamespace();
        }

        StringBuilder xValue = new StringBuilder();
        StringBuilder yValue = new StringBuilder();

        try {
            xValue.append("\"xValue\": [");

			// xValue
            xValue = joinXValue(xValue, timePeriod);

			// yValue
            yValue.append("\"yValue\": [");

			// 判断是否单一NAMESPACE
            if ("".equals(nameSpace) || nameSpace == null) {

				// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 NAMESPACE 资源详情
                KubernetesAPIClientInterface client = kubernetesClientService.getClient();

				// 取得所有NAMESPACE
                NamespaceList namespaceLst = client.getAllNamespaces();
                
                if (namespaceLst != null) {
                	System.out.print("namespaceLst.size()=" + namespaceLst.size());
                	// 循环处理每个NAMESPACE
                	for (int i = 0; i < namespaceLst.size(); i++) {
                		// 命名空间名称
                		String namespaceName = namespaceLst.get(i).getMetadata().getName();
                		
                		// 取得单一NAMESPACE的JSON数据
                		yValue = createNamespaceJson(yValue, timePeriod, namespaceName, "");
                	}
				} else {
					LOG.error("Get all Namespaces failed");
				}
            }
            else if ("".equals(podName) || podName == null) {
				// 取得单一NAMESPACE的JSON数据
                yValue = createNamespaceJson(yValue, timePeriod, nameSpace, "");
            }
            else {
				// 取得单一POD的JSON数据
                yValue = createNamespaceJson(yValue, timePeriod, nameSpace, podName);
            }
			// 去掉最后一个逗号
            yValue = this.deleteLastStr(yValue);

			// yValue结束
            yValue.append("]");
        }
        catch (Exception e) {
            LOG.debug(e.getMessage());
            System.out.println(e.getMessage());
        }
		// 拼接总串
        return "{" + xValue.toString() + yValue.toString() + "}";
    }
    
    /**
     * 
     * Description: <br>
     * 拼接单一NAMESPACE串
     * @param yValue  yValue
     * @param timePeriod timePeriod
     * @param nameSpace namespace
     * @param podName podName
     * @return String
     */
    private StringBuilder createNamespaceJson(StringBuilder yValue, String timePeriod, String nameSpace,
			String podName) {
		// 判断podName是否为空
        if ("".equals(podName)) {
			// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 NAMESPACE 资源详情
            KubernetesAPIClientInterface clientNamespace = kubernetesClientService.getClient(nameSpace);

			// 取得所有此NAMESPACE下的POD
            PodList podLst = clientNamespace.getAllPods();

            System.out.print("podLst=" + podLst);
            System.out.print("podLst.size=" + String.valueOf(podLst.size()));
            if (podLst != null && podLst.getItems() != null && podLst.size() != 0) {
				// 循环所有POD
                for (int i = 0; i < podLst.size(); i++) {
					// POD
                    Pod indexPod = podLst.get(i);
					// 实例名称
                    String indexPodName = indexPod.getMetadata().getName();
					// 拼接POD的JSON数据
                    yValue = createPodJson(yValue, timePeriod, nameSpace, indexPodName);
                }
            }
        } 
        else {
			// 拼接POD的JSON串
            yValue = createPodJson(yValue, timePeriod, nameSpace, podName);
        }
        return yValue;
    }

    /**
     * 
     * Description: <br>
     * 拼接POD的JSON串
     * @param yValue 
     * @param timePeriod 
     * @param namespace 
     * @param podName 
     * @return String
     */
    private StringBuilder createPodJson(StringBuilder yValue, String timePeriod, String namespace, String podName) {

        System.out.print("podName=" + podName);
		// POD开始
        yValue.append("{\"name\": \"" + podName + "\",\"val\": [");

		// 取得CONTAINER_NAME
        MonitorController monCon = new MonitorController();
        influxDB = InfluxDBFactory.connect(url, username, password);
        List<String> containerNameLst = monCon.getAllContainerName(influxDB, dbName, namespace, podName);

        System.out.print("containerNameLst=" + containerNameLst.toString());
		// 循环处理所有container
        for (String containerName : containerNameLst) {

			// CONTAINER开始
            yValue.append("{\"titleText\": \"" + containerName + "\",\"val\": [");

			// memory开始
            yValue.append("{\"title\": \"memory\",\"val\": [");

			// MEM LIMIT
            yValue = joinContainerYValue(yValue, "memoryLimitCurrent", timePeriod, "getMemLimit", namespace, podName,
					containerName);

			// MEM USE
            yValue = joinContainerYValue(yValue, "memoryUsageCurrent", timePeriod, "getMemUse", namespace, podName,
					containerName);

			// MEM WORKING SET
            yValue = joinContainerYValue(yValue, "memoryWorkingSetCurrent", timePeriod, "getMemSet", namespace, podName,
					containerName);

			// 去掉最后一个逗号
            yValue = this.deleteLastStr(yValue);

			// MEM结束
            yValue.append("]},");

			// CPU开始
            yValue.append("{\"title\": \"cpu\",\"val\": [");

			// CPU LIMIT
            yValue = joinContainerYValue(yValue, "cpuLimitCurrent", timePeriod, "getCpuLimit", namespace, podName,
					containerName);

			// CPU USE
            yValue = joinContainerYValue(yValue, "cpuUsageCurrent", timePeriod, "getCpuUse", namespace, podName,
					containerName);

			// 去掉最后一个逗号
            yValue = this.deleteLastStr(yValue);

			// CPU结束
            yValue.append("]}");

			// CONTAINER结束
            yValue.append("]},");
        }

		// 去掉最后一个逗号
        yValue = this.deleteLastStr(yValue);
		// POD结束
        yValue.append("]},");

        System.out.print("yValue=" + yValue.toString());
        return yValue;
    }
    
    /**
     * Description: <br>
     * 跳转进入cluster-create.jsp页面；
     * @return String
     */
    @RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String clusterAdd() {
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
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().build();
        String imageHostPort = config.getUsername();
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

    /**
     * 
     * Description: <br>
     * 从数据库中读取时间数据，并进行拼接
     * @param val 需要拼接的数据
     * @param timePeriod 间隔时间
     * @return String
     */
    private StringBuilder joinXValue(StringBuilder val, String timePeriod) {
        MonitorController monCon = new MonitorController();
        List<String> lst = monCon.getXValue(influxDB, dbName, timePeriod);
        System.out.println("XValue=" + lst.toString());
        for (int i = 0; i < lst.size(); i++) {
            String strDate = lst.get(i);
			// String转为Date
            Date dateDate = DateFormatUtils.formatStringToDate(strDate);
			// 加8小时
            Date comStrDate = DateFormatUtils.dateCompute(dateDate, "hour", 8);
			// Date转为String
            String comDateDate = DateFormatUtils.formatDateToString(comStrDate, DateFormatUtils.YYYY_MM_DD_HH_MM_SS);
			// 拼接字符串
            val.append("\"").append(comDateDate).append("\",");
        }
		// 去掉最后一个逗号
        val = this.deleteLastStr(val);
        val.append("],");
        return val;
    }

    /**
     * 
     * Description: <br>
     * 从数据库中读取监控数据，并进行拼接
     * @param val 需要拼接的数据
     * @param legendName legendName
     * @param timePeriod 时间周期
     * @param dataType 日期
     * @param minionName minionName
     * @return String
     */
    private StringBuilder joinClusterYValue(StringBuilder val, String legendName, String timePeriod, String dataType,
			String minionName) {
        MonitorController monCon = new MonitorController();
        val.append("{\"legendName\": \"");
        val.append(legendName);
        val.append("\",\"yAxis\": [");
        List<String> lst = monCon.getClusterData(influxDB, dbName, timePeriod, dataType, minionName);
        for (int i = 0; i < lst.size(); i++) {
            val.append("\"").append(lst.get(i)).append("\",");
        }
		// 去掉最后一个逗号
        val = this.deleteLastStr(val);
        val.append("]},");
        return val;
    }

    /**
     * 
     * Description: <br>
     * 从数据库中读取监控数据，并进行拼接
     * @param val 需要拼接的数据
     * @param legendName legendName
     * @param timePeriod 时间周期
     * @param dataType dataType
     * @param namespace namespace
     * @param podName podName
     * @param containerName containerName
     * @return String
     */
    private StringBuilder joinContainerYValue(StringBuilder val, String legendName, 
                                                      String timePeriod, String dataType,
                                                      String namespace, String podName, String containerName) {
        MonitorController monCon = new MonitorController();
        val.append("{\"legendName\": \"");
        val.append(legendName);
        val.append("\",\"yAxis\": [");
        List<String> lst = monCon.getContainerData(influxDB, dbName, timePeriod, dataType, namespace, podName,
				containerName);
        for (int i = 0; i < lst.size(); i++) {
            val.append("\"").append(lst.get(i)).append("\",");
        }
		// 去掉最后一个逗号
        val = this.deleteLastStr(val);
        val.append("]},");
        return val;
    }

    /**
     * Description: <br>
     * 判断字符最后一位是否为逗号，如果是删除
     * @param str  需要判断的字符串
     * @return String
     * @see
     */
    private StringBuilder deleteLastStr(StringBuilder str) {
		// 判断最后一位是不是逗号
        if (",".equals(str.substring(str.length() - 1))) {
			// 去掉最后一个NAMESPACE的逗号
            str.deleteCharAt(str.length() - 1);
        }
        return str;
    }
}
