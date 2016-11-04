package com.bonc.epm.paas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.dao.FavorDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserFavorImages;
import com.bonc.epm.paas.util.CmdUtil;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.ResultPager;

/**
 * 
 * 展示和操作镜像
 * @author zhoutao
 * @version 2016年9月6日
 * @see RegistryController
 * @since
 */
@Controller
public class RegistryController {
    
    /**
     * RegistryController 日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(RegistryController.class);
	
	/**
	 * ImageDao接口
	 */
    @Autowired
	private ImageDao imageDao;
    
    /**
     * UserDao接口
     */
    @Autowired
	private UserDao userDao;
	
	/**
	 * 用户收藏镜像操作接口
	 */
    @Autowired
	private FavorDao favorDao;
	
    /**
     * DockerClientService 接口
     */
    @Autowired
	private DockerClientService dockerClientService;
	
    /**
     * 获取docker.image.url的路径信息
     */
    @Value("${docker.image.url}")
    private String url;
		
    /**
     * 获取paas.image.path中的镜像下载地址
     */
    @Value("${paas.image.path}")
	private String imagePath = "../downimage";
    
    /**
     * 获取save image 命令前缀
     */
    @Value("${docker.image.cmdpath}")
    private String imageCmdPath;
    
    /**
     * Description: <br>
     * 进入镜像显示页面，镜像显示分三层分隔显示
     * @param index 显示哪一层的镜像
     * @param model 添加返回数据
     * @return String
     */
    @RequestMapping(value = {"registry/{index}"}, method = RequestMethod.GET)
    public String index(@PathVariable int index, Model model) {
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        String active = null;
        if(index == 0){
            active = "镜像中心";
        }else if(index == 1){
            active = "我的镜像";
        }else if(index == 2){
            active = "我的收藏";
        }
        
        model.addAttribute("menu_flag", "registry");
        model.addAttribute("index", index);
        model.addAttribute("active",active);
        model.addAttribute("userId",userId);

        return "docker-registry/registry.jsp";
    }
    
    /**
     * Description: <br>
     * 使用datatable对镜像进行服务端分页操作；
     * @param index :镜像的级别；
     * @param draw ：画板；
     * @param start ：开始页数；
     * @param length : 每页的长度；
     * @param request ：接受搜索参数；
     * @return String
     */
    @RequestMapping(value = {"registry/pager/{index}"}, method = RequestMethod.GET)
    @ResponseBody
    public String findByPagerImage(@PathVariable int index,String draw, int start,int length,
                                   HttpServletRequest request ){
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        String search = request.getParameter("search[value]");
        Map<String,Object> map = new HashMap<String, Object>();
        PageRequest pageRequest = null;
        Page<Image> images = null;
        //判断事第几页
        if (start == 0) {
            pageRequest = ResultPager.buildPageRequest(null, length);
            
        }else {
            pageRequest = ResultPager.buildPageRequest(start/length + 1, length);
        }
        //判断是否需要搜索镜像
        if (StringUtils.isEmpty(search)) {
            if(index == 0){
                images = imageDao.findByImageType(userId,pageRequest);
            }else if(index == 1){
                images = imageDao.findAllByCreator(userId,pageRequest);
            }else if(index == 2){
                images = imageDao.findAllFavor(userId,pageRequest);
            }
        } else {
            if(index == 0){
                images = imageDao.findByNameCondition("%"+search+"%",userId,pageRequest);
            }else if(index == 1){
                images = imageDao.findByNameOfUser(userId,"%"+search+"%",pageRequest);
            }else if(index == 2){
                images = userDao.findByNameCondition(userId,"%"+search+"%",pageRequest);
            }
        }
        addCurrUserFavor(images.getContent());
        addCreatorName(images.getContent());
        map.put("draw", draw);
        map.put("recordsTotal", images.getTotalElements());
        map.put("recordsFiltered", images.getTotalElements());
        map.put("data", images.getContent());
        
        return JSON.toJSONString(map);
    }
    
//    @RequestMapping(value = {"registry/{index}"}, method = RequestMethod.GET)
//	public String index(@PathVariable int index, Model model) {
//        List<Image> images = null;
//        String active = null;
//        long userId = CurrentUserUtils.getInstance().getUser().getId();
//        if (index == 0) {
//            images = imageDao.findByImageType(1);
//            active = "镜像中心";
//        } 
//        else if (index == 1) {
//            images = imageDao.findAllByCreator(userId);
//            active = "我的镜像";
//        }
//        else if(index == 2){
//            images = userDao.findAllFavor(userId);
//            active = "我的收藏";
//        }
//        addCurrUserFavor(images);
//        addCreatorName(images);
//        model.addAttribute("images", images);
//        model.addAttribute("index", index);
//        model.addAttribute("active",active);
//        model.addAttribute("editImage",userId);
//        model.addAttribute("menu_flag", "registry");
//        return "docker-registry/registry.jsp";
//    }
	
