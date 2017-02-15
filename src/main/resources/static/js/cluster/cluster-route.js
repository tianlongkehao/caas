$(document).ready(function () {
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
})


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
				 if(success){
					 successResult = "成功";
				 }else{
					 successResult = "失败";
				 }
				 htmlTr += '<tr>'+
								'<th>&nbsp;</th>'+
								'<th>'+targetIP+'</th>'+
								'<th>'+successResult+'</th>'+
							'</tr>';
			 }
			 $("#routeList").append(htmlTr);
		 }
	 });
}

function searchRouteNode(){
	$("#routeList").empty();
	loadRoute();
}



