package com.bonc.epm.paas.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.constant.StorageConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.ConfigmapDao;
import com.bonc.epm.paas.dao.EnvTemplateDao;
import com.bonc.epm.paas.dao.EnvVariableDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.dao.RefServiceDao;
import com.bonc.epm.paas.dao.ServiceAndStorageDao;
import com.bonc.epm.paas.dao.ServiceConfigmapDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.ServiceOperationLogDao;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.dao.UserFavorDao;
import com.bonc.epm.paas.docker.model.LogStreamContainerResultCallback;
import com.bonc.epm.paas.docker.model.LogStringContainerResultCallback;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiCodeHook;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.CommonOprationLogUtils;
import com.bonc.epm.paas.entity.Configmap;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.ServiceAndStorage;
import com.bonc.epm.paas.entity.ServiceConfigmap;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserFavor;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.CephFSVolumeSource;
import com.bonc.epm.paas.kubernetes.model.ContainerStatus;
import com.bonc.epm.paas.kubernetes.model.LocalObjectReference;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.PodSpec;
import com.bonc.epm.paas.kubernetes.model.PodTemplateSpec;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerSpec;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceRequirements;
import com.bonc.epm.paas.kubernetes.model.Volume;
import com.bonc.epm.paas.kubernetes.model.VolumeMount;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.kubernetes.util.ResourceService;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.util.SheraClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.PoiUtils;
import com.bonc.epm.paas.util.RandomString;
import com.bonc.epm.paas.util.ResultPager;
import com.bonc.epm.paas.util.SshConnect;
import com.bonc.epm.paas.util.WebClientUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.FrameReader;

/**
 * ServiceController
 *
 * @author fengtao
 * @version 2016年9月7日
 * @see ServiceController
 * @since
 */
@Controller
public class ServiceController {

	/**
	 * ServiceController日志实例
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ServiceController.class);

	/**
	 * smalSet
	 */
	private static Set<Integer> smalSet = new HashSet<Integer>();

	/**
	 * 用户层接口
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * 用户偏好层接口
	 */
	@Autowired
	private UserFavorDao userFavorDao;

	/**
	 * 服务数据层接口
	 */
	@Autowired
	private ServiceDao serviceDao;

	/**
	 * 服务操作记录信息层接口
	 */
	@Autowired
	private ServiceOperationLogDao serviceOperationLogDao;

	/**
	 * 通用操作记录信息层接口
	 */
	@Autowired
	private CommonOperationLogDao commonOperationLogDao;

	/**
	 * 环境变量数据层接口
	 */
	@Autowired
	private EnvVariableDao envVariableDao;

	/**
	 * 配置文件数据层接口
	 */
	@Autowired
	private ConfigmapDao configmapDao;

	/**
	 * 服务与配置文件数据层接口
	 */
	@Autowired
	private ServiceConfigmapDao serviceConfigmapDao;

	/**
	 * portConfig数据层接口
	 */
	@Autowired
	private PortConfigDao portConfigDao;

	/**
	 * refService数据层接口
	 */
	@Autowired
	private RefServiceDao refServiceDao;

	/**
	 * 镜像数据接口
	 */
	@Autowired
	private ImageDao imageDao;

	/**
	 * 构建数据接口
	 */
	@Autowired
	private CiDao ciDao;

	/**
	 * stroge数据接口
	 */
	@Autowired
	private StorageDao storageDao;

	/**
	 * 服务和挂载卷之间的关联数据接口
	 */
	@Autowired
	private ServiceAndStorageDao serviceAndStorageDao;

	/**
	 * 环境变量模板数据接口
	 */
	@Autowired
	private EnvTemplateDao envTemplateDao;

	/**
	 * DockerClientService接口
	 */
	@Autowired
	private DockerClientService dockerClientService;

	/**
	 * KubernetesClientService接口
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	/**
	 * sheraClientService接口
	 */
	@Autowired
	private SheraClientService sheraClientService;

	/**
	 * TemplateConf
	 */
	@Value("${nginxConf.io}")
	private boolean NGINXCONF_IO;

	/**
	 * TemplateConf
	 */
	@Value("${nginxConf.io.serverAddr}")
	private String SERVERADDR;

	/**
	 * 获取ceph.monitor数据
	 */
	@Value("${ceph.monitor}")
	private String CEPH_MONITOR;

	@Value ("${ratio.limittorequestcpu}")
    private int RATIO_LIMITTOREQUESTCPU;

	@Value ("${ratio.limittorequestmemory}")
    private int RATIO_LIMITTOREQUESTMEMORY;

	/**
	 * 获取nginx中的服务分区
	 */
	@Value("${nginx.service.zone}")
	private String NGINX_SERVICE_ZONE;

	/**
	 * 服务日志显示的最大行数
	 */
	@Value("${docker.log.tail}")
	private Integer DOCKER_LOG_TAIL = 1000;

	/**
	 * 服务日志显示的文字最大值
	 */
	@Value("${docker.log.size}")
	private Integer DOCKER_LOG_SIZE = 524288;

	/**
	 * 服务日志显示等候时长
	 */
	@Value("${docker.log.await}")
	private Integer DOCKER_LOG_AWAIT = 3;

	/**
	 * 服务日志下载等候时长
	 */
	@Value("${docker.log.download}")
	private Integer DOCKER_LOG_DOWNLOAD = 30;

	/**
	 * docker api 端口
	 */
	@Value("${docker.io.port}")
	private Integer DOCKER_IO_PORT;

	/**
	 * cpu大小
	 */
	@Value("${service.cpu.size}")
	private String SERVICE_CPU_SIZE;

	/**
	 * memory大小
	 */
	@Value("${service.memory.size}")
	private String SERVICE_MEMORY_SIZE;

	/**
	 * entry地址
	 */
	@Value("${entry.host}")
	private String ENTRY_HOST;

    /**
     * server api 地址
     */
    @Value("${kubernetes.api.address}")
	private String KUBERNETES_API_ADDRESS;

    /**
     * server api url
     */
    @Value("${kubernetes.api.endpoint}")
	private String KUBERNETES_API_ENDPOINT;

    /**
	 * resourcecontroller接口
	 */
	@Autowired
	private ResourceService resourceService;

