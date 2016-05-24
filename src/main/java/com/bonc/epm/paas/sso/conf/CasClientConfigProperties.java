package com.bonc.epm.paas.sso.conf;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "sso", ignoreUnknownFields = false)
public class CasClientConfigProperties {
	/**
     * CAS SSO Enable.
     */
	private boolean enable = false;
	/**
     * CAS server logout URL E.g. https://example.com/cas/logout or https://cas.example/logout. Required.
     */
	private String serverLogoutUrl;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getServerLogoutUrl() {
		return serverLogoutUrl;
	}

	public void setServerLogoutUrl(String serverLogoutUrl) {
		this.serverLogoutUrl = serverLogoutUrl;
	}
}
