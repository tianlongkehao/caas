$(function(){
   loadSnapList();
})

//加载快照列表
function loadSnapList(){
	$.ajax({
		url:""+ctx+"/storage/snapList",
		type:"get",
		success:function(data){
        var data = eval("("+data+")");
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.cephSnaps.length;
        	for(var i=0; i<len; i++){
        			var snap = data.cephSnaps[i];
        			itemsHtml += '<tr>'
        						+'<td style="width: 5%; text-indent:30px">'
        						+'<input class="chkItem" type="checkbox" id="'+snap.id+'">'
        						+'</td>'
        						+'<td>'+snap.name+'</td>'
        						+'<td>'+snap.imgname+'</td>'
        						+'<td>'+snap.createDate+'</td>'
        						+'<td>'+snap.snapdetail+'</td>'
        						+'<td style="text-indent: 5px;" class="item-operation">'
								+'<a onclick="storageRollBack(this)" imgId ='+snap.imgId+' rbd="'+snap.imgname+'" snap="'+snap.name+'" title="回滚磁盘">'
							    +	'<i class="fa fa-history fa-opr"></i>'
							    +'</a>'
							    +'<a onclick="deletesnap(this)" rbd="'+snap.imgname+'" snap="'+snap.name+'">'
							   	+	 '<i class="fa fa-trash fa-opr" title="删除快照"></i>'
							    +'</a>'
							    +'</td>'
        				        +'</tr>';
        	}
        	$("#snapList").empty().append(itemsHtml);
		}
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0,4,5 ]
			} ],
			"aaSorting": [[ 3, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		}
	});
}

function storageRollBack(obj){
	var snap = $(obj).attr("snap");
    var rbd = $(obj).attr("rbd");
    var imgId = $(obj).attr("imgId");

    var loading = layer.load(0, {shade : [ 0.3, '#000' ]});

    //判断是否可以回滚
    $.ajax({
			url:""+ctx+"/ceph/checkrbdrunning?imgId="+imgId,
			type:"get",
			success:function(data){
				var data = eval("(" + data + ")");
				if(data.status =='500'){
					layer.closeAll("loading");
					layer.msg(data.msg,{icon : 5});
					return;
				}
				if(data.status =='200'){

					$.ajax({
						url:""+ctx+"/ceph/rollback?imgname="+rbd+"&snapname="+snap,
						type:"get",
						success:function(data){
							layer.closeAll("loading");
							var data = eval("(" + data + ")");
							if(data.status =='500'){
								layer.msg(data.msg,{icon : 5});
							}
							if(data.status =='200'){
								layer.msg("回滚成功！",{icon : 6});
							}
							return;
						}
					});

				}
			}
		});
}

//删除快照
function deletesnap(obj){
	var rbd = $(obj).attr("rbd");
	var snap = $(obj).attr("snap");

	$.ajax({
		url:""+ctx+"/ceph/deletesnap?imgname="+rbd+"&snapname="+snap,
		type:"get",
		success:function(data){
			layer.closeAll("loading");
			var data = eval("(" + data + ")");
			if(data.status =='500'){
				layer.msg(data.msg,{icon : 5});
			}
			if(data.status =='200'){
				layer.msg("快照删除成功！",{icon : 6});
                                location.reload();
			}
			return;
		}
	});
}

