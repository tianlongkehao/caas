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
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;

public interface KubernetesAPI {

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
    public Status deletePod(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

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
    public Status deleteReplicationController(@PathParam("namespace")String namespace,@PathParam("name") String name)
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
    public Status deleteService(@PathParam("namespace")String namespace,@PathParam("name") String name) throws KubernetesClientException;

    /**
     * Run a label query and retrieve a sub set of Pods.
     * 
     * @param labels
     *            parameter label=label1,label2,label3
     * @return Pods selected Pods by executing the label query.
     * @throws KubernetesClientException
     */
    @GET
    @Path("/pods")
    @Consumes(MediaType.APPLICATION_JSON)
    public PodList getSelectedPods(@PathParam("namespace")String namespace,@QueryParam("labels") String labels) throws KubernetesClientException;
}
