/*
 * 文件名：EtcdClientService.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月27日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.etcd.util;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.etcd.model.KubeSkyEtcd;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdVersionResponse;

/**
 * etcd API 客户端
 * @author ke_wang
 * @version 2016年10月27日
 * @see EtcdClientService
 * @since
 */
@Service
public class EtcdClientService {
    
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(EtcdClientService.class);

    @Value("${etcd.client.url}")
    private String url;
    
    @Value("${etcd.client.domain}")
    private String domain;
    
    public EtcdClient getEtcdClientInstance() {
        EtcdClient etcd = new EtcdClient(URI.create(url));
        return etcd;
    }
    
    public String generateKeyPrefix() {
        String domainPath = "";
        if (StringUtils.isNotBlank(domain)) {
            String[] doArray = domain.split("\\.");
            for (int i=doArray.length-1;i>=0;i--) {
                domainPath += "/"+doArray[i];
            }
        }
        return domainPath;
    }

    /**
     * 
     * Description:
     * 获取etcd版本
     * @return 
     * @see
     */
    public EtcdVersionResponse getEtcdVersion() {
        EtcdClient client = this.getEtcdClientInstance();
        EtcdVersionResponse version = client.version();
        return version;
    }
    
    /**
     * 
     * Description:
     * 插入记录
     * @return 
     * @see
     */
    public EtcdKeysResponse  putRecord(String namespace, String serName, String serHost) {
        EtcdKeysResponse  response = null;
        try {
            String dnsKey = "/skydns" +generateKeyPrefix()+"/"+namespace+"/"+serName;
            EtcdClient client = this.getEtcdClientInstance();
            KubeSkyEtcd etcdValue = new KubeSkyEtcd(serHost,10,10,30,0);
            response = client.put(dnsKey,JSON.toJSONString(etcdValue)).send().get();
        }
        catch (IOException | EtcdException | EtcdAuthenticationException | TimeoutException e) {
            LOG.error("error message：-" + e.getMessage());
        }
        return response;
    }
    
    /**
     * 
     * Description: 
     * 删除记录
     * @return 
     * @see
     */
    public EtcdKeysResponse deleteRecord(String namespace, String serName) {
        EtcdKeysResponse response = null;
        try {
            String dnsKey = generateKeyPrefix()+"/"+namespace+"/"+serName;
            EtcdClient client = this.getEtcdClientInstance();
            response = client.delete(dnsKey).send().get();
        }
        catch (IOException | EtcdException | EtcdAuthenticationException | TimeoutException e) {
            LOG.error("deleteRecord error message:- " + e.getMessage());
            return null;
        }
        return response;
    }
    
    /**
     * 
     * Description: 
     * 更新 etcd dir key value
     * @param namespace
     * @param serName
     * @param serHost
     * @return 
     * @see
     */
    public EtcdKeysResponse updateRecord(String namespace, String serName, String serHost) {
        EtcdKeysResponse response = null;
        try {
            String dnsKey = generateKeyPrefix()+"/"+namespace+"/"+serName;
            EtcdClient client = this.getEtcdClientInstance();
            client.get(dnsKey).send().get(); // 查看是否存在key值
            
            KubeSkyEtcd etcdValue = new KubeSkyEtcd(serHost,10,10,30,0);
            response = client.put(dnsKey,JSON.toJSONString(etcdValue)).prevExist(true).send().get();
        }
        catch (IOException | EtcdException | EtcdAuthenticationException | TimeoutException e) {
            LOG.error("deleteRecord error message:- " + e.getMessage());
            return null;
        }
        return response;
    }
}
