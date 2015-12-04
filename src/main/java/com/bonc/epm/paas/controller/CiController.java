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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CiConstant;
import com.bonc.epm.paas.dao.CiDao;
import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.util.CmdUtil;
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
	
	@RequestMapping(value={"ci"},method=RequestMethod.GET)
	public String index(){
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
	@RequestMapping(value={"ci/detail"},method=RequestMethod.GET)
	public String detail(Model model,long id){
		model.addAttribute("id", id);
		return "ci/ci_detail.jsp";
	}
	@RequestMapping("ci/listCiRecord.do")
	@ResponseBody
	public String listCiRecord(long id) {
		List<CiRecord> ciRecordList = new ArrayList<CiRecord>();
		for(CiRecord ciRecord:ciRecordDao.findAll()){
			ciRecordList.add(ciRecord);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", ciRecordList);
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
	@RequestMapping("ci/delCi.do")
	@ResponseBody
	public String delCi(long id) {
		ciDao.delete(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		return JSON.toJSONString(map);
	}
	
	
	
	@RequestMapping(value={"ci/add"},method=RequestMethod.GET)
	public String addProject(){
		return "ci/ci_add.jsp";
	}
	
	@RequestMapping("ci/addCi.do")
	@ResponseBody
	public String addCi(Ci ci) {
		ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_WAIT);
		ci.setCodeType(CiConstant.CODE_TYPE_GIT);
		ci.setConstructionDate(new Date());
		ciDao.save(ci);
		log.debug("addCi--id:"+ci.getId()+"--name:"+ci.getProjectName());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		map.put("data", ci);
		return JSON.toJSONString(map);
	}
	
	@RequestMapping("ci/constructCi.do")
	@ResponseBody
	public String constructCi(Long id) {
		Ci ci = ciDao.findOne(id);
		long startTime = System.currentTimeMillis();
		CiRecord ciRecord = new CiRecord();
		ciRecord.setCiId(ci.getId());
		ciRecord.setCiVersion(ci.getImgNameVersion());
		ciRecord.setConstructDate(ci.getConstructionDate());
		boolean flag = fetchCode(ci);
		if(flag){
			//flag = construct(ci);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(flag){
			map.put("status", "200");
			ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_OK);
			ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_OK);
		}else{
			map.put("status", "500");
			map.put("msg", "系统错误");
			ci.setConstructionStatus(CiConstant.CONSTRUCTION_STATUS_FAIL);
			ciRecord.setConstructResult(CiConstant.CONSTRUCTION_RESULT_FAIL);
		}
		long endTime = System.currentTimeMillis();
		ci.setConstructionTime(endTime-startTime);
		ci.setConstructionDate(new Date());
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
		ci.setCodeLocation("/usr/local/codetemp/"+ci.getImgNameFisrt()+"/"+ci.getImgNameLast()+"/"+ci.getImgNameVersion());
		String commandStr = "git clone "+ci.getCodeUrl()+" "+ci.getCodeLocation();
		log.info("==========commandStr:"+commandStr);
		return CmdUtil.exeCmd(commandStr);
	}
	/**
	 * 构建镜像
	 * @param ci
	 */
	private boolean construct(Ci ci){
		//docker build -t <镜像名> <Dockerfile路径>
		String commandStr = "docker build -t "+ci.getImgNameFisrt()+"/"+ci.getImgNameLast()+" "+ci.getCodeLocation()+ci.getDockerFileLocation();
		log.info("==========commandStr:"+commandStr);
		return CmdUtil.exeCmd(commandStr);
	}
	
}
