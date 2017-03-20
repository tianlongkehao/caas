 $(document).ready(function () {
	loadServices();
	$("#serviceReloadBtn").click(function(){
		window.location.reload();
	});


	document.getElementById('ExportBtn').onclick = function() {
		var serviceSearchCreator = $("#serviceSearchCreator").val();
		var serviceSearchImage = $("#serviceSearchImage").val();
		var serviceSearchName = $("#serviceSearchName").val();
		location.href = ctx + "/service/exportExcel.do?searchService=" + serviceSearchName + "&searchImage=" + serviceSearchImage + "&searchCreatorName=" + serviceSearchCreator;
	};



	$(document).on('click','.no-drop',function(){
		  return false;
	});

	_refreshCreateTime(60000);
	 $(".dataTables-example tbody").on("click","tr",function(){
		 var table = $('.dataTables-example').DataTable();
		 var serviceId = table.row( this ).data().id;
		 loadContainers(this,serviceId);
	 });

	$("#checkallbox").click(function(){
		 if($(this).prop("checked")){
		     $("input[type='checkbox']").prop("checked",true);
		     $('#deleteButton').removeClass('no-drop').addClass('a-live');
		     $('#deleteButtonFa').removeClass('self_a');
		     $('#ExportBtn').removeClass('no-drop').addClass('a-live');
		     $('#ExportButtonFa').removeClass('self_a');
		     var containersStatus = [];
		     var res = [];
		     $('input[name="chkItem"]:checked').each(function(){
		    	 var containerStatus = $(this).attr("status");
		    	 containersStatus.push(containerStatus);
		     });
		     containersStatus.sort();
		     for(var i=0; i<containersStatus.length;){
		    	 var count = 0;
		    	 for(var j=0; j<containersStatus.length; j++){
		    		 if(containersStatus[i] == containersStatus[j]){
		    			 count++;
		    		 }
		    	 }
		    	 res.push([containersStatus[i],count]);
		    	 i+=count;
		     }
		     //alert(res.length+"res[0][0]"+res[0][0])
		     if(res.length == 1){
		    	 if(res[0][0] == 3){
		    		 $('#stopContainer').removeClass('no-drop').addClass('a-live');
				     $('#stopContainerFa').removeClass('self_a');
		    	 }
		    	 if(res[0][0] == 4){
		    		 $('#startContainer').removeClass('no-drop').addClass('a-live');
				     $('#startContainerFa').removeClass('self_a');
		    	 }
		     }else{
		    	 $('#startContainer').removeClass('a-live').addClass('no-drop');
			     $('#startContainerFa').addClass('self_a');
			     $('#stopContainer').removeClass('a-live').addClass('no-drop');
			     $('#stopContainerFa').addClass('self_a');
		     }
		 }
		 else{
		     $("input[type='checkbox']").prop("checked",false);
		     $('#startContainer').removeClass('a-live').addClass('no-drop');
		     $('#startContainerFa').addClass('self_a');
		     $('#stopContainer').removeClass('a-live').addClass('no-drop');
		     $('#stopContainerFa').addClass('self_a');
		     $('#deleteButton').removeClass('a-live').addClass('no-drop');
		     $('#deleteButtonFa').addClass('self_a');
		     $('#ExportBtn').removeClass('a-live').addClass('no-drop');
		     $('#ExportButtonFa').addClass('self_a');
		 }
	});

	checkbox();

	//服务查找

	//$("#serviceSearchBtn").click(function()
	$("#serviceSearchBtn").unbind('click').bind('click',function(){
		$('.dataTables-example').dataTable().fnClearTable(false);
		$('.dataTables-example').dataTable().fnDestroy();
		loadServices();
	});
 });

 //代码仓库代码发生变化，跳转进入构建详细页面
 function redirectCiCode(serviceId){
	 alert(serviceId);
	 $.ajax({
		 url : ""+ ctx + "service/findCodeId.do?serviceId=" + serviceId ,
		 type : "get",
		 success : function(data){
			 var data = eval("(" + data + ")");
		 }
	 });
 }

