$(document).ready(function(){
	loadImageList();
	getServiceStorageVol();
	getMountPath();
	
	document.getElementById("cpu1").checked=true;
	document.getElementById("ram1").checked=true;
	
	$(".createPadding").addClass("hide");
	
	$("#createButton").click(function(){
		
		if(!saveEnvVariable()) {
			return;
		}
		
		//判断服务名称
		var name = $('#serviceName').val();
		// check the name of container
	    if(!name || name.length < 1){
	      layer.tips('服务名称不能为空','#serviceName',{tips: [1, '#3595CC']});
	      $('#serviceName').focus();
	      return;
	    }
	    if(name.search(/^[a-z][a-z0-9-]*$/) === -1){
	      layer.tips('服务名称只能由小写字母、数字及横线组成，且首字母不能为数字及横线。','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
	    if(name.length > 64 || name.length < 3){
	      layer.tips('服务名称为3~64个字符','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
	    
	    //自定义启动命令的判断
	    var startCommand_input = $("#startCommand_input").val();
	    if($("#startCommand").prop("checked")==true){
		    if(!startCommand_input || startCommand_input.length < 1){
			      layer.tips('自定义启动命令不能为空','#startCommand_input',{tips: [1, '#3595CC']});
			      $('#startCommand_input').focus();
			      return;
		    }		   
/*		    if(startCommand_input.search(/^[a-zA-Z][a-zA-Z0-9-]*$/) === -1){
		    	layer.tips('自定义启动命令只能由字母、数字及横线组成，且首字母不能为数字及横线。','#startCommand_input',{tips: [1, '#3595CC'],time: 3000});
		    	$('#startCommand_input').focus();
		    	return;
		    }
		    if(startCommand_input.length > 64 || startCommand_input.length < 3){
		    	layer.tips('自定义启动命令为3~64个字符','#startCommand_input',{tips: [1, '#3595CC'],time: 3000});
		    	$('#startCommand_input').focus();
		    	return;
		    }*/
	    }
	    
	  //检查服务状态的判断
	    var checkPath = $("#checkSerStatus_input").val();
	    if($("#checkSerStatus").prop("checked")==true){
		    if(!checkPath || checkPath.length < 1){
			      layer.tips('测试路径不能为空','#checkSerStatus_input',{tips: [1, '#3595CC']});
			      $('#checkSerStatus_input').focus();
			      return;
			}
		    if(checkPath.search(/^[a-zA-Z][a-zA-Z0-9-\/]*$/) === -1){
		      layer.tips('测试路径只能由字母、数字及横线组成，且首字母不能为数字及横线。','#checkSerStatus_input',{tips: [1, '#3595CC'],time: 3000});
		      $('#checkSerStatus_input').focus();
		      return;
		    }
		    if(checkPath.length > 64 || checkPath.length < 3){
		      layer.tips('测试路径为3~64个字符','#checkSerStatus_input',{tips: [1, '#3595CC'],time: 3000});
		      $('#checkSerStatus_input').focus();
		      return;
		    }
	    }
	    //服务路径的判断
	    var servicePath = $("#webPath").val();
	    if(!servicePath || servicePath.length < 1){
		      layer.tips('服务路径不能为空','#webPath',{tips: [1, '#3595CC']});
		      $('#webPath').focus();
		      return;
		}
	    if(servicePath.search(/^[a-zA-Z][a-zA-Z0-9-\/]*$/) === -1){
	      layer.tips('服务路径只能由字母、数字及横线组成，且首字母不能为数字及横线。','#webPath',{tips: [1, '#3595CC'],time: 3000});
	      $('#webPath').focus();
	      return;
	    }
	    if(servicePath.length > 64 || servicePath.length < 3){
	      layer.tips('服务路径为3~64个字符','#webPath',{tips: [1, '#3595CC'],time: 3000});
	      $('#webPath').focus();
	      return;
	    }
	    
	    //nginx代理路径的判断
	    var proxyPath = $("#nginxPath").val();
	    if(!proxyPath || proxyPath.length < 1){
		      layer.tips('nginx代理路径不能为空','#nginxPath',{tips: [1, '#3595CC']});
		      $('#nginxPath').focus();
		      return;
		}
	    if(proxyPath.search(/^[a-zA-Z][a-zA-Z0-9-\/]*$/) === -1){
		      layer.tips('服务路径只能由字母、数字及横线组成，且首字母不能为数字及横线。','#nginxPath',{tips: [1, '#3595CC'],time: 3000});
		      $('#nginxPath').focus();
		      return;
	    }
	    if(proxyPath.length > 64 || proxyPath.length < 3){
	      layer.tips('服务路径为3~64个字符','#nginxPath',{tips: [1, '#3595CC'],time: 3000});
	      $('#nginxPath').focus();
	      return;
	    }
	    
	    //判断实例数量是否超过上限
	    var cpuNum = $("input[name='cpuNum']:checked").val();
	    var leftcpu = $("#leftcpu").val();
	    var max1 = leftcpu/cpuNum;
	    var ram = $("input[name='ram']:checked").val();
	    var leftmemory = $("#leftmemory").val();
	    var max2 = leftmemory/ram;
	    var max =0;
	    if(max1<=max2){
	    	max=max1;
	    }else{
	    	max=max2;
	    }
	    var instNum = $("#instanceNum").val();
	    if(instNum>=max){
	    	layer.tips('您可以创建的实例数量不能大于'+Math.floor(max)+',请调整CPU数量和内存',"#instanceNum",{tips: [1, '#3595CC']});
	    	$('#instanceNum').focus();
			return;
	    }
	    
	    //判断cpu是否有足够的剩余
	    var cpuNum = $("input[name='cpuNum']:checked").val();
	    var leftcpu = $("#leftcpu").val();
	    if (cpuNum == null) {
	    	layer.tips('请您选择cpu数量',"#ve_cpu",{tips: [1, '#3595CC']});
		    $('#ve_cpu').focus();
			return;
	    }
	    if (parseInt(cpuNum) > parseInt(leftcpu)) {
	    	layer.tips('cpu剩余不足',"input[name='cpuNum']:checked",{tips: [1, '#3595CC']});
		    $("input[name='cpuNum']:checked").focus();
			return;
	    }
	    
	    //判断内存是否有足够的剩余
	    var ram = $("input[name='ram']:checked").val();
	    var leftmemory = $("#leftmemory").val();
	    if (ram == null) {
	    	layer.tips('请您选择内存数量',"#ve_ram",{tips: [1, '#3595CC']});
		    $("#ve_ram").focus();
			return;
	    }
	    if (parseInt(ram) > parseInt(leftmemory)) {
	    	layer.tips('内存剩余不足',"input[name='ram']:checked",{tips: [1,"#3595CC"]})
	    	$("input[name='ram']:checked").focus();
	    	return;
	    }
	    
	    //挂载路径的判断
	    var mountPath = $("#mountPath").val();
	    var selectVolume = $("#selectVolume").val();
	    if ($("#state_service").prop("checked")==true) {
		    if (!mountPath || mountPath.length < 1) {
			      layer.tips('挂载路径不能为空','#mountPath',{tips: [1, '#3595CC']});
			      $('#mountPath').focus();
			      return;
			}		   
		    if (selectVolume=='0'){
			      layer.tips('请选择一个挂载卷','#selectVolume',{tips: [1, '#3595CC']});
			      $('#selectVolume').focus();
			      return;
		    }
	    }
	    
/*	    var nginxstr = "{";
	    $('input[name="nginxserv"]:checked').each(function(){
    		var servname = $(this).val();
    		var servid = $(this).attr('id');
    		nginxstr = nginxstr+"'"+servid+"'"+":"+"'"+servname+"',";
    	})
    	nginxstr = nginxstr.substring(0,nginxstr.length-1) +"}";
    	// var nginxObj = eval('(' + nginxstr + ')');
    	if(nginxstr == "}"){
    		nginxstr = "{}";
    	}*/
	    var nginxstr = "";
	    $('input[name="nginxserv"]:checked').each(function(){
    		var servname = $(this).val();
    		var servid = $(this).attr('id');
    		if ("" == nginxstr) {
    			nginxstr = servid;
    		} else {
    			nginxstr += ","+servid;
    		}
    	})
    	$('#proxyZone').val(nginxstr);
    	
    	//将端口配置 数据变为json放入到
	    var portJson ="";
        $("#pushPrptpcol tr").each(function (index, domEle){
    				var protocol = "";
    				var mapPort = "";
    				var containerPort = "";
    				$(domEle).find("input").each(function(index,data){  
    					if(index == 0){  containerPort = $(data).val();  }
    					});  
    				$(domEle).find("select").each(function(index,data){  
    					if(index == 0){  protocol = $(data).val();  }
    					}); 
    				$(domEle).find("i").each(function(index,data){  
    					if(index == 0){  mapPort = $(data).html();  }
    					}); 
    				portJson += "{"+"\"containerPort\":\""+containerPort+"\","+"\"protocol\":\""+protocol+"\","
    				+"\"mapPort\":\""+mapPort+"\"},";                 
        });
        if (portJson != "") {  
        		portJson = portJson.substring(0,portJson.length -1);  
        		portJson ="[" +portJson+ "]";  
        }
        $('#portConfig').val(portJson);
	    //var cpuNum = $('#cpuNum').val();
	    /*if(!cpuNum || cpuNum.length < 1){
		      layer.tips('cpu数量不能为空','#cpuNum',{tips: [1, '#3595CC']});
		      $('#cpuNum').focus();
		      return;
		    }*/

	    //var ram = $('#ram').val();
	    /*if(!ram || ram < 1){
		      layer.tips('内存不能为零','#ram',{tips: [1, '#3595CC']});
		      $('#ram').focus();
		      return;
		    }*/
        var serviceName = $("#serviceName").val();
        $.ajax({
    		url : ctx + "/service/matchPath.do",
    		type: "POST",
    		data:{"proxyPath":proxyPath,"serviceName":serviceName},
    		success : function(data) {
    			data = eval("(" + data + ")");
    			if (data.status=="400") {
    				layer.alert("nginx路径名称重复，请重新输入！");
    			} else if (data.status=="500") {
    				layer.alert("服务名称重复，请重新输入！");
    			}else {
    				$("#buildService").submit();
    			}
    		}
    	});
    });
	
	$("#service-path").click(function(){
    	layer.tips('内容必须和上传的项目名一致！', '#service-path', {
            tips: [2, '#0FA6D8'] //还可配置颜色
        });
    });
	
	$("#proxy-path").click(function(){
    	layer.tips('内容建议为“用户名+项目名”！', '#proxy-path', {
            tips: [2, '#0FA6D8'] //还可配置颜色
        });
    });
	
	//选择cpu数量之后，默认选择相对应的内存大小；
	/*$(".cpuNum").change(function(){
		var cpuNum = $("input[name='cpuNum']:checked").val();
		var ramId = "ram"+cpuNum;
		document.getElementById(ramId).checked=true;
    });*/
	
	/*
	 * $(".cpuNum").click(function(){ var tips = $(this).attr("placeholder");
	 * layer.tips(tips,'.cpuNum',{tips: [1, '#3595CC']}); })
	 */

	// 控制checkbook后输入框是否可填写
	$("#save_roll_dev").hide();
	$("#state_service").click(function(){
//		alert($("#state_service").prop("checked"));
		$("#save_roll_dev").toggle();
		$("#mountPath").focus();
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);
	})

	// 启动命令
	$("#startCommand_li").hide();
	$("#startCommand").click(function(){
		$("#startCommand_li").toggle();
		$("#startCommand_input").focus();
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);
	})
	
	// 添加环境变量
	$("#cratePATH").click(function(){
		var addName = $("#Name").val();
		var addValue = $("#Value").val();
		//环境变量Key只能是字母数字下划线；
		if(addName.search(/^[0-9a-zA-Z_]+$/) === -1){
			layer.tips('环境变量key只能是字母数字下划线','#Name',{tips: [1, '#3595CC']});
			$('#Name').focus();
			return;
		}
		//判断value长度
		if(addValue.length >= 4096){
	    	layer.tips('value字符长度不能超过4096','#Value',{tips: [1, '#3595CC']});
		      $('#Value').focus();
		      return;
	    }
		//判断key是否重复，
		var arrayKey = $("#arrayKey").val().split(",");
		for(var i = 0; i<arrayKey.length; i++){
			if(addName == arrayKey[i]){
				layer.tips('环境变量key不能重复','#Name',{tips: [1, '#3595CC']});
				$('#Name').focus();
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
		$("#Path-oper1").append(tr);
		}
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);	
	});
	
	//自动化伸缩范围&伸缩阈值
	var trueOrfalse = false;
	$("#dynamic-range").hide();
	$("#dynamic-threshold").hide();
	$("#dynamic-service").click(function(){
		trueOrfalse = !trueOrfalse;
		$("#instanceNum").attr("disabled",trueOrfalse);
		$("#dynamic-range").toggle();
		$("#dynamic-threshold").toggle();
		$("#minNum").focus();
	});
	
	// 添加端口
	$("#createPort").click(function(){
		$.ajax({
			url : ctx + "/service/generatePortSet.do",
    		type: "GET",
    		success : function(data) {
    		data = eval("(" + data + ")");
    		if(!data.mapPort||"error"==(data.ERROR)){
    				alert("可用映射端口已经用尽，请联系管理员。");
    		}else{
    		var portTr =''+ 
				'<tr class="plus-row">'+
    					'<td>'+
    						'<input class="port" type="text">'+
							'</td>'+
							'<td>'+
									'<select class="T-http">'+
										  '<option>TCP</option>'+
											'<option>HTTP</option>'+
									'</select>'+
							'</td>'+
							'<td>'+
									'<i>'+data.mapPort+'</i>'+
							'</td>'+
							'<td>'+
									'<a href="javascript:void(0)" onclick="deletePortRow(this,'+data.mapPort+')" class="gray">'+
												'<i class="fa fa-trash-o fa-lg"></i>'+
									'</a>'+
						  '</td>'+
				'</tr>';
    		$("#pushPrptpcol").append(portTr);
        		}
    		//调节界面高度
    		var imagePage_height = $(".host_step2").height();
        	$(".step-inner").height(imagePage_height+100);
    		}
		});
		
	});
	
	//导入模板文件选项对勾
	var templateName = null;
	$(document).on("click","#Path-table tr", function () {
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
		$(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
		//$("#Path-table>tbody>tr").parent().find("tr.focus").find("span.vals-path").removeClass("hide")
		$(this).toggleClass("focus");//设定当前行为选中行
		$(this).parent().find("tr.focus").find("span.vals-path").toggleClass("hide");
		templateName = $(this).parent().find("tr.focus").find(".templateName").val();
		console.log(templateName);
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
	    	            $("#Path-oper1").append(html);
	    	            $("#arrayKey").attr("value",arrayKey);
	         		}
	         	});
	        	var containerRes_height = $(".host_step2").height();
	          $(".step-inner").height(containerRes_height+100);
	        	layer.close(index);
	        	
	        }
		})
	});
	
	//另存为模板
	$("#exportBtn").click(function(){
		if (!saveEnvVariable()) {
			return;
		} else {
			layer.open({
			 	type:1,
		        title: '另存为模板',
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
		        		layer.tips('环境变量不能为空','#Path',{tips: [1, '#3595CC']});
						$('#Path').focus();
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
												}
											}	
							        	});

									  }
									});
							}else if (data.status == "200") {
								layer.msg("环境变量模板导入成功",{icon: 6});
								layer.close(index);
							}
						}	
		        	});
		        }
			})
		}
	});

	$("#searchimage").click(function(){
		var imageName = $('#imageName').val();
		$.ajax({
	        url: ""+ctx+"/service/findimages.do?imageName="+imageName,
	        success: function (data) {
	            data = eval("(" + data + ")");
	            var html = "";
	            if (data != null) {
	                if (data['data'].length > 0) {
	                    for (var i in data.data) {
	                        var image = data.data[i];
	                        var portConfigs = JSON.stringify(image.portConfigs);
	                        if (image.isBaseImage == 2) {		//判断是否为基础镜像，是基础镜像不显示
	                        	
		                        html += "<li class='image-item'><span class='img_icon span2'>"+
								"<img src='"+ctx+"/images/image-1.png'>"+
						"</span> <span class='span5 type' type='database'>"+
								"<div class='list-item-description'>"+
									"<div class='name h4'>"+
										""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"../registry/detail/"+image.id+"'>"+
											"<i class='fa fa-external-link-square'></i>"+
										"</a>"+
									"</div>"+
								"</div>"+
						"</span> <span class='span3'>"+
								"<div class='list-item-description'>"+
									"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+
										""+ image.version +"</span> <span imgID='"+image.id+"' resourceName='"+image.resourceName+"' imageName='"+image.name+"' imageVersion='"+image.version+"' portConfigs='"+portConfigs+"' class='pull-deploy btn btn-primary'"+
										"data-attr='tenxcloud/mysql'> 部署 <i"+
										"class='fa fa-arrow-circle-o-right margin fa-lg'></i>"+
									"</span>"+
								"</div>"+
						"</span></li>";
		                        
		                    	}
	                    	}
	                    	$("#imageList").html(html);
	                    	$(".pull-deploy").click(function(){

	                        	var imageName = $(this).attr("imageName");
	                        	var imageVersion = $(this).attr("imageVersion");
	                        	var imgID = $(this).attr("imgID");
	                        	var resourceName = $(this).attr("resourceName");
	                      // 	var portConfigs = $(this).attr("portConfigs");
	                        	var portConfigs ;
	                        	$.ajax({
	                                url: ""+ctx+"/service/getPortConfig.do?imgID="+imgID,
	                                type: "GET",
	                                success: function (data) {
	                                	data = eval("(" + data + ")");
	                                 portConfigs =	JSON.stringify(data.data);
	                                 deploy(imgID,imageName, imageVersion,resourceName,portConfigs);
	                                }
	                        	});
	                        });
	                	}
	            }
	        }
	    })
	});

	cpuMouseOut();

    $(".two_step").click(function(){

        $(this).addClass("hide");
        $(".createPadding").removeClass("hide");
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left","-200%");

    });

    $(".go_backs").click(function(){

        if($(".radius_step").eq(1).hasClass("action")){
            $(".createPadding").addClass("hide");
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".createPadding").removeClass("hide");
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }
    	var imagePage_height = $(".host_step1").height();
    	$(".step-inner").height(imagePage_height +100);
    });
    
    //从镜像中心部署，跳转服务之后的页面高度
    var localUrl = window.location;
	if(localUrl.search != ""){
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height +100);
	}
});

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
	            } 
	            if (html == "") {
	            	html += '<tr><td>没有保存的模板</td></tr>'	
	            }
	            $("#Path-env").empty();
	            $("#Path-env").append(html);
  			}
	 });
}



