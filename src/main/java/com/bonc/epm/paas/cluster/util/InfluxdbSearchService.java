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

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
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
import com.bonc.epm.paas.kubernetes.model.Node;
import com.bonc.epm.paas.kubernetes.model.NodeList;
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
    
    public InfluxDB getInfluxdbClient() {
        return InfluxDBFactory.connect(url, username, password);
    }
    

    /**
     * 
     * Description:
     * 生成X轴坐标信息
     * @param influxDB
     * @param timePeriod
     * @return 
     * @see
     */
    public List<String> generateXValue(InfluxDB influxDB, String timePeriod) {
        List<String> result = new ArrayList<String>();
        MonitorController monCon = new MonitorController();
        List<String> lst = monCon.getXValue(influxDB, dbName, timePeriod);
        for (int i = 0; i < lst.size(); i++) {
            String strDate = lst.get(i);
            // String转为Date
            Date dateDate = DateUtils.formatStringToDate(strDate);
            // 加8小时
            Date comStrDate = DateUtils.dateCompute(dateDate, "hour", 8);
            // Date转为String
            String comDateDate = DateUtils.formatDateToString(comStrDate, DateUtils.YYYY_MM_DD_HH_MM_SS);
            result.add(comDateDate);
        }
        return result;
    }


    /**
     * 
     * Description:
     * 生成Y轴坐标集群信息
     * @param influxDB
     * @param timePeriod
     * @return 
     * @see
     */
    public CatalogResource generateYValueOfCluster(InfluxDB influxDB, String timePeriod) {
        MonitorController monCon = new MonitorController();
        
        // memory
        List<DetailInfo> memDetails = new ArrayList<DetailInfo>();
        DetailInfo limitDetailInfo = new DetailInfo();
        limitDetailInfo.setLegendName(Kind.LIMITCURRENT.name());
        limitDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMLIMITOVERALL.name(), ""));
        memDetails.add(limitDetailInfo);
        
        DetailInfo usageDetailInfo = new DetailInfo();
        usageDetailInfo.setLegendName(Kind.USAGECURRENT.name());
        usageDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMUSEOVERALL.name(), ""));
        memDetails.add(usageDetailInfo);
        
        DetailInfo workDetailInfo = new DetailInfo();
        workDetailInfo.setLegendName(Kind.WORKINGSETCURRENT.name());
        usageDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETMEMSETOVERALL.name(), ""));
        memDetails.add(workDetailInfo);
        
        List<Collectivity> memCollectivitys = new ArrayList<Collectivity>();
        
        Collectivity memCollectivity = new Collectivity();
        memCollectivity.setTitle(Kind.OVERALLCLUSTERMEMORYUSAGE.name());
        memCollectivity.setVal(memDetails);
        memCollectivitys.add(memCollectivity);
        
        Collectivity memNodeCollectivity = new Collectivity();
        memNodeCollectivity.setTitle(Kind.MEMORYUSAGEGROUPBYNODE.name());
        memCollectivitys.add(memNodeCollectivity);
        
        
        List<DetailResource> clusterDetailResources = new ArrayList<DetailResource>();
        
        DetailResource memDetailResource = new DetailResource();
        memDetailResource.setTitleText(Kind.MEMORY.name());
        memDetailResource.setVal(memCollectivitys);
        clusterDetailResources.add(memDetailResource);
        
        
        // CPU
        List<Collectivity> cpuCollectivitys = new ArrayList<Collectivity>();
        Collectivity cpuNodeCollectivity = new Collectivity();
        cpuNodeCollectivity.setTitle(Kind.CPUUSAGEGROUPBYNODE.name());
        cpuCollectivitys.add(cpuNodeCollectivity);
        
        DetailResource cpuDetailResource = new DetailResource();
        cpuDetailResource.setTitleText(Kind.CPU.name());
        cpuDetailResource.setVal(cpuCollectivitys);
        
        clusterDetailResources.add(cpuDetailResource);
        
        //DISK
        List<DetailInfo> diskDetailInfos = new ArrayList<DetailInfo>();
        DetailInfo diskLimitDetailInfo = new DetailInfo();
        diskLimitDetailInfo.setLegendName(Kind.LIMITCURRENT.name());
        diskLimitDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETDISKLIMITOVERALL.name(), ""));
        diskDetailInfos.add(diskLimitDetailInfo);
        
        DetailInfo diskUsageDetailInfo = new DetailInfo();
        diskLimitDetailInfo.setLegendName(Kind.USAGECURRENT.name());
        diskLimitDetailInfo.setyAxis(monCon.getClusterData(influxDB, dbName, timePeriod, Kind.GETDISKUSEOVERALL.name(), ""));
        diskDetailInfos.add(diskUsageDetailInfo);
        
        
        List<Collectivity> diskCollectivitys = new ArrayList<Collectivity>();
        Collectivity diskCollectivity = new Collectivity();
        diskCollectivity.setTitle(Kind.OVERALLCLUSTERDISKUSAGE.name());
        diskCollectivity.setVal(diskDetailInfos);
        diskCollectivitys.add(diskCollectivity);
        
        Collectivity diskNodeCollectivity = new Collectivity();
        diskNodeCollectivity.setTitle(Kind.DISKUSAGEGROUPBYNODE.name());
        diskCollectivitys.add(diskNodeCollectivity);
        
        DetailResource diskDetailResource = new DetailResource();
        diskDetailResource.setTitleText(Kind.DISK.name());
        diskDetailResource.setVal(diskCollectivitys);
        clusterDetailResources.add(diskDetailResource);
        
        
        //NETWORK
        List<Collectivity> networkCollectivitys = new ArrayList<Collectivity>();
        Collectivity networkCollectivity = new Collectivity();
        networkCollectivity.setTitle(Kind.DISKUSAGEGROUPBYNODE.name());
        networkCollectivitys.add(networkCollectivity);
        
        DetailResource networkDetailResource = new DetailResource();
        networkDetailResource.setTitleText(Kind.NETWORK.name());
        networkDetailResource.setVal(networkCollectivitys);
        clusterDetailResources.add(networkDetailResource);
        
        // cluster
        CatalogResource catalogResource = new CatalogResource();
        catalogResource.setName(Kind.CLUSTER.name());
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
        
        
        
        // 创建client
        KubernetesAPIClientInterface client = kubernetesClientService.getClient();
        
        // 取得所有node
        NodeList nodeLst = client.getAllNodes();
        if (null != nodeLst) {
            // 循环处理minion的监控信息
            for (int i = 0; i < nodeLst.size(); i++) {
                Node minionItem = nodeLst.getItems().get(i);
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
                    
                }
                
                
                
                
                
            }
        }
        
        
        
        CatalogResource catalogResource = new CatalogResource();
        return catalogResource;
        
    }
    
}
