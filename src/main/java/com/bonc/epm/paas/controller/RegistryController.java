package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

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

import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;

/**
 * 镜像
 * @author shangvven
 *
 */
@Controller
public class RegistryController {
	private static final Logger log = LoggerFactory.getLogger(RegistryController.class);
	@Autowired
	private ImageDao imageDao;
	@Autowired
	private UserDao userDao;
	@RequestMapping(value = {"registry/{index}"}, method = RequestMethod.GET)
	public String index(@PathVariable int index, Model model) {
		List<Image> images = null;
		long creator = 2;   //用户id；
		if(index == 0){
			images = imageDao.findByImageType(1);
		}else if(index == 1){
			images = imageDao.findAllByCreator(2, creator);
		}else if(index == 2){
			images = userDao.findById(creator).getFavorImages();
		}
		model.addAttribute("images", images);
		model.addAttribute("menu_flag", "registry");
		model.addAttribute("index", index);
		return "docker-registry/registry.jsp";
	}
	
	@RequestMapping(value = {"registry/{index}"},method = RequestMethod.POST)
	public String findByName(@PathVariable int index,@RequestParam String imageName,Model model) {
		List<Image> images = null;
		long creator = 2;   //用户id；
		if(index == 0){
			images = imageDao.findByNameCondition("%"+imageName+"%");
		}else if(index == 1){
			images = imageDao.findByNameOfUser(creator,"%"+imageName+"%");
		}else if(index == 2){
			images = userDao.findByNameCondition(creator, "%"+imageName+"%");
		}
		model.addAttribute("images", images);
		return "docker-registry/registry.jsp";
	}
	@RequestMapping(value = {"registry/detail/{id}"}, method = RequestMethod.GET)
	public String detail(@PathVariable long id, Model model) {
		Image image = imageDao.findById(id);
		int favorUser = imageDao.findAllUserById(id);
		User user = userDao.findById(image.getCreator());
		
		model.addAttribute("image", image);
		model.addAttribute("favorUser",favorUser);
		model.addAttribute("creator", user.getUserName());
		return "docker-registry/detail.jsp";
	}
	
	@RequestMapping(value = {"registry/detail/favor"}, method = RequestMethod.POST)
	@ResponseBody
	public String favor(@RequestParam long imageId) {
		long creator = 2;   //用户id；
		Image image = imageDao.findById(imageId);
		User user = userDao.findById(creator);
		List<Image> images = user.getFavorImages();
		boolean flag = false;
		for(int i = 0 ;i<images.size();i++){
			Image img = images.get(i);
			if(img.getId()==imageId){
				images.remove(i);
				flag = true;
				break;
			}
		}
		if(!flag){
			images.add(image);
		}
		user.setFavorImages(images);
		userDao.save(user);
		if(!flag){
			return "success";
		}else{
			return "delete";
		}
	}
}
