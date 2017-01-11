package com.bonc.epm.paas.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.DockerFileTemplateDao;
import com.bonc.epm.paas.dao.ServiceOperationLogDao;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.DockerFileTemplate;
import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.ServiceOperationLog;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.ResultPager;


@Controller
public class LogController{
    /**
     * service操作日志dao接口
     */
    @Autowired
    private ServiceOperationLogDao serviceOperationLogDao;
    
    /**
     * 通用操作日志dao接口
     */
    @Autowired
	private CommonOperationLogDao commonOperationLogDao;

    /**
     * FileController日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);
    
    /**
     * 跳转到服务操作日志列表页面
     * 
     * @param model
     * @return String
     */
    @RequestMapping(value = "logServices", method = RequestMethod.GET)
	public String logServices(Model model) {
		model.addAttribute("menu_flag", "log");
		model.addAttribute("li_flag", "logService");
		return "log/logService.jsp";
	}
    
    /**
     * Description: <br>
     * 使用datatable对服务操作记录进行服务端分页操作；
     * @param draw ：画板；
     * @param start ：开始记录数；
     * @param length : 每页的长度；
     * @param request ：接受搜索参数；
     * @return String
     */
    @RequestMapping(value = {"logService/pager.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String findByPagerServiceLog(String draw, int start,int length,
                                   HttpServletRequest request ){
        //获取搜索框内容
        String search = request.getParameter("search[value]");
        Map<String,Object> map = new HashMap<String, Object>();
        PageRequest pageRequest = null;
        Page<ServiceOperationLog> serviceList = null;
        //判断是第几页
        if (start == 0) {
            pageRequest = ResultPager.buildPageRequest(null, length);
        }else {
            pageRequest = ResultPager.buildPageRequest(start/length + 1, length);
        }
        //判断是否需要搜索服务日志
       if (StringUtils.isEmpty(search)) {
    	    serviceList=serviceOperationLogDao.findAlls(pageRequest);
        } else {
        	 serviceList = serviceOperationLogDao.findAllByCreateUserName("%"+search+"%", pageRequest);
        }
        map.put("draw", draw);
        map.put("recordsTotal", serviceList.getTotalElements());
        map.put("recordsFiltered", serviceList.getTotalElements());
        map.put("data", serviceList.getContent());
        
        return JSON.toJSONString(map);
    }
     
    /**
     * Description: <br>
     * 使用datatable对操作记录通用功能操作记录进行服务端分页操作；
     * @param draw ：画板；
     * @param start ：开始记录数；
     * @param length : 每页的长度；
     * @param request ：接受搜索参数；
     * @return String
     */
    @RequestMapping(value = {"logCommon/pager.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String findByPagerCommonLog(String draw, int start,int length,
                                   HttpServletRequest request ){
        //获取搜索框内容
        String search = request.getParameter("search[value]");
        Map<String,Object> map = new HashMap<String, Object>();
        PageRequest pageRequest = null;
        Page<CommonOperationLog> commonList = null;
        //判断是第几页
        if (start == 0) {
            pageRequest = ResultPager.buildPageRequest(null, length);
        }else {
            pageRequest = ResultPager.buildPageRequest(start/length + 1, length);
        }
        //判断是否需要搜索服务日志
       if (StringUtils.isEmpty(search)) {
    	   commonList=commonOperationLogDao.findAlls(pageRequest);
        } else {
        	commonList = commonOperationLogDao.findAllByCreateUsername("%"+search+"%", pageRequest);
        }
        map.put("draw", draw);
        map.put("recordsTotal", commonList.getTotalElements());
        map.put("recordsFiltered", commonList.getTotalElements());
        map.put("data", commonList.getContent());
        
        return JSON.toJSONString(map);
    }
    
    
    
    
    
    @RequestMapping(value = "/logCommon", method = RequestMethod.GET)
	public String logCommon(Model model) {
		model.addAttribute("menu_flag", "log");
		model.addAttribute("li_flag", "logCommon");
		return "log/logCommon.jsp";
	}
    
    
   
}
