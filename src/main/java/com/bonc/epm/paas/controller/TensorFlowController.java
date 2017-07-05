package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.bonc.epm.paas.dao.CephRbdInfoDao;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.TensorflowDao;
import com.bonc.epm.paas.dao.TensorflowImageDao;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Tensorflow;
import com.bonc.epm.paas.entity.TensorflowImage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.ceph.CephRbdInfo;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.CephRbd;
import com.bonc.epm.paas.kubernetes.model.LoadBalancerIngress;
import com.bonc.epm.paas.kubernetes.model.LocalObjectReference;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerSpec;
import com.bonc.epm.paas.kubernetes.model.Volume;
import com.bonc.epm.paas.kubernetes.model.VolumeMount;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.EncryptUtils;

@Controller
@RequestMapping(value = "/tensorflow")
public class TensorFlowController {

	private static final Logger LOG = LoggerFactory.getLogger(TensorFlowController.class);

	@Value("${nginxConf.io}")
	private boolean NGINXCONF_IO;

	@Value("${nginxConf.io.serverAddr}")
	private String SERVERADDR;

	@Value("${docker.io.username}")
	private String REGISTRY_ADDR;

	@Autowired
	private ServiceController serviceController;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private TensorflowDao tensorflowDao;

	@Autowired
	private TensorflowImageDao tensorflowImageDao;

	@Autowired
	private CephController cephController;

	@Autowired
	private CephRbdInfoDao cephRbdInfoDao;

	@Autowired
	private KubernetesClientService kubernetesClientService;

	private static Set<Integer> smalSet = new HashSet<Integer>();

	// 容器内端口
	private final String CONTAINER_PORT = "8888";

	/**
	 * 获取ceph.monitor数据
	 */
	@Value("${ceph.monitor}")
	private String CEPH_MONITOR;

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

