package com.bonc.epm.paas.entity;

public class UserInfo {
	UserResource userResource;
	User user;
	Integer imageCount;
	/*
	 * cpu个数
	 */
	double servCpuNum;
	/*
	 * 内存个数
	 */
	String servMemoryNum;
	/*
	 * pod个数
	 */
	String servPodNum;
	/*
	 * 服务个数
	 */
	String servServiceNum;
	/*
	 * 副本控制数
	 */
	String servControllerNum;
	/*
	 * 已使用CPU个数
	 */
	float usedCpuNum;
	/*
	 * 已使用内存
	 */
	float usedMemoryNum;
	/*
	 * 已经使用的POD个数
	 */
	int usedPodNum;
	/*
	 * 已经使用的服务个数
	 */
	int usedServiceNum;
	/*
	 * shera
	 */
	Shera userShera;
	/*
	 * 存储卷
	 */
	double usedStorage;

	public UserResource getUserResource() {
		return userResource;
	}

	public Shera getUserShera() {
		return userShera;
	}

	public void setUserShera(Shera userShera) {
		this.userShera = userShera;
	}

	public double getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(double usedStorage) {
		this.usedStorage = usedStorage;
	}

	public void setUserResource(UserResource userResource) {
		this.userResource = userResource;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

	public double getServCpuNum() {
		return servCpuNum;
	}

	public void setServCpuNum(double servCpuNum) {
		this.servCpuNum = servCpuNum;
	}

	public String getServMemoryNum() {
		return servMemoryNum;
	}

	public void setServMemoryNum(String servMemoryNum) {
		this.servMemoryNum = servMemoryNum;
	}

	public String getServPodNum() {
		return servPodNum;
	}

	public void setServPodNum(String servPodNum) {
		this.servPodNum = servPodNum;
	}

	public String getServServiceNum() {
		return servServiceNum;
	}

	public void setServServiceNum(String servServiceNum) {
		this.servServiceNum = servServiceNum;
	}

	public String getServControllerNum() {
		return servControllerNum;
	}

	public void setServControllerNum(String servControllerNum) {
		this.servControllerNum = servControllerNum;
	}

	public float getUsedCpuNum() {
		return usedCpuNum;
	}

	public void setUsedCpuNum(float usedCpuNum) {
		this.usedCpuNum = usedCpuNum;
	}

	public float getUsedMemoryNum() {
		return usedMemoryNum;
	}

	public void setUsedMemoryNum(float usedMemoryNum) {
		this.usedMemoryNum = usedMemoryNum;
	}

	public int getUsedPodNum() {
		return usedPodNum;
	}

	public void setUsedPodNum(int usedPodNum) {
		this.usedPodNum = usedPodNum;
	}

	public int getUsedServiceNum() {
		return usedServiceNum;
	}

	public void setUsedServiceNum(int usedServiceNum) {
		this.usedServiceNum = usedServiceNum;
	}
	public float getRestCpuNum() {
		return (float)(Math.round(servCpuNum - usedCpuNum)*100)/100;
	}
	public double getRestMemoryNum() {
		return Double.parseDouble(servMemoryNum) - usedMemoryNum;
	}
	public double getRestStorage() {
		return userResource.getVol_size()-usedStorage;
	}

	
}
