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

$(function(){
	dilatationStorage();
});

function dilatationStorage(){
	$(".dilatationStorage").on("click",function(){

		var storageId = $(this).attr("storageId");
		var storageSize = $(this).attr("storageSize");
		var storageName = $(this).attr("storageName");

		$("#upgradeStorageName").val(storageName);
		var storageSizeUpdateSlider = sliderFn('storageSizeUpdateSlider', 1024, 0, 250);

		layer.open({
			type:1,
			title: '扩容',
			area: ['500px', '300px'],
			content: $("#storageUpdate"),
			btn: ['确定', '取消'],
			yes: function(index, layero){ //或者使用btn1
				//按钮【按钮一】的回调
				layer.close(index);
				$.ajax({
					url:"",
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("更改成功");
							window.location.reload();
						}else{
							layer.alert("更改失败，请检查服务器连接");
						}

					}
				})
			},
			cancel: function(index){ //或者使用btn2
				//按钮【按钮二】的回调
			}
		});

	});
}