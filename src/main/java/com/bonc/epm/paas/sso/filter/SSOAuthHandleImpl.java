package com.bonc.epm.paas.sso.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.SpringApplicationContext;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.controller.CephController;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.sso.casclient.CasClientConfigurationProperties;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.ServiceException;
import com.bonc.epm.paas.util.WebClientUtil;

/**
 *  单点登录后的模拟登录
 * @author song
 */
@Component
public class SSOAuthHandleImpl implements com.bonc.sso.client.IAuthHandle{
    /**
     * 输出日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(SSOAuthHandleImpl.class);
    /**
     * CONST_CAS_ASSERTION
     */
    private static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
    /**
     * AUTH_FAILURE_MSG_NO_PRIVILEGE
     */
    private static final String AUTH_FAILURE_NO_PRIVILEGE = "您没有权限登录本系统！";
    /**
     * AUTH_FAILURE_MSG_NO_RESOURCE
     */
    //private static final String AUTH_FAILURE_NO_RESOURCE = "您尚未分配部署资源，请联系管理员！";
    
    /**
     * configProps
     */
    @Autowired
    private CasClientConfigurationProperties configProps;

    /**
     * kubernetesClientService
     */
    @Autowired
    private KubernetesClientService kubernetesClientService;

    /**
     * userDao
     */
    @Autowired
    private UserDao userDao;
    /**
     * resManUrl
     */
    @Value("${resourceMmanage.address}")
    private String resManUrl;
    /**
     * 内存和cpu的比例大小
     */
    @Value("${ratio.memtocpu}")
    private String RATIO_MEMTOCPU = "4";
    
    @Override   
    public boolean onSuccess(HttpServletRequest request, HttpServletResponse response, String loginId) {
        SpringApplicationContext.CONTEXT.getAutowireCapableBeanFactory().autowireBean(this);
        if (configProps.getEnable()) {
            if ((null != request) && (null != loginId) && (loginId.trim().length() > 0)) {
                User currPaasUser = CurrentUserUtils.getInstance().getUser();
                if(currPaasUser!=null && loginId.equals(currPaasUser.getUserName())){ //如果已经登录和并且是一个用户
                    return true;
                }
                
                //HttpServletRequest httpRequest = (HttpServletRequest) request;
                // cas当前认证用户
                Assertion assertion = (Assertion) request.getSession().getAttribute(CONST_CAS_ASSERTION);
                Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
                LOG.info("能力平台USER:" + attributes.toString());
                LOG.info("cas登陆Id" + assertion.getPrincipal().getName() + "||" + loginId);
                
                String tenantId = "";
                String namespace = "";
                String tenantAdmin = "";
                if (null == attributes.get("tenantId")) {
                    attributes.put("tenantId", assertion.getPrincipal().getName());
                }
                if (null != attributes.get("tenantId")) {
                    tenantId = attributes.get("tenantId").toString();
                    namespace = tenantId;
                    //把下划线替换为--
                    if (namespace.contains("_")){
                        namespace = namespace.replace("_", "--");
                    }
                }
                if (attributes.get("tenantAdmin") != null) {
                    tenantAdmin = attributes.get("tenantAdmin").toString();
                }
                
                try {
                    // 同步统一平台租户用户到本地
                    User user = fillUserInfo(assertion, namespace);
                    user.setVol_size(200); // 目前先使用默认值
                    // 统一平台的userId
                    if (null != attributes.get("userId")) {
                         //是租户而且不是管理员
                        if ("1".equals(tenantAdmin) && 
                                ((!"1".equals(attributes.get("userId").toString().trim())) || (!"admin".equals(attributes.get("loginId").toString().trim())))) {
                            if (createNamespace(namespace)) { // 创建命名空间
                                createQuota(user,tenantId, namespace); // 创建资源
                                createCeph(namespace); // 创建ceph
                            }
                        }
                    }
                    userDao.save(user);
                    CurrentUserUtils.getInstance().setUser(user);
                    CurrentUserUtils.getInstance().setCasEnable(configProps.getEnable());
                    //request.getSession().setAttribute("cur_user", user);
                    //request.getSession().setAttribute("cas_enable", configProps.getEnable()); 
                    //request.getSession().setAttribute("ssoConfig", configProps);
                }
                catch (Exception e) {
                    LOG.error(e.getMessage());
                    throw new ServiceException();
                }
                return true;
            }
            return false;
        }
        return true;
    }
    