function loadContainers(obj,serviceId) {
	var serviceID = serviceId;
	var aaa = 'tr[serviceidcon = "' + serviceID + '"]';
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
									+ '<td style="width: 15%;">';
							if (data.service.status == 6){
								containersHtml += '<a style="margin-left: 19px;" href="javascript:debug('+serviceID+','+data.containerList[i].containerStatus+')">';
							} else {
								containersHtml += '<a style="margin-left: 19px;">';
							}
							containersHtml += containerName
									+ '</a>'
									+ '</td>'
									+ '<td colspan="2" style="width: 32%"><i class="'
									+ statusClassName
									+ '"></i>'
									+ containerStatus
									+ '<img src=" '+ctx+'/images/loading4.gif" alt="" class="'
									+ loadingImgShowClass
									+ '"/></td>'
									+ '<td style="width: 12%"></td>'
									+ '<td colspan="2" style="width: 32%"></td>'
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
									layer.msg("服务启动成功",{icon: 6});
									setTimeout('window.location.reload()',1500);
								} else {
									layer.alert("服务启动失败");
								}
							}
						});
					}
				});
			});
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
									layer.msg("服务已停止",{icon: 6});
									setTimeout('window.location.reload()',1500);
								} else {
									layer.alert("服务停止失败，请检查服务器连接");
								}

							}
						});
					}
				});
			});
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
									layer.msg("服务已删除",{icon: 6});
									setTimeout('window.location.reload()',1500);
								} else {
									layer.alert("服务删除失败，请检查服务器连接");
								}
							}
						});
					}
				});

			});
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
									layer.msg("弹性伸缩成功",{icon: 6});
									setTimeout('window.location.reload()',1500);
								} else if (data.status == "400") {
									layer.alert("弹性伸缩失败，请检查服务器连接");
								} else if (data.status == "300") {
									layer.alert("请填写弹性伸缩的数量！");
								}

							}
						});
					},
					cancel : function(index) { // 或者使用btn2
						// 按钮【按钮二】的回调
					}
				});
			});
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
							});
							$("#confRamSlider_input").focus();
							return;
						}
						layer.close(index);
						$.ajax({
							url : "" + ctx + "/service/modifyCPU.do?id=" + id+ "&cpus=" + cpus + "&rams=" + rams,
							success : function(data) {
								data = eval("(" + data + ")");
								if (data.status == "200") {
									layer.msg("更改成功",{icon: 6});
									setTimeout('window.location.reload()',1500);
								} else {
									layer.alert("更改失败，请检查服务器连接");
								}

							}
						});
					},
					cancel : function(index) { // 或者使用btn2
						// 按钮【按钮二】的回调
					}
				});
			});
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
									layer.msg("升级完成",{icon: 6});
									setTimeout('window.location.reload()',1500);
								} else if (data.status == "500") {
									$('#myModal').modal('hide');
									layer.alert("请选择需要升级的版本号！");
								} else if(data.status == "201"){

								} else {
									$('#myModal').modal('hide');
									layer.alert("请检查配置服务！");
								}

							}
						});
					},
					cancel : function(index) { // 或者使用btn2
						// 按钮【按钮二】的回调
					}
				});

			});
}
//导出excel
	function exportExcel(){
		var serviceSearchCreator = $("#serviceSearchCreator").val();
		var serviceSearchImage = $("#serviceSearchImage").val();
		var serviceSearchName = $("#serviceSearchName").val();
		$.ajax({
			url : ctx + "/service/exportExcel.do?"+serviceSearchName+"&searchImage="+serviceSearchImage+"&searchCreatorName="+serviceSearchCreator,
			success : function(data) {

			}
		});
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
				layer.msg("服务启动成功",{icon: 6});
				setTimeout('window.location.reload()',1500);
			} else if (data.status == "501"){
				layer.alert("未查询到该服务！", function() {
					window.location.reload();
				});
			} else if (data.status == "502"){
				layer.alert("服务状态异常！", function() {
					window.location.reload();
				});
			} else {
				layer.alert("服务启动失败");
			}
		}
	});

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
						layer.msg("服务已停止",{icon: 6});
						setTimeout('window.location.reload()',1500);
					} else if (data.status == "501") {
						layer.alert("未查询到该服务！", function() {
							window.location.reload();
						});
					} else if (data.status == "502") {
						layer.alert("服务状态异常！", function() {
							window.location.reload();
						});
					} else {
						layer.alert("服务停止失败，请检查服务器连接");
					}

				}
			});

		}
	});
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
						layer.msg("服务已删除",{icon: 6});
						setTimeout('window.location.reload()',1500);
					} else if (data.status == "501") {
						layer.alert("未查询到该服务！", function() {
							window.location.reload();
						});
					} else if (data.status == "502") {
						layer.alert("服务状态异常！", function() {
							window.location.reload();
						});
					} else {
						layer.alert("服务删除失败，请检查服务器连接");
					}

				}
			});
		}
	});
}


