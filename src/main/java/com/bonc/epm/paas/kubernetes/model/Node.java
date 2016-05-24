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
package com.bonc.epm.paas.kubernetes.model;

public class Node extends AbstractKubernetesModel {

	private ObjectMeta metadata;
	private NodeSpec spec;
	private NodeStatus status;
	
    public Node() {
        super(Kind.NODE);
    }

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public NodeSpec getSpec() {
		return spec;
	}

	public void setSpec(NodeSpec spec) {
		this.spec = spec;
	}

	public NodeStatus getStatus() {
		return status;
	}

	public void setStatus(NodeStatus status) {
		this.status = status;
	}

}
