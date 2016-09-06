package com.bonc.epm.paas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * 帮助文档
 * @author zhoutao
 * @version 2016年9月6日
 * @see ProductController
 * @since
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController {
	
	/**
	 * 帮助文档
	 * 
	 * @param model 添加返回页面的数据
	 * @return String
	 */
    @RequestMapping(value = "/help", method = RequestMethod.GET)
	public String index(Model model) {
        model.addAttribute("menu_flag", "product");
        return "product/help.jsp";
    }
}
