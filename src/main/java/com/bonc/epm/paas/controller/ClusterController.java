package com.bonc.epm.paas.controller;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.entity.Cluster;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.util.SshConnect;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {
    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    public String resourceCluster(Model model) {

        List<Cluster> lstClusters = new ArrayList<>();
        for(Cluster cluster : clusterDao.findAll()){
            lstClusters.add(cluster);
        }
        model.addAttribute("lstClusters",lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
    }
    @RequestMapping(value = {"/management"}, method = RequestMethod.GET)
    public String clusterList(Model model) {

        List<Cluster> lstClusters = new ArrayList<>();
        for(Cluster cluster : clusterDao.findAll()){
            lstClusters.add(cluster);
        }
        model.addAttribute("lstClusters",lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-management.jsp";
    }

    @RequestMapping(value = {"/detail"}, method = RequestMethod.GET)
    public String clusterDetail(Model model) {
        List<Cluster> lstClusters = new ArrayList<>();
        for(Cluster cluster : clusterDao.findAll()){
            lstClusters.add(cluster);
        }
        model.addAttribute("lstClusters",lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-detail.jsp";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String clusterAdd() {
        return "cluster/cluster-create.jsp";
    }

    @Autowired
    private ClusterDao clusterDao;

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
