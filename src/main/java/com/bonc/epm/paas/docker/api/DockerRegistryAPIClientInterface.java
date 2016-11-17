/*
 * 文件名：DockerRegistryAPIClientInterface.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.api;

import javax.ws.rs.PathParam;

import com.bonc.epm.paas.docker.model.Manifest;
import com.bonc.epm.paas.docker.model.Tags;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author ke_wang
 * @version 2016年11月16日
 * @see DockerRegistryAPIClientInterface
 * @since
 */

public interface DockerRegistryAPIClientInterface {
    public static final String VERSION = "v2";
    
    public Tags getTagsofImage(String name);
    
    public Manifest getManifestofImage(String name,String reference);
}
