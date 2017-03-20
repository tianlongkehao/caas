package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * 构建记录信息实体类
 *
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 构建id
	 */
	private long ciId;

	/**
	 * 构建名称
	 */
	private String ciName;

	/**
	 * 构建版本
	 */
	private String ciVersion;

	/**
	 * 构建镜像id
	 */
	private long imageId;

	/**
	 * 构建者
	 */
	private long creatBy;

	/**
	 * 构建者名称
	 */
	@Transient
	private String creatorName;

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
	 * 代码构建的seqNo
	 */
	private Integer executionId;

	/**
	 * 构建日志
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String logPrint;

	/**
	 * 构建dockerFileContent
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String dockerFileContent;


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
		return constructTime/1000;
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

	public Integer getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Integer executionId) {
		this.executionId = executionId;
	}

	public String getLogPrint() {
		return logPrint;
	}

	public void setLogPrint(String logPrint) {
		this.logPrint = logPrint;
	}

	public String getCiName() {
		return ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getDockerFileContent() {
		return dockerFileContent;
	}

	public void setDockerFileContent(String dockerFileContent) {
		this.dockerFileContent = dockerFileContent;
	}

	public long getCreatBy() {
		return creatBy;
	}

	public void setCreatBy(long creatBy) {
		this.creatBy = creatBy;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
}
