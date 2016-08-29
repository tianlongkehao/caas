/*
 * 文件名：File.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：root
 * 修改时间：2016年8月17日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import org.springframework.beans.factory.annotation.Value;

public class FileInfo {
    
    /**
     * 是否是目录
     */
    boolean isDir;
    /**
     * 文件大小kb
     */
    String size;
    /**
     * 时间
     */
    String time;
    /**
     * 文件名
     */
    String fileName;
    /**
     * 修改时间
     */
    String modifiedTime;
    /**
     * 路径
     */
    String path;
    
    public boolean isDir() {
            return isDir;
        }
    public void setDir(boolean dir) {
        this.isDir =dir;
        /* if(authority.charAt(0)=='d'){
            this.isDir =true;
        }else{
            this.isDir =false;
                 }*/
        
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
//    public static void main(String[] args) {
//        String  a="abcdef";
//        System.out.println(a.charAt(0));
//    }
}

