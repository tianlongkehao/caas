 $(document).ready(function () {
	$("#serviceReloadBtn").click(function(){
		loadService();
	});
	
	$(document).on('click','.no-drop',function(){
		  return false;
		});
	
	

	_refreshCreateTime(60000);
	
	checkbox();
	
	
//	$("#serviceSearch").click(function(){
//		var serviceName = $('#searchName').val();
//		$.ajax({
//			url:ctx+"/service/findservice.do?servName="+serviceName,
//			success:function(data){
//				//data = eval("(" + data + ")");
//				data = $.parseJSON(data);
//				if(data.status=="200"){
//	            	var html="";
//	            	var htmlpod="";
//	            	if(data.serviceList.length>0){
//	            		for(var i in data.serviceList){
//	            			var service = data.serviceList[i];
//	            			var StatusHtml = "";
//	            			var btnStyleHtml = "";
//	            			if(service.status = 1){
//	            				StatusHtml = "<i class='fa_run'></i><span style='color: #65BC2C'>运行中 </span>";
//	            			}else if(service.status = 2){
//	            				StatusHtml = "<i class='fa_createing'></i><span style='color: #FFA000'>等待中 </span>";
//	            			}else if(service.status = 3){
//	            				StatusHtml = "<i class='fa_success'></i><span style='color: #5E99E1'>创建中 <img class='margin' src='/images/loading4.gif'></span>";
//	            			}else if(service.status = 4){
//	            				StatusHtml = "<i class='fa_stop'></i><span style='color: #DF582C'>已停止 </span>";
//	            			}else if(service.status = 5){
//	            				StatusHtml = "<i class='fa_stop'></i><span style='color: #DF582C'>创建失败 </span>";
//	            			}else if(service.status = 6){
//	            				StatusHtml = "<i class='fa fa-warning'></i><span>服务异常 </span>";
//	            			}
//	            			html += "<tr class='clusterId' containerName="+service.serviceName+">"+
//		"<td style='width: 5%; text-indent: 30px;'><input type='checkbox'"+
//			"name='chkItem' status='' imagename='' imagetag='' value='"+service.id+"'" +
//					" serviceName='"+service.serviceName +"' serviceNum='"+service.instanceNum +"' confRam='"+service.ram +
//							" status='"+service.status+"' imagename='"+service.imgName +"' imageversion='"+service.imgVersion +
//									" confCpu='"+service.cpuNum+ "'/></td>"+
//		" <td style='width: 20%; white-space: nowrap;'><b"+
//			"class='caret margin' style='transform: rotate(0deg);'></b> <a"+
//			"href='/service/detail/'"+service.id+
//			" class='cluster_mirrer_name'>"+ service.serviceName +"</a> <span class='number-node'>"+ service.instanceNum +"</span>"+
//			"<span class='margin cursor console-code-modal'"+
//			"data-id='#console-code-modal'> <i class='fa fa-desktop'"+
//				"onclick=''></i>"+
//		"</span></td>"+
//		"<td style='width: 10%' id='mysqlstatus'>"+ StatusHtml +
//		"</td><td style='width: 20%;'><img"+
//			"src=''"+
//			"style='max-height: 20px; max-width: 40px;'> <span"+
//			"class='cluster_mirrer'> <a title='点击查看镜像' target='_blank'"+
//				"href=''>"+ service.imgName +"</a>"+
//		"</span></td>"+
//		"<td style='width: 34%' id='mysqlurl'><span class='url'> <a"+
//				"href='' target='_blank'>bonc</a>"+
//		"</span></td>"+
//		"<td style='width: 10%' class='tdTimeStrap'><input type='hidden'"+
//			"class='timeStrap' value=''> <i"+
//			"class='fa_time'></i> <span>"+ service.createDate + "</span></td>"+
//	"</tr>";
//	if(data.containerList.length>0){
//		for(var j in data.containerList){
//			var container = data.containerList[i];
//			if(container.serviceid == service.id){
//				htmlpod += "<tr style='border-left: 1px solid #eee;'>"+
//				"<td colspan='8'><div class='align-center'>"+
//						"<table class='table'>"+
//							"<thead style='background: #FAFAFA; border-top: 1px solid #EDECEC;'>"+
//								"<tr class='tr-row'>"+
//									"<td style='width: 5%'>&nbsp;</td>"+
//									"<td style='width: 20%;'><a style='margin-left: 19px;'"+
//										"href=''>"+ container.containerName +"</a>"+
//									"</td>"+
//									"<td colspan='2' style='width: 30%'>"+ StatusHtml +
//									"<td style='width: 10%'><i class='fa_time'></i>" +service.createDate+ "</td>"+
//								"</tr>"+
//							"</thead>"+
//						"</table>"+
//					"</div></td>"+
//			"</tr>";
//			}
//		}
//		
//	}
//	   
//	   html = html + htmlpod;
//	            		}
//	            	}
//	            	$("#serviceList").html(html);
//	            }else{
//	            	alert(data.msg);
//	            }
//			}
//		})
//	})


	$(".clusterId").each(function(){
		$(this).click(function(e){
			$(":checkbox").click(function(e){
				e.stopPropagation();
			});
			
			if($(this).next("tr").hasClass("hide")){
				$(this).next("tr").removeClass("hide");
				$(this).children().eq(1).children("b").css("transform","rotate(0deg)");
			}else{
				$(this).next("tr").addClass("hide");
				$(this).children().eq(1).children("b").css("transform","rotate(-90deg)");
			}

		});
	});

 });
 
