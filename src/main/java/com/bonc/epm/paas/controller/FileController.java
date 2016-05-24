package com.bonc.epm.paas.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileController{

	private static final Logger log = LoggerFactory
			.getLogger(FileController.class);

	/**
	 * 下载模板文件
	 * @return
	 */
	@RequestMapping(value ="/file/downloadTemplate")
	public void downloadTemplate(String fileName,HttpServletRequest request,HttpServletResponse response) {
        try {
        	response.setContentType(request.getServletContext().getMimeType(fileName));  
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);  
    		String fullFileName = request.getServletContext().getRealPath("/template/"+fileName);
    		log.info("fullFileName:"+fullFileName+",fileName:"+fileName);
    		InputStream inStream=new FileInputStream(fullFileName);
    		ServletOutputStream outputStream= response.getOutputStream();
    		byte[] b = new byte[100];
            int len;
        	while ((len = inStream.read(b)) > 0){
            	outputStream.write(b, 0, len);
            }
            inStream.close();
    		outputStream.close();
        } catch (IOException e) {
            log.error("FileController  downloadTemplate:"+e.getMessage());
        }
	}
}
