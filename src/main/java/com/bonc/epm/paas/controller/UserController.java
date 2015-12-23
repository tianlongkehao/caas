package com.bonc.epm.paas.controller;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.soap.Detail;
import org.aspectj.apache.bcel.generic.RET;
import org.dom4j.util.UserDataElement;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import java.sql.Timestamp;
import java.util.*;

@Controller

	@RequestMapping(value="/user")
	public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	public UserDao userDao;

	@RequestMapping(value={"user"},method=RequestMethod.GET)
	public String index(Model model){
		List<User> userList = new ArrayList<User>();
		for(User user:userDao.findAll()){
			userList.add(user);
		}
		model.addAttribute("userList",userList);
		model.addAttribute("menu_flag", "user");
		return "user/user-management.jsp";
	}
	
	@RequestMapping("user/add.do")
	public String userAdd(User user){
		userDao.save(user);
		log.debug("userName--id:"+user.getUserName());
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("status", "200");
//		map.put("data", user);
//		return JSON.toJSONString(map);
		return "redirect:/user";
	}
	
	@RequestMapping(value={"user/del/{id}"},method=RequestMethod.GET)
	public String userDel(Model model,@PathVariable long id){
		userDao.delete(id);
		//Map<String,Object> map = new HashMap<String,Object>();
		log.debug("del userid======:"+id);
		return "redirect:/user";
	}
	
	@RequestMapping(value={"user/detail/{id}"},method=RequestMethod.GET)
	public String Detail(Model model,@PathVariable long id){
		System.out.printf("user--id:",id);
		User user = userDao.findOne(id);
		
		model.addAttribute("user", user);
		return "user/user.jsp";
		
	}
	
	@RequestMapping(value={"user/add"},method=RequestMethod.GET)
	public String useradd(Model model){
		return "user/user-add.jsp";
	}
	
	@RequestMapping("user/userModifyPsw.do")
	@ResponseBody
	public String userModifyPsw(long id,String password,String newPwd){
		User user = userDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if(user.getPassword().equals(password)){
			user.setPassword(newPwd);
			userDao.save(user);
			map.put("status", "200");
		}else{
			map.put("status", "400");
		}
		
		return JSON.toJSONString(map);
		
	}
	
	@RequestMapping("user/userModifyBasic.do")
	@ResponseBody
	public String userModifyBasic(long id,String email,String company){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userDao.findById(id);
		user.setEmail(email);
		user.setCompany(company);
		if(userDao.save(user) != null){
			map.put("status", "200");
		}else{
			map.put("status", "400");
		}
		
		return JSON.toJSONString(map);
	}
	//初始用户
	@PostConstruct
	public void init(){
		User user = new User();
		user.setId(1);
		user.setEmail("bonc@bonc.com.cn");
		user.setCompany("bonc");
		user.setUserName("admin");
		user.setPassword("admin");
		userDao.save(user);
	}

	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String userList(Model model){

		model.addAttribute("menu_flag", "user");
		return "user/user.jsp";
	}

	@RequestMapping(value={"/add"},method=RequestMethod.GET)
	public String create(){

		return "user/user_create.jsp";
	}

	@RequestMapping(value={"/own"},method=RequestMethod.GET)
	public String userOwn(Model model){
		model.addAttribute("menu_flag", "userOwn");
		return "user/user-own.jsp";
	}

	/*@RequestMapping("user/constructUser.do")
	public String constructUser(){

		return "redirect:/user";
	}*/



}
