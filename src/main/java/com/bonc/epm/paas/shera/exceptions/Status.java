/*
 * 文件名：Status.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.exceptions;

import com.bonc.epm.paas.shera.model.AbstractSheRaModel;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see Status
 * @since
 */
public class Status extends AbstractSheRaModel {
    
/*    protected Status() {
        super(Kind.STATUS);
    }*/
    private String status, message, reason;
    private int code;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}
