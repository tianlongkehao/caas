/*
 * 文件名：NginxCfgAPIClient.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月28日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.nginxcfgs.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import com.bonc.epm.paas.nginxcfgs.model.NginxCfgs;
import com.bonc.epm.paas.rest.util.RestFactory;

/**
 * @author ke_wang
 * @version 2016年11月28日
 * @see NginxCfgAPIClient
 * @since
 */

public class NginxCfgAPIClient implements NginxCfgAPIClientInterface {

    private String nginxUrl;
    private NginxCfgAPI api;
    
    public NginxCfgAPIClient(String url, String username, String password, RestFactory factory) {
        this.nginxUrl = url;
        api = factory.createNginxCfgAPI(nginxUrl,username,password);
    }
    @Override
    public NginxCfgs getNginxCfgs(String servername) {
        try {
            return api.getNginxCfgs(servername);
        } catch (NotFoundException e) {
            return new NginxCfgs();
        } catch (WebApplicationException e) {
            return null;
        }
    }

    @Override
    public List<NginxCfgs> getAllNginxCfgs() {
        try {
            return api.getAllNginxCfgs();
        } catch (NotFoundException e) {
            return new ArrayList<NginxCfgs>();
        } catch (WebApplicationException e) {
            return null;
        }
    }

    @Override
    public boolean addNginxCfgs(NginxCfgs nginxCfgs) {
        try {
            return api.addNginxCfgs(nginxCfgs);
        } catch (NotFoundException e) {
            return false;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    @Override
    public boolean updateNginxCfgs(String servername, NginxCfgs nginxCfgs) {
        try {
            return api.updateNginxCfgs(servername, nginxCfgs);
        } catch (NotFoundException e) {
            return false;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    @Override
    public boolean addExternCfgs(NginxCfgs nginxCfgs) {
        try {
            return api.addExternCfgs(nginxCfgs);
        } catch (NotFoundException e) {
            return false;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    @Override
    public boolean updateExternCfgs(String servername, NginxCfgs nginxCfgs) {
        try {
            return api.updateExternCfgs(servername, nginxCfgs);
        } catch (NotFoundException e) {
            return false;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    @Override
    public boolean addTesttool(String name) {
        try {
            return api.addTesttool(name);
        } catch (NotFoundException e) {
            return false;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    @Override
    public List<String> getTesttoolInfo() {
        try {
            return api.getTesttoolInfo();
        } catch (NotFoundException e) {
            return new ArrayList<String>();
        } catch (WebApplicationException e) {
            return new ArrayList<String>();
        }
    }
}
