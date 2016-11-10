package com.bonc.epm.paas.shera.model;

/**
 * SheRa抽象基类
 * @author ke_wang
 * @version 2016年11月10日
 * @see AbstractSheRaModel
 * @since
 */

public class AbstractSheRaModel {
    private String apiVersion = "v1";

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
}
