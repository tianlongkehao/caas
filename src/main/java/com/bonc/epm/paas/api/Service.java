/**
 *
 */
package com.bonc.epm.paas.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserDao;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/api/v1")
public class Service {

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
	 * Description: <br>
	 * services 获取所有服务
	 *
	 * @return String
	 */
	@RequestMapping(value = { "/services" }, method = RequestMethod.GET)
	@ResponseBody
	public String services() {
		List<com.bonc.epm.paas.entity.Service> serviceList = serviceDao.search("%", "%", "%", null).getContent();
		for (com.bonc.epm.paas.entity.Service service : serviceList) {
			service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
		}
		return JSON.toJSONString(serviceList);

	}

	/**
	 * Description: <br>
	 * userServices 按用户名检索服务
	 *
	 * @param user
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/services" }, method = RequestMethod.GET)
	@ResponseBody
	public String userServices(@PathVariable String user) {
		List<com.bonc.epm.paas.entity.Service> serviceList = serviceDao
				.search("%", "%", (user != null ? user : ""), null).getContent();
		for (com.bonc.epm.paas.entity.Service service : serviceList) {
			service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
		}
		return JSON.toJSONString(serviceList);

	}

	/**
	 * Description: <br>
	 * getPortConfig 按用户名和服务名检索服务
	 *
	 * @param user
	 * @param services
	 * @return String
	 */
	@RequestMapping(value = { "/user/{user}/services/{services}" }, method = RequestMethod.GET)
	@ResponseBody
	public String userServices(@PathVariable String user, @PathVariable String services) {
		List<com.bonc.epm.paas.entity.Service> serviceList = serviceDao
				.search((services != null ? services : ""), "%",(user != null ? user : ""),
						null)
				.getContent();
		for (com.bonc.epm.paas.entity.Service service : serviceList) {
			service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
		}
		return JSON.toJSONString(serviceList);

	}

}
