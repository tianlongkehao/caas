$(document).ready(function(){
	// 添加环境变量
	$("#crateEnvVariate").click(function(){
		var addName = $("#envName").val();
		var addValue = $("#envValue").val();
		//环境变量Key只能是字母数字下划线；
		if(addName.search(/^[0-9a-zA-Z_]+$/) === -1){
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
		//判断value长度
		if(addValue.length >= 4096){
	    	layer.tips('value字符长度不能超过4096','#Value',{tips: [1, '#3595CC']});
		      $('#Value').focus();
		      return;
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
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);	
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
		loadEnvironment();
		layer.open({
		 	type:1,
	        title: '环境变量模板',
	        content: $("#environment-variable"),
	        btn: ['导入', '取消'],
	        scrollbar: false,
	        yes: function(index, layero){
	        	 var arrayKey = $("#arrayKey").val().split(",");
	        	 $.ajax({
	         		url : ctx + "/template/importEnvTemplate.do",
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
	    	            $("#env-oper1").append(html);
	    	            $("#arrayKey").attr("value",arrayKey);
	         		}
	         	});
	        	layer.close(index);
	        }
		})
	});
	
	//保存模板
	$("#exportBtn").click(function(){
		if (!saveEnvVariable()) {
			return;
		} else {
			layer.open({
			 	type:1,
		        title: '保存为为模板',
		        content: $("#environment-template"),
		        btn: ['保存', '取消'],
		        scrollbar: false,
		        yes: function(index, layero){ 
		        	var templateName = $("#envTemplateName").val();
		        	if (templateName == null || templateName == "") {
		        		layer.tips('模板名称不能为空','#envTemplateName',{tips: [1, '#3595CC']});
						$('#envTemplateName').focus();
						return;
		        	}
		        	
		        	var envVariable = $("#envVariable").val();
		        	if (envVariable == null || envVariable == "") {
		        		layer.tips('环境变量不能为空','#env-variate',{tips: [1, '#3595CC']});
						$('#env-variate').focus();
						layer.close(index);
						return;
		        	}
		        	
		        	$.ajax({
						url:ctx+"/template/saveEnvTemplate.do",
						type: "POST",
		         		data:{"templateName":templateName,"envVariable":envVariable},
						success:function(data){
							data = eval("(" + data + ")");
							if(data.status=="400"){
								layer.msg("环境变量模板名称重复",{
									  time: 0 //不自动关闭
									  ,btn: ['覆盖', '重命名']
									  ,yes: function(index2){
							        	$.ajax({
											url:ctx+"/template/modifyEnvTemplate.do",
											type: "POST",
							         		data:{"templateName":templateName,"envVariable":envVariable},
											success:function(data){
												data = eval("(" + data + ")");
												if (data.status == "200") {
													layer.msg("环境变量模板导入成功",{icon: 6});
													layer.close(index2);
													layer.close(index);
													setTimeout('window.location.href = ""+ctx+"/template/env"',1500);
												}
											}	
							        	});

									  }
									});
							}else if (data.status == "200") {
								layer.msg("环境变量模板导入成功",{icon: 6});
								layer.close(index);
								setTimeout('window.location.href = ""+ctx+"/template/env"',1500);
							}
						}	
		        	});
		        }
			})
		}
	});

	//导入模板文件选项对勾
	var templateName = null;
	$(document).on("click","#Path-table tr", function () {
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");//取消原先选中行
		$(this).parent().find("tr.focus").toggleClass("focus");
		$(this).toggleClass("focus");//设定当前行为选中行
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
		templateName = $(this).parent().find("tr.focus").find(".templateName").val();
		console.log(templateName);
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


//保存环境变量到json中
function saveEnvVariable() {
	var dataJson="";  
	var arrayKey = new Array(1) ;
	var flag = 0;
    $("#env-oper1 tr").each(function (index, domEle){
        var envKey = "";  
        var envValue = "";  
        $(domEle).find("input").each(function(index,data){  
            if(index == 0){  
            	envKey = $(data).val();
            } else if (index == 1){  
            	envValue = $(data).val();
            }  
        });  
        
		for (var i = 0; i<arrayKey.length;i++) {
			if (envKey == arrayKey[i]) {
				layer.tips('环境变量Key不能重复','#env-variate',{tips: [1, '#3595CC']});
				$('#env-variate').focus();
				flag = 1;
				break;
			}
		}
		arrayKey.push(envKey);
        
//        dataJson += "{"+"\"envKey\":\""+envKey+"\","+"\"envValue\":\""+envValue+"\"},"; 
		 dataJson += envKey+","+envValue+";";
    });
    
    if (flag == 1) {
    	return false;
    }
    if (dataJson != "") {  
        dataJson = dataJson.substring(0,dataJson.length -1);  
//        dataJson ="[" +dataJson+ "]";  
    }
    $('#envVariable').val(dataJson);
    return true;
}

//单击导入模板，加载模板数据
function loadEnvironment(){
	 $.ajax({
  		url : ctx + "/template/loadEnvTemplate.do",
  		success : function(data) {
  			data = eval("(" + data + ")");
  			var html = "";
	            if (data != null) {
                	for (var i in data.data) {
                		var templateName = data.data[i];
                		html += '<tr>'+
                				'<td class="vals vals-env">'+templateName+'<span class="vals-path hide"><i class="fa fa-check"></i></span>'+
                				'<input type="hidden" class="templateName" value="'+templateName+'" /></td>'+
                			'</tr>'
                	}
	            } else {
	            	html += '<tr><td>没有保存的模板</td></tr>'	
	            }
	            $("#Path-env").empty();
	            $("#Path-env").append(html);
  			}
	 });
}
