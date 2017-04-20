/**
 *
 */
package com.bonc.epm.paas.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.dao.EnvVariableDao;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.CephFSVolumeSource;
import com.bonc.epm.paas.kubernetes.model.LocalObjectReference;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerSpec;
import com.bonc.epm.paas.kubernetes.model.Volume;
import com.bonc.epm.paas.kubernetes.model.VolumeMount;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/api/v1")
public class ServiceApi {

	/**
	 * 服务数据层接口
	 */
	@Autowired
	private ServiceDao serviceDao;

	/**
	 * 用户层接口
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * portConfig数据层接口
	 */
	@Autowired
	private PortConfigDao portConfigDao;

	/**
	 * envVariableDao:数据层接口.
	 */
	@Autowired
	private EnvVariableDao envVariableDao;

	/**
	 * storageDao:数据层接口.
	 */
	@Autowired
	private StorageDao storageDao;

	/**
	 * dockerClientService:docker服务接口.
	 */
	@Autowired
	private DockerClientService dockerClientService;

	/**
	 * kubernetesClientService:kubernetes服务接口.
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

    /**
     * 获取ceph.monitor数据
     */
    @Value("${ceph.monitor}")
	private String CEPH_MONITOR;


	/**
	 * Description: <br>
	 * services 获取所有服务
	 *
	 * @return String
	 */
	@RequestMapping(value = { "/services" }, method = RequestMethod.GET)
	@ResponseBody
	public String services() {
		Iterable<Service> result = serviceDao.search("%", "%", "%");
		Iterator<Service> it = result.iterator();
		List<Service> serviceList = fillServiceInfo(it);
		return JSON.toJSONString(serviceList);
	}

