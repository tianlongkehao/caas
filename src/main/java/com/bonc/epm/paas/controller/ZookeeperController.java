package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.dao.ZookeeperDao;
import com.bonc.epm.paas.dao.ZookeeperImageDao;
import com.bonc.epm.paas.entity.Pod;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.entity.Zookeeper;
import com.bonc.epm.paas.entity.ZookeeperImage;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Affinity;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.ContainerPort;
import com.bonc.epm.paas.kubernetes.model.EnvVar;
import com.bonc.epm.paas.kubernetes.model.ExecAction;
import com.bonc.epm.paas.kubernetes.model.LabelSelector;
import com.bonc.epm.paas.kubernetes.model.LabelSelectorRequirement;
import com.bonc.epm.paas.kubernetes.model.ObjectMeta;
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaim;
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaimSpec;
import com.bonc.epm.paas.kubernetes.model.PodAffinityTerm;
import com.bonc.epm.paas.kubernetes.model.PodAntiAffinity;
import com.bonc.epm.paas.kubernetes.model.PodDisruptionBudget;
import com.bonc.epm.paas.kubernetes.model.PodDisruptionBudgetSpec;
import com.bonc.epm.paas.kubernetes.model.PodSecurityContext;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.Probe;
import com.bonc.epm.paas.kubernetes.model.ResourceRequirements;
import com.bonc.epm.paas.kubernetes.model.SecurityContext;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.model.ServiceSpec;
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.StatefulSetSpec;
import com.bonc.epm.paas.kubernetes.model.VolumeMount;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
@RequestMapping(value = "/zookeeper")
public class ZookeeperController {

	private static final Logger LOG = LoggerFactory.getLogger(ZookeeperController.class);

	@Autowired
	private ServiceController serviceController;

	@Autowired
	private ZookeeperDao zookeeperDao;

	@Autowired
	private ZookeeperImageDao zookeeperImageDao;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private UserResourceDao userResourceDao;

	@Autowired
	private KubernetesClientService kubernetesClientService;

	@Value("${ceph.monitor}")
	private String CEPH_MONITORS;

	@Value("${docker.io.username}")
	private String REGISTRY_ADDR;

	@Value("${ratio.limittorequestcpu}")
	public int RATIO_LIMITTOREQUESTCPU;

	@Value("${ratio.limittorequestmemory}")
	public int RATIO_LIMITTOREQUESTMEMORY;

