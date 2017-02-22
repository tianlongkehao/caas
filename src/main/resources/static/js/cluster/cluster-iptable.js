$(document).ready(function () {
	//首次加载界面，若是admin登录，没选择租户时服务为disabled
	var userAutority = $("#userAutority").val();
	if(userAutority == 1){
		$("#search_service").empty();
		$("#search_service").attr("disabled",true);
	}else{
		$("#search_service").attr("disabled",false);
	}
	
})

//admin登录时选择租户；对应显示该租户下的服务下拉列表
function changeNamespace(){
	$("#search_service").empty();
	var namespace = $("#search_users").val();
	//租户选项为空或者选择相应租户时
	if(namespace == "noNamespace"){
		$("#sameTableList").empty();
		$("#sameNodes").html("请选择需要对比的服务");
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

function searchServiceShowIptables(){
	$("#sameNodes").empty();
	$("#sameTableList").empty();
	var userAutority = $("#userAutority").val();
	var namespace = "";
	//判断登录用户时admin还是租户
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
			//iptables相同的节点名称
			var sameNode = data.same[0].hosts;
			var sameNodeCon = "";
			for(var i=0; i< sameNode.length; i++){
				sameNodeCon += sameNode[i]+"/";
			}
			$("#sameNodes").html(sameNodeCon);
			//相同的nat
			var sameNat1 = data.same[0].item.nat.kubeNodePorts;
			var sameNat2 = data.same[0].item.nat.kubeServices;
			var sameLength = sameNat1.length+sameNat2.length;
			var sameNatHtml = '<tr class="u-line">'+
								'<td rowspan="'+sameLength+'">&nbsp;&nbsp;&nbsp;&nbsp;nat&nbsp;&nbsp;</td>'+
								'<td class="td-line">'+sameNat1[0]+'</td>'+
							  '</tr>';
			for(var j=1; j<sameNat1.length; j++){
				sameNatHtml += '<tr class="u-line">'+
								'<td class="td-line">'+sameNat1[j]+'</td>'+
								'</tr>';
			}
			
			
			for(var k=0; k<sameNat2.length; k++){
				sameNatHtml += '<tr class="u-line">'+
								'<td class="td-line">'+sameNat2[k]+'</td>'+
								'</tr>';
			}
			$("#sameTableList").append(sameNatHtml);
			//相同的filter
			var samefilter = data.same[0].item.filter.kubeServices;
			var samefilterHtml = "";
			if(samefilter == "" || samefilter == null ){
				samefilterHtml = '<tr class="u-line">'+
										'<td>&nbsp;&nbsp;&nbsp;&nbsp;filter</td>'+
										'<td class="td-line">无filter</td>'+
									  '</tr>';
			}else{
				samefilterHtml = '<tr class="u-line">'+
									'<td rowspan="'+samefilter.length+'">&nbsp;&nbsp;&nbsp;&nbsp;filter&nbsp;&nbsp;</td>'+
									'<td class="td-line">'+samefilter[0]+'</td>'+
								  '</tr>';
				for(var m=1; m<samefilter.length; m++){
				samefilterHtml += '<tr class="u-line">'+
							'<td class="td-line">'+samefilter[m]+'</td>'+
							'</tr>';
				}
			}
			$("#sameTableList").append(samefilterHtml);
			
			//不同的iptables
			loadDiff(data);
		}
	})
}

