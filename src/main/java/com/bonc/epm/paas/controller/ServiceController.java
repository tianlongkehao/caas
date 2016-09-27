package com.bonc.epm.paas.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.bonc.epm.paas.kubernetes.exceptions.Status;
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
 * ServiceController
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
     * 服务数据层接口
     */
    @Autowired
	private ServiceDao serviceDao;
	
    /**
     * 环境变量数据层接口
     */
    @Autowired
	private EnvVariableDao envVariableDao;
	
    /**
     * portConfig数据层接口
     */
    @Autowired
	private PortConfigDao portConfigDao;
    
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
     * TemplateConf
     */
    @Autowired
	private TemplateConf templateConf;

    /**
     * esConf
     */
    @Autowired
	private esConf esConf;
    
    /**
     * 获取ceph.monitor数据
     */
    @Value("${ceph.monitor}")
	private String CEPH_MONITOR;
	
	/**
     * 内存和cpu的比例大小
     */
    @Value("${ratio.memtocpu}")
    private String RATIO_MEMTOCPU = "4";
	
    /**
     * NginxServerConf
     */
    @Autowired
	private NginxServerConf nginxServerConf;
    
    /**
     * Description: <br>
     * 查询所有的服务
     * @return String
     */
    @RequestMapping("service/listService.do")
	@ResponseBody
	public String list() {
        List<Service> serviceList = new ArrayList<Service>();
        for (Service service : serviceDao.findAll()) {
            serviceList.add(service);
        }
        LOG.debug("services:===========" + serviceList);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "200");
        map.put("data", serviceList);
        return JSON.toJSONString(map);
    }

	/**
	 * Description: <br>
	 * 展示container和services
	 * @param model 
	 * @return String
	 */
    @RequestMapping(value = { "service" }, method = RequestMethod.GET)
	public String containerLists(Model model) {
        User currentUser = CurrentUserUtils.getInstance().getUser();
		// 获取特殊条件的pods
        try {
            getServiceSource(model, currentUser.getId());
            getNginxServer(model);
            //getleftResource(model);
        } 
        catch (KubernetesClientException e) {
            model.addAttribute("msg", e.getStatus().getMessage());
            LOG.debug("service show:" + e.getStatus().getMessage());
            return "workbench.jsp";
        }
        model.addAttribute("menu_flag", "service");
        return "service/service.jsp";
    }
    
    /**
     * Description: <br>
     * 获取nginxService参数
     * @param model  
     */
    public void getNginxServer(Model model) {
        model.addAttribute("DMZ", nginxServerConf.getDMZ());
        model.addAttribute("USER", nginxServerConf.getUSER());
    }
    
    /**
     * Description: <br>
     * 查询当前用户创建的服务和容器
     * @param model 
     * @param id 当前用户id
     */
    public void getServiceSource(Model model, long id) {
//        List<Service> serviceList = new ArrayList<Service>();
//        List<Container> containerList = new ArrayList<Container>();
//        KubernetesAPIClientInterface client = kubernetesClientService.getClient();
//        for (Service service : serviceDao.findByCreateBy(id)) {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("app", service.getServiceName());
//            PodList podList = client.getLabelSelectorPods(map);
//            if (podList != null) {
//                List<Pod> pods = podList.getItems();
//                if (CollectionUtils.isNotEmpty(pods)) {
//                    int i = 1;
//                    for (Pod pod : pods) {
//                        String podName = pod.getMetadata().getName();
//                        Container container = new Container();
//                        container.setContainerName(service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
//						container.setServiceid(service.getId());
//						if (pod.getStatus().getPhase().equals("Running")) {
//							container.setContainerStatus(0);
//						} else {
//							container.setContainerStatus(1);
//						}
//						containerList.add(container);
//					}
//				}
//			}
//			serviceList.add(service);
//		}
//		model.addAttribute("containerList", containerList);
		model.addAttribute("serviceList", serviceDao.findByCreateBy(id));
	}

    /**
     * Description: <br>
     * 根据服务名称模糊查询服务和容器
     * @param model 
     * @param searchNames 服务名称
     * @return String
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
							if (pod.getStatus().getPhase().equals("Running")) {
								container.setContainerStatus(0);
							} else {
								container.setContainerStatus(1);
							}
							containerList.add(container);
						}
					}
				}
				serviceList.add(service);
				LOG.debug("service=========" + service);
			}
		} catch (Exception e) {
			LOG.error("服务查询错误：" + e);
		}
		model.addAttribute("serviceList", serviceList);
		model.addAttribute("containerList", containerList);

		return "service/service.jsp";
	}
	
    /**
     * Description: <br>
     * 根据服务Id查询当前服务和容器
     * @param model 
     * @param id 
     * @return  String
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
     * 跳转进入service-import.jsp页面
     * @param model 
     * @return String
     */
    @RequestMapping(value = { "service/import" })
	public String serviceImport(Model model) {
        model.addAttribute("menu_flag", "service");
        return "service/service-import.jsp";
    }

    /**
     * Description: <br>
     *  根据Id查找container和servies，跳转进入服务详细页面
     * @param model 
     * @param id 服务Id
     * @return String
     */
    @RequestMapping(value = { "service/detail/{id}" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
        System.out.printf("id: " + id);
        Service service = serviceDao.findOne(id);
		//service.setProxyZone(substr(service.getProxyZone()));
        List<EnvVariable> envVariableList = envVariableDao.findByServiceId(id);
        List<PortConfig> portConfigList = portConfigDao.findByServiceId(service.getId());
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
                	for(com.bonc.epm.paas.kubernetes.model.Container k8scontainer : pod.getSpec().getContainers()){
                		
                		// 获取pod名称
                		String podName = pod.getMetadata().getName();
                		//获取container名称
                		String containerName = k8scontainer.getName();
                		// 初始化es客户端
                		ESClient esClient = new ESClient();
                		esClient.initESClient(esConf.getHost(),esConf.getClusterName());
                		// 设置es查询日期，数据格式，查询的pod名称
//                    String s = esClient.search("logstash-" + dateToString(new Date()), "fluentd", podName);
                		Calendar calendar = Calendar.getInstance();
                		calendar.add(Calendar.HOUR_OF_DAY, -8);
                		calendar.add(Calendar.MINUTE, -3);
                		String dateString = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+00:00").format(calendar.getTime());
                		String s = esClient.search("fluentd", podName,containerName,dateString,"9999-12-31T00:00:00+00:00");
                		
                		// 关闭es客户端
                		esClient.closeESClient();
                		// 拼接日志格式
                		String add = "[" + "App-" + i + "] [" + podName + "] ["+containerName+"]：";
                		s = add + s.replaceAll("\n", "\n" + add).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                		
                		s = s.substring(0, s.length() - add.length());
                		Container container = new Container();
                		container.setContainerName(service.getServiceName() + "-" + service.getImgVersion() + "-" + i++);
                		container.setServiceid(service.getId());
                		containerList.add(container);
                		logList.add(s);
                	}
                }
            }
        }
        model.addAttribute("id", id);
        model.addAttribute("containerList", containerList);
        model.addAttribute("logList", logList);
        model.addAttribute("service", service);
        model.addAttribute("envVariableList", envVariableList);
        model.addAttribute("portConfigList", portConfigList);
        model.addAttribute("menu_flag", "service");
        return "service/service-detail.jsp";
    }

	/**
	 * Description: <br>
	 * 将字符串重新组装
	 * 主要用于proxyZone的重新组装
	 * @param str 
	 * @return String
	 */
    public String substr(String str){
        String strs="";
        String[] splitstr=str.split("'");
        for (int i = 0;i < splitstr.length;i++) {
            if ((i+1)%4==0) {
				//System.out.println(splitstr[i]);
                strs+=splitstr[i]+",";
            }
        }
        strs=strs.substring(0,strs.length()-1);
        return strs;
    }

	/**
	 * Description: <br>
	 * 获取当前服务的日志
	 * @param id 服务Id
	 * @param date 日期
	 * @return String
	 */
    @RequestMapping("service/detail/getlogs.do")
	@ResponseBody
	public String getServiceLogs(long id, String date) {
        Service service = serviceDao.findOne(id);
        KubernetesAPIClientInterface client = kubernetesClientService.getClient();
        List<String> logList = new ArrayList<String>();
//        String logStr = "";
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
                    	
                    	for(com.bonc.epm.paas.kubernetes.model.Container container : pod.getSpec().getContainers()){
                    		
                    		// 获取pod名称
                    		String podName = pod.getMetadata().getName();
                    		
                    		//获取container名称
                    		String containerName = container.getName();
                    		
                    		// 初始化es客户端
                    		ESClient esClient = new ESClient();
                    		esClient.initESClient(esConf.getHost(),esConf.getClusterName());
                    		String s = null;
                    		if (date != "") {
                    			// 设置es查询日期，数据格式，查询的pod名称
//                            s = esClient.search("logstash-" + date.replaceAll("-", "."), "fluentd", podName);
                    			Calendar calendar = Calendar.getInstance();
                    			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date));
                    			calendar.add(Calendar.HOUR_OF_DAY, -8);
                    			String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00").format(calendar.getTime());
                    			s = esClient.search("fluentd", podName,containerName,dateString,"9999-12-31T00:00:00+00:00");
                    		} 
                    		else {
                    			// 设置es查询日期，数据格式，查询的pod名称
                    			Calendar calendar = Calendar.getInstance();
                    			calendar.add(Calendar.HOUR_OF_DAY, -8);
                    			calendar.add(Calendar.MINUTE, -3);
                    			String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00").format(calendar.getTime());
                    			s = esClient.search("fluentd", podName,containerName,dateString,"9999-12-31T00:00:00+00:00");
//                            s = esClient.search("logstash-" + dateToString(new Date()), "fluentd", podName);
                    		}
                    		
                    		// 关闭es客户端
                    		esClient.closeESClient();
                    		// 拼接日志格式
                    		String add = "[" + "App-" + i + "] [" + podName + "] ["+containerName+"]：";
