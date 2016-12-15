/*
 * 文件名：Collectivity.java
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
 * @see Collectivity
 * @since
 */
public class Collectivity {
    private String title;
    private List<DetailInfo> val;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<DetailInfo> getVal() {
        return val;
    }
    public void setVal(List<DetailInfo> val) {
        this.val = val;
    }
}
