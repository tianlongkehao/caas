package com.bonc.epm.paas.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * 集群
 * @author update
 * @version 2016年9月5日
 * @see Cluster
 * @since
 */
@Entity
public class Cluster {
    
    /**
     * 主键Id
     */
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
    
    /**
     * host
     */
    private String host;
	
	/**
	 * port端口
	 */
    private Integer port;
	
	/**
	 * username
	 */
    private String username;
    
    /**
     * password
     */
    private String password;
    
    /**
     * cpu
     */
    private Integer cpu;
    
    /**
     * memory
     */
    private Integer memory;
    
    /**
     * status
     */
    private Integer status;
    
    /**
     * hostType
     */
    private String hostType;

    /**
     * 多对多关联favorImages表
     */
    @ManyToMany(mappedBy = "favorImages")
	private List<User> favorUsers;
	
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public List<User> getFavorUsers() {
        return favorUsers;
    }
    
    public void setFavorUsers(List<User> favorUsers) {
        this.favorUsers = favorUsers;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCpu() { 
        return cpu; 
    }

    public void setCpu(Integer cpu) { 
        this.cpu = cpu; 
    }

    public Integer getMemory() { 
        return memory; 
    }

    public void setMemory(Integer memory) { 
        this.memory = memory; 
    }

    public Integer getStatus() { 
        return status; 
    }

    public void setStatus(Integer status) { 
        this.status = status; 
    }

    public String getHostType() {
        return hostType;
    }

    public void setHostType(String hostType) {
        this.hostType = hostType;
    }
}
