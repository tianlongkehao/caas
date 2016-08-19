package com.bonc.epm.paas.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.alibaba.fastjson.JSONArray;
import com.bonc.epm.paas.constant.NginxServerConf;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.constant.StorageConstant;
import com.bonc.epm.paas.constant.TemplateConf;
import com.bonc.epm.paas.constant.esConf;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.EnvTemplateDao;
import com.bonc.epm.paas.dao.EnvVariableDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.CephFSVolumeSource;
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
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.ESClient;
import com.bonc.epm.paas.util.SshConnect;
import com.bonc.epm.paas.util.TemplateEngine;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.ExposedPort;

/**
 * 
 * @author fengtao
 * @time 2015年12月3日
 */
@Controller
public class ServiceController {
	private static final Logger log = LoggerFactory.getLogger(ServiceController.class);

	public static Set<Integer> smalSet = new HashSet<Integer>();
	@Autowired
	public ServiceDao serviceDao;
	
	@Autowired
	public EnvVariableDao envVariableDao;
	
	@Autowired
	public PortConfigDao portConfigDao;

	@Autowired
	private ImageDao imageDao;
	
	@Autowired
	private CiDao ciDao;

	@Autowired
	private StorageDao storageDao;
	
	@Autowired
	private EnvTemplateDao envTemplateDao;

	@Autowired
	public DockerClientService dockerClientService;

	@Autowired
	private KubernetesClientService kubernetesClientService;

	@Autowired
	private TemplateConf templateConf;

	@Autowired
	private esConf esConf;

	@Value("${ceph.monitor}")
	public String CEPH_MONITOR;

	@Autowired
	private NginxServerConf nginxServerConf;

