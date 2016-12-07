$(function(){
	$("#createKeyBtn").click(function(){
		layer.open({
			type: 1,
	        title: '创建',
	        content: $("#createKeyCon"),
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	
	        }
		})
	})
	
	
    
    
});