$(function(){
	//loadStorageList();
	$("#storageQuickAdd").click(function(){
		var reg = /^[0-9a-zA-Z]+$/;//正则表达式，名称只能为数字和字母，或者两者的组合

		layer.open({
			type:1,
			title: '创建云盘',
			area: ['500px', '416px'],
			content: $("#storageQuickAddInfo"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				var blockname = $("#q-storageName").val();
				if(blockname==""||!reg.test(blockname)){
					layer.tips('磁盘名称不能为空,且只能为数字和字母或者两者的组合！', '#q-storageName', {
						tips : [ 1, '#0FA6D8' ]
					});
					$('#q-storageName').focus();
					return;
				}
				var size=$('input:radio[name="updateStorageSize"]:checked').val();
				if(size ==null){
					alert("请至少选中一个磁盘大小选项！");
					return;
				}
				var disksize;
				if(size="20480"){
					disksize =20;
				}else if(size="51200"){
					disksize =50;
				}else if(size="102400"){
					disksize =100;
				}else{
					disksize = $("#updatedefVol").val();
				}

				if($("#restVol").html()!=""&&disksize>$("#restVol").html()){
					alert("剩余磁盘空间不足，请重新分配磁盘大小！");
					return;
				}

				var diskdetail= $("#storage-mark").val();

				layer.close(index);
				//先检查磁盘名称是否存在，然后新建
				$.ajax({
					url:""+ctx+"/ceph/checkrbd?imgname="+blockname,
					type:"get",
					success:function(data){
                         var data = eval("("+data+")");
                        if(data.status=='500'){
                        	alert("ceph异常！")
                        	return;
                        }
                        if(data.status=='200'){
                        	if(data.exist==1){
                        		alert("磁盘名称重复，请重命名！")
                        		return;
                        	}else{
                        		$.ajax({
                					url:""+ctx+"/ceph/createcephrbd?imgname="+blockname+"&disksize="+disksize
                					     +"&diskdetail="+diskdetail,
                					type:"get",
                					success:function(data){
                						var data = eval("("+data+")");
                						if(data.status=='500'){
                							layer.msg("磁盘创建失败！",{icon : 5});
                						}
                						if(data.status=='200'){
                							layer.msg("磁盘创建成功！",{icon: 6});
                							location.reload();
                						}
                						return;
                					}
                				});
                        	}
                        }
					}
				});

				}
		})
	});

	$(".baseInfo>ul>li>a").click(function(){

        $(".baseInfo>ul>li>a").removeClass("btn-prim");
        $(this).addClass("btn-prim");
    });

    $(".BASE").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerInfo").removeClass("hide");
    });


    $(".INSTANCES").click(function(){

        $(".contentMain>div:not('.baseInfo')").addClass("hide");
        $(".containerInstances").removeClass("hide");
    });
})

/*function loadStorageList(){
	$.ajax({
		url:""+ctx+"/service/storageList",
		type:"post",
		data:{pageable:"pageable"},
		success:function(data){
        var data = eval("("+data+")");
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.storages.length;
        	for(var i=0; i<len; i++){
        			var storage = data.storages[i];
        			var useType = storage.useType ==1 ? "未使用" : "使用" ;

        			if(storage.mountPoint == null || storage.mountPoint == ""){
        				storage.mountPoint = "未挂载";
        			}
        			itemsHtml += ' <tr class="ci-listTr" style="cursor:auto">'+
        							'<td style="width: 5%; text-indent: 30px;"><input type="checkbox" class="chkItem" name="chkItem" value='+storage.id+' /></td>'+
        							' <td style="width: 15%; text-indent:30px;" id = "storageName">'+
        							'<a href="'+ctx+'/service/storage/detail/'+storage.id+'" title="查看详细信息">'+storage.storageName +'</a>'+
        							'</td>'+
        							' <td style="width: 15%; text-indent:15px;"  class="cStatusColumn">' +
        								useType +
        							' </td>'+
        							//' <td style="width: 10%;">' + storage.format + '</td>'+
        							' <td style="width: 15%; text-indent:8px;word-wrap:break-word;word-break:break-all;">' + storage.mountPoint + '</td>'+
        							' <td style="width: 15%; text-indent:10px;">' + storage.storageSize + ' M</td>'+
        							' <td style="width: 15%;">' + storage.createDate + '</td>'+
        							' <td style="width: 10%; text-indent:5px;">' +
        							' <a class="format formatStorage" title="格式化" storageName="'+storage.storageName +'" isVolReadOnly="'+storage.volReadOnly+'"><i class="fa fa-eraser"></i></a>'+
    									' <a class="dilation dilatationStorage" style="margin-left: 5px" title="扩容" storageId="'+storage.id +'" storageSize="'+ storage.storageSize +'" storageName="' + storage.storageName +'"><i class="fa fa-arrows"></i></a>'+
    									' <a class="delete deleteStorage" style="margin-left: 5px" title="删除" storageId="'+storage.id +'"><i class="fa fa-trash"></i></a>'+
        							'</td>'+
        						+'</tr>';
        	}
        	//$('#storageList').html(itemsHtml);
		}
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 7 ]
			} ],
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		}
	});
}*/



