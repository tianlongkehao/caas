/*
 * 文件名：ClusterResources.java
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
 * @see ClusterResources
 * @since
 */
public class ClusterResources {
    List<String> xValue;
    List<CatalogResource> yValue;
    public List<String> getxValue() {
        return xValue;
    }
    public void setxValue(List<String> xValue) {
        this.xValue = xValue;
    }
    public List<CatalogResource> getyValue() {
        return yValue;
    }
    public void setyValue(List<CatalogResource> yValue) {
        this.yValue = yValue;
    }
}
