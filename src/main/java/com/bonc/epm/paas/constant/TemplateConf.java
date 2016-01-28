package com.bonc.epm.paas.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nginxConf.io")
public class TemplateConf {
	    private String confpath;
	    private String cmdpath;
	    private String confurl;
	    private String serverIP;
	    public String getServerIP() {
			return serverIP;
		}
		public void setServerIP(String serverIP) {
			this.serverIP = serverIP;
		}		
		public String getConfpath() {
			return confpath;
		}
		public void setConfpath(String confpath) {
			this.confpath = confpath;
		}
		public String getCmdpath() {
			return cmdpath;
		}
		public void setCmdpath(String cmdpath) {
			this.cmdpath = cmdpath;
		}
		public String getConfurl() {
			return confurl;
		}
		public void setConfurl(String confurl) {
			this.confurl = confurl;
		}
	    
}
