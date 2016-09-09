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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.FavorDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserFavorImages;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.SshConnect;

/**
 * 镜像
 * @author shangvven
 *
 */	
@Controller
public class RegistryController {
	private static final Logger LOG = LoggerFactory.getLogger(RegistryController.class);
	@Autowired
	private ImageDao imageDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private FavorDao favorDao;
	@Autowired
	private DockerClientService dockerClientService;
	
	@Value("${docker.image.url}")
    private String url;
	
	@Value("${docker.ssh.username}")
	private String userName;
	
	@Value("${docker.ssh.password}")
	private String password;
	
	@Value("${docker.ssh.address}")
	private String address;
	
	@Value("${paas.image.path}")
	public String imagePath = "../downimage";
	
	@Value("${paas.saveimage.path}")
	public String saveImagePath;
	
	/**
	 * 响应镜像查询按钮
	 * @param index
	 * @param model
	 * @return
	 */
	
	@SuppressWarnings("null")
	@RequestMapping(value = {"registry/{index}"}, method = RequestMethod.GET)
	public String index(@PathVariable int index, Model model) {
		List<Image> images = null;
		String active = null;
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		if(index == 0){
			images = imageDao.findByImageType(1);
			addCurrUserFavor(images);
			active = "镜像中心";
		}else if(index == 1){
			images = imageDao.findAllByCreator(userId);
			addCurrUserFavor(images);
			active = "我的镜像";
		}else if(index == 2){
			images = userDao.findAllFavor(userId);
			addCurrUserFavor(images);
			active = "我的收藏";
		}
		addCreatorName(images);
		model.addAttribute("images", images);
		model.addAttribute("menu_flag", "registry");
		model.addAttribute("index", index);
		model.addAttribute("active",active);
		
		return "docker-registry/registry.jsp";
	}
	
	/**
	 * 镜像搜索
	 * @param index
	 * @param imageName
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"registry/{index}"},method = RequestMethod.POST)
	public String findByName(@PathVariable int index,@RequestParam String imageName,Model model) {
		List<Image> images = null;
		String active = null;
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		if(index == 0){
			images = imageDao.findByNameCondition("%"+imageName+"%");
			addCurrUserFavor(images);
			active = "镜像中心";
		}else if(index == 1){
			images = imageDao.findByNameOfUser(userId,"%"+imageName+"%");
			addCurrUserFavor(images);
			active = "我的镜像";
		}else if(index == 2){
			images = userDao.findByNameCondition(userId, "%"+imageName+"%");
			addCurrUserFavor(images);
			active = "我的收藏";
		}
		
		model.addAttribute("type", index);
		model.addAttribute("images", images);
		model.addAttribute("active",active);
		
		return "docker-registry/registry.jsp";
	}
	
	/**
	 * 显示当前镜像详细信息
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"registry/detail/{id}"}, method = RequestMethod.GET)
	public String detail(@PathVariable long id, Model model) {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Image image = imageDao.findById(id);
		int favorUser = imageDao.findAllUserById(id);
		User user = userDao.findById(image.getCreator());
		long imageCreator = image.getCreator();
		int  whetherFavor = imageDao.findByUserIdAndImageId(id, userId);
		
		if(userId == imageCreator){
			model.addAttribute("editImage",1);
		}else{
			model.addAttribute("editImage", 2);
		}
		
		model.addAttribute("whetherFavor", whetherFavor);
		model.addAttribute("image", image);
		model.addAttribute("favorUser",favorUser);
		model.addAttribute("creator", user.getUserName());
		model.addAttribute("menu_flag", "registry");
		
		return "docker-registry/detail.jsp";
	}
	
	/**
	 * 编辑当前镜像的简介
	 * @param imageId
	 * @param summary
	 * @return
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
	 * @param imageId
	 * @param remark
	 * @return
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
	 * @param imageId
	 * @return
	 */
	
	@RequestMapping(value = {"registry/detail/favor"}, method = RequestMethod.POST)
	@ResponseBody
	public String favor(@RequestParam long imageId) {
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		UserFavorImages ufi = favorDao.findByImageIdAndUserId(imageId, userId);
		if(ufi != null){
			favorDao.delete(ufi);
			return "delete";
		}else{
			ufi = new UserFavorImages();
			ufi.setFavor_users(userId);
			ufi.setFavor_images(imageId);
			favorDao.save(ufi);
			return "success";
		}
	}
	
