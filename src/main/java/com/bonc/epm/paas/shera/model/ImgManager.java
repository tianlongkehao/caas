/*
 * 文件名：ImgManager.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月15日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月15日
 * @see ImgManager
 * @since
 */
public class ImgManager {
    private String dockerFileContent;
    private String dockerFile;
    private String imgName;
    
    public String getDockerFileContent() {
        return dockerFileContent;
    }
    public void setDockerFileContent(String dockerFileContent) {
        this.dockerFileContent = dockerFileContent;
    }
    public String getDockerFile() {
        return dockerFile;
    }
    public void setDockerFile(String dockerFile) {
        this.dockerFile = dockerFile;
    }
    public String getImgName() {
        return imgName;
    }
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
    
}
