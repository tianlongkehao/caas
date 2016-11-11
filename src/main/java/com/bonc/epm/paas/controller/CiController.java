package com.bonc.epm.paas.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CiConstant;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.ImageConstant;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.dao.DockerFileTemplateDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.DockerFileTemplate;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CmdUtil;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.DateUtils;
import com.bonc.epm.paas.util.FileUtils;
import com.bonc.epm.paas.util.ResultPager;
import com.github.dockerjava.api.DockerClient;
/**
 * 构建容器
 * 
 * @author update
 * @version 2016年8月31日
 * @see CiController
 * @since
 */
@Controller
public class CiController {
    /**
     * 输出日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(CiController.class);
    
    /**
     * CiDao接口
     */
    @Autowired
	private CiDao ciDao;
    
    /**
     * CiRecoredDao接口
     */
    @Autowired
	private CiRecordDao ciRecordDao;
    
    /**
     * ImageDao接口
     */
    @Autowired
	private ImageDao imageDao;
    
    /**
     * DockerFile模板接口
     */
    @Autowired
	private DockerFileTemplateDao dockerFileTemplateDao;
	
    /**
     * DockerClientService 接口
     */
    @Autowired
	private DockerClientService dockerClientService;
	
    /**
     * 获取配置文件中的codetemp路径
     */
    @Value("${paas.codetemp.path}")
	private String CODE_TEMP_PATH = "./codetemp";
	
    /**
     * 进入构建主页面
     * 
     * @param model ：model
     * @return ci.jsp
     * @see
     */
    @RequestMapping(value={"ci"},method=RequestMethod.GET)
	public String index(Model model){
//        User cuurentUser = CurrentUserUtils.getInstance().getUser();
//        List<Ci> ciList = ciDao.findByCreateBy(cuurentUser.getId(), new Sort(new Order(Direction.DESC,"createDate")));
//        model.addAttribute("ciList", ciList);
        model.addAttribute("menu_flag", "ci");
        return "ci/ci.jsp";
    }

    @RequestMapping(value = {"ci/page.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String findServiceByPage(String draw, int start,int length,
                                    HttpServletRequest request){
        long userId = CurrentUserUtils.getInstance().getUser().getId();
        String search = request.getParameter("search[value]");
        Map<String,Object> map = new HashMap<String, Object>();
        Page<Ci> cis = null;
        PageRequest pageRequest = null;
        //判断是第几页
        if (start == 0) {
            pageRequest = ResultPager.buildPageRequest(null, length);
        }else {
            pageRequest = ResultPager.buildPageRequest(start/length + 1, length);
        }
        //判断是否需要搜索服务
        if (StringUtils.isEmpty(search)) {
            cis = ciDao.findByCreateByOrderByCreateDateDesc(userId,pageRequest);
        } else {
            cis = ciDao.findByNameOf(userId, "%" + search + "%",pageRequest);
        }
        map.put("draw", draw);
        map.put("recordsTotal", cis.getTotalElements());
        map.put("recordsFiltered", cis.getTotalElements());
        map.put("data", cis.getContent());
        
        return JSON.toJSONString(map);
    }
    
    /**
     * 
     * Description: <br>
     * 根据构建Id加载构建的详细信息
     * @param model Model 
     * @param id ： 构建Id
     * @return ci_detail.jsp
     * @throws IOException 
     * @see
     */
    @RequestMapping(value={"ci/detail/{id}"},method=RequestMethod.GET)
	public String detail(Model model,@PathVariable long id) throws IOException{
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        Ci ci = ciDao.findOne(id);
        List<CiRecord> ciRecordList = ciRecordDao.findByCiId(id,new Sort(new Order(Direction.DESC,"constructDate")));
        List<Image> images = this.findByBaseImages();
        Image currentBaseImage = imageDao.findById(ci.getBaseImageId());
        if (ci.getType() == 2) {
            List<DockerFileTemplate> dockerFiles = dockerFileTemplateDao.findByCreateBy(cuurentUser.getId());
            File file = new File(ci.getCodeLocation()+"/"+"Dockerfile");
            if (file.exists()) {
                String dockerFileTxt = FileUtils.readFileByLines(ci.getCodeLocation()+"/"+"Dockerfile");
                model.addAttribute("dockerFileTxt", dockerFileTxt);
            }
            model.addAttribute("dockerFiles", dockerFiles);
        }
        model.addAttribute("id", id);
        model.addAttribute("ci", ci);
        model.addAttribute("ciRecordList", ciRecordList);
        model.addAttribute("baseImage", images);
        model.addAttribute("currentBaseImage",currentBaseImage);
        model.addAttribute("docker_regisgtry_address", dockerClientService.getDockerRegistryAddress());
        model.addAttribute("menu_flag", "ci");
        return "ci/ci_detail.jsp";
    }
	
    /**
     * 修改构建信息
     * 
     * @param ci ：ci
     * @return String
     * @see
     */
    @RequestMapping("ci/modifyCi.do")
	@ResponseBody
	public String modifyCi(Ci ci) {
        Ci originCi = ciDao.findOne(ci.getId());
        originCi.setProjectName(ci.getProjectName());
        originCi.setDescription(ci.getDescription());
        originCi.setDockerFileLocation(ci.getDockerFileLocation());
        ciDao.save(originCi);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "200");
        map.put("data", ci);
        return JSON.toJSONString(map);
    }
	
