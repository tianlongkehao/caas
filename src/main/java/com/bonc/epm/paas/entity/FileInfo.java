/**
 * 文件名：File.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：root
 * 修改时间：2016年8月17日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;
/**
 * 文件信息类
 * @author YuanPeng
 * @version 2016年9月5日
 * @see FileInfo
 * @since
 */
public class FileInfo {

    /**
     * 是否是目录
     */
    private boolean isDir;
    /**
     * 是否是链接
     */
    private boolean isLink = false;
    /**
     * 文件大小kb
     */
    private String size;
    /**
     * 时间
     */
    private String time;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 修改时间
     */
    private String modifiedTime;
    /**
     * 路径
     */
    private String path;

    public boolean isDir() {
        return isDir;
    }

    /**
     * 是否是目录
     * @param dir 
     * @see
     */
    public void setDir(boolean dir) {
        this.isDir = dir;
        /*
         * if(authority.charAt(0)=='d'){ this.isDir =true; }else{ this.isDir
         * =false; }
         */
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
    // public static void main(String[] args) {
    // String a="abcdef";
    // System.out.println(a.charAt(0));
    // }

	public boolean isLink() {
		return isLink;
	}

	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
}
