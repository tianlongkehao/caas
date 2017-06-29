package com.bonc.epm.paas.controller;

import java.util.HashMap;
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
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.TensorflowDao;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.Tensorflow;
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {
		User user = CurrentUserUtils.getInstance().getUser();

		if (!user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
			serviceController.getleftResource(model);
		}

		model.addAttribute("username", user.getUserName());
		model.addAttribute("menu_flag", "service");
		model.addAttribute("li_flag", "tensorflow");
		return "service/tensorflow.jsp";
	}

	@RequestMapping(value = { "/add.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String addTensorFlow(Tensorflow tensorflow) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	@RequestMapping(value = { "/update.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String updateTensorFlow() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	@RequestMapping(value = { "/delete.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String deleteTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	@RequestMapping(value = { "/detail.do" }, method = RequestMethod.POST)
	@ResponseBody
	public String detailTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

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
		String tensorflowname = user.getUserName() + "-" + name.trim();

		List<Service> services = serviceDao.findByNameOf(creator, tensorflowname);
		if (CollectionUtils.isNotEmpty(services)) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		Tensorflow tensorflow = tensorflowDao.findByNamespaceAndName(user.getUserName(), tensorflowname);
		if (tensorflow != null) {
			map.put("exist", 1);
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}
}
