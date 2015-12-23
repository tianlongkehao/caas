package com.bonc.epm.paas.util;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.entity.CiRecord;
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
public class DockerClientUtil {
	
	private static final Logger log = LoggerFactory.getLogger(DockerClientUtil.class);
	
	private static final DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
			  .build();
	public static DockerClient getDockerClientInstance(){
		DockerClient dockerClient = DockerClientBuilder.getInstance(config)
		  .build();
		return dockerClient;
	}
	/**
	 * 构建镜像
	 * @param dockerfilePath
	 * @param imageName
	 * @param imageVersion
	 * @return
	 */
	public static boolean buildImage(String dockerfilePath,String imageName,String imageVersion,final CiRecord ciRecord,final CiRecordDao ciRecordDao){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
			File baseDir = new File(dockerfilePath);
			BuildImageResultCallback callback = new BuildImageResultCallback() {
			    @Override
			    public void onNext(BuildResponseItem item) {
			    	if(item.getStream().contains("Step")||item.getStream().contains("--->")||item.getStream().contains("Downloading")||item.getStream().contains("[INFO]")||item.getStream().contains("Removing")||item.getStream().contains("Successfully")){
			    		ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+item.getStream());
			    	}
			    	ciRecordDao.save(ciRecord);
			       log.info("==========BuildResponseItem:"+item);
			       super.onNext(item);
			    }
			};
			String imageId = dockerClient.buildImageCmd(baseDir).exec(callback).awaitImageId();
			//修改镜像名称及版本
			dockerClient.tagImageCmd(imageId, config.getUsername()+"/"+imageName, imageVersion).withForce().exec();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"tagImageCmd:"+imageId+":"+config.getUsername()+"/"+imageName+":"+imageVersion);
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
	public static boolean pushImage(String imageName,String imageVersion,final CiRecord ciRecord,final CiRecordDao ciRecordDao){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
			PushImageResultCallback callback = new PushImageResultCallback() {
				@Override
				public void onNext(PushResponseItem item) {
					ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+item.getProgress());
			    	ciRecordDao.save(ciRecord);
					log.info("==========PushResponseItem:"+item);
				    super.onNext(item);
				}
			};
			dockerClient.pushImageCmd(config.getUsername()+"/"+imageName).withTag(imageVersion).exec(callback).awaitSuccess();
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
	public static boolean removeImage(String imageName,String imageVersion, CiRecord ciRecord, CiRecordDao ciRecordDao){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
			dockerClient.removeImageCmd(config.getUsername()+"/"+imageName+":"+imageVersion).withForce().exec();
			ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"removeImageCmd:"+config.getUsername()+"/"+imageName+":"+imageVersion);
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
	public static boolean pullImage(String imageName,String imageVersion){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
			dockerClient.pullImageCmd(config.getUsername()+"/"+imageName).withTag(imageVersion).exec(new PullImageResultCallback()).awaitSuccess();
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
	public static boolean createContainer(String imageName,String imageVersion,String containerName,Integer exposedPort,Integer bindPort){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
	        ExposedPort tcp = ExposedPort.tcp(exposedPort);
	        Ports portBindings = new Ports();
	        portBindings.bind(tcp, Ports.Binding(bindPort));
	        dockerClient.createContainerCmd(config.getUsername()+"/"+imageName+":"+imageVersion).withName(containerName)
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
	public static boolean startContainer(String containerName){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
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
	public static boolean stopContainer(String containerName){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
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
	public static boolean removeContainer(String containerName){
		try{
			DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
			dockerClient.removeContainerCmd(containerName).exec();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("removeContainer error:"+e.getMessage());
			return false;
		}
	}
	
/*	public static void main(String[] args) {
		System.out.println(DockerClientUtil.pullImage("bonc/helloworld", "latest"));
		System.out.println(DockerClientUtil.createContainer("bonc/helloworld", "latest","container3",8080,10002));
		System.out.println(DockerClientUtil.startContainer("container3"));
	}*/
}
