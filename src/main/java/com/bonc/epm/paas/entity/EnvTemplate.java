/*
 * 版权：Copyright by bonc
 * 描述：环境变量模板
 * 修改人：zhoutao
 * 修改时间：2016年8月18日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;
@Entity
public class EnvTemplate {
    /**
     * 环境变量模板Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    
    /**
     * 环境变量模板创建者
     */
    private long createBy;
    
    /**
     * 环境变量Key
     */
    private String envKey;
    
    /**
     * 环境变量value
     */
    @Column(length = 4096)
    private String envValue;
    
    /**
     * 环境变量模板名称
     */
    private String templateName;
    
    /**
     * 环境变量模板创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }

    public String getEnvKey() {
        return envKey;
    }

    public void setEnvKey(String envKey) {
        this.envKey = envKey;
    }

    public String getEnvValue() {
        return envValue;
    }

    public void setEnvValue(String envValue) {
        this.envValue = envValue;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    
}
