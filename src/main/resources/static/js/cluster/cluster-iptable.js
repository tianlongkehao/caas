$(document).ready(function () {
	showCompIptablesList();
	//首次加载界面，若是admin登录，没选择租户时服务为disabled
	var userAutority = $("#userAutority").val();
	if(userAutority == 1){
		$("#search_service").empty();
		$("#search_service").attr("disabled",true);
	}else{
		$("#search_service").attr("disabled",false);
	}
	
	$("#checkIptables").click(function(){
		$(this).addClass("active");
		$("#compIptables").removeClass("active");
		$(".checkIptablesCon").show();
		$(".compIptablesCon").hide();
	});
	
	$("#compIptables").click(function(){
		$(this).addClass("active");
		$("#checkIptables").removeClass("active");
		$(".checkIptablesCon").hide();
		$(".compIptablesCon").show();
	});
	//失败
	$(".btnDanger").click(function(){
		$(this).addClass("btn-danger").removeClass("btn-default");
		$(".btnDefault").addClass("btn-default").removeClass("btn-danger");
		var problemServicesHtml = "";
		for(var Key in problemServices){
			var externalAccess = problemServices[Key].externalAccess;
			var internalAccess = problemServices[Key].internalAccess;
			var others = problemServices[Key].others;
			var externalAccessHtml = "";
			for(var m in externalAccess){
				externalAccessHtml += '<span>'+externalAccess[m]+'</span><br>'
			}
			var internalAccessHtml = "";
			for(var n in internalAccess){
				internalAccessHtml += '<span>'+internalAccess[n]+'</span><br>'
			}
			var othersHtml="";
			for(var i in others){
				othersHtml += '<span>'+others[i]+'</span><br>'
			}
			problemServicesHtml += '<tr>'
				+'<td style="width:15%;padding-left:20px">'+Key+'</td>'
				+'<td style="width:10%;">失败 </td>'
				+'<td class="resultInfo"><p>externalAccess:[&nbsp;'+externalAccessHtml+']</p>'
				+'<p>internalAccess:[&nbsp;'+internalAccessHtml+']</p>'
				+'<p>others:[&nbsp;'+othersHtml+']</p></td>'
				+'</tr>';
		}
		$("#checkResultList").empty().append(problemServicesHtml);
	});
	
	//all
	$(".btnDefault").click(function(){
		$(this).addClass("btn-danger").removeClass("btn-default");
		$(".btnDanger").addClass("btn-default").removeClass("btn-danger");
		var allServicesHtml = "";
		for(var Key in allServices){
			var externalAccess = problemServices[Key].externalAccess;
			var internalAccess = problemServices[Key].internalAccess;
			var others = problemServices[Key].others;
			var status = "";
			if(externalAccess.length==0 && internalAccess.length ==0 && others.length == 0){
				status ="成功";
			}else{
				status ="失败";
			}
			var externalAccessHtml = "";
			for(var m in externalAccess){
				externalAccessHtml += '<span>'+externalAccess[m]+'</span><br>'
			}
			var internalAccessHtml = "";
			for(var n in internalAccess){
				internalAccessHtml += '<span>'+internalAccess[n]+'</span><br>'
			}
			var othersHtml="";
			for(var i in others){
				othersHtml += '<span>'+others[i]+'</span><br>'
			}
			allServicesHtml += '<tr>'
				+'<td style="width:15%;padding-left:20px">'+Key+'</td>'
				+'<td style="width:10%;">'+status+'</td>'
				+'<td class="resultInfo"><p>externalAccess:[&nbsp;'+externalAccessHtml+']</p>'
				+'<p>internalAccess:[&nbsp;'+internalAccessHtml+']</p>'
				+'<p>others:[&nbsp;'+othersHtml+']</p></td>'
				+'</tr>';
			
			
		}
		$("#checkResultList").empty().append(allServicesHtml);
	});
	
});

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
					serviceNameHtml = '<option value="">无服务</option>';
				}else{
					serviceNameHtml = '<option value="">-----请选择-----</option>';
					for(var i=0; i< data.length; i++){
						var serviceName = data[i].metadata.name;
						serviceNameHtml += '<option value="'+serviceName+'">'+serviceName+'</option>';
					}
				}
				$("#search_service").append(serviceNameHtml);
			}
		});
	}
}

