/*
 * 文件名：DockerRegistryAPIClientInterface.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.api;

import javax.ws.rs.core.MultivaluedMap;

import com.bonc.epm.paas.docker.exception.DokcerRegistryClientException;
import com.bonc.epm.paas.docker.model.Images;
import com.bonc.epm.paas.docker.model.Manifest;
import com.bonc.epm.paas.docker.model.Tags;

/**
 * @author ke_wang
 * @version 2016年11月16日
 * @see DockerRegistryAPIClientInterface
 * @since
 */

public interface DockerRegistryAPIClientInterface {
    public static final String VERSION = "v2";
    
    public Images getImages() throws DokcerRegistryClientException;
    
    public Tags getTagsofImage(String name) throws DokcerRegistryClientException;
    
    public MultivaluedMap<String, Object> getManifestofImage(String name,String reference) throws DokcerRegistryClientException;
    
    public void deleteManifestofImage(String name,String reference) throws DokcerRegistryClientException;
}
