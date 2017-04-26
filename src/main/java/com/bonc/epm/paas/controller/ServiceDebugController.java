package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.ImageConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.ImageDao;
import com.bonc.epm.paas.docker.util.DockerClientService;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.github.dockerjava.api.DockerClient;
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

	/**
	 * ImageDao接口
	 */
	@Autowired
	private ImageDao imageDao;

	/**
	 * KubernetesClientService接口
	 */
	@Autowired
	private DockerClientService dockerClientService;

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
	public void downloadFile(String downfiles, String hostkey, HttpServletRequest request, HttpServletResponse response)
			throws SftpException, IOException {
		// String path = "";
		// ChannelSftp sftp = sftpPool.get(hostkey);
		// if (sftp == null || !sftp.isConnected()) {
		// System.out.println("连接已经断开");
		// return;
		// }
		// path = sftp.pwd();
		// List<String> resultList = new ArrayList<String>();
		//
		// String[] downfile = downfiles.split(",");
		// byte[] buf = new byte[1024];
		//
		// String filename = new String();
		// if (downfile.length == 1) {
		// filename = downfile[0] + ".zip";
		// } else {
		// filename = "download.zip";
		// }
		//
		// File zipFile = new File(filename);
		// ZipOutputStream out;
		//
		// try {
		// out = new ZipOutputStream(new FileOutputStream(zipFile));
		// for (String fileName : downfile) {
		// if (fileName != null) {
		// InputStream in = null;
		// try {
		// in = sftp.get(path + "/" + fileName);
		// } catch (SftpException e) {
		// e.printStackTrace();
		// resultList.add(fileName + "下载失败,文件不存在!");
		// return;
		// }
		// out.putNextEntry(new ZipEntry(fileName));
		// int len;
		// while ((len = in.read(buf)) > 0) {
		// out.write(buf, 0, len);
		// }
		// out.closeEntry();
		// in.close();
		// }
		// }
		// out.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// response.setHeader("Content-Disposition",
		// "attachment;fileName=" + new
		// String(zipFile.getName().getBytes("GBK"), "ISO8859-1"));
		// response.setContentType(request.getServletContext().getMimeType(zipFile.getName()));
		// OutputStream ot = response.getOutputStream();
		// BufferedInputStream bis = new BufferedInputStream(new
		// FileInputStream(zipFile));
		// BufferedOutputStream bos = new BufferedOutputStream(ot);
		// int length = 0;
		// while ((length = bis.read(buf)) > 0) {
		// bos.write(buf, 0, length);
		// }
		// bos.flush();
		// ot.flush();
		// bos.close();
		// bis.close();
		// ot.close();
		// zipFile.delete();
	}

	/**
	 * uploadFile:上传文件到指定容器的指定目录下. <br/>
	 *
	 * @author longkaixiang
	 * @param files
	 * @param path
	 * @param nodeIp
	 * @param containerId
	 * @return String
	 */
	@RequestMapping(value = { "service/uploadFile" }, method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile[] files, String path, String nodeIp,
			String containerId) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			// 获取container所在节点的client
			DockerClient dockerClient = dockerClientService.getSpecifiedDockerClientInstance(nodeIp);
			if (files != null && files.length > 0) {
				// 获取输入流
				InputStream in = files[0].getInputStream();
				// 获取输出流
				dockerClient.copyArchiveToContainerCmd(containerId).withRemotePath(path).withTarInputStream(in).exec();
			} else {
				map.put("status", "300");
				return JSON.toJSONString(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "400");
			return JSON.toJSONString(map);
		}
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * saveAsImage:把容器保存为镜像. <br/>
	 *
	 * @author longkaixiang
	 * @param containerId
	 * @param nodeIP
	 * @param imageName
	 * @param version
	 * @param cmdString
	 * @return String
	 */
	@RequestMapping(value = { "service/saveAsImage" }, method = RequestMethod.POST)
	@ResponseBody
	public String saveAsImage(@RequestParam String containerId, @RequestParam String nodeIP,
			@RequestParam String imageName, @RequestParam String version, @RequestParam String cmdString) {
		Map<String, Object> map = new HashMap<String, Object>();
		User cuurentUser = CurrentUserUtils.getInstance().getUser();
		// 去掉containerId开头的"docker://"
		containerId = containerId.substring(containerId.lastIndexOf('/') + 1);
		// 拼接镜像名
		String imageNameFirst = "";
		if (cuurentUser.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
			imageNameFirst = cuurentUser.getNamespace() + "_" + cuurentUser.getUserName();
		} else {
			imageNameFirst = cuurentUser.getUserName();
		}
		imageName = imageNameFirst + "/" + imageName;
		// 检查是否有重名的镜像
		if (null != imageDao.findByNameAndVersion(imageName, version)) {
			map.put("status", 400);
			return JSON.toJSONString(map);
		}
		// 拼接仓库的地址

		String fullImageName = dockerClientService.getDockerRegistryAddress() + "/" + imageName;
		// 获取container所在节点的client
		DockerClient dockerClient = dockerClientService.getSpecifiedDockerClientInstance(nodeIP);
		// 将container保存为本地镜像
		String imageId = dockerClientService.commitContainer(containerId, fullImageName, version, dockerClient,
				cmdString);
		if (imageId != null) {
			// 本地镜像push到仓库
			if (dockerClientService.pushImage(imageName, version, dockerClient)) {
				User currentUser = CurrentUserUtils.getInstance().getUser();
				// 保存当前debug的镜像到数据库中
				Image image = new Image();
				image.setCreateDate(new Date());
				image.setCreateBy(currentUser.getId());
				image.setImageId(imageId);
				image.setImageType(ImageConstant.privateType);
				image.setIsBaseImage(ImageConstant.NotBaseImage);
				image.setName(imageName);
				image.setVersion(version);
				image.setIsDelete(CommConstant.TYPE_NO_VALUE);
				imageDao.save(image);
				map.put("status", 200);
				// 删除本地镜像
				dockerClient.removeImageCmd(imageId).withForce(true).exec();
			} else {
				// 本地镜像push到仓库时失败，删除本地镜像，返回402
				dockerClient.removeImageCmd(imageId).withForce(true).exec();
				map.put("status", 402);
				return JSON.toJSONString(map);

			}
		} else {
			// container保存commit为本地镜像失败，返回401
			map.put("status", 401);
			return JSON.toJSONString(map);
		}

		return JSON.toJSONString(map);
	}

}
