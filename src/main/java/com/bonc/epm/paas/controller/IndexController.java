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
import org.springframework.beans.factory.annotation.Value;
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

/**
 * 登录
 * @author update
 * @version 2016年9月5日
 * @see IndexController
 * @since
 */
@Controller
public class IndexController {
    
    /**
     * IndexController 日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
    
    /**
     *  configProps 接口
     */
    @Autowired
	private CasClientConfigurationProperties configProps;
	
    /**
     * userDao接口
     */
    @Autowired
	private UserDao userDao;
    @Value("${login.showAuthCode}")
    private boolean showAuthCode;
    /**
     * Description: <br>
     * 跳转登录页面
     * @return login.jsp
     */
    @RequestMapping(value={"bcm"},method=RequestMethod.GET)
	public String bcm(Model model){
        model.addAttribute("showAuthCode", showAuthCode);
        model.addAttribute("menu_flag", "bcm");
        return "bcm-pandect.jsp";
    }
    /**
     * Description: <br>
     * 跳转登录页面
     * @return login.jsp
     */
    @RequestMapping(value={"login"},method=RequestMethod.GET)
	public String login(Model model){
        model.addAttribute("showAuthCode", showAuthCode);
        return "login.jsp";
    }
	
    /**
     * Description: <br>
     * 生成图片验证码
     * @param request request
     * @param response response
     * @param session session
     * @throws IOException  IOException
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
        
        String[] codeSequence = { "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9" }; 
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
    
    /**
     * Description: <br>
     * 生成随机颜色
     * @param fc fc
     * @param bc bc
     * @return color
     */
    public Color getRandColor(int fc,int bc){
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
    }
    
    /**
     * Description: <br>
     * 登录成功后跳转页面
     * @param user user
     * @param redirect redirect
     * @param authCode authCode
     * @param request request
     * @return String
     * @see
     */
    @RequestMapping(value="signin",method=RequestMethod.POST)
	public String login(User user, RedirectAttributes redirect,String authCode,HttpServletRequest request){
        try {
            if (showAuthCode) {
                String judgeCode = (String)request.getSession().getAttribute("strCode");
                if (!judgeCode.equals(authCode.toUpperCase())) {
                    throw new ServiceException("验证码输入错误");
                } 
            }
            user = login(user);
        }
        catch (ServiceException e) {
            LOG.debug(e.getMessage());
            redirect.addFlashAttribute("err_code", e.getMessage());
            redirect.addFlashAttribute("user", user);
            return "redirect:login";
        }
        CurrentUserUtils.getInstance().setUser(user);
        CurrentUserUtils.getInstance().setCasEnable(configProps.getEnable());
        redirect.addFlashAttribute("user", user);
        return "redirect:workbench";
    }
    
    /**
     * Description: <br>
     * 退出登录
     * @param model  添加返回信息
     * @param id 当前登录Id
     * @return String
     */
    @RequestMapping(value={"loginout/{id}"},method=RequestMethod.GET)
	public String loginOut(Model model ,@PathVariable long id){
        User user = userDao.findById(id);
        CurrentUserUtils.getInstance().loginoutUser(user);
        return "redirect:login";
    }
	
    /**
     * Description: <br>
     * 跳转进入index.jsp页面
     * @return  index.jsp
     */
    @RequestMapping(value={"index","/"},method=RequestMethod.GET)
	public String index(){
        return "index.jsp";
    }
    
    /**
     * Description: <br>
     * 跳转进入workbench.jsp页面
     * @return workbench.jsp
     */
    @RequestMapping(value={"workbench"},method=RequestMethod.GET)
	public String workbench(){
        return "workbench.jsp";
    }
    
    /**
     * Description: <br>
     * 跳转进入menu.jsp页面
     * @param model 添加返回信息数据
     * @param flag flag
     * @return menu.jsp
     */
    @RequestMapping(value={"menu"},method=RequestMethod.GET)
	public String menu(Model model,String flag){
        model.addAttribute("flag", flag);
        return "menu.jsp";
    }
	
    /**
     * Description: <br>
     * 用户登录
     * @param user user
     * @return user
     */
    public User login(User user) {
        if (StringUtils.isBlank(user.getUserName())) {
            throw new ServiceException("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new ServiceException("密码不能为空");
        }
        User userEntity = userDao.findByUserName(user.getUserName());
        LOG.debug("user.getUserName()="+user.getUserName());
        if (userEntity == null) {
            throw new ServiceException("用户名不存在");
        }
        LOG.debug("userEntity="+userEntity.toString());
        String password = EncryptUtils.encryptMD5(user.getPassword());
        LOG.debug("password="+password);
        LOG.debug("userEntity.getPassword()="+userEntity.getPassword());
        if (!StringUtils.equals(password, userEntity.getPassword())) {
            throw new ServiceException("密码输入错误");
        }
        return userEntity;
    }
    
    /**
     * 
     * Description: <br>
     *  初始化admin
     */
    @PostConstruct
	public void init(){
        User user = new User();
        user.setUserName("admin");
        user.setPassword(EncryptUtils.encryptMD5("admin"));
        user.setUser_autority("1");
        user.setVol_size(0);
        if (userDao.findByUserName(user.getUserName()) == null) {
            userDao.save(user);
        }
        LOG.info("User init success:"+user.toString());
    }
	
//	public static void main(String[] args)
//    {
//        String admin = EncryptUtils.encryptMD5("paas1234");
//        System.out.println(admin);
//       System.out.println(EncryptUtils.encryptMD5(admin));
//    }
}
