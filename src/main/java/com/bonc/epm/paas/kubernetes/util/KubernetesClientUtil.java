package com.bonc.epm.paas.kubernetes.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.api.KubernetesApiClient;
import com.bonc.epm.paas.kubernetes.api.RestFactory;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.ServiceList;

public class KubernetesClientUtil {
	
	private static final Log log = LogFactory.getLog(KubernetesClientUtil.class);
	
	private static String endpoint;
    private static String username;
    private static String password;
    static{
    	Properties k8sProperties = new Properties();
    	InputStream in = KubernetesClientUtil.class.getClassLoader().getResourceAsStream("kubernetes.api.properties");
    	try {
			k8sProperties.load(in);
			in.close();
			endpoint = k8sProperties.getProperty("kubernetes.api.endpoint");
			username = k8sProperties.getProperty("kubernetes.api.username");
			password = k8sProperties.getProperty("kubernetes.api.password");
		} catch (IOException e) {
			log.error("KubernetesUtil.init:"+e.getMessage());
			e.printStackTrace();
		}
    }
    
	private static KubernetesAPIClientInterface client;
	
	public static KubernetesAPIClientInterface getClient() {
        return getClient("default");
    }

    public static KubernetesAPIClientInterface getClient(String namespace) {
        if (client == null) {
            client = new KubernetesApiClient(namespace,endpoint, username, password,new RestFactory());
        }
        return client;
    }
	
		/*public static void main(String[] args) {
			KubernetesAPIClientInterface client = KubernetesClientUtil.getClient();
			ReplicationControllerList list = client.getAllReplicationControllers();
			System.out.println("ReplicationControllerList:"+JSON.toJSONString(list));
			
			PodList podList = client.getAllPods();
			System.out.println("podList:"+JSON.toJSONString(podList));
			
			ServiceList serviceList = client.getAllServices();
			System.out.println("serviceList:"+JSON.toJSONString(serviceList));
			
		}*/
}
