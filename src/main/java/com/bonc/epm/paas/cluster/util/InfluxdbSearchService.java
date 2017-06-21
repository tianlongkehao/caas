/*
 * 文件名：InfluxdbSearchService.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年12月7日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.cluster.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bonc.epm.paas.cluster.entity.CatalogResource;
import com.bonc.epm.paas.cluster.entity.Collectivity;
import com.bonc.epm.paas.cluster.entity.DetailInfo;
import com.bonc.epm.paas.cluster.entity.DetailResource;
import com.bonc.epm.paas.cluster.entity.Kind;
import com.bonc.epm.paas.controller.MonitorController;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.model.Container;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.NamespaceList;
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.kubernetes.model.PodList;
import com.bonc.epm.paas.kubernetes.model.ServicePort;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.DateUtils;

/**
 * @author ke_wang
 * @version 2016年12月7日
 * @see InfluxdbSearchService
 * @since
 */
@Service
public class InfluxdbSearchService {

    /**
     * InfluxdbSearchService日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(InfluxdbSearchService.class);
    /**
     * 获取配置文件中的监控器的namespace地址信息
     */
    @Value("${monitor.k8s.namespace}")
    private String namespace;
    /**
     * 获取配置文件中的监控器的service地址信息
     */
    @Value("${monitor.k8s.service}")
    private String serviceName;
    /**
     * 获取配置文件中的监控器的url地址信息
     */
    @Value("${monitor.url}")
    private String url;

    /**
     * 获取监控器的username数据信息；
     */
    @Value("${monitor.username}")
    private String username;

    /**
     * 获取监控器的密码；
     */
    @Value("${monitor.password}")
    private String password;

    /**
     * 获取监控器的dbName;
     */
    @Value("${monitor.dbName}")
    private String dbName;

    /**
     * master主节点地址信息
     */
    @Value("${kubernetes.api.address}")
    private String masterAddress;

    /**
     * KubernetesClientService 服务接口
     */
    @Autowired
    private KubernetesClientService kubernetesClientService;

    /**
     *
     * Description:
     * 获取容器运行的influxDB客户端
     * @return InfluxDB
     * @see InfluxDB
     */
    public InfluxDB getInfluxdbClient() {
    	KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
		com.bonc.epm.paas.kubernetes.model.Service service = client.getService(serviceName);
		if (service != null) {
			Integer port = 0;
			List<ServicePort> ports = service.getSpec().getPorts();
			if (CollectionUtils.isNotEmpty(ports)) {
				for (ServicePort servicePort : ports) {
					if (servicePort.getName().equals("api")) {
						port = servicePort.getNodePort();
					}
				}
			}
			PodList podList = client.getLabelSelectorPods(service.getSpec().getSelector());
			String hostIp = new String();
			;
			if (podList != null) {
				List<Pod> pods = podList.getItems();
				if (CollectionUtils.isNotEmpty(pods)) {
					for (Pod pod : pods) {
						hostIp = pod.getStatus().getHostIP();
					}
				}
			}
			url = "http://" + hostIp + ":" + port;
		}
        return InfluxDBFactory.connect(url, username, password);
    }


    /**
     *
     * Description:
     * 根据后台收集的数据 按时间等分 用来生成X轴坐标信息
     * @param influxDB InfluxDB
     * @param timePeriod String
     * @return result
     * @see InfluxDB
     */
    public List<String> generateXValue(InfluxDB influxDB, String timePeriod) {
        List<String> result = new ArrayList<String>();
        MonitorController monCon = new MonitorController();
        List<String> lst = monCon.getXValue(influxDB, dbName, timePeriod);
        for (String strDate : lst) {
            // 加8小时
            Date comStrDate = DateUtils.dateCompute(DateUtils.formatStringToDate(strDate), "hour", 8);
            // Date转为String
            String comDateDate = DateUtils.formatDateToString(comStrDate, DateUtils.YYYY_MM_DD_HH_MM_SS);
            result.add(comDateDate);
        }
        return result;
    }

