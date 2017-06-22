/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:LabelSelectorRequirement.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月22日上午11:37:34
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * ClassName:LabelSelectorRequirement <br/>
 * Date:     2017年6月22日 上午11:37:34 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
public class LabelSelectorRequirement {
	private String key;
	private String operator;
	private List<String> values;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
}
