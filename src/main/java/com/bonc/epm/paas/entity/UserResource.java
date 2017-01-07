/*
 * 文件名：UserResource.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2017年1月4日
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
 * 
 * 租户资源信息表
 * @author zhoutao
 * @version 2017年1月4日
 * @see UserResource
 * @since
 */
@Entity
public class UserResource {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    /**
     * 关联用户id
     */
    private long userId;
    
    /**
     * cpu大小
     */
    private double cpu;
    
    /**
     * 内存大小
     */
    private long memory;
    
    /**
     * 卷组容量
     */
    private long vol_size = 0;
    
    /**
     * 卷组剩余容量
     */
    private long vol_surplus_size = 0;
    
    /**
     * 镜像数量
     */
    private long image_count = 0; 
    
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    
    
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public long getVol_size() {
        return vol_size;
    }

    public void setVol_size(long vol_size) {
        this.vol_size = vol_size;
    }

    public long getImage_count() {
        return image_count;
    }

    public void setImage_count(long image_count) {
        this.image_count = image_count;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public long getVol_surplus_size() {
		return vol_surplus_size;
	}

	public void setVol_surplus_size(long vol_surplus_size) {
		this.vol_surplus_size = vol_surplus_size;
	}
     

}