	/**
	 * 镜像搜索
	 * @param index 需要搜索哪一层的镜像
	 * @param imageName 搜索镜像的名称
	 * @param model 添加返回页面的数据
	 * @return String 
	 */
//    @RequestMapping(value = {"registry/{index}"},method = RequestMethod.POST)
//	public String findByName(@PathVariable int index,@RequestParam String imageName,Model model) {
//        List<Image> images = null;
//        String active = null;
//        long userId = CurrentUserUtils.getInstance().getUser().getId();
//        if (index == 0) {
//            images = imageDao.findByNameCondition("%"+imageName+"%");
//            addCurrUserFavor(images);
//            active = "镜像中心";
//        } 
//        else if (index == 1) {
//            images = imageDao.findByNameOfUser(userId,"%"+imageName+"%");
//            addCurrUserFavor(images);
//            active = "我的镜像";
//        }
//        else if (index == 2) {
//            images = userDao.findByNameCondition(userId, "%"+imageName+"%");
//            addCurrUserFavor(images);
//            active = "我的收藏";
//        }
//		
//        model.addAttribute("type", index);
//        model.addAttribute("images", images);
//        model.addAttribute("active",active);
//		
//        return "docker-registry/registry.jsp";
//    }
	
	/**
	 * 显示当前镜像详细信息
	 * @param id 镜像Id
	 * @param model 添加返回页面的数据
	 * @return String
	 */
    @RequestMapping(value = {"registry/detail/{id}"}, method = RequestMethod.GET)
	public String detail(@PathVariable long id, Model model) {
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        Image image = imageDao.findById(id);
        //判断镜像是否被删除
        if (null == image) {
            model.addAttribute("menu_flag", "registry");
            return "docker-registry/nodetail.jsp";
        }
        //查询有多少租户收藏当前镜像
        int favorUser = imageDao.findAllUserById(id);
        //查询当前镜像的创建者信息
        User user = userDao.findById(image.getCreator());
        long imageCreator = image.getCreator();
        //判断当前镜像当前用户是否收藏
        int  whetherFavor = imageDao.findByUserIdAndImageId(id, userId);
		//判断当前镜像是否是当前用户创建的
        if (userId == imageCreator) {
            model.addAttribute("editImage",1);
        }
        else {
            model.addAttribute("editImage", 2);
        }
		
        if (StringUtils.isEmpty(user)) {
            model.addAttribute("creator", "租户已注销");
        } else {
            model.addAttribute("creator", user.getUserName());
        }
        model.addAttribute("whetherFavor", whetherFavor);
        model.addAttribute("image", image);
        model.addAttribute("favorUser",favorUser);
//        model.addAttribute("creator", user.getUserName());
        model.addAttribute("menu_flag", "registry");
		
        return "docker-registry/detail.jsp";
    }
	
	/**
	 * 编辑当前镜像的简介
	 * @param imageId 镜像Id
	 * @param summary 镜像简介
	 * @return String
	 */
    @RequestMapping(value = {"registry/detail/summary"}, method = RequestMethod.POST)
	@ResponseBody
	public String imageSummary(@RequestParam long imageId,String summary){
        Image image = imageDao.findById(imageId);
        image.setSummary(summary);
        imageDao.save(image);
        return "success";
    }
	
	/**
	 * 编辑当前镜像的详细信息
	 * @param imageId 镜像Id
	 * @param remark 镜像的详细信息
	 * @return String
	 */
    @RequestMapping(value = {"registry/detail/remark"}, method = RequestMethod.POST)
	@ResponseBody
	public String imageRemark(@RequestParam long imageId,String remark){
        Image image = imageDao.findById(imageId);
        image.setRemark(remark);
        imageDao.save(image);
        return "success";
    }
	
	/**
	 * 响应镜像的收藏按钮
	 * @param imageId 镜像Id
	 * @return String
	 */
    @RequestMapping(value = {"registry/detail/favor"}, method = RequestMethod.POST)
	@ResponseBody
	public String favor(@RequestParam long imageId) {
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        UserFavorImages ufi = favorDao.findByImageIdAndUserId(imageId, userId);
        if(ufi != null){
            favorDao.delete(ufi);
            return "delete";
        }
        else {
            ufi = new UserFavorImages();
            ufi.setFavor_users(userId);
            ufi.setFavor_images(imageId);
            favorDao.save(ufi);
            return "success";
        }
    }
	
	/**
	 * 判断中有没有用户下载过当前镜像
	 * @param imageName ： 镜像名称
	 * @param imageVersion ： 镜像版本
	 * @return  String
	 */
    @RequestMapping(value = {"registry/judgeFileExist.do"}, method = RequestMethod.POST)
	@ResponseBody
	public String judgeFileExist(String imageName, String imageVersion){
        Map<String, Object> map = new HashMap<String, Object>();
        File path = new File(imagePath);
        if (null != path.listFiles()) {
            File[] currentFiles = path.listFiles();
            if (null != currentFiles) {
                for (File oneRow : currentFiles) {
                    oneRow.delete();
                }
            }
        }
        
    	String downName = imageName.substring(imageName.lastIndexOf("/")+1) + "-" + imageVersion;
        File file = new File(imagePath+"/"+downName+".tar");
        if (file.exists()) {
            map.put("status", "200");
        } 
        else {
            map.put("status", "500");
        }
        return JSON.toJSONString(map);
    }
	