	@RequestMapping("service/listService.do")
	@ResponseBody
	public String list() {
		List<Service> serviceList = new ArrayList<Service>();
		for (Service service : serviceDao.findAll()) {
			serviceList.add(service);
		}
		log.debug("services:===========" + serviceList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", serviceList);
		return JSON.toJSONString(map);
	}

	/**
	 * 展示container和services
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "service" }, method = RequestMethod.GET)
	public String containerLists(Model model) {
		User currentUser = CurrentUserUtils.getInstance().getUser();
		// 获取特殊条件的pods
		try {
			getServiceSource(model, currentUser.getId());
			getNginxServer(model);
			// getleftResource(model);
		} catch (KubernetesClientException e) {
			model.addAttribute("msg", e.getStatus().getMessage());
			log.debug("service show:" + e.getStatus().getMessage());
			return "workbench.jsp";
		}
		model.addAttribute("menu_flag", "service");

		return "service/service.jsp";

	}

	public void getNginxServer(Model model) {
		model.addAttribute("DMZ", nginxServerConf.getDMZ());
		model.addAttribute("USER", nginxServerConf.getUSER());
	}

	public void getServiceSource(Model model, long id) {
		List<Service> serviceList = new ArrayList<Service>();
		List<Container> containerList = new ArrayList<Container>();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		for (Service service : serviceDao.findByCreateBy(id)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("app", service.getServiceName());
			PodList podList = client.getLabelSelectorPods(map);
			if (podList != null) {
				List<Pod> pods = podList.getItems();
				if (CollectionUtils.isNotEmpty(pods)) {
					int i = 1;
					for (Pod pod : pods) {
						String podName = pod.getMetadata().getName();
						Container container = new Container();
						container
								.setContainerName(service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
						container.setServiceid(service.getId());
						containerList.add(container);
					}
				}
			}
			serviceList.add(service);
		}
		model.addAttribute("containerList", containerList);
		model.addAttribute("serviceList", serviceList);
	}

	/**
	 * 展示container和services
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "service/findservice" }, method = RequestMethod.POST)
	public String findService(Model model, String searchNames) {
		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<Service> serviceList = new ArrayList<Service>();
		List<Container> containerList = new ArrayList<Container>();
		// 获取特殊条件的pods
		try {
			for (Service service : serviceDao.findByNameOf(currentUser.getId(), "%" + searchNames + "%")) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("app", service.getServiceName());
				PodList podList = client.getLabelSelectorPods(map);
				if (podList != null) {
					List<Pod> pods = podList.getItems();
					if (CollectionUtils.isNotEmpty(pods)) {
						int i = 1;
						for (Pod pod : pods) {
							String podName = pod.getMetadata().getName();
							Container container = new Container();
							container.setContainerName(
									service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
							container.setServiceid(service.getId());
							containerList.add(container);
						}
					}
				}
				serviceList.add(service);
				log.debug("service=========" + service);
			}
		} catch (Exception e) {
			log.error("服务查询错误：" + e);
		}
		model.addAttribute("serviceList", serviceList);
		model.addAttribute("containerList", containerList);

		return "service/service.jsp";
	}
	
	
	/**
	 * 展示container和services
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "service/{id}" }, method = RequestMethod.GET)
	public String service(Model model, @PathVariable long id) {
		Service service = serviceDao.findOne(id);
		model.addAttribute("service", service);
		model.addAttribute("menu_flag", "service");

		return "service/service.jsp";

	}
	
	@RequestMapping(value = { "service/import" })
	public String serviceImport(Model model) {
		
		model.addAttribute("menu_flag", "service");

		return "service/service-import.jsp";

	}

	/**
	 * 根据id查找container和services
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "service/detail/{id}" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
		System.out.printf("id: " + id);
		Service service = serviceDao.findOne(id);
		//service.setProxyZone(substr(service.getProxyZone()));
		List<EnvVariable> envVariableList = new ArrayList();
		envVariableList = envVariableDao.findByServiceId(id);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<Container> containerList = new ArrayList<Container>();
		List<String> logList = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("app", service.getServiceName());
		// 通过服务名获取pod列表
		PodList podList = client.getLabelSelectorPods(map);
		if (podList != null) {
			List<Pod> pods = podList.getItems();
			if (CollectionUtils.isNotEmpty(pods)) {
				int i = 1;
				for (Pod pod : pods) {
					// 获取pod名称
					String podName = pod.getMetadata().getName();
					// 初始化es客户端
					ESClient esClient = new ESClient();
					esClient.initESClient(esConf.getHost());
					// 设置es查询日期，数据格式，查询的pod名称
					String s = esClient.search("logstash-" + dateToString(new Date()), "fluentd", podName);
					// 关闭es客户端
					esClient.closeESClient();
					// 拼接日志格式
					String add = "[" + "App-" + i + "] [" + podName + "]：";
					s = add + s.replaceAll("\n", "\n" + add);

					s = s.substring(0, s.length() - add.length());
					Container container = new Container();
					container.setContainerName(service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
					container.setServiceid(service.getId());
					containerList.add(container);
					logList.add(s);
				}
			}
		}
		model.addAttribute("id", id);
		model.addAttribute("containerList", containerList);
		model.addAttribute("logList", logList);
		model.addAttribute("service", service);
		model.addAttribute("envVariableList", envVariableList);
		return "service/service-detail.jsp";
	}

	/**
	 * 将字符串重新组装
	 * 主要用于proxyZone的重新组装
	 * @param str
	 * @return
	 */
	String substr(String str){
		String strs="";
		String[] splitstr=str.split("'");
		for(int i=0;i<splitstr.length;i++){
			if((i+1)%4==0){
				//System.out.println(splitstr[i]);
				strs+=splitstr[i]+",";
			}
	}
		strs=strs.substring(0,strs.length()-1);
		return strs;
	}

	
	@RequestMapping("service/detail/getlogs.do")
	@ResponseBody
	public String getServiceLogs(long id, String date) {
		Service service = serviceDao.findOne(id);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		List<String> logList = new ArrayList<String>();
		String logStr = "";
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> datamap = new HashMap<String, Object>();

		try {
			map.put("app", service.getServiceName());
			PodList podList = client.getLabelSelectorPods(map);
			if (podList != null) {
				List<Pod> pods = podList.getItems();
				if (CollectionUtils.isNotEmpty(pods)) {
					int i = 1;
					for (Pod pod : pods) {
						// 获取pod名称
						String podName = pod.getMetadata().getName();
						// 初始化es客户端
						ESClient esClient = new ESClient();
						esClient.initESClient(esConf.getHost());
						String s = null;
						if (date != "") {
							// 设置es查询日期，数据格式，查询的pod名称
							s = esClient.search("logstash-" + date.replaceAll("-", "."), "fluentd", podName);
						} else {
							// 设置es查询日期，数据格式，查询的pod名称
							s = esClient.search("logstash-" + dateToString(new Date()), "fluentd", podName);
						}

						// 关闭es客户端
						esClient.closeESClient();
						// 拼接日志格式
						String add = "[" + "App-" + i + "] [" + podName + "]：";
						s = add + s.replaceAll("\n", "\n" + add);

						s = s.substring(0, s.length() - add.length());
						logStr = logStr.concat(s);
						logList.add(s);
					}
				}
			}
			datamap.put("logStr", logStr);
			datamap.put("status", "200");
		} catch (Exception e) {
			datamap.put("status", "400");
			log.error("日志读取错误：" + e);
		}

		return JSON.toJSONString(datamap);

	}

	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy.MM.dd");
		String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * 响应“部署”按钮
	 * 
	 * @param imageName
	 * @param imageVersion
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "service/add" }, method = RequestMethod.GET)
	public String create(String imgID, String imageName, String imageVersion, String resourceName, Model model) {		
		String isDepoly = "";
		if (imageName != null) {
			isDepoly = "deploy";
		     // 获取基础镜像的暴露端口信息
			model.addAttribute("portConfigs",JSON.toJSONString(getBaseImageExposedPorts(imgID)));
		}
//TODO 
		/*boolean flag = getleftResource(model);
		if (!flag) {
			model.addAttribute("msg", "请创建租户！");
			return "service/service.jsp";
		}*/
		//TODO

		// 获取配置文件中nginx选择区域
		getNginxServer(model);
		
		User cUser = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> templateNames = envTemplateDao.findTemplateName(cUser.getId());
		
		model.addAttribute("imgID", imgID);
		model.addAttribute("resourceName", resourceName);
		model.addAttribute("imageName", imageName);
		model.addAttribute("imageVersion", imageVersion);
		model.addAttribute("isDepoly", isDepoly);
		model.addAttribute("menu_flag", "service");
		model.addAttribute("templateNames",templateNames);
		return "service/service_create.jsp";
	}
	
	
    private List<PortConfig> getBaseImageExposedPorts(String imgID) {
        Ci ci = ciDao.findByImgId(Long.valueOf(imgID));
        if (null != ci) {
            //Image image = imageDao.findByNameAndVersion(ci.getBaseImageName().substring(ci.getBaseImageName().indexOf("/") +1),ci.getBaseImageVersion());
            Image image = imageDao.findById(ci.getBaseImageId());
            InspectImageResponse iir = dockerClientService.inspectImage(image.getImageId());
            // v1.9
            long countOfExposedPort = iir.getContainerConfig().getExposedPorts().length;
            if (countOfExposedPort > 0) {
                ExposedPort[] exposedPorts = iir.getContainerConfig().getExposedPorts();
                List<PortConfig> tmpPortConfigs = new ArrayList<PortConfig>();
                for (int i = 0;i<countOfExposedPort;i++) {
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
        return null;
    }
    
    @RequestMapping(value = { "service/getPortConfig.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String getPortConfig(String imgID){
        List<PortConfig> list=getBaseImageExposedPorts(imgID);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",list);
        return JSON.toJSONString(map); 
           
    }

	public boolean getleftResource(Model model) {

		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		try {
			ResourceQuota quota = client.getResourceQuota(currentUser.getNamespace());
			if (quota.getStatus() != null) {
				long hard = kubernetesClientService.transMemory(quota.getStatus().getHard().get("memory"));
				long used = kubernetesClientService.transMemory(quota.getStatus().getUsed().get("memory"));

				System.out.println(quota.getStatus().getHard().get("cpu"));
				System.out.println(quota.getStatus().getUsed().get("cpu"));

				double leftCpu = kubernetesClientService.transCpu(quota.getStatus().getHard().get("cpu"))
						- kubernetesClientService.transCpu(quota.getStatus().getUsed().get("cpu"));

				long leftmemory = hard - used;

				System.out.println(hard + "  " + used);
				model.addAttribute("leftcpu", leftCpu);
				model.addAttribute("leftmemory", leftmemory / 1024);
			}

		} catch (Exception e) {
			log.error("getleftResource error:" + e);
			return false;
		}

		return true;
	}

	/**
	 * 展示镜像
	 * 
	 * @return
	 */
	@RequestMapping(value = { "service/images" }, method = RequestMethod.GET)
	@ResponseBody
	public String imageList() {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Image> images = imageDao.findAll(userId);
	/*	if (CollectionUtils.isNotEmpty(images)) {
		    for (Image image : images) {
		        image.setPortConfigs(getBaseImageExposedPorts(String.valueOf(image.getId())));
		    }
		}*/
		map.put("data", images);
		System.out.println(JSON.toJSONString(map));
		return JSON.toJSONString(map);
	}

	/**
	 * 展示镜像
	 * 
	 * @return
	 */
	@RequestMapping("service/findimages.do")
	@ResponseBody
	public String findimages(String imageName) {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Image> images = imageDao.findByNameOf(userId, "%" + imageName + "%");
/*		if (CollectionUtils.isNotEmpty(images)) {
		    for (Image image : images) {
		        image.setPortConfigs(getBaseImageExposedPorts(String.valueOf(image.getId())));
		    }
		}*/
		map.put("data", images);
		return JSON.toJSONString(map);
	}
	
	
	/**
	 * create container and services from dockerfile
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("service/createContainer.do")
	@ResponseBody
	public String CreateContainer(long id) {
		Service service = serviceDao.findOne(id);
		List<EnvVariable> envVariables = envVariableDao.findByServiceId(id);
		List<PortConfig> portConfigs = portConfigDao.findByServiceId(service.getId()); // 获取服务对应的端口映射
		Map<String, Object> map = new HashMap<String, Object>();
		// 使用k8s管理服务
		String registryImgName = dockerClientService.generateRegistryImageName(service.getImgName(),
				service.getImgVersion());
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		ReplicationController controller = null;
		com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
		try {
			controller = client.getReplicationController(service.getServiceName());
		} catch (KubernetesClientException e) {
			controller = null;
		}
		try {
			k8sService = client.getService(service.getServiceName());
		} catch (KubernetesClientException e) {
			k8sService = null;
		}
		try {
			// 如果没有则新增
			if (controller == null) {
			    // 初始化自定义启动命令
			    String startCommand = service.getStartCommand().trim();
			    List<String> command = new ArrayList<String>();
			    List<String> args = new ArrayList<String>();
			    if (StringUtils.isNotBlank(startCommand)) {
			        String[] startCommandArray = startCommand.replaceAll("\\s+", " ").split(" ");
			        for (String item : startCommandArray) {
			            if (CollectionUtils.isEmpty(command)) {
			                command.add(item);
			                continue;
			            }
			            args.add(item);
			        }
			    }
				controller = kubernetesClientService.generateSimpleReplicationController(service.getServiceName(),
						service.getInstanceNum(), registryImgName, portConfigs, service.getCpuNum(), service.getRam(),
						service.getProxyZone(),service.getServicePath(),service.getProxyPath(),envVariables,command,args);
				// 给controller设置卷组挂载的信息
				System.out.println("给rc绑定vol");
				if (!"0".equals(service.getVolName())) {
					controller = this.setVolumeStorage(controller, service.getVolName(), service.getMountPath());
				}
				controller = client.createReplicationController(controller);
			} else {
				controller = client.updateReplicationController(service.getServiceName(), service.getInstanceNum());
			}
			if (k8sService == null) {
				k8sService = kubernetesClientService.generateService(service.getServiceName(),portConfigs,service.getProxyZone(),service.getServicePath(),service.getProxyPath());
				k8sService = client.createService(k8sService);
			}
			if (controller == null || k8sService == null) {
				map.put("status", "500");
			} else {
				map.put("status", "200");
				service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_PENDING);
				serviceDao.save(service);
			}
		} catch (KubernetesClientException e) {
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			log.error("create service error:" + e.getStatus().getMessage());
		}

		return JSON.toJSONString(map);

	}

	/**
	 * create container and services
	 * 
	 * @param container
	 * @return
	 */
	@RequestMapping("service/constructContainer.do")
	public String constructContainer(Service service, String resourceName,String envVariable,String portConfig) {
		User currentUser = CurrentUserUtils.getInstance().getUser();
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
		service.setCreateDate(new Date());
		service.setCreateBy(currentUser.getId());
		if (!StringUtils.isEmpty(resourceName) && !service.getServicePath().trim().equals(resourceName.substring(0, resourceName.indexOf(".")).trim())) {
			service.setServicePath(resourceName.substring(0, resourceName.indexOf(".")).trim());
		}
		serviceDao.save(service);
		
		//将服务中的环境变量循环遍历，保存到相关联的实体类中；
		if (StringUtils.isNotEmpty(envVariable)) {
			JSONArray jsonArray = JSONArray.parseArray(envVariable);  
			for(int i = 0 ; i < jsonArray.size(); i ++ ) {
				EnvVariable envVar = new EnvVariable();
				envVar.setCreateBy(currentUser.getId());
				envVar.setEnvKey(jsonArray.getJSONObject(i).getString("envKey").trim());
				envVar.setEnvValue(jsonArray.getJSONObject(i).getString("envValue").trim());
				envVar.setCreateDate(new Date());
				envVar.setServiceId(service.getId());
				envVariableDao.save(envVar);
			}
		}
		//保存到与service关联的portConfig实体类
		if (StringUtils.isNotEmpty(portConfig)){
			JSONArray jsonArray = JSONArray.parseArray(portConfig);  
			for(int i = 0 ; i < jsonArray.size(); i ++ ) {
				PortConfig portCon = new PortConfig();
				portCon.setContainerPort(jsonArray.getJSONObject(i).getString("containerPort").trim());
				portCon.setMapPort(jsonArray.getJSONObject(i).getString("mapPort").trim());
				portCon.setProtocol(jsonArray.getJSONObject(i).getString("protocol").trim());
//			portCon.setOptions(Integer.valueOf(jsonArray.getJSONObject(i).getString("option")));
				portCon.setCreateDate(new Date());
				portCon.setServiceId(service.getId());
				portConfigDao.save(portCon);
				
				// 向map中添加生成的node端口
				smalSet.add(Integer.valueOf(portCon.getContainerPort()));
			}
		}
		//保存
		
/*		// app为修改nginx配置文件的配置项
		Map<String, String> app = new HashMap<String, String>();
		app.put("userName", currentUser.getUserName());
		app.put("confName", service.getServiceName());
		app.put("port", String.valueOf(vailPortSet()));
		// 判断配置文件是否存在并删除
		if (TemplateEngine.fileIsExist(
				CurrentUserUtils.getInstance().getUser().getUserName() + "-" + service.getServiceName(),
				templateConf)) {
			// 清空配置文件内容
			TemplateEngine.deleteConfig(service.getServiceName(),
					CurrentUserUtils.getInstance().getUser().getUserName() + "-" + service.getServiceName(),
					templateConf);
			// 删除配置文件
			TemplateEngine.fileDel(
					CurrentUserUtils.getInstance().getUser().getUserName() + "-" + service.getServiceName(),
					templateConf);
		}
		// 将ip、端口等信息写入模版并保存到nginx config文件路径
		// TODO 3  windows注释
		 TemplateEngine.generateConfig(app,
		 CurrentUserUtils.getInstance().getUser().getUserName() + "-" +
		 service.getServiceName(), templateConf);
		// 重新启动nginx服务器
		// TODO 2
		 TemplateEngine.cmdReloadConfig(templateConf);
		 service.setServiceAddr(TemplateEngine.getConfUrl(templateConf));*/
		 //service.setPortSet(app.get("port"));
		service.setServiceAddr(templateConf.getServerAddr());
		serviceDao.save(service);
		// 更新挂载卷的使用状态
		if (!"0".equals(service.getVolName()) && StringUtils.isNotBlank(service.getVolName())) {
			this.updateStorageType(service.getVolName(), service.getServiceName());
		}
		log.debug("container--Name:" + service.getServiceName());
		return "redirect:/service";
	}
	
	/**
	 * 当前用户创建服务时匹配服务路径和nginx路径 和服务名称不重复
	 * @param servicePath
	 * @param proxyPath
	 * @return
	 */
	@RequestMapping("service/matchPath.do")
	@ResponseBody
	public String matchServicePathAndProxyPath (String proxyPath,String serviceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		User cUser = CurrentUserUtils.getInstance().getUser();
		for (Service service : serviceDao.findByCreateBy(cUser.getId())) {
			if (service.getServiceName().equals(serviceName)) {
				map.put("status", "500");
				break;
			} else if (service.getProxyPath().equals(proxyPath)) {
				map.put("status", "400");
				break;
			} else {
				map.put("status", "200");
			}
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 保存环境变量模板
	 * 
	 * @param templateName
	 * @return 
	 * @see
	 */
	@RequestMapping("service/saveEnvTemplate.do")
	@ResponseBody
	public String saveEnvTemplate (String templateName,String envVariable) {
		Map<String, Object> map = new HashMap<String, Object>();
		User cUser = CurrentUserUtils.getInstance().getUser();
		
		for (EnvTemplate envTemplate : envTemplateDao.findByCreateBy(cUser.getId())) {
            if (envTemplate.getTemplateName().equals(templateName)) {
                map.put("status", "200"); //模板名称重复
                return JSON.toJSONString(map);
            }
        }
		
		if (StringUtils.isNotEmpty(envVariable)) {
            JSONArray jsonArray = JSONArray.parseArray(envVariable);  
            for(int i = 0 ; i < jsonArray.size(); i ++ ) {
                EnvTemplate envTemplate = new EnvTemplate();
                envTemplate.setCreateBy(cUser.getId());
                envTemplate.setEnvKey(jsonArray.getJSONObject(i).getString("envKey").trim());
                envTemplate.setEnvValue(jsonArray.getJSONObject(i).getString("envValue").trim());
                envTemplate.setCreateDate(new Date());
                envTemplate.setTemplateName(templateName);
                envTemplateDao.save(envTemplate);
            }
            map.put("status", "400");
		}
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 查询用户的环境变量模板，导入到环境变量模板中
	 * @return
	 */
	@RequestMapping("service/importEnvTemplate.do")
	@ResponseBody
	public String findEnvTemplate(String templateName){
		User cUser = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<EnvTemplate> envTemplates = envTemplateDao.findByCreateByAndTemplateName(cUser.getId(),templateName);
		map.put("data", envTemplates);
		return JSON.toJSONString(map);
	}
	
	/**
    * 生成有效的PORTSET,回收端口
    * @return int
    */
   public int vailPortSet(){
       synchronized (this) {
           int offset = kubernetesClientService.getK8sEndPort() - kubernetesClientService.getK8sStartPort();
           Set<Integer> bigSet = Stream.iterate(kubernetesClientService.getK8sStartPort(), item -> item+1)
                                       .limit(offset)
                                       .collect(Collectors.toSet());
/*           Set<Integer> tmpBigSet = bigSet;
           tmpBigSet.removeAll(smalSet);
           if (CollectionUtils.isEmpty(tmpBigSet)) {
               smalSet.clear();
               smalSet.addAll(portConfigDao.findPortSets());
               smalSet.remove(null);
               bigSet.removeAll(smalSet); 
               return -1;
           } else {
               smalSet.addAll(portConfigDao.findPortSets());
               smalSet.remove(null);
               bigSet.removeAll(smalSet);
           }*/
           bigSet.removeAll(smalSet);
           if (CollectionUtils.isEmpty(bigSet)) {
               smalSet.addAll(portConfigDao.findPortSets());
               smalSet.remove(null);
               return -1;
              }
           Object[] obj =bigSet.toArray();
           int portSet=Integer.valueOf(obj[(int)(Math.random()*obj.length)]
                              .toString());
           smalSet.add(portSet);
           return portSet;
       }
   }

	@RequestMapping(value = {"service/generatePortSet.do"} , method = RequestMethod.GET)
	@ResponseBody
	public String generatePortSet(){
		Map<String, String> map = new HashMap<String, String>();
		int mapPort = vailPortSet();
		if(-1 == mapPort){
			map.put("ERROR","error");
			return JSON.toJSONString(map);
		}

		map.put("mapPort", String.valueOf(mapPort));
		return JSON.toJSONString(map);
	}
	
	/**
	 *   删除集合中的某个元素
	 * @param set
	 * @return 
	 */
	@RequestMapping(value = { "service/removeSet.do" } , method = RequestMethod.GET)
	public void removeSet(int set){
	    log.info("移除的端口："+set);
		 smalSet.remove(set);
	}
	
	/**
	 * serviceName 判重
	 * 
	 * @param serviceName
	 * @return
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
	 * stop container and services
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("service/stopContainer.do")
	@ResponseBody
	public String stopContainer(long id) {
		Service service = serviceDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		log.debug("service:=========" + service);
		try {
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			ReplicationController controller = client.updateReplicationController(service.getServiceName(), 0);

			if (controller == null) {
				map.put("status", "500");
			} else {
				map.put("status", "200");
				service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_STOPPED);
				serviceDao.save(service);
			}
		} catch (KubernetesClientException e) {
			map.put("status", "500");
			map.put("msg", e.getStatus().getMessage());
			log.error("stop service error:" + e.getStatus().getMessage());
		}

		return JSON.toJSONString(map);
	}

	@RequestMapping("service/modifyimgVersion.do")
	@ResponseBody
	public String modifyimgVersion(long id, String serviceName, String imgVersion, String imgName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		try {
			if (service.getImgVersion().equals(imgVersion)) {
				map.put("status", "500");
			} else {
				KubernetesAPIClientInterface client = kubernetesClientService.getClient();
				ReplicationController controller = client.getReplicationController(serviceName);
				String NS = controller.getMetadata().getNamespace();
				String cmd = "kubectl rolling-update " + serviceName + " --namespace=" + NS
						+ " --update-period=10s --image="
						+ dockerClientService.generateRegistryImageName(imgName, imgVersion);
				boolean flag = cmdexec(cmd);
				if (flag) {
					service.setImgVersion(imgVersion);
					serviceDao.save(service);
					map.put("status", "200");
				} else {
					map.put("status", "400");
				}
			}
		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			log.error("modify imageVersion error:" + e.getStatus().getMessage());
		}

		return JSON.toJSONString(map);
	}

	/**
	 * ssh cmd
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean cmdexec(String cmd) {
		String hostIp = kubernetesClientService.getK8sAddress();
		String name = kubernetesClientService.getK8sUsername();
		String password = kubernetesClientService.getK8sPasswrod();
		try {
			SshConnect.connect(name, password, hostIp, 22);
			boolean b = false;
			String rollingLog = SshConnect.exec(cmd, 1000);
			while (!b) {
				String str = SshConnect.exec("", 10000);
				if (StringUtils.isNotBlank(str)) {
				    rollingLog += str;
				}
				b = (str.endsWith("$") || str.endsWith("#"));
				//b = str.endsWith("updated");
			}
			log.info("rolling-update log:-"+rollingLog);
			String result = SshConnect.exec("echo $?", 1000);
			if (StringUtils.isNotBlank(result)) {
			    if (!('0' == (result.trim().charAt(result.indexOf("\n")+1)))) {
			        new InterruptedException();
			    }
			} else {
			    new InterruptedException();
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			log.error("error:执行command失败");
			return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("error:ssh连接失败");
			return false;
		} finally {
			SshConnect.disconnect();
		}
		return true;
	}
	
	@RequestMapping("service/findImageVersion.do")
    @ResponseBody
	public String findImageVersion(String imageName){
	    User cUser = CurrentUserUtils.getInstance().getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Image> images = imageDao.findByImageVarsionOfName(cUser.getId(), imageName);
        map.put("data", images);
        return JSON.toJSONString(map);
	}

	/**
	 * 修改服务数量
	 * 
	 * @param id
	 * @param addservice
	 * @return
	 */
	@RequestMapping("service/modifyServiceNum.do")
	@ResponseBody
	public String modifyServiceNum(long id, Integer addservice) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);

		if (service.getInstanceNum() == addservice) {
			map.put("status", "300");
		} else {
			try {
				service.setInstanceNum(addservice);
				serviceDao.save(service);
				KubernetesAPIClientInterface client = kubernetesClientService.getClient();
				ReplicationController controller = client.updateReplicationController(service.getServiceName(),
						addservice);
				if (controller != null) {
					map.put("status", "200");
				} else {
					map.put("status", "400");
				}

			} catch (KubernetesClientException e) {
				map.put("status", "400");
				map.put("msg", e.getStatus().getMessage());
				log.error("modify servicenum error:" + e.getStatus().getMessage());
			} catch (Exception e) {
				map.put("status", "400");
				log.error("modify service error :" + e);
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * modify cpu and ram
	 * 
	 * @param id
	 * @param cpus
	 * @param rams
	 * @return
	 */
	@RequestMapping("service/modifyCPU.do")
	@ResponseBody
	public String modifyCPU(long id, Double cpus, String rams) {
		Map<String, Object> map = new HashMap<String, Object>();
		Service service = serviceDao.findOne(id);
		try {
			service.setCpuNum(cpus);
			service.setRam(rams);
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			ReplicationController controller = client.getReplicationController(service.getServiceName());
			List<com.bonc.epm.paas.kubernetes.model.Container> containers = controller.getSpec().getTemplate().getSpec()
					.getContainers();
			for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
				setContainer(container, cpus, rams);
			}
			client.updateReplicationController(service.getServiceName(), controller);
			serviceDao.save(service);
			map.put("status", "200");
		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			log.error("modify cpu&memory error:" + e.getStatus().getMessage());
		} catch (Exception e) {
			map.put("status", "400");
			log.error("modify cpu&memory error:" + e);
		}
		return JSON.toJSONString(map);
	}

	public com.bonc.epm.paas.kubernetes.model.Container setContainer(
			com.bonc.epm.paas.kubernetes.model.Container container, Double cpus, String rams) {
		ResourceRequirements requirements = new ResourceRequirements();
		requirements.getLimits();
		Map<String, Object> def = new HashMap<String, Object>();
		def.put("cpu", cpus);
		def.put("memory", rams + "Mi");
		Map<String, Object> limit = new HashMap<String, Object>();
		// limit = kubernetesClientService.getlimit(limit);
		limit.put("cpu", cpus);
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
	@RequestMapping("service/delContainer.do")
	@ResponseBody
	public String delContainer(long id) {
		Service service = serviceDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		String confName = service.getServiceName();
		String configName = CurrentUserUtils.getInstance().getUser().getUserName() + "-" + service.getServiceName();
		try {
			if (service.getStatus() != 1) {
				KubernetesAPIClientInterface client = kubernetesClientService.getClient();
				ReplicationController controller = client.getReplicationController(service.getServiceName());
				if (controller != null) {
					client.updateReplicationController(service.getServiceName(), 0);
					client.deleteReplicationController(service.getServiceName());
					client.deleteService(service.getServiceName());
					TemplateEngine.deleteConfig(confName, configName, templateConf);
				}
			}
			map.put("status", "200");
			serviceDao.delete(id);
			envVariableDao.deleteByServiceId(id);
			
			// 删除服务 释放绑定的端口
			List<PortConfig> bindPort = portConfigDao.findByServiceId(id);
			if (CollectionUtils.isNotEmpty(bindPort)) {
			    for (PortConfig oneRow : bindPort) {
			        smalSet.remove(Integer.valueOf(oneRow.getContainerPort()));
			    }
			    portConfigDao.deleteByServiceId(id);
			}
			
			// 更新挂载卷的使用状态
			if (!"0".equals(service.getVolName()) && StringUtils.isNotBlank(service.getVolName())) {
			    this.updateStorageType(service.getVolName(), service.getServiceName());
			}
		} catch (KubernetesClientException e) {
			map.put("status", "400");
			map.put("msg", e.getStatus().getMessage());
			log.error("del service error:" + e.getStatus().getMessage());
		}

		return JSON.toJSONString(map);
	}

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
		try {
			for (long id : ids) {
				delContainer(id);
			}
			maps.put("status", "200");
		} catch (Exception e) {
			maps.put("status", "400");
			log.error("服务删除错误！");
		}
		return JSON.toJSONString(maps);

	}

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
			log.error("服务停止错误！");
		}
		return JSON.toJSONString(maps);
	}

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
			for (long id : ids) {
				CreateContainer(id);
			}
			maps.put("status", "200");
		} catch (Exception e) {
			maps.put("status", "400");
			e.printStackTrace();
			log.error("服务启动错误！");
		}
		return JSON.toJSONString(maps);
	}

	/**
	 * 给controller设置卷组挂载的信息
	 * 
	 * @param controller
	 * @param storageName
	 * @return ReplicationController
	 */
	private ReplicationController setVolumeStorage(ReplicationController controller, String storageName,
			String mountPath) {

		ReplicationControllerSpec rcSpec = controller.getSpec();
		PodTemplateSpec template = rcSpec.getTemplate();
		PodSpec podSpec = template.getSpec();
		List<Volume> volumes = new ArrayList<Volume>();
		Volume volume = new Volume();
		volume.setName("cephfs");
		CephFSVolumeSource cephfs = new CephFSVolumeSource();
		List<String> monitors = new ArrayList<String>();
		System.out.println("CEPH_MONITOR:" + CEPH_MONITOR);
		String[] ceph_monitors = CEPH_MONITOR.split(",");
		for (String ceph_monitor : ceph_monitors) {
			monitors.add(ceph_monitor);
		}
		cephfs.setMonitors(monitors);
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		cephfs.setPath("/" + namespace + "/" + storageName);
		cephfs.setUser("admin");
		LocalObjectReference secretRef = new LocalObjectReference();
		secretRef.setName("ceph-secret");
		cephfs.setSecretRef(secretRef);
		cephfs.setReadOnly(false);
		volume.setCephfs(cephfs);
		volumes.add(volume);
		podSpec.setVolumes(volumes);
		List<com.bonc.epm.paas.kubernetes.model.Container> containers = podSpec.getContainers();
		for (com.bonc.epm.paas.kubernetes.model.Container container : containers) {
			List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
			VolumeMount volumeMount = new VolumeMount();
			volumeMount.setMountPath(mountPath);
			volumeMount.setName("cephfs");
			volumeMounts.add(volumeMount);
			container.setVolumeMounts(volumeMounts);
		}
		return controller;
	}

	/**
	 * 更新挂载卷状态
	 * 
	 * @param volName
	 * @return
	 */
public void updateStorageType(String volName, String serviceName) {

		// userId
		long userId = CurrentUserUtils.getInstance().getUser().getId();

		Storage storage = storageDao.findByCreateByAndStorageName(userId, volName);
		// 设置使用状态
		List<Service> lstService = serviceDao.findByCreateByAndVolName(userId, volName);
		if (lstService.size() == 0) {
			storage.setUseType(StorageConstant.NOT_USER);
		} else {
			storage.setUseType(StorageConstant.IS_USER);
		}
		// 设置挂载点
		StringBuilder newMp = new StringBuilder();
		for (Service service : lstService) {
			newMp.append(service.getServiceName());
			newMp.append(":");
			newMp.append(service.getMountPath());
			newMp.append(";");
		}
		storage.setMountPoint(newMp.toString());
		storageDao.save(storage);
	}
/**
 * 
 * Description: <br>
 *   获取挂在地址
 * 
 * @return 
 * @see
 */
@RequestMapping(value = { "service/getMountPath.do" }, method = RequestMethod.GET)
@ResponseBody
public String getMountPath(String volume){
    String mountPath = storageDao.findByVolume(volume);
    if(StringUtils.isNotBlank(mountPath)){
        mountPath = mountPath.substring(mountPath.indexOf(":/")+1, mountPath.lastIndexOf(";")) ;
     }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("mountPath",mountPath);
    return JSON.toJSONString(map);
    
}
}
