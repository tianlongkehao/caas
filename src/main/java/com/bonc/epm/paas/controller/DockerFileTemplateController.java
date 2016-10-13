package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.DockerFileTemplateDao;
import com.bonc.epm.paas.entity.DockerFileTemplate;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;

/**
 * 
 * 模板管理
 * @author zhoutao
 * @version 2016年9月12日
 * @see DockerFileTemplateController
 * @since
 */
@Controller
@RequestMapping(value = "/template")
public class DockerFileTemplateController {

    /**
     * TemplateController日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(DockerFileTemplateController.class);
    
    /**
     * dockerFile模板dao操作接口
     */
    @Autowired
    private DockerFileTemplateDao dockerFileTemplateDao;
	
	/**
	 * dockerFile模板管理
	 * 
	 * @param model 
	 * @return String
	 */
    @RequestMapping(value = "/dockerfile", method = RequestMethod.GET)
	public String dockerfileTemp(Model model) {
        User user = CurrentUserUtils.getInstance().getUser();
        List<DockerFileTemplate> dockerFileList= dockerFileTemplateDao.findByCreateBy(user.getId());
        LOG.debug("dockerFileList:===========" + dockerFileList);
        model.addAttribute("dockerFileList", dockerFileList);
        model.addAttribute("menu_flag", "template"); 
        return "template/dockerfile-temp.jsp";
    }
    
    /**
     * Description: <br>
     * 跳转进入dockerfile添加页面
     * @param model model
     * @return String
     * @see
     */
    @RequestMapping(value = "/dockerfile/add", method = RequestMethod.GET)
    public String dockerfileAdd(Model model) {
        User user = CurrentUserUtils.getInstance().getUser();
        List<DockerFileTemplate> dockerFileList= dockerFileTemplateDao.findByCreateBy(user.getId());
        LOG.debug("dockerFileList:===========" + dockerFileList);
        model.addAttribute("dockerFileList", dockerFileList);
        model.addAttribute("menu_flag", "template"); 
        return "template/dockerfile-add.jsp";
    }
    
    /**
     * Description: <br>
     * 进入dockerfile详细页面
     * @param index id
     * @param model modle
     * @return String
     */
    @RequestMapping(value = "/dockerfile/detail/{index}", method = RequestMethod.GET)
    public String dockerfileDetail(@PathVariable long index, Model model){
        DockerFileTemplate dockerFileTemp = dockerFileTemplateDao.findOne(index);
        model.addAttribute("dockerFileTemp", dockerFileTemp);
        model.addAttribute("menu_flag", "template"); 
        return "template/dockerfile-edit.jsp";
    }
    
    /**
     * Description: <br>
     * 进入dockerfile详细页面
     * @param index id
     * @param model modle
     * @return String
     */
    @RequestMapping(value = "/dockerfile/content", method = RequestMethod.GET)
    @ResponseBody
    public String getockerfile(long id, Model model){
        DockerFileTemplate dockerFileTemp = dockerFileTemplateDao.findOne(id);
        return JSON.toJSONString(dockerFileTemp).replace("&gt;", ">").replace("&lt;", "<").replace("&quot", " ");
    }
    
    /**
     * Description: <br>
     * 编辑修改dockerFile
     * @param dockerFileId id
     * @param dockerFile dockerfile命令
     * @return String
     */
    @RequestMapping(value = "/dockerfile/modify.do",method = RequestMethod.POST)
    @ResponseBody
    public String modifyDockerFile(long dockerFileId,String dockerFile){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            DockerFileTemplate dockerFileTemp = dockerFileTemplateDao.findOne(dockerFileId);
            dockerFileTemp.setDockerFile(dockerFile);
            dockerFileTemplateDao.save(dockerFileTemp);
            map.put("status", "200");
        }
        catch (Exception e) {
            map.put("status", "400");
            LOG.error("日志读取错误：" + e);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 删除dockerFile模板
     * @param dockerfileId 删除Id
     * @return String
     * @see
     */
    @RequestMapping(value = "/dockerfile/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public String deletedockerfile(long dockerfileId){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            dockerFileTemplateDao.delete(dockerfileId);
            map.put("status", "200");
        }
        catch (Exception e) {
            map.put("status", "400");
            LOG.error("日志读取错误：" + e);
        }
        return JSON.toJSONString(map);
    }
    
}
