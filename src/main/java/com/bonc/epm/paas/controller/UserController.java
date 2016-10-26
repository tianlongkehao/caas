package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.Resource;
import com.bonc.epm.paas.entity.Restriction;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeItem;
import com.bonc.epm.paas.kubernetes.model.LimitRangeSpec;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaSpec;
import com.bonc.epm.paas.kubernetes.model.Secret;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.EncryptUtils;

/**
 * 
 * 用户相关操作控制器
 * @author ke_wang
 * @version 2016年9月5日
 * @see UserController
 * @since 2016年9月5日
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    
    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    /**
     * UserDao
     */
    @Autowired
    private UserDao userDao;
    
    /**
     * StorageDao
     */
    @Autowired
	private StorageDao storageDao;
    
    /**
     * 服务数据层接口
     */
    @Autowired
    private ServiceDao serviceDao;
    
    /**
     * 调用服务controller
     */
    @Autowired
    private ServiceController serviceController;
    
    /**
     * KubernetesClientService
     */
    @Autowired
	private KubernetesClientService kubernetesClientService;

    /**
     * CEPH_KEY ${ceph.key} 
     */
    @Value("${ceph.key}")
    private String CEPH_KEY;
    
    /**
     * 内存和cpu的比例大小
     */
    @Value("${ratio.memtocpu}")
    private String RATIO_MEMTOCPU = "4";
    
    /**
     * Model
     */
    public Model model;

    /**
     * 
     * Description:
     * 展示所有用户信息
     * Q:租户、管理员无差别？
     * @param model Model
     * @return .jsp String
     * @see Model 
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {
        List<User> userList = userDao.checkUser(CurrentUserUtils.getInstance().getUser().getId());
        model.addAttribute("userList", userList);
        model.addAttribute("menu_flag", "user");
        return "user/user.jsp";
    }
    

    /**
     * 
     * Description:
     * @param model Model
     * @param id long
     * @return .jsp String
     * @see
     */
    @RequestMapping(value = "/manage/list/{id}", method = RequestMethod.GET)
	public String userIndex(Model model, @PathVariable long id) {
        List<User> userManageList = userDao.checkUser1manage34(id);
        model.addAttribute("userManageList", userManageList);
        model.addAttribute("menu_flag", "usermanage");
        return "user/user-management.jsp";
    }

    /**
     * 
     * Description:
     * 返回到user_create.jsp页面
     * @param model Model
     * @return .jsp String
     * @see
     */
    @RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String useradd(Model model) {
        model.addAttribute("menu_flag", "user");
        return "user/user_create.jsp";
    }

    /**
     * 
     * Description:
     * 返回user_manage_create.jsp
     * @param model Model
     * @param id long
     * @return .jsp String
     * @see
     */
    @RequestMapping(value = { "/manage/add/{id}" }, method = RequestMethod.GET)
	public String userCreate(Model model, @PathVariable long id) {
        model.addAttribute("menu_flag", "usermanage");
        return "user/user_manage_create.jsp";
    }
    
    /**
     * 
     * Description:
     * 创建新租户 
     * 以用户登陆帐号（用户名）为名称，创建Namespace
     * @param user User
     * @param resource Resource
     * @param restriction Restriction
     * @param model Model
     * @return .jsp String
     * @see Resource Restriction
     */
    @RequestMapping(value = { "/save.do" }, method = RequestMethod.POST)
    @ResponseBody
	public String userSave(User user, Resource resource, Restriction restriction, Model model) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            fillPartUserInfo(user, resource);
			// 以用户名(登陆帐号)为name，为client创建Namespace
            Namespace namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
	        // 以用户名(登陆帐号)为name，创建client
            KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
            
            if (!createNsAndSec(user, namespace, client)) {
                client.deleteNamespace(user.getNamespace());
                map.put("message", "创建namespace或者secret失败！");
                map.put("creatFlag", "400");
                return JSON.toJSONString(map);
            }

            if (!createQuota(user, resource, client)) {
                client.deleteNamespace(user.getNamespace());
                map.put("message", "创建quota失败");
                map.put("creatFlag", "400");
                return JSON.toJSONString(map);
            }

            if (!createCeph(user)) {
                client.deleteNamespace(user.getNamespace());
                client.deleteResourceQuota(user.getNamespace());
                map.put("message", "创建ceph失败");
                map.put("creatFlag", "400");
                return JSON.toJSONString(map);
            }

			// 为client创建资源限制
			// LimitRange limitRange = generateLimitRange(user.getNamespace(),
			// restriction);
			// limitRange = client.createLimitRange(limitRange);
			// System.out.println("limitRange:"+JSON.toJSONString(limitRange));
            
			// DB保存用户信息
            userDao.save(user);
            map.put("creatFlag", "200");
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("message", "创建失败");
            map.put("creatFlag", "400");
            return JSON.toJSONString(map);
        }

        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description:
     * 创建新用户
     * @param user User
     * @param model Model
     * @return .jsp String
     * @see
     */
    @RequestMapping(value = { "/savemanage.do" }, method = RequestMethod.POST)
	public String userManageSave(User user, Model model) {
        try {
            if (null == userDao.checkUsername1(user.getUserName())) {
				// DB保存用户信息
                user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
                user.setParent_id(CurrentUserUtils.getInstance().getUser().getId());
                user.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());
                userDao.save(user);
            }
            model.addAttribute("creatFlag", "200");
        } 
        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("creatFlag", "400");
        }
		
