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

	private String nodename;
    private boolean allpass;

	private boolean ping;
	private String pingoutmsg;
	private double pingtime;
	private boolean pingpass;

	private boolean tracepath;
	private String tracepathoutmsg;
	private double tracetime;
	private boolean tracepass;

	private boolean qperf;
	private String qperfoutmsg;
	private double speed;
	private double latency;
	private boolean qperfpass;

	private boolean curl;
	private String curloutmsg;
	private double curltime;
    private boolean curlpass;

	private int cpu;
	private long memory;
	private boolean dockerpass;

	private String masterdnsoutmsg;
	private String standbydnsoutmsg;
	private boolean masterdns;
	private boolean standbydns;
	private boolean dnspass;


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
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
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
