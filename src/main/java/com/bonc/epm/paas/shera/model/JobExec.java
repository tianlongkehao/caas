/*
 * 文件名：JobExec.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see JobExec
 * @since
 */
public class JobExec {
    private Integer Status;
    private String JobId;
    private Long LastSuccessTime;
    private Long LastFailureTime;
    private Long LastDuration;
    public Integer getStatus() {
        return Status;
    }
    public void setStatus(Integer status) {
        Status = status;
    }
    public String getJobId() {
        return JobId;
    }
    public void setJobId(String jobId) {
        JobId = jobId;
    }
    public Long getLastSuccessTime() {
        return LastSuccessTime;
    }
    public void setLastSuccessTime(Long lastSuccessTime) {
        LastSuccessTime = lastSuccessTime;
    }
    public Long getLastFailureTime() {
        return LastFailureTime;
    }
    public void setLastFailureTime(Long lastFailureTime) {
        LastFailureTime = lastFailureTime;
    }
    public Long getLastDuration() {
        return LastDuration;
    }
    public void setLastDuration(Long lastDuration) {
        LastDuration = lastDuration;
    }
}