//                        s = add + s.replaceAll("\n", "\n" + add);
                    		s = add + s.replaceAll("\n", "\n" + add).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                    		
                    		s = s.substring(0, s.length() - add.length());
//                    		logStr = logStr.concat(s);
                    		logList.add(s);
                    	}
                    }
                }
            }
//            datamap.put("logStr", logStr);
            datamap.put("logList",logList);
            datamap.put("status", "200");
        } 
        catch (Exception e) {
            datamap.put("status", "400");
            LOG.error("日志读取错误：" + e);
        }

        return JSON.toJSONString(datamap);

    }
    
    /**
     * Description: <br>
     * 日期格式的转换
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
     * @param imgID 
     * @param imageName 
     * @param imageVersion 
     * @param resourceName 
     * @param model 
     * @return  String
     */
    @RequestMapping(value = { "service/add" }, method = RequestMethod.GET)
	public String create(String imgID, String imageName, String imageVersion, String resourceName, Model model) {		
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
                }
                else {
                    portConfig.setMapPort("-1");
                }
                list.add(portConfig);
            }
            model.addAttribute("portConfigs",JSON.toJSONString(list));
        }
		//TODO 
        boolean flag = getleftResource(model);
        if (!flag) {
            model.addAttribute("msg", "请创建租户！");
            return "service/service.jsp";
        }
		//TODO

		// 获取配置文件中nginx选择区域
        getNginxServer(model);
        User cUser = CurrentUserUtils.getInstance().getUser();
        Map<String, Object> map = new HashMap<String, Object>();
		
        model.addAttribute("imgID", imgID);
        model.addAttribute("resourceName", resourceName);
        model.addAttribute("imageName", imageName);
        model.addAttribute("imageVersion", imageVersion);
        model.addAttribute("isDepoly", isDepoly);
        model.addAttribute("menu_flag", "service");
        return "service/service_create.jsp";
    }
	
	/**
	 * Description: <br>
	 * 获取基础镜像的端口
	 * @param imgID imgID
	 * @return List
	 */
    private List<PortConfig> getBaseImageExposedPorts(String imgID) {
        try {
            Ci ci = ciDao.findByImgId(Long.valueOf(imgID));
            if (null != ci) {
                Image image = imageDao.findById(Long.valueOf(imgID));
                if (null == image) {
                    image = imageDao.findById(ci.getBaseImageId());
                }
                 
                if (null != image && StringUtils.isNotBlank(image.getImageId())) {
                    dockerClientService.pullImage(image.getName(), image.getVersion());
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
                            } 
                            else {
                                portConfig.setMapPort("-1");
                            }
                            tmpPortConfigs.add(portConfig);
                        }
                        return tmpPortConfigs;
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Description: <br>
     * getPortConfig 获取port端口
     * @param imgID imgID
     * @return String
     */
    @RequestMapping(value = { "service/getPortConfig.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String getPortConfig(String imgID){
        List<PortConfig> list=getBaseImageExposedPorts(imgID);
        Map<String,Object> map = new HashMap<String,Object>();
        if (null == list) {
            list = new ArrayList<PortConfig>();
            PortConfig portConfig = new PortConfig();
            portConfig.setContainerPort("8080");
            int randomPort = vailPortSet();
            if (-1 != randomPort) {
                portConfig.setMapPort(String.valueOf(randomPort));                       
            } 
            else {
                portConfig.setMapPort("-1");
            }
            list.add(portConfig);
        }
        map.put("data",list);
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 获取当前租户的剩余资源
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

                System.out.println(quota.getStatus().getHard().get("cpu"));
                System.out.println(quota.getStatus().getUsed().get("cpu"));

                double leftCpu = kubernetesClientService.transCpu(quota.getStatus().getHard().get("cpu"))
						- kubernetesClientService.transCpu(quota.getStatus().getUsed().get("cpu")) * Integer.valueOf(RATIO_MEMTOCPU);

                long leftmemory = hard - used;

                System.out.println(hard + "  " + used);
                model.addAttribute("leftcpu", leftCpu);
                model.addAttribute("leftmemory", leftmemory / 1024);
            } else {
                LOG.info("用户 " + currentUser.getUserName() + " 没有定义名称为 " + currentUser.getNamespace() + " 的Namespace ");
            }

        } 
        catch (Exception e) {
            LOG.error("getleftResource error:" + e);
            return false;
        }

        return true;
    }

    /**
     * Description: <br>
     * 展示镜像
     * @return String
     */
    @RequestMapping(value = { "service/images" }, method = RequestMethod.GET)
	@ResponseBody
	public String imageList() {
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Image> images = imageDao.findAll(userId);
        /*if (CollectionUtils.isNotEmpty(images)) {
		    for (Image image : images) {
		        image.setPortConfigs(getBaseImageExposedPorts(String.valueOf(image.getId())));
		    }
		}*/
        map.put("data", images);
        System.out.println(JSON.toJSONString(map));
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 根据镜像名称模糊查询镜像
     * @param imageName 镜像名称
     * @return String
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
     * Description: <br>
     * create container and services from dockerfile
     * @param id 
     * @return String
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
        } 
        catch (KubernetesClientException e) {
            controller = null;
        }
        try {
            k8sService = client.getService(service.getServiceName());
        } 
        catch (KubernetesClientException e) {
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
            } 
            else {
                controller = client.updateReplicationController(service.getServiceName(), service.getInstanceNum());
            }
            if (k8sService == null) {
                k8sService = kubernetesClientService.generateService(service.getServiceName(),portConfigs,service.getProxyZone(),service.getServicePath(),service.getProxyPath());
                k8sService = client.createService(k8sService);
            }
            if (controller == null || k8sService == null || controller.getSpec().getReplicas() != service.getInstanceNum()) {
                map.put("status", "500");
            }
            else {
                map.put("status", "200");
                service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_PENDING);
                serviceDao.save(service);
            }
        }
        catch (KubernetesClientException e) {
            map.put("status", "500");
            map.put("msg", e.getStatus().getMessage());
            LOG.error("create service error:" + e.getStatus().getMessage());
        }

        return JSON.toJSONString(map);

    }
    
    /**
     * Description: <br>
     * 服务创建
     * @param service 
     * @param resourceName 
     * @param envVariable 
     * @param portConfig 
     * @return String
     */
    @RequestMapping("service/constructContainer.do")
	public String constructContainer(Service service, String resourceName,String envVariable,String portConfig) {
        User currentUser = CurrentUserUtils.getInstance().getUser();
        service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
        service.setCreateDate(new Date());
        service.setCreateBy(currentUser.getId());
/*        if (!StringUtils.isEmpty(resourceName) && !service.getServicePath().trim().equals(resourceName.substring(0, resourceName.indexOf(".")).trim())) {
            service.setServicePath(resourceName.substring(0, resourceName.indexOf(".")).trim());
        }*/
        serviceDao.save(service);
		
		//将服务中的环境变量循环遍历，保存到相关联的实体类中；
        if (StringUtils.isNotEmpty(envVariable)) {
            String[] envKeyAndValues = envVariable.split(";");
            for (String envKeyAndValue : envKeyAndValues ) {
                EnvVariable envVar = new EnvVariable();
                envVar.setCreateBy(currentUser.getId());
                envVar.setEnvKey(envKeyAndValue.substring(0,envKeyAndValue.indexOf(",")));
                envVar.setEnvValue(envKeyAndValue.substring(envKeyAndValue.indexOf(",")+1));
                envVar.setCreateDate(new Date());
                envVar.setServiceId(service.getId());
                envVariableDao.save(envVar);
            }
        }
		//保存到与service关联的portConfig实体类
        if (StringUtils.isNotEmpty(portConfig)) {
            JSONArray jsonArray = JSONArray.parseArray(portConfig);  
            for (int i = 0 ; i < jsonArray.size(); i ++ ) {
                PortConfig portCon = new PortConfig();
                portCon.setContainerPort(jsonArray.getJSONObject(i).getString("containerPort").trim());
                portCon.setMapPort(jsonArray.getJSONObject(i).getString("mapPort").trim());
                portCon.setProtocol(jsonArray.getJSONObject(i).getString("protocol").trim());
                //portCon.setOptions(Integer.valueOf(jsonArray.getJSONObject(i).getString("option")));
                portCon.setCreateDate(new Date());
                portCon.setServiceId(service.getId());
                portConfigDao.save(portCon);
				// 向map中添加生成的node端口
                smalSet.add(Integer.valueOf(portCon.getContainerPort().trim()));
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
        service.setServiceAddr("http://"+currentUser.getUserName() + "." + templateConf.getServerAddr());
        serviceDao.save(service);
		// 更新挂载卷的使用状态
        if (!"0".equals(service.getVolName()) && StringUtils.isNotBlank(service.getVolName())) {
            this.updateStorageType(service.getVolName(), service.getServiceName());
        }
        LOG.debug("container--Name:" + service.getServiceName());
        return "redirect:/service";
    }
	
	/**
	 * 当前用户创建服务时匹配服务路径和nginx路径 和服务名称不重复
	 * @param proxyPath  
	 * @param serviceName  
	 * @return String
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
            } 
            else if (service.getProxyPath().equals(proxyPath)) {
                map.put("status", "400");
                break;
            }
            else {
                map.put("status", "200");
            }
        }
        return JSON.toJSONString(map);
    }
	
	/**
	 * 加载环境变量模板数据
	 * @return String
	 */
    @RequestMapping("service/loadEnvTemplate.do")
    @ResponseBody
	public String loadEnvTemplate(){
        Map<String, Object> map = new HashMap<String, Object>();
        User cUser = CurrentUserUtils.getInstance().getUser();
        List<String> templateNames = envTemplateDao.findTemplateName(cUser.getId());
        map.put("data", templateNames);
        return JSON.toJSONString(map);
    }
	
    /**
     * Description: <br>
     * 保存环境变量模板
     * @param templateName 模板名称
     * @param envVariable 环境变量
     * @return String
     */
    @RequestMapping("service/saveEnvTemplate.do")
	@ResponseBody
	public String saveEnvTemplate (String templateName,String envVariable) {
        Map<String, Object> map = new HashMap<String, Object>();
        User cUser = CurrentUserUtils.getInstance().getUser();
        for (EnvTemplate envTemplate : envTemplateDao.findByCreateBy(cUser.getId())) {
            if (envTemplate.getTemplateName().equals(templateName)) {
                map.put("status", "400"); //模板名称重复
                return JSON.toJSONString(map);
            }
        }
        if (StringUtils.isNotEmpty(envVariable)) {
            String[] envKeyAndValues = envVariable.split(";");
            for (String envKeyAndValue : envKeyAndValues ) {
                EnvTemplate envTemplate = new EnvTemplate();
                envTemplate.setCreateBy(cUser.getId());
                envTemplate.setEnvKey(envKeyAndValue.substring(0,envKeyAndValue.indexOf(",")));
                envTemplate.setEnvValue(envKeyAndValue.substring(envKeyAndValue.indexOf(",")+1));
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
     * @param templateName 需要导入的模板名称
     * @return String
     * @see
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
     * Description: <br>
     * 生成有效的PORTSET,回收端口
     * @return  int
     */
    public int vailPortSet(){
        synchronized (this) {
            int offset = kubernetesClientService.getK8sEndPort() - kubernetesClientService.getK8sStartPort();
            Set<Integer> bigSet = Stream.iterate(kubernetesClientService.getK8sStartPort(), item -> item+1)
                                       .limit(offset)
                                       .collect(Collectors.toSet());
            smalSet.addAll(portConfigDao.findPortSets());
            smalSet.remove(null);
            bigSet.removeAll(smalSet);
            if (CollectionUtils.isEmpty(bigSet)) {
                return -1;
            }
            Object[] obj =bigSet.toArray();
            int portSet=Integer.valueOf(obj[(int)(Math.random()*obj.length)]
                              .toString());
            PortConfig validPort = portConfigDao.findByMapPort(Integer.toString(portSet));
              // 监测数据库中是否存在相同的端口号
            while ( null != validPort) {
                portSet=Integer.valueOf(obj[(int)(Math.random()*obj.length)]
                   .toString());
                validPort = portConfigDao.findByMapPort(Integer.toString(portSet));
            }
            smalSet.add(portSet);
            return portSet;
        }
    }

    /**
     * Description: <br>
     * generatePortSet 生成port端口
     * @return String
     */
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
     * Description: <br>
     * 移除端口集合中的端口
     * @param set  需要移除的端口
     */
    @RequestMapping(value = { "service/removeSet.do" } , method = RequestMethod.GET)
	public void removeSet(int set){
        LOG.info("移除的端口："+set);
        smalSet.remove(set);
    }
	
    /**
     * Description: <br>
     * serviceName 判重
     * @param serviceName 服务名称
     * @return  String
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
            } 
            else {
                map.put("status", "200");
            }
        }
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 停止服务
     * @param id 服务Id
     * @return String
     */
    @RequestMapping("service/stopContainer.do")
	@ResponseBody
	public String stopContainer(long id) {
        Service service = serviceDao.findOne(id);
        Map<String, Object> map = new HashMap<String, Object>();
        LOG.debug("service:=========" + service);
        try {
            KubernetesAPIClientInterface client = kubernetesClientService.getClient();
            ReplicationController controller = client.updateReplicationController(service.getServiceName(), 0);

            if (controller == null || controller.getSpec().getReplicas() != 0) {
                map.put("status", "500");
            }
            else {
                map.put("status", "200");
                service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_STOPPED);
                serviceDao.save(service);
            }
        }
        catch (KubernetesClientException e) {
            map.put("status", "500");
            map.put("msg", e.getStatus().getMessage());
            LOG.error("stop service error:" + e.getStatus().getMessage());
        }

        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 服务版本升级
     * @param id 服务Id
     * @param serviceName 服务名称
     * @param imgVersion 镜像版本信息
     * @param imgName 镜像名称
     * @return String
     */
    @RequestMapping("service/modifyimgVersion.do")
	@ResponseBody
	public String modifyimgVersion(long id, String serviceName, String imgVersion, String imgName) {
        Map<String, Object> map = new HashMap<String, Object>();
        Service service = serviceDao.findOne(id);
        try {
            if (service.getImgVersion().equals(imgVersion)) {
                map.put("status", "500");
            }
            else {
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
                }
                else {
                    String rollBackCmd = "kubectl rolling-update " + serviceName + " --namespace="+ NS 
				                               + " --rollback";
                    cmdexec(rollBackCmd);
                    map.put("status", "400");
                }
            }
        }
        catch (KubernetesClientException e) {
            map.put("status", "400");
            map.put("msg", e.getStatus().getMessage());
            LOG.error("modify imageVersion error:" + e.getStatus().getMessage());
        }

        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 执行命令行操作
     * @param cmd 命令行
     * @return  boolean
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
                b = (str.endsWith("$") || str.endsWith("#")) || str.endsWith("updated");
            }
            LOG.info("rolling-update LOG:-"+rollingLog);
            String result = SshConnect.exec("echo $?", 1000);
            if (StringUtils.isNotBlank(result)) {
                if (!('0' == (result.trim().charAt(result.indexOf("\n")+1)))) {
                    new InterruptedException();
                }
            }
            else {
                new InterruptedException();
            }
        } 
        catch (InterruptedException e) {
            LOG.error(e.getMessage());
            LOG.error("error:执行command失败");
            return false;
        } 
        catch (Exception e) {
            LOG.error(e.getMessage());
            LOG.error("error:ssh连接失败");
            return false;
        }
        finally {
            SshConnect.disconnect();
        }
        return true;
    }
	
    /**
     * Description: <br>
     * 根据镜像名称查询镜像
     * @param imageName 镜像名称
     * @return String
     */
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
     * Description: <br>
     * 修改服务数量
     * @param id 服务Id 
     * @param addservice 服务数量
     * @return String
     */
    @RequestMapping("service/modifyServiceNum.do")
	@ResponseBody
	public String modifyServiceNum(long id, Integer addservice) {
        Map<String, Object> map = new HashMap<String, Object>();
        Service service = serviceDao.findOne(id);

        if (service.getInstanceNum() == addservice) {
            map.put("status", "300");
        } 
        else {
            try {
                KubernetesAPIClientInterface client = kubernetesClientService.getClient();
                ReplicationController controller = client.updateReplicationController(service.getServiceName(),
						addservice);
                if (controller != null || controller.getSpec().getReplicas() == addservice) {
                    map.put("status", "200");
                    service.setInstanceNum(addservice);
                    serviceDao.save(service);
                } 
                else {
                    map.put("status", "400");
                }

            } 
            catch (KubernetesClientException e) {
                map.put("status", "400");
                map.put("msg", e.getStatus().getMessage());
                LOG.error("modify servicenum error:" + e.getStatus().getMessage());
            } 
            catch (Exception e) {
                map.put("status", "400");
                LOG.error("modify service error :" + e);
            }
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * modify cpu and ram
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
            controller = client.updateReplicationController(service.getServiceName(), controller);
            for (com.bonc.epm.paas.kubernetes.model.Container container2 :controller.getSpec().getTemplate().getSpec().getContainers()) {
				if (container2.getResources().getLimits().get("cpu") != cpus ||
					container2.getResources().getLimits().get("memory").equals(rams + "Mi") ||
					container2.getResources().getRequests().get("cpu") != cpus ||
					container2.getResources().getRequests().get("memory").equals(rams + "Mi")	) {
		            map.put("status", "400");
		            LOG.info("modifyCPU failed:id["+id+"], cpus["+cpus+"], rams["+rams+"]");
		            break;
				}
			}
            if (map.get("status") == null) {
            	map.put("status", "200");
            	serviceDao.save(service);
			}
        }
        catch (KubernetesClientException e) {
            map.put("status", "400");
            map.put("msg", e.getStatus().getMessage());
            LOG.error("modify cpu&memory error:" + e.getStatus().getMessage());
        }
        catch (Exception e) {
            map.put("status", "400");
            LOG.error("modify cpu&memory error:" + e);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * setContainer
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
    
    /**
     * Description: <br>
     * delete container and services from dockerfile
     * @param id 服务Id
     * @return String
     */
    @RequestMapping("service/delContainer.do")
	@ResponseBody
	public String delContainer(long id) {
        Service service = serviceDao.findOne(id);
        Map<String, Object> map = new HashMap<String, Object>();
        String confName = service.getServiceName();
        String configName = CurrentUserUtils.getInstance().getUser().getUserName() + "-" + service.getServiceName();
        try {
        	ReplicationController controller = new ReplicationController();
            if (service.getStatus() != 1) {
                KubernetesAPIClientInterface client = kubernetesClientService.getClient();
                controller = client.getReplicationController(service.getServiceName());
                if (controller != null) {
                	controller =  client.updateReplicationController(service.getServiceName(), 0);
                    if (controller !=null && controller.getSpec().getReplicas() == 0) {
                    	Status status = client.deleteReplicationController(service.getServiceName());
                    	if (status.getStatus().equals("Success")) {
                    		status = client.deleteService(service.getServiceName());
                    		if (status.getStatus().equals("Success")) {
                    			TemplateEngine.deleteConfig(confName, configName, templateConf);
                    		} else {
    	                    	map.put("status", "400");
    	                    	map.put("msg", "Delete a Service failed:ServiceName["+service.getServiceName()+"]");
    	                    	LOG.error("Delete a Service failed:ServiceName["+service.getServiceName()+"]");
    	                    	return JSON.toJSONString(map);
                    		}
						} else {
	                    	map.put("status", "400");
	                    	map.put("msg", "Delete a Replication Controller failed:ServiceName["+service.getServiceName()+"]");
	                    	LOG.error("Delete a Replication Controller failed:ServiceName["+service.getServiceName()+"]");
	                    	return JSON.toJSONString(map);
						}
        			} else {
                    	map.put("status", "400");
                    	map.put("msg", "Update a Replication Controller (update the number of replicas) failed:ServiceName["+service.getServiceName()+"]");
                    	LOG.error("Update a Replication Controller (update the number of replicas) failed:ServiceName["+service.getServiceName()+"]");
                    	return JSON.toJSONString(map);
        			}

                }else {
                	map.put("status", "400");
                	map.put("msg", "ReplicationController取得失败:ServiceName["+service.getServiceName()+"]");
                	LOG.error("ReplicationController取得失败:ServiceName["+service.getServiceName()+"]");
                	return JSON.toJSONString(map);
				}
            }
            map.put("status", "200");
            serviceDao.delete(id);
            envVariableDao.deleteByServiceId(id);
			
			// 删除服务 释放绑定的端口
            List<PortConfig> bindPort = portConfigDao.findByServiceId(id);
            if (CollectionUtils.isNotEmpty(bindPort)) {
                for (PortConfig oneRow : bindPort) {
                    smalSet.remove(Integer.valueOf(oneRow.getContainerPort().trim()));
                }
                portConfigDao.deleteByServiceId(id);
            }
			
			// 更新挂载卷的使用状态
            if (!"0".equals(service.getVolName()) && StringUtils.isNotBlank(service.getVolName())) {
                this.updateStorageType(service.getVolName(), service.getServiceName());
            }
        } 
        catch (KubernetesClientException e) {
            map.put("status", "400");
            map.put("msg", e.getStatus().getMessage());
            LOG.error("del service error:" + e.getStatus().getMessage());
        }

        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 删除多个服务
     * @param serviceIDs 服务id
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
        try {
            for (long id : ids) {
                delContainer(id);
            }
            maps.put("status", "200");
        } 
        catch (Exception e) {
            maps.put("status", "400");
            LOG.error("服务删除错误！");
        }
        return JSON.toJSONString(maps); 
    }
    
    /**
     * Description: <br>
     * 停止多个服务
     * @param serviceIDs 服务Id
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
        }
        catch (Exception e) {
            maps.put("status", "400");
            LOG.error("服务停止错误！");
        }
        return JSON.toJSONString(maps);
    }
    
    /**
     * Description: <br>
     * 启动多个服务
     * @param serviceIDs 服务id 
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
            for (long id : ids) {
                CreateContainer(id);
            }
            maps.put("status", "200");
        }
        catch (Exception e) {
            maps.put("status", "400");
            e.printStackTrace();
            LOG.error("服务启动错误！");
        }
        return JSON.toJSONString(maps);
    }

    /**
     * Description: <br>
     * 给controller设置卷组挂载的信息
     * @param controller 
     * @param storageName 
     * @param mountPath 
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
     * Description: <br>
     * 更新挂载卷状态
     * @param volName 
     * @param serviceName 
     * @see
     */
    public void updateStorageType(String volName, String serviceName) {

		// userId
        long userId = CurrentUserUtils.getInstance().getUser().getId();

        Storage storage = storageDao.findByCreateByAndStorageName(userId, volName);
		// 设置使用状态
        List<Service> lstService = serviceDao.findByCreateByAndVolName(userId, volName);
        if (lstService.size() == 0) {
            storage.setUseType(StorageConstant.NOT_USER);
        } 
        else {
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
     * Description: <br>
     * 获取挂在地址
     * @param volume 
     * @return String
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
    /**
     * Description: <br>
     * 根据服务id查询服务和容器
     * @param serviceID 服务ID
     * @return String
     */
    @RequestMapping(value = { "service/findservice.do" }, method = RequestMethod.GET)
    @ResponseBody
	public String findService(Long serviceID) {
        Map<String, Object> map = new HashMap<String, Object>();
        User currentUser = CurrentUserUtils.getInstance().getUser();
        KubernetesAPIClientInterface client = kubernetesClientService.getClient();
        Service service = serviceDao.findOne(serviceID);
        List<Container> containerList = new ArrayList<Container>();
		// 获取特殊条件的pods
		try {
				Map<String, String> mapapp = new HashMap<String, String>();
				mapapp.put("app", service.getServiceName());
				PodList podList = client.getLabelSelectorPods(mapapp);
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
							if (pod.getStatus().getPhase().equals("Running")) {
								container.setContainerStatus(0);
							} else {
								container.setContainerStatus(1);
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
    
}
