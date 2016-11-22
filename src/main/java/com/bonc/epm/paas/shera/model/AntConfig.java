/*
 * 文件名：AntConfig.java
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
 * @see AntConfig
 * @since
 */
public class AntConfig {
    private String version;
    private String targets;
    private String buildFile;
    private String properties;
    private String javaopts;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getTargets() {
        return targets;
    }
    public void setTargets(String targets) {
        this.targets = targets;
    }
    public String getBuildFile() {
        return buildFile;
    }
    public void setBuildFile(String buildFile) {
        this.buildFile = buildFile;
    }
    public String getProperties() {
        return properties;
    }
    public void setProperties(String properties) {
        this.properties = properties;
    }
    public String getJavaopts() {
        return javaopts;
    }
    public void setJavaopts(String javaopts) {
        this.javaopts = javaopts;
    }
}
