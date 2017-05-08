$(document).ready(function(){
//	 添加配置文件
	$("#crateEnvVariate").click(function(){
	var tr = '<tr>'+
				'<td class="keys"><input  type="text" style="width: 98%" ></td>'+
				'<td class="vals"><textarea  style="width: 98%;height:200px"></textarea></td>'+
				'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
				'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="">'+
				'</td>'+
			'</tr>'
	$("#env-oper1").append(tr);

	//调节界面高度
	var imagePage_height = $(".host_step2").height();
	$(".step-inner").height(imagePage_height+100);
	});

	//保存模板
	$("#exportBtn").click(function(){
		var templateName = $("#configMapName").val();
		if (templateName == null || templateName == "") {
			layer.tips('模板名称不能为空','#configMapName',{tips: [1, '#3595CC']});
			$('#configMapName').focus();
			return;
		}else if (templateName.length > 20) {
			layer.tips('模板名称字符长度不能超过20','#configMapName',{tips: [1, '#3595CC']});
			$('#configMapName').focus();
			return;
		}
		if (!saveEnvVariable()) {
			return;
		} else {
								$.ajax({
									url:ctx+"/configmap/createOrUpdateConfigMap",
									type: "POST",
					         		data:$("#buildForm").serialize(),
									success:function(data){
										data = eval("(" + data + ")");
										if (data.status == "200") {
											layer.msg("配置文件模板保存成功",{icon: 6});
											setTimeout('window.location.href = ""+ctx+"/configmap/list"',1500);
										}
									}
					        	});

		}
	});


});

//删除
function deleteRow(obj){
	$(obj).parent().parent().remove();
}


//校验并为input、textarea的name赋值
function saveEnvVariable() {
	var arrayKey = new Array(1) ;
	var flag = 0;
	var count= 0;
	$("#env-oper1 tr").each(function (idx, domEle){
    	var envKey = "";
        var envValue = "";
        $(domEle).find("input").each(function(index,data){
        	if(index == 0){
        		$(data).attr('name','keyValues['+idx+'].key');
            	envKey = $(data).val();
        	}
        	count++;
        });
        $(domEle).find("textarea").each(function(index,data){
        	    $(data).attr('name','keyValues['+idx+'].value');
            	envValue = $(data).val();
            	count++;
        });

        if(envKey==""){
			layer.tips('文件名不能为空',this.children[0].children[0],{tips: [1, '#3595CC']});
			this.children[0].children[0].focus();
			flag = 1;
			return false;
        }else if(envValue == ""){
			layer.tips('文件内容不能为空',this.children[1].children[0],{tips: [1, '#3595CC']});
			this.children[1].children[0].focus();
			flag = 1;
			return false;
        }else{
        	for (var i = 0; i<arrayKey.length;i++) {
        		if (envKey == arrayKey[i]) {
        			layer.tips('文件名不能重复',this.children[0].children[0],{tips: [1, '#3595CC']});
        			this.children[0].children[0].focus();
        			flag = 1;
        			return false;
        		}
        	}
        }

		arrayKey.push(envKey);
    });

    if (flag == 1) {
    	return false;
    }
  if (count == 0) {
		layer.tips('配置文件不能为空','#env-variate',{tips: [1, '#3595CC']});
		$('#env-variate').focus();
		return false;
    }

    return true;
}
