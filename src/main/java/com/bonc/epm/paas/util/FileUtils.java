package com.bonc.epm.paas.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName FileUtils
 * @Description 附件处理工具类
 * @author yangjian@bonc.com.cn
 * @date 2016年1月26日
 */
public class FileUtils {
	
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	/**
	 * 存储文件
	 * @param in
	 * @param fileName
	 * @return
	 */
	public static boolean storeFile(InputStream in,String fileName){
		try {
			//内存1024byte，以流的形式循环读取上传,防止内存溢出
			BufferedOutputStream bos =
	                new BufferedOutputStream(new FileOutputStream(fileName));
			int b = -1;
			byte[] buffer = new byte[1024];
			while ((b = in.read(buffer)) != -1) {
			    bos.write(buffer, 0, b);
			}
			bos.flush();
	        bos.close();
	        in.close();
	        return true;
		} catch (IOException e) {
			log.error("FileUtils storeFile error:"+e.getMessage());
			return false;
		}
       
	}
}
