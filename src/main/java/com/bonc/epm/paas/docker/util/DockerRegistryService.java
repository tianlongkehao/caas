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

import java.io.IOException;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.docker.api.DockerRegistryClient;
import com.bonc.epm.paas.docker.config.DockerRegistryConfig;
import com.bonc.epm.paas.docker.config.DockerRegistryConfigProvider;
import com.bonc.epm.paas.docker.data.DockerRegistrySearchResponse;
import com.bonc.epm.paas.docker.data.DockerRepositoryContext;
import com.sun.jersey.api.client.Client;

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
    /**
     * 
     * Description:
     * 获取dockerregistryclient实例
     * @return regClient DockerRegistryClient
     * @see DockerRegistryConfig
     */
    public DockerRegistryClient  getDockerRegistryClient() {
        DockerRegistryConfig config = new DockerRegistryConfigProvider().get();
        Client client = new Client();
        DockerRegistryClient regClient = new DockerRegistryClient(client, config);
        LOG.info("===================getDockerRegistryClient============");
        return regClient;
    }

    /**
     * 
     * Description:
     * 搜索仓库
     * @param query String
     * @return  DockerRegistrySearchResponse
     * @see
     */
    public DockerRegistrySearchResponse searchRegistry(String query) {
        DockerRegistryClient regClient = this.getDockerRegistryClient();
        try {
            DockerRegistrySearchResponse searchResponse = regClient.searchRegistry(query);
            return searchResponse;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void deleteRepositoryTag() {
        try {
            DockerRegistryClient regClient = this.getDockerRegistryClient();
            DockerRepositoryContext repContext = new DockerRepositoryContext("testbonc","testtest");
            repContext.setAuth("192.168.0.29:5000", "root");
            regClient.deleteRepositoryTag(repContext, "v1");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * Description:
     * 测试 main 方法 
     * @param args 
     * @see
     */
/*    public static void main(String[] args) {
        DockerRegistryService service = new DockerRegistryService();
        service.deleteRepositoryTag();
        //service.searchRegistry("nodejs");
    }*/
}
