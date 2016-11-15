/*
 * 文件名：Repository.java
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
 * @see Repository
 * @since
 */
public class Repository {
    private String url;
    private String credentials;
    private GitAdvancedConfig advanced;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCredentials() {
        return credentials;
    }
    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
    public GitAdvancedConfig getAdvanced() {
        return advanced;
    }
    public void setAdvanced(GitAdvancedConfig advanced) {
        this.advanced = advanced;
    }  
}
