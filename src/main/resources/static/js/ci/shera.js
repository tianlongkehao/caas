$(function(){
	//创建shera
	$("#createSheraBtn").click(function(){
		
		layer.open({
			type: 1,
	        title: '创建',
	        content: $("#createSheraCon"),
	        area:['800px','500px'],
	        scrollbar:false,
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	
	        }
		})
	});
	
	$("#createjdk").click(function(){
		var addjdkHtml = '<tr class="plus-row">'+
		'<td><input class="jdkName" type="text" value=""></td>'+
		'<td><input class="jdkPath" type="text" value=""></td>'+
		'<td><a onclick="deleteRow(this)" class="gray">'+
			'<i class="fa fa-trash-o fa-lg"></i>'+
		'</a></td>'+
		'</tr>';
		$(".jdktbody").append(addjdkHtml);
	})
	
});

//detail&修改
function sheraDetail(obj){
	layer.open({
		type: 1,
        title: '创建',
        content: $("#createSheraCon"),
        area:['800px','500px'],
        scrollbar:false,
        btn: ['修改', '取消'],
        yes: function(index, layero){ 
        	
        }
	})
}

function deleteRow(obj){
	$(obj).parent().parent().remove();
}






