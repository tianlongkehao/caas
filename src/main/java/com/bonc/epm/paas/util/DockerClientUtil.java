package com.bonc.epm.paas.util;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.DockerCmdExecFactoryImpl;

public class DockerClientUtil {
	
	public static DockerClient getDockerClientInstance(){
		DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
		  .build();

		DockerCmdExecFactoryImpl dockerCmdExecFactory = new DockerCmdExecFactoryImpl()
		  .withReadTimeout(10000)
		  .withConnectTimeout(10000)
		  .withMaxTotalConnections(100)
		  .withMaxPerRouteConnections(10);

		DockerClient dockerClient = DockerClientBuilder.getInstance(config)
		  .withDockerCmdExecFactory(dockerCmdExecFactory)
		  .build();
		return dockerClient;
	}
/*	public static void main(String[] args) {
		DockerClient dockerClient = DockerClientUtil.getDockerClientInstance();
		
		 List<Image> images = dockerClient.listImagesCmd().withShowAll(true)
	                .exec();
		System.out.println("images returned" + images.toString());
		
		List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
		
		System.out.println("containers returned" + containers.toString());
		
		
		File baseDir = new File("C:\\Users\\Administrator\\Desktop\\helloworld\\");

		BuildImageResultCallback callback = new BuildImageResultCallback() {
		    @Override
		    public void onNext(BuildResponseItem item) {
		       System.out.println("" + item);
		       System.out.println("" + item.getStream());
		       super.onNext(item);
		    }
		};
		String imageId = dockerClient.buildImageCmd(baseDir).exec(callback).awaitImageId();
		dockerClient.tagImageCmd(imageId, "test/hw4", "latest");
		
	}*/
}
