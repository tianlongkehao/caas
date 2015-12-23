package com.bonc.epm.paas.kubernetes.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ReplicationController;
import com.bonc.epm.paas.kubernetes.model.ReplicationControllerList;
import com.bonc.epm.paas.kubernetes.model.Service;
import com.bonc.epm.paas.kubernetes.model.ServiceList;
import com.google.common.base.Joiner;

public class KubernetesApiClient implements KubernetesAPIClientInterface {

    private static final Log LOG = LogFactory.getLog(KubernetesApiClient.class);

    private URI endpointURI;
    private KubernetesAPI api;
    private String namespace;
    
    public KubernetesApiClient(String namespace,String endpointUrl, String username, String password, RestFactory factory) {
        try {
            if (endpointUrl.matches("/api/v1[a-z0-9]+")) {
                LOG.warn("Deprecated: KubernetesApiClient endpointUrl should not include the /api/version section in "
                        + endpointUrl);
                endpointURI = new URI(endpointUrl);
            } else {
                endpointURI = new URI(endpointUrl + "/api/" + KubernetesAPIClientInterface.VERSION);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.namespace = namespace;
        api = factory.createAPI(endpointURI, username, password);
    }

    public Pod getPod(String name) throws KubernetesClientException {
        try {
            return api.getPod(namespace,name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public PodList getAllPods() throws KubernetesClientException {
        try {
            return api.getAllPods(namespace);
        } catch (NotFoundException e) {
            return new PodList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public PodList getSelectedPods(Map<String, String> labels) throws KubernetesClientException {
        String param = Joiner.on(",").withKeyValueSeparator("=").join(labels);

        try {
            return api.getSelectedPods(namespace,param);
        } catch (NotFoundException e) {
            return new PodList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Pod createPod(Pod pod) throws KubernetesClientException {
        try {
            return api.createPod(namespace,pod);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Pod deletePod(String name) throws KubernetesClientException {
        try {
            return api.deletePod(namespace,name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController getReplicationController(String name) throws KubernetesClientException {
        try {
            return api.getReplicationController(namespace,name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationControllerList getAllReplicationControllers() throws KubernetesClientException {
        try {
            return api.getAllReplicationControllers(namespace);
        } catch (NotFoundException e) {
            return new ReplicationControllerList();
        } catch (WebApplicationException e) {
        	e.printStackTrace();
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController createReplicationController(ReplicationController controller)
            throws KubernetesClientException {
        try {
            return api.createReplicationController(namespace,controller);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController updateReplicationController(String name, int replicas)
            throws KubernetesClientException {
        try {
            ReplicationController controller = api.getReplicationController(namespace,name);
            controller.getSpec().setReplicas(replicas);
            return api.updateReplicationController(namespace,name, controller);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController updateReplicationController(String name, ReplicationController controller)
            throws KubernetesClientException {
        try {
            return api.updateReplicationController(namespace,name, controller);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Status deleteReplicationController(String name) throws KubernetesClientException {
        try {
            return api.deleteReplicationController(namespace,name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Service getService(String name) throws KubernetesClientException {
        try {
            return api.getService(namespace,name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ServiceList getAllServices() throws KubernetesClientException {
        try {
            return api.getAllServices(namespace);
        } catch (NotFoundException e) {
            return new ServiceList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Service createService(Service service) throws KubernetesClientException {
        try {
            return api.createService(namespace,service);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Status deleteService(String name) throws KubernetesClientException {
        try {
            return api.deleteService(namespace,name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

	public Namespace getNamespace(String name) throws KubernetesClientException {
		try {
            return api.getNamespace(name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
	}

	public NamespaceList getAllNamespaces() throws KubernetesClientException {
		try {
            return api.getAllNamespaces();
        } catch (NotFoundException e) {
            return new NamespaceList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
	}

	public Namespace createNamespace(Namespace namespace) throws KubernetesClientException {
		try {
            return api.createNamespace(namespace);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
	}

	public Namespace deleteNamespace(String name) throws KubernetesClientException {
		try {
            return api.deleteNamespace(name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
	}
   
}
