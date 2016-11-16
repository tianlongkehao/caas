/*
 * 文件名：DockerRegistryAPIClient.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.api;

import com.bonc.epm.paas.docker.model.Manifest;
import com.bonc.epm.paas.docker.model.Tags;

/**
 * @author ke_wang
 * @version 2016年11月16日
 * @see DockerRegistryAPIClient
 * @since
 */

public class DockerRegistryAPIClient implements DockerRegistryAPIClientInterface {

    private DockerRegistryAPI api;
    @Override
    public Tags getTagsofImage(String name) {
        return api.getTagsofImage(name);
    }

    @Override
    public Manifest getManifestofImage(String name, String reference) {
        return api.getManifestofImage(name, reference);
    }

}
