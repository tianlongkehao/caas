package com.bonc.epm.paas.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;


@Controller
public class LogController{
    
    /**
     * FileController日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);

    @RequestMapping(value = "/logServices", method = RequestMethod.GET)
	public String logServices(Model model) {
		model.addAttribute("menu_flag", "log");
		model.addAttribute("li_flag", "logService");
		return "log/logService.jsp";
	}
    @RequestMapping(value = "/logCommon", method = RequestMethod.GET)
	public String logCommon(Model model) {
		model.addAttribute("menu_flag", "log");
		model.addAttribute("li_flag", "logCommon");
		return "log/logCommon.jsp";
	}
   
}
