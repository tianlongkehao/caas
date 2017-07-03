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
package com.bonc.epm.paas.kubernetes.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.ConfigMap;
import com.bonc.epm.paas.kubernetes.model.ConfigMapList;
import com.bonc.epm.paas.kubernetes.model.Endpoints;
import com.bonc.epm.paas.kubernetes.model.EndpointsList;
import com.bonc.epm.paas.kubernetes.model.EventList;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeList;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
import com.bonc.epm.paas.kubernetes.model.PersistentVolume;
import com.bonc.epm.paas.kubernetes.model.PersistentVolumeClaim;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaList;
import com.bonc.epm.paas.kubernetes.model.Secret;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;

public interface KubernetesAPI {

	/* resourcequota API */
	/**
     * Get a resourcequota Info
     *
     * @param controllerId
     *            id of the resourcequota
     * @return {@link ResourceQuota}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/resourcequotas/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceQuota getResourceQuota(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;


    /**
     * Get all resourcequotas.
     *
     * @return {@link ResourceQuota}s
     * @throws KubernetesClientException
     */
    @GET
    @Path("/resourcequotas")
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceQuotaList getAllResourceQuotas() throws KubernetesClientException;

    /**
     * Create a new resourcequota
     *
     * @param controller
     *            controller to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/resourcequotas")
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceQuota createResourceQuota(@PathParam("namespace")String namespace,ResourceQuota resourceQuota)
            throws KubernetesClientException;

    /**
     * Update a resourcequota
     *
     * @param controllerId
     *            id of the controller to be updated
     * @param controller
     *            controller to update (only the number of replicas can be
     *            updated).
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/namespaces/{namespace}/resourcequotas/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceQuota updateResourceQuota(@PathParam("namespace")String namespace,@PathParam("name") String name,
            ResourceQuota resourceQuota) throws KubernetesClientException;

    /**
     * Delete a resourcequota.
     *
     * @param replication
     *            controller id controller id to be deleted.
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{namespace}/resourcequotas/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceQuota deleteResourceQuota(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;

	/* limitrange API */
	/**
     * Get a limitrange Info
     *
     * @param controllerId
     *            id of the limitrange
     * @return {@link LimitRange}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/limitranges/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public LimitRange getLimitRange(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;

    /**
     * Get all limitranges.
     *
     * @return {@link LimitRange}s
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/limitranges")
    @Consumes(MediaType.APPLICATION_JSON)
    public LimitRangeList getAllLimitRanges(@PathParam("namespace")String namespace) throws KubernetesClientException;

    /**
     * Create a new limitrange
     *
     * @param controller
     *            controller to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/limitranges")
    @Consumes(MediaType.APPLICATION_JSON)
    public LimitRange createLimitRange(@PathParam("namespace")String namespace,LimitRange limitRange)
            throws KubernetesClientException;

    /**
     * Update a limitrange
     *
     * @param controllerId
     *            id of the controller to be updated
     * @param controller
     *            controller to update (only the number of replicas can be
     *            updated).
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/namespaces/{namespace}/limitranges/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public LimitRange updateLimitRange(@PathParam("namespace")String namespace,@PathParam("name") String name,
            LimitRange limitRange) throws KubernetesClientException;

    /**
     * Delete a limitrange.
     *
     * @param replication
     *            controller id controller id to be deleted.
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{namespace}/limitranges/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Status deleteLimitRange(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;

    /**
     * Get all Namespaces
     *
     * @return Namespaces
     * @throws KubernetesClientException
     */
    @GET
    @Path("/nodes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NodeList getAllNodes() throws KubernetesClientException;

	/* Namespace API */

    /**
     * Get information of a Namespace given the NamespaceID
     *
     * @param podId
     *            id of the pod
     * @return {@link Namespace}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Namespace getNamespace(@PathParam("name") String name) throws KubernetesClientException;

    /**
     * Get all Namespaces
     *
     * @return Namespaces
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NamespaceList getAllNamespaces() throws KubernetesClientException;

    /**
     * Create a new Namespace
     *
     * @param namespace
     *            Namespace to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Namespace createNamespace(Namespace namespace) throws KubernetesClientException;

    /**
     * Delete a Namespace
     *
     * @param namespaceId
     *            Id of the Namespace to be deleted
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Namespace deleteNamespace(@PathParam("name") String name) throws KubernetesClientException;

    /* Pod API */

    /**
     * Get information of a Pod given the PodID
     *
     * @param podId
     *            id of the pod
     * @return {@link Pod}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Pod getPod(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

    /**
     * Get all Pods
     *
     * @return Pods
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PodList getAllPods(@PathParam("namespace")String namespace) throws KubernetesClientException;

    /**
     * Get all Pods
     *
     * @return Pods
     * @throws KubernetesClientException
     */
    @GET
    @Path("/pods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PodList getPods() throws KubernetesClientException;

    /**
     * Get labelSelector Pods
     *
     * @return Pods
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PodList getLabelSelectorPods(@PathParam("namespace")String namespace,@QueryParam("labelSelector")String labelSelector) throws KubernetesClientException;

    /**
     * Create a new Pod
     *
     * @param pod
     *            Pod to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/pods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Pod createPod(@PathParam("namespace")String namespace,Pod pod) throws KubernetesClientException;

    /**
     * Delete a Pod
     *
     * @param podId
     *            Id of the Pod to be deleted
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{namespace}/pods/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Pod deletePod(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

    /**
     * get pod log
     * @param namespace
     * @param name
     * @param container
     * @param previous
     * @param timestamps
     * @param tailLines
     * @return
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods/{name}/log")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPodLog(@PathParam("namespace")String namespace,@PathParam("name")String name,
    		@QueryParam("container")String container,
    		@QueryParam("previous")Boolean previous, 			//Return previous terminated container logs. Defaults to false.
    		@QueryParam("timestamps")Boolean timestamps,
    		@QueryParam("tailLines")Integer tailLines,
    		@QueryParam("limitBytes")Integer limitBytes
    		)
    		throws KubernetesClientException;

    /**
     * get pod log
     * @param namespace
     * @param name
     * @param container
     * @param previous
     * @param timestamps
     * @return
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods/{name}/log")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPodLog(@PathParam("namespace")String namespace,@PathParam("name")String name,
    		@QueryParam("container")String container,
    		@QueryParam("previous")Boolean previous, 			//Return previous terminated container logs. Defaults to false.
    		@QueryParam("timestamps")Boolean timestamps,
    		@QueryParam("limitBytes")Integer limitBytes
    		)
    		throws KubernetesClientException;

    /**
     * get pod log
     * @param namespace
     * @param name
     * @param container
     * @param previous
     * @param sinceTime
     * @param timestamps
     * @return
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods/{name}/log")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPodLog(@PathParam("namespace")String namespace,@PathParam("name")String name,
    		@QueryParam("container")String container,
    		@QueryParam("previous")Boolean previous, 			//Return previous terminated container logs. Defaults to false.
    		@QueryParam("sinceTime")String sinceTime,
    		@QueryParam("timestamps")Boolean timestamps,
    		@QueryParam("limitBytes")Integer limitBytes
    		)
    		throws KubernetesClientException;

    /**
     * get pod log
     * @param namespace
     * @param name
     * @return
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/pods/{name}/log")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPodLog(@PathParam("namespace")String namespace,@PathParam("name")String name,
    		@QueryParam("limitBytes")Integer limitBytes)
    		throws KubernetesClientException;

    /* ConfigMap API */
    /**
     * Create a new ConfigMap
     *
     * @param configMap
     *            ConfigMap to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/configmaps")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ConfigMap createConfigMap(@PathParam("namespace")String namespace,ConfigMap configMap) throws KubernetesClientException;

    /**
     * Delete a ConfigMap
     *
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{namespace}/configmaps/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Status deleteConfigMap(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

    /**
     * Get a ConfigMap Info
     *
     * @return {@link ConfigMap}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/configmaps/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ConfigMap getConfigMap(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;

    /**
     *  update a ConfigMap
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/namespaces/{namespace}/configmaps/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ConfigMap updateConfigMap(@PathParam("namespace")String namespace,@PathParam("name") String name,ConfigMap configMap)
            throws KubernetesClientException;

    /**
     * Get all the configmaps.
     *
     * @return array of {@link configmaps}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/configmaps")
    @Consumes(MediaType.APPLICATION_JSON)
    public ConfigMapList getAllConfigMaps(@PathParam("namespace")String namespace) throws KubernetesClientException;

    /* Replication Controller API */

    /**
     * Get a Replication Controller Info
     *
     * @param controllerId
     *            id of the Replication Controller
     * @return {@link ReplicationController}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/replicationcontrollers/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReplicationController getReplicationController(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;

    /**
     * Get all Replication Controllers.
     *
     * @return {@link ReplicationController}s
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/replicationcontrollers")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReplicationControllerList getAllReplicationControllers(@PathParam("namespace")String namespace) throws KubernetesClientException;

    /**
     * Create a new Replication Controller
     *
     * @param controller
     *            controller to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/replicationcontrollers")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReplicationController createReplicationController(@PathParam("namespace")String namespace,ReplicationController controller)
            throws KubernetesClientException;

    /**
     * Convenience method to update the number of replicas in a Replication
     * Controller.
     *
     * @param controllerId
     *            id of the controller to be updated
     * @param replicas
     *            update the replicas count of the current controller.
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/namespaces/{namespace}/replicationcontrollers/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Status updateReplicationController(@PathParam("namespace")String namespace,@PathParam("name") String name, int replicas)
            throws KubernetesClientException;

    /**
     * Update a Replication Controller
     *
     * @param controllerId
     *            id of the controller to be updated
     * @param controller
     *            controller to update (only the number of replicas can be
     *            updated).
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/namespaces/{namespace}/replicationcontrollers/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReplicationController updateReplicationController(@PathParam("namespace")String namespace,@PathParam("name") String name,
            ReplicationController replicationController) throws KubernetesClientException;

    /**
     * Delete a Replication Controller.
     *
     * @param replication
     *            controller id controller id to be deleted.
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{namespace}/replicationcontrollers/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReplicationController deleteReplicationController(@PathParam("namespace")String namespace,@PathParam("name") String name)
            throws KubernetesClientException;

    /* Services API */

    /**
     * Get the Service with the given id.
     *
     * @param serviceId
     *            id of the service.
     * @return {@link Service}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/services/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Service getService(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

    /**
     * Get all the services.
     *
     * @return array of {@link Service}s
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/services")
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceList getAllServices(@PathParam("namespace")String namespace) throws KubernetesClientException;

    /**
     * Create a new Kubernetes service.
     *
     * @param service
     *            service to be created.
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/services")
    @Consumes(MediaType.APPLICATION_JSON)
    public Service createService(@PathParam("namespace")String namespace,Service service) throws KubernetesClientException;

    /**
     * Delete a Service.
     *
     * @param serviceId
     *            service id to be deleted.
     * @throws KubernetesClientException
     */
    @DELETE
    @Path("/namespaces/{namespace}/services/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Status deleteService(@PathParam("namespace") String namespace,@PathParam("name") String name) throws KubernetesClientException;

    @PUT
    @Path("/namespaces/{namespace}/services/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Service updateService(@PathParam("namespace") String namespace,@PathParam("name") String name,
                                 Service service) throws KubernetesClientException;

    /**
     * Create a new Kubernetes endpoints.
     *
     * @param endpoints
     *            endpoints to be created.
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/endpoints")
    @Consumes(MediaType.APPLICATION_JSON)
    public Endpoints createEndpoints(@PathParam("namespace")String namespace,
                                     Endpoints endpoints) throws KubernetesClientException;

    @PUT
    @Path("/namespaces/{namespace}/endpoints/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Endpoints updateEndpoints(@PathParam("namespace")String namespace,@PathParam("name") String name,
                                     Endpoints endpoints) throws KubernetesClientException;

    /**
     * Get all the endpoints.
     *
     * @return array of {@link Endpoints}s
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/endpoints")
    @Consumes(MediaType.APPLICATION_JSON)
    public EndpointsList getAllEndpoints(@PathParam("namespace")String namespace) throws KubernetesClientException;

    /**
     * Get the Endpoints with the given id.
     *
     * @param endpointsId
     *            id of the endpoints.
     * @return {@link Endpoints}
     * @throws KubernetesClientException
     */
    @GET
    @Path("/namespaces/{namespace}/endpoints/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Endpoints getEndpoints(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

    /**
     * Create a new secret
     *
     * @param secret
     *            Secret to be created
     * @throws KubernetesClientException
     */
    @POST
    @Path("/namespaces/{namespace}/secrets")
    @Consumes(MediaType.APPLICATION_JSON)
    public Secret createSecret(@PathParam("namespace")String namespace, Secret secret) throws KubernetesClientException;

	/**
	 * read the specified Node
	 *
	 * @param name
	 *            name of the node
	 * @throws KubernetesClientException
	 */
	@GET
	@Path("/nodes/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node getSpecifiedNode(@PathParam("name") String name) throws KubernetesClientException;

    @PUT
    @Path("/nodes/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Node updateSpecifiedNode(@PathParam("name") String name,Node node) throws KubernetesClientException;

	/**
	 * getFieldSelectorEvents:根据fieldSelector获取对应的Events. <br/>
	 *
	 * @author longkaixiang
	 * @param namespace
	 * @param fieldSelector
	 * @return
	 * @throws KubernetesClientException
	 *             PodList
	 */
	@GET
	@Path("/namespaces/{namespace}/events")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EventList getFieldSelectorEvents(@PathParam("namespace") String namespace,
			@QueryParam("fieldSelector") String fieldSelector) throws KubernetesClientException;

	/**
	 * getPersistentVolumeClaim:read the specified PersistentVolumeClaim. <br/>
	 *
	 * @param namespaces
	 * @param name
	 * @return
	 * @throws KubernetesClientException PersistentVolumeClaim
	 */
	@GET
	@Path("/namespaces/{namespace}/persistentvolumeclaims/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PersistentVolumeClaim getPersistentVolumeClaim(@PathParam("namespace") String namespaces, @PathParam("name") String name) throws KubernetesClientException;

	/**
	 * getPersistentVolumeClaim:delete a PersistentVolumeClaim. <br/>
	 *
	 * @param namespaces
	 * @param name
	 * @return
	 * @throws KubernetesClientException PersistentVolumeClaim
	 */
	@DELETE
	@Path("/namespaces/{namespace}/persistentvolumeclaims/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PersistentVolumeClaim deletePersistentVolumeClaim(@PathParam("namespace") String namespaces, @PathParam("name") String name) throws KubernetesClientException;

	/**
	 * getPersistentVolumeClaim:delete a PersistentVolume. <br/>
	 *
	 * @param name
	 * @return
	 * @throws KubernetesClientException PersistentVolumeClaim
	 */
	@DELETE
	@Path("/persistentvolumes/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PersistentVolume deletePersistentVolume(@PathParam("name") String name) throws KubernetesClientException;
}
