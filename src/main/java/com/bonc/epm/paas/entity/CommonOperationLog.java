/*
 * 文件名：CommonOperationLog.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年12月26日
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

/**
 * @author ke_wang
 * @version 2016年12月26日
 * @see CommonOperationLog
 * @since
 */
@Entity
public class CommonOperationLog {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /**
     * 记录信息Name
     */
    private String commonName;
    /**
     * 额外信息
     */
    private String extraInfo;
    /**
     * 类别
     */
    private Integer catalogType;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建人姓名 
     */
    private String createUsername;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public Integer getCatalogType() {
        return catalogType;
    }
    public void setCatalogType(Integer catalogType) {
        this.catalogType = catalogType;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	public Integer getOperationType() {
		return operationType;
	}
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
	public String getCreateUsername() {
		return createUsername;
	}
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}
	
}
