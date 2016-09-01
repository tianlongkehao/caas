/*
* Created on 2009-9-14
* Copyright 2009 by www.xfok.net. All Rights Reserved
*
*/

package com.bonc.epm.paas.util;
import com.bonc.epm.paas.controller.CephController;
import com.bonc.epm.paas.entity.FileInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
* @author YuanPeng
* 转载请注明出处：http://www.xfok.net/2009/10/124485.html
*/
public class SFTPUtil {

/**
* 连接sftp服务器
* @param host 主机
* @param port 端口
* @param username 用户名
* @param password 密码
* @return
*/
    public static ChannelSftp connect(String host, int port, String username,String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

/**
* 上传文件
* @param directory 上传的目录
* @param file2 要上传的文件
* @param sftp
*/
public void upload(String directory, MultipartFile file, ChannelSftp sftp) {
    try {
        sftp.cd(directory);
        sftp.put(new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/**
* 下载文件
* @param directory 下载目录
* @param downloadFile 下载的文件
* @param saveFile 存在本地的路径
* @param sftp
*/
//暂时无效
public InputStream download2(String downloadFile,String directory, ChannelSftp sftp) {
    try {
        sftp.cd(directory);
        return sftp.get(downloadFile);
    
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
/**
 * 
 * Description: 下载文件新方法
 * 
 * @param downloadFile
 * @param directory
 * @param sftp
 * @return 
 * @see
 */
public void download(String downloadFile,String directory, ChannelSftp sftp ,String  dirPath) {
    try {
        sftp.cd(directory);
        File file =new File(dirPath);  
        if(!file .exists()  && !file .isDirectory()){       
          file .mkdir(); 
                  }
        File dfile =new File(dirPath+downloadFile);
        sftp.get(downloadFile, new FileOutputStream(dfile));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/**
* 删除文件
* @param directory 要删除文件所在目录
* @param deleteFile 要删除的文件
* @param sftp
*/
public void delete(String directory, String deleteFile, ChannelSftp sftp) {
    try {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/**
* 列出目录下的文件
* @param directory 要列出的目录
* @param sftp
* @return
* @throws SftpException
*/
public static List<FileInfo> listFileInfo(String directory){
    List<FileInfo> list = new ArrayList<FileInfo>();
        File file = new File(directory);
        File [] tempList =file.listFiles();
        FileInfo ff = new FileInfo();
        ff.setDir(true);
        ff.setFileName("..");
        ff.setPath(directory);
        ff.setSize(" ");
        ff.setModifiedTime(" ");
        list.add(ff);
        for(int i=0;i<tempList.length;i++){
            if(!".".equals(tempList[i].getName())){
            FileInfo fi = new FileInfo();
            fi.setDir(tempList[i].isDirectory());
            fi.setFileName(tempList[i].getName());
            fi.setSize(String.valueOf(tempList[i].length()/1024));
            fi.setPath(directory);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            fi.setTime(formatter.format(tempList[i].lastModified()));
            list.add(fi);
                            }
                }
    return list;
}
/**
 * 
 * Description: 读取修改时间的方法
 * 
 * @param args 
 * @return String 例如： 2009-08-07 10:00:00
 * @see
 */
public static String getModifiedTime(File f){
    Calendar cal = Calendar.getInstance();
    long time = f.lastModified();
    SimpleDateFormat formatter = new SimpleDateFormat();
    cal.setTimeInMillis(time);
    return formatter.format(cal.getTime());
}
/**
 * 
 * Description:获取文件的大小 MB
 * 
 * @param f
 * @return String
 * @see
 */
public static String getFileSize(File f){
    long size=f.length(); //单位 b
    size = size/1024/1024;
    return  String.valueOf(size);
       
}

//    public static void main(String[] args) {
//        SFTPUtil sf = new SFTPUtil(); 
//        String host = "192.168.0.21";
//        int port = 22;
//        String username = "root";
//        String password = "root.123";
//        String directory = "/mnt/k8s";
//        String uploadFile = "D:\\tmp\\upload.txt";
//        String downloadFile = "upload.txt";
//        String saveFile = "D:\\tmp\\download.txt";
//        String deleteFile = "delete.txt";
//        ChannelSftp sftp=sf.connect(host, port, username, password);
//            try{
//                //sftp.cd(directory);
//                //sftp.mkdir("ss");
//                System.out.println("finished");
//            }catch(Exception e){
//                e.printStackTrace();
//            } 
//        }
}