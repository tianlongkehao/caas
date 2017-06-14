$(document).ready(function () {
	$.ajax({
		url:ctx + "/DNSController/getCheckDnsResult.do",
		type:"get",
		success:function(data){
			var data = eval("("+data+")");
			var status = data.status;
			if(status=='200'){
				var pingResultList = data.pingResultList;
				var dnsHtml = "";
				for(var i=0; i<pingResultList.length; i++){
					var dnsip = pingResultList[i].ip == undefined ? "解析失败" : pingResultList[i].ip;
					var dnscreateDate = pingResultList[i].createDate;
					var dnshost = pingResultList[i].host;
					var dnsid = pingResultList[i].id;
					var dnspingResult = pingResultList[i].pingResult;
					dnsHtml += '<tr>'
						+'<td style="width:5%;text-indent:20px">'
						+'<input class="chkItem" type="checkbox" id="'+dnsid+'">'
						+'</td>'
						+'<td style="text-indent:10px"><a class="link" onmouseover="showOneDnsResult(this)" onmouseout="hideOneDnsResult(this)" dnspingResult="'+dnspingResult+'">'+dnshost+'</a></td>'
						+'<td>'+dnsip+'</td>'
						+'<td>'+dnscreateDate+'</td>'
						+'<td>'
						+'<a onclick="delOneDns('+dnsid+')" title="删除"><i class="fa fa-trash"></i></a>'
						+'</td>'
						+'</tr>';
				}
				$("#dnsList").empty().append(dnsHtml);
			}
		}
	})
})
function showOneDnsResult(obj){
	var pingResult = $(obj).attr("dnspingResult");
	tip_index=layer.tips(pingResult, $(obj) ,{
		  tips: [3, 'rgba(237,114,114,0.75)'],area: ['450px', '50px'],time:0,tipsMore: true}
	);
}
function hideOneDnsResult(obj){
	layer.close(tip_index); 
}
//创建dns
function createDns(){
	layer.open({
		type: 1,
		title: "创建dns",
		content: $(".createDnsInfo"),
		area: ["520px"],
		btn: ["确定","取消"],
		yes: function(index,layero){
			var address = $("#address").val();
			if(!checkDns(address)){
				return;
			}
			layer.close(index);
			$.ajax({
				url:ctx + "/DNSController/createDnsService.do",
				type:"post",
				data:{"address":address},
				success:function(data){
					var data = eval("("+data+")");
					if(data.status == '200'){
						layer.msg("创建成功", {
							icon : 1
						});
						setTimeout("location.reload(true)", 1000 );
					}else{
						layer.alert(data.messages[0],{
							icon : 2,
							btn:['关闭'],
							yes:function(){
								layer.closeAll();
								setTimeout("location.reload(true)", 100 );
							}
						});
					}
				}
			});
		}
	})
}
//创建定时监控任务
function createDNSMonitor(){
	$.ajax({
		url:ctx + "/DNSController/getAllDnsService.do",
		type:"get",
		success:function(data){
			var data = eval("("+data+")");
			var monitorHtml = "";
			for(var n=0;n<data.length; n++){
				var address =data[n].address;
				var isMonitor = data[n].isMonitor; //1监控  0未监控
				var isMonitorStatus = isMonitor == 0 ? 'none' : 'block';
				monitorHtml += '<tr onclick="checkServerDomain(this)">'
						+'<td>'+address+'<i class="fa fa-check" domain="'+address+'" style="display:'+isMonitorStatus+'"></i></td>'
						+'</tr>';
			}
			$("#timeTaskList").empty().append(monitorHtml);
			
		}
	});
	layer.open({
		type: 1,
		title: "定时检查",
		content: $(".timedTaskInfo"),
		area: ["520px"],
		btn: ["确定","取消"],
		yes: function(index,layero){
			//判断定时监控任务不能大于5个
			var checkItems = $("i.fa-check:visible");
			var domains = new Array();
			if(checkItems.length>5){
				layer.msg("最多可选5个服务域名", {
					icon : 2
				});
				return;
			}else{
				for(var i=0; i<checkItems.length; i++){
					var checkItem = checkItems[i];
					domains.push($(checkItem).attr("domain"));
				}
				
			}
			//选择定时
			var checkTime = $("#dnsTime").val();
			if(checkTime == 0){
				layer.msg("请选择时间间隔", {
					icon : 2
				});
				return;
			}
			var dnsData = {
					"sleepTime":checkTime*60000,
					"addressString":JSON.stringify(domains)
				}
			//layer.close(index);
			$.ajax({
				url:ctx + "/DNSController/modifyDnsMonitorConfig.do",
				type:"post",
				data: dnsData,
				success:function(data){
					var data = eval("("+data+")");
					var status = data.status;
					layer.close(index);
					if(status=='200'){
						layer.msg("创建定时监控任务完成！",{icon : 1});
						setTimeout("location.reload(true)", 1000 );
					}else if(status=='300'){
						layer.alert(data.messages[0],{
							icon : 2,
							btn:['关闭'],
							yes:function(){
								layer.closeAll();
								setTimeout("location.reload(true)", 100 );
							}
						});
					}
					
				}
			})
		}
	});
}
function checkServerDomain(obj){
	$(obj).find("i.fa-check").toggle();
}
//function addDnsHost(obj){
//	var strHtml = '<tr onclick="checkServerDomain(this)"><td><input type="text"><i class="fa fa-save" onclick="saveAddHost(this)"></i></td></tr>';
//	$(obj).parents(".domainTable").find("tbody").append(strHtml);
//}
//function saveAddHost(obj){
//	var thisVal = $(obj).parent().find("input").val();
//	var changeHtml = thisVal+'<i class="fa fa-check" domain="'+thisVal+'"></i>';
//	$(obj).parent().empty().append(changeHtml);
//}
//定时日志
function dnsHistory(){
	$.ajax({
		url:ctx + "/DNSController/getMonitorLog.do",
		type:"get",
		success:function(data){
			var data = eval("("+data+")");
			var historyHtml ="";
			for(var i=0; i<data.length; i++){
				historyHtml += '<p>'+data[i].createDate+'&nbsp;&nbsp;'+data[i].host+'</p>'
								+'<p>'+data[i].pingResult+'</p>';
			}
			$("#hisrotyInfos").empty().append(historyHtml);
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
	})
}

//创建DNS的验证
function checkDns(address){
	var flag = true;
	//serviceName服务名只能是小写字母开头，只能小写字母加数字
//	if (serviceName == "" ) {
//		layer.tips('服务名不能为空！', '#serviceName', {
//			tips : [1, '#3595CC']
//		});
//		$('#serviceName').focus();
//		flag = false;
//		return;
//	}
//	var check = /^[a-z](?!\d{3,20}$)[a-z\d]{3,20}$/ ;
//	if (!check.test(serviceName)) {
//		layer.tips('服务名必须是小写字母开头，只能是小写字母和数字的4-20个字符组成！', '#serviceName', {
//			tips : [1, '#3595CC']
//		});
//		$('#serviceName').focus();
//		flag = false;
//		return;
//	}
	//address域名就是网址 网址+端口 ip ip加端口
	if (address == "") {
		layer.tips('域名不能为空！', '#address', {
			tips : [1, '#3595CC']
		});
		$('#address').focus();
		flag = false;
		return;
	}
	var checkAddress =/^((?:2[0-5]{2}|1\d{2}|[1-9]\d|[1-9])\.(?:(?:2[0-5]{2}|1\d{2}|[1-9]\d|\d)\.){2}(?:2[0-5]{2}|1\d{2}|[1-9]\d|\d)):(\d|[1-9]\d|[1-9]\d{2,3}|[1-5]\d{4}|6[0-4]\d{3}|654\d{2}|655[0-2]\d|6553[0-5])$/;
	var checkAddr = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/;
	if (checkAddress.test(address)==false && checkAddr.test(address)==false) {
		layer.tips('域名就是 网址+端口，例如:clyxys.bonc.com:50或者192.168.0.1:8080！', '#address', {
			tips : [1, '#3595CC']
		});
		$('#address').focus();
		flag = false;
		return;
	}
	return flag;
}

//删除一个dns
function delOneDns(id){
	var idArray = new Array();
	idArray.push(id);
	var ids = {"idString":JSON.stringify(idArray)};
	layer.open({
		title: "删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			layer.close(index);
			$.ajax({
				url:ctx + "/DNSController/deleteDnsService.do",
				type:"post",
				data:ids,
				success:function(data){
					var data = eval("("+data+")");
					var status = data.status;
					if(status=='200'){
						layer.msg("删除成功！",{icon : 1});
						setTimeout("location.reload(true)", 1500 );
					}else if(status=='400'){
						layer.alert(data.messages[0],
								{icon : 2,
								btn: ["确定"],
								yes:function(index2){
									layer.close(index2);
									location.reload(true);
								}
							});
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
		var id = parseInt($(chkItem[i]).attr("id"));
		idArray.push(id);
	}
	var ids = {"idString":JSON.stringify(idArray)};
	layer.open({
		title: "批量删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			layer.close(index);
			$.ajax({
				url:ctx + "/DNSController/deleteDnsService.do",
				type:"post",
				data:ids,
				success:function(data){
					var data = eval("("+data+")");
					var status = data.status;
					if(status=='200'){
						layer.msg("删除成功！",{icon : 1});
						setTimeout("location.reload(true)", 1500 );
					}else if(status=='400'){
						//layer.alert(data.messages[0],{icon : 2});
						layer.alert(data.messages[0],
								{icon : 2,
								btn: ["确定"],
								yes:function(index2){
									layer.close(index2);
									location.reload(true);
								}
							});
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
		url:ctx + "/getDNSMonitorResultStatus.do?id="+id,
		type:"get",
		success:function(data){
			var data = eval("("+data+")");
			if(data.status == '200'){
				var dnsMonitorResultListNow = data.DNSMonitorResultStatus;
				$("#serviceNameInfo").val(serviceName);
				$("#addressInfo").val(dnsMonitorResultListNow.address);
				$("#resultInfo").val(dnsMonitorResultListNow.status);
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
	});
}
//dns监控日志
function dnsOneHistory(id){
	$("#dnsChangetime").val("1");
	var time = 1;
	$("#thisDnsId").val(id);
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
				$("#hisrotyInfos").empty().append(historyHtml);
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

function dnsChangeTime(){
	var thisId = $("#thisDnsId").val();
	var changeTimeVal = $("#dnsChangetime").val();
	$.ajax({
		url:ctx + "/getDNSMonitorResultList.do?id="+thisId+"&time="+changeTimeVal,
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
				$("#hisrotyInfos").empty().append(historyHtml);
				
			}
		}
	})
}

