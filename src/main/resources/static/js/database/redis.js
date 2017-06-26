//删除一个服务
function oneDelete(id){
	layer.open({
		title: '删除服务',
		content: '确定删除shera？',
		btn: ['确定', '取消'],
		yes: function(index, layero){
			layer.close(index);
			$.ajax({
				url :""+ ctx + "/RedisController/deleteRedisService.do?id="+id,
				success : function(data){
					data = eval("("+data+")");
					if (data.status == "200") {
						window.location.reload(true);
					}
					else {
						layer.alert("删除失败：["+data.message+""]");
					}
				}
			});
		}
	});
}

//集群detail
function clusterDetail(){
	layer.open({
		type:1,
		title:"集群节点信息",
		content:$(".clusterDetailInfo"),
		area:['1000px','600px'],
		btn:['保存','关闭'],
		yes:function(index,layero){

		}
	});
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
	});
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
	});
}