//修改磁盘属性
function changeProperty(obj){
    var rbd = $(obj).parent().parent().parent().parent().attr("rbd");
    var size =$(obj).parent().parent().parent().parent().attr("size");
    var release = $(obj).parent().parent().parent().parent().attr("release");

    $("#rbd").val(rbd);
	$("#size").val(size);
    if(release =='true'){
    	$("#release").prop("checked", true);
    }else{
    	$("#release").prop("checked", false);
    }

	layer.open({
		type:1,
			title: '修改磁盘属性',
			area: ['500px'],
			content: $("#changeProperty"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
                 var release = $("#release").prop('checked');
                 layer.close(index);

                 $.ajax({
  					url:""+ctx+"/ceph/updaterbdproperty?imgname="+rbd+"&release="+release,
  					type:"get",
  					success:function(data){
  						var data = eval("(" + data + ")");
  						if(data.status =='500'){
  							layer.msg(data.msg,{icon : 5});
  						}else{
  							layer.msg("修改磁盘属性成功！",{icon : 6});
  							location.reload();
  						}
  						return;
  					}
  				});
			}
	})
}

//修改磁盘描述
function changeDescribe(obj){
	var rbd = $(obj).parent().parent().parent().parent().attr("rbd");
	var detail = $(obj).parent().parent().parent().parent().attr("detail");
	$("#rbd2").val(rbd);
	$("#update-detail").html(detail);

	layer.open({
		type:1,
			title: '修改磁盘描述',
			area: ['500px'],
			content: $("#changeDescribe"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				var detail =$("#update-detail").val();
				layer.close(index);

				$.ajax({
 					url:""+ctx+"/ceph/updaterbddetail?imgname="+rbd+"&detail="+detail,
 					type:"get",
 					success:function(data){
 						var data = eval("(" + data + ")");
 						if(data.status =='500'){
 							layer.msg(data.msg,{icon : 5});
 						}else{
 							layer.msg("修改描述成功！",{icon : 6});
 							location.reload();
 						}
 						return;
 					}
 				});

			}
	})

}