//check option
 

 // 选择内存滑动
 //$(function(){
	// //sliderFn("confRamSlider", 2024, 0);
 //});

 function createContainer(){
	 var serviceIDs = [];
	
	 $('input[name="chkItem"]:checked').each(function(){
		 
		 var id = $(this).val();
		 serviceIDs.push(id);
		 var $chkItem = $(this);
			 //alert(id);
		 layer.open({
		        title: '启动服务',
		        content: '确定启动服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	var cStatusHtml = "<i class='fa_success'></i>"+
	                				  "启动中"+
	                				  "<img src='"+ctx+"/images/loading4.gif' alt=''/>";
		        	$('#containerStatus').find(".cStatusColumn").html(cStatusHtml);
		        	layer.close(index);
					$.ajax({
						url:""+ctx+"/service/stratServices.do?serviceIDs="+serviceIDs,
						success:function(data){
							data = eval("(" + data + ")");
							if(data.status=="200"){
								layer.alert("服务启动成功");
								window.location.reload();
							}else{
								layer.alert("服务启动失败");
							}
						}	
					})
		        }
		 })
	 }) 
 }
 
 function stopContainer(){
	 var serviceIDs = [];
	 $('input[name="chkItem"]:checked').each(function(){
	        var id = $(this).val();
	        serviceIDs.push(id);
	        layer.open({
		        title: '停止服务',
		        content: '确定停止服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	layer.close(index);
		        						$.ajax({
		        							url:""+ctx+"/service/stopServices.do?serviceIDs="+serviceIDs,
		        							success:function(data){
		        								data = eval("(" + data + ")");
		        								if(data.status=="200"){
		        									layer.alert("服务已停止");
		        									window.location.reload();
		        								}else{
		        									layer.alert("服务停止失败，请检查服务器连接");
		        								}
	        		
		        							}
		        						})
		        	
		        }
	        })
	 })
 }
 
 function delContainer(){
	 var serviceIDs = [];
	 $('input[name="chkItem"]:checked').each(function(){
	      var containerId = $(this).val();
	      serviceIDs.push(containerId);
		 layer.open({
		        title: '删除服务',
		        content: '确定删除服务？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	layer.close(index);
		        				$.ajax({
		        					url:""+ctx+"/service/delServices.do?serviceIDs="+serviceIDs,
		        					success:function(data){
		        						data = eval("(" + data + ")");
		        						if(data.status=="200"){
		        							layer.alert("服务已删除");
		        							window.location.reload();
		        						}else{
		        							layer.alert("服务删除失败，请检查服务器连接");
		        						}
	        		
		        					}
		        				})
		        }
		 })
		 
	 })
	 
 }
 
 
 
 // 每一行的启动
 $(document).off("click",".startContainer_a").on("click",".startContainer_a",function(e){
	 e.stopPropagation();
	 //createContainer()
 });
 
 //响应每一行的启动按钮
 function oneStartContainer(id,status) {
	 if (3 == status) {
		 return;
	 }
	 $('#'+id+'_stop').addClass('a-live');
	 var serviceIDs = [];
	 serviceIDs.push(id);
	 layer.open({
	        title: '启动服务',
	        content: '确定启动服务？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	var cStatusHtml = "<i class='fa_success'></i>"+
                				  "启动中"+
                				  "<img src='"+ctx+"/images/loading4.gif' alt=''/>";
	        	$('#containerStatus').find(".cStatusColumn").html(cStatusHtml);
	        	layer.close(index);
				$.ajax({
					url:""+ctx+"/service/stratServices.do?serviceIDs="+serviceIDs,
					success:function(data){
						data = eval("(" + data + ")");
						if(data.status=="200"){
							layer.alert("服务启动成功");
							window.location.reload();
						}else{
							layer.alert("服务启动失败");
						}
					}	
				})
	        }
	 })
 }
 
 // 响应每一行上的停止按钮
 function oneStopContainer(id,status) {
	 if (4 == status) {
		 return;
	 }
	 var serviceIDs = [];
     serviceIDs.push(id);
     layer.open({
	        title: '停止服务',
	        content: '确定停止服务？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	layer.close(index);
	        						$.ajax({
	        							url:""+ctx+"/service/stopServices.do?serviceIDs="+serviceIDs,
	        							success:function(data){
	        								data = eval("(" + data + ")");
	        								if(data.status=="200"){
	        									layer.alert("服务已停止");
	        									window.location.reload();
	        								}else{
	        									layer.alert("服务停止失败，请检查服务器连接");
	        								}
     		
	        							}
	        						})
	        	
	        }
     })
 }
 
 // 响应每一行上的删除按钮
 function oneDeleteContainer(id) {
	  var serviceIDs = [];
     serviceIDs.push(id);
	  layer.open({
	        title: '删除服务',
	        content: '确定删除服务？',
	        btn: ['确定', '取消'],
	        yes: function(index, layero){ 
	        	layer.close(index);
	        				$.ajax({
	        					url:""+ctx+"/service/delServices.do?serviceIDs="+serviceIDs,
	        					success:function(data){
	        						data = eval("(" + data + ")");
	        						if(data.status=="200"){
	        							layer.alert("服务已删除");
	        							window.location.reload();
	        						}else{
	        							layer.alert("服务删除失败，请检查服务器连接");
	        						}
       		
	        					}
	        				})
	        }
	 })
 }
 
 
 function upGradeContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
		 var containerName = $(el).attr('serviceName');
		 $('#upgradeServiceName').val(containerName);
		 var nums = $(el).attr('serviceNum');
		 $('#numberChange').val(nums);
		 var total = 0;
		 total = parseInt($('#numberChange').attr('max'))+parseInt(nums);
		 //alert(total);
		 $('#numberChange').attr("max",total);
		 $('#leftpod').text(total);
		 
		 
		 //alert(nums);
		 layer.open({
			 type:1,
			 title: '弹性伸缩',
			 content: $("#upgrade"),
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
			 	//按钮【按钮一】的回调
				 layer.close(index);
				 var num = $('#numberChange').val();
				// alert(num);
				 $.ajax({
 					url:""+ctx+"/service/modifyServiceNum.do?id="+id+"&addservice="+num,
 					success:function(data){
 						data = eval("(" + data + ")");
 						if(data.status=="200"){
 							layer.alert("弹性扩容成功！");
 							window.location.reload();
 						}else if(data.status=="400"){
 							layer.alert("弹性扩容失败，请检查服务器连接");
 						}else if(data.status=="300"){
 							layer.alert("请填写弹性扩容的数量！");
 						}
		
 					}
 				})
		 	 },
			 cancel: function(index){ //或者使用btn2
			 	//按钮【按钮二】的回调
		 	 }
		 });
	 })
 }

 function changeContainerConf(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
		 var containerName = $(el).attr('serviceName');
		 //alert(containerName);
		 $('#confServiceName').val(containerName);
		 var cpu = $(el).attr('confCpu');
		 var ram = $(el).attr('confRam');
		 $('#confCpu').val(cpu);
		 $('#confRamSlider_input').val(ram);
		 var totalcpu = 0;
		 var totalram = 0;
		 totalcpu = parseInt($('#confCpu').attr('max'))+parseInt(cpu);
//		 totalram = parseInt($('#confRamSlider_input').attr('left'))+parseInt(ram);
		 totalram = '1024';
		 $('#confCpu').attr("max",totalcpu);
//		 $('#leftcpu').text(totalcpu);
//		 $('#leftram').text(totalram);
		 var confRamSlider = sliderFn('confRamSlider', totalram,0, Number(ram));

		 layer.open({
			 type:1,
			 title: '更改配置',
			 area: ['500px', '300px'],
			 content: $("#changeConf"),
			 btn: ['确定', '取消'],
			 yes: function(index, layero){ //或者使用btn1
				 //按钮【按钮一】的回调
				 layer.close(index);
				 var cpus = $('#confCpu').val();
				 var rams = $('#confRamSlider_input').val();
				 $.ajax({
 					url:""+ctx+"/service/modifyCPU.do?id="+id+"&cpus="+cpus+"&rams="+rams,
 					success:function(data){
 						data = eval("(" + data + ")");
 						if(data.status=="200"){
 							layer.alert("更改成功");
 							window.location.reload();
 						}else{
 							layer.alert("更改失败，请检查服务器连接");
 						}
		
 					}
 				})
			 },
			 cancel: function(index){ //或者使用btn2
				 //按钮【按钮二】的回调
			 }
		 });
	 })
 }

 function refresh(){
	 window.location.reload();//刷新当前页面.
 }
 
 function checkbox(){
	 $('input[name="chkItem"]').click(function(){
			if($(this).prop("checked")){
				$('#startContainer').removeClass('no-drop').addClass('a-live');
				$('#stopContainer').removeClass('no-drop').addClass('a-live');
				$('#scaleCluster').removeClass('no-drop').addClass('a-live');
				$('#upgradeCluster').removeClass('no-drop').addClass('a-live');
				$('#redeployContainer').removeClass('no-drop').addClass('a-live');
				$('#changeConfiguration').removeClass('no-drop').addClass('a-live');
				$('#deleteButton').removeClass('no-drop').addClass('a-live');
				var status = [];
				$('.clusterId').find("input[name='chkItem']").each(function(el) {
					if($(this).is(':checked')){
						status.push($(this).attr('status'));
					}
				});
				var statusStr = status.toString();
				var runIndex = statusStr.indexOf('3');
				var stopIndex = statusStr.indexOf('4');
				if(status.length > 1){
					$('#changeConfiguration').removeClass('a-live').addClass('no-drop');
					$('#scaleCluster').removeClass('a-live').addClass('no-drop');
					$('#upgradeCluster').removeClass('a-live').addClass('no-drop');
				}
				if(statusStr.indexOf('Initialization') > -1){
					$('#startContainer').removeClass('a-live').addClass('no-drop');
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
					$('#changeConfiguration').removeClass('a-live').addClass('no-drop');
					$('#scaleCluster').removeClass('a-live').addClass('no-drop');
					$('#upgradeCluster').removeClass('a-live').addClass('no-drop');
				}
				if(runIndex > -1 && stopIndex < 0){
					$('#startContainer').removeClass('a-live').addClass('no-drop');
				}
				if(runIndex < 0 && stopIndex > -1){
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
				}
				if(runIndex > -1 && stopIndex > -1){
					$('#startContainer').removeClass('a-live').addClass('no-drop');
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
				}
			} else {
				$('#startContainer').removeClass('a-live').addClass('no-drop');
				$('#stopContainer').removeClass('a-live').addClass('no-drop');
				$('#scaleCluster').removeClass('a-live').addClass('no-drop');
				$('#upgradeCluster').removeClass('a-live').addClass('no-drop');
				$('#redeployContainer').removeClass('a-live').addClass('no-drop');
				$('#changeConfiguration').removeClass('a-live').addClass('no-drop');
				$('#deleteButton').removeClass('a-live').addClass('no-drop');
			}
	
	})
 }
 // Refresh create time
 function _refreshCreateTime(interval){
   setInterval(function(){
     $('.tdTimeStrap').each(function(){
       var self = this;
       var timebefore = $(self).find('span').text();
       if (timebefore.indexOf('秒') > -1 || timebefore.indexOf('分钟') > -1) {
         moment.locale('zh-cn');
         var creationTimestamp = $(self).find('.timeStrap').val();
         var timeFromNow = moment(creationTimestamp).fromNow();
         $(self).find('span').text(timeFromNow);
       }
     });
   }, interval);
 }
 
 function refresh1(id){
	
	var url = "service/" + Math.random();
	//create random number
	setTimeout(function() {
	$("#inst_"+id).load(url+id,"");
		  }, 500); //wait one second to run function
	
 }
 
 function versionUpgrade(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
		 var serviceName = $(el).attr('serviceName');
		 $('#upgradeVersionServiceName').val(serviceName);
		 var imgName = $(el).attr('imagename');
		 //查询镜像版本
		 findImageVersion(imgName);
//		 var imgVersion = $("#imgVersionName").val();
		 var imgVersion = $(el).attr('imageversion');
		 $('#upgradeimgName').val(imgName);
//		 $('#imgVersionName').val(imgVersion);
		 layer.open({
			 type:1,
			 title: '升级镜像版本',
			 content: $("#versionUpgrade"),
			 btn: ['确定', '取消'],
			 yes: function(index, layero){
				 layer.close(index);
				 $('#myModal').modal('show');
				 var imgVersion1 = $('#imgVersionName').val();
				 $.ajax({
					 url:ctx+"/service/modifyimgVersion.do?id="+id+"&serviceName="+serviceName+"&imgVersion="+imgVersion1+"&imgName="+imgName,
					 success:function(data){
	 						data = eval("(" + data + ")");
	 						if(data.status=="200"){
	 							$('#myModal').modal('hide');
	 							layer.alert("升级完成");
	 							window.location.reload();
	 						}else if(data.status=="500"){
	 							 $('#myModal').modal('hide');
	 							layer.alert("请选择需要升级的版本号！");
	 						}else{
	 							 $('#myModal').modal('hide');
	 							layer.alert("请检查配置服务！");
	 						}
			
	 					}
				 })
			 },
			 cancel: function(index){ //或者使用btn2
				 //按钮【按钮二】的回调
			 }
		 });
		 
	 })
 }
 
function findImageVersion(imageName){
	$.ajax({
		url:ctx+"/service/findImageVersion.do",
		type:"post",
		data:{"imageName":imageName}, 
		success: function (data) {
            data = eval("(" + data + ")");
            var html = "";
            if (data != null) {
            	if (data['data'].length > 0) {
            		for (var i in data.data) {
            			var image = data.data[i];
            			html += "<option type='text' value='"+image.version+"'>"+image.version+"</option>"
            		}
            	}
            }
            $("#imgVersionName").html(html);    
		}
	})
}
 