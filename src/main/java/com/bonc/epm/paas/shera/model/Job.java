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
    private String jdkVersion;
    private Integer maxExecutionRecords;
    private Integer maxKeepDays;
    private CodeManager codeManager;
    private BuildManager buildManager;
    private ImgManager imgManager;
    
    public ImgManager getImgManager() {
        return imgManager;
    }
    public void setImgManager(ImgManager imgManager) {
        this.imgManager = imgManager;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getJdkVersion() {
        return jdkVersion;
    }
    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
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
