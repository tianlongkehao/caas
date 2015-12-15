package com.bonc.epm.paas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	/**
	 * 跳转登录页面
	 * @author lance
	 * 2014-6-8下午6:49:40
	 * @return
	 */
	@RequestMapping(value={"login"},method=RequestMethod.GET)
	public String login(){
		return "login.jsp";
	}

    @RequestMapping(value={"signin"},method=RequestMethod.POST)
    public String signIn(@RequestParam String userName, @RequestParam String password){

        System.out.println("username;-----------" + userName);
        System.out.println("password;-----------" + password);

        return "workbench.jsp";
    }
	
	@RequestMapping(value={"index","/"},method=RequestMethod.GET)
	public String index(){
		return "index.jsp";
	}
	@RequestMapping(value={"workbench"},method=RequestMethod.GET)
	public String workbench(){
		return "workbench.jsp";
	}
	@RequestMapping(value={"menu"},method=RequestMethod.GET)
	public String menu(Model model,String flag){
		model.addAttribute("flag", flag);
		return "menu.jsp";
	}

}
