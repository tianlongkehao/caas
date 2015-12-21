package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CiConstant;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.util.CmdUtil;
import com.bonc.epm.paas.util.DateFormatUtils;
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
	@RequestMapping(value={"ci/detail/{id}"},method=RequestMethod.GET)
	public String detail(Model model,@PathVariable long id){
        System.out.printf("id: " + id);
        Ci ci = ciDao.findOne(id);
        List<CiRecord> ciRecordList = ciRecordDao.findByCiId(id,new Sort(new Order(Direction. DESC,"constructDate")));
		model.addAttribute("id", id);
        model.addAttribute("ci", ci);
        model.addAttribute("ciRecordList", ciRecordList);
		return "ci/ci_detail.jsp";
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
        model.addAttribute("username", "bonc");
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
	
	@RequestMapping("ci/printCiRecordLog.do")
	@ResponseBody
	public String printCiRecordLog(long id) {
		CiRecord ciRecord = ciRecordDao.findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data",ciRecord);
		return JSON.toJSONString(map);
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
		ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_ING);
		ciRecord.setLogPrint("["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"start");
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
	 * @param ci
	 */
	private boolean fetchCode(Ci ci,CiRecord ciRecord,CiRecordDao ciRecordDao){
		try{
			ci.setCodeLocation(CiConstant.CODE_TEMP_PATH+"/"+ci.getImgNameFirst()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
			if(CiConstant.CODE_TYPE_SVN==ci.getCodeType()){
				String svnCommandStr = "svn export --username="+ci.getCodeUsername()+" --password="+ci.getCodePassword()+" "+ci.getCodeUrl()+" "+ci.getCodeLocation();
				ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"svn export");
				ciRecordDao.save(ciRecord);
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
				ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"git clone");
				ciRecordDao.save(ciRecord);
				log.info("==========gitCommandStr:"+gitCommandStr);
				return CmdUtil.exeCmd(gitCommandStr);
			}
		}catch(Exception e){
			e.printStackTrace();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
			ciRecordDao.save(ciRecord);
			log.error("==========fetchCode error:"+e.getMessage());
		}
		return false;
	}
	
	/**
	 * 构建镜像
	 * @param ci
	 */
	private boolean construct(Ci ci,CiRecord ciRecord,CiRecordDao ciRecordDao){
		//构建镜像
		String dockerfilePath = ci.getCodeLocation()+ci.getDockerFileLocation();
		String imageName = ci.getImgNameFirst()+"/"+ci.getImgNameLast();
		String imageVersion = ci.getImgNameVersion();
		boolean flag = DockerClientUtil.buildImage(dockerfilePath,imageName, imageVersion,ciRecord,ciRecordDao);
		if(flag){
			//上传镜像
			flag = DockerClientUtil.pushImage(imageName, imageVersion,ciRecord,ciRecordDao);
		}
		if(flag){
			//删除本地镜像
			flag = DockerClientUtil.removeImage(imageName, imageVersion,ciRecord,ciRecordDao);
		}
		ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"end");
		ciRecordDao.save(ciRecord);
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
			img.setImageType(ci.getImgType());
			img.setRemark(ci.getDescription());
			img.setCreateTime(new Date());
			imageDao.save(img);
			ci.setImgId(img.getId());
		}
		return flag;
	}
	
}
