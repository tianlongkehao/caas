/*
 * 文件名：PodTopo.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年10月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import java.util.Date;
import java.util.UUID;

public class FileUploadProgress {

	/**
	 * uuid;
	 */
	private String uuid;

	/**
	 * fileName
	 */
	private String fileName;

	/**
	 * size;
	 */
	private long size;

	/**
	 * read;
	 */
	private long read;

	/**
	 * creatTime;
	 */
	private Date creatTime;
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getRead() {
		return read;
	}

	public void setRead(long read) {
		this.read = read;
	}

	public String getUuid() {
		return uuid;
	}

	public String getFileName() {
		return fileName;
	}

	public long getSize() {
		return size;
	}

	public float getProgress() {
		return (float)read/(float)size;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	
}