    /**
	 * Description: <br>
	 * 展示container和services
	 *
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "service" }, method = RequestMethod.GET)
	public String containerLists(Model model, String userName) {
		// 获取特殊条件的pods
		try {
			if (!CurrentUserUtils.getInstance().getUser().getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
				getleftResource(model);
			}

		} catch (KubernetesClientException e) {
			model.addAttribute("msg", e.getStatus().getMessage());
			LOG.debug("service show:" + e.getStatus().getMessage());
			return "workbench.jsp";
		}
		// 获取cpu大小设置
		String[] cpuSize = SERVICE_CPU_SIZE.split(",");
		List<Double> cpuSizeList = new ArrayList<>();
		for (int i = 0; i < cpuSize.length; i++) {
			cpuSizeList.add(Double.parseDouble(cpuSize[i]));
		}
		// 获取memory大小设置
		String[] memorySize = SERVICE_MEMORY_SIZE.split(",");
		List<Object> memorySizeList = new ArrayList<>();
		for (int i = 0; i < memorySize.length; i++) {
			Map<String, Double> map = new HashMap<>();
			map.put("memorySize", Double.parseDouble(memorySize[i]));
			map.put("memoryValue", Double.parseDouble(memorySize[i]) * 1024);
			memorySizeList.add(map);
		}
		model.addAttribute("cpuSizeList", cpuSizeList);
		model.addAttribute("memorySizeList", memorySizeList);
		model.addAttribute("userName", userName);
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "service");
		return "service/service.jsp";
	}

	/**
	 * Description: <br>
	 * 服务在服务端分页查询
	 *
	 * @param draw
	 *            画板
	 * @param start
	 *            开始页数
	 * @param length
	 *            每页的条数
	 * @param request
	 *            获取模糊查询的数据
	 * @return
	 * @see
	 */
	@RequestMapping(value = { "service/page.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String findServiceByPage(String draw, int start, int length, HttpServletRequest request,
			String searchService, String searchImage, String searchCreatorName) {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		String search = request.getParameter("search[value]");
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Service> services = null;
		PageRequest pageRequest = null;
		// 判断是第几页
		if (start == 0) {
			pageRequest = ResultPager.buildPageRequest(null, length);
		} else {
			pageRequest = ResultPager.buildPageRequest(start / length + 1, length);
		}
		// 判断是否是admin
		if (!CurrentUserUtils.getInstance().getUser().getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			// 判断是否需要搜索服务
			if (StringUtils.isEmpty(search)) {
				services = serviceDao.findByCreateBy(userId, pageRequest);
			} else {
				services = serviceDao.findByNameOf(userId, "%" + search + "%", pageRequest);
			}
		} else {
			// admin用户显示一览界面
			services = serviceDao.search("%" + (searchService != null ? searchService : "") + "%",
					"%" + (searchImage != null ? searchImage : "") + "%",
					"%" + (searchCreatorName != null ? searchCreatorName : "") + "%", pageRequest);
			for (Service service : services.getContent()) {
				service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
			}
		}
		// 判断代码仓库中的代码是否发生改变
		List<Service> listService = findIsUpdateCode(services.getContent());
		map.put("draw", draw);
		map.put("recordsTotal", services.getTotalElements());
		map.put("recordsFiltered", services.getTotalElements());
		map.put("data", listService);

		return JSON.toJSONString(map);

	}

	/**
	 * Description: <br>
	 * 查询代码构建中的代码是否更新，服务中添加提醒代码更新
	 *
	 * @param listService
	 *            需要查询的服务
	 * @return
	 * @see
	 */
	public List<Service> findIsUpdateCode(List<Service> listService) {
		for (Service service : listService) {
			CiCodeHook ciCodeHook = serviceDao.findByImgId(service.getImgID());
			if (ciCodeHook != null) {
				try {
					SheraAPIClientInterface client = sheraClientService.getClient();
					ChangeGit changeGit = client.getChangeGit(ciCodeHook.getName());
					service.setUpdateImage(changeGit.isFlag());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return listService;
	}



	/**
	 * Description: <br>
	 * 获取nginxService参数
	 *
	 * @param model
	 */
	public void getNginxServer(Model model) {
		List<String> serviceZone = new ArrayList<>();
		if (StringUtils.isNoneBlank(NGINX_SERVICE_ZONE)) {
			String[] zoneArray = NGINX_SERVICE_ZONE.split(",");
			for (String zone : zoneArray) {
				serviceZone.add(zone);
			}
		}
		model.addAttribute("zoneList", serviceZone);
	}

	/**
	 * Description: <br>
	 * 根据服务Id查询当前服务和容器
	 *
	 * @param model
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = { "service/{id}" }, method = RequestMethod.GET)
	public String service(Model model, @PathVariable long id) {
		Service service = serviceDao.findOne(id);
		model.addAttribute("service", service);
		model.addAttribute("menu_flag", "service");
		return "service/service.jsp";
	}

	/**
	 * Description: <br>
	 * 根据Id查找container和servies，跳转进入服务详细页面
	 *
	 * @param model
	 * @param id
	 *            服务Id
	 * @return String
	 */
	@RequestMapping(value = { "service/detail/{id}" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
		System.out.printf("id: " + id);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		Service service = serviceDao.findOne(id);
		List<EnvVariable> envVariableList = envVariableDao.findByServiceId(id);
		List<PortConfig> portConfigList = portConfigDao.findByServiceId(service.getId());

		List<Configmap> configmapList = configmapDao.findByCreateBy(currentUser.getId());
		List<ServiceConfigmap> serviceConfigmapList = serviceConfigmapDao.findByServiceId(id);
		ServiceConfigmap serviceConfigmap = null;
		if(!CollectionUtils.isEmpty(serviceConfigmapList)){
			serviceConfigmap = serviceConfigmapList.get(0);
		}

		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<com.bonc.epm.paas.entity.Pod> podNameList = new ArrayList<com.bonc.epm.paas.entity.Pod>();
		List<Container> containerList = new ArrayList<Container>();
		List<Pod> podList = new ArrayList<>();
		// 通过服务名获取pod列表
		if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING) {
			com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
			try {
				k8sService = client.getService(service.getServiceName());
			} catch (Exception e) {
				k8sService = null;
			}
			if (k8sService != null) {
				PodList pods = client.getLabelSelectorPods(k8sService.getSpec().getSelector());
				if (pods != null) {
					podList = pods.getItems();
					if (CollectionUtils.isNotEmpty(podList)) {
						int i = 1;
						for (Pod pod : podList) {
							for (@SuppressWarnings("unused")
							com.bonc.epm.paas.kubernetes.model.Container k8scontainer : pod.getSpec().getContainers()) {
								Container container = new Container();
								container.setContainerName(
										service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
								container.setServiceid(service.getId());
								containerList.add(container);
							}
							com.bonc.epm.paas.entity.Pod current_pod = new com.bonc.epm.paas.entity.Pod();
							current_pod.setPodName(pod.getMetadata().getName());
							podNameList.add(current_pod);
						}
					}
				}
			}
		}
		List<Storage> storageList = storageDao.findByServiceId(service.getId());

		model.addAttribute("entryHost", ENTRY_HOST);
		model.addAttribute("dockerIOPort", DOCKER_IO_PORT);
		model.addAttribute("configmapList", configmapList);
		model.addAttribute("serviceConfigmap", serviceConfigmap);
		model.addAttribute("storageList", storageList);
		model.addAttribute("namespace", currentUser.getNamespace());
		model.addAttribute("id", id);
		model.addAttribute("podNameList", podNameList);
		model.addAttribute("podList", podList);
		model.addAttribute("containerList", containerList);
		model.addAttribute("service", service);
		model.addAttribute("envVariableList", envVariableList);
		model.addAttribute("portConfigList", portConfigList);
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "service");
		return "service/service-detail.jsp";
	}

	/**
	 * Description: <br>
	 * 将字符串重新组装 主要用于proxyZone的重新组装
	 *
	 * @param str
	 * @return String
	 */
	public String substr(String str) {
		String strs = "";
		String[] splitstr = str.split("'");
		for (int i = 0; i < splitstr.length; i++) {
			if ((i + 1) % 4 == 0) {
				strs += splitstr[i] + ",";
			}
		}
		strs = strs.substring(0, strs.length() - 1);
		return strs;
	}

	/**
	 * Description: <br>
	 * 日期格式的转换
	 *
	 * @param time
	 * @return String
	 */
	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy.MM.dd");
		String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * Description: <br>
	 * 响应“部署”按钮
	 *
	 * @param imgID
	 * @param imageName
	 * @param imageVersion
	 * @param resourceName
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "service/add" }, method = RequestMethod.GET)
	public String create(String imgID, String imageName, String imageVersion, String resourceName, Model model) {
		User currentUser = CurrentUserUtils.getInstance().getUser();
		String isDepoly = "";
		if (imageName != null) {
			isDepoly = "deploy";
			// 获取基础镜像的暴露端口信息
			List<PortConfig> list = getBaseImageExposedPorts(imgID);
			if (null == list) {
				list = new ArrayList<PortConfig>();
				PortConfig portConfig = new PortConfig();
				portConfig.setContainerPort("8080");
				int randomPort = vailPortSet();
				if (-1 != randomPort) {
					portConfig.setMapPort(String.valueOf(randomPort));
				} else {
					portConfig.setMapPort("-1");
				}
				list.add(portConfig);
			}
			model.addAttribute("portConfigs", JSON.toJSONString(list));
		}
		boolean flag = getleftResource(model);
		if (!flag) {
			model.addAttribute("msg", "请创建租户！");
			return "service/service.jsp";
		}
		// 获取配置文件中nginx选择区域
		getNginxServer(model);
		User cUser = CurrentUserUtils.getInstance().getUser();
		// 获取未使用的存储卷,租户获取租户创建的，用户获取租户和用户创建的
		long createBy = cUser.getId();
		long parentId = cUser.getParent_id();
		List<Storage> storageList = storageDao.findByCreateByAndUseTypeOrderByCreateDateDesc(createBy, 1);
		if (parentId != 1) {
			for (Storage storage : storageDao.findByCreateByAndUseTypeOrderByCreateDateDesc(parentId, 1)) {
				storageList.add(storage);
			}
		}

		// 获取监控配置
		UserFavor userFavor = userFavorDao.findByUserId(currentUser.getId());
		Integer monitor;
		if (null == userFavor) {
			monitor = ServiceConstant.MONITOR_PINPOINT;
		} else {
			monitor = userFavor.getMonitor();
		}
		// 获取cpu大小设置
		String[] cpuSize = SERVICE_CPU_SIZE.split(",");
		List<Double> cpuSizeList = new ArrayList<>();
		for (int i = 0; i < cpuSize.length; i++) {
			cpuSizeList.add(Double.parseDouble(cpuSize[i]));
		}
		// 获取memory大小设置
		String[] memorySize = SERVICE_MEMORY_SIZE.split(",");
		List<Object> memorySizeList = new ArrayList<>();
		for (int i = 0; i < memorySize.length; i++) {
			Map<String, Double> map = new HashMap<>();
			map.put("memorySize", Double.parseDouble(memorySize[i]));
			map.put("memoryValue", Double.parseDouble(memorySize[i]) * 1024);
			memorySizeList.add(map);
		}

		List<Configmap> configmapList = configmapDao.findByCreateBy(currentUser.getId());

		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
        int nodecount = client.getAllNodes().getItems().size();

        model.addAttribute("nodecount",nodecount);
		model.addAttribute("configmapList", configmapList);
		model.addAttribute("cpuSizeList", cpuSizeList);
		model.addAttribute("memorySizeList", memorySizeList);
		model.addAttribute("userName", currentUser.getUserName());
		model.addAttribute("storageList", storageList);
		model.addAttribute("imgID", imgID);
		model.addAttribute("resourceName", resourceName);
		model.addAttribute("imageName", imageName);
		model.addAttribute("imageVersion", imageVersion);
		model.addAttribute("isDepoly", isDepoly);
		model.addAttribute("menu_flag", "service");
		model.addAttribute("monitor", monitor);
		return "service/service_create.jsp";
	}

	// /**
	// * Description: <br>
	// * 获取镜像的启动命令
	// * @param imgName
	// * @param imgVersion
	// * @return List
	// */
	// private String getBaseImageStartCommand(String imgName, String
	// imgVersion) {
	// StringBuffer startCommand = new StringBuffer();
	// try {
	// //获取镜像信息
	// Image image = imageDao.findByNameAndVersion(imgName, imgVersion);
	// if (null != image) {
	// //从仓库中拉取镜像到本地
	// dockerClientService.pullImage(image.getName(), image.getVersion());
	// //获取镜像inspect
	// InspectImageResponse iir =
	// dockerClientService.inspectImage(image.getImageId(),image.getName(),image.getVersion());
	// if (null != iir) {
	// //获取Entrypoint信息
	// if (null != iir.getConfig().getEntrypoint()) {
	// for (String entrypoint : iir.getConfig().getEntrypoint()) {
	// startCommand.append(entrypoint);
	// }
	// }
	// //获取cmd信息
	// if (null != iir.getConfig().getCmd()) {
	// for (String cmd : iir.getConfig().getCmd()) {
	// if (startCommand.length() > 0) {
	// startCommand.append(" ");
	// }
	// startCommand.append(cmd);
	// }
	// }
	// }
	// }
	// }
	// catch (Exception e) {
	// LOG.error(e.getMessage());
	// e.printStackTrace();
	// }
	// return startCommand.toString();
	// }


	/**
	 * Description: <br>
	 * 获取基础镜像的端口
	 *
	 * @param imgID
	 *            imgID
	 * @return List
	 */
	private List<PortConfig> getBaseImageExposedPorts(String imgID) {
		try {
			Image image = imageDao.findById(Long.valueOf(imgID));
			if (null == image) {
				Ci ci = ciDao.findByImgId(Long.valueOf(imgID));
				if (null != ci) {
					image = imageDao.findById(ci.getBaseImageId());
				}
			}
			if (null != image) {
				dockerClientService.pullImage(image.getName(), image.getVersion());
				InspectImageResponse iir = dockerClientService.inspectImage(image.getImageId(), image.getName(),
						image.getVersion());
				if (null != iir) {
					long countOfExposedPort = 0;
					if (iir.getContainerConfig().getExposedPorts() != null) {
						countOfExposedPort = iir.getContainerConfig().getExposedPorts().length;
					}
					if (countOfExposedPort > 0) {
						ExposedPort[] exposedPorts = iir.getContainerConfig().getExposedPorts();
						List<PortConfig> tmpPortConfigs = new ArrayList<PortConfig>();
						for (int i = 0; i < countOfExposedPort; i++) {
							PortConfig portConfig = new PortConfig();
							portConfig.setContainerPort(String.valueOf(exposedPorts[i].getPort()));
							int randomPort = vailPortSet();
							if (-1 != randomPort) {
								portConfig.setMapPort(String.valueOf(randomPort));
							} else {
								portConfig.setMapPort("-1");
							}
							tmpPortConfigs.add(portConfig);
						}
						return tmpPortConfigs;
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: <br>
	 * getPortConfig 获取port端口
	 *
	 * @param imgID
	 *            imgID
	 * @return String
	 */
	@RequestMapping(value = { "service/getPortConfig.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getPortConfig(String imgID) {
		List<PortConfig> list = getBaseImageExposedPorts(imgID);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == list) {
			list = new ArrayList<PortConfig>();
			PortConfig portConfig = new PortConfig();
			portConfig.setContainerPort("8080");
			int randomPort = vailPortSet();
			if (-1 != randomPort) {
				portConfig.setMapPort(String.valueOf(randomPort));
			} else {
				portConfig.setMapPort("-1");
			}
			list.add(portConfig);
		}
		map.put("data", list);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 获取当前租户的剩余资源
	 *
	 * @param model
	 * @return boolean
	 */
	public boolean getleftResource(Model model) {
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

				model.addAttribute("leftcpu", leftCpu);
				model.addAttribute("leftmemory", Math.ceil(leftmemory / 1024.0));
			} else {
				LOG.info("用户 " + currentUser.getUserName() + " 没有定义名称为 " + currentUser.getNamespace() + " 的Namespace ");
			}

		} catch (Exception e) {
			LOG.error("getleftResource error:" + e);
			return false;
		}

		return true;
	}

	/**
	 * Description: <br>
	 * 展示镜像
	 *
	 * @return String
	 */
	@RequestMapping(value = { "service/images" }, method = RequestMethod.GET)
	@ResponseBody
	public String imageList() {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Image> images = imageDao.findAll(userId);
		map.put("data", images);
		System.out.println(JSON.toJSONString(map));
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 根据镜像名称模糊查询镜像
	 *
	 * @param imageName
	 *            镜像名称
	 * @return String
	 */
	@RequestMapping("service/findimages.do")
	@ResponseBody
	public String findimages(String imageName) {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Image> images = imageDao.findByNameOf(userId, "%" + imageName + "%");
		map.put("data", images);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 响应服务的开始和debug按钮
	 *
	 * @param id
	 * @return String
	 */
	@RequestMapping("service/createContainer.do")
	@ResponseBody
	public String CreateContainer(long id, boolean isDebug) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}

		/**
		 * 检查运行该服务之后，剩余的cpu和memory是否满足预留条件
		 */
		boolean chkresult = resourceService.checkRestResource(service);
		if(!chkresult){
			map.put("msg", "cpu或内存不足，请调整资源大小或申请更多资源！");
			map.put("status", "504");
			return JSON.toJSONString(map);
		}

		delPods(service.getServiceName());
		List<EnvVariable> envVariables = envVariableDao.findByServiceId(id);
		List<PortConfig> portConfigs = portConfigDao.findByServiceId(service.getId()); // 获取服务对应的端口映射
		// 使用k8s管理服务
		String registryImgName = dockerClientService.generateRegistryImageName(service.getImgName(),
				service.getImgVersion());
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
		ReplicationController controller = null;

		/*************************************
		 * 服务不是未启动状态时,获取svc和rc
		 *************************************/
		if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING) {
			try {
				// 获取svc
				k8sService = client.getService(service.getServiceName());
			} catch (KubernetesClientException e) {
				k8sService = null;
			}
			try {
				// 获取rc
				controller = client.getReplicationController(service.getServiceName());
			} catch (KubernetesClientException e) {
				controller = null;
			}
		}
		/*************************************
		 * 判断服务信息是否有改动，有改动则删除rc和svc
		 *************************************/
		if (service.getIsModify() == ServiceConstant.MODIFY_TRUE || isDebug) {
			if (delSvcAndRc(service) == false) {
				map.put("status", "500");
				return JSON.toJSONString(map);
			}
			k8sService = null;
			controller = null;
		}

		/***************************************
		 * 如果没有找到svc则创建一个新的svc
		 ***************************************/
		if (k8sService == null) {
			try {
				k8sService = kubernetesClientService.generateService(service.getServiceName(), portConfigs,
						service.getProxyZone(), service.getServicePath(), service.getProxyPath(),
						service.getSessionAffinity(), service.getNodeIpAffinity());
				k8sService = client.createService(k8sService);
			} catch (KubernetesClientException e) {
				e.printStackTrace();
				map.put("status", "500");
				map.put("msg", e.getStatus().getMessage());
				LOG.error("create service error:" + e.getStatus().getMessage());
				return JSON.toJSONString(map);
			}
		}
		// svc依然为null的时候返回500
		if (k8sService == null) {
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		/*************************************
		 * 根据serviceId,查找ServiceConfigmap
		 *************************************/
		List<ServiceConfigmap> serviceConfigmapList = serviceConfigmapDao.findByServiceId(service.getId());

		/*************************************
		 * 先查询rc是否已经创建，如果没有找到则创建一个新的rc
		 *************************************/
		try {
			// 如果没有则新增
			if (controller == null) {
				List<String> command = new ArrayList<String>();
				List<String> args = new ArrayList<String>();
				// 初始化自定义启动命令
				String startCommand = service.getStartCommand().trim();
				// debug模式下
				if (isDebug) {
					command.add("sleep");
					args.add("3153600000");
				} else if (StringUtils.isNotBlank(startCommand)) {
					String[] startCommandArray = startCommand.replaceAll("\\s+", " ").trim().split(" ");
					for (String item : startCommandArray) {
						if (CollectionUtils.isEmpty(command)) {
							command.add(item);
							continue;
						}
						args.add(item);
					}
				}
				controller = kubernetesClientService.generateSimpleReplicationController(service.getServiceName(),
						service.getInstanceNum(), service.getInitialDelay(), service.getTimeoutDetction(),
						service.getPeriodDetction(), registryImgName, portConfigs, service.getCpuNum(),
						service.getRam(), service.getProxyZone(), service.getServicePath(), service.getProxyPath(),
						service.getCheckPath(), envVariables, command, args,serviceConfigmapList,service.isIspodmutex());
				// 给controller设置卷组挂载的信息
				LOG.debug("给rc添加存储卷信息");
				if (service.getServiceType().equals("1")) {
					controller = this.setVolumeStorage(controller, service.getId());
				}
				controller = client.createReplicationController(controller);
			} else {
				List<com.bonc.epm.paas.kubernetes.model.Container> containers = controller.getSpec().getTemplate()
						.getSpec().getContainers();
				// 设置启动命令
				List<String> command = new ArrayList<String>();
				List<String> args = new ArrayList<String>();
				// 初始化自定义启动命令
				String startCommand = service.getStartCommand().trim();
				// debug模式下
				if (isDebug) {
					command.add("sleep");
					args.add("3153600000");
				}
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

				double cpu =service.getCpuNum();
				String memory = service.getRam();
				for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
					container.setCommand(command);
					container.setArgs(args);

					//重启服务时，重新设置container的资源，使得配置文件中的资源系数更改之后，能够生效
					ResourceRequirements requirements = new ResourceRequirements();

					Map<String, Object> def = new HashMap<String, Object>();
					def.put("cpu", cpu / RATIO_LIMITTOREQUESTCPU);
					def.put("memory", Double.parseDouble(memory)/RATIO_LIMITTOREQUESTMEMORY + "Mi");

					Map<String, Object> limit = new HashMap<String, Object>();
					limit.put("cpu", cpu);
					limit.put("memory", Double.parseDouble(memory) + "Mi");

					requirements.setRequests(def);
					requirements.setLimits(limit);

					container.setResources(requirements);
				}
				// 设置实例数量
				if (isDebug) {
					controller.getSpec().setReplicas(1);
				} else {
					controller.getSpec().setReplicas(service.getInstanceNum());
				}
				controller = client.updateReplicationController(service.getServiceName(), controller);
			}
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("create service error:" + e.getStatus().getMessage());
			return JSON.toJSONString(map);
		}
		if (controller == null) {
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		/*************************************
		 * 持久化相关信息
		 *************************************/
		// 保存service的信息
		if (isDebug) {
			service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_DEBUG);
		} else {
			service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_RUNNING);
		}
		Date currentDate = new Date();
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateDate(currentDate);
		service.setUpdateBy(currentUser.getId());
		service.setIsModify(ServiceConstant.MODIFY_FALSE);
		service = serviceDao.save(service);
		// 保存服务操作信息
		Integer operationType;
		if (isDebug) {
			operationType = ServiceConstant.OPERATION_TYPE_DEBUG;
		} else {
			operationType = ServiceConstant.OPERATION_TYPE_START;
		}
		serviceOperationLogDao.save(service.getServiceName(), service.toString(), operationType);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 服务创建
	 *
	 * @param service
	 * @param resourceName
	 * @param envVariable
	 * @param portConfig
	 * @return String
	 */
	@RequestMapping("service/constructContainer.do")
	public String constructContainer(Service service, String resourceName, String envVariable, String portConfig,
			String cephAds,String configmap,String configmapPath) {
		Date currentDate = new Date();
		User currentUser = CurrentUserUtils.getInstance().getUser();
		// 保存服务信息
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
		service.setCreateDate(currentDate);
		service.setCreateBy(currentUser.getId());
		service.setIsModify(ServiceConstant.MODIFY_FALSE);
		service.setUpdateDate(currentDate);
		service.setUpdateBy(currentUser.getId());
		service = serviceDao.save(service);

		if (StringUtils.isEmpty(service.getSessionAffinity())) {
			service.setSessionAffinity(null);
		}

		if(Long.parseLong(configmap)!=-1){//选择了configmap
		   Configmap configmap2 = configmapDao.findOne(Long.parseLong(configmap));
           ServiceConfigmap serviceConfigmap = new ServiceConfigmap();
           serviceConfigmap.setConfigmapId(Long.parseLong(configmap));
           serviceConfigmap.setServiceId(service.getId());
           serviceConfigmap.setCreateDate(currentDate);
           serviceConfigmap.setCreateBy(currentUser.getId());
           serviceConfigmap.setPath(configmapPath);
           serviceConfigmap.setName(configmap2.getName());
           serviceConfigmapDao.save(serviceConfigmap);
		}

		// 将服务中的环境变量循环遍历，保存到相关联的实体类中；
		if (StringUtils.isNotEmpty(envVariable)) {
			String[] envKeyAndValues = envVariable.split(";");
			for (String envKeyAndValue : envKeyAndValues) {
				EnvVariable envVar = new EnvVariable();
				envVar.setCreateBy(currentUser.getId());
				envVar.setEnvKey(envKeyAndValue.substring(0, envKeyAndValue.indexOf(",")));
				envVar.setEnvValue(envKeyAndValue.substring(envKeyAndValue.indexOf(",") + 1));
				envVar.setCreateDate(new Date());
				envVar.setServiceId(service.getId());
				envVariableDao.save(envVar);
			}
		}

		//增加环境变量api_server_ip和api_server_port
		EnvVariable ipVar = new EnvVariable();
		ipVar.setCreateBy(currentUser.getId());
		ipVar.setEnvKey("api_server_ip");
		ipVar.setEnvValue(KUBERNETES_API_ADDRESS);
		ipVar.setCreateDate(new Date());
		ipVar.setServiceId(service.getId());
		envVariableDao.save(ipVar);

		EnvVariable portVar = new EnvVariable();
		portVar.setCreateBy(currentUser.getId());
		portVar.setEnvKey("api_server_port");
		portVar.setEnvValue(KUBERNETES_API_ENDPOINT.split(":")[2].substring(0, 4));
		portVar.setCreateDate(new Date());
		portVar.setServiceId(service.getId());
		envVariableDao.save(portVar);

		// 增加Pinpoint的相关环境变量
		if (service.getMonitor() == ServiceConstant.MONITOR_PINPOINT) {
			// 使用Pinpoint监控时，需增加环境变量[namespace=服务的命名空间,service=服务名]
			EnvVariable envVar = new EnvVariable();
			envVar.setCreateBy(currentUser.getId());
			envVar.setEnvKey("namespace");
			envVar.setEnvValue(currentUser.getNamespace());
			envVar.setCreateDate(new Date());
			envVar.setServiceId(service.getId());
			envVariableDao.save(envVar);
			EnvVariable envVar2 = new EnvVariable();
			envVar2.setCreateBy(currentUser.getId());
			envVar2.setEnvKey("service");
			envVar2.setEnvValue(service.getServiceName());
			envVar2.setCreateDate(new Date());
			envVar2.setServiceId(service.getId());
			envVariableDao.save(envVar2);
		} else {
			// 其他情况时，增加环境变量[APM=1]
			EnvVariable envVar = new EnvVariable();
			envVar.setCreateBy(currentUser.getId());
			envVar.setEnvKey("APM");
			envVar.setEnvValue("1");
			envVar.setCreateDate(new Date());
			envVar.setServiceId(service.getId());
			envVariableDao.save(envVar);
		}

		// 将服务中的挂载卷数据循环遍历，保存到相关联的实体类中
		if (StringUtils.isNotEmpty(cephAds)) {
			String[] cephAddressData = cephAds.split(";");
			for (String cephAddress : cephAddressData) {
				long storageId = Long.parseLong(cephAddress.substring(0, cephAddress.indexOf(",")));
				String mouthPath = cephAddress.substring(cephAddress.indexOf(",") + 1);
				// 保存服务和ceph关联数据
				ServiceAndStorage serviceAndStorage = new ServiceAndStorage();
				serviceAndStorage.setStorageId(storageId);
				serviceAndStorage.setServiceId(service.getId());
				serviceAndStorageDao.save(serviceAndStorage);
				// 更新存储卷信息
				Storage storage = storageDao.findOne(storageId);
				storage.setUseType(StorageConstant.IS_USER);
				storage.setMountPoint(mouthPath);
				storage.setUpdateBy(currentUser.getId());
				storage.setUpdateDate(new Date());
				storageDao.save(storage);

				// 记录更新storage存储卷
				String extraInfo = "更新storage存储卷 " + JSON.toJSONString(storage);
				CommonOperationLog log = CommonOprationLogUtils.getOprationLog(storage.getStorageName(), extraInfo,
						CommConstant.STORAGE, CommConstant.OPERATION_TYPE_CREATED);
				commonOperationLogDao.save(log);
			}
		}

		// 保存到与service关联的portConfig实体类
		if (StringUtils.isNotEmpty(portConfig)) {
			JSONArray jsonArray = JSONArray.parseArray(portConfig);
			for (int i = 0; i < jsonArray.size(); i++) {
				PortConfig portCon = new PortConfig();
				portCon.setContainerPort(jsonArray.getJSONObject(i).getString("containerPort").trim());
				portCon.setMapPort(jsonArray.getJSONObject(i).getString("mapPort").trim());
				portCon.setProtocol(jsonArray.getJSONObject(i).getString("protocol").trim());
				portCon.setServiceId(service.getId());
				portConfigDao.save(portCon);
				// 向map中添加生成的node端口
				smalSet.add(Integer.valueOf(portCon.getMapPort().trim()));
			}
		}
		if (NGINXCONF_IO) {
			service.setServiceAddr("http://" + currentUser.getUserName() + "." + SERVERADDR);
		} else {
			service.setServiceAddr(SERVERADDR);
		}

		// 获取代码质量
		Image image = imageDao.findByNameAndVersion(service.getImgName(), service.getImgVersion());
		service.setCodeRating(image.getCodeRating() == null ? 0 : image.getCodeRating());
		service.setCodeRatingURL(image.getCodeRatingURL() == null ? "" : image.getCodeRatingURL());

		service = serviceDao.save(service);

		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), service.toString(),
				ServiceConstant.OPERATION_TYPE_CREATE);

		LOG.debug("container--Name:" + service.getServiceName());
		return "redirect:/service";
	}

	/**
	 * 当前用户修改服务时服务名称不重复
	 *
	 * @param serviceName
	 * @return String
	 */
	@RequestMapping("service/matchServiceName.do")
	@ResponseBody
	public String matchServicePath(String serviceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		int refsize = refServiceDao.findByCreateByAndSerName(createBy, serviceName).size();
		int serSize = serviceDao.findByNameOf(createBy, serviceName).size();
		if (0 < refsize | 0 < serSize) {
			map.put("status", "500");
		} else {
			map.put("status", "200");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 当前用户创建服务时nginx路径不重复
	 *
	 * @param proxyPath
	 * @return String
	 */
	@RequestMapping("service/matchProxyPath.do")
	@ResponseBody
	public String matchProxyPath(String proxyPath) {
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		int proxySize = serviceDao.findByCreateByAndProxyPath(createBy, proxyPath).size();
		if (0 < proxySize) {
			map.put("status", "400");
		} else {
			map.put("status", "200");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 当前用户创建服务时匹配服务路径和nginx路径 和服务名称不重复
	 *
	 * @param proxyPath
	 * @param serviceName
	 * @return String
	 */
	@RequestMapping("service/matchPath.do")
	@ResponseBody
	public String matchServicePathAndProxyPath(String proxyPath, String serviceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		int refsize = refServiceDao.findByCreateByAndSerName(createBy, serviceName).size();
		int serSize = serviceDao.findByNameOf(createBy, serviceName).size();
		int proxySize = serviceDao.findByCreateByAndProxyPath(createBy, proxyPath).size();
		if (0 < refsize | 0 < serSize) {
			map.put("status", "500");
		} else if (0 < proxySize) {
			map.put("status", "400");
		} else {
			map.put("status", "200");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 加载环境变量模板数据
	 *
	 * @return String
	 */
	@RequestMapping("service/loadEnvTemplate.do")
	@ResponseBody
	public String loadEnvTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();
		User cUser = CurrentUserUtils.getInstance().getUser();
		List<String> templateNames = envTemplateDao.findTemplateName(cUser.getId());
		map.put("data", templateNames);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 保存环境变量模板
	 *
	 * @param templateName
	 *            模板名称
	 * @param envVariable
	 *            环境变量
	 * @return String
	 */
	@RequestMapping("service/saveEnvTemplate.do")
	@ResponseBody
	public String saveEnvTemplate(String templateName, String envVariable) {
		Map<String, Object> map = new HashMap<String, Object>();
		User cUser = CurrentUserUtils.getInstance().getUser();
		for (EnvTemplate envTemplate : envTemplateDao.findByCreateBy(cUser.getId())) {
			if (envTemplate.getTemplateName().equals(templateName)) {
				map.put("status", "400"); // 模板名称重复
				return JSON.toJSONString(map);
			}
		}
		if (StringUtils.isNotEmpty(envVariable)) {
			String[] envKeyAndValues = envVariable.split(";");
			for (String envKeyAndValue : envKeyAndValues) {
				EnvTemplate envTemplate = new EnvTemplate();
				envTemplate.setCreateBy(cUser.getId());
				envTemplate.setEnvKey(envKeyAndValue.substring(0, envKeyAndValue.indexOf(",")));
				envTemplate.setEnvValue(envKeyAndValue.substring(envKeyAndValue.indexOf(",") + 1));
				envTemplate.setCreateDate(new Date());
				envTemplate.setTemplateName(templateName);
				envTemplateDao.save(envTemplate);
			}
			map.put("status", "200");
		}

		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 查询用户的环境变量模板，导入到环境变量模板中
	 *
	 * @param templateName
	 *            需要导入的模板名称
	 * @return String
	 * @see
	 */
	@RequestMapping("service/importEnvTemplate.do")
	@ResponseBody
	public String findEnvTemplate(String templateName) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<EnvTemplate> envTemplates = envTemplateDao.findByCreateByAndTemplateName(cUser.getId(), templateName);
		map.put("data", envTemplates);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 生成有效的PORTSET,回收端口
	 *
	 * @return int
	 */
	public int vailPortSet() {
		synchronized (this) {
			int offset = kubernetesClientService.getK8sEndPort() - kubernetesClientService.getK8sStartPort();
			Set<Integer> bigSet = Stream.iterate(kubernetesClientService.getK8sStartPort(), item -> item + 1)
					.limit(offset).collect(Collectors.toSet());
			smalSet.addAll(portConfigDao.findPortSets());
			smalSet.addAll(refServiceDao.findPortSets());
			smalSet.remove(null);
			bigSet.removeAll(smalSet);
			if (CollectionUtils.isEmpty(bigSet)) {
				return -1;
			}
			Object[] obj = bigSet.toArray();
			int portSet = Integer.valueOf(obj[(int) (Math.random() * obj.length)].toString());
			PortConfig validPort = portConfigDao.findByMapPort(Integer.toString(portSet));
			// 监测数据库中是否存在相同的端口号
			while (null != validPort) {
				portSet = Integer.valueOf(obj[(int) (Math.random() * obj.length)].toString());
				validPort = portConfigDao.findByMapPort(Integer.toString(portSet));
			}
			smalSet.add(portSet);
			return portSet;
		}
	}

	/**
	 * Description: <br>
	 * generatePortSet 生成port端口
	 *
	 * @return String
	 */
	@RequestMapping(value = { "service/generatePortSet.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String generatePortSet() {
		Map<String, String> map = new HashMap<String, String>();
		int mapPort = vailPortSet();
		if (-1 == mapPort) {
			map.put("ERROR", "error");
			return JSON.toJSONString(map);
		}

		map.put("mapPort", String.valueOf(mapPort));
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 移除端口集合中的端口
	 *
	 * @param set
	 *            需要移除的端口
	 */
	@RequestMapping(value = { "service/removeSet.do" }, method = RequestMethod.GET)
	public void removeSet(int set) {
		LOG.info("移除的端口：" + set);
		smalSet.remove(set);
	}

	/**
	 * Description: <br>
	 * serviceName 判重
	 *
	 * @param serviceName
	 *            服务名称
	 * @return String
	 */
	@RequestMapping("service/serviceName.do")
	@ResponseBody
	public String containerName(String serviceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		User cUser = CurrentUserUtils.getInstance().getUser();
		for (Service service : serviceDao.findByCreateBy(cUser.getId())) {
			if (service.getServiceName().equals(serviceName)) {
				map.put("status", "400");
				break;
			} else {
				map.put("status", "200");
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 停止服务
	 *
	 * @param id
	 *            服务Id
	 * @return String
	 */
	@RequestMapping("service/stopContainer.do")
	@ResponseBody
	public String stopContainer(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_RUNNING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_DEBUG) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		LOG.debug("service:=========" + service);
		try {
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			ReplicationController controller = null;
			try {
				controller = client.getReplicationController(service.getServiceName());
			} catch (Exception e1) {
				controller = null;
			}
			if (controller != null) {
				controller = client.updateReplicationController(service.getServiceName(), 0);
			}
			map.put("status", "200");
			// 保存服务信息
			Date currentDate = new Date();
			User currentUser = CurrentUserUtils.getInstance().getUser();
			service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_STOPPED);
			service.setUpdateDate(currentDate);
			service.setUpdateBy(currentUser.getId());
			service = serviceDao.save(service);
			// 保存服务操作信息
			serviceOperationLogDao.save(service.getServiceName(), service.toString(),
					ServiceConstant.OPERATION_TYPE_STOP);
		} catch (KubernetesClientException e) {
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("stop service error:" + e.getStatus().getMessage());
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 服务版本升级
	 *
	 * @param id
	 *            服务Id
	 * @param serviceName
	 *            服务名称
	 * @param imgVersion
	 *            镜像版本信息
	 * @param imgName
	 *            镜像名称
	 * @return String
	 */
	@RequestMapping("service/modifyimgVersion.do")
	@ResponseBody
	public String modifyimgVersion(long id, String serviceName, String imgVersion, String imgName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_RUNNING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_DEBUG) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		try {
			if (service.getImgVersion().equals(imgVersion)) {
				map.put("status", "500");
			} else {
				LOG.info("***************版本升级开始****************************");
				// next Controller postfix
				String random = RandomString.getStringRandom(32);
				// next Controller name
				String nextControllerName = serviceName + "-" + random;
				// 保存服务信息
				service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_UPDATE);
				service.setTempName(nextControllerName);
				Date currentDate = new Date();
				User currentUser = CurrentUserUtils.getInstance().getUser();
				service.setUpdateDate(currentDate);
				service.setUpdateBy(currentUser.getId());
				serviceDao.save(service);
				// 保存服务操作信息
				serviceOperationLogDao.save(service.getServiceName(), service.toString(),
						ServiceConstant.OPERATION_TYPE_ROLLINGUPDATE);

				String image = dockerClientService.generateRegistryImageName(imgName, imgVersion);

				KubernetesAPIClientInterface client = kubernetesClientService.getClient();
				ReplicationController originalController = client.getReplicationController(serviceName);

				// 设置annotations
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				ReplicationController updateOriginalController = firstSetAnnotation(serviceName, nextControllerName,
						client, originalController);

				// 生成新镜像版本的RC
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				createNextRc(random, nextControllerName, image, client, originalController);

				// 设置annotations
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				boolean isSetDeploy = false;
				if (StringUtils.isBlank(updateOriginalController.getSpec().getSelector().get("deployment"))) {
					isSetDeploy = true;
				}

				updateOriginalController = secondSetAnnotation(serviceName, client, isSetDeploy);

				// 滚动升级
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				boolean result = rollingUpdate(id, serviceName, nextControllerName, client, updateOriginalController,
						isSetDeploy);
				if (result != true) {
					return null;
				}
				// 删除旧RC
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				client.deleteReplicationController(serviceName);
				LOG.info("Deleting old controller:" + serviceName);

				// 将新RC的名字重命名为旧RC名字
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				renameNewRc(serviceName, nextControllerName, client);

				// 更新SVC的lable Selector
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				com.bonc.epm.paas.kubernetes.model.Service k8sService = client.getService(serviceName);
				Map<String, String> lableMap = new HashMap<String, String>();
				lableMap.put("app", nextControllerName);
				k8sService.getSpec().setSelector(lableMap);
				client.updateService(serviceName, k8sService);

				// 保存服务信息
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					map.put("status", "201");
					return JSON.toJSONString(map);
				}
				service.setImgVersion(imgVersion);
				Image image2 = imageDao.findByNameAndVersion(service.getImgName(), imgVersion);
				service.setImgID(image2.getId());
				service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_RUNNING);
				service.setTempName("");
				currentDate = new Date();
				service.setUpdateDate(currentDate);
				service.setUpdateBy(currentUser.getId());
				serviceDao.save(service);

				map.put("status", "200");
				LOG.info("replicationController:" + serviceName + " rolling updated.");

				// 服务版本升级 使用 kubectl rolling-update 命令行
				/*
				 * KubernetesAPIClientInterface client =
				 * kubernetesClientService.getClient(); ReplicationController
				 * controller = client.getReplicationController(serviceName);
				 * String NS = controller.getMetadata().getNamespace(); String
				 * cmd = "kubectl rolling-update " + serviceName +
				 * " --namespace=" + NS + " --update-period=10s --image=" +
				 * dockerClientService.generateRegistryImageName(imgName,
				 * imgVersion); boolean flag = cmdexec(cmd); if (flag) {
				 * service.setImgVersion(imgVersion); //取得对应的imageid Image image
				 * = imageDao.findByNameAndVersion(service.getImgName(),
				 * imgVersion); service.setImgID(image.getId());
				 *
				 * Date currentDate = new Date(); User currentUser =
				 * CurrentUserUtils.getInstance().getUser();
				 * service.setUpdateDate(currentDate);
				 * service.setUpdateBy(currentUser.getId()); service =
				 * serviceDao.save(service); // 保存服务操作信息
				 * serviceOperationLogDao.save(service.getServiceName(),
				 * service.toString(),
				 * ServiceConstant.OPERATION_TYPE_ROLLINGUPDATE);
				 *
				 * map.put("status", "200"); } else { String rollBackCmd =
				 * "kubectl rolling-update " + serviceName + " --namespace="+ NS
				 * + " --rollback"; cmdexec(rollBackCmd); map.put("status",
				 * "400"); }
				 */
			}
		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("modify imageVersion error:" + e.getStatus().getMessage());
		} catch (Exception ex) {
			map.put("status", 400);
			map.put("msg", ex.getMessage());
			LOG.error(ex.getMessage());
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 服务版本升级取消
	 *
	 * @param id
	 *            服务Id
	 * @param serviceName
	 *            服务名称
	 * @param imgVersion
	 *            镜像版本信息
	 * @param imgName
	 *            镜像名称
	 * @return String
	 */
	@RequestMapping("service/cancelUpdate.do")
	@ResponseBody
	public String cancelUpdate(long id, String serviceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() == ServiceConstant.CONSTRUCTION_STATUS_CANCELING_UPDATE) {
				map.put("status", "503");
				return JSON.toJSONString(map);
			}
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		// 保存服务信息
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_CANCELING_UPDATE);
		Date currentDate = new Date();
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateDate(currentDate);
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);

		try {
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();

			ReplicationController originalController = null;
			try {
				originalController = client.getReplicationController(serviceName);
			} catch (Exception e) {
				LOG.info(e.getMessage());
				originalController = null;
			}
			if (originalController != null) {
				// 判断是升级后的版本的rc则删除该rc
				if (!originalController.getSpec().getTemplate().getSpec().getContainers().get(0).getImage().equals(
						dockerClientService.generateRegistryImageName(service.getImgName(), service.getImgVersion()))) {
					originalController = client.updateReplicationController(serviceName, 0);
					while (!(client.getLabelSelectorPods(originalController.getSpec().getSelector()).size() == 0)) {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							LOG.error(e.getMessage());
						}
						continue;
					}
					client.deleteReplicationController(serviceName);
				} else {
					// 设置annotations
					if (StringUtils.isNotBlank(originalController.getSpec().getSelector().get("deployment"))) {
						originalController.getSpec().setReplicas(service.getInstanceNum());
						originalController.getSpec().getSelector().remove("deployment");
						originalController.getSpec().getTemplate().getMetadata().getLabels().remove("deployment");
					}
					if (originalController.getMetadata() != null
							&& originalController.getMetadata().getAnnotations() != null) {
						if (originalController.getMetadata().getAnnotations()
								.containsKey("kubectl.kubernetes.io/original-replicas")) {
							originalController.getMetadata().getAnnotations()
									.remove("kubectl.kubernetes.io/original-replicas");
						}
					}
					originalController = client.updateReplicationController(serviceName, originalController);
					LOG.info("update originalController.");
				}
			}

			// 删除新的rc
			String nextControllerName = service.getTempName();
			if (StringUtils.isNotBlank(nextControllerName)) {
				ReplicationController nextController;
				try {
					nextController = client.getReplicationController(nextControllerName);
				} catch (Exception e) {
					LOG.info(e.getMessage());
					nextController = null;
				}
				if (nextController != null) {
					nextController = client.updateReplicationController(nextControllerName, 0);
					while (!(client.getLabelSelectorPods(nextController.getSpec().getSelector()).size() == 0)) {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							LOG.error(e.getMessage());
						}
						continue;
					}
					client.deleteReplicationController(nextControllerName);
				}
			}

			// 删除annotations
			try {
				originalController = client.getReplicationController(serviceName);
				originalController.getMetadata().getAnnotations().remove("kubectl.kubernetes.io/next-controller-id");
				client.updateReplicationController(serviceName, originalController);
				LOG.info("update originalController.  remove next-controller-id:-" + nextControllerName);
			} catch (Exception e) {
				if (!CreateContainer(id, false).contains("200")) {
					map.put("status", "400");
					return JSON.toJSONString(map);
				}
				;
				originalController = client.getReplicationController(serviceName);
			}

			// 更新SVC的lable Selector
			com.bonc.epm.paas.kubernetes.model.Service k8sService = client.getService(serviceName);
			if (!originalController.getSpec().getSelector().get("app")
					.equals(k8sService.getSpec().getSelector().get("app"))) {
				Map<String, String> lableMap = new HashMap<String, String>();
				lableMap.put("app", originalController.getSpec().getSelector().get("app"));
				k8sService.getSpec().setSelector(lableMap);
				client.updateService(serviceName, k8sService);
			}

		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("del service error:" + e.getStatus().getMessage());
			return JSON.toJSONString(map);
		}

		// 保存服务信息
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_RUNNING);
		currentDate = new Date();
		service.setTempName("");
		service.setUpdateDate(currentDate);
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), service.toString(),
				ServiceConstant.OPERATION_TYPE_CANCELUPDATE);

		map.put("status", "200");
		LOG.info("replicationController:" + serviceName + " rolling update canceled.");

		return JSON.toJSONString(map);
	}

	/**
	 * Description: 将新RC的名字重命名为旧RC名字
	 *
	 * @param serviceName
	 * @param nextControllerName
	 * @param client
	 */
	private void renameNewRc(String serviceName, String nextControllerName, KubernetesAPIClientInterface client) {
		ReplicationController resultController = client.getReplicationController(nextControllerName);
		resultController.getMetadata().setName(serviceName);
		resultController.getMetadata().setResourceVersion("");
		// resultController.getSpec().getSelector().put("app",
		// nextControllerName);
		// resultController.getSpec().getTemplate().getMetadata().getLabels().put("app",
		// nextControllerName);

		// resultController.getSpec().getTemplate().getMetadata().setName(serviceName);

		resultController = client.createReplicationController(resultController);

		client.deleteReplicationController(nextControllerName);
		LOG.info("Renaming controller:" + nextControllerName + " to " + serviceName);
	}

	/**
	 * Description: 以轮询的方式升级服务 操作过程： 1.将新RC的副本(replicas)数量增长1个；
	 * 2.迭代查询新RC启动的pod以及container的启动状态，直至启动； 3.将旧RC的副本(replicas)数量减少1个；
	 * 4.确保旧pod数量减少； 5.重复步骤1，直到就RC的副本（replicas）数量减为0；
	 *
	 * @param serviceName
	 * @param nextControllerName
	 * @param client
	 * @param updateOriginalController
	 * @param isSetDeploy
	 */
	private boolean rollingUpdate(long id, String serviceName, String nextControllerName,
			KubernetesAPIClientInterface client, ReplicationController updateOriginalController, boolean isSetDeploy) {
		// 最终新服务应该启动的pod副本数量
		int replicas = Integer.valueOf(
				updateOriginalController.getMetadata().getAnnotations().get("kubectl.kubernetes.io/original-replicas"));
		ReplicationController nextController = client.getReplicationController(nextControllerName);
		for (int i = 1, j = replicas - 1; i <= replicas && j >= 0; i++, j--) {
			LOG.info("Scaling up " + nextControllerName + " from " + (i - 1) + " to " + i + ";scaling down "
					+ serviceName + " from " + (j + 1) + " to " + j);
			// newRc ++
			if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
				return true;
			}
			nextController = client.updateReplicationController(nextControllerName, i);
			boolean podStatus = true;
			PodList podList = client.getLabelSelectorPods(nextController.getSpec().getSelector());
			while (podStatus) {
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					return true;
				}
				try {
					client.getReplicationController(nextControllerName);
				} catch (Exception e) {
					return false;
				}
				if (null != podList && null != podList.getItems() && podList.getItems().size() == i) {
					for (Pod pod : podList.getItems()) {
						if (pod.getStatus().getPhase().equals("Running")) {
							List<ContainerStatus> containerStatuses = pod.getStatus().getContainerStatuses();
							if (CollectionUtils.isNotEmpty(containerStatuses)) {
								boolean conStatus = true;
								for (ContainerStatus containerStatus : containerStatuses) {
									if (null != containerStatus.getState().getRunning()) {
										conStatus = false;
									} else {
										conStatus = true;
										break;
									}
								}
								if (conStatus) {
									podStatus = true;
									break;
								} else {
									podStatus = false;
								}
							}
						} else {
							podStatus = true;
							break;
						}
					}
				}

				if (podStatus) {
					podList = client.getLabelSelectorPods(nextController.getSpec().getSelector());
				}
			}
			// oldRc --
			if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
				return true;
			}
			updateOriginalController = client.getReplicationController(serviceName);
			if (isSetDeploy) { // 首次执行升级
				updateOriginalController.getSpec().getSelector().remove("deployment");
				updateOriginalController.getSpec().getTemplate().getMetadata().getLabels().remove("deployment");
			}

			if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
				return true;
			}
			updateOriginalController.getSpec().setReplicas(j);
			updateOriginalController = client.updateReplicationController(serviceName, updateOriginalController);
			while (!(client.getLabelSelectorPods(updateOriginalController.getSpec().getSelector()).size() == j)) {
				if (serviceDao.getServiceStatus(id) != ServiceConstant.CONSTRUCTION_STATUS_UPDATE) {
					return true;
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					LOG.error(e.getMessage());
				}
				continue;
			}
			LOG.info("Scaling " + nextControllerName + " up to " + i + " and scaling " + serviceName + " down to " + j
					+ " Update succeeded.");
		}
		return true;
	}

	/**
	 * Description: 给要升级镜像版本的rc设置annotations来记录pod的副本数量 设置值为：
	 * key:"kubectl.kubernetes.io/original-replicas" value: 旧RC的副本(replicas)数量
	 *
	 * @param serviceName
	 * @param client
	 * @param isSetDeploy
	 * @param oldRandom
	 * @return updateOriginalController
	 */
	private ReplicationController secondSetAnnotation(String serviceName, KubernetesAPIClientInterface client,
			boolean isSetDeploy) {
		ReplicationController updateOriginalController = client.getReplicationController(serviceName);
		updateOriginalController.getMetadata().getAnnotations().put("kubectl.kubernetes.io/original-replicas",
				String.valueOf(updateOriginalController.getSpec().getReplicas()));
		updateOriginalController = client.updateReplicationController(serviceName, updateOriginalController);
		if (isSetDeploy) {
			updateOriginalController = client.getReplicationController(serviceName);
			String random = RandomString.getStringRandom(32);
			updateOriginalController.getSpec().setReplicas(0);
			updateOriginalController.getSpec().getSelector().put("deployment", random);
			updateOriginalController.getSpec().getTemplate().getMetadata().getLabels().put("deployment", random);
			updateOriginalController = client.updateReplicationController(serviceName, updateOriginalController);

			// 从未升级过的rc
			// client.updateReplicationController(serviceName, 0);
		}

		LOG.info("update originalController.  put original-replicas:-" + updateOriginalController.getMetadata()
				.getAnnotations().get("kubectl.kubernetes.io/original-replicas"));
		return updateOriginalController;
	}

	/**
	 * Description: 根据升级镜像版本的信息，创建并返回新生成的rc notice：需要将RC的设置参数replicas的副本数为0
	 *
	 * @param random
	 * @param nextControllerName
	 * @param image
	 *            需要升级至的镜像版本
	 * @param client
	 * @param originalController
	 * @return nextController
	 */
	private ReplicationController createNextRc(String random, String nextControllerName, String image,
			KubernetesAPIClientInterface client, ReplicationController originalController) {
		ReplicationController nextController = null;
		try {
			nextController = client.getReplicationController(nextControllerName);
		} catch (KubernetesClientException e) {
			nextController = null;
		}
		if (null == nextController) {
			nextController = originalController;
			nextController.getMetadata().setName(nextControllerName);
			nextController.getMetadata().setResourceVersion("");
			Map<String, String> nextAnnotations = new HashMap<String, String>();
			nextAnnotations.put("kubectl.kubernetes.io/desired-replicas",
					String.valueOf(originalController.getSpec().getReplicas()));
			nextAnnotations.put("kubectl.kubernetes.io/update-source-id",
					originalController.getMetadata().getName() + ":" + originalController.getMetadata().getUid());
			nextController.getMetadata().setAnnotations(nextAnnotations);

			nextController.getSpec().setReplicas(0);
			nextController.getSpec().getSelector().put("deployment", random);
			nextController.getSpec().getSelector().put("app", nextControllerName);

			// nextController.getSpec().getTemplate().getMetadata().setName(nextControllerName);
			nextController.getSpec().getTemplate().getMetadata().getLabels().put("deployment", random);
			nextController.getSpec().getTemplate().getMetadata().getLabels().put("app", nextControllerName);

			nextController.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(image);

			nextController = client.createReplicationController(nextController);

		}
		LOG.info("create new replicationController " + nextControllerName + "successed!");
		return nextController;
	}

	/**
	 * Description: 给要升级镜像版本的rc设置annotations来标记 设置值为：
	 * key:"kubectl.kubernetes.io/next-controller-id" value: 新rc的名字
	 *
	 * @param serviceName
	 * @param nextControllerName
	 *            新rc的名字
	 * @param client
	 * @param originalController
	 * @return updateOriginalController
	 */
	private ReplicationController firstSetAnnotation(String serviceName, String nextControllerName,
			KubernetesAPIClientInterface client, ReplicationController originalController) {
		Map<String, String> annotations = new HashMap<String, String>();
		annotations.put("kubectl.kubernetes.io/next-controller-id", nextControllerName);
		originalController.getMetadata().setAnnotations(annotations);
		ReplicationController updateOriginalController = client.updateReplicationController(serviceName,
				originalController);
		LOG.info("update originalController.  put next-controller-id:-" + nextControllerName);
		return updateOriginalController;
	}

	/**
	 * Description: <br>
	 * 执行命令行操作
	 *
	 * @param cmd
	 *            命令行
	 * @return boolean
	 */
	public boolean cmdexec(String cmd) {
		String hostIp = kubernetesClientService.getK8sAddress();
		String name = kubernetesClientService.getK8sUsername();
		String password = kubernetesClientService.getK8sPasswrod();
		try {
			SshConnect.connect(name, password, hostIp, 22);
			boolean b = false;
			String rollingLog = SshConnect.exec(cmd, 10000);
			if (rollingLog.endsWith("$") || rollingLog.endsWith("#") || rollingLog.contains("updated")) {
				b = true;
			}
			while (!b) {
				String str = SshConnect.exec("", 10000);
				if (StringUtils.isNotBlank(str)) {
					rollingLog += str;
				}
				b = (rollingLog.endsWith("$") || rollingLog.endsWith("#") || rollingLog.contains("updated"));
			}
			LOG.info("rolling-update LOG:-" + rollingLog);
			if (rollingLog.contains("error")) {
				return false;
			}
			String result = SshConnect.exec("echo $?", 1000);
			if (StringUtils.isNotBlank(result)) {
				if (!('0' == (result.trim().charAt(result.indexOf("\n") + 1)))) {
					new InterruptedException();
				}
			} else {
				new InterruptedException();
			}
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
			LOG.error("error:执行command失败");
			return false;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			LOG.error("error:ssh连接失败");
			return false;
		} finally {
			SshConnect.disconnect();
		}
		return true;
	}

	/**
	 * Description: <br>
	 * 根据镜像名称查询镜像
	 *
	 * @param imageName
	 *            镜像名称
	 * @return String
	 */
	@RequestMapping("service/findImageVersion.do")
	@ResponseBody
	public String findImageVersion(String imageName) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Image> images = imageDao.findByImageVersionOfName(cUser.getId(), imageName,
				new Sort(new Order(Direction.DESC, "createDate")));
		map.put("data", images);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 修改服务数量
	 *
	 * @param id
	 *            服务Id
	 * @param addservice
	 *            服务数量
	 * @return String
	 */
	@RequestMapping("service/modifyServiceNum.do")
	@ResponseBody
	public String modifyServiceNum(long id, Integer addservice) {
		Map<String, String> map = new HashMap<String, String>();
		Service service = serviceDao.findOne(id);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_RUNNING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_DEBUG) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}

