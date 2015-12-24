package com.bonc.epm.paas.util;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class CmdUtil {

    public static boolean exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String hostExeCmd(String cmd, String host, Integer port) throws IOException {
        Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String res = "";
        try {
            Charset cset = Charset.forName("us-ascii");
            out.println(cmd);
            Integer res_code = Integer.valueOf(in.readLine());
            Integer res_len = Integer.valueOf(in.readLine());

            if (res_len > 0) {
                ByteBuffer buf = ByteBuffer.allocate(res_len);
                in.read(buf.array());
                cset.decode(buf).toString();
            } else {
                res = "";
            }
            if (res_code != 0) {
                throw new Exception(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            in.close();
            socket.close();
        }
        return res;
    }

    //Client中执行命令
    public static String clientExec(String cmdStr, String hostIp) throws IOException {
        String ip = hostIp;
        if ("".equals(hostIp)) {
            ip = "127.0.0.1";
        }
        //执行
        return CmdUtil.hostExeCmd(cmdStr, ip, 1234);
    }
}
