/*
 * 文件名：ServiceTopo.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年10月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import java.util.List;

public class ServiceTopo {
    /**
     * 服务名称
     */
    private String serviceName;
    
    /**
     * namespace
     */
    private String namespace;
    
    /**
     * podName
     */
    private List<String> podName;

    public List<String> getPodName() {
        return podName;
    }

    public void setPodName(List<String> podName) {
        this.podName = podName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    
}
