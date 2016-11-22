/*
 * 文件名：EndpointPort.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月31日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.kubernetes.model;

/**
 * @author ke_wang
 * @version 2016年10月31日
 * @see EndpointPort
 * @since
 */
public class EndpointPort {
    private String name;
    private Integer port;
    private String protocol;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
}
