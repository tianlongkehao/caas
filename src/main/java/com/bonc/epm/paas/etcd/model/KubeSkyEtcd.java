/*
 * 文件名：KubeSkyEtcd.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月27日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.etcd.model;

/**
 * sky2dns写入etcd的value
 * @author ke_wang
 * @version 2016年10月27日
 * @see KubeSkyEtcd
 * @since
 */
public class KubeSkyEtcd {
    /**
     * host
     */
    private String host;
    
    /**
     * priority
     */
    private Integer priority;
    
    /**
     * weight
     */
    private Integer weight;
    
    /**
     * ttl
     */
    private Integer ttl;

    /**
     * targetstrip
     */
    private Integer targetstrip;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public Integer getTargetstrip() {
        return targetstrip;
    }

    public void setTargetstrip(Integer targetstrip) {
        this.targetstrip = targetstrip;
    }
    
    public KubeSkyEtcd(String host) {
        this.host = host;
    }
    
    public KubeSkyEtcd(String host,Integer priority,Integer weight,Integer ttl,Integer targetstrip) {
        this.host = host;
    }
}
