package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.entity.Image;

/**
 * 镜像
 * @author shangvven
 *
 */
@Controller
public class RegistryController {
	private static final Logger log = LoggerFactory.getLogger(RegistryController.class);
	@Autowired
	private ImageDao imageDao;
	
	@RequestMapping(value = {"registry/{index}"}, method = RequestMethod.GET)
	public String index(@PathVariable int index) {
		return "docker-registry/registry.jsp";
	}
	
	@RequestMapping(value = {"registry/images"}, method = RequestMethod.GET)
	@ResponseBody
	public String imageList() {
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<Image> images = imageDao.findAll();
		map.put("data", images);
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value = {"registry/detail"}, method = RequestMethod.GET)
	public String detail() {
		
		return "docker-registry/detail.jsp";
	}
	
	//for test
	@RequestMapping(value = {"registry/create"}, method = RequestMethod.GET)
	@ResponseBody
	private String createImage(){
		Image img1 = new Image();
		img1.setName("Tomcat");
		img1.setRemark("Tomcat 应用服务器");
		imageDao.save(img1);
		
		Image img2 = new Image();
		img2.setName("MySQL");
		img2.setRemark("MySOL 数据库");
		imageDao.save(img2);
		
		return "success";
	}
}
