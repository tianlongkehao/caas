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

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.rest.util.RestFactory;
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
import com.bonc.epm.paas.shera.model.JobExecList;
import com.bonc.epm.paas.shera.model.JobExecView;
import com.bonc.epm.paas.shera.model.JobExecViewList;
import com.bonc.epm.paas.shera.model.Rating;
import com.bonc.epm.paas.shera.model.SonarConfig;
import com.bonc.epm.paas.shera.model.SshConfig;
import com.bonc.epm.paas.shera.model.SshKey;

/**
 * @author ke_wang
 * @version 2016年11月11日
 * @see SheraAPIClient
 * @since
 */

public class SheraAPIClient implements SheraAPIClientInterface {

	private static final Log LOG = LogFactory.getLog(SheraAPIClient.class);

	private String sRURI;
	private SheraAPI api;
	private String namespace;

	public SheraAPIClient(String srUrl, String namespace, String username, String password, RestFactory factory) {
		LOG.info("开始连接shera......");
		this.namespace = namespace;
		this.sRURI = srUrl;
		api = factory.createSheRaAPI(sRURI, username, password);
	}

	@Override
	public JobExecList getAllJobs() throws SheraClientException {
		try {
			LOG.info("调用shera获取所有job接口");
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
			LOG.info("调用shera创建job接口");
			return api.createJob(namespace, job);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public JobExecView execJob(String name, JobExecView jobExecView) throws SheraClientException {
		try {
			LOG.info("调用shera执行job接口");
			return api.execJob(namespace, name, jobExecView);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Jdk deleteJdk(String version) throws SheraClientException {
		try {
			LOG.info("调用shera删除jdk接口");
			return api.deleteJdk(version);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Jdk createJdk(Jdk jdk) throws SheraClientException {
		try {
			LOG.info("调用shera删除jdk接口");
			return api.createJdk(jdk);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public JobExecView deleteExecution(String name, Integer seqno) throws SheraClientException {
		try {
			LOG.info("调用shera删除一个执行接口");
			return api.deleteExecution(namespace, name, seqno);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public JobExecView killExecution(String name, Integer seqno) throws SheraClientException {
		try {
			LOG.info("调用shera停止一个执行接口");
			return api.killExecution(namespace, name, seqno);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public JobExecView getExecution(String name, Integer seqno) throws SheraClientException {
		try {
			LOG.info("调用shera获取执行结果接口");
			return api.getExecution(namespace, name, seqno);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public JobExecViewList getJobAllExecutions(String name) throws SheraClientException {
		try {
			LOG.info("调用shera获取一个job所有执行接口");
			return api.getJobAllExecutions(namespace, name);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Job deleteJob(String name) throws SheraClientException {
		try {
			LOG.info("调用shera删除job接口");
			return api.deleteJob(namespace, name);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Job updateJob(Job job) throws SheraClientException {
		try {
			LOG.info("调用shera更新job接口");
			return api.updateJob(namespace, job);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Job getJob(String name) throws SheraClientException {
		try {
			LOG.info("调用shera获取job配置信息接口");
			return api.getJob(namespace, name);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public com.bonc.epm.paas.shera.model.Log getExecLog(String name, String seqno, Integer seek)
			throws SheraClientException {
		try {
			LOG.info("调用shera获取执行日志接口");
			return api.getExecLog(namespace, name, seqno, seek);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public ChangeGit getChangeGit(String name) throws SheraClientException {
		try {
			LOG.info("调用shera获取代码挂钩接口，验证代码仓库中代码是否发生变化");
			return api.getChangeGit(namespace, name);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public ChangeGit addGitHooks(String name, ChangeGit changeGit) throws SheraClientException {
		try {
			LOG.info("调用shera添加代码挂钩接口");
			return api.addGitHooks(namespace, name, changeGit);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public ChangeGit updateGitHooks(String name, ChangeGit changeGit) throws SheraClientException {
		try {
			LOG.info("调用shera更新代码挂钩接口");
			return api.updateGitHooks(namespace, name, changeGit);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public ChangeGit deleteGitHooks(String name) throws SheraClientException {
		try {
			LOG.info("调用shera删除代码挂钩接口");
			return api.deleteGitHooks(namespace, name);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Jdk updateJdk(Jdk jdk) throws SheraClientException {
		try {
			LOG.info("调用shera更新Jdk");
			return api.updateJdk(jdk);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public JdkList getAllJdk() throws SheraClientException {
		try {
			LOG.info("调用shera获取所有的jdk信息接口");
			return api.getAllJdk();
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public CredentialCheckEntity checkCredential(CredentialCheckEntity credential) throws SheraClientException {
		try {
			LOG.info("调用shera检查代码认证接口");
			return api.checkCredential(credential);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public CredentialKeyList getAllCredentials() throws SheraClientException {
		try {
			LOG.info("调用shera获取所有代码认证信息接口");
			return api.getAllCredentials();
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public CredentialKey addCredential(GitCredential gitCredential) throws SheraClientException {
		try {
			LOG.info("调用shera添加代码认证接口");
			return api.addCredential(gitCredential);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public CredentialKey deleteCredential(String uuid) throws SheraClientException {
		try {
			LOG.info("调用shera删除代码认证接口");
			return api.deleteCredential(uuid);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public SonarConfig createSonarConfig(SonarConfig sonarConfig) throws SheraClientException {
		try {
			LOG.info("调用shera创建SonarConfig");
			return api.createSonarConfig(namespace, sonarConfig);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public SonarConfig updateSonarConfig(SonarConfig sonarConfig) throws SheraClientException {
		try {
			LOG.info("调用shera更新SonarConfig");
			return api.updateSonarConfig(namespace, sonarConfig);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public SonarConfig getSonarConfig() throws SheraClientException {
		try {
			LOG.info("调用shera获取SonarConfig");
			return api.getSonarConfig(namespace);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Rating getJobRating(String projectKey) throws SheraClientException {
		try {
			LOG.info("调用shera获取Rating");
			return api.getJobRating(namespace, projectKey);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	@Override
	public Rating deleteJobRating(String projectKey) throws SheraClientException {
		try {
			LOG.info("调用shera删除Rating");
			return api.deleteJobRating(namespace, projectKey);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#createExecConfig(com.bonc.epm.paas.shera.model.ExecConfig)
	 */
	@Override
	public ExecConfig createExecConfig(ExecConfig execConfig) throws SheraClientException {
		try {
			LOG.info("调用shera创建ExecConfig");
			return api.createExecConfig(execConfig);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#deleteExecConfig(java.lang.String)
	 */
	@Override
	public void deleteExecConfig(String key) throws SheraClientException {
		try {
			LOG.info("调用shera删除ExecConfig");
			api.deleteExecConfig(key);
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#getExecConfig(java.lang.String)
	 */
	@Override
	public List<ExecConfig> getExecConfig(String kindid) throws SheraClientException {
		try {
			LOG.info("调用shera获取ExecConfig");
			return api.getExecConfig("admin", kindid);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#createSshKey(com.bonc.epm.paas.shera.model.SshKey)
	 */
	@Override
	public SshKey createSshKey(SshKey sshKey) throws SheraClientException {
		try {
			LOG.info("调用shera创建SshKey");
			return api.createSshKey(sshKey);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#deleteSshKey(java.lang.String)
	 */
	@Override
	public void deleteSshKey(String userid) throws SheraClientException {
		try {
			LOG.info("调用shera删除SshKey");
			api.deleteSshKey(userid);
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#getSshKey(java.lang.String)
	 */
	@Override
	public SshKey getSshKey(String userid) throws SheraClientException {
		try {
			LOG.info("调用shera获取SshKey");
			return api.getSshKey(userid);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#createSshConfig(com.bonc.epm.paas.shera.model.SshConfig)
	 */
	@Override
	public SshConfig createSshConfig(SshConfig sshConfig) throws SheraClientException {
		try {
			LOG.info("调用shera创建SshConfig");
			return api.createSshConfig(sshConfig);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#deleteSshConfig(java.lang.String)
	 */
	@Override
	public void deleteSshConfig(String userid) throws SheraClientException {
		try {
			LOG.info("调用shera删除SshConfig");
			api.deleteSshConfig(userid);
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.shera.api.SheraAPIClientInterface#getSshConfig(java.lang.String)
	 */
	@Override
	public SshConfig getSshConfig(String userid) throws SheraClientException {
		try {
			LOG.info("调用shera获取SshConfig");
			return api.getSshConfig(userid);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new SheraClientException(e.getMessage());
		}
	}
}