// 响应每一行上的弹性伸缩
function oneUpGradeContainer(id, containerName, nums, cpu, ram) {
	$('#upgradeServiceName').val(containerName);
	$('#numberChange').val(nums);

	var leftcpu = $("#leftcpu").html();
	var leftram = $("#leftram").html();

	var maxcpu = parseInt(leftcpu) / parseInt(cpu);
	var maxram = parseInt(leftram) / parseInt(ram);

	var total = 0;
	if (parseInt(maxcpu) > parseInt(maxram)) {
		total = maxram;
	} else {
		total = maxcpu;
	}

	total += parseInt(nums);
	$('#numberChange').attr("max", parseInt(total));
	$('#leftpod').text(total);

	layer.open({
		type : 1,
		title : '弹性伸缩',
		content : $("#upgrade"),
		btn : ['确定', '取消'],
		yes : function(index, layero) {//或者使用btn1
			//按钮【按钮一】的回调
			var num = $('#numberChange').val();
			if (parseInt(num) > parseInt(total)) {
				layer.tips('租户资源不足', "#numberChange", {
					tips : [1, '#3595CC']
				});
				$("#numberChange").focus();
				return;
			}

			layer.close(index);
			$.ajax({
				url : "" + ctx + "/service/modifyServiceNum.do?id=" + id + "&addservice=" + num,

				success : function(data) {
					data = eval("(" + data + ")");

					if (data.status == "200") {
						layer.msg("弹性伸缩成功", {
							icon : 6
						});
						setTimeout('window.location.reload()', 1500);
					} else if (data.status == "400") {
						layer.alert("弹性伸缩失败，请检查服务器连接");
					} else if (data.status == "300") {
						layer.alert("请填写弹性伸缩的数量！");
					} else if (data.status == "501") {
						layer.alert("未查询到该服务！", function() {
							window.location.reload();
						});
					} else if (data.status == "502") {
						layer.alert("服务状态异常！", function() {
							window.location.reload();
						});
					}

				}
			});
		},
		cancel : function(index) {//或者使用btn2
			//按钮【按钮二】的回调
		}
	});
}



// 响应每一行上的版本升级
function oneVersionUpgrade(id, serviceName, imgName, obj) {
	$('#upgradeVersionServiceName').val(serviceName);
	//查询镜像版本
	findImageVersion(imgName);
	$('#upgradeimgName').val(imgName);
	layer.open({
		type : 1,
		title : '升级镜像版本',
		content : $("#versionUpgrade"),
		btn : ['确定', '取消'],
		yes : function(index, layero) {
			var cStatusHtml = "<i class='fa_success'></i>" + "升级中" + "<img src='" + ctx + "/images/loading4.gif' alt=''/><a href=\"javascript:oneStopContainerUpdate(" + id + ",&apos;" + serviceName + "&apos;)\"><i class='fa fa-times fa-stopUpdate'></i></a>";
			$("#" + id + "_upgradeCluster").parent().parent().parent().parent().parent().parent().find(".cStatusColumn").html(cStatusHtml);
			$("#" + id + "_moreFun").removeClass('a-live').addClass('no-drop');
			$("#" + id + "_moreFun").find('.fa-gears').addClass('self_a');
			$("#" + id + "_stop").find('.fa-power-off').addClass('self_a');
			$("#" + id + "_dropdown").remove();
			layer.close(index);
			var imgVersion1 = $('#imgVersionName').val();
			$.ajax({
				url : ctx + "/service/modifyimgVersion.do?id=" + id + "&serviceName=" + serviceName + "&imgVersion=" + imgVersion1 + "&imgName=" + imgName,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						window.location.reload();
					} else if (data.status == "500") {
						layer.alert("请选择需要升级的版本号！", function() {
							window.location.reload();
						});
					} else if (data.status == "201") {
					} else if (data.status == "501") {
						layer.alert("未查询到该服务！", function() {
							window.location.reload();
						});
					} else if (data.status == "502") {
						layer.alert("服务状态异常！", function() {
							window.location.reload();
						});
					} else {
						layer.alert("请检查配置服务！", function() {
							window.location.reload();
						});
					}
				}
			});
		},
		cancel : function(index) {//或者使用btn2
			//按钮【按钮二】的回调
		}
	});
}


