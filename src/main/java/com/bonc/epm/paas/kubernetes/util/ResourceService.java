package com.bonc.epm.paas.kubernetes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.util.CurrentUserUtils;

@org.springframework.stereotype.Service
public class ResourceService {

	@Value("${ratio.limittorequestcpu}")
	private int RATIO_LIMITTOREQUESTCPU;

	@Value("${ratio.limittorequestmemory}")
	private int RATIO_LIMITTOREQUESTMEMORY;

	/**
	 * 要求预留cpu
	 */
	@Value("${rest.resource.cpu}")
	private int REST_RESOURCE_CPU;

	/**
	 * 要求预留memory
	 */
	@Value("${rest.resource.memory}")
	private int REST_RESOURCE_MEMORY;

	/**
	 * ResourceController日志实例
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

	/**
	 * KubernetesClientService接口
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	public static byte STATUS = -1;
	public static byte CPU_LACK = 1;
	public static byte MEMORY_LACK = 2;

	public  boolean checkRestResource(Service service) {
		return checkRestResource(service.getCpuNum(), service.getRam());
	}

	public boolean checkRestResource(double cpu, String ram){
		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		try {
			ResourceQuota quota = client.getResourceQuota(currentUser.getNamespace());
			if (quota.getStatus() != null) {
				long hard = kubernetesClientService.transMemory(quota.getStatus().getHard().get("memory"));
				long used = kubernetesClientService.transMemory(quota.getStatus().getUsed().get("memory"));

				double leftCpu = kubernetesClientService.transCpu(quota.getStatus().getHard().get("cpu"))
						- kubernetesClientService.transCpu(quota.getStatus().getUsed().get("cpu"));

				long leftmemory = hard - used;

				leftCpu = leftCpu * RATIO_LIMITTOREQUESTCPU;
				leftmemory = leftmemory * RATIO_LIMITTOREQUESTMEMORY;

				leftmemory = Math.round(Math.ceil(leftmemory / 1000.0));

				long serviceMemory = Math.round(Math.ceil(Double.parseDouble(ram)/1000.0));


				if (leftCpu - cpu < REST_RESOURCE_CPU) {
					LOG.info("本次服务请求cpu:"+cpu+",用户剩余cpu："+leftCpu+",需要预留cpu:"+REST_RESOURCE_CPU);
					STATUS =  CPU_LACK;
					return false;
				}

				if (leftmemory - serviceMemory < REST_RESOURCE_MEMORY) {
					LOG.info("本次服务请求memory:"+serviceMemory+",用户剩余memory："+leftmemory+",需要预留memory:"+REST_RESOURCE_MEMORY);
					STATUS = MEMORY_LACK;
					return false;
				}
			} else {
				LOG.info("用户 " + currentUser.getUserName() + " 没有定义名称为 " + currentUser.getNamespace() + " 的Namespace ");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
