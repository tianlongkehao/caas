package com.bonc.epm.paas.entity;


public class Resource {
//	private Integer cpu_account ;//CPU数量
//	private Integer ram ;//内存
//	private Integer vol_count ;//卷组挂载数量
//	private Integer vol ;//卷组容量
//	private Integer pod_count ;//Pod数量
//	private Integer image_control ;//副本控制器
//	private Integer server_count ;//服务
	
	private String cpu_account ;//CPU数量
	private String ram ;//内存
	private String vol_count ;//卷组挂载数量
	private String vol ;//卷组容量
	private String pod_count ;//Pod数量
	private String image_control ;//副本控制器
	private String server_count ;//服务
	
	public String getCpu_account() {
		return cpu_account;
	}
	public void setCpu_account(String cpu_account) {
		this.cpu_account = cpu_account;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getVol_count() {
		return vol_count;
	}
	public void setVol_count(String vol_count) {
		this.vol_count = vol_count;
	}
	public String getVol() {
		return vol;
	}
	public void setVol(String vol) {
		this.vol = vol;
	}
	public String getPod_count() {
		return pod_count;
	}
	public void setPod_count(String pod_count) {
		this.pod_count = pod_count;
	}
	public String getImage_control() {
		return image_control;
	}
	public void setImage_control(String image_control) {
		this.image_control = image_control;
	}
	public String getServer_count() {
		return server_count;
	}
	public void setServer_count(String server_count) {
		this.server_count = server_count;
	}
	
	
}
