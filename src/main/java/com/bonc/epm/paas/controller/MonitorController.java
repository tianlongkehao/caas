package com.bonc.epm.paas.controller;

import com.bonc.epm.paas.constant.MonitorConstant;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiwenguang on 16-3-28.
 */
public class MonitorController {
    @Value("${monitor.url}")
    private String url;
    @Value("${monitor.username}")
    private String username;
    @Value("${monitor.password}")
    private String password;
    @Value("${monitor.dbName}")
    private String dbName;
    private InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);

    //根据条件拼接SQL
    private String joinSQL(String timePeriod, String selCol, String tabName, String hostWhere) {
        //根据查询时间段取得间隔时间
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
                timePeriod = "5m";
                timeGroup = "5s";
        }
        //拼SQL
        String sql = "SELECT " + selCol + " FROM " + tabName + " WHERE \"container_name\" = 'machine' ";
        if ("".equals(hostWhere)) {
            sql = sql + " ORDER BY length(hostname),hostName ";
        }
        sql = sql + " AND time > now() - " + timePeriod + " GROUP BY time(" + timeGroup + ")";
        return sql;
    }

    private List<String> dbSearch(String sql) {
        Query sqlQuery = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(sqlQuery);
        List<List<Object>> listObject = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
        List<String> listString = new ArrayList<>();
        for (List<Object> aListObject : listObject) {
            if (aListObject.get(1) != null) {
                String str = aListObject.get(1).toString();
                listString.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return listString;
    }

    //overall cluster memory usage:mem_limit
    public List<String> getMemLimitOverAll(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_LIMIT, "");
        return dbSearch(sql);
    }

    //overall cluster memory usage:mem_use
    public List<String> getMemUseOverAll(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_USAGE, "");
        return dbSearch(sql);
    }

    //overall cluster memory usage:mem_workingSet
    public List<String> getMemSetOverAll(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.SUN_VALUE, MonitorConstant.MEMORY_WORKING_SET, "");
        return dbSearch(sql);
    }

    //memory usage group by node:memory_limit
    public List<String> getMemSetNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_LIMIT, "");
        return dbSearch(sql);
    }

    //memory usage group by node:memory_use
    public List<String> getMemUseNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET, "");
        return dbSearch(sql);
    }

    //individual node memory usage： mem_limit
    public List<String> getMemLimitMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_LIMIT, "host");
        return dbSearch(sql);
    }

    //individual node memory usage:memUse
    public List<String> getMemUseMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_USAGE, "host");
        return dbSearch(sql);
    }

    //individual node memory usage:memory_working_set
    public List<String> getMemSetMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.MAX_VALUE, MonitorConstant.MEMORY_WORKING_SET, "host");
        return dbSearch(sql);
    }

    //cpu use group by node:cpu_limit
    public List<String> getCpuLimitNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT, "");
        return dbSearch(sql);
    }

    //cpu use group by node:cpu_use
    public List<String> getCpuUseNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.NEGATIVE_VALUE_U, MonitorConstant.CPU_USAGE, "");
        return dbSearch(sql);
    }

    //individual node cpu usage:cpu_limit
    public List<String> getCpuLimitMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.LAST_VALUE, MonitorConstant.CPU_LIMIT, "host");
        return dbSearch(sql);
    }

    //individual node cpu usage:cpu_use
    public List<String> getCpuUseMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.NEGATIVE_VALUE_U, MonitorConstant.CPU_USAGE, "host");
        return dbSearch(sql);
    }

    //overall cluster disk usage:disk_limit
    public List<String> getDiskLimitOverAll(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.SUN_VALUE, MonitorConstant.FILE_LIMIT, "");
        return dbSearch(sql);
    }

    //overall cluster disk usage:disk_use
    public List<String> getDiskUseOverAll(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.SUN_VALUE, MonitorConstant.FILE_USAGE, "");
        return dbSearch(sql);
    }

    //disk usage group by node:disk_limit
    public List<String> getDiskLimitNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.LAST_VALUE, MonitorConstant.FILE_LIMIT, "");
        return dbSearch(sql);
    }

    //disk usage group by node:disk_use
    public List<String> getDiskUseNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.LAST_VALUE, MonitorConstant.FILE_USAGE, "");
        return dbSearch(sql);
    }

    //individual node disk usage:disk_limit
    public List<String> getDiskLimitMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.LAST_VALUE, MonitorConstant.FILE_LIMIT, "host");
        return dbSearch(sql);
    }

    //individual node disk usage:disk_use
    public List<String> getDiskUseMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.LAST_VALUE, MonitorConstant.FILE_USAGE, "host");
        return dbSearch(sql);
    }

    //network usage group by node:tx
    public List<String> getTxNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_TX, "");
        return dbSearch(sql);
    }

    //network usage group by node:rx
    public List<String> getRxNode(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_RX, "");
        return dbSearch(sql);
    }

    //individual node network usage:tx
    public List<String> getTxMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_TX, "");
        return dbSearch(sql);
    }

    //individual node network usage:rx
    public List<String> getRxMinion(String timePeriod) {
        String sql = joinSQL(timePeriod, MonitorConstant.NEGATIVE_VALUE_S, MonitorConstant.NET_RX, "");
        return dbSearch(sql);
    }
}
