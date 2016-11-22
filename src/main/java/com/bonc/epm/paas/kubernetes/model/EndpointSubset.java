/*
 * 文件名：EndpointSubset.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月31日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年10月31日
 * @see EndpointSubset
 * @since
 */

public class EndpointSubset {
    private List<EndpointAddress> addresses;
    private List<EndpointAddress> notReadyAddresses;
    private List<EndpointPort> ports;
    public List<EndpointAddress> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<EndpointAddress> addresses) {
        this.addresses = addresses;
    }
    public List<EndpointAddress> getNotReadyAddresses() {
        return notReadyAddresses;
    }
    public void setNotReadyAddresses(List<EndpointAddress> notReadyAddresses) {
        this.notReadyAddresses = notReadyAddresses;
    }
    public List<EndpointPort> getPorts() {
        return ports;
    }
    public void setPorts(List<EndpointPort> ports) {
        this.ports = ports;
    }
}
