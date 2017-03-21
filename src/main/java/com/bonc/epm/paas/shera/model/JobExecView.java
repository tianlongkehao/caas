/*
 * 文件名：JobExecution.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月15日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月15日
 * @see JobExecution
 * @since
 */

public class JobExecView {
    private Integer seqNo;
    // 执行成功：0、执行失败：1；
    private Integer endStatus;
    //执行没有完成：0、执行完成：1；
    private Integer finished;
    private Long startTime;
    private Long endTime;
    private String imgVersion;
    private String projectKey;

    public Integer getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    public Integer getEndStatus() {
        return endStatus;
    }
    public void setEndStatus(Integer endStatus) {
        this.endStatus = endStatus;
    }
    public Integer getFinished() {
        return finished;
    }
    public void setFinished(Integer finished) {
        this.finished = finished;
    }
    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    public String getImgVersion() {
        return imgVersion;
    }
    public void setImgVersion(String imgVersion) {
        this.imgVersion = imgVersion;
    }
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

}
