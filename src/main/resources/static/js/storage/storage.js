$(document).ready(function () {
	$("#storageReloadBtn").click(function(){
		window.location.reload();
	});

});

$(document).ready(function () {

    $(".deleteStorage").click(function(){
   		var storageId = $(this).attr("storageId");
   		 layer.open({
   		        title: '删除备份',
   		        content: '确定删除备份？',
   		        btn: ['确定', '取消'],
   		        yes: function(index){ 
   		        	layer.close(index);
   		        	$.ajax({
		   		     		url:""+ctx+"/service/storage/delete",
		   		     		type:"post",
		   		     		data:{"storageId":storageId},
		   		     		success:function(data){
		   		     			if(data == "success"){
			   		     			layer.msg( "删除成功！", {
				   						icon: 1
				   					},function(){
				   						window.location.href = "/service/storage";
				   					});
		   		     			}
		   		     		},
		   		     		error:function(){
			   		     		layer.msg( "删除失败", {
			   						icon: 0.5
			   					});
		   		     		}
		   		     	});
   		        	refresh();
   		        }
   		 });
    });
    
});

$(document).ready(function () {
	
	$("#storageName").blur(function(){
		var storageName = $("#storageName").val(); 
		if (storageName === '') {
			 layer.tips('存储名称不能为空', $('#storageName'),{tips: [1, '#EF6578']});
             $('#storageName').focus();
             return;
          }
		var url = ""+ctx+"/service/storage/build/judge";
		$.post(url, {'storageName':storageName}, function(data){
			if(data == 'ok') {
				return;
			}else{
				 layer.tips('存储名称重复', $('#storageName'),{tips: [1, '#EF6578']});
	             $('#storageName').focus();
	             return;
			}
		});
	});	
	
	$("#storageSize").blur(function(){
		 var storageSize = $("#storageSize").val();
		 if(storageSize == ""){
			 layer.tips('存储大小不能为空', $('#storageSize'),{tips: [1, '#EF6578']});
             $('#storageSize').focus();
             return;
		 }
	});
	
	 $("#buildStorage").click(function(){
		 var storageName = $("#storageName").val();
		 var storageSize = $("#storageSize").val();
		 var options=$("#format option:selected"); 
		 var format = options.text();
		 
		 if (storageName === '') {
			 layer.tips('存储名称不能为空', $('#storageName'),{tips: [1, '#EF6578']});
             $('#storageName').focus();
             return;
          }
		 if(storageSize == ""){
			 layer.tips('存储大小不能为空', $('#storageSize'),{tips: [1, '#EF6578']});
             $('#storageSize').focus();
             return;
		 }
		 
		 var url = ""+ctx+"/service/storage/build";
			$.post(url, {'storageName':storageName,'storageSize':storageSize,'format':format}, function(data){
				if(data == 'success') {
					layer.msg( "创建成功！", {
   						icon: 1
   					},function(){
   						window.location.href = "/service/storage";
   					});
				}
				else if(data == 'error'){
					layer.msg( "保存失败。名称相同，请重命名", {
						icon: 0.5
					});
				}else{
					layer.msg( "保存失败。", {
						icon: 0.5
					});
				}
			}); 
	 });
});