	/**
	 * Description: <br>
	 * userServices 按用户名检索服务
	 *
	 * @param user
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/services" }, method = RequestMethod.GET)
	@ResponseBody
	public String userServices(@PathVariable String user) {
		Iterable<Service> result = serviceDao.search("%", "%", (user != null ? user : ""));
		Iterator<Service> it = result.iterator();
		List<Service> serviceList = fillServiceInfo(it);
		return JSON.toJSONString(serviceList);
	}

	/**
	 * Description: <br>
	 * getPortConfig 按用户名和服务名检索服务
	 *
	 * @param user
	 * @param services
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/services/{services}" }, method = RequestMethod.GET)
	@ResponseBody
	public String userServices2(@PathVariable String user, @PathVariable String services) {
		Iterable<Service> result = serviceDao.search((services != null ? services : ""), "%",
				(user != null ? user : ""));
		Iterator<Service> it = result.iterator();
		List<Service> serviceList = fillServiceInfo(it);
		return JSON.toJSONString(serviceList);
	}

	private List<Service> fillServiceInfo(Iterator<Service> it) {
		List<Service> serviceList = new ArrayList<>();
		while (it.hasNext()) {
			Service service = it.next();
			service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
			List<PortConfig> ports = portConfigDao.findByServiceId(service.getId());
			if (CollectionUtils.isNotEmpty(ports)) {
				service.setPortConfigs(ports);
			}
//			Image image = imageDao.findById(service.getImgID());
//			service.setCodeRating(1);
//			service.setCodeRatingURL("http://test.com");
			serviceList.add(service);
		}
		return serviceList;
	}


	@RequestMapping(value = { "/services/build" }, method = RequestMethod.POST)
	@ResponseBody
	public String buildAllServices() {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		Iterable<Service> services = serviceDao.findAll();
		Iterator<Service> iterator = services.iterator();
		while (iterator.hasNext()) {
			Service service = iterator.next();
			if (service.getStatus().equals(ServiceConstant.CONSTRUCTION_STATUS_RUNNING)) {
				User user = userDao.findOne(service.getCreateBy());
				if (null == user) {
					messages.add("找不到创建用户[serviceName"+service.getServiceName()+"]");
					continue;
				}
				messages = createService(user.getNamespace(), service, messages);
			}
		}
		map.put("status", "200");
		if (messages.size() > 0) {
			map.replace("status", "400");
			map.put("message", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * buildSpecifiedUserServices:根据数据库创建指定用户的所有服务. <br/>
	 *
	 * @author longkaixiang
	 * @param userName
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/services/build" }, method = RequestMethod.POST)
	@ResponseBody
	public String buildSpecifiedUserServices(@PathVariable("user") String userName) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		User user = userDao.findByUserName(userName);
		if (null == user) {
			messages.add("未找到用户[userName" + userName + "]");
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		List<Service> services = serviceDao.findByCreateBy(user.getId());
		Iterator<Service> iterator = services.iterator();
		while (iterator.hasNext()) {
			Service service = iterator.next();
			if (service.getStatus().equals(ServiceConstant.CONSTRUCTION_STATUS_RUNNING)) {
				messages = createService(user.getNamespace(), service, messages);
			}
		}
		map.put("status", "200");
		if (messages.size() > 0) {
			map.replace("status", "400");
			map.put("message", messages);
		}
		return JSON.toJSONString(map);
	}


	/**
	 * buildSpecifiedService:根据数据库创建指定用户的指定服务. <br/>
	 *
	 * @author longkaixiang
	 * @param userName
	 * @param serviceName
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/service/{service}/build" }, method = RequestMethod.POST)
	@ResponseBody
	public String buildSpecifiedService(@PathVariable("user") String userName, @PathVariable("service") String serviceName) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		User user = userDao.findByUserName(userName);
		if (null == user) {
			messages.add("未找到用户[userName" + userName + "]");
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		List<Service> services = serviceDao.findByNameOf(user.getId(), serviceName);
		if (CollectionUtils.isEmpty(services)) {
			messages.add("未找到服务[userName" + userName + ", serviceName=" + serviceName + "]");
			map.put("status", "400");
			return JSON.toJSONString(map);
		}

		Service service = services.get(0);
		if (service.getStatus().equals(ServiceConstant.CONSTRUCTION_STATUS_RUNNING)) {
			messages = createService(user.getNamespace(), service, messages);
		}
		map.put("status", "200");
		if (messages.size() > 0) {
			map.replace("status", "400");
			map.put("message", messages);
		}
		return JSON.toJSONString(map);
	}

	private List<String> createService(String namespace, Service service, List<String> messages) {
		if (null == messages) {
			messages = new ArrayList<>();
		}
		// 获取服务对应的环境变量
		List<EnvVariable> envVariables = envVariableDao.findByServiceId(service.getId());
		// 获取服务对应的端口映射
		List<PortConfig> portConfigs = portConfigDao.findByServiceId(service.getId());
		// 使用k8s管理服务
		String registryImgName = dockerClientService.generateRegistryImageName(service.getImgName(),
				service.getImgVersion());
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
		com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
		ReplicationController rc = null;

		/*************************************
		 * 获取svc和rc并删除
		 *************************************/
		try {
			// 获取svc
			k8sService = client.getService(service.getServiceName());
			client.deleteService(service.getServiceName());
		} catch (KubernetesClientException e) {
			k8sService = null;
		}
		try {
			// 获取rc
			rc = client.getReplicationController(service.getServiceName());
			client.deleteReplicationController(service.getServiceName());
		} catch (KubernetesClientException e) {
			rc = null;
		}

		/***************************************
		 * 创建一个新的svc
		 ***************************************/
		try {
			k8sService = kubernetesClientService.generateService(service.getServiceName(), portConfigs,
					service.getProxyZone(), service.getServicePath(), service.getProxyPath(),
					service.getSessionAffinity(), service.getNodeIpAffinity());
			k8sService = client.createService(k8sService);
		} catch (KubernetesClientException e) {
			System.out.println(e.getStatus().getMessage());
			messages.add("service创建失败[ServiceName="+service.getServiceName()+", Message="+e.getStatus().getMessage()+"]");
			return messages;
		}

