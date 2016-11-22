package com.bonc.epm.paas.docker.exception;

import javax.ws.rs.WebApplicationException;

public class DokcerRegistryClientException extends RuntimeException {
	
	private static final long serialVersionUID = -9136787747562661946L;

	private ErrorList errorList;
	
	public DokcerRegistryClientException(String message, Exception exception) {
	        super(message, exception);
	        this.setErrorList(getResponse(exception));
	}

    public DokcerRegistryClientException(String msg, ErrorList errorList) {
        super(msg);
        this.setErrorList(errorList);
    }

    public DokcerRegistryClientException(Exception exception) {
        this(buildMessage(exception), exception);
    }
        
    private static ErrorList getResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException error = (WebApplicationException) exception;
            return error.getResponse().readEntity(ErrorList.class);
        }
        return null;
    }

    private static String buildMessage(Exception exception) {
        ErrorList response = getResponse(exception);
        return response != null ? response.toString() : null;
    }
    
    public ErrorList getErrorList() {
        return errorList;
    }

    public void setErrorList(ErrorList errorList) {
        this.errorList = errorList;
    }
}