// 响应每一行上的修改配置
function oneChangeContainerConf(id,containerName,instanceNum,cpu,ram,status) {
	 $(".confCpu").prop("checked",false);
	 $(".confRam").prop("checked",false);
	 for(var i=0; i<$(".confCpu").length; i++){
		 var cpuVal = $(".confCpu")[i].getAttribute("value");
		 if(cpu==parseInt(cpuVal)){
			 var aa = $(".confCpu")[i];
			 $(aa).prop("checked",true);
			 break;
		 }
	 }
	 for(var j=0; j<$(".confRam").length; j++){
		 var ramVal = $(".confRam")[j].getAttribute("value");
		 if(ram==parseInt(ramVal)){
			 var bb = $(".confRam")[j];
			 $(bb).prop("checked",true);
			 break;
		 }
	 }
	 
	 $('#confServiceName').val(containerName);

	 layer.open({
		 type:1,
		 title: '更改配置',
		 area: ['500px', '350px'],
		 content: $("#changeConf"),
		 btn: ['确定', '取消'],
		 yes: function(index, layero){ //或者使用btn1
			 //按钮【按钮一】的回调
			 var cpus = $(".confCpu:checked").val();
			 var rams = $(".confRam:checked").val();
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
					 layer.tips('内存剩余不足',"#confRamSlider_input",{tips: [1,"#3595CC"]});
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
					 layer.tips('内存剩余不足',"#confRamSlider_input",{tips: [1,"#3595CC"]});
					 $("#confRamSlider_input").focus();
					 return;
				 }
			 }


			layer.close(index);

			$.ajax({
				url : "" + ctx + "/service/modifyCPU.do?id=" + id + "&cpus=" + cpus + "&rams=" + rams,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("更改完成", {
							icon : 6
						});
						setTimeout('window.location.reload()', 1500);
					} else if (data.status == "501") {
						layer.alert("未查询到该服务！", function() {
							window.location.reload();
						});
					} else if (data.status == "502") {
						layer.alert("服务状态异常！", function() {
							window.location.reload();
						});
					} else {
						layer.alert("更改失败，请检查服务器连接");
					}

				}
			});
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
	$(document).on("click",".chkItem",function(){
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
			});
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
						html += "<option type='text' value='" + image.version+ "'>" + image.version + "</option>";
					}
				}
			}
			$("#imgVersionName").html(html);
		}
	});
}


