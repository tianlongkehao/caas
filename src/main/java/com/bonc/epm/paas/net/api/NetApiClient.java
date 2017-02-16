package com.bonc.epm.paas.net.api;

import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.Endpoints;
import com.bonc.epm.paas.kubernetes.model.EndpointsList;
import com.bonc.epm.paas.kubernetes.model.LimitRange;
import com.bonc.epm.paas.kubernetes.model.LimitRangeList;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.model.ResourceQuotaList;
import com.bonc.epm.paas.kubernetes.model.Secret;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;
import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.Nodes;
import com.bonc.epm.paas.rest.util.RestFactory;
import com.google.common.base.Joiner;

public class NetApiClient implements NetAPIClientInterface {

    private static final Log LOG = LogFactory.getLog(NetApiClient.class);

    private String endpointURI;
    private NetAPI api;

    public NetApiClient(String namespace,String endpointUrl, String username, String password, RestFactory factory) {
    	this.endpointURI = endpointUrl+"api/" + NetAPIClientInterface.VERSION;
        api = factory.createNetAPI(endpointURI, username, password);
    }

	@Override
	public Nodes getNodes() throws NetClientException {
		return null;
	}

}
