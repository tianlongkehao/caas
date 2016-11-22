/*
 * 文件名：EndpointsList.java
 * 版权：Copyright by www.bonc.com.cn
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
 * @see EndpointsList
 * @since
 */

public class EndpointsList extends AbstractKubernetesModel{

    protected EndpointsList() {
        super(Kind.ENDPOINTSLIST);
    }
    private ListMeta metadata;
    private List<Endpoints> items;
    public ListMeta getMetadata() {
        return metadata;
    }
    public void setMetadata(ListMeta metadata) {
        this.metadata = metadata;
    }
    public List<Endpoints> getItems() {
        return items;
    }
    public void setItems(List<Endpoints> items) {
        this.items = items;
    }
}
