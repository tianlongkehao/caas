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
					var dockerpass=nodetestresult.dockerpass ? '通过' : '未通过';
					var dockerdetail=nodetestresult.dockermsg;

					var PoolBlocksize = nodetestresult.dockerPoolBlocksize;
					var BaseDeviceSize = nodetestresult.dockerBaseDeviceSize;
					var BackingFilesystem = nodetestresult.dockerBackingFilesystem;
					var Datafile=nodetestresult.dockerDatafile;
					var DataSpaceUsed = nodetestresult.dockerDataSpaceUsed;
					var DataSpaceTotal = nodetestresult.dockerDataSpaceTotal;
					var DataSpaceAvailable=nodetestresult.dockerDataSpaceAvailable;
					var Metadatafile = nodetestresult.dockerMetadatafile;
					var MetaSpaceUsed=nodetestresult.dockerMetaSpaceUsed;
					var MetaSpaceTotal = nodetestresult.dockerMetaSpaceTotal;
					var MetaSpaceAvailable = nodetestresult.dockerMetaSpaceAvailable;
					var DeferredRemovalEnable=nodetestresult.dockerDeferredRemovalEnable;
					var UdevSyncSupported = nodetestresult.dockerUdevSyncSupported;
					var DeferredDeletionEnable = nodetestresult.docekrDeferredDeletionEnable;
					var DeferredDeletedDeviceCount=nodetestresult.dockerDeferredDeletedDeviceCount;

					tabInfoHtml += '<div class="tab-pane fade tabInfo'+count+'" id="dockerTab">'
						+'<table class="table">'
						+'<tbody class="dockerTable">'
						+'<tr>'
						+'<th style="width: 20%">检测状态：</th>'
						+'<td class="tableInput"><input class="" type="text" id="dockerpass" readOnly value="'+dockerpass+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Pool Blocksize(KB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="PoolBlocksize" readOnly value="'+PoolBlocksize+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Base Device Size(GB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="BaseDeviceSize" readOnly value="'+BaseDeviceSize+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Backing Filesystem：</th>'
						+'<td class="tableInput"><input class="" type="text" id="BackingFilesystem" readOnly value="'+BackingFilesystem+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Data file：</th>'
						+'<td class="tableInput"><input class="" type="text" id="Datafile" readOnly value="'+Datafile+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Data Space Used(GB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="DataSpaceUsed" readOnly value="'+DataSpaceUsed+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Data Space Total(GB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="DataSpaceTotal" readOnly value="'+DataSpaceTotal+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Data Space Available(GB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="DataSpaceAvailable" readOnly value="'+DataSpaceAvailable+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Metadata file：</th>'
						+'<td class="tableInput"><input class="" type="text" id="Metadatafile" readOnly value="'+Metadatafile+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Meta Space Used(MB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="MetaSpaceUsed" readOnly value="'+MetaSpaceUsed+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 20%">Meta Space Total(GB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="MetaSpaceTotal" readOnly value="'+MetaSpaceTotal+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 44%">Meta Space Available(KB)：</th>'
						+'<td class="tableInput"><input class="" type="text" id="MetaSpaceAvailable" readOnly value="'+MetaSpaceAvailable+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 44%">Deferred Removal Enable：</th>'
						+'<td class="tableInput"><input class="" type="text" id="DeferredRemovalEnable" readOnly value="'+DeferredRemovalEnable+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 44%">Udev Sync Supported：</th>'
						+'<td class="tableInput"><input class="" type="text" id="UdevSyncSupported" readOnly value="'+UdevSyncSupported+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 44%">Deferred Deletion Enable：</th>'
						+'<td class="tableInput"><input class="" type="text" id="DeferredDeletionEnable" readOnly value="'+DeferredDeletionEnable+'"></td>'
						+'</tr>'
						+'<tr>'
						+'<th style="width: 44%">Deferred Deleted Device Count：</th>'
						+'<td class="tableInput"><input class="" type="text" id="DeferredDeletedDeviceCount" readOnly value="'+DeferredDeletedDeviceCount+'"></td>'
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
						 +'style="width: 40%;">'
						 +'<span >部署完成</span>'
						 +'</div>';
	var deployHtmlFailure = '<div class="progress-bar progress-bar-danger" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: 40%;">'
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
		var flag = true;
		var selectednodes = new Array();
		$("input:checkbox[name='node']").each(function(i) {
			selectednodes.push($(this).val());
			if($(this).attr("teststatus") == "true"){
				layer.msg('所选节点中有执行测试完成节点，若重新部署请先清理该节点！',{icon: 5});
				flag = false;
				return;
			}
		});
		if(flag == true){
			var deployLoading = layer.load(0, {
				shade : [ 0.3, '#000' ]
			});
			var deployingHtml = '<div class="progress-bar progress-bar-striped active progress-bar-success " role="progressbar"'
				 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
				 +'style="width: 35%;">'
				 +'<span >正在部署</span>'
				 +'</div>';
			var defer = $.Deferred();
			//这一步必须要写，要不然下面的then无法使用
			defer.resolve();
			$.each(selectednodes,function(i,e){

				defer = defer.then(function () {
					var checkNode = $('input:checkbox[id="'+e+'"]');
					checkNode.parent().parent().find(".nodeProgress").empty().append(deployingHtml);
				  return $.ajax({
			      url:ctx + "/cluster/deploypodfortest?nodenames="+e,
			      method:'get',
			      success:function(data){
			    	  var data = eval("(" + data + ")");
			    	  if (data.status == '200') {
						checkNode.parent().parent().find(".nodeProgress").empty().append(deployHtmlSuccess);
						checkNode.attr("deployStatus","true");
						checkNode.parent().parent().find(".clusterTestOprBtns").attr("deployStatus","true");
					} else {
						checkNode.parent().parent().find(".nodeProgress").empty().append(deployHtmlFailure);
					}
			      }
			    })
			  });
			});
			defer.done(function(){
				layer.closeAll();
			});
		}

	});

	// 批量 清除部署
	$("#deleteBtn").click(function() {
		$('.checkbox a').empty();
		var loading = layer.load(0, {
			shade : [ 0.3, '#000' ]
		});
		$.ajax({
			url : ctx + "/cluster/deployclear",
			success : function(data) {
				var data = eval("(" + data + ")");
				layer.closeAll("loading");
				if (data.status == '200') {
					var clearHtmlSuccess = '<div class="progress-bar" role="progressbar"'
											+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">'
											+'</div>';
					$("input:checkbox[name='node']").parent().parent().find(".nodeProgress").empty().append(clearHtmlSuccess);
					$("input:checkbox[name='node']").parent().parent().find(".clusterTestOpr").empty();
					$("input:checkbox[name='node']").parent().parent().find(".clusterTestOprBtns").attr("deployStatus","false");
					$("input:checkbox[name='node']").attr("deployStatus","false");
					layer.msg("清除成功！",{icon : 1});
				} else {
					layer.msg("清除失败！",{icon : 5});
				}
			}
		});
	});

	//批量 执行集群测试
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


		return;
		});
});
//加载集群节点列表
function showTestList(){
	$.ajax({
		url:ctx + "/cluster/excutetestResultForAll",
		type:"get",
		success:function(data){
			var data = eval("(" + data + ")");
			var testHtml = showTestListData(data);
			$("#routeList").empty().append(testHtml);
			$('.dataTables-example').dataTable({
			    "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,3] }],
			});
			$("#checkallbox").parent().removeClass("sorting_asc");
		}
	})
}
function showTestListData(data){
	debugger;
	var nodetestresult = data.nodetestresult;
	var testListHtml = "";
	for(var i=0; i<nodetestresult.length; i++){
		var testCount = 0;
		var failCount = 0;
		var deploystatus = data.nodetestresult[i].deploystatus;
		var nodename = data.nodetestresult[i].nodename;
		var teststatus = data.nodetestresult[i].teststatus;
		var nodeTestInfo = data.nodetestresult[i].nodeTestInfo;
		testListHtml += '<tr class="thisTr">'
			+'<td style="width:5%;text-indent:20px">'
			+'<input class="chkItem" name="node" type="checkbox" id="'+nodename+'" value="'+nodename+'" testStatus="'+teststatus+'" deployStatus="'+deploystatus+'">'
			+'</td>'
			+'<td style="width:12%;">'+nodename+'</td>'
			+'<td style="width:55%;" nodeName="'+nodename+'" class="nodeProgressBar">'
			+'<div id="progress_'+nodename+'" class="progress nodeProgress" style="margin:0 auto">'
			+'<div class="progress-bar" role="progressbar"'
			+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">'
			+'</div>';
		if(nodeTestInfo == undefined){
			if(deploystatus == true){
				//deploy=true
				testListHtml +='<div class="progress-bar progress-bar-warning" role="progressbar"'
					 	+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					 	+'style="width: 40%;">'
					 	+'<span >部署完成</span>'
					 	+'</div>';

			}else{
				testListHtml +='<div class="progress-bar progress-bar-warning" role="progressbar"'
					+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					+' style="width: 0%;">'
					+'</div>';
			}
			testListHtml +='</div>'
					+'</td>'
					+'<td class="clusterTestOpr" style="width:12%;text-indent:20px">'
					+'</td>'
					+'</tr>';
		}else{
			var allpass = nodeTestInfo.allpass;
			var pingpass = nodeTestInfo.pingpass;
			var tracepass = nodeTestInfo.tracepass;
			var qperfpass = nodeTestInfo.qperfpass;
			var curlpass = nodeTestInfo.curlpass;
			var dockerpass = nodeTestInfo.dockerpass;
			var dnspass = nodeTestInfo.dnspass;

			var ping = nodeTestInfo.ping;
			var trace = nodeTestInfo.tracepath;
			var qperf = nodeTestInfo.qperf;
			var curl = nodeTestInfo.curl;
			var docker = nodeTestInfo.docker;
			var dns = nodeTestInfo.dns;

			if(ping == true){
				testCount++;
			}
			if(trace == true){
				testCount++;
			}
			if(qperf == true){
				testCount++;
			}
			if(curl == true){
				testCount++;
			}
			if(docker == true){
				testCount++;
			}
			if(dns == true){
				testCount++;
			}
			var progressWidth = 60/testCount+'%';


			if(deploystatus == true){
				//deploy=true
				testListHtml +='<div class="progress-bar progress-bar-warning" role="progressbar"'
					 	+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					 	+'style="width: 40%;">'
					 	+'<span >部署完成</span>'
					 	+'</div>';
				if(ping == true){
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
						failCount++;
					}
				}
				if(trace == true){
					if(tracepass == true){
						testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>trace成功</span>'
							 +'</div>';
					}else{
						testListHtml += '<div class="progress-bar progress-bar-danger" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>trace失败</span>'
							 +'</div>';
						failCount++;
					}
				}
				if(qperf == true){
					if(qperfpass == true){
						testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>qperf成功</span>'
							 +'</div>';
					}else{
						testListHtml += '<div class="progress-bar progress-bar-danger" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>qperf失败</span>'
							 +'</div>';
						failCount++;
					}
				}
				if(curl == true){
					if(curlpass == true){
						testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>curl成功</span>'
							 +'</div>';
					}else{
						testListHtml += '<div class="progress-bar progress-bar-danger" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>curl失败</span>'
							 +'</div>';
						failCount++;
					}
				}
				if(docker == true){
					if(dockerpass == true){
						testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>docker成功</span>'
							 +'</div>';
					}else{
						testListHtml += '<div class="progress-bar progress-bar-danger" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>docker失败</span>'
							 +'</div>';
						failCount++;
					}
				}
				if(dns == true){
					if(dnspass == true){
						testListHtml += '<div class="progress-bar progress-bar-success" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>dns成功</span>'
							 +'</div>';
					}else{
						testListHtml += '<div class="progress-bar progress-bar-danger" role="progressbar"'
							 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
							 +'style="width: '+progressWidth+'">'
							 +'<span>dns失败</span>'
							 +'</div>';
						failCount++;
					}
				}

				testListHtml += '</div>'
						+'</td>'
						+'<td class="clusterTestOpr" style="width:12%;text-indent:20px">'
						+'<a id="'+nodename+'"  nodename="'+nodename+'"'
						+'onclick="detail(this)" title="查看详细信息">';
				if(failCount == 0){
					testListHtml +='<font color="#33CC33" style="font-weight:bold;cursor:pointer" id="nodeTestInfo" nodeTestInfo="${node.nodeTestInfo}">通过<i class="fa fa-question-circle"></i></font>';
				}else{
					testListHtml +='<font color="#FF0033" style="font-weight:bold;cursor:pointer">未通过<i class="fa fa-question-circle"></i></font>';
				}
				testListHtml +='</a>'
						+'</td>'
						+'</tr>';
			}else{
				//deploy=false
				testListHtml +='<div class="progress-bar progress-bar-warning" role="progressbar"'
					+'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					+' style="width: 0%;">'
					+'</div>';
			}
		}
	}
	return testListHtml;
}

