/*
 * 文件名：SheraAPIClientInterface.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.api;

import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.model.CredentialCheckEntity;
import com.bonc.epm.paas.shera.model.CredentialKey;
import com.bonc.epm.paas.shera.model.CredentialKeyList;
import com.bonc.epm.paas.shera.model.GitCredential;
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
 * @version 2016年11月11日
 * @see SheraAPIClientInterface
 * @since
 */

public interface SheraAPIClientInterface {
    public static final String VERSION  = "v1";

    /* Job API*/
    public Job getJob(String jobId) throws SheraClientException;
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
    public JobExecView execJob(String jobId,JobExecView jobExecView) throws SheraClientException;
    
    public Job updateJob(Job job) throws SheraClientException;

    public Log getExecLog(String name, String seqno, Integer seek) throws SheraClientException;

    public ChangeGit getChangeGit(String name) throws SheraClientException;

    public ChangeGit addGitHooks(String name, ChangeGit changeGit) throws SheraClientException;
    
    public ChangeGit deleteGitHooks(String name) throws SheraClientException;

    public Job deleteJob(String jobId) throws SheraClientException;
    
    public JobExecViewList getJobAllExecutions(String jobId) throws SheraClientException;

    public JobExecView getExecution(String jobId, Integer executionId) throws SheraClientException;
    
    public JobExecView deleteExecution(String jobId, Integer executionId) throws SheraClientException;
    
    public JobExecView killExecution(String jobId, Integer executionId) throws SheraClientException;
    
    /* Credential API*/
    public CredentialCheckEntity checkCredential(CredentialCheckEntity credential) throws SheraClientException;
    
    public CredentialKeyList getAllCredentials() throws SheraClientException;
    
    public CredentialKey addCredential(GitCredential gitCredential) throws SheraClientException;
    
    public CredentialKey deleteCredential(String username,Integer type) throws SheraClientException;
    
    /* Jdk API*/
    public JdkList getAllJdk() throws SheraClientException;
    
    public Jdk deleteJdk(String jdkVersion) throws SheraClientException;
    
    public Jdk createJdk(Jdk jdk) throws SheraClientException;
    
    
}
