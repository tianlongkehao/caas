package com.bonc.epm.paas.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSH连接 执行shell命令
 * @author cuidong
 * @version 2015年12月24日
 * @since
 */
public class SshConnect {

    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(SshConnect.class);

    /**
     * INPUTSTREAM
     */
    private static InputStream INPUTSTREAM;
    /**
     * OUTPUTSTREAM
     */
    private static OutputStream OUTPUTSTREAM;
    /**
     * CHANNELSHELL
     */
    private static ChannelShell CHANNELSHELL;
    /**
     * SESSION
     */
    private static Session SESSION;

    /**
     * 
     * Description: 
     * 创建SSH connect
     * @param name String
     * @param password String
     * @param hostIp String
     * @param port Integer
     * @return String 
     * @throws JSchException 
     * @throws IOException  
     */
    public static String connect(String name, String password, String hostIp, Integer port) throws JSchException, IOException {
        JSch jsch = new JSch();
        SESSION = jsch.getSession(name, hostIp, port);
        SESSION.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        SESSION.setConfig(config);
        SESSION.connect();

        CHANNELSHELL = (ChannelShell) SESSION.openChannel("shell");
        CHANNELSHELL.connect(1000);
        CHANNELSHELL.isConnected();

        INPUTSTREAM = CHANNELSHELL.getInputStream();
        OUTPUTSTREAM = CHANNELSHELL.getOutputStream();
        return "";
    }

    /**
     * 
     * Description:
     * @param cmd String
     * @param timeout Integer
     * @return String toString
     * @throws IOException 
     * @throws InterruptedException  
     */
    public static String exec(String cmd, Integer timeout) throws IOException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        String command;
        try {
                // 命令行后面需要补回车
            String cmdTrim = cmd.trim();
            if (cmdTrim.isEmpty()) {
                command = "\n";
            } 
            else {
                Integer index = cmdTrim.length() - 1;
                String a = cmdTrim.substring(index);
                if (Objects.equals("\n", a)) {
                    command = cmdTrim;
                } 
                else {
                    command = cmd + "\n";
                }
            }
            
            OUTPUTSTREAM.write(command.getBytes("UTF-8"));
            OUTPUTSTREAM.flush();
            Thread.sleep(timeout);

            while (INPUTSTREAM.available() > 0) {
                byte[] tmp = ByteBuffer.allocate(INPUTSTREAM.available()).array();
                INPUTSTREAM.read(tmp);
                builder.append(new String(tmp));
            }
        }
        catch (Exception e) {
            LOG.error("error message is :-" + e.getMessage());
        } 
        /*finally {
            OUTPUTSTREAM.close();
            INPUTSTREAM.close();
        }*/
        return builder.toString().trim();
    }

    /**
     * 
     * Description:
     * disconnect
     * @see
     */
    public static void disconnect() {
        CHANNELSHELL.disconnect();
        SESSION.disconnect();
    }
}
