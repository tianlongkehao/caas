/*
 * 文件名：SheraAPIClient.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.api;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.Jdk;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecution;
import com.bonc.epm.paas.shera.model.JobExecutionList;

/**
 * @author ke_wang
 * @version 2016年11月11日
 * @see SheraAPIClient
 * @since
 */

public class SheraAPIClient implements SheraAPIClientInterface {
    
    private static final Log LOG = LogFactory.getLog(KubernetesApiClient.class);
    
    private String sRURI;
    private SheraAPI api;
    private String namespace;
    
    public SheraAPIClient(String srUrl, String namespace, String username, String password, RestFactory factory) {
        this.namespace = namespace;
        this.sRURI = srUrl +"She-Ra";
        api = factory.createSheRaAPI(sRURI,username,password);
    }

    @Override
    public JobExecList getAllJobs() throws SheraClientException {
        try {
            return api.getAllJobs(namespace);
        } catch (NotFoundException e) {
            return new JobExecList();
        } catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public Job createJob(Job job) throws SheraClientException {
        try {
            return api.createJob(namespace, job);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }


    @Override
    public JobExec execJob(String jobId) throws SheraClientException {
        try {
            return api.execJob(namespace, jobId);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#deleteJdk(java.lang.String)
     */
    @Override
    public Jdk deleteJdk(String jdkVersion)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#createJdk(com.bonc.epm.paas.shera.model.Jdk)
     */
    @Override
    public Jdk createJdk(Jdk jdk)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#deleteExecution(java.lang.String, java.lang.Integer)
     */
    @Override
    public JobExecution deleteExecution(String jobId, Integer executionId)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#killExecution(java.lang.String, java.lang.Integer)
     */
    @Override
    public JobExecution killExecution(String jobId, Integer executionId)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#getExecution(java.lang.String, java.lang.Integer)
     */
    @Override
    public JobExecution getExecution(String jobId, Integer executionId)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#getJobAllExecutions(java.lang.String)
     */
    @Override
    public JobExecutionList getJobAllExecutions(String jobId)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#deleteJob(java.lang.String)
     */
    @Override
    public Job deleteJob(String jobId)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#updateJob(com.bonc.epm.paas.shera.model.Job)
     */
    @Override
    public Job updateJob(Job job)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#getJob(java.lang.String)
     */
    @Override
    public Job getJob(String jobId)
        throws SheraClientException {
        // TODO Auto-generated method stub
        return null;
    }
}
