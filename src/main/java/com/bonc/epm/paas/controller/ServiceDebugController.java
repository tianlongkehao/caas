package com.bonc.epm.paas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.entity.FileInfo;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.SFTPUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * ServiceDebugController
 * @author longkx
 * @version 2016年11月1日
 * @see ServiceDebugController
 * @since
 */
@Controller
public class ServiceDebugController {
	
	private String root = "/";
	private String host = "192.168.247.129";
	private int port = 22;
	private String username = "root";
	private String password = "123456";
	private ChannelSftp sftp;
    /**
     * 服务数据层接口
     */
    @Autowired
	private ServiceDao serviceDao;


    /**
     * Description: <br>
     *  跳转进入服务DEBUG页面
     * @param model 
     * @return String
     */
    @RequestMapping(value = { "service/debug/{id}" }, method = RequestMethod.GET)
	public String debug(Model model,@PathVariable long id) {
	    System.out.printf("id: " + id);
	    User currentUser = CurrentUserUtils.getInstance().getUser();
	    Service service = serviceDao.findOne(id);
	    
        //建立connect
        sftp = SFTPUtil.connect(host, port, username, password);
        try {
        	//跳转至初始目录
			sftp.cd(root);
		} catch (SftpException e) {
			e.printStackTrace();
		}
        
        model.addAttribute("namespace",currentUser.getNamespace());
        model.addAttribute("id", id);
        model.addAttribute("service", service);
        model.addAttribute("menu_flag", "service");
       return "service/service-debug.jsp";
    }
    
    /**
     * 
     * Description:展示文件列表
     * 
     * @param path 文件夹路径
     * @param dirName 目录名
     * @param storageName 卷组名
     * @return JSON
     * @see
     */

    @RequestMapping(value = { "service/listFile" }, method = RequestMethod.GET)
    @ResponseBody
    public String fileList(String dirName) {
    	
        List<FileInfo> fileList = new ArrayList<FileInfo>();
        Map<String, Object> map = new HashMap<String, Object>();
        String directory = new String();
        
        try {
	        //没有目标目录时，返回根目录
	        if ("".equals(dirName)) {
	            directory = root;
	        } else {
	        	//有目标目录时，cd到目标目录获取pwd绝对路径
	        	sftp.cd(dirName);
	        	directory = sftp.pwd();
	        }
    		fileList = SFTPUtil.listFileInfo(directory, sftp);
		} catch (SftpException e) {
        	//找不到该directory
        	map.put("status","400");
        	return JSON.toJSONString(map);
        }
    	//断开connect
    	sftp.disconnect();
        map.put("fileList", fileList);
        
        return JSON.toJSONString(map);
    }
    /**
     * 手动删除文件或文件夹
     * 
     * @param storageName
     * @param isVolReadOnly
     * @return 
     * @see
     */
    @RequestMapping(value = { "service/delFile.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String delFile(String path, String fileNames){
        Map map = new HashMap();
        String[] fileName = fileNames.split(",");
        for(int i=0 ;i<fileName.length;i++){
//        	LsEntry file = new File(path+"/"+fileName[i]);
//        	if(file.isDirectory()){
//        		//删除文件夹下文件和此文件夹
//        		FileUtils.delAllFile(path+"/"+fileName[i]);
//        		file.delete();
//        	}else{
//        		file.delete();
//        	}
        }
        map.put("status", "200");
        return JSON.toJSONString(map);
    } 

}
