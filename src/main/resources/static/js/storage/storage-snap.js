$(function(){

})

function storageRollBack(obj){
	var snap = $(obj).attr("snap");
    var rbd = $(obj).attr("rbd");

    var loading = layer.load(0, {shade : [ 0.3, '#000' ]});

    //判断是否可以回滚
    $.ajax({
			url:""+ctx+"/ceph/snapability?imgname="+rbd,
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

