/*
 * 文件名：StreamGobbler.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：root
 * 修改时间：2016年9月28日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 执行shell命令，
 * 处理执行命令行后返回的信息
 * @author ke_wang
 * @version 2016年9月28日
 * @see StreamGobbler
 * @since
 */
public class StreamGobbler extends Thread {  
    
    InputStream is;  
    String type;  
  
    public StreamGobbler(InputStream is, String type) {  
        this.is = is;  
        this.type = type;  
    }  
  
    /**
     * @throws IOException  br.close()
     */
    public void run() {
        try {  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            String line = null;  
            while (null != (line = br.readLine())) {  
                if (type.equals("Error")) {  
                    System.out.println("Error   :" + line);  
                } else {  
                    System.out.println("Debug:" + line);  
                }  
            }  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
          } 
    }
}
