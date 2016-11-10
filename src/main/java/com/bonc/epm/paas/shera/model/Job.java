/*
 * 文件名：Job.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * 代码构建信息
 * @author ke_wang
 * @version 2016年11月10日
 * @see Job
 * @since
 */
public class Job {
    private String id;
    private String nextExecTime;
    private String jdkVersion;
    private String buildImgCmd;
    private String pushImgCmd;
    private Integer currentNumber;
    private Integer maxExecutionRecords;
    private Integer maxKeepDays;
    private Integer jobRemoved;
    private CodeManager codeManager;
    private BuildManager buildManager;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNextExecTime() {
        return nextExecTime;
    }
    public void setNextExecTime(String nextExecTime) {
        this.nextExecTime = nextExecTime;
    }
    public String getJdkVersion() {
        return jdkVersion;
    }
    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }
    public String getBuildImgCmd() {
        return buildImgCmd;
    }
    public void setBuildImgCmd(String buildImgCmd) {
        this.buildImgCmd = buildImgCmd;
    }
    public String getPushImgCmd() {
        return pushImgCmd;
    }
    public void setPushImgCmd(String pushImgCmd) {
        this.pushImgCmd = pushImgCmd;
    }
    public Integer getCurrentNumber() {
        return currentNumber;
    }
    public void setCurrentNumber(Integer currentNumber) {
        this.currentNumber = currentNumber;
    }
    public Integer getMaxExecutionRecords() {
        return maxExecutionRecords;
    }
    public void setMaxExecutionRecords(Integer maxExecutionRecords) {
        this.maxExecutionRecords = maxExecutionRecords;
    }
    public Integer getMaxKeepDays() {
        return maxKeepDays;
    }
    public void setMaxKeepDays(Integer maxKeepDays) {
        this.maxKeepDays = maxKeepDays;
    }
    public Integer getJobRemoved() {
        return jobRemoved;
    }
    public void setJobRemoved(Integer jobRemoved) {
        this.jobRemoved = jobRemoved;
    }
    public CodeManager getCodeManager() {
        return codeManager;
    }
    public void setCodeManager(CodeManager codeManager) {
        this.codeManager = codeManager;
    }
    public BuildManager getBuildManager() {
        return buildManager;
    }
    public void setBuildManager(BuildManager buildManager) {
        this.buildManager = buildManager;
    }
}
