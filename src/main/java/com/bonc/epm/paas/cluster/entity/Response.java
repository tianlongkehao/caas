package com.bonc.epm.paas.cluster.entity;

public class Response {

	private boolean result;
	private String outmsg;
	/*public boolean isResult() {
		return Result;
	}
	public void setResult(boolean result) {
		Result = result;
	}
	public String getOutMsg() {
		return OutMsg;
	}
	public void setOutMsg(String outMsg) {
		OutMsg = outMsg;
	}*/
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getOutmsg() {
		return outmsg;
	}
	public void setOutmsg(String outmsg) {
		this.outmsg = outmsg;
	}

}
