$(function(){

})

function storageRollBack(obj){
	var snap = $(obj).attr("snap");
      var rbd = $(obj).attr("rbd");

    //判断是否可以回滚
    $.ajax({
			url:""+ctx+"/ceph/snapability?imgname="+rbd,
			type:"get",
			success:function(data){
				var data = eval("(" + data + ")");
				if(data.status =='500'){
					layer.msg(data.msg,{icon : 5});
					return;
				}
				if(data.status =='200'){

					$.ajax({
						url:""+ctx+"/ceph/rollback?imgname="+rbd+"&snapname="+snap,
						type:"get",
						success:function(data){
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
