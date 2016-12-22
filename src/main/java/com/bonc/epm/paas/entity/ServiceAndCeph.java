/*
 * 文件名：ServiceAndCeph.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月20日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * 服务和ceph挂载卷之间的关联表
 * @author zhoutao
 * @version 2016年12月20日
 * @see ServiceAndCeph
 * @since
 */
@Entity
public class ServiceAndCeph {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id ;
    
    /**
     * 服务id
     */
    private long serviceId;
    
    /**
     * cephId
     */
    private long cephId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getCephId() {
        return cephId;
    }

    public void setCephId(long cephId) {
        this.cephId = cephId;
    }
    
}
