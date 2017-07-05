/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:NodesConf.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月5日上午10:49:38
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.kubeinstall.model;

import java.util.Map;

/**
 * ClassName: NodesConf <br/>
 * date: 2017年7月5日 上午10:49:38 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class NodesConf extends KubeinstallAbstractModel {
	private Map<String, LoginInfo> nodesIPSet;

	public Map<String, LoginInfo> getNodesIPSet() {
		return nodesIPSet;
	}

	public void setNodesIPSet(Map<String, LoginInfo> nodesIPSet) {
		this.nodesIPSet = nodesIPSet;
	}
}
