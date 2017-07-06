package com.bonc.epm.paas.kubeinstall.exceptions;

import com.bonc.epm.paas.kubeinstall.model.Response;

/**
 * ClassName: KubeinstallClientException <br/>
 * date: 2017年7月5日 下午5:23:53 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class KubeinstallClientException extends RuntimeException {

	private static final long serialVersionUID = -7521673271244696904L;

	private Response response;

	/**
	 * Creates a new instance of KubeinstallClientException.
	 *
	 */
	public KubeinstallClientException(Response response) {
		super(response.getErrorMsg());
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
