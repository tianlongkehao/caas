$(document).ready(function () {
//$(function() {

	$("#selectnode").change(function() {
		if ($(this).prop('checked')) {
			$("input[name='node']").prop("checked", true);
		} else {
			$("input[name='node']").prop("checked", false);
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
			layer.tips('请选择至少一个集群节点', '#selectnode', {
				tips : [ 1, '#3595CC' ]
			});
			$('#selectnode').focus();
			return;
		}
		var index = layer.load(0, {
			shade : [ 0.3, '#000' ]
		});
		$.ajax({
			url : ctx + "/cluster/deploypodfortest?nodenames=" + selectednodes,
			success : function(data) {
				var data = eval("(" + data + ")");
				layer.closeAll("loading");
				// layer.close(index);
				if (data.status == '200') {
					$('#deployednodes').val(selectednodes);
					// alert($('#deployednodes').val());
					layer.msg("部署成功！", {
						icon : 6
					});
				} else {
					$('#deployednodes').val("");
					layer.msg("部署失败！", {
						icon : 5
					});
				}
			}
		});
	});

	// 清除部署
	$("#deleteBtn").click(function() {
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
	$("#excuteBtn").click(function() {
		// 是否已经部署过
		var selectednodes = $('#deployednodes').val();
		if (selectednodes == "") {
			layer.msg("执行测试前请先部署！", {
				icon : 5
			});
			return;
		}

		if (!checkitems()) {
			return;// 校验没通过
		}

		// 显示正在执行中
		var index = layer.load(0, {
			shade : [ 0.3, '#000' ]
		});

		var pingIp = $("#pingip").val();
		var tracepathIp = $("#tracepathip").val();
		$.ajax({
			url : ctx + "/cluster/excutetest?nodenames=" + selectednodes+"&pingIp="+pingIp+"&tracepathIp="+tracepathIp,
			success : function(data) {
				var data = eval("(" + data + ")");
				layer.closeAll("loading");
				if (data.status == '200') {
					initDetail(data);
					layer.msg("检测成功！", {
						icon : 6
					});
				} else {
					layer.msg("检测失败,请联系系统管理员！", {
						icon : 5
					});
				}
			}
		});
	});

	function initDetail(data){
		//初始化docker检测信息
		$('#dockerTable').empty();
		$('#dockerlink').empty();
		var tr='';
		var dockerresult = true;
		var docker =$('#docker').val();
		for (var i = 0; i < data.dockerList.length; i++) {
			tr+='<tr><td>'+data.dockerList[i].nodename
			+'</td><td>'+data.dockerList[i].cpu
			+'</td><td>'+data.dockerList[i].memory
			+'</td></tr>';
			if(docker!=data.dockerList[i].memory){
				dockerresult = false;
			}
		}
		$('#dockerTable').append(tr);
		if(dockerresult){
			$('#dockerlink').append('<font color="#33CC33" style="font-weight:bold">通过</font>');
		}else{
			$('#dockerlink').append('<font color="#FF0033" style="font-weight:bold">未通过</font>');
		}
	}

	// 校验
	function checkitems() {
		// 校验ping项的输入
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
			layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx', '#pingip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#pingip').focus();
			return;
		}

		// 校验tracepathchk项的输入
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
			layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx', '#tracepathip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#tracepathip').focus();
			return;
		}

		return true;
	}

	function pingdetail() {

	}

	function tracepathdetail() {

	}

	function curldetail() {

	}

	function qperfdetail() {

	}

	$("#dockerlink").click(function() {
		layer.open({
		 	type: 1,
	        title: 'docker检测信息',
	        content: $("#dockerdetail"),
	        area: ['600px'],
	        btn: false,
	    })
	});

	function dnsdetail() {

	}

});