//保存环境变量到json中
function saveEnvVariable() {
	var dataJson="";  
	var arrayKey = new Array(1) ;
	var flag = 0;
    $("#Path-oper1 tr").each(function (index, domEle){
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
				layer.tips('环境变量Key不能重复','#Path',{tips: [1, '#3595CC']});
				$('#Path').focus();
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

/**
 * 删除环境变量
 */
// 删除环境变量
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
	//调节界面高度
	var imagePage_height = $(".host_step2").height();
	$(".step-inner").height(imagePage_height+100);	
}

function decideEnvKey(){
	var arrayKey = $("#arrayKey").val().split(",");
	for(var i = 0; i<arrayKey.length; i++){
		for(var j = i+1;i<arrayKey.length;j++){
			alert(arrayKey[i] + ":" + arrayKey[j]);
			if(arrayKey[i] == arrayKey[j]){
				alert(false);
			}
		}
	}
}

function cpuMouseOut(){
	var cpuNum = $('#cpuNum').val();
	var cpuInput = $("#cpuNum")[0];
	if(cpuNum > 12){
		cpuInput.value = 12;
	}
}


/*
 * $(function(){ var ramSlider = $("#ramSlider").slider({ formatter:
 * function(value) { return value; }, value:100, min: 512, max: 8192, step: 512,
 * slide: function( event, ui ) { $( "#ram" ).val( "$" + ui.value ); } }); //$(
 * "#ram" ).val( "$" + $( "#ramSlider" ).slider( "value" ) );
 * 
 * ramSlider.on("slide", function(slideEvt) { $("#ram").val(slideEvt.value);
 * }).on("change", function(slideEvt){ $("#ram").val(slideEvt.value.newValue);
 * });
 * 
 * $("#ram").on("change",function(){ var ramVal = Number($(this).val());
 * console.log(ramVal); ramSlider.slider('setValue', ramVal); }); });
 */

function loadImageList() {
    $.ajax({
        url: ""+ctx+"/service/images",
        success: function (data) {
            data = eval("(" + data + ")");
            var html = "";
            if (data != null) {
                if (data['data'].length > 0) {
                    for (var i in data.data) {
                        var image = data.data[i];
                        var portConfigs = JSON.stringify(image.portConfigs);
                        if (image.isBaseImage == 2) {   //判断是否为基础镜像，是基础镜像不显示
                        	
	                        html += "<li class='image-item'><span class='img_icon span2'>"+
							"<img src='"+ctx+"/images/image-1.png'>"+
					"</span> <span class='span5 type' type='database'>"+
							"<div class='list-item-description'>"+
								"<div class='name h4'>"+
									""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"/registry/detail/"+image.id+"'>"+
										"<i class='fa fa-external-link-square'></i>"+
									"</a>"+
								"</div>"+
							"</div>"+
					"</span> <span class='span3'>"+
							"<div class='list-item-description'>"+
								"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+ 
									""+ image.version +"</span> <span imgID='"+image.id+"'resourceName='"+image.resourceName+"'  imageName='"+image.name+"' imageVersion='"+image.version+"' portConfigs='"+portConfigs+"' class='pull-deploy btn btn-primary'"+
									"data-attr='tenxcloud/mysql'> 部署 <i"+
									"class='fa fa-arrow-circle-o-right margin fa-lg'></i>"+
								"</span>"+
							"</div>"+
					"</span></li>";
	                        
	                    	}
                    	}
                    	$("#imageList").html(html);

                        $(".pull-deploy").click(function(){
                        	var containerRes_height = $(".host_step2").height();
                        	$(".step-inner").height(containerRes_height+100);

                        	var imageName = $(this).attr("imageName");
                        	var imageVersion = $(this).attr("imageVersion");
                        	var imgID = $(this).attr("imgID");
                        	var resourceName = $(this).attr("resourceName");
                      // 	var portConfigs = $(this).attr("portConfigs");
                        	var portConfigs ;
                        	$.ajax({
                                url: ""+ctx+"/service/getPortConfig.do?imgID="+imgID,
                                type: "GET",
                                success: function (data) {
                                	data = eval("(" + data + ")");
                                 portConfigs =	JSON.stringify(data.data);
                                 deploy(imgID,imageName, imageVersion,resourceName,portConfigs);
                                									
                                }
                        	});
                        	// containerName();
                        });
                	}
            }
        }
    })
}

function deploy(imgID,imageName, imageVersion,resourceName,portConfigs){
		if (undefined != portConfigs && 'null' != portConfigs) {
			portConfigs = eval("(" + portConfigs + ")");
			$("#pushPrptpcol").empty();
        	$.each(portConfigs,function(i,n){
        		var portTr = '<tr class="plus-row">'+
				  									'<td>'+
        												'<input class="port" type="text" value=" '+n.containerPort+'">'+
				  									'</td>'+
				  									'<td>' +
				  											'<select class="T-http">'+
				  													'<option>TCP</option>'+
				  													'<option>HTTP</option>'+
				  											'</select>'+
				  									'</td>'+
				  									'<td>'+
				  										'<i>'+ n.mapPort +'</i>'+
				  									'</td>'+
				  									'<td>'+
				  											'<a href="javascript:void(0)" onclick="deletePortRow(this,'+n.mapPort+')" class="gray">'+
				  											'<i class="fa fa-trash-o fa-lg"></i>'+
				  											'</a>'+
				  									'</td>'+
				  								'</tr>';
        		$("#pushPrptpcol").append(portTr);
    				});
		}
	    $("#imgName").val(imageName);
	    $("#imgVersion").val(imageVersion);
	    $("#imgID").val(imgID);
	    $("#resourceName").val(resourceName);
	    $("#portConfigs").val(portConfigs);
	    $(".step-inner").css("left","-100%");
	    $(".createPadding").removeClass("hide");
	    $(".radius_step").removeClass("action").eq(1).addClass("action");
	    $(".two_step").removeClass("hide");
}

function containerName(){
	var serviceName = $("#serviceName").val();
			 $.ajax({
					url:""+ctx+"/service/serviceName.do?serviceName="+serviceName,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="400"){
							layer.alert("服务名称重复，请重新输入！");
							}else{
								$("#buildService").submit();
							}
					}
			 });
}

