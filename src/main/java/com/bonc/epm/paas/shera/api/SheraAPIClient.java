/*
 * 文件名：SheraAPIClient.java
 * 版权：Copyright by www.bonc.com.cn
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
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.model.CredentialCheckEntity;
import com.bonc.epm.paas.shera.model.CredentialKey;
import com.bonc.epm.paas.shera.model.CredentialKeyList;
import com.bonc.epm.paas.shera.model.GitCredential;
import com.bonc.epm.paas.shera.model.Jdk;
import com.bonc.epm.paas.shera.model.JdkList;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecView;
import com.bonc.epm.paas.shera.model.JobExecViewList;

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
        this.sRURI = srUrl +"she-ra";
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
    public JobExecView execJob(String name) throws SheraClientException {
        try {
            return api.execJob(namespace, name);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public Jdk deleteJdk(String jdkVersion) throws SheraClientException {
        try {
            return api.deleteJdk(jdkVersion);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public Jdk createJdk(Jdk jdk) throws SheraClientException {
        try {
            return api.createJdk(jdk);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public JobExecView deleteExecution(String name, Integer seqno) throws SheraClientException {
        try {
            return api.deleteExecution(namespace, name, seqno);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public JobExecView killExecution(String name, Integer seqno) throws SheraClientException {
        try {
            return api.killExecution(namespace, name, seqno);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public JobExecView getExecution(String name, Integer seqno) throws SheraClientException {
        try {
            return api.killExecution(namespace, name, seqno);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public JobExecViewList getJobAllExecutions(String name) throws SheraClientException {
        try {
            return api.getJobAllExecutions(namespace, name);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public Job deleteJob(String name) throws SheraClientException {
        try {
            return api.deleteJob(namespace, name);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public Job updateJob(Job job) throws SheraClientException {
        try {
            return api.updateJob(namespace, job);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public Job getJob(String name) throws SheraClientException {
        try {
            return api.getJob(namespace, name);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public com.bonc.epm.paas.shera.model.Log getExecLog(String name, String seqno,
                                                        com.bonc.epm.paas.shera.model.Log log) throws SheraClientException {
        try {
            return api.getExecLog(namespace, name, seqno, log);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public ChangeGit getChangeGit(String name) throws SheraClientException {
        try {
            return api.getChangeGit(namespace, name);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public ChangeGit addGitHooks(String name, ChangeGit changeGit) throws SheraClientException {
        try {
            return api.addGitHooks(namespace, name, changeGit);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public ChangeGit deleteGitHooks(String name, ChangeGit changeGit) throws SheraClientException {
        try {
            return api.deleteGitHooks(namespace, name, changeGit);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public JdkList getAllJdk() throws SheraClientException {
        try {
            return api.getAllJdk();
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public CredentialCheckEntity checkCredential(CredentialCheckEntity credential) throws SheraClientException {
        try {
            return api.checkCredential(credential);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public CredentialKeyList getAllCredentials() throws SheraClientException {
        try {
            return api.getAllCredentials();
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public GitCredential addCredential(GitCredential gitCredential) throws SheraClientException {
        try {
            return api.addCredential(gitCredential);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }

    @Override
    public CredentialKey deleteCredential(CredentialKey credentialKey) throws SheraClientException {
        try {
            return api.deleteCredential(credentialKey);
        }
        catch (NotFoundException e) {
            return null;
        }
        catch (WebApplicationException e) {
            throw new SheraClientException(e.getMessage());
        }
    }
}
