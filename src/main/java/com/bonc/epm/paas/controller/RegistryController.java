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
		long creator = 1;   //用户id；
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
		if(index == 0){
			images = imageDao.findByNameCondition(1,"%"+imageName+"%");
		}else if(index == 1){
			images = imageDao.findByNameCondition(2,"%"+imageName+"%");
		}else if(index == 2){

		}
		model.addAttribute("images", images);
		return "docker-registry/registry.jsp";
	}
	@RequestMapping(value = {"registry/detail/{id}"}, method = RequestMethod.GET)
	public String detail(@PathVariable long id, Model model) {
		Image image = imageDao.findById(id);
		model.addAttribute("image", image);
		return "docker-registry/detail.jsp";
	}
	
	@RequestMapping(value = {"registry/detail/favor"}, method = RequestMethod.POST)
	@ResponseBody
	public String favor(@RequestParam long imageId) {
		
		Image image = imageDao.findById(imageId);
		User user = userDao.findById(1);
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
	
	//for test
	@PostConstruct
	public void init(){
		Image img1 = new Image();
		img1.setName("bonc/tomcat-maven");
		img1.setRemark("配置Maven环境的Tomcat应用服务器");
		img1.setVersion("1.0");
		img1.setImageType(1);
		img1.setCreator(1);
		img1.setId(1);
		imageDao.save(img1);
		
		Image img2 = new Image();
		img2.setName("test/hw2");
		img2.setRemark("helloworld");
		img2.setVersion("latest");
		img2.setImageType(1);
		img2.setCreator(2);
		imageDao.save(img2);
		
		Image img3 = new Image();
		img3.setName("test/hw3");
		img3.setRemark("helloworld -------3");
		img3.setVersion("latest");
		img3.setImageType(2);
		img3.setCreator(1);
		imageDao.save(img3);
		
		Image img4 = new Image();
		img4.setName("test/hw4");
		img4.setRemark("helloworld -------4");
		img4.setVersion("latest");
		img4.setImageType(2);
		img4.setCreator(1);
		imageDao.save(img4);
		
		List<Image> images = new ArrayList<Image>();
		images.add(img1);
		User user1 = new User();
		user1.setId(1);
		user1.setUserName("张三");
		user1.setFavorImages(images);
		userDao.save(user1);
		User user2 = new User();
		user2.setId(2);
		user2.setUserName("lisi");
		images.add(img2);
		images.add(img3);
		user2.setFavorImages(images);
		userDao.save(user2);
		 
		log.debug("init images bonc/tomcat-maven");
	}
}
