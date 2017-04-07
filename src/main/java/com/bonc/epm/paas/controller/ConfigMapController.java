package com.bonc.epm.paas.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ConfigmapDao;
import com.bonc.epm.paas.dao.ConfigmapDataDao;
import com.bonc.epm.paas.dao.ServiceConfigmapDao;
import com.bonc.epm.paas.entity.Configmap;
import com.bonc.epm.paas.entity.ConfigmapData;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.ConfigMap;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
@RequestMapping(value = "/configmap")
public class ConfigMapController {

	/**
	 * ServiceController日志实例
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ServiceController.class);

	/**
	 * KubernetesClientService接口
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	/**
	 * 服务与配置文件接口
	 */
	@Autowired
	private ServiceConfigmapDao serviceConfigmapDao;

	/**
	 * 配置文件接口
	 */
	@Autowired
	private ConfigmapDao configmapDao;

	/**
	 * 配置文件内容接口
	 */
	@Autowired
	private ConfigmapDataDao configmapDataDao;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getConfigmapList(Model model) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		List<Configmap> configmaps = configmapDao.findByCreateBy(cUser.getId());

		model.addAttribute("configmapList",configmaps);
		model.addAttribute("menu_flag", "template");
		model.addAttribute("li_flag", "configmap");
		//return "template/env-temp.jsp";
		return "configmap/configmap.jsp";
	}

	/**
	 * 创建ConfigMap
	 *
	 * @param configMapName
	 *            名称
	 * @param keyValues
	 *            键值对
	 * @return String
	 */
	@RequestMapping("/createOrUpdateConfigMap")
	@ResponseBody
	public String createOrUpdateConfigMap(String configmapId, String configMapName, Map<String, String> configMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();

		ConfigMap configmap = kubernetesClientService.generateConfigMap(configMapName, configMap);
		try {
			if (client.getConfigMap(configMapName) != null) {// 已经存在，则更新
				client.updateConfigMap(configMapName, configmap);
				updateConfigmap(configmapId, configmap);
			} else {// 没有，则新增
				client.createConfigMap(configmap);
				addConfigmap(configmap);
			}
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("create configMap error:" + e.getStatus().getMessage());
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	private void addConfigmap(ConfigMap configMap) {
		Configmap mapEntity = new Configmap();
		mapEntity.setName(configMap.getMetadata().getName());
		mapEntity.setCreateDate(new Date());
		mapEntity.setUpdateDate(new Date());
		mapEntity.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
		mapEntity.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());

		Configmap savedEntity = configmapDao.save(mapEntity);
		Map<String, String> data = configMap.getData();
		Iterator<String> iterator = data.keySet().iterator();

		while (iterator.hasNext()) {
			ConfigmapData configmapData = new ConfigmapData();
			configmapData.setConfigmapId(savedEntity.getId());
			configmapData.setCreateDate(new Date());
			configmapData.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());
			configmapData.setKey(iterator.next());
			configmapData.setValue(data.get(iterator.next()));
			configmapDataDao.save(configmapData);
		}
	}

	private void updateConfigmap(String configmapId, ConfigMap configMap) {
		Configmap configmap = configmapDao.findOne(Long.parseLong(configmapId));
		configmap.setUpdateDate(new Date());
		configmapDao.save(configmap);

		configmapDataDao.deleteByConfigmapId(Long.parseLong(configmapId));

		Map<String, String> data = configMap.getData();
		Iterator<String> iterator = data.keySet().iterator();

		while (iterator.hasNext()) {
			ConfigmapData configmapData = new ConfigmapData();
			configmapData.setConfigmapId(Long.parseLong(configmapId));
			configmapData.setCreateDate(new Date());
			configmapData.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());
			configmapData.setKey(iterator.next());
			configmapData.setValue(data.get(iterator.next()));
			configmapDataDao.save(configmapData);
		}
	}

	/**
	 * 检验用户输入的ConfigMap名称是否重复
	 *
	 * @param configMapName
	 * @return String
	 */
	@RequestMapping("/matchConfigMapName")
	@ResponseBody
	public String matchConfigMapName(String configMapName) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		ConfigMap configMap = client.getConfigMap(configMapName);

		if (configMap != null) {
			map.put("status", "500");
			return JSON.toJSONString(map);//已存在
		}

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除ConfigMap
	 *
	 * @param configMapName
	 * @return String
	 */
	@RequestMapping("/deleteConfigMap")
	@ResponseBody
	public String deleteConfigMap(String configmapId, String configMapName) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			client.deleteConfigMap(configMapName);
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("delete configMap error:" + e.getStatus().getMessage());
			return JSON.toJSONString(map);
		}

		configmapDao.deleteByName(configMapName);// 从数据库删除所有引用此ConfigMap的记录
		configmapDataDao.deleteByConfigmapId(Long.parseLong(configmapId));
		serviceConfigmapDao.deleteByConfigmapId(Long.parseLong(configmapId));

		map.put("status", "200");
		return JSON.toJSONString(map);
	}
}
