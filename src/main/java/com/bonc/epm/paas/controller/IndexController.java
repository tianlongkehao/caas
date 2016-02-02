package com.bonc.epm.paas.controller;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.ServiceException;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private  UserDao userDao;
	/**
	 * 跳转登录页面
	 * @author lance
	 * 2014-6-8下午6:49:40
	 * @return
	 */
	@RequestMapping(value={"login"},method=RequestMethod.GET)
	public String login(){
		return "login.jsp";
	}

    /**
	 * 登录成功后跳转页面
	 * @author fengtao
	 * 2015-12-22
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value="signin",method=RequestMethod.POST)
	public String login(User user, RedirectAttributes redirect){
		try {
			user = login(user);
		} catch (ServiceException e) {
			log.debug(e.getMessage());
			redirect.addFlashAttribute("err_code", e.getMessage());
			redirect.addFlashAttribute("user", user);
			return "redirect:login";
		}
		CurrentUserUtils.getInstance().setUser(user);
		redirect.addFlashAttribute("user", user);
		return "redirect:workbench";
	}


	
	@RequestMapping(value={"loginout/{id}"},method=RequestMethod.GET)
	public String loginOut(Model model ,@PathVariable long id){
		User user = userDao.findById(id);
		CurrentUserUtils.getInstance().loginoutUser(user);
		return "redirect:login";
	}
	
	@RequestMapping(value={"index","/"},method=RequestMethod.GET)
	public String index(){
		return "index.jsp";
	}

	@RequestMapping(value={"workbench"},method=RequestMethod.GET)
	public String workbench(){
		return "workbench.jsp";
	}

	@RequestMapping(value={"menu"},method=RequestMethod.GET)
	public String menu(Model model,String flag){
		model.addAttribute("flag", flag);
		return "menu.jsp";
	}
	
	public User login(User user) {
		if(StringUtils.isBlank(user.getUserName())) {
			throw new ServiceException("用户名不能为空");
		}
		if(StringUtils.isBlank(user.getPassword())) {
			throw new ServiceException("密码不能为空");
		}
		User userEntity = userDao.findByUserName(user.getUserName());
		log.debug("user.getUserName()="+user.getUserName());
		if(userEntity == null){
			throw new ServiceException("用户名不存在");
		}
		log.debug("userEntity="+userEntity.toString());
		//String password = EncryptUtils.encryptMD5(user.getPassword());
		String password = user.getPassword();
		log.debug("password="+password);
		log.debug("userEntity.getPassword()="+userEntity.getPassword());
		if(!StringUtils.equals(password, userEntity.getPassword())){
			throw new ServiceException("密码输入错误");
		}
		return userEntity;
	}

	/**
	 * 初始化一个User
	 */
	@PostConstruct
	public void init(){
		User user = new User();
		user.setUserName("admin");
		user.setPassword("admin");
		user.setUser_autority("1");
		if(userDao.findByUserName(user.getUserName()) == null) {
			userDao.save(user);
		}
		log.info("User init success:"+user.toString());
	}
}
