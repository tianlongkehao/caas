package com.bonc.epm.paas.docker.module;

import com.bonc.epm.paas.docker.api.DockerRegistryClient;
import com.bonc.epm.paas.docker.api.DockerRegistryClientIF;
import com.bonc.epm.paas.docker.config.DockerRegistryConfig;
import com.bonc.epm.paas.docker.config.DockerRegistryConfigProvider;
import com.google.inject.AbstractModule;

public class DockerRegistryModule extends AbstractModule {

    @Override
    protected void configure () {
        bind(DockerRegistryClientIF.class).to(DockerRegistryClient.class);
        bind(DockerRegistryClient.class).toProvider(DockerRegistryClientProvider.class).asEagerSingleton();
        bind(DockerRegistryConfig.class).toProvider(DockerRegistryConfigProvider.class).asEagerSingleton();
    }

}
