package com.bonc.epm.paas.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.bonc.epm.paas.sso.casclient.CasClientConfigurationProperties;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.EncryptUtils;
import com.bonc.epm.paas.util.ServiceException;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	CasClientConfigurationProperties configProps;
	
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
	 * 生成图片验证码
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException 
	 * @see
	 */
	@RequestMapping(value={"authCode"},method=RequestMethod.GET)
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response,HttpSession session)
            throws IOException {
        int width = 100;
        int height = 37;
        Random random = new Random();
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman",0,28));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        for(int i=0;i<40;i++){
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }
        
        String[] codeSequence = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",        
                        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",        
                        "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }; 
        //绘制字符
        String strCode = "";
        for(int i=0;i<5;i++){
            String code = codeSequence[random.nextInt(36)];
            strCode += code;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(code, 13*i+6, 28);
        }
        //将字符保存到session中用于前端的验证
        request.getSession().setAttribute("strCode", strCode);
        g.dispose();

        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();
    }

    public Color getRandColor(int fc,int bc){
        Random random = new Random();
        if(fc>255)
            fc = 255;
        if(bc>255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
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
	public String login(User user, RedirectAttributes redirect,String authCode,HttpServletRequest request){
		try {
		    String judgeCode = (String)request.getSession().getAttribute("strCode");
		    if (!judgeCode.equals(authCode.toUpperCase())) {
		        throw new ServiceException("验证码输入错误");
		    }
			user = login(user);
		} catch (ServiceException e) {
			log.debug(e.getMessage());
			redirect.addFlashAttribute("err_code", e.getMessage());
			redirect.addFlashAttribute("user", user);
			return "redirect:login";
		}
		CurrentUserUtils.getInstance().setUser(user);
		CurrentUserUtils.getInstance().setCasEnable(configProps.getEnable());
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
		String password = EncryptUtils.encryptMD5(user.getPassword());
//		String password = user.getPassword();
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
		user.setPassword(EncryptUtils.encryptMD5("admin"));
		user.setUser_autority("1");
		user.setVol_size(0);
		if(userDao.findByUserName(user.getUserName()) == null) {
			userDao.save(user);
		}
		log.info("User init success:"+user.toString());
	}
	
//	public static void main(String[] args)
//    {
//        String admin = EncryptUtils.encryptMD5("bonc1111");
//        System.out.println(admin);
//        System.out.println(EncryptUtils.encryptMD5(admin));
//    }
}