	/**
	 * 主页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

		UserResource userResource = userResourceDao.findByUserId(user.getId());
		long restStorage = 0;
		if (userResource != null) {
			restStorage = userResource.getVol_surplus_size();
		}

		List<Zookeeper> zookeepers = zookeeperDao.findByNamespace(user.getNamespace());
		List<ZookeeperImage> zookeeperImages = new ArrayList<ZookeeperImage>();
		Iterable<ZookeeperImage> iterable = zookeeperImageDao.findAll();
		if (null != iterable) {
			Iterator<ZookeeperImage> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				zookeeperImages.add(iterator.next());
			}
		}

		model.addAttribute("leftstorage", restStorage);
		model.addAttribute("images", zookeeperImages);
		model.addAttribute("zookeepers", zookeepers);
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "tensorflow");
		return "market/zookeeper.jsp";
	}

	/**
	 * 显示指定的zookeeper
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String showSpecifiedZookeeper(Model model, @PathVariable long id) {
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

		return "market/zookeeper.jsp";
	}

	/**
	 * 新增
	 *
	 * @param zookeeper
	 * @return
	 */
	@RequestMapping(value = { "/add.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String addZookeeper(Zookeeper zookeeper) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		zookeeper.setCreateDate(new Date());
		zookeeper.setCreateBy(user.getId());
		zookeeper.setNamespace(user.getNamespace());
		zookeeperDao.save(zookeeper);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 更新
	 *
	 * @return
	 */
	@RequestMapping(value = { "/update.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String updateZookeeper(Zookeeper zookeeper) {
		Map<String, Object> map = new HashMap<String, Object>();

		Zookeeper originalzookeeper = zookeeperDao.findOne(zookeeper.getId());
		originalzookeeper.setCpu(zookeeper.getCpu());
		originalzookeeper.setMemory(zookeeper.getMemory());
		originalzookeeper.setDetail(zookeeper.getDetail());
		originalzookeeper.setStarttimeout(zookeeper.getStarttimeout());
		originalzookeeper.setTimeoutdeadline(zookeeper.getTimeoutdeadline());
		originalzookeeper.setSyntimeout(zookeeper.getSyntimeout());
		originalzookeeper.setMaxnode(zookeeper.getMaxnode());
		zookeeperDao.save(originalzookeeper);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/delete.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String deleteZookeeper(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Zookeeper zookeeper = zookeeperDao.findOne(id);

		zookeeperDao.delete(zookeeper);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 获取信息
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/detail.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String detailZookeeper(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Zookeeper zookeeper = zookeeperDao.findOne(id);

		map.put("zookeeper", zookeeper);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 0：不存在，1：存在
	 *
	 * @param name
	 * @return
	 */
	@RequestMapping(value = { "/exist.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String zookeeperExist(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("exist", 0);

		User user = CurrentUserUtils.getInstance().getUser();
		long creator = user.getId();

		List<Service> services = serviceDao.findByNameOf(creator, name.trim());
		if (CollectionUtils.isNotEmpty(services)) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		Zookeeper zookeeper = zookeeperDao.findByNamespaceAndName(user.getNamespace(), name);
		if (zookeeper != null) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 启动
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/start.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String start(long id) {
		User user = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		KubernetesAPIClientInterface apiClient = kubernetesClientService.getClient();
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();

		Zookeeper zookeeper = zookeeperDao.findOne(id);

		createService(apiClient, zookeeper.getName());
		createService2(apiClient, zookeeper.getName());
		//createPodDisruptionBudget(apisClient, zookeeper);
		createStatefulset(apisClient, zookeeper);

		zookeeper.setStatus(1);
		zookeeperDao.save(zookeeper);

		return JSON.toJSONString(map);
	}

	/**
	 * 停止
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/stop.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String stop(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		KubernetesAPIClientInterface apiClient = kubernetesClientService.getClient();
		KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();

		Zookeeper zookeeper = zookeeperDao.findOne(id);

		deleteService(apiClient, zookeeper.getName());
		deleteService(apiClient, zookeeper.getName() + "-headless");
		//deletePodDisruptionBudget(apisClient, zookeeper.getName() + "-budget");
		deleteStatefulSet(apisClient, zookeeper.getName());
		deletePods(apiClient, zookeeper.getName());

		zookeeper.setStatus(0);
		zookeeperDao.save(zookeeper);

		return JSON.toJSONString(map);
	}

	private void deletePods(KubernetesAPIClientInterface apiClient, String name) {
		com.bonc.epm.paas.kubernetes.model.Pod pod1 = null;
		com.bonc.epm.paas.kubernetes.model.Pod pod2 = null;
		com.bonc.epm.paas.kubernetes.model.Pod pod3 = null;
		try {
			pod1 = apiClient.getPod(name + "-0");
		} catch (KubernetesClientException e) {
			pod1 = null;
		}
		if (null != pod1) {
			apiClient.deletePod(name + "-0");
		}

		try {
			pod2= apiClient.getPod(name + "-1");
		} catch (KubernetesClientException e) {
			pod2 = null;
		}
		if (null != pod2) {
			apiClient.deletePod(name + "-1");
		}

		try {
			pod3 = apiClient.getPod(name + "-2");
		} catch (KubernetesClientException e) {
			pod3 = null;
		}
		if (null != pod3) {
			apiClient.deletePod(name + "-2");
		}
	}

	private void deleteService(KubernetesAPIClientInterface apiClient, String name) {
		com.bonc.epm.paas.kubernetes.model.Service service = null;
		try {
			service = apiClient.getService(name);
		} catch (KubernetesClientException e) {
			service = null;
		}

		if (service != null) {
			apiClient.deleteService(name);
		}
	}

	private void deleteStatefulSet(KubernetesAPISClientInterface apisClient, String name) {
		if (null != apisClient.getStatefulSet(name)) {
			apisClient.deleteStatefulSet(name);
		}
	}

	private void deletePodDisruptionBudget(KubernetesAPISClientInterface apisClient, String name) {
		if (null != apisClient.getPodDisruptionBudget(name)) {
			apisClient.deletePodDisruptionBudget(name);
		}
	}

	private boolean createService(KubernetesAPIClientInterface apiClient, String name) {
		User user = CurrentUserUtils.getInstance().getUser();
		com.bonc.epm.paas.kubernetes.model.Service service = new com.bonc.epm.paas.kubernetes.model.Service();
		ObjectMeta objectMeta = new ObjectMeta();
		objectMeta.setName(name);
		objectMeta.setNamespace(user.getNamespace());

		ServiceSpec serviceSpec = new ServiceSpec();

		List<ServicePort> servicePorts = new ArrayList<ServicePort>();
		ServicePort servicePort = new ServicePort();
		servicePort.setPort(2181);
		servicePort.setName("client");
		servicePorts.add(servicePort);

		Map<String, String> selector = new HashMap<String, String>();
		selector.put("app", name);

		serviceSpec.setPorts(servicePorts);
		serviceSpec.setSelector(selector);
		service.setMetadata(objectMeta);
		service.setSpec(serviceSpec);

		try {
			apiClient.createService(service);
		} catch (KubernetesClientException e) {
			return false;
		}

		return true;
	}

	private boolean createService2(KubernetesAPIClientInterface apiClient, String name) {
		User user = CurrentUserUtils.getInstance().getUser();
		com.bonc.epm.paas.kubernetes.model.Service service = new com.bonc.epm.paas.kubernetes.model.Service();
		ObjectMeta objectMeta = new ObjectMeta();
		objectMeta.setName(name + "-headless");
		objectMeta.setNamespace(user.getNamespace());

		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", name + "-headless");
		objectMeta.setLabels(labels);

		ServiceSpec serviceSpec = new ServiceSpec();

		List<ServicePort> servicePorts = new ArrayList<ServicePort>();
		ServicePort servicePort = new ServicePort();
		servicePort.setPort(2888);
		servicePort.setName("server");

		ServicePort servicePort2 = new ServicePort();
		servicePort2.setPort(3888);
		servicePort2.setName("leader-election");

		servicePorts.add(servicePort);
		servicePorts.add(servicePort2);

		Map<String, String> selector = new HashMap<String, String>();
		selector.put("app", name);

		serviceSpec.setPorts(servicePorts);
		serviceSpec.setSelector(selector);
		serviceSpec.setClusterIP("None");
		service.setMetadata(objectMeta);
		service.setSpec(serviceSpec);

		try {
			apiClient.createService(service);
		} catch (KubernetesClientException e) {
			return false;
		}

		return true;
	}

	private boolean createPodDisruptionBudget(KubernetesAPISClientInterface apisClient, Zookeeper zookeeper) {
		PodDisruptionBudget podDisruptionBudget = new PodDisruptionBudget();
		ObjectMeta metadata = new ObjectMeta();
		metadata.setName(zookeeper.getName() + "-budget");
		metadata.setNamespace(zookeeper.getNamespace());
		PodDisruptionBudgetSpec spec = new PodDisruptionBudgetSpec();
		LabelSelector selector = new LabelSelector();
		Map<String, String> matchLabels = new HashMap<String, String>();
		matchLabels.put("app", zookeeper.getName());
		selector.setMatchLabels(matchLabels);
		spec.setSelector(selector);
		spec.setMinAvailable(2);
		podDisruptionBudget.setMetadata(metadata);
		podDisruptionBudget.setSpec(spec);

		try {
			apisClient.createPodDisruptionBudget(podDisruptionBudget);
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean createStatefulset(KubernetesAPISClientInterface apisClient, Zookeeper zookeeper) {
		User user = CurrentUserUtils.getInstance().getUser();

		StatefulSet statefulSet = new StatefulSet();

		ObjectMeta objectMeta = new ObjectMeta();
		objectMeta.setName(zookeeper.getName());
		objectMeta.setNamespace(user.getNamespace());

		StatefulSetSpec statefulSetSpec = new StatefulSetSpec();
		statefulSetSpec.setServiceName(zookeeper.getName() + "-headness");
		statefulSetSpec.setReplicas(3);

		PodTemplateSpec podTemplateSpec = new PodTemplateSpec();

		ObjectMeta podMeta = new ObjectMeta();
		Map<String, String> labels = new HashMap<>();
		labels.put("app", zookeeper.getName());
		Map<String, String> annotations = new HashMap<>();
		annotations.put("pod.alpha.kubernetes.io/initialized", "true");
		podMeta.setLabels(labels);
		podMeta.setAnnotations(annotations);

		PodSpec podSpec = new PodSpec();
		podSpec.setAffinity(createAffinity(zookeeper.getName()));
		podSpec.setContainers(createContainerList(zookeeper));
		PodSecurityContext podSecurityContext = new PodSecurityContext();
		podSecurityContext.setFsGroup(1000);
		podSecurityContext.setRunAsUser(1000);
		podSpec.setSecurityContext(podSecurityContext);

		podTemplateSpec.setMetadata(podMeta);
		podTemplateSpec.setSpec(podSpec);

		statefulSetSpec.setTemplate(podTemplateSpec);
		statefulSetSpec.setVolumeClaimTemplates(createPersistentVolumeClaims(zookeeper));

		statefulSet.setMetadata(objectMeta);
		statefulSet.setSpec(statefulSetSpec);
		try {
			apisClient.createStatefulSet(statefulSet);
		} catch (KubernetesClientException e) {
			return false;
		}
		return true;
	}

	private List<PersistentVolumeClaim> createPersistentVolumeClaims(Zookeeper zookeeper) {
		List<PersistentVolumeClaim> persistentVolumeClaims = new ArrayList<PersistentVolumeClaim>();
		PersistentVolumeClaim persistentVolumeClaim = new PersistentVolumeClaim();
		ObjectMeta pvcMeta = new ObjectMeta();
		pvcMeta.setName("zkdir");
		pvcMeta.setNamespace(zookeeper.getNamespace());

		PersistentVolumeClaimSpec persistentVolumeClaimSpec = new PersistentVolumeClaimSpec();
		List<String> accessModes = new ArrayList<String>();
		accessModes.add("ReadWriteOnce");

		ResourceRequirements resourceRequirements = new ResourceRequirements();
		Map<String, Object> requests = new HashMap<String, Object>();
		requests.put("storage", zookeeper.getStorage() + "Gi");
		resourceRequirements.setRequests(requests);

		LabelSelector labelSelector = new LabelSelector();
		List<LabelSelectorRequirement> labelSelectorRequirements = new ArrayList<LabelSelectorRequirement>();
		LabelSelectorRequirement labelSelectorRequirement = new LabelSelectorRequirement();
		labelSelectorRequirement.setKey("app");
		labelSelectorRequirement.setOperator("In");
		List<String> values = new ArrayList<>();
		values.add("zook");
		labelSelectorRequirement.setValues(values);
		labelSelectorRequirements.add(labelSelectorRequirement);
		labelSelector.setMatchExpressions(labelSelectorRequirements);

		persistentVolumeClaimSpec.setSelector(labelSelector);
		persistentVolumeClaimSpec.setAccessModes(accessModes);
		persistentVolumeClaimSpec.setResources(resourceRequirements);
		//persistentVolumeClaimSpec.setStorageClassName(zookeeper.getNamespace());
		persistentVolumeClaim.setMetadata(pvcMeta);
		persistentVolumeClaim.setSpec(persistentVolumeClaimSpec);

		persistentVolumeClaims.add(persistentVolumeClaim);

		return persistentVolumeClaims;
	}

	private Affinity createAffinity(String name) {
		Affinity affinity = new Affinity();
		PodAntiAffinity podAntiAffinity = new PodAntiAffinity();
		List<PodAffinityTerm> podAffinityTerms = new ArrayList<PodAffinityTerm>();
		PodAffinityTerm podAffinityTerm = new PodAffinityTerm();

		LabelSelector labelSelector = new LabelSelector();
		List<LabelSelectorRequirement> labelSelectorRequirements = new ArrayList<LabelSelectorRequirement>();
		LabelSelectorRequirement labelSelectorRequirement = new LabelSelectorRequirement();
		labelSelectorRequirement.setKey("app");
		labelSelectorRequirement.setOperator("In");
		List<String> values = new ArrayList<String>();
		values.add(name + "-headness");
		labelSelectorRequirement.setValues(values);
		labelSelectorRequirements.add(labelSelectorRequirement);

		labelSelector.setMatchExpressions(labelSelectorRequirements);
		podAffinityTerm.setLabelSelector(labelSelector);
		podAffinityTerm.setTopologyKey("kubernetes.io/hostname");
		podAffinityTerms.add(podAffinityTerm);

		podAntiAffinity.setRequiredDuringSchedulingIgnoredDuringExecution(podAffinityTerms);
		affinity.setPodAntiAffinity(podAntiAffinity);

		return affinity;
	}

	private List<Container> createContainerList(Zookeeper zookeeper) {
		List<Container> containers = new ArrayList<Container>();
		Container container = new Container();
		container.setName(zookeeper.getName());
		container.setImagePullPolicy("Always");
		container.setImage(REGISTRY_ADDR + "/" + zookeeper.getImage());

		List<ContainerPort> containerPorts = new ArrayList<ContainerPort>();
		ContainerPort containerPort1 = new ContainerPort();
		ContainerPort containerPort2 = new ContainerPort();
		ContainerPort containerPort3 = new ContainerPort();
		containerPort1.setName("client");
		containerPort1.setContainerPort(2181);
		containerPort2.setName("server");
		containerPort2.setContainerPort(2888);
		containerPort3.setName("leader-election");
		containerPort3.setContainerPort(3888);

		containerPorts.add(containerPort1);
		containerPorts.add(containerPort2);
		containerPorts.add(containerPort3);

		ResourceRequirements resourceRequirements = new ResourceRequirements();
		Map<String, Object> requests = new HashMap<String, Object>();
		requests.put("cpu", zookeeper.getCpu() / RATIO_LIMITTOREQUESTCPU);
		requests.put("memory", (zookeeper.getMemory() * 1024l) / RATIO_LIMITTOREQUESTMEMORY + "Mi");

		Map<String, Object> limits = new HashMap<String, Object>();
		limits.put("cpu", zookeeper.getCpu());
		limits.put("memory", zookeeper.getMemory() * 1024l + "Mi");
		resourceRequirements.setLimits(limits);
		resourceRequirements.setRequests(requests);

		List<EnvVar> envVars = new ArrayList<EnvVar>();
		EnvVar envVar1 = new EnvVar();
		envVar1.setName("ZK_ENSEMBLE");
		envVar1.setValue(zookeeper.getName() + "-0;" + zookeeper.getName() + "-1;" + zookeeper.getName() + "-2");
		EnvVar envVar2 = new EnvVar();
		envVar2.setName("ZK_HEAP_SIZE");
		envVar2.setValue("2G");
		EnvVar envVar3 = new EnvVar();
		envVar3.setName("ZK_TICK_TIME");
		envVar3.setValue(String.valueOf(zookeeper.getTimeoutdeadline()));
		EnvVar envVar4 = new EnvVar();
		envVar4.setName("ZK_INIT_LIMIT");
		envVar4.setValue(String.valueOf(zookeeper.getStarttimeout()));
		EnvVar envVar5 = new EnvVar();
		envVar5.setName("ZK_SYNC_LIMIT");
		envVar5.setValue(String.valueOf(zookeeper.getSyntimeout()));
		EnvVar envVar6 = new EnvVar();
		envVar6.setName("ZK_MAX_CLIENT_CNXNS");
		envVar6.setValue(String.valueOf(zookeeper.getMaxnode()));
		EnvVar envVar7 = new EnvVar();
		envVar7.setName("ZK_SNAP_RETAIN_COUNT");
		envVar7.setValue("3");
		EnvVar envVar8 = new EnvVar();
		envVar8.setName("ZK_PURGE_INTERVAL");
		envVar8.setValue("1");
		EnvVar envVar9 = new EnvVar();
		envVar9.setName("ZK_CLIENT_PORT");
		envVar9.setValue("2181");
		EnvVar envVar10 = new EnvVar();
		envVar10.setName("ZK_SERVER_PORT");
		envVar10.setValue("2888");
		EnvVar envVar11 = new EnvVar();
		envVar11.setName("ZK_ELECTION_PORT");
		envVar11.setValue("3888");

		envVars.add(envVar1);
		envVars.add(envVar2);
		envVars.add(envVar3);
		envVars.add(envVar4);
		envVars.add(envVar5);
		envVars.add(envVar6);
		envVars.add(envVar7);
		envVars.add(envVar8);
		envVars.add(envVar9);
		envVars.add(envVar10);
		envVars.add(envVar11);

		List<String> commands = new ArrayList<String>();
		commands.add("sh");
		commands.add("-c");
		commands.add("zkGenConfig.sh && zkServer.sh start-foreground");

		Probe readinessProbe = new Probe();
		ExecAction execAction = new ExecAction();
		List<String> readinessCommands = new ArrayList<String>();
		readinessCommands.add("zkOk.sh");
		execAction.setCommand(readinessCommands);
		readinessProbe.setInitialDelaySeconds(15);
		readinessProbe.setTimeoutSeconds(5);
		readinessProbe.setExec(execAction);

		List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
		VolumeMount volumeMount = new VolumeMount();
		volumeMount.setName("zkdir");
		volumeMount.setMountPath("/var/lib/zookeeper");
		volumeMounts.add(volumeMount);

		container.setVolumeMounts(volumeMounts);
		container.setLivenessProbe(readinessProbe);
		container.setReadinessProbe(readinessProbe);
		container.setCommand(commands);
		container.setEnv(envVars);
		container.setResources(resourceRequirements);
		container.setPorts(containerPorts);
		containers.add(container);

		return containers;
	}

	/**
	 * 增加zookeeperimage
	 *
	 * @param name
	 * @param version
	 * @return
	 */
	@RequestMapping(value = { "/addImage.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String addImage(String name, String version) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		ZookeeperImage zookeeperImage = new ZookeeperImage();
		zookeeperImage.setName(name);
		zookeeperImage.setVersion(version);
		zookeeperImage.setCreateDate(new Date());
		zookeeperImageDao.save(zookeeperImage);

		return JSON.toJSONString(map);
	}
}
