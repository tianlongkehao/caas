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
    private String url;
    @Value("${docker.registry.api.username}")
    private String username;
    @Value("${docker.registry.api.password}")
    private String password;
    
    public DockerRegistryAPIClientInterface getClient() {
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        return getClient(namespace);
    }
    
    public DockerRegistryAPIClientInterface getClient(String namespace) {
        return new DockerRegistryAPIClient(url, username, password,new RestFactory());
    }

}
