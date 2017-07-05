package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.internal.compiler.codegen.DoubleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.SheraConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.SheraDao;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.dao.UserAndSheraDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.dao.UserFavorDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.CommonOprationLogUtils;
import com.bonc.epm.paas.entity.Resource;
import com.bonc.epm.paas.entity.Restriction;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Shera;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserAndShera;
import com.bonc.epm.paas.entity.UserFavor;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
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
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.ExecConfig;
import com.bonc.epm.paas.shera.model.Jdk;
import com.bonc.epm.paas.shera.model.JdkList;
import com.bonc.epm.paas.shera.model.SonarConfig;
import com.bonc.epm.paas.shera.util.SheraClientService;
import com.bonc.epm.paas.util.ConvertUtil;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.EncryptUtils;

/**
 *
 * 用户相关操作控制器
 *
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
	 * UserFavorDao
	 */
	@Autowired
	private UserFavorDao userFavorDao;

	/**
	 * userResourceDao
	 */
	@Autowired
	private UserResourceDao userResourceDao;

	/**
	 * StorageDao
	 */
	@Autowired
	private StorageDao storageDao;

	/**
	 * sheraDao
	 */
	@Autowired
	private SheraDao sheraDao;

	/**
	 * UserAndSheraDao
	 */
	@Autowired
	private UserAndSheraDao userAndSheraDao;

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
	 * sheraClientService
	 */
	@Autowired
	private SheraClientService sheraClientService;

	/**
	 * commonOperationLogDao接口
	 */
	@Autowired
	private CommonOperationLogDao commonOperationLogDao;

	@Autowired
	private CephController ceph;

	/**
	 * CEPH_KEY ${ceph.key}
	 */
	@Value("${ceph.key}")
	private String CEPH_KEY;

	/*
	 * cpu系数
	 */
	@Value("${ratio.limittorequestcpu}")
	private int RATIO_LIMITTOREQUESTCPU;

	/*
	 * memory系数
	 */
	@Value("${ratio.limittorequestmemory}")
	private int RATIO_LIMITTOREQUESTMEMORY;

	/**
	 * Model
	 */
	public Model model;

	/*
	 * 预留的cpu资源
	 */
	@Value("${rest.resource.cpu}")
	private int REST_RESOURCE_CPU;

	/*
	 * 预留的memory资源
	 */
	@Value("${rest.resource.memory}")
	private int REST_RESOURCE_MEMORY;

	/**
	 *
	 * Description: 展示所有用户信息 Q:租户、管理员无差别？
	 *
	 * @param model
	 *            Model
	 * @return .jsp String
	 * @see Model
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {
		long id = CurrentUserUtils.getInstance().getUser().getId();
		List<User> userList;
		if (userDao.findOne(id).getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			userList = userDao.findAllTenant();
		} else {
			userList = userDao.checkUser(CurrentUserUtils.getInstance().getUser().getId());
		}
		model.addAttribute("userList", userList);
		model.addAttribute("menu_flag", "user");
		model.addAttribute("li_flag", "user");
		return "user/user.jsp";
	}

	/**
	 * s
	 *
	 * Description:
	 *
	 * @param model
	 *            Model
	 * @param id
	 *            long
	 * @return .jsp String
	 * @see
	 */
	@RequestMapping(value = "/manage/list/{id}", method = RequestMethod.GET)
	public String userIndex(Model model, @PathVariable long id) {
		List<User> userManageList = userDao.checkUser1manage34(id);
		model.addAttribute("userManageList", userManageList);
		model.addAttribute("menu_flag", "usermanage");
		model.addAttribute("li_flag", "manage");
		return "user/user-management.jsp";
	}

	/**
	 *
	 * Description: 返回到user_create.jsp页面
	 *
	 * @param model
	 *            Model
	 * @return .jsp String
	 * @see
	 */
	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String useradd(Model model) {
		Iterable<Shera> sheraList = sheraDao.findAll();
		model.addAttribute("sheraList", sheraList);
		model.addAttribute("menu_flag", "user");
		return "user/user_create.jsp";
	}

	/**
	 *
	 * Description: 返回user_manage_create.jsp
	 *
	 * @param model
	 *            Model
	 * @param id
	 *            long
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
	 * Description: 创建新租户 以用户登陆帐号（用户名）为名称，创建Namespace
	 *
	 * @param user
	 *            User
	 * @param resource
	 *            Resource
	 * @param restriction
	 *            Restriction
	 * @param model
	 *            Model
	 * @return .jsp String
	 * @see Resource Restriction
	 */
	@RequestMapping(value = { "/save.do" }, method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String userSave(User user, Resource resource, Restriction restriction, long sheraId, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserResource userResource = new UserResource();
		// 以用户名(登陆帐号)为name，创建client
		KubernetesAPIClientInterface client = null;
		try {
			// 添加租户资源信息
			userResource.setCpu(Double.valueOf(resource.getCpu_account()));
			userResource.setMemory(Long.parseLong(resource.getRam()));
			userResource.setVol_size(resource.getVol());
			// 创建租户时卷组剩余容量默认和卷组容量相等
			userResource.setVol_surplus_size(resource.getVol());

			userResource.setImage_count(resource.getImage_count());
			userResource.setCreateDate(new Date());

			fillPartUserInfo(user, resource);
			// 以用户名(登陆帐号)为name，为client创建Namespace
			Namespace namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
			client = kubernetesClientService.getClient(user.getNamespace());
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
			userResource.setUserId(user.getId());
			userResourceDao.save(userResource);
			if (sheraId != 0) {
				UserAndShera userAndShera = new UserAndShera();
				userAndShera.setSheraId(sheraId);
				userAndShera.setUserId(user.getId());
				userAndSheraDao.save(userAndShera);
			}
			map.put("creatFlag", "200");

			// 记录新增租户信息
			String extraInfo = "新增租户 ： " + user.getUserName() + "信息" + JSON.toJSONString(user) + "租户资源信息："
					+ JSON.toJSONString(userResource);
			CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
					CommConstant.TENANT_MANAGER, CommConstant.OPERATION_TYPE_CREATED);
			commonOperationLogDao.save(log);
		} catch (Exception e) {
			client.deleteNamespace(user.getNamespace());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			map.put("message", "创建失败");
			map.put("creatFlag", "400");
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 创建新用户
	 *
	 * @param user
	 *            User
	 * @param model
	 *            Model
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

				// 记录用户添加用户操作
				String extraInfo = "新增用户 ： " + user.getUserName() + "信息" + JSON.toJSONString(user);
				CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
						CommConstant.USER_MANAGER, CommConstant.OPERATION_TYPE_CREATED);
				commonOperationLogDao.save(log);
			}
			model.addAttribute("creatFlag", "200");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("creatFlag", "400");
		}

		// User userManger =
		// userDao.findOne(CurrentUserUtils.getInstance().getUser().getId());
		// List<User> userManageList =
		// userDao.checkUsermanage34(userManger.getUser_province());
		List<User> userManageList = userDao.checkUser1manage34(user.getParent_id());
		model.addAttribute("userManageList", userManageList);
		model.addAttribute("menu_flag", "usermanage");
		return "user/user-management.jsp";
	}

	/**
	 *
	 * Description: 更新用户偏好设置
	 *
	 * @param Integer
	 *            monitor
	 * @return .jsp String
	 * @see
	 */
	@RequestMapping(value = { "/userFavorUpdate.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String userFavorUpdate(Integer monitor) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();
		UserFavor userFavor = userFavorDao.findByUserId(user.getId());
		if (null == userFavor) {
			userFavor = new UserFavor();
			userFavor.setUserId(user.getId());
		}
		userFavor.setMonitor(monitor);
		userFavorDao.save(userFavor);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 更新用户偏好设置
	 *
	 * @param Integer
	 *            monitor
	 * @return .jsp String
	 * @see
	 */
	@RequestMapping(value = { "/updateSonarConfig.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String updateSonarConfig(SonarConfig sonarConfig) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		SheraAPIClientInterface client = null;
		try {
			client = sheraClientService.getClient();
		} catch (Exception e2) {
			map.put("status", "500");
			LOG.info(e2.getMessage());
			return JSON.toJSONString(map);
		}
		try {
			client.getSonarConfig();
		} catch (Exception e) {
			try {
				client.createSonarConfig(sonarConfig);
			} catch (Exception e1) {
				map.put("status", "400");
				e.printStackTrace();
			}
		}
		try {
			client.updateSonarConfig(sonarConfig);
		} catch (Exception e) {
			map.put("status", "400");
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 更新租户信息
	 *
	 * @param user
	 *            User
	 * @param resource
	 *            Resource
	 * @param restriction
	 *            Restriction
	 * @param model
	 *            Model
	 * @return .jsp String
	 * @see
	 */
	@RequestMapping(value = { "/update.do" }, method = RequestMethod.POST)
	@Transactional
	public String userUpdate(User user, Resource resource, Restriction restriction, long sheraId, Model model) {
		try {
			UserResource userResource = userResourceDao.findByUserId(user.getId());
			if (userResource == null) {
				userResource = new UserResource();
				userResource.setUserId(user.getId());
				userResource.setCreateDate(new Date());
			}

			// 设置存储卷余量
			userResource.setVol_surplus_size(
					userResource.getVol_surplus_size() + resource.getVol() - userResource.getVol_size());
			userResource.setCpu(Double.valueOf(resource.getCpu_account()));
			userResource.setMemory(new Double(resource.getRam()).longValue());
			userResource.setVol_size(resource.getVol());
			userResource.setImage_count(resource.getImage_count());
			userResource.setUpdateDate(new Date());

			updateUserInfo(user, resource);
			// 以用户名(登陆帐号)为name，创建client
			KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
			Namespace namespace = null;
			try {
				namespace = client.getNamespace(user.getNamespace());
			} catch (Exception e) {
				LOG.error("no namespace info!");
				namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
				namespace = client.createNamespace(namespace);
			}

			if (namespace != null) {
				try {
					ResourceQuota quota = updateQuotaInfo(client, user, resource);
					client.updateResourceQuota(user.getNamespace(), quota);

					// LimitRange limit = updateLimitRange(client,
					// user.getNamespace(),
					// restriction);
					// LimitRange updateLimitRange =
					// client.updateLimitRange(user.getNamespace(), limit);

					userDao.save(user);
					userResourceDao.save(userResource);
					model.addAttribute("updateFlag", "200");

					// 记录更新租户信息
					String extraInfo = "更新租户 " + user.getUserName() + "的信息" + JSON.toJSONString(user) + "租户资源信息："
							+ JSON.toJSONString(userResource);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
							CommConstant.TENANT_MANAGER, CommConstant.OPERATION_TYPE_UPDATE);
					commonOperationLogDao.save(log);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("updateFlag", "400");
				}
			} else {
				LOG.error("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
			}

			if (sheraId != 0) {
				userAndSheraUpdate(user.getId(), sheraId);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOG.error("error message:-" + e.getMessage());
		}

		List<User> userList = userDao.checkUser(CurrentUserUtils.getInstance().getUser().getId());
		model.addAttribute("userList", userList);
		model.addAttribute("menu_flag", "user");
		return "user/user.jsp";
	}

	/**
	 * Description: <br>
	 * userAndShera数据的更新
	 *
	 * @param userId
	 *            用户Id
	 * @param sheraId
	 *            sheraId
	 */
	public void userAndSheraUpdate(long userId, long sheraId) {
		UserAndShera userAndShera = userAndSheraDao.findByUserId(userId);
		if (org.springframework.util.StringUtils.isEmpty(userAndShera)) {
			userAndShera = new UserAndShera();
			userAndShera.setSheraId(sheraId);
			userAndShera.setUserId(userId);
		} else {
			userAndShera.setSheraId(sheraId);
		}
		userAndSheraDao.save(userAndShera);
	}

	/**
	 *
	 * Description: 租户修改自己创建的用户的信息
	 *
	 * @param user
	 *            ：用户信息
	 * @param model
	 *            ： model
	 * @return .jsp String
	 * @see
	 */
	@RequestMapping(value = { "/update_management.do" }, method = RequestMethod.POST)
	public String userManageUpdate(User user, Model model) {
		User userManage = CurrentUserUtils.getInstance().getUser();
		try {
			if (StringUtils.isEmpty(user.getPassword())) {
				user.setPassword(userDao.findById(user.getId()).getPassword());
			} else {
				user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
			}
			user.setNamespace(userManage.getNamespace());
			user.setParent_id(userManage.getId());

			// 获取修改前的user
			userDao.save(user);

			// 记录修改用户信息操作
			String extraInfo = "更新用户" + user.getUserName() + "的信息，" + JSON.toJSONString(user);
			CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
					CommConstant.USER_MANAGER, CommConstant.OPERATION_TYPE_UPDATE);
			commonOperationLogDao.save(log);
		} catch (KubernetesClientException e) {
			LOG.error("error message :-" + e.getMessage());
		}
		List<User> userManageList = userDao.checkUser1manage34(userManage.getId());
		model.addAttribute("userManageList", userManageList);
		model.addAttribute("menu_flag", "usermanage");
		return "user/user-management.jsp";
	}

	/**
	 *
	 * Description: 局部刷新，批量删除租户
	 *
	 * @param ids
	 *            String
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
				List<User> users = deleteTenAndUser(idList, namespaceList);
				delNamespace(namespaceList);
				userDao.delete(users);
			}
			map.put("status", "200");
		} catch (javax.ws.rs.ProcessingException e) {
			LOG.error("javax.ws.rs.ProcessingException :-" + e.getMessage());
			map.put("status", "400");
		} catch (KubernetesClientException e) {
			LOG.error("KubernetesClientException :-" + e.getMessage());
			map.put("status", "400");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 批量删除用户
	 *
	 * @param ids
	 *            id
	 * @return String
	 */
	@RequestMapping("/delUser.do")
	@ResponseBody
	public String userDel(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] idArr = ids.split(",");
				for (int i = 0; i < idArr.length; i++) {
					delUserService(Long.parseLong(idArr[i]));
					// 获取删除前的user
					User user = userDao.findById(Long.parseLong(idArr[i]));

					// 记录用户删除用户操作
					String extraInfo = "删除用户" + user.getUserName() + "的信息" + JSON.toJSONString(user);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
							CommConstant.USER_MANAGER, CommConstant.OPERATION_TYPE_DELETE);
					commonOperationLogDao.save(log);

					userDao.delete(user);
				}
			}
			map.put("status", "200");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 删除用户时同步删除用户创建的服务
	 *
	 * @param userId
	 *            : 用户Id
	 * @return
	 * @see
	 */
	public void delUserService(long userId) {
		try {
			List<Service> list = serviceDao.findByCreateBy(userId);
			String serviceIds = "";
			if (list.size() > 0) {
				for (Service service : list) {
					serviceIds += service.getId() + ",";
				}
				serviceController.delServices(serviceIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * Description: 根据用户id查询用户信息
	 *
	 * @param model
	 *            Model
	 * @param id
	 *            long
	 * @return .jsp String
	 */
	@RequestMapping(value = { "detail/{id}" }, method = RequestMethod.GET)
	public String detailById(Model model, @PathVariable long id) {
		User user = userDao.findOne(id);
		UserFavor userFavor = userFavorDao.findByUserId(id);
		UserResource userResource = userResourceDao.findByUserId(id);
		Resource resource = new Resource();
		Restriction restriction = new Restriction();
		try {
			// 以用户名(登陆帐号)为name，创建client
			KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
			Namespace ns = client.getNamespace(user.getNamespace());
			if (null != ns) {
				ResourceQuota quota = client.getResourceQuota(user.getNamespace());
				if (null != quota) {
					// 用户真实的资源*资源系数 = 页面显示资源
					Map<String, String> map = quota.getSpec().getHard();
					double totalCpu = ConvertUtil.convertCpu(map.get("cpu"))*RATIO_LIMITTOREQUESTCPU - REST_RESOURCE_CPU;
					double totalMem = ConvertUtil.convertMemory(map.get("memory"))*RATIO_LIMITTOREQUESTMEMORY - REST_RESOURCE_MEMORY;
					totalCpu = Math.floor(totalCpu);
					totalMem = Math.floor(totalMem);

					resource.setCpu_account(String.valueOf(totalCpu));// CPU数量
				    resource.setRam(String.valueOf(totalMem));// 内存

					LOG.info("+++++++++++++" + totalCpu + "------" + totalMem);
				}
			} else {
				LOG.info("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
			}
		} catch (KubernetesClientException e) {
			LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
		}
		Shera shera = sheraDao.findByUserId(id);
		Iterable<Shera> sheraList = sheraDao.findAll();
		model.addAttribute("userShera", shera);
		model.addAttribute("sheraList", sheraList);
		model.addAttribute("restriction", restriction);
		model.addAttribute("resource", resource);
		model.addAttribute("userResource", userResource);
		model.addAttribute("user", user);
		model.addAttribute("userFavor", userFavor);
		model.addAttribute("menu_flag", "user");
		return "user/user_detail.jsp";
	}

	/**
	 *
	 * Description: 管理员明细
	 *
	 * @param model
	 *            Model
	 * @param id
	 *            long
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
	 * Description: 通过筛选条件查询用户
	 *
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
			for (User user : userDao.findBy4(company, department, arr[0].trim(), realName, province, parentId)) {
				userList.add(user);
			}
		} else {
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
	 * Description: 租户搜查
	 *
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
			for (User user : userDao.findBy4(company, department, arr[0].trim(), realName, province, parentId)) {
				userManageList.add(user);
			}
		} else if (null != arr && 1 != arr.length) {
			for (User user : userDao.find12By3(company, department, realName, province, parentId)) {
				userManageList.add(user);
			}
		} else {
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
	 * Description: 查询用户名是存在
	 *
	 * @param username
	 *            String
	 * @return jsonstring
	 */
	@RequestMapping(value = { "/checkUsername/{username}" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkUsername(@PathVariable String username) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> names = userDao.checkUsername(username);
		if (CollectionUtils.isNotEmpty(names)) {
			map.put("status", "400");
		} else {
			try {
				KubernetesAPIClientInterface client = kubernetesClientService.getClient(username);
				Namespace namespace = client.getNamespace(username);
				if (null != namespace) {
					map.put("status", "300");
				} else {
					map.put("status", "200");
				}
			} catch (KubernetesClientException e) {
				LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
				map.put("status", "200");
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description:
	 *
	 * @param user
	 *            User
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
	 * Description: 删除单条记录
	 *
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
	 * Description: 查询登陆用户的基本信息、资源信息
	 *
	 * @param model
	 * @param id
	 * @return .jsp
	 * @see
	 */
	@RequestMapping(value = { "/detail/{id}/a", "/detail/{id}/b" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
		try {
			User user = userDao.findOne(id);
			UserFavor userFavor = userFavorDao.findByUserId(user.getId());
			// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 namespace 资源详情
			KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
			Namespace ns = client.getNamespace(user.getNamespace());
			if (null != ns) {
				getUserResourceInfo(model, user, client);
			} else {
				LOG.info("用户 " + user.getUserName() + " 还没有定义服务！");
			}

			double usedstorage = 0;
			List<Storage> list = storageDao.findByCreateBy(CurrentUserUtils.getInstance().getUser().getId());
			for (Storage storage : list) {
				usedstorage = usedstorage + (double) storage.getStorageSize();
			}
			Shera shera = sheraDao.findByUserId(id);

			// 获取sonarConfig
			SonarConfig sonarConfig = new SonarConfig();
			sonarConfig.setHidden(false);
			sonarConfig.setBreak(false);
			sonarConfig.setThreshold(6);
			try {
				SheraAPIClientInterface sheraClient = sheraClientService.getClient();
				sonarConfig = sheraClient.getSonarConfig();
			} catch (Exception e) {
				LOG.info(e.getMessage());
			}
			model.addAttribute("userFavor", userFavor);
			model.addAttribute("userShera", shera);
			model.addAttribute("usedstorage", usedstorage / 1024);
			model.addAttribute("sonarConfig", sonarConfig);
		} catch (KubernetesClientException e) {
			LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("error message:-" + e.getMessage());
			e.printStackTrace();
		}
		return "user/user-own.jsp";
	}

	/**
	 *
	 * Description: userModifyPsw
	 *
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
		User user1 = userDao.findOne(id);
		if (user.getPassword().trim().equals(EncryptUtils.encryptMD5(password.trim()))) {
			user.setPassword(EncryptUtils.encryptMD5(newpwd));
			userDao.save(user);
			// 记录用户修改密码操作
			String extraInfo = "修改用户 ： " + user.getUserName() + "密码：" + JSON.toJSONString(user);
			CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
					CommConstant.TENANT_MANAGER, CommConstant.OPERATION_TYPE_UPDATE);
			commonOperationLogDao.save(log);

			// 退出当前用户
			CurrentUserUtils.getInstance().loginoutUser(user1);
			map.put("status", "200");
		} else {
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 修改用户基本信息
	 *
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
	public String userModifyBasic(long id, String email, String company, String user_cellphone, String user_department,
			String user_employee_id, String user_phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = updateUserInfo(id, email, company, user_cellphone, user_department, user_employee_id, user_phone);
		if (null != user) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description:
	 *
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
	// 2016年12月30日 17:42:02 判定为无效方法
	// /**
	// *
	// * Description:
	// * userOwn
	// * @param model
	// * @return .jsp String
	// */
	// @RequestMapping(value = { "/own" }, method = RequestMethod.GET)
	// public String userOwn(Model model) {
	// model.addAttribute("menu_flag", "userOwn");
	// return "user/user-own.jsp";
	// }

	/**
	 *
	 * Description: 获取更新后的 LimitRange
	 *
	 * @param client
	 * @param namespace
	 * @param restriction
	 * @return limitRange LimitRange
	 * @see LimitRange
	 */
	@SuppressWarnings("unused")
	private LimitRange updateLimitRange(KubernetesAPIClientInterface client, String namespace,
			Restriction restriction) {
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
	 *
	 * @param client
	 * @param namespace
	 * @param resource
	 * @return quota ResourceQuota
	 */
	private ResourceQuota updateQuotaInfo(KubernetesAPIClientInterface client, User user, Resource resource) {
		ResourceQuota quota = null;
		try {
			quota = client.getResourceQuota(user.getNamespace());
		} catch (Exception e) {
			createQuota(user, resource, client);
		}
		quota = client.getResourceQuota(user.getNamespace());
		ResourceQuotaSpec spec = quota.getSpec();

		Map<String, String> hard = quota.getSpec().getHard();
		// 资源按照系数调整
		hard.put("memory",
				(Double.parseDouble(resource.getRam()) + REST_RESOURCE_MEMORY) / RATIO_LIMITTOREQUESTMEMORY + "G"); // 内存
		hard.put("cpu",
				(Double.parseDouble(resource.getCpu_account()) + REST_RESOURCE_CPU) / RATIO_LIMITTOREQUESTCPU + "");// CPU数量
		hard.put("persistentvolumeclaims", resource.getVol() + "");// 卷组数量
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
	 * Description: fillPartUserInfo
	 *
	 * @param user
	 * @param resource
	 * @see Resource
	 */
	private void fillPartUserInfo(User user, Resource resource) {
		user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
		// user.setVol_size(resource.getVol());
		// user.setImage_count(resource.getImage_count());
		user.setNamespace(user.getUserName());
		user.setParent_id(CurrentUserUtils.getInstance().getUser().getId());
	}

	/**
	 *
	 * Description: ceph中创建租户目录
	 *
	 * @param user
	 * @return boolean
	 * @see
	 */
	public boolean createCeph(User user) {
		try {
			// CephController ceph = new CephController();
			ceph.connectCephFS();
			ceph.createNamespaceCephFS(user.getNamespace());
			return true;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
	}

	/**
	 *
	 * Description: 为client创建资源配额
	 *
	 * @param user
	 *            User
	 * @param resource
	 *            Resource
	 * @param client
	 *            KubernetesAPIClientInterface
	 * @return boolean
	 * @see KubernetesAPIClientInterface Resource
	 */
	public boolean createQuota(User user, Resource resource, KubernetesAPIClientInterface client) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			// map.put("memory", resource.getRam() + "G"); // 内存
			// map.put("cpu", Double.valueOf(resource.getCpu_account()) + "");//
			// CPU数量(个)
			// 实际分配资源=页面分配资源/分配系数
			map.put("memory",
					(Double.parseDouble(resource.getRam()) + REST_RESOURCE_MEMORY) / RATIO_LIMITTOREQUESTMEMORY + "G"); // 内存
			map.put("cpu",
					(Double.parseDouble(resource.getCpu_account()) + REST_RESOURCE_CPU) / RATIO_LIMITTOREQUESTCPU + "");// CPU数量(个)
			map.put("persistentvolumeclaims", resource.getVol() + "");// 卷组数量
			// map.put("pods", resource.getPod_count() + "");//POD数量
			// map.put("services", resource.getServer_count() + "");//服务
			// map.put("replicationcontrollers", resource.getImage_control()
			// +"");//副本控制器
			// map.put("resourcequotas", "1");//资源配额数量
			ResourceQuota quota = kubernetesClientService.generateSimpleResourceQuota(user.getNamespace(), map);
			quota = client.createResourceQuota(quota);
			if (quota != null) {
				LOG.info("create quota:" + JSON.toJSONString(quota));
				return true;
			} else {
				LOG.info("create quota failed: namespace=" + user.getNamespace() + "hard=" + map.toString());
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
	}

	/**
	 *
	 * Description: 创建命名空间的secret
	 *
	 * @param user
	 *            User
	 * @param namespace
	 *            Namespace
	 * @param client
	 * @return boolean
	 * @see
	 */
	public boolean createNsAndSec(User user, Namespace namespace, KubernetesAPIClientInterface client) {
		try {
			namespace = client.createNamespace(namespace);
			if (namespace == null) {
				LOG.error("Create a new Namespace:namespace[" + namespace + "]");
			} else {
				LOG.info("create namespace:" + JSON.toJSONString(namespace));
			}

			Secret secret = kubernetesClientService.generateSecret("ceph-secret", user.getNamespace(), CEPH_KEY);
			secret = client.createSecret(secret);
			LOG.info("create secret:" + JSON.toJSONString(secret));
			return true;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}

	}

	/**
	 *
	 * Description: 更新用户信息
	 *
	 * @param user
	 *            User
	 * @param resource
	 *            Resource
	 * @see
	 */
	private void updateUserInfo(User user, Resource resource) {
		if (StringUtils.isEmpty(user.getPassword())) {
			user.setPassword(userDao.findById(user.getId()).getPassword());
		} else {
			user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
		}
		// 卷组更新功能
		// user.setVol_size(resource.getVol());
		// user.setImage_count(resource.getImage_count());
		user.setParent_id(CurrentUserUtils.getInstance().getUser().getId());
		user.setNamespace(user.getUserName());
	}

	/**
	 *
	 * Description: deleteTenAndUser
	 *
	 * @param idList
	 *            List<Long>
	 * @param namespaceList
	 *            List<String>
	 * @see
	 */
	private List<User> deleteTenAndUser(List<Long> idList, List<String> namespaceList) {
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
				// 记录删除租户信息
				String extraInfo = "删除租户 ： " + user.getUserName() + "信息：" + JSON.toJSONString(user);
				CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
						CommConstant.TENANT_MANAGER, CommConstant.OPERATION_TYPE_DELETE);
				commonOperationLogDao.save(log);
			}
		}
		return users;
	}

	/**
	 *
	 * Description: 删除租户命名空间
	 *
	 * @param namespaceList
	 */
	private void delNamespace(List<String> namespaceList) {
		if (CollectionUtils.isNotEmpty(namespaceList)) {
			for (String namespace : namespaceList) {
				// 以用户名(登陆帐号)为name，创建client
				KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
				Namespace namespace2 = null;

				try {
					namespace2 = client.getNamespace(namespace);
				} catch (Exception e) {
					LOG.error("can not find namespace " + e.getMessage());
					continue;
				}

				if (null != namespace2) {
					try {
						client.deleteNamespace(namespace);
						// 逻辑删除卷组信息
					} catch (javax.ws.rs.ProcessingException e) {
						LOG.error("delete namespace error" + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 *
	 * Description: 获取用户的资源使用信息
	 *
	 * @param model
	 * @param user
	 * @param client
	 * @see
	 */
	private void getUserResourceInfo(Model model, User user, KubernetesAPIClientInterface client) {
		ResourceQuota quota = client.getResourceQuota(user.getNamespace());
		if (null != quota) {
			UserResource userResource = new UserResource();
			if (user.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
				userResource = userResourceDao.findByUserId(user.getParent_id());
			} else {
				userResource = userResourceDao.findByUserId(user.getId());
			}
			model.addAttribute("userResource", userResource);
			model.addAttribute("user", user);
			Map<String, String> hard = quota.getStatus().getHard();
			model.addAttribute("servCpuNum", ConvertUtil.convertCpu(hard.get("cpu"))); // cpu个数
			model.addAttribute("servMemoryNum", ConvertUtil.convertMemory(hard.get("memory")));// 内存个数
			model.addAttribute("servPodNum", hard.get("pods"));// pod个数
			model.addAttribute("servServiceNum", hard.get("services")); // 服务个数
			model.addAttribute("servControllerNum", hard.get("replicationcontrollers"));// 副本控制数

			Map<String, String> used = quota.getStatus().getUsed();
			ReplicationControllerList rcList = client.getAllReplicationControllers();
			PodList podList = client.getAllPods();
			model.addAttribute("usedCpuNum", ConvertUtil.convertCpu(used.get("cpu"))); // 已使用CPU个数
			model.addAttribute("usedMemoryNum", ConvertUtil.convertMemory(used.get("memory")));// 已使用内存
			model.addAttribute("usedPodNum", (null != podList) ? podList.size() : 0); // 已经使用的POD个数
			model.addAttribute("usedServiceNum", (null != rcList) ? rcList.size() : 0);// 已经使用的服务个数
			// model.addAttribute("usedControllerNum", usedControllerNum);
		} else {
			LOG.info("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
		}
	}

	/**
	 *
	 * Description: 更新用户信息
	 *
	 * @param id
	 * @param email
	 * @param company
	 * @param user_cellphone
	 * @param user_department
	 * @param user_employee_id
	 * @param user_phone
	 * @return user User
	 */
	private User updateUserInfo(long id, String email, String company, String user_cellphone, String user_department,
			String user_employee_id, String user_phone) {
		User user = userDao.findById(id);
		user.setEmail(email);
		user.setCompany(company);
		user.setUser_department(user_department);
		user.setUser_employee_id(user_employee_id);
		user.setUser_cellphone(user_cellphone);
		user.setUser_phone(user_phone);
		user = userDao.save(user);

		// 记录更新用户信息操作
		String extraInfo = "更新用户 ： " + user.getUserName() + "信息" + JSON.toJSONString(user);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(user.getUserName(), extraInfo,
				CommConstant.TENANT_MANAGER, CommConstant.OPERATION_TYPE_UPDATE);
		commonOperationLogDao.save(log);

		return user;
	}

	/**
	 * Description: <br>
	 * 查询所有的shera
	 *
	 * @return jsp
	 */
	@RequestMapping("/shera")
	public String findShera(Model model) {
		Iterable<Shera> sheraList = sheraDao.findAll();
		model.addAttribute("sheraList", sheraList);
		model.addAttribute("menu_flag", "ci");
		model.addAttribute("li_flag", "shera");
		return "ci/shera.jsp";
	}

	/**
	 * createShera:创建shera. <br/>
	 *
	 * @author longkaixiang
	 * @param shera
	 * @param jdkJson
	 * @param mavenJson
	 * @param antJson
	 * @param sonarJson
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ("/shera/creatShera.do"), method = RequestMethod.POST)
	@ResponseBody
	public String createShera(Shera shera, String jdkJson, String mavenJson, String antJson, String sonarJson) {
		Map<String, Object> map = new HashMap<>();
		if (shera.getId() == 0) {
			List<Shera> sheraList = sheraDao.findByUrlAndPort(shera.getSheraUrl(), shera.getPort());
			if (CollectionUtils.isNotEmpty(sheraList)) {
				// 有重复的配置
				map.put("status", "305");
				return JSON.toJSONString(map);
			}
		}
		SheraAPIClientInterface client = sheraClientService.getClient(shera);
		try {
			// 用任意一个api检测当前配置的shera能否使用
			client.getAllJdk();
		} catch (Exception e) {
			// 配置不能使用的情况下返回300
			map.put("status", "300");
			return JSON.toJSONString(map);
		}

		// 创建jdk
		if (StringUtils.isNotBlank(jdkJson)) {
			try {
				// 删除旧的jdk
				JdkList allJdk = client.getAllJdk();
				if (null != allJdk && null != allJdk.getItems() && allJdk.getItems().size() != 0) {
					for (Jdk jdk : allJdk.getItems()) {
						client.deleteJdk(jdk.getVersion());
					}
				}
				Map<String, String> jdkMap = new HashMap<>();
				jdkMap = JSON.parseObject(jdkJson, jdkMap.getClass());
				for (String key : jdkMap.keySet()) {
					Jdk jdk = sheraClientService.generateJdk(key, jdkMap.get(key));
					client.createJdk(jdk);
				}
			} catch (Exception e) {
				map.put("status", "301");
				map.put("message", "创建jdk配置失败:[jdkJson:" + jdkJson + "]");
				return JSON.toJSONString(map);
			}
		}

		// 创建mvn的配置
		if (StringUtils.isNoneBlank(mavenJson)) {
			try {
				client.deleteExecConfig("admin", SheraConstant.EXEC_MAVEN_CONFIG);
				if (!createExecConfig(client, "admin", mavenJson)) {
					map.put("status", "302");
					map.put("message", "创建mvn配置失败:[mavenJson:" + mavenJson + "]");
					return JSON.toJSONString(map);
				}
			} catch (Exception e) {
				map.put("status", "302");
				map.put("message", "创建mvn配置失败");
				e.printStackTrace();
				return JSON.toJSONString(map);
			}
		}

		// 创建ant的配置
		if (StringUtils.isNoneBlank(antJson)) {
			try {
				client.deleteExecConfig("admin", SheraConstant.EXEC_ANT_CONFIG);
				if (!createExecConfig(client, "admin", antJson)) {
					map.put("status", "303");
					map.put("message", "创建ant配置失败:[antJson:" + antJson + "]");
					return JSON.toJSONString(map);
				}
			} catch (Exception e) {
				map.put("status", "303");
				map.put("message", "创建ant配置失败");
				e.printStackTrace();
				return JSON.toJSONString(map);
			}
		}

		// 创建sonar的配置
		if (StringUtils.isNoneBlank(sonarJson)) {
			try {
				client.deleteExecConfig("admin", SheraConstant.EXEC_SONAR_CONFIG);
				if (!createExecConfig(client, "admin", sonarJson)) {
					map.put("status", "304");
					map.put("message", "创建sonar配置失败:[sonarJson:" + sonarJson + "]");
					return JSON.toJSONString(map);
				}
			} catch (Exception e) {
				map.put("status", "304");
				map.put("message", "创建sonar配置失败");
				e.printStackTrace();
				return JSON.toJSONString(map);
			}
		}

		// 创建成功的情况,保存shera
		shera.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
		shera.setCreateDate(new Date());
		sheraDao.save(shera);
		// 记录新增shera操作
		String extraInfo = "新增shera ： " + shera.getSheraUrl() + "信息：" + JSON.toJSONString(shera);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(shera.getSheraUrl(), extraInfo,
				CommConstant.SHERA_MANAGER, CommConstant.OPERATION_TYPE_CREATED);
		commonOperationLogDao.save(log);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * createExecConfig:更具json字符串创建ExecConfig. <br/>
	 *
	 * @author longkaixiang
	 * @param client
	 * @param userid
	 * @param jsonString
	 * @return boolean
	 */
	private boolean createExecConfig(SheraAPIClientInterface client, String userid, String jsonString) {
		List<ExecConfig> configList = new ArrayList<>();
		configList = JSON.parseArray(jsonString, ExecConfig.class);
		for (ExecConfig config : configList) {
			config.setUserid("admin");
			try {
				client.createExecConfig(config);
			} catch (SheraClientException e) {
				LOG.error("创建execConfig异常：[jsonString:" + jsonString + "]");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 删除shera
	 *
	 * @param sheraId
	 *            sheraId
	 * @return
	 * @see
	 */
	@RequestMapping("/shera/deleteShera.do")
	@ResponseBody
	public String deleteShera(long sheraId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Shera shera = sheraDao.findOne(sheraId);
			sheraDao.delete(sheraId);
			userAndSheraDao.deleteBySheraId(sheraId);

			// 记录删除shera操作
			String extraInfo = "删除shera ： " + shera.getSheraUrl() + "信息：" + JSON.toJSONString(shera);
			CommonOperationLog log = CommonOprationLogUtils.getOprationLog(shera.getSheraUrl(), extraInfo,
					CommConstant.SHERA_MANAGER, CommConstant.OPERATION_TYPE_DELETE);
			commonOperationLogDao.save(log);

			map.put("status", "200");
		} catch (Exception e) {
			LOG.error("delete shera error : " + e.getMessage());
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 批量删除shera
	 *
	 * @param sheraIds
	 *            sheraids
	 * @return String
	 */
	@RequestMapping("/shera/delsheras.do")
	@ResponseBody
	public String delSheras(String sheraIds) {
		Map<String, Object> map = new HashMap<>();
		ArrayList<Long> ids = new ArrayList<Long>();
		String[] str = sheraIds.split(",");
		if (str != null && str.length > 0) {
			for (String id : str) {
				ids.add(Long.valueOf(id));
			}
		}
		try {
			for (long id : ids) {
				deleteShera(id);
			}
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "400");
			LOG.error("delete sheras error : " + e.getMessage());
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 更新shera
	 *
	 * @param shera
	 *            shera
	 * @return
	 */
	@RequestMapping("/shera/updateShera.do")
	@ResponseBody
	public String updateShera(Shera shera, String jdkJson) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Shera oldShera = sheraDao.findOne(shera.getId());
			oldShera.setSheraUrl(shera.getSheraUrl());
			oldShera.setPort(shera.getPort());
			oldShera.setUserName(shera.getUserName());
			oldShera.setPassword(shera.getPassword());
			oldShera.setRemark(shera.getRemark());
			sheraDao.save(oldShera);

			// 记录更新shera操作
			String extraInfo = "更新shera ： " + shera.getSheraUrl() + "信息：" + JSON.toJSONString(shera);
			CommonOperationLog log = CommonOprationLogUtils.getOprationLog(shera.getSheraUrl(), extraInfo,
					CommConstant.SHERA_MANAGER, CommConstant.OPERATION_TYPE_UPDATE);
			commonOperationLogDao.save(log);

			if (updateJdk(oldShera, jdkJson)) {
				map.put("status", "200");
			} else {
				map.put("status", "400");
			}
		} catch (Exception e) {
			LOG.error("update shera error" + e.getMessage());
			map.put("status", "400");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 更新jdk信息
	 *
	 * @param shera
	 *            shera
	 * @param jdkJson
	 *            jdk数据
	 * @return boolean
	 */
	public boolean updateJdk(Shera shera, String jdkJson) {
		try {
			SheraAPIClientInterface client = sheraClientService.getClient(shera);
			JdkList jdkList = client.getAllJdk();
			if (jdkList.getItems() != null) {
				for (Jdk jdk : jdkList) {
					client.deleteJdk(jdk.getVersion());
				}
			}
			if (StringUtils.isNotEmpty(jdkJson)) {
				String[] jdkData = jdkJson.split(";");
				for (String jdkRow : jdkData) {
					String jdkVersion = jdkRow.substring(0, jdkRow.indexOf(","));
					String jdkPath = jdkRow.substring(jdkRow.indexOf(",") + 1);
					Jdk jdk = sheraClientService.generateJdk(jdkVersion, jdkPath);
					client.createJdk(jdk);
				}
			}
		} catch (Exception e) {
			LOG.error("update JDK error : " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Description: <br>
	 * 获取shera的详细信息
	 *
	 * @param sheraId
	 *            id；
	 * @return String
	 * @see
	 */
	@RequestMapping("/shera/detail.do")
	@ResponseBody
	public String detailShera(long sheraId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Shera shera = sheraDao.findOne(sheraId);
		map.put("shera", shera);
		try {
			SheraAPIClientInterface client = sheraClientService.getClient(shera);
			JdkList jdkList = client.getAllJdk();
			map.put("jdkList", jdkList.getItems());
		} catch (Exception e) {
			LOG.error("get shera detail error : " + e.getMessage());
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Description: <br>
	 * 删除指定的loginId
	 *
	 * @param loginId
	 * @return String
	 * @see
	 */
	@RequestMapping("/deleteByLoginId.do")
	@ResponseBody
	public String deleteByLoginId(String loginId) {
		LOG.info("DELETE USER:" + loginId);
		User user = userDao.findByUserName(loginId);
		if (user.getUser_autority().equals(UserConstant.AUTORITY_TENANT)) {
			return userDelMul(user.getId() + "");
		} else {
			return userDel(user.getId() + "");
		}
	}

	/**
	 * detailShera:shera detail页面. <br/>
	 *
	 * @author longkaixiang
	 * @param model
	 * @param id
	 * @return String
	 */
	@RequestMapping("/shera/detail/{id}")
	public String detailShera(Model model, @PathVariable long id) {
		Shera shera = sheraDao.findOne(id);
		SheraAPIClientInterface client = sheraClientService.getClient(shera);
		JdkList allJdk = null;
		try {
			allJdk = client.getAllJdk();
		} catch (SheraClientException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> mvnConfigList = getFormatExecConfig(client, SheraConstant.EXEC_MAVEN_CONFIG);
		List<Map<String, Object>> antConfigList = getFormatExecConfig(client, SheraConstant.EXEC_ANT_CONFIG);
		List<Map<String, Object>> sonarConfigList = getFormatExecConfig(client, SheraConstant.EXEC_SONAR_CONFIG);

		model.addAttribute("shera", shera);
		model.addAttribute("allJdk", allJdk);
		model.addAttribute("mvnConfig", mvnConfigList);
		model.addAttribute("antConfig", antConfigList);
		model.addAttribute("sonarConfig", sonarConfigList);
		model.addAttribute("menu_flag", "ci");
		model.addAttribute("li_flag", "shera");
		return "ci/shera-detail.jsp";
	}

	/**
	 * getFormatExecConfig:获取格式化的ExecConfig. <br/>
	 *
	 * @author longkaixiang
	 * @param client
	 * @param Kind
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String, Object>> getFormatExecConfig(SheraAPIClientInterface client, Integer Kind) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<ExecConfig> sonarConfigs;
		try {
			sonarConfigs = client.getExecConfig(Kind);
		} catch (Exception e) {
			sonarConfigs = null;
		}
		if (sonarConfigs != null) {
			for (ExecConfig sonarConfig : sonarConfigs) {
				Map<String, Object> map = new HashMap<>();
				map.put("version", sonarConfig.getVersion());

				Map<String, String> env = sonarConfig.getEnv();
				List<Object> envList = new ArrayList<>();
				if (MapUtils.isNotEmpty(env)) {
					for (String key : env.keySet()) {
						Map<String, String> envItem = new HashMap<>();
						envItem.put("key", key);
						envItem.put("value", env.get(key));
						envList.add(envItem);
					}
				}
				map.put("env", envList);
				resultList.add(map);
			}
		}
		return resultList;
	}

}
