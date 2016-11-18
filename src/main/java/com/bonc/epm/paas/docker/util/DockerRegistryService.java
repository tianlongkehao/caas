/*
 * 文件名：DockerRegistryService.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.docker.api.DockerRegistryAPIClient;
import com.bonc.epm.paas.docker.api.DockerRegistryAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.util.CurrentUserUtils;

/**
 * DockerRegistryService
 * @author ke_wang
 * @version 2016年10月10日
 * @see DockerRegistryService
 * @since
 */
@Service
public class DockerRegistryService {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(DockerRegistryService.class);
    
    @Value("${docker.registry.api.url}")
    private String url ="http://192.168.0.76:5000";
    @Value("${docker.registry.api.username}")
    private String username="docker";
    @Value("${docker.registry.api.password}")
    private String password="docker";
    
    public DockerRegistryAPIClientInterface getClient() {
        return new DockerRegistryAPIClient(url, username, password,new RestFactory());
    }
    
    public static void main(String[] args) {
        DockerRegistryService dockerRegistryService = new DockerRegistryService();
        DockerRegistryAPIClientInterface client = dockerRegistryService.getClient();
        System.out.println(client.getManifestofImage("centos", "7"));
    }

}
