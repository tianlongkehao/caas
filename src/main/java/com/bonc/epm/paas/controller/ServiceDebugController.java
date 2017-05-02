package com.bonc.epm.paas.controller;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import com.bonc.epm.paas.util.FileUtils;
import com.github.dockerjava.api.DockerClient;

/**
 * ClassName: ServiceDebugController <br/>
 * Function: 服务文件的操作. <br/>
 * date: 2017年4月26日 下午1:44:47 <br/>
 *
 * @author longkaixiang
 * @version
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
	 * downloadFile:下载容器中的文件. <br/>
	 *
	 * @author longkaixiang
	 * @param request
	 * @param response
	 * @param nodeIp
	 * @param containerId
	 * @param path
	 * @param fileName
	 *            void
	 */
	@RequestMapping(value = { "service/downloadFile" }, method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String nodeIp,
			String containerId, String path, String fileName) {
		// 获取container所在节点的client
		DockerClient dockerClient = dockerClientService.getSpecifiedDockerClientInstance(nodeIp);

		try {
			response.setHeader("Content-Disposition",
					"attachment;fileName=" + new String((fileName + ".tar").getBytes("GBK"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setContentType(request.getServletContext().getMimeType(fileName + ".tar"));
		try (InputStream inputStream = dockerClient.copyArchiveFromContainerCmd(containerId, path + "/" + fileName)
				.exec(); OutputStream outputStream = response.getOutputStream()) {
			byte[] b = new byte[1024];
			int num;
			while ((num = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, num);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * uploadFile:上传文件到指定容器的指定目录下. <br/>
	 *
	 * @author longkaixiang
	 * @param files
	 * @param currentFilePath
	 * @param currentContainerIp
	 * @param currentContainerId
	 * @return String
	 */
	@RequestMapping(value = { "service/uploadFile" }, method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile[] files, String currentFilePath,
			String currentContainerIp, String currentContainerId) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			// 获取container所在节点的client
			DockerClient dockerClient = dockerClientService.getSpecifiedDockerClientInstance(currentContainerIp);
			if (files != null && files.length > 0) {
				File file = new File(
						"../containerFile" + "/" + CurrentUserUtils.getInstance().getUser().getNamespace());
				if (!file.exists()) {
					file.mkdirs();
				}
				String path = "../containerFile" + "/" + CurrentUserUtils.getInstance().getUser().getNamespace() + "/"
						+ files[0].getOriginalFilename();
				FileUtils.storeFile(files[0].getInputStream(), path);
				// 获取输出流
				dockerClient.copyArchiveToContainerCmd(currentContainerId).withRemotePath(currentFilePath)
						.withHostResource(path).exec().wait();
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
