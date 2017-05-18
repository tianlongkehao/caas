$(document).ready(function () {
	
})


function timedTask(){
	layer.open({
		type: 1,
		title: "定时检查",
		content: $(".timedTaskInfo"),
		area: ["500px"],
		btn: ["确定","取消"],
		yes: function(index,layero){
			var checkItems = $("i.fa-check:visible");
			var domains = new Array();
			if(checkItems.length>5){
				layer.msg("最多可选5个服务域名", {
					icon : 2
				});
				return false;
			}else{
				for(var i=0; i<checkItems; i++){
					var checkItem = checkItems[i];
					domains.push($(checkItems).attr("domain"));
				}
				layer.close(index);
			}
			
		}
	});
}

function checkServerDomain(obj){
	$(obj).find("i.fa-check").toggle();
}
//创建DNS监控
function createDNSMonitor(){
	
	layer.open({
		type: 1,
		title: "创建DNS监控",
		content: $(".createDnsInfo"),
		area: ["500px"],
		btn: ["确定","取消"],
		yes: function(index,layero){
			var serviceName = $("#serviceName").val();
			var address = $("#address").val();
			$.ajax({
				url:ctx + "/createDNSMonitor.do?serviceName="+serviceName+"&address="+address,
				type:"post",
				success:function(data){
					var data = eval("("+data+")");
					var status = data.status;
					layer.close(index);
					if(status=='200'){
						layer.msg("创建成功！",{icon : 1});
					}else if(status=='400'){
						layer.alert(data.messages[0],{icon : 2});
					}
				}
			})
		}
	});
}

//删除一个dns
function delOneDns(id){
	var idArray = new Array();
	idArray.push(id);
	var ids = {ids:JSON.stringify(idArray)};
	layer.open({
		title: "删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			$.ajax({
				url:ctx + "/deleteDNSMonitor.do",
				type:"post",
				data:ids,
				success:function(data){
					var data = eval("("+data+")");
					var status = data.status;
					layer.close(index);
					if(status=='200'){
						layer.msg("删除成功！",{icon : 1});
					}else if(status=='400'){
						layer.alert(data.messages[0],{icon : 2});
					}
				}
			})
		}
	})
}

//批量删除dns
function delDns(){
	var chkItem = $(".chkItem:checked");
	var idArray = new Array();
	for(var i=0; i<chkItem.length; i++){
		var id = $(chkItem[i]).attr("id");
		idArray.push(id);
	}
	idArray.push(id);
	var ids = {ids:JSON.stringify(idArray)};
	layer.open({
		title: "删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			$.ajax({
				url:ctx + "/deleteDNSMonitor.do",
				type:"delete",
				data:ids,
				success:function(data){
					var data = eval("("+data+")");
					var status = data.status;
					layer.close(index);
					if(status=='200'){
						layer.msg("删除成功！",{icon : 1});
					}else if(status=='400'){
						layer.alert(data.messages[0],{icon : 2});
					}
				}
			})
		}
	})
}

//获得dns当前监控
function getDnsResult(obj,id){
	var serviceName = $(obj).attr("serviceName");
	$.ajax({
		url:ctx + "/getDNSMonitorResultList.do?id="+id+"&time=1",
		type:"get",
		success:function(data){
			var data = eval("("+data+")");
			if(data.status == '200'){
				var dnsMonitorResultListNow = data.dnsMonitorResultList[0];
				$("#serviceNameInfo").val(serviceName);
				$("#addressInfo").val(dnsMonitorResultListNow.host);
				var resultInfo = "";
				if(dnsMonitorResultListNow.success){
					resultInfo = "成功";
				}else{
					resultInfo = "失败";
				}
				$("#resultInfo").val(resultInfo);
				$("#ipInfo").val(dnsMonitorResultListNow.ip);
				$("#timeInfo").val("1");
				$("#logInfo").val(dnsMonitorResultListNow.pingResult);
				layer.open({
					type: 1,
					title: "DNS监控信息",
					content: $(".dnsResultInfo"),
					area: ["500px"],
					btn: ["关闭"],
					yes: function(index,layero){
						layer.close(index);
					}
				})
			}else if(status=='400'){
				layer.alert(data.messages[0],{icon : 2});
			}
		}
	})
}
//dns监控日志
function dnsOneHistory(id){
	var time = 1;
	$.ajax({
		url:ctx + "/getDNSMonitorResultList.do?id="+id+"&time="+time,
		type:"get",
		success:function(data){
			var data = eval("("+data+")");
			if(data.status == '200'){
				var dnsMonitorResultList = data.dnsMonitorResultList;
				var historyHtml ="";
				for(var i=0; i<dnsMonitorResultList.length; i++){
					historyHtml += '<p>'+dnsMonitorResultList[i].createDate+'</p>'
									+'<p>'+dnsMonitorResultList[i].pingResult+'</p>';
				}
				$("#hisrotyInfos").append(historyHtml);
				layer.open({
					type: 1,
					title: "DNS监控信息",
					content: $(".dnshistoryInfo"),
					area: ["1000px","600px"],
					btn: ["关闭"],
					yes: function(index,layero){
						layer.close(index);
					}
				})
			}
		}
	})
}

