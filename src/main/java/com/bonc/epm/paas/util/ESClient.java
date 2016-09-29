package com.bonc.epm.paas.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 
 * elasticsearch 日志查询公共类
 *  提供查询es集群客户端的初始化和关闭功能
 *  以及筛选日志和获取基本信息功能
 * @author fengtao
 * @version 2016年4月30日
 * @see ESClient
 * @since 2016年4月30日
 * 版本更新时间：2016年8月30
 * 更新人：王珂
 */
@Service
public class ESClient {
    /**
     * ESClient类日志实例
     */
    private static final Logger LOG = LoggerFactory.getLogger(ESClient.class);
	
    /**
	 * 声明客户端类型变量
	 */
    private Client client;
	

    /**
     * 
     * Description:
      *  初始化es客户端
     * @param es
     * @param clusterName 
     * @see
     */
    @Before
	public void initESClient(String es,String clusterName) {	
		// 配置你的es,现在这里只配置了集群的名,默认是elasticsearch,跟服务器的相同
        Settings settings = Settings.settingsBuilder()
                        				.put("cluster.name", clusterName)
                        				.put("discovery.type", "zen")//发现集群方式
                        				.put("discovery.zen.minimum_master_nodes", 1)//最少有1个master存在
                        				.put("discovery.zen.ping_timeout", "2000ms")//集群ping时间，太小可能会因为网络通信而导致不能发现集群
                        				.put("discovery.initial_state_timeout", "500ms")
                        				//.put("gateway.type", "local")//(fs, none, local)
                        				.put("index.number_of_shards", 1)
                        				.put("action.auto_create_index", false)//配置是否自动创建索引
                        				.put("cluster.routing.schedule", "50ms")//发现新节点时间 
                        				.build();
        try {
            String transportAddresses = es;
               // 集群地址配置
            List<InetSocketTransportAddress> list = new ArrayList<InetSocketTransportAddress>();
            if (!StringUtils.isEmpty(transportAddresses)) {
                String[] strArr = transportAddresses.split(",");
                for (String str : strArr) {
                    String[] addressAndPort = str.split(":");
                    String address = addressAndPort[0];
                    int port = Integer.valueOf(addressAndPort[1]);
                    InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
                                                                                                InetAddress.getByName(address), port);
                    list.add(inetSocketTransportAddress);
                }
            }
			// 这里可以同时连接集群的服务器,可以多个,并且连接服务是可访问的
            InetSocketTransportAddress[] addressList = (InetSocketTransportAddress[]) list.toArray(new InetSocketTransportAddress[list.size()]);
            client = new Builder().settings(settings)
                                  .build()
					              .addTransportAddresses(addressList);
        } 
        catch (UnknownHostException e) {
            LOG.error("初始化elasticsearch异常！");
            e.printStackTrace();
        }
        LOG.debug("初始化elasticsearch成功！");
    }

    /**
     * 
     * Description: 
     * 关闭es客户端  
     * @see
     */
    @After
	public void closeESClient() {
        client.close();
        LOG.info("elasticsearch已关闭");
    }

    /**
     * 
     * Description: <br>
     * 根据关键字查找日志内容
     * @param index String
     * @param type String
     * @param keyWord String  
     * @return log String
     * @see
     */
    @SuppressWarnings("rawtypes")
	public String search(String index,String type,String keyWord){
        String string ="";
        try {
            SearchRequestBuilder searchRequestBuilder = null;
            searchRequestBuilder = client.prepareSearch(index)
                                           .setTypes(type)
                                           .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                                           .setQuery(QueryBuilders.matchPhraseQuery("kubernetes.pod_name", keyWord))
                                           .addSort("@timestamp", SortOrder.ASC)
                                           .setFrom(0)
                                           .setSize(5000)
                                           .setExplain(true);
      
            SearchResponse response = searchRequestBuilder.execute().actionGet();
            SearchHit[] hits = response.getHits().getHits();
            for (int i = 0; i < hits.length; i++) {
                SearchHit hit = hits[i];
                Map result = hit.getSource();			
                string = string + result.get("log");
            }
        } 
        catch (IndexNotFoundException infe) {
            LOG.error("搜索日志的Index出错：-" + infe.getMessage());
        }
        catch (Exception e) {
            LOG.error(keyWord+"日志出错！错误信息：-" + e.getMessage());
        }
		
        LOG.debug("start*******************************************************************************");
        LOG.debug("pod{"+keyWord+"}日志:"+string);
        LOG.debug("end*********************************************************************************");
        return string;
    }
	
    
    /**
     * 
     * Description: <br>
     * 根据关键字和时间范围查找日志内容
     * @param type String
     * @param keyWord String  
     * @param from String  
     * @param to String  
     * @return log String
     * @see
     */
    @SuppressWarnings("rawtypes")
	public String search(String type,String pod_name,String container_name,String from,String to){
        String string ="";
        try {
            SearchRequestBuilder searchRequestBuilder = null;
            searchRequestBuilder = client.prepareSearch()
                    .setTypes(type)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                    .setQuery(QueryBuilders.matchPhraseQuery("kubernetes.pod_name", pod_name))
					.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("kubernetes.pod_name", pod_name))
							.must(QueryBuilders.matchQuery("kubernetes.container_name", container_name)))
					.setPostFilter(QueryBuilders.rangeQuery("@timestamp").from(from).to(to))
					.addSort("@timestamp", SortOrder.ASC).setFrom(0)
					.setSize(5000)
                    .setExplain(true);
            
            SearchResponse response = searchRequestBuilder.execute().actionGet();
            SearchHit[] hits = response.getHits().getHits();
            for (int i = 0; i < hits.length; i++) {
                SearchHit hit = hits[i];
                Map result = hit.getSource();			
                string = string + result.get("log");
            }
        } 
        catch (IndexNotFoundException infe) {
            LOG.error("搜索日志的Index出错：-" + infe.getMessage());
        }
        catch (Exception e) {
            LOG.error("pod{"+pod_name+"}container{"+container_name+"}日志出错！错误信息：-" + e.getMessage());
        }
		
        LOG.debug("start*******************************************************************************");
        LOG.debug("pod{"+pod_name+"}container{"+container_name+"}日志:"+string);
        LOG.debug("end*********************************************************************************");
        return string;
    }

    /**
     * 
     * Description:
     * 获取日志返回相应信息
     * @see
     */
    public void get(){
        GetResponse response = client.prepareGet("logstash-2016.01.25", "fluentd", "AVL5NVZZ2fZxynvv9zEb")
				                     .execute()
				                     .actionGet();
        LOG.info("headers:-" + response.getHeaders() + ";" + 
				                     "exists:-" + response.isExists() + ";" + "sourceString:-" + response.getSourceAsString());
    }

	/**
	 * 
	 * Description:
	 * 删除 
	 * @see
	 */
    public void delete(){
        DeleteResponse response = client.prepareDelete("blog", "post", "AVJjRJVqW-UsQoTouwCF")
				                        .execute()
				                        .actionGet();
        boolean isFound = response.isFound();
        LOG.info("delete result:-" + isFound);
    }
	
	
