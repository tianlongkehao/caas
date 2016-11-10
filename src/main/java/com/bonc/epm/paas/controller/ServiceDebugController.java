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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.dao.ServiceDao;
import com.bonc.epm.paas.entity.FileInfo;
import com.bonc.epm.paas.entity.PortConfig;
import com.bonc.epm.paas.entity.Service;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;
import com.bonc.epm.paas.util.SFTPUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * ServiceDebugController
 * 
 * @author longkx
 * @version 2016年11月1日
 * @see ServiceDebugController
 * @since
 */
@Controller
public class ServiceDebugController {

	private static final String ROOT = "/";
	private ChannelSftp sftp;
	/**
	 * 服务数据层接口
	 */
	@Autowired
	private ServiceDao serviceDao;
	
    /**
     * portConfig数据层接口
     */
    @Autowired
	private PortConfigDao portConfigDao;

	/**
	 * Description: <br>
	 * 跳转进入服务DEBUG页面
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "service/debug/{id}" }, method = RequestMethod.GET)
	public String debug(Model model, @PathVariable long id) {
		System.out.printf("id: " + id);
		User currentUser = CurrentUserUtils.getInstance().getUser();
		Service service = serviceDao.findOne(id);
		List<PortConfig> portConfigList = portConfigDao.findByServiceId(service.getId());
		String host = "192.168.0.80";
		int port = 32364;
		String username = "debug";
		String password = "debug";

		// 建立connect
		sftp = SFTPUtil.connect(host, port, username, password);
		try {
			// 跳转至初始目录
			sftp.cd(ROOT);
		} catch (SftpException e) {
			e.printStackTrace();
		}

		model.addAttribute("namespace", currentUser.getNamespace());
		model.addAttribute("id", id);
		model.addAttribute("service", service);
		model.addAttribute("menu_flag", "service");
		return "service/service-debug.jsp";
	}

	/**
	 * 
	 * Description:展示文件列表
	 * 
	 * @param path
	 *            文件夹路径
	 * @param dirName
	 *            目录名
	 * @param storageName
	 *            卷组名
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
			// 没有目标目录时，返回根目录
			if ("".equals(dirName)) {
				directory = ROOT;
			} else {
				// 有目标目录时，cd到目标目录获取pwd绝对路径
				System.out.println(sftp.pwd());
				sftp.cd(dirName);
				directory = sftp.pwd();
			}
			fileList = SFTPUtil.listFileInfo(directory, sftp);
		} catch (SftpException e) {
			// 找不到该directory
			map.put("status", "400");
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "service/delFile.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String delFile(String fileNames) {
		Map<String, String> map = new HashMap<String, String>();
		String[] fileName = fileNames.split(",");
		String path = ROOT;
		String filename = new String();
		try {
			path = sftp.pwd();
			for (int i = 0; i < fileName.length; i++) {
				filename = fileName[i];
				SftpATTRS sftpATTRS = sftp.lstat(filename);
				if (sftpATTRS.isDir()) {
					// 删除文件夹下文件和此文件夹
					sftp.cd(fileName[i]);
					Vector<LsEntry> list = sftp.ls(sftp.pwd());
					for (LsEntry lsEntry : list) {
						if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..")) {
							delFile(lsEntry.getFilename());
						}
					}
					sftp.cd(path);
					sftp.rmdir(filename);
				} else {
					sftp.rm(filename);
				}
			}
		} catch (SftpException e) {
			try {
				sftp.cd(path);
			} catch (SftpException e1) {
				e1.printStackTrace();
			}
			// 找不到该文件
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
	public String createFile(String dirName) {
		Map<String, String> map = new HashMap<String, String>();
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

	/**
	 * 
	 * Description: 下载文件
	 * 
	 * @param downfiles
	 *            需要下载的文件
	 * @param request
	 * @param response
	 * @throws SftpException
	 * @throws IOException
	 * @see
	 */

	@RequestMapping(value = { "service/downloadFile" }, method = RequestMethod.GET)
	public void downloadFile(String downfiles, HttpServletRequest request, HttpServletResponse response) throws SftpException, IOException {
		String path = "";
		path = sftp.pwd();
		List<String> resultList = new ArrayList<String>();
		
		String[] downfile = downfiles.split(",");
		byte[] buf = new byte[1024];
		
		String filename = new String();
		if (downfile.length == 1) {
			filename = downfile[0]+".zip"; 
		} else {
			filename = "download.zip"; 
		}

		File zipFile = new File(filename);
		ZipOutputStream out;

		try {
			out = new ZipOutputStream(new FileOutputStream(zipFile));
			for (String fileName : downfile) {
				if (fileName != null) {
					InputStream in = null;
					try {
						in = sftp.get(path + "/" + fileName);
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

		response.setHeader("Content-Disposition", "attachment;fileName=" + new String(zipFile.getName().getBytes("GBK"), "ISO8859-1"));
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
	}

	/**
	 * 
	 * Description: 上传文件
	 * 
	 * @param file
	 *            上传文件名
	 * @param path
	 *            路径
	 * @param storageName
	 *            卷组名
	 * @param id
	 *            卷组的id
	 * @return JSON
	 * @see
	 */

	@RequestMapping(value = { "service/uploadFile" }, method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile[] files, String path) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < files.length; i++) {
				File file = new File(files[i].getOriginalFilename());
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				out.write(files[i].getBytes());
				out.flush();
				out.close();
				SFTPUtil.upLoadFile(sftp, new File(files[i].getOriginalFilename()), sftp.pwd());
				file.delete();
				map.put("status", "200");
			}
			return JSON.toJSONString(map);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "上传失败," + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return "上传失败," + e.getMessage();
		} catch(Exception e){
			e.printStackTrace();
			return "上传失败," + e.getMessage();
		}
	}

}
