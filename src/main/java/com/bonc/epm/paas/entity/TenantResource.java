package com.bonc.epm.paas.entity;

public class TenantResource {

	private long id;
	private String name;
	private double cpu;
	private long memory;
	private String quotaCpu;
	private String quotaMem;
	private String quotaCpuUsed;
	private String quotaMemUsed;

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

	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public String getQuotaCpu() {
		return quotaCpu;
	}

	public void setQuotaCpu(String quotaCpu) {
		this.quotaCpu = quotaCpu;
	}

	public String getQuotaMem() {
		return quotaMem;
	}

	public void setQuotaMem(String quotaMem) {
		this.quotaMem = quotaMem;
	}

	public String getQuotaCpuUsed() {
		return quotaCpuUsed;
	}

	public void setQuotaCpuUsed(String quotaCpuUsed) {
		this.quotaCpuUsed = quotaCpuUsed;
	}

	public String getQuotaMemUsed() {
		return quotaMemUsed;
	}

	public void setQuotaMemUsed(String quotaMemUsed) {
		this.quotaMemUsed = quotaMemUsed;
	}

}
