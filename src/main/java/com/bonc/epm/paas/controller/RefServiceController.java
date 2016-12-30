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
import com.bonc.epm.paas.constant.RefServiceConstant;
import com.bonc.epm.paas.dao.RefServiceDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.RefService;
import com.bonc.epm.paas.etcd.util.EtcdClientService;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.Endpoints;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;

import mousio.etcd4j.responses.EtcdKeyAction;
import mousio.etcd4j.responses.EtcdKeysResponse;

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
     * serviceDao
     */
    @Autowired
  private  ServiceDao serviceDao;
    /**
     * refSreviceDao
     */
    @Autowired
    private RefServiceDao refServiceDao;
    /**
     * UserDao
     */
    @Autowired
    private UserDao userDao;
    /**
     * KubernetesClientService接口
     */
    @Autowired
    private KubernetesClientService kubernetesClientService;
    
    /**
     * etcdAPI客户端
     */
    @Autowired
    private EtcdClientService etcdClientService;
    
    /**
     * Description: <br>
     * 跳转进入service-import.jsp页面
     * @param model 
     * @return String
     */
    @RequestMapping(value = { "service/import" })
    public String serviceImport(Model model) {
        String  namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        model.addAttribute("menu_flag", "service");
        model.addAttribute("li_flag", "import");
        model.addAttribute("namespace", namespace);
        return "service/service-import.jsp";
    }
    
    /**
     * 
     * Description: 外部服务列表
     * @return json
     * @see
     */
    @RequestMapping("refservice/list.do")
    @ResponseBody
    private String list(){
        Map<String,Object> map = new HashMap<String,Object>();
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        List<RefService> list = refServiceDao.findByCreateByOrViDomainOrderByCreateDateDesc(userId,1);
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
    * @return string json
    * @see
    */
    @SuppressWarnings("static-access")
    @RequestMapping(value="refservice/add.do", method = RequestMethod.POST)
    @ResponseBody
    private String add(String serName,String serAddress,String refAddress, int refPort, int viDomain,int importSerMode,String refSerDesc,String useProxy){
        Map<String, Object> map = new HashMap<String, Object>();
        
        RefService refService = fillRefServiceInfo(null, serName, serAddress, refAddress, refPort, viDomain, importSerMode,refSerDesc,useProxy);
        String nameSpace = "";
        if (RefServiceConstant.ALL_TENANT == refService.getViDomain()) {
            nameSpace = "default";
        } else {
            nameSpace = CurrentUserUtils.getInstance().getUser().getNamespace();
                }
        // etcd 和 service
        if (RefServiceConstant.ETCD_MODE == refService.getimprotSerMode()) {
            EtcdKeysResponse response = etcdClientService.putRecord(nameSpace, refService.getSerAddress(), refService.getRefAddress());
            if (null == response) {
                map.put("status", "500");
            }
            else {
                response.getAction();
                if (EtcdKeyAction.create.name().equals("create"))
                map.put("status", "200");
                refServiceDao.save(refService); 
            }
        } 
        else {
            createServiceMode(map, refService, nameSpace);
        }        
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
    @SuppressWarnings("static-access")
    @RequestMapping(value = "refservice/edit.do", method = RequestMethod.POST)
    @ResponseBody
    private String editRefService(Long id, String serName,String serAddress,String refAddress ,int refPort, int viDomain,int importSerMode,String refSerDesc,String useProxy){
        Map<String, Object> map = new HashMap<String,Object>();

        RefService refService = fillRefServiceInfo(id, serName, serAddress, refAddress, refPort, viDomain, importSerMode, refSerDesc, useProxy);
        String nameSpace = "";
        if (RefServiceConstant.ALL_TENANT == refService.getViDomain()) {
            nameSpace = "default";
        } else {
            nameSpace = CurrentUserUtils.getInstance().getUser().getNamespace();
        }
        
        // 首先判断服务名是否发生变化
        RefService localRefService = refServiceDao.findOne(id);
        if (null != localRefService && !localRefService.getSerAddress().trim().equals(serAddress)) {
            try {
                KubernetesAPIClientInterface client = kubernetesClientService.getClient(nameSpace);
                client.deleteService(localRefService.getSerAddress());
            }
            catch (KubernetesClientException e) {
                LOG.error("delete k8s Service error; namespace:-"+nameSpace+";serviceName:-"+ localRefService.getSerAddress());
            }
        }
        // etcd 和 service
        if (RefServiceConstant.ETCD_MODE == refService.getimprotSerMode()) {
            EtcdKeysResponse response = etcdClientService.putRecord(nameSpace, refService.getSerAddress(), refService.getRefAddress());
            if (null == response) {
                map.put("status", "500");
            }
            else {
                response.getAction();
                if (EtcdKeyAction.create.name().equals("create"))
                map.put("status", "200");
                refServiceDao.save(refService); 
            }
        } 
        else {
            createServiceMode(map, refService, nameSpace);
        }        
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
        Map<String,Object> map = new HashMap<String,Object>();
        String[] id = ids.split(",");
        for(int i=0 ;i<id.length;i++){
            removeRefService(Long.parseLong(id[i]));
        }
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
    
    /**
     * Description:
     * 删除引入的外部服务
     * @param string 
     * @see 
     */
    private void removeRefService(long id) {
        RefService localRefService = refServiceDao.findOne(id);
        if (null != localRefService) {
            String nameSpace = "";
            if (RefServiceConstant.ALL_TENANT == localRefService.getViDomain()) {
                nameSpace = "default";
            } else {
                nameSpace = userDao.findById(localRefService.getCreateBy()).getNamespace();
            }
            if (RefServiceConstant.ETCD_MODE == localRefService.getimprotSerMode()) {
                //TODO etcd
            }
            else {
                KubernetesAPIClientInterface client = kubernetesClientService.getClient(nameSpace);
                try {
                    Status status = client.deleteService(localRefService.getSerName());
                    if (200 == status.getCode() || 404 == status.getCode()) {
                        refServiceDao.delete(id);
                    }
                }
                catch (KubernetesClientException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }

    @RequestMapping(value = "refservice/checkName.do", method = RequestMethod.GET)
    @ResponseBody
    private String checkName(String un){
        Map<String, Object> map = new HashMap<String,Object>();
        long createBy = CurrentUserUtils.getInstance().getUser().getId();
        int refsize = refServiceDao.findByCreateByAndSerName(createBy, un).size();
        int serSize = serviceDao.findByNameOf(createBy, un).size();
        if(0<refsize | 0<serSize){
            map.put("status", "400");
        }else{
            map.put("status", "200");
        }
        return JSON.toJSONString(map);
    }
    

    /**
     * 
     * Description:
     * 创建Servie方式 引入外部服务
     * @param map Map
     * @param refService RefService
     * @param nameSpace String
     * @see
     */
    private void createServiceMode(Map<String, Object> map, RefService refService, String nameSpace) {
        KubernetesAPIClientInterface client = kubernetesClientService.getClient(nameSpace);
        com.bonc.epm.paas.kubernetes.model.Service k8sService = null;
        Endpoints k8sEndpoints = null;
        try {
            k8sService = client.getService(refService.getSerName());
        } 
        catch (KubernetesClientException e) {
            k8sService = null;
        }
        
        try {
            com.bonc.epm.paas.kubernetes.model.Service k8sServiceTmp = kubernetesClientService.generateRefService(refService.getSerName(),refService.getRefPort());
            if (k8sService == null) {
                k8sService = client.createService(k8sServiceTmp);
            } 
            else { 
                try {
                    client.deleteService(refService.getSerName());
                    k8sService = client.createService(k8sServiceTmp);
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                
            }
            
            Endpoints k8sEndpointsTmp = kubernetesClientService.generateEndpoints(refService.getSerName(),refService.getRefAddress(),refService.getRefPort(),refService.getUseProxy());
            if (null == k8sEndpoints) {
                k8sEndpoints = client.createEndpoints(k8sEndpointsTmp);
            } /*else {
                k8sEndpoints = client.updateEndpoints(refService.getSerAddress(), k8sEndpointsTmp);
            }*/
            if (k8sService == null || null == k8sEndpoints) {
                map.put("status", "500");
            }
            else {
                map.put("status", "200");
                List<ServicePort> servicePort = k8sService.getSpec().getPorts();
                if (!StringUtils.isEmpty(servicePort)) {
                    refService.setNodePort(servicePort.get(0).getNodePort());
                }
                refService.setSerAddress(k8sService.getSpec().getClusterIP());
                refServiceDao.save(refService);
            }
        }
        catch (KubernetesClientException e) {
            map.put("status", "500");
            map.put("msg", e.getStatus().getMessage());
            LOG.error("create service error:" + e.getStatus().getMessage());
        }
    }

    /**
     * 
     * Description:
     * new RefService
     * @param serName  String
     * @param serAddress String
     * @param refAddress String
     * @param viDomain int
     * @param importSerMode int
     * @param importSerMode2 
     * @return refService RefService
     * @see
     */
    private RefService fillRefServiceInfo(Long id, String serName, String serAddress, String refAddress,
                                                              int refPort,int viDomain, int importSerMode,String refSerDesc,String useProxy) {
        RefService refService = null;
        if (null == id || id < 0) {
            refService = new RefService(); 
        } else {
            refService = refServiceDao.findOne(id);
        }
        refService.setSerName(serName);
        refService.setRefAddress(refAddress);
        refService.setSerAddress(serAddress);
        refService.setRefPort(refPort);
        refService.setRefSerDesc(refSerDesc);
        refService.setViDomain(viDomain == RefServiceConstant.ALL_TENANT ? RefServiceConstant.ALL_TENANT : RefServiceConstant.SELF_TENANT);
        refService.setimprotSerMode(importSerMode == RefServiceConstant.SERVICE_MODE ? RefServiceConstant.SERVICE_MODE : RefServiceConstant.ETCD_MODE);
        refService.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
        refService.setCreateDate(new Date());
        refService.setUseProxy(useProxy);
        return refService;
    }
}