/*     public static void main(String args[]){
      ESClient esc = new ESClient();
      esc.initESClient("192.168.50.3:8300");
      //esc.createIndex();
      esc.search("logstash-2016.09.01","fluentd","casvass-ctksu");
      //esc.getIndex();
      //esc.get();
      //esc.delete();
      esc.closeESClient();
    }*/
    
    /**
     * 
     * Description: <br>
     * 根据关键字和时间范围查找日志内容
     * @param type String
     * @param keyWord String  
     * @param from String  
     * @param to String  
     * @return log String
     * @see
     */
    @SuppressWarnings("rawtypes")
	public List<String> searchLogsByService(String type,String serviceName,String nameSpace,String from,String to){
        List<String> logList = new ArrayList<String>();
        try {
            SearchRequestBuilder searchRequestBuilder = null;
            searchRequestBuilder = client.prepareSearch()
                    .setTypes(type)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                    .setQuery(QueryBuilders.matchPhraseQuery("kubernetes.pod_name", pod_name))
					.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("kubernetes.namespace_name", nameSpace))
							.must(QueryBuilders.matchQuery("kubernetes.labels.app", serviceName)))
					.setPostFilter(QueryBuilders.rangeQuery("@timestamp").from(from).to(to))
//					.addSort("kubernetes.pod_id", SortOrder.ASC)
//					.addSort("docker.container_id", SortOrder.ASC)
					.addSort("@timestamp", SortOrder.ASC)
					.setFrom(0)
					.setSize(5000)
                    .setExplain(true);
            
            SearchResponse response = searchRequestBuilder.execute().actionGet();
            SearchHit[] hits = response.getHits().getHits();
            
            String string = "";
            
            String current_pod_id ="";
            String current_pod_name ="";
            String current_container_id ="";
            String current_container_name ="";
            
            for (int i = 0; i < hits.length; i++) {
                SearchHit hit = hits[i];
                Map result = hit.getSource();
                if (current_pod_id.equals(((Map)result.get("kubernetes")).get("pod_id"))
                		&& current_container_id.equals(((Map)result.get("docker")).get("container_id"))) {
                	string = string + "["+current_pod_name+"]["+current_container_name+"]"+result.get("log");
				} else {
					current_pod_id = (String) ((Map)result.get("kubernetes")).get("pod_id");
					current_pod_name = (String) ((Map)result.get("kubernetes")).get("pod_name");
					current_container_id =(String) ((Map)result.get("docker")).get("container_id");
					current_container_name = (String) ((Map)result.get("kubernetes")).get("container_name");
					if (!string.equals("")) {
						logList.add(string);
					}
					string = new String() + result.get("log");
				}
            }
            logList.add(string);
        } 
        catch (IndexNotFoundException infe) {
            LOG.error("搜索日志的Index出错：-" + infe.getMessage());
        }
        catch (Exception e) {
            LOG.error("serviceName{"+serviceName+"}nameSpace{"+nameSpace+"}日志出错！错误信息：-" + e.getMessage());
        }
		
        LOG.debug("start*******************************************************************************");
        LOG.debug("serviceName{"+serviceName+"}nameSpace{"+nameSpace+"}日志:"+logList.toString());
        LOG.debug("end*********************************************************************************");
        return logList;
    }
 
    
    
    
    
}
