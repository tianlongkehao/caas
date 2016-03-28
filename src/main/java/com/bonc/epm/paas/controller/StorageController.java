package com.bonc.epm.paas.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.StorageConstant;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.entity.Ci;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
public class StorageController {
	private static final Logger log = LoggerFactory.getLogger(StorageController.class);
	@Autowired
	private StorageDao storageDao;
	
	/**
	 * 进入存储卷组页面
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value={"service/storage"}, method = RequestMethod.GET)
	public String findStorages(Model model){
//		long createBy = CurrentUserUtils.getInstance().getUser().getId();
//		List<Storage> storages = storageDao.findByCreateBy(createBy);
//		model.addAttribute("storages",storages);
		model.addAttribute("menu_flag", "service");
		
		return "storage/storage.jsp";
	}
	
	/**
	 * 根据分页配置查询当前存储卷组
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value={"service/storageList"},method = RequestMethod.POST)
	@ResponseBody
	public String findStorageList(Pageable pageable){
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		List<Storage> storages = storageDao.findAllByCreateByOrderByCreateDateDesc(createBy, pageable);
		map.put("storages", storages);
		map.put("status", "200");
		map.put("count", storageDao.countByCreateBy(createBy));
		return JSON.toJSONString(map);
		
	}
	
	/**
	 * 根据存储卷id跳转进入存储卷组详细页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value={"service/storage/detail/{id}"},method = RequestMethod.GET)
	public String detail(Model model,@PathVariable long id){
		Storage storage= storageDao.findOne(id);
		model.addAttribute("id", id);
        model.addAttribute("storage", storage);
        model.addAttribute("menu_flag", "service");
		return "storage/storage_detail.jsp";
	}
	
	/**
	 * 跳转进入新建卷组页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"service/storage/add"}, method = RequestMethod.GET)
	public String storageAdd(Model model){
		model.addAttribute("menu_flag", "service");
		return "storage/storage_add.jsp";
	}
	
	/**
	 * 新建存储
	 * @param storage
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"service/storage/build"},method=RequestMethod.POST)
	@ResponseBody
	public String buildStorage(Storage storage,Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		storage.setCreateDate(new Date());
		storage.setUseType(StorageConstant.NOT_USER);
		storage.setCreateBy(createBy);
		Storage StorageValidate = storageDao.findByCreateByAndStorageName(createBy, storage.getStorageName());
		if(StorageValidate == null){
			storageDao.save(storage);
			map.put("status", "200");
		}else{
			map.put("status", "500");
		}
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 新建存储时，对存储名进行查重；
	 * @param storageName
	 * @return
	 */
	@RequestMapping(value = {"service/storage/build/validate"},method=RequestMethod.POST)
	@ResponseBody
	public String validate( String storageName){
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		Storage StorageValidate = storageDao.findByCreateByAndStorageName(createBy, storageName);
		if(StorageValidate == null){
			map.put("status", "200");
		}else{
			map.put("status", "500");
		}
		
		return JSON.toJSONString(map);
	}	
	
	/**
	 * 根据存储卷组Id修改存储卷组的存储大小；
	 * @param storageId
	 * @param storageUpdateSize
	 * @return
	 */
	@RequestMapping(value = {"service/storage/dilatation"},method = RequestMethod.POST)
	@ResponseBody
	public String dilatationStorage(long storageId,Integer storageUpdateSize){
		Map<String, Object> map = new HashMap<String, Object>();
		Storage storage = storageDao.findOne(storageId);
		storage.setStorageSize(storageUpdateSize);
		storageDao.save(storage);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 删除选定的存储卷组；
	 * @param storageId
	 * @return
	 */
	@RequestMapping(value = {"service/storage/delete"})
	@ResponseBody
	public String deleteStorage(@RequestParam long storageId){
		Map<String, Object> map = new HashMap<String, Object>();
		storageDao.delete(storageId);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}
}