    /**
     * DoclerFile构建修改构建信息
     * 
     * @param ci ： ci
     * @param sourceCode ： 代码
     * @param dockerFile ： dockerFile文件
     * @return  String
     * @see
     */
    @RequestMapping("ci/modifyDockerFileCi.do")
	@ResponseBody
	public String modifyDockerFileCi(Ci ci,@RequestParam("sourceCode") MultipartFile sourceCode,String dockerFile) {
        Ci originCi = ciDao.findOne(ci.getId());
        originCi.setProjectName(ci.getProjectName());
        originCi.setDescription(ci.getDescription());
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            File file = new File(originCi.getCodeLocation());
            if (!file.exists()) {
                file.mkdirs();
            }
            if (sourceCode!=null&&!sourceCode.isEmpty()) {
                FileUtils.storeFile(sourceCode.getInputStream(), originCi.getCodeLocation()+"/"+sourceCode.getOriginalFilename());
            }
            if (!dockerFile.isEmpty()) {
                ByteArrayInputStream in = new ByteArrayInputStream(dockerFile.getBytes());  
                FileUtils.storeFile(in, originCi.getCodeLocation()+"/"+"Dockerfile");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            LOG.error("modifyResourceCi error:"+e.getMessage());
            map.put("status", "500");
            map.put("msg","上传文件出错");
          
            return JSON.toJSONString(map);
        }
        ciDao.save(originCi);
        map.put("status", "200");
        map.put("data", ci);
        return JSON.toJSONString(map);
    }
	
