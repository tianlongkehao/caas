package com.bonc.epm.paas.controller;

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
    @Value("${cc.url}")
    private String url;
    @Value("${cc.username}")
    private String username;
    @Value("${cc.password}")
    private String password;
    @Value("${cc.dbName}")
    private String dbName;
    private InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);

    private String hostName = "";

    //overall cluster memory usage:mem_limit
    public List<String> getMemLimitOverAll(String timePeriod) {
        String sql = "SELECT sum(\"value\") " +
                " FROM \"memory/limit_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND time > now() - 30m " +
                " GROUP BY time(3m)";
        Query mem_limit = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(mem_limit);
        List<List<Object>> result_mem_limit_list = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();
        List<String> memLimitAllList = new ArrayList<String>();
        for (int i = 0; i < result_mem_limit_list.size(); i++) {
            if (result_mem_limit_list.get(i).get(1) != null) {
                String str = result_mem_limit_list.get(i).get(1).toString();
                memLimitAllList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memLimitAllList;
    }

    //overall cluster memory usage:mem_use
    public List<String> getMemUseOverAll(String timePeriod) {
        String sql = "SELECT sum(\"value\") " +
                " FROM \"memory/usage_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " time > now() - 30m " +
                " GROUP BY time(3m)";
        Query mem_use = new Query(sql, dbName);
        QueryResult result_mem_use = influxDB.query(mem_use);
        List<List<Object>> result_mem_use_list = result_mem_use.getResults().get(0).getSeries().get(0).getValues();

        List<String> memUseAllList = new ArrayList<String>();
        for (int i = 0; i < result_mem_use_list.size(); i++) {
            if (result_mem_use_list.get(i).get(1) != null) {
                String str = result_mem_use_list.get(i).get(1).toString();
                memUseAllList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memUseAllList;
    }

    //overall cluster memory usage:mem_workingSet
    public List<String> getMemSetOverAll(String timePeriod) {
        String sql = "SELECT sum(\"value\") " +
                "FROM \"memory/working_set_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(3m)";
        Query mem_workingSet = new Query(sql, dbName);
        QueryResult result_mem_workingSet = influxDB.query(mem_workingSet);
        List<List<Object>> result_mem_workingSet_list = result_mem_workingSet.getResults().get(0).getSeries().get(0).getValues();

        List<String> memWorkingSetAllList = new ArrayList<String>();
        for (int i = 0; i < result_mem_workingSet_list.size(); i++) {
            if (result_mem_workingSet_list.get(i).get(1) != null) {
                String str = result_mem_workingSet_list.get(i).get(1).toString();
                memWorkingSetAllList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memWorkingSetAllList;
    }

    //memory usage group by node:memory_limit
    public List<String> getMemSetNode(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                "FROM \"memory/limit_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query mem_limit_node = new Query(sql, dbName);
        QueryResult result_mem_limit_node = influxDB.query(mem_limit_node);
        List<List<Object>> result_mem_limit_node_list = result_mem_limit_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> memLimitNodeList = new ArrayList<String>();
        for (int i = 0; i < result_mem_limit_node_list.size(); i++) {
            if (result_mem_limit_node_list.get(i).get(1) != null) {
                String str = result_mem_limit_node_list.get(i).get(1).toString();
                memLimitNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memLimitNodeList;
    }

    //memory usage group by node:memory_use
    public List<String> getMemUseNode(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                "FROM \"memory/working_set_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query mem_use_node = new Query(sql, dbName);
        QueryResult result_mem_use_node = influxDB.query(mem_use_node);
        List<List<Object>> result_mem_use_node_list = result_mem_use_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> memUseNodeList = new ArrayList<String>();
        for (int i = 0; i < result_mem_use_node_list.size(); i++) {
            if (result_mem_use_node_list.get(i).get(1) != null) {
                String str = result_mem_use_node_list.get(i).get(1).toString();
                memUseNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memUseNodeList;
    }

    //individual node memory usage： mem_limit
    public List<String> getMemLimitMinion(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                "FROM \"memory/limit_bytes_gauge\" " +
                "WHERE \"hostname\" =~ /" + hostName + "/ " +
                "AND \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query mem_limit_ind = new Query(sql, dbName);
        QueryResult result_mem_limit_ind = influxDB.query(mem_limit_ind);
        List<List<Object>> result_mem_limit_ind_list = result_mem_limit_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> memLimitMinionList = new ArrayList<String>();
        for (int i = 0; i < result_mem_limit_ind_list.size(); i++) {
            if (result_mem_limit_ind_list.get(i).get(1) != null) {
                String str = result_mem_limit_ind_list.get(i).get(1).toString();
                memLimitMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memLimitMinionList;
    }

    //individual node memory usage:memUse
    public List<String> getMemUseMinion(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                "FROM \"memory/usage_bytes_gauge\" " +
                "WHERE \"hostname\" =~ /" + hostName + "/ " +
                "AND \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query mem_use_ind = new Query(sql, dbName);
        QueryResult result_mem_use_ind = influxDB.query(mem_use_ind);
        List<List<Object>> result_mem_use_ind_list = result_mem_use_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> memUseMinionList = new ArrayList<String>();
        for (int i = 0; i < result_mem_use_ind_list.size(); i++) {
            if (result_mem_use_ind_list.get(i).get(1) != null) {
                String str = result_mem_use_ind_list.get(i).get(1).toString();
                memUseMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memUseMinionList;
    }

    //individual node memory usage:memory_working_set
    public List<String> getMemSetMinion(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                "FROM \"memory/working_set_bytes_gauge\" " +
                "WHERE \"hostname\" =~ /" + hostName + "/ " +
                "AND \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query mem_set_ind = new Query(sql, dbName);
        QueryResult result_mem_set_ind = influxDB.query(mem_set_ind);
        List<List<Object>> result_mem_set_ind_list = result_mem_set_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> memSetMinionList = new ArrayList<String>();
        for (int i = 0; i < result_mem_set_ind_list.size(); i++) {
            if (result_mem_set_ind_list.get(i).get(1) != null) {
                String str = result_mem_set_ind_list.get(i).get(1).toString();
                memSetMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return memSetMinionList;
    }

    //cpu use group by node:cpu_limit
    public List<String> getCpuLimitNode(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                "FROM \"cpu/limit_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query cpu_limit_node = new Query(sql, dbName);
        QueryResult result_cpu_limit_node = influxDB.query(cpu_limit_node);
        List<List<Object>> result_cpu_limit_node_list = result_cpu_limit_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> cpuLimitNodeList = new ArrayList<String>();
        for (int i = 0; i < result_cpu_limit_node_list.size(); i++) {
            if (result_cpu_limit_node_list.get(i).get(1) != null) {
                String str = result_cpu_limit_node_list.get(i).get(1).toString();
                cpuLimitNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return cpuLimitNodeList;
    }

    //cpu use group by node:cpu_use
    public List<String> getCpuUseNode(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1u) " +
                "FROM \"cpu/usage_ns_cumulative\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query cpu_use_node = new Query(sql, dbName);
        QueryResult result_cpu_use_node = influxDB.query(cpu_use_node);
        List<List<Object>> result_cpu_use_node_list = result_cpu_use_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> cpuUseNodeList = new ArrayList<String>();
        for (int i = 0; i < result_cpu_use_node_list.size(); i++) {
            if (result_cpu_use_node_list.get(i).get(1) != null) {
                String str = result_cpu_use_node_list.get(i).get(1).toString();
                cpuUseNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return cpuUseNodeList;
    }

    //individual node cpu usage:cpu_limit
    public List<String> getCpuLimitMinion(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                "FROM \"cpu/limit_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND \"hostname\" =~ /" + hostName + "/ " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query cpu_limit_ind = new Query(sql, dbName);
        QueryResult result_cpu_limit_ind = influxDB.query(cpu_limit_ind);
        List<List<Object>> result_cpu_limit_ind_list = result_cpu_limit_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> cpuLimitMinionList = new ArrayList<String>();
        for (int i = 0; i < result_cpu_limit_ind_list.size(); i++) {
            if (result_cpu_limit_ind_list.get(i).get(1) != null) {
                String str = result_cpu_limit_ind_list.get(i).get(1).toString();
                cpuLimitMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return cpuLimitMinionList;
    }

    //individual node cpu usage:cpu_use
    public List<String> getCpuUseMinion(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1u) " +
                "FROM \"cpu/usage_ns_cumulative\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND \"hostname\" =~ /" + hostName + "/ " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query cpu_use_ind = new Query(sql, dbName);
        QueryResult result_cpu_use_ind = influxDB.query(cpu_use_ind);
        List<List<Object>> result_cpu_use_ind_list = result_cpu_use_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> cpuUseMinionList = new ArrayList<String>();
        for (int i = 0; i < result_cpu_use_ind_list.size(); i++) {
            if (result_cpu_use_ind_list.get(i).get(1) != null) {
                String str = result_cpu_use_ind_list.get(i).get(1).toString();
                cpuUseMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return cpuUseMinionList;
    }

    //overall cluster disk usage:disk_limit
    public List<String> getDiskLimitOverAll(String timePeriod) {
        String sql = "SELECT sum(\"value\") " +
                "FROM \"filesystem/limit_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query disk_limit = new Query(sql, dbName);
        QueryResult result_disk_limit = influxDB.query(disk_limit);
        List<List<Object>> result_disk_limit_list = result_disk_limit.getResults().get(0).getSeries().get(0).getValues();

        List<String> diskLimitAllList = new ArrayList<String>();
        for (int i = 0; i < result_disk_limit_list.size(); i++) {
            if (result_disk_limit_list.get(i).get(1) != null) {
                String str = result_disk_limit_list.get(i).get(1).toString();
                diskLimitAllList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return diskLimitAllList;
    }

    //overall cluster disk usage:disk_use
    public List<String> getDiskUseOverAll(String timePeriod) {
        String sql = "SELECT sum(\"value\") " +
                "FROM \"filesystem/usage_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query disk_use = new Query(sql, dbName);
        QueryResult result_disk_use = influxDB.query(disk_use);
        List<List<Object>> result_disk_use_list = result_disk_use.getResults().get(0).getSeries().get(0).getValues();

        List<String> diskUseAllList = new ArrayList<String>();
        for (int i = 0; i < result_disk_use_list.size(); i++) {
            if (result_disk_use_list.get(i).get(1) != null) {
                String str = result_disk_use_list.get(i).get(1).toString();
                diskUseAllList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return diskUseAllList;
    }

    //disk usage group by node:disk_limit
    public List<String> getDiskLimitNode(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                "FROM \"filesystem/limit_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query disk_limit_node = new Query(sql, dbName);
        QueryResult result_disk_limit_node = influxDB.query(disk_limit_node);
        List<List<Object>> result_disk_limit_node_list = result_disk_limit_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> diskLimitNodeList = new ArrayList<String>();
        for (int i = 0; i < result_disk_limit_node_list.size(); i++) {
            if (result_disk_limit_node_list.get(i).get(1) != null) {
                String str = result_disk_limit_node_list.get(i).get(1).toString();
                diskLimitNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return diskLimitNodeList;
    }

    //disk usage group by node:disk_use
    public List<String> getDiskUseNode(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                "FROM \"filesystem/usage_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query disk_use_node = new Query(sql, dbName);
        QueryResult result_disk_use_node = influxDB.query(disk_use_node);
        List<List<Object>> result_disk_use_node_list = result_disk_use_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> diskUseNodeList = new ArrayList<String>();
        for (int i = 0; i < result_disk_use_node_list.size(); i++) {
            if (result_disk_use_node_list.get(i).get(1) != null) {
                String str = result_disk_use_node_list.get(i).get(1).toString();
                diskUseNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return diskUseNodeList;
    }

    //individual node disk usage:disk_limit
    public List<String> getDiskLimitMinion(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                "FROM \"filesystem/limit_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND \"hostname\" =~ /" + hostName + "/ " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query disk_limit_ind = new Query(sql, dbName);
        QueryResult result_disk_limit_ind = influxDB.query(disk_limit_ind);
        List<List<Object>> result_disk_limit_ind_list = result_disk_limit_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> diskLimitMinionList = new ArrayList<String>();
        for (int i = 0; i < result_disk_limit_ind_list.size(); i++) {
            if (result_disk_limit_ind_list.get(i).get(1) != null) {
                String str = result_disk_limit_ind_list.get(i).get(1).toString();
                diskLimitMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return diskLimitMinionList;
    }

    //individual node disk usage:disk_use
    public List<String> getDiskUseMinion(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                "FROM \"filesystem/usage_bytes_gauge\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND \"hostname\" =~ /" + hostName + "/ " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query disk_use_ind = new Query(sql, dbName);
        QueryResult result_disk_use_ind = influxDB.query(disk_use_ind);
        List<List<Object>> result_disk_use_ind_list = result_disk_use_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> diskUseMinionList = new ArrayList<String>();
        for (int i = 0; i < result_disk_use_ind_list.size(); i++) {
            if (result_disk_use_ind_list.get(i).get(1) != null) {
                String str = result_disk_use_ind_list.get(i).get(1).toString();
                diskUseMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return diskUseMinionList;
    }

    //network usage group by node:tx
    public List<String> getTxNode(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1s) " +
                "FROM \"network/tx_bytes_cumulative\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query network_tx_node = new Query(sql, dbName);
        QueryResult result_network_tx_node = influxDB.query(network_tx_node);
        List<List<Object>> result_network_tx_node_list = result_network_tx_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> networkTxNodeList = new ArrayList<String>();
        for (int i = 0; i < result_network_tx_node_list.size(); i++) {
            if (result_network_tx_node_list.get(i).get(1) != null) {
                String str = result_network_tx_node_list.get(i).get(1).toString();
                networkTxNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return networkTxNodeList;
    }

    //network usage group by node:rx
    public List<String> getRxNode(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1s) " +
                "FROM \"network/rx_bytes_cumulative\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s), \"hostname\"";
        Query network_rx_node = new Query(sql, dbName);
        QueryResult result_network_rx_node = influxDB.query(network_rx_node);
        List<List<Object>> result_network_rx_node_list = result_network_rx_node.getResults().get(0).getSeries().get(0).getValues();

        List<String> networkRxNodeList = new ArrayList<String>();
        for (int i = 0; i < result_network_rx_node_list.size(); i++) {
            if (result_network_rx_node_list.get(i).get(1) != null) {
                String str = result_network_rx_node_list.get(i).get(1).toString();
                networkRxNodeList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return networkRxNodeList;
    }

    //individual node network usage:tx
    public List<String> getTxMinion(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1s) " +
                "FROM \"network/tx_bytes_cumulative\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND \"hostname\" =~ /" + hostName + "/ " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query network_tx_ind = new Query(sql, dbName);
        QueryResult result_network_tx_ind = influxDB.query(network_tx_ind);
        List<List<Object>> result_network_tx_ind_list = result_network_tx_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> networkTxMinionList = new ArrayList<String>();
        for (int i = 0; i < result_network_tx_ind_list.size(); i++) {
            if (result_network_tx_ind_list.get(i).get(1) != null) {
                String str = result_network_tx_ind_list.get(i).get(1).toString();
                networkTxMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return networkTxMinionList;
    }

    //individual node network usage:rx
    public List<String> getRxMinion(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1s) " +
                "FROM \"network/rx_bytes_cumulative\" " +
                "WHERE \"container_name\" = 'machine' " +
                "AND \"hostname\" =~ /" + hostName + "/ " +
                "AND time > now() - 30m " +
                "GROUP BY time(30s)";
        Query network_rx_ind = new Query(sql, dbName);
        QueryResult result_network_rx_ind = influxDB.query(network_rx_ind);
        List<List<Object>> result_network_rx_ind_list = result_network_rx_ind.getResults().get(0).getSeries().get(0).getValues();

        List<String> networkRxMinionList = new ArrayList<String>();
        for (int i = 0; i < result_network_rx_ind_list.size(); i++) {
            if (result_network_rx_ind_list.get(i).get(1) != null) {
                String str = result_network_rx_ind_list.get(i).get(1).toString();
                networkRxMinionList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return networkRxMinionList;
    }

    /*//查询主机cpu使用值
    public List<String> getMinionCpuUse(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1u) " +
                " FROM \"cpu/usage_ns_cumulative\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " AND time > now() - 5m " +
                " GROUP BY time(30s) ";
        Query query_cpu_use = new Query(sql, dbName);
        QueryResult result_cpu_use = influxDB.query(query_cpu_use);
        //取得value列表
        List<List<Object>> result_cpu_values = result_cpu_use.getResults().get(0).getSeries().get(0).getValues();//[[2016-03-07T01:36:20Z, null], [2016-03-07T01:36:30Z, 184.5981786], [2016-03-07T01:36:40Z, null]]
        List<String> cpuUseList = new ArrayList<>();
        for (int i = 0; i < result_cpu_values.size(); i++) {
            if (result_cpu_values.get(i).get(1) != null) {
                String str = result_cpu_values.get(i).get(1).toString();
                cpuUseList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return cpuUseList;
    }

    //查询主机的cpuLimit
    public List<String> getMinionCpuLimit() {
        String sql = "SELECT last(\"value\") " +
                " FROM \"cpu/limit_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_cpu_limit = new Query(sql, dbName);
        QueryResult result_cpu_limit = influxDB.query(query_cpu_limit);
        //取得主机CPU
        List<List<Object>> cpu_limit = result_cpu_limit.getResults().get(0).getSeries().get(0).getValues();//.get(0).get(1).toString();
        List<String> cpuLimitList = new ArrayList<>();
        for (int i = 0; i < cpu_limit.size(); i++) {
            if (cpu_limit.get(i).get(1) != null) {
                String str = cpu_limit.get(i).get(1).toString();
                cpuLimitList.add(str);
            }
        }
        return cpuLimitList;
    }

    //查询内存使用量memUse
    public List<String> getMinionMemUse(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                " FROM \"memory/usage_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_mem_use = new Query(sql, dbName);
        QueryResult result_mem_use = influxDB.query(query_mem_use);
        //取得value列表
        List<List<Object>> result_mem_use_values = result_mem_use.getResults().get(0).getSeries().get(0).getValues();
        List<String> memUseList = new ArrayList<>();
        for (int i = 0; i < result_mem_use_values.size(); i++) {
            if (result_mem_use_values.get(i).get(1) != null) {
                String str = result_mem_use_values.get(i).get(1).toString();
                memUseList.add(str);
            }
        }
        return memUseList;
    }

    //查询内存memWorking Set
    public List<String> getMinionMemSet(String timePeriod) {
        String sql = "SELECT max(\"value\") " +
                " FROM \"memory/working_set_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_mem_set = new Query(sql, dbName);
        QueryResult result_mem_set = influxDB.query(query_mem_set);
        //取得value列表
        List<List<Object>> result_mem_set_values = result_mem_set.getResults().get(0).getSeries().get(0).getValues();
        List<String> memSetList = new ArrayList<>();
        for (int i = 0; i < result_mem_set_values.size(); i++) {
            if (result_mem_set_values.get(i).get(1) != null) {
                String str = result_mem_set_values.get(i).get(1).toString();
                memSetList.add(str);
            }
        }
        return memSetList;
    }

    //查询主机内存memLimit
    public List<String> getMinionMemLimit() {
        String sql = "SELECT max(\"value\") " +
                " FROM \"memory/limit_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_mem_limit = new Query(sql, dbName);
        QueryResult result_mem_limit = influxDB.query(query_mem_limit);
        //取得主机MEM
        List<List<Object>> mem_limit = result_mem_limit.getResults().get(0).getSeries().get(0).getValues();//.get(0).get(1).toString();
        List<String> memLimitList = new ArrayList<>();
        for (int i = 0; i < mem_limit.size(); i++) {
            if (mem_limit.get(i).get(1) != null) {
                String str = mem_limit.get(i).get(1).toString();
                memLimitList.add(str);
            }
        }
        return memLimitList;
    }

    //查询disk_use
    public List<String> getMinionDiskUse(String timePeriod) {
        String sql = "SELECT last(\"value\") " +
                " FROM \"filesystem/usage_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_disk_use = new Query(sql, dbName);
        QueryResult result_disk_use = influxDB.query(query_disk_use);
        List<List<Object>> result_disk_values = result_disk_use.getResults().get(0).getSeries().get(0).getValues();
        List<String> diskUseList = new ArrayList<>();
        for (int i = 0; i < result_disk_values.size(); i++) {
            if (result_disk_values.get(i).get(1) != null) {
                String str = result_disk_values.get(i).get(1).toString();
                diskUseList.add(str);
            }
        }
        return diskUseList;
    }

    //查询disk_limit
    public List<String> getMinionDiskLimit() {
        String sql = "SELECT last(\"value\") " +
                " FROM \"filesystem/limit_bytes_gauge\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_disk_limit = new Query(sql, dbName);
        QueryResult result_disk_limit = influxDB.query(query_disk_limit);
        List<List<Object>> disk_limit = result_disk_limit.getResults().get(0).getSeries().get(0).getValues();//.get(0).get(1).toString();
        List<String> diskLimitList = new ArrayList<>();
        for (int i = 0; i < disk_limit.size(); i++) {
            if (disk_limit.get(i).get(1) != null) {
                String str = disk_limit.get(i).get(1).toString();
                diskLimitList.add(str);
            }
        }
        return diskLimitList;
    }

    //查询网络上行值tx
    public List<String> getMinionTxUse(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1s) " +
                " FROM \"network/tx_bytes_cumulative\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_network_tx = new Query(sql, dbName);
        QueryResult result_network_tx = influxDB.query(query_network_tx);
        List<List<Object>> result_network_txs = result_network_tx.getResults().get(0).getSeries().get(0).getValues();
        List<String> networkTxList = new ArrayList<>();
        for (int i = 0; i < result_network_txs.size(); i++) {
            if (result_network_txs.get(i).get(1) != null) {
                String str = result_network_txs.get(i).get(1).toString();
                networkTxList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return networkTxList;
    }

    //查询网络上行值rx
    public List<String> getMinionRxUse(String timePeriod) {
        String sql = "SELECT non_negative_derivative(max(value),1s) " +
                " FROM \"network/rx_bytes_cumulative\" " +
                " WHERE \"container_name\" = 'machine' " +
                " AND \"hostname\"=\'" + hostName + "\' " +
                " and time > now() - 5m " +
                " GROUP BY time(30s)";
        Query query_network_rx = new Query(sql, dbName);
        QueryResult result_network_rx = influxDB.query(query_network_rx);
        List<List<Object>> result_network_rxs = result_network_rx.getResults().get(0).getSeries().get(0).getValues();
        List<String> networkRxList = new ArrayList<>();
        for (int i = 0; i < result_network_rxs.size(); i++) {
            if (result_network_rxs.get(i).get(1) != null) {
                String str = result_network_rxs.get(i).get(1).toString();
                networkRxList.add(str.substring(0, str.indexOf(".") + 3));
            }
        }
        return networkRxList;
    }*/

}
