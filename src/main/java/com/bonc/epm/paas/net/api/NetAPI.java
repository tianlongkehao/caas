/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.bonc.epm.paas.net.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.Diff;
import com.bonc.epm.paas.net.model.NodeInfo;
import com.bonc.epm.paas.net.model.Nodes;
import com.bonc.epm.paas.net.model.RecoverResult;
import com.bonc.epm.paas.net.model.RouteTable;
import com.bonc.epm.paas.net.model.Service;

public interface NetAPI {

	/**
	 * Get all nodes
	 *
	 * @return {@link Nodes}
	 * @throws NetClientException
	 */
	@GET
	@Path("/get/nodes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Nodes getNodes() throws NetClientException;

	/**
	 * check Routetable
	 *
	 * @return {@link RouteTable}
	 * @throws NetClientException
	 */
	@GET
	@Path("/check/routetable")
	@Consumes(MediaType.APPLICATION_JSON)
	public RouteTable checkRoutetable() throws NetClientException;

	/**
	 * getDiff:获取iptable的不同. <br/>
	 *
	 * @author longkaixiang
	 * @param service
	 * @return
	 * @throws NetClientException Diff
	 */
	@PUT
	@Path("/get/Diff")
	@Consumes(MediaType.APPLICATION_JSON)
	public Diff getDiff(Service service) throws NetClientException;

	/**
	 * checkIptable:检查当前节点的Iptable. <br/>
	 *
	 * @author longkaixiang
	 * @return
	 * @throws NetClientException RecoverResult
	 */
	@PUT
	@Path("/check/iptables")
	@Consumes(MediaType.APPLICATION_JSON)
	public String checkIptable() throws NetClientException;

	/**
	 * recoverRoutetable:恢复当前节点的Routetable. <br/>
	 *
	 * @author longkaixiang
	 * @param node
	 * @return
	 * @throws NetClientException RecoverResult
	 */
	@PUT
	@Path("/recover/routetable")
	@Consumes(MediaType.APPLICATION_JSON)
	public RecoverResult recoverRoutetable(NodeInfo node) throws NetClientException;

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
