/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") throws KubernetesClientException; you may not use this file except in compliance
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscalerList;
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.StatefulSetList;

public interface KubernetesAPIS {

	/**
	 * create a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@POST
	@Path("/autoscaling/v1/namespaces/{namespace}/horizontalpodautoscalers")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscaler createHorizontalPodAutoscaler(@PathParam("namespace")String namespace, HorizontalPodAutoscaler horizontalPodAutoscaler) throws KubernetesClientException;

	/**
	 * replace the specified HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@PUT
	@Path("/autoscaling/v1/namespaces/{namespace}/horizontalpodautoscalers/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscaler replaceHorizontalPodAutoscaler(@PathParam("namespace")String namespace, @PathParam("name")String name, HorizontalPodAutoscaler horizontalPodAutoscaler) throws KubernetesClientException;

	/**
	 * delete a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@DELETE
	@Path("/autoscaling/v1/namespaces/{namespace}/horizontalpodautoscalers/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Status deleteHorizontalPodAutoscaler(@PathParam("namespace")String namespace, @PathParam("name")String name) throws KubernetesClientException;

	/**
	 * delete collection of HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@DELETE
	@Path("/autoscaling/v1/namespaces/{namespace}/horizontalpodautoscalers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Status deleteHorizontalPodAutoscalers(@PathParam("namespace")String namespace) throws KubernetesClientException;

	/**
	 * read the specified HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@GET
	@Path("/autoscaling/v1/namespaces/{namespace}/horizontalpodautoscalers/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscaler getHorizontalPodAutoscaler(@PathParam("namespace")String namespace, @PathParam("name")String name) throws KubernetesClientException;

	/**
	 * list or watch objects of kind HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@GET
	@Path("/autoscaling/v1/namespaces/{namespace}/horizontalpodautoscalers")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscalerList listHorizontalPodAutoscalers(@PathParam("namespace")String namespace) throws KubernetesClientException;

	/**
	 * list or watch all objects of kind HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	@GET
	@Path("/autoscaling/v1/horizontalpodautoscalers")
	@Consumes(MediaType.APPLICATION_JSON)
	public HorizontalPodAutoscalerList listAllHorizontalPodAutoscalers() throws KubernetesClientException;

	/**
	 * createStatefulSet:create a StatefulSet. <br/>
	 *
	 * @param namespace
	 * @param statefulSet
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	@POST
	@Path("/apps/v1beta1/namespaces/{namespace}/statefulsets")
	@Consumes(MediaType.APPLICATION_JSON)
	public StatefulSet createStatefulSet(@PathParam("namespace")String namespace, StatefulSet statefulSet) throws KubernetesClientException;

	/**
	 * replaceStatefulSet:replace the specified StatefulSet. <br/>
	 *
	 * @param namespace
	 * @param name
	 * @param statefulSet
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	@PUT
	@Path("/apps/v1beta1/namespaces/{namespace}/statefulsets/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public StatefulSet replaceStatefulSet(@PathParam("namespace")String namespace, @PathParam("name")String name, StatefulSet statefulSet) throws KubernetesClientException;

	/**
	 * deleteStatefulSet:delete a StatefulSet. <br/>
	 *
	 * @param namespace
	 * @param name
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	@DELETE
	@Path("/apps/v1beta1/namespaces/{namespace}/statefulsets/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public StatefulSet deleteStatefulSet(@PathParam("namespace")String namespace, @PathParam("name")String name) throws KubernetesClientException;

	/**
	 * deleteStatefulSets:delete collection of StatefulSet. <br/>
	 *
	 * @param namespace
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	@DELETE
	@Path("/apps/v1beta1/namespaces/{namespace}/statefulsets")
	@Consumes(MediaType.APPLICATION_JSON)
	public Status deleteStatefulSets(@PathParam("namespace")String namespace) throws KubernetesClientException;

	/**
	 * getStatefulSets:read the specified StatefulSet. <br/>
	 *
	 * @param namespace
	 * @param name
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	@GET
	@Path("/apps/v1beta1/namespaces/{namespace}/statefulsets/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public StatefulSet getStatefulSet(@PathParam("namespace")String namespace, @PathParam("name")String name) throws KubernetesClientException;

	/**
	 * getStatefulSets:list objects of kind StatefulSet. <br/>
	 *
	 * @param namespace
	 * @return
	 * @throws KubernetesClientException StatefulSetList
	 */
	@GET
	@Path("/apps/v1beta1/namespaces/{namespace}/statefulsets")
	@Consumes(MediaType.APPLICATION_JSON)
	public StatefulSetList getStatefulSets(@PathParam("namespace")String namespace) throws KubernetesClientException;

	/**
	 * getStatefulSets:list objects of kind StatefulSet. <br/>
	 *
	 * @return
	 * @throws KubernetesClientException StatefulSetList
	 */
	@GET
	@Path("/apps/v1beta1/statefulsets")
	@Consumes(MediaType.APPLICATION_JSON)
	public StatefulSetList getStatefulSets() throws KubernetesClientException;
}
