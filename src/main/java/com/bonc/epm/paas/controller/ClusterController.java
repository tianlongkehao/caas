package com.bonc.epm.paas.controller;

import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.entity.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {
    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String clusterList(Model model) {
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String clusterAdd() {

        return "cluster/cluster-create.jsp";
    }

    @Autowired
    private ClusterDao clusterDao;

    @RequestMapping(value = "/getClusters", method = RequestMethod.GET)
    public String getClusters(String ip, Model model) {
        List<String> lstIps = new ArrayList<>();
        List<Cluster> lstClusters = new ArrayList<>();
        List<String> existIps = new ArrayList<>();
        int index = ip.indexOf("[");
        if (index != -1) {
            String ipHalf = ip.substring(0, index);
            String ipSect = ip.substring(index + 1, ip.length() - 1);
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
            String[] ipsArray = ip.split(",");
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
        model.addAttribute("lstIps", lstClusters);
        return "cluster/Cluster.jsp";
    }

    @RequestMapping(value = "/installCluster", method = RequestMethod.GET)
    public String installCluster(String ip, Model model) {

        return "cluster/installCluster.jsp";
    }


}
