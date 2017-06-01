function detail(obj) {
	var nodename = $(obj).attr('nodename');
	clearDetailData();

	$.ajax({
		url:ctx + "/cluster/excutetestResult?nodename="+nodename,
		type:"get",
		success:function(data){
			var data = eval("(" + data + ")");
			var status = data.status;
			var nodetestresult = data.nodetestresult;
			if(status == '200'){
				layer.open({
					type : 1,
					title : nodename + '节点检测信息',
					content : $("#detail"),
					area : [ '600px','520px' ],
					btn : ['关闭']
				})
				var tabHtml = "";
				var tabInfoHtml = "";
				var count = 0;
				if(nodetestresult.ping){
					tabHtml += '<li id="tab'+count+'"><a href="#pingTab" data-toggle="tab">Ping</a></li>';
					var pingpass = nodetestresult.pingpass ? '通过' : '未通过';
					var pingavg = nodetestresult.pingtime;
					var pingdetail = nodetestresult.pingoutmsg;
					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="pingTab">'
						+'<table class="table">'
						+'<tbody class="pingTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input type="text" id="pingpass" readOnly value="'+pingpass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">平均响应时间(ms)：</th>'
						+'<td class="tableInput"><input type="text" id="pingavg" readOnly value="'+pingavg+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2">'
						+'<textarea id="pingdetail" style="width: 100%; height: 200px;"  readOnly value="'+pingdetail+'">'+pingdetail+'</textarea>'
						+'</td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>';
					count++;
				}
				if(nodetestresult.tracepath){
					tabHtml += '<li id="tab'+count+'"><a href="#traceTab" data-toggle="tab">Trace</a></li>';
					var tracepass = nodetestresult.tracepass ? '通过' : '未通过';
					var tracetime = nodetestresult.tracetime;
					var tracedetail = nodetestresult.tracepathoutmsg;
					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="traceTab">'
						+'<table class="table">'
						+'<tbody class="traceTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input class="" type="text" id="tracepass" readOnly value="'+tracepass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">平均响应时间(s)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="tracetime" readOnly value="'+tracetime+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2"><textarea id="tracedetail" style="width: 100%; height: 200px;" readOnly value="'+tracedetail+'">'+tracedetail+'</textarea></td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>';
					count++;
				}
				if(nodetestresult.qperf){
					tabHtml += '<li id="tab'+count+'"><a href="#qperfTab" data-toggle="tab">Qperf</a></li>';
					var qperfpass = nodetestresult.qperfpass ? '通过' : '未通过';
					var speed = nodetestresult.speed;
					var latency = nodetestresult.latency;
					var qperfdetail = nodetestresult.qperfoutmsg;
					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="qperfTab">'
						+'<table class="table">'
						+'<tbody class="qperfTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input class="" type="text" id="qperfpass" readOnly value="'+qperfpass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">带宽(MB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="speed" readOnly value="'+speed+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">延迟(ms)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="latency" readOnly value="'+latency+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2"><textarea id="qperfdetail" style="width: 100%; height: 160px;" readOnly value="'+qperfdetail+'">'+qperfdetail+'</textarea></td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>';
					count++;
				}
				if(nodetestresult.curl){
					tabHtml += '<li id="tab'+count+'"><a href="#curlTab" data-toggle="tab">Curl</a></li>';
					var curlpass = nodetestresult.curlpass ? '通过' : '未通过';
					var curlavg = nodetestresult.curltime;
					var curldetail = nodetestresult.curloutmsg;
					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="curlTab">'
						+'<table class="table">'
						+'<tbody class="curlTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input class="" type="text" id="curlpass" readOnly value="'+curlpass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">响应时间(ms)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="curlavg" readOnly value="'+curlavg+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2"><textarea id="curldetail" style="width: 100%; height: 200px;" readOnly value="'+curldetail+'">'+curldetail+'</textarea></td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>';
					count++;
				}
				if(nodetestresult.docker){
					tabHtml += '<li id="tab'+count+'"><a href="#dockerTab" data-toggle="tab">Docker</a></li>';
					var memory=nodetestresult.disk;
					var dockerpass=nodetestresult.dockerpass ? '通过' : '未通过';
					var dockerdetail=nodetestresult.dockermsg;
					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="dockerTab">'
						+'<table class="table">'
						+'<tbody class="dockerTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input class="" type="text" id="dockerpass" readOnly value="'+dockerpass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">磁盘大小(G)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="memory" readOnly value="'+memory+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2"><textarea id="dockerdetail" style="width: 100%; height: 200px;" readOnly value="'+dockerdetail+'">'+dockerdetail+'</textarea></td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>';
					count++;
				}
				if(nodetestresult.dns){
					tabHtml += '<li id="tab'+count+'"><a href="#dnsTab" data-toggle="tab">Dns</a></li>';
					var dnspass= nodetestresult.dnspass ? '通过' : '未通过';
					var masterstatus=nodetestresult.masterdns ? '成功' : '失败';
					var masterdetail=nodetestresult.masterdnsoutmsg;
					var standbystatus=nodetestresult.standbydns ? '成功' : '失败';
					var standbydetail=nodetestresult.standbydnsoutmsg;
					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="dnsTab">'
						+'<table class="table">'
						+'<tbody class="curlTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input class="" type="text" id="dnspass" readOnly value="'+dnspass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">主dns操作：</th>'
						+'<td class="tableInput"><input class="" type="text" id="masterstatus" readOnly value="'+masterstatus+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2"><textarea id="masterdetail" style="width: 100%; height: 100px;" readOnly value="'+masterdetail+'">'+masterdetail+'</textarea></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">备dns操作：</th>'
						+'<td class="tableInput"><input class="" type="text" id="standbystatus" readOnly value="'+standbystatus+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<td colspan="2"><textarea id="standbydetail" style="width: 100%; height: 100px;"  readOnly value="'+standbydetail+'">'+standbydetail+'</textarea></td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>';
					count++;
				}
				$("#myTab").empty().append(tabHtml);
				$("#myTabContent").empty().append(tabInfoHtml);
				$("#tab0").addClass("active");
				$(".tabInfo0").addClass("active").addClass("in");
			}else{
				layer.msg(data.msg);
			}
		}
	})

}
function  clearDetailData(){
	$('#pingstatus').val('');
	$('#pingpass').val('');
	$('#pingavg').val('');
	$('#pingdetail').html('');

	$('#tracestatus').val('');
	$('#tracepass').val('');
	$('#tracetime').val('');
	$('#tracedetail').html('');

	$('#qperfstatus').val('');
	$('#qperfpass').val('');
	$('#speed').val('');
	$('#latency').val('');
	$('#qperfdetail').html('');

	$('#curlstatus').val('');
	$('#curlpass').val('');
	$('#curlavg').val('');
	$('#curldetail').html('');

	$('#dockerstatus').val('');
	$('#memory').val('');
	$('#dockerpass').val('');
	$('#dockerdetail').html('');

	$('#dnsstatus').val('');
	$('#dnspass').val('');
	$('#masterstatus').val('');
	$('#masterdetail').html('');
	$('#standbystatus').val('');
	$('#standbydetail').html('');
}

