/*
 * 文件名：JobExecution.java
 * 版权：Copyright by www.huawei.com
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

public class JobExecution {
    private String namespace;
    private String jobId;
    private Integer seqNo;
    private Integer progress;
    private Integer endStatus;
    private Integer finished;
    private Integer cancelled;
    private Integer startTime;
    private Integer endTime;
    
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public Integer getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    public Integer getProgress() {
        return progress;
    }
    public void setProgress(Integer progress) {
        this.progress = progress;
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
    public Integer getCancelled() {
        return cancelled;
    }
    public void setCancelled(Integer cancelled) {
        this.cancelled = cancelled;
    }
    public Integer getStartTime() {
        return startTime;
    }
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }
    public Integer getEndTime() {
        return endTime;
    }
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