function loadServices() {
	$("#serviceList").empty();
	var serviceSearchCreator = $("#serviceSearchCreator").val();
	var serviceSearchImage = $("#serviceSearchImage").val();
	var serviceSearchName = $("#serviceSearchName").val();
	var curUserAutority = $("#curUserAutority").val();
	var searchingFlag = "";
	if(curUserAutority == 1){
		searchingFlag = false;
	}else{
		searchingFlag = true;
	}

	$('.dataTables-example').dataTable({
	 	"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,6] }],
	 	"searching" : searchingFlag,
	 	"autoWidth": false,
        "processing": true,
        "serverSide": true,
        "stateSave":true,
        "ordering":false,
        "ajax": ctx+"/service/page.do?searchService="+serviceSearchName+"&searchImage="+serviceSearchImage+"&searchCreatorName="+serviceSearchCreator,
        "columns": [
					{
						data : null,
						render : function ( data, type, row ) {
							var html = '<input type= "checkbox" class="chkItem" style="margin-left:30px;" name="chkItem"'+
								'autocomplete="off" id="checkboxID" value="'+row.id+'"'+
								'serviceName="'+row.serviceName+'"'+
								'serviceNum="'+row.instanceNum +'"'+
								'confRam="'+row.ram +'" status="'+row.status +'"'+
								'imagename="'+row.imgName +'"'+
								'imageversion="'+row.imgVersion +'"'+
								'confCpu="'+row.cpuNum + '"/>';
							return html;
						}
					},

					{
						data : null,
						render : function (data,type,row) {
							var html = "";
							if(curUserAutority == 1){
								html =
								'<span serviceId="'+row.id +'"'+
								'class="cluster_mirrer_name" style="width: 10px;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;">'+ row.serviceName +'</span>'+
								'<span class="number-node">' + row.instanceNum + '</span>';
							}else{
								html = '<b '+
								'class="caret margin" style="transform: rotate(-90deg);" rotate="hide"></b>'+
								'<a href="'+ctx+'/service/detail/'+row.id +'" serviceId="'+row.id +'"'+
								'class="cluster_mirrer_name" style="width: 10px;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;">'+ row.serviceName +'</a>'+
								'<span class="number-node">' + row.instanceNum + '</span>';
							}
							if (row.updateImage == true) {
								html += '<a id="'+row.id+'_code" class="margin cursor console-code-modal"'+
											'href="'+ctx+'ci/findCodeCiId.do?imgId='+row.imgID+'"'+
										'style="margin-left: 5px" ><img src="'+ctx+'/images/sd.gif" title="代码更新"></a>';
							}

							return html;
						}
					},

					{
						data : null,
						className:"cStatusColumn",
						render : function (data,type,row) {
							var html = '';
							if (row.status == 1) {
								html = '<i class="fa_stop"></i>' +
									'未启动 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.status == 2) {
								html = '<i class="fa_success"></i>' +
									'启动中 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.status == 3) {
								html = '<i class="fa_run"></i>' +
									'运行中 <img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.status == 4) {
								html = '<i class="fa_stop"></i>' +
									'已停止<img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.status == 5) {
								html = '<i class="fa_stop"></i>' +
									'启动失败<img src="'+ctx+'/images/loading4.gif"'+
									'alt="" class="hide" />';
							}
							if (row.status == 6) {
								html = '<i class="fa_run"></i>' +
								'调试中<img src="'+ctx+'/images/loading4.gif"'+
								'alt="" class="hide" />';
							}
							if (row.status == 7) {
								html = '<i class="fa_success"></i>' +
									'升级中<img src="'+ctx+'/images/loading4.gif"'+
									'alt="" /><a href="javascript:oneStopContainerUpdate('+row.id+',&apos;'+row.serviceName+'&apos;)"><i class="fa fa-times fa-stopUpdate"></i></a>';
							}
							if (row.status == 8) {
								html = '<i class="fa_success"></i>' +
									'升级中<img src="'+ctx+'/images/loading4.gif"'+
									'alt="" /><a href="javascript:oneStopContainerUpdate('+row.id+',&apos;'+row.serviceName+'&apos;)"><i class="fa fa-times fa-stopUpdate"></i></a>';
							}
							return html;
						}
					},

					{
						data:null,
						render : function (data,type,row) {
							var html = "";
							if(curUserAutority == 1){
								html = '<span class="cluster_mirrer">'+row.imgName+'</span>';
							}else{
								html = '<span class="cluster_mirrer">'+
								'<a title="点击查看镜像" target="_blank"'+
								'href="'+ctx+'/registry/detail/'+row.imgID +'">'+row.imgName +'</a>'+
								'</span>';
							}

							return html;
						}
					},

					{
						data : null,
						render : function (data,type,row) {
							var html = '<span class="url">';
							if(curUserAutority == 1){
								if (row.serviceAddr!=null && row.serviceAddr!='') {
									html += '<span href="'+row.serviceAddr +'/'+ row.proxyPath +'"'+
										'target="_blank" title="'+row.serviceAddr +'/'+ row.proxyPath+'">';
									if(row.serviceChName!=null && row.serviceChName!=''){
										html += row.serviceChName;
									} else {
										html += row.serviceAddr +'/'+ row.proxyPath;
									}
									html += '</span>';
								}
							}else{
								if (row.serviceAddr!=null && row.serviceAddr!='') {
									html += '<a href="'+row.serviceAddr +'/'+ row.proxyPath +'"'+
										'target="_blank" title="'+row.serviceAddr +'/'+ row.proxyPath+'">';
									if(row.serviceChName!=null && row.serviceChName!=''){
										html += row.serviceChName;
									} else {
										html += row.serviceAddr +'/'+ row.proxyPath;
									}
									html += '</a>';
								}
							}
							html += '</span>';
							return html;
						}
					},

					{
						data : null,
						render : function (data,type,row) {
							var html = '<input type="hidden" class="timeStrap" value="">'+
								'<i class="fa_time"></i><span>' + row.createDate +'</span>';
							return html;
						}
					},

					{
						data : null,
						render : function (data,type,row) {
							var html = '';

							if(curUserAutority == 1){
								var html = '<span class="cluster_creator">'+row.creatorName+'</span>';
							}else{

							if (row.status == 3 || row.status == 6) {

								html += '<a id="'+row.id+'_start" class="no-drop startContainer_a "'+
										'href="javascript:oneStartContainer('+ row.id +','+ row.status +')"'+
										'style="margin-left: 5px" title="启动"><i class="fa fa-play self_a"></i></a>';

								html += '<a id="'+row.id+'_stop" class="a-live stopContainer_a "'+
										'href="javascript:oneStopContainer('+ row.id +','+ row.status +')"'+
										'style="margin-left: 5px" title="停止"> '+
	                                    '<i class="fa fa-power-off"></i></a>';

							} else if (row.status == 7 || row.status == 8) {

								html += '<a id="'+row.id+'_start" class="no-drop startContainer_a "'+
										'href="javascript:oneStartContainer('+ row.id +','+ row.status +')"'+
										'style="margin-left: 5px" title="启动"><i class="fa fa-play self_a"></i></a>';

								html += '<a id="'+row.id+'_stop" class="no-drop stopContainer_a "'+
										'href="javascript:oneStopContainer('+ row.id +','+ row.status +')"'+
										'style="margin-left: 5px" title="停止"> '+
										'<i class="fa fa-power-off self_a"></i></a>';
							} else {
								html += '<a id="'+row.id+'_start" class="a-live startContainer_a "'+
                                        'href="javascript:oneStartContainer('+ row.id +','+ row.status +')"'+
                                        'style="margin-left: 5px" title="启动"><i class="fa fa-play"></i></a>';

								html += '<a id="'+row.id+'_stop" class="no-drop stopContainer_a "'+
										'href="javascript:oneStopContainer('+ row.id +','+ row.status +')"'+
										'style="margin-left: 5px" title="停止"> '+
										'<i class="fa fa-power-off self_a"></i></a>';
							}
							if (row.status != 3 && row.status != 6 && row.status != 7 && row.status != 8) {
								html += '<a id="'+row.id+'_change" class="a-live change " '+
										'href="javascript:startdebug('+ row.id +','+ row.status +')" title="调试"'+
										'style="margin-left: 5px">'+
											'<i class="fa fa-bug"></i>'
										+'</a> ';
							} else {
								html += '<a id="'+row.id+'_change" class="a-live change " '+
								'href="javascript:void(0)" title="调试"'+
								'style="margin-left: 5px">'+
									'<i class="fa fa-bug  self_a"></i>'
								+'</a> ';
							}

							if ( row.status == 7 || row.status == 8){
								html += '<ul class="moreFun" style="margin-bottom:0px;line-height:40px;" id="'+row.id+'" serviceName="'+row.serviceName+'" imgName="'+row.imgName+'">'+
								'<li class="dropdown">'+
									'<a id="'+row.id+'_moreFun" class="dropdown-toggle no-drop" data-toggle="dropdown" style="margin-left: 5px" title="更多配置">'+
									'<i class="fa fa-gears self_a"></i></a>';
							} else {
								html += '<ul class="moreFun" style="margin-bottom:0px;line-height:40px;" id="'+row.id+'" serviceName="'+row.serviceName+'" imgName="'+row.imgName+'">'+
								'<li class="dropdown">'+
									'<a id="'+row.id+'_moreFun" class="dropdown-toggle a-live" data-toggle="dropdown" style="margin-left: 5px" title="更多配置">'+
									'<i class="fa fa-gears"></i></a>';
							}
							if ( row.status != 7 && row.status != 8){
								html +=	'<ul id="'+row.id+'_dropdown" class="dropdown-menu">'+
										'<li>';
								if ( row.status == 6){
									html += '<a id="'+row.id+'_scaleCluster" class="no-drop scaleCluster_a "'+
									'href="javascript:void(0)" title="弹性伸缩"'+
									'> <i class="fa fa-arrows self_a"></i>弹性伸缩</a>';
								} else {
									html += '<a id="'+row.id+'_scaleCluster" class="a-live scaleCluster_a "'+
									'href="javascript:oneUpGradeContainer('+row.id+',&apos;'+row.serviceName +'&apos;,'+row.instanceNum +','+row.cpuNum +','+row.ram + ')" title="弹性伸缩"'+
									'> <i class="fa fa-arrows"></i>弹性伸缩</a>';
								}
								html +='</li>'+
										'<li>';
								if (row.status == 3) {
									html += '<a id="'+row.id+'_upgradeCluster" class="a-live upgradeCluster_a " '+
											'href="javascript:oneVersionUpgrade('+row.id+',&apos;'+ row.serviceName +'&apos;,&apos;'+row.imgName+'&apos;)" title="版本升级"'+
											'><i class="fa fa-arrow-up"></i>版本升级</a> ';
								} else {
									html += ' <a id="'+row.id+'_upgradeCluster" class="no-drop upgradeCluster_a " '+
	                                    	'href="javascript:oneVersionUpgrade('+row.id+',&apos;'+row.serviceName+'&apos;,&apos;'+row.imgName+'&apos;,this)" title="版本升级"'+
	                                        '><i class="fa fa-arrow-up self_a"></i>版本升级</a>';
								}
								html +=	'</li>'+
										'<li>';

								if ( row.status == 6){
									html += '<a id="'+row.id+'_changeConfiguration" class="no-drop changeConfiguration_a " '+
									'href="javascript:void(0);" title="更改配置"'+
									'><i class="fa fa-cog self_a"></i>更改配置</a> ';
								} else {
									html += '<a id="'+row.id+'_changeConfiguration" class="a-live changeConfiguration_a " '+
									'href="javascript:oneChangeContainerConf('+row.id+',&apos;'+ row.serviceName +'&apos;,'+row.instanceNum +','+row.cpuNum +','+row.ram +','+row.status +');" title="更改配置"'+
									'><i class="fa fa-cog"></i>更改配置</a> '	;
								}
								html +=	'</li>'+
									'</ul>'+
								'</li>'+
								'</ul>';
							}
							html += '<a id="'+row.id+'_del" class="a-live deleteButton_a "'+
									'href="javascript:oneDeleteContainer('+row.id+')"'+
									'style="margin-left: 5px" title="删除"> <i class="fa fa-trash"></i></a>';
						}
							return html;
						}
					}

                   ],

		"createdRow": function( row, data, dataIndex ) {
	          $(row).addClass( 'clusterId' );
	          $(row).data(row.id);
		}

	});
}

