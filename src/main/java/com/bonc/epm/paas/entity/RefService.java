/*
 * 文件名：RefService.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：Hello World
 * 修改时间：2016年9月12日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 外部服务信息类
 * @author YuanPeng
 * @version 2016年9月12日
 * @see RefService
 * @since
 */
@Entity
public class RefService {

    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    /**
     * 服务名称
     */
    private String serName;
    /**
     * 创建者
     */
    private long createBy;
    /**
     * 服务访问地址
     */
    private String serAddress;
    /**
     * 外部服务地址
     */
    private String refAddress;
    /**
     * 外部服务端口
     */
    private int refPort;
    /**
     * 可见域 （0：本租户；1：所有租户）
     */
    private int viDomain;
    
    /**
     * 服务引入方式 （0：service方式 ；1：etcd方式）
     */
    private int improtSerMode;
    
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    
    /**
     * 服务node暴露端口
     */
    private Integer nodePort;
    /**
     * 描述
     */
    private String refSerDesc;
    /**
     * 是否使用代理
     */
    private String useProxy;

    public String getUseProxy() {
        return useProxy;
    }
    public void setUseProxy(String useProxy) {
        this.useProxy = useProxy;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getCreateBy() {
        return createBy;
    }
    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }
    public String getSerAddress() {
        return serAddress;
    }
    public String getSerName() {
        return serName;
    }
    public void setSerName(String serName) {
        this.serName = serName;
    }
    public void setSerAddress(String serAddress) {
        this.serAddress = serAddress;
    }
    public String getRefAddress() {
        return refAddress;
    }
    public void setRefAddress(String refAddress) {
        this.refAddress = refAddress;
    }
    public int getRefPort() {
        return refPort;
    }
    public void setRefPort(int refPort) {
        this.refPort = refPort;
    }
    public int getViDomain() {
        return viDomain;
    }
    public void setViDomain(int viDomain) {
        this.viDomain = viDomain;
    }
    public int getimprotSerMode() {
        return improtSerMode;
    }
    public void setimprotSerMode(int improtSerMode) {
        this.improtSerMode = improtSerMode;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Integer getNodePort() {
        return nodePort;
    }
    public void setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
    }
    public String getRefSerDesc() {
        return refSerDesc;
    }
    public void setRefSerDesc(String refSerDesc) {
        this.refSerDesc = refSerDesc;
    }
    
}
