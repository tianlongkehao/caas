$(document).ready(function(){
	/*高级配置*/
	$(".btn-higher").click(function(){
		cfgHighter();
	});
	$(".step2Btn").click(function(){
		cfgHighter();
	});
	$(".step1Btn").click(function(){
		cfgNoHighter();
	});
	$(".btn-nohigher").click(function(){
		cfgNoHighter();
	});
});
function cfgHighter(){
	$(".createRedisStep1").hide();
	$(".createRedisStep2").show();
	$(".step1Btn").removeClass("action");
	$(".step2Btn").addClass("action");
}
function cfgNoHighter(){
	$(".createRedisStep1").show();
	$(".createRedisStep2").hide();
	$(".step1Btn").addClass("action");
	$(".step2Btn").removeClass("action");
}
//创建集群
function createCluster(){
	layer.open({
		type:1,
		title:"创建集群",
		content:$(".createClusterInfo"),
		area:['800px','550px'],
		btn:['创建','关闭'],
		yes:function(index,layero){
			
		}
	})
}
//选择创建类型
function changeClusterType(obj){
	var thisType = $(obj).val();
	if(thisType == 1){
		$(".singlecfg").show();
	}else{
		$(".singlecfg").hide();
	}
}
//修改集群
function changeCluster(){
	layer.open({
		type:1,
		title:"创建集群",
		content:$(".createClusterInfo"),
		area:['800px','550px'],
		btn:['保存','关闭'],
		yes:function(index,layero){
			
		}
	})
}
//集群配置
function cfgCluster(){
	layer.open({
		type:1,
		title:"集群clusterName配置信息",
		content:$(".cfgClusterInfo"),
		area:['800px','600px'],
		btn:['关闭'],
		yes:function(index,layero){
			layer.close(index);
		}
	})
}
//集群详细信息
function clusterDetail(){
	layer.open({
		type:1,
		title:"集群clusterName详细信息",
		content:$(".clusterDetailInfo"),
		area:['1000px','600px'],
		btn:['关闭'],
		yes:function(index,layero){
			layer.close(index);
		}
	})
}
//一个节点的日志
function oneNodeLogs(){
	layer.open({
		type:1,
		title:"节点cluster-NO.日志",
		content:$(".oneNodeLogsInfo"),
		area:['700px','600'],
		btn:['关闭'],
		yes:function(index,layero){
			layer.close(index);
		}
	})
}
