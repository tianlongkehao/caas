/*
 * 文件名：HookAndImages.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月1日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 
 * 代码挂钩和镜像的一对多关联
 * @author zhoutao
 * @version 2016年12月1日
 * @see HookAndImages
 * @since
 */
@Entity
public class HookAndImages {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /**
     * hookid
     */
    private long hookId;
    
    /**
     * 镜像Id
     */
    private long imageId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHookId() {
        return hookId;
    }

    public void setHookId(long hookId) {
        this.hookId = hookId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
    
}
