package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.TenantResource;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaSpec;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;

@Controller
public class ResourceController {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);

	@Value("${ratio.limittorequestcpu}")
	private int RATIO_LIMITTOREQUESTCPU;

	@Value("${ratio.limittorequestmemory}")
	private int RATIO_LIMITTOREQUESTMEMORY;

	/*
	 * 预留的cpu资源
	 */
	@Value("${rest.resource.cpu}")
	private int REST_RESOURCE_CPU;

	/*
	 * 预留的memory资源
	 */
	@Value("${rest.resource.memory}")
	private int REST_RESOURCE_MEMORY;

	/**
	 * 服务数据层接口
	 */
	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserResourceDao userResourceDao;
	/**
	 * KubernetesClientService接口
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	/**
	 * 更新Rc中的每个container的request资源，按系数调整
	 *
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = { "quota/updateRc.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateRC(long tenantId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		// 获取租户下的所有用户
		List<User> users = userDao.getByParentId(tenantId);
		User tenant = userDao.findById(tenantId);
		// 获取指定租户的client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(tenant.getNamespace());
		users.add(tenant);
		for (User user : users) {
			List<Service> services = serviceDao.findByCreateBy(user.getId());
			for (Service service : services) {
				double cpu = service.getCpuNum();
				String mem = service.getRam();
				// 只对运行中的服务进行调整
				if (service.getStatus() == 3) {
					ReplicationController replicationController = client
							.getReplicationController(service.getServiceName());
					List<Container> containers = replicationController.getSpec().getTemplate().getSpec()
							.getContainers();
					for (Container container : containers) {
						container.getResources().getRequests().put("cpu", cpu / RATIO_LIMITTOREQUESTCPU);
						container.getResources().getRequests().put("memory",
								Double.parseDouble(mem) / RATIO_LIMITTOREQUESTMEMORY + "Mi");
					}
					client.updateReplicationController(service.getServiceName(), replicationController);
				}
			}
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 删除租户下的所有Pod，Rc会自动重新创建
	 *
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = { "quota/deletePods.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String deleteRunningPod(long tenantId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		// 获取租户下的所有用户
		User tenant = userDao.findById(tenantId);
		String namespace = tenant.getNamespace();
		// 获取指定租户的client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
		List<Pod> pods = client.getAllPods().getItems();
		// 删除租户下的所有pod
		for (Pod pod : pods) {
			client.deletePodOfNamespace(namespace, pod.getMetadata().getName());
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 更改租户分配的quota
	 *
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = { "quota/updateQuota.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateQuota(long tenantId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		User tenant = userDao.findById(tenantId);
		String namespace = tenant.getNamespace();
		// 获取指定租户的client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
		UserResource userResource = userResourceDao.findByUserId(tenantId);
		double cpu = userResource.getCpu();
		long mem = userResource.getMemory();

		// 更改quota
		ResourceQuota quota = client.getResourceQuota(namespace);
		ResourceQuotaSpec spec = quota.getSpec();
		Map<String, String> hard = quota.getSpec().getHard();

		cpu = (cpu + REST_RESOURCE_CPU) / RATIO_LIMITTOREQUESTCPU;
		double tempMem = 1.0d*(mem + REST_RESOURCE_MEMORY) / RATIO_LIMITTOREQUESTMEMORY;

		// 资源按照系数调整
		hard.put("cpu", cpu + "");// CPU数量
		hard.put("memory", tempMem + "G"); // 内存
		spec.setHard(hard);
		quota.setSpec(spec);

		client.updateResourceQuota(namespace, quota);

		return JSON.toJSONString(map);
	}

	/**
	 * 获取租户的资源信息
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "quota/allQuotas.do" }, method = RequestMethod.GET)
	public String getAllTenantQuota(Model model) {
		List<User> tenants = userDao.findAllTenant();
		List<TenantResource> tenantResources = new ArrayList<TenantResource>();
		for (User tenant : tenants) {
			UserResource userResource = userResourceDao.findByUserId(tenant.getId());
			if(userResource==null){
				LOG.error(tenant.getUserName()+"在表UserResource中无记录！");
				continue;
			}
			KubernetesAPIClientInterface client =null;
			try {
				client = kubernetesClientService.getClient(tenant.getNamespace());
			} catch (Exception e) {
				LOG.error(tenant.getNamespace()+"异常！");
				continue;
			}
			double cpu = userResource.getCpu();
			long mem = userResource.getMemory();
			ResourceQuota resourceQuota=null;
			try {
				 resourceQuota = client.getResourceQuota(tenant.getNamespace());
			} catch (Exception e) {
				LOG.error(tenant.getUserName()+"没有分配Quota！");
				continue;
			}
			String quotaCpu = resourceQuota.getSpec().getHard().get("cpu");
			String quotaMem = resourceQuota.getSpec().getHard().get("memory").replaceAll("G", "").trim();
			String usedQuotaCpu = resourceQuota.getStatus().getUsed().get("cpu");
			String usedQuotaMem = resourceQuota.getStatus().getUsed().get("memory").replaceAll("G", "").trim();
			TenantResource tenantResource = new TenantResource();
			tenantResource.setId(tenant.getId());
			tenantResource.setName(tenant.getUserName());
			tenantResource.setCpu(cpu);
			tenantResource.setMemory(mem);
			tenantResource.setQuotaCpu(quotaCpu);
			tenantResource.setQuotaMem(quotaMem);
			tenantResource.setQuotaCpuUsed(usedQuotaCpu);
			tenantResource.setQuotaMemUsed(usedQuotaMem);
			tenantResources.add(tenantResource);
		}
		model.addAttribute("tenantResources", tenantResources);
		return "tenantResource.jsp";
	}
}
