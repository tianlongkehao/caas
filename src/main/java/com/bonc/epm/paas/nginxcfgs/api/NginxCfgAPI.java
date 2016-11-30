/*
 * 文件名：NginxCfgAPI.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月28日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.nginxcfgs.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.nginxcfgs.model.NginxCfgs;

/**
 * @author ke_wang
 * @version 2016年11月28日
 * @see NginxCfgAPI
 * @since
 */
public interface NginxCfgAPI {

    /**
     * get a k8snginxcfgs by servername
     * @param servername
     *           servername of k8snginxcfgs
     * @return {@link NginxCfgs}
     * @throws  
     */
    @GET
    @Path("/k8snginxcfgs/{servername}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NginxCfgs getNginxCfgs(@PathParam("servername") String servername);

    /**
     * get all k8snginxcfgs
     * @return {@link NginxCfgs}s
     * @throws  
     */
    @GET
    @Path("/k8snginxcfgs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<NginxCfgs> getAllNginxCfgs();
    
    /**
     * add a k8snginxcfgs
     * @param nginxCfgs
     *           NginxCfgs
     * @return {@link NginxCfgs}
     * @throws  
     */    
    @POST
    @Path("/k8snginxcfgs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean addNginxCfgs(NginxCfgs nginxCfgs);

    /**
     * update a k8snginxcfgs
     * @param servername
     *           servername of k8snginxcfgs
     * @param nginxCfgs
     *           NginxCfgs
     * @return {@link NginxCfgs}
     * @throws  
     */    
    @PUT
    @Path("/k8snginxcfgs/{servername}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateNginxCfgs(@PathParam("servername") String servername, NginxCfgs nginxCfgs);

    /**
     * add a externnginxcfgs
     * @param nginxCfgs
     *           NginxCfgs
     * @return {@link NginxCfgs}
     * @throws  
     */ 
    @POST
    @Path("/externnginxcfgs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean addExternCfgs(NginxCfgs nginxCfgs);
    
    /**
     * update a externnginxcfgs
     * @param  servername
     *           servername of k8snginxcfgs
     * @param nginxCfgs
     *           NginxCfgs
     * @return {@link NginxCfgs}
     * @throws  
     */
    @PUT
    @Path("/externnginxcfgs/{servername}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public boolean updateExternCfgs(@PathParam("servername") String servername, NginxCfgs nginxCfgs);
    
    /**
     * add a testtoolinfo
     * @param name
     *           String
     * @return {@link boolean}
     * @throws  
     */ 
    @PUT
    @Path("/testtoolinfo/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public boolean addTesttool(@PathParam("name") String name);

    /**
     * get testtoolinfo
     * @return {@link String}
     * @throws  
     */     
    @GET
    @Path("/testtoolinfo")
    @Produces(MediaType.APPLICATION_JSON)     
    public List<String> getTesttoolInfo();
}