		List<Tensorflow> tensorflows = tensorflowDao.findByNamespace(user.getNamespace());
		List<TensorflowImage> tensorflowImages = new ArrayList<TensorflowImage>();
		Iterable<TensorflowImage> iterable = tensorflowImageDao.findAll();
		if (null != iterable) {
			Iterator<TensorflowImage> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				tensorflowImages.add(iterator.next());
			}
		}

		serviceController.getNginxServer(model);
		model.addAttribute("rbds", cephController.getUnUsedCephRbd());
		model.addAttribute("images", tensorflowImages);
		model.addAttribute("tensorflows", tensorflows);
		model.addAttribute("username", user.getUserName());
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "tensorflow");
		return "service/tensorflow.jsp";
	}

	/**
	 * 显示指定的tensorflow
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String showSpecifiedTensorflow(Model model,@PathVariable long id){
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

		//List<Tensorflow> tensorflows = tensorflowDao.findByNamespace(user.getNamespace());
		List<Tensorflow> tensorflows = new ArrayList<Tensorflow>();
		tensorflows.add(tensorflowDao.findOne(id));

		List<TensorflowImage> tensorflowImages = new ArrayList<TensorflowImage>();
		Iterable<TensorflowImage> iterable = tensorflowImageDao.findAll();
		if (null != iterable) {
			Iterator<TensorflowImage> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				tensorflowImages.add(iterator.next());
			}
		}

		serviceController.getNginxServer(model);
		model.addAttribute("rbds", cephController.getUnUsedCephRbd());
		model.addAttribute("images", tensorflowImages);
		model.addAttribute("tensorflows", tensorflows);
		model.addAttribute("username", user.getUserName());
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "tensorflow");
		return "service/tensorflow.jsp";
	}

	/**
	 * 新增
	 *
	 * @param tensorflow
	 * @return
	 */
	@RequestMapping(value = { "/add.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String addTensorFlow(Tensorflow tensorflow) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		if (NGINXCONF_IO) {
			tensorflow.setUrl("http://" + user.getUserName() + "." + SERVERADDR);
		} else {
			tensorflow.setUrl(SERVERADDR);
		}
		tensorflow.setUrl(tensorflow.getUrl() + "/" + tensorflow.getName());

		int mapPort = serviceController.vailPortSet();
		if (-1 == mapPort) {
			map.put("status", "500");
			return JSON.toJSONString(map);
		}
		smalSet.add(mapPort);

		tensorflow.setContainerPort(CONTAINER_PORT);
		tensorflow.setNodePort("" + mapPort);
		tensorflow.setPassword(tensorflow.getPassword());
		tensorflow.setCreateDate(new Date());
		tensorflow.setNamespace(user.getNamespace());
		tensorflow.setCreateBy(user.getId());
		tensorflowDao.save(tensorflow);

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
	public String updateTensorFlow() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

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
	public String deleteTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		Tensorflow tensorflow = tensorflowDao.findOne(id);

		deleteSvcAndRc(tensorflow);

		long rbdId = tensorflow.getRbdId();
		if (rbdId != 0) {
			CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(rbdId);
			cephRbdInfo.setUsed(false);
			cephRbdInfoDao.save(cephRbdInfo);
		}

		smalSet.remove(tensorflow.getNodePort());
		tensorflowDao.delete(tensorflow);

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
	public String detailTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Tensorflow tensorflow = tensorflowDao.findOne(id);

		map.put("tensorflow", tensorflow);
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
	public String tensorFlowExist(String name) {
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

		Tensorflow tensorflow = tensorflowDao.findByNamespaceAndName(user.getUserName(), name.trim());
		if (tensorflow != null) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 挂载cephrbd 固定挂载路径/data
	 *
	 * @param controller
	 * @param tensorflow
	 * @return
	 */
	private ReplicationController mountCephRbd(ReplicationController controller, Tensorflow tensorflow) {
		long rbdId = tensorflow.getRbdId();
		if (rbdId != 0l) {
			ReplicationControllerSpec rcSpec = controller.getSpec();
			PodTemplateSpec template = rcSpec.getTemplate();
			PodSpec podSpec = template.getSpec();
			List<Volume> volumes = new ArrayList<Volume>();
			List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();

			Volume volume = new Volume();
			volume.setName(tensorflow.getName());
			CephRbd cephrbd = new CephRbd();
			List<String> monitors = new ArrayList<String>();
			String[] ceph_monitors = CEPH_MONITOR.split(",");
			for (String ceph_monitor : ceph_monitors) {
				monitors.add(ceph_monitor);
			}
			cephrbd.setMonitors(monitors);
			cephrbd.setPool(CurrentUserUtils.getInstance().getUser().getNamespace());
			cephrbd.setImage(tensorflow.getRbd());
			cephrbd.setUser("admin");
			cephrbd.setFsType("ext4");
			cephrbd.setReadOnly(false);
			LocalObjectReference secretRef = new LocalObjectReference();
			secretRef.setName("ceph-secret");
			cephrbd.setSecretRef(secretRef);
			volume.setRbd(cephrbd);
			volumes.add(volume);

			VolumeMount volumeMount = new VolumeMount();
			volumeMount.setMountPath("/data");
			volumeMount.setName(tensorflow.getName());
			volumeMounts.add(volumeMount);

			podSpec.setVolumes(volumes);
			List<com.bonc.epm.paas.kubernetes.model.Container> containers = podSpec.getContainers();
			for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
				container.setVolumeMounts(volumeMounts);
			}
		}
		return controller;
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

		Tensorflow tensorflow = tensorflowDao.findOne(id);

		List<PortConfig> portConfigs = new ArrayList<PortConfig>();
		PortConfig portConfig = new PortConfig();
		portConfig.setContainerPort(tensorflow.getContainerPort());
		portConfig.setMapPort(tensorflow.getNodePort());
		portConfig.setProtocol("TCP");
		portConfigs.add(portConfig);

		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		try {
			// 创建服务
			com.bonc.epm.paas.kubernetes.model.Service k8sService = kubernetesClientService.generateService(
					tensorflow.getName(), portConfigs, tensorflow.getProxyZone(), tensorflow.getName(), null);
			System.out.println(JSON.toJSON(k8sService).toString());
			k8sService = client.createService(k8sService);
		} catch (KubernetesClientException e) {
			LOG.error("tensorflow" + tensorflow.getName() + "服务创建失败!");
			e.printStackTrace();
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		String image = REGISTRY_ADDR + "/" + tensorflow.getImage();

		List<String> commands = new ArrayList<String>();
		commands.add("start-notebook.sh");

		List<String> args = new ArrayList<String>();
		args.add("--NotebookApp.password=sha1:111111111111:"
				+ DigestUtils.sha1Hex(tensorflow.getPassword() + "111111111111"));
		args.add("--NotebookApp.base_url=/" + tensorflow.getName());

		String path="http://" + user.getUserName() + "." + SERVERADDR;
		args.add("--NotebookApp.allow_origin="+path);

		args.add("--NotebookApp.notebook_dir=/data");

		try {
			// 创建rc
			ReplicationController controller = kubernetesClientService.generateSimpleReplicationController(
					tensorflow.getName(), 1, null, null, null, image, portConfigs, (double) (tensorflow.getCpu()),
					String.valueOf(tensorflow.getMemory()*1024l), tensorflow.getProxyZone(), "", "", null, commands, args,
					null, false);
			controller = mountCephRbd(controller, tensorflow);
			controller = client.createReplicationController(controller);
		} catch (KubernetesClientException e) {
			LOG.error("tensorflow" + tensorflow.getName() + "RC创建失败!");
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		// 保存rbd使用状态信息
		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(tensorflow.getRbdId());
		if (null != cephRbdInfo) {
			cephRbdInfo.setUsed(true);
			cephRbdInfoDao.save(cephRbdInfo);
		}

		tensorflow.setStatus(1);
		tensorflowDao.save(tensorflow);
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

		Tensorflow tensorflow = tensorflowDao.findOne(id);

		deleteSvcAndRc(tensorflow);

		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(tensorflow.getRbdId());
		if (null != cephRbdInfo) {
			cephRbdInfo.setUsed(false);
			cephRbdInfoDao.save(cephRbdInfo);
		}

		tensorflow.setStatus(0);
		tensorflowDao.save(tensorflow);
		return JSON.toJSONString(map);
	}

	/**
	 * 删除rc和svc
	 *
	 * @param tensorflow
	 */
	private void deleteSvcAndRc(Tensorflow tensorflow) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		ReplicationController controller = null;
		try {
			controller = client.getReplicationController(tensorflow.getName());
		} catch (Exception e1) {
			controller = null;
		}

		if (controller != null) {
			controller = client.updateReplicationController(tensorflow.getName(), 0);
			try {
				client.deleteReplicationController(tensorflow.getName());
				client.deleteService(tensorflow.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 增加tensorflowimage
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

		TensorflowImage tensorflowImage = new TensorflowImage();
		tensorflowImage.setName(name);
		tensorflowImage.setVersion(version);
		tensorflowImage.setCreateDate(new Date());
		tensorflowImageDao.save(tensorflowImage);

		return JSON.toJSONString(map);
	}
}
