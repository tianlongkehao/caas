/*
 * 文件名：CredentialKey.java
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
 * @see CredentialKey
 * @since
 */
public class CredentialKey {
    private String username;
    private Integer type;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
}