function loadDiff(data){
	$("#diffTable").empty();
	var dataDiff = data.diff;
	var diffNodeHtml = "";
	if(dataDiff.length == 0 ){
		diffNodeHtml += '<table class="table table-striped table-hover differentTable">'+
								'<thead class="diffTable">'+
								'<tr class="u-line">'+
									'<th colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;对比结果无不同的iptables</th>'+
								'</tr>'+
							'</thead>'+
							'<table>';
	}else{
		for(var diffNum=0; diffNum<dataDiff.length; diffNum++){
			var diffNatSep = dataDiff[diffNum].nat.kubeSep;
			var diffNatServices = dataDiff[diffNum].nat.kubeServices;
			var diffNatNodePorts = dataDiff[diffNum].nat.kubeNodePorts;
			var diffSvc = dataDiff[diffNum].nat.kubeSvc;
			var diffOthers = dataDiff[diffNum].nat.others;
			var diffNatNum = 0;
			
			var diffFilterSep = dataDiff[diffNum].filter.kubeSep;
			var diffFilterServices = dataDiff[diffNum].filter.kubeServices;
			var diffFilterNodePorts = dataDiff[diffNum].filter.kubeNodePorts;
			var diffFilterSvc = dataDiff[diffNum].filter.kubeSvc;
			var diffFilterOthers = dataDiff[diffNum].filter.others;
			var diffFilterNum = 0;
			
			var diffLeft = dataDiff[diffNum].left;
			var diffRight = dataDiff[diffNum].right;
			diffNodeHtml += '<table class="table table-hover differentTable">'+
								'<thead class="diffTable'+diffNum+'">'+
								'<tr class="u-line" style="background:#ddd">'+
									'<th colspan="2">&nbsp;&nbsp;相对比的两个节点</th>'+
									'<th class="td-line">&nbsp;&nbsp;'+dataDiff[diffNum].left+'</th>'+
									'<th class="td-line">&nbsp;&nbsp;'+dataDiff[diffNum].right+'</th>'+
								'</tr>'+
							'</thead>'+
							'<tbody class="routeList">';
			//nat
			//kubeSep
			var natSepLength = 1;
			if(diffNatSep.flag == false){
				var sepLeftLength = 1;
				if(diffNatSep.diffLeft != null){
					sepLeftLength = diffNatSep.diffLeft.length;
				}
				var sepRigthLength = 1;
				if(diffNatSep.diffRight != null){
					sepRigthLength = diffNatSep.diffRight.length;
				}
				natSepLength = (sepLeftLength > sepRigthLength) ? sepLeftLength : sepRigthLength;
				diffNatNum += natSepLength;
			}else{
				diffNatNum += 1;
			}
			//kubeService
			var natServicesLength = 1;
			var servicesLeftLength = 1;
			var servicesRightLength = 1;
			if(diffNatServices.flag == false){
				if(diffNatServices.diffLeft != null){
					servicesLeftLength = diffNatServices.diffLeft.length;
				}
				if(diffNatServices.diffRight != null){
					servicesRightLength = diffNatServices.diffRight.length;
				}
				natServicesLength = (servicesLeftLength > servicesRightLength) ? servicesLeftLength :servicesRightLength;
				diffNatNum += natServicesLength;
			}else{
				diffNatNum += 1;
			}
			//kubeNodePorts
			var natNodePortsLength = 1;
			if(diffNatNodePorts.flag == false){
				var portLeftLength = 1;
				if(diffNatNodePorts.diffLeft != null){
					portLeftLength = diffNatNodePorts.diffLeft.length;
				}
				var portRightLength = 1;
				if(diffNatNodePorts.diffRight != null){
					portRightLength = diffNatNodePorts.diffRight.length;
				}
				natNodePortsLength = (portLeftLength > portRightLength) ? portLeftLength :portRightLength;
				diffNatNum += natNodePortsLength;
			}else{
				diffNatNum += 1;
			}
			//kubeSvc
			var natSvcLength = 1;
			if(diffSvc.flag == false){
				var svcLeftLength = 1;
				if(diffSvc.diffLeft != null){
					svcLeftLength = diffSvc.diffLeft.length;
				}
				var svcRightLength = 1;
				if(diffSvc.diffRight != null){
					svcRightLength = diffSvc.diffRight.length;
				}
				natSvcLength = (svcLeftLength > svcRightLength) ? svcLeftLength :svcRightLength;
				diffNatNum += natSvcLength;
			}else{
				diffNatNum += 1;
			}
			//kubeOthers
			var natOthersLength = 1;
			if(diffOthers.flag == false){
				var othersLeftLength = 1;
				if(diffOthers.diffLeft != null){
					othersLeftLength = diffOthers.diffLeft.length;
				}
				var othersRightLength = 1;
				if(diffOthers.diffRight != null){
					othersRightLength = diffOthers.diffRight.length;
				}
				natOthersLength = (othersLeftLength > othersRightLength) ? othersLeftLength :othersRightLength;
				diffNatNum += natOthersLength;
			}else{
				diffNatNum += 1;
			}
			
			diffNodeHtml += '<tr class="u-line">'+
								'<td rowspan="'+diffNatNum+'">&nbsp;&nbsp;&nbsp;&nbsp;nat&nbsp;&nbsp;</td>';
			if(diffNatSep.flag == true){
				diffNodeHtml += '<td class="td-line">&nbsp;&nbsp;&nbsp;&nbsp;kubeSep</td>'+
									'<td class="td-line">无</td>'+
									'<td class="td-line">无</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<td class="td-line" rowspan="'+diffNatSep.diffLeft.length+'">&nbsp;&nbsp;kubeSep</td>'+
									'<td class="td-line">'+diffNatSep.diffLeft[0]+'</td>'+
									'<td class="td-line">'+diffNatSep.diffRight[0]+'</td>'+
									'</tr>';
			}
			//kubeServices content
			if(diffNatServices.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>'+
									'<td class="td-line">无</td>'+
									'<td class="td-line">无</td>'+
									'</tr>';
			}else{
				
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+natServicesLength+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>';
				if(natServicesLength == 1){
					if(servicesLeftLength == 1){
						diffNodeHtml += '<td class="td-line">'+diffNatServices.diffLeft[0]+'</td>'+
											'<td class="td-line">'+diffNatServices.diffRight[0]+'</td>'+
											'</tr>';
					}
				}
									'<td class="td-line">'+diffNatServices.diffLeft[0]+'</td>'+
									'<td class="td-line">'+diffNatServices.diffRight[0]+'</td>'+
									'</tr>';
			}
			if(diffNatNodePorts.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeNodePorts</td>'+
									'<td class="td-line">无</td>'+
									'<td class="td-line">无</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+diffNatNodePorts.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeNodePorts</td>'+
									'<td class="td-line">'+diffNatNodePorts.diffLeft[0]+'</td>'+
									'<td class="td-line">'+diffNatNodePorts.diffRight[0]+'</td>'+
									'</tr>';
			}
			if(diffSvc.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeSvc</td>'+
									'<td class="td-line">无</td>'+
									'<td class="td-line">无</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+diffSvc.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeSvc</td>'+
									'<td class="td-line">'+diffSvc.diffLeft[0]+'</td>'+
									'<td class="td-line">'+diffSvc.diffRight[0]+'</td>'+
									'</tr>';
			}
			if(diffOthers.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;others</td>'+
									'<td class="td-line">无</td>'+
									'<td class="td-line">无</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+diffOthers.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;others</td>'+
									'<td class="td-line">'+diffOthers.diffLeft[0]+'</td>'+
									'<td class="td-line">'+diffOthers.diffRight[0]+'</td>'+
									'</tr>';
			}
								
			/*//filter
			//var diffFilterSep = dataDiff[diffNum].filter.kubeSep;
			var diffFilterServices = dataDiff[diffNum].filter.kubeServices;
			//var diffFilterNodePorts = dataDiff[diffNum].filter.kubeNodePorts;
			//var diffFilterSvc = dataDiff[diffNum].filter.kubeSvc;
			var diffFilterOthers = dataDiff[diffNum].filter.others;
			var diffFilterNum = 0;
//			if(diffFilterSep.flag == false){
//				diffFilterNum += diffFilterSep.diffLeft.length;
//			}else{
//				diffFilterNum += 1;
//			}
			if(diffFilterServices.flag == false){
				diffFilterNum += diffFilterServices.diffLeft.length
			}else{
				diffFilterNum += 1;
			}
//			if(diffFilterNodePorts.flag == false){
//				diffFilterNum += diffFilterNodePorts.diffLeft.length
//			}else{
//				diffFilterNum += 1;
//			}
//			if(diffFilterSvc.flag == false){
//				diffFilterNum += diffFilterSvc.diffLeft.length
//			}else{
//				diffFilterNum += 1;
//			}
			if(diffFilterOthers.flag == false){
				diffFilterNum += diffFilterOthers.diffLeft.length
			}else{
				diffFilterNum += 1;
			}
			
			diffNodeHtml += '<tr class="u-line">'+
						'<td rowspan="'+diffFilterNum+'">&nbsp;&nbsp;&nbsp;&nbsp;filter&nbsp;&nbsp;</td>';
//			if(diffFilterSep.flag == true){
//				diffNodeHtml += '<td class="td-line">&nbsp;&nbsp;&nbsp;&nbsp;kubeSep</td>'+
//								'<td class="td-line">无</td>'+
//								'<td class="td-line">无</td>'+
//								'</tr>';
//			}else{
//				diffNodeHtml += '<td class="td-line" rowspan="'+diffFilterSep.diffLeft.length+'">&nbsp;&nbsp;kubeSep</td>'+
//							'<td class="td-line">'+diffFilterSep.diffLeft[0]+'</td>'+
//							'<td class="td-line">'+diffFilterSep.diffRight[0]+'</td>'+
//							'</tr>';
//			}
			if(diffFilterServices.flag == true){
				diffNodeHtml += '<td class="td-line">&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>'+
							'<td class="td-line">无</td>'+
							'<td class="td-line">无</td>'+
							'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td class="td-line" rowspan="'+diffFilterServices.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>'+
							'<td class="td-line">'+diffFilterServices.diffLeft[0]+'</td>'+
							'<td class="td-line">'+diffFilterServices.diffRight[0]+'</td>'+
							'</tr>';
			}
//			if(diffFilterNodePorts.flag == true){
//				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeNodePorts</td>'+
//							'<td class="td-line">无</td>'+
//							'<td class="td-line">无</td>'+
//							'</tr>';
//			}else{
//				diffNodeHtml += '<tr class="u-line"><td rowspan="'+diffFilterNodePorts.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeNodePorts</td>'+
//							'<td class="td-line">'+diffFilterNodePorts.diffLeft[0]+'</td>'+
//							'<td class="td-line">'+diffFilterNodePorts.diffRight[0]+'</td>'+
//							'</tr>';
//			}
//			if(diffFilterSvc.flag == true){
//				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeSvc</td>'+
//							'<td class="td-line">无</td>'+
//							'<td class="td-line">无</td>'+
//							'</tr>';
//			}else{
//				diffNodeHtml += '<tr class="u-line"><td rowspan="'+diffFilterSvc.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeSvc</td>'+
//							'<td class="td-line">'+diffFilterSvc.diffLeft[0]+'</td>'+
//							'<td class="td-line">'+diffFilterSvc.diffRight[0]+'</td>'+
//							'</tr>';
//			}
			if(diffFilterOthers.flag == true){
				diffNodeHtml += '<tr class="u-line td-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;others</td>'+
							'<td class="td-line">无</td>'+
							'<td class="td-line">无</td>'+
							'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line td-line"><td rowspan="'+diffFilterOthers.diffLeft.length+'">&nbsp;&nbsp;&nbsp;&nbsp;others</td>'+
							'<td class="td-line">'+diffFilterOthers.diffLeft[0]+'</td>'+
							'<td class="td-line">'+diffFilterOthers.diffRight[0]+'</td>'+
							'</tr>';
			}						*/
							
				
			diffNodeHtml +='</tbody></table>';
				
		}
		
		$("#diffTable").append(diffNodeHtml);
		
	}
}


