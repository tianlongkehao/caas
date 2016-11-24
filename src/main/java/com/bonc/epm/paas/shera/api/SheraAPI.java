/*
 * 文件名：SheraAPI.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.model.Jdk;
import com.bonc.epm.paas.shera.model.JdkList;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecView;
import com.bonc.epm.paas.shera.model.JobExecViewList;
import com.bonc.epm.paas.shera.model.Log;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see SheraAPI
 * @since
 */
public interface SheraAPI {
    
    /* Job API*/
    
    /**
     * get a job by id
     * @param namespace
     * @param name
     *           id of job
     * @return {@link Job}
     * @throws SheraClientException 
     */
    @GET
    @Path("/jobs/get/{namespace}/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Job getJob(@PathParam("namespace") String namespace ,@PathParam("name") String name) throws SheraClientException;
    
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
    @Produces(MediaType.APPLICATION_JSON)
    public Job createJob(@PathParam("namespace") String namespace, Job job) throws SheraClientException;
    
    /**
     * exec a Job
     * 
     * @param namespace 
     * @param job-id 
     *              job to be exec
     */
    @POST
    @Path("/jobs/exec/{namespace}/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JobExecView execJob(@PathParam("namespace") String namespace, @PathParam("name") String name, JobExecView jobExecView) throws SheraClientException;
    
    /**
     * update a job 
     * @param namespace
     * @param job
     *         a job
     * @throws SheraClientException 
     * @return {@link Job}
     */
    @PUT
    @Path("/jobs/update/{namespace}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public Job updateJob(@PathParam("namespace") String namespace, Job job) throws SheraClientException;

    /**
     * get a job exec log
     * @param namespace
     * @param name
     *         id of a job
     * @param seqno
     *         seqno of a job execution
     * @throws SheraClientException 
     * @return {@link Log}
     */    
    @POST
    @Path("/jobs/exec/log/{namespace}/{name}/{seqno}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)     
    public Log getExecLog(@PathParam("namespace") String namespace, @PathParam("name") String name, @PathParam("seqno") String seqno, Log log) throws SheraClientException;
    
    /**
     * obtain git changes
     * @param namespace
     * @param name
     *         id of a job
     * @throws SheraClientException 
     * @return {@link ChangeGit}
     */  
    @POST
    @Path("/git/changes/{namespace}/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)        
    public ChangeGit getChangeGit(@PathParam("namespace") String namespace, @PathParam("name") String name) throws SheraClientException;

    /**
     * add a git hook 
     * @param namespace
     * @param name
     *         id of a job
     * @param changeGit
     *          a ChangeGit
     * @throws SheraClientException 
     * @return {@link ChangeGit}
     */
    @POST
    @Path("/git/hooks/add/{namespace}/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)  
    public ChangeGit addGitHooks(@PathParam("namespace") String namespace, @PathParam("name") String name,ChangeGit changeGit) throws SheraClientException;

    /**
     * delete a git hook 
     * @param namespace
     * @param name
     *         id of a job
     * @param changeGit
     *          a ChangeGit
     * @throws SheraClientException 
     * @return {@link ChangeGit}
     */
    @DELETE
    @Path("/git/hooks/del/{namespace}/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeGit deleteGitHooks(@PathParam("namespace") String namespace, @PathParam("name") String name,ChangeGit changeGit) throws SheraClientException;
    
    /**
     * delete a job 
     * @param namespace
     * @param job
     *         a job
     * @throws SheraClientException 
     * @return {@link Job}
     */
    @DELETE
    @Path("/jobs/del/{namespace}/{name}")
    @Produces(MediaType.APPLICATION_JSON)  
    public Job deleteJob(@PathParam("namespace") String namespace ,@PathParam("name") String name) throws SheraClientException;    

    /**
     * 
     * Description:
     * get all executions of a job
     * @param namespace
     * @param jobId
     *              id of a job
     * @return {@link JobExecution}s
     * @throws SheraClientException 
     */
    @GET
    @Path("/jobs/get/{namespace}/{name}/executions")
    @Produces(MediaType.APPLICATION_JSON)
    public JobExecViewList getJobAllExecutions(@PathParam("namespace") String namespace ,@PathParam("name") String name) throws SheraClientException;

    /**
     * 
     * Description:
     * get a execution of a job
     * @param namespace
     * @param jobId
     *              id of a job
     * @param executionId
     *              id of a jobExecution
     * @return {@link JobExecution}
     * @throws SheraClientException 
     */
    @GET
    @Path("/jobs/get/{namespace}/{name}/{seqno}")
    @Produces(MediaType.APPLICATION_JSON)
    public JobExecView getExecution(@PathParam("namespace") String namespace ,@PathParam("name") String name, @PathParam("seqno") Integer seqno) throws SheraClientException;
    
    /**
     * 
     * Description: 
     * delete a execution
     * @param namespace
     * @param jobId
     * @param executionId
     * @return {@link JobExecution}
     * @throws SheraClientException 
     */
    @DELETE
    @Path("/jobs/del/{namespace}/{name}/{seqno}")
    @Produces(MediaType.APPLICATION_JSON)
    public JobExecView deleteExecution(@PathParam("namespace") String namespace ,@PathParam("name") String name, @PathParam("seqno") Integer seqno) throws SheraClientException;
    
    /**
     * 
     * Description: 
     * kill a execution
     * @param namespace
     * @param jobId
     * @param executionId
     * @return {@link JobExecution}
     * @throws SheraClientException 
     */
    @PUT
    @Path("/jobs/kill/{namespace}/{name}/{seqno}")
    @Produces(MediaType.APPLICATION_JSON)
    public JobExecView killExecution(@PathParam("namespace") String namespace ,@PathParam("name") String name, @PathParam("seqno") Integer seqno) throws SheraClientException;

    
    /* JDK API */

    /**
     * 
     * Description: 
     * get all jdk
     * @return {@link Jdk}s
     * @throws SheraClientException 
     */
    @GET
    @Path("/jdk/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public JdkList getAllJdk() throws SheraClientException;
    /**
     * 
     * Description: 
     * delete a jdk
     * @param jdkVersion
     * @return {@link Jdk}
     * @throws SheraClientException 
     */
    @DELETE
    @Path("/jdk/del/{jdkVersion}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Jdk deleteJdk(@PathParam("jdkVersion") String jdkVersion) throws SheraClientException;

    /**
     * 
     * Description:
     *  create a new jdk 
     * @param jdk
     *         a jdk
     * @return {@link Jdk}
     * @throws SheraClientException 
     * @see
     */
    @POST
    @Path("/jdk/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Jdk createJdk(Jdk jdk) throws SheraClientException;
}