//单独部署一个节点
function deployOneNode(obj){
	var selectednode = $(obj).parent().attr("nodeName");
	var index = layer.load(0, {
		shade : [ 0.3, '#000' ]
	});
	var deployHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success " role="progressbar"'
		 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
		 +'style="width: 35%;">'
		 +'<span >正在部署</span>'
		 +'</div>';
	$(obj).parents(".thisTr").find(".nodeProgress").empty().append(deployHtml);
	$.ajax({
		url : ctx + "/cluster/deploypodfortest?nodenames=" + selectednode,
		success : function(data) {
			var data = eval("(" + data + ")");
			layer.closeAll("loading");
			if (data.status == '200') {
				var deployHtmlSuccess = '<div class="progress-bar progress-bar-warning" role="progressbar"'
								 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
								 +'style="width: 40%;">'
								 +'<span >部署完成</span>'
								 +'</div>';

				$(obj).parents(".thisTr").find(".nodeProgress").empty().append(deployHtmlSuccess);
				$(obj).parents(".thisTr").find(".clusterTestOpr").children("a").empty();
				$(obj).parent().attr("deployStatus","true");
				$(obj).parents(".thisTr").find("input:checkbox").attr("deployStatus","true");
			} else {
				var deployHtmlFailure = '<div class="progress-bar progress-bar-danger" role="progressbar"'
					 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					 +'style="width: 10%;">'
					 +'<span >部署失败</span>'
					 +'</div>';
				$(obj).parents(".thisTr").find(".nodeProgress").empty().append(deployHtmlFailure);
				$(obj).parents(".thisTr").find(".clusterTestOpr").children("a").empty();
				//layer.msg("部署失败！",{icon : 5});
			}
		}
	});
}
//清理一个节点的部署
function clearOneNode(obj){
	var selectednode = $(obj).parent().attr("nodeName");
	var index = layer.load(0, {
		shade : [ 0.3, '#000' ]
	});
	$.ajax({
		url : ctx + "/cluster/clearspecifiedpod?nodenames=" + selectednode,
		success : function(data) {
			var data = eval("(" + data + ")");
			layer.closeAll("loading");
			if (data.status == '200') {
				var clearHtmlSuccess = '<div class="progress-bar" role="progressbar"'
										 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">'
										 +'</div>';
				$(obj).parents(".thisTr").find(".nodeProgress").empty().append(clearHtmlSuccess);
				$(obj).parents(".thisTr").find(".clusterTestOpr").children("a").empty();
				$(obj).parent().attr("deployStatus","false");
				$(obj).parent().attr("testStatus","false");
				$(obj).parents(".thisTr").find("input:checkbox").attr("deployStatus","false");
				layer.msg("清除成功！",{icon : 1});
			} else {
				layer.msg("清除失败！",{icon : 5});
			}
		}
	});
}
//单独执行测试一个节点
function testOneNode(obj){
	$("#selectitem").prop("checked", false);
	var thisDeployStatus = $(obj).parent().attr("deployStatus");
	// 是否已经部署过
	if(thisDeployStatus == 'false'){
		layer.msg('执行测试前请先部署！',{icon: 5});
		return;
	}
	var selectednode = $(obj).parent().attr("nodeName");
	$.ajax({
		url:ctx + "/cluster/excutetestResult?nodename="+selectednode,
		type:"get",
		success:function(data){
			var data = eval("(" + data + ")");
			var status = data.status;
			if(status == '500'){
				layer.msg(data.msg,{icon : 5});
				return;
			}

			$("#pingitem").prop("checked",false);
			$("#traceitem").prop("checked",false);
			$("#curlitem").prop("checked",false);
			$("#qperfitem").prop("checked",false);
			$("#dockeritem").prop("checked",false);
			$("#dnsitem").prop("checked",false);
//测试默认值TODO
			$("#pingip").val("192.168.0.75");
			$("#tracepathip").val("192.168.0.75");
			$("#pingtime").val("10");
			$("#tracepathtime").val("2");
			$("#qperf").val("2000");
			$("#qperftime").val("10");
			$("#curltime").val("2");
			$("#docker").val("100");

			if(status == '200'){
				var nodetestresult = data.nodetestresult==undefined ? false : data.nodetestresult;
				var pingitem = nodetestresult.ping==undefined ? false : true;
				var traceitem = nodetestresult.tracepath==undefined ? false : true;
				var qperfitem = nodetestresult.qperf==undefined ? false : true;
				var curlitem = nodetestresult.curl==undefined ? false : true;
				var dockeritem = nodetestresult.docker==undefined ? false : true;
				var dnsitem = nodetestresult.dns==undefined ? false : true;

				$("#pingitem").prop("checked",pingitem);
				$("#traceitem").prop("checked",traceitem);
				$("#curlitem").prop("checked",curlitem);
				$("#qperfitem").prop("checked",qperfitem);
				$("#dockeritem").prop("checked",dockeritem);
				$("#dnsitem").prop("checked",dnsitem);
				if ($(".checkItem:checked").length == $(".checkItem").length) {
	                $("#selectitem").prop("checked", "checked");
	            }
				if(pingitem){
					$("#pingip").val(nodetestresult.pingIp);
					$("#pingtime").val(nodetestresult.pingtimetarget);
				}
				if(traceitem){
					$("#tracepathip").val(nodetestresult.traceIp);
					$("#tracepathtime").val(nodetestresult.tracetimetarget);
				}
				if(qperfitem){
					$("#qperf").val(nodetestresult.speedtarget);
					$("#qperftime").val(nodetestresult.latencytarget);
				}
				if(curlitem){
					$("#curltime").val(nodetestresult.curltimetarget);
				}
				if(dockeritem){
					$("#docker").val(nodetestresult.memorytarget);
				}
			}
			layer.open({
				type : 1,
				title : '检测项信息',
				content : $("#chkitem"),
				area : [ '600px' ],
				btn :  [ '确定', '取消' ],
				yes : function(index, layero) {
					
					if (!checkitems()) {
						return;// 校验没通过
					}
					layer.close(index);
					var selecteditems = "";
					$("input:checkbox[name='item']:checked").each(function(i) {
							if (0 == i) {
								selecteditems = $(this).val();
							} else {
								selecteditems += ("," + $(this).val());
							}
					});
					items = selecteditems;
					// 显示正在执行中
					var loading = layer.load(0,{shade : [ 0.3, '#000' ]});

					$('.checkbox a').empty();
					var pingIp = $("#pingip").val();
					var tracepathIp = $("#tracepathip").val();
					var pingtime = $("#pingtime").val();
					var tracetime = $("#tracepathtime").val();
					var curltime = $("#curltime").val();
					var speed = $("#qperf").val();
					var latency = $("#qperftime").val();
					var memory = $("#docker").val();
					var nodeTestInfo = {
							"nodename":selectednode,
							"pingIp":pingIp,
							"pingtimetarget":pingtime,
							"traceIp":tracepathIp,
							"tracetimetarget":tracetime,
							"curltimetarget":curltime,
							"speed":speed,
							"latency":latency,
							"memorytarget":memory
						};
					
					var nodeProgressDiv = $(obj).parents(".thisTr").find(".nodeProgress");
					var nodeTestOpr = $(obj).parents(".thisTr").find(".clusterTestOpr").children("a");
					var progressLength = $("input[name='item']:checked").length;
					var progressWidth = 60/progressLength+'%';
					var testprogressWidth = 60/progressLength-2+'%';
					var count = 0;
					var failNum = 0;
					
					var ids = new Array();
					var checkedItems = $("input:checkbox[name='item']:checked");
					for(var i=0; i<checkedItems.length; i++){
						var itemId = $(checkedItems[i]).attr("id");
						ids.push(itemId);
					}
					//1
					var testHtmlResule = '<div class="progress-bar progress-bar-warning" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: 40%;">'
						 +'<span >部署完成</span>'
						 +'</div>';
					var testPingHtml = testHtmlResule+'<div class="progress-bar progress-bar-striped active progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+testprogressWidth+';">'
						 +'<span>'+ids[count]+'测试中</span>'
						 +'</div>';
					nodeProgressDiv.empty().append(testPingHtml);
					$.ajax({
						url : ctx+"/cluster/test"+ids[count],
						//type:'get',
						data:nodeTestInfo,
						success : function(data) {
							var data = eval("(" + data + ")");
							var passStatus = passStatusFun(ids[count],data.nodeInfo);
							if (passStatus == true) {
								testHtmlResule = testHtmlResule
									 +'<div class="progress-bar progress-bar-success" role="progressbar"'
									 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
									 +'style="width: '+progressWidth+'">'
									 +'<span>'+ids[count]+'成功</span>'
									 +'</div>';
							}else{
								testHtmlResule = testHtmlResule
									 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
									 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
									 +'style="width: '+progressWidth+'">'
									 +'<span>'+ids[count]+'失败</span>'
									 +'</div>';
								failNum++;
							}
							count++;
							nodeProgressDiv.empty().append(testHtmlResule);
							if(count == progressLength){
								if(failNum == 0){
									nodeTestOpr.empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
								}else{
									nodeTestOpr.empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
								}
								layer.close(loading);
								return;
							}
							//2
							var testTraceHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success" role="progressbar"'
									 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
									 +'style="width: '+testprogressWidth+'">'
									 +'<span>'+ids[count]+'测试中</span>'
									 +'</div>';
							nodeProgressDiv.append(testTraceHtml);
							$.ajax({
								url : ctx+"/cluster/test"+ids[count],
								data:nodeTestInfo,
								success : function(data) {
									var data = eval("(" + data + ")");
									var passStatus = 'data.nodeInfo.'+ids[count]+'paas';
									if (passStatus == true) {
										testHtmlResule = testHtmlResule
											 +'<div class="progress-bar progress-bar-success" role="progressbar"'
											 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
											 +'style="width: '+progressWidth+'">'
											 +'<span>'+ids[count]+'成功</span>'
											 +'</div>';
									}else{
										testHtmlResule = testHtmlResule
											 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
											 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
											 +'style="width: '+progressWidth+'">'
											 +'<span>'+ids[count]+'失败</span>'
											 +'</div>';
										failNum++;
									}
									count++;
									nodeProgressDiv.empty().append(testHtmlResule);
									if(count == progressLength){
										if(failNum == 0){
											nodeTestOpr.empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
										}else{
											nodeTestOpr.empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
										}
										layer.close(loading);
										return;
									}
									//3
									var testCurlHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success" role="progressbar"'
											 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
											 +'style="width: '+testprogressWidth+'">'
											 +'<span>'+ids[count]+'测试中</span>'
											 +'</div>';
									nodeProgressDiv.append(testCurlHtml);
									$.ajax({
										url : ctx+"/cluster/test"+ids[count],
										data:nodeTestInfo,
										success : function(data) {
											var data = eval("(" + data + ")");
											
											var passStatus = ids[count]+'paas';
												if (data.nodeInfo.passStatus == true) {
													testHtmlResule = testHtmlResule
														 +'<div class="progress-bar progress-bar-success" role="progressbar"'
														 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
														 +'style="width: '+progressWidth+'">'
														 +'<span>'+ids[count]+'成功</span>'
														 +'</div>';
												}else{
													testHtmlResule = testHtmlResule
														 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
														 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
														 +'style="width: '+progressWidth+'">'
														 +'<span>'+ids[count]+'失败</span>'
														 +'</div>';
													failNum++;
												}
												count++;
											
											nodeProgressDiv.empty().append(testHtmlResule);
											if(count == progressLength){
												if(failNum == 0){
													nodeTestOpr.empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
												}else{
													nodeTestOpr.empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
												}
												layer.close(loading);
												return;
											}
											//4
											var testQperfHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success" role="progressbar"'
													 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
													 +'style="width: '+testprogressWidth+'">'
													 +'<span>'+ids[count]+'测试中</span>'
													 +'</div>';
											nodeProgressDiv.append(testQperfHtml);
											$.ajax({
												url : ctx+"/cluster/test"+ids[count],
												data:nodeTestInfo,
												success : function(data) {
													var data = eval("(" + data + ")");
													
													var passStatus = ids[count]+'paas';
														if (data.nodeInfo.passStatus == true) {
															testHtmlResule = testHtmlResule
																 +'<div class="progress-bar progress-bar-success" role="progressbar"'
																 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																 +'style="width: '+progressWidth+'">'
																 +'<span>'+ids[count]+'成功</span>'
																 +'</div>';
														}else{
															testHtmlResule = testHtmlResule
																 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
																 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																 +'style="width: '+progressWidth+'">'
																 +'<span>'+ids[count]+'失败</span>'
																 +'</div>';
															failNum++;
														}
														count++;
													
													nodeProgressDiv.empty().append(testHtmlResule);
													if(count == progressLength){
														if(failNum == 0){
															nodeTestOpr.empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
														}else{
															nodeTestOpr.empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
														}
														layer.close(loading);
														return;
													}
													//5
													var testDockerHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success" role="progressbar"'
															 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
															 +'style="width: '+testprogressWidth+'">'
															 +'<span>'+ids[count]+'测试中</span>'
															 +'</div>';
													nodeProgressDiv.append(testDockerHtml);
													$.ajax({
														url : ctx+"/cluster/test"+ids[count],
														data:nodeTestInfo,
														success : function(data) {
															var data = eval("(" + data + ")");
															var passStatus = ids[count]+'paas';
																if (data.nodeInfo.passStatus == true) {
																	testHtmlResule = testHtmlResule
																		 +'<div class="progress-bar progress-bar-success" role="progressbar"'
																		 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																		 +'style="width: '+progressWidth+'">'
																		 +'<span>'+ids[count]+'成功</span>'
																		 +'</div>';
																}else{
																	testHtmlResule = testHtmlResule
																		 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
																		 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																		 +'style="width: '+progressWidth+'">'
																		 +'<span>'+ids[count]+'失败</span>'
																		 +'</div>';
																	failNum++;
																}
																count++;
															
															nodeProgressDiv.empty().append(testHtmlResule);
															if(count == progressLength){
																if(failNum == 0){
																	nodeTestOpr.empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
																}else{
																	nodeTestOpr.empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
																}
																layer.close(loading);
																return;
															}
															//6
															var testDnsHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success" role="progressbar"'
																	 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																	 +'style="width: '+testprogressWidth+'">'
																	 +'<span>'+ids[count]+'测试中</span>'
																	 +'</div>';
															nodeProgressDiv.append(testDnsHtml);
															$.ajax({
																url : ctx+"/cluster/test"+ids[count],
																data:nodeTestInfo,
																success : function(data) {
																	var data = eval("(" + data + ")");
																	var passStatus = ids[count]+'paas';
																		if (data.nodeInfo.passStatus == true) {
																			testHtmlResule = testHtmlResule
																				 +'<div class="progress-bar progress-bar-success" role="progressbar"'
																				 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																				 +'style="width: '+progressWidth+'">'
																				 +'<span>'+ids[count]+'成功</span>'
																				 +'</div>';
																		}else{
																			testHtmlResule = testHtmlResule
																				 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
																				 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
																				 +'style="width: '+progressWidth+'">'
																				 +'<span>'+ids[count]+'失败</span>'
																				 +'</div>';
																			failNum++;
																		}
																		count++;
																	
																	nodeProgressDiv.empty().append(testHtmlResule);
																	if(count == progressLength){
																		if(failNum == 0){
																			nodeTestOpr.empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
																		}else{
																			nodeTestOpr.empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
																		}
																		layer.close(loading);
																		return;
																	}
																}
															})
														}
													})
												}
											})
										}
									})
								}
							})
						}
					})
				}
			})
		}
	});
}
function passStatusFun(id,dataNodeInfo){
	var passStatusStr = "";
	if(id == 'ping'){
		passStatusStr = dataNodeInfo.pingpass;
	}else if(id == 'trace'){
		passStatusStr = dataNodeInfo.tracepass;
	}else if(id == 'qperf'){
		passStatusStr = dataNodeInfo.qperfpass;
	}else if(id == 'curl'){
		passStatusStr = dataNodeInfo.curlpass;
	}else if(id == 'docker'){
		passStatusStr = dataNodeInfo.dockerpass;
	}else if(id == 'dns'){
		passStatusStr = dataNodeInfo.dnspass;
	}
	return passStatusStr;
}
function initDetail(data) {
	for (var i = 0; i < data.nodeInfos.length; i++) {
		if (data.nodeInfos[i].allpass) {
			var testHtmlSuccess = '<div class="progress-bar progress-bar-warning" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: 15%;">'
							 +'<span >部署完成</span>'
							 +'</div>'
							 +'<div class="progress-bar progress-bar-success" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: 85%;">'
							 +'<span>执行完成</span>'
							 +'</div>';

			$('#progress_'+data.nodeInfos[i].nodename).empty().append(testHtmlSuccess);

			$('#' + data.nodeInfos[i].nodename).empty()
					.append(
							'<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
		} else {
			var testHtmlFailure = '<div class="progress-bar progress-bar-warning" role="progressbar"'
				 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
				 +'style="width: 15%;">'
				 +'<span >部署完成</span>'
				 +'</div>'
				 +'<div class="progress-bar progress-bar-danger" role="progressbar"'
				 +' aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
				 +'style="width: 85%;">'
				 +'<span>执行失败</span>'
				 +'</div>';
			$('#progress_'+data.nodeInfos[i].nodename).empty().append(testHtmlFailure);
			$('#' + data.nodeInfos[i].nodename).empty()
					.append(
							'<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
		}
	}
}

// 校验
function checkitems() {
	var selecteditems = "";
	$("input:checkbox[name='item']:checked").each(
		function(i) {
			if (0 == i) {
				selecteditems = $(this).val();
			} else {
				selecteditems += ("," + $(this).val());
			}
		});
	if (selecteditems == "") {
		layer.tips('请选择至少一项检测项', '#selectitem', {
			tips : [ 1, '#3595CC' ]
		});
		$('#selectitem').focus();
		return;
	}

	// 校验ping项的输入
	if ($("input:checkbox[value='pingitem']").prop('checked')) {
		var ip = $("#pingip").val();
		if (ip.length === 0) {
			layer.tips('ip地址不能为空', '#pingip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#pingip').focus();
			return;
		}

		if (ip.search(/^([1-9]|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])(\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])){3}$/ig) === -1) {
			layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx','#pingip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#pingip').focus();
			return;
		}
		var pingtime = $("#pingtime").val();
		if(pingtime == ''||pingtime<=0){
			layer.tips('平均响应时间不能为空，且应是正数！', '#pingtime', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#pingtime').focus();
			return;
		}
	}
	// 校验tracepathchk项的输入
	if ($("input:checkbox[value='traceitem']").prop('checked')) {
		ip = $("#tracepathip").val();
		if (ip.length === 0) {
			layer.tips('ip地址不能为空', '#tracepathip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#tracepathip').focus();
			return;
		}

		if (ip.search(/^([1-9]|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])(\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])){3}$/ig) === -1) {
			layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx','#tracepathip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#tracepathip').focus();
			return;
		}
		var tracepathtime = $("#tracepathtime").val();
		if(tracepathtime == ''||tracepathtime<=0){
			layer.tips('平均响应时间不能为空，且应是正数！', '#tracepathtime', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#tracepathtime').focus();
			return;
		}
	}
	// 校验tracepathchk项的输入
	if ($("input:checkbox[value='qperfitem']").prop('checked')) {
		var qperf = $("#qperf").val();
		if(qperf == ''||qperf<=0){
			layer.tips('qperf带宽不能为空，且应是正数！', '#qperf', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#qperf').focus();
			return;
		}

		var qperftime = $("#qperftime").val();
		if(qperftime == ''||qperftime<=0){
			layer.tips('延迟不能为空，且应是正数！', '#qperftime', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#qperftime').focus();
			return;
		}
	}
	// 校验tracepathchk项的输入
	if ($("input:checkbox[value='curlitem']").prop('checked')) {
		var curltime = $("#curltime").val();
		if(curltime == ''||curltime<=0){
			layer.tips('curl响应时间不能为空，且应是正数！', '#curltime', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#curltime').focus();
			return;
		}

	}

	// 校验tracepathchk项的输入
	if ($("input:checkbox[value='dockeritem']").prop('checked')) {
		var docker = $("#docker").val();
		if(docker == ''||docker<=0){
			layer.tips('docker磁盘大小不能为空，且应是正数！', '#docker', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#docker').focus();
			return;
		}

	}
	return true;
}

var nodeInfos = {};
var items = "";

$(document).ready(function() {
	showTestList();
	$("#selectitem").prop("checked",false);
	var deployHtmlSuccess = '<div class="progress-bar progress-bar-warning" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: 15%;">'
						 +'<span >部署完成</span>'
						 +'</div>';
	var deployHtmlFailure = '<div class="progress-bar progress-bar-danger" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: 15%;">'
						 +'<span >部署失败</span>'
						 +'</div>';
	var deployedpod = $("#deployednodes").val().split(',');
	for (var i = 0; i < deployedpod.length; i++) {
		if (deployedpod[i] != '') {
			$("td[nodeName='" + deployedpod[i] + "']").find(".nodeProgress").empty().append(deployHtml);
		}
	}

//					$("#checkallbox").change(function() {
//						if ($(this).prop('checked')) {
//							$("input[name='node']").prop("checked", true);
//						} else {
//							$("input[name='node']").prop("checked", false);
//						}
//					});
	//执行参数的全选
	$("#selectitem").change(function() {
		if ($(this).prop('checked')) {
			$("input[name='item']").prop("checked", true);
		} else {
			$("input[name='item']").prop("checked", false);
		}
	});
	// 每个参数checkbox 都选中 时   全选也被选定
    $(document).on("click",".checkItem", function(){
        if($(this).is(":checked")){
            if ($(".checkItem:checked").length == $(".checkItem").length) {
                $("#selectitem").prop("checked", "checked");
            }
        }else{
            $("#selectitem").prop('checked', $(this).is(":checked"));
        }
    });

	// 批量部署，部署pod和service
	$("#deployBtn").click(function() {
		var selectednodes = "";
		$("input:checkbox[name='node']:checked").each(function(i) {
			if (0 == i) {
				selectednodes = $(this).val();
			} else {
				selectednodes += ("," + $(this).val());
			}
		});
		if (selectednodes == "") {
			layer.tips('请选择至少一个集群节点','#checkallbox', {
				tips : [ 1, '#3595CC' ]
			});
			$('#checkallbox').focus();
			return;
		}
		$('.checkbox a').empty();
		var index = layer.load(0, {
			shade : [ 0.3, '#000' ]
		});
		$.ajax({
			url : ctx + "/cluster/deploypodfortest?nodenames=" + selectednodes,
			success : function(data) {
				var data = eval("(" + data + ")");
				layer.closeAll("loading");
				if (data.status == '200') {
					$("input:checkbox[name='node']:checked").parent().parent().find(".nodeProgress").empty().append(deployHtmlSuccess);
					$("input:checkbox[name='node']:checked").attr("deployStatus","true");
					$("input:checkbox[name='node']:checked").parent().parent().find(".clusterTestOprBtns").attr("deployStatus","true");
				} else {
					$("input:checkbox[name='node']:checked").parent().parent().find(".nodeProgress").empty().append(deployHtmlFailure);
				}
			}
		});
	});

	// 批量 清除部署
	$("#deleteBtn").click(function() {
		/*var selectednodes = "";
		$("input:checkbox[name='node']:checked").each(function(i) {
			if (0 == i) {
				selectednodes = $(this).val();
			} else {
				selectednodes += ("," + $(this).val());
			}
		});
		if (selectednodes == "") {
			layer.tips('请选择至少一个集群节点','#checkallbox', {
				tips : [ 1, '#3595CC' ]
			});
			$('#checkallbox').focus();
			return;
		}*/
		$('.checkbox a').empty();
		var index = layer.load(0, {
			shade : [ 0.3, '#000' ]
		});

		$.ajax({
			//url : ctx + "/cluster/clearspecifiedpod?nodenames=" + selectednodes,
			url : ctx + "/cluster/deployclear",
			success : function(data) {
				var data = eval("(" + data + ")");
				layer.closeAll("loading");
				if (data.status == '200') {
					var clearHtmlSuccess = '<div class="progress-bar" role="progressbar"'
											+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">'
											+'</div>';
					$("input:checkbox[name='node']:checked").parent().parent().find(".nodeProgress").empty().append(clearHtmlSuccess);
					$("input:checkbox[name='node']:checked").parent().parent().find(".clusterTestOpr").children("a").empty();
					$("input:checkbox[name='node']:checked").parent().parent().find(".clusterTestOprBtns").attr("deployStatus","false");
					$("input:checkbox[name='node']:checked").attr("deployStatus","false");
				} else {
					layer.msg("清除失败！",{icon : 5});
				}
			}
		});
	});

	// 执行集群测试
	$("#excuteBtn").click(function() {
		var selectednodes = "";
		$("input:checkbox[name='node']:checked").each(function(i) {
			if (0 == i) {
				selectednodes = $(this).val();
			} else {
				selectednodes += ("," + $(this).val());
			}
		});
		if (selectednodes == "") {
			layer.tips('请选择至少一个集群节点','#checkallbox', {
				tips : [ 1, '#3595CC' ]
			});
			$('#checkallbox').focus();
			return;
		}
		if ($(".checkItem:checked").length == $(".checkItem").length) {
            $("#selectitem").prop("checked", "checked");
        }
		//检查所选节点是否都已经部署
		var chkItem = $(".chkItem:checked");
		for(var ci=0; ci<chkItem.length; ci++){
			var itemDeployStatus =$(chkItem[ci]).attr("deployStatus");
			if(itemDeployStatus == 'false'){
				layer.msg("执行测试前请先部署！", {icon : 5});
				return;
			}
		}
		layer.open({
			type : 1,
			title : '检测项信息',
			content : $("#chkitem"),
			area : [ '600px' ],
			btn :  [ '确定', '取消' ],
			yes : function(index, layero) {
				if (!checkitems()) {
					return;// 校验没通过
				}
				var selecteditems = "";
				$("input:checkbox[name='item']:checked").each(function(i) {
						if (0 == i) {
							selecteditems = $(this).val();
						} else {
							selecteditems += ("," + $(this).val());
						}
				});
				items = selecteditems;
				// 显示正在执行中
				var idx = layer.load(0, {shade : [ 0.3, '#000' ]});

				$('.checkbox a').empty();
				var pingIp = $("#pingip").val();
				var tracepathIp = $("#tracepathip").val();
				var pingtime = $("#pingtime").val();
				var tracetime = $("#tracepathtime").val();
				var curltime = $("#curltime").val();
				var speed = $("#qperf").val();
				var latency = $("#qperftime").val();
				var memory = $("#docker").val();
				$.ajax({
					url : ctx
						+ "/cluster/excutetest?nodenames="
						+ selectednodes + "&items="
						+ selecteditems + "&pingIp="
						+ pingIp + "&pingtime=" + pingtime
						+ "&tracepathIp=" + tracepathIp
						+ "&tracetime=" + tracetime
						+ "&curltime=" + curltime
						+ "&speed=" + speed + "&latency="
						+ latency + "&memory=" + memory,
					success : function(data) {
						var data = eval("(" + data + ")");
						layer.closeAll("loading");
						if (data.status == '200') {
							initDetail(data);
							nodeInfos = data.nodeInfos;
							layer.close(index);
						} else {
							layer.alert(data.msg, {
								icon : 5
							});
						}
					}
				});
			}
		})
		return;
		});
});

function showTestList(){
	$.ajax({
		url:ctx + "/cluster/excutetestResultForAll",
		type:"get",
		success:function(data){
			var data = eval("(" + data + ")");
			var nodetestresult = data.nodetestresult;
			for(var i=0; i<nodetestresult.length; i++){
				var testCount = 0;
				var allpass = data.nodetestresult[i].allpass;
				var nodeName = data.nodetestresult[i].nodename;
				var pingpass = nodetestresult[i].pingpass;
				var tracepass = nodetestresult[i].tracepass;
				var qpertpass = nodetestresult[i].qpertpass;
				var curlpass = nodetestresult[i].curlpass;
				var dockerpass = nodetestresult[i].dockerpass;
				var dnspass = nodetestresult[i].dnspass;
				if(pingpass == true){
					testCount++;
				}else if(pingpass == true){
					testCount++;
				}else if(tracepass == true){
					testCount++;
				}else if(qpertpass == true){
					testCount++;
				}else if(curlpass == true){
					testCount++;
				}else if(dockerpass == true){
					testCount++;
				}else if(dnspass == true){
					testCount++;
				}
				var progressWidth = 60/testCount+'%';
				
				var testListHtml = '<tr class="thisTr">'
				+'<td style="width:5%;text-indent:20px">'
				+'<input class="chkItem" name="node" type="checkbox" value="${node.nodename }" testStatus="${node.teststatus }" deployStatus="${node.deploystatus }">'
				+'</td>'
				+'<td style="width:12%;">${node.nodename }</td>'
				+'<td style="width:55%;" nodeName="${node.nodename }" class="nodeProgressBar">'
				+'<div id="progress_${node.nodename }" class="progress nodeProgress" style="margin:0 auto">'
				+'<div class="progress-bar" role="progressbar"'
				+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">'
				+'</div>';
//						<div class="progress-bar progress-bar-warning" role="progressbar"
//							 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
//							 style="width: 40%;">
//							<span >部署完成</span>
//						</div>
//				<div class="progress-bar progress-bar-warning" role="progressbar"
//					 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
//					 style="width: 0%;">
//				</div>
				if(pingpass == true){
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>ping成功</span>'
						 +'</div>';
				}else{
					testListHtml += '<div class="progress-bar progress-bar-danger" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>ping失败</span>'
						 +'</div>';
				} 
				
				if(tracepass == true){
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>trace成功</span>'
						 +'</div>';
				}else{
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>trace失败</span>'
						 +'</div>';
				} 
				if(qpertpass == true){
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>qpert成功</span>'
						 +'</div>';
				}else{
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>qpert失败</span>'
						 +'</div>';
				} 
				if(curlpass == true){
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>curl成功</span>'
						 +'</div>';
				}else{
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>curl失败</span>'
						 +'</div>';
				} 
				if(dockerpass == true){
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>docker成功</span>'
						 +'</div>';
				}else{
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>docker失败</span>'
						 +'</div>';
				} 
				if(dnspass == true){
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>dns成功</span>'
						 +'</div>';
				}else{
					testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: '+progressWidth+'">'
						 +'<span>dns失败</span>'
						 +'</div>';
				}
						
								
						
				testListHtml += '</div>'
						+'</td>'
						+'<td class="clusterTestOpr" style="width:12%;text-indent:20px">'
						+'<a id="${node.nodename }"  nodename="${node.nodename }"'
						+'onclick="detail(this)" title="查看详细信息">';
				if(allpass == true){
					testListHtml +='<font color="#33CC33" style="font-weight:bold;cursor:pointer" id="nodeTestInfo" nodeTestInfo="${node.nodeTestInfo}">通过<i class="fa fa-question-circle"></i></font>';
				}else{
					testListHtml +='<font color="#FF0033" style="font-weight:bold;cursor:pointer">未通过<i class="fa fa-question-circle"></i></font>';
				}
				testListHtml +='</a>'
						+'</td>'
						+'<td class="clusterTestOprBtns" nodeName="${node.nodename }" testStatus="${node.teststatus }" deployStatus="${node.deploystatus }">'
						+'<a onclick="deployOneNode(this)"><i>部署</i></a>'
						+'<a onclick="testOneNode(this)"><i>执行</i></a>'
						+'<a onclick="clearOneNode(this)"><i>清理部署</i></a>'
						+'</td>'
						+'</tr>';	
				
				$("#routeList").empty().append(testListHtml);
			}
		}
	})
}
