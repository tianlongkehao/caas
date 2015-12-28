package com.bonc.epm.paas.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.dao.ContainerDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
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
	private ImageDao imageDao;
	
	@Autowired
	private ContainerDao containerDao;
	
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
		//List<Container> containerList = new ArrayList<Container>();
	    List<Service> serviceList = new ArrayList<Service>();
		for(Service service:serviceDao.findAll()){
			serviceList.add(service);
		}
		//log.debug("containerlist========"+containerList);
		
		//model.addAttribute("containerList",containerList);
		model.addAttribute("serviceList", serviceList);
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
        //List<Service> serviceList = serviceDao.findByContainerID(id);

		model.addAttribute("id", id);
        //model.addAttribute("container", container);
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
	public String create(String imageName, String imageVersion, Model model){

		String isDepoly = "";
		if(imageName != null){
			isDepoly = "deploy";
		}

		model.addAttribute("imageName", imageName);
		model.addAttribute("imageVersion", imageVersion);
		model.addAttribute("isDepoly",isDepoly);
		model.addAttribute("menu_flag", "service");

		return "service/service_create.jsp";
	}
	/**
	 * 展示镜像
	 * @return
	 */
	@RequestMapping(value={"service/images"},method=RequestMethod.GET)
	@ResponseBody
	public String imageList(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<Image> images = imageDao.findAll();
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
		//Container container = containerDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = modifyStatus(id, ServiceConstant.CONSTRUCTION_STATUS_RUNNING);
		if(flag){
			flag = DockerClientUtil.pullImage(service.getImgName(), service.getImgVersion());
			if(flag){
				DockerClientUtil.createContainer(service.getImgName(), service.getImgVersion(), service.getServiceName(), 8080, 10004);
				flag = DockerClientUtil.startContainer(service.getServiceName());
				if(flag){
					map.put("status", "200");
					
				}else{
					map.put("status", "500");
					
				}
			}
		}
		
		return JSON.toJSONString(map);
		
	}
	/**
	 * 修改container、service状态
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean modifyStatus(long id,Integer status){
		try {
			Container container = containerDao.findOne(id);
			container.setContainerStatus(status);
			containerDao.save(container);
			for(Service service:serviceDao.findByContainerID(id)){
				service.setStatus(status);	
				serviceDao.save(service);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
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
//		List<Service> serviceList = new ArrayList<Service>();
//		log.debug("Container--ID:"+container.getId()+"Container--Name:"+container.getContainerName());
//		for(Integer i=0;i<container.getServiceNum();i++){
//			Service service2 = new Service();
//			service2.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
//			service2.setCreateDate(new Date());
//			service2.setServiceName(container.getContainerName()+"-"+getRandomString(5));
//			service2.setContainerID(container.getId());
//			service2.setImgName(container.getImageName());
//			service2.setImgVersion(container.getImageVersion());
//			serviceList.add(service2);
//		}
//		serviceDao.save(serviceList);
		/*Map<String, String > app = new HashMap<String, String>();
		app.put("confName", "myserver");
		app.put("port", "30000");
		String path = TemplateEngine.class.getClassLoader().getResource("conf.tpl").getPath();
		String path1 = TemplateEngine.class.getClassLoader().getResource("").getPath();
		String template = TemplateEngine.readConf(path); 
		String datastring = TemplateEngine.replaceArgs(template, app);
		TemplateEngine.writeConf(path1+"user.conf", datastring, true);*/
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
		//Service service = serviceDao.findOne(id);
		Container container = containerDao.findOne(id);
		Map<String, Object> map = new HashMap<String,Object>();
		boolean flag = modifyStatus(id, ServiceConstant.CONSTRUCTION_STATUS_STOPPED);
		if(flag){
			flag = DockerClientUtil.stopContainer(container.getContainerName());
			if(flag){
				map.put("status", "200");
			}else{
				map.put("status", "500");
			}
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
		
//		if(service.getInstanceNum()<addservice)
//		{
//			for(int i=0;i<(addservice-container.getServiceNum());i++){
//				Service service = new Service();
//				service.setContainerID(id);
//				service.setCreateDate(new Date());
//				service.setImgName(container.getImageName());
//				service.setImgVersion(container.getImageVersion());
//				service.setServiceName(container.getContainerName()+"-"+getRandomString(5));
//				service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
//				serviceList.add(service);
//			}
//			try {
//				serviceDao.save(serviceList);
//				container.setServiceNum(addservice);
//				containerDao.save(container);
//				map.put("status", "200");
//			} catch (Exception e) {
//				map.put("status", "400");
//			}
//		}
//		if(container.getServiceNum()>addservice){
//			List<Service> serviceList1 = new ArrayList<Service>();
//			serviceList = serviceDao.findByContainerID(id);
//			serviceList1 = serviceDao.findByContainerID(id);
//			serviceList.remove(container.getServiceNum()-addservice);
//			try {
//				serviceDao.delete(serviceList1);
//				serviceDao.save(serviceList);
//				container.setServiceNum(addservice);
//				containerDao.save(container);
//				map.put("status", "200");
//			} catch (Exception e) {
//				map.put("status", "400");
//			}
//		}
		if(service.getInstanceNum()==addservice){
			map.put("status", "300");
		}else {
			
			try {
				service.setInstanceNum(addservice);
				serviceDao.save(service);
				map.put("status", "200");
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
			serviceDao.save(service);
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
		//Container container = containerDao.findOne(id);
		Map<String, Object> map = new HashMap<String,Object>();
		boolean flag = DockerClientUtil.removeContainer(service.getServiceName());
		if(flag){
			flag = containerDel(id);
			if(flag){
				map.put("status", "200");
			}else {
				map.put("status", "500");
			}			
		}
		return JSON.toJSONString(map);
	}
	/**
	 * delete container and services from database
	 * @param id
	 * @return
	 */
	private boolean containerDel(long id){
		try {
			
			serviceDao.delete(id);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		return true;
	}
	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length){  
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
          
        for(int i = 0 ; i < length; ++i){  
            int number = random.nextInt(62);//[0,62)  
              
            sb.append(str.charAt(number));  
        }  
        return sb.toString();  
    }  
	
	public String createConf(){
		
		return null;
	}
	

}
