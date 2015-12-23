package com.bonc.epm.paas.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.ServerCookie;
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
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.util.CmdUtil;
import com.bonc.epm.paas.util.DockerClientUtil;
import com.github.dockerjava.api.command.CreateContainerCmd;


 
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
	
	@RequestMapping(value={"services"},method=RequestMethod.GET)
	public String index(Model model){
        List<Service> serviceList = new ArrayList<Service>();
        for(Service service:serviceDao.findAll()){
            serviceList.add(service);
        }

        model.addAttribute("serviceList", serviceList);
        model.addAttribute("menu_flag", "service");

		return "service/service.jsp";
	}
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
	
	@RequestMapping(value={"service"},method=RequestMethod.GET)
	public String containerLists(Model model){
		List<Container> containerList = new ArrayList<Container>();
	    List<Service> serviceList = new ArrayList<Service>();
		for(Container container:containerDao.findAll()){
			for(Service service:serviceDao.findByContainerID(container.getId())){
				serviceList.add(service);
			}
			containerList.add(container);
		}
		log.debug("containerlist========"+containerList);
		
		model.addAttribute("containerList",containerList);
		model.addAttribute("serviceList", serviceList);
		model.addAttribute("menu_flag", "service");
		
		return "service/service.jsp";
//		Map<String, Object> map = new HashMap<String,Object>();
//		map.put("status","200");
//		map.put("data", serviceList);
//		map.put("container", containerList);
//		
//		return JSON.toJSONString(map);
		
	}
	
	@RequestMapping(value={"service/detail/{id}"},method=RequestMethod.GET)
	public String detail(Model model,@PathVariable long id){
        System.out.printf("id: " + id);
        Container container = containerDao.findOne(id);
        List<Service> serviceList = serviceDao.findByContainerID(id);

		model.addAttribute("id", id);
        model.addAttribute("container", container);
        model.addAttribute("serviceList", serviceList);
		return "service/service-detail.jsp";
	}
	
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
	
	@RequestMapping(value={"service/images"},method=RequestMethod.GET)
	@ResponseBody
	public String imageList(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<Image> images = imageDao.findAll();
		map.put("data", images);
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping("service/serviceCreate.do")
	@ResponseBody
	public String  serviceCreate(Service service){
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
		service.setCreateDate(new Date());
		serviceDao.save(service);
		log.debug("createService--id:"+service.getId()+"--servicename:"+service.getServiceName());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", service);
		return JSON.toJSONString(map);
	}
	
	@RequestMapping("service/createContainer.do")
	@ResponseBody
	public String CreateContainer(long id){
		Service service = serviceDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = modifyStatus(id, ServiceConstant.CONSTRUCTION_STATUS_RUNNING);
		if(flag){
			flag = DockerClientUtil.pullImage(service.getImgName(), service.getImgVersion());
			if(flag){
				DockerClientUtil.createContainer(service.getImgName(),service.getImgVersion(), service.getServiceName(), 8080, 10004);
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
	
	
	@RequestMapping("service/constructContainer.do")
	public String constructContainer(Container container,Service service){
		container.setContainerStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
		container.setCreateDate(new Date());
		container.setCreateTimestap(new Timestamp(System.currentTimeMillis()));
		containerDao.save(container);
		List<Service> serviceList = new ArrayList<Service>();
		log.debug("Container--ID:"+container.getId()+"Container--Name:"+container.getContainerName());
		if(container.getServiceNum()==1){
			service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
			service.setCreateDate(new Date());
			service.setServiceName(container.getContainerName());
			service.setContainerID(container.getId());
			service.setImgName(container.getImageName());
			service.setImgVersion(container.getImageVersion());
			serviceDao.save(service);
		}else{
			if(container.getServiceNum()>1){
				for(Integer i=0;i<container.getServiceNum();i++){
					Service service2 = new Service();
					service2.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
					service2.setCreateDate(new Date());
					service2.setServiceName(container.getContainerName());
					service2.setContainerID(container.getId());
					service2.setImgName(container.getImageName());
					service2.setImgVersion(container.getImageVersion());
					serviceList.add(service2);
				}
				serviceDao.save(serviceList);
			}
		}
		
		log.debug("service--Name:"+service.getServiceName());
		return "redirect:/service";
	}
	
	@RequestMapping(value={"service/containerName"},method=RequestMethod.GET)
	@ResponseBody
	public String containerName(String containerName){
		Map<String, Object> map = new HashMap<String, Object>();
		for(Container container:containerDao.findAll()){
			if(container.getContainerName().equals(containerName))
			{
				map.put("status", "400");
				break;
			}else{
				map.put("status", "200");
			}
			
		}
		return JSON.toJSONString(map);
	}
	
	@RequestMapping("service/stopContainer.do")
	@ResponseBody
	public String stopContainer(long id){
		Service service = serviceDao.findOne(id);
		Map<String, Object> map = new HashMap<String,Object>();
		boolean flag = modifyStatus(id, ServiceConstant.CONSTRUCTION_STATUS_STOPPED);
		if(flag){
			flag = DockerClientUtil.stopContainer(service.getServiceName());
			if(flag){
				map.put("status", "200");
			}else{
				map.put("status", "500");
			}
		}
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping("service/delContainer.do")
	@ResponseBody
	public String delContainer(long id){
		Service service = serviceDao.findOne(id);
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
	
	private boolean containerDel(long id){
		try {
			//Container container = containerDao.findOne(id);
			for(Service service:serviceDao.findByContainerID(id)){
				serviceDao.delete(service.getId());
			}
			containerDao.delete(id);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		return true;
	}
	
	@RequestMapping("service/serviceConstruct.do")
	@ResponseBody
	public String serviceConstruct(long id){
		Service service = serviceDao.findOne(id);
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_WAITING);
		service.setCreateDate(new Date());
		log.debug("createService--id:"+service.getId()+"--servicename:"+service.getServiceName());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", service);
		return JSON.toJSONString(map);
	}
	
	

}
