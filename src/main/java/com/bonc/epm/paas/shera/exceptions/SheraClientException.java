/*
 * 文件名：SheraClientException.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.exceptions;

import javax.ws.rs.WebApplicationException;

import com.bonc.epm.paas.kubernetes.exceptions.Status;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see SheraClientException
 * @since
 */

public class SheraClientException extends RuntimeException {
    
    private static final long serialVersionUID = -7521673271244696905L;

    private Status status;

    public SheraClientException(String message,Exception exception) {
        super(message,exception);
        this.setStatus(getResponse(exception));
    }

    public SheraClientException(String msg, Status status) {
        super(msg);
        this.status = status;
    }
    
    public SheraClientException(Exception exception) {
        this(buildMessage(exception),exception);
    }
    
    public SheraClientException(String msg) {
        super(msg);
    }

    private static String buildMessage(Exception exception) {
        Status response = getResponse(exception);
        return response != null ? response.getMessage() : null;
    }
    
    private static Status getResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException error = (WebApplicationException) exception;
            return error.getResponse().readEntity(Status.class);
        }
        return null;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
