package com.bonc.epm.paas.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	/**
	 * 写文件
	 * @param fileName
	 * @param data
	 */
	public static void writeFileByLines(String fileTemplate,Map<String,String> data,String toFile) {
        File file1 = new File(fileTemplate);
        File file2 = new File(toFile);
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file1));
            String tempString = "";
            String bufferstr="";
            while ((tempString = reader.readLine()) != null) {
            	try {
            		tempString.getBytes("utf-8");
            		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
            		Matcher matcher = pattern.matcher(tempString);
            		if (matcher.find()) {
            			tempString = tempString.replace(matcher.group(0), data.get(matcher.group(0)));
            			bufferstr+=tempString+"\n";
            		}else{
            			bufferstr+=tempString+"\n";
            		}
        		} catch (UnsupportedEncodingException e1) {
        			e1.printStackTrace();
        		}
            }
            BufferedWriter writer=new BufferedWriter(new FileWriter(file2));
        	writer.write(bufferstr);
        	reader.close();
        	writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	/**
	 * 读取dockerFile文件中的文本数据；
	 * 
	 * @param fileTemplate ： dockerfile文件路径
	 * @return String
	 * @see
	 */
	public static String readFileByLines(String fileTemplate){
	    String dockerFile = "";
	    File file = new File(fileTemplate);
	    
	    BufferedReader reader = null;
	    
	    try{
	        reader = new BufferedReader(new FileReader(file));
            String tempString = "";
            while ((tempString = reader.readLine()) != null) {
                try {
                    tempString.getBytes("utf-8");
                    dockerFile+=tempString+"\n";
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	    return dockerFile;
	}
	
}
