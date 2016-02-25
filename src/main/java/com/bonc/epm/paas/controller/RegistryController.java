package com.bonc.epm.paas.controller;

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

import com.bonc.epm.paas.dao.FavorDao;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserFavorImages;
import com.bonc.epm.paas.util.CurrentUserUtils;

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
	
	/**
	 * 响应镜像查询按钮
	 * @param index
	 * @param model
	 * @return
	 */
	
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
	
	/**删除当前镜像
	 * 
	 * @param imageId
	 * @return
	 */
	
	@RequestMapping(value = {"registry/detail/deleteimage"}, method = RequestMethod.POST)
	@ResponseBody
	public String deleteImage(@RequestParam long imageId){
		imageDao.delete(imageId);
		return "ok";
	}
	
	
	private void addCurrUserFavor(List<Image> images){
		long userId = CurrentUserUtils.getInstance().getUser().getId();
		for(Image image:images){
			image.setCurrUserFavor(imageDao.findByUserIdAndImageId(image.getId(), userId));
		}
	}
}
