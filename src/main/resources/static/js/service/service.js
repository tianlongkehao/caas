 $(document).ready(function () {
	$("#serviceReloadBtn").click(function(){
		loadService();
	});
	
	$(document).on('click','.no-drop',function(){
		  return false;
		});	
	
	_refreshCreateTime(60000);
	
	
	$("#checkallbox").click(function(){
		 if($(this).prop("checked")){
		     $("input[type='checkbox']").prop("checked",true);
		     $('#deleteButton').removeClass('no-drop').addClass('a-live');
		     $('#deleteButtonFa').removeClass('self_a');
		 }
		 else{
		     $("input[type='checkbox']").prop("checked",false);
		     $('#startContainer').removeClass('a-live').addClass('no-drop');
		     $('#startContainerFa').addClass('self_a');
		     $('#stopContainer').removeClass('a-live').addClass('no-drop');
		     $('#stopContainerFa').addClass('self_a');
		     $('#deleteButton').removeClass('a-live').addClass('no-drop');
		     $('#deleteButtonFa').addClass('self_a');
		 }
	});
	
	checkbox();
 });
 
function loadContainers(obj) {
	var serviceID = $(obj).attr("serviceid");
	var aaa = 'tr[serviceidcon = "' + serviceID + '"]'
	if ($(obj).children().eq(1).children("b").attr("rotate") == "show") {
		$(aaa).remove();
		$(obj).children().eq(1).children("b")
				.css("transform", "rotate(-90deg)");
		$(obj).children().eq(1).children("b").attr("rotate", "hide");
	} else {
		$.ajax({
					url : "" + ctx +"/service/findservice.do?serviceID="+serviceID,
					type : "get",
					success : function(data) {
						$(aaa).remove();
						var containersHtml = "";
						var data = eval("(" + data + ")");
						var containerLength = data.containerList.length;
						for (var i = 0; i < containerLength; i++) {
							var containerName = data.containerList[i].containerName;
							var containerStatus = data.containerList[i].containerStatus == 1 ? "未启动" : "运行中";
							var statusClassName = data.containerList[i].containerStatus == 1 ? "fa_stop" : "fa_run";
							var loadingImgShowClass = data.containerList[i].containerStatus == 1 ? "hide" : "hide";
							containersHtml += '<tr class="tr-row" serviceidcon="'
									+ serviceID
									+ '">'
									+ '<td style="width: 5%">&nbsp;</td>'
									+ '<td style="width: 15%;">'
									+ '<a style="margin-left: 19px;" href="javascript:void(0)">'
									+ containerName
									+ '</a>'
									+ '</td>'
									+ '<td colspan="2" style="width: 30%"><i class="'
									+ statusClassName
									+ '"></i>'
									+ containerStatus
									+ '<img src=" '+ctx+'/images/loading4.gif" alt="" class="'
									+ loadingImgShowClass
									+ '"/></td>'
									+ '<td style="width: 24%"></td>'
									+ '<td colspan="2" style="width: 20%"></td>'
									+ '</tr>';
						}
						$(obj).after(containersHtml);

						$(obj).children().eq(1).children("b").css("transform",
								"rotate(0deg)");
						$(obj).children().eq(1).children("b").attr("rotate",
								"show");

					}
				});
	}
}

function createContainer() {
	var serviceIDs = [];
	$('input[name="chkItem"]:checked').each(
			function() {
				var id = $(this).val();
				serviceIDs.push(id);
				var $chkItem = $(this);
				// alert(id);
				layer.open({
					title : '启动服务',
					content : '确定启动服务？',
					btn : [ '确定', '取消' ],
					yes : function(index, layero) {
						var cStatusHtml = "<i class='fa_success'></i>" + "启动中"
								+ "<img src='" + ctx + "/images/loading4.gif' alt=''/>";
						$('#containerStatus').find(".cStatusColumn").html(
								cStatusHtml);
						layer.close(index);
						$.ajax({
							url : "" + ctx
									+ "/service/stratServices.do?serviceIDs="
									+ serviceIDs,
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									layer.alert("服务启动成功");
									window.location.reload();
								} else {
									layer.alert("服务启动失败");
								}
							}
						})
					}
				})
			})
}

