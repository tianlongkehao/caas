package com.bonc.epm.paas.kubernetes.api;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class MethodInvoker {
	private String url;
	private String userName;
	private String password;
	
	private Method method;
	
	public MethodInvoker(String url, String userName, String password,Method method) {
		super();
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.method = method;
	}

	public Object invoke(Object[] args){
		Client client = ClientBuilder.newClient(new ClientConfig()).register(JacksonJaxbJsonProvider.class).register(JacksonConfig.class);
    	WebTarget webTarget = client.target(url);
    	HttpAuthenticationFeature.basicBuilder()
    		    .nonPreemptive().credentials(userName, password).build();
    	Path path = method.getAnnotation(Path.class);
    	String pathValue = path.value();
    	
    	Entity<?> entity = null;
    	Map<String,String> queryParamMap = new HashMap<String,String>();
    	Parameter[] parameters = method.getParameters();
    	if(parameters!=null&&parameters.length>0){
    		for(int i=0;i<parameters.length;i++){
    			Parameter parameter = parameters[i];
    			PathParam pathParam = parameter.getAnnotation(PathParam.class);
    			if(pathParam!=null){
    				pathValue = pathValue.replace("{"+pathParam.value()+"}", String.valueOf(args[i]));
    			}else{
    				entity = Entity.entity(args[i], MediaType.APPLICATION_JSON_TYPE);
    			}
    			QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
    			if(queryParam!=null){
    				queryParamMap.put(queryParam.value(), String.valueOf(args[i]));
    			}
    		}
    	}
    	
    	WebTarget pathWebTarget = webTarget.path("/api/" + KubernetesAPIClientInterface.VERSION+pathValue);
    	if(queryParamMap.size()>0){
    		for(Entry<String,String> queryParam:queryParamMap.entrySet()){
    			pathWebTarget = pathWebTarget.queryParam(queryParam.getKey(), queryParam.getValue());
    		}
    	}
    	Invocation.Builder invocationBuilder =
    			pathWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
    	
    	GET get = method.getAnnotation(GET.class);
    	POST post = method.getAnnotation(POST.class);
    	DELETE delete = method.getAnnotation(DELETE.class);
    	PUT put = method.getAnnotation(PUT.class);
    	Response response = null;
    	if(get!=null){
    		response = invocationBuilder.get();
    	}else if(post!=null){
    		response = invocationBuilder.post(entity);  
    	}else if(delete!=null){
    		response = invocationBuilder.delete();
    	}else if(put!=null){
    		response = invocationBuilder.put(entity);
    	}
    	response.bufferEntity();
    	try{
    		return response.readEntity(method.getReturnType());
    	}catch(Exception e){
    		throw new KubernetesClientException("unexpect k8s response",response.readEntity(Status.class));
    	}
	}
}
