package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class Service {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 镜像名称
	 */
	private String imgName;
	/**
	 * 镜像版本
	 */
	private String imgVersion;
	/**
	 * 镜像类型
	 */
	private String imgType;
	/**
	 * 服务名称
	 */
	private String serviceName;
	/**
	 * 集群设置
	 */
	private String groupSet;
	/**
	 * 容器设置
	 */
	private String containerSet;
	/**
	 * 实例数量
	 */
	private Integer instanceNum;
	/**
	 * 服务类型(1有状态 2无状态)
	 */
	private String serviceType;
	/**
	 * 镜像设置
	 */
	private String imgSet;
	/**
	 * 检查服务状态填写的路径
	 */
	private String checkPath;
	/**
	 * 服务地址
	 */
	private String serviceAddr;
	/**
	 * 运行状态(1未启动 2创建中 3运行中 4已停止 5创建失败 6服务异常)
	 */
	private Integer status;
	/**
	 * 环境变量
	 */
	private String buildPath;
	/**
	 * 端口配置
	 */
	private String portSet;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	private long containerID;
	private long createBy;
	private double cpuNum;
	private String ram;
	private long imgID;
	// 挂载卷名称
	private String volName;
	// 挂载卷名称
	private String mountPath;
	// private List<String> podName;
	// public List<String> getPodName() {
	// return podName;
	// }
	// public void setPodName(List<String> podName) {
	// this.podName = podName;
	// }
	
	/**
	 * 自定义启动命令
	 */
	private String startCommand;
	
	/**
	 * 服务访问路径
	 */
	private String servicePath;
	
	/**
	 * nginx代理区域
	 */
	private String proxyZone;
	
	/**
	 * nginx代理路径
	 */
	private String proxyPath;
	
	/**
	 * 服务会话黏连方式
	 */
	private String sessionAffinity;
	
	/**
	 * 服务检测延迟
	 */
	private Integer initialDelay;
	
	/**
	 * 服务检测超时
	 */
	private Integer timeoutDetction;
	
	/**
	 * 服务检测频率
	 */
	private Integer periodDetction;
	/**
	 */
	private String nodeIpAffinity;
	
	/**
	 * 服务中文名称
	 */
	private String serviceChName;

	
    public String getServiceChName() {
		return serviceChName;
	}

	public void setServiceChName(String serviceChName) {
		this.serviceChName = serviceChName;
	}

	public long getImgID() {
		return imgID;
	}

	public void setImgID(long imgID) {
		this.imgID = imgID;
	}

	public double getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(double cpuNum) {
		this.cpuNum = cpuNum;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public long getContainerID() {
		return containerID;
	}

	public void setContainerID(long containerID) {
		this.containerID = containerID;
	}

	public String getBuildPath() {
		return buildPath;
	}

	public void setBuildPath(String buildPath) {
		this.buildPath = buildPath;
	}

	public String getPortSet() {
		return portSet;
	}

	public void setPortSet(String portSet) {
		this.portSet = portSet;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgVersion() {
		return imgVersion;
	}

	public void setImgVersion(String imgVersion) {
		this.imgVersion = imgVersion;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getGroupSet() {
		return groupSet;
	}

	public void setGroupSet(String groupSet) {
		this.groupSet = groupSet;
	}

	public String getContainerSet() {
		return containerSet;
	}

	public void setContainerSet(String containerSet) {
		this.containerSet = containerSet;
	}

	public Integer getInstanceNum() {
		return instanceNum;
	}

	public void setInstanceNum(Integer instanceNum) {
		this.instanceNum = instanceNum;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getImgSet() {
		return imgSet;
	}

	public void setImgSet(String imgSet) {
		this.imgSet = imgSet;
	}

	public String getCheckPath() {
        return checkPath;
    }

    public void setCheckPath(String checkPath) {
        this.checkPath = checkPath;
    }

    public String getServiceAddr() {
		return serviceAddr;
	}

	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getVolName() {
		return volName;
	}

	public void setVolName(String volName) {
		this.volName = volName;
	}

	public String getMountPath() {
		return mountPath;
	}

	public void setMountPath(String mountPath) {
		this.mountPath = mountPath;
	}

	public String getStartCommand() {
		return startCommand;
	}

	public void setStartCommand(String startCommand) {
		this.startCommand = startCommand;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getProxyZone() {
		return proxyZone;
	}

	public void setProxyZone(String proxyZone) {
		this.proxyZone = proxyZone;
	}

	public String getProxyPath() {
		return proxyPath;
	}

	public void setProxyPath(String proxyPath) {
		this.proxyPath = proxyPath;
	}
	
    public String getSessionAffinity() {
        return sessionAffinity;
    }

    public void setSessionAffinity(String sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }

    public Integer getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(Integer initialDelay) {
        this.initialDelay = initialDelay;
    }

    public Integer getTimeoutDetction() {
        return timeoutDetction;
    }

    public void setTimeoutDetction(Integer timeoutDetction) {
        this.timeoutDetction = timeoutDetction;
    }

    public Integer getPeriodDetction() {
        return periodDetction;
    }

    public void setPeriodDetction(Integer periodDetction) {
        this.periodDetction = periodDetction;
    }

    public String getNodeIpAffinity() {
        return nodeIpAffinity;
    }

    public void setNodeIpAffinity(String nodeIpAffinity) {
        this.nodeIpAffinity = nodeIpAffinity;
    }

}
