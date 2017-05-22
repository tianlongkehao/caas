package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 映射端口类
 * @author YuanPeng
 * @version 2016年9月18日
 * @see PortConfig
 * @since
 */
@Entity
public class PortConfig {

	/**
	 * 端口配置主键
	 */
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long portId;

	/**
	 * 容器端口
	 */
    private String containerPort;
	/**
	 * 协议
	 */
    private String protocol;
	/**
	 * 映射端口
	 */
    private String mapPort;

	/**
	 * 关联服务Id
	 */
    private long serviceId;

	/**
	 * 关联DNS服务Id
	 */
    private long dnsServiceId;


    public long getPortId() {
    	return portId;
    }
    public void setPortId(long portId) {
    	this.portId = portId;
    }
    public String getContainerPort() {
    	return containerPort;
    }
    public void setContainerPort(String containerPort) {
    	this.containerPort = containerPort;
    }
    public String getProtocol() {
    	return protocol;
    }
    public void setProtocol(String protocol) {
    	this.protocol = protocol;
    }
    public String getMapPort() {
    	return mapPort;
    }
    public void setMapPort(String mapPort) {
    	this.mapPort = mapPort;
    }
    public long getServiceId() {
    	return serviceId;
    }
    public void setServiceId(long serviceId) {
    	this.serviceId = serviceId;
    }
    public long getDnsServiceId() {
		return dnsServiceId;
	}
	public void setDnsServiceId(long dnsServiceId) {
		this.dnsServiceId = dnsServiceId;
	}
	@Override
	public String toString() {
		return "portId:" + portId + ",containerPort:" + containerPort + ",protocol:" + protocol + ",mapPort:" + mapPort
				+ ",serviceId:" + serviceId;
	}
}
