package com.bonc.epm.paas.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.dao.ContainerDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeItem;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ResourceRequirements;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.SshConnect;
import com.bonc.epm.paas.util.TemplateEngine;


 
/**
 * 
 * @author fengtao
 * @time 2015年12月3日
 */
@Controller
public class ServiceController {
	private static final Logger log = LoggerFactory.getLogger(ServiceController.class);
	
	@Autowired
	public ServiceDao serviceDao;
	
	@Autowired
	private ContainerDao containerDao;
	@Autowired
	private ImageDao imageDao;
	@Autowired
	public DockerClientService dockerClientService;
	@Autowired
	private KubernetesClientService kubernetesClientService;
	
	
	@RequestMapping("service/listService.do")
	@ResponseBody
	public String list(){
		List<Service> serviceList = new ArrayList<Service>();
		for(Service service:serviceDao.findAll()){
			serviceList.add(service);
		}
		log.debug("services:==========="+serviceList);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("status", "200");
		map.put("data", serviceList);
		return JSON.toJSONString(map);
	}
	/**
	 * 展示container和services
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"service"},method=RequestMethod.GET)
	public String containerLists(Model model){
		User  currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
	    List<Service> serviceList = new ArrayList<Service>();
	    List<Container> containerList = new ArrayList<Container>();
	    if((client.getNamespace(currentUser.getUserName()) == null)&( client.getResourceQuota(currentUser.getUserName())== null)){
	    	model.addAttribute("msg", "请规范创建租户！");
			return "workbench.jsp";
	    }
	  //获取特殊条件的pods
		
	    	try {
	    		for(Service service:serviceDao.findByCreateBy(currentUser.getId())){
	    			Map<String,String> map = new HashMap<String,String>();
	    	    	map.put("app", service.getServiceName());
	    	    	PodList podList = client.getLabelSelectorPods(map);
	    	    	if(podList!=null){
	    	    		List<Pod> pods = podList.getItems();
	    	    		if(!CollectionUtils.isEmpty(pods)){
	    	    			int i=0;
	    	    			for(Pod pod:pods){
	    	    				String podName = pod.getMetadata().getName();
	    	    				Container container = new Container();
	    	    				container.setContainerName(service.getServiceName()+"-"+service.getImgVersion()+"-"+i++);
	    	    				container.setServiceid(service.getId());
	    	    				containerList.add(container);
	    	    			}
	    	    		}
	    	    	}
	    	    	serviceList.add(service);
	    		}
			} catch (Exception e) {
				model.addAttribute("msg", "请检查K8S服务器连接！");
				return "workbench.jsp";
			}
		model.addAttribute("containerList", containerList);
		model.addAttribute("serviceList", serviceList);
		model.addAttribute("menu_flag", "service");
		
		return "service/service.jsp";
		
	}
	
	/**
	 * 展示container和services
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"findservice/{servName}"},method=RequestMethod.GET)
	public String searchService(Model model,@PathVariable String servName){
		User  currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
	    List<Service> serviceList = new ArrayList<Service>();
	    List<Container> containerList = new ArrayList<Container>();
	  //获取特殊条件的pods
		
	    	try {
	    		for(Service service:serviceDao.findByNameOf(currentUser.getId(),"%"+servName+"%")){
	    			Map<String,String> map = new HashMap<String,String>();
	    	    	map.put("app", service.getServiceName());
	    	    	PodList podList = client.getLabelSelectorPods(map);
	    	    	if(podList!=null){
	    	    		List<Pod> pods = podList.getItems();
	    	    		if(!CollectionUtils.isEmpty(pods)){
	    	    			int i=0;
	    	    			for(Pod pod:pods){
	    	    				String podName = pod.getMetadata().getName();
	    	    				Container container = new Container();
	    	    				container.setContainerName(service.getServiceName()+"-"+service.getImgVersion()+"-"+i++);
	    	    				container.setServiceid(service.getId());
	    	    				containerList.add(container);
	    	    			}
	    	    		}
	    	    	}
	    	    	serviceList.add(service);
	    		}
			} catch (Exception e) {
				model.addAttribute("msg", "请检查K8S服务器连接！");
				return "workbench.jsp";
			}
		model.addAttribute("containerList", containerList);
		model.addAttribute("serviceList", serviceList);
		model.addAttribute("menu_flag", "service");
		
		return "service/service.jsp";
		
	}
	/**
	 * 展示container和services
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"service/{id}"},method=RequestMethod.GET)
	public String service(Model model,@PathVariable long id){
		Service service = serviceDao.findOne(id);
		model.addAttribute("service", service);
		model.addAttribute("menu_flag", "service");
		
		return "service/service.jsp";
		
	}
	/**
	 * 根据id查找container和services
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value={"service/detail/{id}"},method=RequestMethod.GET)
	public String detail(Model model,@PathVariable long id){
        System.out.printf("id: " + id);
        Service service = serviceDao.findOne(id);
        KubernetesAPIClientInterface client = kubernetesClientService.getClient();
        List<Container> containerList = new ArrayList<Container>();
        List<String> logList = new ArrayList<String>();
        Map<String,String> map = new HashMap<String,String>();
    	map.put("app", service.getServiceName());
    	PodList podList = client.getLabelSelectorPods(map);
    	if(podList!=null){
    		List<Pod> pods = podList.getItems();
    		if(!CollectionUtils.isEmpty(pods)){
    			int i=0;
    			for(Pod pod:pods){
    				String podName = pod.getMetadata().getName();
    				String s  = client.getPodLog(podName);
    				Container container = new Container();
	    			container.setContainerName(service.getServiceName()+"-"+service.getImgVersion()+"-"+i++);
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
		return "service/service-detail.jsp";
	}
	/**
	 * 响应“部署”按钮
	 * @param imageName
	 * @param imageVersion
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"service/add"},method=RequestMethod.GET)
	public String create(String imgID,String imageName, String imageVersion, Model model){

		String isDepoly = "";
		if(imageName != null){
			isDepoly = "deploy";
		}
		
		boolean flag = getleftResource(model);
		if(!flag){
			model.addAttribute("msg","请创建租户！");
			return "service/service.jsp";
		}

		model.addAttribute("imgID", imgID);
		model.addAttribute("imageName", imageName);
		model.addAttribute("imageVersion", imageVersion);
		model.addAttribute("isDepoly",isDepoly);
		model.addAttribute("menu_flag", "service");

		return "service/service_create.jsp";
	}
	
	public boolean getleftResource(Model model){

		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		try {
//			ResourceQuota rq = client.getResourceQuota(currentUser.getUserName());
//		    String cpus = rq.getSpec().getHard().get("cpu");
//		    String rams = rq.getSpec().getHard().get("memory").replace("G", "");
//		    Integer irams = Integer.valueOf(rams)*1024;
//		    String pods = rq.getSpec().getHard().get("pods");
		    
		    LimitRange limitRange = client.getLimitRange(currentUser.getUserName());
		    LimitRangeItem limitRangeItem = limitRange.getSpec().getLimits().get(0);
		    String cpuMax = limitRangeItem.getMax().get("cpu").replace("m", "");
		    double icpuMax = Double.valueOf(cpuMax)/1024;
		    String cpudefault = limitRangeItem.getDefaultVal().get("cpu").replace("m", "");
		    double icpudefault = Double.valueOf(cpudefault)/1024;
		    String cpuMin = limitRangeItem.getMin().get("cpu").replace("m", "");
		    double icpuMin = Double.valueOf(cpuMin)/1024;
		    String memoryMax = limitRangeItem.getMax().get("memory").replace("M", "");
		    //Integer imemoryMax = Integer.valueOf(memoryMax)*1024;
		    String memorydefault = limitRangeItem.getDefaultVal().get("memory").replace("M", "");
		    //Integer imemorydefault = Integer.valueOf(memorydefault)*1024;
		    String memoryMin = limitRangeItem.getMin().get("memory").replace("M", "");
		    //Integer imemoryMin = Integer.valueOf(memoryMin)*1024;
		    System.out.println("icpudefault======="+cpudefault);
		    System.out.println("limitRange======="+limitRange.getSpec().getLimits());
			model.addAttribute("memorymin", memoryMin);
			model.addAttribute("memorymax", memoryMax);
			model.addAttribute("cpumin", icpuMin);
			model.addAttribute("cpumax", icpuMax);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	/**
	 * 展示镜像
	 * @return
	 */
	@RequestMapping(value={"service/images"},method=RequestMethod.GET)
	@ResponseBody
	public String imageList(){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> map = new HashMap<String,Object>();
//		List<Image> images = imageDao.findAllByCreator(CiConstant.IMG_TYPE_PUBLIC,CurrentUserUtils.getInstance().getUser().getId());
		List<Image> images = imageDao.findAll(userId);
		map.put("data", images);
		return JSON.toJSONString(map);
	}
	/**
	 * 展示镜像
	 * @return
	 */
	@RequestMapping("service/findimages.do")
	@ResponseBody
	public String findimages(String imageName){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> map = new HashMap<String,Object>();
//		List<Image> images = imageDao.findAllByCreator(CiConstant.IMG_TYPE_PUBLIC,CurrentUserUtils.getInstance().getUser().getId());
		List<Image> images = imageDao.findByNameOf(userId, "%"+imageName+"%");
		map.put("data", images);
		return JSON.toJSONString(map);
	}
	
