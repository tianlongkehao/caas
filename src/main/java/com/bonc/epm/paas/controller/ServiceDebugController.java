package com.bonc.epm.paas.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.SFTPUtil;
import com.bonc.epm.paas.util.ZipCompressing;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
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
	private static ChannelSftp sftp;
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
	        	System.out.println(sftp.pwd());
	        	sftp.cd(dirName);
	        	directory = sftp.pwd();
	        }
    		fileList = SFTPUtil.listFileInfo(directory, sftp);
		} catch (SftpException e) {
        	//找不到该directory
        	map.put("status","400");
        	return JSON.toJSONString(map);
        }
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
    public String delFile(String fileNames){
        Map<String, String> map = new HashMap();
        String[] fileName = fileNames.split(",");
        String path = root;
        String filename = new String();
        try {
        	path = sftp.pwd();
        	for(int i=0 ;i<fileName.length;i++){
        		filename = fileName[i];
				SftpATTRS sftpATTRS = sftp.lstat(filename);
	        	if(sftpATTRS.isDir()){
	        		//删除文件夹下文件和此文件夹
	        		sftp.cd(fileName[i]);
	        		Vector<LsEntry> list = sftp.ls(sftp.pwd());
	        		for (LsEntry lsEntry : list) {
	        			if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..")) {
	        				delFile(lsEntry.getFilename());
						}
					}
	        		sftp.cd(path);
	        		sftp.rmdir(filename);
	        	}else{
	        		sftp.rm(filename);
	        	}
        	}
        } catch (SftpException e) {
        	try {
				sftp.cd(path);
			} catch (SftpException e1) {
				e1.printStackTrace();
			}
        	//找不到该文件
        	map.put("status", "400");
        	map.put("fileName", filename);
        	e.printStackTrace();
        	return JSON.toJSONString(map);
        }
        map.put("status", "200");
        return JSON.toJSONString(map);
    } 
    
    /**
     * 手动创建文件夹
     * 
     * @param isVolReadOnly
     * @return 
     * @see
     */
    @RequestMapping(value = { "service/createFile.do" }, method = RequestMethod.POST)
    @ResponseBody
    public String createFile(String dirName){
        Map<String,String> map = new HashMap<String,String>();
        try {
			sftp.mkdir(dirName);
		} catch (SftpException e) {
			map.put("status", "500");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
        map.put("status", "200");
        return JSON.toJSONString(map);
    }

	public static void main(String[] args) {
		SFTPUtil sf = new SFTPUtil();
		String host = "192.168.247.129";
		int port = 22;
		String username = "root";
		String password = "123456";
		String directory = "/mnt/k8s";
		String uploadFile = "D:\\tmp\\upload.txt";
		String downloadFile = "upload.txt";
		String saveFile = "D:\\tmp\\download.txt";
		String deleteFile = "delete.txt";
		ChannelSftp sftp = sf.connect(host, port, username, password);
		try {
			sftp.cd("/");
			sftp.cd("./");
			System.out.println(sftp.pwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 
     * Description: 下载文件
     * 
     * @param downfiles 需要下载的文件和文件夹名们
     * @param directory 所在目录
     * @param request 
     * @param response 
     * @throws SftpException 
     * @throws IOException 
     * @see
     */

    @RequestMapping(value = { "service/downloadFile" }, method = RequestMethod.GET)
    public void downloadFile(String downfiles,  HttpServletRequest request,HttpServletResponse response) throws SftpException, IOException {
    	String path="";
		path = sftp.pwd();
        List<String> resultList = new ArrayList<String>();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newdownfile = df.format(new Date()) + ".zip";
        String[] downfile = downfiles.split(",");
        byte[] buf = new byte[1024];
        
        File zipFile = new File("batchDownload.zip");
        ZipOutputStream out;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFile));
	        for (String fileName : downfile) {
	            if (fileName != null) {
	                InputStream in = null;
	                try {
	                    in = sftp.get(path+"/"+fileName);
	                } catch (SftpException e) {
	                    e.printStackTrace();
	                    resultList.add(fileName + "下载失败,文件不存在!");
	                    return;
	                }
	                out.putNextEntry(new ZipEntry(fileName));
	                int len;
	                while ((len = in.read(buf)) > 0) {
	                    out.write(buf, 0, len);
	                }
	                out.closeEntry();
	                in.close();
	            }
	        }
	        out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        response.setHeader("Content-Disposition", "attachment;fileName="
                + new String(zipFile.getName().getBytes("GBK"), "ISO8859-1"));
        response.setContentType(request.getServletContext().getMimeType(zipFile.getName()));
        OutputStream ot = response.getOutputStream();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFile));
        BufferedOutputStream bos = new BufferedOutputStream(ot);
        int length = 0;
        while ((length = bis.read(buf)) > 0) {
            bos.write(buf, 0, length);
        }
        bos.flush();
        ot.flush();
        bos.close();
        bis.close();
        ot.close();
        zipFile.delete();



        
        
        
        
//        // 下载
//        response.setContentType(request.getServletContext().getMimeType(fullPath));
//        response.setHeader("Content-Disposition", "attachment;filename=" + newdownfile);
//        try {
//            InputStream myStream = new FileInputStream(fullPath);
//            IOUtils.copy(myStream, response.getOutputStream());
//            response.flushBuffer();
//            System.out.println("下载成功");
//        } 
//        catch (IOException e) {
//        } 
//        finally {
//            File rm = new File(fullPath);
//            rm.delete();
//        }
    }

}
