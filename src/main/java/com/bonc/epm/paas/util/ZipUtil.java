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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java处理压缩包公共类
 * 目前支持：1.浏览tar中的文件列表
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
        TarInputStream taris = null;
        boolean flag = false;
        try {
                fileIn = new FileInputStream(targzFile);
                bufIn = new BufferedInputStream(fileIn);
                taris = new TarInputStream(bufIn);
                TarEntry entry = null;
                while ((entry = taris.getNextEntry()) != null) {
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
/*                    if (entry.getName().trim().equals("repositories")) {
                        System.out.println("this is a normal image.");
                        break;
                    }
                    if (entry.getName().trim().equals(".dockerinit") || entry.getName().trim().equals(".dockerenv")) {
                        System.out.println("this is a container image.");
                        break;
                    }*/
                }
                return flag;
        } finally {
            taris.close();
            bufIn.close();
            fileIn.close();
        }
    }
}
