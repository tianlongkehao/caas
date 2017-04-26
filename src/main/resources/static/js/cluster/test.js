$(function() {

	$("#selectnode").change(function() {
		if ($(this).prop('checked')) {
			$("input[name='node']").prop("checked", true);
		} else {
			$("input[name='node']").prop("checked", false);
		}
	});

	/*
	 * $("#selectitem").change(function() { if ($(this).prop('checked')) {
	 * $("input[name='item']").prop("checked", true); } else {
	 * $("input[name='item']").prop("checked", false); } });
	 */

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
	$("#excuteBtn").click(
			function() {
				// 是否已经部署过
				var selectednodes = $('#deployednodes').val();
				if (selectednodes == "") {
					layer.msg("执行测试前请先部署！", {
						icon : 5
					});
					return;
				}

				/*
				 * //是否选择了检测项 var selecteditems = "";
				 * $("input:checkbox[name='item']:checked").each(function(i) {
				 * if (0 == i) { selecteditems = $(this).val(); } else {
				 * selecteditems += ("," + $(this).val()); } });
				 *
				 * if(selecteditems==""){ layer.msg("请选择至少一项检测项！",{icon: 5});
				 * return; }
				 */

				if (!checkitems()) {
					return;// 校验没通过
				}

				//显示正在执行中
				var index = layer.load(0, {
					shade : [ 0.3, '#000' ]
				});

				$.ajax({
					url : ctx + "/cluster/excutetest?nodenames="+ selectednodes,
					success : function(data) {
						var data = eval("(" + data + ")");
						layer.closeAll("loading");
						if (data.status == '200') {
							layer.msg("完成检测！", {
								icon : 6
							});
						}
					}
				});
			});

	// 校验
	function checkitems() {
		// 校验ping项的输入
		var ip = $("#pingip").val();
		if (ip.length === 0) {
			layer.tips('ip地址', '#pingip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#pingip').focus();
			return;
		}

		if (ip.search(/^([1-9]|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])(\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])){3}$/ig) === -1) {
			layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx', '#pingip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#pingip').focus();
			return;
		}

		// 校验tracepathchk项的输入
		ip = $("#tracepathip").val();
		if (ip.length === 0) {
			layer.tips('ip地址', '#tracepathip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#tracepathip').focus();
			return;
		}

		if (ip.search(/^([1-9]|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])(\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])){3}$/ig) === -1) {
			layer.tips('你的ip地址格式不是xxx.xxx.xxx.xxx', '#tracepathip', {
				tips : [ 1, '#0FA6D8' ]
			});
			$('#tracepathip').focus();
			return;
		}

		return true;
	}

	function pingdetail() {
		layer.open({
			type : 1,
			title : 'ping测试信息',
			content : $("#pingdetail"),
			area : [ '600px' ],
			btn : false,
		})
	}

	function tracepathdetail() {

	}

	function curldetail() {

	}

	function qperfdetail() {

	}

	function dockerdetail() {

	}

	function dnsdetail() {

	}

});