//        User userManger = userDao.findOne(CurrentUserUtils.getInstance().getUser().getId());
//        List<User> userManageList = userDao.checkUsermanage34(userManger.getUser_province());
        List<User> userManageList = userDao.checkUser1manage34(user.getParent_id());
        model.addAttribute("userManageList", userManageList);
        model.addAttribute("menu_flag", "usermanage");
        return "user/user-management.jsp";
    }

    /**
     * 
     * Description:
     * 更新租户信息 
     * @param user User
     * @param resource Resource
     * @param restriction Restriction
     * @param model Model
     * @return .jsp String
     * @see
     */
    @RequestMapping(value = { "/update.do" }, method = RequestMethod.POST)
	public String userUpdate(User user, Resource resource, Restriction restriction, Model model) {
        try {
            updateUserInfo(user, resource);
			// 以用户名(登陆帐号)为name，创建client
            KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
            Namespace namespace = client.getNamespace(user.getNamespace());
            if (namespace != null) {
            	try {
            		ResourceQuota quota = updateQuotaInfo(client, user.getNamespace(), resource);
            		client.updateResourceQuota(user.getNamespace(), quota);
            		
            		// LimitRange limit = updateLimitRange(client, user.getNamespace(),
            		// restriction);
            		// LimitRange updateLimitRange =
            		// client.updateLimitRange(user.getNamespace(), limit);
            		
            		userDao.save(user);
            		model.addAttribute("updateFlag", "200");
            	}
            	catch (Exception e) {
            		e.printStackTrace();
            		model.addAttribute("updateFlag", "400");
            	}
			} else {
				LOG.error("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
			}
        } 
        catch (KubernetesClientException e) {
            LOG.error("error message:-"+ e.getMessage());
        }

        List<User> userList = userDao.checkUser(CurrentUserUtils.getInstance().getUser().getId());
        model.addAttribute("userList", userList);
        model.addAttribute("menu_flag", "user");
        return "user/user.jsp";
    }

    /**
     * 
     * Description:
     * 租户修改自己创建的用户的信息
     * @param user ：用户信息
     * @param model ： model
     * @return .jsp String
     * @see
     */
    @RequestMapping(value = { "/update_management.do" }, method = RequestMethod.POST)
	public String userManageUpdate(User user,Model model){
        User userManage =  CurrentUserUtils.getInstance().getUser();
        try {
            if (StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(userDao.findById(user.getId()).getPassword());
            }
            else{
                user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
            }
            user.setNamespace(userManage.getNamespace());
            user.setParent_id(userManage.getId());
            userDao.save(user);
        } 
        catch (KubernetesClientException e) {
            LOG.error("error message :-"+ e.getMessage());
        }
        List<User> userManageList = userDao.checkUser1manage34(userManage.getId());
        model.addAttribute("userManageList", userManageList);
        model.addAttribute("menu_flag", "usermanage");
        return "user/user-management.jsp";
    }
	

    /**
     * 
     * Description:
     * 局部刷新，批量删除租户
     * @param ids String
     * @return JSON.toJSONString(map)
     * @see
     */
    @RequestMapping("/delMul.do")
	@ResponseBody
	public String userDelMul(String ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(ids)) {
                String[] idArr = ids.split(",");
                List<Long> idList = new ArrayList<Long>();
                for (int i = 0; i < idArr.length; i++) {
                    idList.add(Long.parseLong(idArr[i]));
                }
                
                List<String> namespaceList = new ArrayList<String>();
                deleteTenAndUser(idList, namespaceList);
                delNamespace(namespaceList);
            }
            map.put("status", "200");
        }
        catch (javax.ws.rs.ProcessingException e) {
            LOG.error("javax.ws.rs.ProcessingException :-" + e.getMessage());
            map.put("status", "400");
        } 
        catch (KubernetesClientException e) {
            LOG.error("KubernetesClientException :-" + e.getMessage());
            map.put("status", "400");
        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 批量删除用户
     * @param ids id
     * @return  String
     */
    @RequestMapping("/delUser.do")
    @ResponseBody
    public String userDel(String ids){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(ids)) {
                String[] idArr = ids.split(",");
                for (int i = 0; i < idArr.length; i++) {
                    delUserService(Long.parseLong(idArr[i]));
                    userDao.delete(Long.parseLong(idArr[i]));
                }
            }
            map.put("status", "200");
        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * Description: <br>
     * 删除用户时同步删除用户创建的服务
     * @param userId : 用户Id
     * @return 
     * @see
     */
    public void delUserService(long userId){
        try {
            List<Service> list = serviceDao.findByCreateBy(userId);
            String serviceIds = "";
            if (list.size() > 0) {
                for (Service service : list) {
                    serviceIds += service.getId() + ",";
                }
                serviceController.delServices(serviceIds);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Description:
     * 根据用户id查询用户信息
     * @param model Model
     * @param id long
     * @return  .jsp String
     */
    @RequestMapping(value = { "detail/{id}" }, method = RequestMethod.GET)
    public String detailById(Model model, @PathVariable long id) {
        User user = userDao.findOne(id);
        Resource resource = new Resource();
        Restriction restriction = new Restriction();
        try {
			// 以用户名(登陆帐号)为name，创建client
            KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
            Namespace ns = client.getNamespace(user.getNamespace());
            if (null != ns) {
                ResourceQuota quota = client.getResourceQuota(user.getNamespace());
                if (null != quota) {
                    Map<String, String> map = quota.getSpec().getHard();
                    String leftCpu = String.valueOf(kubernetesClientService.transCpu(map.get("cpu")) * Integer.valueOf(RATIO_MEMTOCPU));
                    resource.setCpu_account(leftCpu);// CPU数量
                    resource.setRam(map.get("memory").replace("G", "").replace("i", ""));// 内存
                    LOG.info("+++++++++++++" + map.get("cpu") + "------" + map.get("memory"));
                    /* resource.setImage_control(map.get("replicationcontrollers"));//副本控制器
                    resource.setPod_count(map.get("pods"));//POD数量
                    resource.setServer_count(map.get("services"));//服务 */
                }

/*                LimitRange lr = client.getLimitRange(user.getNamespace());
                
                 if (lr != null && lr.getSpec().getLimits().size() > 0) {
                 for (LimitRangeItem limit : lr.getSpec().getLimits()) {
                 String type = limit.getType();
                 Map<String, String> def = limit.getDefaultVal();
                 Map<String, String> max = limit.getMax();
                 Map<String, String> min = limit.getMin();
                
                 if (type.trim().equals("pod")) {
                 restriction.setPod_cpu_default(computeCpuOut(def));
                 restriction.setPod_memory_default(computeMemoryOut(def));
                 restriction.setPod_cpu_max(computeCpuOut(max));
                 restriction.setPod_memory_max(computeMemoryOut(max));
                 restriction.setPod_cpu_min(computeCpuOut(min));
                 restriction.setPod_memory_min(computeMemoryOut(min));
                 }
                 if (type.trim().equals("Container")) {
                 restriction.setContainer_cpu_default(computeCpuOut(def));
                 restriction.setContainer_memory_default(computeMemoryOut(def));
                 restriction.setContainer_cpu_max(computeCpuOut(max));
                 restriction.setContainer_memory_max(computeMemoryOut(max));
                 restriction.setContainer_cpu_min(computeCpuOut(min));
                 restriction.setContainer_memory_min(computeMemoryOut(min));
                 }
                 }
                 }*/
            } 
            else {
                LOG.info("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
            }
        }
        catch (KubernetesClientException e) {
            LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
        }
        model.addAttribute("restriction", restriction);
        model.addAttribute("resource", resource);
        model.addAttribute("user", user);
        model.addAttribute("menu_flag", "user");
        return "user/user_detail.jsp";
    }

    /**
     * 
     * Description: 
     * 管理员明细
     * @param model Model
     * @param id long
     * @return .jsp string
     * @see
     */
    @RequestMapping(value = { "manage/detail/{id}" }, method = RequestMethod.GET)
	public String manageDetail(Model model, @PathVariable long id) {
        this.model = model;
        User user = userDao.findOne(id);
        model.addAttribute("user", user);
        model.addAttribute("menu_flag", "usermanage");
        return "user/user-manage-detail.jsp";
    }

    /**
     * 
     * Description:
     * 通过筛选条件查询用户
     * @param search_company 
     * @param search_department 
     * @param search_autority 
     * @param search_userName 
     * @param search_province 
     * @param model 
     * @return .jsp String
     */
    @RequestMapping(value = { "/searchByCondition" }, method = RequestMethod.POST)
	public String searchByCondition(String search_company, String search_department, String search_autority,
	                                            String search_userName, String search_province, Model model) {
        String company = "";
        String department = "";
        String realName = "";
        String province = "";
        String[] arr = null;
        if (StringUtils.isNotBlank(search_company)) {
            company = search_company.trim();
        }
        if (StringUtils.isNotBlank(search_department)) {
            department = search_department.trim();
        }
        if (StringUtils.isNotBlank(search_province)) {
            province = search_province.trim();
        }
        if (StringUtils.isNotBlank(search_userName)) {
            realName = search_userName.trim();
        }
        if (StringUtils.isNotBlank(search_autority)) {
            String autority = search_autority.trim();
            arr = autority.substring(0, autority.length() - 1).split(",");
        }

        List<User> userList = new ArrayList<User>();
        Long parentId = CurrentUserUtils.getInstance().getUser().getId();
        if (null != arr && 1 == arr.length) {
            for (User user : userDao.findBy4(company, department, arr[0].trim(), realName, province,parentId)) {
                userList.add(user);
            }
        }
        else {
            for (User user : userDao.find12By3(company, department, realName, province, parentId)) {
                userList.add(user);
            }
        }
        model.addAttribute("userList", userList);
        model.addAttribute("menu_flag", "user");
        return "user/user.jsp";
    }

    /**
     * 
     * Description:
     * 租户搜查
     * @param search_company 
     * @param search_department 
     * @param search_autority 
     * @param search_userName  
     * @param search_province 
     * @param model 
     * @return .jsp string 
     * @see
     */
    @RequestMapping(value = { "/manage/searchByCondition/{id}" }, method = RequestMethod.POST)
    public String searchByCondition2(String search_company, String search_department, String search_autority,
                                                         String search_userName, String search_province, Model model) {
        String company = "";
        String department = "";
        String realName = "";
        String province = "";
        String[] arr = null;
        if (StringUtils.isNotBlank(search_company)) {
            company = search_company.trim();
        }
        if (StringUtils.isNotBlank(search_department)) {
            department = search_department.trim();
        }
        if (StringUtils.isNotBlank(search_userName)) {
            realName = search_userName.trim();
        }
        if (StringUtils.isNotBlank(search_province)) {
            province = search_province.trim();
        }
        if (StringUtils.isNotBlank(search_autority)) {
            String autority = search_autority.trim();
            arr = autority.substring(0, autority.length() - 1).split(",");
        }
        
        List<User> userManageList = new ArrayList<User>();
        Long parentId = CurrentUserUtils.getInstance().getUser().getId();
        if (null != arr && 1 == arr.length) {
            for (User user : userDao.findBy4(company, department, arr[0].trim(), realName, province,parentId)) {
                userManageList.add(user);
            }
        } 
        else if (null != arr && 1 != arr.length) {
            for (User user : userDao.find12By3(company, department, realName, province, parentId)) {
                userManageList.add(user);
            }
        } 
        else {
            for (User user : userDao.find34By3(company, department, realName, province, parentId)) {
                userManageList.add(user);
            }
        }
        model.addAttribute("userManageList", userManageList);
        model.addAttribute("menu_flag", "user");
        return "user/user-management.jsp";
    }

    /**
     * 
     * Description:
     * 查询用户名是存在
     * @param username String
     * @return jsonstring
     */
    @RequestMapping(value = { "/checkUsername/{username}" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkUsername(@PathVariable String username) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> names = userDao.checkUsername(username);
        if (CollectionUtils.isNotEmpty(names)) {
            map.put("status", "400");
        } 
        else {
            try {
                KubernetesAPIClientInterface client = kubernetesClientService.getClient(username);
                Namespace namespace = client.getNamespace(username);
                if (null != namespace) {
                    map.put("status", "300");
                }
                else {
                    map.put("status", "200");
                }
            } 
            catch (KubernetesClientException e) {
                LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
                map.put("status", "200");
            }
        }
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description:
     * @param user User
     * @return redirect .jsp
     */
    @RequestMapping("user/add.do")
	public String userAdd(User user) {
        user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
        userDao.save(user);
        LOG.debug("userName--id:" + user.getUserName());
        return "redirect:/user";
    }

    /**
     * 
     * Description:
     * 删除单条记录
     * @param model 
     * @param id 
     * @return redirect .jsp
     */
    @RequestMapping(value = { "user/del/{id}" }, method = RequestMethod.GET)
	public String userDel(Model model, @PathVariable long id) {
        userDao.delete(id);
        LOG.debug("del userid======:" + id);
        return "redirect:/user";
    }

    /**
     * 
     * Description:
     * 查询登陆用户的基本信息、资源信息
     * @param model 
     * @param id 
     * @return .jsp 
     * @see
     */
    @RequestMapping(value = { "/detail/{id}/a", "/detail/{id}/b" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
        try {
            User user = userDao.findOne(id);
			// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 namespace 资源详情
            KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
            Namespace ns = client.getNamespace(user.getNamespace());
            if (null != ns) {
                getUserResourceInfo(model, user, client);
            }
            else {
                LOG.info("用户 " + user.getUserName() + " 还没有定义服务！");
            }
            
            double usedstorage = 0;
            List<Storage> list = storageDao.findByCreateBy(CurrentUserUtils.getInstance().getUser().getId());
            for (Storage storage : list) {
                usedstorage = usedstorage + (double) storage.getStorageSize();
            }
            model.addAttribute("usedstorage",  usedstorage / 1024);
        }
        catch (KubernetesClientException e) {
            LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
            e.printStackTrace();
        }
        catch (Exception e) {
            LOG.error("error message:-" + e.getMessage());
            e.printStackTrace();
        }
        return "user/user-own.jsp";
    }

    /**
     * 
     * Description:
     * userModifyPsw
     * @param id 
     * @param password 
     * @param newpwd 
     * @return jsonString
     * @see
     */
    @RequestMapping("/userModifyPsw.do")
	@ResponseBody
	public String userModifyPsw(long id, String password, String newpwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userDao.findOne(id);
        if (user.getPassword().trim().equals(EncryptUtils.encryptMD5(password.trim()))) {
            user.setPassword(EncryptUtils.encryptMD5(newpwd));
            userDao.save(user);
            map.put("status", "200");
        }
        else {
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description:
     * 修改用户基本信息
     * @param id 
     * @param email 
     * @param company 
     * @param user_cellphone 
     * @param user_department 
     * @param user_employee_id 
     * @param user_phone 
     * @return jsonString 
     */
    @RequestMapping("/userModifyBasic.do")
	@ResponseBody
	public String userModifyBasic(long id, String email, String company, String user_cellphone, 
	                                              String user_department,String user_employee_id, String user_phone) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = updateUserInfo(id, email, company, user_cellphone, user_department,user_employee_id, user_phone);
        if (null != user) {
            map.put("status", "200");
        } 
        else {
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description:
     * @param model 
     * @param id 
     * @return .jsp String
     */
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String userList(Model model, @PathVariable long id) {
        User user = userDao.findOne(id);
        model.addAttribute("user", user);
        model.addAttribute("menu_flag", "user");
        return "user/user.jsp";
    }

    /**
     * 
     * Description:
     * userOwn
     * @param model  
     * @return .jsp String
     */
    @RequestMapping(value = { "/own" }, method = RequestMethod.GET)
	public String userOwn(Model model) {
        model.addAttribute("menu_flag", "userOwn");
        return "user/user-own.jsp";
    }

    /**
     * 
     * Description:
     * 获取更新后的 LimitRange
     * @param client 
     * @param namespace 
     * @param restriction 
     * @return limitRange LimitRange
     * @see LimitRange
     */
    @SuppressWarnings("unused")
    private LimitRange updateLimitRange(KubernetesAPIClientInterface client, String namespace,Restriction restriction) {
        LimitRange limitRange = client.getLimitRange(namespace);
        LimitRangeSpec spec = limitRange.getSpec();

        List<LimitRangeItem> limits = new ArrayList<LimitRangeItem>();
        LimitRangeItem podLimitRangeItem = new LimitRangeItem();
        LimitRangeItem containerLimitRangeItem = new LimitRangeItem();

        limits.add(podLimitRangeItem);
        limits.add(containerLimitRangeItem);
        spec.setLimits(limits);
        limitRange.setSpec(spec);
        return limitRange;
    }

	/**
	 * 获取更新后的 ResourceQuota
	 * @param client 
	 * @param namespace 
	 * @param resource 
	 * @return quota ResourceQuota
	 */
    private ResourceQuota updateQuotaInfo(KubernetesAPIClientInterface client, String namespace, Resource resource) {
        ResourceQuota quota = client.getResourceQuota(namespace);
        ResourceQuotaSpec spec = quota.getSpec();
        
        Map<String, String> hard = quota.getSpec().getHard();
        hard.put("memory", resource.getRam() + "G"); // 内存
        hard.put("cpu", Integer.valueOf(resource.getCpu_account())/Integer.valueOf(RATIO_MEMTOCPU) + "");// CPU数量
		// hard.put("pods", resource.getPod_count() + "");//POD数量
		// hard.put("services", resource.getServer_count() + "");//服务
		// hard.put("replicationcontrollers", resource.getImage_control() +
		// "");//副本控制器
		// hard.put("resourcequotas", "1");//资源配额数量
        spec.setHard(hard);
        quota.setSpec(spec);
        return quota;
    }
	
    /**
     * 
     * Description:
     * fillPartUserInfo
     * @param user 
     * @param resource  
     * @see Resource 
     */
    private void fillPartUserInfo(User user, Resource resource) {
        user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
        user.setVol_size(resource.getVol()); 
        user.setNamespace(user.getUserName());
        user.setParent_id(CurrentUserUtils.getInstance().getUser().getId());
    }

    /**
     * 
     * Description:
     * ceph中创建租户目录  
     * @param user 
     * @return boolean
     * @see
     */
    public boolean createCeph(User user) {
        try {
            CephController ceph = new CephController();
            ceph.connectCephFS();
            ceph.createNamespaceCephFS(user.getNamespace());
            return true;
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    /**
     * 
     * Description:
     * 为client创建资源配额
     * @param user User
     * @param resource Resource
     * @param client KubernetesAPIClientInterface
     * @return boolean 
     * @see KubernetesAPIClientInterface Resource
     */
    public boolean createQuota(User user, Resource resource, KubernetesAPIClientInterface client) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("memory", resource.getRam() + "G"); // 内存
            map.put("cpu", Integer.valueOf(resource.getCpu_account())/Integer.valueOf(RATIO_MEMTOCPU) + "");// CPU数量(个)
            map.put("persistentvolumeclaims", resource.getVol() + "");// 卷组数量
            //map.put("pods", resource.getPod_count() + "");//POD数量
            //map.put("services", resource.getServer_count() + "");//服务
            //map.put("replicationcontrollers", resource.getImage_control() +"");//副本控制器
            //map.put("resourcequotas", "1");//资源配额数量
            ResourceQuota quota = kubernetesClientService.generateSimpleResourceQuota(user.getNamespace(), map);
            quota = client.createResourceQuota(quota);
            if (quota != null) {
            	LOG.info("create quota:" + JSON.toJSONString(quota));
            	return true;
			} else {
				LOG.info("create quota failed: namespace=" + user.getNamespace() + "hard=" + map.toString());
				return false;
			}
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    /**
     * 
     * Description:
     * 创建命名空间的secret
     * @param user User
     * @param namespace Namespace
     * @param client 
     * @return boolean
     * @see
     */
    public boolean createNsAndSec(User user, Namespace namespace,KubernetesAPIClientInterface client) {
        try {
            namespace = client.createNamespace(namespace);
            if (namespace == null) {
				LOG.error("Create a new Namespace:namespace["+namespace+"]");
			}else {
				LOG.info("create namespace:" + JSON.toJSONString(namespace));
			}

            Secret secret = kubernetesClientService.generateSecret("ceph-secret", user.getNamespace(), CEPH_KEY);
            secret = client.createSecret(secret);
            LOG.info("create secret:" + JSON.toJSONString(secret));
            return true;
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }

    }
    
    /**
     * 
     * Description:
     * 更新用户信息
     * @param user User
     * @param resource Resource
     * @see
     */
    private void updateUserInfo(User user, Resource resource) {
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(userDao.findById(user.getId()).getPassword());
        }
        else{
            user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
        }
        //卷组更新功能
        user.setVol_size(resource.getVol());
        user.setParent_id(CurrentUserUtils.getInstance().getUser().getId());
        user.setNamespace(user.getUserName());
    }
    
    /**
     * 
     * Description:
     * deleteTenAndUser
     * @param idList List<Long>
     * @param namespaceList List<String>
     * @see
     */
    private void deleteTenAndUser(List<Long> idList, List<String> namespaceList) {
        List<User> users = new ArrayList<User>();
        for (User user : userDao.findAll(idList)) {
            users.add(user);
            namespaceList.add(user.getNamespace());
            // 如果删除的为租户
            if (UserConstant.AUTORITY_TENANT.equals(user.getUser_autority())) {
                Long userId = user.getId();
                // 取得用户
                for (User sonUser : userDao.getByParentId(userId)) {
                    users.add(sonUser);
                }
            }
        }
        userDao.delete(users);
    }
    
    /**
     * 
     * Description:
     * 删除租户命名空间
     * @param namespaceList 
     */
    private void delNamespace(List<String> namespaceList) {
        if (CollectionUtils.isNotEmpty(namespaceList)) {
            for (String namespace : namespaceList) {
                // 以用户名(登陆帐号)为name，创建client
                KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
//                client.getNamespace(namespace);
                
                if (null != client.getNamespace(namespace)) {
                    try {
                        Status status = client.deleteLimitRange(namespace);
                        if (!status.getStatus().equals("Success")) {
                        	LOG.error("delete LimitRange failed:namespace["+namespace+"]");
						}
                        status =  client.deleteResourceQuota(namespace);
                        if (!status.getStatus().equals("Success")) {
							LOG.error("delete ResourceQuota failed:namespace["+namespace+"]");
						}
                    } 
                    catch (javax.ws.rs.ProcessingException e) {
                        System.out.println(e.getMessage());
                    }
                    client.deleteNamespace(namespace);
                    //TODO 逻辑删除卷组信息
                }
            } 
        }
    }
    
    /**
     * 
     * Description:
     * computeMemoryOut
     * @param val Map<String, String>
     * @return memVal String
     * @see
     */
    private String computeMemoryOut(Map<String, String> val) {
        String memVal = val.get("memory");
        if (memVal.contains("Mi")) {
            Float a1 = Float.valueOf(memVal.replace("Mi", "")) / 1024;
            return a1.toString();
        } 
        else {
            return memVal.replace("Gi", "");
        }
    }
    
    /**
     * 
     * Description:
     * computeCpuOut
     * @param val Map<String, String> val
     * @return cpuVal String
     * @see
     */
    private String computeCpuOut(Map<String, String> val) {
        String cpuVal = val.get("cpu");
        if (cpuVal.contains("m")) {
            Float a1 = Float.valueOf(cpuVal.replace("m", "")) / 1000;
            return a1.toString();
        } 
        else {
            return cpuVal;
        }
    }
    
    /**
     * 
     * Description:
     * 获取用户的资源使用信息
     * @param model 
     * @param user 
     * @param client  
     * @see
     */
    private void getUserResourceInfo(Model model, User user, KubernetesAPIClientInterface client) {
        ResourceQuota quota = client.getResourceQuota(user.getNamespace());
        if (null != quota) {
            model.addAttribute("user", user);
            
            Map<String, String> hard = quota.getStatus().getHard();
            model.addAttribute("servCpuNum", kubernetesClientService.transCpu(hard.get("cpu")) * Integer.valueOf(RATIO_MEMTOCPU)); // cpu个数
            model.addAttribute("servMemoryNum", hard.get("memory").replace("i", "").replace("G", ""));// 内存个数
            model.addAttribute("servPodNum", hard.get("pods"));// pod个数
            model.addAttribute("servServiceNum", hard.get("services")); // 服务个数
            model.addAttribute("servControllerNum", hard.get("replicationcontrollers"));// 副本控制数
            
            Map<String, String> used = quota.getStatus().getUsed();
            ReplicationControllerList rcList = client.getAllReplicationControllers();
            PodList podList = client.getAllPods();                   
            model.addAttribute("usedCpuNum", Float.valueOf(this.computeCpuOut(used)) * Integer.valueOf(RATIO_MEMTOCPU)); // 已使用CPU个数
            model.addAttribute("usedMemoryNum", Float.valueOf(this.computeMemoryOut(used)));// 已使用内存
            model.addAttribute("usedPodNum", (null != podList) ? podList.size() : 0); // 已经使用的POD个数
            model.addAttribute("usedServiceNum", (null !=rcList) ? rcList.size() : 0);// 已经使用的服务个数
            // model.addAttribute("usedControllerNum", usedControllerNum);
        } else {
            LOG.info("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
        }
    }
    
    /**
     * 
     * Description:
     * 更新用户信息
     * @param id 
     * @param email 
     * @param company 
     * @param user_cellphone 
     * @param user_department 
     * @param user_employee_id 
     * @param user_phone 
     * @return user User
     */
    private User updateUserInfo(long id, String email, String company, String user_cellphone,
                                            String user_department, String user_employee_id,String user_phone) {
        User user = userDao.findById(id);
        user.setEmail(email);
        user.setCompany(company);
        user.setUser_department(user_department);
        user.setUser_employee_id(user_employee_id);
        user.setUser_cellphone(user_cellphone);
        user.setUser_phone(user_phone);
        user = userDao.save(user);
        return user;
    }
}
