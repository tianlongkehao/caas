package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

public class Container {
	private String name;
	private String image;
	private List<String> command;
	private List<String> args;
	private String workingDir;
	private List<ContainerPort> ports;
	private List<EnvVar> env;
	private ResourceRequirements resources;
	private List<VolumeMount> volumeMounts;
	private Probe livenessProbe;
	private Probe readinessProbe;
	private Lifecycle lifecycle;
	private String terminationMessagePath;
	private String imagePullPolicy;
	private SecurityContext securityContext;
	private Boolean stdin;
	private Boolean stdinOnce;
	private Boolean tty;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<String> getCommand() {
		return command;
	}
	public void setCommand(List<String> command) {
		this.command = command;
	}
	public List<String> getArgs() {
		return args;
	}
	public void setArgs(List<String> args) {
		this.args = args;
	}
	public String getWorkingDir() {
		return workingDir;
	}
	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}
	public List<ContainerPort> getPorts() {
		return ports;
	}
	public void setPorts(List<ContainerPort> ports) {
		this.ports = ports;
	}
	public List<EnvVar> getEnv() {
		return env;
	}
	public void setEnv(List<EnvVar> env) {
		this.env = env;
	}
	public ResourceRequirements getResources() {
		return resources;
	}
	public void setResources(ResourceRequirements resources) {
		this.resources = resources;
	}
	public List<VolumeMount> getVolumeMounts() {
		return volumeMounts;
	}
	public void setVolumeMounts(List<VolumeMount> volumeMounts) {
		this.volumeMounts = volumeMounts;
	}
	public Probe getLivenessProbe() {
		return livenessProbe;
	}
	public void setLivenessProbe(Probe livenessProbe) {
		this.livenessProbe = livenessProbe;
	}
	public Probe getReadinessProbe() {
		return readinessProbe;
	}
	public void setReadinessProbe(Probe readinessProbe) {
		this.readinessProbe = readinessProbe;
	}
	public Lifecycle getLifecycle() {
		return lifecycle;
	}
	public void setLifecycle(Lifecycle lifecycle) {
		this.lifecycle = lifecycle;
	}
	public String getTerminationMessagePath() {
		return terminationMessagePath;
	}
	public void setTerminationMessagePath(String terminationMessagePath) {
		this.terminationMessagePath = terminationMessagePath;
	}
	public String getImagePullPolicy() {
		return imagePullPolicy;
	}
	public void setImagePullPolicy(String imagePullPolicy) {
		this.imagePullPolicy = imagePullPolicy;
	}
	public SecurityContext getSecurityContext() {
		return securityContext;
	}
	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}
	public Boolean isStdin() {
		return stdin;
	}
	public void setStdin(Boolean stdin) {
		this.stdin = stdin;
	}
	public Boolean isStdinOnce() {
		return stdinOnce;
	}
	public void setStdinOnce(Boolean stdinOnce) {
		this.stdinOnce = stdinOnce;
	}
	public Boolean isTty() {
		return tty;
	}
	public void setTty(Boolean tty) {
		this.tty = tty;
	}
	
}
