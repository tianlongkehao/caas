$(function(){
	loadStorageList();
	
	
}) 

function loadStorageList(){
	$.ajax({
		url:""+ctx+"/service/storageList",
		type:"post",
		data:{pageable:"pageable"},
		success:function(data){
        var data = eval("("+data+")");
		if(data.status == 200) {
        	var itemsHtml = '';
        	var len = data.storages.length;
/*        	if(len == 0){
        		if(searchFlag) {
        			itemsHtml = '<tr><td colspan="4">未找到匹配的数据...</td></tr>';
        		}else {
        			itemsHtml = '<tr><td colspan="4">无数据...</td></tr>';
        		}
        	}*/
        	for(var i=0; i<len; i++){
        			var storage = data.storages[i];
        			var useType = storage.useType ==1 ? "未使用" : "使用" ;
        			
        			if(storage.mountPoint == null || storage.mountPoint == ""){
        				storage.mountPoint = "未挂载";
        			}
        			itemsHtml += ' <tr class="ci-listTr" style="cursor:auto">'+
        							'<td style="width: 5%; text-indent: 30px;"><input type="checkbox" class="chkItem" name="chkItem" /></td>'+
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
        	$('#storageList').html(itemsHtml);
        	mountLocalCeph();
		}
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 6 ]
			} ],
			"aaSorting": [[ 5, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		}
	});
	
}
$(document).ready(function () {
	$("#storageReloadBtn").click(function(){
		window.location.reload();
	});

	$("#updatedefVol").click(function(){
		$("#updatedefVolNum").attr("checked","checked");
	 });
});

$(function(){
	dilatationStorage();
	
	delStorage();
	
	formatStorage();
});

function mountLocalCeph(){
	$.ajax({
		url:""+ctx+"/storage/mount.do",
		success:function(data){
			data = eval("(" + data + ")");
			if(data.status=="200"){
//				layer.msg( "修改成功！", {
//						icon: 1
//					},function(){
//						window.location.reload();
//					});
			} else if(data.status=="500"){
				layer.alert("挂载有点异常啊哈");
			}

		}
	})
}

function formatStorage(){
	$(document).on("click",".formatStorage",function(){
		var storageName = $(this).attr("storageName");
		var isVolReadOnly = $(this).attr("isVolReadOnly");
  		 layer.open({
		        title: '格式化卷组',
		        content: '确定格式化卷组？',
		        btn: ['确定', '取消'],
		        yes: function(index){ 
		        	layer.close(index);
		        	$.ajax({
		   		     		url:""+ctx+"/storage/format",
		   		     		type:"post",
		   		     		data:{"storageName":storageName,"isVolReadOnly":isVolReadOnly},
		   		     		success:function(data){
		   		     		data = eval("(" + data + ")");
							if(data.status=="200"){
			   		     			layer.msg( "格式化成功！", {
				   						icon: 1
				   					},function(){
				   						window.location.href = ""+ctx+"/service/storage";
				   					});
		   		     			}
		   		     		},
		   		     		error:function(){
			   		     		layer.msg( "格式化失败", {
			   						icon: 0.5
			   					});
		   		     		}
		   		     	});
		        	refresh();
		        }
		 });
	});
}

function delStorage() {

	$(document).on("click",".deleteStorage",function(){
		var storageId = $(this).attr("storageId");
   		 layer.open({
   		        title: '删除卷组',
   		        content: '确定删除卷组？',
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
						}else if(data.status=="500"){
							layer.alert("更改失败，可用大小不足");
						}
						else{
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