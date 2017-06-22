$(function() {
    loadStrategyList();
})

//加载策略列表
function loadStrategyList(){
	$.ajax({
		url:""+ctx+"/storage/strategyList",
		type:"get",
		success:function(data){
        var data = eval("("+data+")");
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.snapStrategies.length;
        	for(var i=0; i<len; i++){
        			var strategy = data.snapStrategies[i];
        			itemsHtml += '<tr strategyId='+strategy.id+' strategyName="'+strategy.name+'" bindCount="'+strategy.bindCount +'"keep='+strategy.keep+' time="'+strategy.time +'" week="'+strategy.week +'">'
        						+'<td style="width: 5%; text-indent:30px">'
        						+'<input class="chkItem" type="checkbox" id="'+strategy.id+'">'
        						+'</td>'
        						+'<td>'+strategy.name+'</td>'
        						+'<td>'+strategy.bindCount+'</td>'
        						+'<td>'+strategy.excutingCount+'</td>'
        						+'<td>'+strategy.createDate+'</td>'
        						+'<td>'+strategy.endData+'</td>'
        						+'<td style="width: 10%;" class="item-operation">'
                            	+'<a onclick="snapStrategyEdit(this)" title="修改策略"><i class="fa fa-edit fa-opr"></i></a>'
                            	+'<a onclick="setStorage(this)" title="设置磁盘"><i class="fa fa-cog fa-opr"></i></a>'
                            	+'<a onclick="delOneStorage(this)" title="删除磁盘"><i class="fa fa-trash fa-opr"></i></a>'
                                +'</td>'
        				        +'</tr>';
        	}
        	$("#strategyList").empty().append(itemsHtml);
		}
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0,6 ]
			} ],
			"aaSorting": [[ 4, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		}
	});
}

function checkStrategy() {
	var strategyname = $("#strategyname").val();
	if (strategyname == '') {
		layer.tips('策略名称为空！', '#strategyname', {
			tips : [ 1, '#3595CC' ]
		});
		$('#strategyname').focus();
		return false;
	}

	if ($("input:checkbox[name='time']:checked").length == 0) {
		layer.msg("创建时间为空！", {
			icon : 5
		});
		return false;
	}

	if ($("input:checkbox[name='week']:checked").length == 0) {
		layer.msg("重复日期为空！", {
			icon : 5
		});
		return false;
	}

	if ($("input:radio[name='retainTime']:checked").length == 0) {
		layer.msg("保留时间为空！", {
			icon : 5
		});
		return false;
	} else {
		if ($('input:radio[name="retainTime"]:checked').val() == '1') {
			if ($('#keep').val() == '') {
				layer.tips('自定义时长为空！', '#keep', {
					tips : [ 1, '#3595CC' ]
				});
				$('#keep').focus();
				return false;
			}
		}
	}

	return true;
}

function snapStrategyAdd() {
	layer.open({
		type : 1,
		title : '创建自动快照策略',
		area : [ '500px' ],
		content : $("#snapStrategyInfo"),
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			if (!checkStrategy()) {
				return;
			}

			var name = $("#strategyname").val();
			var time = "";
			$("input:checkbox[name='time']:checked").each(function(i) {
				if (0 == i) {
					time = $(this).val();
				} else {
					time += ("," + $(this).val());
				}
			});
			var week = "";
			$("input:checkbox[name='week']:checked").each(function(i) {
				if (0 == i) {
					week = $(this).val();
				} else {
					week += ("," + $(this).val());
				}
			});
			var keep;
			if ($('input:radio[name="retainTime"]:checked').val() == '1') {
				keep = $('#keep').val();
			}
			if ($('input:radio[name="retainTime"]:checked').val() == '2') {
				keep = 100000;
			}

			layer.close(index);
			// 增加快照策略
			$.ajax({
				url : ctx + "/ceph/createSnapStrategy?name=" + name + "&time="
						+ time + "&week=" + week + "&keep=" + keep,
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == '200') {
						layer.msg("创建快照策略成功！", {
							icon : 6
						});
						location.reload();
					} else {
						layer.msg("创建快照策略失败！", {
							icon : 5
						});
					}
				}
			});
		}
	})
}

