package com.bonc.epm.paas.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="nginx.server")
public class NginxServerConf {

	private String DMZ;
	private String USER;
	public String getDMZ() {
		return DMZ;
	}
	public void setDMZ(String dMZ) {
		DMZ = dMZ;
	}
	public String getUSER() {
		return USER;
	}
	public void setUSER(String uSER) {
		USER = uSER;
	}
	
}
