var loading = "";
$(document).ready(function () {
	loading = layer.load(0, {
		shade : [ 0.3, '#000' ]
	});
	loadRoute();
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
	$.ajax({
		 url : ctx + "/cluster/checkRoute.do",
		 type : "get",
		 success : function(data){
			 layer.closeAll();
			 var data = eval("(" + data + ")");
			 var status = data.status;
			 if(status == '200'){
				 var nodeList = data.nodeList;
				 var routeHtmlTr = "";
				 for(var i=0; i<nodeList.length; i++){
					 var nodeIp = nodeList[i].nodeIp;
					 var nodeName = nodeList[i].nodeName;
					 var problem = nodeList[i].problem;
					 var problemHtml = "";
					 if(problem == 'false'){
						 problemHtml = "成功";
					 }else if(problem == 'true'){
						 problemHtml = "失败";
					 }else if(problem == 'unknown'){
						 problemHtml = "未知";
					 }
					 routeHtmlTr += '<tr>'
						//+'<td><input type="checkbox" class="chkItem" nodeIp="'+nodeIp+'" nodeName="'+nodeName+'"/></td>'
						+'<td style="text-indent:10px">'+nodeName+'（'+nodeIp+'）</td>'
						+'<th>&nbsp;</th>'
						+'<th>&nbsp;</th>'
						+'<td>'+problemHtml+'</td>'
						+'<td class="routeBtns">'
						+'<a class="fa-caret" nodeIp="'+nodeIp+'" onclick="nodeTargetIPDetail(this)"><i class="fa fa-caret-right" flag="1">测试</i></a>'
						+'<a onclick="recoverOneRoute(this)" nodeIp="'+nodeIp+'" nodeName="'+nodeName+'"><i>恢复</i></a>'
						+'</td>'
						+'</tr>';
				 }
				 $("#routeList").empty().append(routeHtmlTr);
				 $('.dataTables-example').dataTable({
					    "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 1,2,4] }],
					    "aaSorting": [[ 0, "desc" ]]
				 });
				$("#checkallbox").parent().removeClass("sorting_asc");
			 }
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
							//+'<td>&nbsp;</td>'
							+'<td style="text-indent:10px">targetIP</td>'
							+'<td>期望网关</td>'
							+'<td>实际网关</td>'
							+'<td>结果</td>'
							+'<td>&nbsp;</td>'
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
							//+'<td>&nbsp;</td>'
							+'<td style="text-indent:10px">'+targetIP+'</td>'
							+'<td>'+expectedGW+'</td>'
							+'<td>'+realGW+'</td>'
							+'<td>'+successResult+'</td>'
							+'<td>&nbsp;</td>'
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
//批量测试路由
function testRoutes(){
	var loading = layer.load(0, {
		shade : [ 0.3, '#000' ]
	});
	
	loadRoute();
}
