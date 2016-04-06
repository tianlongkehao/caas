package com.bonc.epm.paas.controller;

import com.bonc.epm.paas.constant.MonitorConstant;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiwenguang on 16-3-28.
 */
public class MonitorController {
    
	private InfluxDB influxDB;
	private String timePeriod;
	private String dbName;
	
	//根据timePeriod计算timeGroup
	private String getTimeGroup(String timePeriod){
		String timeGroup;
		switch (timePeriod) {
	        case "5m":
	            timeGroup = "30s";
	            break;
	        case "30m":
	            timeGroup = "2m";
	            break;
	        case "1h":
	            timeGroup = "5m";
	            break;
	        case "6h":
	            timeGroup = "30m";
	            break;
	        case "12h":
	            timeGroup = "1h";
	            break;
	        case "24h":
	            timeGroup = "2h";
	            break;
	        case "7d":
	            timeGroup = "12h";
	            break;
	        case "30d":
	            timeGroup = "3d";
	            break;
	        default:
	            timeGroup = "5s";
		}
		return timeGroup;
	}
    /**
     * 根据Cluster条件拼接SQL
     * 
     * @param selCol
     * @param tabName
     * @param hostWhere
     * @return
     */
    private String joinClusterSQL(String selCol, String tabName) {
        //根据查询时间段取得间隔时间
        String timeGroup = getTimeGroup(timePeriod);
        //拼SQL
        String sql = "SELECT " + selCol + " FROM " + tabName + " WHERE \"container_name\" = 'machine' ";
        
        sql = sql + " AND time > now() - " + timePeriod + " GROUP BY time(" + timeGroup + ")";
        return sql;
    }
    /**
     * 根据Container条件拼接SQL
     * 
     * @param selCol
     * @param tabName
     * @param hostWhere
     * @param namespace
     * @param svcName
     * @param podName
     * @return
     */
    private String joinContainerSQL(String selCol, String tabName, String namespace, String podName, String containerName) {
        //根据查询时间段取得间隔时间
        String timeGroup = getTimeGroup(timePeriod);
        
        //拼SQL
        String sql = "SELECT " + selCol + " FROM " + tabName + " WHERE 1=1 ";
        
        if (namespace != null && !"".equals(namespace)) {
        	sql += " and \"pod_namespace\" =\'" + namespace + "\'";
        }
        if (podName != null && !"".equals(podName)) {
        	sql += " and \"pod_name\" =\'" + podName + "\'";
        }
        if (containerName != null && !"".equals(containerName)) {
        	sql += " and \"container_name\" =\'" + containerName + "\'";
        }
        sql = sql + " AND time > now() - " + timePeriod + " GROUP BY pod_namespace ,pod_name ,container_name, time(" + timeGroup + ") fill(null)";
        return sql;
    }

