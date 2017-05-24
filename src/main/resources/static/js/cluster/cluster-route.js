$(document).ready(function () {
	//loadRoute();
	/*var nodeIP = $("#search_routeNode").val();
	$('.dataTables-example').dataTable({
	 	//"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ] }],
        "processing": true,
        "serverSide": true,
        "ordering":false,
        "searching":false,
        "ajax": ctx + "/cluster/getRouteTable.do?ip=192.168.0.80",
        "columns": [
					{
						data : null,
						render : function ( data, type, row ) {
							var html = '<td>&nbsp;&nbsp;&nbsp;</td>';
							return html;
						}
					},
                    {
                    	data : null,
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}
                    		return data;
                			var html = '<td>'+ row.node +'</td>';
						    return html;
                    	}
                    },
                    {
                    	data : null ,
                		render : function ( data, type, row ) {
                    		if (data == null || data == "") {
                    			return "";
                    		}else if(success) {
                    			return "成功";
                    		}else {
                    			return "失败";
                    		}
                			var html = '<td>'+ row.node +'</td>';
						    return html;
                    	}
                    },

                   ]
	});*/
});


/**
 * 加载路由监控数据
 */
function loadRoute() {
	var nodeIP = $("#search_routeNode").val();
	$.ajax({
		 url : ctx + "/cluster/getRouteTable.do?ip="+nodeIP,
		 type : "get",
		 success : function(data){
			 var data = eval("(" + data + ")");
			 var targetNum = data.items;
			 var htmlTr = "";
			 for(var i=0; i<targetNum.length; i++){
				 var targetIP = targetNum[i].targetIP;
				 var success = targetNum[i].success;
				 var successResult = "";
				 if(success == 'false'){
					 successResult = "成功";
				 }else if(success == 'true'){
					 successResult = "失败";
				 }else if(success == 'unknown'){
					 successResult = "未知";
				 }
				 htmlTr += '<tr>'
					+'<td><input type="checkbox" class="chkItem" nodeIp="${nodeList.nodeIp }" nodeName="${nodeList.nodeName }"/></td>'
					+'<td colspan="3">${nodeList.nodeName }（${nodeList.nodeIp }）</td>';
					+'<td>'+successResult+'</td>';
					+'<td class="routeBtns">'
					+'<a class="fa-caret" nodeIp="${nodeList.nodeIp }" onclick="nodeTargetIPDetail(this)"><i class="fa fa-caret-right" flag="1">测试</i></a>'
					+'<a onclick="recoverOneRoute(this)" nodeIp="${nodeList.nodeIp }" nodeName="${nodeList.nodeName }"><i>恢复</i></a>'
					+'</td>'
					+'</tr>';
			 }
			 $("#routeList").append(htmlTr);
		 }
	 });
}

//function searchRouteNode(){
//	$("#routeList").empty();
//	loadRoute();
//}

function nodeTargetIPDetail(obj){
	var thisobj = $(obj);
	var flag = thisobj.find("i").attr("flag");
	var nodeIP = thisobj.attr("nodeIp");
	var targetTrClass = 'targetDetail'+nodeIP.substring(nodeIP.length-2,nodeIP.length);
	if(flag == "1"){
		thisobj.find("i").removeClass("fa-caret-right").addClass("fa-caret-down").attr("flag","2");
		$.ajax({
			 url : ctx + "/cluster/getRouteTable.do?ip="+nodeIP,
			 type : "get",
			 success : function(data){
				 var data = eval("(" + data + ")");
				 if(data.status == "200"){
					 var targetNum = data.checkRoutetable.items;
					 var htmlTr = '<tr class="targetDetail '+targetTrClass+'">'
							+'<td>&nbsp;</td>'
							+'<td>targetIP</td>'
							+'<td>期望网关</td>'
							+'<td >实际网关</td>'
							+'<td colspan="2">结果</td>'
							+'</tr>';;
					 for(var i=0; i<targetNum.length; i++){
						 var targetIP = targetNum[i].targetIP;
						 var expectedGW = targetNum[i].expectedGW;
						 var realGW = targetNum[i].realGW;
						 var success = targetNum[i].success;
						 var successResult = "";
						 if(success == 'true'){
							 successResult = "成功";
						 }else{
							 successResult = "失败";
						 }
						 htmlTr += '<tr class="targetDetail '+targetTrClass+'">'
							+'<td>&nbsp;</td>'
							+'<td>'+targetIP+'</td>'
							+'<td>'+expectedGW+'</td>'
							+'<td >'+realGW+'</td>'
							+'<td colspan="2">'+successResult+'</td>'
							+'</tr>';
					 }
					 thisobj.parent().parent().after(htmlTr);
				 }
			 }
		 });
	}else{
		thisobj.find("i").removeClass("fa-caret-down").addClass("fa-caret-right").attr("flag","1");
		var removeTr = 'tr.'+targetTrClass;
		$(removeTr).remove();
	}
}
//单独操作 恢复一个节点路由
function recoverOneRoute(obj){
	var nodeIp = $(obj).attr("nodeIp");
	var nodeName = $(obj).attr("nodeName");
	var oneNodeData = new Array();
	var oneNode = {"name":nodeName, "ip":nodeIp};
	oneNodeData.push(oneNode);
	var nodeListString = {"nodeListString":JSON.stringify(oneNodeData)};
	var loading = layer.load(0, {
		shade : [ 0.3, '#000' ]
	});
	$.ajax({
		url: ctx + "/cluster/recoverRoutetable.do",
		type:"POST",
		data:nodeListString,
		success:function(data){
			var data = eval('('+data+')');
			var status = data.status;
			if(status == '200'){
				layer.msg("恢复成功！",{icon : 1});
			}else if(status == '300'){
				layer.msg( data.messages+"恢复失败!",{icon : 2});
			}else if(status == '400'){
				layer.msg("恢复失败，链接异常！",{icon : 2});
			}
			layer.close(loading);
		}
	})
}
//批量恢复
function recoverRoutes(){
	var checkNodes = $(".chkItem:checked");
	var nodesData = new Array();
	if(checkNodes.length == 0){
		layer.tips('请选择至少一个集群节点','#checkallbox', {
			tips : [ 1, '#3595CC' ]
		});
		$('#checkallbox').focus();
		return;
	}else{
		for(var i = 0; i < checkNodes.length; i++){
			var nodeIp = $(checkNodes[i]).attr("nodeIp");
			var nodeName = $(checkNodes[i]).attr("nodeName");
			var nodeInfo = {"name":nodeName, "ip":nodeIp};
			nodesData.push(nodeInfo);
		}
		var nodeListString = {"nodeListString":JSON.stringify(nodesData)};
		var loading = layer.load(0, {
			shade : [ 0.3, '#000' ]
		});
		$.ajax({
			url: ctx + "/cluster/recoverRoutetable.do",
			type:"POST",
			data:nodeListString,
			success:function(data){
				var data = eval('('+data+')');
				layer.close(loading);
				var status = data.status;
				if(status == "200"){
					layer.msg("恢复成功！",{icon : 1});
				}else if(status == "300"){
					layer.msg( data.messages+"恢复失败!",{icon : 2});
				}else if(status == "400"){
					layer.msg("恢复失败，链接异常！",{icon : 2});
				}
			}
		})
	}
}



