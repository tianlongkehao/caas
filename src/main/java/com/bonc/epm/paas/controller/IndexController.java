package com.bonc.epm.paas.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.EncryptUtils;
import com.bonc.epm.paas.util.ServiceException;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private HttpSession session;
	
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

    @RequestMapping(value={"signinworkbench"},method=RequestMethod.POST)
    public String signIn(@RequestParam String userName, @RequestParam String password){

        System.out.println("username;-----------" + userName);
        System.out.println("password;-----------" + password);

        return "workbench.jsp";
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
	public String login(User user, RedirectAttributes redirect,Model model){
		try {
			user = login(user);
		} catch (ServiceException e) {
			log.debug(e.getMessage());
			redirect.addFlashAttribute("err_code", e.getMessage());
			redirect.addFlashAttribute("user", user);
			return "redirect:login";
		}
		
		CurrentUserUtils.getInstance().serUser(user);
		model.addAttribute("user", user);
		return "redirect:workbench";
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
		
		User  userEntity = new User();
		
		if(userDao.findByUserName(user.getUserName()) != null){
			userEntity = userDao.findByUserName(user.getUserName());
		}
		
		if(null == userEntity){
			throw new ServiceException("用户名不存在");
		}
		
		//String password = EncryptUtils.encryptMD5(user.getPassword());
		String password = user.getPassword();
		if(!StringUtils.equals(password, userEntity.getPassword())){
			throw new ServiceException("密码输入错误");
		}
		
		return userEntity;
	}

}
