package com.bonc.epm.paas.util;
/**
 * 
 * 处理service抛出运行时异常处理
 * @author lance
 * @version 2016年8月31日
 * @see ServiceException
 * @since
 */
public class ServiceException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1389958090308317369L;

    public ServiceException() {
		super();
    }

    public ServiceException(String msg, Throwable clause) {
		super(msg, clause);
    }

    public ServiceException(String msg) {
		super(msg);
    }

    public ServiceException(Throwable clause) {
		super(clause);
    }
	
}
