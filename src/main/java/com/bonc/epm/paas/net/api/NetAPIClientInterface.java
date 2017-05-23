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

import java.util.List;

import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.Diff;
import com.bonc.epm.paas.net.model.Iptable;
import com.bonc.epm.paas.net.model.NodeInfo;
import com.bonc.epm.paas.net.model.Nodes;
import com.bonc.epm.paas.net.model.RecoverResult;
import com.bonc.epm.paas.net.model.RouteTable;
import com.bonc.epm.paas.net.model.Service;

public interface NetAPIClientInterface {


	/**
	 * Get a Nodes Info
	 *
	 * @return {@link Nodes}
	 * @throws NetClientException
	 */
	public Nodes getNodes() throws NetClientException;

	/**
	 * Get RouteTable
	 *
	 * @return {@link RouteTable}
	 * @throws NetClientException
	 */
	public RouteTable checkRoutetable() throws NetClientException;

	/**
	 * Get Diff
	 *
	 * @return {@link Diff}
	 * @throws NetClientException
	 */
	public Diff getDiff(Service service) throws NetClientException;

	/**
	 * checkIptable:检查Iptable. <br/>
	 *
	 * @author longkaixiang
	 * @return
	 * @throws NetClientException List<String>
	 */
	public List<Iptable> checkIptable() throws NetClientException;

	/**
	 * recoverRoutetable:修复Routetable. <br/>
	 *
	 * @author longkaixiang
	 * @param node
	 * @return
	 * @throws NetClientException RecoverResult
	 */
	public RecoverResult recoverRoutetable(NodeInfo node) throws NetClientException;

	/**
	 * recoverIptables:修复Iptable. <br/>
	 *
	 * @author longkaixiang
	 * @return
	 * @throws NetClientException RecoverResult
	 */
	public RecoverResult recoverIptables() throws NetClientException;

}
