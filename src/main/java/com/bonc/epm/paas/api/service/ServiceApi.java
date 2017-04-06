/**
 *
 */
package com.bonc.epm.paas.api.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.Service;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/api/v1")
public class ServiceApi {

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
	 * 镜像数据接口
	 */
	@Autowired
	private ImageDao imageDao;

	/**
	 * portConfig数据层接口
	 */
	@Autowired
	private PortConfigDao portConfigDao;

	/**
	 * Description: <br>
	 * services 获取所有服务
	 *
	 * @return String
	 */
	@RequestMapping(value = { "/services" }, method = RequestMethod.GET)
	@ResponseBody
	public String services() {
		Iterable<Service> result = serviceDao.search("%", "%", "%");
		Iterator<Service> it = result.iterator();
		List<Service> serviceList = fillServiceInfo(it);
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
		Iterable<Service> result = serviceDao.search("%", "%", (user != null ? user : ""));
		Iterator<Service> it = result.iterator();
		List<Service> serviceList = fillServiceInfo(it);
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
	public String userServices2(@PathVariable String user, @PathVariable String services) {
		Iterable<Service> result = serviceDao.search((services != null ? services : ""), "%",
				(user != null ? user : ""));
		Iterator<Service> it = result.iterator();
		List<Service> serviceList = fillServiceInfo(it);
		return JSON.toJSONString(serviceList);
	}

	private List<Service> fillServiceInfo(Iterator<Service> it) {
		List<Service> serviceList = new ArrayList<>();
		while (it.hasNext()) {
			Service service = it.next();
			service.setCreatorName(userDao.findById(service.getCreateBy()).getUserName());
			List<PortConfig> ports = portConfigDao.findByServiceId(service.getId());
			if (CollectionUtils.isNotEmpty(ports)) {
				service.setPortConfigs(ports);
			}
//			Image image = imageDao.findById(service.getImgID());
//			service.setCodeRating(1);
//			service.setCodeRatingURL("http://test.com");
			serviceList.add(service);
		}
		return serviceList;
	}
}
