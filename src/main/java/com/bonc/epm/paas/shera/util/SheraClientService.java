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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.SheraDao;
import com.bonc.epm.paas.entity.CiInvoke;
import com.bonc.epm.paas.entity.Shera;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.shera.api.SheraAPIClient;
import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;
import com.bonc.epm.paas.shera.model.AntConfig;
import com.bonc.epm.paas.shera.model.BuildManager;
import com.bonc.epm.paas.shera.model.ChangeGit;
import com.bonc.epm.paas.shera.model.CodeManager;
import com.bonc.epm.paas.shera.model.CredentialCheckEntity;
import com.bonc.epm.paas.shera.model.CredentialKey;
import com.bonc.epm.paas.shera.model.GitAdvancedConfig;
import com.bonc.epm.paas.shera.model.GitConfig;
import com.bonc.epm.paas.shera.model.GitCredential;
import com.bonc.epm.paas.shera.model.ImgManager;
import com.bonc.epm.paas.shera.model.Jdk;
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
    
    private String endpoint="";
    private String username="";
    private String password="";
    
    @Autowired
    private SheraDao sheraDao;
    
    public SheraAPIClientInterface getClient() {
        User user = CurrentUserUtils.getInstance().getUser();
        Shera shera = new Shera();
        if (user.getUser_autority().equals(UserConstant.AUTORITY_USER)) {
            shera = sheraDao.findByUserId(user.getParent_id());
        }
        else {
            shera = sheraDao.findByUserId(user.getId());
        }
        return getClient(shera);
    }
    
    public SheraAPIClientInterface getClient(Shera shera){
        String namespace = CurrentUserUtils.getInstance().getUser().getUserName();
        this.endpoint = "http://" + shera.getSheraUrl() + ":" + shera.getPort() + "/she-ra";
        this.username = shera.getUserName();
        this.password = shera.getPassword();
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
                           String dockerFileContent,String dockerFile,String imgNamePre,String imgName,
                           List<CiInvoke> ciInvokeList,String userName,Integer type,Integer codeType) {
        Job job = new Job();
        job.setId(id);
        job.setJdkVersion(jdkVersion);
        job.setMaxExecutionRecords(2);
        job.setMaxKeepDays(2);
        CodeManager codeManager = new CodeManager();
        codeManager.setChoice(codeType);
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
            imgManager.setDockerFile("");
        }
        if (StringUtils.isNotEmpty(dockerFile)) {
            imgManager.setDockerFileContent("");
            imgManager.setDockerFile(dockerFile);
        }
        imgManager.setImgNamePre(imgNamePre);
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
    public JobExecView generateJobExecView(long startTime,String imgVersion){
        JobExecView jobExecView = new JobExecView();
        jobExecView.setStartTime(startTime);
        jobExecView.setImgVersion(imgVersion);
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
    
    /**
     * Description: <br>
     * 验证代码地址是否正确
     * @param url ： 代码地址
     * @param username ： 用户名；
     * @param type ： 类型
     * @return 
     */
    public CredentialCheckEntity generateCredentialCheckEntity(String url,String username,Integer type){
        CredentialCheckEntity credentialCheckEntity = new CredentialCheckEntity();
        credentialCheckEntity.setUrl(url);
        CredentialKey credentialKey = new CredentialKey();
        credentialKey.setType(type);
        credentialKey.setUsername(username);
        credentialCheckEntity.setKey(credentialKey);
        return credentialCheckEntity;
    }
    
    /**
     * Description: <br>
     * 封装hookgit数据参数
     * @param namespace ：命名空间
     * @param name ： 项目名称
     * @param giturl ： git地址
     * @param branch ： 分支
     * @return ChangeGit
     */
    public ChangeGit generateChangeGit(String namespace,String name,String giturl,String branch){
        ChangeGit changeGit = new ChangeGit();
        changeGit.setNamespace(namespace);
        changeGit.setName(name);
        changeGit.setGiturl(giturl);
        changeGit.setBranch(branch);
        changeGit.setFlag(false);
        return changeGit;
    }
    
    /**
     * Description: <br>
     * 封装jdk数据
     * @param version 版本
     * @param path 路径
     * @return jdk
     */
    public Jdk generateJdk(String version,String path){
        Jdk jdk = new Jdk();
        jdk.setPath(path);
        jdk.setVersion(version);
        return jdk;
    }
    
//    public static void main(String[] args) {
//        SheraClientService sheraClientService = new SheraClientService();
//        SheraAPIClientInterface client = sheraClientService.getclient("testbonc");
//        ChangeGit chengeGit = client.getChangeGit("test-wxwl1");
//        chengeGit = client.deleteGitHooks("test-wxwl1", chengeGit);
//        System.err.println(chengeGit);
//    }
    
//    public static void main(String[] args) {
//        SheraClientService sheraClientService = new SheraClientService();
//        SheraAPIClientInterface client = sheraClientService.getclient("testbonc");
//        try {
//            JdkList jdkList  = client.getAllJdk();
//            for (Jdk jdk : jdkList) {
//                System.out.println(jdk.toString());
//            }
//            
////            JobExecView jobExecView = client.getExecution("testdemo1",1);
////            System.out.println(jobExecView);
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
