package com.bonc.epm.paas.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.entity.Service;


 
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
	
	@RequestMapping(value={"service"},method=RequestMethod.GET)
	public String index(){
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
		map.put("date", serviceList);
		return JSON.toJSONString(map);
	}
	

}
