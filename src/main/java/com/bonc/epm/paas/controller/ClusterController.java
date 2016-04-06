package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Series;
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
import com.bonc.epm.paas.entity.ContainerUse;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.AbstractKubernetesModelList;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.entity.Cluster;
import com.bonc.epm.paas.entity.ClusterUse;
import com.bonc.epm.paas.util.SshConnect;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {
    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    private KubernetesClientService kubernetesClientService;
    
    @Autowired
    private ClusterDao clusterDao;

    @Value("${yumConf.io.address}")
    private String yumSource;

    @Value("${monitor.url}")
    private String url;
    @Value("${monitor.username}")
    private String username;
    @Value("${monitor.password}")
    private String password;
    @Value("${monitor.dbName}")
    private String dbName;
    
    private InfluxDB influxDB;
 
    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    public String resourceCluster(Model model) {
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
    }

    @RequestMapping(value = {"/containers"}, method = RequestMethod.GET)
    public String resourceContainers(Model model) {
        model.addAttribute("menu_flag", "containers");
        return "cluster/containers.jsp";
    }

    @RequestMapping(value = {"/management"}, method = RequestMethod.GET)
    public String clusterList(Model model) {

        List<Cluster> lstClusters = new ArrayList<>();
        for (Cluster cluster : clusterDao.findAll()) {
            lstClusters.add(cluster);
        }
        model.addAttribute("lstClusters", lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-management.jsp";
    }

    @RequestMapping(value = {"/detail"}, method = RequestMethod.GET)
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
     * 取得单一主机资源使用情况
     *
     * @param timePeriod timePeriod
     * @return ClusterUse
     */
    private ClusterUse getClustersUse(String timePeriod) {
        try {
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }
    
    /**
     * 取得集群监控数据
     * 
     * @param timePeriod
     * @return
     */
    @RequestMapping(value = {"/getClusterMonitor"}, method = RequestMethod.GET)
    @ResponseBody
    public String getClusterMonitor(String timePeriod) {
        StringBuilder xValue = new StringBuilder();
        StringBuilder yValue = new StringBuilder();
        try {
            influxDB = InfluxDBFactory.connect(url, username, password);
            
            xValue.append("\"xValue\": [");
            
            //xValue
            xValue = joinXValue(xValue, timePeriod);
            
            //yValue
            yValue.append("\"yValue\": [");

            //cluster
            yValue.append("{\"name\": \"cluster\",\"val\": [");
            
            //memory
            yValue.append("{\"titleText\": \"memory\",\"val\": [");
            
            //OVERALL CLUSTER MEMORY USAGE
            yValue.append("{\"title\": \"OVERALL CLUSTER MEMORY USAGE\",\"val\": [");
            
            //overall cluster memory usage:mem_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getMemLimitOverAll");
            
            //overall cluster memory usage:mem_use
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getMemUseOverAll");

            //overall cluster memory usage:mem_workingSet
            yValue = joinClusterYValue(yValue, "WorkingSetCurrent", timePeriod, "getMemSetOverAll");

            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);
            yValue.append("]},");

            //MEMORY USAGE GROUP BY NODE
            yValue.append("{\"title\": \"MEMORY USAGE GROUP BY NODE\",\"val\": [");
            
            /*//memory usage group by node:memory_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getMemLimitNode");
            
            //memory usage group by node:memory_use(workding_set表取)
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getMemUseNode");
            
            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);*/
            
            //MEM NODE结束,MEM结束
            yValue.append("]}]},");
            
            //CPU
            yValue.append("{\"titleText\": \"CPU\",\"val\": [");

            //CPU USAGE GROUP BY NODE
            yValue.append("{\"title\": \"CPU USAGE GROUP BY NODE\",\"val\": [");
            
            /*//CPU use group by node:cpu_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getCpuLimitNode");

            //CPU use group by node:cpu_use
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getCpuUseNode");

            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);*/
            
            //CPU结束
            yValue.append("]}]},");
            
            //DISK
            yValue.append("{\"titleText\": \"DISK\",\"val\": [");

            //OVERALL CLUSTER DISK USAGE
            yValue.append("{\"title\": \"OVERALL CLUSTER DISK USAGE\",\"val\": [");
            
            //overall cluster disk usage:disk_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getDiskLimitOverAll");
            
            //overall cluster disk usage:disk_use
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getDiskUseOverAll");

            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);
            yValue.append("]},");

            //DISK USAGE GROUP BY NODE
            yValue.append("{\"title\": \"DISK USAGE GROUP BY NODE\",\"val\": [");
            
            /*//disk usage group by node:disk_limit
            yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getDiskLimitNode");

            //disk usage group by node:disk_use
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getDiskUseNode");

            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);*/
            
            //DISK结束
            yValue.append("]}]},");
            
            //NETWORK
            yValue.append("{\"titleText\": \"NETWORK\",\"val\": [");

            //NETWORK USAGE GROUP BY NODE
            yValue.append("{\"title\": \"NETWORK USAGE GROUP BY NODE\",\"val\": [");
            
            /*//network usage group by node:tx
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getTxNode");

            //network usage group by node:rx
            yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getRxNode");

            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);*/
            
            //NET结束
            yValue.append("]}]}]},");
            
            //minion
            yValue.append("{\"name\": \"minmon\",\"val\": [");
            
            //从表中取出所有slave节点
            List<Cluster> clusterLst = (List<Cluster>) clusterDao.getByHostType("slave");
            
            //循环处理minion的监控信息
            for ( int i = 0 ; i < clusterLst.size() ; i++ ) {
                //memory
                yValue.append("{\"titleText\": \"").append("minion" + clusterLst.get(i).getHost().split("\\.")[3]).append("\",\"val\": [");
                
                //memory
                yValue.append("{\"title\": \"memory\",\"val\": [");
                
                //individual node memory usage： mem_limit
                yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getMemLimitMinion");

                //individual node memory usage:memUse
                yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getMemUseMinion");
                
                //individual node memory usage:memory_working_set
                yValue = joinClusterYValue(yValue, "WorkingSetCurrent", timePeriod, "getMemSetMinion");

                //去掉最后一个逗号
                yValue.deleteCharAt(yValue.length() - 1);
                //memory结束
                yValue.append("]},");

                //CPU
                yValue.append("{\"title\": \"cpu\",\"val\": [");
                
                //individual node CPU usage:cpu_limit
                yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getCpuLimitMinion");

                //individual node CPU usage:cpu_use
                yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getCpuUseMinion");
                
                //去掉最后一个逗号
                yValue.deleteCharAt(yValue.length() - 1);
                //CPU结束
                yValue.append("]},");

                //disk
                yValue.append("{\"title\": \"disk\",\"val\": [");

                //individual node disk usage:disk_limit
                yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getDiskLimitMinion");
                
                //individual node disk usage:disk_use
                yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getDiskUseMinion");

                //去掉最后一个逗号
                yValue.deleteCharAt(yValue.length() - 1);
                //disk结束
                yValue.append("]},");

                //network
                yValue.append("{\"title\": \"network\",\"val\": [");
                
                //individual node network usage:tx
                yValue = joinClusterYValue(yValue, "LimitCurrent", timePeriod, "getTxMinion");

                //individual node network usage:rx
                yValue = joinClusterYValue(yValue, "UsageCurrent", timePeriod, "getRxMinion");
                
                //去掉最后一个逗号
                yValue.deleteCharAt(yValue.length() - 1);
                //network结束
                yValue.append("]},");
                
                //去掉最后一个逗号
                yValue.deleteCharAt(yValue.length() - 1);
                
                //minion节点串结束
                yValue.append("]},");
            }
            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);
            //所有minion结束
            yValue.append("]}");
            
            //yValue结束
            yValue.append("]");
        } catch (Exception e) {
            log.debug(e.getMessage());
            System.out.println(e.getMessage());
        }
        //拼接总串
        return "{" + xValue.toString() + yValue.toString() + "}";
    }


    /**
     * 取得容器资源使用情况;
     *
     * @param nameSpace  nameSpace
     * @param podName podName
     * @return String
     */
    @RequestMapping(value = {"/getContainerMonitor"}, method = RequestMethod.GET)
    @ResponseBody
    public String getContainerMonitor(String nameSpace, String podName, String timePeriod) {

        StringBuilder xValue = new StringBuilder();
        StringBuilder yValue = new StringBuilder();
        
        try {
            influxDB = InfluxDBFactory.connect(url, username, password);

            xValue.append("\"xValue\": [");
            
            //xValue
            xValue = joinXValue(xValue, timePeriod);
            
            //yValue
            yValue.append("\"yValue\": [");
            
            //判断是否单一NAMESPACE
            if ("".equals(nameSpace) || nameSpace == null) {
                //以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 NAMESPACE 资源详情
                KubernetesAPIClientInterface client = kubernetesClientService.getClient();
                
                //取得所有NAMESPACE
                NamespaceList namespaceLst = client.getAllNamespaces();

                //循环处理每个NAMESPACE
                for ( int i=0 ; i < namespaceLst.size() ; i++ ) {
                	//服务名称
                	String namespaceName = namespaceLst.get(i).getMetadata().getName();

                	//取得单一NAMESPACE的JSON数据
                	yValue = createNamespaceJson(yValue, timePeriod, namespaceName, "");
                }
                //去掉最后一个NAMESPACE的逗号
                yValue.deleteCharAt(yValue.length() - 1);
            } else if ("".equals(podName) || podName == null){
            	//取得单一NAMESPACE的JSON数据
            	yValue = createNamespaceJson(yValue, timePeriod, nameSpace, "");
            } else {
            	//取得单一POD的JSON数据
            	yValue = createNamespaceJson(yValue, timePeriod, nameSpace, podName);
            }
            
            //yValue结束
            yValue.append("]");
        } catch (Exception e) {
            log.debug(e.getMessage());
            System.out.println(e.getMessage());
        }
        //拼接总串
        return "{" + xValue.toString() + yValue.toString() + "}";
    }
    
    /**
     * 拼接单一NAMESPACE串
     * 
     * @param yValue yValue
     * @param timePeriod timePeriod
     * @param namespace nameSpace
     * @param podName podName
     * @return StringBuilder
     */
    private StringBuilder createNamespaceJson(StringBuilder yValue, String timePeriod, 
    		String nameSpace, String podName){

    	//判断podName是否为空
    	if ("".equals(podName)){
            //以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 NAMESPACE 资源详情
            KubernetesAPIClientInterface clientNamespace = kubernetesClientService.getClient(nameSpace);
            
            //取得所有此NAMESPACE下的POD
            PodList podLst = clientNamespace.getAllPods();

            //循环所有POD
            for (int i = 0 ; i < podLst.size() ; i++) {
            	//POD
            	Pod indexPod = podLst.get(i);
            	//实例名称
            	String indexPodName = indexPod.getMetadata().getName();
            	//拼接POD的JSON数据
            	yValue = createPodJson(yValue, timePeriod, nameSpace, indexPodName);
            }
    	} else {
        	//拼接POD的JSON串
        	yValue = createPodJson(yValue, timePeriod, nameSpace, podName);
    	}
        return yValue;
    }
    /**
     * 拼接POD的JSON串
     * 
     * @param yValue
     * @param timePeriod
     * @param namespace
     * @param svcName
     * @param podName
     * @return
     */
    private StringBuilder createPodJson(StringBuilder yValue, String timePeriod, String namespace, String podName){

    	//POD开始
        yValue.append("{\"name\": \"" + podName + "\",\"val\": [");
        
    	//取得CONTAINER_NAME
    	List<String> containerNameLst = getAllContainerName(namespace, podName);
    	
    	//循环处理所有container
    	for (String containerName : containerNameLst) {

        	//CONTAINER开始
            yValue.append("{\"titleText\": \"" + containerName + "\",\"val\": [");
            
            //memory开始
            yValue.append("{\"title\": \"memory\",\"val\": [");

            //MEM LIMIT
            yValue = joinContainerYValue(yValue, "memoryLimitCurrent", timePeriod, "getMemLimit", namespace, podName, containerName);
            
            //MEM USE
            yValue = joinContainerYValue(yValue, "memoryUsageCurrent", timePeriod, "getMemUse", namespace, podName, containerName);

            //MEM WORKING SET
            yValue = joinContainerYValue(yValue, "memoryWorkingSetCurrent", timePeriod, "getMemSet", namespace, podName, containerName);

            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);
            
            //MEM结束
            yValue.append("]},");

            //CPU开始
            yValue.append("{\"title\": \"cpu\",\"val\": [");
            
            //CPU LIMIT
            yValue = joinContainerYValue(yValue, "cpuLimitCurrent", timePeriod, "getCpuLimit", namespace, podName, containerName);
            
            //CPU USE
            yValue = joinContainerYValue(yValue, "cpuUsageCurrent", timePeriod, "getCpuUse", namespace, podName, containerName);
            
            //去掉最后一个逗号
            yValue.deleteCharAt(yValue.length() - 1);
            
            //CPU结束
            yValue.append("]}");
            
            //CONTAINER结束
            yValue.append("]},");
    	}
    	
        //去掉最后一个逗号
        yValue.deleteCharAt(yValue.length() - 1);
        //POD结束
        yValue.append("]},");
        
        return yValue;
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String clusterAdd() {
        return "cluster/cluster-create.jsp";
    }

    @RequestMapping(value = {"/searchCluster"}, method = RequestMethod.POST)
    public String searchCluster(@RequestParam String searchIP, Model model) {

        List<Cluster> lstClusters = clusterDao.findByHostLike(searchIP);
        model.addAttribute("lstClusters", lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-management.jsp";
    }

    @RequestMapping(value = {"/getClusters"}, method = RequestMethod.POST)
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
            //增加数据库中是否存在的验证
            if (!existIps.contains(ipSon)) {
                try {
                    Socket socket = new Socket(ipSon, 22);
                    if (socket.isConnected()) {
                        Cluster conCluster = new Cluster();
                        conCluster.setHost(ipSon);
                        conCluster.setPort(22);
                        lstClusters.add(conCluster);
                    }
                } catch (NoRouteToHostException e) {
                    log.error("无法SSH到目标主机:" + ipSon);
                } catch (UnknownHostException e) {
                    log.error("未知主机:" + ipSon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("lstClusters", lstClusters);
        model.addAttribute("ipRange", ipRange);
        return "cluster/cluster-create.jsp";
    }


    @RequestMapping(value = {"/installCluster"}, method = RequestMethod.GET)
    @ResponseBody
    public String installCluster(@RequestParam String user, @RequestParam String pass, @RequestParam String ip,
                                 @RequestParam Integer port, @RequestParam String type) {

        try {
            //拷贝安装脚本
            copyFile(user, pass, ip, port);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return "拷贝安装脚本失败";
        }
        //读取私有仓库地址
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().build();
        String imageHostPort = config.getUsername();
        //ssh连接
        try {
            SshConnect.connect(user, pass, ip, port);
            //获取主机的内存大小
            /*Integer memLimit = 1000000;
            String memCmd = "cat /proc/meminfo | grep MemTotal | awk -F ':' '{print $2}' | awk '{print $1}'";
            String memRtn = SshConnect.exec(memCmd, 1000);
            String[] b = memRtn.split("\n");
            memLimit = Integer.valueOf(b[b.length - 2].trim());*/
            //安装环境
            String masterName = "centos-master";
            String hostName = "centos-minion" + ip.split("\\.")[3];
            String cmd = "cd /opt/;chmod +x ./envInstall.sh;nohup ./envInstall.sh " + imageHostPort + " " + yumSource + " " + type + " " + masterName + " " + hostName;
            Boolean endFlg = false;
            SshConnect.exec(cmd, 10000);
            while (!endFlg) {
                String strRtn = SshConnect.exec("echo $?", 1000);
                endFlg = strRtn.endsWith("#");
            }
            //插入主机数据
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
            log.error(e.getMessage());
            return "执行command失败";
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return "ssh连接失败";
        } finally {
            //关闭SSH连接
            SshConnect.disconnect();
        }
    }

    private void copyFile(String user, String pass, String ip, Integer port)
            throws IOException, JSchException, InterruptedException {
        JSch jsch = new JSch();
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
            //创建目录并拷贝文件
            sftpConn.put(lpwdPath + "/src/main/resources/static/bin/envInstall.sh", "/opt/");
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中读取时间轴数据,并进行拼接
     * @param val val
     * @param timePeriod timePeriod
     * @return
     */
    private StringBuilder joinXValue(StringBuilder val, String timePeriod){
    	MonitorController monCon = new MonitorController();
        List<String> lst = monCon.getXValue(influxDB, dbName, timePeriod);
        for (int i=0;i < lst.size();i++){
        	val.append("\"").append(lst.get(i)).append("\",");
        }
        //去掉最后一个逗号
        val.deleteCharAt(val.length() - 1);
        val.append("],");
        return val;
    }
    
    /**
     * 从数据库中读取监控数据,并进行拼接
     * @param val val
     * @param legendName legendName
     * @param timePeriod timePeriod
     * @param dataType dataType
     * @return
     */
    private StringBuilder joinClusterYValue(StringBuilder val, String legendName, String timePeriod, String dataType){
        MonitorController monCon = new MonitorController();
    	val.append("{\"legendName\": \"");
    	val.append(legendName);
    	val.append("\",\"yAxis\": [");
        List<String> lst = monCon.getClusterData(influxDB, dbName, timePeriod, dataType);
        for (int i=0;i < lst.size();i++){
        	val.append("\"").append(lst.get(i)).append("\",");
        }
        //去掉最后一个逗号
        val.deleteCharAt(val.length() - 1);
        val.append("]},");
        return val;
    }
    
    /**
     * 从数据库中读取监控数据,并进行拼接
     * @param val val
     * @param legendName legendName
     * @param timePeriod timePeriod
     * @param dataType dataType
     * @return
     */
    private StringBuilder joinContainerYValue(StringBuilder val, String legendName, String timePeriod, 
    		String dataType, String namespace, String podName, String containerName){
        MonitorController monCon = new MonitorController();
    	val.append("{\"legendName\": \"");
    	val.append(legendName);
    	val.append("\",\"yAxis\": [");
        List<String> lst = monCon.getContainerData(influxDB, dbName, timePeriod, dataType, namespace, podName, containerName);
        for (int i=0;i < lst.size();i++){
        	val.append("\"").append(lst.get(i)).append("\",");
        }
        //去掉最后一个逗号
        val.deleteCharAt(val.length() - 1);
        val.append("]},");
        return val;
    }
    
    /**
     * 取得所有容器名称
     * 
     * @param namespace
     * @param podName
     * @return
     */
    private List<String> getAllContainerName(String namespace, String podName){

        List<String> listString = new ArrayList<>();
        
    	String sql = "SELECT  container_name, last(\"value\")  FROM  \"memory/limit_bytes_gauge\"  WHERE 1=1  and " + 
    	        "\"pod_namespace\" = \'" + namespace + 
    			"\'  AND \"pod_name\" = \'" + podName + 
    			"\' AND time > now() - 5m GROUP BY pod_namespace ,pod_name ,container_name, time(1m) fill(null)";
        Query sqlQuery = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(sqlQuery);
        List<Series> seriesLst = result_mem_limit.getResults().get(0).getSeries();
        for (Series series : seriesLst){
        	List<List<Object>> listObject = series.getValues();
        	listString.add(listObject.get(1).get(1).toString());
        }
        return listString;
    }
}
