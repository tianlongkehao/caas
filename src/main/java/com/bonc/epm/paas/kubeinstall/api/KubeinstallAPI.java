package com.bonc.epm.paas.kubeinstall.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.RecoverResult;

public interface KubeinstallAPI {
	/**
	 * recoverIptables:恢复当前节点的Iptables. <br/>
	 *
	 * @author longkaixiang
	 * @return
	 * @throws NetClientException RecoverResult
	 */
	@PUT
	@Path("/recover/iptables")
	@Consumes(MediaType.APPLICATION_JSON)
	public RecoverResult recoverIptables() throws NetClientException;
}
