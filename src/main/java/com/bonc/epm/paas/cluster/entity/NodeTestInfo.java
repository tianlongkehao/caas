package com.bonc.epm.paas.cluster.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NodeTestInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	//节点名称
	private String nodename;

	//测试项目是否全部通过
    private boolean allpass;

    //是否测试ping项目
	private boolean ping;
	private String pingoutmsg;
	private double pingtime;
    //ping是否通过
	private boolean pingpass;
	private String pingIp;
	private int pingtimetarget;

	//是否测试trace项目
	private boolean tracepath;
	private String tracepathoutmsg;
	private double tracetime;
	private boolean tracepass;
	private String traceIp;
	private int tracetimetarget;

	private boolean qperf;
	private String qperfoutmsg;
	private double speed;
	private double latency;
	private boolean qperfpass;
	private int speedtarget;
	private int latencytarget;

	private boolean curl;
	private String curloutmsg;
	private double curltime;
    private boolean curlpass;
    private int curltimetarget;

    private boolean docker;
	private String dockermsg;
	private boolean dockerpass;

	private int dockerPoolBlocksize;
	private int dockerBaseDeviceSize;
	private String dockerBackingFilesystem;
	private String dockerDatafile;
	private int dockerMetaSpaceUsed;
	private int dockerMetaSpaceAvailable;
	private boolean dockerDeferredRemovalEnable;
	private String dockerMetadatafile;
	private int dockerDataSpaceUsed;
	private int dockerDataSpaceTotal;
	private int dockerDataSpaceAvailable;
	private int dockerMetaSpaceTotal;
	private boolean dockerUdevSyncSupported;
	private boolean docekrDeferredDeletionEnable;
	private int dockerDeferredDeletedDeviceCount;

	private int dockerPoolBlocksizeTarget;
	private int dockerBaseDeviceSizeTarget;
	private String dockerBackingFilesystemTarget;
	private String dockerDatafileTarget;
	private int dockerMetaSpaceUsedTarget;
	private int dockerMetaSpaceAvailableTarget;
	private boolean dockerDeferredRemovalEnableTarget;
	private String dockerMetadatafileTarget;
	private int dockerDataSpaceUsedTarget;
	private int dockerDataSpaceTotalTarget;
	private int dockerDataSpaceAvailableTarget;
	private int dockerMetaSpaceTotalTarget;
	private boolean dockerUdevSyncSupportedTarget;
	private boolean docekrDeferredDeletionEnableTarget;
	private int dockerDeferredDeletedDeviceCountTarget;

	private boolean dns;
	private String masterdnsoutmsg;
	private String standbydnsoutmsg;
	private boolean masterdns;
	private boolean standbydns;
	private boolean dnspass;

	public int getDockerDeferredDeletedDeviceCount() {
		return dockerDeferredDeletedDeviceCount;
	}
	public void setDockerDeferredDeletedDeviceCount(int dockerDeferredDeletedDeviceCount) {
		this.dockerDeferredDeletedDeviceCount = dockerDeferredDeletedDeviceCount;
	}
	public int getDockerDeferredDeletedDeviceCountTarget() {
		return dockerDeferredDeletedDeviceCountTarget;
	}
	public void setDockerDeferredDeletedDeviceCountTarget(int dockerDeferredDeletedDeviceCountTarget) {
		this.dockerDeferredDeletedDeviceCountTarget = dockerDeferredDeletedDeviceCountTarget;
	}
	public String getPingIp() {
		return pingIp;
	}
	public void setPingIp(String pingIp) {
		this.pingIp = pingIp;
	}
	public int getPingtimetarget() {
		return pingtimetarget;
	}
	public void setPingtimetarget(int pingtimetarget) {
		this.pingtimetarget = pingtimetarget;
	}
	public String getTraceIp() {
		return traceIp;
	}
	public void setTraceIp(String traceIp) {
		this.traceIp = traceIp;
	}
	public int getTracetimetarget() {
		return tracetimetarget;
	}
	public void setTracetimetarget(int tracetimetarget) {
		this.tracetimetarget = tracetimetarget;
	}
	public int getSpeedtarget() {
		return speedtarget;
	}
	public void setSpeedtarget(int speedtarget) {
		this.speedtarget = speedtarget;
	}
	public int getLatencytarget() {
		return latencytarget;
	}
	public void setLatencytarget(int latencytarget) {
		this.latencytarget = latencytarget;
	}
	public int getCurltimetarget() {
		return curltimetarget;
	}
	public void setCurltimetarget(int curltimetarget) {
		this.curltimetarget = curltimetarget;
	}
	public boolean isDns() {
		return dns;
	}
	public void setDns(boolean dns) {
		this.dns = dns;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isDocker() {
		return docker;
	}
	public void setDocker(boolean docker) {
		this.docker = docker;
	}
	public boolean isDockerpass() {
		return dockerpass;
	}
	public void setDockerpass(boolean dockerpass) {
		this.dockerpass = dockerpass;
	}
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	public boolean isPing() {
		return ping;
	}
	public void setPing(boolean ping) {
		this.ping = ping;
	}
	public String getPingoutmsg() {
		return pingoutmsg;
	}
	public void setPingoutmsg(String pingoutmsg) {
		this.pingoutmsg = pingoutmsg;
	}
	public double getPingtime() {
		return pingtime;
	}
	public void setPingtime(double pingtime) {
		this.pingtime = pingtime;
	}
	public boolean isTracepath() {
		return tracepath;
	}
	public void setTracepath(boolean tracepath) {
		this.tracepath = tracepath;
	}
	public String getTracepathoutmsg() {
		return tracepathoutmsg;
	}
	public void setTracepathoutmsg(String tracepathoutmsg) {
		this.tracepathoutmsg = tracepathoutmsg;
	}
	public double getTracetime() {
		return tracetime;
	}
	public void setTracetime(double tracetime) {
		this.tracetime = tracetime;
	}
	public boolean isQperf() {
		return qperf;
	}
	public void setQperf(boolean qperf) {
		this.qperf = qperf;
	}
	public String getQperfoutmsg() {
		return qperfoutmsg;
	}
	public void setQperfoutmsg(String qperfoutmsg) {
		this.qperfoutmsg = qperfoutmsg;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getLatency() {
		return latency;
	}
	public void setLatency(double latency) {
		this.latency = latency;
	}
	public boolean isCurl() {
		return curl;
	}
	public void setCurl(boolean curl) {
		this.curl = curl;
	}
	public String getCurloutmsg() {
		return curloutmsg;
	}
	public void setCurloutmsg(String curloutmsg) {
		this.curloutmsg = curloutmsg;
	}
	public double getCurltime() {
		return curltime;
	}
	public void setCurltime(double curltime) {
		this.curltime = curltime;
	}
	public String getDockermsg() {
		return dockermsg;
	}
	public void setDockermsg(String dockermsg) {
		this.dockermsg = dockermsg;
	}
	public String getMasterdnsoutmsg() {
		return masterdnsoutmsg;
	}
	public void setMasterdnsoutmsg(String masterdnsoutmsg) {
		this.masterdnsoutmsg = masterdnsoutmsg;
	}
	public String getStandbydnsoutmsg() {
		return standbydnsoutmsg;
	}
	public void setStandbydnsoutmsg(String standbydnsoutmsg) {
		this.standbydnsoutmsg = standbydnsoutmsg;
	}
	public boolean isMasterdns() {
		return masterdns;
	}
	public void setMasterdns(boolean masterdns) {
		this.masterdns = masterdns;
	}
	public boolean isStandbydns() {
		return standbydns;
	}
	public void setStandbydns(boolean standbydns) {
		this.standbydns = standbydns;
	}
	public boolean isPingpass() {
		return pingpass;
	}
	public void setPingpass(boolean pingpass) {
		this.pingpass = pingpass;
	}
	public boolean isTracepass() {
		return tracepass;
	}
	public void setTracepass(boolean tracepass) {
		this.tracepass = tracepass;
	}
	public boolean isQperfpass() {
		return qperfpass;
	}
	public void setQperfpass(boolean qperfpass) {
		this.qperfpass = qperfpass;
	}
	public boolean isCurlpass() {
		return curlpass;
	}
	public void setCurlpass(boolean curlpass) {
		this.curlpass = curlpass;
	}
	public boolean isDnspass() {
		return dnspass;
	}
	public void setDnspass(boolean dnspass) {
		this.dnspass = dnspass;
	}
	public boolean isAllpass() {
		return allpass;
	}
	public void setAllpass(boolean allpass) {
		this.allpass = allpass;
	}
	public int getDockerPoolBlocksize() {
		return dockerPoolBlocksize;
	}
	public void setDockerPoolBlocksize(int dockerPoolBlocksize) {
		this.dockerPoolBlocksize = dockerPoolBlocksize;
	}
	public int getDockerBaseDeviceSize() {
		return dockerBaseDeviceSize;
	}
	public void setDockerBaseDeviceSize(int dockerBaseDeviceSize) {
		this.dockerBaseDeviceSize = dockerBaseDeviceSize;
	}
	public String getDockerBackingFilesystem() {
		return dockerBackingFilesystem;
	}
	public void setDockerBackingFilesystem(String dockerBackingFilesystem) {
		this.dockerBackingFilesystem = dockerBackingFilesystem;
	}
	public String getDockerDatafile() {
		return dockerDatafile;
	}
	public void setDockerDatafile(String dockerDatafile) {
		this.dockerDatafile = dockerDatafile;
	}
	public int getDockerMetaSpaceUsed() {
		return dockerMetaSpaceUsed;
	}
	public void setDockerMetaSpaceUsed(int dockerMetaSpaceUsed) {
		this.dockerMetaSpaceUsed = dockerMetaSpaceUsed;
	}
	public int getDockerMetaSpaceAvailable() {
		return dockerMetaSpaceAvailable;
	}
	public void setDockerMetaSpaceAvailable(int dockerMetaSpaceAvailable) {
		this.dockerMetaSpaceAvailable = dockerMetaSpaceAvailable;
	}
	public boolean isDockerDeferredRemovalEnable() {
		return dockerDeferredRemovalEnable;
	}
	public void setDockerDeferredRemovalEnable(boolean dockerDeferredRemovalEnable) {
		this.dockerDeferredRemovalEnable = dockerDeferredRemovalEnable;
	}
	public String getDockerMetadatafile() {
		return dockerMetadatafile;
	}
	public void setDockerMetadatafile(String dockerMetadatafile) {
		this.dockerMetadatafile = dockerMetadatafile;
	}
	public int getDockerDataSpaceUsed() {
		return dockerDataSpaceUsed;
	}
	public void setDockerDataSpaceUsed(int dockerDataSpaceUsed) {
		this.dockerDataSpaceUsed = dockerDataSpaceUsed;
	}
	public int getDockerDataSpaceTotal() {
		return dockerDataSpaceTotal;
	}
	public void setDockerDataSpaceTotal(int dockerDataSpaceTotal) {
		this.dockerDataSpaceTotal = dockerDataSpaceTotal;
	}
	public int getDockerDataSpaceAvailable() {
		return dockerDataSpaceAvailable;
	}
	public void setDockerDataSpaceAvailable(int dockerDataSpaceAvailable) {
		this.dockerDataSpaceAvailable = dockerDataSpaceAvailable;
	}
	public int getDockerMetaSpaceTotal() {
		return dockerMetaSpaceTotal;
	}
	public void setDockerMetaSpaceTotal(int dockerMetaSpaceTotal) {
		this.dockerMetaSpaceTotal = dockerMetaSpaceTotal;
	}
	public boolean isDockerUdevSyncSupported() {
		return dockerUdevSyncSupported;
	}
	public void setDockerUdevSyncSupported(boolean dockerUdevSyncSupported) {
		this.dockerUdevSyncSupported = dockerUdevSyncSupported;
	}
	public boolean isDocekrDeferredDeletionEnable() {
		return docekrDeferredDeletionEnable;
	}
	public void setDocekrDeferredDeletionEnable(boolean docekrDeferredDeletionEnable) {
		this.docekrDeferredDeletionEnable = docekrDeferredDeletionEnable;
	}
	public int getDockerPoolBlocksizeTarget() {
		return dockerPoolBlocksizeTarget;
	}
	public void setDockerPoolBlocksizeTarget(int dockerPoolBlocksizeTarget) {
		this.dockerPoolBlocksizeTarget = dockerPoolBlocksizeTarget;
	}
	public int getDockerBaseDeviceSizeTarget() {
		return dockerBaseDeviceSizeTarget;
	}
	public void setDockerBaseDeviceSizeTarget(int dockerBaseDeviceSizeTarget) {
		this.dockerBaseDeviceSizeTarget = dockerBaseDeviceSizeTarget;
	}
	public String getDockerBackingFilesystemTarget() {
		return dockerBackingFilesystemTarget;
	}
	public void setDockerBackingFilesystemTarget(String dockerBackingFilesystemTarget) {
		this.dockerBackingFilesystemTarget = dockerBackingFilesystemTarget;
	}
	public String getDockerDatafileTarget() {
		return dockerDatafileTarget;
	}
	public void setDockerDatafileTarget(String dockerDatafileTarget) {
		this.dockerDatafileTarget = dockerDatafileTarget;
	}
	public int getDockerMetaSpaceUsedTarget() {
		return dockerMetaSpaceUsedTarget;
	}
	public void setDockerMetaSpaceUsedTarget(int dockerMetaSpaceUsedTarget) {
		this.dockerMetaSpaceUsedTarget = dockerMetaSpaceUsedTarget;
	}
	public int getDockerMetaSpaceAvailableTarget() {
		return dockerMetaSpaceAvailableTarget;
	}
	public void setDockerMetaSpaceAvailableTarget(int dockerMetaSpaceAvailableTarget) {
		this.dockerMetaSpaceAvailableTarget = dockerMetaSpaceAvailableTarget;
	}
	public boolean isDockerDeferredRemovalEnableTarget() {
		return dockerDeferredRemovalEnableTarget;
	}
	public void setDockerDeferredRemovalEnableTarget(boolean dockerDeferredRemovalEnableTarget) {
		this.dockerDeferredRemovalEnableTarget = dockerDeferredRemovalEnableTarget;
	}
	public String getDockerMetadatafileTarget() {
		return dockerMetadatafileTarget;
	}
	public void setDockerMetadatafileTarget(String dockerMetadatafileTarget) {
		this.dockerMetadatafileTarget = dockerMetadatafileTarget;
	}
	public int getDockerDataSpaceUsedTarget() {
		return dockerDataSpaceUsedTarget;
	}
	public void setDockerDataSpaceUsedTarget(int dockerDataSpaceUsedTarget) {
		this.dockerDataSpaceUsedTarget = dockerDataSpaceUsedTarget;
	}
	public int getDockerDataSpaceTotalTarget() {
		return dockerDataSpaceTotalTarget;
	}
	public void setDockerDataSpaceTotalTarget(int dockerDataSpaceTotalTarget) {
		this.dockerDataSpaceTotalTarget = dockerDataSpaceTotalTarget;
	}
	public int getDockerDataSpaceAvailableTarget() {
		return dockerDataSpaceAvailableTarget;
	}
	public void setDockerDataSpaceAvailableTarget(int dockerDataSpaceAvailableTarget) {
		this.dockerDataSpaceAvailableTarget = dockerDataSpaceAvailableTarget;
	}
	public int getDockerMetaSpaceTotalTarget() {
		return dockerMetaSpaceTotalTarget;
	}
	public void setDockerMetaSpaceTotalTarget(int dockerMetaSpaceTotalTarget) {
		this.dockerMetaSpaceTotalTarget = dockerMetaSpaceTotalTarget;
	}
	public boolean isDockerUdevSyncSupportedTarget() {
		return dockerUdevSyncSupportedTarget;
	}
	public void setDockerUdevSyncSupportedTarget(boolean dockerUdevSyncSupportedTarget) {
		this.dockerUdevSyncSupportedTarget = dockerUdevSyncSupportedTarget;
	}
	public boolean isDocekrDeferredDeletionEnableTarget() {
		return docekrDeferredDeletionEnableTarget;
	}
	public void setDocekrDeferredDeletionEnableTarget(boolean docekrDeferredDeletionEnableTarget) {
		this.docekrDeferredDeletionEnableTarget = docekrDeferredDeletionEnableTarget;
	}

}
