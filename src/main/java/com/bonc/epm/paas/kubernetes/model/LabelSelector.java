/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:LabelSelector.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月22日上午11:26:13
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

import java.util.List;
import java.util.Map;

/**
 * ClassName: LabelSelector <br/>
 * date: 2017年6月22日 上午11:26:13 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class LabelSelector {
	private List<LabelSelectorRequirement> matchExpressions;

	private Map<String, String> matchLabels;

	public List<LabelSelectorRequirement> getMatchExpressions() {
		return matchExpressions;
	}

	public void setMatchExpressions(List<LabelSelectorRequirement> matchExpressions) {
		this.matchExpressions = matchExpressions;
	}

	public Map<String, String> getMatchLabels() {
		return matchLabels;
	}

	public void setMatchLabels(Map<String, String> matchLabels) {
		this.matchLabels = matchLabels;
	}
}
