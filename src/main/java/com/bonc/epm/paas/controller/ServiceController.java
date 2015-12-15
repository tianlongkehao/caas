package com.bonc.epm.paas.controller;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.ServiceDao;
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
	
	@RequestMapping(value={"service"},method=RequestMethod.GET)
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
	
	@RequestMapping(value={"service/add"},method=RequestMethod.GET)
	public String create(){

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
		service.setStatus(ServiceConstant.CONSTRUCTION_STATUS_PENDING);
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
		boolean flag = DockerClientUtil.pullImage(service.getImgName(), service.getImgVersion());
		if(flag){
			DockerClientUtil.createContainer(service.getImgName(),service.getImgVersion(), service.getServiceName(), 8080, 10004);
			flag = DockerClientUtil.startContainer(service.getServiceName());
			if(flag){
				map.put("status", "200");
				
			}else{
				map.put("status", "500");
				
			}
		}
		return JSON.toJSONString(map);
		
	}
	
	@RequestMapping("service/stopContainer.do")
	@ResponseBody
	public String stopContainer(long id){
		Service service = serviceDao.findOne(id);
		Map<String, Object> map = new HashMap<String,Object>();
		boolean flag = DockerClientUtil.stopContainer(service.getServiceName());
		if(flag){
			map.put("status", "200");
		}else{
			map.put("status", "500");
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
			map.put("status", "200");
		}else{
			map.put("status", "500");
		}
		return JSON.toJSONString(map);
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
