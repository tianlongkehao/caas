package com.bonc.epm.paas.rest.util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import com.bonc.epm.paas.docker.api.DockerRegistryAPI;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPI;
import com.bonc.epm.paas.net.api.NetAPI;
import com.bonc.epm.paas.shera.api.SheraAPI;

public class RestFactory {
    public KubernetesAPI createKubernetesAPI(String url, String userName, String password){
    	Class<KubernetesAPI> clazz = KubernetesAPI.class;
    	Class<?>[] intfs =
		{
			clazz
		};
    	HashMap<Method, MethodInvoker> methodMap = new HashMap<Method, MethodInvoker>();
    	for (Method method : clazz.getMethods())
		{
	         MethodInvoker invoker = new MethodInvoker(url,userName,password,method);
	         methodMap.put(method, invoker);
		}
    	return (KubernetesAPI)Proxy.newProxyInstance(clazz.getClassLoader(),intfs,new ClientProxy(methodMap));
    }

    public NetAPI createNetAPI(String url, String userName, String password){
    	Class<KubernetesAPI> clazz = KubernetesAPI.class;
    	Class<?>[] intfs =
		{
			clazz
		};
    	HashMap<Method, MethodInvoker> methodMap = new HashMap<Method, MethodInvoker>();
    	for (Method method : clazz.getMethods())
		{
	         MethodInvoker invoker = new MethodInvoker(url,userName,password,method);
	         methodMap.put(method, invoker);
		}
    	return (NetAPI)Proxy.newProxyInstance(clazz.getClassLoader(),intfs,new ClientProxy(methodMap));
    }

    public SheraAPI createSheRaAPI(String sRURI, String userName, String password) {
        Class<SheraAPI> clazz = SheraAPI.class;
        Class<?>[] intfs =
        {
            clazz
        };
        HashMap<Method,MethodInvoker> methodMap = new HashMap<Method,MethodInvoker>();
        for (Method method : clazz.getMethods()) {
            MethodInvoker invoker = new MethodInvoker(sRURI,userName,password,method);
            methodMap.put(method, invoker);
        }
        return (SheraAPI) Proxy.newProxyInstance(clazz.getClassLoader(), intfs, new ClientProxy(methodMap));
    }

    public DockerRegistryAPI createDockerRegistryAPI(String url, String userName, String password){
    	Class<DockerRegistryAPI> clazz = DockerRegistryAPI.class;
    	Class<?>[] intfs =
		{
			clazz
		};
    	HashMap<Method, MethodInvoker> methodMap = new HashMap<Method, MethodInvoker>();
    	for (Method method : clazz.getMethods())
		{
	         MethodInvoker invoker = new MethodInvoker(url,userName,password,method);
	         methodMap.put(method, invoker);
		}
    	return (DockerRegistryAPI)Proxy.newProxyInstance(clazz.getClassLoader(),intfs,new ClientProxy(methodMap));
    }
}
