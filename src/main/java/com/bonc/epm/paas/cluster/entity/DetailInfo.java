/*
 * 文件名：DetailInfo.java
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
 * @see DetailInfo
 * @since
 */
public class DetailInfo {
    private String legendName;
    private List<String> yAxis;
    public String getLegendName() {
        return legendName;
    }
    public void setLegendName(String legendName) {
        this.legendName = legendName;
    }
    public List<String> getyAxis() {
        return yAxis;
    }
    public void setyAxis(List<String> yAxis) {
        this.yAxis = yAxis;
    }
}
