package com.bonc.epm.paas.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonc.epm.paas.constant.MonitorConstant;

/**
 *MonitorController
 * @author chiwenguang
 * @version 2016年9月6日
 * @see MonitorController
 * @since
 */
public class MonitorController {
	
    /**
     * MonitorController日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(MonitorController.class);
	
    /**
     * influxDB
     */
    private InfluxDB influxDB;
	
    /**
     * 间隔时间
     */
    private String timePeriod;
	
	/**
	 * dbName
	 */
    private String dbName;
	
    /**
     * memDivisor
     */
    private Integer memDivisor = 1024*1024*1024;
	
    /**
     * cpuDivisor
     */
    private Integer cpuDivisor = 1;
	
    /**
     * diskDivisor
     */
    private Integer diskDivisor = 1024*1024*1024;
	
    /**
     * netDivisor
     */
    private Integer netDivisor = 1000;
	
	/**
	 * 根据timePeriod计算timeGroup
	 * @param timePeriod 
	 * @return String
	 */
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
     * @param selCol String
     * @param tabName String
     * @param minionName String
     * @return sql String
     */
    private String joinClusterSQL(String selCol, String tabName, String minionName) {
        //根据查询时间段取得间隔时间
        String timeGroup = getTimeGroup(timePeriod);
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT " + selCol + " FROM " + tabName + " WHERE \"container_name\" = 'machine' ");
        if (StringUtils.isNotBlank(minionName)){
            sqlSb.append(" AND \"hostname\" =~ /" + minionName + "/");
        }
        sqlSb.append(" AND time > now() - " + timePeriod + " GROUP BY time(" + timeGroup + ")");
        return sqlSb.toString();
    }
    
    /**
     * 根据Container条件拼接SQL
     * @param selCol 查询结果应该包含的信息
     * @param tabName 被查询的表名
     * @param namespace 
     * @param podName 
     * @param containerName 
     * @return sqlSb
     */
    private String joinContainerSQL(String selCol, String tabName, String namespace, String podName, String containerName) {
        //根据查询时间段取得间隔时间
        String timeGroup = getTimeGroup(timePeriod);
        //拼SQL
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT " + selCol + " FROM " + tabName + " WHERE 1=1 ");
        
        if (StringUtils.isNotBlank(namespace)) {
            sqlSb.append(" and \"pod_namespace\" =\'" + namespace + "\'");
        }
        if (StringUtils.isNotBlank(podName)) {
            sqlSb.append(" and \"pod_name\" =\'" + podName + "\'");
        }
        if (StringUtils.isNotBlank(containerName)) {
            sqlSb.append(" and \"container_name\" =\'" + containerName + "\'");
        }
        sqlSb.append(" AND time > now() - " + timePeriod + " GROUP BY pod_namespace ,pod_name ,container_name, time(" + timeGroup + ") fill(null)");
        return sqlSb.toString();
    }
    
    /**
     * 查詢INFLUXDB
     * @param sql 
     * @param divisor 
     * @return List
     */
    private List<String> dbSearch(String sql, Integer divisor) {
        Query sqlQuery = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(sqlQuery);
        List<String> listString = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(result_mem_limit.getResults().get(0).getSeries())) {
                List<List<Object>> listObject = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
                for (List<Object> aListObject : listObject) {
                    //判断值是否为null
                    if (aListObject.get(1) != null) {
                        Double ad = (Double) aListObject.get(1)/divisor;
                        String strDouble = ad.toString();
                        BigDecimal bigDecimal = new BigDecimal(strDouble);
                        String str = bigDecimal.toPlainString();
                        //判断小数点位数
                        if (str.length() <= str.indexOf(".") + 3) {
                            listString.add(str);
                        }
                        else {
                            listString.add(str.substring(0, str.indexOf(".") + 3));
                        }
                    } 
                    else {
                        listString.add(null);
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.error("execute sql failed. sql:-"+sqlQuery);
            LOG.error("failed message is :-"+ e.getMessage());
            return listString;
        }
        return listString;
    }
    
    /**
     * 获取指定容器的各项监控数据
     * @param influxDB InfluxDB
     * @param dbName String
     * @param timePeriod String
     * @param dataType String
     * @param namespace String
     * @param podName String
     * @param containerName String
     * @return List
     */
    public List<String> getContainerData(InfluxDB influxDB,String dbName, String timePeriod, 
                                                 String dataType, String namespace, String podName, String containerName){
    	this.influxDB = influxDB;
    	this.timePeriod = timePeriod;
    	this.dbName = dbName;
        switch (dataType) {
            case "getMemLimit":
                //mem_limit
                return dbSearch(joinContainerSQL(MonitorConstant.LAST_VALUE, MonitorConstant.MEMORY_LIMIT, namespace, podName, containerName), memDivisor);
            case "getMemUse":
                //mem_use
                return dbSearch(joinContainerSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_USAGE, namespace, podName, containerName), memDivisor);
            case "getMemSet":
                //mem_set
                return dbSearch(joinContainerSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET, namespace, podName, containerName), memDivisor);
            case "getCpuLimit":
                //cpu_limit
                return dbSearch(joinContainerSQL(MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT, namespace, podName, containerName), cpuDivisor);
            case "getCpuUse":
                //cpu_use
                return dbSearch(joinContainerSQL(MonitorConstant.MAX_VALUE, MonitorConstant.CPU_USAGE, namespace, podName, containerName), cpuDivisor);
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
     * @return List
     */
    public List<String> getXValue(InfluxDB influxDB,String dbName, String timePeriod){
    	this.influxDB = influxDB;
    	this.timePeriod = timePeriod;
    	this.dbName = dbName;
    	List<String> listString = new ArrayList<>();
    	try {
            String sql = joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_LIMIT, "");
            Query sqlQuery = new Query(sql, dbName);
            QueryResult result_mem_limit = influxDB.query(sqlQuery);
            List<List<Object>> listObject = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
            for (List<Object> aListObject : listObject) {
                listString.add(aListObject.get(0).toString().replace("T", " ").replace("Z", ""));
            }
        }
        catch (Exception e) {
            LOG.error("obtain X axis failed. the error message:-"+e.getMessage());
            LOG.info("the reason perhaps is that time isnot enough.");
        }
        return listString;
    }
    
    /**
     * 取得CLUSTER监控数据
     * @param influxDB InfluxDB
     * @param dbName String
     * @param timePeriod String
     * @param dataType String
     * @param minionName String
     * @return str List<String>
     */
    public List<String> getClusterData(InfluxDB influxDB, String dbName, 
                                               String timePeriod, String dataType, String minionName){
    	this.influxDB = influxDB;
    	this.timePeriod = timePeriod;
    	this.dbName = dbName;
    	switch (dataType) {
            case "getMemLimitOverAll":
                //overall cluster memory usage:mem_limit
            	return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_LIMIT, minionName), memDivisor);
            case "getMemUseOverAll":
                //overall cluster memory usage:mem_use
                return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_USAGE, minionName), memDivisor);
            case "getMemSetOverAll":
                //overall cluster memory usage:mem_workingSet
                return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_WORKING_SET, minionName), memDivisor);
        	
            case "getMemLimitMinion":
            	//individual node memory usage： mem_limit
            	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_LIMIT, minionName), memDivisor);
            case "getMemUseMinion":
            	//individual node memory usage:memUse
            	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_USAGE, minionName), memDivisor);
            case "getMemSetMinion":
            	//individual node memory usage:memory_working_set
            	return dbSearch(joinClusterSQL(MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET, minionName), memDivisor);
        	
            case "getCpuLimitMinion":
            	//individual node cpu usage:cpu_limit
            	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT, minionName), cpuDivisor);
            case "getCpuUseMinion":
            	//individual node cpu usage:cpu_use
            	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_U, MonitorConstant.CPU_USAGE, minionName), cpuDivisor);
        	
            case "getDiskLimitOverAll":
            	//overall cluster disk usage:disk_limit
            	return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.FILE_LIMIT, minionName), diskDivisor);
            case "getDiskUseOverAll":
            	//overall cluster disk usage:disk_use
            	return dbSearch(joinClusterSQL(MonitorConstant.SUN_VALUE, MonitorConstant.FILE_USAGE, minionName), diskDivisor);
        	
            case "getDiskLimitMinion":
            	//individual node disk usage:disk_limit
            	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.FILE_LIMIT, minionName), diskDivisor);
            case "getDiskUseMinion":
            	//individual node disk usage:disk_use
            	return dbSearch(joinClusterSQL(MonitorConstant.LAST_VALUE, MonitorConstant.FILE_USAGE, minionName), diskDivisor);
        	
            case "getTxMinion":
            	//individual node network usage:tx
            	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_TX, minionName), netDivisor);
            case "getRxMinion":
            	//individual node network usage:rx
            	return dbSearch(joinClusterSQL(MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_RX, minionName), netDivisor);
            default:
                return new ArrayList<String>(); 
    	}
    }

	/**
	 * 取得指定命名空间和pod下的所有容器
	 * @param influxDB InfluxDB
	 * @param dbName String
	 * @param namespace String
	 * @param podName String 
	 * @return listString List<String>
	 */
    public List<String> getAllContainerName(InfluxDB influxDB, String dbName, String namespace, String podName) {
        List<String> listString = new ArrayList<>();
        String sql = "SELECT  container_name, last(\"value\")  FROM  \"memory/limit_bytes_gauge\"  WHERE 1=1  and "
				+ "\"pod_namespace\" = \'" + namespace + "\'  AND \"pod_name\" = \'" + podName
				+ "\' AND time > now() - 5m GROUP BY pod_namespace ,pod_name ,container_name, time(1m) fill(null)";
        try {
            Query sqlQuery = new Query(sql, dbName);
            QueryResult result_mem_limit = influxDB.query(sqlQuery);
            List<Series> seriesLst = result_mem_limit.getResults().get(0).getSeries();
            for (Series series : seriesLst) {
                List<List<Object>> listObject = series.getValues();
                listString.add(listObject.get(1).get(1).toString());
            }
        } 
        catch (Exception e) {
            LOG.error("get container info failed. namespace:-"+ namespace +";podName:-"+podName+",error message:-"+e.getMessage());
            return new ArrayList<>();
        }
        return listString;
    }
}
