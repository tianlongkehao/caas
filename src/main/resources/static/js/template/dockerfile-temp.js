$(document).ready(function(){
	
});

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
		    				layer.alert("删除成功");
		    				window.location.href = ""+ctx+"/template/dockerfile";
		    			} else if (data.status=="400") {
		    				layer.alert("删除失败");
		    			}
   		     		}
   		     	});
	        }
	 });
}


