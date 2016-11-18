/*
 * 文件名：SheraAPIClientInterface.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.api;

import javax.ws.rs.PathParam;

import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.Jdk;
import com.bonc.epm.paas.shera.model.JdkList;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecution;
import com.bonc.epm.paas.shera.model.JobExecutionList;

/**
 * @author ke_wang
 * @version 2016年11月11日
 * @see SheraAPIClientInterface
 * @since
 */

public interface SheraAPIClientInterface {
    public static final String VERSION  = "v1";

    /**
     * Get JobExecList Info
     *
     * @return {@link JobExecList}
     * @throws SheraClientException
     */
    public JobExecList getAllJobs() throws SheraClientException;
    
    /**
     * Create a new Job
     * 
     * @param job
     *            job to be created
     * @return {@link Job}
     * @throws SheraClientException
     */
    public Job createJob(Job job) throws SheraClientException;
    
    
    /**
     * exec a Job
     * 
     * @param namespace 
     * @param job-id 
     *              job to be exec
     * @return {@link JobExec}
     * @throws SheraClientException
     */
    public JobExec execJob(String jobId) throws SheraClientException;
    
    public Jdk deleteJdk(String jdkVersion) throws SheraClientException;
    
    public Jdk createJdk(Jdk jdk) throws SheraClientException;
    
    public JdkList getAllJdk() throws SheraClientException;
    
    public JobExecution deleteExecution(String jobId, Integer executionId) throws SheraClientException;
    
    public JobExecution killExecution(String jobId, Integer executionId) throws SheraClientException;
    
    public JobExecution getExecution(String jobId, Integer executionId) throws SheraClientException;
    
    public JobExecutionList getJobAllExecutions(String jobId) throws SheraClientException;
    
    public Job deleteJob(String jobId) throws SheraClientException;
    
    public Job updateJob(Job job) throws SheraClientException;
    
    public Job getJob(String jobId) throws SheraClientException;
    
}
