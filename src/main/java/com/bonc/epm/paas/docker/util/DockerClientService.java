package com.bonc.epm.paas.docker.util;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.docker.api.DockerRegistryAPI;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.util.DateFormatUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;

/**
 * docker-java 工具类
 * @author yangjian
 *
 */
@Service
public class DockerClientService {
	
	@Value("${docker.io.url}")
	private String url;
	@Value("${docker.io.dockerCertPath}")
	private String dockerCertPath;
	@Value("${docker.io.username}")
	private String username;
	@Value("${docker.io.password}")
	private String password;
	@Value("${docker.io.email}")
	private String email;
	@Value("${docker.io.serverAddress}")
	private String serverAddress;
	
	private static final Logger log = LoggerFactory.getLogger(DockerClientService.class);
	
	public DockerRegistryAPI getDockerRegistryAPIClient() {
        return new RestFactory().createDockerRegistryAPI(serverAddress, username, password);
	}
	
	public DockerClient getDockerClientInstance(){
		DockerClientConfig config = DockerClientConfig
				.createDefaultConfigBuilder()
				.withUri(url)
				.withDockerCertPath(dockerCertPath)
				.withUsername(username)
				.withPassword(password)
				.withEmail(email)
				.withServerAddress(serverAddress)
				.build();
		DockerClient dockerClient = DockerClientBuilder.getInstance(config)
		  .build();
		return dockerClient;
	}
	public String generateRegistryImageName(String imageName,String imageVersion){
		return username+"/"+imageName+":"+imageVersion;
	}
	/**
	 * 构建镜像
	 * @param dockerfilePath
	 * @param imageName
	 * @param imageVersion
	 * @return
	 */
	public boolean buildImage(String dockerfilePath,String imageName,String imageVersion,final CiRecord ciRecord,final CiRecordDao ciRecordDao){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			File baseDir = new File(dockerfilePath);
			BuildImageResultCallback callback = new BuildImageResultCallback() {
			    @Override
			    public void onNext(BuildResponseItem item) {
			    	if(item!=null&&item.getStream()!=null){
				    	if(item.getStream().contains("Step")||item.getStream().contains("--->")||item.getStream().contains("Downloading")||item.getStream().contains("[INFO]")||item.getStream().contains("Removing")||item.getStream().contains("Successfully")){
				    		ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+item.getStream());
				    		ciRecordDao.save(ciRecord);
				    	}
			    	}
			       log.info("==========BuildResponseItem:"+item);
			       super.onNext(item);
			    }
			};
			String imageId = dockerClient.buildImageCmd(baseDir).exec(callback).awaitImageId();
			//修改镜像名称及版本
			dockerClient.tagImageCmd(imageId, username+"/"+imageName, imageVersion).withForce().exec();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"tagImageCmd:"+imageId+":"+username+"/"+imageName+":"+imageVersion);
	    	ciRecordDao.save(ciRecord);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
	    	ciRecordDao.save(ciRecord);
			log.error("buildImage error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 上传到镜像仓库
	 * @param imageName
	 * @param imageVersion
	 * @return
	 */
	public boolean pushImage(String imageName,String imageVersion,final CiRecord ciRecord,final CiRecordDao ciRecordDao){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			PushImageResultCallback callback = new PushImageResultCallback() {
				@SuppressWarnings("deprecation")
				@Override
				public void onNext(PushResponseItem item) {
					if(item!=null&&item.getStream()!=null){
						if(!item.getStream().contains("null")){
							ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+item.getProgress());
					    	ciRecordDao.save(ciRecord);
						}
					}
					log.info("==========PushResponseItem:"+item);
				    super.onNext(item);
				}
			};
			dockerClient.pushImageCmd(username+"/"+imageName).withTag(imageVersion).exec(callback).awaitSuccess();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"push image complete");
	    	ciRecordDao.save(ciRecord);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
	    	ciRecordDao.save(ciRecord);
			log.error("pushImage error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 删除镜像
	 * @param imageName
	 * @param imageVersion
	 * @param ciRecordDao 
	 * @param ciRecord 
	 * @return
	 */
	public boolean removeImage(String imageName,String imageVersion, CiRecord ciRecord, CiRecordDao ciRecordDao){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			dockerClient.removeImageCmd(username+"/"+imageName+":"+imageVersion).withForce().exec();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"removeImageCmd:"+username+"/"+imageName+":"+imageVersion);
	    	ciRecordDao.save(ciRecord);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
	    	ciRecordDao.save(ciRecord);
			log.error("removeImage error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 从镜像仓库下载镜像
	 * @param imageName
	 * @param imageVersion
	 * @return
	 */
	public boolean pullImage(String imageName,String imageVersion){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			dockerClient.pullImageCmd(username+"/"+imageName).withTag(imageVersion).exec(new PullImageResultCallback()).awaitSuccess();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("pullImage error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 创建容器（暂时简单实现端口对应）
	 * @param imageName
	 * @param imageVersion
	 * @param containerName
	 * @param exposedPort
	 * @param bindPort
	 * @return
	 */
	public boolean createContainer(String imageName,String imageVersion,String containerName,Integer exposedPort,Integer bindPort){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
	        ExposedPort tcp = ExposedPort.tcp(exposedPort);
	        Ports portBindings = new Ports();
	        portBindings.bind(tcp, Ports.Binding(bindPort));
	        dockerClient.createContainerCmd(username+"/"+imageName+":"+imageVersion).withName(containerName)
	                .withExposedPorts(tcp).withPortBindings(portBindings).exec();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("createContainer error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 开启容器
	 * @param containerName
	 * @return
	 */
	public boolean startContainer(String containerName){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			dockerClient.startContainerCmd(containerName).exec();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("startContainer error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 停止容器
	 * @param containerName
	 * @return
	 */
	public boolean stopContainer(String containerName){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			dockerClient.stopContainerCmd(containerName).exec();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("stopContainer error:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 删除容器
	 * @param containerName
	 * @return
	 */
	public boolean removeContainer(String containerName){
		try{
			DockerClient dockerClient = this.getDockerClientInstance();
			dockerClient.removeContainerCmd(containerName).exec();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("removeContainer error:"+e.getMessage());
			return false;
		}
	}
	
	/*public void main(String[] args) {
		System.out.println(DockerClientUtil.buildImage("C:\\Users\\Administrator\\Desktop\\linyiyj-helloworld-master\\helloworld\\war-test\\","admin/hw1", "latest"));
		System.out.println(DockerClientUtil.pullImage("bonc/helloworld", "latest"));
		System.out.println(DockerClientUtil.createContainer("bonc/helloworld", "latest","container3",8080,10002));
		System.out.println(DockerClientUtil.startContainer("container3"));
	}*/
}
