/*
 * 文件名：Credential.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月24日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月24日
 * @see Credential
 * @since
 */
public class CredentialCheckEntity {
    private String url;
    private CredentialKey key;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public CredentialKey getKey() {
        return key;
    }
    public void setKey(CredentialKey key) {
        this.key = key;
    }
}
