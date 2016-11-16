/*
 * 文件名：DockerRegistryAPI.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.docker.model.Manifest;
import com.bonc.epm.paas.docker.model.Tags;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;

/**
 * @author ke_wang
 * @version 2016年11月16日
 * @see DockerRegistryAPI
 * @since
 */

public interface DockerRegistryAPI {

    /**
     * 
     * Description:
     * Fetch the tags under the repository identified by name
     * @param name
     *              image name
     * @return {@link Tags}
     */
    @GET
    @Path("/{name}/tags/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tags getTagsofImage(@PathParam("name") String name);
    
    /**
     * 
     * Description:
     * Fetch the manifest identified by name and reference where reference can be a tag or digest.
     * @param name
     *            image name
     * @param reference
     *            a tag or digest  
     * @return {@link Manifest}
     */
    @GET
    @Path("/{name}/manifests/{reference}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public Manifest getManifestofImage(@PathParam("name") String name,@PathParam("reference") String reference);
}
