package com.bonc.epm.paas.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.controller.ServiceController;
import com.bonc.epm.paas.controller.UserController;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaSpec;
import com.bonc.epm.paas.kubernetes.model.Secret;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.EncryptUtils;

@Controller
@RequestMapping(value = "/api/v1")
public class UserApi {

	/**
	 * 内存和cpu的比例大小
	 */
	@Value("${ratio.memtocpu}")
	private String RATIO_MEMTOCPU = "4";

	/**
	 * CEPH_KEY ${ceph.key}
	 */
	@Value("${ceph.key}")
	private String CEPH_KEY;

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	/**
	 * KubernetesClientService
	 */
	@Autowired
	private KubernetesClientService kubernetesClientService;

	/**
	 * 调用服务controller
	 */
	@Autowired
	private ServiceController serviceController;

	/**
	 * 服务数据层接口
	 */
	@Autowired
	private ServiceDao serviceDao;

	/**
	 * 用户层接口
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * userResourceDao
	 */
	@Autowired
	private UserResourceDao userResourceDao;

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
	 * Description: 删除用户
	 *
	 * @param tenantName
	 *            String 租户姓名
	 * @param userName
	 *            String 用户姓名
	 * @see
	 */
	@RequestMapping(value = { "/tenant/{tenantName}/user/{username}" }, method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteUser(@PathVariable String tenantName, @PathVariable String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		User parent = userDao.findByUserName(tenantName);

		// 校验是否存在租户
		if (parent == null) {
			map.put("message", "不存在该租户，无法删除用户！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		Long parentId = parent.getId();
		List<User> userlist = userDao.getByParentIdAndUsername(parentId, username);

		if (CollectionUtils.isEmpty(userlist)) {
			map.put("message", "指定租户下不存在该用户！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		delUserService(userlist.get(0).getId());

		// 获取删除前的user
		User user = userDao.findById(userlist.get(0).getId());

		userDao.delete(user);

		map.put("flag", "200");
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 创建或更新用户
	 *
	 * @param tenantName
	 *            String 租户姓名
	 * @param user
	 *            User 用户
	 * @see
	 */
	@RequestMapping(value = { "/tenant/{tenantName}/user" }, method = RequestMethod.POST)
	@ResponseBody
	public String saveUser(@PathVariable String tenantName, @RequestBody User user) {
		Map<String, Object> map = new HashMap<String, Object>();

		User parent = userDao.findByUserName(tenantName);

		// 校验是否存在租户
		if (parent == null) {
			map.put("message", "不存在该租户，无法新增用户！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		Long parentId = parent.getId();

		List<User> userlist = userDao.getByParentIdAndUsername(parentId, user.getUserName());

		user.setParent_id(parentId);
		user.setNamespace(parent.getNamespace());
		user.setUser_autority("3");

		if (userlist.size() == 0) { // 新增
			user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
			map.put("message", "新增用户成功！");
		} else { // 更新
			if (StringUtils.isEmpty(user.getPassword())) {
				user.setPassword(userlist.get(0).getPassword());
			} else {
				user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
			}
			map.put("message", "修改用户成功！");
			user.setId(userlist.get(0).getId());
		}

		// 保存
		userDao.save(user);

		map.put("flag", "200");
		System.out.println(JSON.toJSONString(map));
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 创建或更新租户
	 *
	 * @param user
	 *            User
	 * @param resource
	 *            UserResource
	 * @see
	 */
	@RequestMapping(value = { "/tenant" }, method = RequestMethod.POST)
	@ResponseBody
	public String saveTenant(@RequestBody User user) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 判断用户名称是否为空
		if (StringUtils.isBlank(user.getUserName())) {
			map.put("message", "用户名称为空，操作失败！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		KubernetesAPIClientInterface client = null;
		user.setNamespace(user.getUserName());

		// 判断操作为新增还是更新
		if (userDao.findByUserName(user.getUserName()) == null) {// 新增
			user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
			try {

				Namespace namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
				client = kubernetesClientService.getClient(user.getNamespace());

				if (!createNsAndSec(user, namespace, client)) {
					client.deleteNamespace(user.getNamespace());
					map.put("message", "创建namespace或者secret失败！");
					map.put("flag", "400");
					return JSON.toJSONString(map);
				}

				if (!createCeph(user)) {
					client.deleteNamespace(user.getNamespace());
					// client.deleteResourceQuota(user.getNamespace());
					map.put("message", "创建ceph失败");
					map.put("flag", "400");
					return JSON.toJSONString(map);
				}
			} catch (Exception e) {
				client.deleteNamespace(user.getNamespace());
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				e.printStackTrace();
				map.put("message", "创建失败");
				map.put("flag", "400");
				return JSON.toJSONString(map);
			}
		} else {// 更新
			if (StringUtils.isEmpty(user.getPassword())) {
				user.setPassword(userDao.findById(user.getId()).getPassword());
			} else {
				user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
			}

			client = kubernetesClientService.getClient(user.getNamespace());
			Namespace namespace = null;

			try {
				namespace = client.getNamespace(user.getNamespace());
			} catch (Exception e) {
				LOG.error("no namespace info!");
				namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
				namespace = client.createNamespace(namespace);
			}

			Long originalId = userDao.findByUserName(user.getUserName()).getId();// 该用户原ID
			user.setId(originalId);
		}

		user.setParent_id(1);// 默认填写为1
		user.setUser_autority("2");

		// DB保存用户信息
		userDao.save(user);
		map.put("flag", "200");

		return JSON.toJSONString(map);
	}

	/**
	 *
	 * Description: 创建或更新资源配额
	 *
	 * @param user
	 *            User
	 * @param resource
	 *            UserResource
	 * @see
	 */
	@RequestMapping(value = { "/tenant/{tenantName}/resource" }, method = RequestMethod.POST)
	@ResponseBody
	public String saveResoure(@PathVariable String tenantName, @RequestBody UserResource resource) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 判断用户名称是否为空
		if (StringUtils.isBlank(tenantName)) {
			map.put("message", "租户名称为空，操作失败！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		KubernetesAPIClientInterface client = null;
		User user = userDao.findByUserName(tenantName);

		// 判断是否存在该租户
		if (user == null) {
			map.put("message", "指定的租户不存在，操作失败！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		client = kubernetesClientService.getClient(user.getNamespace());
		Namespace namespace = null;

		try {
			namespace = client.getNamespace(user.getNamespace());
		} catch (Exception e) {
			LOG.error("no namespace info!");
			namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
			namespace = client.createNamespace(namespace);
		}

		if (namespace != null) {
			UserResource userResource = userResourceDao.findByUserId(user.getId());

			// 根据资源是否存在，判断操作为新增还是更新
			if (userResource == null) {// 新增
				if (!createQuota(user, resource, client)) {
					map.put("message", "创建quota失败");
					map.put("flag", "400");
					return JSON.toJSONString(map);
				}
				resource.setCreateDate(new Date());
			} else {
				try {
					ResourceQuota quota = updateQuotaInfo(client, user, resource);
					client.updateResourceQuota(user.getNamespace(), quota);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("message", "更新失败");
					map.put("flag", "400");
					return JSON.toJSONString(map);
				}
				resource.setId(userResource.getId());
				resource.setCreateDate(userResource.getCreateDate());
			}
		} else {
			LOG.error("租户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
			map.put("message", "没有Namespace，操作失败！");
			map.put("flag", "400");
			return JSON.toJSONString(map);
		}

		resource.setUserId(user.getId());
		resource.setUpdateDate(new Date());
		userResourceDao.save(resource);

		map.put("flag", "200");

		return JSON.toJSONString(map);
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
	public boolean createQuota(User user, UserResource resource, KubernetesAPIClientInterface client) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("memory", resource.getMemory() + "G"); // 内存
			map.put("cpu", Double.valueOf(resource.getCpu()) / Double.valueOf(RATIO_MEMTOCPU) + "");// CPU数量(个)
			map.put("persistentvolumeclaims", resource.getVol_size() + "");// 卷组数量
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
	 * Description: ceph中创建租户目录
	 *
	 * @param user
	 * @return boolean
	 * @see
	 */
	public boolean createCeph(User user) {
		// try {
		// CephController ceph = new CephController();
		// ceph.connectCephFS();
		// ceph.createNamespaceCephFS(user.getNamespace());
		return true;
		// } catch (Exception e) {
		// LOG.error(e.getMessage());
		// return false;
		// }
	}

	/**
	 * 获取更新后的 ResourceQuota
	 *
	 * @param client
	 * @param namespace
	 * @param resource
	 * @return quota ResourceQuota
	 */
	private ResourceQuota updateQuotaInfo(KubernetesAPIClientInterface client, User user, UserResource resource) {
		ResourceQuota quota = null;
		try {
			quota = client.getResourceQuota(user.getNamespace());
		} catch (Exception e) {
			createQuota(user, resource, client);
		}
		quota = client.getResourceQuota(user.getNamespace());
		ResourceQuotaSpec spec = quota.getSpec();

		Map<String, String> hard = quota.getSpec().getHard();
		hard.put("memory", resource.getMemory() + "G"); // 内存
		hard.put("cpu", Double.valueOf(resource.getCpu()) / Double.valueOf(RATIO_MEMTOCPU) + "");// CPU数量
		hard.put("persistentvolumeclaims", resource.getVol_size() + "");// 卷组数量
		spec.setHard(hard);
		quota.setSpec(spec);
		return quota;
	}
	/**
	 * buildUsers:根据数据库创建用户的namespace，quota，ceph. <br/>
	 *
	 * @author longkaixiang
	 * @return String
	 */
	@RequestMapping(value = { "/user/build" }, method = RequestMethod.POST)
	@ResponseBody
	public String buildUsers() {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		Iterable<User> allUsers = userDao.findAllTenant();
		Iterator<User> iterator = allUsers.iterator();
		while (iterator.hasNext()) {
			messages = createUserResource(iterator.next(), messages);
		}
		map.put("status", "200");
		if (messages.size() > 0) {
			map.replace("status", "400");
			map.put("message", messages);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * buildUsers:根据用户名查找数据库创建用户的namespace，quota，ceph. <br/>
	 *
	 * @author longkaixiang
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/build" }, method = RequestMethod.POST)
	@ResponseBody
	public String buildSpecifiedUser(@PathVariable("user") String userName) {
		Map<String, Object> map = new HashMap<>();
		List<String> messages = new ArrayList<>();
		User user = userDao.findByUserName(userName);
		if (null == user) {
			messages.add("未找到用户[" + userName + "]");
		}
		messages = createUserResource(user, messages);
		map.put("status", "200");
		if (messages.size() > 0) {
			map.replace("status", "400");
			map.put("message", messages);
		}
		return JSON.toJSONString(map);
	}

	private List<String> createUserResource(User user, List<String> messages) {
		if (null == messages) {
			messages = new ArrayList<>();
		}
		if (user.getUser_autority().equals(UserConstant.AUTORITY_TENANT)) {
			UserResource userResource = userResourceDao.findByUserId(user.getId());

			KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
			Namespace namespace = null;
			try {
				namespace = client.getNamespace(user.getNamespace());
			} catch (KubernetesClientException e1) {
				namespace = null;
			}
			if (namespace != null) {
				messages.add("namespace已存在[userName=" + user.getUserName() + ",nameSpace=" + user.getNamespace() + "]");
				return messages;
			}
			namespace = kubernetesClientService.generateSimpleNamespace(user.getNamespace());
			try {
				if (!createNsAndSec(user, namespace, client)) {
					client.deleteNamespace(user.getNamespace());
					messages.add("创建namespace或者secret失败！[userName=" + user.getUserName() + ",nameSpace="
							+ user.getNamespace() + "]");
				}

				// 根据资源是否存在，判断操作为新增还是更新
				if (userResource != null) {
					if (!createQuota(user, userResource, client)) {
						client.deleteNamespace(user.getNamespace());
						messages.add(
								"创建quota失败[userName=" + user.getUserName() + ",nameSpace=" + user.getNamespace() + "]");
					}
				}

				if (!createCeph(user)) {
					client.deleteNamespace(user.getNamespace());
					messages.add(
							"创建ceph失败！[userName=" + user.getUserName() + ",nameSpace=" + user.getNamespace() + "]");
				}
			} catch (Exception e) {
				client.deleteNamespace(user.getNamespace());
				e.printStackTrace();
				messages.add("创建失败！[userName=" + user.getUserName() + ",nameSpace=" + user.getNamespace() + "]");
			}
		} else {
			messages.add("不是租户：[userName=" + user.getUserName() + ",nameSpace=" + user.getNamespace() + "]");
		}
		return messages;
	}

}
