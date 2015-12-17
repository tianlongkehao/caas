package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
	public String index(@PathVariable int index, Model model) {
		if(index == 0){
			List<Image> images = imageDao.findAll();
			model.addAttribute("images", images);
			model.addAttribute("menu_flag", "registry");
		}else if(index == 1){
			
		}else if(index == 2){
			
		}

		return "docker-registry/registry.jsp";
	}
	
	@RequestMapping(value = {"registry/0"},method = RequestMethod.POST)
	public String findByName(@RequestParam String imageName,Model model) {
		List<Image> images = imageDao.findByNameCondition("%"+imageName+"%");
		model.addAttribute("images", images);
		
		return "docker-registry/registry.jsp";
	}
	@RequestMapping(value = {"registry/detail/{id}"}, method = RequestMethod.GET)
	public String detail(@PathVariable long id, Model model) {
		System.out.println(id);
		Image image = imageDao.findById(id);
		model.addAttribute("image", image);
		System.out.println(image.toString());
		return "docker-registry/detail.jsp";
	}
	
	//for test
	@PostConstruct
	public void init(){
		Image img1 = new Image();
		img1.setName("bonc/tomcat-maven");
		img1.setRemark("配置Maven环境的Tomcat应用服务器");
		img1.setVersion("1.0");
		imageDao.save(img1);
		Image img2 = new Image();
		img2.setName("test/hw2");
		img2.setRemark("helloworld");
		img2.setVersion("latest");
		imageDao.save(img2);

		
		
		
		log.debug("init images bonc/tomcat-maven");
	}
}
