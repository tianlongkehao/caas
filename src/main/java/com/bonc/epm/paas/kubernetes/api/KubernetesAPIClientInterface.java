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

import java.util.Map;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;

public interface KubernetesAPIClientInterface {

    public static final String VERSION = "v1";

    /* Pod API */

    /**
     * Get information of a Pod given the PodID
     * 
     * @param name
     *            id of the pod
     * @return {@link Pod}
     * @throws KubernetesClientException
     */
    public Pod getPod(String name) throws KubernetesClientException;

    /**
     * Get all Pods
     * 
     * @return Pods
     * @throws KubernetesClientException
     */
    public PodList getAllPods() throws KubernetesClientException;

    /**
     * Create a new Pod
     * 
     * @param pod
     *            Pod to be created
     * @return
     * @throws KubernetesClientException
     */
    public Pod createPod(Pod pod) throws KubernetesClientException;

    /**
     * Delete a Pod
     * 
     * @param name
     *            Id of the Pod to be deleted
     * @throws KubernetesClientException
     */
    public Status deletePod(String name) throws KubernetesClientException;

    /* Replication Controller API */

    /**
     * Get a Replication Controller Info
     * 
     * @param name
     *            id of the Replication Controller
     * @return {@link ReplicationController}
     * @throws KubernetesClientException
     */
    public ReplicationController getReplicationController(String name) throws KubernetesClientException;

    /**
     * Get all Replication Controllers.
     * 
     * @return {@link ReplicationController}s
     * @throws KubernetesClientException
     */
    public ReplicationControllerList getAllReplicationControllers() throws KubernetesClientException;

    /**
     * Create a new Replication Controller
     * 
     * @param controller
     *            controller to be created
     * @throws KubernetesClientException
     */
    public ReplicationController createReplicationController(ReplicationController controller)
            throws KubernetesClientException;

    /**
     * Update a Replication Controller (update the number of replicas).
     * 
     * @param name
     *            id of the controller to be updated
     * @param replicas
     *            update the replicas count of the current controller.
     * @throws KubernetesClientException
     */
    public ReplicationController updateReplicationController(String name, int replicas)
            throws KubernetesClientException;

    /**
     * Delete a Replication Controller.
     * 
     * @param replication
     *            controller id controller id to be deleted.
     * @throws KubernetesClientException
     */
    public Status deleteReplicationController(String name) throws KubernetesClientException;

    /* Services API */

    /**
     * Get the Service with the given id.
     * 
     * @param name
     *            id of the service.
     * @return {@link Service}
     * @throws KubernetesClientException
     */
    public Service getService(String name) throws KubernetesClientException;

    /**
     * Get all the services.
     * 
     * @return array of {@link Service}s
     * @throws KubernetesClientException
     */
    public ServiceList getAllServices() throws KubernetesClientException;

    /**
     * Create a new Kubernetes service.
     * 
     * @param service
     *            service to be created.
     * @throws KubernetesClientException
     */
    public Service createService(Service service) throws KubernetesClientException;

    /**
     * Delete a Service.
     * 
     * @param name
     *            service id to be deleted.
     * @throws KubernetesClientException
     */
    public Status deleteService(String name) throws KubernetesClientException;

    /**
     * Run a label query and retrieve a sub set of Pods.
     * 
     * @param label
     *            of labels for the label query
     * @return Pods selected Pods by executing the label query.
     * @throws KubernetesClientException
     */
    public PodList getSelectedPods(Map<String, String> labels) throws KubernetesClientException;
}