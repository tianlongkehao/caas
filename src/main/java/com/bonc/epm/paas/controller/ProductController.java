package com.bonc.epm.paas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/product")
public class ProductController {
	
	/**
	 * 帮助文档
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("menu_flag", "product");
		return "product/help.jsp";
	}

	
}