function getServiceStorageVol(){
	$.ajax({
		url : ctx + "/service/storageList?pageable=",
		type: "POST",
		success : function(data) {
			var jsonData = $.parseJSON(data);
			for(var i = 0; i < jsonData.count; i++){
				var storageVolOpt = '<option name="volName" value=""></option>';
				var txt = jsonData.storages[i].storageName +" "+ jsonData.storages[i].storageSize + "M";
				$("#selectVolume").append(storageVolOpt);
				$("#selectVolume")[0].children[i+1].innerHTML = txt;
				$("#selectVolume")[0].children[i+1].value = jsonData.storages[i].storageName;
			}
			
		}
	})
}
//获取挂载地址mountPath
function getMountPath(){
	$("#selectVolume").change(function(){
		var volume = $("#selectVolume").val();
		$.ajax({
			url : ctx + "/service/getMountPath.do?volume="+volume,
			type: "GET",
			success : function(data) {
				data = eval("(" + data + ")");
				$("#mountPath").val(data.mountPath);
			}
		})
    });
}
//删除port
function deletePortRow(obj,int){
	//alert(int);
	 $.ajax({
			url:""+ctx+"/service/removeSet.do?set="+int,
			type: "GET",
			success : function(data) {
				
			}
	 });
	 $(obj).parent().parent().remove();
	//调节界面高度
	var imagePage_height = $(".host_step2").height();
 	$(".step-inner").height(imagePage_height);	
}

/*
 * function deleteRow(obj){ var id = "";
 * $(":checked[name='ids']").each(function(){ id = id + jQuery(this).val() +
 * ","; }); if ("" == id) { alert("请选择至少一个用户"); return; } else { id =
 * id.substring(0, id.length - 1); layer.open({ title: '删除用户',
 * content:'确定删除多个用户吗？', btn: ['确定', '取消'], yes: function(index, layero){
 * //或者使用btn1 layer.close(index); $.ajax({ url:ctx+"/user/delMul.do?ids="+id,
 * success:function(data){ data = eval("(" + data + ")");
 * if(data.status=="200"){ layer.alert("用户信息删除成功"); }else{
 * layer.alert("用户信息删除失败，请检查服务器连接"); } //location.href =
 * ctx+"redirect:/user/list"; location.reload(true); } })
 *  }, cancel: function(index){ //或者使用btn2 //按钮【按钮二】的回调 } }); } }
 */


