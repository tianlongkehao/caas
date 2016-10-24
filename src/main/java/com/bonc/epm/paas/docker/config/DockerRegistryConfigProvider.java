package com.bonc.epm.paas.docker.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.inject.Provider;

@Component
public class DockerRegistryConfigProvider implements Provider<DockerRegistryConfig> {

    @Value("${docker.io.serverAddress}")
    private String url;
    @Value("${docker.io.username}")
    private String username;
    @Value("${docker.io.password}")
    private String password;
    //private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DockerRegistryConfig get () {
        Map<String, String> configMap = new HashMap<String,String>();
        configMap.put("default.base_url", url);
        configMap.put("default.user_name", username);
        configMap.put("default.password", password);
        DockerRegistryConfig config = new DockerRegistryConfig(configMap);
        
/*        try (InputStream stream = this.getClass().getResourceAsStream("/docker_registry_config.json")) {
            @SuppressWarnings("unchecked")
            Map<String, String> configMap = mapper.readValue(stream, Map.class);
            config = new DockerRegistryConfig(configMap);
        } catch (Exception e) {
            throw new RuntimeException("could not load config for docker_registry");
        }*/
        return config;
    }
}