	/**
	 * create container and services from dockerfile
	 * @param id
	 * @return
	 */
	@RequestMapping("service/createContainer.do")
	@ResponseBody
	public String CreateContainer(long id){
		Service service = serviceDao.findOne(id);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		//使用k8s管理服务
		String registryImgName = dockerClientService.generateRegistryImageName(service.getImgName(), service.getImgVersion());
		//如果没有则新增
		ReplicationController controller = client.getReplicationController(service.getServiceName());
		if(controller==null){
			controller = kubernetesClientService.generateSimpleReplicationController(service.getServiceName(),service.getInstanceNum(),registryImgName,8080,service.getCpuNum(),service.getRam());
			//controller = kubernetesClientService.generateSimpleReplicationController(service.getServiceName(),service.getInstanceNum(),registryImgName,8080);
			controller = client.createReplicationController(controller);
		}else{
			controller = client.updateReplicationController(service.getServiceName(), service.getInstanceNum());
		}
		com.bonc.epm.paas.kubernetes.model.Service k8sService = client.getService(service.getServiceName());
		if(k8sService==null){
			k8sService = kubernetesClientService.generateService(service.getServiceName(),80,8080,(int)service.getId()+kubernetesClientService.getK8sStartPort());
			k8sService = client.createService(k8sService);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(controller==null||k8sService==null){
			map.put("status", "500");
		}else{
			map.put("status", "200");
			service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_PENDING);
			serviceDao.save(service);
		}
		return JSON.toJSONString(map);
		
	}
	
