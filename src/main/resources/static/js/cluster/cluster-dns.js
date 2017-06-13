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
	$("#serviceName").val("");
	$("#address").val("");
	layer.open({
		type: 1,
		title: "创建DNS监控",
		content: $(".createDnsInfo"),
		area: ["500px"],
		btn: ["确定","取消"],
		yes: function(index,layero){
			var serviceName = $("#serviceName").val();
			var address = $("#address").val();
			if(!checkDns(serviceName,address)){
				return;
			}else{
				$.ajax({
					url:ctx + "/createDNSMonitor.do?serviceName="+serviceName+"&address="+address,
					type:"post",
					success:function(data){
						var data = eval("("+data+")");
						var status = data.status;
						layer.close(index);
						if(status=='200'){
							layer.msg("创建成功！",{icon : 1});
							setTimeout("location.reload(true)", 1000 );
						}else if(status=='400'){
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
		}
	});
}
//创建DNS的验证
function checkDns(serviceName,address){
	var flag = true;
	//serviceName服务名只能是小写字母开头，只能小写字母加数字
	if (serviceName == "" ) {
		layer.tips('服务名不能为空！', '#serviceName', {
			tips : [1, '#3595CC']
		});
		$('#serviceName').focus();
		flag = false;
		return;
	}
	var check = /^[a-z](?!\d{3,20}$)[a-z\d]{3,20}$/ ;
	if (!check.test(serviceName)) {
		layer.tips('服务名必须是小写字母开头，只能是小写字母和数字的4-20个字符组成！', '#serviceName', {
			tips : [1, '#3595CC']
		});
		$('#serviceName').focus();
		flag = false;
		return;
	}
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
	var ids = {ids:JSON.stringify(idArray)};
	layer.open({
		title: "删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			layer.close(index);
			$.ajax({
				url:ctx + "/deleteDNSMonitor.do",
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
		var id = $(chkItem[i]).attr("id");
		idArray.push(id);
	}
	var ids = {ids:JSON.stringify(idArray)};
	layer.open({
		title: "批量删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			layer.close(index);
			$.ajax({
				url:ctx + "/deleteDNSMonitor.do",
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

