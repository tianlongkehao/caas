/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:KubeinstallController.java
 * Package Name:com.bonc.epm.paas.controller
 * Date:2017年7月5日下午4:51:45
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.kubeinstall.api.KubeinstallApiClient;
import com.bonc.epm.paas.kubeinstall.exceptions.KubeinstallClientException;
import com.bonc.epm.paas.kubeinstall.model.DockerConf;
import com.bonc.epm.paas.kubeinstall.model.EtcdConf;
import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.LoginInfo;
import com.bonc.epm.paas.kubeinstall.model.MasterConf;
import com.bonc.epm.paas.kubeinstall.model.NodesConf;
import com.bonc.epm.paas.kubeinstall.model.Response;
import com.bonc.epm.paas.kubeinstall.model.YUMConf;
import com.bonc.epm.paas.kubeinstall.util.KubeinstallClientService;

/**
 * ClassName: KubeinstallController <br/>
 * date: 2017年7月5日 下午4:51:45 <br/>
 *
 * @author longkaixiang
 * @version
 */
@Controller
@RequestMapping(value = "/KubeinstallController")
public class KubeinstallController {

	@Autowired
	KubeinstallClientService kubeinstallClientService;

	/**
	 * step1CheckInstallPlan:第一步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step1CheckInstallPlan", method=RequestMethod.POST)
	@ResponseBody
	public String step1CheckInstallPlan(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step1CheckInstallPlan(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * step2CreateYUMStorage:第二步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step2CreateYUMStorage", method=RequestMethod.POST)
	@ResponseBody
	public String step2CreateYUMStorage(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step2CreateYUMStorage(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * step3InstallRPMs:第三步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step3InstallRPMs", method=RequestMethod.POST)
	@ResponseBody
	public String step3InstallRPMs(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step3InstallRPMs(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * step4CreateDockerRegistry:第四步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step4CreateDockerRegistry", method=RequestMethod.POST)
	@ResponseBody
	public String step4CreateDockerRegistry(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step4CreateDockerRegistry(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * step5InstallEtcd:第五步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step5InstallEtcd", method=RequestMethod.POST)
	@ResponseBody
	public String step5InstallEtcd(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step5InstallEtcd(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * step6MasterInit:第六步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step6MasterInit", method=RequestMethod.POST)
	@ResponseBody
	public String step6MasterInit(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step6MasterInit(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * step7NodesJoin:第 七步. <br/>
	 *
	 * @param machineSSHSetString
	 * @param etcdCfgString
	 * @param masterCfgString
	 * @param dockerCfgString
	 * @param yumCfgString
	 * @param nodesCfgString
	 * @return String
	 */
	@RequestMapping(value="/step7NodesJoin", method=RequestMethod.POST)
	@ResponseBody
	public String step7NodesJoin(String machineSSHSetString, String etcdCfgString,
			String masterCfgString, String dockerCfgString, String yumCfgString, String nodesCfgString) {
		Map<String, Object> map = new HashMap<>();
		KubeinstallApiClient client = kubeinstallClientService.getClient();
		InstallPlan installPlan = generateParameter(machineSSHSetString, etcdCfgString, masterCfgString,
				dockerCfgString, yumCfgString, nodesCfgString);
		try {
			Response response = client.step7NodesJoin(installPlan);
			map.put("status", "200");
			map.put("response", response);
		} catch (KubeinstallClientException e) {
			map.put("status", "300");
			map.put("response", e.getResponse());
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	private InstallPlan generateParameter(String machineSSHSetString, String etcdCfgString, String masterCfgString,
			String dockerCfgString, String yumCfgString, String nodesCfgString) {
		// 配置machineSSHSet
		Map<String, LoginInfo> machineSSHSet = new HashMap<>();
		JSONObject machineSSHSetMap = JSONObject.parseObject(machineSSHSetString);
		Iterator<Entry<String, Object>> iterator = machineSSHSetMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> next = iterator.next();
			machineSSHSet.put(next.getKey(), JSONObject.parseObject(next.getValue().toString(), LoginInfo.class));
		}
		// 配置etcdCfg
		EtcdConf etcdCfg = null;
		if (etcdCfgString != null) {
			etcdCfg = JSONObject.parseObject(etcdCfgString, EtcdConf.class);
		}
		// 配置masterCfg
		MasterConf masterCfg = null;
		if (masterCfgString != null) {
			masterCfg = JSONObject.parseObject(masterCfgString, MasterConf.class);
		}
		// 配置dockerCfg
		DockerConf dockerCfg = null;
		if (dockerCfgString != null) {
			dockerCfg = JSONObject.parseObject(dockerCfgString, DockerConf.class);
		}
		// 配置yumCfg
		YUMConf yumCfg = null;
		if (yumCfgString != null) {
			yumCfg = JSONObject.parseObject(yumCfgString, YUMConf.class);
		}
		// 配置nodesCfg
		NodesConf nodesCfg = null;
		if (nodesCfgString != null) {
			nodesCfg = JSONObject.parseObject(nodesCfgString, NodesConf.class);
		}
		return kubeinstallClientService.generateInstallPlan(machineSSHSet, etcdCfg, masterCfg, dockerCfg, yumCfg,
				nodesCfg);
	}
}
