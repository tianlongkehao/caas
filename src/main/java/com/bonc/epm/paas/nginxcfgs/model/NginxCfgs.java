/*
 * 文件名：NginxCfgs.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月28日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.nginxcfgs.model;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年11月28日
 * @see NginxCfgs
 * @since
 */
public class NginxCfgs {
    /**
     * nginx配置server_name选项（域名 IP 方式均可）
     */
    private String serverName;
    
    /**
     * nginx配置listen选项
     */
    private String listenPort;
    
    /**
     * 真实提供服务的路径
     */
    private String realServerPath;
    
    /**
     * namespace 租户
     */
    private String namespace;
    
    /**
     * app 名字 用户制作路径 文件名等
     */
    private String appName;
    
    /**
     * nginx配置location选项（相当原来的proxy_path）
     */
    private String location;
    
    /**
     * nginx配置proxy_redirect选项 源路径
     */
    private String proxyRedirectSrcPath;
    
    /**
     * nginx配置proxy_redirect选项 目的路径
     */
    private String proxyRedirectDestPath;
    
    /**
     * nginx配置upstream IP （不支持界面配置该选项 单支持获取该选项）
     */
    private List<String> upstreamIPs;
    
    /**
     * nginx配置upstream port （不支持界面配置该选项 单支持获取该选项）
     */
    private String upstreamPort;
    
    /**
     * 是否需要ip_hash
     */
    private boolean isUpstreamIPHash;
    
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getListenPort() {
        return listenPort;
    }
    public void setListenPort(String listenPort) {
        this.listenPort = listenPort;
    }
    public String getRealServerPath() {
        return realServerPath;
    }
    public void setRealServerPath(String realServerPath) {
        this.realServerPath = realServerPath;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getProxyRedirectSrcPath() {
        return proxyRedirectSrcPath;
    }
    public void setProxyRedirectSrcPath(String proxyRedirectSrcPath) {
        this.proxyRedirectSrcPath = proxyRedirectSrcPath;
    }
    public String getProxyRedirectDestPath() {
        return proxyRedirectDestPath;
    }
    public void setProxyRedirectDestPath(String proxyRedirectDestPath) {
        this.proxyRedirectDestPath = proxyRedirectDestPath;
    }
    public List<String> getUpstreamIPs() {
        return upstreamIPs;
    }
    public void setUpstreamIPs(List<String> upstreamIPs) {
        this.upstreamIPs = upstreamIPs;
    }
    public String getUpstreamPort() {
        return upstreamPort;
    }
    public void setUpstreamPort(String upstreamPort) {
        this.upstreamPort = upstreamPort;
    }
    public boolean isUpstreamIPHash() {
        return isUpstreamIPHash;
    }
    public void setUpstreamIPHash(boolean isUpstreamIPHash) {
        this.isUpstreamIPHash = isUpstreamIPHash;
    }
}
