$(document).ready(function(){
	loadImageList();

	getServiceStorageVol();
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
	    
	    var nginxstr = "{";
	    $('input[name="nginxserv"]:checked').each(function(){
    		var servname = $(this).val();
    		var servid = $(this).attr('id');
    		nginxstr = nginxstr+"'"+servid+"'"+":"+"'"+servname+"',";
    	})
    	nginxstr = nginxstr.substring(0,nginxstr.length-1) +"}";
    	// var nginxObj = eval('(' + nginxstr + ')');
    	if(nginxstr == "}"){
    		nginxstr = "{}";
    	}
<<<<<<< HEAD
    	$('#nginxZone').val(nginxstr);
	    // var cpuNum = $('#cpuNum').val();
	    /*
		 * if(!cpuNum || cpuNum.length < 1){
		 * layer.tips('cpu数量不能为空','#cpuNum',{tips: [1, '#3595CC']});
		 * $('#cpuNum').focus(); return; }
		 */
=======
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
    	
	    //var cpuNum = $('#cpuNum').val();
	    /*if(!cpuNum || cpuNum.length < 1){
		      layer.tips('cpu数量不能为空','#cpuNum',{tips: [1, '#3595CC']});
		      $('#cpuNum').focus();
		      return;
		    }*/
>>>>>>> branch 'develop' of https://git.oschina.net/llizhuping/EPM_PAAS_CLOUD.git



<<<<<<< HEAD
	    // var ram = $('#ram').val();
	    /*
		 * if(!ram || ram < 1){ layer.tips('内存不能为零','#ram',{tips: [1,
		 * '#3595CC']}); $('#ram').focus(); return; }
		 */

		containerName();
=======
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
        
>>>>>>> branch 'develop' of https://git.oschina.net/llizhuping/EPM_PAAS_CLOUD.git
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
		if(addName != "" && addValue != ""){
			var tr = '<tr>'+
			'<td class="keys"><input type="text" style="width: 98%" value="'+addName+'"></td>'+
			'<td class="vals"><input type="text" style="width: 98%" value="'+addValue+'"></td>'+
			'<td class="func"><a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">'+
			'<i class="fa fa-trash-o fa-lg"></i></a><input type="hidden" class="oldValue" value="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin">'+
			'</td>'+
		'</tr>'
		$("#Path-oper1").append(tr);
		}
		
	});
	
	// 添加端口
	$("#createPort").click(function(){
		var portTr = '<tr class="plus-row">'+
					  '<td><input class="port" type="text"></td>'+
					  '<td><select class="T-http">'+
					  '<option>TCP</option>'+
					  '<option>HTTP</option>'+
					  '</select></td>'+
					  '<td><i>'+'动态生成'+'</i></td>'+
					  '<td><a href="javascript:void(0)" onclick="deletePortRow(this)" class="gray">'+
					  '<i class="fa fa-trash-o fa-lg"></i>'+
					  '</a></td></tr>';
		$("#pushPrptpcol").append(portTr);
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
									""+ image.version +"</span> <span imgID='"+image.id+"' resourceName='"+image.resourceName+"' imageName='"+image.name+"' imageVersion='"+image.version+"' class='pull-deploy btn btn-primary'"+
									"data-attr='tenxcloud/mysql'> 部署 <i"+
									"class='fa fa-arrow-circle-o-right margin fa-lg'></i>"+
								"</span>"+
							"</div>"+
					"</span></li>";
	                    	}
	                    	$("#imageList").html(html);

	                        $(".pull-deploy").click(function(){

	                        	var imageName = $(this).attr("imageName");
	                        	var imageVersion = $(this).attr("imageVersion");
	                        	var imgID = $(this).attr("imgID");
	                        	var resourceName = $(this).attr("resourceName");
	                        	// containerName();
	                            deploy(imgID,imageName, imageVersion,resourceName);
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
								""+ image.version +"</span> <span imgID='"+image.id+"'resourceName='"+image.resourceName+"'  imageName='"+image.name+"' imageVersion='"+image.version+"' class='pull-deploy btn btn-primary'"+
								"data-attr='tenxcloud/mysql'> 部署 <i"+
								"class='fa fa-arrow-circle-o-right margin fa-lg'></i>"+
							"</span>"+
						"</div>"+
				"</span></li>";
                    	}
                    	$("#imageList").html(html);

                        $(".pull-deploy").click(function(){

                        	var imageName = $(this).attr("imageName");
                        	var imageVersion = $(this).attr("imageVersion");
                        	var imgID = $(this).attr("imgID");
                        	var resourceName = $(this).attr("resourceName");
                        	// containerName();
                            deploy(imgID,imageName, imageVersion,resourceName);
                        });
                	}
            }
        }
    })
}

function deploy(imgID,imageName, imageVersion,resourceName){

    $("#imgName").val(imageName);
    $("#imgVersion").val(imageVersion);
    $("#imgID").val(imgID);
    $("#resourceName").val(resourceName);
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
	$(obj).parent().parent().remove();
	
}
//删除port
function deletePortRow(obj){
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


