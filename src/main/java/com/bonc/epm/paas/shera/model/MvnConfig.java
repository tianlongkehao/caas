/*
 * 文件名：MvnConfig.java
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
 * @author ke_wang
 * @version 2016年11月10日
 * @see MvnConfig
 * @since
 */
public class MvnConfig {
    private String version;
    private String goals;
    private String pom;
    private String properties;
    private String jvmopts;
    private String settingFile;
    private String globalSettingFile;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getGoals() {
        return goals;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }
    public String getPom() {
        return pom;
    }
    public void setPom(String pom) {
        this.pom = pom;
    }
    public String getProperties() {
        return properties;
    }
    public void setProperties(String properties) {
        this.properties = properties;
    }
    public String getJvmopts() {
        return jvmopts;
    }
    public void setJvmopts(String jvmopts) {
        this.jvmopts = jvmopts;
    }
    public String getSettingFile() {
        return settingFile;
    }
    public void setSettingFile(String settingFile) {
        this.settingFile = settingFile;
    }
    public String getGlobalSettingFile() {
        return globalSettingFile;
    }
    public void setGlobalSettingFile(String globalSettingFile) {
        this.globalSettingFile = globalSettingFile;
    }
}
