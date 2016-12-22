$(document).ready(function(){
//	 添加环境变量
	$("#crateEnvVariate").click(function(){
	var tr = '<tr>'+
				'<td class="keys"><input type="text" style="width: 98%" value=""></td>'+
				'<td class="vals"><input type="text" style="width: 98%" value=""></td>'+
				'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
				'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="">'+
				'</td>'+
			'</tr>'
	$("#env-oper1").append(tr);
	
	//调节界面高度
	var imagePage_height = $(".host_step2").height();
	$(".step-inner").height(imagePage_height+100);	
	});
	
	//导入模板文件选项对勾
	$(document).on("click","#Path-table tr", function () {
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");//取消原先选中行
		$(this).parent().find("tr.focus").toggleClass("focus");
		$(this).toggleClass("focus");//设定当前行为选中行
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
		selectedTemplateName = $(this).parent().find("tr.focus").find(".templateName").val();
		console.log(selectedTemplateName);
	});

	//导入模板
	$("#importBtn").click(function(){
		loadEnvironment();
	});
	
	//保存模板
	$("#exportBtn").click(function(){
		if (!saveEnvVariable()) {
			return;
		} else {
			layer.open({
			 	type:1,
		        title: '保存为模板',
		        content: $("#environment-template"),
		        btn: ['保存', '取消'],
		        scrollbar: false,
		        yes: function(index, layero){ 
		        	var templateName = $("#envTemplateName").val();
		        	if (templateName == null || templateName == "") {
		        		layer.tips('模板名称不能为空','#envTemplateName',{tips: [1, '#3595CC']});
						$('#envTemplateName').focus();
						return;
		        	}else if (templateName.length > 20) {
		        		layer.tips('模板名称字符长度不能超过20','#envTemplateName',{tips: [1, '#3595CC']});
						$('#envTemplateName').focus();
						return;
		        	}
		        	var envVariable = $('#envVariable').val();
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
													layer.msg("环境变量模板保存成功",{icon: 6});
													layer.close(index2);
													layer.close(index);
													setTimeout('window.location.href = ""+ctx+"/template/env"',1500);
												}
											}	
							        	});

									  }
									});
							}else if (data.status == "200") {
								layer.msg("环境变量模板保存成功",{icon: 6});
								layer.close(index);
								setTimeout('window.location.href = ""+ctx+"/template/env"',1500);
							}
						}	
		        	});
		        }
			})
		}
	});


});
var selectedTemplateName = null;

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
        if(envKey==""){
			layer.tips('环境变量Key不能为空',this.children[0].children[0],{tips: [1, '#3595CC']});
			this.children[0].children[0].focus();
			flag = 1;
			return false;
        }else if(envValue == ""){
			layer.tips('环境变量Value不能为空',this.children[1].children[0],{tips: [1, '#3595CC']});
			this.children[1].children[0].focus();
			flag = 1;
			return false;
        }else{
        	for (var i = 0; i<arrayKey.length;i++) {
        		if (envKey == arrayKey[i]) {
        			layer.tips('环境变量Key不能重复',this.children[0].children[0],{tips: [1, '#3595CC']});
        			this.children[0].children[0].focus();
        			flag = 1;
        			return false;
        		}
        	}
        }
        
		arrayKey.push(envKey);
        
		dataJson += envKey+","+envValue+";";
    });
    
    if (flag == 1) {
    	return false;
    }
    if (dataJson != "") {  
        dataJson = dataJson.substring(0,dataJson.length -1);
        $('#envVariable').val(dataJson);
        return true;
    } else {
		layer.tips('环境变量不能为空','#env-variate',{tips: [1, '#3595CC']});
		$('#env-variate').focus();
		return false;
    }
    

}
var count = 1;
//单击导入模板，加载模板数据
function loadEnvironment(){
	 $.ajax({
  		url : ctx + "/template/loadEnvTemplate.do",
  		success : function(data) {
  			data = eval("(" + data + ")");
  			var html = "";
	            if (data != null) {
	            	if (data['data'].length > 0){
	                	for (var i in data.data) {
	                		var templateName = data.data[i];
	                		html += '<tr>'+
	                				'<td class="vals vals-env">'+templateName+'<span class="vals-path hide"><i class="fa fa-check"></i></span>'+
	                				'<input type="hidden" class="templateName" value="'+templateName+'" /></td>'+
	                			'</tr>'
	                	}
	            	}else {
	            		html += '<tr><td>没有保存的模板</td></tr>'	
	            	}
	            } 
	            $("#Path-env").empty();
	            $("#Path-env").append(html);
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
	    	         		data:{"templateName":selectedTemplateName},
	    	         		success : function(data) {
	    	         			data = eval("(" + data + ")");
	    	         			var html = "";
	    	    	            if (data != null) {
	    	    	                if (data['data'].length > 0) {
	    	    	                	for (var i in data.data) {
	    	    	                		var envTemplate = data.data[i];
	    	    	                		html = '<tr>'+
			    		    	    	    			'<td class="keys"><input id = "key_'+count+'" type="text" style="width: 98%"></td>'+
			    		    	    	    			'<td class="vals"><input id = "value_'+count+'" type="text" style="width: 98%"></td>'+
			    		    	    	    			'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
			    		    	    	    				'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="'+envTemplate.envKey+'">'+
			    		    	    	    			'</td>'+
			    		    	    	    		'</tr>'
	    	    	                		$("#env-oper1").append(html);
	    	    	                		$("#key_"+count).val(envTemplate.envKey);
	    	    	                		$("#value_"+count).val(envTemplate.envValue);
	    	    	                		arrayKey.push(envTemplate.envKey+",");
	    	    	                		count++;
	    	    	                	}
	    	    	                }
	    	    	            }
	    	    	            $("#arrayKey").attr("value",arrayKey);
	    	         		}
	    	         	});
	    	        	layer.close(index);
	    	        }
	    		})
  			}
	 });
}
