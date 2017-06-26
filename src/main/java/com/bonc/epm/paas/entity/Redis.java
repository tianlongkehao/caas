package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * ClassName: Redis <br/>
 * date: 2017年6月23日 下午2:18:32 <br/>
 *
 * @author longkaixiang
 * @version
 */
@Entity
public class Redis {

	/**
	 * 主键Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	/**
	 * user:使用者.
	 */
	private String user;

	/**
	 * version:版本.
	 */
	private String version;

	/**
	 * nodeNum:节点数.
	 */
	private Integer nodeNum;

	/**
	 * ram:内存.
	 */
	private Integer ram;

	/**
	 * storage:存储卷.
	 */
	private Integer storage;

	/**
	 * cpu:cpu个数.
	 */
	private Integer cpu;

	/**
	 * databaseNum:数据库个数.
	 */
	private Integer databaseNum;

	/**
	 * port:端口.
	 */
	private Integer port;

	/**
	 * memoryPolicy:内存消除策略.
	 */
	private String memoryPolicy;

	/**
	 * daemon:是否配置守护进程.
	 */
	private Integer daemon;

	/**
	 * aof:是否开启AOF.
	 */
	private Integer aof;

	/**
	 * aofSync:AOF同步到磁盘.
	 */
	private String aofSync;

	/**
	 * clientTimeout:客户端超时时间.
	 */
	private Integer clientTimeout;

	/**
	 * databaseDir:数据库存放目录.
	 */
	private String databaseDir;

	/**
	 * databaseFileName:数据库文件名.
	 */
	private String databaseFileName;

	/**
	 * nodeTimeout:节点互连超时时间.
	 */
	private Integer nodeTimeout;

	/**
	 * nodeConfigFile:节点配置文件.
	 */
	private String nodeConfigFile;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(Integer nodeNum) {
		this.nodeNum = nodeNum;
	}

	public Integer getRam() {
		return ram;
	}

	public void setRam(Integer ram) {
		this.ram = ram;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public Integer getCpu() {
		return cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}

	public Integer getDatabaseNum() {
		return databaseNum;
	}

	public void setDatabaseNum(Integer databaseNum) {
		this.databaseNum = databaseNum;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getMemoryPolicy() {
		return memoryPolicy;
	}

	public void setMemoryPolicy(String memoryPolicy) {
		this.memoryPolicy = memoryPolicy;
	}

	public Integer getDaemon() {
		return daemon;
	}

	public void setDaemon(Integer daemon) {
		this.daemon = daemon;
	}

	public Integer getAof() {
		return aof;
	}

	public void setAof(Integer aof) {
		this.aof = aof;
	}

	public String getAofSync() {
		return aofSync;
	}

	public void setAofSync(String aofSync) {
		this.aofSync = aofSync;
	}

	public Integer getClientTimeout() {
		return clientTimeout;
	}

	public void setClientTimeout(Integer clientTimeout) {
		this.clientTimeout = clientTimeout;
	}

	public String getDatabaseDir() {
		return databaseDir;
	}

	public void setDatabaseDir(String databaseDir) {
		this.databaseDir = databaseDir;
	}

	public String getDatabaseFileName() {
		return databaseFileName;
	}

	public void setDatabaseFileName(String databaseFileName) {
		this.databaseFileName = databaseFileName;
	}

	public Integer getNodeTimeout() {
		return nodeTimeout;
	}

	public void setNodeTimeout(Integer nodeTimeout) {
		this.nodeTimeout = nodeTimeout;
	}

	public String getNodeConfigFile() {
		return nodeConfigFile;
	}

	public void setNodeConfigFile(String nodeConfigFile) {
		this.nodeConfigFile = nodeConfigFile;
	}
}
