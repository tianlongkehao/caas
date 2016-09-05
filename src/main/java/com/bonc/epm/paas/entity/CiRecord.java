package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 构建记录信息实体类
 * @author update
 * @version 2016年8月31日
 * @see CiRecord
 * @since
 */
@Entity
public class CiRecord {
    
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	/**
	 * 构建id
	 */
    private long ciId;
    
	/**
	 * 构建版本
	 */
    private String ciVersion;
    
	/**
	 * 构建时间
	 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date constructDate;
    
	/**
	 * 构建时长
	 */
    private long constructTime;
    
	/**
	 * 构建结果：1成功2失败3构建中
	 */
    private Integer constructResult;
	
	/**
	 * 构建日志
	 */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	private String logPrint;
	
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getCiId() {
        return ciId;
    }
    
    public void setCiId(long ciId) {
        this.ciId = ciId;
    }
    
    public String getCiVersion() {
        return ciVersion;
    }
    
    public void setCiVersion(String ciVersion) {
        this.ciVersion = ciVersion;
    }
    
    public Date getConstructDate() {
        return constructDate;
    }
    
    public void setConstructDate(Date constructDate) {
        this.constructDate = constructDate;
    }
    
    public long getConstructTime() {
        return constructTime;
    }
    
    public void setConstructTime(long constructTime) {
        this.constructTime = constructTime;
    }
    
    public Integer getConstructResult() {
        return constructResult;
    }
    
    public void setConstructResult(Integer constructResult) {
        this.constructResult = constructResult;
    }
	
    public String getLogPrint() {
        return logPrint;
    }
    
    public void setLogPrint(String logPrint) {
        this.logPrint = logPrint;
    }
	
	
}