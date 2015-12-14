package com.bonc.epm.paas.entity;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class CiRecord {

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
	private Clob logPrint;
	
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
	public Clob getLogPrint() {
		return logPrint;
	}
	public void setLogPrint(Clob logPrint) {
		this.logPrint = logPrint;
	}
	
	public String getLogPrintStr() {
		try {
			if(logPrint!=null){
				return logPrint.getSubString(1,(int)logPrint.length());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setLogPrintStr(String str){
        try {
			logPrint = new javax.sql.rowset.serial.SerialClob(str.toCharArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}