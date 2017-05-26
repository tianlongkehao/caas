/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:EventList.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年5月25日下午4:36:14
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
package com.bonc.epm.paas.kubernetes.model;
/**
 * ClassName:EventList <br/>
 * Date:     2017年5月25日 下午4:36:14 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
public class EventList extends AbstractKubernetesModelList<Event> {

	private ListMeta metadata;

	public EventList() {
		super(Kind.EVENTLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}

}
