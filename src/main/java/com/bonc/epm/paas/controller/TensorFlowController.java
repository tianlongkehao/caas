package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.TensorflowDao;
import com.bonc.epm.paas.dao.TensorflowImageDao;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Tensorflow;
import com.bonc.epm.paas.entity.TensorflowImage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
@RequestMapping(value = "/tensorflow")
public class TensorFlowController {

	@Autowired
	private ServiceController serviceController;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private TensorflowDao tensorflowDao;

	@Autowired
	private TensorflowImageDao tensorflowImageDao;

	/**
	 * 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

		List<Tensorflow> tensorflows = tensorflowDao.findByNamespace(user.getNamespace());
		List<TensorflowImage> tensorflowImages = new ArrayList<TensorflowImage>();
		Iterable<TensorflowImage> iterable = tensorflowImageDao.findAll();
        if (null!=iterable) {
			Iterator<TensorflowImage> iterator = iterable.iterator();
			while(iterator.hasNext()){
				tensorflowImages.add(iterator.next());
			}
		}

        model.addAttribute("images", tensorflowImages);
		model.addAttribute("tensorflows",tensorflows);
		model.addAttribute("username", user.getUserName());
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "tensorflow");
		return "service/tensorflow.jsp";
	}

	/**
	 * 新增
	 * @param tensorflow
	 * @return
	 */
	@RequestMapping(value = { "/add.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String addTensorFlow(Tensorflow tensorflow) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		tensorflow.setCreateDate(new Date());
		tensorflow.setNamespace(user.getNamespace());
		tensorflow.setCreateBy(user.getId());
		tensorflowDao.save(tensorflow);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 更新
	 * @return
	 */
	@RequestMapping(value = { "/update.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String updateTensorFlow() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/delete.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String deleteTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		tensorflowDao.delete(id);

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/detail.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String detailTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
        Tensorflow tensorflow = tensorflowDao.findOne(id);

        map.put("tensorflow", tensorflow);
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
	public String tensorFlowExist(String name) {
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

		Tensorflow tensorflow = tensorflowDao.findByNamespaceAndName(user.getUserName(), name.trim());
		if (tensorflow != null) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 启动
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/start.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String start(long id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

        Tensorflow tensorflow = tensorflowDao.findOne(id);
        tensorflow.setStatus(1);
        tensorflowDao.save(tensorflow);
		return JSON.toJSONString(map);
	}

	/**
	 * 停止
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/stop.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String stop(long id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		Tensorflow tensorflow = tensorflowDao.findOne(id);
        tensorflow.setStatus(0);
        tensorflowDao.save(tensorflow);
		return JSON.toJSONString(map);
	}

	/**
	 * 增加tensorflowimage
	 * @param name
	 * @param version
	 * @return
	 */
	@RequestMapping(value = { "/addImage.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String addImage(String name,String version){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");

		TensorflowImage tensorflowImage = new TensorflowImage();
		tensorflowImage.setName(name);
		tensorflowImage.setVersion(version);
		tensorflowImage.setCreateDate(new Date());
		tensorflowImageDao.save(tensorflowImage);

		return JSON.toJSONString(map);
	}
}
