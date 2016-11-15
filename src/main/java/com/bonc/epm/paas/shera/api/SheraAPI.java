/*
 * 文件名：SheraAPI.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see SheraAPI
 * @since
 */
public interface SheraAPI {

    /**
     * Get all JobExecs.
     * 
     * @return {@link JobExec}s
     */
    @GET
    @Path("/jobs/getall/{namespace}")
    @Consumes(MediaType.APPLICATION_JSON)
    public JobExecList getAllJobs(@PathParam("namespace") String namespace) throws SheraClientException;
    
    /**
     * Create a new Job
     * 
     * @param job
     *            job to be created
     */
    @POST
    @Path("/jobs/create/{namespace}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Job createJob(@PathParam("namespace") String namespace, Job job) throws SheraClientException;

    /**
     * exec a Job
     * 
     * @param namespace 
     * @param job-id 
     *              job to be exec
     */
    @POST
    @Path("/jobs/exec/{namespace}/{job-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public JobExec execJob(@PathParam("namespace") String namespace, @PathParam("job-id") String jobId) throws SheraClientException;
}
