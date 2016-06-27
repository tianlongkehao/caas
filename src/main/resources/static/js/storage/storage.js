$(function(){
	loadStorageList();
}) 

function loadStorageList(){
	var url = ""+ctx+"/service/storageList";
	var json = {pageable:"pageable"};
	
	jqList.query(url,json, function(data){
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.storages.length;
        	if(len == 0){
        		if(searchFlag) {
        			itemsHtml = '<tr><td colspan="4">未找到匹配的数据...</td></tr>';
        		}else {
        			itemsHtml = '<tr><td colspan="4">无数据...</td></tr>';
        		}
        	}
        	for(var i=0; i<len; i++){
        		(function(){
        			var storage = data.storages[i];
        			var useType = storage.useType ==1 ? "未使用" : "使用" ;
        			itemsHtml += ' <tr class="ci-listTr" style="cursor:auto">'+
        							' <td style="width: 15%; text-indent:22px;" id = "storageName">'+
        							'<a href="'+ctx+'/service/storage/detail/'+storage.id+'" title="查看详细信息">'+storage.storageName +'</a>'+
        							'</td>'+
        							' <td style="width: 10%;" class="cStatusColumn">' +
        								useType +
        							' </td>'+
        							' <td style="width: 10%;">' + storage.format + '</td>'+
        							' <td style="width: 10%;">' + storage.mountPoint + '</td>'+
        							' <td style="width: 12%;">' + storage.storageSize + ' M</td>'+
        							' <td style="width: 15%;">' + storage.createDate + '</td>'+
        							' <td style="width: 20%;">' + 
        								' <span class="btn btn-primary format formatStorage"> 格式化 </span>'+
        								' <span class="btn btn-primary dilation dilatationStorage" storageId="'+storage.id +'" storageSize="'+ storage.storageSize +'" storageName="' + storage.storageName +'">扩容</span>'+
        								' <span class="btn btn-primary delete deleteStorage" storageId="'+storage.id +'"> 删除 </span>'+
        							'</td>'+	
        						+'</tr>'
        		})(i);
        	}
        	$('tbody #storageList').html(itemsHtml);
		}
	});
}
$(document).ready(function () {
	$("#storageReloadBtn").click(function(){
		window.location.reload();
	});

});

$(function(){
	dilatationStorage();
	
	delStorage();
});

function delStorage() {

	$(document).on("click",".deleteStorage",function(){
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
		   		     		data = eval("(" + data + ")");
							if(data.status=="200"){
			   		     			layer.msg( "删除成功！", {
				   						icon: 1
				   					},function(){
				   						window.location.href = ""+ctx+"/service/storage";
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
    
};


function dilatationStorage(){
//	$(".dilatationStorage").on("click",function(){
	$(document).on("click",".dilatationStorage",function(){
		var storageId = $(this).attr("storageId");
		var storageSize = $(this).attr("storageSize");
		var storageName = $(this).attr("storageName");

		$("#upgradeStorageName").val(storageName);
		//$("#storageSizeUpdateSlider_input").val(storageSize);
		//var storageSizeUpdateSlider = sliderFn('storageSizeUpdateSlider', 1024, 0, parseInt(storageSize));
		layer.open({
			type:1,
			title: '扩容',
			area: ['500px', '300px'],
			content: $("#storageUpdate"),
			btn: ['确定', '取消'],
			yes: function(index, layero){ //或者使用btn1
				//按钮【按钮一】的回调
				layer.close(index);
				//var storageUpdateSize = $("#storageSizeUpdateSlider_input").val();
				$("#updatedefVolNum")[0].value = $("#updatedefVol").val()*1024;
				var storageUpdateSize = $(".updateStorageSize:checked").val();
				$.ajax({
					url:""+ctx+"/service/storage/dilatation",
					type:"post",
					data:{"storageId":storageId,"storageUpdateSize":storageUpdateSize},
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.msg( "修改成功！", {
		   						icon: 1
		   					},function(){
		   						window.location.reload();
		   					});
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