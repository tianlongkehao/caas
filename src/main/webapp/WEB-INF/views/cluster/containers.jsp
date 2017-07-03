<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>集群</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/cluster.css" />
<script type="text/javascript" src="<%=path%>/js/cluster/cluster.js"></script>
<script type="text/javascript"
	src="<%=path%>/plugins/echarts/src/echarts.js"></script>
</head>

<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="cluster" value="" />
	</jsp:include>
	<input type="hidden" id="checkedHosts">

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">容器监控</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>容器监控
									</h5>
									<div class="ibox-tools">
										 <a href="javascript:window.location.reload(true);"
											 title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content" style="padding:0px"></div>

					<div class="caption clearfix" style="padding-bottom: 0px">
						<ul class="toolbox clearfix hide">
							<li><a style="cursor:pointer"
								id="containerUpdate"><i class="fa fa-repeat"></i></a></li>
						</ul>
						<form id="search_form" class="form-inline"
							action="<%=path%>/user/searchByCondition" method="post">
							<div class="searchFun"
								style="text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">时间:</label> <select
									name="search_time" id="search_time" onchange="searchTime()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option name="search_time" value="5m">最近5分钟</option>
									<option name="search_time" value="30m">最近30分钟</option>
									<option name="search_time" value="1h">最近1小时</option>
									<option name="search_time" value="6h">最近6个小时</option>
									<option name="search_time" value="12h">最近12小时</option>
									<option name="search_time" value="24h">最近24小时</option>
									<option name="search_time" value="7d">最近7天</option>
									<option name="search_time" value="30d">最近30天</option>
								</select>
							</div>
							
							<div class="searchFun"
								style="float: right; text-align: center; margin: 0px 10px"
								align="right">
								<label style="line-height: 35px">实例:</label> <select
									name="search_pod" id="search_pod" onchange="searchPod()"
									disabled
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option name="search_pod" value="0">-----请选择-----</option>
								</select>
							</div>
							
							<c:if test="${cur_user.user_autority == 1}">
								<div class="searchFun"
									style="float: right; text-align: center; margin: 0px 10px"
									align="right">
									<label style="line-height: 35px">租户:</label> <select                                                 
										name="search_namespace" id="search_namespace"
										onchange="searchNamespace()"
										style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
										<option name="search_namespace" value="0">-----请选择-----</option>
									</select>
								</div>
							</c:if>
	
							<%--<div class="searchFun" style="float: left; text-align: center; margin: 0px 10px" align="right">--%>
							<%--<label style="line-height: 35px">容器:</label>--%>
							<%--<select name="search_container" id="search_container" onchange="searchContainer()" disabled--%>
							<%--style="height: 30px;display: inline; width: 140px; border-radius: 5px;">--%>
							<%--<option name="search_container" selected="" value="0"></option>--%>
							<%--</select>--%>
							<%--</div>--%>
							

						</form>
					</div>
					<div>
						<div id="clusterResource_wrap" class="tab_wrap">
							<div class="detail-info">
								<div class="info-list" id="resourceContainer"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>

	<script type="text/javascript">
		
		var colorData = [ '#7EB26D', '#EAB839', '#6ED0E0' ];

		var aPods = [];
		var aCons = [];
		
		 var loading = layer.load(0, {
				shade : [ 0.3, '#000' ]
			});
		//默认监控5分钟

		 if (document.getElementById('search_namespace') != null) {
			 getContainerMonitor("5m", "", "", false);
			getAllNamespace();
		} else {
			getContainerMonitor("5m", "", "", true);
			$("#search_pod").removeAttr("disabled");
		} 

		//获取监控数据
		function getContainerMonitor(timePeriod, nameSpace, podName, isNamespace) {
			$.ajax({
				url : ctx + "/cluster/getContainerMonitor?timePeriod="
						+ timePeriod + "&nameSpace=" + nameSpace + "&podName="
						+ podName,
				success : function(data) {
					layer.closeAll();
					var jsonData = $.parseJSON(data);
					createMap(jsonData);
					if (isNamespace) {
						showPodOpt(jsonData)
					}
				}
			})
		}
		
		function createMap(containerData) {
			aPods = [];
			var count = 0;
			//循环pod
			for (var s = 0; s < containerData.yValue.length; s++) {
				var containerDataYval = containerData.yValue[s];
				aCons = [];
				//循环container
				for (var j = 0; j < containerDataYval.val.length; j++) {
					
					var option = {
						title : {
							text : ''
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							bottom : '1%',
							data : []
						},
						grid : {
							left : '3%',
							right : '4%',
							bottom : '20%',
							containLabel : true
						},
						xAxis : [],
						yAxis : [],
						series : []
					};
					//横坐标数据
					var xAxis = {
						type : 'category',
						boundaryGap : false,
						data : containerData.xValue
					};
					//memory
					var seriesDataMem = MemSeriesData(containerDataYval, j);
					//纵坐标
					var c = {
						type : 'value',
						scale : true,
						axisLabel : {
							formatter : '{value} Gib'
						}
					};
					
					
					//添加 内存option数据
					var objmem = setOtherOptionMem(containerDataYval,j);
					option.legend.data = objmem.memLegend;
					option.title.text = objmem.titleText;
					
					option.xAxis.push(xAxis);
					option.yAxis.push(c);
					//MEMORY纵坐标数据
					option.series = seriesDataMem;
					//内存画布
					addContainerLeftImg();
					var containersMemImg = document.getElementById('resourceContainer').children[count];
					$(containersMemImg).addClass(containerDataYval.name);
					$(containersMemImg).addClass(containerDataYval.val[j].titleText);
					var containersMem = echarts.init(containersMemImg);
					//绘制内存监控图
					containersMem.setOption(option);
					
					count++;
					//清空option的数据，以备下次使用
					option.yAxis = [];
					option.xAxis = [];

					//绘制cpu监控图
					var seriesDataCpu = cpuSeriesData(containerDataYval, j);
					//CPU纵坐标
					var c = {
						type : 'value',
						scale : true,
						axisLabel : {
							formatter : '{value} 个'
						}
					};
					var objcpu = setOtherOptionCpu(containerDataYval,j);
					option.legend.data = objcpu.cupLegend;
					option.title.text = objcpu.titleText;
					
					option.xAxis.push(xAxis);
					option.yAxis.push(c);
					//CPU横坐标数据
					option.series = seriesDataCpu;

					addContainerRightImg();
					var containersCpuImg = document
							.getElementById('resourceContainer').children[count];
					$(containersCpuImg).addClass(containerDataYval.name);
					$(containersCpuImg).addClass(
							containerDataYval.val[j].titleText);
					var containersCpu = echarts.init(containersCpuImg);
					//绘制CPU监控图
					containersCpu.setOption(option);
					
					count++;
					//清空option的数据，以备下次使用
					option.yAxis = [];
					option.xAxis = [];
					
					//TODO
					//Sockets
					var seriesDataSockets = socketsSeriesData(containerDataYval, j);
					//纵坐标
					var c = {
						type : 'value',
						scale : true,
						axisLabel : {
							formatter : '{value} 个'
						}
					};
					//添加 内存option数据
					var objmem = setOtherOptionSockets(containerDataYval,j);
					option.legend.data = objmem.memLegend;
					option.title.text = objmem.titleText;
					
					option.xAxis.push(xAxis);
					option.yAxis.push(c);
					//MEMORY纵坐标数据
					option.series = seriesDataSockets;
					//内存画布
					addContainerLeftImg();
					var containersMemImg = document.getElementById('resourceContainer').children[count];
					$(containersMemImg).addClass(containerDataYval.name);
					$(containersMemImg).addClass(containerDataYval.val[j].titleText);
					var containersSockets = echarts.init(containersMemImg);
					//绘制内存监控图
					containersSockets.setOption(option);
					count++;
					//清空option的数据，以备下次使用
					option.yAxis = [];
					option.xAxis = [];
					
					//TODO
					
					//threads
					var threadsDataSockets = threadsSeriesData(containerDataYval, j);
					//纵坐标
					var c = {
						type : 'value',
						scale : true,
						axisLabel : {
							formatter : '{value} 个'
						}
					};
					//添加 内存option数据
					var objmem = setOtherOptionThreads(containerDataYval,j);
					option.legend.data = objmem.memLegend;
					option.title.text = objmem.titleText;
					
					option.xAxis.push(xAxis);
					option.yAxis.push(c);
					//MEMORY纵坐标数据
					option.series = threadsDataSockets;
					//内存画布
					addContainerRightImg();
					var containersMemImg = document.getElementById('resourceContainer').children[count];
					$(containersMemImg).addClass(containerDataYval.name);
					$(containersMemImg).addClass(containerDataYval.val[j].titleText);
					var containersThreads = echarts.init(containersMemImg);
					//绘制内存监控图
					containersThreads.setOption(option);
					count++;
					//清空option的数据，以备下次使用
					option.yAxis = [];
					option.xAxis = [];
					
					//TODO
					
					var aaaObj = {};
					aaaObj.mem = containersMem;
					aaaObj.cpu = containersCpu;
					aaaObj.soc = containersSockets;
					aaaObj.threads = containersThreads;
					aCons.push(aaaObj);
					//实时刷新功能
					//showDynamic(containersMem,containersCpu,containersSockets,containersThreads);
				}
				aPods.push(aCons);
			}
			document.getElementById("containerUpdate").addEventListener("click", function() {
				update(aPods);
			}, false);
		}
		function setOtherOptionMem(containerDataYval,j){
			var containerDataYmem = containerDataYval.val[j].val[0];
			var memLegend = [];
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerDataYmemVal = containerDataYmem.val[i].yAxis;
				var a = {
					name : containerDataYmem.val[i].legendName,
					icon : 'roundRect'
				};
				memLegend.push(a);	
				
				var titleText = containerDataYval.name + " "
				+ containerDataYval.val[j].titleText + " "
				+ containerDataYmem.title;
				var objmem = {};
				objmem.memLegend = memLegend;
				objmem.titleText = titleText;
			}
			return objmem;		
		}
		function setOtherOptionCpu(containerDataYval,j){
			var cupLegend = [];
			var containerDataYcpu = containerDataYval.val[j].val[1];
			for (var k = 0; k < containerDataYcpu.val.length; k++) {
				var containerYCpuval = containerDataYcpu.val[k].yAxis;
				var d = {
					name : containerDataYcpu.val[k].legendName,
					icon : 'roundRect'
				};
				cupLegend.push(d);
				
				var titleTextCpu = containerDataYval.name + " "
						+ containerDataYval.val[j].titleText + " "
						+ containerDataYcpu.title;
				var objcpu = {};
				objcpu.cupLegend = cupLegend;
				objcpu.titleText = titleTextCpu;
				
			}
			return objcpu;
		}
		//TODO
		function setOtherOptionSockets(containerDataYval,j){
			var containerDataYmem = containerDataYval.val[j].val[2];
			var memLegend = [];
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerDataYmemVal = containerDataYmem.val[i].yAxis;
				var a = {
					name : containerDataYmem.val[i].legendName,
					icon : 'roundRect'
				};
				memLegend.push(a);	
				
				var titleText = containerDataYval.name + " "
				+ containerDataYval.val[j].titleText + " "
				+ containerDataYmem.title;
				var objmem = {};
				objmem.memLegend = memLegend;
				objmem.titleText = titleText;
			}
			return objmem;		
		}
		//TODO
		function setOtherOptionThreads(containerDataYval,j){
			var containerDataYmem = containerDataYval.val[j].val[3];
			var memLegend = [];
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerDataYmemVal = containerDataYmem.val[i].yAxis;
				var a = {
					name : containerDataYmem.val[i].legendName,
					icon : 'roundRect'
				};
				memLegend.push(a);	
				
				var titleText = containerDataYval.name + " "
				+ containerDataYval.val[j].titleText + " "
				+ containerDataYmem.title;
				var objmem = {};
				objmem.memLegend = memLegend;
				objmem.titleText = titleText;
			}
			return objmem;		
		}
		function showDynamic(containersMem,containersCpu){
// 			setInterval(function() {
// 				update(containersMem, containersCpu);
// 			}, 60000);
		}
		//局部刷新
		function update(aPods){
			debugger
			$.ajax({
				url : ctx + "/cluster/getContainerMonitor?timePeriod="
						+ "5m" + "&nameSpace=" + "" + "&podName="
						+ "",
				success : function(data) {
					layer.close(loading);
					var containerData = $.parseJSON(data);
					//按POD循环
					for (var s = 0; s < containerData.yValue.length; s++) {
						var containerDataYval = containerData.yValue[s];
						var aaaObj = aPods[s];
						//按container循环
						for (var j = 0; j < containerDataYval.val.length; j++) {
							containersMem = aaaObj[j].mem;
							containersCpu = aaaObj[j].cpu;
							var seriesDataMem = MemSeriesData(
									containerDataYval, j);

							var xAxis = {
								type : 'category',
								boundaryGap : false,
								data : containerData.xValue
							};
							var abc =[];
							abc.push(xAxis); 
							
							containersMem.setOption({
								xAxis: abc
						    });
							containersMem.setOption({
						        series: seriesDataMem
						    });
							
							
							var seriesDataCpu = cpuSeriesData(containerDataYval, j);
							
							containersCpu.setOption({
								xAxis: abc
						    });
							containersCpu.setOption({
						        series: seriesDataCpu
						    });
						}
					}
				}
			})
		}

		
		//获取所有NAMESPACE
		function getAllNamespace() {
			$.ajax({
				url : ctx + "/cluster/getAllNamespace",
				success : function(data) {
					showNamespceOpt($.parseJSON(data));
				}
			})
		}

		//添加container memory画布
		function addContainerLeftImg() {
			var memTxt = '<div class="table-lists pod"  style="margin-top: 10px; float: left;width: 49.5%;height:260px;">'
					+ '</div>';
			$("#resourceContainer").append(memTxt);
		}
		//添加container cpu画布
		function addContainerRightImg() {
			var cpuTxt = '<div class="table-lists pod"  style="margin-top: 10px; float: right;width: 49.5%;height:260px;">'
					+ '</div>';
			$("#resourceContainer").append(cpuTxt);
		}
		if (document.getElementById("search_namespace") != null) {
			//添加namespace下拉选项
			function addNamespacesOpt() {
				var namespaceOpt = '<option name="search_namespace" value=""></option>'
				$("#search_namespace").append(namespaceOpt);
			}
			//由namespaceData得到租户的option下拉选项
			function showNamespceOpt(namespaceData) {
				for (var namespaceVal = 0; namespaceVal < namespaceData.length; namespaceVal++) {
					addNamespacesOpt();
					var namespaceOpt = document
							.getElementById('search_namespace').children[namespaceVal + 1];
					namespaceOpt.value = namespaceData[namespaceVal];
					namespaceOpt.innerHTML = namespaceData[namespaceVal];
				}
			}
		}
		//添加pod option下拉选项
		function addPodOpt() {
			var serOpt = '<option name="search_pod" value=""></option>'
			$("#search_pod").append(serOpt);
		}
		//由containerData得到pod的option下拉选项
		function showPodOpt(containerData) {
			for (var podNum = 0; podNum < containerData.yValue.length; podNum++) {
				var containerDataYval = containerData.yValue[podNum];
				//search_pod_options
				addPodOpt();
				var podOpt = document.getElementById('search_pod').children[podNum + 1];
				podOpt.value = containerDataYval.name;
				podOpt.innerHTML = containerDataYval.name;
			}
		}
		//删除页面上所有pod画布
		function removePod() {
			var imgLst = document.getElementById("resourceContainer");
			var count = imgLst.childNodes.length;
			for (var i = 0; i < count; i++) {
				imgLst.removeChild(imgLst.childNodes[0]);
			}
		}
		
		//筛选租户
		function searchNamespace() {
			var loading = layer.load(0, {
	    		shade : [ 0.3, '#000' ]
	    	});
			//操作租户筛选时，让实例筛选可选，并锁定为空白
			$("#search_pod").removeAttr("disabled");
			$("#search_pod")[0].children[0].selected = true;
			//获取namespace和时间筛选的selected值
			var namespace0val = $("#search_namespace")[0].value;
			var time0val = $("#search_time")[0].value;

			if ($("#search_namespace")[0].children[0].selected == true) {
				//namespace选空白时，给实例筛选添加disable
				$("#search_pod").attr("disabled", "disabled");
				removePod();
				getContainerMonitor(time0val, "", "", true)
			} else {
				removePod();
				var podOptCount = $("#search_pod")[0].options.length;
				var podLst = document.getElementById("search_pod");
				for (var j = 1; j < podOptCount; j++) {
					podLst.removeChild(podLst.options[1])
				}
				getContainerMonitor(time0val, namespace0val, "", true);
			}
		}

		function searchPod() {
			var loading = layer.load(0, {
				shade : [ 0.3, '#000' ]
			});
			if (document.getElementById("search_namespace") != null) {

				var pod0val = $("#search_pod")[0].value;
				var namespace0val = $("#search_namespace")[0].value;
				var time0val = $("#search_time")[0].value;
				if ($("#search_pod")[0].children[0].selected == true) {
					removePod();
					getContainerMonitor(time0val, namespace0val, "", false);
				} else {
					removePod();
					getContainerMonitor(time0val, namespace0val, pod0val, false);
				}
			} else {
				var pod0val = $("#search_pod")[0].value;
				var time0val = $("#search_time")[0].value;

				if ($("#search_pod")[0].children[0].selected == true) {
					removePod();
					getContainerMonitor(time0val, "", "", false);
				} else {
					removePod();
					getContainerMonitor(time0val, "", pod0val, false);
				}
			}
		}

		function searchTime() {
			var loading = layer.load(0, {
				shade : [ 0.3, '#000' ]
			});
			if (document.getElementById("search_namespace") != null) {
				//管理员登录，有namespace请款下，时间筛选
				removePod();
				if ($("#search_pod")[0].value == "0") {
					var pod0val = "";
				} else {
					var pod0val = $("#search_pod")[0].value;
				}
				var time0val = $("#search_time")[0].value;

				if ($("#search_namespace")[0].children[0].selected == true) {

					getContainerMonitor(time0val, "", pod0val, false);
				} else {
					var namespace0val = $("#search_namespace")[0].value;
					getContainerMonitor(time0val, namespace0val, pod0val, false);
				}
			} else {
				//租户登录，无namespace情况下，时间筛选
				$("#search_pod").removeAttr("disabled");

				removePod();
				var pod0val = "";
				if ($("#search_pod")[0].value != "0") {
					pod0val = $("#search_pod")[0].value;
				}
				var time0val = $("#search_time")[0].value;
				getContainerMonitor(time0val, "", pod0val, false);
			}
		}
		//监控中的series:[]
		//生成cpu监控
		function MemSeriesData( containerDataYval, j) {
			var seriesData = [];
			var containerDataYmem = containerDataYval.val[j].val[0];
			//循环memory的三条数据线containerDataYmem.val.length=3
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerDataYmemVal = containerDataYmem.val[i].yAxis;
				//红色预警，当使用量超过总量的90%时，显示红色
				var seriesVal = {
							name : containerDataYmem.val[i].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
							itemStyle : {
								normal : {
									color : colorData[i]
								}
							},
							label : {},
							areaStyle : {
								normal : {
									color : colorData[i],
									opacity : 0.3
								}
							},
							data : containerDataYmemVal
						};
				if(i == 1){
					for (var arrayNum = 0; arrayNum < containerDataYmemVal.length; arrayNum++) {
						var usageVal = containerDataYmem.val[1].yAxis[arrayNum];
						var limitVal = containerDataYmem.val[0].yAxis[arrayNum];
						console.log("usageVal"+usageVal);
						console.log("limitVal"+limitVal);
						
						if (null!=usageVal && null!=limitVal && usageVal >= limitVal * 0.9) {
							
							var dangerSeriesLabel = {
									normal : {
										show : true,
										position : 'top',
										textStyle : {
											color : '#CC0000'
										}
									}
								};
							var dangerSeriesAreaStyle = {
									normal : {
										color : '#CC0000',
										opacity : 0.6
									}
								};
							seriesVal.label=dangerSeriesLabel;
							seriesVal.areaStyle=dangerSeriesAreaStyle;
							break
						}	
					}
				}
				seriesData.push(seriesVal);
			}
			return seriesData;
		}
		//生成cpu监控
		function cpuSeriesData(containerDataYval, j) {
			var seriesData = [];
			var containerDataYcpu = containerDataYval.val[j].val[1];
			//循环CPU的三条数据线containerDataYcpu.val.length=3
			for (var k = 0; k < containerDataYcpu.val.length; k++) {
				var containerYCpuval = containerDataYcpu.val[k].yAxis;
				var cpuSeriesVal = {
						name : containerDataYcpu.val[k].legendName,
						type : 'line',
						barWidth : 5,
						barHeight : 2,
						label : {},
						itemStyle : {
							normal : {
								color : colorData[k]
							}
						},
						areaStyle : {
							normal : {
								color : colorData[k],
								opacity : 0.3
							}
						},
						data : containerYCpuval
					};
				//红色预警
				if(k == 1){
					for (var arrayNumCpu = 0; arrayNumCpu < containerYCpuval.length; arrayNumCpu++) {
						var usageVal = containerDataYcpu.val[1].yAxis[arrayNumCpu];
						var limitVal = containerDataYcpu.val[0].yAxis[arrayNumCpu];
						if (null!=usageVal && null!=limitVal && usageVal >= limitVal * 0.9) {
							var dangerCpuSeriesLabel = {
									normal : {
										show : true,
										position : 'top',
										textStyle : {
											color : '#CC0000'
										}
									}
								};
							var dangerCpuSeriesAreaStyle = {
									normal : {
										color : '#CC0000',
										opacity : 0.6
									}
								};
							cpuSeriesVal.label = dangerCpuSeriesLabel;
							cpuSeriesVal.areaStyle = dangerCpuSeriesAreaStyle;
							break
						}
					}
				}
				seriesData.push(cpuSeriesVal);
			}
			return seriesData;
		}
		//TODO
		//生成sockets监控
		function socketsSeriesData( containerDataYval, j) {
			var seriesData = [];
			var containerDataYmem = containerDataYval.val[j].val[2];
			//循环memory的三条数据线containerDataYmem.val.length=3
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerDataYmemVal = containerDataYmem.val[i].yAxis;
				//红色预警，当使用量超过总量的90%时，显示红色
				var seriesVal = {
							name : containerDataYmem.val[i].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
							itemStyle : {
								normal : {
									color : colorData[i]
								}
							},
							label : {},
							areaStyle : {
								normal : {
									color : colorData[i],
									opacity : 0.3
								}
							},
							data : containerDataYmemVal
						};
				if(i == 1){
					for (var arrayNum = 0; arrayNum < containerDataYmemVal.length; arrayNum++) {
						var usageVal = containerDataYmem.val[1].yAxis[arrayNum];
						var limitVal = containerDataYmem.val[0].yAxis[arrayNum];
						console.log("usageVal"+usageVal);
						console.log("limitVal"+limitVal);
						
						if (null!=usageVal && null!=limitVal && usageVal >= limitVal * 0.9) {
							
							var dangerSeriesLabel = {
									normal : {
										show : true,
										position : 'top',
										textStyle : {
											color : '#CC0000'
										}
									}
								};
							var dangerSeriesAreaStyle = {
									normal : {
										color : '#CC0000',
										opacity : 0.6
									}
								};
							seriesVal.label=dangerSeriesLabel;
							seriesVal.areaStyle=dangerSeriesAreaStyle;
							break
						}	
					}
				}
				seriesData.push(seriesVal);
			}
			return seriesData;
		}
		//TODO
		//生成threads监控
		function threadsSeriesData( containerDataYval, j) {
			var seriesData = [];
			var containerDataYmem = containerDataYval.val[j].val[3];
			//循环memory的三条数据线containerDataYmem.val.length=3
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerDataYmemVal = containerDataYmem.val[i].yAxis;
				//红色预警，当使用量超过总量的90%时，显示红色
				var seriesVal = {
							name : containerDataYmem.val[i].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
							itemStyle : {
								normal : {
									color : colorData[i]
								}
							},
							label : {},
							areaStyle : {
								normal : {
									color : colorData[i],
									opacity : 0.3
								}
							},
							data : containerDataYmemVal
						};
				if(i == 1){
					for (var arrayNum = 0; arrayNum < containerDataYmemVal.length; arrayNum++) {
						var usageVal = containerDataYmem.val[1].yAxis[arrayNum];
						var limitVal = containerDataYmem.val[0].yAxis[arrayNum];
						console.log("usageVal"+usageVal);
						console.log("limitVal"+limitVal);
						
						if (null!=usageVal && null!=limitVal && usageVal >= limitVal * 0.9) {
							
							var dangerSeriesLabel = {
									normal : {
										show : true,
										position : 'top',
										textStyle : {
											color : '#CC0000'
										}
									}
								};
							var dangerSeriesAreaStyle = {
									normal : {
										color : '#CC0000',
										opacity : 0.6
									}
								};
							seriesVal.label=dangerSeriesLabel;
							seriesVal.areaStyle=dangerSeriesAreaStyle;
							break
						}	
					}
				}
				seriesData.push(seriesVal);
			}
			return seriesData;
		}

	</script>
</body>
</html>