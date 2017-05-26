/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:Event.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年5月25日下午4:38:45
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

/**
 * ClassName:Event <br/>
 * Date: 2017年5月25日 下午4:38:45 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class Event extends AbstractKubernetesModel {

	private Integer count;
	private String message;
	private String reason;
	private String type;
	private String firstTimestamp;
	private String lastTimestamp;
	private ObjectReference involvedObject;
	private EventSource source;

	public Event() {
		super(Kind.EVENT);
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFirstTimestamp() {
		return firstTimestamp;
	}

	public void setFirstTimestamp(String firstTimestamp) {
		this.firstTimestamp = firstTimestamp;
	}

	public String getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(String lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public ObjectReference getInvolvedObject() {
		return involvedObject;
	}

	public void setInvolvedObject(ObjectReference involvedObject) {
		this.involvedObject = involvedObject;
	}

	public EventSource getSource() {
		return source;
	}

	public void setSource(EventSource source) {
		this.source = source;
	}

}
