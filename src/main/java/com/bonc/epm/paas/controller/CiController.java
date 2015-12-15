package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CiConstant;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.util.CmdUtil;
import com.bonc.epm.paas.util.DockerClientUtil;
/**
 * 构建
 * @author yangjian
 *
 */
@Controller
public class CiController {
	private static final Logger log = LoggerFactory.getLogger(CiController.class);
	@Autowired
	public CiDao ciDao;
	@Autowired
	public CiRecordDao ciRecordDao;
	@Autowired
	public ImageDao imageDao;
	
	@RequestMapping(value={"ci"},method=RequestMethod.GET)
	public String index(Model model){
        List<Ci> ciList = new ArrayList<Ci>();
        for(Ci ci:ciDao.findAll()){
            ciList.add(ci);
        }

        model.addAttribute("ciList", ciList);
        model.addAttribute("menu_flag", "ci");

		return "ci/ci.jsp";
	}
	@RequestMapping("ci/listCi.do")
	@ResponseBody
	public String list() {
		List<Ci> ciList = new ArrayList<Ci>();
		for(Ci ci:ciDao.findAll()){
			ciList.add(ci);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", ciList);
		return JSON.toJSONString(map);
	}
	@RequestMapping(value={"ci/detail/{id}"},method=RequestMethod.GET)
	public String detail(Model model,@PathVariable long id){
        System.out.printf("id: " + id);
        Ci ci = ciDao.findOne(id);
        List<CiRecord> ciRecordList = ciRecordDao.findByCiId(id);

		model.addAttribute("id", id);
        model.addAttribute("ci", ci);
        model.addAttribute("ciRecordList", ciRecordList);
		return "ci/ci_detail.jsp";
	}
	@RequestMapping("ci/listCiRecord.do")
	@ResponseBody
	public String listCiRecord(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", ciRecordDao.findByCiId(id));
		map.put("ci", ciDao.findOne(id));
		return JSON.toJSONString(map);
	}
	@RequestMapping("ci/findCi.do")
	@ResponseBody
	public String findCi(long id) {
		Ci ci = ciDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", ci);
		return JSON.toJSONString(map);
	}
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
	@RequestMapping(value = "ci/delCi.do", method = RequestMethod.POST)
	@ResponseBody
	public String delCi(@RequestParam String id) {
        Long idl = Long.parseLong(id);
		ciDao.delete(idl);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		return JSON.toJSONString(map);
	}
	
	
	
	@RequestMapping(value={"ci/add"},method=RequestMethod.GET)
	public String addProject(Model model){
        model.addAttribute("menu_flag", "ci");
		return "ci/ci_add.jsp";
	}
	
	@RequestMapping("ci/addCi.do")
	public String addCi(Ci ci) {
		ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
		ci.setCodeType(CiConstant.CODE_TYPE_GIT);
		ci.setConstructionDate(new Date());
		ciDao.save(ci);
		log.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());

//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("status", "200");
//		map.put("data", ci);
//		return JSON.toJSONString(map);

        return "redirect:/ci";

	}
	
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
		ciRecordDao.save(ciRecord);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_OK);
		ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_OK);
		boolean fetchCodeFlag = fetchCode(ci);
		if(!fetchCodeFlag){
			map.put("status", "500");
			map.put("msg", "获取代码出错,请检查代码地址和账号密码");
			ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
			ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
		}
		boolean constructFlag = construct(ci);
		if(!constructFlag){
			map.put("status", "500");
			map.put("msg", "构建镜像失败，请检查配置是否正确");
			ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
			ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
		}
		long endTime = System.currentTimeMillis();
		ci.setConstructionTime(endTime-startTime);
		ciRecord.setConstructTime(endTime-startTime);
		ciDao.save(ci);
		ciRecordDao.save(ciRecord);
		map.put("data", ci);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取代码
	 * @param ci
	 */
	private boolean fetchCode(Ci ci){
		ci.setCodeLocation(CiConstant.CODE_TEMP_PATH+"/"+ci.getImgNameFisrt()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
		if(CiConstant.CODE_TYPE_SVN==ci.getCodeType()){
			String svnCommandStr = "svn export --username="+ci.getCodeUsername()+" --password="+ci.getCodePassword()+" "+ci.getCodeUrl()+" "+ci.getCodeLocation();
			log.info("==========svnCommandStr:"+svnCommandStr);
			return CmdUtil.exeCmd(svnCommandStr);
		}else if(CiConstant.CODE_TYPE_GIT==ci.getCodeType()){
			String codeUrl = ci.getCodeUrl();
			if(StringUtils.isEmpty(codeUrl)||codeUrl.indexOf("//")<=0){
				return false;
			}
			String nCodeUrl = codeUrl.substring(0,codeUrl.indexOf("//")+2)+ci.getCodeUsername()+":"+ci.getCodePassword()+"@"+codeUrl.substring(codeUrl.indexOf("//")+2,codeUrl.length());
			String rmCommonStr = "rm -rf "+ci.getCodeLocation();
			log.info("==========rmCommonStr:"+rmCommonStr);
			CmdUtil.exeCmd(rmCommonStr);
			String gitCommandStr = "git clone "+nCodeUrl+" "+ci.getCodeLocation();
			log.info("==========gitCommandStr:"+gitCommandStr);
			return CmdUtil.exeCmd(gitCommandStr);
		}
		return false;
	}
	
	/**
	 * 构建镜像
	 * @param ci
	 */
	private boolean construct(Ci ci){
		//构建镜像
		String dockerfilePath = ci.getCodeLocation()+ci.getDockerFileLocation();
		String imageName = ci.getImgNameFisrt()+"/"+ci.getImgNameLast();
		String imageVersion = ci.getImgNameVersion();
		boolean flag = DockerClientUtil.buildImage(dockerfilePath,imageName, imageVersion);
		if(flag){
			//上传镜像
			flag = DockerClientUtil.pushImage(imageName, imageVersion);
		}
		if(flag){
			//删除本地镜像
			flag = DockerClientUtil.removeImage(imageName, imageVersion);
		}
		if(flag){
			//排重添加镜像数据
			Image img = null;
			List<Image> imageList = imageDao.findByName(imageName);
			if(!CollectionUtils.isEmpty(imageList)){
				for(Image image:imageList){
					if(ci.getImgNameVersion().equals(imageVersion)){
						img = image;
					}
				}
			}
			if(img==null){
				img = new Image();
				img.setName(imageName);
				img.setVersion(imageVersion);
			}
			img.setRemark(ci.getDescription());
			img.setCreateTime(new Date());
			imageDao.save(img);
			ci.setImgId(img.getId());
		}
		return flag;
	}
	
}
