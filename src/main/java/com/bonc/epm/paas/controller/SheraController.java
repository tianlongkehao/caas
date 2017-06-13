package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.SheraConstant;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.ExecConfig;
import com.bonc.epm.paas.shera.util.SheraClientService;

@Controller
public class SheraController {
	/**
	 * SheraController日志实例
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SheraController.class);

	/**
	 * sheraClientService:shera服务接口.
	 */
	@Autowired
	SheraClientService sheraClientService;

	/**
	 * getSheraMavenConfig:获取当前用户所用shera的maven配置. <br/>
	 *
	 * @author longkaixiang
	 * @return String
	 */
	@RequestMapping(value = { "shera/getSheraMavenConfig.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getSheraMavenConfig() {
		Map<String, Object> map = new HashMap<>();
		List<ExecConfig> execConfig;
		try {
			SheraAPIClientInterface client = sheraClientService.getClient();
			execConfig = client.getExecConfig(SheraConstant.EXEC_MAVEN_CONFIG);
		} catch (SheraClientException e) {
			map.put("status", "300");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			map.put("status", "300");
			LOG.error("获取ExecConfig异常");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		map.put("mavenList", execConfig);
		return JSON.toJSONString(map);
	}

	/**
	 * getSheraAntConfig:获取当前用户所用shera的maven配置. <br/>
	 *
	 * @author longkaixiang
	 * @return String
	 */
	@RequestMapping(value = { "shera/getSheraAntConfig.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getSheraAntConfig() {
		Map<String, Object> map = new HashMap<>();
		List<ExecConfig> execConfig;
		try {
			SheraAPIClientInterface client = sheraClientService.getClient();
			execConfig = client.getExecConfig(SheraConstant.EXEC_ANT_CONFIG);
		} catch (SheraClientException e) {
			map.put("status", "300");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			map.put("status", "300");
			LOG.error("获取ExecConfig异常");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		map.put("antList", execConfig);
		return JSON.toJSONString(map);
	}

	/**
	 * getSheraSonarConfig:获取当前用户所用shera的maven配置. <br/>
	 *
	 * @author longkaixiang
	 * @return String
	 */
	@RequestMapping(value = { "shera/getSheraSonarConfig.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getSheraSonarConfig() {
		Map<String, Object> map = new HashMap<>();
		List<ExecConfig> execConfig;
		try {
			SheraAPIClientInterface client = sheraClientService.getClient();
			execConfig = client.getExecConfig(SheraConstant.EXEC_SONAR_CONFIG);
		} catch (SheraClientException e) {
			map.put("status", "300");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			map.put("status", "300");
			LOG.error("获取ExecConfig异常");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		map.put("sonarList", execConfig);
		return JSON.toJSONString(map);
	}

	/**
	 * deleteExecConfig:删除ExecConfig. <br/>
	 *
	 * @author longkaixiang
	 * @param key
	 * @return String
	 */
	@RequestMapping(value = { "shera/deleteExecConfig.do" }, method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteExecConfig(String key) {
		Map<String, Object> map = new HashMap<>();
		try {
			SheraAPIClientInterface client = sheraClientService.getClient();
			client.deleteExecConfig(key);
		} catch (SheraClientException e) {
			map.put("status", "300");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			map.put("status", "300");
			LOG.error("删除ExecConfig异常");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * modifyExecConfig:modifyExecConfig. <br/>
	 *
	 * @author longkaixiang
	 * @param key
	 * @param execConfig
	 * @return String
	 */
	@RequestMapping(value = { "shera/modifyExecConfig.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String modifyExecConfig(String key, ExecConfig execConfig) {
		Map<String, Object> map = new HashMap<>();
		try {
			SheraAPIClientInterface client = sheraClientService.getClient();
			client.deleteExecConfig(key);
			execConfig = client.createExecConfig(execConfig);

		} catch (SheraClientException e) {
			map.put("status", "300");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			map.put("status", "300");
			LOG.error("更新ExecConfig异常");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		map.put("execConfig", execConfig);
		return JSON.toJSONString(map);
	}

}
