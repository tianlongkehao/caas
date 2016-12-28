var count = 1;
$(function(){
	//创建shera
	$("#createSheraBtn").click(function(){
		delData();
		layer.open({
			type: 1,
	        title: '创建',
	        content: $("#createSheraCon"),
	        area:['800px','500px'],
	        scrollbar:false,
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	if (!checkShera()){
	        		return;
	        	}
	        	var sheraUrl = $("#sheraIp").val();
	        	var userName = $("#shreaName").val();
	        	var password= $("#shreaPassword").val();
	        	var remark = $("#sheraRemark").val();
	        	var jdkJson = $("#arrayJdk").val();
	        	$.ajax({
	        		url :""+ ctx + "/user/shera/creatShera.do",
	        		type : "POST",
	        		data:{"sheraUrl":sheraUrl,"userName":userName,"password":password,"remark":remark,"jdkJson":jdkJson},
	        		success : function(data){
	        			data = eval("(" + data + ")");
	        			if (data.status=="200") {
	        				window.location.reload();
	        			} else if (data.status=="400") {
	        				layer.alert("创建失败，请您查看Ip地址是否有误");
	        			}
	        		}
	        	});
	        }
		})
	});
	
	$("#createjdk").click(function(){
		var addjdkHtml = '<tr class="plus-row">'+
							'<td><input class="jdkName" type="text" value=""></td>'+
							'<td><input class="jdkPath" type="text" value=""></td>'+
							'<td><a onclick="deleteRow(this)" class="gray">'+
									'<i class="fa fa-trash-o fa-lg"></i>'+
								'</a>'+
							'</td>'+
						'</tr>';
		$(".jdktbody").append(addjdkHtml);
	})
	
});

//detail&修改
function sheraDetail(id){
	delData();
	findSheraData(id);
	layer.open({
		type: 1,
        title: '修改',
        content: $("#createSheraCon"),
        area:['800px','500px'],
        scrollbar:false,
        btn: ['修改', '取消'],
        yes: function(index, layero){ 
        	if (!checkShera()){
        		return;
        	}
        	var sheraUrl = $("#sheraIp").val();
        	var userName = $("#shreaName").val();
        	var password= $("#shreaPassword").val();
        	var remark = $("#sheraRemark").val();
        	var jdkJson = $("#arrayJdk").val();
        	$.ajax({
        		url :""+ ctx + "/user/shera/updateShera.do",
        		type : "POST",
        		data:{"id":id,"sheraUrl":sheraUrl,"userName":userName,"password":password,"remark":remark,"jdkJson":jdkJson},
        		success : function(data){
        			data = eval("(" + data + ")");
        			if (data.status=="200") {
        				window.location.reload();
        			} else if (data.status=="400") {
        				layer.alert("修改失败，请您查看Ip地址是否有误");
        			}
        		}
        	});
        }
	})
}

//查询当前的shera的详细信息
function findSheraData(id){
	$.ajax({
		url : ""+ctx+"/user/shera/detail.do?sheraId="+id,
		async:false,
		success : function (data) {
			data = eval("("+data+")");
			var shera = data.shera;
			var jdkList = data.jdkList;
			$("#sheraIp").val(shera.sheraUrl);
			$("#shreaName").val(shera.userName);
			$("#shreaPassword").val(shera.password);
			$("#sheraRemark").val(shera.remark);
			if (jdkList != null && jdkList.length > 0) {
				for (var i = 0 ; i < jdkList.length; i++) {
					var jdk = jdkList[i];
					var addjdkHtml = '<tr class="plus-row">'+
										'<td><input class="jdkName" id = "jdkName-'+count+'" type="text"></td>'+
										'<td><input class="jdkPath" id = "jdkPath-'+count+'" type="text"></td>'+
										'<td><a onclick="deleteRow(this)" class="gray">'+
											'<i class="fa fa-trash-o fa-lg"></i>'+
											'</a>'+
										'</td>'+
									'</tr>';
					$(".jdktbody").append(addjdkHtml);
					$("#jdkName-"+count).val(jdk.version);
					$("#jdkPath-"+count).val(jdk.path);
					count++;
				}
			}
		}
	});
	
}

