/*
 * 文件名：Log.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月21日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月21日
 * @see Log
 * @since
 */

public class Log {
    private String content;
    private boolean flag;
    private Long seek;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public Long getSeek() {
        return seek;
    }
    public void setSeek(Long seek) {
        this.seek = seek;
    }
}
