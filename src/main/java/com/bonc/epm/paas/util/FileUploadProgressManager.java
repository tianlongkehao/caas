package com.bonc.epm.paas.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bonc.epm.paas.entity.FileUploadProgress;

public class FileUploadProgressManager {

	//用于存放所有的进度对象
	public static Map<String, FileUploadProgress> progressPool = new HashMap<>();
	
	//根据uuid获取对应的进度对象
	public static FileUploadProgress getFileUploadProgress(String uuid) {
		if (progressPool.containsKey(uuid)) {
			return progressPool.get(uuid);
		} else {
			return null;
		}
	}
	
	//创建新的根据MultipartFile文件创建新的进度对象
	public static FileUploadProgress createFileUploadProgress(String uuid, MultipartFile file) {
		FileUploadProgress progress = new FileUploadProgress();
		progress.setUuid(uuid);
		progress.setFileName(file.getOriginalFilename());
		progress.setSize(file.getSize());
		progress.setRead(0);
		progress.setCreatTime(new Date());
		progressPool.put(progress.getUuid(), progress);
		return progress;
		
	}
	
	//删除指定的进度对象
	public static void deleteFileUploadProgress(String uuid) {
		progressPool.remove(uuid);
	}
	
	
}
