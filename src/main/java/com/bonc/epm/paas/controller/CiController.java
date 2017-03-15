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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.constant.CiConstant;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.ImageConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.CiCodeCredentialDao;
import com.bonc.epm.paas.dao.CiCodeDao;
import com.bonc.epm.paas.dao.CiCodeHookDao;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.CiInvokeDao;
import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.DockerFileTemplateDao;
import com.bonc.epm.paas.dao.HookAndImagesDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.SheraDao;
import com.bonc.epm.paas.dao.CodeCiToolDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiCode;
import com.bonc.epm.paas.entity.CiCodeCredential;
import com.bonc.epm.paas.entity.CiCodeHook;
import com.bonc.epm.paas.entity.CiInvoke;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.CommonOprationLogUtils;
import com.bonc.epm.paas.entity.DockerFileTemplate;
import com.bonc.epm.paas.entity.HookAndImages;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.Shera;
import com.bonc.epm.paas.entity.CodeCiTool;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.model.CredentialCheckEntity;
import com.bonc.epm.paas.shera.model.JdkList;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecView;
import com.bonc.epm.paas.shera.model.Log;
import com.bonc.epm.paas.shera.util.SheraClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.DateUtils;
import com.bonc.epm.paas.util.FileUtils;
import com.bonc.epm.paas.util.ResultPager;
import com.bonc.epm.paas.util.ZipUtil;
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
     * userResourceDao
     */
    @Autowired
    private UserResourceDao userResourceDao;

    /**
     * CiDao接口
     */
    @Autowired
	private CiDao ciDao;

    /**
     * CiCode接口
     */
    @Autowired
    private CiCodeDao ciCodeDao;

    /**
     * CiRecoredDao接口
     */
    @Autowired
	private CiRecordDao ciRecordDao;

    /**
     * ciInvokeDao接口
     */
    @Autowired
    private CiInvokeDao ciInvokeDao;

    /**
     * ciCodeCredentialDao接口
     */
    @Autowired
    private CiCodeCredentialDao ciCodeCredentialDao;

    /**
     * 代码挂钩接口
     */
    @Autowired
    private CiCodeHookDao ciCodeHookDao;

    /**
     * sheraDao数据接口
     */
    @Autowired
    private SheraDao sheraDao;

    /**
     * hookAndImages中间表接口
     */
    @Autowired
    private HookAndImagesDao hookAndImagesDao;

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
     * SheraClientService 接口
     */
    @Autowired
    private SheraClientService sheraClientService;

    /**
     * commonOperationLogDao接口
     */
    @Autowired
    private CommonOperationLogDao commonOperationLogDao;

    /**
     * commonOperationLogDao接口
     */
    @Autowired
    private CodeCiToolDao codeCiToolDao;

    /**
     * 进入构建主页面
     *
     * @param model ：model
     * @return ci.jsp
     * @see
     */
    @RequestMapping(value={"ci"},method=RequestMethod.GET)
	public String index(Model model){
        model.addAttribute("menu_flag", "ci");
        model.addAttribute("li_flag", "ci");
        return "ci/ci.jsp";
    }

    /**
     * Description: <br>
     * 快速构建和dockerfile构建信息的查询
     * @param draw 画图
     * @param start 开始页码
     * @param length 页面长度
     * @param request ：
     * @return String
     */
    @RequestMapping(value = {"ci/page.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String findCiByPage(String draw, int start,int length,HttpServletRequest request){
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
            cis = ciDao.findByCreateByQuickCi(userId,pageRequest);
        } else {
            cis = ciDao.findByNameOfQuickCi(userId, "%" + search + "%",pageRequest);
        }
        map.put("draw", draw);
        map.put("recordsTotal", cis.getTotalElements());
        map.put("recordsFiltered", cis.getTotalElements());
        map.put("data", cis.getContent());
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * codeCi构建数据的查询
     * @param draw ： 画布
     * @param start：开始页数
     * @param length 页面长度
     * @param request ：
     * @return String
     */
    @RequestMapping(value = {"ci/codepage.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String findCodeCiByPage(String draw, int start,int length,
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
            cis = ciDao.findByCreateByCodeCi(userId,pageRequest);
        } else {
            cis = ciDao.findByNameOfCodeCi(userId, "%" + search + "%",pageRequest);
        }
        //判断是否获取shera信息
        if (cis.getContent().size() > 0) {
            List<Ci> ciList = findSheraData(cis.getContent());
            map.put("data", ciList);
        } else {
            map.put("data", cis.getContent());
        }
        map.put("draw", draw);
        map.put("recordsTotal", cis.getTotalElements());
        map.put("recordsFiltered", cis.getTotalElements());
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 代码构建shera中job的构建信息查询和展示；
     * @param cis cis
     * @return
     */
    public List<Ci> findSheraData(List<Ci> cis){
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            JobExecList jobExecList = client.getAllJobs();
            if (StringUtils.isEmpty(jobExecList.getItems())) {
                return cis;
            }
            if (cis.size()!=0 && jobExecList != null) {
                for (Ci ci :cis) {
                    for (JobExec jobExec :jobExecList) {
                        if (ci.getProjectName().equals(jobExec.getJobId())) {
                            if (jobExec.getLastSuccessTime() != 0 ) {
                                ci.setConstructionDate(DateUtils.getLongToDate(jobExec.getLastSuccessTime()));
                            }
                            if (jobExec.getLastFailureTime() != 0) {
                                ci.setConstructionFailDate(DateUtils.getLongToDate(jobExec.getLastFailureTime()));
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.error("find all job data error : " + e.getMessage());
            e.printStackTrace();
        }
        return cis;
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
        //代码构建
        if (ci.getType() == 1) {
            try {
                //获取jdk信息
                SheraAPIClientInterface client = sheraClientService.getClient();
                JdkList jdkList = client.getAllJdk();
                Job job = client.getJob(ci.getProjectName());
                CiCode ciCode = ciCodeDao.findByCiId(ci.getId());
                //代码验证信息的查询和加载
                Iterable<CiCodeCredential> ciCredentialList = ciCodeCredentialDao.findAll();
        		List<Object> toolGroups = getToolGroups();
        		String basicImage = codeCiToolDao.findAllImages().get(0).gettoolCode();

        		model.addAttribute("ciCode", ciCode);
        		model.addAttribute("basicImage", basicImage);
                model.addAttribute("toolGroups", toolGroups);
                model.addAttribute("ciCredentialList", ciCredentialList);
				model.addAttribute("dockerFileContent", job.getImgManager().getDockerFileContent()
						.replace("\"", "&quot;").replace("'", "&apos;").replace(">", "&gt;").replace("<", "&lt;"));
                model.addAttribute("jdkList",jdkList.getItems());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        //dockerfile构建
        if (ci.getType() == 2) {
            List<DockerFileTemplate> dockerFiles = dockerFileTemplateDao.findByCreateBy(cuurentUser.getId());
            File file = new File(ci.getCodeLocation()+"/"+"Dockerfile");
            if (file.exists()) {
                String dockerFileTxt = FileUtils.readFileByLines(ci.getCodeLocation()+"/"+"Dockerfile");
                model.addAttribute("dockerFileTxt", dockerFileTxt);
            }
            model.addAttribute("dockerFiles", dockerFiles);
        }
        //快速构建
        if (ci.getType() == 3) {
            Image currentBaseImage = imageDao.findById(ci.getBaseImageId());
            List<Image> images = this.findByBaseImages();
            model.addAttribute("currentBaseImage",currentBaseImage);
            model.addAttribute("baseImage", images);
        }
        model.addAttribute("ci", ci);
        model.addAttribute("ciRecordList", ciRecordList);
        model.addAttribute("docker_regisgtry_address", dockerClientService.getDockerRegistryAddress());
        model.addAttribute("userAutority", cuurentUser.getUser_autority());
        model.addAttribute("menu_flag", "ci");
        model.addAttribute("li_flag", "ci");
        return "ci/ci_detail.jsp";
    }

    /**
     * Description: <br>
     * 根据构建id查询相关联的代码构建详细数据
     * @param id
     * @return
     * @see
     */
    @RequestMapping("ci/invokeData.do")
    @ResponseBody
    public String fingCiInvokeData(long id){
        Map<String, Object> map = new HashMap<String, Object>();
        List<CiInvoke> ciInvokeList = ciInvokeDao.findByCiId(id);
        map.put("data", ciInvokeList);
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 修改构建信息判断项目名称是否重复
     * @param projectName ： 项目名称
     * @param id 构建Id
     * @return
     */
    @RequestMapping("ci/judgeProjectName.do")
    @ResponseBody
    public String judgeModifyProjectName(String projectName,long id){
        long createBy = CurrentUserUtils.getInstance().getUser().getId();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Ci> ciList = ciDao.findByProjectNameAndCreateBy(projectName,createBy);
        if (ciList.size() > 1) {
            map.put("status", "400");
        }
        if (ciList.size() == 1) {
            if (ciList.get(0).getId() == id) {
                map.put("status", "200");
            }else {
                map.put("status","400");
            }
        }
        if (ciList.size() == 0) {
            map.put("status", "200");
        }
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 修改代码构建信息
     * @param ci ： ci
     * @param jsonData ： invoke数据
     * @param dockerFileContentEdit ： dockerfile文件
     * @return String
     */
    @RequestMapping("ci/modifyCodeCi.do")
	@ResponseBody
	public String modifyCodeCi(Ci ci,CiCode ciCode,String jsonData,String dockerFileContentEdit) {
        User user =CurrentUserUtils.getInstance().getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        String extraInfo= "修改代码构建 ：" + ci.getProjectName() + "的详细信息,原始数据：";    //日志信息
        Ci originCi = ciDao.findOne(ci.getId());
        CiCode originCiCode = ciCodeDao.findByCiId(ci.getId());
        extraInfo += JSON.toJSONString(originCi) + JSON.toJSONString(originCiCode) ;
        originCi.setProjectName(ci.getProjectName());
        originCi.setImgNameVersion(ci.getImgNameVersion());
        originCi.setImgNameLast(ci.getImgNameLast());
        originCi.setImgType(ci.getImgType());
        originCi.setDescription(ci.getDescription());
        originCi.setDockerFileLocation(ci.getDockerFileLocation());
        originCi.setDockerFileContent(dockerFileContentEdit);
        originCiCode.setIsBaseImage(ciCode.getIsBaseImage());
        originCiCode.setJdkVersion(ciCode.getJdkVersion());
        originCiCode.setCodeType(ciCode.getCodeType());
        originCiCode.setCodeUrl(ciCode.getCodeUrl().trim());
        originCiCode.setCodeCredentials(ciCode.getCodeCredentials());
        originCiCode.setCodeBranch(ciCode.getCodeBranch());
        originCiCode.setIsHookCode(ciCode.getIsHookCode());
        originCiCode.setCodeName(ciCode.getCodeName());
        originCiCode.setCodeRefspec(ciCode.getCodeRefspec());
        originCiCode.setCiTools(ciCode.getCiTools());
        List<CiInvoke> ciInvokeList = addCiInvokes(jsonData,ci.getId());
        CiCodeCredential ciCodeCredential = new CiCodeCredential();
        if (!StringUtils.isEmpty(ciCode.getCodeCredentials())) {
            ciCodeCredential = ciCodeCredentialDao.findOne(ciCode.getCodeCredentials());
        }
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            Job job = sheraClientService.generateJob(ci.getProjectName(),ciCode.getJdkVersion(),ciCode.getCodeBranch(),ciCode.getCodeUrl(),
                                                            ciCode.getCodeName(),ciCode.getCodeRefspec(),dockerFileContentEdit,ci.getDockerFileLocation(),
                                                            originCi.getImgNameFirst(),ci.getImgNameLast(),ciInvokeList,ciCodeCredential.getUserName(),
                                                            ciCodeCredential.getType(),ciCode.getCodeType(),ciCodeCredential.getUniqueKey(),ciCode.getCiTools());
            client.updateJob(job);
            //添加代码挂钩
            if (ciCode.getIsHookCode() == 1) {
                ChangeGit changeGit = sheraClientService.generateChangeGit(user.getNamespace(), ci.getProjectName(),
                                                                                   ciCode.getCodeUrl(), ciCode.getCodeBranch());
                changeGit = client.addGitHooks(ci.getProjectName(), changeGit);
                CiCodeHook ciCodeHook = new CiCodeHook();
                if (!StringUtils.isEmpty(originCiCode.getHookCodeId()) && originCiCode.getHookCodeId() !=0 ) {
                    ciCodeHook = ciCodeHookDao.findOne(originCiCode.getHookCodeId());
                }
                ciCodeHook.setCreateDate(new Date());
                ciCodeHook.setNamespace(changeGit.getNamespace());
                ciCodeHook.setName(changeGit.getName());
                ciCodeHook.setBranch(changeGit.getBranch());
                ciCodeHook.setGiturl(changeGit.getGiturl());
                ciCodeHook.setFlag(false);
                ciCodeHookDao.save(ciCodeHook);
                originCiCode.setHookCodeId(ciCodeHook.getId());
            }
            ciDao.save(originCi);
            ciCodeDao.save(originCiCode);
            if (!StringUtils.isEmpty(ciInvokeList)) {
                ciInvokeDao.deleteByCiId(ci.getId());
                ciInvokeDao.save(ciInvokeList);
            }
            map.put("status", "200");

            //保存日志信息
            extraInfo += "修改之后的数据：" + JSON.toJSONString(originCi) +  JSON.toJSONString(originCiCode);
            CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.CODE_CI, CommConstant.OPERATION_TYPE_UPDATE);
            commonOperationLogDao.save(log);
        }
        catch (Exception e) {
            LOG.error("modifyCodeCi error : "+e.getMessage());
            map.put("status", "400");
        }
        map.put("data", originCi);
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
        String extraInfo= "修改DockerFile构建 " + ci.getProjectName() + "的详细信息,原始数据：" + JSON.toJSONString(originCi);    //日志信息
        originCi.setProjectName(ci.getProjectName());
        originCi.setDescription(ci.getDescription());
        originCi.setDockerFileContent(dockerFile);
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
            LOG.error("modifyResourceCi error : "+e.getMessage());
            map.put("status", "500");
            map.put("msg","上传文件出错");

            return JSON.toJSONString(map);
        }
        ciDao.save(originCi);

        //保存日志信息
        extraInfo += "修改之后的数据：" + JSON.toJSONString(originCi);
        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.DOCKER_FILE_CI, CommConstant.OPERATION_TYPE_UPDATE);
        commonOperationLogDao.save(log);
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
        String extraInfo= "修改快速构建 " + ci.getProjectName() + "的详细信息,原始数据：" + JSON.toJSONString(originCi);    //日志信息
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
            LOG.error("modifyResourceCi error : "+e.getMessage());
            map.put("status", "500");
            map.put("msg","上传文件出错");

            return JSON.toJSONString(map);
        }
        ciDao.save(originCi);

        //保存日志信息
        extraInfo += "修改之后的数据：" + JSON.toJSONString(originCi);
        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.QUICK_CI, CommConstant.OPERATION_TYPE_UPDATE);
        commonOperationLogDao.save(log);
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
        Map<String, Object> map = new HashMap<String, Object>();
        Integer catalogType = CommConstant.CODE_CI;

        try {
            Long idl = Long.parseLong(id);
            Ci ci = ciDao.findOne(idl);
            if (ci.getType() == CiConstant.TYPE_DOCKERFILE) {
                catalogType = CommConstant.DOCKER_FILE_CI;
            } else if (ci.getType() == CiConstant.TYPE_QUICK) {
                catalogType = CommConstant.QUICK_CI;
            }
            //判断是否为代码构建
            if (ci.getType() == CiConstant.TYPE_CODE) {

                try {
                    CiCode ciCode = ciCodeDao.findByCiId(idl);
                    SheraAPIClientInterface client = sheraClientService.getClient();
                    client.deleteJob(ci.getProjectName());
                    //删除关联的hook数据
                    if (!StringUtils.isEmpty(ciCode.getHookCodeId()) && ciCode.getHookCodeId() !=0 ) {
                        CiCodeHook ciCodeHook = ciCodeHookDao.findOne(ciCode.getHookCodeId());
                        hookAndImagesDao.deleteByHookId(ciCodeHook.getId());
                        ciCodeHookDao.delete(ciCodeHook);
                    }
                    ciInvokeDao.deleteByCiId(idl);
                    ciCodeDao.delete(ciCode);
                }
                catch (SheraClientException e) {
                    e.printStackTrace();
                }
            }
            ciRecordDao.deleteByCiId(idl);
            ciDao.delete(idl);
            //添加删除日志
            String extraInfo = "删除构建：" + JSON.toJSONString(ci);
            CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, catalogType, CommConstant.OPERATION_TYPE_DELETE);
            commonOperationLogDao.save(log);
            map.put("status", "200");
            map.put("type", ci.getType());
        }
        catch (Exception e) {
            LOG.error("delete Ci error : " + e.getMessage());
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 进入代码构建页面
     *
     * @param model ： model
     * @return ci_add.jsp
     * @see
     */
	@RequestMapping(value = { "ci/add" }, method = RequestMethod.GET)
	public String addProject(Model model) {
		User cuurentUser = CurrentUserUtils.getInstance().getUser();
		try {
			SheraAPIClientInterface client = sheraClientService.getClient();
			JdkList jdkList = client.getAllJdk();
			Iterable<CiCodeCredential> ciCodeList = ciCodeCredentialDao.findAll();
			model.addAttribute("ciCodeList", ciCodeList);
			model.addAttribute("jdkList", jdkList.getItems());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String imageNameFirst = "";
		if (cuurentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
			imageNameFirst = cuurentUser.getNamespace() + "_" + cuurentUser.getUserName();
		} else {
			imageNameFirst = cuurentUser.getUserName();
		}

		String basicImage = new String();

		List<CodeCiTool> allImages = codeCiToolDao.findAllImages();
		if (!CollectionUtils.isEmpty(allImages)) {
			basicImage = allImages.get(0).gettoolCode();
		}

		List<Object> toolGroups = getToolGroups();

		model.addAttribute("username", imageNameFirst);
		model.addAttribute("basicImage", basicImage);
		model.addAttribute("toolGroups", toolGroups);
		model.addAttribute("userAutority", cuurentUser.getUser_autority());
		model.addAttribute("menu_flag", "ci");
		model.addAttribute("li_flag", "ci");
		return "ci/ci_add.jsp";
	}


	private List<Object> getToolGroups() {
		List<CodeCiTool> allTools = codeCiToolDao.findAllTools();
		List<Object> toolGroups = new ArrayList<>();
		List<CodeCiTool> tools = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("tools", tools);
		toolGroups.add(map);
		for (int i = 0; i < allTools.size(); i++) {
			if (i == 0) {
				map.put("groupName", allTools.get(i).gettoolGroup());
				tools.add(allTools.get(i));
			} else {
				if (allTools.get(i).gettoolGroup().equals(allTools.get(i - 1).gettoolGroup())) {
					tools.add(allTools.get(i));
				} else {
					tools = new ArrayList<>();
					map = new HashMap<>();
					map.put("tools", tools);
					toolGroups.add(map);
					map.put("groupName", allTools.get(i).gettoolGroup());
					tools.add(allTools.get(i));
				}
			}
		}
		return toolGroups;
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
        String imageNameFirst = "";
        if (cuurentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
            imageNameFirst = cuurentUser.getNamespace() + "_" + cuurentUser.getUserName();
        } else {
            imageNameFirst = cuurentUser.getUserName();
        }
        model.addAttribute("username", imageNameFirst);
        model.addAttribute("menu_flag", "ci");
        model.addAttribute("li_flag", "ci");
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
        String imageNameFirst = "";
        if (cuurentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
            imageNameFirst = cuurentUser.getNamespace() + "_" + cuurentUser.getUserName();
        } else {
            imageNameFirst = cuurentUser.getUserName();
        }
        model.addAttribute("username", imageNameFirst);
        model.addAttribute("menu_flag", "ci");
        model.addAttribute("li", "ci");
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
        String imageNameFirst = "";
        if (cuurentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
            imageNameFirst = cuurentUser.getNamespace() + "_" + cuurentUser.getUserName();
        } else {
            imageNameFirst = cuurentUser.getUserName();
        }
        model.addAttribute("username", imageNameFirst);
        model.addAttribute("menu_flag", "ci");
        model.addAttribute("li", "ci");
        model.addAttribute("docker_regisgtry_address", dockerClientService.getDockerRegistryAddress());
        model.addAttribute("baseImage", images);
        return "ci/ci_addCodeSource.jsp";
    }

    /**
     * Description: <br>
     * 判断用户镜像数量
     * @param model
     * @return String
     */
    @RequestMapping(value={"ci/judgeUserImages.do"},method=RequestMethod.GET)
    @ResponseBody
    public String judgeUserImages(Model model) {
        Map<String, Object> map = new HashMap<String, Object>();
        User currentUser = CurrentUserUtils.getInstance().getUser();
        //取得当前用户的所有镜像
        List<Image> imagesOfUser = imageDao.findByCreateBy(currentUser.getId());
        long maxSize = 0;
        if (currentUser.getUser_autority().equals(UserConstant.AUTORITY_TENANT) ||
                    currentUser.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)) {
        	//当前是租户的场合，maxSize为租户的ImageCount字段
        	maxSize = userResourceDao.findByUserId(currentUser.getId()).getImage_count();
		} else if (currentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
			//当前是用户的场合，maxSize为该用户所在租户的ImageCount字段
			maxSize = userResourceDao.findByUserId(currentUser.getParent_id()).getImage_count();
		} else {
			map.put("overwhelm", false);
			return JSON.toJSONString(map);
		}
        //如果当前用户创建镜像不为空&&创建的镜像数大于maxSize时，返回true
        if (!CollectionUtils.isEmpty(imagesOfUser) && maxSize <= imagesOfUser.size()) {
            map.put("overwhelm", true);
        } else {
            map.put("overwhelm", false);
        }
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 进行代码构建时判断当前租户是否添加了shera环境
     * @return String
     * @see
     */
    @RequestMapping("ci/judgeShera.do")
    @ResponseBody
    public String judgeShera(){
        Map<String,Object> map = new HashMap<>();
        User user = CurrentUserUtils.getInstance().getUser();
        Shera shera = new Shera();
        //用户使用租户的shera环境
        if (user.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
            shera = sheraDao.findByUserId(user.getParent_id());
        }
        else {
            shera = sheraDao.findByUserId(user.getId());
        }
        if (StringUtils.isEmpty(shera)) {
            map.put("status", "400");
        }
        else {
            map.put("status", "200");
        }
        return JSON.toJSONString(map);

    }

    /**
     * Description: <br>
     * 代码构建验证代码地址是否正确
     * @param codeUrl
     * @param codeCredentialId
     * @return
     * @see
     */
    @RequestMapping(value={"ci/judgeCodeUrl.do"},method=RequestMethod.POST)
    @ResponseBody
    public String judgeCodeUrl(String codeUrl,long codeCredentialId){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            CiCodeCredential ciCodeCredential = ciCodeCredentialDao.findOne(codeCredentialId);
            SheraAPIClientInterface client = sheraClientService.getClient();
            Integer Credential = 0;
            if (ciCodeCredential.getCodeType() == 1) {
                Credential = ciCodeCredential.getType();  // 1 : git http 类型，2：git ssh类型
            }
            else {
                Credential = ciCodeCredential.getType() + 2;  // 3 ：svn http类型，4：svn ssh类型
            }
            CredentialCheckEntity credentialCheckEntity = sheraClientService.generateCredentialCheckEntity(codeUrl, ciCodeCredential.getUserName(), Credential,ciCodeCredential.getRemark(),ciCodeCredential.getUniqueKey());
            try {
                credentialCheckEntity = client.checkCredential(credentialCheckEntity);
                if (StringUtils.isEmpty(credentialCheckEntity)) {   //判断返回结果是否为空
                    map.put("status", "400");
                }
            }
            catch (SheraClientException e) {
                LOG.error("judge codeUrl by credential error : "+e.getMessage());
                map.put("status", "400");
            }
        }
        catch (Exception e) {
            LOG.error("shera connection error : "+e.getMessage());
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 代码构建的创建
     *
     * @param ci ： ci
     * @return String
     * @see
     */
    @RequestMapping("ci/addCodeCi.do")
    public String addCodeCi(Ci ci,CiCode ciCode,String jsonData,String dockerFileContent){
            User cuurentUser = CurrentUserUtils.getInstance().getUser();
            ci.setCreateBy(cuurentUser.getId());
            String imageNameFirst = "";
            if (cuurentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
                imageNameFirst = cuurentUser.getNamespace() + "_" + cuurentUser.getUserName();
            } else {
                imageNameFirst = cuurentUser.getUserName();
            }
            ci.setImgNameFirst(imageNameFirst);
            ci.setCreateDate(new Date());
            ci.setType(CiConstant.TYPE_CODE);
            ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
            ci.setDockerFileContent(dockerFileContent);
            ciDao.save(ci);

            //添加代码构建详细信息
            ciCode.setCiId(ci.getId());
            ciCode.setCodeUrl(ciCode.getCodeUrl().trim());
            ciCodeDao.save(ciCode);

			//查询代码构建详细信息
            List<CiInvoke> ciInvokeList = addCiInvokes(jsonData,ci.getId());
            if (!StringUtils.isEmpty(ciInvokeList)) {
                ciInvokeDao.save(ciInvokeList);
                LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
            }

            //添加日志
            String extraInfo = "代码构建的创建：" + JSON.toJSONString(ci) + JSON.toJSONString(ciCode) + JSON.toJSONString(ciInvokeList);
            CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.CODE_CI, CommConstant.OPERATION_TYPE_CREATED);
            commonOperationLogDao.save(log);

            //查询代码认证
            CiCodeCredential ciCodeCredential = new CiCodeCredential();
            if (!StringUtils.isEmpty(ciCode.getCodeCredentials())) {
                ciCodeCredential = ciCodeCredentialDao.findOne(ciCode.getCodeCredentials());
            }
            try {
                SheraAPIClientInterface client = sheraClientService.getClient();
                Job job = sheraClientService.generateJob(ci.getProjectName(),ciCode.getJdkVersion(),ciCode.getCodeBranch(),ciCode.getCodeUrl(),
                    ciCode.getCodeName(),ciCode.getCodeRefspec(), dockerFileContent,ci.getDockerFileLocation(),
                    ci.getImgNameFirst(),ci.getImgNameLast(),ciInvokeList,ciCodeCredential.getUserName(),
                    ciCodeCredential.getType(),ciCode.getCodeType(),ciCodeCredential.getUniqueKey(),ciCode.getCiTools());
                client.createJob(job);
                //添加代码挂钩
                if (ciCode.getIsHookCode() == 1) {
                    ChangeGit changeGit = sheraClientService.generateChangeGit(cuurentUser.getNamespace(), ci.getProjectName(), ciCode.getCodeUrl(), ciCode.getCodeBranch());
                    changeGit = client.addGitHooks(ci.getProjectName(), changeGit);
                    CiCodeHook ciCodeHook = new CiCodeHook();
                    ciCodeHook.setCreateDate(new Date());
                    ciCodeHook.setNamespace(changeGit.getNamespace());
                    ciCodeHook.setName(changeGit.getName());
                    ciCodeHook.setBranch(changeGit.getBranch());
                    ciCodeHook.setGiturl(changeGit.getGiturl());
                    ciCodeHook.setFlag(changeGit.isFlag());
                    ciCodeHookDao.save(ciCodeHook);
                    ciCode.setHookCodeId(ciCodeHook.getId());
                    ciCodeDao.save(ciCode);
                }
            }
            catch (Exception e) {
                LOG.error("create job error : "+e.getMessage());
                ciInvokeDao.deleteByCiId(ci.getId());
                ciDao.delete(ci);
            }
        return "redirect:/ci?code";
    }

    /**
     * Description: <br>
     * 将json数据封装成ciInvoke对象；
     * @param jsonData json数据；
     * @param ciId 构建Id
     * @return list
     */
    public List<CiInvoke> addCiInvokes(String jsonData,long ciId){
        List<CiInvoke> ciInvokeList = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONArray.parseArray(jsonData);
            for (int i = 0 ; i < jsonArray.size();i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                CiInvoke ciInvoke = (CiInvoke)JSONObject.toJavaObject(jsonObj, CiInvoke.class);
                ciInvoke.setCiId(ciId);
                ciInvoke.setJobOrderId(i+1);
                ciInvoke.setCreateBy(CurrentUserUtils.getInstance().getUser().getId());
                ciInvoke.setCreateDate(new Date());
                ciInvokeList.add(ciInvoke);
            }
        }
        catch (Exception e) {
            LOG.error("add ciInvoke error : "+e.getMessage());
            return null;
        }
        return ciInvokeList;
    }

    /**
     * Description: <br>
     * 根据项目名称和创建者判断项目名称的重复
     * @param projectName ： 项目名称
     * @return ：map
     */
    @RequestMapping("ci/judgeCodeCiName.do")
    @ResponseBody
    public String judgeCodeCiName(String projectName){
        Map<String,Object> result = new HashMap<String, Object>();
        long createBy = CurrentUserUtils.getInstance().getUser().getId();
        List<Ci> ciList = ciDao.findByProjectNameAndCreateBy(projectName,createBy);
        if (ciList.size() > 0) {
            result.put("status", "400");
        }
        else {
            result.put("status", "200");
        }
        return JSON.toJSONString(result);
    }

//    /**
//     * 快速构建的创建
//     *
//     * @param ci ： ci
//     * @return String
//     * @see
//     */
//    @RequestMapping("ci/addCi.do")
//	public String addCi(Ci ci) {
//        User cuurentUser = CurrentUserUtils.getInstance().getUser();
//        ci.setCreateBy(cuurentUser.getId());
//        ci.setCreateDate(new Date());
//        ci.setType(CiConstant.TYPE_QUICK);
//        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
//        ci.setConstructionDate(new Date());
//        ci.setCodeLocation(CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
//        ciDao.save(ci);
//
//        //添加日志
//        String extraInfo = "快速构建的创建：" + JSON.toJSONString(ci);
//        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.QUICK_CI, CommConstant.OPERATION_TYPE_CREATED);
//        commonOperationLogDao.save(log);
//
//        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
//        return "redirect:/ci";
//
//    }

//	/**
//	 * 最开始的dockerfile构建
//	 *
//	 * @param ci ：ci
//	 * @param sourceCode : 上传代码
//	 * @param dockerFile MultipartFile
//	 * @return  String
//	 * @see MultipartFile
//	 */
//    @RequestMapping(value={"ci/addResourceCi.do"},method=RequestMethod.POST)
//	public String addResourceCi(Ci ci,@RequestParam("sourceCode") MultipartFile sourceCode,@RequestParam("dockerFile") MultipartFile dockerFile) {
//        User cuurentUser = CurrentUserUtils.getInstance().getUser();
//        ci.setCreateBy(cuurentUser.getId());
//        ci.setCreateDate(new Date());
//        ci.setType(CiConstant.TYPE_DOCKERFILE);
//        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
//        ci.setConstructionDate(new Date());
//        ci.setCodeLocation(CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
//        ci.setDockerFileLocation("/");
//        try {
//            File file = new File(ci.getCodeLocation());
//            if(!file.exists()){
//                file.mkdirs();
//            }
//            if (!sourceCode.isEmpty()) {
//                FileUtils.storeFile(sourceCode.getInputStream(), ci.getCodeLocation()+"/"+sourceCode.getOriginalFilename());
//            }
//            if (!dockerFile.isEmpty()) {
//                FileUtils.storeFile(dockerFile.getInputStream(), ci.getCodeLocation()+"/"+dockerFile.getOriginalFilename());
//            }
//        }
//        catch (Exception e) {
//            LOG.error("modifyResourceCi error:"+e.getMessage());
//
//            return "redirect:/error";
//        }
//        ciDao.save(ci);
//        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
//
//        String extraInfo = "DockerFile构建的创建：" + JSON.toJSONString(ci);
//        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.DOCKER_FILE_CI, CommConstant.OPERATION_TYPE_CREATED);
//        commonOperationLogDao.save(log);
//
//        return "redirect:/ci";
//    }

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
	public String addResourceImage(Image image , String imgNameFirst, @RequestParam("sourceCode") MultipartFile sourceCode) {
        User currentUser = CurrentUserUtils.getInstance().getUser();
        image.setName(imgNameFirst +"/" + image.getName());
        image.setResourceName(sourceCode.getOriginalFilename());
        image.setCreateDate(new Date());
        image.setCreateBy(currentUser.getId());
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
            boolean flag = loadOrSaveAndPushImage(image, imagePath, sourceCode.getOriginalFilename());
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

                //添加上传镜像记录记录
                String extraInfo="上传镜像："+img.getName()+"的信息"+JSON.toJSONString(img);
                CommonOperationLog log=CommonOprationLogUtils.getOprationLog(img.getName(), extraInfo, CommConstant.UPLOAD_IMAGE, CommConstant.OPERATION_TYPE_CREATED);
                commonOperationLogDao.save(log);
            }
        }
        catch (Exception e) {
            LOG.error("uploadImage error : "+e.getMessage());
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
    private boolean loadOrSaveAndPushImage(Image image,String imagePath,String sourceName) throws IOException{
        InputStream uploadStream = Files.newInputStream(Paths.get(imagePath+"/"+sourceName));
        boolean flag = false;
        try {
            flag = ZipUtil.visitTAR(new File(imagePath+"/"+sourceName), "repositories");
            if (flag) {
                flag = ZipUtil.extTarFileList(new File(imagePath+"/"+sourceName), imagePath, "repositories");
                if (flag) {
                    String[] originImageInfo =  ZipUtil.readFileByLines(imagePath, "repositories");
                    flag = dockerClientService.loadAndPushImage(originImageInfo,image, uploadStream);
                    if(flag){
                        //删除本地镜像
                        flag = dockerClientService.removeImage(originImageInfo[0], originImageInfo[1],null,null,null,image);
                    }
                }
            } else {
                flag = dockerClientService.importAndPushImage(image, uploadStream);
                if (flag) {
                    flag = dockerClientService.removeImage(image.getName(), image.getVersion(), null, null, null,null);
                }
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
        ci.setDockerFileContent(dockerFile);
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
            LOG.error("add dockerfileCi error:"+e.getMessage());
            return "redirect:/error";
        }

        ciDao.save(ci);
        LOG.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
        String extraInfo = "DockerFile构建的创建：" + JSON.toJSONString(ci);
        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.DOCKER_FILE_CI, CommConstant.OPERATION_TYPE_CREATED);
        commonOperationLogDao.save(log);

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
            LOG.error("find dockerfile baseImage error : "+e.getMessage());
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
        String extraInfo = "快速构建的创建：" + JSON.toJSONString(ci);
        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, CommConstant.QUICK_CI, CommConstant.OPERATION_TYPE_CREATED);
        commonOperationLogDao.save(log);
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
    public String constructCi(Long id){
        long startTime = System.currentTimeMillis();
        Ci ci = ciDao.findOne(id);
        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_ING);
        ciDao.save(ci);
        CiRecord ciRecord = new CiRecord();
        ciRecord.setCiId(ci.getId());
        ciRecord.setCiName(ci.getImgNameFirst() + "/" + ci.getImgNameLast());
        ciRecord.setCiVersion(ci.getImgNameVersion());
        ciRecord.setCreatBy(CurrentUserUtils.getInstance().getUser().getId());
        ciRecord.setConstructDate(new Date());
        ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_ING);
        ciRecord.setLogPrint("["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"start");
        ciRecord.setDockerFileContent(ci.getDockerFileContent());
        ciRecordDao.save(ciRecord);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "200");

        Integer catalogType = CommConstant.CODE_CI;
        if (CiConstant.TYPE_QUICK == ci.getType()) {
            catalogType = CommConstant.QUICK_CI;
        } else if (CiConstant.TYPE_DOCKERFILE == ci.getType()) {
            catalogType = CommConstant.DOCKER_FILE_CI;
        }
        if (CiConstant.TYPE_CODE.equals(ci.getType())) {
            //判断是否需要添加默认版本信息
            CiCode ciCode = ciCodeDao.findByCiId(ci.getId());
            if (StringUtils.isEmpty(ci.getImgNameVersion())) {
                ciRecord.setCiVersion(DateUtils.getLongStr(startTime));
            }
            else {
                ciRecord.setCiVersion(ci.getImgNameVersion());
            }
            boolean fetchCodeCiFlag = fetchCodeCi(ci,ciCode,ciRecord,startTime,sheraClientService,imageDao,ciDao,ciRecordDao,hookAndImagesDao);
            if (!fetchCodeCiFlag) {
                map.put("status", "500");
                map.put("msg", "构建镜像失败，请检查配置是否正确");
                ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
                ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
            }
            else {
                ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_ING);
                ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_ING);
            }
        }
        else {
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
                ciRecord.setImageId(ci.getImgId());
                ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_OK);
                ci.setConstructionDate(new Date());
            }
        }
        ciDao.save(ci);
        ciRecordDao.save(ciRecord);
        String extraInfo = "对当前项目开始构建镜像：" + JSON.toJSONString(ci);
        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(ci.getProjectName(), extraInfo, catalogType, CommConstant.OPERATION_TYPE_START_CI);
        commonOperationLogDao.save(log);
        map.put("data", ci);
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 代码构建，添加镜像信息
     * @param ci ci
     * @param ciRecord ciRecord
     * @param startTime 时间戳
     * @return boolean
     */
    public boolean fetchCodeCi(final Ci ci,final CiCode ciCode,final CiRecord ciRecord,final long startTime,
                               final SheraClientService sheraClientService,final ImageDao imageDao,
                               final CiDao ciDao,final CiRecordDao ciRecordDao,final HookAndImagesDao hookAndImagesDao) {
        try {
            final SheraAPIClientInterface client = sheraClientService.getClient();
            JobExecView jobExecViewNew = sheraClientService.generateJobExecView(startTime,ciRecord.getCiVersion());
            jobExecViewNew = client.execJob(ci.getProjectName(), jobExecViewNew);
            ciRecord.setExecutionId(jobExecViewNew.getSeqNo());
            ciRecordDao.save(ciRecord);
            //获取执行状态和执行日志
            new Thread() {
                public void run() {
                    try {
                        boolean flag = true;
                        Integer seek = 0;
                        while(flag){
                            Thread.sleep(5000);
                            JobExecView jobExecView = new JobExecView();
                            Log log = new Log();
                            try {
                                jobExecView = client.getExecution(ci.getProjectName(),ciRecord.getExecutionId());
                                log = client.getExecLog(ci.getProjectName(),ciRecord.getExecutionId().toString(),seek);
                                seek = log.getSeek();
                            }
                            catch (Exception e) {
                                LOG.error("exec job error : "+e.getMessage());
                            }
                            //执行完成
                            if (jobExecView.getFinished() == 1) {
                                //执行成功
                                if (jobExecView.getEndStatus() == 0) {
                                    //判断是否添加镜像
                                    if (!StringUtils.isEmpty(ci.getImgNameLast())) {
                                        Image image;
                                        String imageName = ci.getImgNameFirst()+"/"+ci.getImgNameLast();
                                        //根据镜像名称和版本信息查询镜像信息是否存在
                                        image = imageDao.findByNameAndVersion(imageName, ciRecord.getCiVersion());
                                        if (!StringUtils.isEmpty(image)){
                                            hookAndImagesDao.deleteByImageId(image.getId());
                                        }
                                        else {
                                            image = new Image();
                                        }
                                        image.setName(imageName);
                                        image.setVersion(ciRecord.getCiVersion());
                                        image.setImageType(ci.getImgType());
                                        image.setRemark(ci.getDescription());
                                        image.setCreateBy(ci.getCreateBy());
                                        image.setCreateDate(DateUtils.getLongToDate(startTime));
                                        image.setIsBaseImage(ciCode.getIsBaseImage());
                                        image.setIsDelete(CommConstant.TYPE_NO_VALUE);
                                        imageDao.save(image);
                                        //判断是否需要添加hookAndImages关联信息
                                        if (ciCode.getIsHookCode() == 1) {
                                            HookAndImages hookAndImg = new HookAndImages();
                                            hookAndImg.setHookId(ciCode.getHookCodeId());
                                            hookAndImg.setImageId(image.getId());
                                            hookAndImagesDao.save(hookAndImg);
                                        }
                                        ci.setImgId(image.getId());
                                    }
                                    //添加构建和日志的信息
                                    ci.setConstructionTime(jobExecView.getEndTime()-startTime);
                                    ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_OK);
                                    ciRecord.setConstructTime(jobExecView.getEndTime()-startTime);
                                    ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_OK);
                                    ciRecord.setImageId(ci.getImgId());
                                }
                                //执行失败
                                if (jobExecView.getEndStatus() == 1) {
                                    ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
                                    ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
                                }
                                ciDao.save(ci);
                                flag = false;
                            }
                            //获取和保存日志
                            ciRecord.setLogPrint(ciRecord.getLogPrint()+log.getContent());
                            ciRecordDao.save(ciRecord);
                        }
                    }
                    catch (Exception e) {
                        LOG.error("exec job error : "+e.getMessage());
                        ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
                        ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
                        ciDao.save(ci);
                        ciRecordDao.save(ciRecord);
                    }
                }

            }.start();
            return true;
        }
        catch(SheraClientException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            CiRecord newCiRecord = ciRecord;
            newCiRecord.setLogPrint(newCiRecord.getLogPrint()+"<br>"+"["+DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
            ciRecordDao.save(newCiRecord);
            LOG.error("==========fetchCode error:"+e.getMessage());
        }
        return false;
    }

    /**
     * Description: <br>
     * 停止构建
     * @param id 项目id
     * @return String
     */
    @RequestMapping("ci/stopCi.do")
    @ResponseBody
    public String stopCi(long id){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            Ci ci = ciDao.findOne(id);
            CiRecord ciRecord = new CiRecord();
            List<CiRecord> listRecord = ciRecordDao.findByCiId(id, new Sort(new Order(Direction.DESC,"constructDate")));
            for (CiRecord record : listRecord) {
                if (record.getConstructResult().equals(CiConstant.CONSTRUCTION_RESULT_ING)) {
                    ciRecord = record;
                }
            }
            if (ci.getType().equals(CiConstant.TYPE_CODE)) {
                stopCodeCi(ci.getProjectName(),ciRecord.getExecutionId());
            }
            ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
            ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
            ciDao.save(ci);
            ciRecordDao.save(ciRecord);
            map.put("status", "200");
            map.put("ci", ci);
        }
        catch (Exception e) {
            map.put("status", "400");
            LOG.error("stop ci error" + e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 停止正在执行的代码构建
     * @param projectName 项目名称
     * @param executionId 项目版本号
     * @return String
     */
    @RequestMapping("ci/stopCodeCi.do")
    @ResponseBody
    public String stopCodeCi(String projectName,int executionId){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            client.killExecution(projectName, executionId);
            //添加日志
            String extraInfo = "停止构建项目：" + projectName;
            CommonOperationLog log=CommonOprationLogUtils.getOprationLog(projectName, extraInfo, CommConstant.CODE_CI, CommConstant.OPERATION_TYPE_STOP_CI);
            commonOperationLogDao.save(log);
            map.put("status", "200");
        }
        catch (Exception e) {
            LOG.error("stop codeCi error : "+e.getMessage());
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 删除一个代码构建执行
     * @param projectName 项目名称
     * @param executionId 项目版本号
     * @param ciRecordId 构建记录
     * @return String
     */
    @RequestMapping("ci/deleteCodeCi.do")
    @ResponseBody
    public String deleteCodeCiExecution(String projectName,int executionId,long ciRecordId){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            SheraAPIClientInterface client = sheraClientService.getClient();
            client.deleteExecution(projectName, executionId);
            ciRecordDao.delete(ciRecordId);

            //添加日志
            String extraInfo = "删除代码构建执行：" + projectName;
            CommonOperationLog log=CommonOprationLogUtils.getOprationLog(projectName, extraInfo, CommConstant.CODE_CI, CommConstant.OPERATION_TYPE_DEL_CI);
            commonOperationLogDao.save(log);

            map.put("status", "200");
        }
        catch (Exception e) {
            LOG.error("delete codeCi error : "+e.getMessage());
            map.put("status", "400");
        }
        return JSON.toJSONString(map);
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
            flag = dockerClientService.removeImage(imageName, imageVersion,ciRecord,ciRecordDao,dockerClient,null);
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
            img.setCreateBy(ci.getCreateBy());
            img.setCreateDate(new Date());
            img.setIsBaseImage(ImageConstant.NotBaseImage);
            img.setIsDelete(CommConstant.TYPE_NO_VALUE);
            imageDao.save(img);
            ci.setImgId(img.getId());

            //构建成功之后添加镜像记录
            String extraInfo="添加构建镜像："+img.getName()+"的信息"+JSON.toJSONString(img);
            CommonOperationLog comlog=CommonOprationLogUtils.getOprationLog(img.getName(), extraInfo, CommConstant.IMAGE, CommConstant.OPERATION_TYPE_CREATED);
            commonOperationLogDao.save(comlog);
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
        dkFile =  dockerFileTemplateDao.save(dkFile);


        //记录用户创建DockerFile操作
        String extraInfo="新增templateName:"+templateName+"包含的内容: "+dockerFile;
        CommonOperationLog log=CommonOprationLogUtils.getOprationLog(templateName, extraInfo, CommConstant.DOCKFILE_TEMPLATE, CommConstant.OPERATION_TYPE_CREATED);
        commonOperationLogDao.save(log);
        map.put("status", "200");
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 镜像版本查重
     * @param model
     * @param imgNameLast
     * @param imgNameVersion
     * @return
     * @see
     */
    @RequestMapping(value = {"ci/validciinfo.do"} , method = RequestMethod.POST)
    @ResponseBody
    public String validCiInfo(Model model, String imgNameFirst,String imgNameLast, String imgNameVersion) {
        Map<String,Object> result = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(imgNameFirst) && !StringUtils.isEmpty(imgNameLast) && !StringUtils.isEmpty(imgNameVersion)) {
            Image image = imageDao.findByNameAndVersion(imgNameFirst+"/"+imgNameLast, imgNameVersion);
            if (!StringUtils.isEmpty(image)) {
                result.put("status", "400");
            }
        }
        return JSON.toJSONString(result);
    }

    /**
     * Description: <br>
     * 代码构建详细页面中镜像版本判断重复
     * @param id 当前构建Id
     * @param imgNameFirst
     * @param imgNameLast
     * @param imgNameVersion
     * @return String
     */
    @RequestMapping(value = {"ci/validCiDetailVersion.do"} , method = RequestMethod.POST)
    @ResponseBody
    public String validCiDetailVersion(long id,String imgNameFirst,String imgNameLast, String imgNameVersion){
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("status", 200);
        if (!StringUtils.isEmpty(imgNameFirst) && !StringUtils.isEmpty(imgNameLast) && !StringUtils.isEmpty(imgNameVersion)) {
            Ci ci = ciDao.findOne(id);
            if(null!= ci) {
                if (!ci.getImgNameFirst().equals(imgNameFirst) ||
                            !ci.getImgNameLast().equals(imgNameLast) ||
                            !ci.getImgNameVersion().equals(imgNameVersion)) {
                    Image image = imageDao.findByNameAndVersion(imgNameFirst+"/"+imgNameLast, imgNameVersion);
                    if (!StringUtils.isEmpty(image)) {
                        result.put("status", 400);
                    }
                }
            }
        }
        return JSON.toJSONString(result);
    }

    /**
     * Description: <br>
     * 镜像名称查重
     * @param model
     * @param name
     * @param version
     * @return
     * @see
     */
    @RequestMapping(value = {"ci/validimageinfo.do"} , method = RequestMethod.POST)
    @ResponseBody
    public String validImageInfo(Model model,String imgNameFirst, String name, String version) {
        Map<String,Object> result = new HashMap<String, Object>();
        name =imgNameFirst + "/" + name;
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(version)) {
            Image image = imageDao.findByNameAndVersion(name, version);
            if (null != image) {
                result.put("status", "400");
            }
        }
        return JSON.toJSONString(result);
    }

    /**
     * Description: <br>
     * 删除代码挂钩
     * @param ciId ciId
     * @return String
     */
    @RequestMapping(value = {"ci/delCodeHook.do"} , method = RequestMethod.POST)
    @ResponseBody
    public String delCodeHook(long ciId){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            CiCode ciCode = ciCodeDao.findByCiId(ciId);
            CiCodeHook ciCodeHook = ciCodeHookDao.findOne(ciCode.getHookCodeId());
            SheraAPIClientInterface client = sheraClientService.getClient();
            client.deleteGitHooks(ciCodeHook.getName());
            ciCode.setIsHookCode(0);
            ciCodeDao.save(ciCode);

            //添加日志
            String projectName = ciDao.findOne(ciId).getProjectName();
            String extraInfo = "删除代码构建中的代码挂钩 ： " + projectName;
            CommonOperationLog log=CommonOprationLogUtils.getOprationLog(projectName, extraInfo, CommConstant.CODE_CI, CommConstant.OPERATION_TYPE_DELETE);
            commonOperationLogDao.save(log);
            map.put("status", "200");
        }
        catch (Exception e) {
           LOG.error("delete CodeHook error : "+e.getMessage());
           map.put("status", "400");
        }
        return JSON.toJSONString(map);
    }

    /**
     * Description: <br>
     * 根据镜像id，查询相关联的构建数据，跳转进入构建详细页面
     * @param imgId  镜像Id
     * @see
     */
    @RequestMapping(value = {"ci/findCodeCiId.do"}, method = RequestMethod.GET)
    public String  redirectCodeDetail(long imgId){
        CiCode ciCode = ciCodeDao.fingByImageIdAndHookId(imgId);
        if (!StringUtils.isEmpty(ciCode)) {
            return "redirect:/ci/detail/"+ciCode.getCiId();
        }
        return "redirect:/error";
    }
}
