/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:RedisController.java
 * Package Name:com.bonc.epm.paas.controller
 * Date:2017年6月21日下午4:03:17
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.model.ConfigMap;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;

/**
 * ClassName:RedisController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年6月21日 下午4:03:17 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
@Controller
@RequestMapping(value = "/RedisController")
public class RedisController {

	private static final String REDIS_CONF = "redis.conf";
	private static final String REDIS_CONF_FILE = "REDISCONF";

	@Autowired
	KubernetesClientService kubernetesClientService;

	private ConfigMap generateRedisConfigMap(String name) throws IOException {
		User user = CurrentUserUtils.getInstance().getUser();
		String namespace = user.getNamespace();
		Map<String, String> data = new HashMap<>();
		
		Map<String, String> replaceMap = new HashMap<>();
		//TODO 替换文字
		String redisConf = FileUtils.readFileByLines(REDIS_CONF_FILE, replaceMap);

		data.put(REDIS_CONF, redisConf);
		return kubernetesClientService.generateConfigMap(name, namespace, data);
	}
}
