package com.bonc.epm.paas.cluster.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NodeTestInfo {

	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	//节点名称
	@Id
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
	private int disk;
	private String dockermsg;
	private boolean dockerpass;
	private int memorytarget;

	private boolean dns;
	private String masterdnsoutmsg;
	private String standbydnsoutmsg;
	private boolean masterdns;
	private boolean standbydns;
	private boolean dnspass;


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
	public int getMemorytarget() {
		return memorytarget;
	}
	public void setMemorytarget(int memorytarget) {
		this.memorytarget = memorytarget;
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
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
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

}