function startdebug(id, status) {
	if (3 == status) {
		return;
	}

	$.ajax({
		url : "" + ctx + "/service/isDebugService.do?id=" + id,
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.status != "200") {
				layer.alert("该服务不支持调试");
			} else {
				$.ajax({
					url : "" + ctx + "/service/createContainer.do?id=" + id + "&isDebug=" + true,
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.msg("服务启动成功", {
								icon : 6
							});
							setTimeout('window.location.reload()', 1500);
						} else if (data.status == "501") {
							layer.alert("未查询到该服务！", function() {
								window.location.reload();
							});
						} else if (data.status == "502") {
							layer.alert("服务状态异常！", function() {
								window.location.reload();
							});
						} else {
							layer.alert("服务启动失败");
						}
					}
				});

			}
		}
	});

}

function debug(id, status){
	if (1 == status) {
		return;
	}

//	$.ajax({
//		url : "" + ctx+ "/service/debug.do?id="+ id,
//		success : function(data) {
//			data = eval("(" + data + ")");
//			if (data.status == "200") {
				var href = ctx + "/service/debug/" + id;
				window.open(href);
//				window.location.href = ctx + "/service/debug/" + id;

//			}
//			else if (data.status == "400") {
//				layer.alert("该服务不支持调试");
//			} else if(data.status == "401") {
//				layer.alert("该服务不是调试状态");
//			}

//		}
//	})
}


function oneStopContainerUpdate(id, serviceName) {
	layer.open({
		title : '取消升级',
		content : '确定取消升级？',
		btn : ['确定', '取消'],
		yes : function(index, layero) {
			layer.close(index);
			$.ajax({
				url : "" + ctx + "/service/cancelUpdate.do?id=" + id + "&serviceName=" + serviceName,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("服务升级取消", {
							icon : 6
						});
						setTimeout('window.location.reload()', 1500);
					} else if (data.status == "501") {
						layer.alert("未查询到该服务！", function() {
							window.location.reload();
						});
					} else if (data.status == "502") {
						layer.alert("服务状态异常！", function() {
							window.location.reload();
						});
					} else if (data.status == "503") {
						layer.alert("正在取消升级，请稍等。", function() {
							window.location.reload();
						});
					} else {
						layer.alert("服务升级取消失败");
					}
				}
			});
		}
	});
}


