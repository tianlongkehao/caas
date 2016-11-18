/*
 * 文件名：CiInvoke.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年11月11日
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

import com.alibaba.fastjson.annotation.JSONField;
@Entity
public class CiInvoke {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    /**
     * 关联ci表
     */
    private long ciId;
    
    /**
     * 创建人
     */
    private long createBy;
    
    /**
     * 工作顺序id
     */
    private Integer jobOrderId;
    
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    
    /**
     * 构建时调动的方式：0:none、1:ant、2:maven、3：shell;
     */
    private Integer invokeType;
    
    /**
     * ant版本
     */
    private String antVersion;
    
    /**
     * ant构建目标
     */
    private String antTargets;
    
    /**
     * ant构建文件路径
     */
    private String antBuildFileLocation;
    
    /**
     * ant性能
     */
    private String antProperties;
    
    /**
     * ant java选择项
     */
    private String antJavaOpts;
    
    /**
     * maven版本
     */
    private String mavenVersion;
    
    /**
     * maven目标
     */
    private String mavenGoals;
    
    /**
     * maven中的pom文件路径
     */
    private String pomLocation;
    
    /**
     * jvm选项
     */
    private String mavenJVMOptions;
    
    /**
     * maven属性
     */
    private String mavenProperty;
    
    /**
     * 是否使用maven私有仓库，0：不使用、1：使用
     */
    private Integer isUserPrivateRegistry;
    
    /**
     * 是否注入建立变量， 0：不注入、1：注入
     */
    private Integer injectBuildVariables;
    
    /**
     * maven配置文件
     */
    private String mavenSetFile;
    
    /**
     * maven全局配置文件
     */
    private String mavenGlobalSetFile;
    
    /**
     * shell脚本命令
     */
    private String shellCommand;

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

    public long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }

    public Integer getJobOrderId() {
        return jobOrderId;
    }

    public void setJobOrderId(Integer jobOrderId) {
        this.jobOrderId = jobOrderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(Integer invokeType) {
        this.invokeType = invokeType;
    }

    public String getAntVersion() {
        return antVersion;
    }

    public void setAntVersion(String antVersion) {
        this.antVersion = antVersion;
    }

    public String getAntTargets() {
        return antTargets;
    }

    public void setAntTargets(String antTargets) {
        this.antTargets = antTargets;
    }

    public String getAntBuildFileLocation() {
        return antBuildFileLocation;
    }

    public void setAntBuildFileLocation(String antBuildFileLocation) {
        this.antBuildFileLocation = antBuildFileLocation;
    }

    public String getAntProperties() {
        return antProperties;
    }

    public void setAntProperties(String antProperties) {
        this.antProperties = antProperties;
    }

    public String getAntJavaOpts() {
        return antJavaOpts;
    }

    public void setAntJavaOpts(String antJavaOpts) {
        this.antJavaOpts = antJavaOpts;
    }

    public String getMavenVersion() {
        return mavenVersion;
    }

    public void setMavenVersion(String mavenVersion) {
        this.mavenVersion = mavenVersion;
    }

    public String getMavenGoals() {
        return mavenGoals;
    }

    public void setMavenGoals(String mavenGoals) {
        this.mavenGoals = mavenGoals;
    }

    public String getPomLocation() {
        return pomLocation;
    }

    public void setPomLocation(String pomLocation) {
        this.pomLocation = pomLocation;
    }

    public String getMavenJVMOptions() {
        return mavenJVMOptions;
    }

    public void setMavenJVMOptions(String mavenJVMOptions) {
        this.mavenJVMOptions = mavenJVMOptions;
    }

    public String getMavenProperty() {
        return mavenProperty;
    }

    public void setMavenProperty(String mavenProperty) {
        this.mavenProperty = mavenProperty;
    }

    public Integer getIsUserPrivateRegistry() {
        return isUserPrivateRegistry;
    }

    public void setIsUserPrivateRegistry(Integer isUserPrivateRegistry) {
        this.isUserPrivateRegistry = isUserPrivateRegistry;
    }

    public Integer getInjectBuildVariables() {
        return injectBuildVariables;
    }

    public void setInjectBuildVariables(Integer injectBuildVariables) {
        this.injectBuildVariables = injectBuildVariables;
    }

    public String getMavenSetFile() {
        return mavenSetFile;
    }

    public void setMavenSetFile(String mavenSetFile) {
        this.mavenSetFile = mavenSetFile;
    }

    public String getMavenGlobalSetFile() {
        return mavenGlobalSetFile;
    }

    public void setMavenGlobalSetFile(String mavenGlobalSetFile) {
        this.mavenGlobalSetFile = mavenGlobalSetFile;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public void setShellCommand(String shellCommand) {
        this.shellCommand = shellCommand;
    }
    
}
