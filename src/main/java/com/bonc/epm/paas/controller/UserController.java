package com.bonc.epm.paas.controller;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.entity.Container;
import com.bonc.epm.paas.entity.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String index(){

		return "user/user.jsp";
	}

	@RequestMapping(value={"/add"},method=RequestMethod.GET)
	public String create(){

		return "user/user_create.jsp";
	}

	/*@RequestMapping("user/constructUser.do")
	public String constructUser(){

		return "redirect:/user";
	}*/



}
