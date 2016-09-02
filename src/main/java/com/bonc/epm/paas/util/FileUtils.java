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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 附件处理工具类
 * @author yangjian@bonc.com.cn
 * @version 2016年8月31日
 * @see FileUtils
 * @since
 */
public class FileUtils {
    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	
    /**
     * 
     * Description:
     * 存储文件 
     * 内存1024byte，以流的形式循环读取上传,防止内存溢出
     * @param in InputStream
     * @param fileName String
     * @return boolean 
     * @throws IOException 
     * @see InputStream
     */
    public static boolean storeFile(InputStream in,String fileName) throws IOException{
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(fileName));
            int b = -1;
            byte[] buffer = new byte[1024];
            while ((b = in.read(buffer)) != -1) {
                bos.write(buffer, 0, b);
            }
            bos.flush();
            return true;
        } 
        catch (IOException e) {
            LOG.error("FileUtils storeFile error:"+e.getMessage());
            return false;
        }
        finally {
            bos.close();
            in.close(); 
        }
    }
	
    /**
     * 
     * Description:
     * 写文件
     * @param fileTemplate 模板文件
     * @param data 模板中需要替换的值
     * @param toFile  
     * @throws IOException 
     * @see
     */
    public static void writeFileByLines(String fileTemplate,Map<String,String> data,String toFile) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer= null;
        try {
            reader = new BufferedReader(new FileReader(new File(fileTemplate)));
            String tempString = "";
            String bufferstr="";
            while (null != (tempString = reader.readLine())) {
                Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
                Matcher matcher = pattern.matcher(tempString);
                if (matcher.find()) {
                    tempString = tempString.replace(matcher.group(0), data.get(matcher.group(0)));
                    bufferstr+=tempString+"\n";
                }
                else{
                    bufferstr+=tempString+"\n";
                }
            }
            writer=new BufferedWriter(new FileWriter(new File(toFile)));
            writer.write(bufferstr);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reader.close();
            writer.close();
        }
    }
	
    /**
     * 
     * Description:
     * 读取dockerFile文件中的文本数据；
     * @param fileTemplate ： dockerfile文件路径
     * @return dockerFile String
     * @throws IOException 
     * @see
     */
    public static String readFileByLines(String fileTemplate) throws IOException{
        String dockerFile = "";
        BufferedReader reader = null;
        try{
            String tempString = "";
            reader = new BufferedReader(new FileReader(new File(fileTemplate)));
            while (null != (tempString = reader.readLine())) {
                dockerFile+=tempString+"\n";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            reader.close();
        }
        return dockerFile;
    }
	
}
