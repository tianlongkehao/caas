/*
 * 文件名：Endpoints.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月31日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年10月31日
 * @see Endpoints
 * @since
 */

public class Endpoints extends AbstractKubernetesModel{

    /**
     * @param kind
     */
    public Endpoints() {
        super(Kind.ENDPOINTS);
    }
    
    private ObjectMeta metadata;
    
    private List<EndpointSubset> subsets;

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public List<EndpointSubset> getSubsets() {
        return subsets;
    }

    public void setSubsets(List<EndpointSubset> subsets) {
        this.subsets = subsets;
    }
}
