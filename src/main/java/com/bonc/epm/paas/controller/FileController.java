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

/**
 * 
 * 下载模板文件
 * @author update
 * @version 2016年9月5日
 * @see FileController
 * @since
 */
@Controller
public class FileController{
    
    /**
     * FileController日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    /**
     * Description: <br>
     * 下载模板文件
     * @param fileName 文件名称
     * @param request request
     * @param response response
     */
    @RequestMapping(value ="/file/downloadTemplate")
	public void downloadTemplate(String fileName,HttpServletRequest request,HttpServletResponse response) {
        try {
            response.setContentType(request.getServletContext().getMimeType(fileName));  
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);  
            String fullFileName = request.getServletContext().getRealPath("/template/"+fileName);
            LOG.info("fullFileName:"+fullFileName+",fileName:"+fileName);
            InputStream inStream=new FileInputStream(fullFileName);
            ServletOutputStream outputStream= response.getOutputStream();
            byte[] b = new byte[100];
            int len;
            while ((len = inStream.read(b)) > 0){
            	outputStream.write(b, 0, len);
            }
            inStream.close();
            outputStream.close();
        } 
        catch (IOException e) {
            LOG.error("FileController  downloadTemplate:"+e.getMessage());
        }
    }
}
