package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.Kind;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;


/**
 * ServiceController
 * @author fengtao
 * @version 2016年9月7日
 * @see HorizontalPodAutoscalerController
 * @since
 */
@Controller
public class HorizontalPodAutoscalerController {

	/**
	 * serviceDao:service数据接口.
	 */
	@Autowired
	private ServiceDao serviceDao;

	/**
	 * kubernetesClientService:kubernetes服务接口.
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	private Logger LOG = LoggerFactory.getLogger(HorizontalPodAutoscalerController.class);

	/**
	 * createHPA:创建hpa自动扩容. <br/>
	 *
	 * @author longkaixiang
	 * @param ServiceName
	 * @param minReplicas
	 * @param maxReplicas
	 * @param targetCPUUtilizationPercentage
	 * @return String
	 */
	@RequestMapping(value = { "/services/{service}/hpa" }, method = RequestMethod.POST)
	@ResponseBody
	public String createHPA(@PathVariable("service") String ServiceName, Integer minReplicas, Integer maxReplicas,
			Integer targetCPUUtilizationPercentage) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();
		//判断服务是否存在
		List<Service> services = serviceDao.findByNameOf(user.getId(), ServiceName);
		if (services.size() != 1) {
			//找不到该服务
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		//创建hpa
		HorizontalPodAutoscaler hpa = kubernetesClientService.generateHorizontalPodAutoscaler(ServiceName, maxReplicas,
				minReplicas, Kind.REPLICATIONCONTROLLER, targetCPUUtilizationPercentage);
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();
		try {
			LOG.info("createHPA:[" + JSON.toJSONString(hpa) + "]");
			hpa = apisClient.createHorizontalPodAutoscaler(hpa);
		} catch (KubernetesClientException e) {
			map.put("status", "401");
			map.put("exception", e);
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		// 保存service
		Service service = services.get(0);
		service.setMinReplicas(minReplicas);
		service.setMaxReplicas(maxReplicas);
		service.setTargetCPUUtilizationPercentage(targetCPUUtilizationPercentage);
		serviceDao.save(service);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * replaceHPA:替换hpa. <br/>
	 *
	 * @author longkaixiang
	 * @param ServiceName
	 * @param minReplicas
	 * @param maxReplicas
	 * @param targetCPUUtilizationPercentage
	 * @return String
	 */
	@RequestMapping(value = { "/services/{service}/hpa" }, method = RequestMethod.PUT)
	@ResponseBody
	public String replaceHPA(String ServiceName, Integer minReplicas, Integer maxReplicas,
			Integer targetCPUUtilizationPercentage) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();
		//判断服务是否存在
		List<Service> services = serviceDao.findByNameOf(user.getId(), ServiceName);
		if (services.size() != 1) {
			//找不到该服务
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();
		//获取已存在的hpa
		HorizontalPodAutoscaler hpa = null;
		try {
			hpa = apisClient.getHorizontalPodAutoscaler(ServiceName);
		} catch (KubernetesClientException e1) {
			LOG.error(e1.getMessage());
		}
		//初始化新的hpa
		HorizontalPodAutoscaler newHPA = kubernetesClientService.generateHorizontalPodAutoscaler(ServiceName, maxReplicas,
				minReplicas, Kind.REPLICATIONCONTROLLER, targetCPUUtilizationPercentage);
		try {
			if(null == hpa){
				//不存在旧的hpa时，创建新的hpa
				LOG.info("createHPA:[" + JSON.toJSONString(newHPA) + "]");
				newHPA = apisClient.createHorizontalPodAutoscaler(newHPA);
			} else {
				//存在旧的hpa时，替换为新的hpa
				LOG.info("replaceHPA:[" + JSON.toJSONString(newHPA) + "]");
				newHPA = apisClient.replaceHorizontalPodAutoscaler(ServiceName, newHPA);
			}
		} catch (KubernetesClientException e) {
			map.put("status", "401");
			map.put("exception", e);
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		// 保存service
		Service service = services.get(0);
		service.setMinReplicas(minReplicas);
		service.setMaxReplicas(maxReplicas);
		service.setTargetCPUUtilizationPercentage(targetCPUUtilizationPercentage);
		serviceDao.save(service);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * deleteHPA:删除指定服务的hpa. <br/>
	 *
	 * @author longkaixiang
	 * @param ServiceName
	 * @return String
	 */
	@RequestMapping(value = { "/services/{service}/hpa" }, method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteHPA(String ServiceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();
		//判断服务是否存在
		List<Service> services = serviceDao.findByNameOf(user.getId(), ServiceName);
		if (services.size() != 1) {
			//找不到该服务
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		//删除hpa
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();
		try {
			LOG.info("deleteHPA:[" + ServiceName + "]");
			apisClient.deleteHorizontalPodAutoscaler(ServiceName);
		} catch (KubernetesClientException e) {
			map.put("status", "401");
			map.put("exception", e);
			e.printStackTrace();
			return JSON.toJSONString(map);
		}

		// 保存service
		Service service = services.get(0);
		service.setMinReplicas(0);
		service.setMaxReplicas(0);
		service.setTargetCPUUtilizationPercentage(0);
		serviceDao.save(service);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}
}
