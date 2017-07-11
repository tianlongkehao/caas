/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:Response.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月5日上午10:51:52
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName: Response <br/>
 * date: 2017年7月5日 上午10:51:52 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class Response {
	private Boolean result;
	private String errorMsg;
	private String output;
	private InstallPlan content;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public InstallPlan getContent() {
		return content;
	}

	public void setContent(InstallPlan content) {
		this.content = content;
	}
}
