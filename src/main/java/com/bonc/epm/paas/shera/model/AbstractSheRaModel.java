package com.bonc.epm.paas.shera.model;

import com.bonc.epm.paas.shera.api.SheraAPIClientInterface;

/**
 * SheRa抽象基类
 * @author ke_wang
 * @version 2016年11月10日
 * @see AbstractSheRaModel
 * @since
 */
public class AbstractSheRaModel {
    private Kind kind;
    private String apiVersion = SheraAPIClientInterface.VERSION;
    
    protected AbstractSheRaModel(Kind kind) {
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }
    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
}
