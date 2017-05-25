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
					area : [ '600px' ],
					btn : ['关闭']
				})
				if(nodetestresult.ping){
					$('#ptab').show();
					$('#pingstatus').val('已设置');
					$('#pingpass').val(nodetestresult.pingpass ? '通过' : '未通过');
					$('#pingavg').val(nodetestresult.pingtime);
					$('#pingdetail').html(nodetestresult.pingoutmsg);
				}else{
					$('#ptab').hide();
					$('#pingstatus').val('未设置');
				}
				if(nodetestresult.tracepath){
					$('#ttab').show();
					$('#tracestatus').val('已设置');
					$('#tracepass').val(nodetestresult.tracepass ? '通过' : '未通过');
					$('#tracetime').val(nodetestresult.tracetime);
					$('#tracedetail').html(nodetestresult.tracepathoutmsg);
				}else{
					$('#ttab').hide();
					$('#tracestatus').val('未设置');
				}
				if(nodetestresult.qperf){
					$('#qtab').show();
					$('#qperfstatus').val('已设置');
					$('#qperfpass').val(nodetestresult.qperfpass ? '通过' : '未通过');
					$('#speed').val(nodetestresult.speed);
					$('#latency').val(nodetestresult.latency);
					$('#qperfdetail').html(nodetestresult.qperfoutmsg);
				}else{
					$('#qtab').hide();
					$('#qperfstatus').val('未设置');
				}
				if(nodetestresult.curl){
					$('#ctab').show();
					$('#curlstatus').val('已设置');
					$('#curlpass').val(nodetestresult.curlpass ? '通过' : '未通过');
					$('#curlavg').val(nodetestresult.curltime);
					$('#curldetail').html(nodetestresult.curloutmsg);
				}else{
					$('#ctab').hide();
					$('#curlstatus').val('未设置');
				}
				if(nodetestresult.docker){
					$('#dtab').show();
					$('#dockerstatus').val('已设置');
					$('#memory').val(nodetestresult.disk);
					$('#dockerpass').val(nodetestresult.dockerpass ? '通过' : '未通过');
					$('#dockerdetail').html(nodetestresult.dockermsg);
				}else{
					$('#dtab').hide();
					$('#dockerstatus').val('未设置');
				}
				if(nodetestresult.dns){
					$('#dstab').show();
					$('#dnsstatus').val('已设置');
					$('#dnspass').val(nodetestresult.dnspass ? '通过' : '未通过');
					$('#masterstatus').val(nodetestresult.masterdns ? '成功' : '失败');
					$('#masterdetail').html(nodetestresult.masterdnsoutmsg);
					$('#standbystatus').val(nodetestresult.standbydns ? '成功' : '失败');
					$('#standbydetail').html(nodetestresult.standbydnsoutmsg);
				}else{
					$('#dstab').hide();
					$('#dnsstatus').val('未设置');
				}
				$('#myTab li').each(function(){
					$(this).attr('class','active');
				});
				$($('#myTab li:visible')[0]).attr('class','active');

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
	$.ajax({
		url : ctx + "/cluster/deploypodfortest?nodenames=" + selectednode,
		success : function(data) {
			var data = eval("(" + data + ")");
			layer.closeAll("loading");
			if (data.status == '200') {
				var deployHtmlSuccess = '<div class="progress-bar progress-bar-warning" role="progressbar"'
								 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
								 +'style="width: 15%;">'
								 +'<span >部署完成</span>'
								 +'</div>';

				$(obj).parents(".thisTr").find(".nodeProgress").empty().append(deployHtmlSuccess);
				$(obj).parents(".thisTr").find(".clusterTestOpr").children("a").empty();
				$(obj).parent().attr("deployStatus","true");
				$(obj).parents(".thisTr").find("input:checkbox").attr("deployStatus","true");
			} else {
				var deployHtmlFailure = '<div class="progress-bar progress-bar-danger" role="progressbar"'
					 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
					 +'style="width: 15%;">'
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
			if(status == '200'){
				var nodetestresult = data.nodetestresult;
				var pingitem = nodetestresult.ping;
				var traceitem = nodetestresult.tracepath;
				var qperfitem = nodetestresult.qperf;
				var curlitem = nodetestresult.curl;
				var dockeritem = nodetestresult.docker;
				var dnsitem = nodetestresult.dns;

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
		}
	});
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
			var loading = layer.load(0, {shade : [ 0.3, '#000' ]});

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
					+ selectednode + "&items="
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
						//initDetail(data);
						nodeInfos = data.nodeInfos;
						if(data.nodeInfos[0].allpass){
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

							$(obj).parents(".thisTr").find(".nodeProgress").empty().append(testHtmlSuccess);
							$(obj).parents(".thisTr").find(".clusterTestOpr").children("a").empty().append('<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
						}else{
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
							$(obj).parents(".thisTr").find(".nodeProgress").empty().append(testHtmlFailure);
							$(obj).parents(".thisTr").find(".clusterTestOpr").children("a").empty().append('<font color="#FF0033" style="font-weight:bold">未通过<i class="fa fa-question-circle"></i></font>');
						}
						layer.close(index);
					} else {
						layer.alert(data.msg,{icon: 2});
					}
				}
			});
		}
	})
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
	}
	return true;
}

var nodeInfos = {};
var items = "";

$(document).ready(function() {
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
			url : ctx + "/cluster/clearspecifiedpod?nodenames=" + selectednodes,
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
