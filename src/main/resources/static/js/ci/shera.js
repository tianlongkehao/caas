var count = 1;
$(function(){
	//创建shera
	$("#saveSheraBtn").click(function(){
//		delData();
//		if (!checkShera()){
//    		return;
//    	}
		
    	var sheraUrl = $("#sheraIp").val();
    	var port = $("#port").val();
    	var userName = $("#shreaName").val();
    	var password= $("#shreaPassword").val();
    	var sonarInfoList = $("#sonarInfoList").val();
    	var remark = $("#sheraRemark").val();
    	var jdkJson = new Array();
    	var jdkTr = $(".jdktbody").find("tr");
    	for (var jdkNum=0; jdkNum<jdkTr.length; jdkNum++){
    		var jdkKey = $(jdkTr[jdkNum]).find(".jdkKey").val();
    		var jdkVal = $(jdkTr[jdkNum]).find(".jdkVal").val();
    		var jdkItem = {"key":jdkKey,"val":jdkVal};
    		jdkJson.push(jdkItem);
    	}
    	//maven
    	var mavenJson = new Array();
    	var mavensNum = $(".mavens").find(".mavenIbox");
    	for(var i=0; i<mavensNum.length; i++){
    		var mavenVersion = $(mavensNum[i]).find(".mavenVersion").val();
    		
    		var mavenEnv = new Array();
        	var mavenTr = $(mavensNum[i]).find(".maventbody").find("tr");
        	for(var mavNum=0; mavNum<mavenTr.length; mavNum++){
        		var mavenKey = $(mavenTr[mavNum]).find(".mavenKey").val();
        		var mavenVal = $(mavenTr[mavNum]).find(".mavenVal").val();
        		var mavenItem = {"key":mavenKey,"val":mavenVal};
        		mavenEnv.push(mavenItem);
        	}
    		
    		var mavenItems = {
        			"proid": 1, 
            	    "version": mavenVersion,
            	    "env":mavenEnv
        	}; 
    		mavenJson.push(mavenItems);
    	}
    	//ant
    	var antJson = new Array();
    	var antsNum = $(".ants").find(".antIbox");
    	for(var i=0; i<antsNum.length; i++){
    		var antVersion = $(antsNum[i]).find(".antVersion").val();
    		
    		var antEnv = new Array();
        	var antTr = $(antsNum[i]).find(".anttbody").find("tr");
        	for(var antNum=0; antNum<antTr.length; antNum++){
        		var antKey = $(antTr[antNum]).find(".antKey").val();
        		var antVal = $(antTr[antNum]).find(".antVal").val();
        		var antItem = {"key":antKey,"val":antVal};
        		antEnv.push(antItem);
        	}
    		
    		var antItems = {
        			"proid": 2, 
            	    "version": antVersion,
            	    "env":antEnv
        	}; 
    		antJson.push(antItems);
    	}
    	//sonar
    	var sonarJson = new Array();
    	var sonarVersion = $(".sonarIbox").find(".sonarVersion").val();
    	var sonarTr = $(".sonartbody").find("tr");
    	var sonarEnv = new Array();
    	for(var sonNum = 0; sonNum < sonarTr.length; sonNum++){
    		var sonarKey = $(sonarTr[sonNum]).find(".sonarKey").val();
    		var sonarVal = $(sonarTr[sonNum]).find(".sonarVal").val();
    		var sonarItem = {"key":sonarKey,"val":sonarVal};
    		sonarEnv.push(sonarItem);
    	}
    	var sonarItems ={
    			"proid": 3, 
        	    "version": sonarVersion,
        	    "env":antEnv
    	}
    	sonarJson.push(sonarItems);
    	//shera
    	var sheraData = {
    			"sheraUrl":sheraUrl,
    			"port":port,
    			"userName":userName,
    			"password":password,
    			"remark":remark,
    			"jdkJson":jdkJson,
    			"sonarJson":sonarJson,
    			"mavenJson":mavenJson,
    			"antJson":antJson,
    		};
//    	$.ajax({
//    		url :""+ ctx + "/user/shera/creatShera.do",
//    		type : "POST",
//    		data:sheraData,
//    		success : function(data){
//    			data = eval("(" + data + ")");
//    			if (data.status=="200") {
//    				window.location.reload();
//    			} else if (data.status=="400") {
//    				layer.alert("创建失败，请您查看Ip地址是否有误");
//    			}
//    		}
//    	});
	});
	
	
	//$(".create").click(function(){
	$(document).on("click",".create",function(){
		var createId = this.getAttribute("id");
		var addHtml = '<tr class="plus-row">'
						+'<td><input class="'+createId+'Key addKey" type="text" value=""></td>'
						+'<td><input class="'+createId+'Val addVal" type="text" value=""></td>'
						+'<td><a onclick="deleteRow(this)" class="gray">'
						+'<i class="fa fa-trash-o fa-lg"></i>'
						+'</a>'
						+'</td>'
						+'</tr>';
		//var bb = '.'+createId+'tbody';
		$(this).parent().parent().prev("tbody").append(addHtml);
	});
	
	$(document).on("click",".fa-delete",function(){
		$(this).parents(".conCredIbox").remove();
	});
	
	//折叠ibox
    $(document).on('click','.collapse-link',function(){
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
        ibox.css("border-bottom","1px solid #dadada");
    });
	
	
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
        	var port = $("#port").val();
        	var userName = $("#shreaName").val();
        	var password= $("#shreaPassword").val();
        	var remark = $("#sheraRemark").val();
        	var jdkJson = $("#arrayJdk").val();
        	$.ajax({
        		url :""+ ctx + "/user/shera/updateShera.do",
        		type : "POST",
        		data:{"id":id,"sheraUrl":sheraUrl,"port":port,"userName":userName,"password":password,"remark":remark,"jdkJson":jdkJson},
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
			$("#port").val(shera.port);
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
	$("#port").val("");
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
    var sheraIds = [];
    $('input[name="ids"]:checked').each(function(){
		 var id = $(this).val();
		 sheraIds.push(id);
		 
	 })
	 if ("" == sheraIds) {
		layer.alert("请选择至少一个Shera", {icon:0});
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
	var port = $("#port").val();
	var userName = $("#shreaName").val();
	var password= $("#shreaPassword").val();
	if (!sheraUrl || sheraUrl.length < 1){
	      layer.tips('sheraIp地址不能为空','#sheraIp',{tips: [1, '#3595CC']});
	      $('#sheraIp').focus();
	      return;
	}
	if (!port || port.length < 1){
	      layer.tips('端口不能为空','#port',{tips: [1, '#3595CC']});
	      $('#port').focus();
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
function addJdk() {
	var dataJson = "";
	var arrayKey = new Array(1);
	var flag = 0;
	$(".jdktbody tr").each(function(index, domEle) {
		var envKey = "";
		var envValue = "";
		$(domEle).find("input").each(function(index, data) {
			if (index == 1) {
				envValue = $(data).val();
				if (!envValue || envValue.length < 1) {
					layer.tips('jdk路径不能为空', data, {
						tips : [ 1, '#3595CC' ]
					});
					$(data).focus();
					return;
				}
			}
			if (index == 0) {
				envKey = $(data).val();
				if (!envKey || envKey.length < 1) {
					layer.tips('jdk名称不能为空', data, {
						tips : [ 1, '#3595CC' ]
					});
					$(data).focus();
					return;
				}
			}
		});

		for (var i = 0; i < arrayKey.length; i++) {
			if (envKey == arrayKey[i]) {
				layer.tips('jdk名称有误，请您重新填写', '.jdktbody', {
					tips : [ 1, '#3595CC' ]
				});
				$('.jdktbody').focus();
				flag = 1;
				break;
			}
		}
		arrayKey.push(envKey);
		dataJson += envKey + "," + envValue + ";";
	});

	if (flag == 1) {
		return false;
	}
	if (dataJson != "") {
		dataJson = dataJson.substring(0, dataJson.length - 1);
	}
	$('#arrayJdk').val(dataJson);
	return true;
}

function addMaven(){
	var addMavenHtml = '<div class="row conCred conCredIbox mavenIbox">'
						+'<div class="">'
						+'<div class="ibox float-e-margins">'
						+'<div class="ibox-title">'
						+'<h5>maven</h5>'
						+'<div class="ibox-tools">'
						+'<a class="collapse-link"> <i class="fa fa-chevron-up"></i>'
						+'</a> <a class="close-link"> <i class="fa fa-times fa-delete"></i>'
						+'</a>'
						+'</div>'
						+'</div>'
						+'<div class="ibox-content">'
						+'<div class="row ant-config">'
						+'<div class="form-group col-md-12">'
						+'<label class="labelCred">maven版本:</label>'
						+'<input type="text" class="form-control conCred mavenVersion" value="">'
						+'</div>'
						+'<div class="form-group col-md-12">'
						+'<span class="labelCred">环境变量：</span>'
						+'<table class="table enabled conCred jdkCon">'
						+'<thead>'
						+'<tr>'
						+'<th style="width: 35%">key</th>'
						+'<th style="width: 35%">val</th>'
						+'<th style="vertical-align: middle; width: 10%">操作</th>'
						+'</tr>'
						+'</thead>'
						+'<tbody class="maventbody">'
					
						+'</tbody>'
						+'<tfoot>'
						+'<tr>'
						+'<td colspan="3" id="maven" class="create"><i class="fa fa-plus margin"></i>添加</td>'
						+'</tr>'
						+'</tfoot>'
						+'</table>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>';
	$(".mavens").append(addMavenHtml);
}
//var addAntNum = 1;
function addAnt(){
	var addAntHtml = '<div class="row conCred conCredIbox antIbox">'
						+'<div class="">'
						+'<div class="ibox float-e-margins">'
						+'<div class="ibox-title">'
						+'<h5>ant</h5>'
						+'<div class="ibox-tools">'
						+'<a class="collapse-link"> <i class="fa fa-chevron-up"></i>'
						+'</a> <a class="close-link"> <i class="fa fa-times fa-delete"></i>'
						+'</a>'
						+'</div>'
						+'</div>'
						+'<div class="ibox-content">'
						+'<div class="row ant-config">'
						+'<div class="form-group col-md-12">'
						+'<label class="labelCred">ant版本:</label>'
						+'<input type="text" class="form-control conCred antVersion" value="">'
						+'</div>'
						+'<div class="form-group col-md-12">'
						+'<span class="labelCred">环境变量：</span>'
						+'<table class="table enabled conCred jdkCon">'
						+'<thead>'
						+'<tr>'
						+'<th style="width: 35%">key</th>'
						+'<th style="width: 35%">val</th>'
						+'<th style="vertical-align: middle; width: 10%">操作</th>'
						+'</tr>'
						+'</thead>'
						+'<tbody class="anttbody">'
					
						+'</tbody>'
						+'<tfoot>'
						+'<tr>'
						+'<td colspan="3" id="ant" class="create"><i class="fa fa-plus margin"></i>添加</td>'
						+'</tr>'
						+'</tfoot>'
						+'</table>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>';
	$(".ants").append(addAntHtml);
	//addAntNum++;
}




