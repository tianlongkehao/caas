package com.bonc.epm.paas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.collections.CollectionUtils;
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
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.FavorDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.docker.api.DockerRegistryAPIClientInterface;
import com.bonc.epm.paas.docker.exception.DokcerRegistryClientException;
import com.bonc.epm.paas.docker.exception.ErrorList;
import com.bonc.epm.paas.docker.model.Images;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.docker.util.DockerRegistryService;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.CommonOprationLogUtils;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserFavorImages;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.DateUtils;
import com.bonc.epm.paas.util.FileUtils;
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
     * 日志记录dao层接口
     */
    @Autowired
    private CommonOperationLogDao commonOperationLogDao;
    
    /**
     * DockerClientService 接口
     */
    @Autowired
	private DockerClientService dockerClientService;
	
    /**
     * DockerClientService 接口
     */
    @Autowired
	private DockerRegistryService dockerRegistryService;
		
    /**
     * 获取paas.image.path中的镜像下载地址
     */
    @Value("${paas.image.path}")
	private String imagePath = "../downimage";
    
    /**
     * Description: <br>
     * 进入镜像显示页面，镜像显示分三层分隔显示
     * @param index 显示哪一层的镜像
     * @param model 添加返回数据
     * @return String
     */
    @RequestMapping(value = {"registry/{index}"}, method = RequestMethod.GET)
    public String index(@PathVariable int index, Model model) {
    	User user = CurrentUserUtils.getInstance().getUser();
        long userId = user.getId();
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
        model.addAttribute("user",user);

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
                images = imageDao.findAllByCreateBy(userId,pageRequest);
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
        User user = userDao.findById(image.getCreateBy());
        long imageCreator = image.getCreateBy();
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
        try {           
            String downName = imageName.substring(imageName.lastIndexOf("/")+1) + "-" + imageVersion;
            File file = new File(imagePath+"/"+downName+".tar");
            if (file.exists()) {
                LOG.info("filename:-"+file.getName()+"; filesize:-" + file.length());
                if (file.length() <=0) {
                    file.delete();
                    map.put("status", "500");
                } else {
                    map.put("status", "200"); 
                }
            } else {
                map.put("status", "500");
            }
        }
        catch (Exception e) {
            LOG.error("error judgeFileExist:" + e.getMessage());
            e.printStackTrace();
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
	 * @throws IOException
     */
    @RequestMapping(value = {"registry/downloadImage"}, method = RequestMethod.GET)
    @ResponseBody
    public String downloadImage(String imgID, String imageName, String imageVersion, String resourceName,
                                                Model model,HttpServletRequest request, HttpServletResponse response) throws IOException{
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
                InputStream inputStream = null;
                try {
                    inputStream = dockerClientService.saveImage(imageName,imageVersion);
                    FileUtils.storeFile(inputStream,imagePath+"/"+downName+".tar");
                    flag = true;
                }
                catch (IOException e) {
                    LOG.error("error message:-" + e.getMessage());
                    flag = false;
                } 
                finally {
                    if (null != inputStream) {
                        inputStream.close();
                    }
                }
                 // deprecated save image method
//                String url="192.168.0.76:5000";
//                String imageCmdPath ="docker save -o";
//                try {
//                    String cmd = imageCmdPath +" "+ imagePath +"/"
//                        + downName + ".tar  "+ url +"/"+ imageName + ":" + imageVersion;
//                    flag = CmdUtil.exeCmd(cmd);
//                }
//                catch (IOException e) {
//                   LOG.error("error message:-" + e.getMessage());
//                   map.put("status", "500");
//                }
            }
            dockerClientService.removeImage(imageName, imageVersion, null, null,null,null);
            if (flag) {
                //添加下载镜像操作记录
                String extraInfo="下载镜像："+imageName + "版本信息" + imageVersion;
                CommonOperationLog comlog=CommonOprationLogUtils.getOprationLog(imageName, extraInfo, CommConstant.IMAGE, CommConstant.OPERATION_TYPE_EXPORT);
                commonOperationLogDao.save(comlog);
                
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
            response.reset();
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
            //return "redirect:registry/0";
    }
    
    /**
     * 
     * Description:
     * 删除本地数据库镜像，以及远程仓库镜像信息
     * 1.正常删除流程：删除本地镜像信息和远程镜像清单信息manifests
     * 2.非正常处理流程：
     *      其一：无法获取本地数据库镜像信息的清单信息manifests 不删除
     *      其二：获取镜像信息的清单信息manifests后，调用删除清单API 返回errorList信息  不删除
     *  TODO
     *   对于无法通过正常渠道删除的镜像信息，
     *   需要通过镜像同步、GC回收、手动清除垃圾镜像等手段处理
     * @param imageId 镜像Id
     * @return String 
     * @see
     */
    @RequestMapping(value = {"registry/detail/deleteimage"}, method = RequestMethod.POST)
	@ResponseBody
	public String deleteImage(@RequestParam long imageId){
        boolean isDeleteFlag = false;
        Image image = imageDao.findOne(imageId);
        if (null != image) {
            try {
                DockerRegistryAPIClientInterface client = dockerRegistryService.getClient();
                MultivaluedMap<String, Object> mult = client.getManifestofImage(image.getName(), image.getVersion());
                if (null != mult.get("Etag") && mult.get("Etag").size() > 0) {
                    for (Object oneRow : mult.get("Etag")) {
                        ErrorList errors = client.deleteManifestofImage(image.getName(), String.valueOf(oneRow).substring(1, String.valueOf(oneRow).length()-1));
                        if (null == errors) {
                            isDeleteFlag = true;
                        }
                        LOG.info("delete image, docker regsitry API return msg: -"+JSON.toJSONString(errors));
                    }
                }
                if (isDeleteFlag) {
                    image.setIsDelete(CommConstant.TYPE_YES_VALUE);
                    imageDao.save(image);
                    
                    //添加删除镜像操作记录
                    String extraInfo = "删除镜像" + image.getName() + "的信息" + JSON.toJSONString(image);
                    CommonOperationLog comlog=CommonOprationLogUtils.getOprationLog(image.getName(), extraInfo, CommConstant.IMAGE, CommConstant.OPERATION_TYPE_DELETE);
                    commonOperationLogDao.save(comlog);
                    
                    return "ok";
                }
            }
            catch (DokcerRegistryClientException dockerEx) {
                LOG.error("delete image error. error message:-"+JSON.toJSONString(dockerEx.getErrorList()));
                return "error";               
            }
            catch (Exception e) {
                LOG.error("delete image error. error message:-"+e.getMessage());
                return "error";
            }
        } 
        return "error";
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
                // TODO 返回批量删除中失败的镜像信息
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
            User user = userDao.findById(image.getCreateBy());
            if (null != user) {
                image.setCreatorName(user.getUserName());
            }
        }
    }
    
    /**
     * 
     * Description:
     * 管理员添加，同步本地数据库和私有仓库的镜像信息
     * @see
     */
	@SuppressWarnings("unchecked")
	@RequestMapping("registry/refresh.do")
    @ResponseBody
	public String refresh() {
		Map<String, Object> maps = new HashMap<String, Object>();
		
        DockerRegistryAPIClientInterface client = dockerRegistryService.getClient();
        //获取数据库中所有的镜像
        ArrayList<Image> imageList = (ArrayList<Image>) imageDao.findAll();
        //获取所有的镜像分组名称
        Images images = client.getImages();
        if (images != null) {
        	//遍历数据库中的镜像
	        for (Image image : imageList) {
	        	//判断仓库中是否有对应的镜像名和tag
				if (!images.getRepositories().contains(image.getName()) ||
						!client.getTagsofImage(image.getName()).getTags().contains(image.getVersion())) {
					//将数据库中的镜像的isDelete字段设置为1
					image.setIsDelete(1);
					imageDao.save(image);
					LOG.info(image.getName()+":"+image.getVersion()+" is deleted");
				};
			}
		}
		maps.put("status", "200");
		return JSON.toJSONString(maps);
	}
	
	/**
	 * 
	 * Description:
	 * 获取创建者按镜像名称分组的镜像 
	 * @see
	 */
	@RequestMapping("registry/getImagesGroupByName.do")
	@ResponseBody
	public String getImagesGroupByName() {
		long creator = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> maps = new HashMap<String, Object>();
		//获取所有的镜像
		List<Image> imageList = imageDao.findByCreateByOrderByName(creator);
		//用于存放分组后所有的镜像
		List<Object> images = new ArrayList<>();
		String lastImageName= null;
		List<Image> sameNameImages = new ArrayList<Image>();
		for (Image image : imageList) {
			//镜像名和上一个镜像名不同，且上个镜像不是空的时候
			if (!image.getName().equals(lastImageName) && CollectionUtils.isNotEmpty(sameNameImages)) {
				images.add(sameNameImages);
				sameNameImages = new ArrayList<Image>();
			}
			lastImageName = image.getName();
			sameNameImages.add(image);
		}
		images.add(sameNameImages);
		maps.put("imagesGroupByName", images);
		maps.put("status", "200");
		return JSON.toJSONString(maps);
	}
	
    /**
     * 
     * Description:
     * 获取创建者按创建月份分组的镜像 
     * @see
     */
	@RequestMapping("registry/getImagesGroupByMonth.do")
    @ResponseBody
	public String getImagesGroupByMonth() {
		long creator = CurrentUserUtils.getInstance().getUser().getId();
		Map<String, Object> maps = new HashMap<String, Object>();
		//获取该用户的所有镜像
		List<Image> imageList = imageDao.findByCreateByOrderByCreatTime(creator);
		//获取当前的时间
		Calendar calendar = Calendar.getInstance();
		
		//当月月份yyyyMM
		String Period = DateUtils.getDate2SStr3(calendar.getTime());
		//上一个月月份yyyyMM
		calendar.add(Calendar.MONTH, -1);
		String Period1 = DateUtils.getDate2SStr3(calendar.getTime());
		//上两个月月份yyyyMM
		calendar.add(Calendar.MONTH, -1);
		String Period2 = DateUtils.getDate2SStr3(calendar.getTime());
		//用于存放所有的分组
		List<Object> allImages = new ArrayList<>();
		//用于存放当月的镜像
		List<Image> images = new ArrayList<Image>();
		//用于存放上一个月的镜像
		List<Image> images1 = new ArrayList<Image>();
		//用于存放上两个月的镜像
		List<Image> images2 = new ArrayList<Image>();
		//用于存放更早的的镜像
		List<Image> images3 = new ArrayList<Image>();
		
		for (Image image : imageList) {
			//获取格式化的镜像创建时间yyyyMM
			String creatTime = DateUtils.getDate2SStr3(image.getCreateDate());
			//根据月份放入对应的集合
			if (creatTime.equals(Period)) {
				images.add(image);
				continue;
			} else if (creatTime.equals(Period1)) {
				images1.add(image);
				continue;
			} else if (creatTime.equals(Period2)) {
				images2.add(image);
				continue;
			} else {
				images3.add(image);
			}
		}
		allImages.add(images);
		allImages.add(images1);
		allImages.add(images2);
		allImages.add(images3);
		maps.put("allImages", allImages);
//		maps.put("images1", images1);
//		maps.put("images2", images2);
//		maps.put("images3", images3);
		maps.put("status", "200");
		return JSON.toJSONString(maps);
	}

}
