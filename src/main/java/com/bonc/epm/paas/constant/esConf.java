package com.bonc.epm.paas.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "es.io")
public class esConf {
	private String host;
	private String clusterName;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
	
	
	
}
