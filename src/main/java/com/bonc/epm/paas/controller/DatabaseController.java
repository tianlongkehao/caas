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
 * @see DatabaseController
 * @since
 */
@Controller
@RequestMapping(value = "/db")
public class DatabaseController {
	
	/**
	 * 帮助文档
	 * 
	 * @param model 添加返回页面的数据
	 * @return String
	 */
	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	public String indexRedis(Model model) {
        model.addAttribute("menu_flag", "database");
        model.addAttribute("li_flag", "redis");
        return "database/redis.jsp";
    }
	@RequestMapping(value = "/redis/create", method = RequestMethod.GET)
	public String redisCreate(Model model) {
        model.addAttribute("menu_flag", "database");
        model.addAttribute("li_flag", "redis");
        return "database/redis-create.jsp";
    }
}
