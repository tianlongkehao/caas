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
 * @version 2016年11月10日
 * @see SheraAPI
 * @since
 */
public interface SheraAPI {

	/* Job API */

	/**
	 * get a job by id
	 *
	 * @param namespace
	 *            * @param name id of job
	 * @return {@link Job}
	 * @throws SheraClientException
	 */
	@GET
	@Path("/jobs/get/{namespace}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Job getJob(@PathParam("namespace") String namespace, @PathParam("name") String name)
			throws SheraClientException;

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
	 *            job to be exec
	 */
	@POST
	@Path("/jobs/exec/{namespace}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JobExecView execJob(@PathParam("namespace") String namespace, @PathParam("name") String name,
			JobExecView jobExecView) throws SheraClientException;

	/**
	 * update a job
	 *
	 * @param namespace
	 * @param job
	 *            a job
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
	 *
	 * @param namespace
	 * @param name
	 *            id of a job
	 * @param seqno
	 *            seqno of a job execution
	 * @throws SheraClientException
	 * @return {@link Log}
	 */
	@POST
	@Path("/jobs/exec/log/{namespace}/{name}/{seqno}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Log getExecLog(@PathParam("namespace") String namespace, @PathParam("name") String name,
			@PathParam("seqno") String seqno, Integer seek) throws SheraClientException;

	/**
	 * obtain git changes
	 *
	 * @param namespace
	 * @param name
	 *            id of a job
	 * @throws SheraClientException
	 * @return {@link ChangeGit}
	 */
	@GET
	@Path("/git/changes/{namespace}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ChangeGit getChangeGit(@PathParam("namespace") String namespace, @PathParam("name") String name)
			throws SheraClientException;

	/**
	 * add a git hook
	 *
	 * @param namespace
	 * @param name
	 *            id of a job
	 * @param changeGit
	 *            a ChangeGit
	 * @throws SheraClientException
	 * @return {@link ChangeGit}
	 */
	@POST
	@Path("/git/hooks/add/{namespace}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ChangeGit addGitHooks(@PathParam("namespace") String namespace, @PathParam("name") String name,
			ChangeGit changeGit) throws SheraClientException;

	/**
	 * delete a git hook
	 *
	 * @param namespace
	 * @param name
	 *            id of a job
	 * @param changeGit
	 *            a ChangeGit
	 * @throws SheraClientException
	 * @return {@link ChangeGit}
	 */
	@DELETE
	@Path("/git/hooks/del/{namespace}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ChangeGit deleteGitHooks(@PathParam("namespace") String namespace, @PathParam("name") String name)
			throws SheraClientException;

	/**
	 * update a git hook
	 *
	 * @param namespace
	 * @param name
	 *            id of a job
	 * @param changeGit
	 *            a ChangeGit
	 * @throws SheraClientException
	 * @return {@link ChangeGit}
	 */
	@DELETE
	@Path("/git/hooks/update/{namespace}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ChangeGit updateGitHooks(@PathParam("namespace") String namespace, @PathParam("name") String name,
			ChangeGit changeGit) throws SheraClientException;

	/**
	 * delete a job
	 *
	 * @param namespace
	 * @param job
	 *            a job
	 * @throws SheraClientException
	 * @return {@link Job}
	 */
	@DELETE
	@Path("/jobs/del/{namespace}/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Job deleteJob(@PathParam("namespace") String namespace, @PathParam("name") String name)
			throws SheraClientException;

	/**
	 *
	 * Description: get all executions of a job
	 *
	 * @param namespace
	 * @param jobId
	 *            id of a job
	 * @return {@link JobExecution}s
	 * @throws SheraClientException
	 */
	@GET
	@Path("/jobs/get/{namespace}/{name}/executions")
	@Produces(MediaType.APPLICATION_JSON)
	public JobExecViewList getJobAllExecutions(@PathParam("namespace") String namespace, @PathParam("name") String name)
			throws SheraClientException;

	/**
	 *
	 * Description: get a execution of a job
	 *
	 * @param namespace
	 * @param jobId
	 *            id of a job
	 * @param executionId
	 *            id of a jobExecution
	 * @return {@link JobExecution}
	 * @throws SheraClientException
	 */
	@GET
	@Path("/jobs/get/{namespace}/{name}/{seqno}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobExecView getExecution(@PathParam("namespace") String namespace, @PathParam("name") String name,
			@PathParam("seqno") Integer seqno) throws SheraClientException;

	/**
	 *
	 * Description: delete a execution
	 *
	 * @param namespace
	 * @param jobId
	 * @param executionId
	 * @return {@link JobExecution}
	 * @throws SheraClientException
	 */
	@DELETE
	@Path("/jobs/del/{namespace}/{name}/{seqno}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobExecView deleteExecution(@PathParam("namespace") String namespace, @PathParam("name") String name,
			@PathParam("seqno") Integer seqno) throws SheraClientException;

	/**
	 *
	 * Description: kill a execution
	 *
	 * @param namespace
	 * @param jobId
	 * @param executionId
	 * @return {@link JobExecution}
	 * @throws SheraClientException
	 */
	@GET
	@Path("/jobs/kill/{namespace}/{name}/{seqno}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobExecView killExecution(@PathParam("namespace") String namespace, @PathParam("name") String name,
			@PathParam("seqno") Integer seqno) throws SheraClientException;

	/* Credentail API */
	/**
	 *
	 * Description: check a credential
	 *
	 * @param credential
	 *            Credential
	 * @return {@link Credential}
	 * @throws SheraClientException
	 */
	@PUT
	@Path("/credential/check")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CredentialCheckEntity checkCredential(CredentialCheckEntity credential) throws SheraClientException;

	/**
	 *
	 * Description: get all credentials
	 *
	 * @return {@link CredentialKey}s
	 * @throws SheraClientException
	 */
	@GET
	@Path("/credential/all")
	@Produces(MediaType.APPLICATION_JSON)
	public CredentialKeyList getAllCredentials() throws SheraClientException;

	/**
	 *
	 * Description: add a credentials
	 *
	 * @return {@link GitCredential}
	 * @throws SheraClientException
	 */
	@POST
	@Path("/credential/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CredentialKey addCredential(GitCredential gitCredential) throws SheraClientException;

	/**
	 *
	 * Description: delete a credentials
	 *
	 * @return {@link CredentialKey}
	 * @throws SheraClientException
	 */
	@DELETE
	@Path("/credential/del/{uuid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CredentialKey deleteCredential(@PathParam("uuid") String uuid) throws SheraClientException;

	/* JDK API */

	/**
	 *
	 * Description: get all jdk
	 *
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
	 * Description: update jdk
	 *
	 * @return {@link Jdk}s
	 * @throws SheraClientException
	 */
	@PUT
	@Path("/jdk/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Jdk updateJdk(Jdk jdk) throws SheraClientException;

	/**
	 *
	 * Description: delete a jdk
	 *
	 * @param jdkVersion
	 * @return {@link Jdk}
	 * @throws SheraClientException
	 */
	@DELETE
	@Path("/jdk/del/{version}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Jdk deleteJdk(@PathParam("version") String version) throws SheraClientException;

	/**
	 *
	 * Description: create a new jdk
	 *
	 * @param jdk
	 *            a jdk
	 * @return {@link Jdk}
	 * @throws SheraClientException
	 * @see
	 */
	@POST
	@Path("/jdk/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Jdk createJdk(Jdk jdk) throws SheraClientException;

	/**
	 *
	 * Description: create sonar config for one namespace
	 *
	 * @param SonarConfig
	 * @return {@link SonarConfig}
	 * @throws SheraClientException
	 * @see
	 */
	@POST
	@Path("/sonar/config/{namespace}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SonarConfig createSonarConfig(@PathParam("namespace") String namespace, SonarConfig sonarConfig)
			throws SheraClientException;

	/**
	 *
	 * Description: update sonar config for one namespace
	 *
	 * @param SonarConfig
	 * @return {@link SonarConfig}
	 * @throws SheraClientException
	 * @see
	 */
	@PUT
	@Path("/sonar/update/{namespace}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SonarConfig updateSonarConfig(@PathParam("namespace") String namespace, SonarConfig sonarConfig)
			throws SheraClientException;

	/**
	 *
	 * Description: get sonar config for one namespace
	 *
	 * @param namespace
	 * @param sonarConfig
	 * @return SonarConfig
	 * @throws SheraClientException
	 */
	@GET
	@Path("/sonar/get/{namespace}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SonarConfig getSonarConfig(@PathParam("namespace") String namespace) throws SheraClientException;

	/**
	 *
	 * Description: get quality rating
	 *
	 * @param namespace
	 * @param sonarConfig
	 * @return SonarConfig
	 * @throws SheraClientException
	 */
	@GET
	@Path("/sonar/rating/{namespace}/{projectKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rating getJobRating(@PathParam("namespace") String namespace, @PathParam("projectKey") String projectKey)
			throws SheraClientException;

	/**
	 *
	 * Description: delete one sonar project
	 *
	 * @param namespace
	 * @param sonarConfig
	 * @return SonarConfig
	 * @throws SheraClientException
	 */
	@DELETE
	@Path("/sonar/{projectKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rating deleteJobRating(@PathParam("namespace") String namespace, @PathParam("projectKey") String projectKey)
			throws SheraClientException;

	/**
	 * createExecConfig:创建shera的工具配置. <br/>
	 *
	 * @author longkaixiang
	 * @param execConfig
	 * @return ExecConfig
	 */
	@POST
	@Path("/exec")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ExecConfig createExecConfig(ExecConfig execConfig);

	/**
	 * deleteExecConfig:删除shera的工具配置. <br/>
	 *
	 * @author longkaixiang
	 * @param userid
	 * @param kindid
	 * @return ExecConfig
	 */
	@DELETE
	@Path("/exec/{userid}/{kindid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteExecConfig(@PathParam("userid") String userid, @PathParam("kindid") Integer kindid);

	/**
	 * deleteExecConfig:删除shera的工具配置. <br/>
	 *
	 * @author longkaixiang
	 * @param key void
	 */
	@DELETE
	@Path("/exec/{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteExecConfig(@PathParam("key") String key);

	/**
	 * updateExecConfig:修改shera的工具配置. <br/>
	 *
	 * @author longkaixiang
	 * @param key
	 * @param execConfig void
	 */
	@PUT
	@Path("/exec/{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ExecConfig updateExecConfig(@PathParam("key") String key, ExecConfig execConfig);

	/**
	 * getExecConfig:获取shera的工具配置. <br/>
	 *
	 * @author longkaixiang
	 * @param key
	 * @param kindid void
	 */
	@GET
	@Path("/exec/{userid}/{kindid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getExecConfig(@PathParam("userid") String userid, @PathParam("kindid") Integer kindid);

	/**
	 * createSshKey:create ssh rsa public key and private key. <br/>
	 *
	 * @author longkaixiang
	 * @param sshKey
	 * @return SshKey
	 */
	@POST
	@Path("/ssh")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SshKey createSshKey(SshKey sshKey);

	/**
	 * createSshKey:del ssh rsa from db by user id. <br/>
	 *
	 * @author longkaixiang
	 * @param userid
	 * @return SshKey
	 */
	@DELETE
	@Path("/ssh/{userid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteSshKey(@PathParam("userid") String userid);

	/**
	 * getSshKey:get ssh rsa from db by user id. <br/>
	 *
	 * @author longkaixiang
	 * @param userid
	 * @return SshKey
	 */
	@GET
	@Path("/ssh/{userid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SshKey getSshKey(@PathParam("userid") String userid);

	/**
	 * createSshConfig:create ssh config. <br/>
	 *
	 * @author longkaixiang
	 * @param sshConfig
	 * @return SshConfig
	 */
	@POST
	@Path("/ssh/config")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SshConfig createSshConfig(SshConfig sshConfig);

	/**
	 * deleteSshConfig:delete ssh config. <br/>
	 *
	 * @author longkaixiang
	 * @param userid
	 * @return SshConfig
	 */
	@DELETE
	@Path("/ssh/config/{userid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SshConfig deleteSshConfig(@PathParam("userid") String userid);

	/**
	 * getSshConfig:get  ssh config. <br/>
	 *
	 * @author longkaixiang
	 * @param userid
	 * @return SshConfig
	 */
	@GET
	@Path("/ssh/config/{userid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SshConfig getSshConfig(@PathParam("userid") String userid);

}