//弹窗信息的清理
function delData(){
	$("#sheraIp").val("");
	$("#shreaName").val("");
	$("#shreaPassword").val("");
	$("#sheraRemark").val("");
	$("#arrayJdk").val("");
	$(".jdktbody").html("");
}

//删除jdk
function deleteRow(obj){
	$(obj).parent().parent().remove();
}

//批量删除sheras
function delSheras(){
	obj = document.getElementsByName("ids");
	var sheraIds = [];
    for (k in obj) {
        if (obj[k].checked) {
        	sheraIds.push(obj[k].value);
        }
    }
    if (sheraIds.length <=0) {
    	layer.msg( "请选择需要删除的镜像", {icon: 2 });
    	return;
    }
    layer.open({
        title: '删除shera',
        content: '确定删除shera？',
        btn: ['确定', '取消'],
        yes: function(index, layero){ 
        	layer.close(index);
        	$.ajax({
        		url :""+ ctx + "/user/shera/delsheras.do?sheraIds="+sheraIds,
        		success : function(data){
        			data = eval("("+data+")");
        			if (data.status == "200") {
        				window.location.reload(true);
        			}
        			else {
        				layer.alert("删除失败,请检出服务器连接");
        			}
        		}
        	});
        }
    })
}

//删除一个shera
function delOneTenement(id){
	layer.open({
        title: '删除shera',
        content: '确定删除shera？',
        btn: ['确定', '取消'],
        yes: function(index, layero){ 
        	layer.close(index);
        	$.ajax({
        		url :""+ ctx + "/user/shera/deleteShera.do?sheraId="+id,
        		success : function(data){
        			data = eval("("+data+")");
        			if (data.status == "200") {
        				window.location.reload(true);
        			}
        			else {
        				layer.alert("删除失败,请检出服务器连接");
        			}
        		}
        	});
        }
    })
}

//验证shera信息
function checkShera(){
	var sheraUrl = $("#sheraIp").val();
	var userName = $("#shreaName").val();
	var password= $("#shreaPassword").val();
	if (!sheraUrl || sheraUrl.length < 1){
	      layer.tips('sheraIp地址不能为空','#sheraIp',{tips: [1, '#3595CC']});
	      $('#sheraIp').focus();
	      return;
	}
	if (!userName || userName.length < 1){
	      layer.tips('账号名称不能为空','#shreaName',{tips: [1, '#3595CC']});
	      $('#shreaName').focus();
	      return;
	}
	if (!password || password.length < 1){
	      layer.tips('账号密码不能为空','#shreaPassword',{tips: [1, '#3595CC']});
	      $('#shreaPassword').focus();
	      return;
	}
	if (!addJdk()) {
		return;
	}
	return true;
}

//添加jdk信息
function addJdk(){
		var dataJson="";  
		var arrayKey = new Array(1) ;
		var flag = 0;
	    $(".jdktbody tr").each(function (index, domEle){
	        var envKey = "";  
	        var envValue = "";  
	        $(domEle).find("input").each(function(index,data){  
	        	if (index == 1){  
	            	envValue = $(data).val();
	            	if (!envValue || envValue.length < 1){
		          	      layer.tips('jdk路径不能为空',data,{tips: [1, '#3595CC']});
		          	      $(data).focus();
		          	      return ;
	            	}
	            } 
	            if(index == 0){  
	            	envKey = $(data).val();
	            	if (!envKey || envKey.length < 1){
		          	      layer.tips('jdk名称不能为空',data,{tips: [1, '#3595CC']});
		          	      $(data).focus();
		          	      return ;
	            	}
	            }
	        });  
	        
			for (var i = 0; i<arrayKey.length;i++) {
				if (envKey == arrayKey[i]) {
					layer.tips('jdk名称有误，请您重新填写','.jdktbody',{tips: [1, '#3595CC']});
					$('.jdktbody').focus();
					flag = 1;
					break;
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
	    }
	    $('#arrayJdk').val(dataJson);
	    return true;
}






