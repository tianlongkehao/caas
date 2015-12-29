package com.bonc.epm.paas.controller;

import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.entity.Cluster;
import com.bonc.epm.paas.entity.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class ClusterController {
    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @RequestMapping(value = {"cluster/list"}, method = RequestMethod.GET)
    public String clusterList(Model model) {
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
    }
    @RequestMapping(value = {"cluster/detail"}, method = RequestMethod.GET)
    public String clusterDetail(Model model) {
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-detail.jsp";
    }

    @RequestMapping(value = {"cluster/add"}, method = RequestMethod.GET)
    public String clusterAdd() {
        return "cluster/cluster-create.jsp";
    }

    @Autowired
    private ClusterDao clusterDao;

    @RequestMapping(value = {"cluster/getClusters"}, method = RequestMethod.POST)
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
                for (int i = ipStart; i < ipEnd; i++) {
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

    @RequestMapping(value = {"cluster/installCluster"}, method = RequestMethod.GET)
    @ResponseBody
    public String installCluster(@RequestParam String user, @RequestParam String pass, @RequestParam String ip,
                                 @RequestParam Integer port, @RequestParam String type)
            throws IOException, JSchException, InterruptedException {

        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().build();
        String imageHostPort = config.getUsername();

        SshConnect.connect(user, pass, ip, port);
        //获取主机的内存大小
        Integer memLimit = 1000000;
        String memCmd = "cat /proc/meminfo | grep MemTotal | awk -F ':' '{print $2}' | awk '{print $1}'";
        String memRtn = SshConnect.exec(memCmd, 1000);
        String[] b = memRtn.split("\n");
        memLimit = Integer.valueOf(b[b.length - 2].trim());
        //安装环境
        String hostName = "centos-minion" + ip.split(".")[3];
        String cmd = "cd /opt/;nohup ./envInstall.sh " + imageHostPort + " " + type + " 172.16.71.171 " + hostName;
        SshConnect.exec(cmd, 30000);
        //关闭SSH连接
        SshConnect.disconnect();
        Cluster cluster = clusterDao.findByHost(ip);
        if (cluster == null) {
            Cluster newCluster = new Cluster();
            newCluster.setUsername(user);
            newCluster.setPassword(pass);
            newCluster.setHost(ip);
            newCluster.setPort(port);
            clusterDao.save(newCluster);
        }
        return "";
    }

    @RequestMapping(value = {"cluster/copyFile"}, method = RequestMethod.GET)
    @ResponseBody
    public String copyFile(@RequestParam String user, @RequestParam String pass, @RequestParam String ip,
                           @RequestParam Integer port)
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
        return "安装文件拷贝成功";

    }
}
