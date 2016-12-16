/*
 * 文件名：CredentialController.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月13日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.CiCodeCredentialDao;
import com.bonc.epm.paas.entity.CiCodeCredential;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.model.CredentialKey;
import com.bonc.epm.paas.shera.model.GitCredential;
import com.bonc.epm.paas.shera.util.SheraClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
public class CredentialController {
    
    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(CredentialController.class);
    
    /**
     * shera客户端接口
     */
    @Autowired
    private SheraClientService sheraClientService;
    
    /**
     * ciCodeCredential数据持久化接口
     */
    @Autowired
    private CiCodeCredentialDao ciCodeCredentialDao;
    
    /**
     * Description: <br>
     * 查询当前租户创建的密钥
     * @param model model
     * @return String
     */
    @RequestMapping(value = {"secret/Credential"},method = RequestMethod.GET)
    public String findCredential(Model model){
        User user = CurrentUserUtils.getInstance().getUser();
        List<CiCodeCredential> creList = ciCodeCredentialDao.findByCreateBy(user.getId());
        model.addAttribute("creList", creList);
        model.addAttribute("menu_flag", "usermanage");
        return "user/secretKey.jsp";
    }
    
    /**
     * Description: <br>
     * 添加代码构建认证方式
     * @param ciCodeCredential
     * @return 
     * @see
     */
    @RequestMapping(value = {"secret/addCredential.do"} , method = RequestMethod.GET)
    @ResponseBody
    public String saveCiCodeCredential(CiCodeCredential ciCodeCredential) {
        Map<String,Object> result = new HashMap<String, Object>();
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            GitCredential gitCredential;
            if (StringUtils.isEmpty(ciCodeCredential.getPassword())) {
                gitCredential = sheraClientService.generateGitCredential(ciCodeCredential.getPrivateKey(), ciCodeCredential.getUserName(), ciCodeCredential.getType());
            }
            else {
                String password = URLEncoder.encode(ciCodeCredential.getPassword(), "UTF-8");
                gitCredential = sheraClientService.generateGitCredential(password, ciCodeCredential.getUserName(), ciCodeCredential.getType());
            }
            CredentialKey credentialKey = client.addCredential(gitCredential);
            ciCodeCredential.setUniqueKey(credentialKey.getUuid());
            ciCodeCredential.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
            ciCodeCredential.setCreateDate(new Date());
            ciCodeCredentialDao.save(ciCodeCredential);
            result.put("status", "200");
            result.put("id",ciCodeCredential.getId());
        }
        catch (Exception e) {
            LOG.error("add credential error : " + e.getMessage());
            result.put("status", "400");
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * Description: <br>
     * 删除单个密钥
     * @param id id
     * @return String
     */
    @RequestMapping("secret/delCredential.do")
    @ResponseBody
    public String delCredential(long id){
        Map<String,Object> map = new HashMap<String,Object>();
        CiCodeCredential ciCodeCredential = ciCodeCredentialDao.findOne(id);
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            client.deleteCredential(ciCodeCredential.getUniqueKey());
            ciCodeCredentialDao.delete(ciCodeCredential);
            map.put("status", "200");
        }
        catch (Exception e) {
            LOG.error("delete credential error : " + e.getMessage());
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 批量删除credentials
     * @param credentials credentials
     * @return String
     */
    @RequestMapping("secret/delCredentials.do")
    @ResponseBody
    public String delSheras(String credentials){
        Map<String,Object> map = new HashMap<>();
        ArrayList<Long> ids = new ArrayList<Long>();
        String[] str = credentials.split(",");
        if (str != null && str.length > 0) {
            for (String id : str) {
                ids.add(Long.valueOf(id));
            }
        }
        try {
            for (long id : ids) {
                delCredential(id);
            }
            map.put("status", "200");
        } 
        catch (Exception e) {
            map.put("status", "400");
            LOG.error("delete cerdentials error : " + e.getMessage());
        }
        return JSON.toJSONString(map); 
    }
    /**
     * Description: <br>
     * 更新credential信息
     * @param ciCodeCredential 数据
     * @return String
     */
    @RequestMapping("secret/updateCredential.do")
    @ResponseBody
    public String updateCredential(CiCodeCredential ciCodeCredential) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            client.deleteCredential(ciCodeCredentialDao.findOne(ciCodeCredential.getId()).getUniqueKey());
            GitCredential gitCredential;
            if (StringUtils.isEmpty(ciCodeCredential.getPassword())) {
                gitCredential = sheraClientService.generateGitCredential(ciCodeCredential.getPrivateKey(), ciCodeCredential.getUserName(), ciCodeCredential.getType());
            }
            else {
                String password = URLEncoder.encode(ciCodeCredential.getPassword(), "UTF-8");
                gitCredential = sheraClientService.generateGitCredential(password, ciCodeCredential.getUserName(), ciCodeCredential.getType());
            }
            CredentialKey credentialKey = client.addCredential(gitCredential);
            ciCodeCredential.setUniqueKey(credentialKey.getUuid());
            ciCodeCredential.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
            ciCodeCredential.setCreateDate(new Date());
            ciCodeCredentialDao.save(ciCodeCredential);
            map.put("status", "200");
        }
        catch (Exception e) {
            LOG.error("update credential error : " + e.getMessage());
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 根据id查询credential详细信息
     * @param id id
     * @return String
     * @see
     */
    @RequestMapping("secret/detailCredential.do")
    @ResponseBody
    public String detailCredential(long id){
        Map<String,Object> map = new HashMap<>();
        CiCodeCredential ciCodeCredential = ciCodeCredentialDao.findOne(id);
        map.put("credential", ciCodeCredential);
        return JSON.toJSONString(map);
    }
}
