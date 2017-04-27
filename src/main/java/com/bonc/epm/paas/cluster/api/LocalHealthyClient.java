package com.bonc.epm.paas.cluster.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.cluster.entity.Response;
import com.bonc.epm.paas.rest.util.RestFactory;

public class LocalHealthyClient {

	private static final Log LOG = LogFactory.getLog(LocalHealthyClient.class);

	private String nodeIp;
	private String port;
	private LocalHealthyAPI api;

	public LocalHealthyClient(String nodeIp,String port,RestFactory factory){
		this.nodeIp = nodeIp;
		this.port = port;
		api = factory.createLocalHealthyAPI( "http://" + this.nodeIp + ":" + this.port);
	}

	public LocalHealthyClient(String nodeIp,RestFactory factory){
		this(nodeIp,"8011",factory);
	}

	public Response dns(String type){
		LOG.info("集群测试，调用LocalHealthy测试dns："+type);
		return api.dns(type);
	}

	public Response ping(String ip){
		LOG.info("集群测试，调用ping,from:"+nodeIp+"to:"+ip);
		/*//String response = api.ping(ip);
		String response = "{\"result\": true,\"outMsg\": \"PING 192.168.0.81 (192.168.0.81) 56(84) bytes of data.\n64 bytes from 192.168.0.81: icmp_seq=1 ttl=64 time=0.738 ms\n64 bytes from 192.168.0.81: icmp_seq=2 ttl=64 time=0.443 ms\n64 bytes from 192.168.0.81: icmp_seq=3 ttl=64 time=0.287 ms\n64 bytes from 192.168.0.81: icmp_seq=4 ttl=64 time=0.393 ms\n64 bytes from 192.168.0.81: icmp_seq=5 ttl=64 time=0.311 ms\n64 bytes from 192.168.0.81: icmp_seq=6 ttl=64 time=0.281 ms\n64 bytes from 192.168.0.81: icmp_seq=7 ttl=64 time=0.360 ms\n64 bytes from 192.168.0.81: icmp_seq=8 ttl=64 time=0.385 ms\n64 bytes from 192.168.0.81: icmp_seq=9 ttl=64 time=0.402 ms\n64 bytes from 192.168.0.81: icmp_seq=10 ttl=64 time=0.329 ms\n\n--- 192.168.0.81 ping statistics ---\n10 packets transmitted, 10 received, 0% packet loss, time 9000ms\nrtt min/avg/max/mdev = 0.281/0.392/0.738/0.128 ms\n\"}";
		JSONObject jsonObject = JSONObject.parseObject(response);
		Response response2 = JSON.toJavaObject(jsonObject, Response.class);
		return response2;*/
		return api.ping(ip);
	}

	public Response tracepath(String ip){
		LOG.info("集群测试，调用tracepath,from:"+nodeIp+"to:"+ip);
		return api.tracepath(ip);
	}
}
