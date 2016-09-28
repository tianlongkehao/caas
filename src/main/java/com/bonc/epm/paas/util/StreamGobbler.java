package com.bonc.epm.paas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 执行shell命令，
 * 处理执行命令行后返回的信息
 * @author ke_wang
 * @version 2016年9月28日
 * @see StreamGobbler
 * @since
 */
public class StreamGobbler extends Thread {
    /**
     * StreamGobbler日志实例
     */
    private static final Log LOG = LogFactory.getLog(StreamGobbler.class);
    /**
     * InputStream is
     */
    private InputStream is;
    /**
     * type
     */
    private String type;  
  
    public StreamGobbler(InputStream is, String type) {  
        this.is = is;  
        this.type = type;  
    }  
  

    @Override
    public void run() {
        try {  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            String line = null;  
            while (null != (line = br.readLine())) {  
                if (type.equals("Error")) {  
                    LOG.info("Error   :" + line);  
                } 
                else {  
                    LOG.info("Debug:" + line);  
                }  
            }  
        } 
        catch (IOException ioe) {  
            ioe.printStackTrace();  
        } 
    }
}