function searchServiceShowIptables(){
	$("#sameNodes").empty();
	$("#sameTableList").empty();
	$("#diffTable").empty;
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
			var sameNode = "";
			var sameNodeCon = "";
			if(data.same.length == 0){
				sameNodeCon = "此服务所有节点的iptables均不相同";
				$("#sameNodes").html(sameNodeCon);
			}else{
				sameNode = data.same[0].hosts;
				for(var i=0; i< sameNode.length; i++){
					sameNodeCon += sameNode[i]+"/";
				}
				$("#sameNodes").html(sameNodeCon);
				//相同的nat
				var dataSame = data.same[0].item;
				var sameNat = dataSame.nat;
				var sameFilter = dataSame.filter;

				var sameNatKubeSep = sameNat.kubeSep;
				var sameNatKubeServices = sameNat.kubeServices;
				var sameNatKubeNodePorts = sameNat.kubeNodePorts;
				var sameNatkubeSvc = sameNat.kubeSvc;
				var sameNatOthers = sameNat.others;

				var sameFilterKubeServices = sameFilter.kubeServices;
				var sameFilterOthers = sameFilter.others;

				var sameNatLength = 1;
				if(sameNatKubeSep != null){
					sameNatLength += sameNatKubeSep.length;
				}
				if(sameNatKubeServices != null){
					sameNatLength += sameNatKubeServices.length;
				}
				if(sameNatKubeNodePorts != null){
					sameNatLength += sameNatKubeNodePorts.length;
				}
				if(sameNatkubeSvc != null){
					sameNatLength += sameNatkubeSvc.length;
				}
				if(sameNatOthers != null){
					sameNatLength += sameNatOthers.length;
				}

				var sameFilterLength = 1;
				if(sameFilterKubeServices != null){
					sameFilterLength += sameFilterKubeServices.length;
				}
				if(sameFilterOthers != null){
					sameFilterLength += sameFilterOthers.length;
				}
				//sameNat
				var sameNatHtml = '<tr class="u-line">'+
									'<td rowspan="'+sameNatLength+'">&nbsp;&nbsp;&nbsp;&nbsp;nat&nbsp;&nbsp;</td>'+
									'<td class="td-line" style="color:#ccc">*nat</td>'+
									'</tr>';
				//same Nat KubeSep
				if(sameNatKubeSep != null){
					for(var sepNum=0; sepNum<sameNatKubeSep.length; sepNum++){
						sameNatHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameNatKubeSep[sepNum]+'</td>'+
						'</tr>';
					}
				}
				//same Nat KubeServices
				if(sameNatKubeServices != null){
					for(var serviceNum=0; serviceNum<sameNatKubeServices.length; serviceNum++){
						sameNatHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameNatKubeServices[serviceNum]+'</td>'+
						'</tr>';
					}
				}
				//sameNatKubeNodePorts
				if(sameNatKubeNodePorts != null){
					for(var nodeportNum=0; nodeportNum<sameNatKubeNodePorts.length; nodeportNum++){
						sameNatHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameNatKubeNodePorts[nodeportNum]+'</td>'+
						'</tr>';
					}
				}
				//sameNatkubeSvc
				if(sameNatkubeSvc != null){
					for(var svcNum=0; svcNum<sameNatkubeSvc.length; svcNum++){
						sameNatHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameNatkubeSvc[svcNum]+'</td>'+
						'</tr>';
					}
				}
				//sameNatOthers
				if(sameNatOthers != null){
					for(var othersNum=0; othersNum<sameNatOthers.length; othersNum++){
						sameNatHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameNatOthers[othersNum]+'</td>'+
						'</tr>';
					}
				}

				$("#sameTableList").append(sameNatHtml);

				//filter
				samefilterHtml = '<tr class="u-line">'+
									'<td rowspan="'+sameFilterLength+'">&nbsp;&nbsp;&nbsp;&nbsp;filter&nbsp;&nbsp;</td>'+
									'<td class="td-line" style="color:#ccc">*filter</td>'+
								  '</tr>';
				if(sameFilterKubeServices != null){
					for(var filterServicesNum=0; filterServicesNum<sameFilterKubeServices.length; filterServicesNum++){
						samefilterHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameFilterKubeServices[filterServicesNum]+'</td>'+
						'</tr>';
					}
				}
				if(sameFilterOthers != null){
					for(var filterothersNum=0; filterothersNum<sameFilterOthers.length; filterothersNum++){
						samefilterHtml += '<tr class="u-line">'+
						'<td class="td-line">'+sameFilterOthers[filterothersNum]+'</td>'+
						'</tr>';
					}
				}
				$("#sameTableList").append(samefilterHtml);

				/*var sameNatHtml = '<tr class="u-line">'+
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
				$("#sameTableList").append(samefilterHtml);*/
			}

			//不同的iptables
			if(data.diff.length == 0){
				diffNodeCon = '<span>&nbsp;&nbsp;&nbsp;&nbsp;此服务所有节点的iptables均相同<span>';
				$("#diffTable").html(diffNodeCon);
			}else{
				loadDiff(data);
			}

		}
	});
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
			var sepLeftLength = 0;
			var sepRigthLength = 0;
			if(diffNatSep.flag == false){
				if(diffNatSep.diffLeft != null){
					sepLeftLength = diffNatSep.diffLeft.length;
				}
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
			var servicesLeftLength = 0;
			var servicesRightLength = 0;
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
			var portLeftLength = 0;
			var portRightLength = 0;
			if(diffNatNodePorts.flag == false){
				if(diffNatNodePorts.diffLeft != null){
					portLeftLength = diffNatNodePorts.diffLeft.length;
				}
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
			var svcLeftLength = 0;
			var svcRightLength = 0;
			if(diffSvc.flag == false){
				if(diffSvc.diffLeft != null){
					svcLeftLength = diffSvc.diffLeft.length;
				}
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
			var othersLeftLength = 0;
			var othersRightLength = 0;
			if(diffOthers.flag == false){
				if(diffOthers.diffLeft != null){
					othersLeftLength = diffOthers.diffLeft.length;
				}
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
			//kubeSep content
			if(diffNatSep.flag == true){
				diffNodeHtml += '<td class="td-line">&nbsp;&nbsp;&nbsp;&nbsp;kubeSep</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<td class="td-line" rowspan="'+natSepLength+'">&nbsp;&nbsp;kubeSep</td>';
				if(natSepLength == 1){
					if(sepLeftLength == 0 && sepRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffNatSep.diffRight[0]+'</td>'+
											'</tr>';
					}else if(sepLeftLength == 1 && sepRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffNatSep.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffNatSep.diffLeft[0]+'</td>'+
										'<td class="td-line">'+diffNatSep.diffRight[0]+'</td>'+
										'</tr>';
					}
				}else{
					if(sepLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
						'<td class="td-line">'+diffNatSep.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natSepLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffNatSep.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(sepRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffNatSep.diffLeft[0]+'</td>'+
						'<td class="td-line">&nbsp;</td>'+
						'</tr>';
						for(var i=1; i<natSepLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffNatSep.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffNatSep.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffNatSep.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natSepLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffNatSep.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffNatSep.diffRight[i]+'</td>'+
												'</tr>';
						}
					}

				}
			}
			//kubeServices content
			if(diffNatServices.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+natServicesLength+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>';
				if(natServicesLength == 1){
					if(servicesLeftLength == 0 && servicesRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffNatServices.diffRight[0]+'</td>'+
											'</tr>';
					}else if(servicesLeftLength == 1 && servicesRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffNatServices.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffNatServices.diffLeft[0]+'</td>'+
											'<td class="td-line">'+diffNatServices.diffRight[0]+'</td>'+
											'</tr>';
					}
				}else{
					if(servicesLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
						'<td class="td-line">'+diffNatServices.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natServicesLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffNatServices.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(servicesRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffNatServices.diffLeft[0]+'</td>'+
						'<td class="td-line">&nbsp;</td>'+
						'</tr>';
						for(var i=1; i<natServicesLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffNatServices.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffNatServices.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffNatServices.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natServicesLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffNatServices.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffNatServices.diffRight[i]+'</td>'+
												'</tr>';
						}
					}

				}
			}
			//kubeNodePorts content
			if(diffNatNodePorts.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeNodePorts</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+natNodePortsLength+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeNodePorts</td>';
				if(natNodePortsLength == 1){
					if(portLeftLength == 0 && portRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffNatNodePorts.diffRight[0]+'</td>'+
											'</tr>';
					}else if(portLeftLength == 1 && portRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffNatNodePorts.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffNatNodePorts.diffLeft[0]+'</td>'+
										'<td class="td-line">'+diffNatNodePorts.diffRight[0]+'</td>'+
										'</tr>';
					}
				}else{
					if(portLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
										'<td class="td-line">'+diffNatNodePorts.diffRight[0]+'</td>'+
										'</tr>';
						for(var i=1; i<natNodePortsLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffNatNodePorts.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(portRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffNatNodePorts.diffLeft[0]+'</td>'+
										'<td class="td-line">&nbsp;</td>'+
										'</tr>';
						for(var i=1; i<natNodePortsLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffNatNodePorts.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffNatNodePorts.diffLeft[0]+'</td>'+
										'<td class="td-line">'+diffNatNodePorts.diffRight[0]+'</td>'+
										'</tr>';
						for(var i=1; i<natNodePortsLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffNatNodePorts.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffNatNodePorts.diffRight[i]+'</td>'+
												'</tr>';
						}
					}

				}

			}
			//kubeSvc content
			if(diffSvc.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;kubeSvc</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+natSvcLength+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeSvc</td>';
				if(natSvcLength == 1){
					if(svcLeftLength == 0 && svcRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffSvc.diffRight[0]+'</td>'+
											'</tr>';
					}else if(svcLeftLength == 1 && svcRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffSvc.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffSvc.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffSvc.diffRight[0]+'</td>'+
						'</tr>';
					}
				}else{
					if(svcLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
						'<td class="td-line">'+diffSvc.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natSvcLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffSvc.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(svcRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffSvc.diffLeft[0]+'</td>'+
						'<td class="td-line">&nbsp;</td>'+
						'</tr>';
						for(var i=1; i<natSvcLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffSvc.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffSvc.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffSvc.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natSvcLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffSvc.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffSvc.diffRight[i]+'</td>'+
												'</tr>';
						}
					}

				}
			}
			//others content
			if(diffOthers.flag == true){
				diffNodeHtml += '<tr class="u-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;others</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'<td class="td-line">&nbsp;</td>'+
									'</tr>';
			}else{
				diffNodeHtml += '<tr class="u-line"><td rowspan="'+natOthersLength+'">&nbsp;&nbsp;&nbsp;&nbsp;others</td>';
				if(natOthersLength == 1){
					if(othersLeftLength == 0 && othersRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffOthers.diffRight[0]+'</td>'+
											'</tr>';
					}else if(othersLeftLength == 1 && othersRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffOthers.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffOthers.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffOthers.diffRight[0]+'</td>'+
						'</tr>';
					}
				}else{
					if(othersLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
						'<td class="td-line">'+diffOthers.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natOthersLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffOthers.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(othersRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffOthers.diffLeft[0]+'</td>'+
						'<td class="td-line">&nbsp;</td>'+
						'</tr>';
						for(var i=1; i<natOthersLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffOthers.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffOthers.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffOthers.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<natOthersLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffOthers.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffOthers.diffRight[i]+'</td>'+
												'</tr>';
						}
					}

				}
			}

			//filter
			var diffFilterServices = dataDiff[diffNum].filter.kubeServices;
			var diffFilterOthers = dataDiff[diffNum].filter.others;
			var diffFilterNum = 0;
			//filter kubeServices
			var filterServicesLength = 1;
			var filterServicesLeftLength = 0;
			var filterServicesRightLength = 0;
			if(diffFilterServices.flag == false){
				if(diffFilterServices.diffLeft != null){
					filterServicesLeftLength = diffFilterServices.diffLeft.length;
				}
				if(diffFilterServices.diffRight != null){
					filterServicesRightLength = diffFilterServices.diffRight.length;
				}
				filterServicesLength = (filterServicesLeftLength > filterServicesRightLength) ? filterServicesLeftLength :filterServicesRightLength;
				diffFilterNum += filterServicesLength;
			}else{
				diffFilterNum += 1;
			}
			//filter kubeOthers
			var filterOthersLength = 1;
			var filterOthersLeftLength = 0;
			var filterOthersRightLength = 0;
			if(diffFilterOthers.flag == false){
				if(diffFilterOthers.diffLeft != null){
					filterOthersLeftLength = diffFilterOthers.diffLeft.length;
				}
				if(diffFilterOthers.diffRight != null){
					filterOthersRightLength = diffFilterOthers.diffRight.length;
				}
				filterOthersLength = (filterOthersLeftLength > filterOthersRightLength) ? filterOthersLeftLength :filterOthersRightLength;
				diffFilterNum += filterOthersLength;
			}else{
				diffFilterNum += 1;
			}

			diffNodeHtml += '<tr class="u-line">'+
						'<td rowspan="'+diffFilterNum+'">&nbsp;&nbsp;&nbsp;&nbsp;filter&nbsp;&nbsp;</td>';
			//filter kubeServices content
			if(diffFilterServices.flag == true){
				diffNodeHtml += '<td class="td-line">&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>'+
							'<td class="td-line">&nbsp;</td>'+
							'<td class="td-line">&nbsp;</td>'+
							'</tr>';
			}else{
				diffNodeHtml += '<td class="td-line" rowspan="'+filterServicesLength+'">&nbsp;&nbsp;&nbsp;&nbsp;kubeServices</td>';
				if(filterServicesLength == 1){
					if(filterServicesLeftLength == 0 && filterServicesRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffFilterServices.diffRight[0]+'</td>'+
											'</tr>';
					}else if(filterServicesLeftLength == 1 && filterServicesRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffFilterServices.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffFilterServices.diffLeft[0]+'</td>'+
											'<td class="td-line">'+diffFilterServices.diffRight[0]+'</td>'+
											'</tr>';
					}
				}else{
					if(filterServicesLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
						'<td class="td-line">'+diffFilterServices.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<filterServicesLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffFilterServices.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(filterServicesRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffFilterServices.diffLeft[0]+'</td>'+
						'<td class="td-line">&nbsp;</td>'+
						'</tr>';
						for(var i=1; i<filterServicesLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffFilterServices.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffFilterServices.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffFilterServices.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<filterServicesLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffFilterServices.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffFilterServices.diffRight[i]+'</td>'+
												'</tr>';
						}
					}

				}
			}
			if(diffFilterOthers.flag == true){
				diffNodeHtml += '<tr class="u-line td-line"><td>&nbsp;&nbsp;&nbsp;&nbsp;others</td>'+
							'<td class="td-line">&nbsp;</td>'+
							'<td class="td-line">&nbsp;</td>'+
							'</tr>';
			}else{
				diffNodeHtml += '<td rowspan="'+filterOthersLength+'">&nbsp;&nbsp;&nbsp;&nbsp;others</td>';
				if(filterOthersLength == 1){
					if(filterOthersLeftLength == 0 && filterOthersRightLength == 1){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
											'<td class="td-line">'+diffFilterOthers.diffRight[0]+'</td>'+
											'</tr>';
					}else if(filterOthersLeftLength == 1 && filterOthersRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffFilterOthers.diffLeft[0]+'</td>'+
											'<td class="td-line">&nbsp;</td>'+
											'</tr>';
					}else{
						diffNodeHtml += '<td class="td-line">'+diffFilterOthers.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffFilterOthers.diffRight[0]+'</td>'+
						'</tr>';
					}
				}else{
					if(filterOthersLeftLength == 0){
						diffNodeHtml += '<td class="td-line">&nbsp;</td>'+
						'<td class="td-line">'+diffFilterOthers.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<filterOthersLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">&nbsp;</td>'+
												'<td class="td-line">'+diffFilterOthers.diffRight[i]+'</td>'+
												'</tr>';
						}
					}else if(filterOthersRightLength == 0){
						diffNodeHtml += '<td class="td-line">'+diffFilterOthers.diffLeft[0]+'</td>'+
						'<td class="td-line">&nbsp;</td>'+
						'</tr>';
						for(var i=1; i<filterOthersLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffFilterOthers.diffLeft[i]+'</td>'+
												'<td class="td-line">&nbsp;</td>'+
												'</tr>';
						}
					}else{
						diffNodeHtml += '<td class="td-line">'+diffFilterOthers.diffLeft[0]+'</td>'+
						'<td class="td-line">'+diffFilterOthers.diffRight[0]+'</td>'+
						'</tr>';
						for(var i=1; i<filterOthersLength; i++){
							diffNodeHtml += '<tr class="u-line"><td class="td-line">'+diffFilterOthers.diffLeft[i]+'</td>'+
												'<td class="td-line">'+diffFilterOthers.diffRight[i]+'</td>'+
												'</tr>';
						}
					}
				}
			}
			diffNodeHtml +='</tbody></table>';
		}
		$("#diffTable").append(diffNodeHtml);
	}
}

var resultData = "";
function showCompIptablesList(){
	$.ajax({
		url:ctx + "/cluster/checkIptables.do",
		type:'get',
		success:function(data){
			var data = eval('('+data+')');
			if(data.status == '200'){
				resultData = data.resultList;
				var tableHtml ="";
				for(var i=0; i<resultData.length; i++){
					var nodeName = resultData[i].nodeName;
					var problem = resultData[i].problem;
					var problemHtml = "";
					if(problem == true){
						problemHtml = "正常";
					}else{
						problemHtml = "有问题";
					}
					tableHtml += '<tr>'
									+'<td><input type="checkbox" class="chkItems" nodeName="'+nodeName+'"/></td>'
									+'<td><a class="checkResult" onclick="checkResultDetail(this)" nodeName="'+nodeName+'">'+nodeName+'</a></td>'
									+'<td>'+problemHtml+'</td>'
									+'<td class="iptableOpr">'
									//+'<a><i>测试</i></a>'
									+'<a onclick="recoverOneIptables(this)" nodeName="'+nodeName+'"><i>恢复</i></a>'
									+'</td>'
									+'</tr>';
				}
				$("#compIptablesList").append(tableHtml);
			}
		}
	})
}
var problemServices = "";
var allServices ="";
function checkResultDetail(obj){
	layer.open({
		type:1,
		title : '转发规则详细信息',
		content : $(".checkResultDetailInfo"),
		area:['1000px','600px'],
		btn : ['关闭'],
		yes : function(index, layero) {
			layer.close(index);
		}
	});
	var nodeName = $(obj).attr("nodeName");
	var problemServicesHtml = "";
	for(var j = 0; j< resultData.length; j++){
		if(resultData[j].nodeName == nodeName){
			problemServices = resultData[j].problemServices;
			allServices = resultData[j].allServices;
			//$("#option1").val(problemServices);
			//$("#option2").val(allServices);
			var problemServicesHtml = "";
			for(var Key in problemServices){
				var externalAccess = problemServices[Key].externalAccess;
				var internalAccess = problemServices[Key].internalAccess;
				var others = problemServices[Key].others;
				var externalAccessHtml = "";
				for(var m in externalAccess){
					externalAccessHtml += '<span>'+externalAccess[m]+'</span><br>'
				}
				var internalAccessHtml = "";
				for(var n in internalAccess){
					internalAccessHtml += '<span>'+internalAccess[n]+'</span><br>'
				}
				var othersHtml="";
				for(var i in others){
					othersHtml += '<span>'+others[i]+'</span><br>'
				}
				problemServicesHtml += '<tr>'
					+'<td style="width:15%;padding-left:20px">'+Key+'</td>'
					+'<td style="width:10%;">失败 </td>'
					+'<td class="resultInfo"><p>externalAccess:[&nbsp;'+externalAccessHtml+']</p>'
					+'<p>internalAccess:[&nbsp;'+internalAccessHtml+']</p>'
					+'<p>others:[&nbsp;'+othersHtml+']</p></td>'
					+'</tr>';
			}
		}
	}
	$("#checkResultList").empty().append(problemServicesHtml);
}
//恢复一个iptable
function recoverOneIptables(obj){
	var nodeName = $(obj).attr("nodeName");
	var nodeNameArray = new Array();
	nodeNameArray.push(nodeName);
	var oneNodeData = {"nodeNameListString":JSON.stringify(nodeNameArray)};
	$.ajax({
		url:ctx + "/cluster/recoverIptables.do",
		type:'post',
		data:oneNodeData,
		success:function(data){
			var data = eval('('+data+')');
			var status = data.status;
		}
	})
}

//批量恢复iptable
function recoverIptables(){
	
	var nodeData = new Array();
	var checkIptable = $(".chkItems:checked");
	if(checkIptable.length == 0){
		layer.tips('请选择至少一个集群节点','#checkallbox', {
			tips : [ 1, '#3595CC' ]
		});
		$('#checkallbox').focus();
		return;
	}else{
		for(var i=0; i<checkIptable.length; i++){
			nodeData.push(checkIptable[i]);
		}
		var oneNodeData = {"nodeNameListString":JSON.stringify(nodeData)};
		$.ajax({
			url:ctx + "/cluster/recoverIptables.do",
			type:'post',
			data:oneNodeData,
			success:function(data){
				var status = data.status;
			}
		})
	}
	
}














