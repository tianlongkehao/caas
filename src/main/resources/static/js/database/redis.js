//启动一个服务
function oneStart(id) {
	$.ajax({
		url : "" + ctx + "/RedisController/startRedisService.do?id=" + id,
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.status == "200") {
				layer.msg("启动成功！", {
					icon : 1,
					time : 1500,
					end : function() {
						window.location.reload(true);
					}
				});
			} else {
				layer.alert("启动失败：[" + data.message + "]");
			}
		}
	});
}

//停止一个服务
function oneStop(id) {
	layer.open({
		title : '停止服务',
		content : '确定停止redis服务？',
		btn : ['确定', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			$("#redisStatus_"+id).html("正在停止<img src='" + ctx + "/images/loading4.gif' alt=''/>");
			$.ajax({
				url : "" + ctx + "/RedisController/stopRedisService.do?id=" + id,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("停止成功！", {
							icon : 1,
							time : 1500,
							end : function() {
								window.location.reload(true);
							}
						});
					} else {
						layer.alert("停止失败：[" + data.message + "]");
					}
				}
			});
		}
	});
}

//删除一个服务
function oneDelete(id) {
	layer.open({
		title : '删除服务',
		content : '确定删除redis服务？',
		btn : ['确定', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			var loading = layer.load(1, {
				shade : [0.3, '#000']
			});
			$.ajax({
				url : "" + ctx + "/RedisController/deleteRedisService.do?id=" + id,
				success : function(data) {
					layer.close(loading);
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("删除成功！", {
							icon : 1,
							time : 1500,
							end : function() {
								window.location.reload(true);
							}
						});
					} else {
						layer.alert("删除失败：[" + data.message + "]");
					}
				}
			});
		}
	});
}

//集群detail
function clusterDetail(id) {
	layer.open({
		type : 1,
		title : "集群节点信息",
		content : $(".clusterDetailInfo"),
		area : ['700px', '600px'],
		btn : ['关闭'],
		yes : function(index, layero) {
			layer.close(index);
		}
	});
	$.ajax({
		url : ctx + "/RedisController/getPodList.do?id=" + id,
		type : "get",
		success : function(data){
			data = eval("(" + data + ")");
			var podHtml = "";
			if(data.status != "200"){
				podHtml = "<tr><td>" + data.message + "</td></tr>";
				$("#clusterInfoList").empty().append(podHtml);
			} else {
				for(var i in data.podList){
					var pod = data.podList[i];
					podHtml +=	'<tr>'
							+		'<td style="width:17%;padding-left:20px">' + pod.metadata.name+'</td>'
							+		'<td style="width:10%;">' + pod.status.podIP + '</td>'
							+		'<td style="width:10%;">' + data.redis.port + '</td>'
							+		'<td style="width:10%;">'
							+			'<a class="link"><i>重启</i></a>'
							+			'<a class="link" onclick="oneNodeLogs()"><i>日志</i></a>'
							+			'<a class="link" href="' + ctx + '/RedisController/cmd/' + id + '/' + pod.metadata.name + '" target="_blank"><i>终端</i></a>'
							+		'</td>'
							+	'</tr>';
				}
				$("#clusterInfoList").empty().append(podHtml);
			}
		}
	});
}

//集群配置
function cfgCluster() {
	layer.open({
		type : 1,
		title : "集群clusterName配置信息",
		content : $(".cfgClusterInfo"),
		area : ['800px', '600px'],
		btn : ['关闭'],
		yes : function(index, layero) {
			layer.close(index);
		}
	});
}

//一个节点的日志
function oneNodeLogs() {
	layer.open({
		type : 1,
		title : "节点cluster-NO.日志",
		content : $(".oneNodeLogsInfo"),
		area : ['700px', '600'],
		btn : ['关闭'],
		yes : function(index, layero) {
			layer.close(index);
		}
	});
}