    /**
     * 修改快速构建构建信息
     * 
     * @param ci ： ci
     * @param sourceCode ： 上传代码
     * @return String
     * @see
     */
    @RequestMapping("ci/modifyCodeResourceCi.do")
	@ResponseBody
	public String modifyCodeResourceCi(Ci ci,@RequestParam("sourceCode") MultipartFile sourceCode) {
        Ci originCi = ciDao.findOne(ci.getId());
        originCi.setProjectName(ci.getProjectName());
        originCi.setDescription(ci.getDescription());
        originCi.setBaseImageName(dockerClientService.getDockerRegistryAddress() + "/" + ci.getBaseImageName());
        originCi.setBaseImageId(ci.getBaseImageId());
        originCi.setBaseImageVersion(imageDao.findById(ci.getBaseImageId()).getVersion());
        //originCi.setBaseImageName(ci.getBaseImageName());
        //originCi.setBaseImageVersion(ci.getBaseImageVersion());
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            File file = new File(originCi.getCodeLocation());
            if(!file.exists()){
                file.mkdirs();
            }
            if (sourceCode != null && !sourceCode.isEmpty()) {
                originCi.setResourceName(sourceCode.getOriginalFilename());
                FileUtils.storeFile(sourceCode.getInputStream(), originCi.getCodeLocation()+"/"+sourceCode.getOriginalFilename());
            }
        	
            String fileTemplate = FileUtils.class.getClassLoader().getResource("Dockerfile").getPath();
            String toFile = originCi.getCodeLocation()+"/"+"Dockerfile";
            Map<String,String> data = new HashMap<String, String>();
            data.put("${baseImage}", originCi.getBaseImageName()+":"+originCi.getBaseImageVersion());
            data.put("${fileName}", originCi.getResourceName());
            FileUtils.writeFileByLines(fileTemplate, data, toFile);
        } 
        catch (Exception e) {
            e.printStackTrace();
            LOG.error("modifyResourceCi error:"+e.getMessage());
            map.put("status", "500");
            map.put("msg","上传文件出错");
            
            return JSON.toJSONString(map);
        }
        ciDao.save(originCi);
        map.put("status", "200");
        map.put("data", ci);
        return JSON.toJSONString(map);
    }
	
	/**
	 * 删除构建
	 * 
	 * @param id ：id
	 * @return String
	 * @see
	 */
    @RequestMapping(value = "ci/delCi.do", method = RequestMethod.POST)
	@ResponseBody
	public String delCi(@RequestParam String id) {
        Long idl = Long.parseLong(id);
        ciDao.delete(idl);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
	
    /**
     * 进入代码构建页面
     * 
     * @param model ： model
     * @return ci_add.jsp
     * @see
     */
    @RequestMapping(value={"ci/add"},method=RequestMethod.GET)
	public String addProject(Model model){
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        model.addAttribute("username", cuurentUser.getUserName());
        model.addAttribute("menu_flag", "ci");
        return "ci/ci_add.jsp";
    }
	
    /**
     * 进入上传镜像页面
     * 
     * @param model ： 
     * @return ci_uploadImage.jsp
     * @see
     */
    @RequestMapping(value={"ci/uploadImage"},method=RequestMethod.GET)
	public String addSource(Model model){
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        model.addAttribute("username", cuurentUser.getUserName());
        model.addAttribute("menu_flag", "ci");
        return "ci/ci_uploadImage.jsp";
    }
	
    /**
     * 进入dockerFile构建页面
     * 
     * @param model ： model
     * @return ci_dockerfile.jsp
     * @see
     */
    @RequestMapping(value={"ci/dockerfile"},method=RequestMethod.GET)
	public String dockerfile(Model model){
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        model.addAttribute("username", cuurentUser.getUserName());
        model.addAttribute("menu_flag", "ci");
        return "ci/ci_dockerfile.jsp";
    }
	
    /**
     * 进入快速构建页面
     * 
     * @param model ： model
     * @return ci_addCodeSource.jsp
     * @see
     */
    @RequestMapping(value={"ci/addCodeSource"},method=RequestMethod.GET)
	public String oo(Model model){
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        List<Image> images = this.findByBaseImages();
        model.addAttribute("username", cuurentUser.getUserName());
        model.addAttribute("menu_flag", "ci");
        model.addAttribute("docker_regisgtry_address", dockerClientService.getDockerRegistryAddress());
        model.addAttribute("baseImage", images);
        return "ci/ci_addCodeSource.jsp";
    }
	
    /**
     * 代码构建的创建
     * 
     * @param ci ： ci
     * @return String
     * @see
     */
    @RequestMapping("ci/addCi.do")
	public String addCi(Ci ci) {
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        ci.setCreateBy(cuurentUser.getId());
        ci.setCreateDate(new Date());
        ci.setType(CiConstant.TYPE_QUICK);
        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
        ci.setConstructionDate(new Date());
        ci.setCodeLocation(CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
        ciDao.save(ci);
        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
        return "redirect:/ci";
		
    }
	/**
	 * 最开始的dockerfile构建
	 * 
	 * @param ci ：ci
	 * @param sourceCode : 上传代码
	 * @param dockerFile MultipartFile
	 * @return  String 
	 * @see MultipartFile
	 */
    @RequestMapping(value={"ci/addResourceCi.do"},method=RequestMethod.POST)
	public String addResourceCi(Ci ci,@RequestParam("sourceCode") MultipartFile sourceCode,@RequestParam("dockerFile") MultipartFile dockerFile) {
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        ci.setCreateBy(cuurentUser.getId());
        ci.setCreateDate(new Date());
        ci.setType(CiConstant.TYPE_DOCKERFILE);
        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
        ci.setConstructionDate(new Date());
        ci.setCodeLocation(CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
        ci.setDockerFileLocation("/");
        try {
            File file = new File(ci.getCodeLocation());
            if(!file.exists()){
                file.mkdirs();
            }
            if (!sourceCode.isEmpty()) {
                FileUtils.storeFile(sourceCode.getInputStream(), ci.getCodeLocation()+"/"+sourceCode.getOriginalFilename());
            }
            if (!dockerFile.isEmpty()) {
                FileUtils.storeFile(dockerFile.getInputStream(), ci.getCodeLocation()+"/"+dockerFile.getOriginalFilename());
            }
        } 
        catch (Exception e) {
            LOG.error("modifyResourceCi error:"+e.getMessage());
        	
            return "redirect:/error"; 
        }
        ciDao.save(ci);
        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
    
        return "redirect:/ci";
    }
	
	/**
	 * 
	 * Description: <br>
     *  上传镜像
	 * @param image ：
	 * @param sourceCode  MultipartFile
	 * @return  String
	 * @see MultipartFile
	 */
    @RequestMapping("ci/addResourceImage.do")
	public String addResourceImage(Image image , @RequestParam("sourceCode") MultipartFile sourceCode) {
        User currentUser = CurrentUserUtils.getInstance().getUser();
        image.setName(currentUser.getUserName() +"/" + image.getName());
        image.setResourceName(sourceCode.getOriginalFilename());
        image.setCreateTime(new Date());
        image.setCreator(currentUser.getId());
        image.setIsDelete(CommConstant.TYPE_NO_VALUE);
        
        try {
            String imagePath = CODE_TEMP_PATH +"/"+ image.getName() + "/" + image.getVersion();
            File file = new File(imagePath);
            if(!file.exists()) {
                file.mkdirs();
            }
            if (!sourceCode.isEmpty()) {
                FileUtils.storeFile(sourceCode.getInputStream(), imagePath+"/"+sourceCode.getOriginalFilename());
            }
            boolean flag = loadAndPushImage(image,imagePath+"/"+sourceCode.getOriginalFilename());
            if(flag){
                //排重添加镜像数据
                Image img = null;
                List<Image> imageList = imageDao.findByName(image.getName());
                if(!CollectionUtils.isEmpty(imageList)){
                    for(Image oneRow : imageList){
                        if(image.getVersion().equals(oneRow.getVersion())){
                            img = oneRow;
                        }
                    }
                }
                if(img==null){
                    img = image;
                }
                if (ImageConstant.BaseImage == img.getIsBaseImage()) {
                    img.setIsBaseImage(ImageConstant.BaseImage);
                } 
                else {
                    img.setIsBaseImage(ImageConstant.NotBaseImage);
                }
                imageDao.save(img);
            }
        } 
        catch (Exception e) {
            LOG.error("uploadImage error:"+e.getMessage());
            return "redirect:/error"; 
        }
        return "redirect:/registry/1";
    }
	
    /**
     * 
     * Description:
     * 导入和上传镜像
     * @param image Image
     * @param inputStream InputStream
     * @return flag boolean
     * @see
     */
    private boolean loadAndPushImage(Image image,String tarPath) throws IOException{
        InputStream uploadStream = Files.newInputStream(Paths.get(tarPath));
        boolean flag = false;
        try {
            // load and push image
            //String cmd = "docker load --input "+tarPath;
            //CmdUtil.exeCmd(cmd);
            flag = dockerClientService.loadAndPushImage(image, uploadStream);
            if(flag){
                //删除本地镜像
                flag = dockerClientService.removeImage(image.getName().split("/")[1], image.getVersion(),null,null,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 使用dockerFile构建之前判读DockerFile文件中是否包含基础镜像
     * 
     * @param dockerFile ：dockerFile文件
     * @return String 
     * @see
     */
    @RequestMapping(value={"ci/judgeBaseImage.do"},method=RequestMethod.GET)
    @ResponseBody
    public String judgeDocFileBaseImage(String dockerFile){
        Map<String, Object> map = new HashMap<String, Object>();
        String[] baseImage = dockerFileBaseImage(dockerFile);
        String name = baseImage[0];
        String varsion = baseImage[1];
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(varsion) ) {
            map.put("status", "400");
            return JSON.toJSONString(map);
        }
        
        Image image = imageDao.findByNameAndVersion(baseImage[0].substring(baseImage[0].indexOf("/") +1),baseImage[1]);
        if (null == image) {
            map.put("status", "500");
            return JSON.toJSONString(map);
        }
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
    
	/**
	 * 使用DockerFile构建
	 * 
	 * @param ci ： 构建数据
	 * @param sourceCodes ： 代码文件
	 * @param dockerFile ：dockerFile文本
	 * @return String
	 * @see
	 */
    @RequestMapping(value={"ci/addDockerFileCi.do"},method=RequestMethod.POST)
    public String addDockerFileCi(Ci ci,@RequestParam("sourceCode") MultipartFile[] sourceCodes,String dockerFile) {
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        String[] baseImage = dockerFileBaseImage(dockerFile);
        Image image = imageDao.findByNameAndVersion(baseImage[0].substring(baseImage[0].indexOf("/") +1),baseImage[1]);
        ci.setBaseImageId(image.getId());
        ci.setBaseImageName(baseImage[0]);
        ci.setBaseImageVersion(baseImage[1]);
        ci.setCreateBy(cuurentUser.getId());
        ci.setCreateDate(new Date());
        ci.setType(CiConstant.TYPE_DOCKERFILE);
        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
        ci.setConstructionDate(new Date());
        ci.setCodeLocation(CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
        ci.setDockerFileLocation("/");
        try {
            File file = new File(ci.getCodeLocation());
            if(!file.exists()){
                file.mkdirs();
            }
            for (MultipartFile sourceCode : sourceCodes) {
                if (!sourceCode.isEmpty()) {
                    ci.setResourceName(sourceCode.getOriginalFilename());
                    FileUtils.storeFile(sourceCode.getInputStream(), ci.getCodeLocation()+"/"+sourceCode.getOriginalFilename());
                }
            }
            if (!dockerFile.isEmpty()) {
                ByteArrayInputStream in=new ByteArrayInputStream(dockerFile.getBytes());  
                FileUtils.storeFile(in, ci.getCodeLocation()+"/"+"Dockerfile");
            }
        } 
        catch (Exception e) {
            LOG.error("modifyResourceCi error:"+e.getMessage());
            return "redirect:/error"; 
        }
        
        ciDao.save(ci);
        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
        
        return "redirect:/ci";

    }
    
	/**
	 * 从dockerFile文件中获取dockerFile构建的基础镜像
	 * 
	 * @param dockerFile ： dockerFile文件
	 * @return String
	 * @see
	 */
    public  String[] dockerFileBaseImage(String dockerFile){
        String[] baseImage = new String[10];
        try {
            if (dockerFile.indexOf("FROM") == -1) {
                return baseImage;
            }
            dockerFile = dockerFile.substring(dockerFile.indexOf("FROM")).replaceAll("\\s+", " ");
            String[] ssh = dockerFile.split(" ");
            System.out.println(ssh[1]);
            String name = ssh[1].substring(0, ssh[1].lastIndexOf(":"));
            String version = ssh[1].substring(ssh[1].lastIndexOf(":")+1);
            if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(version)) {
                baseImage[0] = name;
                baseImage[1] = version;
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return baseImage;
    }
	
    /**
     * 快速构建构建项目
     * 
     * @param ci ：ci 
     * @param sourceCode ： 上传代码
     * @return  String
     * @see
     */
    @RequestMapping("ci/addCodeResourceCi.do")
	public String addCodeResourceCi(Ci ci,@RequestParam("sourceCode") MultipartFile sourceCode) {
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        ci.setBaseImageName(dockerClientService.getDockerRegistryAddress() + "/" + ci.getBaseImageName());
        ci.setBaseImageVersion(imageDao.findById(ci.getBaseImageId()).getVersion());
        ci.setCreateBy(cuurentUser.getId());
        ci.setCreateDate(new Date());
        ci.setType(CiConstant.TYPE_QUICK);
        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
        ci.setConstructionDate(new Date());
        ci.setCodeLocation(CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
        ci.setDockerFileLocation("/");
        try {
            File file = new File(ci.getCodeLocation());
            if(!file.exists()){
                file.mkdirs();
            }
            if (!sourceCode.isEmpty()) {
                ci.setResourceName(sourceCode.getOriginalFilename());
                FileUtils.storeFile(sourceCode.getInputStream(), ci.getCodeLocation()+"/"+sourceCode.getOriginalFilename());
            }
        	
            String fileTemplate = null;
            if (ci.getResourceName().endsWith("war")) {
                fileTemplate = FileUtils.class.getClassLoader().getResource("Dockerfile").getPath();
            } 
            else {
                fileTemplate = FileUtils.class.getClassLoader().getResource("Dockerfilejar").getPath();
            }
        	
            String toFile = ci.getCodeLocation()+"/"+"Dockerfile";
            Map<String,String> data = new HashMap<String, String>();
            data.put("${baseImage}", ci.getBaseImageName()+":"+ci.getBaseImageVersion());
            data.put("${fileName}", ci.getResourceName());
            FileUtils.writeFileByLines(fileTemplate, data, toFile);
        } 
        catch (Exception e) {
            LOG.error("modifyResourceCi error:"+e.getMessage());
            return "redirect:/error"; 
        }
        ciDao.save(ci);
        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
        return "redirect:/ci";
    }
		
	/**
	 * 输出构建记录的详细信息
	 * 
	 * @param id : 构建Id
	 * @return String
	 * @see
	 */
    @RequestMapping("ci/printCiRecordLog.do")
	@ResponseBody
	public String printCiRecordLog(long id) {
        CiRecord ciRecord = ciRecordDao.findOne(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "200");
        map.put("data",ciRecord);
        return JSON.toJSONString(map);
    }
	
    /**
     * 创建完成之后，开始构建镜像；
     * 
     * @param id ： 构建Id
     * @return String
     * @see
     */
    @RequestMapping("ci/constructCi.do")
	@ResponseBody
	public String constructCi(Long id) {
        Ci ci = ciDao.findOne(id);
        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_ING);
        ci.setConstructionDate(new Date());
        ciDao.save(ci);
        long startTime = System.currentTimeMillis();
        CiRecord ciRecord = new CiRecord();
        ciRecord.setCiId(ci.getId());
        ciRecord.setCiVersion(ci.getImgNameVersion());
        ciRecord.setConstructDate(ci.getConstructionDate());
        ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_ING);
        ciRecord.setLogPrint("["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"start");
        ciRecordDao.save(ciRecord);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "200");
        boolean fetchCodeFlag = fetchCode(ci,ciRecord,ciRecordDao);
        if(!fetchCodeFlag){
            map.put("status", "500");
            map.put("msg", "获取代码出错,请检查代码地址和账号密码");
            ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
            ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
        }
        boolean constructFlag = construct(ci,ciRecord,ciRecordDao);
        if(!constructFlag){
            map.put("status", "500");
            map.put("msg", "构建镜像失败，请检查配置是否正确");
            ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
            ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
        }
        long endTime = System.currentTimeMillis();
        ci.setConstructionTime(endTime-startTime);
        ciRecord.setConstructTime(endTime-startTime);
        if(CiConstant.CONSTRUCTION_RESULT_FAIL!=ciRecord.getConstructResult()){
            ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_OK);
            ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_OK);
        }
        ciDao.save(ci);
        ciRecordDao.save(ciRecord);
        map.put("data", ci);
    
        return JSON.toJSONString(map);
    }
	
	/**
	 * 获取代码
	 * 
	 * @param ci ：ci
	 * @param ciRecord : ciRecord
	 * @param ciRecordDao : ciRecordDao
	 * @return boolean
	 * @see
	 */
    private boolean fetchCode(Ci ci,CiRecord ciRecord,CiRecordDao ciRecordDao){
		//如果是代码上传，不需要重新下载源代码
        if (CiConstant.TYPE_QUICK.equals(ci.getType()) || CiConstant.TYPE_DOCKERFILE.equals(ci.getType())) {
            return true;
        }
        try{
            if(CiConstant.CODE_TYPE_SVN.equals(ci.getCodeType())){
                String svnCommandStr = "svn export --username="+ci.getCodeUsername()+" --password="+ci.getCodePassword()+" "+ci.getCodeUrl()+" "+ci.getCodeLocation();
                ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"svn export");
                ciRecordDao.save(ciRecord);
                LOG.info("==========svnCommandStr:"+svnCommandStr);
                return CmdUtil.exeCmd(svnCommandStr);
            }
            else if(CiConstant.CODE_TYPE_GIT.equals(ci.getCodeType())){
                String codeUrl = ci.getCodeUrl();
                if(StringUtils.isEmpty(codeUrl)||codeUrl.indexOf("//")<=0){
                    return false;
                }
                String nCodeUrl = codeUrl;
                String codeUsername = ci.getCodeUsername();
                String codePassword = ci.getCodePassword();
                if(!StringUtils.isEmpty(codeUsername)&&!StringUtils.isEmpty(codePassword)){
					//如果有账号密码
                    if(ci.getCodeUsername().indexOf("@")>0){
						//如果git用户名是带@邮箱格式的，则去掉邮箱后缀
                        codeUsername = codeUsername.substring(0, codeUsername.indexOf("@"));
                    }
                    nCodeUrl = codeUrl.substring(0,codeUrl.indexOf("//")+2)+codeUsername+":"+codePassword+"@"+codeUrl.substring(codeUrl.indexOf("//")+2,codeUrl.length());
                }
                String rmCommonStr = "rm -rf "+ci.getCodeLocation();
                LOG.info("==========rmCommonStr:"+rmCommonStr);
                CmdUtil.exeCmd(rmCommonStr);
                String gitCommandStr = "git clone "+nCodeUrl+" "+ci.getCodeLocation();
                String logmsg = "git clone " +
                                    codeUrl.substring(0,codeUrl.indexOf("//")+2)+ci.getCodeUsername() +
                                    ":******@"+codeUrl.substring(codeUrl.indexOf("//")+2,codeUrl.length())+" "+ci.getCodeLocation();
                ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+logmsg);
                ciRecordDao.save(ciRecord);
                LOG.info("==========gitCommandStr:"+gitCommandStr);
                return CmdUtil.exeCmd(gitCommandStr);
            }
        }
        catch(Exception e){
            ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
            ciRecordDao.save(ciRecord);
            LOG.error("==========fetchCode error:"+e.getMessage());
        }
        return false;
    }
	
    /**
     * 构建镜像
     * 
     * @param ci ：ci
     * @param ciRecord : cirecord
     * @param ciRecordDao :ciRecordDao
     * @return boolean
     * @see
     */
    private boolean construct(Ci ci,CiRecord ciRecord,CiRecordDao ciRecordDao){
		//构建镜像
        String dockerfilePath = ci.getCodeLocation()+ci.getDockerFileLocation();
        String imageName = ci.getImgNameFirst()+"/"+ci.getImgNameLast();
        String imageVersion = ci.getImgNameVersion();
        Image imageId = new Image();
		
        DockerClient dockerClient = dockerClientService.getNormalDockerClientInstance();
        boolean flag = dockerClientService.buildImage(dockerfilePath,imageName, imageVersion,ciRecord,ciRecordDao,imageId, dockerClient);
        if(flag){
		    //上传镜像
            flag = dockerClientService.pushImage(imageName, imageVersion,ciRecord,ciRecordDao,dockerClient);
        }
        if(flag){
			//删除本地镜像
            flag = dockerClientService.removeImage(imageName, imageVersion,ciRecord,ciRecordDao,dockerClient);
        }
        ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"end");
        ciRecordDao.save(ciRecord);
        if(flag){
			//排重添加镜像数据
            Image img = null;
            List<Image> imageList = imageDao.findByName(imageName);
            if(!CollectionUtils.isEmpty(imageList)){
                for(Image image:imageList){
                    if(image.getVersion().equals(imageVersion)){
                        img = image;
                    }
                }
            }
            if(img==null){
                img = new Image();
                img.setName(imageName);
                img.setVersion(imageVersion);
            }
            img.setImageId(imageId.getImageId());
            img.setResourceName(ci.getResourceName());
            img.setImageType(ci.getImgType());
            img.setRemark(ci.getDescription());
            img.setCreator(ci.getCreateBy());
            img.setCreateTime(new Date());
            img.setIsBaseImage(ImageConstant.NotBaseImage);
            img.setIsDelete(CommConstant.TYPE_NO_VALUE);
            imageDao.save(img);
            ci.setImgId(img.getId());
        }
        return flag;
    }
	
    /**
     * 查找基础镜像的版本
     * 
     * @param baseImageName ： 基础镜像的名称
     * @return String
     * @see
     */
    @RequestMapping("ci/findBaseImageVersion.do")
	@ResponseBody
	public String findBaseImage(String baseImageName){
        User cUser = CurrentUserUtils.getInstance().getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Image> images = imageDao.findByBaseImageVarsionOfName(cUser.getId(), baseImageName);
        map.put("data", images);
        
        return JSON.toJSONString(map);
    }
	
	/**
	 * 查询基础镜像，去掉重复名称的基础镜像
	 * 
	 * @return  list
	 * @see
	 */
    public List<Image> findByBaseImages(){
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        List<Image> images = imageDao.findByBaseImage(cuurentUser.getId());
        List<Image> result = new ArrayList<Image>();
        //去掉镜像名称相同的镜像
        if (!CollectionUtils.isEmpty(images)) {
            for (Image oneRow : images) {
                if (result.size() < 0) {
                    result.add(oneRow);
                } else {
                    boolean flag = false;
                    for (Image image : result) {
                        if (oneRow.getName().trim().equals(image.getName().trim())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        result.add(oneRow);
                    }
                }
            }
        }
        return result;
    }
	
	/**
     * 加载DockerFile模板数据
     * 
     * @return String
     * @see
     */
    @RequestMapping("ci/loadDockerFileTemplate.do")
    @ResponseBody
    public String loadEnvTemplate(){
        Map<String, Object> map = new HashMap<String, Object>();
        User cuurentUser = CurrentUserUtils.getInstance().getUser();
        List<DockerFileTemplate> dockerFiles = dockerFileTemplateDao.findByCreateBy(cuurentUser.getId());
        map.put("data", dockerFiles);
        return JSON.toJSONString(map);
    }
	
	/**
	 * dockerFile模板保存，匹配模板名称是否重复
	 * 
	 * @param templateName ： 模板名称
	 * @param dockerFile ： dockerFile文件
	 * @return String
	 * @see
	 */
    @RequestMapping("ci/saveDockerFileTemplate.do")
	@ResponseBody
    public String saveDockerFileTemplate (String templateName,String dockerFile) {
        Map<String, Object> map = new HashMap<String, Object>();
        User cUser = CurrentUserUtils.getInstance().getUser();
        for (DockerFileTemplate dkFile : dockerFileTemplateDao.findByCreateBy(cUser.getId())) {
            if (StringUtils.isEmpty(dkFile.getTemplateName())) {
                continue;
            }
            if (dkFile.getTemplateName().equals(templateName)) {
                map.put("status", "400");
                return JSON.toJSONString(map);
            }
        }
        DockerFileTemplate dkFile = new DockerFileTemplate();
        dkFile.setCreateBy(cUser.getId());
        dkFile.setCreateDate(new Date());
        dkFile.setDockerFile(dockerFile);
        dkFile.setTemplateName(templateName);
        dockerFileTemplateDao.save(dkFile);
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = {"ci/validciinfo.do"} , method = RequestMethod.POST)
    @ResponseBody
    public String validCiInfo(Model model, String imgNameLast, String imgNameVersion) {
        Map<String,Object> result = new HashMap<String, Object>();
        String imgNameFirst = CurrentUserUtils.getInstance().getUser().getUserName();
        if (!StringUtils.isEmpty(imgNameFirst) && !StringUtils.isEmpty(imgNameLast) && !StringUtils.isEmpty(imgNameVersion)) {
            Image image = imageDao.findByNameAndVersion(imgNameFirst+"/"+imgNameLast, imgNameVersion);
//            List<Ci> ciList = ciDao.findByImgNameFirstAndImgNameLastAndImgNameVersion(imgNameFirst,imgNameLast,imgNameVersion);
            if (!StringUtils.isEmpty(image)) {
                result.put("status", "400");
            }
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = {"ci/validimageinfo.do"} , method = RequestMethod.POST)
    @ResponseBody
    public String validImageInfo(Model model, String name, String version) {
        Map<String,Object> result = new HashMap<String, Object>();
        name = CurrentUserUtils.getInstance().getUser().getUserName()+"/"+name;
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(version)) {
            Image image = imageDao.findByNameAndVersion(name, version);
            if (null != image) {
                result.put("status", "400");
            }
        }
        return JSON.toJSONString(result);
    }
}
