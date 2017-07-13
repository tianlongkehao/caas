package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.dao.ZookeeperDao;
import com.bonc.epm.paas.dao.ZookeeperImageDao;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Tensorflow;
import com.bonc.epm.paas.entity.TensorflowImage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.entity.Zookeeper;
import com.bonc.epm.paas.entity.ZookeeperImage;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
@RequestMapping(value = "/zookeeper")
public class ZookeeperController {

	private static final Logger LOG = LoggerFactory.getLogger(ZookeeperController.class);

	@Autowired
	private ServiceController serviceController;

	@Autowired
	private ZookeeperDao zookeeperDao;

	@Autowired
	private ZookeeperImageDao zookeeperImageDao;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private UserResourceDao userResourceDao;

	/**
	 * 主页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model){
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

        UserResource userResource = userResourceDao.findByUserId(user.getId());
        long restStorage = 0;
        if(userResource!=null){
        	restStorage = userResource.getVol_surplus_size();
        }

		List<Zookeeper> zookeepers = zookeeperDao.findByNamespace(user.getNamespace());
		List<ZookeeperImage> zookeeperImages = new ArrayList<ZookeeperImage>();
		Iterable<ZookeeperImage> iterable = zookeeperImageDao.findAll();
		if (null != iterable) {
			Iterator<ZookeeperImage> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				zookeeperImages.add(iterator.next());
			}
		}

		model.addAttribute("leftstorage", restStorage);
		model.addAttribute("images", zookeeperImages);
		model.addAttribute("zookeepers", zookeepers);
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "tensorflow");
		return "market/zookeeper.jsp";
	}

	/**
	 * 显示指定的zookeeper
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String showSpecifiedZookeeper(Model model,@PathVariable long id){
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

		return "market/zookeeper.jsp";
	}

	/**
	 * 新增
	 *
	 * @param zookeeper
	 * @return
	 */
	@RequestMapping(value = { "/add.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String addZookeeper(Zookeeper zookeeper) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

        zookeeper.setCreateDate(new Date());
        zookeeper.setCreateBy(user.getId());
        zookeeper.setNamespace(user.getNamespace());
		zookeeperDao.save(zookeeper);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 更新
	 *
	 * @return
	 */
	@RequestMapping(value = { "/update.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String updateZookeeper(Zookeeper zookeeper) {
		Map<String, Object> map = new HashMap<String, Object>();

		Zookeeper originalzookeeper = zookeeperDao.findOne(zookeeper.getId());
		originalzookeeper.setCpu(zookeeper.getCpu());
		originalzookeeper.setMemory(zookeeper.getMemory());
		originalzookeeper.setDetail(zookeeper.getDetail());
		originalzookeeper.setStarttimeout(zookeeper.getStarttimeout());
		originalzookeeper.setTimeoutdeadline(zookeeper.getTimeoutdeadline());
	    originalzookeeper.setSyntimeout(zookeeper.getSyntimeout());
	    originalzookeeper.setMaxnode(zookeeper.getMaxnode());
	    originalzookeeper.setMaxrequest(zookeeper.getMaxrequest());
		zookeeperDao.save(originalzookeeper);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/delete.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String deleteZookeeper(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		zookeeperDao.delete(id);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 获取信息
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/detail.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String detailZookeeper(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Zookeeper zookeeper = zookeeperDao.findOne(id);

		map.put("zookeeper", zookeeper);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 0：不存在，1：存在
	 *
	 * @param name
	 * @return
	 */
	@RequestMapping(value = { "/exist.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String zookeeperExist(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("exist", 0);

		User user = CurrentUserUtils.getInstance().getUser();
		long creator = user.getId();

		List<Service> services = serviceDao.findByNameOf(creator, name.trim());
		if (CollectionUtils.isNotEmpty(services)) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		Zookeeper zookeeper = zookeeperDao.findByNamespaceAndName(user.getNamespace(), name);
		if (zookeeper != null) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 启动
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/start.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String start(long id) {
		User user = CurrentUserUtils.getInstance().getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		Zookeeper zookeeper = zookeeperDao.findOne(id);
		zookeeper.setStatus(1);
		zookeeperDao.save(zookeeper);

		return JSON.toJSONString(map);
	}

	/**
	 * 停止
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/stop.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String stop(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		Zookeeper zookeeper = zookeeperDao.findOne(id);
		zookeeper.setStatus(0);
		zookeeperDao.save(zookeeper);

		return JSON.toJSONString(map);
	}

	/**
	 * 增加zookeeperimage
	 *
	 * @param name
	 * @param version
	 * @return
	 */
	@RequestMapping(value = { "/addImage.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String addImage(String name, String version) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		ZookeeperImage zookeeperImage = new ZookeeperImage();
		zookeeperImage.setName(name);
		zookeeperImage.setVersion(version);
		zookeeperImage.setCreateDate(new Date());
		zookeeperImageDao.save(zookeeperImage);

		return JSON.toJSONString(map);
	}
}
