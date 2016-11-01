/*
 * 文件名：EndpointAddress.java
 * 版权：Copyright by www.huawei.com
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
 * @see EndpointAddress
 * @since
 */
public class EndpointAddress {
    private String ip;
    private String hostname;
    private String nodeName;
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
}