    /**
     *
     * Description:
     * 获得集群概要信息
     * 包含mem、cpu、disk、network
     * @param influxDB InfluxDB
     * @param timePeriod String
     * @return catalogResource CatalogResource
     * @see InfluxDB
     * @see CatalogResource
     */
    public CatalogResource generateYValueOfCluster(InfluxDB influxDB, String timePeriod) {
        MonitorController monCon = new MonitorController();
        // memory
        List<DetailInfo> memDetails = new ArrayList<DetailInfo>();
        DetailInfo limitDetailInfo = new DetailInfo();
        limitDetailInfo.setLegendName(Kind.LIMITCURRENT.toString());
        limitDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMLIMITOVERALL.toString(), ""));
        memDetails.add(limitDetailInfo);

        DetailInfo usageDetailInfo = new DetailInfo();
        usageDetailInfo.setLegendName(Kind.USAGECURRENT.toString());
        usageDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMUSEOVERALL.toString(), ""));
        memDetails.add(usageDetailInfo);

        DetailInfo workDetailInfo = new DetailInfo();
        workDetailInfo.setLegendName(Kind.WORKINGSETCURRENT.toString());
        workDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMSETOVERALL.toString(), ""));
        memDetails.add(workDetailInfo);

        List<Collectivity> memCollectivitys = new ArrayList<Collectivity>();

        Collectivity memCollectivity = new Collectivity();
        memCollectivity.setTitle(Kind.OVERALLCLUSTERMEMORYUSAGE.toString());
        memCollectivity.setVal(memDetails);
        memCollectivitys.add(memCollectivity);

        Collectivity memNodeCollectivity = new Collectivity();
        memNodeCollectivity.setTitle(Kind.MEMORYUSAGEGROUPBYNODE.toString());
        memNodeCollectivity.setVal(new ArrayList<DetailInfo>());
        memCollectivitys.add(memNodeCollectivity);

        List<DetailResource> clusterDetailResources = new ArrayList<DetailResource>();

        DetailResource memDetailResource = new DetailResource();
        memDetailResource.setTitleText(Kind.MEMORY.toString());
        memDetailResource.setVal(memCollectivitys);
        clusterDetailResources.add(memDetailResource);


        // CPU
        List<Collectivity> cpuCollectivitys = new ArrayList<Collectivity>();
        Collectivity cpuNodeCollectivity = new Collectivity();
        cpuNodeCollectivity.setTitle(Kind.CPUUSAGEGROUPBYNODE.toString());
        cpuNodeCollectivity.setVal(new ArrayList<DetailInfo>());
        cpuCollectivitys.add(cpuNodeCollectivity);

        DetailResource cpuDetailResource = new DetailResource();
        cpuDetailResource.setTitleText(Kind.CPU.toString());
        cpuDetailResource.setVal(cpuCollectivitys);

        clusterDetailResources.add(cpuDetailResource);


        //DISK
        List<DetailInfo> diskDetailInfos = new ArrayList<DetailInfo>();
        DetailInfo diskLimitDetailInfo = new DetailInfo();
        diskLimitDetailInfo.setLegendName(Kind.LIMITCURRENT.toString());
        diskLimitDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETDISKLIMITOVERALL.toString(), ""));
        diskDetailInfos.add(diskLimitDetailInfo);

        DetailInfo diskUsageDetailInfo = new DetailInfo();
        diskUsageDetailInfo.setLegendName(Kind.USAGECURRENT.toString());
        diskUsageDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETDISKUSEOVERALL.toString(), ""));
        diskDetailInfos.add(diskUsageDetailInfo);

        List<Collectivity> diskCollectivitys = new ArrayList<Collectivity>();
        Collectivity diskCollectivity = new Collectivity();
        diskCollectivity.setTitle(Kind.OVERALLCLUSTERDISKUSAGE.toString());
        diskCollectivity.setVal(diskDetailInfos);
        diskCollectivitys.add(diskCollectivity);

        Collectivity diskNodeCollectivity = new Collectivity();
        diskNodeCollectivity.setTitle(Kind.DISKUSAGEGROUPBYNODE.toString());
        diskNodeCollectivity.setVal(new ArrayList<DetailInfo>());
        diskCollectivitys.add(diskNodeCollectivity);

        DetailResource diskDetailResource = new DetailResource();
        diskDetailResource.setTitleText(Kind.DISK.toString());
        diskDetailResource.setVal(diskCollectivitys);
        clusterDetailResources.add(diskDetailResource);


        //NETWORK
        List<Collectivity> networkCollectivitys = new ArrayList<Collectivity>();
        Collectivity networkCollectivity = new Collectivity();
        networkCollectivity.setTitle(Kind.DISKUSAGEGROUPBYNODE.toString());
        networkCollectivity.setVal(new ArrayList<DetailInfo>());
        networkCollectivitys.add(networkCollectivity);

        DetailResource networkDetailResource = new DetailResource();
        networkDetailResource.setTitleText(Kind.NETWORK.toString());
        networkDetailResource.setVal(networkCollectivitys);
        clusterDetailResources.add(networkDetailResource);

        // cluster
        CatalogResource catalogResource = new CatalogResource();
        catalogResource.setName(Kind.CLUSTER.toString());
        catalogResource.setVal(clusterDetailResources);
        return catalogResource;
    }

    /**
     *
     * Description:
     * 生成Y轴坐标节点信息
     * @param influxDB
     * @param timePeriod
     * @return
     * @see
     */
    public CatalogResource generateYValueOfMinmon(InfluxDB influxDB, String timePeriod) {
        MonitorController monCon = new MonitorController();
        CatalogResource minmonCatalogResource = new CatalogResource();
        KubernetesAPIClientInterface client = kubernetesClientService.getClient();
        NodeList nodeLst = client.getAllNodes();
        if (null != nodeLst) {
            minmonCatalogResource.setName(Kind.MINMON.toString());

            List<DetailResource> minmonDetailResources = new ArrayList<DetailResource>();
            // 循环处理minion的监控信息
            for (Node minionItem : nodeLst.getItems()) {
                // 子节点名称
                String minionName = minionItem.getMetadata().getName();
                // 子节点类型
                String minionType= "";
                String minionType0 = minionItem.getStatus().getConditions().get(0).getType();
                String minionType1 = minionItem.getStatus().getConditions().get(1).getType();
                if ("Ready".equals(minionType0) || "Ready".equals(minionType1)){
                    minionType= "Ready";
                }
                // 子节点状态
                String minionStatus = "";
                String minionStatus0 = minionItem.getStatus().getConditions().get(0).getStatus();
                String minionStatus1 = minionItem.getStatus().getConditions().get(1).getStatus();
                if ("True".equals(minionStatus0) || "True".equals(minionStatus1)){
                    minionStatus= "True";
                }

                // 判断节点非master,type为Ready,status为True
                if (!"127.0.0.1".equals(minionName) && "Ready".equals(minionType) && "True".equals(minionStatus)) {
                    List<Collectivity> minmonCollectivitys = new ArrayList<Collectivity>();

                    // memory
                    List<DetailInfo> memDetailInfos = new ArrayList<DetailInfo>();

                    DetailInfo memLimitCurrent = new DetailInfo();
                    memLimitCurrent.setLegendName(Kind.LIMITCURRENT.toString());
                    memLimitCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMLIMITMINION.toString(), minionName));
                    memDetailInfos.add(memLimitCurrent);

                    DetailInfo memUsageCurrent = new DetailInfo();
                    memUsageCurrent.setLegendName(Kind.USAGECURRENT.toString());
                    memUsageCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMUSEMINION.toString(), minionName));
                    memDetailInfos.add(memUsageCurrent);

                    DetailInfo memWorkingSetCurrent = new DetailInfo();
                    memWorkingSetCurrent.setLegendName(Kind.WORKINGSETCURRENT.toString());
                    memWorkingSetCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMSETMINION.toString(), minionName));
                    memDetailInfos.add(memWorkingSetCurrent);

                    Collectivity memcollectivity = new Collectivity();
                    memcollectivity.setTitle(Kind.MEMORY.toString());
                    memcollectivity.setVal(memDetailInfos);

                    minmonCollectivitys.add(memcollectivity);


                    // CPU
                    List<DetailInfo> cpuDetailInfos = new ArrayList<DetailInfo>();

                    DetailInfo cpuLimitCurrent = new DetailInfo();
                    cpuLimitCurrent.setLegendName(Kind.LIMITCURRENT.toString());
                    cpuLimitCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETCPULIMITMINION.toString(), minionName));
                    cpuDetailInfos.add(cpuLimitCurrent);

                    DetailInfo cpuUsageCurrent = new DetailInfo();
                    cpuUsageCurrent.setLegendName(Kind.USAGECURRENT.toString());
                    cpuUsageCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETCPUUSEMINION.toString(), minionName));
                    cpuDetailInfos.add(cpuUsageCurrent);

                    Collectivity cpuCollectivity = new Collectivity();
                    cpuCollectivity.setTitle(Kind.LOWERCPU.toString());
                    cpuCollectivity.setVal(cpuDetailInfos);

                    minmonCollectivitys.add(cpuCollectivity);


                    // DISK
                    List<DetailInfo> diskDetailInfos = new ArrayList<DetailInfo>();

                    DetailInfo diskLimitCurrent = new DetailInfo();
                    diskLimitCurrent.setLegendName(Kind.LIMITCURRENT.toString());
                    diskLimitCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETDISKLIMITMINION.toString(), minionName));
                    diskDetailInfos.add(diskLimitCurrent);

                    DetailInfo diskUsageCurrent = new DetailInfo();
                    diskUsageCurrent.setLegendName(Kind.USAGECURRENT.toString());
                    diskUsageCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETDISKUSEMINION.toString(), minionName));
                    diskDetailInfos.add(diskUsageCurrent);

                    Collectivity diskCollectivity = new Collectivity();
                    diskCollectivity.setTitle(Kind.LOWERDISK.toString());
                    diskCollectivity.setVal(diskDetailInfos);

                    minmonCollectivitys.add(diskCollectivity);


                    // NETWORK
                    List<DetailInfo> networkDetailInfos = new ArrayList<DetailInfo>();

                    DetailInfo networkTxCurrent = new DetailInfo();
                    networkTxCurrent.setLegendName(Kind.TXCURRENT.toString());
                    networkTxCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETTXMINION.toString(), minionName));
                    networkDetailInfos.add(networkTxCurrent);

                    DetailInfo networkRxCurrent = new DetailInfo();
                    networkRxCurrent.setLegendName(Kind.RXCURRENT.toString());
                    networkRxCurrent.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETRXMINION.toString(), minionName));
                    networkDetailInfos.add(networkRxCurrent);

                    Collectivity networkCollectivity = new Collectivity();
                    networkCollectivity.setTitle(Kind.LOWERNETWORK.toString());
                    networkCollectivity.setVal(networkDetailInfos);

                    minmonCollectivitys.add(networkCollectivity);

                    DetailResource minmonDetailResource = new DetailResource();
                    minmonDetailResource.setTitleText(minionName);
                    minmonDetailResource.setVal(minmonCollectivitys);

                    minmonDetailResources.add(minmonDetailResource);
                }
            }
            minmonCatalogResource.setVal(minmonDetailResources);
        }
        return minmonCatalogResource;
    }


    /**
     * Description:
     * 获取Pod中容器资源使用的监控信息
     * @param influxDB InfluxDB
     * @param timePeriod String
     * @param podName String
     * @param nameSpace String
     * @return containerCatalogResources List<CatalogResource>
     * @see CatalogResource
     */
    public List<CatalogResource> generateContainerMonitorYValue(InfluxDB influxDB,String timePeriod,
                                                                            String nameSpaceParam, String podNameParam) {
        List<CatalogResource> containerCatalogResources = new ArrayList<CatalogResource>();
        MonitorController monCon = new MonitorController();

        if (StringUtils.isBlank(nameSpaceParam) && StringUtils.isBlank(podNameParam)) { // 查询所有的pod容器
            LOG.info("查询所有pod的container监控信息.");
            KubernetesAPIClientInterface client = kubernetesClientService.getClient();
            NamespaceList namespaceLst = client.getAllNamespaces();
            if (null != namespaceLst) {
                // 循环处理每个NAMESPACE
                for (Namespace namespace : namespaceLst) {
                    String namespaceName = namespace.getMetadata().getName();
                    KubernetesAPIClientInterface clientNamespace = kubernetesClientService.getClient(namespaceName);
                    PodList podLst = clientNamespace.getAllPods();
                    if (null != podLst) {
                        for (Pod pod : podLst) {
                            CatalogResource podCatalogResource = new CatalogResource();

                            String indexPodName = pod.getMetadata().getName();
                            podCatalogResource.setName(indexPodName);

                            List<DetailResource> containersOfPod= new ArrayList<DetailResource>();
                            List<String> containerNameLst = monCon.getAllContainerName(influxDB, dbName, namespaceName, indexPodName);
                            // 如果集群还未收集到container的监控信息，则使用k8s集群中的容器名字
                            if (CollectionUtils.isEmpty(containerNameLst)) {
                                List<Container> containers = pod.getSpec().getContainers();
                                if (CollectionUtils.isNotEmpty(containers)) {
                                    for (Container container : containers) {
                                        containerNameLst.add(container.getName());
                                    }
                                }
                            }
                            if (CollectionUtils.isNotEmpty(containerNameLst)) {
                                for (String containerName : containerNameLst) {
                                    DetailResource detailResource = generateContainerMonitorInfo(influxDB, timePeriod, namespaceName, indexPodName,containerName);
                                    containersOfPod.add(detailResource);
                                }
                            }
                            podCatalogResource.setVal(containersOfPod);
                            containerCatalogResources.add(podCatalogResource);
                        }
                    }
                }
            }
        }
        else if (StringUtils.isNotBlank(nameSpaceParam) && StringUtils.isBlank(podNameParam)) { // 查询指定命名空间下的pod监控数据
            KubernetesAPIClientInterface clientNamespace = kubernetesClientService.getClient(nameSpaceParam);
            PodList podLst = clientNamespace.getAllPods();
            if (null != podLst) {
                for (Pod pod : podLst) {
                    CatalogResource podCatalogResource = new CatalogResource();

                    String indexPodName = pod.getMetadata().getName();
                    podCatalogResource.setName(indexPodName);

                    List<DetailResource> containersOfPod= new ArrayList<DetailResource>();
                    List<String> containerNameLst = monCon.getAllContainerName(influxDB, dbName, nameSpaceParam, indexPodName);
                    // 如果集群还未收集到container的监控信息，则使用k8s集群中的容器名字
                    if (CollectionUtils.isEmpty(containerNameLst)) {
                        List<Container> containers = pod.getSpec().getContainers();
                        if (CollectionUtils.isNotEmpty(containers)) {
                            for (Container container : containers) {
                                containerNameLst.add(container.getName());
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(containerNameLst)) {
                        for (String containerName : containerNameLst) {
                            DetailResource detailResource = generateContainerMonitorInfo(influxDB, timePeriod, nameSpaceParam, indexPodName,containerName);
                            containersOfPod.add(detailResource);
                        }
                    }
                    podCatalogResource.setVal(containersOfPod);
                    containerCatalogResources.add(podCatalogResource);
                }
            }
        } else if (StringUtils.isNotBlank(nameSpaceParam) && StringUtils.isNotBlank(podNameParam)) {  //以namespace和podName过滤
            KubernetesAPIClientInterface clientNamespace = kubernetesClientService.getClient(nameSpaceParam);
            CatalogResource podCatalogResource = new CatalogResource();
            podCatalogResource.setName(podNameParam);
            List<DetailResource> containersOfPod= new ArrayList<DetailResource>();
            List<String> containerNameLst = monCon.getAllContainerName(influxDB, dbName, nameSpaceParam, podNameParam);
            // 如果集群还未收集到container的监控信息，则使用k8s集群中的容器名字
            if (CollectionUtils.isEmpty(containerNameLst)) {
                Pod pod = clientNamespace.getPod(podNameParam);
                List<Container> containers = pod.getSpec().getContainers();
                if (CollectionUtils.isNotEmpty(containers)) {
                    for (Container container : containers) {
                        containerNameLst.add(container.getName());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(containerNameLst)) {
                for (String containerName : containerNameLst) {
                    DetailResource detailResource = generateContainerMonitorInfo(influxDB, timePeriod, nameSpaceParam, podNameParam,containerName);
                    containersOfPod.add(detailResource);
                }
            }
            podCatalogResource.setVal(containersOfPod);
            containerCatalogResources.add(podCatalogResource);
        }
        return containerCatalogResources;
    }

    /**
     *
     * Description:
     * 获取指定命名空间和pod，以及容器的监控信息
     * @param influxDB InfluxDB
     * @param timePeriod String
     * @param nameSpace String
     * @param podName String
     * @param containerName String
     * @return containerDetailResource DetailResource
     * @see InfluxDB
     * @see DetailResource
     */
    public DetailResource generateContainerMonitorInfo(InfluxDB influxDB,String timePeriod,
			String nameSpace, String podName, String containerName) {
		DetailResource containerDetailResource = new DetailResource();
		containerDetailResource.setTitleText(containerName);

		MonitorController monCon = new MonitorController();

		// memory
		List<DetailInfo> memDetailInfos = new ArrayList<DetailInfo>();
		DetailInfo memLimitCurrent = new DetailInfo();
		memLimitCurrent.setLegendName(Kind.MEMORYLIMITCURRENT.toString());
		memLimitCurrent.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod, Kind.GETMEMLIMIT.toString(),
				nameSpace, podName, containerName));
		memDetailInfos.add(memLimitCurrent);

		DetailInfo memUsageCurrent = new DetailInfo();
		memUsageCurrent.setLegendName(Kind.MEMORYUSAGECURRENT.toString());
		memUsageCurrent.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod, Kind.GETMEMUSE.toString(),
				nameSpace, podName, containerName));
		memDetailInfos.add(memUsageCurrent);

		DetailInfo memWorkSet = new DetailInfo();
		memWorkSet.setLegendName(Kind.MEMORYWORKINGSETCURRENT.toString());
		memWorkSet.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod, Kind.GETMEMSET.toString(), nameSpace,
				podName, containerName));
		memDetailInfos.add(memWorkSet);

		Collectivity memCollectivity = new Collectivity();
		memCollectivity.setTitle(Kind.MEMORY.toString());
		memCollectivity.setVal(memDetailInfos);

		// CPU
		List<DetailInfo> cpuDetailInfos = new ArrayList<DetailInfo>();
		DetailInfo cpuLimitCurrent = new DetailInfo();
		cpuLimitCurrent.setLegendName(Kind.CPULIMITCURRENT.toString());
		cpuLimitCurrent.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod, Kind.GETCPULIMIT.toString(),
				nameSpace, podName, containerName));
		cpuDetailInfos.add(cpuLimitCurrent);

		DetailInfo cpuUsageCurrent = new DetailInfo();
		cpuUsageCurrent.setLegendName(Kind.CPUUSAGECURRENT.toString());
		cpuUsageCurrent.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod, Kind.GETCPUUSE.toString(),
				nameSpace, podName, containerName));
		cpuDetailInfos.add(cpuUsageCurrent);

		Collectivity cpuCollectivity = new Collectivity();
		cpuCollectivity.setTitle(Kind.LOWERCPU.toString());
		cpuCollectivity.setVal(cpuDetailInfos);

		// Sockets
		List<DetailInfo> socketsDetailInfos = new ArrayList<DetailInfo>();

		DetailInfo socketsUsageCurrent = new DetailInfo();
		socketsUsageCurrent.setLegendName(Kind.SOCKETSUSAGECURRENT.toString());
		socketsUsageCurrent.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod,
				Kind.GETSOCKETSUSE.toString(), nameSpace, podName, containerName));
		socketsDetailInfos.add(socketsUsageCurrent);

		Collectivity socketsCollectivity = new Collectivity();
		socketsCollectivity.setTitle(Kind.LOWERSOCKETS.toString());
		socketsCollectivity.setVal(socketsDetailInfos);

		// Threads
		List<DetailInfo> threadsDetailInfos = new ArrayList<DetailInfo>();

		DetailInfo threadsUsageCurrent = new DetailInfo();
		threadsUsageCurrent.setLegendName(Kind.THREADSUSAGECURRENT.toString());
		threadsUsageCurrent.setyAxis(monCon.getContainerData(influxDB, dbName, timePeriod,
				Kind.GETTHREADSUSE.toString(), nameSpace, podName, containerName));
		threadsDetailInfos.add(threadsUsageCurrent);

		Collectivity threadsCollectivity = new Collectivity();
		threadsCollectivity.setTitle(Kind.LOWERTHREADS.toString());
		threadsCollectivity.setVal(threadsDetailInfos);

		// 结果队列
		List<Collectivity> containerCollectivity = new ArrayList<Collectivity>();
		containerCollectivity.add(memCollectivity);
		containerCollectivity.add(cpuCollectivity);
		containerCollectivity.add(socketsCollectivity);
		containerCollectivity.add(threadsCollectivity);

		containerDetailResource.setVal(containerCollectivity);
		return containerDetailResource;

	}
}