function snapStrategyEdit(obj) {
	$("input:checkbox[name='time']:checked").each(function(i) {
		$(this).prop('checked', false);
	});

	$("input:checkbox[name='week']:checked").each(function(i) {
		$(this).prop('checked', false);
	});

	$('input:radio[name="retainTime"]:checked').prop('checked', false);

	var id = $(obj).parent().parent().attr("strategyId");
	var name = $(obj).parent().parent().attr("strategyName");
	var time = $(obj).parent().parent().attr("time");
	var week = $(obj).parent().parent().attr("week");
	var keep = $(obj).parent().parent().attr("keep");

	$("#strategyname").val(name);

	var times = time.split(',');
	for (var i = 0; i < times.length; i++) {
		$("input:checkbox[name='time']").each(function(index) {
			if($(this).val()==times[i]){
				$(this).prop('checked', true);
			}
		});
	}

	var weeks = week.split(',');
	for (var i = 0; i < weeks.length; i++) {
		$("input:checkbox[name='week']").each(function(index) {
			if($(this).val()==weeks[i]){
				$(this).prop('checked', true);
			}
		});
	}

	if (keep == 100000) {
		$("input:radio[value='2']").prop('checked', true);
	} else {
		$("input:radio[value='1']").prop('checked', true);
		$('#keep').val(keep);
	}

	layer.open({
		type : 1,
		title : '修改策略',
		area : [ '500px' ],
		content : $("#snapStrategyInfo"),
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			if (!checkStrategy()) {
				return;
			}

			 name = $("#strategyname").val();
			 time = "";
			$("input:checkbox[name='time']:checked").each(function(i) {
				if (0 == i) {
					time = $(this).val();
				} else {
					time += ("," + $(this).val());
				}
			});
			 week = "";
			$("input:checkbox[name='week']:checked").each(function(i) {
				if (0 == i) {
					week = $(this).val();
				} else {
					week += ("," + $(this).val());
				}
			});

			if ($('input:radio[name="retainTime"]:checked').val() == '1') {
				keep = $('#keep').val();
			}
			if ($('input:radio[name="retainTime"]:checked').val() == '2') {
				keep = 100000;
			}

			layer.close(index);
			// 更新快照策略
			$.ajax({
				url : ctx + "/ceph/updateSnapStrategy?id=" + id + "&name="
						+ name + "&time=" + time + "&week=" + week + "&keep="
						+ keep,
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == '200') {
						layer.msg("更新快照策略成功！", {
							icon : 6
						});
						location.reload();
					} else {
						layer.msg(data.msg, {
							icon : 5
						});
					}
				}
			});
		}
	})
}

function delOneStorage(obj) {
	var id = $(obj).parent().parent().attr("strategyId");
	layer.open({
		title : '删除自动快照策略',
		content : "是否确定删除该自动快照策略？",
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			layer.close(index);
			// 更新快照策略
			$.ajax({
				url : ctx + "/ceph/removeSnapStrategy?strategyId=" + id,
				success : function(data) {
					var data = eval("(" + data + ")");
					if (data.status == '200') {
						layer.msg("删除快照策略成功！", {
							icon : 6
						});
						location.reload();
					} else {
						layer.msg(data.msg, {
							icon : 5
						});
					}
				}
			});
		}
	})
}

