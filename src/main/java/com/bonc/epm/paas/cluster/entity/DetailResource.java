/*
 * 文件名：DetailResource.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年12月7日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.cluster.entity;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年12月7日
 * @see DetailResource
 * @since
 */
public class DetailResource {
    private String titleText;
    private List<Collectivity> val;
    public String getTitleText() {
        return titleText;
    }
    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
    public List<Collectivity> getVal() {
        return val;
    }
    public void setVal(List<Collectivity> val) {
        this.val = val;
    }
}
