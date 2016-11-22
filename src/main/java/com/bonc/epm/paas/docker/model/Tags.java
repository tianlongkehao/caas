/*
 * 文件名：TagList.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.model;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年11月16日
 * @see Tags
 * @since
 */

public class Tags {
    private String name;
    private List<String> tags;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
