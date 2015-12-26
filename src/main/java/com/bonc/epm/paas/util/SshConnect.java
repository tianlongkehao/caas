package com.bonc.epm.paas.util;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by cuidong on 15-12-24.
 */
public class SshConnect {

    private static InputStream inputStream;
    private static OutputStream outputString;
    private static ChannelShell channelShell;
    private static Session session;


    public static String connect(String name, String password, String hostIp, Integer port) throws JSchException, IOException {
        JSch jsch = new JSch();
        session = jsch.getSession(name, hostIp, port);
        Properties config = new Properties();

        channelShell = (ChannelShell) session.openChannel("shell");


        session.setPassword(password);
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        channelShell.connect(1000);
        inputStream = channelShell.getInputStream();
        outputString = channelShell.getOutputStream();
        return "";
    }

    public static String exec(String cmd, Integer timeout) throws IOException, InterruptedException {
        // 命令行后面需要补回车
        String command;
        String cmdTrim = cmd.trim();
        if (cmdTrim.isEmpty()) {
            command = "\n";
        } else {
            Integer index = cmdTrim.length() - 1;
            String a = cmdTrim.substring(index);
            if (Objects.equals("\n", a)) {
                command = cmd + "\n";
            } else {
                command = cmdTrim;
            }
        }
        String charset = "UTF-8";
        outputString.write(command.getBytes(charset));
        outputString.flush();
        Thread.sleep(timeout);

        StringBuilder builder = new StringBuilder();
        while (inputStream.available() > 0) {
            byte[] tmp = ByteBuffer.allocate(inputStream.available()).array();
            inputStream.read(tmp);
            String str = new String(tmp);
            builder.append(str);
        }
        String str = builder.toString().trim();
        if (str.isEmpty()) {
            return "未知错误:命令执行结果为空";
        } else {
            switch (str) {
                case "$":
                    return str;
                case "#":
                case ":":
                    return str;
                case "?":
                    return str;
                default:
                    return str;

            }
        }
    }

    public static void disconnect() {
        channelShell.disconnect();
        session.disconnect();
    }
}
