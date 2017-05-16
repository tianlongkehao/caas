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
function delOneDns(obj){
	var id="1";
	layer.open({
		title: "删除",
		content: "确定删除？",
		btn: ["确定","取消"],
		yes: function(index,layero){
			$.ajax({
				url:ctx + "/deleteDNSMonitor.do?id="+id,
				type:"get",
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



