$(document).ready(function(){
	loadImageList();
	getServiceStorageVol();
	
	$("#createButton").click(function(){
		var name = $('#serviceName').val();
		// check the name of container
	    if(!name || name.length < 1){
	      layer.tips('服务名称不能为空','#serviceName',{tips: [1, '#3595CC']});
	      $('#serviceName').focus();
	      return;
	    }
	    name = name.toLowerCase();
	    if(name.search(/^[a-z][a-z0-9-]*$/) === -1){
	      layer.tips('服务名称只能由字母、数字及横线组成，且首字母不能为数字及横线。','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
	    if(name.length > 50 || name.length < 3){
	      layer.tips('服务名称为3~50个字符','#serviceName',{tips: [1, '#3595CC'],time: 3000});
	      $('#serviceName').focus();
	      return;
	    }
	    
	    var servicePath = $("#webPath").val();
	    if(!servicePath || servicePath.length < 1){
		      layer.tips('服务路径不能为空','#webPath',{tips: [1, '#3595CC']});
		      $('#webPath').focus();
		      return;
		}
	    
	    var proxyPath = $("#nginxPath").val();
	    if(!proxyPath || proxyPath.length < 1){
		      layer.tips('nginx代理路径不能为空','#nginxPath',{tips: [1, '#3595CC']});
		      $('#nginxPath').focus();
		      return;
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
    	
    	var dataJson="";  
        $("#Path-oper1 tr").each(function (index, domEle){
            var envKey = "";  
            var envValue = "";  
            $(domEle).find("input").each(function(index,data){  
                if(index == 0){  
                	envKey = $(data).val();  
                }else if (index == 1){  
                	envValue = $(data).val();
                }  
            });  
            dataJson += "{"+"\"envKey\":\""+envKey+"\","+"\"envValue\":\""+envValue+"\"},";               
        });
        if (dataJson != "") {  
            dataJson = dataJson.substring(0,dataJson.length -1);  
            dataJson ="[" +dataJson+ "]";  
        }
        $('#envVariable').val(dataJson);
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
	
	/*
	 * $(".cpuNum").click(function(){ var tips = $(this).attr("placeholder");
	 * layer.tips(tips,'.cpuNum',{tips: [1, '#3595CC']}); })
	 */

	// 控制checkbook后输入框是否可填写
	var forFalse = true;
	$("#save_roll_dev").hide();
	$("#state_service").click(function(){
		$("#save_roll_dev").toggle();
		$("#mountPath").focus();
	})

	// 启动命令
	$("#startCommand_li").hide();
	$("#startCommand").click(function(){
		$("#startCommand_li").toggle();
		$("#startCommand_input").focus();
	})
	
	// 添加环境变量
	$("#cratePATH").click(function(){
		var addName = $("#Name").val();
		var addValue = $("#Value").val();
		//判断key是否重复，
		var arrayKey = $("#arrayKey").val().split(",");
		for(var i = 0; i<arrayKey.length; i++){
			if(addName == arrayKey[i]){
				layer.tips('环境变量key不能重复','#Name',{tips: [1, '#3595CC']});
				$('#Name').focus();
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
		$("#Path-oper1").append(tr);
		}
		
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
		});
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
	        yes: function(index, layero){
	        	 var arrayKey = $("#arrayKey").val().split(",");
	        	 $.ajax({
	         		url : ctx + "/service/importEnvVariable.do",
	         		type: "POST",
	         		data:{"templateName":templateName},
	         		success : function(data) {
	         			data = eval("(" + data + ")");
	         			var html = "";
	    	            if (data != null) {
	    	                if (data['data'].length > 0) {
	    	                	for (var i in data.data) {
	    	                		var envVariable = data.data[i];
	    	                		html += '<tr>'+
		    	    	    			'<td class="keys"><input type="text" style="width: 98%" value="'+envVariable.envKey+'"></td>'+
		    	    	    			'<td class="vals"><input type="text" style="width: 98%" value="'+envVariable.envValue+'"></td>'+
		    	    	    			'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
		    	    	    			'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="'+envVariable.envKey+'">'+
		    	    	    			'</td>'+
		    	    	    		'</tr>'
		    	    	    		arrayKey.push(envVariable.envKey+",");
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
	
	//另存为模板
	$("#exportBtn").click(function(){
		layer.open({
		 	type:1,
	        title: '另存为模板',
	        content: $("#environment-template"),
	        btn: ['保存', '取消'],
	        yes: function(index, layero){ 
	        	var templateName = $("#envTemplateName").val();
	        	$.ajax({
					url:ctx+"/service/matchTemplateName.do",
					type: "POST",
	         		data:{"templateName":templateName},
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("环境变量模板名称重复");
						}else{
							layer.alert("环境变量模板导入成功");
							$('#templateName').val(templateName);
							layer.close(index);
						}
					}	
	        	});
	        }
		})
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
									"<span class='id h5' title='latest,5.6' value='"+ image.version+"'>版本:"+ image.isBaseImage +
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
	                        	var portConfigs = $(this).attr("portConfigs");
	                        	// containerName();
	                            deploy(imgID,imageName, imageVersion,resourceName,portConfigs);
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

    });

});

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
									""+ image.name +" <a title='点击查看镜像详情' target='_blank' href='"+ctx+"../registry/detail/"+image.id+"'>"+
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

                        	var imageName = $(this).attr("imageName");
                        	var imageVersion = $(this).attr("imageVersion");
                        	var imgID = $(this).attr("imgID");
                        	var resourceName = $(this).attr("resourceName");
                        	var portConfigs = $(this).attr("portConfigs");
                        	// containerName();
                          deploy(imgID,imageName, imageVersion,resourceName,portConfigs);
                        });
                	}
            }
        }
    })
}

function deploy(imgID,imageName, imageVersion,resourceName,portConfigs){
	if ('undefined' != portConfigs) {
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

/**
 * 删除环境变量
 */
// 删除环境变量
function deleteRow(obj){
	var envKey = $(obj).parent().find("input:first").val();
	var arrayKey = $("#arrayKey").val().split(",");
	var newArray = new Array();
	for(var i = 0; i<arrayKey.length; i++){
		if(envKey !=arrayKey[i]){
			newArray.push(arrayKey[i]);
		}
	}
	$("#arrayKey").attr("value",newArray);
	$(obj).parent().parent().remove();
	
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
//删除port
function deletePortRow(obj,int){
	//alert(int);
	 $.ajax({
			url:""+ctx+"/service/removeSet.do?set="+int,
			type: "GET"
	 });
	 $(obj).parent().parent().remove();
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


