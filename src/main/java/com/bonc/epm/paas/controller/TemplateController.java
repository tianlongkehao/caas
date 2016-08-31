package com.bonc.epm.paas.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bonc.epm.paas.dao.EnvTemplateDao;
import com.bonc.epm.paas.entity.EnvTemplate;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;


@Controller
public class TemplateController {

	@Autowired
	private EnvTemplateDao envTemplateDao;
	
	/**
	 * 加载环境变量模板数据
	 * 
	 * @return String
	 * @see
	 */
	@RequestMapping("service/loadEnvTemplate.do")
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
	@RequestMapping("service/saveEnvTemplate.do")
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
			JSONArray jsonArray = JSONArray.parseArray(envVariable);
			for (int i = 0; i < jsonArray.size(); i++) {
				EnvTemplate envTemplate = new EnvTemplate();
				envTemplate.setCreateBy(cUser.getId());
				envTemplate.setEnvKey(jsonArray.getJSONObject(i).getString("envKey").trim());
				envTemplate.setEnvValue(jsonArray.getJSONObject(i).getString("envValue").trim());
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
	@RequestMapping("service/importEnvTemplate.do")
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
	@RequestMapping("service/modifyEnvTemplate.do")
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
	@RequestMapping("service/delEnvTemplate.do")
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
	
}
