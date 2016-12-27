package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.EnvTemplateDao;
import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
@RequestMapping(value = "/template")
public class TemplateController {

	@Autowired
	private EnvTemplateDao envTemplateDao;
	
	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public String envTemp(Model model) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		List<EnvTemplate> envTemplates = envTemplateDao.findByCreateBy(cUser.getId());
		List<EnvTemplate> envTemplates2 = new ArrayList<EnvTemplate>();
		for (int i = 0; i<envTemplates.size(); i++) {
			if (i == 0) {
				envTemplates2.add(envTemplates.get(i));
			}else if (!envTemplates.get(i).getTemplateName().equals(envTemplates.get(i-1).getTemplateName())) {
				envTemplates2.add(envTemplates.get(i));
			}
		}
		model.addAttribute("envTemplateList",envTemplates2);
		model.addAttribute("menu_flag", "template");
		model.addAttribute("li_flag", "env");
		return "template/env-temp.jsp";
	}
	
	@RequestMapping(value = "/env/add", method = RequestMethod.GET)
	public String envAdd(Model model) {
		model.addAttribute("menu_flag", "template"); 
		return "template/env-add.jsp";
	}
	
	/**
	 * 加载环境变量模板数据
	 * 
	 * @return String
	 * @see
	 */
	@RequestMapping("/loadEnvTemplate.do")
    @ResponseBody
	public String loadEnvTemplate(){
	    Map<String, Object> map = new HashMap<String, Object>();
        User cUser = CurrentUserUtils.getInstance().getUser();
        List<String> templateNames = envTemplateDao.findTemplateName(cUser.getId());
        map.put("data", templateNames);
        return JSON.toJSONString(map);
	}

	/**
	 * 保存环境变量模板
	 * 
	 * @param templateName
	 * @return
	 * @see
	 */
	@RequestMapping("/saveEnvTemplate.do")
	@ResponseBody
	public String saveEnvTemplate(String templateName, String envVariable) {
		Map<String, Object> map = new HashMap<String, Object>();
		User cUser = CurrentUserUtils.getInstance().getUser();

		for (EnvTemplate envTemplate : envTemplateDao.findByCreateBy(cUser.getId())) {
			if (envTemplate.getTemplateName().equals(templateName)) {
				map.put("status", "400"); // 模板名称重复
				return JSON.toJSONString(map);
			}
		}

		if (StringUtils.isNotEmpty(envVariable)) {
		    String[] envKeyAndValues = envVariable.split(";");
            for (String envKeyAndValue : envKeyAndValues ) {
                EnvTemplate envTemplate = new EnvTemplate();
                envTemplate.setCreateBy(cUser.getId());
                envTemplate.setEnvKey(envKeyAndValue.substring(0,envKeyAndValue.indexOf(",")));
                envTemplate.setEnvValue(envKeyAndValue.substring(envKeyAndValue.indexOf(",")+1));
                envTemplate.setCreateDate(new Date());
                envTemplate.setTemplateName(templateName);
                envTemplateDao.save(envTemplate);
            }
            map.put("status", "200");
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 查询用户的环境变量模板，导入到环境变量模板中
	 * 
	 * @return
	 */
	@RequestMapping("/importEnvTemplate.do")
	@ResponseBody
	public String findEnvTemplate(String templateName) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<EnvTemplate> envTemplates = envTemplateDao.findByCreateByAndTemplateName(cUser.getId(), templateName);
		map.put("data", envTemplates);
		return JSON.toJSONString(map);
	}

	/**
	 * 覆盖环境变量模板
	 * 
	 * @return
	 */
	@RequestMapping("/modifyEnvTemplate.do")
	@ResponseBody
	public String modifyEnvTemplate(String templateName, String envVariable) {
		delEnvTemplate(templateName);
		saveEnvTemplate(templateName, envVariable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 删除环境变量模板
	 * 
	 * @return
	 */
	@RequestMapping("/delEnvTemplate.do")
	@ResponseBody
	public String delEnvTemplate(String templateName) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<EnvTemplate> envTemplates = envTemplateDao.findByCreateByAndTemplateName(cUser.getId(), templateName);
		for (EnvTemplate envTemplate : envTemplates) {
			envTemplateDao.delete(envTemplate);
		} 
		map.put("status", "200");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 删除环境变量
	 * 
	 * @return
	 */
	@RequestMapping("/delEnvTemplates.do")
	@ResponseBody
	public String delEnvTemplates(String templateNames) {
		// 解析获取的id List
        String[] str = templateNames.split(",");
        if (str != null && str.length > 0) {
            for (String template : str) {
            	delEnvTemplate(template);
            }
        }
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("status", "200");
        return JSON.toJSONString(maps); 
	}
	/**
     * Description: <br>
     *  根据userId和templateName查找envTemplates，跳转进入环境变量模板详细页面
     * @param model 
     * @param templateName 环境变量模板名称
     * @return String
     */
	@RequestMapping(value = { "/env/detail/{templateName}" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable String templateName){
		User cUser = CurrentUserUtils.getInstance().getUser();
		List<EnvTemplate> envTemplates = envTemplateDao.findByCreateByAndTemplateName(cUser.getId(), templateName);
		model.addAttribute("envTemplateList",envTemplates);
		model.addAttribute("menu_flag", "template"); 
		return "template/env-edit.jsp";
	}
    

	
}
