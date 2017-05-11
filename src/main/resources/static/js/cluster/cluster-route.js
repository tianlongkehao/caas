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


///**
// * 加载路由监控数据
// */
//function loadRoute() {
//	var nodeIP = $("#search_routeNode").val();
//	$.ajax({
//		 url : ctx + "/cluster/getRouteTable.do?ip="+nodeIP,
//		 type : "get",
//		 success : function(data){
//			 var data = eval("(" + data + ")");
//			 var targetNum = data.items;
//			 var htmlTr = "";
//			 for(var i=0; i<targetNum.length; i++){
//				 var targetIP = targetNum[i].targetIP;
//				 var success = targetNum[i].success;
//				 var successResult = "";
//				 if(success){
//					 successResult = "成功";
//				 }else{
//					 successResult = "失败";
//				 }
//				 htmlTr += '<tr>'+
//								'<th>&nbsp;</th>'+
//								'<th>'+targetIP+'</th>'+
//								'<th>'+successResult+'</th>'+
//							'</tr>';
//			 }
//			 $("#routeList").append(htmlTr);
//		 }
//	 });
//}

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