function stopContainer() {
	var serviceIDs = [];
	$('input[name="chkItem"]:checked').each(
			function() {
				var id = $(this).val();
				serviceIDs.push(id);
				layer.open({
					title : '停止服务',
					content : '确定停止服务？',
					btn : [ '确定', '取消' ],
					yes : function(index, layero) {
						layer.close(index);
						$.ajax({
							url : "" + ctx+ "/service/stopServices.do?serviceIDs="+ serviceIDs,
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									layer.alert("服务已停止");
									window.location.reload();
								} else {
									layer.alert("服务停止失败，请检查服务器连接");
								}

							}
						})
					}
				})
			})
}

function delContainer() {
	var serviceIDs = [];
	$('input[name="chkItem"]:checked').each(
			function() {
				var containerId = $(this).val();
				serviceIDs.push(containerId);
				layer.open({
					title : '删除服务',
					content : '确定删除服务？',
					btn : [ '确定', '取消' ],
					yes : function(index, layero) {
						layer.close(index);
						$.ajax({
							url : "" + ctx+ "/service/delServices.do?serviceIDs="+ serviceIDs,
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									layer.alert("服务已删除");
									window.location.reload();
								} else {
									layer.alert("服务删除失败，请检查服务器连接");
								}
							}
						})
					}
				})

			})
}

function upGradeContainer() {
	$('input[name="chkItem"]:checked').each(
			function(index, el) {
				var id = $(el).val();
				var containerName = $(el).attr('serviceName');
				$('#upgradeServiceName').val(containerName);
				var nums = $(el).attr('serviceNum');
				$('#numberChange').val(nums);
				var total = 0;
				total = parseInt($('#numberChange').attr('max'))+ parseInt(nums);
				// alert(total);
				$('#numberChange').attr("max", total);
				$('#leftpod').text(total);

				// alert(nums);
				layer.open({
					type : 1,
					title : '弹性伸缩',
					content : $("#upgrade"),
					btn : [ '确定', '取消' ],
					yes : function(index, layero) { // 或者使用btn1
						// 按钮【按钮一】的回调
						layer.close(index);
						var num = $('#numberChange').val();
						// alert(num);
						$.ajax({
							url : "" + ctx + "/service/modifyServiceNum.do?id="+ id + "&addservice=" + num,
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									layer.alert("弹性扩容成功！");
									window.location.reload();
								} else if (data.status == "400") {
									layer.alert("弹性扩容失败，请检查服务器连接");
								} else if (data.status == "300") {
									layer.alert("请填写弹性扩容的数量！");
								}

							}
						})
					},
					cancel : function(index) { // 或者使用btn2
						// 按钮【按钮二】的回调
					}
				});
			})
}

function changeContainerConf() {
	$('input[name="chkItem"]:checked').each(
			function(index, el) {
				var id = $(el).val();
				var containerName = $(el).attr('serviceName');
				// alert(containerName);
				$('#confServiceName').val(containerName);
				var cpu = $(el).attr('confCpu');
				var ram = $(el).attr('confRam');
				$('#confCpu').val(cpu);
				$('#confRamSlider_input').val(ram);
				var totalcpu = 0;
				var totalram = 0;
				totalcpu = parseInt($('#confCpu').attr('max')) + parseInt(cpu);
				// totalram =
				// parseInt($('#confRamSlider_input').attr('left'))+parseInt(ram);
				totalram = '1024';
				$('#confCpu').attr("max", totalcpu);
				// $('#leftcpu').text(totalcpu);
				// $('#leftram').text(totalram);
//				var confRamSlider = sliderFn('confRamSlider', totalram, 0,
//						Number(ram));

				layer.open({
					type : 1,
					title : '更改配置',
					area : [ '500px', '300px' ],
					content : $("#changeConf"),
					btn : [ '确定', '取消' ],
					yes : function(index, layero) { // 或者使用btn1
						// 按钮【按钮一】的回调
						var cpus = $('#confCpu').val();
						var rams = $('#confRamSlider_input').val();
						var leftcpu = $("#leftcpu").html();
						if (parseFloat(cpus) > parseFloat(leftcpu)) {
							layer.tips('cpu剩余不足', "#confCpu", {
								tips : [ 1, '#3595CC' ]
							});
							$("#confCpu").focus();
							return;
						}
						var leftmemory = $("#leftram").html();
						if (parseFloat(rams) > parseFloat(leftmemory)) {
							layer.tips('内存剩余不足', "#confRamSlider_input", {
								tips : [ 1, "#3595CC" ]
							})
							$("#confRamSlider_input").focus();
							return;
						}
						layer.close(index);
						$.ajax({
							url : "" + ctx + "/service/modifyCPU.do?id=" + id+ "&cpus=" + cpus + "&rams=" + rams,
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									layer.alert("更改成功");
									window.location.reload();
								} else {
									layer.alert("更改失败，请检查服务器连接");
								}

							}
						})
					},
					cancel : function(index) { // 或者使用btn2
						// 按钮【按钮二】的回调
					}
				});
			})
}

