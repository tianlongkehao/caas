package com.bonc.epm.paas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/template")
public class TemplateController {
	
	/**
	 * 模板管理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dockerfile", method = RequestMethod.GET)
	public String dockerfileTemp(Model model) {
		model.addAttribute("menu_flag", "template"); 
		return "template/dockerfile-temp.jsp";
	}
	
	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public String envTemp(Model model) {
		model.addAttribute("menu_flag", "template"); 
		return "template/env-temp.jsp";
	}
	
	@RequestMapping(value = "/dockerfile/add", method = RequestMethod.GET)
	public String dockerfileAdd(Model model) {
		model.addAttribute("menu_flag", "template"); 
		return "template/dockerfile-add.jsp";
	}
	
	@RequestMapping(value = "/env/add", method = RequestMethod.GET)
	public String envAdd(Model model) {
		model.addAttribute("menu_flag", "template"); 
		return "template/env-add.jsp";
	}

	
}
