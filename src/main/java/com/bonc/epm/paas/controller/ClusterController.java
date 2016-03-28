package com.bonc.epm.paas.controller;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Series;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.bonc.epm.paas.entity.ContainerUse;
import com.bonc.epm.paas.dao.ClusterDao;
import com.bonc.epm.paas.entity.Cluster;
import com.bonc.epm.paas.entity.ClusterUse;
import com.bonc.epm.paas.util.SshConnect;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {
    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    private ClusterDao clusterDao;

    @Value("${yumConf.io.address}")
    private String yumSource;

    @Value("${cc.url}")
    private String url;
    @Value("${cc.username}")
    private String username;
    @Value("${cc.password}")
    private String password;
    @Value("${cc.dbName}")
    private String dbName;
    
    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    public String resourceCluster(Model model) {
    	
//    	List<Cluster> clusterlst=(List<Cluster>) clusterDao.findAll();
//    	Cluster cluster=null;
//    	JSONObject json=null;
//    	for(int i=0;i<clusterlst.size();i++){
//    		cluster=clusterlst.get(i);
//    		ClusterUse clusterUse=this.getClusterUse(cluster.getHost());
//    		json=new JSONObject(clusterUse);
//    		model.addAttribute("clusterUse"+i,json);
//    	}
//    	
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster.jsp";
    }
    
    @RequestMapping(value = {"/containers"}, method = RequestMethod.GET)
    public String resourceContainers(Model model) {
    	/**
    	 * containers.jsp
    	 */
    	
    	
        model.addAttribute("menu_flag", "containers");
        return "cluster/containers.jsp";
    }

    @RequestMapping(value = {"/management"}, method = RequestMethod.GET)
    public String clusterList(Model model) {

        List<Cluster> lstClusters = new ArrayList<>();
        for (Cluster cluster : clusterDao.findAll()) {
            lstClusters.add(cluster);
        }
        model.addAttribute("lstClusters", lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-management.jsp";
    }

    @RequestMapping(value = {"/detail"}, method = RequestMethod.GET)
    public String clusterDetail(@RequestParam String hostIps, Model model) {

        List<ClusterUse> lstClustersUse = new ArrayList<>();
        String[] strHostIps = hostIps.split(",");
        for (String hostIp : strHostIps) {
            ClusterUse clusterUse = getClusterUse(hostIp);
            lstClustersUse.add(clusterUse);
        }
        model.addAttribute("lstClustersUse", lstClustersUse);

        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-detail.jsp";
    }

    /**
     * 取得单一主机资源使用情况
     *
     * @param hostIp hostIp
     * @return ClusterUse
     */
	private ClusterUse getClusterUse(String hostIp /*,String timePeriod,String timeGroup*/) {
        ClusterUse clusterUse = null;
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(url,username,password);

            clusterUse = new ClusterUse();
            //取得主机hostName
            String hostName = "minion" + hostIp.split("\\.")[3];
            
            //查询主机cpu使用值
            String sql="SELECT non_negative_derivative(max(value),1u) FROM \"cpu/usage_ns_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s) ";
            Query query_cpu_use = new Query(sql, dbName);
            QueryResult result_cpu_use = influxDB.query(query_cpu_use);
            //取得value列表
            List<List<Object>> result_cpu_values = result_cpu_use.getResults().get(0).getSeries().get(0).getValues();//[[2016-03-07T01:36:20Z, null], [2016-03-07T01:36:30Z, 184.5981786], [2016-03-07T01:36:40Z, null]]
            
            List<String> cpuUseList = new ArrayList<String>(); 
            for(int i=0;i<result_cpu_values.size();i++){
            	if(result_cpu_values.get(i).get(1)!=null){
            		String str=result_cpu_values.get(i).get(1).toString();
            		cpuUseList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
            clusterUse.setCpuUse(cpuUseList);
            
            
            //查询主机的cpuLimit
            Query query_cpu_limit = new Query("SELECT last(\"value\") FROM \"cpu/limit_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_cpu_limit = influxDB.query(query_cpu_limit);
            //取得主机CPU
            List<List<Object>> cpu_limit = result_cpu_limit.getResults().get(0).getSeries().get(0).getValues();//.get(0).get(1).toString();
            List<String> cpuLimitList =new ArrayList<String>();
            for(int i=0;i<cpu_limit.size();i++){
            	if(cpu_limit.get(i).get(1)!=null){
            		String str=cpu_limit.get(i).get(1).toString();
            		cpuLimitList.add(str);
            	}
            }
            clusterUse.setCpuLimit(cpuLimitList);
            
            //查询内存使用量memUse
            Query query_mem_use = new Query("SELECT max(\"value\") FROM \"memory/usage_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_mem_use = influxDB.query(query_mem_use);
            //取得value列表
            List<List<Object>> result_mem_use_values = result_mem_use.getResults().get(0).getSeries().get(0).getValues();
            List<String> memUseList =null;
            for(int i=0;i<result_mem_use_values.size();i++){
            	if(result_mem_use_values.get(i).get(1)!=null){
            		String str=result_mem_use_values.get(i).get(1).toString();
            		memUseList.add(str);
            	}
            }
            clusterUse.setMemUse(memUseList);
            
            //查询内存memWorking Set
            Query query_mem_set = new Query("SELECT max(\"value\") FROM \"memory/working_set_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_mem_set = influxDB.query(query_mem_set);
            //取得value列表
            List<List<Object>> result_mem_set_values = result_mem_set.getResults().get(0).getSeries().get(0).getValues();
            List<String> memSetList=new ArrayList<String>();
            for (int i = 0; i < result_mem_set_values.size(); i++) {
            	if(result_mem_set_values.get(i).get(1)!=null){
            		String str=result_mem_set_values.get(i).get(1).toString();
            		memSetList.add(str);
            	}
			}
            clusterUse.setMemSet(memSetList);
            
            //查询主机内存memLimit
            Query query_mem_limit = new Query("SELECT max(\"value\") FROM \"memory/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_mem_limit = influxDB.query(query_mem_limit);
            //取得主机CPU
            List<List<Object>> mem_limit = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();//.get(0).get(1).toString();
            List<String> memLimitList= new ArrayList<String>();
            for (int i = 0; i < mem_limit.size(); i++) {
            	if(mem_limit.get(i).get(1)!=null){
            		String str=mem_limit.get(i).get(1).toString();
            		memLimitList.add(str);
            	}
			}
            clusterUse.setMemLimit(memLimitList);
            
            //查询disk_use
            Query query_disk_use=new Query("SELECT last(\"value\") FROM \"filesystem/usage_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)",dbName);
            QueryResult result_disk_use = influxDB.query(query_disk_use);
            List<List<Object>> result_disk_values=result_disk_use.getResults().get(0).getSeries().get(0).getValues();
            List<String> diskUseList=new ArrayList<String>();
            for (int i = 0; i < result_disk_values.size(); i++) {
            	if(result_disk_values.get(i).get(1)!=null){
            		String str=result_disk_values.get(i).get(1).toString();
            		diskUseList.add(str);
            	}
			}
            clusterUse.setDiskUse(diskUseList);
            
            //查询disk_limi
            Query query_disk_limit=new Query("SELECT last(\"value\") FROM \"filesystem/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_disk_limit = influxDB.query(query_disk_limit);
            List<List<Object>> disk_limit = result_disk_limit.getResults().get(0).getSeries().get(0).getValues();//.get(0).get(1).toString();
            List<String> diskLimitList=new ArrayList<String>();
            for (int i = 0; i < disk_limit.size(); i++) {
            	if(disk_limit.get(i).get(1)!=null){
            		String str=disk_limit.get(i).get(1).toString();
            		diskLimitList.add(str);
            	}
			}
            clusterUse.setDiskLimit(diskLimitList);
            
            //查询网络上行值tx
            Query query_network_tx=new Query("SELECT non_negative_derivative(max(value),1s) FROM \"network/tx_bytes_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_network_tx = influxDB.query(query_network_tx);
            List<List<Object>> result_network_txs=result_network_tx.getResults().get(0).getSeries().get(0).getValues();
            List<String> networkTxList=new ArrayList<String>();
            for (int i = 0; i < result_network_txs.size(); i++) {
            	if(result_network_txs.get(i).get(1)!=null){
            		String str=result_network_txs.get(i).get(1).toString();
            		networkTxList.add(str.substring(0,str.indexOf(".")+3));
            	}
			}
            clusterUse.setNetworkTx(networkTxList);
            
            //查询网络下行值rx
            Query query_network_rx=new Query("SELECT non_negative_derivative(max(value),1s) FROM \"network/rx_bytes_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\"=\'"+hostName+"\' and time > now() - 5m GROUP BY time(30s)", dbName);
            QueryResult result_network_rx = influxDB.query(query_network_rx);
            List<List<Object>> result_network_rxs=result_network_rx.getResults().get(0).getSeries().get(0).getValues();
            List<String> networkRxList=new ArrayList<String>();
            for (int i = 0; i < result_network_rxs.size(); i++) {
            	if(result_network_rxs.get(i).get(1)!=null){
            		String str=result_network_rxs.get(i).get(1).toString();
            		networkRxList.add(str.substring(0,str.indexOf(".")+3));
            	}
			}
            clusterUse.setNetworkRx(networkRxList);
            
            //设置主机IP
            clusterUse.setHost(hostIp);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }
    
	public Object getClusterUse2(String hostIp){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			InfluxDB influxDB = InfluxDBFactory.connect(url,username,password);
			
			String hostName="minion" + hostIp.split("\\.")[3];
			//overall cluster memory usage:mem_limit
            Query mem_limit = new Query("SELECT sum(\"value\") FROM \"memory/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(3m)", dbName);
            QueryResult result_mem_limit = influxDB.query(mem_limit);
            List<List<Object>> result_mem_limit_list = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memLimitList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_limit_list.size();i++){
            	if(result_mem_limit_list.get(i).get(1)!=null){
            		String str=result_mem_limit_list.get(i).get(1).toString();
            		memLimitList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
            JSONObject json1=new JSONObject();
            json1.put("yAxis", memLimitList);
            JSONArray jsonArr1=new JSONArray();
            jsonArr1.add(0,memLimitList);
			map.put("yAxis", memLimitList);
			
			//overall cluster memory usage:mem_use
			Query mem_use = new Query("SELECT sum(\"value\") FROM \"memory/usage_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(3m)", dbName);
            QueryResult result_mem_use = influxDB.query(mem_use);
            List<List<Object>> result_mem_use_list = result_mem_use.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memUseList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_use_list.size();i++){
            	if(result_mem_use_list.get(i).get(1)!=null){
            		String str=result_mem_use_list.get(i).get(1).toString();
            		memUseList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memUseList);
			
			
			//overall cluster memory usage:mem_workingSet
			Query mem_workingSet = new Query("SELECT sum(\"value\") FROM \"memory/working_set_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(3m)", dbName);
            QueryResult result_mem_workingSet = influxDB.query(mem_workingSet);
            List<List<Object>> result_mem_workingSet_list = result_mem_workingSet.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memWorkingSetList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_workingSet_list.size();i++){
            	if(result_mem_workingSet_list.get(i).get(1)!=null){
            		String str=result_mem_workingSet_list.get(i).get(1).toString();
            		memWorkingSetList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memWorkingSetList);
			
			//memory usage group by node:memory_limit
			Query mem_limit_node = new Query("SELECT max(\"value\") FROM \"memory/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_mem_limit_node = influxDB.query(mem_limit_node);
            List<List<Object>> result_mem_limit_node_list = result_mem_limit_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memLimitNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_limit_node_list.size();i++){
            	if(result_mem_limit_node_list.get(i).get(1)!=null){
            		String str=result_mem_limit_node_list.get(i).get(1).toString();
            		memLimitNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memLimitNodeList);
			
			//memory usage group by node:memory_use
			Query mem_use_node = new Query("SELECT max(\"value\") FROM \"memory/working_set_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_mem_use_node = influxDB.query(mem_use_node);
            List<List<Object>> result_mem_use_node_list = result_mem_use_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memUseNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_use_node_list.size();i++){
            	if(result_mem_use_node_list.get(i).get(1)!=null){
            		String str=result_mem_use_node_list.get(i).get(1).toString();
            		memUseNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memUseNodeList);
			
			//individual node memory usage： mem_limit
			Query mem_limit_ind = new Query("SELECT max(\"value\") FROM \"memory/limit_bytes_gauge\" WHERE \"hostname\" =~ /"+hostName+"/ AND \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_mem_limit_ind = influxDB.query(mem_limit_ind);
            List<List<Object>> result_mem_limit_ind_list = result_mem_limit_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memLimitIndList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_limit_ind_list.size();i++){
            	if(result_mem_limit_ind_list.get(i).get(1)!=null){
            		String str=result_mem_limit_ind_list.get(i).get(1).toString();
            		memLimitIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memLimitIndList);
			
			//individual node memory usage:memUse
			Query mem_use_ind = new Query("SELECT max(\"value\") FROM \"memory/usage_bytes_gauge\" WHERE \"hostname\" =~ /"+hostName+"/ AND \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_mem_use_ind = influxDB.query(mem_use_ind);
            List<List<Object>> result_mem_use_ind_list = result_mem_use_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memUseIndList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_use_ind_list.size();i++){
            	if(result_mem_use_ind_list.get(i).get(1)!=null){
            		String str=result_mem_use_ind_list.get(i).get(1).toString();
            		memUseIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memUseIndList);
			
			//individual node memory usage:memory_working_set
			Query mem_set_ind = new Query("SELECT max(\"value\") FROM \"memory/working_set_bytes_gauge\" WHERE \"hostname\" =~ /"+hostName+"/ AND \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_mem_set_ind = influxDB.query(mem_set_ind);
            List<List<Object>> result_mem_set_ind_list = result_mem_set_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> memSetIndList = new ArrayList<String>(); 
            for(int i=0;i<result_mem_set_ind_list.size();i++){
            	if(result_mem_set_ind_list.get(i).get(1)!=null){
            		String str=result_mem_set_ind_list.get(i).get(1).toString();
            		memSetIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", memSetIndList);
			
			//cpu use group by node:cpu_limit
			Query cpu_limit_node = new Query("SELECT last(\"value\") FROM \"cpu/limit_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_cpu_limit_node = influxDB.query(cpu_limit_node);
            List<List<Object>> result_cpu_limit_node_list = result_cpu_limit_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> cpuLimitNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_cpu_limit_node_list.size();i++){
            	if(result_cpu_limit_node_list.get(i).get(1)!=null){
            		String str=result_cpu_limit_node_list.get(i).get(1).toString();
            		cpuLimitNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", cpuLimitNodeList);
			
			//cpu use group by node:cpu_use
			Query cpu_use_node = new Query("SELECT non_negative_derivative(max(value),1u) FROM \"cpu/usage_ns_cumulative\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_cpu_use_node = influxDB.query(cpu_use_node);
            List<List<Object>> result_cpu_use_node_list = result_cpu_use_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> cpuUseNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_cpu_use_node_list.size();i++){
            	if(result_cpu_use_node_list.get(i).get(1)!=null){
            		String str=result_cpu_use_node_list.get(i).get(1).toString();
            		cpuUseNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", cpuUseNodeList);
			
			//individual node cpu usage:cpu_limit
			Query cpu_limit_ind = new Query("SELECT last(\"value\") FROM \"cpu/limit_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /"+hostName+"/ AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_cpu_limit_ind = influxDB.query(cpu_limit_ind);
            List<List<Object>> result_cpu_limit_ind_list = result_cpu_limit_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> cpuLimitIndList = new ArrayList<String>(); 
            for(int i=0;i<result_cpu_limit_ind_list.size();i++){
            	if(result_cpu_limit_ind_list.get(i).get(1)!=null){
            		String str=result_cpu_limit_ind_list.get(i).get(1).toString();
            		cpuLimitIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", cpuLimitIndList);
			
			//individual node cpu usage:cpu_use
			Query cpu_use_ind = new Query("SELECT non_negative_derivative(max(value),1u) FROM \"cpu/usage_ns_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /"+hostName+"/ AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_cpu_use_ind = influxDB.query(cpu_use_ind);
            List<List<Object>> result_cpu_use_ind_list = result_cpu_use_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> cpuUseIndList = new ArrayList<String>(); 
            for(int i=0;i<result_cpu_use_ind_list.size();i++){
            	if(result_cpu_use_ind_list.get(i).get(1)!=null){
            		String str=result_cpu_use_ind_list.get(i).get(1).toString();
            		cpuUseIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", cpuUseIndList);
			
			//overall cluster disk usage:disk_limit
			Query disk_limit = new Query("SELECT sum(\"value\") FROM \"filesystem/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_disk_limit = influxDB.query(disk_limit);
            List<List<Object>> result_disk_limit_list = result_disk_limit.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> diskLimitList = new ArrayList<String>(); 
            for(int i=0;i<result_disk_limit_list.size();i++){
            	if(result_disk_limit_list.get(i).get(1)!=null){
            		String str=result_disk_limit_list.get(i).get(1).toString();
            		diskLimitList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", diskLimitList);
			
			//overall cluster disk usage:disk_use
			Query disk_use = new Query("SELECT sum(\"value\") FROM \"filesystem/usage_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_disk_use = influxDB.query(disk_use);
            List<List<Object>> result_disk_use_list = result_disk_use.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> diskUseList = new ArrayList<String>(); 
            for(int i=0;i<result_disk_use_list.size();i++){
            	if(result_disk_use_list.get(i).get(1)!=null){
            		String str=result_disk_use_list.get(i).get(1).toString();
            		diskUseList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", diskUseList);
			
			//disk usage group by node:disk_limit
			Query disk_limit_node = new Query("SELECT last(\"value\") FROM \"filesystem/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_disk_limit_node = influxDB.query(disk_limit_node);
            List<List<Object>> result_disk_limit_node_list = result_disk_limit_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> diskLimitNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_disk_limit_node_list.size();i++){
            	if(result_disk_limit_node_list.get(i).get(1)!=null){
            		String str=result_disk_limit_node_list.get(i).get(1).toString();
            		diskLimitNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", diskLimitNodeList);
			
			//disk usage group by node:disk_use
			Query disk_use_node = new Query("SELECT last(\"value\") FROM \"filesystem/usage_bytes_gauge\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_disk_use_node = influxDB.query(disk_use_node);
            List<List<Object>> result_disk_use_node_list = result_disk_use_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> diskUseNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_disk_use_node_list.size();i++){
            	if(result_disk_use_node_list.get(i).get(1)!=null){
            		String str=result_disk_use_node_list.get(i).get(1).toString();
            		diskUseNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", diskUseNodeList);
			
			//individual node disk usage:disk_limit
			Query disk_limit_ind = new Query("SELECT last(\"value\") FROM \"filesystem/limit_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /"+hostName+"/ AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_disk_limit_ind = influxDB.query(disk_limit_ind);
            List<List<Object>> result_disk_limit_ind_list = result_disk_limit_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> diskLimitIndList = new ArrayList<String>(); 
            for(int i=0;i<result_disk_limit_ind_list.size();i++){
            	if(result_disk_limit_ind_list.get(i).get(1)!=null){
            		String str=result_disk_limit_ind_list.get(i).get(1).toString();
            		diskLimitIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", diskLimitIndList);
			
			//individual node disk usage:disk_use
			Query disk_use_ind = new Query("SELECT last(\"value\") FROM \"filesystem/usage_bytes_gauge\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /"+hostName+"/ AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_disk_use_ind = influxDB.query(disk_use_ind);
            List<List<Object>> result_disk_use_ind_list = result_disk_use_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> diskUseIndList = new ArrayList<String>(); 
            for(int i=0;i<result_disk_use_ind_list.size();i++){
            	if(result_disk_use_ind_list.get(i).get(1)!=null){
            		String str=result_disk_use_ind_list.get(i).get(1).toString();
            		diskUseIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", diskUseIndList);
			
			//network usage group by node:tx
			Query network_tx_node = new Query("SELECT non_negative_derivative(max(value),1s) FROM \"network/tx_bytes_cumulative\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_network_tx_node = influxDB.query(network_tx_node);
            List<List<Object>> result_network_tx_node_list = result_network_tx_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> networkTxNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_network_tx_node_list.size();i++){
            	if(result_network_tx_node_list.get(i).get(1)!=null){
            		String str=result_network_tx_node_list.get(i).get(1).toString();
            		networkTxNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", networkTxNodeList);
			
			//network usage group by node:rx
			Query network_rx_node = new Query("SELECT non_negative_derivative(max(value),1s) FROM \"network/rx_bytes_cumulative\" WHERE \"container_name\" = 'machine' AND time > now() - 30m GROUP BY time(30s), \"hostname\"", dbName);
            QueryResult result_network_rx_node = influxDB.query(network_rx_node);
            List<List<Object>> result_network_rx_node_list = result_network_rx_node.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> networkRxNodeList = new ArrayList<String>(); 
            for(int i=0;i<result_network_rx_node_list.size();i++){
            	if(result_network_rx_node_list.get(i).get(1)!=null){
            		String str=result_network_rx_node_list.get(i).get(1).toString();
            		networkRxNodeList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", networkRxNodeList);
			
			
			//individual node network usage:tx
			Query network_tx_ind = new Query("SELECT non_negative_derivative(max(value),1s) FROM \"network/tx_bytes_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /"+hostName+"/ AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_network_tx_ind = influxDB.query(network_tx_ind);
            List<List<Object>> result_network_tx_ind_list = result_network_tx_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> networkTxIndList = new ArrayList<String>(); 
            for(int i=0;i<result_network_tx_ind_list.size();i++){
            	if(result_network_tx_ind_list.get(i).get(1)!=null){
            		String str=result_network_tx_ind_list.get(i).get(1).toString();
            		networkTxIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", networkTxIndList);
			
			//individual node network usage:rx
			Query network_rx_ind = new Query("SELECT non_negative_derivative(max(value),1s) FROM \"network/rx_bytes_cumulative\" WHERE \"container_name\" = 'machine' AND \"hostname\" =~ /"+hostName+"/ AND time > now() - 30m GROUP BY time(30s)", dbName);
            QueryResult result_network_rx_ind = influxDB.query(network_rx_ind);
            List<List<Object>> result_network_rx_ind_list = result_network_rx_ind.getResults().get(0).getSeries().get(0).getValues();
            
            List<String> networkRxIndList = new ArrayList<String>(); 
            for(int i=0;i<result_network_rx_ind_list.size();i++){
            	if(result_network_rx_ind_list.get(i).get(1)!=null){
            		String str=result_network_rx_ind_list.get(i).get(1).toString();
            		networkRxIndList.add(str.substring(0,str.indexOf(".")+3));
            	}
            }
			map.put("yAxis", networkRxIndList);
			
			
			
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		
		return null;
	}
	
	
    /**
     * 根据pod_namespace、pod_name、container_name取得资源使用情况;
     * @param pod_namespace
     * @param pod_name
     * @param container_name
     * @return ContainerUse
     */
	public List<ContainerUse> getContainerUse(String pod_namespace,String pod_name,String container_name,String timePeriod,String timeGroup){
		List<ContainerUse> containerUseList = null;
    	
    	try {
    		InfluxDB influxDB=InfluxDBFactory.connect(url, username, password);
        	
        	String cpuUse_sql="SELECT non_negative_derivative(max(value),1u) FROM \"cpu/usage_ns_cumulative\" WHERE 1=1 ";
        		if(pod_namespace!=null&&!"".equals(pod_namespace)){
        			cpuUse_sql+= "and \"pod_namespace\" = \'"+pod_namespace+"\'";
        		}
        		if(pod_name!=null&&!"".equals(pod_name)){
        			cpuUse_sql+= " AND  \"pod_name\" =  \'"+pod_name+"\'";
        		}
        		if(container_name!=null&&!"".equals(container_name)){
        			cpuUse_sql+= " and \"container_name\" = \'"+container_name+"\'";
        		}
        		cpuUse_sql+= " AND time > now() - 30m GROUP BY  pod_namespace ,pod_name ,container_name,time(30s) fill(null)";
    		Query query_cpu_use = new Query(cpuUse_sql, dbName);
    		QueryResult result_cpu_use = influxDB.query(query_cpu_use);//Returns:  a List of Series which matched the query.
    		//取得series列表
            List<Series> cpu_use_values = result_cpu_use.getResults().get(0).getSeries();//.get(0).getValues();
            
            
            String cpuLimit_sql="SELECT last(\"value\") FROM \"cpu/limit_gauge\" WHERE 1=1 ";
            	if(pod_namespace!=null&&!"".equals(pod_namespace)){
            		cpuLimit_sql+=" and \"pod_namespace\" = \'"+pod_namespace+"\'";
            	}
            	if(pod_name!=null&&!"".equals(pod_name)){
            		cpuLimit_sql+= " AND  \"pod_name\" = \'"+pod_name+"\'";
            	}
            	if(container_name!=null&&!"".equals(container_name)){
            		cpuLimit_sql+= " and \"container_name\" =\'"+container_name+"\'";
            	}	
            	cpuLimit_sql+=" AND time > now() - 30m GROUP BY pod_namespace ,pod_name ,container_name, time(30s) fill(null)";
    		Query query_cpu_limit = new Query(cpuLimit_sql, dbName);
    		QueryResult result_cpu_limit = influxDB.query(query_cpu_limit);
    		List<Series> cpu_limit_values = result_cpu_limit.getResults().get(0).getSeries();//.get(0).getValues().get(0).get(1).toString();
            
    		
    		String memUse_sql = "SELECT max(\"value\") FROM \"memory/usage_bytes_gauge\" WHERE  1=1 ";
    			if (pod_namespace != null && !"".equals(pod_namespace)) {
    				memUse_sql += " and \"pod_namespace\" =\'"+pod_namespace+"\'";
    			}
    			if (pod_name != null && !"".equals(pod_name)) {
    				memUse_sql += " AND  \"pod_name\" = \'"+pod_name+"\'";
    			}
    			if (container_name != null && !"".equals(container_name)) {
    				memUse_sql += " and \"container_name\" =\'"+container_name+"\'";
    			}
    			memUse_sql +=" AND time > now() - 30m GROUP BY  pod_namespace ,pod_name ,container_name,time(30s) fill(null)";
            Query query_mem_use = new Query(memUse_sql, dbName);
            QueryResult result_mem_use = influxDB.query(query_mem_use);
            List<Series> mem_use_values = result_mem_use.getResults().get(0).getSeries();//.get(0).getValues();
            
            
            String memLimit_sql="SELECT last(\"value\") FROM \"memory/limit_bytes_gauge\" WHERE 1=1  ";
    			if(pod_namespace!=null&&!"".equals(pod_namespace)){
    				memLimit_sql+="and \"pod_namespace\" =\'"+pod_namespace+"\'";
    			}
            	if(pod_name!=null&&!"".equals(pod_name)){
            		memLimit_sql+= " AND  \"pod_name\" =\'"+pod_name+"\'";
            	}
            	if(container_name!=null&&!"".equals(container_name)){
            		memLimit_sql+= " and \"container_name\" =\'"+container_name+"\'";
            	}
            	memLimit_sql+= " AND time > now() - 30m GROUP BY pod_namespace ,pod_name ,container_name, time(30s) fill(null)";
            Query query_mem_limit = new Query(memLimit_sql, dbName);
            QueryResult result_mem_limit = influxDB.query(query_mem_limit);
            List<Series> mem_limit_values=result_mem_limit.getResults().get(0).getSeries();
            
            
            String memWorkingSet_sql="SELECT max(\"value\") FROM \"memory/working_set_bytes_gauge\" WHERE  1=1 ";
    			if(pod_namespace!=null&&!"".equals(pod_namespace)){
    				memWorkingSet_sql+=" and \"pod_namespace\" =\'"+pod_namespace+"\'";
    			}
    			if(pod_name!=null&!"".equals(pod_name)){
    				memWorkingSet_sql+= " AND  \"pod_name\" = \'"+pod_name+"\'";
    				
    			}
    			if(container_name!=null&&!"".equals(container_name)){
    				memWorkingSet_sql+= " and \"container_name\" =\'"+container_name+"\'";
    			}
    			memWorkingSet_sql+=" AND time > now() - 30m GROUP BY pod_namespace ,pod_name ,container_name, time(30s) fill(null)";
    		Query query_mem_set = new Query(memWorkingSet_sql, dbName);
    		QueryResult result_mem_set = influxDB.query(query_mem_set);
            List<Series> mem_set_values = result_mem_set.getResults().get(0).getSeries();//.get(0).getValues();
            
            
            List<List<Object>> cpu_use_list=null;
            List<List<Object>> cpu_limit_list=null;
            List<List<Object>> mem_use_list=null;
            List<List<Object>> mem_limit_list=null;
            List<List<Object>> mem_set_list=null;
            for (int i=0;i<cpu_use_values.size();i++){
            	
            	cpu_use_list=cpu_use_values.get(i).getValues();
            	cpu_limit_list=cpu_limit_values.get(i).getValues();
            	mem_use_list=mem_use_values.get(i).getValues();
            	mem_limit_list=mem_limit_values.get(i).getValues();
            	mem_set_list=mem_set_values.get(i).getValues();
            	
            	List<String> cpuUseList = null;
            	List<String> cpuLimitList = null;
            	List<String> memUseList = null;
            	List<String> memLimitList = null;
            	List<String> memSetList = null;
            	for(int j=0;j<cpu_use_values.size();j++){
            		
            		String strCpuUse=cpu_use_list.get(j).get(1).toString();
            		String strCpuLimit=cpu_limit_list.get(j).get(1).toString();
            		String strMemUse=mem_use_list.get(j).get(1).toString();
            		String strMemLimit=mem_limit_list.get(j).get(1).toString();
            		String strMemSet=mem_set_list.get(j).get(1).toString();
            		
            		cpuUseList.add(strCpuUse);
            		cpuLimitList.add(strCpuLimit);
            		memUseList.add(strMemUse);
            		memLimitList.add(strMemLimit);
            		memSetList.add(strMemSet);
            		
            	}
            	ContainerUse containerUse=null;
            	containerUse.setCpuUse(cpuUseList);
            	containerUse.setCpuLimit(cpuLimitList);
            	containerUse.setMemUse(memUseList);
            	containerUse.setMemLimit(memLimitList);
            	containerUse.setMemWorkingSet(memSetList);
            	
            	containerUseList.add(containerUse);
            }
            
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
    	return  containerUseList;
    }
    
    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String clusterAdd() {
        return "cluster/cluster-create.jsp";
    }

    @RequestMapping(value = {"/searchCluster"}, method = RequestMethod.POST)
    public String searchCluster(@RequestParam String searchIP, Model model) {

        List<Cluster> lstClusters = clusterDao.findByHostLike(searchIP);
        model.addAttribute("lstClusters", lstClusters);
        model.addAttribute("menu_flag", "cluster");
        return "cluster/cluster-management.jsp";
    }

    @RequestMapping(value = {"/getClusters"}, method = RequestMethod.POST)
    public String getClusters(@RequestParam String ipRange, Model model) {
        List<String> lstIps = new ArrayList<>();
        List<Cluster> lstClusters = new ArrayList<>();
        List<String> existIps = new ArrayList<>();
        int index = ipRange.indexOf("[");
        if (index != -1) {
            String ipHalf = ipRange.substring(0, index);
            String ipSect = ipRange.substring(index + 1, ipRange.length() - 1);
            if (ipSect.contains("-")) {
                String[] ipsArray = ipSect.split("-");
                int ipStart = Integer.valueOf(ipsArray[0]);
                int ipEnd = Integer.valueOf(ipsArray[1]);
                for (int i = ipStart; i < ipEnd + 1; i++) {
                    lstIps.add(ipHalf + i);
                }
            } else {
                String[] ipsArray = ipSect.split(",");
                for (String ipSon : ipsArray) {
                    lstIps.add(ipHalf + ipSon);
                }
            }
        } else {
            String[] ipsArray = ipRange.split(",");
            for (String ipSon : ipsArray) {
                lstIps.add(ipSon);
            }
        }
        Iterable<Cluster> a = clusterDao.findAll();
        for (Cluster b : a) {
            existIps.add(b.getHost());
        }
        for (String ipSon : lstIps) {
            //增加数据库中是否存在的验证
            if (!existIps.contains(ipSon)) {
                try {
                    Socket socket = new Socket(ipSon, 22);
                    if (socket.isConnected()) {
                        Cluster conCluster = new Cluster();
                        conCluster.setHost(ipSon);
                        conCluster.setPort(22);
                        lstClusters.add(conCluster);
                    }
                } catch (NoRouteToHostException e) {
                    log.error("无法SSH到目标主机:" + ipSon);
                } catch (UnknownHostException e) {
                    log.error("未知主机:" + ipSon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("lstClusters", lstClusters);
        model.addAttribute("ipRange", ipRange);
        return "cluster/cluster-create.jsp";
    }


    @RequestMapping(value = {"/installCluster"}, method = RequestMethod.GET)
    @ResponseBody
    public String installCluster(@RequestParam String user, @RequestParam String pass, @RequestParam String ip,
                                 @RequestParam Integer port, @RequestParam String type) {

        try {
            //拷贝安装脚本
            copyFile(user, pass, ip, port);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return "拷贝安装脚本失败";
        }
        //读取私有仓库地址
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().build();
        String imageHostPort = config.getUsername();
        //ssh连接
        try {
            SshConnect.connect(user, pass, ip, port);
            //获取主机的内存大小
            /*Integer memLimit = 1000000;
            String memCmd = "cat /proc/meminfo | grep MemTotal | awk -F ':' '{print $2}' | awk '{print $1}'";
            String memRtn = SshConnect.exec(memCmd, 1000);
            String[] b = memRtn.split("\n");
            memLimit = Integer.valueOf(b[b.length - 2].trim());*/
            //安装环境
            String masterName = "centos-master";
            String hostName = "centos-minion" + ip.split("\\.")[3];
            String cmd = "cd /opt/;chmod +x ./envInstall.sh;nohup ./envInstall.sh " + imageHostPort + " " + yumSource + " " + type + " " + masterName + " " + hostName;
            Boolean endFlg = false;
            SshConnect.exec(cmd, 10000);
            while (!endFlg) {
                String strRtn = SshConnect.exec("echo $?", 1000);
                endFlg = strRtn.endsWith("#");
            }
            //插入主机数据
            Cluster cluster = clusterDao.findByHost(ip);
            if (cluster == null) {
                Cluster newCluster = new Cluster();
                newCluster.setUsername(user);
                newCluster.setPassword(pass);
                newCluster.setHost(ip);
                newCluster.setPort(port);
                newCluster.setHostType(type);
                clusterDao.save(newCluster);
            }
            String ins_detail = SshConnect.exec("tail -n 5 /opt/nohup.out", 1000);
            return ins_detail.split("nohup.out")[1].trim().split("\\[root")[0].trim();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return "执行command失败";
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return "ssh连接失败";
        } finally {
            //关闭SSH连接
            SshConnect.disconnect();
        }
    }

    private void copyFile(String user, String pass, String ip, Integer port)
            throws IOException, JSchException, InterruptedException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, ip, port);
        session.setPassword(pass);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect(30000);
        ChannelSftp sftpConn = (ChannelSftp) session.openChannel("sftp");
        sftpConn.connect(1000);
        try {
            String lpwdPath = sftpConn.lpwd();
            //创建目录并拷贝文件
            sftpConn.put(lpwdPath + "/src/main/resources/static/bin/envInstall.sh", "/opt/");
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
}
