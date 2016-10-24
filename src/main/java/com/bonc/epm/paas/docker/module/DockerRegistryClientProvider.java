package com.bonc.epm.paas.docker.module;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.bonc.epm.paas.docker.api.DockerRegistryClient;
import com.bonc.epm.paas.docker.config.DockerRegistryConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class DockerRegistryClientProvider implements Provider<DockerRegistryClient> {

    private static final Client client;
    static {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 500);
        cc.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 500);
        cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
        client = Client.create(cc);
    }

    private final DockerRegistryClient docker;

    @Inject
    public DockerRegistryClientProvider (DockerRegistryConfig config) {
        docker = new DockerRegistryClient(client, config);
    }

    @Override
    public DockerRegistryClient get () {
        return docker;
    }
}
