$(document).ready(function () {
	var userAutority = $("#userAutority").val();
	if(userAutority == 1){
		$("#search_service").empty();
		$("#search_service").attr("disabled",true);
	}else{
		$("#search_service").attr("disabled",false);
	}
	
})


function changeNamespace(){
	$("#search_service").empty();
	var namespace = $("#search_users").val();
	if(namespace == "noNamespace"){
		$("#search_service").attr("disabled",true);
	}else{
		$("#search_service").attr("disabled",false);
		$.ajax({
			url:ctx+"/cluster/getServices.do?namespace="+namespace,
			type : "get",
			success: function(data){
				data = eval("(" + data + ")");
				var serviceNameHtml = '';
				if( data.length == 0 ){
					serviceNameHtml = '<option value="">无服务</option>'
				}else{
					serviceNameHtml = '<option value="">-----请选择-----</option>';
					for(var i=0; i< data.length; i++){
						var serviceName = data[i].metadata.name;
						serviceNameHtml += '<option value="'+serviceName+'">'+serviceName+'</option>';
					}
				}
				$("#search_service").append(serviceNameHtml);
			}
		})
	}
}

function searchServiceDiff(){
	$("#sameNodes").empty();
	var userAutority = $("#userAutority").val();
	var namespace = "";
	if(userAutority == 1){
		namespace = $("#search_users").val();
	}else{
		namespace = $("#userName").val();
	}
	var serviceName = $("#search_service").val();
	$.ajax({
		url:ctx+"/cluster/getDiff.do?namespace="+namespace+"&serviceName="+serviceName,
		type : "get",
		success: function(data){
			data = eval("(" + data + ")");
			var sameNode = data.same[0].hosts;
			var sameNodeCon = "";
			for(var i=0; i< sameNode.length; i++){
				sameNodeCon += sameNode[i]+"/";
			}
			$("#sameNodes").html(sameNodeCon);
		}
	})
}




