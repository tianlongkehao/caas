/*
 * 文件名：DockerRegistryAPIClient.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.api;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import com.bonc.epm.paas.docker.exception.DokcerRegistryClientException;
import com.bonc.epm.paas.docker.model.Images;
import com.bonc.epm.paas.docker.model.Manifest;
import com.bonc.epm.paas.docker.model.Tags;
import com.bonc.epm.paas.rest.util.RestFactory;

/**
 * @author ke_wang
 * @version 2016年11月16日
 * @see DockerRegistryAPIClient
 * @since
 */

public class DockerRegistryAPIClient implements DockerRegistryAPIClientInterface {

    private DockerRegistryAPI api;
    private String endpointURI;
    
    public DockerRegistryAPIClient(String url, String username, String password, RestFactory factory) {
        this.endpointURI = url+"/" + DockerRegistryAPIClientInterface.VERSION;
        api = factory.createDockerRegistryAPI(endpointURI, username, password);
    }
    
    @Override
    public Images getImages() throws DokcerRegistryClientException{
        try {
            return api.getImages();
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new DokcerRegistryClientException(e);
        }
    }

    @Override
    public Tags getTagsofImage(String name) throws DokcerRegistryClientException{
        try {
            return api.getTagsofImage(name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new DokcerRegistryClientException(e);
        }
    }

    @Override
    public Object getManifestofImage(String name, String reference) throws DokcerRegistryClientException{
        try {
            return api.getManifestofImage(name, reference);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new DokcerRegistryClientException(e);
        }
    }

    @Override
    public Manifest deleteManifestofImage(String name, String reference) throws DokcerRegistryClientException{
        try {
            return api.deleteManifestofImage(name, reference);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new DokcerRegistryClientException(e);
        }
    }

}
