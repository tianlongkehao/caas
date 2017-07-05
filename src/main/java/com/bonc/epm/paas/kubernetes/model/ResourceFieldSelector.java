/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:ResourceFieldSelector.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月23日上午9:44:50
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;
/**
 * ClassName:ResourceFieldSelector <br/>
 * Date:     2017年6月23日 上午9:44:50 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
public class ResourceFieldSelector {
	private String containerName;
	private Quantity divisor;
	private String resource;
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public Quantity getDivisor() {
		return divisor;
	}
	public void setDivisor(Quantity divisor) {
		this.divisor = divisor;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
}