//创建快照
function createSnapshoot(obj){
	var rbdname = $(obj).parent().attr("rbd");
	var reg = /^[0-9a-zA-Z]+$/;//正则表达式，快照名称只能为数字和字母，或者两者的组合
	if(rbdname ==''){
		layer.msg("未获取到块设备名称！",{icon : 5});
		return;
	}
	$("#rbd-snap").val(rbdname);
	layer.open({
		type:1,
			title: '创建快照',
			area: ['500px'],
			content: $("#createSnapshoot"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				var snapname = $("#snapshootName").val();
				var detail = $("#snap-mark").val();
				if(snapname ==''||!reg.test(snapname)){
					layer.tips('快照名称不能为空,且只能为数字和字母或者两者的组合！', '#snapshootName', {
						tips : [ 1, '#0FA6D8' ]
					});
					$('#snapshootName').focus();
					return;
				}
				$.ajax({
 					url:""+ctx+"/ceph/checksnap?imgname="+rbdname+"&snapname="+snapname,
 					type:"get",
 					success:function(data){
 						layer.close(index);
 						var data = eval("(" + data + ")");
 						if(data.status =='500'){
 							layer.msg(data.msg,{icon : 5});
 						}else{
 							if(data.exist == '1'){
 								layer.msg("快照名称重复！",{icon : 5});
 							}else{
 								$.ajax({
 				 					url:""+ctx+"/ceph/createsnap?imgname="+rbdname+"&snapname="+snapname+"&snapdetail="+detail,
 				 					type:"get",
 				 					success:function(data){
 				 						var data = eval("(" + data + ")");
 				 						if(data.status =='500'){
 				 							layer.msg(data.msg,{icon : 5});
 				 						}else{
 				 							layer.msg("快照创建成功！",{icon : 6});
 				 						}
 				 						return;
 				 					}
 				 				});
 							}
 						}
 					}
 				});


				}
	})
}



//修改磁盘大小
function changeStorageSize(obj){
	var rbd = $(obj).parent().parent().parent().parent().attr("rbd");
    var size =$(obj).parent().parent().parent().parent().attr("size");
    $("#rbd3").val(rbd);
	$("#size3").val(size);

	layer.open({
		type:1,
			title: '磁盘扩容',
			area: ['500px'],
			content: $("#changeStorageSize"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				var size =  $("#extendsize").val();
				if(size==''){
					layer.tips('磁盘扩容不能为空！', '#extendsize', {
						tips : [ 1, '#0FA6D8' ]
					});
					$('#extendsize').focus();
					return;
				}

				layer.close(index);
				$.ajax({
 					url:""+ctx+"/ceph/updaterbdsize?imgname="+rbd+"&size="+size,
 					type:"get",
 					success:function(data){
 						var data = eval("(" + data + ")");
 						if(data.status =='500'){
 							layer.msg(data.msg,{icon : 5});
 						}else{
 							layer.msg("修改磁盘大小成功！",{icon : 6});
 							location.reload();
 						}
 						return;
 					}
 				});

				}
	})
}

//释放块设备1.确定是否有启动中的服务正在使用此块设备2.删除块设备
function releaseStorage(obj){
	var rbdId = $(obj).parent().parent().parent().parent().attr("rbdId");

	layer.open({
			title: '释放',
			content: "删除磁盘将不可恢复，是否确定删除该磁盘？",
			btn: ['确定', '取消'],
			yes: function(index, layero){
				   layer.close(index);
				   var loading = layer.load(0, {shade : [ 0.3, '#000' ]});

				   $.ajax({
	 					url:""+ctx+"/ceph/checkrbdrunning?imgId="+rbdId,
	 					type:"get",
	 					success:function(data){
	 						layer.closeAll("loading");
	 						var data = eval("(" + data + ")");
	 						if(data.status =='500'){
	 							layer.msg(data.msg,{icon : 5});
	 						}else{
	 							$.ajax({
	 			 					url:""+ctx+"/ceph/deleterbd?imgId="+rbdId,
	 			 					type:"get",
	 			 					success:function(data){
	 			 						var data = eval("(" + data + ")");
	 			 						if(data.status =='500'){
	 			 							layer.msg(data.msg,{icon : 5});
	 			 						}else{
	 			 							layer.msg("磁盘删除成功！",{icon : 6});
	 			 							location.reload();
	 			 						}
	 			 					}
	 			 				});
	 						}
	 					}
	 				});


			}
	})
}

function formatStrategy(obj){
	var rbdname = $(obj).parent().attr("rbd");

	layer.open({
		title: '格式化磁盘',
		content: "是否确定格式化该磁盘？",
		btn: ['确定', '取消'],
		yes: function(index, layero){
			/*$.ajax({
					url:""+ctx+"/ceph/clearcephrbd?imgName="+rbdname,
					type:"get",
					success:function(data){
						var data = eval("(" + data + ")");
						if(data.status =='500'){
							layer.msg(data.msg,{icon : 5});
						}else{
							layer.msg("格式化成功！",{icon : 6});
							location.reload();
						}
					}
				});*/
			}
})
}