function versionUpgrade() {
	$('input[name="chkItem"]:checked').each(
			function(index, el) {
				var id = $(el).val();
				var serviceName = $(el).attr('serviceName');
				$('#upgradeVersionServiceName').val(serviceName);
				var imgName = $(el).attr('imagename');
				// 查询镜像版本
				findImageVersion(imgName);
				// var imgVersion = $("#imgVersionName").val();
				var imgVersion = $(el).attr('imageversion');
				$('#upgradeimgName').val(imgName);
				// $('#imgVersionName').val(imgVersion);
				layer.open({
					type : 1,
					title : '升级镜像版本',
					content : $("#versionUpgrade"),
					btn : [ '确定', '取消' ],
					yes : function(index, layero) {
						layer.close(index);
						$('#myModal').modal('show');
						var imgVersion1 = $('#imgVersionName').val();
						$.ajax({
							url : ctx + "/service/modifyimgVersion.do?id=" + id+ "&serviceName=" + serviceName
																			+ "&imgVersion=" + imgVersion1 + "&imgName=" + imgName,
							async:true,		
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									$('#myModal').modal('hide');
									layer.alert("升级完成");
									window.location.reload();
								} else if (data.status == "500") {
									$('#myModal').modal('hide');
									layer.alert("请选择需要升级的版本号！");
								} else {
									$('#myModal').modal('hide');
									layer.alert("请检查配置服务！");
								}

							}
						})
					},
					cancel : function(index) { // 或者使用btn2
						// 按钮【按钮二】的回调
					}
				});

			})
}

// 每一行的启动
$(document).off("click", ".startContainer_a").on("click", ".startContainer_a",
		function(e) {
			e.stopPropagation();
			// createContainer()
		});

// 响应每一行的启动按钮
function oneStartContainer(id, status) {
	if (3 == status) {
		return;
	}
	$('#' + id + '_stop').addClass('a-live');
	var serviceIDs = [];
	serviceIDs.push(id);
	var cStatusHtml = "<i class='fa_success'></i>" + "启动中" + "<img src='"+ctx+"/images/loading4.gif' alt=''/>";
	$('#containerStatus').find(".cStatusColumn").html(cStatusHtml);
	
	$.ajax({
		url : "" + ctx + "/service/stratServices.do?serviceIDs=" + serviceIDs,
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.status == "200") {
				layer.alert("服务启动成功");
				window.location.reload();
			} else {
				layer.alert("服务启动失败");
			}
		}
	})
	
}

// 响应每一行上的停止按钮
function oneStopContainer(id, status) {
	if (4 == status) {
		return;
	}
	var serviceIDs = [];
	serviceIDs.push(id);
	layer.open({
		title : '停止服务',
		content : '确定停止服务？',
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			layer.close(index);
			$.ajax({
				url : "" + ctx + "/service/stopServices.do?serviceIDs="+ serviceIDs,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.alert("服务已停止");
						window.location.reload();
					} else {
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
		title : '删除服务',
		content : '确定删除服务？',
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			layer.close(index);
			$.ajax({
				url : "" + ctx + "/service/delServices.do?serviceIDs="+ serviceIDs,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.alert("服务已删除");
						window.location.reload();
					} else {
						layer.alert("服务删除失败，请检查服务器连接");
					}

				}
			})
		}
	})
}