		/*************************************
		 * 创建一个新的rc
		 *************************************/
		try {
			// 如果没有则新增
			List<String> command = new ArrayList<String>();
			List<String> args = new ArrayList<String>();
			// 初始化自定义启动命令
			String startCommand = service.getStartCommand().trim();
			if (StringUtils.isNotBlank(startCommand)) {
				String[] startCommandArray = startCommand.replaceAll("\\s+", " ").replaceAll("/debug.sh", "").trim()
						.split(" ");
				for (String item : startCommandArray) {
					if (CollectionUtils.isEmpty(command)) {
						command.add(item);
						continue;
					}
					args.add(item);
				}
			}
			rc = kubernetesClientService.generateSimpleReplicationController(service.getServiceName(),
					service.getInstanceNum(), service.getInitialDelay(), service.getTimeoutDetction(),
					service.getPeriodDetction(), registryImgName, portConfigs, service.getCpuNum(),
					service.getRam(), service.getProxyZone(), service.getServicePath(), service.getProxyPath(),
					service.getCheckPath(), envVariables, command, args);
			// 给rc设置卷组挂载的信息
			if (service.getServiceType().equals("1")) {
				rc = setVolumeStorage(namespace, rc, service.getId());
			}
			rc = client.createReplicationController(rc);
		} catch (KubernetesClientException e) {
			System.out.println(e.getStatus().getMessage());
			messages.add("rc创建失败[ServiceName="+service.getServiceName()+", Message="+e.getStatus().getMessage()+"]");
			return messages;
		}
		return messages;
	}

    /**
     * setVolumeStorage:给rc设置卷组挂载的信息. <br/>
     *
     * @author longkaixiang
     * @param rc
     * @param serviceId
     * @return ReplicationController
     */
    private ReplicationController setVolumeStorage(String namespace, ReplicationController rc,long serviceId) {
        List<Storage> storageList = storageDao.findByServiceId(serviceId);
        ReplicationControllerSpec rcSpec = rc.getSpec();
        PodTemplateSpec template = rcSpec.getTemplate();
        PodSpec podSpec = template.getSpec();
        List<Volume> volumes = new ArrayList<Volume>();
        List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
        int i = 0;
        for (Storage storage : storageList) {
        	i++;
            Volume volume = new Volume();
//            volume.setName("cephfs-"+storage.getStorageName());
            volume.setName("cephfs-" + i);
            CephFSVolumeSource cephfs = new CephFSVolumeSource();
            List<String> monitors = new ArrayList<String>();
            System.out.println("CEPH_MONITOR:" + CEPH_MONITOR);
            String[] ceph_monitors = CEPH_MONITOR.split(",");
            for (String ceph_monitor : ceph_monitors) {
                monitors.add(ceph_monitor);
            }
            cephfs.setMonitors(monitors);
            cephfs.setPath("/" + namespace + "/" + storage.getStorageName());
            cephfs.setUser("admin");
            LocalObjectReference secretRef = new LocalObjectReference();
            secretRef.setName("ceph-secret");
            cephfs.setSecretRef(secretRef);
            cephfs.setReadOnly(false);
            volume.setCephfs(cephfs);
            volumes.add(volume);

            VolumeMount volumeMount = new VolumeMount();
            volumeMount.setMountPath(storage.getMountPoint());
//            volumeMount.setName("cephfs-"+storage.getStorageName());
            volumeMount.setName("cephfs-" + i);
            volumeMounts.add(volumeMount);
        }

        podSpec.setVolumes(volumes);
        List<com.bonc.epm.paas.kubernetes.model.Container> containers = podSpec.getContainers();
        for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
            container.setVolumeMounts(volumeMounts);
        }
        return rc;
    }
}
