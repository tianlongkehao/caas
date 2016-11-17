/*
 * 文件名：GitAdvancedConfig.java
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
 * @see GitAdvancedConfig
 * @since
 */

public class GitAdvancedConfig {
    private String name;
    private String refspec;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRefspec() {
        return refspec;
    }
    public void setRefspec(String refspec) {
        this.refspec = refspec;
    }
    
}
