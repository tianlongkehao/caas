package com.bonc.epm.paas.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.ServiceConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.ServiceOperationLogDao;
import com.bonc.epm.paas.dao.SheraDao;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.dao.UserVisitingLogDao;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.ServiceOperationLog;
import com.bonc.epm.paas.entity.Shera;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserInfo;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.entity.UserVisitingLog;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaList;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
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

    /**
     * userResourceDao
     */
    @Autowired
    private UserResourceDao userResourceDao;

    /**
     * imageDao
     */
    @Autowired
    private ImageDao ImageDao;

    /**
     * 记录访问日志dao接口
     */
    @Autowired
    private UserVisitingLogDao userVisitingLogDao;

    @Value("${login.showAuthCode}")
    private boolean showAuthCode;
    /**
     * KubernetesClientService
     */
    @Autowired
	private KubernetesClientService kubernetesClientService;
    /**
     * StorageDao
     */
    @Autowired
	private StorageDao storageDao;

    /**
     * sheraDao
     */
    @Autowired
    private SheraDao sheraDao;
    /**
     * 内存和cpu的比例大小
     */
    @Value("${ratio.memtocpu}")
    private String RATIO_MEMTOCPU = "4";

    /**
     * service操作日志dao接口
     */
    @Autowired
    private ServiceOperationLogDao serviceOperationLogDao;

    /**
     * 通用操作日志dao接口
     */
    @Autowired
	private CommonOperationLogDao commonOperationLogDao;


    /**
     * Description: <br>
     * 首页
     * @return home.jsp
     */
    @RequestMapping(value={"home"},method=RequestMethod.GET)
	public String home(Model model){
        return "home.jsp";
    }

	/**
	 * Description: <br>
	 * 总览页面
	 *
	 * @return bcm-pandect.jsp
	 */
	@RequestMapping(value = { "bcm/{id}" }, method = RequestMethod.GET)
	public String bcm(Model model, @PathVariable long id) {

		try {
			User user = userDao.findOne(id);
			if (!("1".equals(user.getUser_autority()))) {
				// 以用户名(登陆帐号)为name，创建client，查询以登陆名命名的 namespace 资源详情
				KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
				Namespace ns = client.getNamespace(user.getNamespace());
				if (null != ns) {
					getUserResourceInfo(model, user, client);
				} else {
					LOG.info("用户 " + user.getUserName() + " 还没有定义服务！");
				}

				double usedstorage = 0;
				List<Storage> list = storageDao.findByCreateBy(CurrentUserUtils.getInstance().getUser().getId());
				for (Storage storage : list) {
					usedstorage = usedstorage + (double) storage.getStorageSize();
				}
				Shera shera = sheraDao.findByUserId(id);
				model.addAttribute("userShera", shera);
				model.addAttribute("usedstorage", usedstorage / 1024);
			} else {
				//获取所有租户的信息
				List<User> userList = userDao.findAllTenant();
				List<UserInfo> userInfos = new ArrayList<>();
				for (User user2 : userList) {
					userInfos.add(getUserInfo(user2));
				}
				model.addAttribute("userInfos", userInfos);

				KubernetesAPIClientInterface client = kubernetesClientService.getClient(KubernetesClientService.adminNameSpace);
				//获取所有node
				NodeList allNodes = client.getAllNodes();
				double cpuCount = 0;
				double memoryCount = 0;
				for (Node node : allNodes.getItems()) {
					Map<String, String> capacity = node.getStatus().getCapacity();
					cpuCount += Float.valueOf(this.computeCpuOut(capacity)) * Integer.valueOf(RATIO_MEMTOCPU);
					memoryCount += Float.valueOf(this.computeMemoryOut(capacity));
				}
				//获取所有quota
				double usedCpuCount = 0;
				double usedMemoryCount = 0;
				java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");
				ResourceQuotaList allResourceQuotas = client.getAllResourceQuotas();
				for (ResourceQuota resourceQuota : allResourceQuotas.getItems()) {
					Map<String, String> used = resourceQuota.getStatus().getUsed();
					usedCpuCount += Float.valueOf(this.computeCpuOut(used)) * Integer.valueOf(RATIO_MEMTOCPU);
					usedMemoryCount += Float.valueOf(this.computeMemoryOut(used));
				}
				model.addAttribute("cpuCount", df.format(cpuCount));
				model.addAttribute("memoryCount", df.format(memoryCount));
				model.addAttribute("usedCpuCount", df.format(usedCpuCount));
				model.addAttribute("usedMemoryCount", df.format(usedMemoryCount));
			}

			// ---------最近操作---------------

			List<ServiceOperationLog> svcLogs = serviceOperationLogDao.findFourByCreateBy(id,
					new PageRequest(0, 4, Direction.DESC, "createDate"));
			List<CommonOperationLog> commonLogs = commonOperationLogDao.findFourByCreateBy(id,
					new PageRequest(0, 4, Direction.DESC, "createDate"));
			Map<Date, String> map = new HashMap<Date, String>();
			for (ServiceOperationLog serviceOperationLog : svcLogs) {
				String oprationRecord = "对服务: " + serviceOperationLog.getServiceName() + " 进行了"
						+ ServiceConstant.OPERATION_TYPE_MAP.get(serviceOperationLog.getOperationType()) + "操作";
				map.put(serviceOperationLog.getCreateDate(), oprationRecord);
			}
			for (CommonOperationLog commonOperationLog : commonLogs) {
				String oprationRecord = "在" + CommConstant.CatalogType_MAP.get(commonOperationLog.getCatalogType())
						+ "模块进行了" + CommConstant.OPERATION_TYPE_MAP.get(commonOperationLog.getOperationType()) + "操作";
				map.put(commonOperationLog.getCreateDate(), oprationRecord);
			}

			// 根据时间进行排序
			// 将Map转化为List集合，List采用ArrayList
			List<Map.Entry<Date, String>> list_Data = new ArrayList<Map.Entry<Date, String>>(map.entrySet());

			// 通过Collections.sort(List I,Comparator c)方法进行排序
			Collections.sort(list_Data, new Comparator<Map.Entry<Date, String>>() {

				@Override
				public int compare(Entry<Date, String> o1, Entry<Date, String> o2) {
					if (o1.getKey().before(o2.getKey())) {// o1比o2早
						return 1;
					}
					return -1;
				}

			});

			Iterator<Map.Entry<Date, String>> it = list_Data.iterator();
			while (it.hasNext()) {
				Entry<Date, String> a = it.next();
				if (a.getKey().before(list_Data.get(3).getKey())) {
					it.remove();
				}
			}
			System.out.println(list_Data);
			model.addAttribute("list_Data", list_Data);
			// ------------------------

		} catch (KubernetesClientException e) {
			LOG.error(e.getMessage() + ":" + JSON.toJSON(e.getStatus()));
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("error message:-" + e.getMessage());
			e.printStackTrace();
		}

		model.addAttribute("showAuthCode", showAuthCode);

		model.addAttribute("menu_flag", "bcm");
		return "bcm-pandect.jsp";
	}
    /**
     *
     * Description:
     * 获取用户的资源使用信息
     * @param model
     * @param user
     * @param client
     * @see
     */
    private void getUserResourceInfo(Model model, User user, KubernetesAPIClientInterface client) {
        ResourceQuota quota = client.getResourceQuota(user.getNamespace());
        if (null != quota) {
        	UserResource userResource = new UserResource();
        	if (user.getUser_autority().equals(UserConstant.AUTORITY_USER)){
                userResource = userResourceDao.findByUserId(user.getParent_id());
            }
            else {
                userResource = userResourceDao.findByUserId(user.getId());
            }
            model.addAttribute("userResource", userResource);
            model.addAttribute("user", user);

            Integer imageCount = ImageDao.findByCreateBy(user.getId()).size();

            model.addAttribute("imageCount", imageCount);

            Map<String, String> hard = quota.getStatus().getHard();
            model.addAttribute("servCpuNum", kubernetesClientService.transCpu(hard.get("cpu")) * Integer.valueOf(RATIO_MEMTOCPU)); // cpu个数
            model.addAttribute("servMemoryNum", hard.get("memory").replace("i", "").replace("G", ""));// 内存个数
            model.addAttribute("servPodNum", hard.get("pods"));// pod个数
            model.addAttribute("servServiceNum", hard.get("services")); // 服务个数
            model.addAttribute("servControllerNum", hard.get("replicationcontrollers"));// 副本控制数

            Map<String, String> used = quota.getStatus().getUsed();
            ReplicationControllerList rcList = client.getAllReplicationControllers();
            PodList podList = client.getAllPods();
            model.addAttribute("usedCpuNum", Float.valueOf(this.computeCpuOut(used)) * Integer.valueOf(RATIO_MEMTOCPU)); // 已使用CPU个数
            model.addAttribute("usedMemoryNum", Float.valueOf(this.computeMemoryOut(used)));// 已使用内存
            model.addAttribute("usedPodNum", (null != podList) ? podList.size() : 0); // 已经使用的POD个数
            model.addAttribute("usedServiceNum", (null !=rcList) ? rcList.size() : 0);// 已经使用的服务个数
            // model.addAttribute("usedControllerNum", usedControllerNum);
        } else {
            LOG.info("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
        }
    }
    /**
     *
     * Description:
     * computeCpuOut
     * @param val Map<String, String> val
     * @return cpuVal String
     * @see
     */
    private String computeCpuOut(Map<String, String> val) {
        String cpuVal = val.get("cpu");
        if (cpuVal.contains("m")) {
            Float a1 = Float.valueOf(cpuVal.replace("m", "")) / 1000;
            return a1.toString();
        }
        else {
            return cpuVal;
        }
    }
    /**
     *
     * Description:
     * computeMemoryOut
     * @param val Map<String, String>
     * @return memVal String
     * @see
     */
	private String computeMemoryOut(Map<String, String> val) {
		String memVal = val.get("memory");
		if (memVal.contains("Ki")) {
			Float a1 = Float.valueOf(memVal.replace("Ki", "")) / 1024 / 1024;
			return a1.toString();
		} else if (memVal.contains("Mi")) {
			Float a1 = Float.valueOf(memVal.replace("Mi", "")) / 1024;
			return a1.toString();
		} else {
			return memVal.replace("Gi", "");
		}
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
     * 跳转镜像广场
     * @return imageShow.jsp
     */
    @RequestMapping(value={"imageShow"},method=RequestMethod.GET)
	public String imageShow(Model model){
        //model.addAttribute("showAuthCode", showAuthCode);
        return "imageShow.jsp";
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
        String hostIp = request.getRemoteAddr();
        String headerData = request.getHeader("User-agent");
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
            addUserVisitingLog(user, hostIp, headerData, false);
            return "redirect:login";
        }
        CurrentUserUtils.getInstance().setUser(user);
        CurrentUserUtils.getInstance().setCasEnable(configProps.getEnable());
        redirect.addFlashAttribute("user", user);

        addUserVisitingLog(user, hostIp, headerData, true);
        return "redirect:home";
    }

    /**
     * Description: <br>
     * 添加访问记录日志
     * @param user user
     * @param hostIp ip
     * @param browser 浏览器
     * @param isLegal 是否合法
     * @see
     */
    public void addUserVisitingLog (User user,String hostIp,String headerData,boolean isLegal) {
        UserVisitingLog userVisitingLog = new UserVisitingLog();
        userVisitingLog = userVisitingLog.addUserVisitingLog(user, hostIp, headerData, isLegal);
        userVisitingLogDao.save(userVisitingLog);
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
//        user.setVol_size(0);
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

	/**
	 *
	 * Description: 获取用户的资源使用信息
	 *
	 * @param user
	 * @see
	 */
	private UserInfo getUserInfo(User user) {
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(user.getNamespace());
		UserInfo userInfo = new UserInfo();
		Namespace ns = client.getNamespace(user.getNamespace());
		if (null == ns) {
			LOG.info("用户 " + user.getUserName() + " 还没有定义服务！");
			return userInfo;
		}

		ResourceQuota quota = null;
		try {
			quota = client.getResourceQuota(user.getNamespace());
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			return userInfo;
		}

		if (null != quota) {
			UserResource userResource = new UserResource();
			if (user.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
				userResource = userResourceDao.findByUserId(user.getParent_id());
			} else {
				userResource = userResourceDao.findByUserId(user.getId());
			}
			userInfo.setUserResource(userResource);
			userInfo.setUser(user);

			Integer imageCount = ImageDao.findByCreateBy(user.getId()).size();

			userInfo.setImageCount(imageCount);

			Map<String, String> hard = quota.getStatus().getHard();
			userInfo.setServCpuNum(kubernetesClientService.transCpu(hard.get("cpu")) * Integer.valueOf(RATIO_MEMTOCPU)); // cpu个数
			userInfo.setServMemoryNum(hard.get("memory").replace("i", "").replace("G", ""));// 内存个数
			userInfo.setServPodNum(hard.get("pods"));// pod个数
			userInfo.setServServiceNum(hard.get("services")); // 服务个数
			userInfo.setServControllerNum(hard.get("replicationcontrollers"));// 副本控制数

			Map<String, String> used = quota.getStatus().getUsed();
			ReplicationControllerList rcList = client.getAllReplicationControllers();
			PodList podList = client.getAllPods();
			userInfo.setUsedCpuNum(Float.valueOf(this.computeCpuOut(used)) * Integer.valueOf(RATIO_MEMTOCPU)); // 已使用CPU个数
			userInfo.setUsedMemoryNum(Float.valueOf(this.computeMemoryOut(used)));// 已使用内存
			userInfo.setUsedPodNum((null != podList) ? podList.size() : 0); // 已经使用的POD个数
			userInfo.setUsedServiceNum((null != rcList) ? rcList.size() : 0);// 已经使用的服务个数
			double usedstorage = 0;
			List<Storage> list = storageDao.findByCreateBy(user.getId());
			for (Storage storage : list) {
				usedstorage = usedstorage + (double) storage.getStorageSize();
			}
			Shera shera = sheraDao.findByUserId(user.getId());
			userInfo.setUserShera(shera);
			userInfo.setUsedStorage(usedstorage / 1024);

		} else {
			LOG.info("用户 " + user.getUserName() + " 没有定义名称为 " + user.getNamespace() + " 的Namespace ");
		}
		return userInfo;
	}

}
