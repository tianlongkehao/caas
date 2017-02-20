package com.bonc.epm.paas.kubernetes.api;

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
import com.bonc.epm.paas.rest.util.RestFactory;
import com.google.common.base.Joiner;

public class KubernetesApiClient implements KubernetesAPIClientInterface {

	private static final Log LOG = LogFactory.getLog(KubernetesApiClient.class);

    private String endpointURI;
    private KubernetesAPI api;
    private String namespace;

    public KubernetesApiClient(String namespace,String endpointUrl, String username, String password, RestFactory factory) {
    	this.endpointURI = endpointUrl+"api/" + KubernetesAPIClientInterface.VERSION;
        this.namespace = namespace;
        api = factory.createKubernetesAPI(endpointURI, username, password);
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

    public PodList getLabelSelectorPods(Map<String, String> labelSelector) throws KubernetesClientException {
    	String param = Joiner.on(",").withKeyValueSeparator("=").join(labelSelector);
        try {
            return api.getLabelSelectorPods(namespace,param);
        } catch (NotFoundException e) {
            return new PodList();
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

    public Service updateService(String name,Service service) throws KubernetesClientException {
        try {
            return api.updateService(namespace, name, service);
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

	public String getPodLog(String name,String container, Boolean previous,
			Boolean timestamps, Integer tailLines, Integer limitBytes) throws KubernetesClientException {
		try {
			return api.getPodLog(namespace, name, container,
					previous, timestamps, tailLines, limitBytes);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	public String getPodLog(String name,String container, Boolean previous,
			Boolean timestamps, Integer limitBytes) throws KubernetesClientException {
		try {
			return api.getPodLog(namespace, name, container,
					previous, timestamps, limitBytes);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	public String getPodLog(String name,String container, Boolean previous,
			String sinceTime, Boolean timestamps, Integer limitBytes) throws KubernetesClientException {
		try {
			return api.getPodLog(namespace, name, container,
					previous, sinceTime, timestamps, limitBytes);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	public LimitRange getLimitRange(String name) throws KubernetesClientException {
        try {
            return api.getLimitRange(namespace,name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public LimitRangeList getAllLimitRanges() throws KubernetesClientException {
        try {
            return api.getAllLimitRanges(namespace);
        } catch (NotFoundException e) {
            return new LimitRangeList();
        } catch (WebApplicationException e) {
        	e.printStackTrace();
            throw new KubernetesClientException(e);
        }
    }

    public LimitRange createLimitRange(LimitRange limitRange)
            throws KubernetesClientException {
        try {
            return api.createLimitRange(namespace,limitRange);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public LimitRange updateLimitRange(String name, LimitRange limitRange)
            throws KubernetesClientException {
        try {
            return api.updateLimitRange(namespace,name, limitRange);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Status deleteLimitRange(String name) throws KubernetesClientException {
        try {
            return api.deleteLimitRange(namespace,name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ResourceQuota getResourceQuota(String name) throws KubernetesClientException {
        try {
            return api.getResourceQuota(namespace,name);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ResourceQuotaList getAllResourceQuotas() throws KubernetesClientException {
        try {
            return api.getAllResourceQuotas();
        } catch (NotFoundException e) {
            return new ResourceQuotaList();
        } catch (WebApplicationException e) {
        	e.printStackTrace();
            throw new KubernetesClientException(e);
        }
    }

    public ResourceQuota createResourceQuota(ResourceQuota resourceQuota)
            throws KubernetesClientException {
        try {
            return api.createResourceQuota(namespace,resourceQuota);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ResourceQuota updateResourceQuota(String name, ResourceQuota resourceQuota)
            throws KubernetesClientException {
        try {
            return api.updateResourceQuota(namespace,name, resourceQuota);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ResourceQuota deleteResourceQuota(String name) throws KubernetesClientException {
        try {
            return api.deleteResourceQuota(namespace,name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

	@Override
	public NodeList getAllNodes() throws KubernetesClientException {

		try {
            return api.getAllNodes();
        } catch (NotFoundException e) {
        	return new NodeList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
	}

	public Secret createSecret(Secret secret) throws KubernetesClientException {
		try {
            return api.createSecret(namespace, secret);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
	}

    @Override
    public Endpoints createEndpoints(Endpoints endpoints) throws KubernetesClientException {
        try {
            return api.createEndpoints(namespace, endpoints);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }
    @Override
    public EndpointsList getAllEndpoints() throws KubernetesClientException {
        try {
            return api.getAllEndpoints(namespace);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    @Override
    public Endpoints getEndpoints(String name) throws KubernetesClientException {
        try {
            return api.getEndpoints(namespace, name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    @Override
    public Endpoints updateEndpoints(String name, Endpoints endpoints) throws KubernetesClientException {
    	try {
    		return api.updateEndpoints(namespace, name, endpoints);
    	} catch (WebApplicationException e) {
    		throw new KubernetesClientException(e);
    	}
    }

    @Override
    public Node getSpecifiedNode(String name) throws KubernetesClientException {
        try {
            return api.getSpecifiedNode(name);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }
}
