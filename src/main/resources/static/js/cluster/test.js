function detail(obj) {
	var nodename = $(obj).attr('nodename');
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

	$('#cpu').val('');
	$('#memory').val('');
	$('#dockerpass').val('');

	$('#dnspass').val('');
	$('#masterstatus').val('');
	$('#masterdetail').html('');
	$('#standbystatus').val('');
	$('#standbydetail').html('');

	var pingitem = false;
	var traceitem = false;
	var qperfitem = false;
	var curlitem = false;
	var dockeritem = false;
	var dnsitem = false;

	var chkitems = items.split(",");
	for (var i = 0; i < chkitems.length; i++) {
		if (chkitems[i] == 'pingitem') {
			pingitem = true;
			continue;
		}
		if (chkitems[i] == 'traceitem') {
			traceitem = true;
			continue;
		}
		if (chkitems[i] == 'qperfitem') {
			qperfitem = true;
			continue;
		}
		if (chkitems[i] == 'curlitem') {
			curlitem = true;
			continue;
		}
		if (chkitems[i] == 'dockeritem') {
			dockeritem = true;
			continue;
		}
		if (chkitems[i] == 'dnsitem') {
			dnsitem = true;
			continue;
		}
	}

	for (var i = 0; i < nodeInfos.length; i++) {
		if (nodename == nodeInfos[i].nodename) {
			if (pingitem) {
				$('#pingstatus').val(nodeInfos[i].ping ? '成功' : '失败');
				$('#pingpass').val(nodeInfos[i].pingpass ? '通过' : '未通过');
				$('#pingavg').val(nodeInfos[i].pingtime);
				$('#pingdetail').html(nodeInfos[i].pingoutmsg);
			}
			if (traceitem) {
				$('#tracestatus').val(nodeInfos[i].tracepath ? '成功' : '失败');
				$('#tracepass').val(nodeInfos[i].tracepass ? '通过' : '未通过');
				$('#tracetime').val(nodeInfos[i].tracetime);
				$('#tracedetail').html(nodeInfos[i].tracepathoutmsg);
			}
			if (qperfitem) {
				$('#qperfstatus').val(nodeInfos[i].qperf ? '成功' : '失败');
				$('#qperfpass').val(nodeInfos[i].qperfpass ? '通过' : '未通过');
				$('#speed').val(nodeInfos[i].speed);
				$('#latency').val(nodeInfos[i].latency);
				$('#qperfdetail').html(nodeInfos[i].qperfoutmsg);
			}
			if (curlitem) {
				$('#curlstatus').val(nodeInfos[i].curl ? '成功' : '失败');
				$('#curlpass').val(nodeInfos[i].curlpass ? '通过' : '未通过');
				$('#curlavg').val(nodeInfos[i].curltime);
				$('#curldetail').html(nodeInfos[i].curloutmsg);
			}
			if (dockeritem) {
				$('#cpu').val(nodeInfos[i].cpu);
				$('#memory').val(nodeInfos[i].memory);
				$('#dockerpass').val(nodeInfos[i].dockerpass ? '通过' : '未通过');
			}
			if (dnsitem) {
				$('#dnspass').val(nodeInfos[i].dnspass ? '通过' : '未通过');
				$('#masterstatus').val(nodeInfos[i].masterdns ? '成功' : '失败');
				$('#masterdetail').html(nodeInfos[i].masterdnsoutmsg);
				$('#standbystatus').val(nodeInfos[i].standbydns ? '成功' : '失败');
				$('#standbydetail').html(nodeInfos[i].standbydnsoutmsg);
			}
			break;
		}
	}

	layer.open({
		type : 1,
		title : nodename + '节点检测信息',
		content : $("#detail"),
		area : [ '600px' ],
		btn : false,
	})
}

var nodeInfos = {};
var items = "";