	/**
	 * 判断中有没有用户下载过当前镜像
	 * 
	 * @param imageName ： 镜像名称
	 * @param imageVersion ： 镜像版本
	 * @return  String
	 * @see
	 */
	@RequestMapping(value = {"registry/judgeFileExist.do"}, method = RequestMethod.POST)
	@ResponseBody
	public String judgeFileExist(String imageName, String imageVersion){
	    Map<String, Object> map = new HashMap<String, Object>();
    	String downName = imageName.substring(imageName.lastIndexOf("/")+1) + "-" + imageVersion;
        File file = new File(imagePath+"/"+downName+".tar");
        boolean exist = file.exists();
        if (exist) {
            map.put("status", "200");
        } else {
            map.put("status", "500");
        }
        return JSON.toJSONString(map);
	}
	
	/**
     * 响应镜像“下载”按钮的实现
     * 
     * @param imgID ： 镜像Id
     * @param imageName ： 镜像名称
     * @param imageVersion 镜像版本
     * @param resourceName 资源
     * @param model model
     * @return  String
     * @see
     */
    @RequestMapping(value = {"registry/downloadImage"}, method = RequestMethod.GET)
    @ResponseBody
    public void downloadImage(String imgID, String imageName, String imageVersion, String resourceName,
                                Model model,HttpServletRequest request, HttpServletResponse response){
        
        String downName = imageName.substring(imageName.lastIndexOf("/")+1) + "-" + imageVersion;
        File file = new File(imagePath+"/"+downName+".tar");
        boolean exist = file.exists();
        if (exist) {
            getDownload(downName+".tar",request,response);
        }else {
            boolean complete= dockerClientService.pullImage(imageName, imageVersion);
            boolean flag = false;
            if (complete) {
                String cmd = "sudo docker save -o " + saveImagePath
                    + downName + ".tar "+ url +"/"+ imageName + ":" + imageVersion;
                flag = cmdexec(cmd);
            }
            dockerClientService.removeImage(imageName, imageVersion, null, null,null);
            if (flag) {
                getDownload(downName+".tar",request,response);
            }
        }
    }
    
    /**
     * 下载镜像文件
     * 
     * @param fileName : 文件名称
     * @param request ：request
     * @param response  ： response
     * @see
     */
    public void getDownload(String fileName,HttpServletRequest request, HttpServletResponse response) {  
        
        //设置文件MIME类型  
        response.setContentType(request.getServletContext().getMimeType(imagePath+"/"+fileName));  
        //设置Content-Disposition  
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);  
        try {  
            InputStream myStream = new FileInputStream(imagePath+"/"+fileName);  
            IOUtils.copy(myStream, response.getOutputStream());  
            response.flushBuffer();  
        } catch (IOException e) {  
            LOG.error("downloadImage error:"+e.getMessage());
        }  
    }
    
    /**
     * ssh cmd
     * 
     * @param cmd
     * @return
     */
    public boolean cmdexec(String cmd) {
        try {
            SshConnect.connect(userName, password, address, 22);
            boolean b = false;
            String rollingLog = SshConnect.exec(cmd, 1000);
            while (!b) {
                String str = SshConnect.exec("", 10000);
                if (StringUtils.isNotBlank(str)) {
                    rollingLog += str;
                }
                b = (str.endsWith("$") || str.endsWith("#"));
                //b = str.endsWith("updated");
            }
            LOG.info("rolling-update log:-"+rollingLog);
            String result = SshConnect.exec("echo $?", 1000);
            if (StringUtils.isNotBlank(result)) {
                if (!('0' == (result.trim().charAt(result.indexOf("\n")+1)))) {
                    new InterruptedException();
                }
            } else {
                new InterruptedException();
            }
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
            LOG.error("error:执行command失败");
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            LOG.error("error:ssh连接失败");
            return false;
        } finally {
            SshConnect.disconnect();
        }
        return true;
    }
    
	
	/**删除当前镜像
	 * 
	 * @param imageId
	 * @return
	 */
	
	@RequestMapping(value = {"registry/detail/deleteimage"}, method = RequestMethod.POST)
	@ResponseBody
	public String deleteImage(@RequestParam long imageId){
		imageDao.delete(imageId);
		// TODO 应该删除本地镜像和仓库中的镜像
		return "ok";
	}
	
	
	private void addCurrUserFavor(List<Image> images){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		for(Image image:images){
			image.setCurrUserFavor(imageDao.findByUserIdAndImageId(image.getId(), userId));
		}
	}
	private void addCreatorName(List<Image> images){
		for(Image image:images){
			User user = userDao.findById(image.getCreator());
			if (user != null) {
				image.setCreatorName(user.getUserName());
			}
		}
	}

}