		if (service.getInstanceNum() == addservice) {
			map.put("status", "300");
		} else {
			try {
				if (service.getStatus() == ServiceConstant.CONSTRUCTION_STATUS_RUNNING
						|| service.getStatus() == ServiceConstant.CONSTRUCTION_STATUS_DEBUG) {
					KubernetesAPIClientInterface client = kubernetesClientService.getClient();
					ReplicationController controller = client.updateReplicationController(service.getServiceName(),
							addservice);
					if (controller != null && controller.getSpec().getReplicas() == addservice) {
						map.put("status", "200");
					} else {
						map.put("status", "400");
					}
				} else {
					map.put("status", "200");
				}
				if (map.get("status").equals("200")) {
					Date currentDate = new Date();
					User currentUser = CurrentUserUtils.getInstance().getUser();
					// 保存服务信息
					service.setInstanceNum(addservice);
					service.setUpdateDate(currentDate);
					service.setUpdateBy(currentUser.getId());
					service = serviceDao.save(service);
					// 保存服务操作信息
					serviceOperationLogDao.save(service.getServiceName(), service.toString(),
							ServiceConstant.OPERATION_TYPE_SCALING);
				}
			} catch (KubernetesClientException e) {
				map.put("status", "400");
				map.put("msg", e.getStatus().getMessage());
				LOG.error("modify servicenum error:" + e.getStatus().getMessage());
			} catch (Exception e) {
				map.put("status", "400");
				LOG.error("modify service error :" + e);
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * modify cpu and ram
	 *
	 * @param id
	 * @param cpus
	 * @param rams
	 * @return String
	 */
	@RequestMapping("service/modifyCPU.do")
	@ResponseBody
	public String modifyCPU(long id, Double cpus, String rams) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_RUNNING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_DEBUG) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		try {

			/**
			 * 检查运行该服务之后，剩余的cpu和memory是否满足预留条件
			 */
			boolean chkresult = resourceService.checkRestResource(cpus - service.getCpuNum(),String.valueOf(Double.parseDouble(rams)-Double.parseDouble(service.getRam())));
			if(!chkresult){
				map.put("msg", "cpu或内存不足，请调整资源大小或申请更多资源！");
				map.put("status", "504");
				return JSON.toJSONString(map);
			}

			service.setCpuNum(cpus);
			service.setRam(rams);
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			ReplicationController controller = client.getReplicationController(service.getServiceName());
			if (controller != null) {

				List<com.bonc.epm.paas.kubernetes.model.Container> containers = controller.getSpec().getTemplate()
						.getSpec().getContainers();
				for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
					setContainer(container, cpus, rams);
				}
				controller = client.updateReplicationController(service.getServiceName(), controller);
				if (map.get("status") == null) {
					map.put("status", "200");
					Date currentDate = new Date();
					User currentUser = CurrentUserUtils.getInstance().getUser();
					// 保存服务信息
					service.setUpdateDate(currentDate);
					service.setUpdateBy(currentUser.getId());
					service = serviceDao.save(service);
					// 保存服务操作信息
					serviceOperationLogDao.save(service.getServiceName(), service.toString(),
							ServiceConstant.OPERATION_TYPE_CONFIGURE);
				}
			} else {
				map.put("status", "400");
				map.put("msg",
						"Get a Replication Controller Info failed:ServiceName[" + service.getServiceName() + "]");
				LOG.error("Get a Replication Controller Info failed:ServiceName[" + service.getServiceName() + "]");
			}
		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("modify cpu&memory error:" + e.getStatus().getMessage());
		} catch (Exception e) {
			map.put("status", "400");
			LOG.error("modify cpu&memory error:" + e);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * setContainer
	 *
	 * @param container
	 * @param cpus
	 * @param rams
	 * @return Container
	 */
	public com.bonc.epm.paas.kubernetes.model.Container setContainer(
			com.bonc.epm.paas.kubernetes.model.Container container, Double cpus, String rams) {
		ResourceRequirements requirements = new ResourceRequirements();
		requirements.getLimits();
		Map<String, Object> def = new HashMap<String, Object>();
		def.put("cpu", cpus / Integer.valueOf(RATIO_LIMITTOREQUESTCPU));
		def.put("memory", Double.parseDouble(rams) / Integer.valueOf(RATIO_LIMITTOREQUESTMEMORY)+ "Mi");
		Map<String, Object> limit = new HashMap<String, Object>();
		// limit = kubernetesClientService.getlimit(limit);
		limit.put("cpu", cpus );
		limit.put("memory", rams + "Mi");
		requirements.setRequests(def);
		requirements.setLimits(limit);
		container.setResources(requirements);

		return container;
	}

	/**
	 * delete container and services from dockerfile
	 *
	 * @param id
	 * @return
	 */

	/**
	 * Description: <br>
	 * delete container and services from dockerfile
	 *
	 * @param id
	 *            服务Id
	 * @return String
	 */
	@RequestMapping("service/delContainer.do")
	@ResponseBody
	public String delContainer(long id) {
		User user = CurrentUserUtils.getInstance().getUser();
		Service service = serviceDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		}
		//如果服务有hpa，则删除hpa
		if (service.getTargetCPUUtilizationPercentage() != null) {
			KubernetesAPISClientInterface apisClient = kubernetesClientService.getApisClient();
			try {
				apisClient.deleteHorizontalPodAutoscaler(service.getServiceName());
			} catch (KubernetesClientException e) {
				LOG.error(e.getMessage());
			}
		}
		try {
			if (service.getStatus() != 1) {
				KubernetesAPIClientInterface client = kubernetesClientService.getClient();
				// 删除rc
				ReplicationController controller = null;
				try {
					controller = client.getReplicationController(service.getServiceName());
				} catch (Exception e1) {
					controller = null;
				}
				if (controller != null) {
					controller = client.updateReplicationController(service.getServiceName(), 0);
					if (controller != null && controller.getSpec().getReplicas() == 0) {
						Status status = client.deleteReplicationController(service.getServiceName());
						if (!status.getStatus().equals("Success")) {
							map.put("status", "400");
							map.put("msg", "Delete a Replication Controller failed:ServiceName["
									+ service.getServiceName() + "]");
							LOG.error("Delete a Replication Controller failed:ServiceName[" + service.getServiceName()
									+ "]");
							return JSON.toJSONString(map);
						}
					} else {
						map.put("status", "400");
						map.put("msg",
								"Update a Replication Controller (update the number of replicas) failed:ServiceName["
										+ service.getServiceName() + "]");
						LOG.error("Update a Replication Controller (update the number of replicas) failed:ServiceName["
								+ service.getServiceName() + "]");
						return JSON.toJSONString(map);
					}
				}
				if (StringUtils.isNotBlank(service.getTempName())) {
					try {
						controller = client.getReplicationController(service.getTempName());
					} catch (Exception e1) {
						controller = null;
					}
					if (controller != null) {
						controller = client.updateReplicationController(service.getTempName(), 0);
						if (controller != null && controller.getSpec().getReplicas() == 0) {
							Status status = client.deleteReplicationController(service.getTempName());
							if (!status.getStatus().equals("Success")) {
								map.put("status", "400");
								map.put("msg", "Delete a Replication Controller failed:ServiceName["
										+ service.getTempName() + "]");
								LOG.error("Delete a Replication Controller failed:ServiceName[" + service.getTempName()
										+ "]");
								return JSON.toJSONString(map);
							}
						} else {
							map.put("status", "400");
							map.put("msg",
									"Update a Replication Controller (update the number of replicas) failed:ServiceName["
											+ service.getTempName() + "]");
							LOG.error(
									"Update a Replication Controller (update the number of replicas) failed:ServiceName["
											+ service.getTempName() + "]");
							return JSON.toJSONString(map);
						}
					}
				}
				// 删除svc
				com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
				try {
					// 查询svc是否已经创建
					k8sService = client.getService(service.getServiceName());
				} catch (KubernetesClientException e) {
					k8sService = null;
				}
				if (null != k8sService) {
					Status status = client.deleteService(service.getServiceName());
					if (!status.getStatus().equals("Success")) {
						map.put("status", "400");
						map.put("msg", "Delete a Service failed:ServiceName[" + service.getServiceName() + "]");
						LOG.error("Delete a Service failed:ServiceName[" + service.getServiceName() + "]");
						return JSON.toJSONString(map);
					}
				}
			}
			map.put("status", "200");
			serviceDao.delete(id);
			envVariableDao.deleteByServiceId(id);
			serviceConfigmapDao.deleteByServiceId(id);//删除serviceConfigmap记录
			// 保存服务操作信息
			serviceOperationLogDao.save(service.getServiceName(), service.toString(),
					ServiceConstant.OPERATION_TYPE_DELETE);

			// 删除服务 释放绑定的端口
			List<PortConfig> bindPort = portConfigDao.findByServiceId(id);
			if (CollectionUtils.isNotEmpty(bindPort)) {
				for (PortConfig oneRow : bindPort) {
					smalSet.remove(Integer.valueOf(oneRow.getMapPort().trim()));
				}
				portConfigDao.deleteByServiceId(id);
			}

			// 更新挂载卷的使用状态
			if (service.getServiceType().equals("1")) {
				List<ServiceAndStorage> svcAndStoList = serviceAndStorageDao.findByServiceId(service.getId());
				for (ServiceAndStorage serviceAndStorage : svcAndStoList) {
					// 更新存储卷信息
					Storage storage = storageDao.findOne(serviceAndStorage.getStorageId());
					storage.setUseType(StorageConstant.NOT_USER);
					storage.setMountPoint("");
					storage.setUpdateBy(user.getId());
					storage.setUpdateDate(new Date());
					;
					storageDao.save(storage);

					// 记录更新storage存储卷
					String extraInfo = "新增storage存储卷 " + JSON.toJSONString(storage);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(storage.getStorageName(), extraInfo,
							CommConstant.STORAGE, CommConstant.OPERATION_TYPE_CREATED);
					commonOperationLogDao.save(log);
				}
				serviceAndStorageDao.delete(svcAndStoList);
			}
		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			LOG.error("del service error:" + e.getStatus().getMessage());
		}

		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 删除多个服务
	 *
	 * @param serviceIDs
	 *            服务id
	 * @return String
	 */
	@RequestMapping("service/delServices.do")
	@ResponseBody
	public String delServices(String serviceIDs) {
		// 解析获取的id List
		ArrayList<Long> ids = new ArrayList<Long>();
		String[] str = serviceIDs.split(",");
		if (str != null && str.length > 0) {
			for (String id : str) {
				ids.add(Long.valueOf(id));
			}
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		for (long id : ids) {
			String result = delContainer(id);
			if (!result.contains("200")) {
				maps.put("status", "400");
				LOG.error("服务删除错误！");
				return JSON.toJSONString(maps);
			}
		}
		maps.put("status", "200");
		return JSON.toJSONString(maps);
	}

	/**
	 * Description: <br>
	 * 停止多个服务
	 *
	 * @param serviceIDs
	 *            服务Id
	 * @return String
	 */
	@RequestMapping("service/stopServices.do")
	@ResponseBody
	public String stopServices(String serviceIDs) {
		ArrayList<Long> ids = new ArrayList<Long>();
		String[] str = serviceIDs.split(",");
		if (str != null && str.length > 0) {
			for (String id : str) {
				ids.add(Long.valueOf(id));
			}
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		try {
			for (long id : ids) {
				stopContainer(id);
			}
			maps.put("status", "200");
		} catch (Exception e) {
			maps.put("status", "400");
			LOG.error("服务停止错误！");
		}
		return JSON.toJSONString(maps);
	}

	/**
	 * Description: <br>
	 * 启动多个服务
	 *
	 * @param serviceIDs
	 *            服务id
	 * @return String
	 */
	@RequestMapping("service/stratServices.do")
	@ResponseBody
	public String startServices(String serviceIDs) {
		ArrayList<Long> ids = new ArrayList<Long>();
		String[] str = serviceIDs.split(",");
		if (str != null && str.length > 0) {
			for (String id : str) {
				ids.add(Long.valueOf(id));
			}
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		try {
			maps.put("status", "200");
			for (long id : ids) {
				String creatresult = CreateContainer(id, false);
				if (!creatresult.contains("200")) {
					if(creatresult.contains("504")){
						if(ResourceService.STATUS == ResourceService.CPU_LACK){
							maps.put("msg", "cpu不足，请调整服务的cpu大小或申请更多cpu!");
						}else{
							maps.put("msg", "ram不足，请调整服务的ram大小或申请更多ram!");
						}
						maps.put("status", "504");
						break;
					}else{
						maps.put("status", "400");
					}
				}
				;
			}
		} catch (Exception e) {
			maps.put("status", "400");
			e.printStackTrace();
			LOG.error("服务启动错误！");
		}
		return JSON.toJSONString(maps);
	}

	/**
	 * Description: <br>
	 * 给controller设置卷组挂载的信息
	 *
	 * @param controller
	 * @param storageName
	 * @param mountPath
	 * @return ReplicationController
	 */
	private ReplicationController setVolumeStorage(ReplicationController controller, long serviceId) {
		List<Storage> storageList = storageDao.findByServiceId(serviceId);
		ReplicationControllerSpec rcSpec = controller.getSpec();
		PodTemplateSpec template = rcSpec.getTemplate();
		PodSpec podSpec = template.getSpec();
		List<Volume> volumes = new ArrayList<Volume>();
		List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
		int i = 0;
		for (Storage storage : storageList) {
			i++;
			Volume volume = new Volume();
			volume.setName("cephfs-" + i);
			CephFSVolumeSource cephfs = new CephFSVolumeSource();
			List<String> monitors = new ArrayList<String>();
			System.out.println("CEPH_MONITOR:" + CEPH_MONITOR);
			String[] ceph_monitors = CEPH_MONITOR.split(",");
			for (String ceph_monitor : ceph_monitors) {
				monitors.add(ceph_monitor);
			}
			cephfs.setMonitors(monitors);
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
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
			volumeMount.setName("cephfs-" + i);
			volumeMounts.add(volumeMount);
		}

		podSpec.setVolumes(volumes);
		List<com.bonc.epm.paas.kubernetes.model.Container> containers = podSpec.getContainers();
		for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
			container.setVolumeMounts(volumeMounts);
		}
		return controller;
	}

	/**
	 * Description: <br>
	 * 获取挂在地址
	 *
	 * @param volume
	 * @return String
	 */
	@RequestMapping(value = { "service/getMountPath.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String getMountPath(String volume) {
		String mountPath = storageDao.findByVolume(volume);
		if (StringUtils.isNotBlank(mountPath)) {
			mountPath = mountPath.substring(mountPath.indexOf(":/") + 1, mountPath.lastIndexOf(";"));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mountPath", mountPath);
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 根据服务id查询服务和容器
	 *
	 * @param serviceID
	 *            服务ID
	 * @return String
	 */
	@RequestMapping(value = { "service/findservice.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String findPodsOfService(Long serviceID) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(serviceID);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		}
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<Container> containerList = new ArrayList<Container>();
		// 获取特殊条件的pods
		try {
			com.bonc.epm.paas.kubernetes.model.Service k8sService = client.getService(service.getServiceName());
			PodList podList = client.getLabelSelectorPods(k8sService.getSpec().getSelector());
			if (podList != null) {
				List<Pod> pods = podList.getItems();
				if (CollectionUtils.isNotEmpty(pods)) {
					int i = 1;
					for (Pod pod : pods) {
						Container container = new Container();
						container
								.setContainerName(service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
						container.setServiceid(service.getId());
						// 默认状态为0
						container.setContainerStatus(0);
						// pod状态不是Running时候
						if (!pod.getStatus().getPhase().equals("Running")) {
							container.setContainerStatus(1);
						} else {
							// container状态
							for (ContainerStatus status : pod.getStatus().getContainerStatuses()) {
								if (status.getState().getRunning() == null) {
									container.setContainerStatus(1);
								}
							}
						}

						containerList.add(container);
					}
				}
			}

		} catch (Exception e) {
			LOG.error("服务查询错误：" + e);
		}
		map.put("service", service);
		map.put("containerList", containerList);
		map.put("status", "200");
		return JSON.toJSONString(map);

	}

	/**
	 * getPodLogByPeriod:根据起止时间获取日志. <br/>
	 *
	 * @author longkaixiang
	 * @param podName
	 * @param since
	 * @param until
	 * @return String
	 */
	@RequestMapping("service/detail/getPodLogByPeriod.do")
	@ResponseBody
	public String getPodLogByPeriod(String podName, String since, String until) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer logs = new StringBuffer();
		// 验证since和until不能为空
		if (StringUtils.isBlank(since) || StringUtils.isBlank(until)) {
			map.put("status", "300");
			map.put("logStr", "时间参数不能为空！");
			return JSON.toJSONString(map);
		}
		// 验证是否都符合时间格式
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date sinceDate;
		Date untilDate;
		try {
			sinceDate = simpleDateFormat.parse(since);
			untilDate = simpleDateFormat.parse(until);
		} catch (ParseException e1) {
			map.put("status", "400");
			map.put("logStr", "时间参数格式不正确！");
			return JSON.toJSONString(map);
		}
		// 验证since不能大于until
		if (sinceDate.compareTo(untilDate) >= 0) {
			map.put("status", "500");
			map.put("logStr", "起始时间大于结束时间！");
			return JSON.toJSONString(map);
		}

		try {
			Pod pod = client.getPod(podName);
			String containerId = pod.getStatus().getContainerStatuses().get(0).getContainerID().replace("docker://",
					"");
			String containerHost = pod.getStatus().getHostIP();

			String url = "http://" + containerHost + ":" + DOCKER_IO_PORT + "/containers/" + containerId + "/logs";
			Map<String, Object> params = new HashMap<>();
			params.put("stdout", true);
			params.put("stderr", true);
			params.put("since", sinceDate.getTime()/1000);
			params.put("until", untilDate.getTime()/1000);
			try (InputStream inputStream = WebClientUtil.doGetStream(url, params);) {
				if (inputStream != null) {
					FrameReader frameReader = new FrameReader(inputStream);
					try {
						Frame frame = frameReader.readFrame();
						while (frame != null) {
							logs.append(new String(frame.getPayload()));
							if (logs.length() > 1000000) {
								break;
							}
							frame = frameReader.readFrame();
						}
					} catch (Throwable t) {
					} finally {
						try {
							frameReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String logStr = logs.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");

			map.put("logStr", logStr);
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "400");
			LOG.error("日志读取错误：" + e);
		}

		return JSON.toJSONString(map);

	}

	/**
	 * getPodLogs:获取当前Pod的日志. <br/>
	 *
	 * @author longkaixiang
	 * @param podName
	 * @return String
	 */
	@RequestMapping("service/detail/getPodlogs.do")
	@ResponseBody
	public String getPodLogs(String podName) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		String logStr = "";
		Map<String, Object> datamap = new HashMap<String, Object>();

		try {
			Pod pod = client.getPod(podName);
			String containerId = pod.getStatus().getContainerStatuses().get(0).getContainerID().replace("docker://",
					"");
			DockerClient dockerClient = dockerClientService
					.getSpecifiedDockerClientInstance(pod.getStatus().getHostIP());
			LogStringContainerResultCallback callback = new LogStringContainerResultCallback();
			dockerClient.logContainerCmd(containerId).withTail(DOCKER_LOG_TAIL).withStdOut(true).withStdErr(true)
					.exec(callback).awaitCompletion(DOCKER_LOG_AWAIT, TimeUnit.SECONDS);
			logStr = callback.toString();
			if (logStr.length() > DOCKER_LOG_SIZE) {
				logStr = logStr.substring(logStr.length() - DOCKER_LOG_SIZE);
			}

			logStr = logStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

			datamap.put("logStr", logStr);
			datamap.put("status", "200");
		} catch (Exception e) {
			datamap.put("status", "400");
			LOG.error("日志读取错误：" + e);
		}
		return JSON.toJSONString(datamap);
	}

	/**
	 * Description: <br>
	 * 输入命令，获取返回结果
	 *
	 * @param cmd
	 *            ： 命令
	 * @return String
	 */
	@RequestMapping("service/detail/execcmd.do")
	@ResponseBody
	public String getCmdResult(String cmd) {
		String hostIp = kubernetesClientService.getK8sAddress();
		String name = kubernetesClientService.getK8sUsername();
		String password = kubernetesClientService.getK8sPasswrod();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SshConnect.connect(name, password, hostIp, 22);
			boolean b = false;
			String rollingLog = SshConnect.exec(cmd, 10000);
			if (rollingLog.endsWith("$") || rollingLog.endsWith("#") || rollingLog.contains("updated")) {
				b = true;
			}
			long sys = System.currentTimeMillis();
			while (!b) {
				String str = SshConnect.exec("", 10000);
				if (StringUtils.isNotBlank(str)) {
					rollingLog += str;
				}
				b = (rollingLog.endsWith("$") || rollingLog.endsWith("#") || rollingLog.contains("updated"));
				long endsys = System.currentTimeMillis();
				if (endsys - sys > 20000) {
					map.put("status", "400");
					return JSON.toJSONString(map);
				}
			}

			if (rollingLog.contains("error")) {
				map.put("status", "400");
				return JSON.toJSONString(map);
			} else {
				map.put("result", rollingLog);
			}

			String result = SshConnect.exec("echo $?", 1000);
			if (StringUtils.isNotBlank(result)) {
				if (!('0' == (result.trim().charAt(result.indexOf("\n") + 1)))) {
					new InterruptedException();
				}
			} else {
				new InterruptedException();
			}
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
			LOG.error("error:执行command失败");
			map.put("status", "400");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			LOG.error("error:ssh连接失败");
			map.put("status", "400");
			return JSON.toJSONString(map);
		} finally {
			SshConnect.disconnect();
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * getCurrentPodLogs:获取当前Pod的实时日志. <br/>
	 *
	 * @author longkaixiang
	 * @param podName
	 * @return String
	 */
	@RequestMapping("service/detail/getCurrentPodlogs.do")
	@ResponseBody
	public String getCurrentPodLogs(String podName) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		String logStr = "";
		Map<String, Object> datamap = new HashMap<String, Object>();

		try {
			Pod pod = client.getPod(podName);
			String containerId = pod.getStatus().getContainerStatuses().get(0).getContainerID().replace("docker://",
					"");
			DockerClient dockerClient = dockerClientService
					.getSpecifiedDockerClientInstance(pod.getStatus().getHostIP());
			LogStringContainerResultCallback callback = new LogStringContainerResultCallback();
			dockerClient.logContainerCmd(containerId).withTail(DOCKER_LOG_TAIL).withStdOut(true).withStdErr(true)
					.exec(callback).awaitCompletion();
			logStr = callback.toString();
			if (logStr.length() > DOCKER_LOG_SIZE) {
				logStr = logStr.substring(logStr.length() - DOCKER_LOG_SIZE);
			}
			logStr = logStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

			datamap.put("logStr", logStr);
			datamap.put("status", "200");
		} catch (Exception e) {
			datamap.put("status", "400");
			LOG.error("日志读取错误：" + e);
		}

		return JSON.toJSONString(datamap);

	}

	/**
	 * downloadPodlogFile:下载日志文件. <br/>
	 *
	 * @author longkaixiang
	 * @param podName
	 * @param request
	 * @param response
	 *            void
	 */
	@RequestMapping(value = "/service/detail/getPodlogFile", method = RequestMethod.GET)
	public void downloadPodlogFile(String podName, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("===============podlog=download=start===============");
		try {
			response.reset();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment;filename=" + podName + ".log");
			ServletOutputStream outputStream = response.getOutputStream();

			KubernetesAPIClientInterface k8sClient = kubernetesClientService.getClient();

			Pod pod = k8sClient.getPod(podName);
			String containerId = pod.getStatus().getContainerStatuses().get(0).getContainerID().replace("docker://",
					"");
			DockerClient dockerClient = dockerClientService
					.getSpecifiedDockerClientInstance(pod.getStatus().getHostIP());
			LogStreamContainerResultCallback callback = new LogStreamContainerResultCallback(outputStream);
			dockerClient.logContainerCmd(containerId).withStdOut(true).withStdErr(true).exec(callback)
					.awaitCompletion(DOCKER_LOG_DOWNLOAD, TimeUnit.SECONDS);
			System.out.println("===============podlog=download=end===============");

		} catch (IOException e) {
			LOG.error("FileController  downloadTemplate:" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("日志读取错误：" + e);
		}
	}

	/**
	 * 获取前缀
	 *
	 * @return prex
	 * @see
	 */
	@RequestMapping("service/detail/getprex.do")
	@ResponseBody
	public String getprex() {
		Map<String, Object> map = new HashMap<String, Object>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		String prex = "http://" + namespace + ".";
		map.put("prex", prex);
		return JSON.toJSONString(map);
	}

	/**
	 * 修改服务地址
	 *
	 * @param serviceAddr
	 * @param proxyPath
	 * @param serId
	 * @return status
	 * @see
	 */
	@RequestMapping("service/detail/editSerAddr.do")
	@ResponseBody
	public String editSerAddr(String serviceAddr, String proxyPath, Long serId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (serviceDao.findByServiceAddrAndProxyPath(serviceAddr, proxyPath).size() > 0) {
			map.put("status", "500");
		} else {
			Service service = serviceDao.findOne(serId);
			// 服务状态判断
			if (null == service) {
				map.put("status", "501");
				return JSON.toJSONString(map);
			}
			service.setServiceAddr(serviceAddr);
			service.setProxyPath(proxyPath);
			try {
				Date currentDate = new Date();
				User currentUser = CurrentUserUtils.getInstance().getUser();
				service.setUpdateDate(currentDate);
				service.setUpdateBy(currentUser.getId());
				service = serviceDao.save(service);
				// 保存服务操作信息
				serviceOperationLogDao.save(service.getServiceName(), service.toString(),
						ServiceConstant.OPERATION_TYPE_UPDATE);
				map.put("status", "200");
			} catch (Exception e) {
				map.put("status", "400");
				e.printStackTrace();
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * editResponse:修改责任人信息. <br/>
	 *
	 * @author longkaixiang
	 * @param responsiblePerson
	 * @param responsiblePersonTelephone
	 * @param serId
	 * @return String
	 */
	@RequestMapping("service/detail/editResponse.do")
	@ResponseBody
	public String editResponse(String responsiblePerson, String responsiblePersonTelephone, Long serId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(serId);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		}
		service.setResponsiblePerson(responsiblePerson);
		;
		service.setResponsiblePersonTelephone(responsiblePersonTelephone);
		try {
			Date currentDate = new Date();
			User currentUser = CurrentUserUtils.getInstance().getUser();
			service.setUpdateDate(currentDate);
			service.setUpdateBy(currentUser.getId());
			service = serviceDao.save(service);
			// 保存服务操作信息
			serviceOperationLogDao.save(service.getServiceName(), service.toString(),
					ServiceConstant.OPERATION_TYPE_UPDATE);
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "400");
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 修改服务详情的基础信息表
	 *
	 * @param service
	 * @return
	 * @return
	 * @see
	 */
	@RequestMapping(value = "service/detail/editBaseSerForm.do")
	@ResponseBody
	public String editBaseSerForm(Model model, Service service) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service ser = serviceDao.findOne(service.getId());
		// 服务状态判断
		if (null == ser) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (ser.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& ser.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		if (delSvcAndRc(ser) == false) {
			map.put("status", "500");
			return JSON.toJSONString(map);
		}
		// 服务名
		ser.setServiceName(service.getServiceName());
		// 启动命令
		ser.setStartCommand(service.getStartCommand());
		// 服务访问路径
		ser.setServicePath(service.getServicePath());
		// nginx代理区域
		ser.setProxyZone(service.getProxyZone());
		// nginx代理路径
		ser.setProxyPath(service.getProxyPath());
		// 服务会话黏连方式
		ser.setSessionAffinity(service.getSessionAffinity());
		// 黏连
		ser.setNodeIpAffinity(service.getNodeIpAffinity());
		// 检查服务状态填写的路径
		ser.setCheckPath(service.getCheckPath());
		// Pod互斥
		ser.setIspodmutex(service.isIspodmutex());

		if (StringUtils.isNotBlank(service.getCheckPath())) {
			// 服务检测超时
			ser.setTimeoutDetction(
					service.getTimeoutDetction() != null ? service.getTimeoutDetction() : ServiceConstant.TIMEOUT);
			// 服务检测频率
			ser.setPeriodDetction(
					service.getPeriodDetction() != null ? service.getPeriodDetction() : ServiceConstant.PERIOD);
			// 服务检测延迟
			ser.setInitialDelay(
					service.getInitialDelay() != null ? service.getInitialDelay() : ServiceConstant.INNIALDELAY);
		} else {
			// 服务检测超时
			ser.setTimeoutDetction(null);
			// 服务检测频率
			ser.setPeriodDetction(null);
			// 服务检测延迟
			ser.setInitialDelay(null);
		}
		// 服务信息是否有修改
		ser.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		ser.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		ser.setUpdateBy(currentUser.getId());
		ser = serviceDao.save(ser);
		// 保存服务操作信息
		serviceOperationLogDao.save(ser.getServiceName(), ser.toString(), ServiceConstant.OPERATION_TYPE_UPDATE);
		map.put("status", "200");

		return JSON.toJSONString(map);

	}


	/**
	 *
	 * Description: 编辑端口配置信息
	 *
	 * @param portConfig
	 * @param serviceName
	 * @param serviceId
	 * @return String
	 * @see
	 */
	@RequestMapping(value = "service/detail/editConfigmap.do")
	@ResponseBody
	public String editConfigmap(String serviceId,String configmapPath, String configmap) {
		Map<String, String> map = new HashMap<String, String>();
		Service service = serviceDao.findOne(Long.parseLong(serviceId));
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		Date currentDate = new Date();
		User currentUser = CurrentUserUtils.getInstance().getUser();
		//保存serviceConfigmap

		serviceConfigmapDao.deleteByServiceId(service.getId());
		ServiceConfigmap serviceConfigmap =null;
		if(!configmap.equals("-1")){
			Configmap configmap2 = configmapDao.findOne(Long.parseLong(configmap));
			serviceConfigmap = new ServiceConfigmap();
			serviceConfigmap.setConfigmapId(Long.parseLong(configmap));
			serviceConfigmap.setCreateBy(currentUser.getId());
			serviceConfigmap.setCreateDate(currentDate);
			serviceConfigmap.setName(configmap2.getName());
			serviceConfigmap.setPath(configmapPath);
			serviceConfigmap.setServiceId(service.getId());
			serviceConfigmapDao.save(serviceConfigmap);
		}

		// 保存服务信息
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		service.setUpdateDate(currentDate);
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), serviceConfigmap !=null?serviceConfigmap.toString():"没选择configmap",
				ServiceConstant.OPERATION_TYPE_UPDATE);
		map.put("status", "200");

		return JSON.toJSONString(map);
	}
	/**
	 *
	 * Description: 编辑端口配置信息
	 *
	 * @param portConfig
	 * @param serviceName
	 * @param serviceId
	 * @return String
	 * @see
	 */
	@RequestMapping(value = "service/detail/editPortConfig.do")
	@ResponseBody
	public String editPortCfgForm(PortConfig portConfig, String serviceName, long serviceId) {
		Map<String, String> map = new HashMap<String, String>();
		Service service = serviceDao.findOne(serviceId);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		PortConfig portCfg = portConfigDao.findOne(portConfig.getPortId());
		portCfg.setContainerPort(portConfig.getContainerPort());
		portCfg = portConfigDao.save(portCfg);
		// 保存服务信息
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		service.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), portCfg.toString(),
				ServiceConstant.OPERATION_TYPE_UPDATE);
		map.put("status", "200");

		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 编辑环境变量配置信息<br>
	 *
	 * @param envVariable
	 * @param serviceName
	 * @param serviceId
	 * @return String
	 * @see
	 */
	@RequestMapping(value = "service/detail/editEnv.do")
	@ResponseBody
	public String editEnvForm(EnvVariable envVariable, String serviceName, long serviceId) {
		Map<String, String> map = new HashMap<String, String>();
		Service service = serviceDao.findOne(serviceId);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		EnvVariable envVar = new EnvVariable();
		envVar = envVariableDao.findOne(envVariable.getEnvId());
		envVar.setEnvKey(envVariable.getEnvKey());
		envVar.setEnvValue(envVariable.getEnvValue());
		envVar = envVariableDao.save(envVar);
		// 保存服务信息
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		service.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), envVar.toString(), ServiceConstant.OPERATION_TYPE_UPDATE);
		map.put("status", "200");
		// 返回状态
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 新增一个环境变量<br>
	 *
	 * @param envVariable
	 * @param serviceId
	 * @return string
	 * @see
	 */
	@RequestMapping(value = "service/detail/addEnv.do")
	@ResponseBody
	public String addEvn(EnvVariable envVariable, long serviceId) {
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		EnvVariable envVar = new EnvVariable();
		EnvVariable env = new EnvVariable();
		envVar.setCreateBy(createBy);
		envVar.setEnvKey(envVariable.getEnvKey());
		envVar.setEnvValue(envVariable.getEnvValue());
		envVar.setCreateDate(new Date());
		envVar.setServiceId(serviceId);
		env = envVariableDao.save(envVar);
		// 保存服务信息
		Service service = serviceDao.findOne(serviceId);
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		service.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), env.toString(), ServiceConstant.OPERATION_TYPE_UPDATE);
		return JSON.toJSONString(env);
	}

	/**
	 *
	 * Description: 删除一个环境变量 <br>
	 *
	 * @param envId
	 * @return
	 * @see
	 */
	@RequestMapping(value = "service/detail/delEnv.do", method = RequestMethod.POST)
	@ResponseBody
	public String delEvn(long envId) {
		Map<String, String> map = new HashMap<String, String>();
		EnvVariable env = envVariableDao.findOne(envId);
		// 保存服务信息
		Service service = serviceDao.findOne(env.getServiceId());
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		service.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), env.toString(), ServiceConstant.OPERATION_TYPE_UPDATE);
		try {
			envVariableDao.delete(envId);
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "500");
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 删除一个端口信息 <br>
	 *
	 * @param portId
	 * @return
	 * @see
	 */
	@RequestMapping(value = "service/detail/delPortCfg.do", method = RequestMethod.POST)
	@ResponseBody
	public String delPortCfg(long portId) {
		Map<String, String> map = new HashMap<String, String>();
		PortConfig portConfig = portConfigDao.findOne(portId);
		Service service = serviceDao.findOne(portConfig.getServiceId());
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		// 保存服务信息
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		service.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), portConfig.toString(),
				ServiceConstant.OPERATION_TYPE_UPDATE);
		try {
			portConfigDao.delete(portId);
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "500");
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 添加一个端口信息 <br>
	 *
	 * @param portConfig
	 * @param serviceId
	 * @return
	 * @see
	 */
	@RequestMapping(value = "service/detail/addPortCfg.do", method = RequestMethod.GET)
	@ResponseBody
	public String addPortCfg(PortConfig portConfig, long serviceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(serviceId);
		// 服务状态判断
		if (null == service) {
			map.put("status", "501");
			return JSON.toJSONString(map);
		} else {
			if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING
					&& service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_STOPPED) {
				map.put("status", "502");
				return JSON.toJSONString(map);
			}
		}
		PortConfig portCon = new PortConfig();
		PortConfig pCfg = new PortConfig();
		portCon.setContainerPort(portConfig.getContainerPort());
		portCon.setMapPort(String.valueOf(vailPortSet()));
		portCon.setProtocol(portConfig.getProtocol());
		portCon.setServiceId(serviceId);
		pCfg = portConfigDao.save(portCon);
		// 保存服务信息
		service.setIsModify(ServiceConstant.MODIFY_TRUE);
		Date currentDate = new Date();
		service.setUpdateDate(currentDate);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setUpdateBy(currentUser.getId());
		serviceDao.save(service);
		// 保存服务操作信息
		serviceOperationLogDao.save(service.getServiceName(), pCfg.toString(), ServiceConstant.OPERATION_TYPE_UPDATE);
		map.put("pCfg", pCfg);
		map.put("service", service);
		// 向map中添加生成的node端口
		return JSON.toJSONString(map);

	}

	/**
	 *
	 * Description: 服务列表导出excel
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 * @see
	 */
	@RequestMapping("service/exportExcel.do")
	@ResponseBody
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String searchService,
			String searchImage, String searchCreatorName) throws IOException {
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		List<Service> serviceList = new ArrayList<>();
		if (CurrentUserUtils.getInstance().getUser().getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			// admin用户显示一览界面
			serviceList = serviceDao.search("%" + (searchService != null ? searchService : "") + "%",
					"%" + (searchImage != null ? searchImage : "") + "%",
					"%" + (searchCreatorName != null ? searchCreatorName : "") + "%", null).getContent();
		} else {
			serviceList = serviceDao.findByCreateBy(createBy);
		}
		for (Service service : serviceList) {
			service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newdownfile = df.format(new Date()) + "--" + createBy + ".xls";
		PoiUtils poiUtil = new PoiUtils();
		String[] header = { "名称", "中文名", "状态", "镜像", "服务地址", "创建时间", "创建人" };
		List<String[]> context = new ArrayList<String[]>();
		for (int i = 0; i < serviceList.size(); i++) {
			Service serviceObj = serviceList.get(i);

			String serviceAddr = "";
			if (StringUtils.isNoneBlank(serviceObj.getServiceAddr())) {
				serviceAddr = serviceObj.getServiceAddr();
			}
			String[] service = { serviceObj.getServiceName(), serviceObj.getServiceChName(),
					mapStatus(serviceObj.getStatus()), serviceObj.getImgName(),
					new StringBuffer(serviceAddr).append("/").append(serviceObj.getProxyPath()).toString(),
					serviceObj.getCreateDate().toString(), serviceObj.getCreatorName() };
			context.add(service);
		}
		HSSFWorkbook wb = poiUtil.exportTest(serviceList, header, context);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + newdownfile);
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.flush();
		out.close();
	}

	/**
	 *
	 * Description: 服务状态号映射为中文
	 *
	 * @param status
	 * @return
	 * @see
	 */
	public static String mapStatus(Integer status) {
		if (1 == status) {
			return "未启动";
		}
		if (2 == status) {
			return "启动中";
		}
		if (3 == status) {
			return "运行中";
		}
		if (4 == status) {
			return "已停止";
		} else {
			return "启动失败";
		}
	}

	/**
	 * 判断服务有没有22端口，并判断服务是不是debug状态
	 *
	 * @param serviceId
	 * @see
	 */
	@RequestMapping(value = "service/debug.do", method = RequestMethod.GET)
	@ResponseBody
	public String debug(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 获取端口信息
		List<PortConfig> portConfigList = portConfigDao.findByServiceId(service.getId());
		int port = 0;
		for (PortConfig portConfig : portConfigList) {
			if (portConfig.getContainerPort().equals("22")) {
				port = Integer.parseInt(portConfig.getMapPort());
				break;
			}
		}
		// 如果没有查到22端口，返回400
		if (port == 0) {
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		// 如果服务不是debug状态，返回401
		if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_DEBUG) {
			map.put("status", "401");
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 判断服务有没有22端口
	 *
	 * @param serviceId
	 * @see
	 */
	@RequestMapping(value = "service/isDebugService.do", method = RequestMethod.GET)
	@ResponseBody
	public String isDebugService(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		// 获取端口信息
		List<PortConfig> portConfigList = portConfigDao.findByServiceId(service.getId());
		int port = 0;
		for (PortConfig portConfig : portConfigList) {
			if (portConfig.getContainerPort().equals("22")) {
				port = Integer.parseInt(portConfig.getMapPort());
				break;
			}
		}
		// 如果没有查到22端口，返回400
		if (port == 0) {
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 删除对应服务下的所有pod
	 *
	 * @param serviceName
	 */
	public void delPods(String serviceName) {
		LOG.info("************************before starting Service, delete garbage pod first*********************");
		try {
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			com.bonc.epm.paas.kubernetes.model.Service k8sService;
			try {
				k8sService = client.getService(serviceName);
			} catch (Exception e1) {
				return;
			}
			Map<String, String> labelSelector = new HashMap<String, String>();
			labelSelector.put("app", k8sService.getSpec().getSelector().get("app"));
			PodList podList = client.getLabelSelectorPods(labelSelector);
			if (podList != null) {
				for (Pod pod : podList.getItems()) {
					try {
						client.deletePod(pod.getMetadata().getName());
					} catch (KubernetesClientException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			LOG.error("garbage pod delete failed. error message:-" + e.getMessage());
		}
	}

	public boolean delSvcAndRc(Service service) {
		// 使用k8s管理服务
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
		ReplicationController controller = null;

		/*************************************
		 * 服务不是未启动状态时,获取svc和rc
		 *************************************/
		if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING) {
			try {
				// 获取svc
				k8sService = client.getService(service.getServiceName());
			} catch (KubernetesClientException e) {
				k8sService = null;
			}
			try {
				// 获取rc
				controller = client.getReplicationController(service.getServiceName());
			} catch (KubernetesClientException e) {
				controller = null;
			}
		}
		/*************************************
		 * 判断服务信息是否有改动，有改动则删除rc和svc
		 *************************************/
		try {
			// 删除svc
			if (k8sService != null) {
				client.deleteService(service.getServiceName());
			}
			// 删除rc
			if (controller != null) {
				client.deleteReplicationController(service.getServiceName());
			}
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//终端
	@RequestMapping(value = { "service/cmd/{id}/{podName}" }, method = RequestMethod.GET)
	public String serviceCmd(Model model, @PathVariable long id, @PathVariable String podName) {
		Service service = serviceDao.findOne(id);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<Pod> podList = new ArrayList<>();
		String hostIP = null;
		String containerID = null;
		Pod pod = null;
		// 通过服务名获取pod列表
		if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING) {
			com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
			try {
				k8sService = client.getService(service.getServiceName());
			} catch (Exception e) {
				k8sService = null;
			}
			if (k8sService != null) {
				PodList pods = client.getLabelSelectorPods(k8sService.getSpec().getSelector());
				if (pods != null) {
					podList = pods.getItems();
					if (CollectionUtils.isNotEmpty(podList)) {
						for (Pod poditem : podList) {
							if (poditem.getMetadata().getName().equals(podName)) {
								pod = poditem;
								hostIP = poditem.getStatus().getHostIP();
								containerID = poditem.getStatus().getContainerStatuses().get(0).getContainerID();
							}
						}
					}
				}
			}
		}

		model.addAttribute("pod", pod);
		model.addAttribute("dockerServerURL", hostIP);
		model.addAttribute("containerid", containerID);
		model.addAttribute("entryHost", ENTRY_HOST);
		model.addAttribute("dockerIOPort", DOCKER_IO_PORT);

		return "service/service-cmd.jsp";
	}

	// 文件
	@RequestMapping(value = { "service/file/{id}/{podName}" }, method = RequestMethod.GET)
	public String serviceFile(Model model, @PathVariable long id, @PathVariable String podName) {
		Service service = serviceDao.findOne(id);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<Pod> podList = new ArrayList<>();
		String hostIP = null;
		String containerID = null;
		Pod pod = null;
		// 通过服务名获取pod列表
		if (service.getStatus() != ServiceConstant.CONSTRUCTION_STATUS_WAITING) {
			com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
			try {
				k8sService = client.getService(service.getServiceName());
			} catch (Exception e) {
				k8sService = null;
			}
			if (k8sService != null) {
				PodList pods = client.getLabelSelectorPods(k8sService.getSpec().getSelector());
				if (pods != null) {
					podList = pods.getItems();
					if (CollectionUtils.isNotEmpty(podList)) {
						for (Pod poditem : podList) {
							if (poditem.getMetadata().getName().equals(podName)) {
								pod = poditem;
								hostIP = poditem.getStatus().getHostIP();
								containerID = poditem.getStatus().getContainerStatuses().get(0).getContainerID();
							}
						}
					}
				}
			}
		}

		model.addAttribute("pod", pod);
		model.addAttribute("dockerServerURL", hostIP);
		model.addAttribute("containerid", containerID);
		model.addAttribute("entryHost", ENTRY_HOST);
		model.addAttribute("dockerIOPort", DOCKER_IO_PORT);

		return "service/service-file.jsp";
	}
}
