package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    public String resourceCluster(Model model) {

        Float allClusterCpuUse = 0F;
        Float allClusterCpuLimit = 0F;
        Float allClusterMemUse = 0F;
        Float allClusterMemLimit = 0F;

        List<ClusterUse> lstClustersUse = new ArrayList<>();
        List<Cluster> lstClusters = (List<Cluster>) clusterDao.findAll();
        for (Cluster cluster : lstClusters) {
            if ("slave".equals(cluster.getHostType())) {
                ClusterUse clusterUse = getClusterUse(cluster.getHost());
                lstClustersUse.add(clusterUse);
                allClusterCpuUse = allClusterCpuUse + Float.valueOf(clusterUse.getCpuUse());
                allClusterCpuLimit = allClusterCpuLimit + Float.valueOf(clusterUse.getCpuLimit());
                allClusterMemUse = allClusterMemUse + Float.valueOf(clusterUse.getMemUse());
                allClusterMemLimit = allClusterMemLimit + Float.valueOf(clusterUse.getMemLimit());
            }
        }

        model.addAttribute("allClusterCpuUse", allClusterCpuUse);
        model.addAttribute("allClusterCpuLimit", allClusterCpuLimit);
        model.addAttribute("allClusterMemUse", allClusterMemUse);
        model.addAttribute("allClusterMemLimit", allClusterMemLimit);
        model.addAttribute("lstClusters", lstClustersUse);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
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
            ClusterUse clusterUse = getClusterUse(hostIp);
            lstClustersUse.add(clusterUse);
        }
        model.addAttribute("lstClustersUse", lstClustersUse);

        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-detail.jsp";
    }

    /**
     * 取得单一主机资源使用情况
     *
     * @param hostIp
     * @return
     */
    private ClusterUse getClusterUse(String hostIp) {
        ClusterUse clusterUse = new ClusterUse();
        try {
            InfluxDB influxDB = InfluxDBFactory.connect("http://172.16.71.172:30002", "root", "root");

            String dbName = "k8s";
            //设置主机IP
            clusterUse.setHost(hostIp);
            //取得主机hostName
            String hostName = "centos-minion" + hostIp.split("\\.")[3];
            //查询主机cpu使用值
            Query query_cpu_use = new Query("SELECT derivative(value)/10000000 FROM \"cpu/usage_ns_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /" + hostName + "/ AND time > now() - 30s GROUP BY time(10s)", dbName);
            QueryResult result_cpu_use = influxDB.query(query_cpu_use);
            //取得value列表
            List<List<Object>> result_cpu_values = result_cpu_use.getResults().get(0).getSeries().get(0).getValues();
            //value个数
            int result_cpu_size = result_cpu_values.size();
            //取得最后一个value
            String cpu_use = result_cpu_values.get(result_cpu_size - 1).get(1).toString();
            //取小数点后两位
            clusterUse.setCpuUse(cpu_use.substring(0, cpu_use.indexOf(".") + 3));
            //查询主机的CPU
            Query query_cpu_limit = new Query("SELECT value FROM \"cpu/limit_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /" + hostName + "/ order by time desc limit 1", dbName);
            QueryResult result_cpu_limit = influxDB.query(query_cpu_limit);
            //取得主机CPU
            String cpu_limit = result_cpu_limit.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
            //去掉小数点
            clusterUse.setCpuLimit(cpu_limit.substring(0, cpu_limit.indexOf(".")));
            //查询内存使用量
            Query query_mem_use = new Query("SELECT last(value)/1024/1024 FROM \"memory/usage_bytes_gauge\" WHERE \"hostname\" =~ /" + hostName + "/ AND \"container_name\" = 'machine' AND time > now() - 30s GROUP BY time(10s)", dbName);
            QueryResult result_mem_use = influxDB.query(query_mem_use);
            //取得value列表
            List<List<Object>> result_mem_use_values = result_mem_use.getResults().get(0).getSeries().get(0).getValues();
            //value个数
            int result_mem_use_size = result_mem_use_values.size();
            //取得value(防止取到NULL,所以从后面取第三个值)
            String mem_use = result_mem_use_values.get(result_mem_use_size - 3).get(1).toString();
            //取小数点后两位
            clusterUse.setMemUse(mem_use.substring(0, mem_use.indexOf(".") + 3));
            //查询内存Working Set
            Query query_mem_set = new Query("SELECT last(value)/1024/1024 FROM \"memory/working_set_bytes_gauge\" WHERE \"hostname\" =~ /" + hostName + "/ AND \"container_name\" = 'machine' AND time > now() - 30s GROUP BY time(10s)", dbName);
            QueryResult result_mem_set = influxDB.query(query_mem_set);
            //取得value列表
            List<List<Object>> result_mem_set_values = result_mem_set.getResults().get(0).getSeries().get(0).getValues();
            //value个数
            int result_mem_set_size = result_mem_set_values.size();
            //取得value(防止取到NULL,所以从后面取第二个值)
            String mem_set = result_mem_set_values.get(result_mem_set_size - 2).get(1).toString();
            //取小数点后两位
            clusterUse.setMemSet(mem_set.substring(0, mem_set.indexOf(".") + 3));
            //查询主机内存
            Query query_mem_limit = new Query("SELECT value/1024/1024 FROM \"memory/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /" + hostName + "/ order by time desc limit 1", dbName);
            QueryResult result_mem_limit = influxDB.query(query_mem_limit);
            //取得主机CPU
            String mem_limit = result_mem_limit.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
            //去掉小数点
            clusterUse.setMemLimit(mem_limit.substring(0, mem_limit.indexOf(".") + 3));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return clusterUse;
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
            if (ipSect.indexOf("-") != -1) {
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
                } catch (UnknownHostException e) {
                    e.printStackTrace();
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
            String yumSource = "172.16.71.172";
            String cmd = "cd /opt/;chmod +x ./envInstall.sh;nohup ./envInstall.sh " + imageHostPort + " " + yumSource + " " + type + " " + masterName + " " + hostName;
            log.debug("cmd-----------------------------------------------------------------------------------" + cmd);
            SshConnect.exec(cmd, 300000);
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
            return "安装成功";
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
