package com.bonc.epm.paas.net.model;

/**
 * ClassName: RecoverResult <br/>
 * Function: Recover的返回对象. <br/>
 * date: 2017年5月10日 上午11:41:50 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class RecoverResult {
	private NodeInfo node;

	/**
	 * cordon和drain:同时成功时表示节点驱逐成功.
	 */
	private boolean cordon;
	private boolean drain;

	/**
	 * restart:重启成功 恢复iptable只需要看这一个参数.
	 */
	private boolean restart;

	/**
	 * uncordon:解除驱逐成功.
	 */
	private boolean uncordon;

	public NodeInfo getNode() {
		return node;
	}

	public void setNode(NodeInfo node) {
		this.node = node;
	}

	public boolean isCordon() {
		return cordon;
	}

	public void setCordon(boolean cordon) {
		this.cordon = cordon;
	}

	public boolean isDrain() {
		return drain;
	}

	public void setDrain(boolean drain) {
		this.drain = drain;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public boolean isUncordon() {
		return uncordon;
	}

	public void setUncordon(boolean uncordon) {
		this.uncordon = uncordon;
	}
}
