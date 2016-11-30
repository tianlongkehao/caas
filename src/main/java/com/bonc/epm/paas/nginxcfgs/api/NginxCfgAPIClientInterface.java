/*
 * 文件名：NginxCfgAPIClientInterface.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月28日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.nginxcfgs.api;

import java.util.List;

import com.bonc.epm.paas.nginxcfgs.model.NginxCfgs;

/**
 * @author ke_wang
 * @version 2016年11月28日
 * @see NginxCfgAPIClientInterface
 * @since
 */
public interface NginxCfgAPIClientInterface {
    
    public NginxCfgs getNginxCfgs(String nginxcfg, String servername);
    
    public List<NginxCfgs> getAllNginxCfgs();
    
    public boolean addNginxCfgs(String nginxcfg, NginxCfgs nginxCfgs);
    
    public boolean updateNginxCfgs(String nginxcfg, String servername, NginxCfgs nginxCfgs);
    
    public boolean deleteNginxCfgs(String nginxcfg, String servername);
    
    public boolean addTesttool(String name);
    
    public List<String> getTesttoolInfo();
}
