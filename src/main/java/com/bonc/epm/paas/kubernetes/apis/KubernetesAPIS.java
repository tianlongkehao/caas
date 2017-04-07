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
package com.bonc.epm.paas.kubernetes.apis;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;

public interface KubernetesAPIS {

	/**
	 * create a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@POST
	@Path("/namespaces/{namespace}/horizontalpodautoscalers")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscaler createHorizontalPodAutoscaler(@PathParam("namespace")String namespace, HorizontalPodAutoscaler horizontalPodAutoscaler);

	/**
	 * replace a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@POST
	@Path("/namespaces/{namespace}/horizontalpodautoscalers/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscaler replaceHorizontalPodAutoscaler(@PathParam("namespace")String namespace, HorizontalPodAutoscaler horizontalPodAutoscaler);

	/**
	 * @param namespace
	 * @param name
	 * @return
	 */
	@GET
	@Path("/nodes/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscaler getHorizontalPodAutoscaler(@PathParam("namespace")String namespace, @PathParam("namespace")String name);

}
