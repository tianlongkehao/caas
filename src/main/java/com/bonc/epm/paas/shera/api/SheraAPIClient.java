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
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;

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
    
    public SheraAPIClient(String srUrl, String namespace, RestFactory factory) {
        this.namespace = namespace;
        this.sRURI = srUrl +"She-Ra/"+SheraAPIClientInterface.VERSION;
        api = factory.createSheRaAPI(sRURI);
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
}
