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
	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="cluster" value="" />
	</jsp:include>
	<input type="hidden" id="checkedHosts">

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">集群资源</li>
					</ol>
				</div>
				<div class="contentMain">

					<aside class="aside-btn">
						<div class="btns-group">
							<a id="clusterResource" class="Record action"><span
								class="btn btn-defaults btn-white"><span class="ic_left">容器监控</span></span></a>
						</div>
					</aside>

					<div class="caption clearfix" style="padding-bottom: 0px">
						<ul class="toolbox clearfix">
							<li><a href="javascript:window.location.reload(true);"
								id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
						</ul>
						<form id="search_form" class="form-inline"
							action="<%=path%>/user/searchByCondition" method="post">
							<c:if test="${cur_user.user_autority == 1}">
								<div class="searchFun"
									style="float: left; text-align: center; margin: 0px 10px"
									align="right">
									<label style="line-height: 35px">租户:</label> <select
										name="search_namespace" id="search_namespace"
										onchange="searchNamespace()"
										style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
										<option name="search_namespace" value="0"></option>
									</select>
								</div>
							</c:if>
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px"
								align="right">
								<label style="line-height: 35px">实例:</label> <select
									name="search_pod" id="search_pod" onchange="searchPod()"
									disabled
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option name="search_pod" value="0"></option>
								</select>
							</div>

							<%--<div class="searchFun" style="float: left; text-align: center; margin: 0px 10px" align="right">--%>
							<%--<label style="line-height: 35px">容器:</label>--%>
							<%--<select name="search_container" id="search_container" onchange="searchContainer()" disabled--%>
							<%--style="height: 30px;display: inline; width: 140px; border-radius: 5px;">--%>
							<%--<option name="search_container" selected="" value="0"></option>--%>
							<%--</select>--%>
							<%--</div>--%>
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px; float: right"
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

		//默认监控5分钟

		if (document.getElementById('search_namespace') != null) {
			getContainerMonitor("5m", "", "", false);
			getAllNamespace();
		} else {
			getContainerMonitor("5m", "", "", true);
			$("#search_pod").removeAttr("disabled");
		}

		//获取监控数据
		function getContainerMonitor(timePeriod, nameSpace, podName,
				isNamespace) {
			$.ajax({
				url : ctx + "/cluster/getContainerMonitor?timePeriod="
						+ timePeriod + "&nameSpace=" + nameSpace + "&podName="
						+ podName,
				success : function(data) {
					var jsonData = $.parseJSON(data);
					showContainerImg(jsonData);
					if (isNamespace) {
						showPodOpt(jsonData)
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

		//     var containerData = {
		//     'xValue': ['2014-11-19', '2014-11-20', '2014-11-21', '2014-11-22', '2014-11-23', '2014-11-24', '2014-11-25', '2014-11-26', '2014-11-27'],
		//     'yValue': [{
		//     'name': 'pod01', 'val': [{
		//     'titleText': 'container01',
		//     'val': [{
		//     'title': 'memory',
		//     'val': [{
		//     'legendName': 'Limit Current',
		//     'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     },
		//     {
		//     'legendName': 'Working Set Current',
		//     'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
		//     }]
		//     },
		//     {
		//     'title': 'cpu',
		//     'val': [{
		//     'legendName': 'Limit Current',
		//     'yAxis': [320, 182, 391, 234, 390, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     }]
		//     }]
		//     },
		//     {
		//     'titleText': 'container02', 'val': [{
		//
		//     'title': 'memory',
		//     'val': [{
		//     'legendName': 'Limit Current',
		//     'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     },
		//     {
		//     'legendName': 'Working Set Current',
		//     'yAxis': [98, 90, 96, 96, 93, 95, 86, 89, 85]
		//     }]
		//     },
		//     {
		//     'title': 'cpu',
		//     'val': [{
		//     'legendName': 'cLimit Current',
		//     'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'cUsage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     }]
		//
		//     }]
		//     }]
		//     },
		//     {
		//     'name': 'pod02', 'val': [{
		//     'titleText': 'container03', 'val': [{
		//
		//     'title': 'memory',
		//     'val': [{
		//     'legendName': 'Limit Current',
		//     'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     },
		//     {
		//     'legendName': 'Working Set Current',
		//     'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
		//     }]
		//     }
		//     ,
		//
		//     {
		//     'title': 'cpu',
		//     'val': [{
		//     'legendName': 'cpu Limit Current',
		//     'yAxis': [320, 182, 391, 234, 390, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'cpu Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     }]
		//
		//
		//     }]
		//     }, {
		//     'titleText': 'container04', 'val': [{
		//
		//     'title': 'memory',
		//     'val': [{
		//     'legendName': 'Limit Current',
		//     'yAxis': [220, 182, 191, 234, 290, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     },
		//     {
		//     'legendName': 'Working Set Current',
		//     'yAxis': [10, 11, 10, 12, 12, 12, 12, 12, 12]
		//     }]
		//     }
		//     ,
		//
		//     {
		//     'title': 'cpu',
		//     'val': [{
		//     'legendName': 'Limit Current',
		//     'yAxis': [320, 182, 391, 234, 390, 330, 310, 290, 330]
		//     },
		//     {
		//     'legendName': 'Usage Current',
		//     'yAxis': [120, 132, 101, 134, 90, 230, 210, 101, 134]
		//     }]
		//
		//
		//     }]
		//     }]
		//     }
		//     ]
		//     };

		//添加container memory画布
		function addContainerMemImg() {
			var memTxt = '<div class="table-lists pod"  style="margin-top: 10px; float: left;width: 49.5%;height:260px;">'
					+ '</div>';
			$("#resourceContainer").append(memTxt);
		}
		//添加container cpu画布
		function addContainerCpuImg() {
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
		//删除pod下拉列表项目
		function removePod() {
			var imgLst = document.getElementById("resourceContainer");
			var count = imgLst.childNodes.length;
			for (var i = 0; i < count; i++) {
				imgLst.removeChild(imgLst.childNodes[0]);
			}
		}
		//筛选租户
		function searchNamespace() {
			$("#search_pod").removeAttr("disabled");
			$("#search_pod")[0].children[0].selected = true;
			var namespace0val = $("#search_namespace")[0].value;
			var time0val = $("#search_time")[0].value;

			if ($("#search_namespace")[0].children[0].selected == true) {
				$("#search_pod").attr("disabled", "disabled");
				removePod();
				getContainerMonitor(time0val, "", "", true)
			} else {
				removePod();
				getContainerMonitor(time0val, namespace0val, "", true);
				var podOptCount = $("#search_pod")[0].options.length;
				var podLst = document.getElementById("search_pod");
				for (var j = 1; j < podOptCount; j++) {
					podLst.removeChild(podLst.options[1])
				}
			}

		}

		function searchPod() {

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
				$("#search_pod").removeAttr("disabled");
				removePod();
				var pod0val = $("#search_pod")[0].value;
				var time0val = $("#search_time")[0].value;
				if ($("#search_pod")[0].children[0].selected == true) {
					getContainerMonitor(time0val, "", "", true);
				} else {
					getContainerMonitor(time0val, "", pod0val, true);
				}
			}
		}

		function searchTime() {

			if (document.getElementById("search_namespace") != null) {

				removePod();
				if ($("#search_pod")[0].value == "0") {
					var pod0val = "";
				} else {
					var pod0val = $("#search_pod")[0].value;
				}
				var time0val = $("#search_time")[0].value;
				getContainerMonitor(time0val, namespace0val, pod0val, false);
			} else {

				$("#search_pod").removeAttr("disabled");

				removePod();
				if ($("#search_pod")[0].value == "0") {
					var pod0val = "";
				} else {
					var pod0val = $("#search_pod")[0].value;
				}
				var time0val = $("#search_time")[0].value;
				getContainerMonitor(time0val, "", pod0val, false);
			}
		}

		//生成容器监控画布
		function showContainerImg(containerData) {
			var count = 0;
			for (var s = 0; s < containerData.yValue.length; s++) {
				var containerDataYval = containerData.yValue[s];
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
							bottom : '10%',
							containLabel : true
						},
						xAxis : [],
						yAxis : [],
						series : []
					};

					var xAxis = {
						type : 'category',
						boundaryGap : false,
						data : containerData.xValue
					};
					option.xAxis.push(xAxis);
					count = showContainerMemImg(count, containerDataYval, j,
							option);
					count = showContainerCpuImg(count, containerDataYval, j,
							option);
				}
			}
		}
		//生成内存监控
		function showContainerMemImg(count, containerDataYval, j, option) {
			var c = {
				type : 'value',
				scale : true,
				axisLabel : {
					formatter : '{value} Gib'
				}
			};
			option.yAxis.push(c);
			var containerDataYmem = containerDataYval.val[j].val[0];
			for (var i = 0; i < containerDataYmem.val.length; i++) {
				var containerYval = containerDataYmem.val[i].yAxis;
				var a = {
					name : containerDataYmem.val[i].legendName,
					icon : 'roundRect'
				};
				option.legend.data.push(a);

				//红色预警
				for (var arrayNum = 0; arrayNum < containerYval.length; arrayNum++) {
					var usageVal = containerDataYmem.val[1].yAxis[arrayNum];
					var limitVal = containerDataYmem.val[0].yAxis[arrayNum];
					if (i == 1 && usageVal >= limitVal * 0.9) {
						var bc = {
							name : containerDataYmem.val[i].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
							itemStyle : {
								normal : {
									color : colorData[i]
								}
							},
							areaStyle : {
								normal : {
									color : '#ff0000',
									opacity : 0.3
								}
							},
							data : containerYval
						};
						option.series.push(bc);
					} else {
						var b = {
							name : containerDataYmem.val[i].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
							itemStyle : {
								normal : {
									color : colorData[i]
								}
							},
							areaStyle : {
								normal : {
									color : colorData[i],
									opacity : 0.3
								}
							},
							data : containerYval
						};
						option.series.push(b);
					}
					break
				}

				var titleText = containerDataYval.name + " "
						+ containerDataYval.val[j].titleText + " "
						+ containerDataYmem.title;
				option.title.text = titleText;
			}
			addContainerMemImg();
			var containersMemImg = document.getElementById('resourceContainer').children[count];
			$(containersMemImg).addClass(containerDataYval.name);
			$(containersMemImg).addClass(containerDataYval.val[j].titleText);

			var containersMem = echarts.init(containersMemImg);
			containersMem.setOption(option);

			count++;
			option.legend.data = [];
			option.series = [];
			option.yAxis = [];
			return count;
		}
		//生成cpu监控
		function showContainerCpuImg(count, containerDataYval, j, option) {
			var f = {
				type : 'value',
				scale : true,
				axisLabel : {
					formatter : '{value} ms'
				},
			};
			option.yAxis.push(f);
			var containerDataYcpu = containerDataYval.val[j].val[1];

			for (var k = 0; k < containerDataYcpu.val.length; k++) {
				var containerYCpuval = containerDataYcpu.val[k].yAxis;
				var d = {
					name : containerDataYcpu.val[k].legendName,
					icon : 'roundRect'
				};
				option.legend.data.push(d);

				//红色预警
				for (var arrayNumCpu = 0; arrayNumCpu < containerYCpuval.length; arrayNumCpu++) {
					var usageVal = containerDataYcpu.val[1].yAxis[arrayNumCpu];
					var limitVal = containerDataYcpu.val[0].yAxis[arrayNumCpu];
					if (k == 1 && usageVal >= limitVal * 0.9) {
						var ec = {
							name : containerDataYcpu.val[k].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
							itemStyle : {
								normal : {
									color : colorData[k]
								}
							},
							areaStyle : {
								normal : {
									color : '#ff0000',
									opacity : 0.3
								}
							},
							data : containerYCpuval
						};
						option.series.push(ec);
					} else {
						var e = {
							name : containerDataYcpu.val[k].legendName,
							type : 'line',
							barWidth : 5,
							barHeight : 2,
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
						option.series.push(e);
					}
					break
				}

				var titleTextCpu = containerDataYval.name + " "
						+ containerDataYval.val[j].titleText + " "
						+ containerDataYcpu.title;
				option.title.text = titleTextCpu;

			}
			addContainerCpuImg();
			var containersCpuImg = document.getElementById('resourceContainer').children[count];
			$(containersCpuImg).addClass(containerDataYval.name);
			$(containersCpuImg).addClass(containerDataYval.val[j].titleText);
			var containersCpu = echarts.init(containersCpuImg);
			containersCpu.setOption(option);
			count++;
			return count;
		}
	</script>
</body>
</html>