$(document)
		.ready(
				function() {
					var deployHtml = '<div class="progress-bar progress-bar-warning" role="progressbar"'
						 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
						 +'style="width: 15%;">'
						 +'<span >部署完成</span>'
						 +'</div>';
					var deployedpod = $("#deployednodes").val().split(',');
					for (var i = 0; i < deployedpod.length; i++) {
						if (deployedpod[i] != '') {
//							$("input[value='" + deployedpod[i] + "']").prop(
//									"checked", true);
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
//
					$("#selectitem").change(function() {
						if ($(this).prop('checked')) {
							$("input[name='item']").prop("checked", true);
						} else {
							$("input[name='item']").prop("checked", false);
						}
					});

					// 测试部署，部署pod和service
					$("#deployBtn").click(function() {
						var selectednodes = "";
						$("input:checkbox[name='node']:checked").each(function(i) {
							if (0 == i) {
								selectednodes = $(this).val();
							} else {
								selectednodes += ("," + $(this).val());
							}
						});
						// alert(selectednodes);
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
									$('#deployednodes').val(selectednodes);
//									var deployHtml = '<div class="progress-bar progress-bar-warning" role="progressbar"'
//													 +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'
//													 +'style="width: 15%;">'
//													 +'<span >部署完成</span>'
//													 +'</div>';
									$("input:checkbox[name='node']:checked").parent().parent().find(".nodeProgress").empty().append(deployHtml);
									//layer.msg("部署成功！",{icon : 6});
								} else {
									$('#deployednodes').val("");
									layer.msg("部署失败！",{icon : 5});
								}
							}
						});
					});

					// 清除部署
					$("#deleteBtn").click(function() {
						//$('.checkbox a').empty();
						$.ajax({
							url : ctx + "/cluster/deployclear",
							success : function(data) {
								var data = eval("(" + data + ")");
								if (data.status == '200') {
									$('#deployednodes').val("");
									layer.msg("清除部署成功！", {
										icon : 6
									});
								}
							}
						});
					});

					// 执行集群测试
					$("#excuteBtn").click(
							function() {

								layer.open({
									type : 1,
									title : '检测项信息',
									content : $("#chkitem"),
									area : [ '600px' ],
									btn :  [ '确定', '取消' ],
									yes : function(index, layero) {

										// 是否已经部署过
										var selectednodes = $('#deployednodes').val();
										if (selectednodes == "") {
											layer.msg("执行测试前请先部署！", {
												icon : 5
											});
											layer.close(index);
											return;
										}

										if (!checkitems()) {
											return;// 校验没通过
										}

										var selecteditems = "";
										$("input:checkbox[name='item']:checked").each(
												function(i) {
													if (0 == i) {
														selecteditems = $(this).val();
													} else {
														selecteditems += ("," + $(this)
																.val());
													}
												});
										items = selecteditems;
										// 显示正在执行中
										var idx = layer.load(0, {
											shade : [ 0.3, '#000' ]
										});

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
													layer.msg("检测成功！", {
														icon : 6
													});
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

					function initDetail(data) {
						for (var i = 0; i < data.nodeInfos.length; i++) {
							if (data.nodeInfos[i].allpass) {
								$('#' + data.nodeInfos[i].nodename)
										.append(
												'<font color="#33CC33" style="font-weight:bold">通过<i class="fa fa-question-circle"></i></font>');
							} else {
								$('#' + data.nodeInfos[i].nodename)
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
						if ($("input:checkbox[value='pingitem']").prop(
								'checked')) {

							var ip = $("#pingip").val();
							if (ip.length === 0) {
								layer.tips('ip地址不能为空', '#pingip', {
									tips : [ 1, '#0FA6D8' ]
								});
								$('#pingip').focus();
								return;
							}

							if (ip
									.search(/^([1-9]|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])(\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])){3}$/ig) === -1) {
								layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx',
										'#pingip', {
											tips : [ 1, '#0FA6D8' ]
										});
								$('#pingip').focus();
								return;
							}
						}
						// 校验tracepathchk项的输入
						if ($("input:checkbox[value='traceitem']").prop(
								'checked')) {
							ip = $("#tracepathip").val();
							if (ip.length === 0) {
								layer.tips('ip地址不能为空', '#tracepathip', {
									tips : [ 1, '#0FA6D8' ]
								});
								$('#tracepathip').focus();
								return;
							}

							if (ip
									.search(/^([1-9]|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])(\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])){3}$/ig) === -1) {
								layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx',
										'#tracepathip', {
											tips : [ 1, '#0FA6D8' ]
										});
								$('#tracepathip').focus();
								return;
							}
						}
						return true;
					}

				});