function setStorage(obj) {
	$('#not').empty();
	$('#have').empty();
	var id = $(obj).parent().parent().attr("strategyId");

	$.ajax({
		url : ctx + "/ceph/snapStrategyInfo?strategyId=" + id,
		success : function(data) {
			var data = eval("(" + data + ")");
			if (data.status == '200') {

				var unbindedRbd = data.unbindedRbd;
				var bindedRbd =  data.bindedRbd;
				var tr="";
				for(var i=0;i<unbindedRbd.length;i++){
                    tr="<tr strategyId="+id+" imgId="+unbindedRbd[i].id+">"
                    	+"<td>"+unbindedRbd[i].name+"</td>"
                    	+"<td>"+unbindedRbd[i].size+"</td>"
                    	+"<td>"+unbindedRbd[i].detail+"</td>"
                    	+"<td><button class=\"textBtn\" onclick=\"bind(this)\">绑定</button></td>"
                    	+"</tr>";
                    $('#not').append(tr);
				}

				for(var i=0;i<bindedRbd.length;i++){
					tr="<tr strategyId="+id+" imgId="+bindedRbd[i].id+">"
                	+"<td>"+bindedRbd[i].name+"</td>"
                	+"<td>"+bindedRbd[i].size+"</td>"
                	+"<td>"+bindedRbd[i].detail+"</td>";
					if(bindedRbd[i].strategyexcuting){
						tr=tr+"<td><button class=\"textBtn\" onclick=\"cancel(this)\">取消执行</button></td>";
					}else{
						tr=tr+"<td><button class=\"textBtn\" onclick=\"unbind(this)\">解除绑定</button>";
						tr=tr+"<button class=\"textBtn\" onclick=\"excute(this)\">执行策略</button></td>"
					}
                	tr=tr+"</tr>";
                	$('#have').append(tr);
				}

				layer.open({
					type : 1,
					title : '设置策略',
					area : [ '900px','600px' ],
					content : $("#setStrategyInfo")
				})

			}
		}
	});

}

function bind(obj){
	var strategyId = $(obj).parent().parent().attr("strategyId");
	var imgId = $(obj).parent().parent().attr("imgId");

	// 绑定快照策略
	$.ajax({
		url : ctx + "/ceph/bindSnapStrategy?imgId="+imgId+"&strategyId=" + strategyId,
		success : function(data) {
			var data = eval("(" + data + ")");
			if (data.status == '200') {
				layer.msg("绑定成功！", {
					icon : 6
				});

				var parent = $(obj).parent();
				$(obj).remove();
				$(parent).append("<button class=\"textBtn\" onclick=\"unbind(this)\">解除绑定</button>" +
						"<button class=\"textBtn\" onclick=\"excute(this)\">执行策略</button>");
				var grandpa = $(parent).parent();
				$('#have').append($(grandpa));

			} else {
				layer.msg(data.msg, {
					icon : 5
				});
			}
		}
	});
}

function unbind(obj){
	var imgId = $(obj).parent().parent().attr("imgId");

	// 解除绑定快照策略
	$.ajax({
		url : ctx + "/ceph/unbindSnapStrategy?imgId="+imgId,
		success : function(data) {
			var data = eval("(" + data + ")");
			if (data.status == '200') {
				layer.msg("解除绑定成功！", {
					icon : 6
				});

				var parent = $(obj).parent();
				$(parent).empty();
				$(parent).append("<button class=\"textBtn\" onclick=\"bind(this)\">绑定</button>");
				var grandpa = $(parent).parent();
				$('#not').append($(grandpa));

			} else {
				layer.msg(data.msg, {
					icon : 5
				});
			}
		}
	});
}

function excute(obj){
	var imgId = $(obj).parent().parent().attr("imgId");
	//var strategyId = $(obj).parent().parent().attr("strategyId");

	// 执行快照策略
	$.ajax({
		url : ctx + "/ceph/excuteSnapStrategy?imgId="+imgId,
		success : function(data) {
			var data = eval("(" + data + ")");
			if (data.status == '200') {
				layer.msg("执行策略成功！", {
					icon : 6
				});

				var parent = $(obj).parent();
				$(parent).empty();
				$(parent).append("<button class=\"textBtn\" onclick=\"cancel(this)\">取消执行</button>");

			} else {
				layer.msg(data.msg, {
					icon : 5
				});
			}
		}
	});
}

function cancel(obj){
	var imgId = $(obj).parent().parent().attr("imgId");

	// 取消执行
	$.ajax({
		url : ctx + "/ceph/cancelSnapStrategy?imgId="+imgId,
		success : function(data) {
			var data = eval("(" + data + ")");
			if (data.status == '200') {
				layer.msg("取消执行成功！", {
					icon : 6
				});

				var parent = $(obj).parent();
				$(parent).empty();
				$(parent).append("<button class=\"textBtn\" onclick=\"unbind(this)\">解除绑定</button><button class=\"textBtn\" onclick=\"excute(this)\">执行策略</button>");

			} else {
				layer.msg(data.msg, {
					icon : 5
				});
			}
		}
	});
}