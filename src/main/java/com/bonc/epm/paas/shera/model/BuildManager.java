/*
 * 文件名：BuildManager.java
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
 * @see BuildManager
 * @since
 */
public class BuildManager {
    private AntConfig antConfig;
    private MvnConfig mvnConfig;
    public AntConfig getAntConfig() {
        return antConfig;
    }
    public void setAntConfig(AntConfig antConfig) {
        this.antConfig = antConfig;
    }
    public MvnConfig getMvnConfig() {
        return mvnConfig;
    }
    public void setMvnConfig(MvnConfig mvnConfig) {
        this.mvnConfig = mvnConfig;
    }
}
