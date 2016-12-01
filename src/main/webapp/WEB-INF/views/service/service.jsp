<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
<script type="text/javascript" src="<%=path%>/js/service/service.js"></script>
</head>
<body>
	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">服务</li>
					</ol>
				</div>
				<div class="contentMain">


					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>服务
									</h5>

									<div class="ibox-tools">
										<a href="<%=path%>/service/add" id="serviceCreateBtn" title="创建服务">
											<i class="fa fa-plus"></i>
										</a>
										<a id="startContainer" class="no-drop" href="javascript:createContainer()" title="启动">
											<i id = "startContainerFa" class="fa fa-play self_a"></i>
										</a> 
										<a id="stopContainer" class="no-drop" href="javascript:stopContainer()" title="停止"> 
											<i id = "stopContainerFa" class="fa fa-power-off self_a"></i>
										</a>
										<a id="deleteButton" class="no-drop" href="javascript:delContainer()" title="删除">
											<i id = "deleteButtonFa" class="fa fa-trash self_a"></i>
										</a>
										<a id="ExportBtn" title="导出EXCEL">
					                        <i id = "ExportButtonFa" class="fa fa-share-square-o"></i>
					                    </a>
										<a href="" id="serviceReloadBtn" title="刷新">
											<i class="fa fa-repeat"></i>
										</a>
										
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-stripped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></th>
												<th style="width: 15%; padding-left: 5px;">名称</th>
												<th style="width: 12%; text-indent: 8px;">运行状态</th>
												<th style="width: 20%;">镜像</th>
												<th style="width: 12%;">服务地址</th>
												<th style="width: 12%;">创建于</th>
												<th style="width: 20%;">操作</th>
											</tr>
										</thead>

										<tbody id="serviceList">

											<c:forEach items="${serviceList}" var="service">
												<c:choose>
													<c:when test="${service.status == 1 }">
														<c:set var="statusName" value="未启动"></c:set>
														<c:set var="statusClassName" value="fa_stop"></c:set>
														<c:set var="loadingImgShowClass" value="hide"></c:set>
													</c:when>
													<c:when test="${service.status == 2 }">
														<c:set var="statusName" value="启动中"></c:set>
														<c:set var="statusClassName" value="fa_success"></c:set>
														<c:set var="loadingImgShowClass" value=""></c:set>
														<c:set var="loadingImgShowClass" value="hide"></c:set>
													</c:when>
													<c:when test="${service.status == 3 }">
														<c:set var="statusName" value="运行中"></c:set>
														<c:set var="statusClassName" value="fa_run"></c:set>
														<c:set var="loadingImgShowClass" value="hide"></c:set>
													</c:when>
													<c:when test="${service.status == 4 }">
														<c:set var="statusName" value="已停止"></c:set>
														<c:set var="statusClassName" value="fa_stop"></c:set>
														<c:set var="loadingImgShowClass" value="hide"></c:set>
													</c:when>
													<c:when test="${service.status == 5 }">
														<c:set var="statusName" value="启动失败"></c:set>
														<c:set var="statusClassName" value="fa_stop"></c:set>
														<c:set var="loadingImgShowClass" value="hide"></c:set>
													</c:when>
												</c:choose>
												<c:if test="${service.id == null || service.id == 0}">
													<c:set var="cursorClass" value="cursor-no-drop"></c:set>
												</c:if>



												<tr class="clusterId" id="inst_${service.id }" onclick="loadContainers(this)"
													containerName="${service.serviceName }" serviceId="${service.id }">
													<td style="width: 5%; text-indent: 30px;"><input
														type="checkbox" class="chkItem" name="chkItem"
														autocomplete="off" id="checkboxID" value="${service.id }"
														serviceName="${service.serviceName }"
														serviceNum="${service.instanceNum }"
														confRam="${service.ram }" status="${service.status }"
														imagename="${service.imgName }"
														imageversion="${service.imgVersion }"
														confCpu="${service.cpuNum }" /></td>
													<td style="width: 15%; white-space: nowrap;" ><b
														class="caret margin" style="transform: rotate(-90deg);" rotate="hide"></b>
														<a href="<%=path %>/service/detail/${service.id}" serviceId="${service.id}"
														class="cluster_mirrer_name" style="width: 10px;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;">${service.serviceName}</a> <span
														class="number-node">${service.instanceNum }</span> <span
														class="margin cursor console-code-modal"
														data-id="#console-code-modal"> <i
															class="fa fa-desktop" onclick="_showConsole('mysql');"></i>
													</span></td>
													<td style="width: 12%" class="cStatusColumn"
														id="containerStatus"><i class="${statusClassName}"></i>
														${statusName} <img src="<%=path %>/images/loading4.gif"
														alt="" class="${loadingImgShowClass}" /></td>
													<td style="width: 15%;"><span class="cluster_mirrer">
															<a title="点击查看镜像" target="_blank"
															href="<%=path %>../registry/detail/${service.imgID }">${service.imgName }</a>
													</span></td>
													<td style="width: 25%" id="mysqlurl"><span class="url">
															<c:if
																test="${service.serviceAddr!=null&&service.serviceAddr!='' }">
																<a href="${service.serviceAddr}/${service.proxyPath}"
																	target="_blank">${service.serviceAddr}/${service.proxyPath}</a>
															</c:if>
													</span></td>

													<td style="width: 12%" class="tdTimeStrap">
														<input type="hidden" class="timeStrap" value=""> 
														<i class="fa_time"></i><span>${service.createDate }</span>
													</td>
													<td style="width: 23%">
														<!-- 未运行 -->
													<c:choose>
													    <c:when test="${service.status == 3}">
															<a id="${service.id}_start" class="no-drop startContainer_a "
																href="javascript:oneStartContainer(${service.id},${service.status})"
																style="margin-left: 5px" title="启动">
																<i class="fa fa-play self_a"></i>
															</a>
														</c:when>
														<c:otherwise>
														    <a id="${service.id}_start" class="a-live startContainer_a "
                                                                href="javascript:oneStartContainer(${service.id},${service.status})"
                                                                style="margin-left: 5px" title="启动">
														        <i class="fa fa-play"></i>
                                                            </a>
														 </c:otherwise>
													</c:choose>
                                                    <c:choose>
                                                        <c:when test="${service.status == 3}">
															<a id="${service.id}_stop" class="a-live stopContainer_a "
																href="javascript:oneStopContainer(${service.id},${service.status})"
																style="margin-left: 5px" title="停止"> 
	                                                            <i class="fa fa-power-off"></i>
															</a>
                                                        </c:when>
                                                        <c:otherwise>
	                                                        <a id="${service.id}_stop" class="no-drop stopContainer_a "
                                                                href="javascript:oneStopContainer(${service.id},${service.status})"
                                                                style="margin-left: 5px" title="停止">
                                                                <i class="fa fa-power-off self_a"></i>
	                                                        </a>
                                                        </c:otherwise>
                                                    </c:choose>
														<a id="${service.id}_scaleCluster" class="a-live scaleCluster_a " 
															href="javascript:oneUpGradeContainer(${service.id },'${service.serviceName }',${service.instanceNum },${service.cpuNum },${service.ram })" title="弹性伸缩"
															style="margin-left: 5px">
																<i class="fa fa-arrows"></i>
														</a> 
									                   <c:choose>
                                                          <c:when test="${service.status == 3}">
															<a id="${service.id}_upgradeCluster" class="a-live upgradeCluster_a " 
																href="javascript:oneVersionUpgrade(${service.id },'${service.serviceName }','${service.imgName }')" title="版本升级"
																style="margin-left: 10px">
	                                                            <i class="fa fa-arrow-up"></i>
															</a> 
                                                          </c:when>
                                                          <c:otherwise>
                                                             <a id="${service.id}_upgradeCluster" class="no-drop upgradeCluster_a " 
                                                                href="javascript:oneVersionUpgrade(${service.id },'${service.serviceName }','${service.imgName }',this)" title="版本升级"
                                                                style="margin-left: 5px;margin-right:5px">
                                                                <i class="fa fa-arrow-up self_a"></i>
                                                            </a>    
                                                          </c:otherwise>
                                                       </c:choose>
														<a id="${service.id}_changeConfiguration" class="a-live changeConfiguration_a " 
															href="javascript:oneChangeContainerConf(${service.id },'${service.serviceName }',${service.instanceNum },${service.cpuNum },${service.ram },${service.status });" title="更改配置"
															style="margin-left: 5px">
																<i class="fa fa-cog"></i>
														</a> 														
														<a id="${service.id}_change" class="a-live change " 
															href="<%=path %>/service/debug/${service.id}" title="更改配置"
															style="margin-left: 5px">
																<i class="fa fa-bug"></i>
														</a> 														
														<a id="${service.id}_del" class="a-live deleteButton_a "
															href="javascript:oneDeleteContainer(${service.id})"
															style="margin-left: 5px" title="删除"> 
																<i class="fa fa-trash"></i>
														</a>
													</td>
												</tr>
											</c:forEach>
												
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="7">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>
	<!-- 弹性伸缩 -->
	<div id="upgrade" style="display: none">
		<ul class="popWin">
			<li class="line-h-3"><span class="edit-name-c">服务名称：</span> <input
				id="upgradeServiceName" disabled="disabled" class="c-con"
				style="margin-top: 5px; width: 165px;" type="text" value="">
			</li>
			<li class="line-h-3" id="instsizeChange">
				<div class="param-set">
					<span class="edit-name-c" style="margin-top: 5px;">实例数量：</span> <input
						value="1" id="numberChange" class="c-con" min="1" max=""
						style="margin-top: 10px; width: 165px;" type="number"> <span
						class="unit">个</span>
					<!-- <span  style="color: grey;margin-left: 50px;">可用实例数量：<label id="leftpod" ></label></span> -->
				</div>
			</li>
		</ul>
	</div>
	<div id="versionUpgrade" style="display: none">
		<ul class="popWin">
			<li class="line-h-3"><span class="edit-name-c">服务名称：</span> <input
				id="upgradeVersionServiceName" disabled="disabled" class="c-con"
				style="margin-top: 5px; width: 165px;" type="text" value="">
			</li>
			<li class="line-h-3"><span class="edit-name-c">镜像名称：</span> <input
				id="upgradeimgName" disabled="disabled" class="c-con"
				style="margin-top: 5px; width: 165px;" type="text" value="">
			</li>
			<li class="line-h-3" id="instsizeChange">
				<div class="param-set">
					<span class="edit-name-c" style="margin-top: 5px;">镜像版本：</span>
					<!--<select class="form-control" style="margin-top: 10px;width: 165px;display: inline; float: right;" id="imgVersionName" name="user_autority">
                                    <option name="user_autority" value="2">租户</option>
                                    <option name="user_autority" value="1">管理员</option>
                                                   </select>  -->
					<select id="imgVersionName" name="imgVersionName">

					</select>
					<!--  <input value="" id="imgVersionName" class=""  style="margin-top: 10px;width: 165px;" type="text"> -->
					<!-- <span  style="color: grey;margin-left: 50px;">可用实例数量：<label id="leftpod" ></label></span> -->
				</div>
			</li>
		</ul>
	</div>
	<!-- 更改配置 -->
	<div id="changeConf" style="display: none">
		<ul class="popWin">
			<li class="line-h-3 c-ser">
				<div class="param-set">
					<span class="c-title">服务名称：</span> <input class="c-con"
					id="confServiceName" disabled="disabled" type="text" value="">
				</div>
			</li>
			<li class="line-h-3 c-ser">
				<div class="param-set">
					<span class="c-title">CPU数量：</span> <input type="number" value="" class="c-con"
						min="${cpumin }"
						max="${cpumax }" autocomplete="off" step="0.1" placeholder=""
						id="confCpu" name="confCpu"> <span class="unit">个</span>
					<span style="color: grey;margin-left: 50px;">当前可用cpu数量：<label id="leftcpu" >${leftcpu>0?leftcpu:0 }</label></span>
				</div>
			</li>
			<li class="line-h-3 c-ser">
				<div class="param-set">
					<span class="c-title">内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;存：</span>  <input type="text" left="" value="" class="c-con"
						id="confRamSlider_input" name="confRam" min="${memorymin }" max="${memorymax }"> <span class="unit">M</span>
					<span style="color: grey;margin-left: 50px;">当前可用ram：<label id="leftram" >${leftmemory * 1024 }</label>M</span>
				</div>
			</li>
		</ul>
	</div>
	<!--版本升级进度条 -->
	<div class="modal fade container" id="myModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
		style="width: 30%">
		<div class="progress progress-striped active" id="loading"
			style="margin-top: 87%;">
			<div class="progress-bar progress-bar-info" role="progressbar"
				aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
				style="width: 100%;"></div>
		</div>
	</div>

	${msg}
	<c:if test="${msg!= null} ">
		<input type="hidden" id="errorMsg" value="${msg}">
		<script type="text/javascript">
			//var errorMsg = $("#errorMsg").val();
			var errorMsg = '${msg}';
			alert(errorMsg);
		</script>
	</c:if>
	<script type="text/javascript">
		 /* $('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 6 ]
			} ],
			"aaSorting": [[ 5, "desc" ]]
		}); */
		$("#checkallbox").parent().removeClass("sorting_asc"); 
	</script>
	<script type="text/javascript">
         document.getElementById('ExportBtn').onclick = function(){
        location.href = ctx + "/service/exportExcel.do";
        } 
    </script>
</body>
</html> 