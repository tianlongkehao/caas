/*
 * 文件名：ZipUtil.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月14日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * java处理压缩包公共类
 * 目前支持：1.浏览tar类型中的文件列表
 * 镜像和容器快照的tar包
 * @author ke_wang
 * @version 2016年11月14日
 * @see ZipUtil
 * @since
 */

public  class ZipUtil {
    /**
     * 输出日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(ZipUtil.class);
    
    /**
     * 
     * Description: 
     * 浏览tar文件,判断是否包含estimate数组中的文件名
     * @param targzFile
     * @throws IOException 
     * @see
     */
    public static  boolean visitTAR(File targzFile,String... estimate) throws IOException {
        FileInputStream fileIn = null;
        BufferedInputStream bufIn = null;
        TarArchiveInputStream taris = null;
        boolean flag = false;
        try {
                fileIn = new FileInputStream(targzFile);
                bufIn = new BufferedInputStream(fileIn);
                taris = new TarArchiveInputStream(bufIn);
                TarArchiveEntry  entry = null;
                while ((entry = taris.getNextTarEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    if (estimate.length > 0) {
                        for (int i=0;i<estimate.length;i++) {
                            if (entry.getName().trim().equals(estimate[i])) {
                                LOG.info("********find specific file name *********** filename:-"+ estimate[i]);
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag)  break;
                }
                return flag;
        }
        finally {
            taris.close();
            bufIn.close();
            fileIn.close();
        }
    }
    
    
    /** 
     * 解压tar包 
     * @param filename 
     *            tar文件 
     * @param directory 
     *            解压目录 
     * @return 
     */  
    public static boolean extTarFileList(File targzFile, String directory,String fileName) {  
        boolean flag = false;  
        OutputStream out = null;  
        try {  
            TarInputStream in = new TarInputStream(new FileInputStream(targzFile));  
            TarEntry entry = null;  
            while ((entry = in.getNextEntry()) != null) {  
                if (entry.isDirectory()) {  
                    continue;  
                }  
                if (fileName.equals(entry.getName())) {
                    File outfile = new File(directory +"/"+ entry.getName());  
                    new File(outfile.getParent()).mkdirs();  
                    out = new BufferedOutputStream(new FileOutputStream(outfile));  
                    int x = 0;  
                    while ((x = in.read()) != -1) {  
                        out.write(x);  
                    }  
                    out.close();                      
                }
            }  
            in.close();  
            flag = true;  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
            flag = false;  
        }  
        return flag;  
    }
    
    /**
     *  
     * Description:
     * 以行为单位读取文件，常用于读面向行的格式化文件
     * 目前特殊的处理镜像包文件中的说明文件，形如：
     * {
            "192.168.0.76:5000/testbonc/demo": {
                "2016-12-28-15-46-44": "7a3b9a636658b0c3b55d415a8b58262e150680a48191c00eb8d359271e21ab4b"
            }
        }
       , 中获得当前镜像的名称和tag信息
     * @param directory 
     * @param fileName 
     * @return result String[]
     */
    @SuppressWarnings("rawtypes")
    public static String[] readFileByLines(String directory,String fileName) {
        String[] result = new String[2];
        File file = new File(directory+"/"+fileName);  
        BufferedReader reader = null;  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            int line = 1;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号  
                System.out.println("line " + line + ": " + tempString);
                JSONObject jsonObj = JSONObject.parseObject(tempString); 
                
                Iterator it = jsonObj.keySet().iterator();  
                while(it.hasNext()){
                    String key = it.next().toString();
                    result[0]= key; // 获取镜像的名称信息
                    
                    JSONObject jsonObj1 = JSONObject.parseObject(jsonObj.get(key).toString()); 
                    Iterator it1 = jsonObj1.keySet().iterator();  
                    while(it1.hasNext()){
                        String key1 = it1.next().toString();
                        result[1] = key1;  // 获取镜像的版本信息
                    }
                }
                line++;  
            }  
            reader.close();  
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
        return result;
    }
}