	/**
	 * create container and services
	 * @param container
	 * @return
	 */
	@RequestMapping("service/constructContainer.do")
	public String constructContainer(Service service){
		User  currentUser = CurrentUserUtils.getInstance().getUser();
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
		service.setCreateDate(new Date());
		service.setCreateBy(currentUser.getId());
		serviceDao.save(service);
		Map<String, String > app = new HashMap<String, String>();
		app.put("confName", service.getServiceName());
		app.put("port", String.valueOf(service.getId()+kubernetesClientService.getK8sStartPort()));
		TemplateEngine.generateConfig(app, CurrentUserUtils.getInstance().getUser().getUserName()+"-"+service.getServiceName());
		TemplateEngine.cmdReloadConfig();
		service.setServiceAddr(TemplateEngine.getConfUrl());
		service.setPortSet(String.valueOf(service.getId()+kubernetesClientService.getK8sStartPort()));
		serviceDao.save(service);
		log.debug("container--Name:"+service.getServiceName());
		return "redirect:/service";
	}
	/**
	 * serviceName 判重
	 * @param serviceName
	 * @return
	 */
	@RequestMapping("service/serviceName.do")
	@ResponseBody
	public String containerName(String serviceName){
		Map<String, Object> map = new HashMap<String, Object>();
		for(Service service:serviceDao.findAll()){
			if(service.getServiceName().equals(serviceName))
			{
				map.put("status", "400");
				break;
			}else{
				map.put("status", "200");
			}
			
		}
		return JSON.toJSONString(map);
	}
	/**
	 * stop container and services
	 * @param id
	 * @return
	 */
	@RequestMapping("service/stopContainer.do")
	@ResponseBody
	public String stopContainer(long id){
		Service service = serviceDao.findOne(id);
		log.debug("service:========="+service);
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		ReplicationController controller = client.updateReplicationController(service.getServiceName(), 0);
		Map<String, Object> map = new HashMap<String, Object>();
		if(controller==null){
			map.put("status", "500");
		}else{
			map.put("status", "200");
			service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_STOPPED);
			serviceDao.save(service);
		}
		return JSON.toJSONString(map);
	}
	@RequestMapping("service/modifyimgVersion.do")
	@ResponseBody
	public String modifyimgVersion(long id,String serviceName,String imgVersion,String imgName){
		Map<String, Object> map = new HashMap<String,Object>();
		Service service = serviceDao.findOne(id);
		if(service.getImgVersion().equals(imgVersion)){
			map.put("status", "500");
		}else{
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			ReplicationController controller = client.getReplicationController(serviceName);
			String NS = controller.getMetadata().getNamespace();
			String cmd = "kubectl rolling-update "+serviceName+" --namespace="+NS+" --update-period=10s  --image="+getDockerip()+"/"+imgName+":"+imgVersion;
			boolean flag = cmdexec(cmd);
			if (flag) {
				service.setImgVersion(imgVersion);
				serviceDao.save(service);
				map.put("status", "200");
			}else{
				map.put("status", "400");
			}
		}
		return JSON.toJSONString(map);
	}
	
	public static String getDockerip(){
		String dockerip = null;
		Properties DockerProperties = new Properties();
		InputStream in = ServiceController.class.getClassLoader().getResourceAsStream("docker.io.properties");
		try {
			DockerProperties.load(in);
			in.close();
			dockerip = DockerProperties.getProperty("docker.io.username");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Docker.io.init:"+e.getMessage());
			e.printStackTrace();
		}
		return dockerip;
	}
	/**
	 * ssh cmd
	 * @param cmd
	 * @return
	 */
	public static boolean cmdexec(String cmd){
		String hostIp = null;
		String name = null;
		String password = null;
		Properties kubernetesProperties = new Properties();
    	InputStream in = ServiceController.class.getClassLoader().getResourceAsStream("kubernetes.api.properties");
    	try {
    		kubernetesProperties.load(in);
			in.close();
			hostIp = kubernetesProperties.getProperty("kubernetes.api.address");
			name = kubernetesProperties.getProperty("kubernetes.api.username"); 
			password = kubernetesProperties.getProperty("kubernetes.api.password");
		} catch (IOException e) {
			log.error("kubernetes.api.init:"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		try {
			SshConnect.connect(name, password, hostIp, 22);
			//String str = SshConnect.exec("echo $?", 1000);
			boolean b = false;
			SshConnect.exec(cmd, 1000);
			while(!b){
				String str = SshConnect.exec("echo $?", 1000);
				b = str.endsWith("#");
			}
			
		} catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("error:执行command失败");
            return false;
        } catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
            log.error(e.getMessage());
            log.error("error:ssh连接失败");
            return false;
		}finally {
			SshConnect.disconnect();
		}
		return true;
	}
	
	/**
	 * 修改服务数量
	 * @param id
	 * @param addservice
	 * @return
	 */
	@RequestMapping("service/modifyServiceNum.do")
	@ResponseBody
	public String modifyServiceNum(long id,Integer addservice){
		Map<String, Object> map = new HashMap<String,Object>();
		Service service = serviceDao.findOne(id);
		
		if(service.getInstanceNum()==addservice){
			map.put("status", "300");
		}else {
			try {
				service.setInstanceNum(addservice);
				serviceDao.save(service);
				KubernetesAPIClientInterface client = kubernetesClientService.getClient();
				ReplicationController controller = client.updateReplicationController(service.getServiceName(), addservice);
				if(controller!=null){
					map.put("status", "200");
				}else {
					map.put("status", "400");
				}
				
			} catch (Exception e) {
				map.put("status", "400");
			}
		}
		return JSON.toJSONString(map);
	}
	/**
	 * modify cpu and ram
	 * @param id
	 * @param cpus
	 * @param rams
	 * @return
	 */
	@RequestMapping("service/modifyCPU.do")
	@ResponseBody
	public String modifyCPU(long id,Double cpus,String rams){
		Map<String, Object> map = new HashMap<String,Object>();
		Service service = serviceDao.findOne(id);
		try {
			service.setCpuNum(cpus);
			service.setRam(rams);
			KubernetesAPIClientInterface client = kubernetesClientService.getClient();
			Map<String,String> app = new HashMap<String,String>();
			map.put("app", service.getServiceName());
			PodList podList = client.getLabelSelectorPods(app);
			if(podList!=null){
				List<Pod> pods = podList.getItems();
				if(!CollectionUtils.isEmpty(pods)){
					for(Pod pod:pods){
						List<com.bonc.epm.paas.kubernetes.model.Container> containers = new ArrayList<com.bonc.epm.paas.kubernetes.model.Container>();
						containers = pod.getSpec().getContainers();
						for(com.bonc.epm.paas.kubernetes.model.Container container:containers){
							setContainer(container, cpus, rams);
						}
					}
				}
			}
			serviceDao.save(service);
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}
	
	public com.bonc.epm.paas.kubernetes.model.Container  setContainer(com.bonc.epm.paas.kubernetes.model.Container container,Double cpus,String rams){
		ResourceRequirements requirements = new ResourceRequirements();
		requirements.getLimits();
		Map<String,Object> def = new HashMap<String,Object>();
		//float fcpu = cpu*1024;
		def.put("cpu", cpus);
		def.put("memory", rams+"Mi");
		Map<String,Object> limit = new HashMap<String,Object>();
		limit = kubernetesClientService.getlimit(limit);
		requirements.setRequests(def);
		requirements.setLimits(limit);
		container.setResources(requirements);
		
		return container;
	}
	
	/**
	 * delete container and services from dockerfile
	 * @param id
	 * @return
	 */
	@RequestMapping("service/delContainer.do")
	@ResponseBody
	public String delContainer(long id){
		Service service = serviceDao.findOne(id);
		String confName = service.getServiceName();
		String configName = CurrentUserUtils.getInstance().getUser().getUserName()+"-"+service.getServiceName();
		KubernetesAPIClientInterface client = kubernetesClientService.getClient();
		ReplicationController controller = client.getReplicationController(service.getServiceName());
		if(controller!=null){
			client.updateReplicationController(service.getServiceName(), 0);
			client.deleteReplicationController(service.getServiceName());
			client.deleteService(service.getServiceName());
			TemplateEngine.deleteConfig(confName, configName);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		serviceDao.delete(id);
		return JSON.toJSONString(map);
	}

}