	/**
     * 响应镜像“下载”按钮的实现
     * @param imgID ： 镜像Id
     * @param imageName ： 镜像名称
     * @param imageVersion 镜像版本
     * @param resourceName 资源
     * @param request  
     * @param response 
     * @param model
     * @return map JSONString
     */
    @RequestMapping(value = {"registry/downloadImage"}, method = RequestMethod.GET)
    @ResponseBody
    public String downloadImage(String imgID, String imageName, String imageVersion, String resourceName,
                                                Model model,HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<String, Object>();
        String downName = imageName.substring(imageName.lastIndexOf("/")+1) + "-" + imageVersion;
        File path = new File(imagePath);
        if (!path.exists() && !path.isDirectory()) {
            path.mkdirs();
        }

        File file = new File(imagePath+"/"+downName+".tar");
        boolean exist = file.exists();
        if (exist) {
            map.put("status", "200");
        }
        else {
            boolean complete= dockerClientService.pullImage(imageName, imageVersion);
            boolean flag = false;
            if (complete) {
                try {
                    String cmd = imageCmdPath +" "+ imagePath +"/"
                        + downName + ".tar  "+ url +"/"+ imageName + ":" + imageVersion;
                    flag = CmdUtil.exeCmd(cmd);
                }
                catch (IOException e) {
                   LOG.error("error message:-" + e.getMessage());
                   map.put("status", "500");
                }
            }
            dockerClientService.removeImage(imageName, imageVersion, null, null,null);
            if (flag) {
                map.put("status", "200");
            }
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 下载镜像文件
     * @param imageName String
     * @param imageVersion String
     * @param request ：request
     * @param response  ： response
     * @see
     */
    @RequestMapping(value = {"registry/download"}, method = RequestMethod.GET)
    public void getDownload(String imageName, String imageVersion,
                                HttpServletRequest request, HttpServletResponse response) {
        String fileName = imageName.substring(imageName.lastIndexOf("/")+1) + "-" + imageVersion + ".tar";
        //设置文件MIME类型  
        response.setContentType(request.getServletContext().getMimeType(imagePath+"/"+fileName));  
        //设置Content-Disposition  
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);  
        try {  
            InputStream myStream = new FileInputStream(imagePath+"/"+fileName);  
            IOUtils.copy(myStream, response.getOutputStream());  
            response.flushBuffer();  
        } 
        catch (IOException e) {  
            LOG.error("downloadImage error:"+e.getMessage());
        }
    }
    
	/**
	 * 删除当前镜像
	 * @param imageId 镜像Id
	 * @return String
	 */
    @RequestMapping(value = {"registry/detail/deleteimage"}, method = RequestMethod.POST)
	@ResponseBody
	public String deleteImage(@RequestParam long imageId){
        
        Image image = imageDao.findOne(imageId);
        if (null != image) {
            image.setIsDelete(CommConstant.TYPE_YES_VALUE);
            imageDao.save(image);
            // TODO 应该删除本地镜像和仓库中的镜像
            return "ok";
        } 
        else {
            return "error";
        }
    }
    
    
   /**
    * Description: <br>
    * 批量删除镜像
    * @param imageIds 
    * @return String
    */
    @RequestMapping("registry/delImages.do")
    @ResponseBody
    public String deleteImages(String imageIds) {
        // 解析获取的id List
        ArrayList<Long> ids = new ArrayList<Long>();
        String[] str = imageIds.split(",");
        if (str != null && str.length > 0) {
            for (String id : str) {
                ids.add(Long.valueOf(id));
            }
        }
        Map<String, Object> maps = new HashMap<String, Object>();
        try {
            for (long id : ids) {
                deleteImage(id);
            }
            maps.put("status", "200");
        } 
        catch (Exception e) {
            maps.put("status", "400");
            LOG.error("镜像删除错误！");
        }
        return JSON.toJSONString(maps); 
    }
       
    /**
     * Description: <br>
     * 循环遍历镜像集合，查询当前用户是否收藏该镜像
     * @param images 镜像集合
     */
    private void addCurrUserFavor(List<Image> images){
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        for(Image image:images){
            image.setCurrUserFavor(imageDao.findByUserIdAndImageId(image.getId(), userId));
            image.setFavorUsers(null);
        }
    }
    
    /**
     * 
     * Description:
     * addCreatorName
     * @param images 
     * @see
     */
    private void addCreatorName(List<Image> images){
        for(Image image:images){
            User user = userDao.findById(image.getCreator());
            if (null != user) {
                image.setCreatorName(user.getUserName());
            }
        }
    }
}
