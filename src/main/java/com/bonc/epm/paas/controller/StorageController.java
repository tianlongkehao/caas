package com.bonc.epm.paas.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.util.CurrentUserUtils;

@Controller
public class StorageController {
	private static final Logger log = LoggerFactory.getLogger(StorageController.class);
	@Autowired
	private StorageDao storageDao;
	
	/**
	 * 查询当前用户的存储卷组
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value={"service/storage"}, method = RequestMethod.GET)
	public String findByUser( Model model){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		List<Storage> storages = storageDao.findByUserId(userId);
		
		model.addAttribute("storages",storages);
		return "storage/storage.jsp";
	}
	
	@RequestMapping(value={"service/storage/add"}, method = RequestMethod.GET)
	public String stirageAdd( Model model){
		
		return "storage/storage_add.jsp";
	}
	
	@RequestMapping(value = {"service/storage/build"},method=RequestMethod.POST)
	@ResponseBody
	public String create(@RequestParam String storageName,String format,Integer storageSize,Model model){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		Storage storage = new Storage();
		
		storage.setStorageName(storageName);
		storage.setFormat(format);
		storage.setStorageSize(storageSize);
		storage.setCreateDate(new Date());
		storage.setUseType(0);
		storage.setUserId(userId);
		
		int judge = storageDao.findByName(userId, storageName);
		
		if(judge == 0){
			storageDao.save(storage);
			return "success";
		}else{
			return "error";
		}
		
	}
	
	@RequestMapping(value = {"service/storage/build/judge"},method=RequestMethod.POST)
	@ResponseBody
	public String judge(@RequestParam String storageName,Model model){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		int judge = storageDao.findByName(userId, storageName);
		if(judge == 0){
			return "ok";
		}else{
			return "no";
		}
	}	
	@RequestMapping(value = {"service/storage/dilatation"},method = RequestMethod.POST)
	@ResponseBody
	public String dilatationStorage(@RequestParam long id,Integer storageSize){
		Storage storage = storageDao.findOne(id);
		storage.setStorageSize(storageSize);
		storageDao.save(storage);
		
		return "success";
	}
	
	@RequestMapping(value = {"service/storage/delete"},method = RequestMethod.POST)
	@ResponseBody
	public String deleteStorage(@RequestParam long storageId){
		storageDao.delete(storageId);
		
		return "success";
	}
}
