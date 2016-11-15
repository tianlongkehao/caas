/*
 * 文件名：SheraClientService.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.util;

import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.shera.api.SheraAPIClient;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.util.CurrentUserUtils;

/**
 * @author ke_wang
 * @version 2016年11月11日
 * @see SheraClientService
 * @since
 */

public class SheraClientService {
    
    @Value("${shera.api.endpoint}")
    private String endpoint="http://192.168.0.76:8383/";
    private String username="shera";
    private String password="shera";
    
    public SheraAPIClientInterface getClient() {
        String namespace = "admin";
        return getclient(namespace);
    }

    public SheraAPIClientInterface getclient(String namespace) {
        return new SheraAPIClient(endpoint, namespace, username, password,new RestFactory());
    }
    
    public Job generateJob(String id ,String nextExecTime, String jdkVersion) {
        Job job = new Job();
        job.setId(id);
        job.setJdkVersion(jdkVersion);
        return job;
    }
    
    public static void main(String[] args) {
        SheraClientService sheraClientService = new SheraClientService();
        SheraAPIClientInterface client = sheraClientService.getClient();
        try {
            client.getAllJobs();
        }
        catch (SheraClientException e) {
           e.printStackTrace();
        }
        
    }
}
