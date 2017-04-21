$(document).ready(function(){
	// 控制环境变量的参数
	var count=1;
	loadImageList();

	$(".cpuNum")[0].setAttribute("checked",true);
	$(".ram")[0].setAttribute("checked",true);
	//调节高度
	var imagePage_height = $(".host_step1").height();
	//$(".step-inner").height(imagePage_height +100);

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
	    reg=/^[a-z]([-a-z0-9]*[a-z0-9])?$/;
	    if(!reg.test(name)){
		      layer.tips('服务名称只能由小写字母、数字及横线组成，且首字母不能为数字及横线。','#serviceName',{tips: [1, '#3595CC'],time: 3000});
		      $('#serviceName').focus();
		      return;
	    }
	    if(name.length > 24 || name.length < 1){
	      layer.tips('服务名称为1~24个字符','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
		var serviceChName = $('#serviceChName').val();
		if (serviceChName.length > 24 || serviceChName.length < 1) {
			layer.tips('服务中文名称为1~24个字符', '#serviceChName', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#serviceChName').focus();
			return;
		}
		var responsiblePerson = $('#responsiblePerson').val();
		if (responsiblePerson.length > 24 || responsiblePerson.length < 1) {
			layer.tips('责任人为1~24个字符', '#responsiblePerson', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#responsiblePerson').focus();
			return;
		}
		reg = /^((\d{11})|((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})))$/;
		var responsiblePersonTelephone = $('#responsiblePersonTelephone').val();
		if (!reg.test(responsiblePersonTelephone)) {
			layer.tips('非法的电话格式', '#responsiblePersonTelephone', {
				tips : [1, '#3595CC'],
				time : 3000
			});
			$('#responsiblePersonTelephone').focus();
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
	    } else {
	    	$("#startCommand_input").val(null);
	    }

	  //检查服务状态的判断
	    var checkPath = $("#checkSerStatus_input").val();
	    var initialDelay = $("#initialDelay").val();
	    var timeoutDetction = $("#timeoutDetction").val();
	    var periodDetction = $("#periodDetction").val();
	    if($("#checkSerStatus").prop("checked")==true){
		    if(!checkPath || checkPath.length < 1){
			      layer.tips('测试路径不能为空','#checkSerStatus_input',{tips: [1, '#3595CC']});
			      $('#checkSerStatus_input').focus();
			      return;
			}
		    if(checkPath.search(/^[a-zA-Z\/][a-zA-Z0-9-\/]*$/) === -1){
			      layer.tips('测试路径只能由字母、数字、斜线及横线组成，且首字母不能为数字及横线。','#checkSerStatus_input',{tips: [1, '#3595CC'],time: 3000});
			      $('#checkSerStatus_input').focus();
			      return;
		    }
		    if(checkPath.length > 64 || checkPath.length < 3){
			      layer.tips('测试路径为3~64个字符','#checkSerStatus_input',{tips: [1, '#3595CC'],time: 3000});
			      $('#checkSerStatus_input').focus();
			      return;
		    }

		    if (!initialDelay || initialDelay.length < 1) {
		    	  layer.tips('检测延迟时间不能为空','#initialDelay',{tips: [1, '#3595CC'],time: 3000});
			      $('#initialDelay').focus();
			      return;
		    }

		    if (!timeoutDetction || timeoutDetction.length < 1) {
		    	  layer.tips('检测超时时间不能为空','#timeoutDetction',{tips: [1, '#3595CC'],time: 3000});
			      $('#timeoutDetction').focus();
			      return;
		    }

		    if (!periodDetction || periodDetction.length < 1) {
		    	  layer.tips('检测频率时间不能为空','#periodDetction',{tips: [1, '#3595CC'],time: 3000});
			      $('#periodDetction').focus();
			      return;
		    }

	    } else {
	    	$("#checkSerStatus_input").val(null);
		    $("#initialDelay").val(null);
		    $("#timeoutDetction").val(null);
		    $("#periodDetction").val(null);
	    }
	    //监控设置
	    if($("#monitorStatus").prop("checked")==true){
	    	$("#monitor").val("1");
	    } else {
	    	$("#monitor").val("0");
	    }
	    //服务路径的判断
	    var servicePath = $("#webPath").val();
	    if(!servicePath || servicePath.length < 1){
		      layer.tips('服务路径不能为空','#webPath',{tips: [1, '#3595CC']});
		      $('#webPath').focus();
		      return;
		}
	    if(servicePath.search(/^[a-zA-Z\/][a-zA-Z0-9-\/]*$/) === -1){
	      layer.tips('服务路径只能由字母、数字、斜线及横线组成，且首字母不能为数字及横线。','#webPath',{tips: [1, '#3595CC'],time: 3000});
	      $('#webPath').focus();
	      return;
	    }
//	    if(servicePath.length > 64 || servicePath.length < 3){
//	      layer.tips('服务路径为3~64个字符','#webPath',{tips: [1, '#3595CC'],time: 3000});
//	      $('#webPath').focus();
//	      return;
//	    }

	    //nginx代理路径的判断
	    var proxyPath = $("#nginxPath").val();
	    if(!proxyPath || proxyPath.length < 1){
		      layer.tips('nginx代理路径不能为空','#nginxPath',{tips: [1, '#3595CC']});
		      $('#nginxPath').focus();
		      return;
		}
	    if(proxyPath.search(/^[a-zA-Z\/][a-zA-Z0-9-\/]*$/) === -1){
		      layer.tips('nginx代理路径只能由字母、数字、斜线及横线组成，且首字母不能为数字及横线。','#nginxPath',{tips: [1, '#3595CC'],time: 3000});
		      $('#nginxPath').focus();
		      return;
	    }
//	    if(proxyPath.length > 64 || proxyPath.length < 3){
//		      layer.tips('nginx代理路径为3~64个字符','#nginxPath',{tips: [1, '#3595CC'],time: 3000});
//		      $('#nginxPath').focus();
//		      return;
//	    }

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
	    if(!instNum || instNum.length < 1){
		      layer.tips('实例数量不能为空','#instanceNum',{tips: [1, '#3595CC']});
		      $('#instanceNum').focus();
		      return;
		}
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
	    	layer.tips('内存剩余不足',"input[name='ram']:checked",{tips: [1,"#3595CC"]});
	    	$("input[name='ram']:checked").focus();
	    	return;
	    }

	    //挂载路径的判断
	    if ($("#state_service").prop("checked")==true) {
	    	if(!saveCephData()) {
				return;
			}
	    } else {
	    	$("#serviceType").val(2);
	    }

	    var nginxstr = "";
	    $('input[name="nginxserv"]:checked').each(function(){
    		var servname = $(this).val();
    		var servid = $(this).attr('id');
    		if ("" == nginxstr) {
    			nginxstr = servid;
    		} else {
    			nginxstr += ","+servid;
    		}
    	});
    	$('#proxyZone').val(nginxstr);

    	//将端口配置 数据变为json放入到
	    var flag = false;
	    var portJson ="";
        $("#pushPrptpcol tr").each(function (index, domEle){
			var protocol = "";
			var mapPort = "";
			var containerPort = "";
			$(domEle).find("input").each(function(index,data){
				if(index == 0){
					containerPort = $(data).val();
					if(!containerPort || containerPort.length < 1){
					      layer.tips('容器端口不能为空',data,{tips: [1, '#3595CC']});
					      $(data).focus();
					      flag = true;
					      return;
					}
					if(containerPort.search(/^[0-9]*$/) === -1){
					      layer.tips('容器端口只能是数字组成',data,{tips: [1, '#3595CC'],time: 3000});
					      $(data).focus();
					      flag = true;
					      return;
					}
				}
			});
			$(domEle).find("select").each(function(index,data){
				if(index == 0){
					protocol = $(data).val();
				}
			});
			$(domEle).find("i").each(function(index,data){
				if(index == 0){
					mapPort = $(data).html();
				}
			});
			portJson += "{"+"\"containerPort\":\""+containerPort+"\","+"\"protocol\":\""+protocol+"\","
			+"\"mapPort\":\""+mapPort+"\"},";
        });
        if (flag) {
        	return;
        }
        if (portJson != "") {
        		portJson = portJson.substring(0,portJson.length -1);
        		portJson ="[" +portJson+ "]";
        }
        $('#portConfig').val(portJson);

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
            tips: [2, '#0FA6D8']
        });
    });

	$("#proxy-path").click(function(){
    	layer.tips('内容建议为“用户名+项目名”！', '#proxy-path', {
            tips: [2, '#0FA6D8']
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
		$("#save_roll_dev").toggle();
		$("#mountPath").focus();
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);
	});

	// 启动命令
	$("#startCommand_li").hide();
	$("#startCommand").click(function(){
		$("#startCommand_li").toggle();
		$("#startCommand_input").focus();
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);
	});

	// 启动命令
	$("#checkSerStatus_li").hide();
	$("#checkItems").hide();
	$("#checkSerStatus").click(function(){
		$("#checkSerStatus_li").toggle();
		$("#checkItems").toggle();
		$("#checkSerStatus_input").focus();
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);
	});

	// 添加环境变量
	$("#cratePATH").click(function(){
		var addName = $("#Name").val();
		var addValue = $("#Value").val();
		//环境变量Key只能是字母数字下划线；
		reg=/^[A-Za-z_][A-Za-z0-9_]*$/;
		if(!reg.test(addName)){
			layer.tips('环境变量key只能是字母数字下划线，不能以数字开头','#Name',{tips: [1, '#3595CC']});
			$('#Name').focus();
			return;
		}
		//判断addName长度
		if(addName.length >= 4096){
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
						'<td class="keys"><input id="key_'+count+'" type="text" style="width: 98%"></td>'+
						'<td class="vals"><input id="value_'+count+'" type="text" style="width: 98%"></td>'+
						'<td class="func">'+
							'<a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
								'<i class="fa fa-trash-o fa-lg"></i>'+
							'</a>'+
							'<input type="hidden" class="oldValue" value="'+addName+'">'+
						'</td>'+
					'</tr>';
			$("#Path-oper1").append(tr);
			$("#key_"+count).val(addName);
			$("#value_"+count).val(addValue);
			count++;
		}
		//调节界面高度
		var imagePage_height = $(".host_step2").height();
    	$(".step-inner").height(imagePage_height+100);
	});

	// 添加挂载卷
	var size = 0;
	$("#addVolume").click(function(){
		var id = $("#selectVolume").val();
		var selectVolume = $("#selectVolume option:selected").text();
		var mountPath = $("#mountPath").val();

		if (id == null || id == "") {
    		layer.tips('请选择存储卷','#selectVolume',{tips: [1, '#3595CC']});
			$('#selectVolume').focus();
			return;
    	}

		if (mountPath == null || mountPath == "") {
    		layer.tips('挂载地址不能为空','#mountPath',{tips: [1, '#3595CC']});
			$('#mountPath').focus();
			return;
    	}
		if(mountPath.search(/^[0-9a-zA-Z_/]+$/) === -1){
			layer.tips('挂载地址只能是字母数字下划线斜线','#mountPath',{tips: [1, '#3595CC']});
			$('#mountPath').focus();
			return;
		}

		var tr = '<tr>'+
					'<td class="keys"><input type="text" id = "storageName_'+count+'" style="width: 98% " readonly="readonly"></td>'+
					'<td class="vals"><input type="text" id = "mountPoint_'+count+'" style="width: 98%"></td>'+
					'<td class="func"><a href="javascript:void(0)" onclick="deleteCephRow(this)" class="gray">'+
						'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" id = "id_'+count+'">'+
					'</td>'+
				'</tr>';
		$("#volList").append(tr);
		$("#storageName_"+count).val(selectVolume);
		$("#mountPoint_"+count).val(mountPath);
		$("#id_"+count).val(id);
		count++;
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
													'<option>UDP</option>'+
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
	    	            if (data != null) {
	    	                if (data['data'].length > 0) {
	    	                	for (var i in data.data) {
	    	                		var html = "";
	    	                		var envTemplate = data.data[i];
	    	                		html = '<tr>'+
		    	    	    					'<td class="keys"><input id="key_'+count+'" type="text" style="width: 98%"></td>'+
		    	    	    					'<td class="vals"><input id="value_'+count+'" type="text" style="width: 98%"></td>'+
		    	    	    					'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
		    	    	    						'<i class="fa fa-trash-o fa-lg"></i>'+
		    	    	    						'</a><input type="hidden" class="oldValue" value="'+envTemplate.envKey+'">'+
		    	    	    					'</td>'+
		    	    	    				'</tr>';

	    	                		$("#Path-oper1").append(html);
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
	        	var containerRes_height = $(".host_step2").height();
	          $(".step-inner").height(containerRes_height+100);
	        	layer.close(index);

	        }
		});
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
													layer.msg("环境变量模板保存成功",{icon: 6});
													layer.close(index2);
													layer.close(index);
												}
											}
							        	});
									  }
									});
							}else if (data.status == "200") {
								layer.msg("环境变量模板保存成功",{icon: 6});
								layer.close(index);
							}
						}
		        	});
		        }
			});
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

		                        html += "<li class='image-item'><span class='img_icon span16'>"+
								"<img src='"+ctx+"/images/image-1.png'>"+
						"</span> <span class='span4 type span5add' type='database'>"+
								"<div class='list-item-description'>"+
									"<div class='name h4'>"+
										""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"../registry/detail/"+image.id+"'>"+
											"<i class='fa fa-external-link-square'></i>"+
										"</a>"+
									"</div>"+
								"</div>"+
						"</span> <span class='span4'>"+
								"<div class='list-item-description'>"+
									"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+
										""+ image.version +"</span> <span imgID='"+image.id+"' resourceName='"+image.resourceName+"' imageName='"+image.name+"' imageVersion='"+image.version+"' portConfigs='"+portConfigs+"' class='pull-deploy btn btn-primary btn-color'"+
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
	                        });
	                	}
	            }
	        }
	   });
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
//	//监控设置checkbox的响应事件
//	$("#monitorStatus").click(function() {
//		$("#APM1").remove();
//		$("#APM2").remove();
//		$("#APM3").remove();
//		if ($("#monitorStatus").prop("checked") == true) {
//			html = '<tr id="APM2" style="display:none;">'
//				+ '<input type="hidden" value="namespace"></td>'
//				+ '<input type="hidden" id="value_namespace" value=""></td>'
//				+ '</tr>'
//				+ '<tr id="APM3" style="display:none;">'
//				+ '<input type="hidden" value="service"></td>'
//				+ '<input type="hidden" id="value_service" value=""></td>'
//				+ '</tr>'
//			$("#Path-oper1").append(html);
//			$("#value_namespace").val($("#userName").val());
//			$("#value_service").val($("#serviceName").val());
//		} else {
//			html = '<tr id="APM1" style="display:none;">'
//				+ '<input type="hidden" value="APM"></td>'
//				+ '<input type="hidden" id="value_id" value="true"></td>'
//				+ '</tr>'
//			$("#Path-oper1").append(html);
//			$("#value_namespace").val($("#userName").val());
//			$("#value_service").val($("#serviceName").val());
//		}
//	})
//	$("#monitorStatus").prop("checked","checked");
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
	                					'<input type="hidden" class="templateName" value="'+templateName+'" />'+
	                				'</td>'+
	                			'</tr>';
                	}
	            }
	            if (html == "") {
	            	html += '<tr><td>没有保存的模板</td></tr>';
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
				layer.tips('环境变量Key不能重复',$(domEle).find("input")[0],{tips: [1, '#3595CC']});
				$('#Path').focus();
				flag = 1;
				break;
			}
		}
		//环境变量Key只能是字母数字下划线；
		reg=/^[A-Za-z_][A-Za-z0-9_]*$/;
		if(!reg.test(envKey)){
			layer.tips('环境变量key只能是字母数字下划线，不能以数字开头',$(domEle).find("input")[0],{tips: [1, '#3595CC']});
			$('#Name').focus();
				flag = 1;
		}
		//判断envKey长度
		if(envKey.length >= 4096){
	    	layer.tips('key字符长度不能超过4096',$(domEle).find("input")[0],{tips: [1, '#3595CC']});
		      $('#Value').focus();
				flag = 1;
	    }
		//判断envValue长度
		if(envValue.length >= 4096){
	    	layer.tips('value字符长度不能超过4096',$(domEle).find("input")[1],{tips: [1, '#3595CC']});
		      $('#Value').focus();
				flag = 1;
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

//保存挂载地址数据到json中
function saveCephData(){
	var dataJson="";
	var arrayKey = new Array(1) ;
	var arrayValue = new Array(1);
	var flag = 0;
    $("#volList tr").each(function (index, domEle){
    	 var id = "";
         var mountPoint = "";
         $(domEle).find("input").each(function(index,data){
             if (index == 1){
             	mountPoint = $(data).val();
             }
             if(index == 2){
             	id = $(data).val();
             }
         });

 		for (var i = 0; i<arrayKey.length;i++) {
 			if (id == arrayKey[i]) {
 				layer.tips('存储卷不能重复，请您重新选择',domEle,{tips: [1, '#3595CC']});
 				$(domEle).focus();
 				flag = 1;
 				break;
 			}
 		}
 		arrayKey.push(id);
 		for (var i = 0; i<arrayValue.length;i++) {
 			if (mountPoint == arrayValue[i]) {
 				layer.tips('挂载地址不能重复，请您重新填写',domEle,{tips: [1, '#3595CC']});
 				$(domEle).focus();
 				flag = 1;
 				break;
 			}
 		}
 		arrayValue.push(mountPoint);
 		dataJson += id+","+mountPoint+";";
    });

    if (flag == 1) {
    	return false;
    }
    if (dataJson != "") {
        dataJson = dataJson.substring(0,dataJson.length -1);
    }
    $('#cephAds').val(dataJson);
    return true;
}


//删除挂载卷
function deleteCephRow(obj){
	$(obj).parent().parent().remove();
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

	                        html += "<li class='image-item'><span class='img_icon span16'>"+
							"<img src='"+ctx+"/images/image-1.png'>"+
					"</span> <span class='span4 type span5add' type='database'>"+
							"<div class='list-item-description'>"+
								"<div class='name h4'>"+
									""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"/registry/detail/"+image.id+"'>"+
										"<i class='fa fa-external-link-square'></i>"+
									"</a>"+
								"</div>"+
							"</div>"+
					"</span> <span class='span4'>"+
							"<div class='list-item-description'>"+
								"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+
									""+ image.version +"</span> <span imgID='"+image.id+"'resourceName='"+image.resourceName+"'  imageName='"+image.name+"' imageVersion='"+image.version+"' portConfigs='"+portConfigs+"' class='pull-deploy btn btn-primary btn-color'"+
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
    });
}

function deploy(imgID,imageName, imageVersion,resourceName,portConfigs){
		if (undefined != portConfigs && 'null' != portConfigs) {
			portConfigs = eval("(" + portConfigs + ")");
			$("#pushPrptpcol").empty();
        	$.each(portConfigs,function(i,n){
        		var portTr = '<tr class="plus-row">'+
				  									'<td>'+
        												'<input class="port" type="text" value="'+n.containerPort+'">'+
				  									'</td>'+
				  									'<td>' +
				  											'<select class="T-http">'+
				  													'<option>TCP</option>'+
				  													'<option>UDP</option>'+
				  											'</select>'+
				  									'</td>'+
				  									'<td>'+
				  										'<i>'+n.mapPort +'</i>'+
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

//function onEditServiceName() {
//	if ($("#monitorStatus").prop("checked") != true) {
//		$("#value_namespace").val($("#userName").val());
//		$("#value_service").val($("#serviceName").val());
//	}
//}
