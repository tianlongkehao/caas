/*
 * 文件名：DataResource.java
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
 * @see DataResource
 * @since
 */
public class CatalogResource {
    private String name;
    private List<DetailResource> val;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<DetailResource> getVal() {
        return val;
    }
    public void setVal(List<DetailResource> val) {
        this.val = val;
    }
}