    /**
     * 
     * Description:
     * 创建ceph卷组空间
     * @param namespace 
     * @see
     */
    private void createCeph(String namespace) {
        try {
            CephController ceph = new CephController();
            ceph.connectCephFS();
            ceph.createNamespaceCephFS(namespace);
        }
        catch (Exception e) {
            LOG.error("create ceph error" +e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * Description: 
     * 保存用户
     * @param assertion Assertion
     * @param namespace String
     * @exception Exception 
     * @return user User
     * @see Assertion
     */
    private User fillUserInfo(Assertion assertion, String namespace) {
        String loginId = assertion.getPrincipal().getName();
        User user = userDao.findByUserName(loginId);
        if (null == user) {
            user = new User();
            user.setPassword(UserConstant.INIT_PASSWORD);
        }
        user.setNamespace(namespace);
        user.setUserName(loginId);
        Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
        if (null != attributes.get("userName")) { // 用户的名称
            user.setUser_realname(attributes.get("userName").toString());
        }
        if (null != attributes.get("userId")) { // 统一平台的userId
            user.setOpen_user_id(attributes.get("userId").toString());
        }
        if (null != attributes.get("email")) { // email
            user.setEmail(attributes.get("email").toString());
        }
        if (null != attributes.get("mobile")) { // 手机
            user.setUser_cellphone(attributes.get("mobile").toString());
        }
        if (null != attributes.get("telephone")) { // 电话
            user.setUser_phone(attributes.get("telephone").toString());
        }
        if (null != attributes.get("tenantAdmin")) {
            String tenantAdmin = attributes.get("tenantAdmin").toString();
            if("1".equals(tenantAdmin) && 
                        (("1".equals(attributes.get("userId").toString())) || 
                            ("admin".equals(loginId.trim())))){ //判断是否为超级管理员
                user.setUser_autority(UserConstant.AUTORITY_MANAGER);
            }
            else if ("1".equals(tenantAdmin)) { // 是租户
                user.setUser_autority(UserConstant.AUTORITY_TENANT);
            } 
            else if ("0".equals(tenantAdmin)) {
                user.setUser_autority(UserConstant.AUTORITY_USER);
            }
        }
        return user;
    }
    
    /**
     * 
     * Description:
     * 创建nameSpace
     * @param namespace 
     * @return boolean 
     * @throws ServiceException  
     */
    private boolean createNamespace(String namespace) throws ServiceException{
        // 以用户名(登陆帐号)为name，创建client ??
        KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
        // 是否创建nameSpace
        try {
            client.getNamespace(namespace);
        } 
        catch (KubernetesClientException e) {
            // 以用户名(登陆帐号)为name，为client创建nameSpace
            Namespace nSpace = kubernetesClientService.generateSimpleNamespace(namespace);
            nSpace = client.createNamespace(nSpace);
            if (nSpace == null) {
                client.deleteNamespace(namespace);
                LOG.error("Create a new Namespace:namespace["+nSpace+"]");
                return false;
            }
            else {
                LOG.info("create namespace:" + JSON.toJSONString(nSpace));
            }
        }
        catch (RuntimeException e) {
            LOG.error("连接kubernetesAPI超时！" + e);
            throw new ServiceException();
        }
        return true;
    }

    /**
     * 
     * Description:
     * 给租户创建资源
     * @param tenantId 
     * @param namespace  
     * @exception Exception 
     */
    private void createQuota(User user,String tenantId, String namespace) throws Exception{
        String openCpu = "0";
        String openMem = "0";
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("tenant_id","\""+tenantId+"\"");
            data.put("resource_type_code","\"Docker\"");
            params.put("param", data);
            String rtnStr = WebClientUtil.doGet(resManUrl, params);
            // 解析返回资源数据
            if (StringUtils.isNotBlank(rtnStr)) {
                JSONObject jsStr = JSONObject.parseObject(rtnStr);
                if ((boolean)jsStr.get("success")) {
                    JSONArray jsArrData = (JSONArray) jsStr.get("data");
                    JSONObject jsObj = (JSONObject) jsArrData.get(0);
                    JSONArray jsArrData2 = (JSONArray) jsObj.get("data");
                    JSONObject jsObj2 = (JSONObject) jsArrData2.get(0);
                    JSONArray jsPro = (JSONArray) jsObj2.get("property");
                    if (jsPro != null && jsPro.size() > 0) { // 获取CPU、MEM和Volume的值
                        for (Object oneRow : jsPro) {
                            JSONObject tmp = (JSONObject) oneRow;
                            if (((String)tmp.get("property_code")).trim().equals("CPU")) {
                                openCpu = (String) tmp.get("prop_value");
                            }
                            else if (((String)tmp.get("property_code")).trim().equals("memory")) {
                                openMem = (String) tmp.get("prop_value"); 
                            }
                            else if (((String)tmp.get("property_code")).trim().equals("Volume")){
                                user.setVol_size(Long.parseLong((String)tmp.get("prop_value")));
                            }
                        }
                    }
                    LOG.info("能力平台租户已分配资源:{" + "cpu:" + openCpu + ",mem:" + openMem +",volume:"+user.getVol_size()+"}");
                    createResourceQuota(namespace, openCpu, openMem, user.getVol_size());
                }
            }
        }
        catch (Exception e) {
            LOG.error("获取能力平台租户资源出错！" + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 
     * Description: 
     *  创建能力平台租户资源
     * @param namespace 
     * @param openCpu 
     * @param openMem  
     */
    private void createResourceQuota(String namespace, String openCpu, String openMem, Long volSize) {
        // 是否创建resourceQuota
        boolean isCreate = false;
        // KUBERNETES是否可以连接
        boolean isConnect = true;
        ResourceQuota currentQuota = null;
        
        // 以用户名(登陆帐号)为name，创建client
        KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
        try {
            currentQuota = client.getResourceQuota(namespace); // 获得resourceQuota
            LOG.info("k8s租户资源:" + currentQuota.toString());
        } 
        catch (KubernetesClientException e) {
            LOG.error("k8s环境中没有此租户的资源.错误信息：-" + e.getMessage());
            isCreate = true;
        } 
        catch (RuntimeException e) {
            LOG.error("连接kubernetesAPI超时！错误信息：-" + e.getMessage());
            isConnect = false;
        }
        // KUBERNETES不能连接
        if (isConnect) {
            // 为client创建资源配额
            Map<String, String> openMap = new HashMap<String, String>();
            openMap.put("memory", openMem + "G");// 内存（G）
            openMap.put("cpu", Double.valueOf(openCpu)/Double.valueOf(RATIO_MEMTOCPU) + "");// CPU数量(个)
            openMap.put("persistentvolumeclaims", volSize + "");// 卷组数量
            
            ResourceQuota openQuota = kubernetesClientService.generateSimpleResourceQuota(namespace, openMap);
            if (isCreate) { // 是否新建quota
                openQuota = client.createResourceQuota(openQuota); // 创建quota
                if (openQuota != null) {
                    LOG.info("create quota:" + JSON.toJSONString(openQuota));
                } 
                else {
                    LOG.info("create quota failed: namespace=" + namespace + "hard=" + openMap.toString());
                }
            } 
            else {
                Map<String, String> map = currentQuota.getSpec().getHard();
                Integer intCurCpu = Integer.parseInt(map.get("cpu"));
                Integer intCurMem = Integer.parseInt(map.get("memory").replace("Gi", "").replace("G", ""));
                Integer intOpenCpu = Integer.parseInt(openCpu);
                Integer intOpenMem = Integer.parseInt(openMem);
                // 判断资源是否变化
                if ((intOpenCpu != intCurCpu) || (intOpenMem != intCurMem)) {
                    client.updateResourceQuota(namespace, openQuota);
                }
            }
        }
    }
}
