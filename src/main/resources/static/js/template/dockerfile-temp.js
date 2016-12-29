$(document).ready(function(){
	
});
//单个删除
function deletedockerfile(obj){
	var dockerfileId = $(obj).attr("dockerfileId");
	layer.open({
	        title: '删除模板',
	        content: '确定删除模板？',
	        btn: ['确定', '取消'],
	        yes: function(index){ 
	        	layer.close(index);
	        	$.ajax({
	        		url:""+ctx+"/template/dockerfile/delete.do",
   		     		type:"post",
   		     		data:{"dockerfileId":dockerfileId},
   		     		success:function(data){
			     		data = eval("(" + data + ")");
		    			if (data.status=="200") {
		    				layer.msg("dockerfile模板删除成功",{icon: 6})
							setTimeout('window.location.href = ""+ctx+"/template/dockerfile"',1500);
		    			} else if (data.status=="400") {
		    				layer.alert("dockerfile模板删除失败，请检查服务器连接");
		    			}
   		     		}
   		     	});
	        }
	 });
}

//批量删除
function delDockerfiles(){
	var ids = [];
	$('input[name="chkItem"]:checked').each(function(){
		 var dockerFileId = $(this).val();
		 ids.push(dockerFileId);
		 layer.open({
	        title: '删除dockerfile模板',
	        content: '确定批量删除dockerfile模板？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	layer.close(index);
	        	$.ajax({
					url:""+ctx+"/template/dockerfiles/delete.do?dockerfileIds="+ids,
	        		success: function (data) {
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.msg("dockerfile模板删除成功",{icon: 6})
							setTimeout('window.location.reload()',1500);
						}else{
							layer.alert("dockerfile模板删除失败，请检查服务器连接");
						}
	        		}
	        	})  
	        }
		 })
	 })
	 
}
