package com.bonc.epm.paas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	public UserDao userDao;
	
	@RequestMapping("/login")
	@ResponseBody
	public String login(String loginName) {
		userDao.save(new User(loginName));
		User user = userDao.findByLoginName(loginName);
		log.debug("aaa:"+user);
		return "user:" + user.getId()+":"+user.getLoginName();
	}
	
	
	@RequestMapping(value={"/home"},method=RequestMethod.GET)
	public String home(){
		return "user/home.jsp";
	}

}
