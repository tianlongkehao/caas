/*
 * 文件名：NginxCfgClientService.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月29日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.nginxcfgs.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.nginxcfgs.api.NginxCfgAPIClient;
import com.bonc.epm.paas.nginxcfgs.api.NginxCfgAPIClientInterface;
import com.bonc.epm.paas.nginxcfgs.model.NginxCfgs;
import com.bonc.epm.paas.rest.util.RestFactory;

/**
 * @author ke_wang
 * @version 2016年11月29日
 * @see NginxCfgClientService
 * @since
 */
@Service
public class NginxCfgClientService {
    
    @Value("${nginxcfg.api.endpoint}")
    private String endpoint="192.168.0.75:8888/";
    private String username="nginx";
    private String password="nginx";
    
    public NginxCfgAPIClientInterface getclient() {
        return new NginxCfgAPIClient(endpoint, username, password,new RestFactory());
    }
    
    
/*  public static void main(String[] args) {
      NginxCfgClientService nginxCfgClientService = new NginxCfgClientService();
      NginxCfgAPIClientInterface client = nginxCfgClientService.getclient();
      List<NginxCfgs> cfgs = client.getAllNginxCfgs();
      System.out.println(cfgs);
  }*/
}
