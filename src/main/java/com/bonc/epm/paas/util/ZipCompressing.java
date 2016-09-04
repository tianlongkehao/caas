package com.bonc.epm.paas.util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tools.zip.*;

import java.io.*;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
 
/**
 * Function : 文件压缩成zip
 * @author  : lqf
 * @Date    : 2015-12-15
 */
public class ZipCompressing {
    private static final Logger logger = LoggerFactory.getLogger(ZipCompressing.class);
 
    static int k = 1; // 定义递归次数变量
    public ZipCompressing() {}
 
 
    /**
     * 压缩指定的单个或多个文件，如果是目录，则遍历目录下所有文件进行压缩
     * @param zipFileName ZIP文件名包含全路径
     * @param files  文件列表
     */
    public static boolean zip(String zipFileName, File[] files) {
        logger.info("压缩: "+zipFileName);
        ZipOutputStream out = null;
        BufferedOutputStream bo = null;
        try {
            createDir(zipFileName);
            out = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (int i = 0; i < files.length; i++) {
                if (null != files[i]) {
                    zip(out, files[i], files[i].getName());
                }
            }
            out.close(); // 输出流关闭
            logger.info("压缩完成");
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
 
    /**
     * 执行压缩
     * @param out ZIP输入流
     * @param f   被压缩的文件
     * @param base  被压缩的文件名
     */
    private static void zip(ZipOutputStream out, File f, String base) { // 方法重载
        try {
            if (f.isDirectory()) {//压缩目录
                try {
                    File[] fl = f.listFiles();
                    if (fl.length == 0) {
                        ZipEntry zipEntry = new ZipEntry(base + "/");
                        zipEntry.setUnixMode(755);//解决linux乱码   
                        out.putNextEntry(zipEntry);  // 创建zip实体
                        logger.info(base + "/");
                    }
                    for (int i = 0; i < fl.length; i++) {
                        zip(out, fl[i], base + "/" + fl[i].getName()); // 递归遍历子文件夹
                    }
                    //System.out.println("第" + k + "次递归");
                    k++;
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }else{ //压缩单个文件
                logger.info(base);
                ZipEntry zipEntry = new ZipEntry(base);
                zipEntry.setUnixMode(755);//解决linux乱码   
                out.putNextEntry(zipEntry); // 创建zip实体
                FileInputStream in = new FileInputStream(f);
                BufferedInputStream bi = new BufferedInputStream(in);
                int b;
                while ((b = bi.read()) != -1) {
                    out.write(b); // 将字节流写入当前zip目录
                }
                out.setEncoding("UTF-8");
                out.closeEntry(); //关闭zip实体
                in.close(); // 输入流关闭
            }
 
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
 
 
    /**
     * 目录不存在时，先创建目录
     * @param zipFileName
     */
    private static void createDir(String zipFileName){
        String filePath = StringUtils.substringBeforeLast(zipFileName, "/");
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {//目录不存在时，先创建目录
            targetFile.mkdirs();
        }
    }
 
 
    /**
     * @param args
     */
//    public static void main(String[] args) {
//        try {
//            File[] files = new File[2];
//            files[0] = new File("/mnt/yuan");files[1] = new File("/mnt/远鹏.txt");
//            ZipCompressing.zip("/mnt/远鹏.zip",files);   //测试多个文件
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
// 
//    }
}