    private List<String> dbSearch(String sql) {
        Query sqlQuery = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(sqlQuery);
        List<List<Object>> listObject = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
        List<String> listString = new ArrayList<>();
        for (List<Object> aListObject : listObject) {
            //判断值是否为null
        	if (aListObject.get(1) != null) {
                String str = aListObject.get(1).toString();
                //判断小数点位数
                if(str.length() <= str.indexOf(".") + 3){
                	listString.add(str);
                } else {
                    listString.add(str.substring(0, str.indexOf(".") + 3));
                }
            } else {
            	listString.add(null);
            }
        }
        return listString;
    }

    
    /**
     *  取得集群监控数据
     * 
     * @param influxDB
     * @param dbName
     * @param timePeriod
     * @param dataType
     * @return
     */
    public List<String> getContainerData(InfluxDB influxDB,String dbName, String timePeriod, 
    		String dataType, String namespace, String podName, String containerName){
    	this.influxDB = influxDB;
    	this.timePeriod = timePeriod;
    	this.dbName = dbName;
    	switch (dataType) {
        case "getMemLimit":
            //mem_limit
        	return dbSearch(joinContainerSQL(MonitorConstant.LAST_VALUE, MonitorConstant.MEMORY_LIMIT, namespace, podName, containerName));
        case "getMemUse":
            //mem_use
        	return dbSearch(joinContainerSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_USAGE, namespace, podName, containerName));
        case "getMemSet":
            //mem_set
        	return dbSearch(joinContainerSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET, namespace, podName, containerName));
        case "getCpuLimit":
            //cpu_limit
        	return dbSearch(joinContainerSQL(MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT, namespace, podName, containerName));
        case "getCpuUse":
            //cpu_use
        	return dbSearch(joinContainerSQL(MonitorConstant.MAX_VALUE, MonitorConstant.CPU_USAGE, namespace, podName, containerName));
		default:
            return new ArrayList<String>(); 
    	}
    }

    /**
     * 取得X轴横坐标
     * 
     * @param influxDB
     * @param dbName
     * @param timePeriod
     * @return
     */
    public List<String> getXValue(InfluxDB influxDB,String dbName, String timePeriod){
    	this.influxDB = influxDB;
    	this.timePeriod = timePeriod;
    	this.dbName = dbName;
    	String sql = joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_LIMIT);
    	Query sqlQuery = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(sqlQuery);
        List<List<Object>> listObject = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
        List<String> listString = new ArrayList<>();
        for (List<Object> aListObject : listObject) {
                listString.add(aListObject.get(0).toString().replace("T", " ").replace("Z", ""));
        }
        return listString;
    }
    
    /**
     *  取得集群监控数据
     * 
     * @param influxDB
     * @param dbName
     * @param timePeriod
     * @param dataType
     * @return
     */
    public List<String> getClusterData(InfluxDB influxDB,String dbName, String timePeriod, String dataType){
    	this.influxDB = influxDB;
    	this.timePeriod = timePeriod;
    	this.dbName = dbName;
    	switch (dataType) {
        case "getMemLimitOverAll":
            //overall cluster memory usage:mem_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_LIMIT));
        case "getMemUseOverAll":
            //overall cluster memory usage:mem_use
            return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_USAGE));
        case "getMemSetOverAll":
            //overall cluster memory usage:mem_workingSet
            return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_WORKING_SET));
            
        case "getMemLimitNode":
            //memory usage group by node:memory_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_LIMIT));
        case "getMemUseNode":
            //memory usage group by node:memory_use
        	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET));
        	
        case "getMemLimitMinion":
        	//individual node memory usage： mem_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_LIMIT));
        case "getMemUseMinion":
        	//individual node memory usage:memUse
        	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_USAGE));
        case "getMemSetMinion":
        	//individual node memory usage:memory_working_set
        	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET));
        	
        case "getCpuLimitNode":
        	//cpu use group by node:cpu_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT));
        case "getCpuUseNode":
        	//cpu use group by node:cpu_use
        	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_U, MonitorConstant.CPU_USAGE));
        	
        case "getCpuLimitMinion":
        	//individual node cpu usage:cpu_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT));
        case "getCpuUseMinion":
        	//individual node cpu usage:cpu_use
        	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_U, MonitorConstant.CPU_USAGE));
        	
        case "getDiskLimitOverAll":
        	//overall cluster disk usage:disk_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.FILE_LIMIT));
        case "getDiskUseOverAll":
        	//overall cluster disk usage:disk_use
        	return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.FILE_USAGE));
        	
        case "getDiskLimitNode":
        	//disk usage group by node:disk_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.FILE_LIMIT));
        case "getDiskUseNode":
        	//disk usage group by node:disk_use
        	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.FILE_USAGE));
        	
        case "getDiskLimitMinion":
        	//individual node disk usage:disk_limit
        	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.FILE_LIMIT));
        case "getDiskUseMinion":
        	//individual node disk usage:disk_use
        	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.FILE_USAGE));
        	
        case "getTxNode":
        	//network usage group by node:tx
        	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_TX));
        case "getRxNode":
        	//network usage group by node:rx
        	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_RX));
        case "getTxMinion":
        	//individual node network usage:tx
        	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_TX));
        case "getRxMinion":
        	//individual node network usage:rx
        	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_RX));
        	
		default:
            return new ArrayList<String>(); 
    	}
    }
}
