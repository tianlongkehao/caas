 $(document).ready(function () {
	$("#serviceReloadBtn").click(function(){
		loadService();
	});
//	$("#serviceCreateBtn").click(function(){
//		$(".contentMain").load("/service/add");
//	});
	
//	loadService();
	$('input[name="chkItem"').click(function(){
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
				//alert(statusStr);
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
	
	_refreshCreateTime(60000);
	
	$("#serviceSearch").click(function(){
		var serviceName = $('#searchName').val();
		$.ajax({
			url:""+ctx+"findservice/"+serviceName,
			success:function(){}
		})
	})

	$(".clusterId").each(function(){
		$(this).click(function(){
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

 function sliderFn(sliderId, max, min, value){

	 if(value == undefined){
		 value = 10;
	 }

	 var sliderObj = $("#"+sliderId).slider({
		 formatter: function(value) {
			 return value;
		 },
		 max:max,
		 min:min,
		 value : value,
		 tooltip:'hide'
	 });

	 sliderObj.on("slide", function(slideEvt) {
		 $("#"+sliderId+'_input').val(slideEvt.value);
	 }).on("change", function(slideEvt){
		 $("#"+sliderId+'_input').val(slideEvt.value.newValue);
	 });

	 $("#"+sliderId+'_input').on("change",function(){
		 var sliderVal = Number($(this).val());
		 //sliderObj.setValue(sliderVal);
		 sliderObj.slider('setValue', sliderVal);
	 });

	 return sliderObj;
 }

 function createContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
		 var $chkItem = $(this);
			 //alert(id);
		 layer.open({
		        title: '启动容器',
		        content: '确定启动容器？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	var cStatusHtml = "<i class='fa_success'></i>"+
	                				  "启动中"+
	                				  "<img src='"+ctx+"images/loading4.gif' alt=''/>";
		        	$('#containerStatus').find(".cStatusColumn").html(cStatusHtml);
		        	layer.close(index);
					$.ajax({
						url:""+ctx+"service/createContainer.do?id="+id,
						success:function(data){
							data = eval("(" + data + ")");
							if(data.status=="200"){
								layer.alert("容器启动成功");
								window.location.reload();
							}else{
								layer.alert("容器启动失败");
							}
						}	
					})
		        }
		 })
	 }) 
 }
 
 
 function stopContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
	        var id = $(el).val();
	        //alert(id);
	        layer.open({
		        title: '停止容器',
		        content: '确定停止容器？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	layer.close(index);
		        						$.ajax({
		        							url:""+ctx+"service/stopContainer.do?id="+id,
		        							success:function(data){
		        								data = eval("(" + data + ")");
		        								if(data.status=="200"){
		        									layer.alert("容器已停止");
		        									window.location.reload();
		        								}else{
		        									layer.alert("容器停止失败，请检查服务器连接");
		        								}
	        		
		        							}
		        						})
		        	
		        }
	        })
	 })
 }
 
 function delContainer(){
	 $('input[name="chkItem"]:checked').each(function(index, el){
		 var id = $(el).val();
		 layer.open({
		        title: '删除容器',
		        content: '确定删除容器？',
		        btn: ['确定', '取消'],
		        yes: function(index, layero){ 
		        	layer.close(index);
		        				$.ajax({
		        					url:""+ctx+"service/delContainer.do?id="+id,
		        					success:function(data){
		        						data = eval("(" + data + ")");
		        						if(data.status=="200"){
		        							layer.alert("容器已删除");
		        							window.location.reload();
		        						}else{
		        							layer.alert("容器删除失败，请检查服务器连接");
		        						}
	        		
		        					}
		        				})
		        }
		 })
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
 					url:""+ctx+"service/modifyServiceNum.do?id="+id+"&addservice="+num,
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
 					url:""+ctx+"service/modifyCPU.do?id="+id+"&cpus="+cpus+"&rams="+rams,
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
		 var imgVersion = $(el).attr('imageversion');
		 $('#upgradeimgName').val(imgName);
		 $('#imgVersionName').val(imgVersion);
		 layer.open({
			 type:1,
			 title: '更改镜像版本',
			 content: $("#versionUpgrade"),
			 btn: ['确定', '取消'],
			 yes: function(index, layero){
				 layer.close(index);
				 var imgVersion1 = $('#imgVersionName').val();
				 $.ajax({
					 url:""+ctx+"service/modifyimgVersion.do?id="+id+"&serviceName="+serviceName+"&imgVersion="+imgVersion1+"&imgName="+imgName,
					 success:function(data){
	 						data = eval("(" + data + ")");
	 						if(data.status=="200"){
	 							layer.alert("更改成功");
	 							window.location.reload();
	 						}else if(data.status=="500"){
	 							layer.alert("请选择需要升级的版本号！");
	 						}else{
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
 

 