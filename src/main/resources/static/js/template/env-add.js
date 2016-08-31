$(document).ready(function(){
	// 添加环境变量
	$("#crateEnvVariate").click(function(){
		var addName = $("#envName").val();
		var addValue = $("#envValue").val();
		//环境变量Key只能是字母数字下划线；
		if(addName.search(/^[a-z][a-z0-9_]*$/) === -1){
			layer.tips('环境变量key只能是字母数字下划线','#envName',{tips: [1, '#3595CC']});
			$('#envName').focus();
			return;
		}
		//判断value长度
		if(addValue.length >= 4096){
	    	layer.tips('value字符长度不能超过4096','#envValue',{tips: [1, '#3595CC']});
		      $('#Value').focus();
		      return;
	    }
		//判断key是否重复，
		var arrayKey = $("#arrayKey").val().split(",");
		for(var i = 0; i<arrayKey.length; i++){
			if(addName == arrayKey[i]){
				layer.tips('环境变量key不能重复','#envName',{tips: [1, '#3595CC']});
				$('#envName').focus();
				return;
			}
		}
		arrayKey.push(addName);
		$("#arrayKey").attr("value",arrayKey);
		
		if(addName != "" && addValue != ""){
			var tr = '<tr>'+
			'<td class="keys"><input type="text" style="width: 98%" value="'+addName+'"></td>'+
			'<td class="vals"><input type="text" style="width: 98%" value="'+addValue+'"></td>'+
			'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
			'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="'+addName+'">'+
			'</td>'+
		'</tr>'
		$("#env-oper1").append(tr);
		}
		
	});
	
	//导入模板文件选项对勾
	var templateName = null;
	$("#Path-table>tbody>tr").on("click", function () {
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
		$(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
		//$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
		$(this).toggleClass("focus");//设定当前行为选中行
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
		templateName = $(this).parent().find("tr.focus").find(".templateName").val();
	});
	
	//导入模板
	$("#importBtn").click(function(){
		layer.open({
		 	type:1,
	        title: '环境变量模板',
	        content: $("#environment-variable"),
	        btn: ['导入', '取消'],
	        scrollbar: false,
	        yes: function(index, layero){
	        	 var arrayKey = $("#arrayKey").val().split(",");
	        	 $.ajax({
	         		url : ctx + "/service/importEnvTemplate.do",
	         		type: "POST",
	         		data:{"templateName":templateName},
	         		success : function(data) {
	         			data = eval("(" + data + ")");
	         			var html = "";
	    	            if (data != null) {
	    	                if (data['data'].length > 0) {
	    	                	for (var i in data.data) {
	    	                		var envTemplate = data.data[i];
	    	                		html += '<tr>'+
		    	    	    			'<td class="keys"><input type="text" style="width: 98%" value="'+envTemplate.envKey+'"></td>'+
		    	    	    			'<td class="vals"><input type="text" style="width: 98%" value="'+envTemplate.envValue+'"></td>'+
		    	    	    			'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
		    	    	    			'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="'+envTemplate.envKey+'">'+
		    	    	    			'</td>'+
		    	    	    		'</tr>'
		    	    	    		arrayKey.push(envTemplate.envKey+",");
	    	                	}
	    	                }
	    	            }
	    	            $("#Path-oper1").append(html);
	    	            $("#arrayKey").attr("value",arrayKey);
	         		}
	         	});
	        	layer.close(index);
	        }
		})
	});
	
	
});

//删除环境变量
function deleteRow(obj){
	var envKey = $(obj).parent().find("input:first").val();
	var arrayKey = $("#arrayKey").val().split(",");
	for(var i = 0; i<arrayKey.length; i++){
		if(envKey ==arrayKey[i]){
			arrayKey.splice(i,1);
			break;
		}
	}
	$("#arrayKey").attr("value",arrayKey);
	$(obj).parent().parent().remove();
}



