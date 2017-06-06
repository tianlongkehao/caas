<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
<script type="text/javascript"
	src="<%=path%>/js/service/service_create.js"></script>
<script type="text/javascript">
	$(function() {
		// 判断是否从镜像点击部署按钮跳转
		var isDepoly = '${isDepoly}';
		var imgID = '${imgID}';
		var imageName = '${imageName}';
		var imageVersion = '${imageVersion}';
		var resourceName = '${resourceName}';
		var portConfigs = '${portConfigs}'
		if (isDepoly == 'deploy') {
			deploy(imgID, imageName, imageVersion, resourceName, portConfigs);
		}
	});
</script>
<style type="text/css">
.self-define {
    height: 34px;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
}
</style>
</head>
<body>

	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li><a href="<%=path %>/service"><span id="nav2">服务管理</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">创建服务</li>
					</ol>
				</div>
				<div class="contentMain">


					<div class="modalCrealApp">

						<div class="steps-main">
							<div class="progre">
								<ul style="padding: 0 4rem;">
									<li class="radius_step action"><a class="go_backs"><span>1</span> 镜像来源</a></li>
									<li class="radius_step"><span>2</span> 容器配置</li>
									<!-- <li class="radius_step"><span>3</span> 高级设置</li> -->
								</ul>
							</div>
							<div class="step-inner" style="left: 0%;">

								<%-- 镜像来源 --%>
								<div class="host_step1">
									<div class="content">
										<div class="search">

											<input id="imageName" name="imageName" class="search-img"
												placeholder="搜索镜像" type="text">
											<button id="searchimage" class="btn btn-primary btn-color btn-send">搜索</button>

										</div>
									</div>

									<div class="blankapp">
										<ul class="blankapp-list">
											<li class="list-wrapper">
												<ul id="imageList">
												</ul>
											</li>
										</ul>
									</div>
								</div>

								<%-- 容器配置 --%>
								<form id="buildService" name="buildService"
									action="<%=path%>/service/constructContainer.do">
									<input type="hidden" id="envVariable" name="envVariable" value=""></input>
									<input type="hidden" id="portConfig" name="portConfig" value=""></input>
									<div class="host_step2">
										<ul class="safeSet">
											<li class="line-h-3"><span class="ve_top">镜像名称：</span> <input
												type="text" value="" class="in_style form-control" id="imgName"
												name="imgName" readOnly="readOnly"> <input
												type="hidden" value="" id="imgID" name="imgID"></li>
											<li class="line-h-3"><span class="ve_top">镜像版本：</span> <input
												type="text" value="" class="in_style form-control" id="imgVersion"
												name="imgVersion" readOnly="readOnly"></li>
											<li class="line-h-3"><span class="ve_top">服务名称：<font
													color="red">*</font></span><input type="text" value=""
												class="in_style form-control" id="serviceName" name="serviceName">
												<input type="hidden" value="" class="in_style"
												id="resourceName" name="resourceName"></li>
											<li class="line-h-3"><span class="ve_top">服务中文名称：<font
													color="red">*</font>
											</span><input type="text" value=""
												class="in_style form-control" id="serviceChName" name="serviceChName"></li>
											<li class="line-h-3"><span class="ve_top">责任人：<font color="red">*</font>
											</span><input type="text" value=""
												class="in_style form-control" id="responsiblePerson" name="responsiblePerson"></li>
											<li class="line-h-3"><span class="ve_top">责任人电话：<font color="red">*</font>
											</span><input type="text" value=""
												class="in_style form-control" id="responsiblePersonTelephone" name="responsiblePersonTelephone"></li>
											<li id="service_type"><span class="ve_top">启动命令：</span>
												<span class="update-mi"> <input type="checkbox"
													id="startCommand"> <label for="startCommand"><font
														color="blue">自定义启动命令</font></label>
											</span></li>
											<li class="line-h-3" id="startCommand_li"><input
												type="text" value="" class="in_style form-control"
												id="startCommand_input" name="startCommand"
												style="margin-left: 150px"></li>
											<!-- 检查服务状态 -->
											<li id="service_stat_check"><span class="ve_top">检查状态：</span>
												<span class="update-mi"> <input type="checkbox"
													id="checkSerStatus"> <label for="checkSerStatus"><font
														color="blue">检查服务状态</font></label>
											<li class="line-h-3" id="checkSerStatus_li"><input
												type="text" value="" class="in_style form-control"
												id="checkSerStatus_input" name="checkPath"
												style="margin-left: 150px"></li>
											</span></li>
											<li class="line-h-3" id="checkItems">
												<ul class="checkitems">
													<li><span class="check-lable">检测延迟：</span> <input
														type="number" value="600" class="number form-control"
														id="initialDelay"
														onkeyup="this.value=this.value.replace(/\D/g,'')" min="0"
														name="initialDelay"> <span class="c-unit">s</span></li>
													<li><span class="check-lable">检测超时：</span> <input
														type="number" value="5" class="number form-control"
														id="timeoutDetction"
														onkeyup="this.value=this.value.replace(/\D/g,'')" min="0"
														name="timeoutDetction"> <span class="c-unit">s</span></li>
													<li><span class="check-lable">检测频率：</span> <input
														type="number" value="10" class="number form-control"
														placeholder="1" id="periodDetction"
														onkeyup="this.value=this.value.replace(/\D/g,'')" min="1"
														name="periodDetction"> <span class="c-unit">s</span></li>
												</ul>
											</li>
											<!-- 监控设置 -->
											<li class="line-h-3" id="start_monitor"><span class="ve_top">监控设置：</span>
												<span> <c:if test="${monitor==0 }">
														<input type="checkbox" id="monitorStatus">
													</c:if> <c:if test="${monitor==1 }">
														<input type="checkbox" id="monitorStatus" checked="1">
													</c:if> <label for="monitorStatus"><font color="blue">Pinpoint监控</font></label>
													<input type="hidden" id="monitor" name="monitor" value="">
											</span></li>
											<!-- Pod互斥 -->
											<li class="line-h-3"  id="pod_mutex_type"><span class="ve_top">Pod调度方式：</span>
												<span><input type="checkbox"
												id="podmutex"> <label for="podmutex"><font
												         color="blue">Pod互斥</font></label>
												<input type="hidden" id="ispodmutex" name="ispodmutex" value="">
												<label id="podmutexlabel" style="display: none;float:right" ><font color="red">注意：为了满足pod互斥，实例数量不应大于集群节点数量${nodecount }</font></label>
											</span></li>
											<li class="line-h-3"><span class="ve_top">服务访问路径：<font
													color="red">*</font></span>
												<input type="text" value="" class="in_style form-control" id="webPath" name="servicePath">
												<span style="color:#1dd2af;" id="service-path"><i class="fa fa-info-circle"></i></span>
											</li>
											<li class="line-h-3"><span class="ve_top">nginx代理区域：</span>
												<c:forEach items="${zoneList}" var="zone">
                                                    <label class="checkbox-inline"> <input
                                                        type="checkbox" id="${zone }" name="nginxserv"
                                                        value="${zone }"> ${zone }区
                                                     </label>
												</c:forEach>
											    <input type="hidden" value="" class="in_style" id="proxyZone" name="proxyZone">
                                            </li>
											<li class="line-h-3"> <span class="ve_top">ClientIP黏连方式：</span>
	                                            <select class="selectVolume form-control" id="sessionAffinity" name="sessionAffinity"
	                                                style="height: 34px; width: 230px;">
	                                                <option name="sessionAffinity" value="" >NONE</option>
	                                                <option name="sessionAffinity" value="ClientIP">ClientIP</option>
	                                            </select>
                                            </li>
											<!--<li class="line-h-3"><span class="ve_top">选择集群：</span>
                                    <div class="select-versions" data-toggle="dropdown">
                                        <span class="clusterText" data="tenx_district2">北京二区</span>
                                        <div class="sign">
                                            <i class="fa fa-angle-right"></i>
                                        </div>
                                    </div>
                                    <ul id="clusterList" class="dropdown-menu dropdown-position">
                                        <li data="default">北京一区</li>
                                        <li data="tenx_district2">北京二区</li>
                                    </ul></li>
                                 <li class="line-h-3" style="min-height: 60px; padding-top: 10px;">
                                    <span class="ve_top display-block">容器配置：</span>

                                </li> -->
											<li class="line-h-3" id="instsize">
												<div class="param-set">
													<span class="ve_top">实例数量：<font color="red">*</font></span>
													<input type="number" value="1" class="number form-control" min="1"
														autocomplete="off" max="" placeholder="1" id="instanceNum"
														onkeyup="this.value=this.value.replace(/\D/g,'')"
														name="instanceNum"><span class="s-unit">个</span>
													<%-- 													<span style="color: grey;margin-left: 50px;">当前可用实例数量：${leftpod }</span>
													<span class="dynamic-scale"> <input type="checkbox"
														id="dynamic-service"> <label for="dynamic-service"><font
															color="blue">自动化伸缩</font></label>
													</span> --%>

												</div>
											</li>

											<li class="line-h-3" id="dynamic-range">

												<div class="param-set">
													<span class="ve_top">伸缩范围：<font color="red">*</font></span>
													<input type="number" value="1" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="minNum"
														name="instanceNum"> <span class="unit">个</span> <span
														style="margin-left: 34px"> ~ </span> <input type="number"
														value="10" class="number" min="1" autocomplete="off"
														max="" placeholder="10" id="maxNum" name="instanceNum">
													<span class="unit">个</span>
												</div>

											</li>
											<li class="line-h-3" id="dynamic-threshold">
												<div class="param-set">
													<span class="ve_top">伸缩阈值：<font color="red">*</font></span>
													<input type="number" placeholder="60"><span
														class="unit">%</span> <span
														style="color: grey; margin-left: 40px;">CPU资源使用率</span>
												</div>
											</li>

											<li class="line-h-3">
												<div class="param-set">
													<span class="ve_top" id= "ve_cpu">CPU数量：<font color="red">*</font></span>
													<c:forEach items="${cpuSizeList }" var="cpuSize" >
														<label><input type="radio" class="cpuNum" name="cpuNum" value="${cpuSize }">${cpuSize }
														<span>个 </span></label>
													</c:forEach>
													<span style="color: #1E90FF; margin-left: 50px;">当前可用cpu数量：${leftcpu }个</span>
													<input type = "hidden" id = "leftcpu" value = "${leftcpu }"/>
												</div>

											</li>
											<li class="line-h-3">
												<div class="param-set">
													<span class="ve_top" id = "ve_ram" >内存：<font color="red">*</font></span>
													<c:forEach items="${memorySizeList }" var="memorySize" >
														<label><input type="radio" class="ram" name="ram" value="${memorySize.memoryValue }">${memorySize.memorySize }<span> G  </span></label>
													</c:forEach>
													<span style="color: #1E90FF; margin-left: 60px;">当前可用内存：${leftmemory }G</span>
													<input type="hidden" id = "leftmemory" value = "${leftmemory * 1024 }"/>
												</div>
											</li>

											<li id="configmap" class="line-h-3" ><span class="ve_top">配置文件模板：</span>
												  <select class="selectVolume self-define " id="configmap" name="configmap" style="height: 34px; width: 230px;">
                                                    <option value="-1" >NONE</option>
                                                    <c:forEach items="${configmapList}" var="configmap">
                                                       <option value="${configmap.id}">${configmap.name}</option>
                                                    </c:forEach>
                                                  </select>
                                                  <span> 挂载路径：</span>
                                                  <input type="text" class="self-define " style="width: 330px;" id = "configmapPath" name ="configmapPath" value="/configfiles" />
											</li>

											<li id="service_type"><span class="ve_top">服务类型：</span>
												<span class="update-mi"> <input type="checkbox"
													id="state_service" stateless="0"> <label
													for="state_service"><font color="blue">有状态服务</font></label>
													<span class="mountTips"></span>
												<input type="hidden" id = "serviceType" name ="serviceType" value = "1"/>
											</span></li>

											<li class="hide-set" id="save_roll_dev">
												<ol id="mountPathList">

													<li class="hide-select hide">
														<select class="selectVolumeAddItme form-control"
																		style="height: 30px; width: 98%;margin:0 auto;padding-top:4px;margin-left:4px">
																<option  value="">选择一个存储卷</option>
																<c:forEach items="${storageList }" var = "storage">
																    <option  value="${storage.id }">${storage.storageName }  ${storage.storageSize }M</option>
																</c:forEach>
													    </select>
														<input type="text" id="mountPath" class="form-control" value="" />
														<!-- <a id="addVolume"><i class="fa fa-plus"></i>添加</a> -->
													</li>
													<li>
														<table class="table table-hover enabled" id="volPath">
															<thead>
																<tr>
																	<th style="width: 45%">存储卷</th>
																	<th style="width: 45%">挂载地址</th>
																	<th style="width: 10%">操作</th>
																</tr>
															</thead>
															<tbody id="volList">
															</tbody>
															<tfoot class="addTfootBtn">
																<tr id="addVolume">
																	<td colspan="3"><i class="fa fa-plus margin"></i>添加挂载</td>
																</tr>
															</tfoot>
														</table>
														<input type="hidden" id="cephAds" name = "cephAds" value="" />
													</li>
												</ol></li>
											<li class="hide-set"><span class="ve_top">环境变量：</span>
												<input type = "hidden" id = "userName" value="${userName }"/>
												<ol>
													<li class="hide-select">
												       <!-- <input type="text" class="form-control" placeholder="name" id="Name">
												       <input type="text" class="form-control" placeholder="value" id="Value">
												       <a id="cratePATH"><i class="fa fa-plus"></i>添加</a> -->
														<div style="float: left;">
															<span id="importBtn" class=" btn-info btn-sm"
																style="cursor: pointer">导入模板</span> <span id="exportBtn"
																class=" btn-info btn-sm" style="cursor: pointer">另存为模板</span>
														</div>
													</li>
													<li>
														<table class="table table-hover enabled" id="Path">
															<thead>
																<tr>
																	<th style="width: 45%">键</th>
																	<th style="width: 45%">值</th>
																	<th style="width: 10%">操作</th>
																</tr>
															</thead>
															<tbody id="Path-oper1">
																<input type="hidden" id="arrayKey" value="APM,id,name" />
															</tbody>
															<tfoot class="addTfootBtn">
																<tr id="cratePATH">
																	<td colspan="3"><i class="fa fa-plus margin"></i>添加环境变量</td>
																</tr>
															</tfoot>
														</table>
													</li>
												</ol></li>
											<li class="hide-set"><span class="ve_top">端口配置：</span>
												<table class="table enabled">
													<thead>
														<tr>
															<th style="width: 35%">容器端口</th>
															<th style="width: 35%">协议</th>
															<th style="width: 15%">映射端口</th>
															<th style="vertical-align: middle; width: 8.9%">操作</th>
														</tr>
													</thead>
													<tbody class="BORDER" id="pushPrptpcol">
														<!-- <tr class="plus-row">
															<td><input class="port" type="text" value="8080"></td>
															<td><select class="T-http">
																	<option>TCP</option>
																	<option>UDP</option>
															</select></td>
															<td><i>30099</i></td>
															<td><a href="javascript:void(0)"
																onclick="deletePortRow(this)" class="gray"> <i
																	class="fa fa-trash-o fa-lg"></i>
															</a></td>
														</tr> -->
													</tbody>
													<tfoot class="addTfootBtn">
														<tr id="createPort">
															<td colspan="4"><i class="fa fa-plus margin"></i>添加端口</td>
														</tr>
													</tfoot>
												</table>
											</li>
										</ul>
									</div>
								</form>
								<!-- 环境变量导入模板 -->
								<div id="environment-variable" style="display:none; max-height:360px;overflow-y:scroll;overflow-x:hidden;">
									<table class="table table-hover enabled" id="Path-table"
										style="width: 326px; margin: 5px 10px 5px 10px">
										<tbody id="Path-env">

										</tbody>
									</table>
								</div>
								<!-- 环境变量另存为模板 -->
								<div id="environment-template" style="display:none; max-height:170px;overflow-y:scroll;overflow-x:hidden;">
									<div style="width: 326px; margin: 5px 10px 5px 10px">
										<span>模板名称：</span><input type="text" id="envTemplateName"
											style="width: 77%" autofocus="autofocus" />
									</div>
								</div>
							</div>
						</div>
						<div class="createPadding">
							<button class=" btn btn-default go_backs">上一步</button>
							<!-- <button class="btn btn-success two_step hide">高级设置</button> -->
							<button id="createButton"
								class="pull-right btn btn-primary btn-color pull_confirm">创建</button>
						</div>

					</div>
				</div>
			</div>
		</article>
	</div>


</body>
</html>
