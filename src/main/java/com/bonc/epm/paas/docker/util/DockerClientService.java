package com.bonc.epm.paas.docker.util;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.dao.CiRecordDao;
import com.bonc.epm.paas.docker.api.DockerRegistryAPI;
import com.bonc.epm.paas.entity.CiRecord;
import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.bonc.epm.paas.util.DateFormatUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.PullResponseItem;
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
	@Value("${docker.io.nodeUrl}")
	private String nodeUrl;
	private HashMap<String,Integer> nodeMap = null;
	
	private static final Logger log = LoggerFactory.getLogger(DockerClientService.class);
	
	/**
	 * 
	 * Description: <br>
    * dockerRegistryAPIClient
	 * @return 
	 * @see
	 */
	public DockerRegistryAPI getDockerRegistryAPIClient() {
        return new RestFactory().createDockerRegistryAPI(serverAddress, username, password);
	}
	
	/**
	 * 
	 * Description: <br>
     *  获取指定node节点的dockerClient实例
	 * @return 
	 * @see
	 */
	public DockerClient getSpecialDockerClientInstance(){
		DockerClientConfig config = DockerClientConfig
				.createDefaultConfigBuilder()
				.withUri(url)
				.withDockerCertPath(dockerCertPath)
				.withUsername(username)
				.withPassword(password)
				.withEmail(email)
				.withServerAddress(serverAddress)
				.build();
		DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
		return dockerClient;
	}
	
	/**
	 * 
	 * Description: <br>
	 * 获取最合适node节点的dockerClient实例
	 * @return 
	 * @see
	 */
   public DockerClient getNormalDockerClientInstance(){
       if (null == nodeMap) {
           nodeMap = new HashMap<String,Integer>();
           if (StringUtils.isNoneBlank(nodeUrl)) {
               String[] nodeArray = nodeUrl.split(",");
               int init = 0;
               for (int i=0;i<nodeArray.length;i++) {
                   nodeMap.put(nodeArray[i], init);
                  }
             }
         }
       Iterator<Entry<String, Integer>> iter = nodeMap.entrySet().iterator();
       Map.Entry<String, Integer> first = (Map.Entry<String, Integer>) iter.next();
       String url = first.getKey();
       int weight = first.getValue();
       while (iter.hasNext()) {
           Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter.next();
           if (entry.getValue() < weight) {
               url = entry.getKey();
               weight = entry.getValue();
              }
           if (entry.getValue() >= 65535) {
               nodeMap.put(entry.getKey(), 0);
             }
         }
       nodeMap.put(url, nodeMap.get(url) + 1);
       System.out.println("当前使用的docker客户端地址是：-" + url);
       
        DockerClientConfig config = DockerClientConfig
                .createDefaultConfigBuilder()
                .withUri(url)
                .withDockerCertPath(dockerCertPath)
                .withUsername(username)
                .withPassword(password)
                .withEmail(email)
                .withServerAddress(serverAddress)
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

        return dockerClient;
    }
   
	public String getDockerRegistryAddress(){
		return username;
	}
	public String generateRegistryImageName(String imageName,String imageVersion){
		return username+"/"+imageName+":"+imageVersion;
	}
	
	/**
	 * 
	 * Description:
	 * 查看镜像信息
	 * @param imageId String
	 * @return InspectImageResponse 
	 * @see InspectImageResponse 
	 */
	public InspectImageResponse inspectImage(String imageId) {
	    try {
            DockerClient dockerClient = this.getSpecialDockerClientInstance();
            return dockerClient.inspectImageCmd(imageId).exec();
        }
        catch (Exception e) {
           log.error("error inspect image,message:-"+e.getMessage());
           e.printStackTrace();
           return null;
        }
	}
	
	/**
	 * 
	 * Description:
	 * 导入镜像并push到远程
	 * @param image
	 * @param inputStream
	 * @return 
	 * @see
	 */
	@SuppressWarnings("deprecation")
	public boolean createAndPushImage(Image image, InputStream inputStream) {
	    try {
            DockerClient dockerClient = this.getSpecialDockerClientInstance();
            
            String imageId = dockerClient.createImageCmd(image.getName(), inputStream).withTag(image.getVersion()).exec().getId();
            imageId = imageId.substring(0,12); // ?? why is not the response same with building image.
            dockerClient.tagImageCmd(imageId, username +"/"+image.getName(), image.getVersion()).withForce().exec();
            
            // push image
            PushImageResultCallback callback = new PushImageResultCallback() {
                @Override
                public void onNext(PushResponseItem item) {
                    log.info("==========PushResponseItem:"+item);
                    super.onNext(item);
                   }
               };
            dockerClient.pushImageCmd(username +"/"+ image.getName()).withTag(image.getVersion()).exec(callback).awaitSuccess();
            image.setImageId(imageId);
            return true;
        }
        catch (Exception e) {
            log.error("createImage error:"+e.getMessage());
            return false;
        }
	}

	/**
	 * 构建镜像
	 * @param dockerfilePath
	 * @param imageName
	 * @param imageVersion
	 * @param imageId 
	 * @param dockerClient 
	 * @return
	 */
	public boolean buildImage(String dockerfilePath,String imageName,
	                              String imageVersion,final CiRecord ciRecord,
	                                  final CiRecordDao ciRecordDao, Image imageId, DockerClient dockerClient){
		try{
		    if (null == dockerClient) {
		        dockerClient = this.getSpecialDockerClientInstance();
		     }
			
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
			String imgId = dockerClient.buildImageCmd(baseDir).exec(callback).awaitImageId();
			//修改镜像名称及版本
			dockerClient.tagImageCmd(imgId, username+"/"+imageName, imageVersion).withForce().exec();
			// 得到镜像的ImageId
			imageId.setImageId(imgId.substring(0,12));
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
	 * @param dockerClient
	 * @return
	 */
	public boolean pushImage(String imageName,String imageVersion,final CiRecord ciRecord,final CiRecordDao ciRecordDao, DockerClient dockerClient){
		try{
		    if (null == dockerClient) {
		       dockerClient = this.getSpecialDockerClientInstance();
		    }
			
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
	 * @param dockerClient 
	 * @return
	 */
	public boolean removeImage(String imageName,String imageVersion, CiRecord ciRecord, CiRecordDao ciRecordDao, DockerClient dockerClient){
		try{
		    if (null == dockerClient) {
		         dockerClient = this.getSpecialDockerClientInstance(); 
		    }
			dockerClient.removeImageCmd(username+"/"+imageName+":"+imageVersion).withForce().exec();
			if (null != ciRecord && null != ciRecordDao) {
	          ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"removeImageCmd:"+username+"/"+imageName+":"+imageVersion);
	          ciRecordDao.save(ciRecord); 
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			if (null != ciRecord && null != ciRecordDao) {
	            ciRecord.setLogPrint(ciRecord.getLogPrint()+"<br>"+"["+DateFormatUtils.formatDateToString(new Date(), DateFormatUtils.YYYY_MM_DD_HH_MM_SS)+"] "+"error:"+e.getMessage());
	            ciRecordDao.save(ciRecord);			    
			}
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
			DockerClient dockerClient = this.getSpecialDockerClientInstance();
            // pull image
			PullImageResultCallback callback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    log.info("==========PullResponseItem:"+item);
                    super.onNext(item);
                   }
            };
			dockerClient.pullImageCmd(username+"/"+imageName).withTag(imageVersion).exec(callback);
			try{
			    Thread.sleep(10*1000);//暂停10秒后程序继续执行
			}catch (InterruptedException e) {
			    e.printStackTrace();
			} 
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
			DockerClient dockerClient = this.getSpecialDockerClientInstance();
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
			DockerClient dockerClient = this.getSpecialDockerClientInstance();
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
			DockerClient dockerClient = this.getSpecialDockerClientInstance();
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
			DockerClient dockerClient = this.getSpecialDockerClientInstance();
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
