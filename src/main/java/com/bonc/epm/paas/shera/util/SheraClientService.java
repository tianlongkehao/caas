/*
 * 文件名：SheraClientService.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.entity.CiInvoke;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.shera.api.SheraAPIClient;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.model.AntConfig;
import com.bonc.epm.paas.shera.model.BuildManager;
import com.bonc.epm.paas.shera.model.CodeManager;
import com.bonc.epm.paas.shera.model.CredentialKey;
import com.bonc.epm.paas.shera.model.GitAdvancedConfig;
import com.bonc.epm.paas.shera.model.GitConfig;
import com.bonc.epm.paas.shera.model.GitCredential;
import com.bonc.epm.paas.shera.model.ImgManager;
import com.bonc.epm.paas.shera.model.Job;
import com.bonc.epm.paas.shera.model.JobExecView;
import com.bonc.epm.paas.shera.model.Key;
import com.bonc.epm.paas.shera.model.MvnConfig;
import com.bonc.epm.paas.shera.model.Repository;
import com.bonc.epm.paas.util.CurrentUserUtils;

/**
 * @author ke_wang
 * @version 2016年11月11日
 * @see SheraClientService
 * @since
 */
@Service
public class SheraClientService {
    
    @Value("${shera.api.endpoint}")
    private String endpoint="http://192.168.0.76:8282/";
    private String username="shera";
    private String password="shera";
    
    public SheraAPIClientInterface getClient() {
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        return getclient(namespace);
    }

    public SheraAPIClientInterface getclient(String namespace) {
        return new SheraAPIClient(endpoint, namespace, username, password,new RestFactory());
    }
    
    /**
     * Description: <br>
     * 创建Job
     * @param id：名称
     * @param jdkVersion jdk版本；
     * @param branch ：代码分之
     * @param url ： 代码地址
     * @param credentials ：
     * @param codeName ： 代码名称
     * @param refspec ： refspec
     * @param dockerFileContent ：dockerFile文件
     * @param dockerFile ： dockerFile地址
     * @param imgName ： 镜像名称
     * @param ciInvokeList ：构建信息
     * @return 
     * @see
     */
    public Job generateJob(String id ,String jdkVersion,String branch,String url,
                           String codeName,String refspec,
                           String dockerFileContent,String dockerFile,String imgName ,
                           List<CiInvoke> ciInvokeList,String userName,Integer type) {
        Job job = new Job();
        job.setId(id);
        job.setJdkVersion(jdkVersion);
        CodeManager codeManager = new CodeManager();
        GitConfig gitConfig = new GitConfig();
        gitConfig.setBranch(branch);
        Repository repo = new Repository();
        repo.setUrl(url);
//        repo.setCredentials(credentials);
        Key key = new Key();
        key.setUsername(userName);
        key.setType(type);
        repo.setKey(key);
        GitAdvancedConfig advanced = new GitAdvancedConfig();
        advanced.setName(codeName);
        advanced.setRefspec(refspec);
        repo.setAdvanced(advanced);
        gitConfig.setRepo(repo);
        codeManager.setGitConfig(gitConfig);
        job.setCodeManager(codeManager);
        
        BuildManager buildManager = new BuildManager();
        List<Integer> seqNo = new ArrayList<Integer>();
        List<String> cmds = new ArrayList<String>();
        List<AntConfig> antConfigs = new ArrayList<AntConfig>();
        List<MvnConfig> mvnConfigs = new ArrayList<MvnConfig>();
        if (ciInvokeList.size() == 0) {
            seqNo.add(0);
        }
        for (CiInvoke ciInvoke : ciInvokeList ) {
            if (ciInvoke.getInvokeType() == 1) {
                seqNo.add(1);
                AntConfig antConfig = new AntConfig();
                antConfig.setVersion(ciInvoke.getAntVersion());
                antConfig.setTargets(ciInvoke.getAntTargets());
                antConfig.setBuildFile(ciInvoke.getAntBuildFileLocation());
                antConfig.setProperties(ciInvoke.getAntProperties());
                antConfig.setJavaopts(ciInvoke.getAntJavaOpts());
                antConfigs.add(antConfig);
            }
            if (ciInvoke.getInvokeType() == 2) {
                seqNo.add(2);
                MvnConfig mvnConfig = new MvnConfig();
                mvnConfig.setVersion(ciInvoke.getMavenVersion());
                mvnConfig.setGoals(ciInvoke.getMavenGoals());
                mvnConfig.setPom(ciInvoke.getPomLocation());
                mvnConfig.setProperties(ciInvoke.getMavenProperty());
                mvnConfig.setJvmopts(ciInvoke.getMavenJVMOptions());
                mvnConfig.setSettingFile(ciInvoke.getMavenSetFile());
                mvnConfig.setGlobalSettingFile(ciInvoke.getMavenGlobalSetFile());
                mvnConfigs.add(mvnConfig);
            }
            if (ciInvoke.getInvokeType() == 3) {
                seqNo.add(3);
                cmds.add(ciInvoke.getShellCommand());
            }
        }
        buildManager.setAntConfigs(antConfigs);
        buildManager.setCmds(cmds);
        buildManager.setMvnConfigs(mvnConfigs);
        buildManager.setSeqNo(seqNo);
        job.setBuildManager(buildManager);
        
        ImgManager imgManager = new ImgManager();
        if (StringUtils.isNotEmpty(dockerFileContent)) {
            imgManager.setDockerFileContent(dockerFileContent);
        }
        if (StringUtils.isNotEmpty(dockerFile)) {
            imgManager.setDockerFile(dockerFile);
        }
        imgManager.setImgName(imgName);
        job.setImgManager(imgManager);
        return job;
    }

    /**
     * Description: <br>
     * 封装jobExec数据
     * @param startTime
     * @return 
     * @see
     */
    public JobExecView generateJobExecView(long startTime){
        JobExecView jobExecView = new JobExecView();
        jobExecView.setStartTime(startTime);
        return jobExecView;
    }
    
    /**
     * Description: <br>
     * 封装GitCredent数据
     * @param secretInfo 
     * @param username 
     * @param type 
     * @return 
     */
    public GitCredential generateGitCredential(String secretInfo,String username,Integer type){
        GitCredential gitCredential = new GitCredential();
        gitCredential.setSecretInfo(secretInfo);
        CredentialKey credentialKey = new CredentialKey();
        credentialKey.setType(type);
        credentialKey.setUsername(username);
        gitCredential.setKey(credentialKey);
        return gitCredential;
    }
    
//    public static void main(String[] args) {
//        SheraClientService sheraClientService = new SheraClientService();
//        SheraAPIClientInterface client = sheraClientService.getclient("testbonc");
//        try {
//            JobExecViewList  jobExecViewList = client.getJobAllExecutions("testdemo1");
//            System.out.println(jobExecViewList);
//            
//            JobExecView jobExecView = client.getExecution("testdemo1",1);
//            System.out.println(jobExecView);
//           
////            JobExecView jobExecView2 = client.killExecution("testdemo1",2);
////            System.out.println(jobExecView2);
//            
//        }
//        catch (SheraClientException e) {
//           e.printStackTrace();
//        }
//    }
}
