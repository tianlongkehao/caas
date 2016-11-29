package com.bonc.epm.paas.rest.util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import com.bonc.epm.paas.docker.api.DockerRegistryAPI;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPI;
import com.bonc.epm.paas.nginxcfgs.api.NginxCfgAPI;
import com.bonc.epm.paas.shera.api.SheraAPI;

public class RestFactory {
	/* 
    private ClassLoader classLoader;
    private int connectionPoolSize;

    public RestFactory() {
    }

    public RestFactory(ClassLoader classLoader) {
        classLoader(classLoader);
    }

    public RestFactory classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public RestFactory connectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
        return this;
    }

   public KubernetesAPI createAPI(URI uri, String userName, String password) {

        // Configure HttpClient to authenticate preemptively
        // by prepopulating the authentication data cache.
        // http://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/html/RESTEasy_Client_Framework.html#transport_layer
        // http://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/authentication.html#d5e1032

        HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(userName, password));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

        // 4. Create client executor and proxy
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, localcontext);
        ResteasyClient client = new ResteasyClientBuilder().connectionPoolSize(connectionPoolSize).httpEngine(engine)
                .build();

        client.register(JacksonJaxbJsonProvider.class).register(JacksonConfig.class);
        ProxyBuilder<KubernetesAPI> proxyBuilder = client.target(uri).proxyBuilder(KubernetesAPI.class);
        if (classLoader != null) {
            proxyBuilder = proxyBuilder.classloader(classLoader);
        }
        return proxyBuilder.build();
    }

    public KubernetesAPI createAPI(String url, String userName, String password) throws URISyntaxException {
        URI uri = new URI(url);
        return createAPI(uri, userName, password);
    }*/
    
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
    
    public NginxCfgAPI createNginxCfgAPI(String url, String userName, String password){
        Class<NginxCfgAPI> clazz = NginxCfgAPI.class;
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
        return (NginxCfgAPI)Proxy.newProxyInstance(clazz.getClassLoader(),intfs,new ClientProxy(methodMap));
    }
    
}
