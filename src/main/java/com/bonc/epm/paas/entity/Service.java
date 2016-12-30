package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

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
	 * 服务名称
	 */
	private String serviceName;
	/**
	 * 实例数量
	 */
	private Integer instanceNum;
	/**
	 * 服务类型(1有状态 2无状态)
	 */
	private String serviceType;
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
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	private long createBy;
	private double cpuNum;
	private String ram;
	private long imgID;         //TODO imageId
	
	/**
	 * 自定义启动命令
	 */
	private String startCommand;
	
	/**
	 * 监控设置
	 */
	private String monitor;
	
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
	 * 黏连
	 */
	private String nodeIpAffinity;    
	
	/**
	 * 服务中文名称
	 */
	private String serviceChName;
	
	/**
	 * 更新时间
	 */
	private Date updateDate;
	
	/**
	 * 更新者id
	 */
	private long updateBy;
	
	/**
	 * 根据代码仓库中的代码是否发生变化，来提醒用户是否需要重新构建镜像
	 */
	@Transient
	private boolean updateImage;
	
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

    public boolean isUpdateImage() {
        return updateImage;
    }

    public void setUpdateImage(boolean updateImage) {
        this.updateImage = updateImage;
    }

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}
    
	@Override
	public String toString() {
		return "id:" + id + ",cpuNum:" + cpuNum + ",imgID:" + imgID + ",imgName:" + imgName + ",imgVersion:"
				+ imgVersion + ",instanceNum:" + instanceNum + ",ram:" + ram + ",serviceAddr:" + serviceAddr
				+ ",serviceName:" + serviceName + ",serviceChName:" + serviceChName + ",serviceType:" + serviceType
				+ ",status:" + status + ",checkPath:" + checkPath + ",sessionAffinity:" + sessionAffinity
				+ ",initialDelay:" + initialDelay + ",periodDetction:" + periodDetction + ",timeoutDetction:"
				+ timeoutDetction + ",nodeIpAffinity:" + nodeIpAffinity + ",proxyPath:" + proxyPath + ",proxyZone:"
				+ proxyZone + ",servicePath:" + servicePath + ",startCommand:" + startCommand + ",createDate:"
				+ createDate + ",createBy:" + createBy + ",updateDate:" + updateDate + ",updateBy:" + updateBy;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}
}
