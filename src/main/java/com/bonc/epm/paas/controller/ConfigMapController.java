package com.bonc.epm.paas.controller;

import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.ConfigmapDao;
import com.bonc.epm.paas.dao.ConfigmapDataDao;
import com.bonc.epm.paas.dao.ServiceConfigmapDao;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.CommonOprationLogUtils;
import com.bonc.epm.paas.entity.Configmap;
import com.bonc.epm.paas.entity.ConfigmapData;
import com.bonc.epm.paas.entity.ConfigmapForm;
import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.KeyValue;
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

	@Autowired
	private CommonOperationLogDao commonOperationLogDao;

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

		model.addAttribute("configmapList", configmaps);
		model.addAttribute("menu_flag", "template");
		model.addAttribute("li_flag", "configmap");
		return "template/configmap.jsp";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String envAdd(Model model) {
		model.addAttribute("menu_flag", "template");
		model.addAttribute("li_flag", "configmap");
		return "template/configmap-add.jsp";
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
	public String createOrUpdateConfigMap(ConfigmapForm form) {
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();

		if (form == null) {
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		String configmapId = form.getConfigmapId();
		String configMapName = form.getConfigMapName();
		List<KeyValue> list = form.getKeyValues();

		if (StringUtils.isEmpty(configMapName)) {
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		ConfigMap configmap = kubernetesClientService.generateConfigMap(configMapName, list);

		try {
			if (!StringUtils.isEmpty(configmapId)) {
				// if (client.getConfigMap(configMapName) != null) {// 已经存在，则更新
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
		} catch (SerialException e) {
			e.printStackTrace();
			map.put("status", "500");
			LOG.error("Fail to update the configmapData table!" );
			return JSON.toJSONString(map);
		} catch (SQLException e) {
			e.printStackTrace();
			map.put("status", "500");
			LOG.error("Fail to update the configmapData table!" );
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	private void addConfigmap(ConfigMap configMap) throws SerialException, SQLException {
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
			String key = iterator.next();
			ConfigmapData configmapData = new ConfigmapData();
			configmapData.setConfigmapId(savedEntity.getId());
			configmapData.setCreateDate(new Date());
			configmapData.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());
			configmapData.setName(key);
			Clob clob = new SerialClob(data.get(key).toCharArray());
			configmapData.setContent(clob);
			configmapDataDao.save(configmapData);
		}
	}

	private void updateConfigmap(String configmapId, ConfigMap configMap) throws SerialException, SQLException {
		Configmap configmap = configmapDao.findOne(Long.parseLong(configmapId));
		configmap.setUpdateDate(new Date());
		configmapDao.save(configmap);

		configmapDataDao.deleteByConfigmapId(Long.parseLong(configmapId));

		Map<String, String> data = configMap.getData();
		Iterator<String> iterator = data.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			ConfigmapData configmapData = new ConfigmapData();
			configmapData.setConfigmapId(Long.parseLong(configmapId));
			configmapData.setCreateDate(new Date());
			configmapData.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());
			configmapData.setName(key);
			Clob clob = new SerialClob(data.get(key).toCharArray());
			configmapData.setContent(clob);
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
		List<Configmap> list = configmapDao
				.findByNamespaceAndName(CurrentUserUtils.getInstance().getUser().getNamespace(), configMapName);
		// if (configMap != null) {
		if (!CollectionUtils.isEmpty(list)) {
			map.put("status", "500");
			return JSON.toJSONString(map);// 已存在
		}

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除单个ConfigMap
	 *
	 * @param configMapName
	 * @return String
	 */
	@RequestMapping("/deleteConfigMap")
	@ResponseBody
	public String deleteConfigMap(String configmapId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Configmap configmap;
		try {
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			configmap = configmapDao.findOne(Long.parseLong(configmapId));
			client.deleteConfigMap(configmap.getName());
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("delete configMap error:" + e.getStatus().getMessage());
			return JSON.toJSONString(map);
		}

		configmapDao.deleteById(Long.parseLong(configmapId));// 从数据库删除所有引用此ConfigMap的记录
		configmapDataDao.deleteByConfigmapId(Long.parseLong(configmapId));
		serviceConfigmapDao.deleteByConfigmapId(Long.parseLong(configmapId));

		String extraInfo = "已删除configmap:" + configmap.getName();
		// 记录用户删除环境变量模板操作
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(configmap.getName(), extraInfo,
				CommConstant.ENV_TEMPLATE, CommConstant.OPERATION_TYPE_DELETE);
		commonOperationLogDao.save(log);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除多个configmap
	 *
	 * @return
	 */
	@RequestMapping("/delconfigmaps.do")
	@ResponseBody
	public String deleteConfigmaps(String ids) {
		// 解析获取的id List
		String[] str = ids.split(",");
		if (str != null && str.length > 0) {
			for (String id : str) {
				deleteConfigMap(id);
			}
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("status", "200");
		return JSON.toJSONString(maps);
	}

	/**
     *
     * @param model
     * @param configmapId 配置文件模板id
     * @return String
     */
	@RequestMapping(value = { "/configmapData/{configmapId}" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable String configmapId){
		Configmap configmap = configmapDao.findOne(Long.parseLong(configmapId));
		List<ConfigmapData> configmapDataList = configmapDataDao.findByConfigmapId(Long.parseLong(configmapId));
		List<KeyValue> keyValues = new ArrayList<KeyValue>();
		Reader reader = null;
		char[] tempDoc;
		try {
			for(ConfigmapData data : configmapDataList){
				KeyValue keyValue = new KeyValue();
				keyValue.setKey(data.getName());

				Clob clob = data.getContent();
				reader = clob.getCharacterStream();
				tempDoc = new char[(int) clob.length()];
				reader.read(tempDoc);

				keyValue.setValue(new String(tempDoc));
				keyValues.add(keyValue);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("configmapDataList",keyValues);
		model.addAttribute("configmapId", configmapId);
		model.addAttribute("configMapName", configmap.getName());
		model.addAttribute("menu_flag", "template");
		model.addAttribute("li_flag", "configmap");
		return "template/configmap-edit.jsp";
	}
}