// 响应每一行上的弹性伸缩
function oneUpGradeContainer(id,containerName,nums,cpu,ram) {
	 $('#upgradeServiceName').val(containerName);
	 $('#numberChange').val(nums);
	 
	 var leftcpu = $("#leftcpu").html();
	 var leftram = $("#leftram").html();
	 
	 var maxcpu = parseInt(leftcpu)/parseInt(cpu);
	 var maxram = parseInt(leftram)/parseInt(ram);
	 
	 var total = 0;
	 if (parseInt(maxcpu) > parseInt(maxram)) {
		 total = maxram;
	 } else {
		 total = maxcpu;
	 }
	 
	 total += parseInt(nums);
	 $('#numberChange').attr("max",parseInt(total));
	 $('#leftpod').text(total);

	 layer.open({
		 type:1,
		 title: '弹性伸缩',
		 content: $("#upgrade"),
		 btn: ['确定', '取消'],
		 yes: function(index, layero){ //或者使用btn1
		 	//按钮【按钮一】的回调
			 var num = $('#numberChange').val();
			 if (parseInt(num) > parseInt(total)) {
				 layer.tips('租户资源不足',"#numberChange",{tips: [1, '#3595CC']});
			     $("#numberChange").focus();
				 return;
			 }
			 
			 layer.close(index);
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
}

// 响应每一行上的版本升级
function oneVersionUpgrade(id,serviceName,imgName,obj) {
		 $('#upgradeVersionServiceName').val(serviceName);
		 //查询镜像版本
		 findImageVersion(imgName);
		 $('#upgradeimgName').val(imgName);
		 layer.open({
			 type:1,
			 title: '升级镜像版本',
			 content: $("#versionUpgrade"),
			 btn: ['确定', '取消'],
			 yes: function(index, layero){
		        var cStatusHtml = "<i class='fa_success'></i>"+
	                				"升级中"+
	                				"<img src='"+ctx+"/images/loading4.gif' alt=''/>";
		        $("#"+id+"_upgradeCluster").parent().parent().find(".cStatusColumn").html(cStatusHtml);
				 layer.close(index);
				 var imgVersion1 = $('#imgVersionName').val();
				 $.ajax({
					 url:ctx+"/service/modifyimgVersion.do?id="+id+"&serviceName="+serviceName+"&imgVersion=" +imgVersion1 +"&imgName="+imgName,
					 success:function(data){
	 						data = eval("(" + data + ")");
	 						if(data.status=="200"){
	 							//layer.alert("升级完成");
	 						}else if(data.status=="500"){
	 							 //$('#myModal').modal('hide');
	 							layer.alert("请选择需要升级的版本号！");
	 						}else{
	 							 //$('#myModal').modal('hide');
	 							layer.alert("请检查配置服务！");
	 						}
	 						window.location.reload();
	 					}
				 })
			 },
			 cancel: function(index){ //或者使用btn2
				 //按钮【按钮二】的回调
			 }
		 });
}

// 响应每一行上的修改配置
function oneChangeContainerConf(id,containerName,instanceNum,cpu,ram,status) {
	 $('#confServiceName').val(containerName);
	 $('#confCpu').val(cpu);
	 $('#confRamSlider_input').val(ram);
	 var totalcpu = 0;
	 var totalram = 0;
	 totalcpu = parseInt($('#confCpu').attr('max'))+parseFloat(cpu);
	 totalram = '1024';
	 $('#confCpu').attr("max",totalcpu);
	 //var confRamSlider = sliderFn('confRamSlider', totalram,0, Number(ram));

	 layer.open({
		 type:1,
		 title: '更改配置',
		 area: ['500px', '300px'],
		 content: $("#changeConf"),
		 btn: ['确定', '取消'],
		 yes: function(index, layero){ //或者使用btn1
			 //按钮【按钮一】的回调
			 var cpus = $('#confCpu').val();
			 var rams = $('#confRamSlider_input').val();
			 var leftcpu = $("#leftcpu").html();
			 var leftmemory = $("#leftram").html();
			 //服务在运行中
			 if (status == 3) {
				 
				 if ((parseFloat(cpus)*parseFloat(instanceNum)) > ( parseFloat(leftcpu) + parseFloat(cpu)*parseFloat(instanceNum))) {
					 layer.tips('cpu剩余不足,pod数量为'+instanceNum,"#confCpu",{tips: [1, '#3595CC']});
					 $("#confCpu").focus();
					 return;
				 }
			 
				 if ((parseFloat(rams)*parseFloat(instanceNum)) > (parseFloat(leftmemory) + parseFloat(ram)*parseFloat(instanceNum) )) {
					 layer.tips('内存剩余不足',"#confRamSlider_input",{tips: [1,"#3595CC"]})
					 $("#confRamSlider_input").focus();
					 return;
				 }
			 } else {
				 if ((parseFloat(cpus)*parseFloat(instanceNum)) > parseFloat(leftcpu)) {
					 layer.tips('cpu剩余不足,pod数量为'+instanceNum,"#confCpu",{tips: [1, '#3595CC']});
					 $("#confCpu").focus();
					 return;
				 }
				 
				 if ((parseFloat(rams)*parseFloat(instanceNum)) > parseFloat(leftmemory)) {
					 layer.tips('内存剩余不足',"#confRamSlider_input",{tips: [1,"#3595CC"]})
					 $("#confRamSlider_input").focus();
					 return;
				 }
			 }
			 
			 
			 layer.close(index);
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
}

function refresh() {
	window.location.reload();// 刷新当前页面.
}

function checkbox() {
	
	$('input[name="chkItem"]').click(
			function() {
				if ($(this).prop("checked")) {
					$('#startContainer').removeClass('no-drop').addClass('a-live');
					$('#startContainerFa').removeClass('self_a');
					$('#stopContainer').removeClass('no-drop').addClass('a-live');
					$('#stopContainerFa').removeClass('self_a');
					$('#deleteButton').removeClass('no-drop').addClass('a-live');
					$('#deleteButtonFa').removeClass('self_a');
					var status = [];
					$('.clusterId').find("input[name='chkItem']").each(
							function(el) {
								if ($(this).is(':checked')) {
									status.push($(this).attr('status'));
								}
							});
					var statusStr = status.toString();
					var runIndex = statusStr.indexOf('3');
					var stopIndex = statusStr.indexOf('4');
					var noIndex = statusStr.indexOf('1');
					if (statusStr.indexOf('Initialization') > -1) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
					}
					if (runIndex > -1 && stopIndex < 0 && noIndex < 0) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
					}
					if (runIndex < 0 && (stopIndex > -1 || noIndex > -1)) {
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
					}
					if (runIndex > -1 && (stopIndex > -1 || noIndex > -1)) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
					}
				} else {
					$('#startContainer').removeClass('a-live').addClass('no-drop');
					$('#startContainerFa').removeClass('self_a');
					$('#stopContainer').removeClass('a-live').addClass('no-drop');
					$('#stopContainerFa').removeClass('self_a');
					$('#deleteButton').removeClass('a-live').addClass('no-drop');
					$('#deleteButtonFa').removeClass('self_a');
					
					var status = [];
					$('.clusterId').find("input[name='chkItem']").each(
							function(el) {
								if ($(this).is(':checked')) {
									status.push($(this).attr('status'));
								}
							});
					var statusStr = status.toString();
					var noIndex = statusStr.indexOf('1');
					var runIndex = statusStr.indexOf('3');
					var stopIndex = statusStr.indexOf('4');
					if (statusStr.indexOf('Initialization') > -1) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
					}
					if (runIndex > -1 && stopIndex < 0 && noIndex <0) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
					}
					if (runIndex < 0 && (stopIndex > -1 || noIndex > -1)) {
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
					}
					if (runIndex > -1 && (stopIndex > -1 || noIndex > -1 )) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
					}
					if (status.length == 0) {
						$('#startContainer').removeClass('a-live').addClass('no-drop');
						$('#startContainerFa').addClass('self_a');
						$('#stopContainer').removeClass('a-live').addClass('no-drop');
						$('#stopContainerFa').addClass('self_a');
						$('#deleteButton').removeClass('a-live').addClass('no-drop');
						$('#deleteButtonFa').addClass('self_a');
					}
				}
			})
}

// Refresh create time
function _refreshCreateTime(interval) {
	setInterval(function() {
		$('.tdTimeStrap').each(function() {
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

function refresh1(id) {

	var url = "service/" + Math.random();
	// create random number
	setTimeout(function() {
		$("#inst_" + id).load(url + id, "");
	}, 500); // wait one second to run function

}

function findImageVersion(imageName) {
	$.ajax({
		url : ctx + "/service/findImageVersion.do",
		type : "post",
		async:false,
		data : {
			"imageName" : imageName
		},
		success : function(data) {
			data = eval("(" + data + ")");
			var html = "";
			if (data != null) {
				if (data['data'].length > 0) {
					for ( var i in data.data) {
						var image = data.data[i];
						html += "<option type='text' value='" + image.version+ "'>" + image.version + "</option>"
					}
				}
			}
			$("#imgVersionName").html(html);
		}
	})
}
