package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
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

import com.alibaba.fastjson.JSONArray;
import com.bonc.epm.paas.entity.ContainerUse;
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
    private ClusterDao clusterDao;

    @Value("${yumConf.io.address}")
    private String yumSource;

    @Value("${cc.url}")
    private String url;
    @Value("${cc.username}")
    private String username;
    @Value("${cc.password}")
    private String password;
    @Value("${cc.dbName}")
    private String dbName;

    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    public String resourceCluster(Model model) {

//    	List<Cluster> clusterlst=(List<Cluster>) clusterDao.findAll();
//    	Cluster cluster=null;
//    	JSONObject json=null;
//    	for(int i=0;i<clusterlst.size();i++){
//    		cluster=clusterlst.get(i);
//    		ClusterUse clusterUse=this.getClusterUse(cluster.getHost());
//    		json=new JSONObject(clusterUse);
//    		model.addAttribute("clusterUse"+i,json);
//    	}
//    	
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
    }

    @RequestMapping(value = {"/containers"}, method = RequestMethod.GET)
    public String resourceContainers(Model model) {
        /**
         * containers.jsp
         */


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
        ClusterUse clusterUse;
        try {
            clusterUse = new ClusterUse();
            MonitorController monCon = new MonitorController();

            /*//取得主机hostName
            //String hostName = "minion" + hostIp.split("\\.")[3];

            //查询主机cpu使用值
            List<String> cpuUseList = monCon.getMinionCpuUse(timePeriod);
            clusterUse.setCpuUse(cpuUseList);

            //查询主机的cpuLimit
            List<String> cpuLimitList = monCon.getMinionCpuLimit();
            clusterUse.setCpuLimit(cpuLimitList);

            //查询内存使用量memUse
            List<String> memUseList = monCon.getMinionMemUse(timePeriod);
            clusterUse.setMemUse(memUseList);

            //查询内存memWorking Set
            List<String> memSetList = monCon.getMinionMemSet(timePeriod);

            clusterUse.setMemSet(memSetList);

            //查询主机内存memLimit
            List<String> memLimitList = monCon.getMinionMemLimit();
            clusterUse.setMemLimit(memLimitList);

            //查询disk_use
            List<String> diskUseList = monCon.getMinionDiskUse(timePeriod);
            clusterUse.setDiskUse(diskUseList);

            //查询disk_limit
            List<String> diskLimitList = monCon.getMinionDiskLimit();
            clusterUse.setDiskLimit(diskLimitList);

            //查询网络上行值tx
            List<String> networkTxList = monCon.getMinionTxUse(timePeriod);
            clusterUse.setNetworkTx(networkTxList);

            //查询网络下行值rx
            List<String> networkRxList = monCon.getMinionRxUse(timePeriod);
            clusterUse.setNetworkRx(networkRxList);

            //设置主机IP
            clusterUse.setHost(hostIp);*/
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public Object getClusterUse2(String timePeriod) {
        MonitorController monCon = new MonitorController();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //overall cluster memory usage:mem_limit
            List<String> memLimitAllList = monCon.getMemLimitOverAll(timePeriod);
            JSONObject json1 = new JSONObject();
            json1.put("yAxis", memLimitAllList);
            JSONArray jsonArr1 = new JSONArray();
            jsonArr1.add(0, memLimitAllList);
            map.put("yAxis", memLimitAllList);

            //overall cluster memory usage:mem_use
            List<String> memUseAllList = monCon.getMemUseOverAll(timePeriod);
            map.put("yAxis", memUseAllList);


            //overall cluster memory usage:mem_workingSet
            List<String> memWorkingSetAllList = monCon.getMemSetOverAll(timePeriod);
            map.put("yAxis", memWorkingSetAllList);

            //memory usage group by node:memory_limit
            List<String> memLimitNodeList = monCon.getMemSetNode(timePeriod);
            map.put("yAxis", memLimitNodeList);

            //memory usage group by node:memory_use
            List<String> memUseNodeList = monCon.getMemUseNode(timePeriod);
            map.put("yAxis", memUseNodeList);

            //individual node memory usage： mem_limit
            List<String> memLimitMinionList = monCon.getMemLimitMinion(timePeriod);
            map.put("yAxis", memLimitMinionList);

            //individual node memory usage:memUse
            List<String> memUseMinionList = monCon.getMemUseMinion(timePeriod);
            map.put("yAxis", memUseMinionList);

            //individual node memory usage:memory_working_set
            List<String> memSetMinionList = monCon.getMemSetMinion(timePeriod);
            map.put("yAxis", memSetMinionList);

            //cpu use group by node:cpu_limit
            List<String> cpuLimitNodeList = monCon.getCpuLimitNode(timePeriod);
            map.put("yAxis", cpuLimitNodeList);

            //cpu use group by node:cpu_use
            List<String> cpuUseNodeList = monCon.getCpuUseNode(timePeriod);
            map.put("yAxis", cpuUseNodeList);

            //individual node cpu usage:cpu_limit
            List<String> cpuLimitMinionList = monCon.getCpuLimitMinion(timePeriod);
            map.put("yAxis", cpuLimitMinionList);

            //individual node cpu usage:cpu_use
            List<String> cpuUseMinionList = monCon.getCpuUseMinion(timePeriod);
            map.put("yAxis", cpuUseMinionList);

            //overall cluster disk usage:disk_limit
            List<String> diskLimitAllList = monCon.getDiskLimitOverAll(timePeriod);
            map.put("yAxis", diskLimitAllList);

            //overall cluster disk usage:disk_use
            List<String> diskUseAllList = monCon.getDiskUseOverAll(timePeriod);
            map.put("yAxis", diskUseAllList);

            //disk usage group by node:disk_limit
            List<String> diskLimitNodeList = monCon.getDiskLimitNode(timePeriod);
            map.put("yAxis", diskLimitNodeList);

            //disk usage group by node:disk_use
            List<String> diskUseNodeList = monCon.getDiskUseNode(timePeriod);
            map.put("yAxis", diskUseNodeList);

            //individual node disk usage:disk_limit
            List<String> diskLimitMinionList = monCon.getDiskLimitMinion(timePeriod);
            map.put("yAxis", diskLimitMinionList);

            //individual node disk usage:disk_use
            List<String> diskUseIndList = monCon.getDiskUseMinion(timePeriod);
            map.put("yAxis", diskUseIndList);

            //network usage group by node:tx
            List<String> networkTxNodeList = monCon.getTxNode(timePeriod);
            map.put("yAxis", networkTxNodeList);

            //network usage group by node:rx
            List<String> networkRxNodeList = monCon.getRxNode(timePeriod);
            map.put("yAxis", networkRxNodeList);


            //individual node network usage:tx
            List<String> networkTxMinionList = monCon.getTxMinion(timePeriod);
            map.put("yAxis", networkTxMinionList);

            //individual node network usage:rx
            List<String> networkRxMinionList = monCon.getRxMinion(timePeriod);
            map.put("yAxis", networkRxMinionList);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return null;
    }


    /**
     * 根据pod_namespace、pod_name、container_name取得资源使用情况;
     *
     * @param pod_namespace
     * @param pod_name
     * @param container_name
     * @return ContainerUse
     */
    public List<ContainerUse> getContainerUse(String pod_namespace, String pod_name, String container_name, String timePeriod, String timeGroup) {
        List<ContainerUse> containerUseList = null;

        try {
            InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);

            String cpuUse_sql = "SELECT non_negative_derivative(max(value),1u) FROM \"cpu/usage_ns_cumulative\" WHERE 1=1 ";
            if (pod_namespace != null && !"".equals(pod_namespace)) {
                cpuUse_sql += "and \"pod_namespace\" = \'" + pod_namespace + "\'";
            }
            if (pod_name != null && !"".equals(pod_name)) {
                cpuUse_sql += " AND  \"pod_name\" =  \'" + pod_name + "\'";
            }
            if (container_name != null && !"".equals(container_name)) {
                cpuUse_sql += " and \"container_name\" = \'" + container_name + "\'";
            }
            cpuUse_sql += " AND time > now() - 30m GROUP BY  pod_namespace ,pod_name ,container_name,time(30s) fill(null)";
            Query query_cpu_use = new Query(cpuUse_sql, dbName);
            QueryResult result_cpu_use = influxDB.query(query_cpu_use);//Returns:  a List of Series which matched the query.
            //取得series列表
            List<Series> cpu_use_values = result_cpu_use.getResults().get(0).getSeries();//.get(0).getValues();


            String cpuLimit_sql = "SELECT last(\"value\") FROM \"cpu/limit_gauge\" WHERE 1=1 ";
            if (pod_namespace != null && !"".equals(pod_namespace)) {
                cpuLimit_sql += " and \"pod_namespace\" = \'" + pod_namespace + "\'";
            }
            if (pod_name != null && !"".equals(pod_name)) {
                cpuLimit_sql += " AND  \"pod_name\" = \'" + pod_name + "\'";
            }
            if (container_name != null && !"".equals(container_name)) {
                cpuLimit_sql += " and \"container_name\" =\'" + container_name + "\'";
            }
            cpuLimit_sql += " AND time > now() - 30m GROUP BY pod_namespace ,pod_name ,container_name, time(30s) fill(null)";
            Query query_cpu_limit = new Query(cpuLimit_sql, dbName);
            QueryResult result_cpu_limit = influxDB.query(query_cpu_limit);
            List<Series> cpu_limit_values = result_cpu_limit.getResults().get(0).getSeries();//.get(0).getValues().get(0).get(1).toString();


            String memUse_sql = "SELECT max(\"value\") FROM \"memory/usage_bytes_gauge\" WHERE  1=1 ";
            if (pod_namespace != null && !"".equals(pod_namespace)) {
                memUse_sql += " and \"pod_namespace\" =\'" + pod_namespace + "\'";
            }
            if (pod_name != null && !"".equals(pod_name)) {
                memUse_sql += " AND  \"pod_name\" = \'" + pod_name + "\'";
            }
            if (container_name != null && !"".equals(container_name)) {
                memUse_sql += " and \"container_name\" =\'" + container_name + "\'";
            }
            memUse_sql += " AND time > now() - 30m GROUP BY  pod_namespace ,pod_name ,container_name,time(30s) fill(null)";
            Query query_mem_use = new Query(memUse_sql, dbName);
            QueryResult result_mem_use = influxDB.query(query_mem_use);
            List<Series> mem_use_values = result_mem_use.getResults().get(0).getSeries();//.get(0).getValues();


            String memLimit_sql = "SELECT last(\"value\") FROM \"memory/limit_bytes_gauge\" WHERE 1=1  ";
            if (pod_namespace != null && !"".equals(pod_namespace)) {
                memLimit_sql += "and \"pod_namespace\" =\'" + pod_namespace + "\'";
            }
            if (pod_name != null && !"".equals(pod_name)) {
                memLimit_sql += " AND  \"pod_name\" =\'" + pod_name + "\'";
            }
            if (container_name != null && !"".equals(container_name)) {
                memLimit_sql += " and \"container_name\" =\'" + container_name + "\'";
            }
            memLimit_sql += " AND time > now() - 30m GROUP BY pod_namespace ,pod_name ,container_name, time(30s) fill(null)";
            Query query_mem_limit = new Query(memLimit_sql, dbName);
            QueryResult result_mem_limit = influxDB.query(query_mem_limit);
            List<Series> mem_limit_values = result_mem_limit.getResults().get(0).getSeries();


            String memWorkingSet_sql = "SELECT max(\"value\") FROM \"memory/working_set_bytes_gauge\" WHERE  1=1 ";
            if (pod_namespace != null && !"".equals(pod_namespace)) {
                memWorkingSet_sql += " and \"pod_namespace\" =\'" + pod_namespace + "\'";
            }
            if (pod_name != null & !"".equals(pod_name)) {
                memWorkingSet_sql += " AND  \"pod_name\" = \'" + pod_name + "\'";

            }
            if (container_name != null && !"".equals(container_name)) {
                memWorkingSet_sql += " and \"container_name\" =\'" + container_name + "\'";
            }
            memWorkingSet_sql += " AND time > now() - 30m GROUP BY pod_namespace ,pod_name ,container_name, time(30s) fill(null)";
            Query query_mem_set = new Query(memWorkingSet_sql, dbName);
            QueryResult result_mem_set = influxDB.query(query_mem_set);
            List<Series> mem_set_values = result_mem_set.getResults().get(0).getSeries();//.get(0).getValues();


            List<List<Object>> cpu_use_list = null;
            List<List<Object>> cpu_limit_list = null;
            List<List<Object>> mem_use_list = null;
            List<List<Object>> mem_limit_list = null;
            List<List<Object>> mem_set_list = null;
            for (int i = 0; i < cpu_use_values.size(); i++) {

                cpu_use_list = cpu_use_values.get(i).getValues();
                cpu_limit_list = cpu_limit_values.get(i).getValues();
                mem_use_list = mem_use_values.get(i).getValues();
                mem_limit_list = mem_limit_values.get(i).getValues();
                mem_set_list = mem_set_values.get(i).getValues();

                List<String> cpuUseList = null;
                List<String> cpuLimitList = null;
                List<String> memUseList = null;
                List<String> memLimitList = null;
                List<String> memSetList = null;
                for (int j = 0; j < cpu_use_values.size(); j++) {

                    String strCpuUse = cpu_use_list.get(j).get(1).toString();
                    String strCpuLimit = cpu_limit_list.get(j).get(1).toString();
                    String strMemUse = mem_use_list.get(j).get(1).toString();
                    String strMemLimit = mem_limit_list.get(j).get(1).toString();
                    String strMemSet = mem_set_list.get(j).get(1).toString();

                    cpuUseList.add(strCpuUse);
                    cpuLimitList.add(strCpuLimit);
                    memUseList.add(strMemUse);
                    memLimitList.add(strMemLimit);
                    memSetList.add(strMemSet);

                }
                ContainerUse containerUse = null;
                containerUse.setCpuUse(cpuUseList);
                containerUse.setCpuLimit(cpuLimitList);
                containerUse.setMemUse(memUseList);
                containerUse.setMemLimit(memLimitList);
                containerUse.setMemWorkingSet(memSetList);

                containerUseList.add(containerUse);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return containerUseList;
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
            for (String ipSon : ipsArray) {
                lstIps.add(ipSon);
            }
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
}
