package com.bonc.epm.paas.controller;

import com.bonc.epm.paas.dao.HostDao;
import com.bonc.epm.paas.entity.Host;
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
@RequestMapping(value = "/host")
public class HostController {

    @Autowired
    private HostDao hostDao;

    @RequestMapping(value = "/getHosts", method = RequestMethod.GET)
    public String getHosts(String ip, Model model) {
        List<String> lstIps = new ArrayList<>();
        List<Host> lstHosts = new ArrayList<>();
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
        Iterable<Host> a = hostDao.findAll();
        for (Host b : a) {
            existIps.add(b.getHost());
        }
        for (String ipSon : lstIps) {
            //增加数据库中是否存在的验证
            if (!existIps.contains(ipSon)) {
                try {
                    Socket socket = new Socket(ipSon, 22);
                    if (socket.isConnected()) {
                        Host conHost = new Host();
                        conHost.setHost(ipSon);
                        conHost.setPort(22);
                        lstHosts.add(conHost);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("lstIps", lstHosts);
        return "host/hosts.jsp";
    }

    @RequestMapping(value = "/installHost", method = RequestMethod.GET)
    public String installHost(String ip, Model model) {



        return "host/installHost.jsp";
    }

}
