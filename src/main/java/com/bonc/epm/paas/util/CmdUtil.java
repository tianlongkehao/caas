package com.bonc.epm.paas.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CmdUtil
 * @author ke_wang
 * @version 2016年8月31日
 * @see CmdUtil
 * @since
 */
public class CmdUtil {
    /**
     * CmdUtil日志实例
     */
    private static final Log LOG = LogFactory.getLog(CmdUtil.class);
  
    /**
     * 
     * Description:
     *  在控制台执行输入的命令行
     * @param commandStr 字符串化的命令行
     * @return  boolean 
     * @throws IOException  br.close()
     * @see
     */
    public static boolean exeCmd(String commandStr) throws IOException {
        try {
            Process process = Runtime.getRuntime().exec(commandStr);
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");
            errorGobbler.start();
            outputGobbler.start();
            try {
                process.waitFor();
            }
            catch (InterruptedException e) {
                LOG.error("exec command failed. InterruptedException message:-" + e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }
        catch (IOException e) {
            LOG.error("exec command failed. IOException message:-" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 
     * Description: 在制定目录下执行命令
     * 
     * @param commandStr 命令
     * @param dir 目录
     * @return boolean true或false
     * @throws IOException 
     * @see
     */
    public static boolean exeCmd(String commandStr ,String dir) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            File file = new File(dir);
            Process p = Runtime.getRuntime().exec(commandStr,null ,file);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while (null != (line = br.readLine())) {
                sb.append(line + "\n");
            }
            return true;
        } 
        catch (Exception e) {
            LOG.error("exec command failed. message:-" + e.getMessage());
            e.printStackTrace();
            return false;
        } 
        finally {
            if (br != null) {
                br.close();
            }
        }
    }
    /**
     * 
     * Description:
     * 连接服务器，执行命令行
     * @param cmd 命令行
     * @param host 主机IP地址
     * @param port 主键端口
     * @return res 返回信息
     * @throws IOException 
     * @see Socket,PrintWriter,DataInputStream
     */
    @SuppressWarnings("deprecation")
    private static String hostExeCmd(String cmd, String host, Integer port) throws IOException {
        String res = "";
        Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        try {
            Charset cset = Charset.forName("us-ascii");
            out.println(cmd);
            Integer resCode = Integer.valueOf(in.readLine());
            Integer resLen = Integer.valueOf(in.readLine());

            if (resLen > 0) {
                ByteBuffer buf = ByteBuffer.allocate(resLen);
                in.read(buf.array());
                res = cset.decode(buf).toString();
            } 
            if (resCode != 0) {
                throw new Exception(res);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            out.close();
            in.close();
            socket.close();
        }
        return res;
    }

    /**
     * 
     * Description:
     * Client中执行命令
     * @param cmdStr 命令行
     * @param hostIp 主机IP地址
     * @return string 
     * @throws IOException 
     * @see
     */
    public static String clientExec(String cmdStr, String hostIp) throws IOException {
        String ip = hostIp;
        if ("".equals(hostIp)) {
            ip = "127.0.0.1";
        }
        return CmdUtil.hostExeCmd(cmdStr, ip, 1234);
    }
}
