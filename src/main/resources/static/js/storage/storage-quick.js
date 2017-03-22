$(function(){
	loadStorageList();
	$("#storageQuickAdd").click(function(){
		layer.open({
			type:1,
			title: '创建云盘',
			area: ['500px', '416px'],
			content: $("#storageQuickAddInfo"),
			btn: ['确定', '取消'],
			yes: function(index, layero){
				
				}
		})
		
	});
	//是否支持卸载 按钮  1支持2不支持
	$("#unloadBtn").click(function(){
		var $this = $(this);
		var unloadVal =  $this.attr("value");
		if(unloadVal == 1){
			$this.removeClass("fa-on").addClass("fa-off");
			$this.next().empty().html("不支持");
			$this.attr("value","2");
		}else{
			$this.removeClass("fa-off").addClass("fa-on");
			$this.next().empty().html("支持");
			$this.attr("value","1");
		}
	});
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
        	for(var i=0; i<len; i++){
        			var storage = data.storages[i];
        			var useType = storage.useType ==1 ? "未使用" : "使用" ;
        			
        			if(storage.mountPoint == null || storage.mountPoint == ""){
        				storage.mountPoint = "未挂载";
        			}
        			itemsHtml += ' <tr class="ci-listTr" style="cursor:auto">'+
        							'<td style="width: 5%; text-indent: 30px;"><input type="checkbox" class="chkItem" name="chkItem" value='+storage.id+' /></td>'+
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
        	//$('#storageList').html(itemsHtml);
        	
		}
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 7 ]
			} ],
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		}
	});
	
}