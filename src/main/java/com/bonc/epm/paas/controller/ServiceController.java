package com.bonc.epm.paas.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.reflect.MethodDelegate;
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
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientUtil;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.DockerClientUtil;
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
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
	    List<Service> serviceList = new ArrayList<Service>();
	    List<Container> containerList = new ArrayList<Container>();
	    if(client.getNamespace(currentUser.getUserName()) == null){
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
	    	    			for(Pod pod:pods){
	    	    				String podName = pod.getMetadata().getName();
	    	    				Container container = new Container();
	    	    				container.setContainerName(podName);
	    	    				container.setServiceid(service.getId());
	    	    				containerList.add(container);
	    	    			}
	    	    		}
	    	    	}
	    	    	serviceList.add(service);
	    		}
	    		getleftResource(model);
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
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
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
	    	    			for(Pod pod:pods){
	    	    				String podName = pod.getMetadata().getName();
	    	    				Container container = new Container();
	    	    				container.setContainerName(podName);
	    	    				container.setServiceid(service.getId());
	    	    				containerList.add(container);
	    	    			}
	    	    		}
	    	    	}
	    	    	serviceList.add(service);
	    		}
	    		getleftResource(model);
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
        KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
        List<Container> containerList = new ArrayList<Container>();
        Map<String,String> map = new HashMap<String,String>();
    	map.put("app", service.getServiceName());
    	PodList podList = client.getLabelSelectorPods(map);
    	if(podList!=null){
    		List<Pod> pods = podList.getItems();
    		if(!CollectionUtils.isEmpty(pods)){
    			for(Pod pod:pods){
    				String podName = pod.getMetadata().getName();
    				Container container = new Container();
	    			container.setContainerName(podName);
	    			container.setServiceid(service.getId());
	    			containerList.add(container);
    			}
    		}
    	}
    	
		model.addAttribute("id", id);
        model.addAttribute("containerList", containerList);
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
		
		try {
			getleftResource(model);
			model.addAttribute("imgID", imgID);
			model.addAttribute("imageName", imageName);
			model.addAttribute("imageVersion", imageVersion);
			model.addAttribute("isDepoly",isDepoly);
			model.addAttribute("menu_flag", "service");
		} catch (Exception e) {
			model.addAttribute("msg","请创建租户！");
			return "service/service.jsp";
		}
	    

		return "service/service_create.jsp";
	}
	
	public boolean getleftResource(Model model){
		Integer usedcpu = 0;
		Integer usedram = 0;
		Integer usedpod = 0;
		User currentUser = CurrentUserUtils.getInstance().getUser();
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
		try {
			ResourceQuota rq = client.getResourceQuota(currentUser.getUserName());
		    String cpus = rq.getSpec().getHard().get("cpu");
		    String rams = rq.getSpec().getHard().get("memory").replace("M", "");
		    String pods = rq.getSpec().getHard().get("pods");
		    for(Service service:serviceDao.findByCreateBy(currentUser.getId())){
		    	Integer cpu = service.getCpuNum();
		    	String ram = service.getRam();
		    	Integer pod = service.getInstanceNum();
		    	usedram = usedram + Integer.valueOf(ram);
		    	usedcpu = usedcpu + cpu;
		    	usedpod = usedpod + pod;
		    }
		    Integer leftcpu = Integer.valueOf(cpus) - usedcpu;
			Integer leftram = Integer.valueOf(rams) - usedram;
			Integer leftpod = Integer.valueOf(pods) - usedpod;
			model.addAttribute("leftpod",leftpod);
		    model.addAttribute("leftcpu",leftcpu);
		    model.addAttribute("leftram",leftram);
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
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
		//使用k8s管理服务
		String registryImgName = DockerClientUtil.generateRegistryImageName(service.getImgName(), service.getImgVersion());
		//如果没有则新增
		ReplicationController controller = client.getReplicationController(service.getServiceName());
		if(controller==null){
			controller = KubernetesClientUtil.generateSimpleReplicationController(service.getServiceName(),service.getInstanceNum(),registryImgName,8080);
			controller = client.createReplicationController(controller);
		}else{
			controller = client.updateReplicationController(service.getServiceName(), service.getInstanceNum());
		}
		com.bonc.epm.paas.kubernetes.model.Service k8sService = client.getService(service.getServiceName());
		if(k8sService==null){
			k8sService = KubernetesClientUtil.generateService(service.getServiceName(),80,8080,(int)service.getId()+KubernetesClientUtil.getK8sStartPort());
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
		app.put("port", String.valueOf(service.getId()+KubernetesClientUtil.getK8sStartPort()));
		TemplateEngine.generateConfig(app, CurrentUserUtils.getInstance().getUser().getUserName()+"-"+service.getServiceName());
		TemplateEngine.cmdReloadConfig();
		service.setServiceAddr(TemplateEngine.getConfUrl());
		service.setPortSet(String.valueOf(service.getId()+KubernetesClientUtil.getK8sStartPort()));
		serviceDao.save(service);
		log.debug("container--Name:"+service.getServiceName());
		return "redirect:/service";
	}
	/**
	 * containerName 判重
	 * @param containerName
	 * @return
	 */
	@RequestMapping(value={"service/containerName"},method=RequestMethod.GET)
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
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
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
				KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
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
	public String modifyCPU(long id,Integer cpus,String rams){
		Map<String, Object> map = new HashMap<String,Object>();
		Service service = serviceDao.findOne(id);
		service.setCpuNum(cpus);
		service.setRam(rams);
		try {
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
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
		KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
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
