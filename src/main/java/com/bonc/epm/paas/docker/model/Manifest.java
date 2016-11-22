/*
 * 文件名：Manifest.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月16日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.model;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年11月16日
 * @see Manifest
 * @since
 */

public class Manifest {
    private String name;
    private String tag;
    private String architecture;
    private List<FsLayer> fsLayers;
    private List<History> history;
    private Integer schemaVersion;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getArchitecture() {
        return architecture;
    }
    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }
    public List<FsLayer> getFsLayers() {
        return fsLayers;
    }
    public void setFsLayers(List<FsLayer> fsLayers) {
        this.fsLayers = fsLayers;
    }
    public List<History> getHistory() {
        return history;
    }
    public void setHistory(List<History> history) {
        this.history = history;
    }
    public Integer getSchemaVersion() {
        return schemaVersion;
    }
    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