function createStrategy(obj){
	var imgId = $(obj).parent().attr("rbdId");

	$.ajax({
			url:""+ctx+"/ceph/specifiedSnapStrategyInfo?imgId="+imgId,
			type:"get",
			success:function(data){
				var data = eval("(" + data + ")");
				if(data.status =='404'){
					layer.msg("该设备目前还没有快照策略！");
				}else{

					$("input:checkbox[name='time']:checked").each(function(i) {
						$(this).prop('checked', false);
					});

					$("input:checkbox[name='week']:checked").each(function(i) {
						$(this).prop('checked', false);
					});

					$('input:radio[name="retainTime"]:checked').prop('checked', false);

					$("#strategyname").val(data.snapStrategy.name);

					var times = data.snapStrategy.time.split(',');
					for (var i = 0; i < times.length; i++) {
						$("input:checkbox[name='time']").each(function(index) {
							if($(this).val()==times[i]){
								$(this).prop('checked', true);
							}
						});
					}

					var weeks = data.snapStrategy.week.split(',');
					for (var i = 0; i < weeks.length; i++) {
						$("input:checkbox[name='week']").each(function(index) {
							if($(this).val()==weeks[i]){
								$(this).prop('checked', true);
							}
						});
					}

					if (data.snapStrategy.keep == 100000) {
						$("input:radio[value='2']").prop('checked', true);
					} else {
						$("input:radio[value='1']").prop('checked', true);
						$('#keep').val(data.snapStrategy.keep);
					}

					$("#operationBtn").empty();
					var btn;
					if(data.excuting == true){
						btn="<button class=\"textBtn\" style=\"float:right\" onclick=\"cancel("+imgId+")\">取消执行</button>";
					}else{
						btn="<button class=\"textBtn\" style=\"float:right\" onclick=\"excute("+imgId+")\">执行策略</button>";
						btn=btn+"<button class=\"textBtn\" style=\"float:right\" onclick=\"unbind("+imgId+")\">解除绑定</button>";
					}
					$("#operationBtn").append(btn);

					layer.open({
						    type:1,
							title: '快照策略',
							area: ['500px'],
							content: $("#createStrategy")
					})
				}
			}
		});

}

function excute(imgId){
	$.ajax({
		url:""+ctx+"/ceph/excuteSnapStrategy?imgId="+imgId,
		type:"get",
		success:function(data){

			var data = eval("(" + data + ")");
			if(data.status =='500'){
				layer.msg(data.msg,{icon : 5});
			}else{
				layer.msg("执行成功！",{icon : 6});

				$("#operationBtn").empty();
				var btn;
				btn="<button class=\"textBtn\" style=\"float:right\" onclick=\"cancel("+imgId+")\">取消执行</button>";
				$("#operationBtn").append(btn);
			}
		}
	});
}


function cancel(imgId){
	$.ajax({
			url:""+ctx+"/ceph/cancelSnapStrategy?imgId="+imgId,
			type:"get",
			success:function(data){

				var data = eval("(" + data + ")");
				if(data.status =='500'){
					layer.msg(data.msg,{icon : 5});
				}else{
					layer.msg("取消执行成功！",{icon : 6});

					$("#operationBtn").empty();
					var btn;
					btn="<button class=\"textBtn\" style=\"float:right\" onclick=\"excute("+imgId+")\">执行策略</button>";
					btn=btn+"<button class=\"textBtn\" style=\"float:right\" onclick=\"unbind("+imgId+")\">解除绑定</button>";

					$("#operationBtn").append(btn);
				}
			}
		});
}

function unbind(imgId){
	$.ajax({
		url:""+ctx+"/ceph/unbindSnapStrategy?imgId="+imgId,
		type:"get",
		success:function(data){

			var data = eval("(" + data + ")");
			if(data.status =='500'){
				layer.msg(data.msg,{icon : 5});
			}else{
				layer.msg("解除绑定成功！",{icon : 6});
                 location.reload();
			}
		}
	});
}


