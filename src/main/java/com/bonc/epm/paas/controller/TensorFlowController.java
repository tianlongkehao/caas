package com.bonc.epm.paas.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;

import io.swagger.models.Model;

@Controller
@RequestMapping(value = "/tensorflow")
public class TensorFlowController {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index(Model model) {

		return "service/tensorflow.jsp";
	}

	@RequestMapping(value = { "/add.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String addTensorFlow() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	@RequestMapping(value = { "/update.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateTensorFlow() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	@RequestMapping(value = { "/delete.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String deleteTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	@RequestMapping(value = { "/detail.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String detailTensorFlow(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = CurrentUserUtils.getInstance().getUser();

		map.put("status", "200");
		return JSON.toJSONString(map);
	}
}
