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

import java.util.List;

import com.bonc.epm.paas.shera.exceptions.SheraClientException;
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.model.CredentialCheckEntity;
import com.bonc.epm.paas.shera.model.CredentialKey;
import com.bonc.epm.paas.shera.model.CredentialKeyList;
import com.bonc.epm.paas.shera.model.ExecConfig;
import com.bonc.epm.paas.shera.model.GitCredential;
import com.bonc.epm.paas.shera.model.Jdk;
import com.bonc.epm.paas.shera.model.JdkList;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExec;
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecView;
import com.bonc.epm.paas.shera.model.JobExecViewList;
import com.bonc.epm.paas.shera.model.Log;
import com.bonc.epm.paas.shera.model.Rating;
import com.bonc.epm.paas.shera.model.SonarConfig;
import com.bonc.epm.paas.shera.model.SshConfig;
import com.bonc.epm.paas.shera.model.SshKey;

/**
 * @author ke_wang
 * @version 2016年11月11日
 * @see SheraAPIClientInterface
 * @since
 */

public interface SheraAPIClientInterface {
	public static final String VERSION = "v1";

	/* Job API */
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
	 *            job to be exec
	 * @return {@link JobExec}
	 * @throws SheraClientException
	 */
	public JobExecView execJob(String jobId, JobExecView jobExecView) throws SheraClientException;

	public Job updateJob(Job job) throws SheraClientException;

	public Log getExecLog(String name, String seqno, Integer seek) throws SheraClientException;

	public ChangeGit getChangeGit(String name) throws SheraClientException;

	public ChangeGit addGitHooks(String name, ChangeGit changeGit) throws SheraClientException;

	public ChangeGit updateGitHooks(String name, ChangeGit changeGit) throws SheraClientException;

	public ChangeGit deleteGitHooks(String name) throws SheraClientException;

	public Job deleteJob(String jobId) throws SheraClientException;

	public JobExecViewList getJobAllExecutions(String jobId) throws SheraClientException;

	public JobExecView getExecution(String jobId, Integer executionId) throws SheraClientException;

	public JobExecView deleteExecution(String jobId, Integer executionId) throws SheraClientException;

	public JobExecView killExecution(String jobId, Integer executionId) throws SheraClientException;

	/* Credential API */
	public CredentialCheckEntity checkCredential(CredentialCheckEntity credential) throws SheraClientException;

	public CredentialKeyList getAllCredentials() throws SheraClientException;

	public CredentialKey addCredential(GitCredential gitCredential) throws SheraClientException;

	public CredentialKey deleteCredential(String uuid) throws SheraClientException;

	/* Jdk API */
	public JdkList getAllJdk() throws SheraClientException;

	public Jdk deleteJdk(String version) throws SheraClientException;

	public Jdk updateJdk(Jdk jdk) throws SheraClientException;

	public Jdk createJdk(Jdk jdk) throws SheraClientException;

	/* Sonar API */
	public SonarConfig createSonarConfig(SonarConfig sonarConfig) throws SheraClientException;

	public SonarConfig updateSonarConfig(SonarConfig sonarConfig) throws SheraClientException;

	public SonarConfig getSonarConfig() throws SheraClientException;

	public Rating getJobRating(String projectKey) throws SheraClientException;

	public Rating deleteJobRating(String projectKey) throws SheraClientException;

	/* exec config API */
	public ExecConfig createExecConfig(ExecConfig execConfig) throws SheraClientException;

	public void deleteExecConfig(String key) throws SheraClientException;

	public List<ExecConfig> getExecConfig(String kindid) throws SheraClientException;

	/* ssh key API */
	public SshKey createSshKey(SshKey sshKey) throws SheraClientException;

	public void deleteSshKey(String userid) throws SheraClientException;

	public SshKey getSshKey(String userid) throws SheraClientException;

	/* ssh config API */
	public SshConfig createSshConfig(SshConfig sshConfig) throws SheraClientException;

	public void deleteSshConfig(String userid) throws SheraClientException;

	public SshConfig getSshConfig(String userid) throws SheraClientException;

}
