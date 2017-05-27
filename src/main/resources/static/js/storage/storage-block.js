$(function(){
	//loadStorageList();
	$("#storageQuickAdd").click(function(){
		layer.open({
			type:1,
			title: '创建云盘',
			area: ['500px', '416px'],
			content: $("#storageQuickAddInfo"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				var blockname = $("#q-storageName").val();
				if(blockname==""){
					layer.tips('磁盘名称不能为空！', '#q-storageName', {
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
				}
				disksize = $("#updatedefVol").val();

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
	if(rbdname ==''){
		layer.msg("未获取到块设备名称！",{icon : 5});
		return;
	}
	layer.open({
		type:1,
			title: '创建快照',
			area: ['500px'],
			content: $("#createSnapshoot"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				var snapname = $("#snapshootName").val();
				var detail = $("#snap-mark").val();
				if(snapname ==''){
					layer.tips('快照名称不能为空！', '#snapshootName', {
						tips : [ 1, '#0FA6D8' ]
					});
					$('#snapshootName').focus();
					return;
				}
				layer.close(index);
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

//释放块设备
function releaseStorage(obj){
	var rbd = $(obj).parent().parent().parent().parent().attr("rbd");

	layer.open({
			title: '释放',
			content: "删除磁盘将不可恢复，是否确定删除该磁盘？",
			btn: ['确定', '取消'],
			yes: function(index, layero){
				   layer.close(index);
				   var loading = layer.load(0, {shade : [ 0.3, '#000' ]});
					$.ajax({
	 					url:""+ctx+"/ceph/deleterbd?imgname="+rbd,
	 					type:"get",
	 					success:function(data){

	 						layer.closeAll("loading");

	 						var data = eval("(" + data + ")");
	 						if(data.status =='500'){
	 							layer.msg(data.msg,{icon : 5});
	 						}else{
	 							layer.msg("磁盘删除成功！",{icon : 6});
	 							location.reload();
	 						}
	 						return;
	 					}
	 				});
			}
	})
}

function formatStrategy(){
	layer.open({
		title: '格式化磁盘',
		content: "是否确定格式化该磁盘？",
		btn: ['确定', '取消'],
		yes: function(index, layero){

			}
})
}

function createStrategy(){
	layer.open({
		type:1,
			title: '创建策略',
			area: ['500px'],
			content: $("#createStrategy"),
			btn: ['确定', '取消'],
			yes: function(index, layero){

				}
	})

}





