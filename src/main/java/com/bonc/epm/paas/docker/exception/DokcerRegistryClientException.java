package com.bonc.epm.paas.docker.exception;

public class DokcerRegistryClientException extends Exception {
	
	private static final long serialVersionUID = -9136787747562661946L;

	public DokcerRegistryClientException(String message, Exception exception) {
	        super(message, exception);
	    }
	    
	    public DokcerRegistryClientException(Exception exception) {
	        super(exception);
	    }

	    public DokcerRegistryClientException(String msg) {
	        super(msg);
	    }
}
