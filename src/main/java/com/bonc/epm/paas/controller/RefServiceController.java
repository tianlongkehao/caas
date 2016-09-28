/*
 * 文件名：RefServiceController.java
 * 描述：
 * 修改人：YuanPeng
 * 修改时间：2016年9月12日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.RefServiceDao;
import com.bonc.epm.paas.entity.RefService;
import com.bonc.epm.paas.util.CurrentUserUtils;

/**
 * 引用外部服务 控制类
 * @author YuanpPeng
 * @version 2016年9月12日
 * @see RefServiceController
 * @since
 */

@Controller
public class RefServiceController {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(RefServiceController.class);
    
    /**
     * refSreviceDao
     */
    @Autowired
    private RefServiceDao refServiceDao;
    
    /**
     * 
     * Description: 外部服务列表
     * Implement: <br>
     * @return json
     * @see
     */
    @RequestMapping("refservice/list.do")
    @ResponseBody
    private String list(){
        List list = new ArrayList();
        Map map = new HashMap();
        String user = CurrentUserUtils.getInstance().getUser().getNamespace(); 
        list = refServiceDao.findByCreateByOrViDomain(user,1);
        map.put("data", list);
        return JSON.toJSONString(map);
    }
    
   /**
    * 
    * Description: 添加新的引用外部服务信息
    * @param serName 服务名
    * @param serAddress 服务地址 
    * @param refAddress 外部服务地址
    * @param viDomain 可见域
    * @return json
    * @see
    */
    @RequestMapping(value="refservice/add.do", method = RequestMethod.POST)
    @ResponseBody
    private String add(String serName,String serAddress,String refAddress ,int viDomain){
        Map map = new HashMap();
        String user = CurrentUserUtils.getInstance().getUser().getNamespace();
        RefService refService = new RefService();
        refService.setCreateBy(user);
        refService.setCreateDate(new Date());
        refService.setRefAddress(refAddress);
        refService.setSerAddress(serAddress);
        refService.setViDomain(viDomain);
        refService.setSerName(serName);
        refServiceDao.save(refService);
        map.put("status", 200);
        return JSON.toJSONString(map);
    }
    /**
     * 
     * Description: 修改引用外部服务的信息
     * @param id id
     * @param serName 服务名
     * @param serAddress 服务地址 
     * @param refAddress 外部服务地址
     * @param viDomain 可见域
     * @return json
     * @see
     */
    @RequestMapping(value = "refservice/edit.do", method = RequestMethod.POST)
    @ResponseBody
    private String editRefService(Long id, String serName,String serAddress,String refAddress ,int viDomain){
        Map map = new HashMap();
        RefService refService = refServiceDao.findOne(id);
        refService.setSerName(serName);
        refService.setRefAddress(refAddress);
        refService.setSerAddress(serAddress);
        refService.setViDomain(viDomain);
        refServiceDao.save(refService);
        map.put("status", 200);
        return JSON.toJSONString(map);
    }
    /**
     * 
     * Description: 删除一个或多个外部服务
     * @param ids 要删除的id
     * @return json
     * @see
     */
    @RequestMapping(value = "refservice/delete.do", method = RequestMethod.GET)
    @ResponseBody
    private String delete(String ids){
        Map map = new HashMap();
        String[] id = ids.split(",");
        for(int i=0 ;i<id.length;i++){
            refServiceDao.delete(Long.parseLong(id[i]));
        }
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
    @RequestMapping(value = "refservice/checkName.do", method = RequestMethod.GET)
    @ResponseBody
    private String checkName(String un){
        Map map = new HashMap();
        int size = refServiceDao.findBySerName(un).size();
        if(0<size){
            map.put("status", "400");
        }else{
            map.put("status", "200");
        }
        return JSON.toJSONString(map);
    }
}