//批量  执行测试
function testNodes(obj){
	$("#selectitem").prop("checked", false);
	var selectednodes = new Array();
	var flag = true;
	if(obj == undefined){
		//批量   选中的节点
		$("input:checkbox[name='node']:checked").each(function(i) {
			selectednodes.push($(this).val());
		});
		if (selectednodes.length == 0) {
			layer.tips('请选择至少一个集群节点','#checkallbox', {
				tips : [ 1, '#3595CC' ]
			});
			$('#checkallbox').focus();
			flag = false;
			return;
		}
		// 是否已经部署过
		$("input:checkbox[name='node']:checked").each(function(i) {
			if($(this).attr("deploystatus") == "false"){
				layer.msg('执行测试前请先部署！',{icon: 5});
				flag = false;
				return;
			}
		});
		$.ajax({
			url:ctx + "/cluster/testparam",
			type:"get",
			success:function(data){
				var data = eval("(" + data + ")");
				var status = data.status;
				if(status == '500'){
					layer.msg(data.msg,{icon : 5});
					return;
				}
				if(status == '200'){
					$("#ping").prop("checked",false);
					$("#trace").prop("checked",false);
					$("#curl").prop("checked",false);
					$("#qperf").prop("checked",false);
					$("#docker").prop("checked",false);
					$("#dns").prop("checked",false);

					var pingitem = "";
					var traceitem = "";
					var qperfitem = "";
					var curlitem = "";
					var dockeritem = "";
					var dnsitem = "";
					var nodetestresult = data.testparam;

					if(nodetestresult ==undefined){
						pingitem = false;
						traceitem = false;
						qperfitem = false;
						curlitem = false;
						dockeritem = false;
						dnsitem = false;
					}else{
						pingitem = nodetestresult.ping;
						traceitem = nodetestresult.tracepath;
						qperfitem = nodetestresult.qperf;
						curlitem = nodetestresult.curl;
						dockeritem = nodetestresult.docker;
						dnsitem = nodetestresult.dns;
					}
					$("#ping").prop("checked",pingitem);
					$("#trace").prop("checked",traceitem);
					$("#curl").prop("checked",curlitem);
					$("#qperf").prop("checked",qperfitem);
					$("#docker").prop("checked",dockeritem);
					$("#dns").prop("checked",dnsitem);

					if ($(".checkItem:checked").length == $(".checkItem").length) {
		                $("#selectitem").prop("checked", "checked");
		            }
					//前一次的测试值
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
						$("#PoolBlocksizeTarget").val(nodetestresult.dockerPoolBlocksizeTarget);
						$("#BaseDeviceSizeTarget").val(nodetestresult.dockerBaseDeviceSizeTarget);
						$("#BackingFilesystemTarget").val(nodetestresult.dockerBackingFilesystemTarget);
						$("#DatafileTarget").val(nodetestresult.dockerDatafileTarget);
						$("#MetaSpaceUsedTarget").val(nodetestresult.dockerMetaSpaceUsedTarget);
						$("#MetaSpaceAvailableTarget").val(nodetestresult.dockerMetaSpaceAvailableTarget);
						$("#DeferredRemovalEnableTarget").val(nodetestresult.dockerDeferredRemovalEnableTarget);
						$("#MetadatafileTarget").val(nodetestresult.dockerMetadatafileTarget);
						$("#DataSpaceUsedTarget").val(nodetestresult.dockerDataSpaceUsedTarget);
						$("#DataSpaceTotalTarget").val(nodetestresult.dockerDataSpaceTotalTarget);
						$("#DataSpaceAvailableTarget").val(nodetestresult.dockerDataSpaceAvailableTarget);
						$("#MetaSpaceTotalTarget").val(nodetestresult.dockerMetaSpaceTotalTarget);
						$("#UdevSyncSupportedTarget").val(nodetestresult.dockerUdevSyncSupportedTarget);
						$("#DeferredDeletionEnableTarget").val(nodetestresult.dockerDeferredDeletionEnableTarget);
						$("#DeferredDeletedDeviceCountTarget").val(nodetestresult.dockerDeferredDeletedDeviceCountTarget);
					}
				}

			}
		})
	}

	if(flag == true){
		layer.open({
			type : 1,
			title : '检测项信息',
			content : $("#chkitem"),
			area : [ '600px','500px' ],
			btn :  [ '确定', '取消' ],
			yes : function(index, layero) {
				if (!checkitems()) {
					return;// 校验没通过
				}
				layer.close(index);
				//遮挡层
				var loading = layer.load(0,{shade : [ 0.3, '#000' ]});
				//假同步执行清理节点
				var deferClear = $.Deferred();
				deferClear.resolve();
				$.each(selectednodes,function(i,e){


					deferClear = deferClear.then(function () {
					    return $.ajax({
							url:ctx + "/cluster/deleteTestInfo?nodename="+e,
							type:"get",
							success:function(){
								console.log("清理节点"+i+"==="+e);//TODO

							}
						})
				  });
				});
				deferClear.done(function(){
					var deferTest = $.Deferred();
					deferTest.resolve();
					times(selectednodes,0);
				});
			}
		})
	}
}
function times(selectednodes,j){
	if(j >= selectednodes.length){
		return;
	}
	debugger;
	var pingIp = $("#pingip").val();
	var tracepathIp = $("#tracepathip").val();
	var pingtime = $("#pingtime").val()=="" ? 0 : $("#pingtime").val();
	var tracetime = $("#tracepathtime").val()=="" ? 0 : $("#tracepathtime").val();
	var curltime = $("#curltime").val()=="" ? 0 : $("#curltime").val();
	var speed = $("#speed").val()=="" ? 0 : $("#speed").val();
	var latency = $("#qperftime").val()=="" ? 0 : $("#qperftime").val();

	var dockerPoolBlocksize = $("#PoolBlocksizeTarget").val()=="" ? 0 : $("#PoolBlocksizeTarget").val();
	var dockerBaseDeviceSize  = $("#BaseDeviceSizeTarget").val()=="" ? 0 : $("#BaseDeviceSizeTarget").val();
	var dockerBackingFilesystem = $("#BackingFilesystemTarget").val();
	var dockerDatafile = $("#DatafileTarget").val();
	var dockerMetaSpaceUsed  = $("#MetaSpaceUsedTarget").val()=="" ? 0 : $("#MetaSpaceUsedTarget").val();
	var dockerMetaSpaceAvailable = $("#MetaSpaceAvailableTarget").val()=="" ? 0 : $("#MetaSpaceAvailableTarget").val();
	var dockerDeferredRemovalEnable = $("#DeferredRemovalEnableTarget").val();
	var dockerMetadatafile = $("#MetadatafileTarget").val();
	var dockerDataSpaceUsed = $("#DataSpaceUsedTarget").val()=="" ? 0 : $("#DataSpaceUsedTarget").val();
	var dockerDataSpaceTotal = $("#DataSpaceTotalTarget").val()=="" ? 0 : $("#DataSpaceTotalTarget").val();
	var dockerDataSpaceAvailable = $("#DataSpaceAvailableTarget").val()=="" ? 0 : $("#DataSpaceAvailableTarget").val();
	var dockerMetaSpaceTotal = $("#MetaSpaceTotalTarget").val()=="" ? 0 : $("#MetaSpaceTotalTarget").val();
	var dockerUdevSyncSupported = $("#UdevSyncSupportedTarget").val();
	var docekrDeferredDeletionEnable = $("#DeferredDeletionEnableTarget").val();
	var dockerDeferredDeletedDeviceCount = $("#DeferredDeletedDeviceCountTarget").val()=="" ? 0 : $("#DeferredDeletedDeviceCountTarget").val();

	var thisNodeName = selectednodes[j];
	var nodeTestInfo = {
			"nodename":thisNodeName,
			"pingIp":pingIp,
			"pingtimetarget":pingtime,
			"traceIp":tracepathIp,
			"tracetimetarget":tracetime,
			"curltimetarget":curltime,
			"speedtarget":speed,
			"latencytarget":latency,
			"dockerPoolBlocksizeTarget": dockerPoolBlocksize, //0
			"dockerBaseDeviceSizeTarget":dockerBaseDeviceSize, //0
			"dockerBackingFilesystemTarget":dockerBackingFilesystem,
			"dockerDatafileTarget":dockerDatafile,
			"dockerMetaSpaceUsedTarget":dockerMetaSpaceUsed, //0
			"dockerMetaSpaceAvailableTarget":dockerMetaSpaceAvailable, //0
			"dockerDeferredRemovalEnableTarget":dockerDeferredRemovalEnable,
			"dockerMetadatafileTarget":dockerMetadatafile,
			"dockerDataSpaceUsedTarget":dockerDataSpaceUsed, //0
			"dockerDataSpaceTotalTarget":dockerDataSpaceTotal, //0
			"dockerDataSpaceAvailableTarget":dockerDataSpaceAvailable, //0
			"dockerMetaSpaceTotalTarget":dockerMetaSpaceTotal, //0
			"dockerUdevSyncSupportedTarget":dockerUdevSyncSupported,
			"docekrDeferredDeletionEnableTarget":docekrDeferredDeletionEnable,
			"dockerDeferredDeletedDeviceCountTarget":dockerDeferredDeletedDeviceCount //0
		};

	var successInfoBtn = '<a id="'+thisNodeName+'"  nodename="'+thisNodeName+'" onclick="detail(this)" title="查看详细信息">'
					+'<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font></a>';
	var failInfoBtn = '<a id="'+thisNodeName+'"  nodename="'+thisNodeName+'" onclick="detail(this)" title="查看详细信息">'
					+'<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font></a>';
	var progressLength = $("input[name='item']:checked").length;
	var progressWidth = 60/progressLength+'%';
	var testprogressWidth = 60/progressLength-2+'%';
	var count = 0;
	var failNum = 0;
	//选中的测试项
	var ids = new Array();
	var checkedItems = $("input:checkbox[name='item']:checked");
	for(var i=0; i<checkedItems.length; i++){
		var itemId = $(checkedItems[i]).attr("id");
		ids.push(itemId);
	}
	//测试进度条
	var nodeProgressDiv = $('input:checkbox[id="'+thisNodeName+'"]:checked').parents(".thisTr").find(".nodeProgress");
	//测试结果
	var nodeTestOpr = $('input:checkbox[id="'+thisNodeName+'"]:checked').parents(".thisTr").find(".clusterTestOpr");

	nodeTestOpr.empty();
	nodeProgressDiv.empty();
	console.log("节点测试"+j+"==="+thisNodeName);//TODO
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
			if(data.status == '500'){
				layer.msg(data.msg,{icon:2});
				setTimeout("layer.closeAll()",2000);
				return;
			}else{

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
				console.log("节点==="+thisNodeName+"的"+ids[count]+"测试完成");//TODO
				count++;
				nodeProgressDiv.empty().append(testHtmlResule);
				if(count == progressLength){
					times(selectednodes,j+1);
					if(failNum == 0){
						nodeTestOpr.empty().append(successInfoBtn);
					}else{
						nodeTestOpr.empty().append(failInfoBtn);
					}
					if(j == selectednodes.length-1){
						layer.closeAll();
						console.log("关闭遮挡层");
					}
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
						console.log("节点==="+thisNodeName+"的"+ids[count]+"测试完成");//TODO
						count++;
						nodeProgressDiv.empty().append(testHtmlResule);
						if(count == progressLength){
							times(selectednodes,j+1);
							if(failNum == 0){
								nodeTestOpr.empty().append(successInfoBtn);
							}else{
								nodeTestOpr.empty().append(failInfoBtn);
							}
							if(j == selectednodes.length-1){
								layer.closeAll();
								console.log("关闭遮挡层");
							}
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
								console.log("节点==="+thisNodeName+"的"+ids[count]+"测试完成");//TODO
								count++;
								nodeProgressDiv.empty().append(testHtmlResule);
								if(count == progressLength){
									times(selectednodes,j+1);
									if(failNum == 0){
										nodeTestOpr.empty().append(successInfoBtn);
									}else{
										nodeTestOpr.empty().append(failInfoBtn);
									}
									if(j == selectednodes.length-1){
										layer.closeAll();
										console.log("关闭遮挡层");
									}
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
										console.log("节点==="+thisNodeName+"的"+ids[count]+"测试完成");//TODO
										count++;
										nodeProgressDiv.empty().append(testHtmlResule);
										if(count == progressLength){
											times(selectednodes,j+1);
											if(failNum == 0){
												nodeTestOpr.empty().append(successInfoBtn);
											}else{
												nodeTestOpr.empty().append(failInfoBtn);
											}
											if(j == selectednodes.length-1){
												layer.closeAll();
												console.log("关闭遮挡层");
											}
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
												console.log("节点==="+thisNodeName+"的"+ids[count]+"测试完成");//TODO
												count++;
												nodeProgressDiv.empty().append(testHtmlResule);
												if(count == progressLength){
													times(selectednodes,j+1);
													if(failNum == 0){
														nodeTestOpr.empty().append(successInfoBtn);
													}else{
														nodeTestOpr.empty().append(failInfoBtn);
													}
													if(j == selectednodes.length-1){
														layer.closeAll();
														console.log("关闭遮挡层");
													}
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
														console.log("节点==="+thisNodeName+"的"+ids[count]+"测试完成");//TODO
														count++;
														nodeProgressDiv.empty().append(testHtmlResule);
														if(count == progressLength){
															times(selectednodes,j+1);
															if(failNum == 0){
																nodeTestOpr.empty().append(successInfoBtn);
															}else{
																nodeTestOpr.empty().append(failInfoBtn);
															}
															if(j == selectednodes.length-1){
																layer.closeAll();
																console.log("关闭遮挡层");
															}
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
		}
	});
}
