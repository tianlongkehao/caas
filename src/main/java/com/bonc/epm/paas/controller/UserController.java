package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.Resource;
import com.bonc.epm.paas.entity.Restriction;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeItem;
import com.bonc.epm.paas.kubernetes.model.LimitRangeSpec;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ObjectMeta;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaSpec;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;

@Controller

@RequestMapping(value = "/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static Map<String, KubernetesAPIClientInterface> clientMap;
	@Autowired
	public UserDao userDao;
	@Autowired
	private KubernetesClientService kubernetesClientService;
	private Model model;

	/**
	 * 展示所有用户信息、
	 * 1、租户、管理员无差别？
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {
		List<User> userList = new ArrayList<User>();
		for (User user : userDao.checkUser("3")) {
			userList.add(user);
		}
		model.addAttribute("userList", userList);
		model.addAttribute("menu_flag", "user");
		return "user/user.jsp";
	}

	@RequestMapping(value="/manage/list/{id}",method=RequestMethod.GET)
	public String userIndex(Model model,@PathVariable long id){
		User userManger = userDao.findOne(id);
		List<User> userManageList = new ArrayList<>();
		for(User user:userDao.checkUsermanage("3", userManger.getUser_province())){
			userManageList.add(user);
		}
		for(User user:userDao.checkUsermanage("4", userManger.getUser_province())){
			userManageList.add(user);
		}
		model.addAttribute("userManageList",userManageList);
		model.addAttribute("menu_flag", "usermanage");
		return "user/user-management.jsp";
	}

	/**
	 * 跳转到租户创建页面： user/user_create.jsp
	 *
	 * @return
	 */

	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
	public String useradd(Model model) {
		model.addAttribute("menu_flag", "user");
		return "user/user_create.jsp";
	}

	@RequestMapping(value={"/manage/add/{id}"},method=RequestMethod.GET)
	public String userCreate(Model model, @PathVariable long id){
		User userMangerCreat = userDao.findOne(id);
		model.addAttribute("menu_flag", "user");
		return "user/user_manage_create.jsp";
	}


			/**
			 * 创建新租户
			 * 以用户登陆帐号（用户名）为名称，创建Namespace
			 *
			 * @param user
			 * @return
			 */
			@RequestMapping(value = {"/save.do"}, method = RequestMethod.POST)
			public String userSave(User user, Resource resource, Restriction restriction, Model model) {
				System.out.println("save.do=============================================");
				try {
					//以用户名(登陆帐号)为name，创建client
					KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getUserName());

					//以用户名(登陆帐号)为name，为client创建Namespace
					Namespace namespace = kubernetesClientService.generateSimpleNamespace(user.getUserName());
					namespace = client.createNamespace(namespace);
					System.out.println("namespace:" + JSON.toJSONString(namespace));

					//为client创建资源配额
					Map<String, String> map = new HashMap<String, String>();
					map.put("memory", resource.getRam() + "G");    //内存

//	    	map.put("cpu", Integer.valueOf(resource.getCpu_account())*1024+"");//CPU数量
					map.put("cpu", resource.getCpu_account() + "");//CPU数量(个)
					map.put("pods", resource.getPod_count() + "");//POD数量
					map.put("services", resource.getServer_count() + "");//服务
					map.put("replicationcontrollers", resource.getImage_control() + "");//副本控制器
					map.put("resourcequotas", "1");//资源配额数量
					ResourceQuota quota = kubernetesClientService.generateSimpleResourceQuota(user.getUserName(), map);
					System.out.println("before quota: " + JSON.toJSONString(quota));
					quota = client.createResourceQuota(quota);
					System.out.println("quota:" + JSON.toJSONString(quota));

					//为client创建资源限制
					LimitRange limitRange = generateLimitRange(user.getUserName(), restriction);
					limitRange = client.createLimitRange(limitRange);
//	    	System.out.println("limitRange:"+JSON.toJSONString(limitRange));

					//DB保存用户信息
					userDao.save(user);
					model.addAttribute("creatFlag", "200");
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("creatFlag", "400");
				}

				//返回 user.jsp 页面，展示所用用户信息
				List<User> userList = new ArrayList<User>();
				for(User uu : userDao.findAll()){
					userList.add(uu);
				}
				model.addAttribute("userList",userList);
				model.addAttribute("menu_flag", "user");
				return "user/user.jsp";
			}


			/**
			 * 创建新用户
			 */
			@RequestMapping(value = {"/savemanage.do"}, method = RequestMethod.POST)
			public String userManageSave(User user, Model model) {
				System.out.println("savemanage.do=============================================");
				try {
					//DB保存用户信息
					userDao.save(user);
					model.addAttribute("creatFlag", "200");
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("creatFlag", "400");
				}

				//返回 user-management.jsp 页面，展示该租户所创建的所有用户信息
				List<User> userList = new ArrayList<User>();
				for (User uuuu : userDao.findAll()) {
					userList.add(uuuu);
				}
				model.addAttribute("userList", userList);

				/*User userManger = userDao.findOne(id);
				List<User> userManageList = new ArrayList<>();
				for(User uu:userDao.checkUsermanage("3", userManger.getUser_province())){
					userManageList.add(uu);
				}
				model.addAttribute("userManageList",userManageList);*/

				model.addAttribute("menu_flag", "user");
				return "user/user-management.jsp";
			}


			/**
			 * 更新用户信息
			 *
			 * @param user
			 * @return
			 */
			@RequestMapping(value = {"/update.do"}, method = RequestMethod.POST)
			public String userUpdate(User user, Resource resource, Restriction restriction, Model model) {

				//以用户名(登陆帐号)为name，创建client
				KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getUserName());

				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~"+user.getUserName());
				ResourceQuota quota = updateQuota(client, user.getUserName(), resource);
				LimitRange limit = updateLimitRange(client, user.getUserName(), restriction);

				try {
					ResourceQuota updateQuota = client.updateResourceQuota(user.getUserName(), quota);
					LimitRange updateLimitRange = client.updateLimitRange(user.getUserName(), limit);
					System.out.println("+++++++++++++++++++++++++"+user.getUser_autority());
					userDao.save(user);
					model.addAttribute("updateFlag", "200");
					//返回 user.jsp 页面，展示所用用户信息
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("updateFlag", "400");
				}
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~"+"updateFlag");

				List<User> userList = new ArrayList<User>();
				for (User uu : userDao.findAll()) {
					userList.add(uu);
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~" + uu);
				}
				model.addAttribute("userList", userList);
				model.addAttribute("menu_flag", "user");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "??????????????????????????");
				return "user/user.jsp";
			}


			/**
			 * 局部刷新，批量删除用户
			 *
			 * @return
			 */
			@RequestMapping("/delMul.do")
			@ResponseBody
			public String userDelMul(String ids) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<User> users = new ArrayList<User>();
				List<Long> idList = new ArrayList<Long>();
				List<String> userNameList = new ArrayList<String>();
				try {
					String[] idArr = ids.split(",");
					for (int i = 0; i < idArr.length; i++) {
						idList.add(Long.parseLong(idArr[i]));
					}
					for (User user : userDao.findAll(idList)) {
						users.add(user);
						userNameList.add(user.getUserName());
					}
					userDao.delete(users);
					for (String name : userNameList) {
						//以用户名(登陆帐号)为name，创建client
						KubernetesAPIClientInterface client = kubernetesClientService.getClient(name);
						if (client.getNamespace(name) != null) {
							client.deleteLimitRange(name);
							client.deleteResourceQuota(name);
							client.deleteNamespace(name);
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
			 * 根据用户id查询用户信息
			 *
			 * @param model
			 * @param id
			 * @return
			 */
			@RequestMapping(value = {"detail/{id}"}, method = RequestMethod.GET)
			public String Detail(Model model, @PathVariable long id) {
				System.out.println("/user/user/detail========================================");
				User user = userDao.findOne(id);
				Resource resource = new Resource();
				Restriction restriction = new Restriction();

				//以用户名(登陆帐号)为name，创建client
				KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getUserName());
				Namespace ns = client.getNamespace(user.getUserName());
				System.out.println("user.getUserName()========================================");
				if (ns != null) {
					System.out.println("namespace:" + JSON.toJSONString(ns));

					ResourceQuota quota = client.getResourceQuota(user.getUserName());
					System.out.println("resourceQuota:" + JSON.toJSONString(quota));

					if (quota != null) {
						Map<String, String> map = quota.getSpec().getHard();
		//				Integer a=Integer.valueOf(map.get("cpu"))/1024;
		//	    		resource.setCpu_account(a.toString());//CPU数量
						resource.setCpu_account(map.get("cpu"));//CPU数量
						resource.setImage_control(map.get("replicationcontrollers"));//副本控制器
						resource.setPod_count(map.get("pods"));//POD数量
						resource.setRam(map.get("memory").substring(0, map.get("memory").length() - 1));//内存
						resource.setServer_count(map.get("services"));//服务
					}

					LimitRange lr = client.getLimitRange(user.getUserName());
					System.out.println("UserName:" + user.getUserName());
					System.out.println("limitRange:" + JSON.toJSONString(lr));

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
					}
				} else {
					System.out.println("用户 " + user.getUserName() + " 没有定义名称为 " + user.getUserName() + " 的Namespace ");
				}
				model.addAttribute("restriction", restriction);
				model.addAttribute("resource", resource);
				model.addAttribute("user", user);
				return "user/user_detail.jsp";
			}

			private String computeMemoryOut(Map<String, String> val) {
				String memVal = val.get("memory");
				if (memVal.contains("Mi")) {
					Float a1 = Float.valueOf(memVal.replace("Mi", "")) / 1024;
					return a1.toString();
				} else {
					return memVal.replace("Gi", "");
				}
			}

			private String computeCpuOut(Map<String, String> val) {
				String cpuVal = val.get("cpu");
				if (cpuVal.contains("m")) {
					Float a1 = Float.valueOf(cpuVal.replace("m", "")) / 1000;
					return a1.toString();
				} else {
					return cpuVal;
				}
			}


			@RequestMapping(value = {"manage/detail/{id}"}, method = RequestMethod.GET)
			public String manageDetail(Model model, @PathVariable long id) {
				this.model = model;
				System.out.println("/user/user/detail========================================");
				User user = userDao.findOne(id);
				model.addAttribute("user", user);
				return "user/user-manage-detail.jsp";
			}


			@RequestMapping(value = {"/searchByCondition"}, method = RequestMethod.POST)
			public String searchByCondition(String search_company, String search_department,
											String search_autority, String search_userName,
											String search_province,
											Model model) {
				List<User> userList = new ArrayList<User>();
				String company = "";
				String user_department = "";
				String user_autority = "";
				String user_realname = "";
				String user_province = "";

				if (search_company != null && !search_company.trim().equals("")) {
					company = search_company.trim();
				}
				if (search_department != null && !search_department.trim().equals("")) {
					user_department = search_department.trim();
				}
				if (search_province != null && !search_province.trim().equals("")) {
					user_province = search_province.trim();
				}
				if (search_userName != null && !search_userName.trim().equals("")) {
					user_realname = search_userName.trim();
				}

				if (search_autority.trim().length() > 0) {
					String[] arr = search_autority.trim().substring(0, search_autority.trim().length() - 1).split(",");
					if (arr.length == 1) {
		//				System.out.println("findby4");
						user_autority = arr[0].trim();
						for (User user : userDao.findBy4(company, user_department, user_autority, user_realname, user_province)) {
							userList.add(user);
						}
					} else {
		//				System.out.println("findby3");
						for (User user : userDao.find12By3(company, user_department, user_realname, user_province)) {
							userList.add(user);
						}

					}
				} else {
		//			System.out.println("find12By3");
					for (User user : userDao.find12By3(company, user_department, user_realname, user_province)) {
						userList.add(user);
					}
				}
				model.addAttribute("userList", userList);
				model.addAttribute("menu_flag", "user");
				return "user/user.jsp";
			}



	//租户搜查
			@RequestMapping(value={"/manage/searchByCondition/{id}"},method=RequestMethod.POST)
			public String searchByConditionS(
					String search_company, String search_department,
					String search_autority, String search_userName,
					String search_province,
					Model model){
				List<User> userManageList = new ArrayList<User>();
				String company = "";
				String user_department = "";
				String user_autority = "";
				String user_realname = "";
				String user_province = "";
				if(search_company != null && !search_company.trim().equals("")){
					company = search_company.trim();
				}
				if(search_department != null && !search_department.trim().equals("")){
					user_department = search_department.trim();
				}
				if(search_userName != null && !search_userName.trim().equals("")){
					user_realname = search_userName.trim();
				}
				if (search_province != null && !search_province.trim().equals("")) {
					user_province = search_province.trim();
				}
				if(search_autority.trim().length()>0){
					String[] arr = search_autority.trim().substring(0, search_autority.trim().length()-1).split(",");
					if(arr.length == 1){
						System.out.println("findby4");
						user_autority = arr[0].trim();
						System.out.println(user_autority);
						for(User user : userDao.findBy4(company, user_department, user_autority, user_realname, user_province)){
							userManageList.add(user);
						}
					}
					else {
						System.out.println("find12by3");
						for(User user : userDao.find12By3(company, user_department, user_realname, user_province)){
							userManageList.add(user);
						}

					}
				}
				else{
					System.out.println("find34by3");
					for(User user : userDao.find34By3(company, user_department, user_realname, user_province)){
						userManageList.add(user);
					}
//					user_autority = "4";
//					for(User user : userDao.findBy4(company, user_department, user_autority, user_realname, user_province)){
//						userManageList.add(user);
//					}
				}


				model.addAttribute("userManageList",userManageList);
				model.addAttribute("menu_flag", "user");
				return "user/user-management.jsp";
			}


	/**
	 * 查询用户名是存在
	 *
	 * @param username
	 * @return
	 */
	@RequestMapping(value = {"/checkUsername/{username}"}, method = RequestMethod.GET)
	@ResponseBody
	public String checkUsername(@PathVariable String username) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> names = userDao.checkUsername(username);
		if (names.size() > 0) {
			map.put("status", "400");
		} else {
			try {
				KubernetesAPIClientInterface client = kubernetesClientService.getClient(username);
				client.getNamespace(username);
                map.put("status", "300");
			}catch (KubernetesClientException e){
				System.out.print(e.getMessage()+":"+JSON.toJSON(e.getStatus()));
                map.put("status", "200");
			}
		}
		return JSON.toJSONString(map);
	}

	/**********************************************************************/

	@RequestMapping("user/add.do")
	public String userAdd(User user) {
		userDao.save(user);
		log.debug("userName--id:" + user.getUserName());
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("status", "200");
//		map.put("data", user);
//		return JSON.toJSONString(map);
		return "redirect:/user";
	}

	/**
	 * 删除单条记录
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"user/del/{id}"}, method = RequestMethod.GET)
	public String userDel(Model model, @PathVariable long id) {
		userDao.delete(id);
		//Map<String,Object> map = new HashMap<String,Object>();
		log.debug("del userid======:" + id);
		return "redirect:/user";
	}

	/**
	 * 查询登陆用户的基本信息、资源信息
	 *
	 * @param model
	 * @param id
	 * @return
	 */

	@RequestMapping(value = {"/detail/{id}/a", "/detail/{id}/b"}, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
		System.out.printf("user--id:", id);
		User user = userDao.findOne(id);

		String servServiceNum = "";//服务个数
		String usedCpuNum = "";//已使用CPU个数
		String usedMemoryNum = "";//已使用内存

		//以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 namespace 资源详情
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getUserName());
		Namespace ns = client.getNamespace(user.getUserName());
		System.out.println("namespace:" + JSON.toJSONString(ns));

		if (ns != null) {
			ResourceQuota quota = client.getResourceQuota(user.getUserName());
			System.out.println("resourceQuota:" + JSON.toJSONString(quota));
			if (quota != null) {
				Map<String, String> hard = quota.getStatus().getHard();
				Map<String, String> used = quota.getStatus().getUsed();
				servServiceNum = hard.get("services");//服务个数
				usedCpuNum = used.get("cpu");//已使用CPU个数
				usedMemoryNum = used.get("memory");//已使用内存
				/*******************************************************************/
            /* 					添加其它资源信息									   */
				/*******************************************************************/
			}
		} else {
			System.out.println("用户 " + user.getUserName() + " 还没有定义服务！");
		}
		model.addAttribute("user", user);
		model.addAttribute("servServiceNum", servServiceNum);
		model.addAttribute("usedCpuNum", usedCpuNum);
		model.addAttribute("usedMemoryNum", usedMemoryNum);
		return "user/user-own.jsp";
	}


	@RequestMapping("/userModifyPsw.do")
	@ResponseBody
	public String userModifyPsw(long id, String password, String newpwd) {
		User user = userDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user.getPassword().equals(password)) {
			user.setPassword(newpwd);
			userDao.save(user);
			map.put("status", "200");
		} else {
			map.put("status", "400");
		}

		return JSON.toJSONString(map);

	}

	@RequestMapping("/userModifyBasic.do")
	@ResponseBody
	public String userModifyBasic(long id, String email, String company, String user_cellphone,
								  String user_department, String user_employee_id, String user_phone) {
		Map<String, Object> map = new HashMap<String, Object>();

		User user = userDao.findById(id);
		user.setEmail(email);
		user.setCompany(company);
		user.setUser_department(user_department);
		user.setUser_employee_id(user_employee_id);
		user.setUser_cellphone(user_cellphone);
		user.setUser_phone(user_phone);

		if (userDao.save(user) != null) {
			map.put("status", "200");
		} else {
			map.put("status", "400");
		}

		return JSON.toJSONString(map);
	}

	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String userList(Model model, @PathVariable long id) {
		System.out.printf("user--id:", id);
		User user = userDao.findOne(id);

		model.addAttribute("user", user);
		model.addAttribute("menu_flag", "user");
		return "user/user.jsp";
	}

	@RequestMapping(value = {"/own"}, method = RequestMethod.GET)
	public String userOwn(Model model) {
		model.addAttribute("menu_flag", "userOwn");
		return "user/user-own.jsp";
	}

	/**
	 * <!-- 工具方法 -->
	 * 根据页面 Restriction restriction，定义LimitRange
	 *
	 * @param name
	 * @param restriction
	 * @return
	 */
	private LimitRange generateLimitRange(String name, Restriction restriction) {
		LimitRange limitRange = new LimitRange();
		ObjectMeta meta = new ObjectMeta();
		meta.setName(name);
		limitRange.setMetadata(meta);

		LimitRangeSpec spec = new LimitRangeSpec();
		List<LimitRangeItem> limits = new ArrayList<LimitRangeItem>();
		LimitRangeItem podLimitRangeItem = new LimitRangeItem();
		LimitRangeItem containerLimitRangeItem = new LimitRangeItem();

		//创建POD资源限制 LimitRangeItem
		podLimitRangeItem.setType("pod");
		Map<String, String> podMax = new HashMap<String, String>();
		Map<String, String> podMin = new HashMap<String, String>();
		Map<String, String> podDefault = new HashMap<String, String>();

		podMax.put("memory", computeMemory(restriction.getPod_memory_max()));
		podMax.put("cpu", computeCpu(restriction.getPod_cpu_max()));
		podMin.put("memory", computeMemory(restriction.getPod_memory_min()));
		podMin.put("cpu", computeCpu(restriction.getPod_cpu_min()));
		podDefault.put("memory", computeMemory(restriction.getPod_memory_default()));
		podDefault.put("cpu", computeCpu(restriction.getPod_cpu_default()));

		podLimitRangeItem.setDefaultVal(podDefault);
		podLimitRangeItem.setMax(podMax);
		podLimitRangeItem.setMin(podMin);

		//创建Container资源限制 LimitRangeItem
		containerLimitRangeItem.setType("Container");
		Map<String, String> containerMax = new HashMap<String, String>();
		Map<String, String> containerMin = new HashMap<String, String>();
		Map<String, String> containerDefault = new HashMap<String, String>();

		containerMax.put("memory", computeMemory(restriction.getContainer_memory_max()));
		containerMax.put("cpu", computeCpu(restriction.getContainer_cpu_max()));
		containerMin.put("memory", computeMemory(restriction.getContainer_memory_min()));
		containerMin.put("cpu", computeCpu(restriction.getContainer_cpu_min()));
		containerDefault.put("memory", computeMemory(restriction.getContainer_memory_default()));
		containerDefault.put("cpu", computeCpu(restriction.getContainer_cpu_default()));

		containerLimitRangeItem.setMax(containerMax);
		containerLimitRangeItem.setMin(containerMin);
		containerLimitRangeItem.setDefaultVal(containerDefault);

		limits.add(podLimitRangeItem);
		limits.add(containerLimitRangeItem);
		spec.setLimits(limits);
		limitRange.setSpec(spec);
		return limitRange;
	}

	private String computeMemory(String memory) {
		Float a = Float.valueOf(memory) * 1024;
		String b = a.toString();
		if (b.contains(".")) {
			return b.substring(0, b.indexOf(".")) + "Mi";
		} else {
			return b + "Mi";
		}
	}

	private String computeCpu(String cpu) {
		Float a = Float.valueOf(cpu) * 1000;
		String b = a.toString();
		if (b.contains(".")) {
			return b.substring(0, b.indexOf(".")) + "m";
		} else {
			return b + "m";
		}
	}

	/**
	 * <!-- 工具方法 -->
	 * 获取更新后的 LimitRange
	 *
	 * @param client
	 * @param username
	 * @param restriction
	 * @return
	 */
	private LimitRange updateLimitRange(KubernetesAPIClientInterface client, String username, Restriction restriction) {

		LimitRange limitRange = client.getLimitRange(username);
		LimitRangeSpec spec = limitRange.getSpec();

		List<LimitRangeItem> limits = new ArrayList<LimitRangeItem>();
		LimitRangeItem podLimitRangeItem = new LimitRangeItem();
		LimitRangeItem containerLimitRangeItem = new LimitRangeItem();

		//创建POD资源限制 LimitRangeItem
		podLimitRangeItem.setType("pod");
		Map<String, String> podMax = new HashMap<String, String>();
		Map<String, String> podMin = new HashMap<String, String>();
		Map<String, String> podDefault = new HashMap<String, String>();

		podMax.put("memory", computeMemory(restriction.getPod_memory_max()));
		podMax.put("cpu", computeCpu(restriction.getPod_cpu_max()));
		podMin.put("memory", computeMemory(restriction.getPod_memory_min()));
		podMin.put("cpu", computeCpu(restriction.getPod_cpu_min()));
		podDefault.put("memory", computeMemory(restriction.getPod_memory_default()));
		podDefault.put("cpu", computeCpu(restriction.getPod_cpu_default()));

		podLimitRangeItem.setDefaultVal(podDefault);
		podLimitRangeItem.setMax(podMax);
		podLimitRangeItem.setMin(podMin);

		//创建Container资源限制 LimitRangeItem
		containerLimitRangeItem.setType("Container");
		Map<String, String> containerMax = new HashMap<String, String>();
		Map<String, String> containerMin = new HashMap<String, String>();
		Map<String, String> containerDefault = new HashMap<String, String>();

		containerMax.put("memory", computeMemory(restriction.getContainer_memory_max()));
		containerMax.put("cpu", computeCpu(restriction.getContainer_cpu_max()));
		containerMin.put("memory", computeMemory(restriction.getContainer_memory_min()));
		containerMin.put("cpu", computeCpu(restriction.getContainer_cpu_min()));
		containerDefault.put("memory", computeMemory(restriction.getContainer_memory_default()));
		containerDefault.put("cpu", computeCpu(restriction.getContainer_cpu_default()));

		containerLimitRangeItem.setMax(containerMax);
		containerLimitRangeItem.setMin(containerMin);
		containerLimitRangeItem.setDefaultVal(containerDefault);

		limits.add(podLimitRangeItem);
		limits.add(containerLimitRangeItem);
		spec.setLimits(limits);
		limitRange.setSpec(spec);
		return limitRange;

	}

	/**
	 * <!-- 工具方法 -->
	 * 获取更新后的 ResourceQuota
	 *
	 * @param client
	 * @param username
	 * @param resource
	 * @return
	 */
	private ResourceQuota updateQuota(KubernetesAPIClientInterface client, String username, Resource resource) {
		ResourceQuota quota = client.getResourceQuota(username);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+quota);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+username);
		ResourceQuotaSpec spec = quota.getSpec();
		Map<String, String> hard = quota.getSpec().getHard();

		hard.put("memory", resource.getRam() + "G");    //内存
		hard.put("cpu", resource.getCpu_account() + "");//CPU数量
		hard.put("pods", resource.getPod_count() + "");//POD数量
		hard.put("services", resource.getServer_count() + "");//服务
		hard.put("replicationcontrollers", resource.getImage_control() + "");//副本控制器
		hard.put("resourcequotas", "1");//资源配额数量

		spec.setHard(hard);
		quota.setSpec(spec);

		return quota;
	}
}
