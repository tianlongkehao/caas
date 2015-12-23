package com.bonc.epm.paas.controller;

import com.bonc.epm.paas.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
	@RequestMapping(value="/cluster")
	public class ClusterController {
	private static final Logger log = LoggerFactory.getLogger(ClusterController.class);
	/*@Autowired
	public UserDao userDao;*/

	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String index(){

		return "cluster/cluster.jsp";
	}

/*	@RequestMapping(value={"/add"},method=RequestMethod.GET)
	public String create(){

		return "user/user_create.jsp";
	}*/





}
