package com.bonc.epm.paas.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author Administrator
 *
 */
@Component
public class RESTResourceClient {

	private RestTemplate template = new RestTemplate();

	private final static String url = "http://10.0.93.174:8088/resreq/restlet/";

	public String show() {
		return template.getForObject(url + "show.do", String.class, "");
	}

	public Object getResById(String id) {
		return template.getForObject(url + "resourceArchive/showAllResource?param={tenant_id:" + id + "}", String.class,
				"");
	}

	public Object getResByName(String name) {
		return template.getForObject(url + "getByName/{name}.do", String.class, name);
	}

	public String addResr(String user) {
		return template.postForObject(url + "add.do?user={user}", null, String.class, user);
	}

	public String editRes(String user) {
		template.put(url + "edit.do?user={user}", null, user);
		return user;
	}

	public String removeUser(String id) {
		template.delete(url + "/remove/{id}.do", id);
		return id;
	}
}