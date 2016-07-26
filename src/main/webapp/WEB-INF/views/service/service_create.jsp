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
			deploy(imgID, imageName, imageVersion, resourceName,portConfigs);
		}
	});
</script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">创建服务</li>
					</ol>
				</div>
				<div class="contentMain">


					<div class="modalCrealApp">

						<div class="steps-main">
							<div class="progre">
								<ul style="padding: 0 4rem;">
									<li class="radius_step action"><span>1</span> 镜像来源</li>
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
											<button id="searchimage" class="btn btn-primary btn-send">搜索</button>

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
									<input type="hidden" id="templateName" name="templateName" value=""></input>
									
									<div class="host_step2">
										<ul class="safeSet">
											<li class="line-h-3"><span class="ve_top">镜像名称：</span> <input
												type="text" value="" class="in_style" id="imgName"
												name="imgName" readOnly="readOnly"> <input
												type="hidden" value="" id="imgID" name="imgID"></li>
											<li class="line-h-3"><span class="ve_top">镜像版本：</span> <input
												type="text" value="" class="in_style" id="imgVersion"
												name="imgVersion" readOnly="readOnly"></li>
											<li class="line-h-3"><span class="ve_top">服务名称：</span> <input
												type="text" value="" class="in_style" id="serviceName"
												name="serviceName"> <input type="hidden" value=""
												class="in_style" id="resourceName" name="resourceName">
											</li>
											<li id="service_type"><span class="ve_top">启动命令：</span>
												<span class="update-mi"> <input type="checkbox"
													id="startCommand"> <label for="startCommand"><font
														color="blue">自定义启动命令</font></label>
											</span></li>
											<li class="line-h-3" id="startCommand_li"><input
												type="text" value="" class="in_style"
												id="startCommand_input" name="startCommand"
												style="margin-left: 120px"></li>
											<li class="line-h-3"><span class="ve_top">服务访问路径：</span>
												<input type="text" value="" class="in_style" id="webPath"
												name="servicePath"></li>
											<c:if test="${DMZ!=null && USER!=null  }">
												<li class="line-h-3"><span class="ve_top">nginx代理区域：</span>
													<label class="checkbox-inline"> <input
														type="checkbox" id="${DMZ }" name="nginxserv"
														value="${DMZ }"> DMZ区
												</label> <label class="checkbox-inline"> <input
														type="checkbox" id="${USER }" name="nginxserv"
														value="${USER }"> USER区
												</label> <input type="hidden" value="" class="in_style"
													id="proxyZone" name="proxyZone"></li>
											</c:if>
											<li class="line-h-3"><span class="ve_top">nginx代理路径：</span>
												<input type="text" value="" class="in_style" id="nginxPath"
												name="proxyPath"></li>


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
													<span class="number-title">实例数量：</span> <input
														type="number" value="1" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="instanceNum"
														name="instanceNum"> <span class="unit">个</span>
													<!-- <span style="color: grey;margin-left: 50px;">当前可用实例数量：${leftpod }</span> -->
													<span class="dynamic-scale"> <input type="checkbox"
														id="dynamic-service"> <label for="dynamic-service"><font
															color="blue">自动化伸缩</font></label>
													</span>

												</div>
											</li>
											<li class="line-h-3">
												<div class="param-set">
													<span class="number-title">伸缩范围：</span><input type="number"
														value="1" class="number" min="1">
													<span class="unit">个</span> <span
														style="margin-left: 15px">~</span> <input type="number"
														value="5" class="number"> <span class="unit">个</span>
												</div>


											</li>
											<li class="line-h-3">
												<div class="param-set">
													<span class="number-title">CPU数量：</span> <input
														type="radio" class="cpuNum" name="cpuNum" value="1"
														placeholder="当前可用cpu数量：${leftcpu }个">1<span>个</span>
													<input type="radio" class="cpuNum" name="cpuNum" value="2"
														placeholder="当前可用cpu数量：${leftcpu }个">2<span>个</span>
													<input type="radio" class="cpuNum" name="cpuNum" value="4"
														placeholder="当前可用cpu数量：${leftcpu }个">4<span>个</span>
													<span style="color: #1E90FF; margin-left: 50px;">当前可用cpu数量：${leftcpu }个</span>
												</div>

											</li>
											<li class="line-h-3">
												<div class="param-set">
													<span class="number-title">内存：</span> <input type="radio"
														class="ram" name="ram" value="2048">2<span>G</span>
													<input type="radio" class="ram" name="ram" value="4098">4<span>G</span>
													<input type="radio" class="ram" name="ram" value="8192">8<span>G</span>
													<span style="color: #1E90FF; margin-left: 60px;">当前可用内存：${leftmemory }G</span>
												</div>
											</li>
											<li id="service_type"><span class="ve_top">服务类型：</span>
												<span class="update-mi"> <input type="checkbox"
													id="state_service" stateless="0"> <label
													for="state_service"><font color="blue">有状态服务</font></label>
													<span class="mountTips"></span>
											</span></li>
											<li class="hide-set" id="save_roll_dev">
												<ul id="mountPathList">
													<li class="mount line-h-3">
														<!-- <span class="ve_top">挂载地址：</span> -->
														<table class="pull-left" style="margin-left: 120px;">
															<tbody>
																<tr>
																	<td><input type="text" id="mountPath"
																		name="mountPath" value="/var/lib/mysql" /></td>
																	<td style="padding-left: 10px;"><select
																		class="selectVolume" id="selectVolume" name="volName"
																		style="height: 30px; width: 230px;">
																			<option name="volName" value="0">选择一个存储卷</option>

																	</select></td>
																	<!-- <td>
                                                        <ins class="ins">
                                                            <input type="checkbox" id="readOnlyOp1" name="isVolumeReadOnly"
                                                                   class="isVolumeReadonly"> <label for="readOnlyOp1" style="margin-bottom:0px;">只读</label>
                                                        </ins>
                                                        <a class="refreshVolume" title="刷新"
                                                           href="javascript:void(0)"> <i
                                                                class="fa fa-refresh fa-lg" style="font-size: 16px"></i>
                                                        </a> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <a
                                                            class="delVolume" title="删除" href="javascript:void(0)">
                                                        <i class="fa fa-trash-o" style="font-size: 16px"></i>
                                                    	</a>
                                                    </td> -->
																</tr>
															</tbody>
														</table>
													</li>

													<!-- <li class="line-h-3"><span class="ve_top">镜像设置：</span> <span
                                                class="update-mi"><input type="checkbox" id="pullPolicy"
                                                                         name="pullPolicy" checked=""> <label
                                                for="pullPolicy">更新镜像</label></span>
                                        </li> -->
												</ul> <!-- <div style="height: 80px !important;"></div> -->
											</li>
											<li class="hide-set"><span class="ve_top">环境变量：</span>
												<ol>
													<li class="hide-select"><input type="text"
														placeholder="name" id="Name"> <input type="text"
														placeholder="value" id="Value"> <a id="cratePATH"><i
															class="fa fa-plus"></i>添加</a>
														<div style="float: right">
															<span id="importBtn" class=" btn-info btn-sm"
																style="cursor: pointer">导入模板</span> <span id="exportBtn"
																class=" btn-info btn-sm" style="cursor: pointer">另存为模板</span>
														</div></li>
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
																<input type="hidden" id = "arrayKey" value="" />
															</tbody>
														</table>
													</li>
												</ol></li>
											<li class="hide-set"><span class="ve_top">端口配置：</span>
												<table class="table enabled">
													<thead style="background: #fafafa">
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
																	<option>HTTP</option>
															</select></td>
															<td><i>30099</i></td>
															<td><a href="javascript:void(0)"
																onclick="deletePortRow(this)" class="gray"> <i
																	class="fa fa-trash-o fa-lg"></i>
															</a></td>
														</tr> -->
													</tbody>
												</table>
												<div class="createPort">
													<span id="createPort"><i class="fa fa-plus margin"></i>添加端口</span>
												</div></li>
										</ul>
									</div>

									<%-- 高级设置 --%>
									<div class="host_step3 hide" style="height: auto;">
										<ul class="advanced">
											<li class="hide-set"><span class="ve_top">链接服务：</span>
												<ol class="pull-left" style="width: 100%;">
													<li class="hide-select"><select class="col col-4"
														id="linkcontainers"
														style="min-width: 100px; height: 28px;">
															<option>mysql</option>
													</select> <input id="containernickname" type="text"
														placeholder="value"> <a id="addcontainer"
														class="cursor">添加</a></li>

													<li>
														<table id="linkcontainertb"
															class="table table-hover enabled" style="display: none">
															<thead>
																<tr>
																	<th style="">服务</th>
																	<th style="width: 40%">名称</th>
																	<th style="width: 20%">操作</th>
																</tr>
															</thead>
															<tbody id="linkcontainerbody"></tbody>
														</table>
													</li>
												</ol></li>
											<li class="hide-set"><span class="ve_top">环境变量：</span>
												<ol class="pull-left" style="width: 100%;">
													<li class="hide-select"><input type="text"
														placeholder="name" id="Name"> <input type="text"
														placeholder="value" id="Value"> <a id="cratePATH">添加</a>
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
															<tbody id="Path-oper">
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled="" value="PATH"></td>
																	<td class="vals"><input type="text"
																		placeholder="value"
																		value="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin">
																	</td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue"
																		value="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin">
																	</td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled="" value="DEBIAN_FRONTEND"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="noninteractive"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="noninteractive"></td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled="" value="MYSQL_USER"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="admin"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="admin"></td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled="" value="MYSQL_PASS"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="**Random**"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="**Random**"></td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled=""
																		value="REPLICATION_MASTER"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="**False**"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="**False**"></td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled=""
																		value="REPLICATION_SLAVE"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="**False**"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="**False**"></td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled=""
																		value="REPLICATION_USER"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="replica"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="replica"></td>
																</tr>
																<tr>
																	<td class="keys"><input type="text"
																		style="width: 98%" disabled=""
																		value="REPLICATION_PASS"></td>
																	<td class="vals"><input type="text"
																		placeholder="value" value="replica"></td>
																	<td class="func"><a href="javascript:void(0)"
																		onclick="deleteRow(this)" class="gray"><i
																			class="fa fa-trash-o fa-lg"></i></a><input type="hidden"
																		class="oldValue" value="replica"></td>
																</tr>
															</tbody>
														</table>
													</li>
												</ol></li>
											<li class="hide-set"><span class="ve_top">端口配置：</span>
												<table class="table enabled">
													<thead style="background: #fafafa">
														<tr>
															<th style="width: 35%">容器端口</th>
															<th style="width: 35%">协议</th>
															<th style="width: 15%">映射端口</th>
															<th style="vertical-align: middle; width: 8.9%">操作</th>
														</tr>
													</thead>
													<tbody class="BORDER" id="pushPrptpcol2">
														<tr class="plus-row">
															<td><input class="port" type="text" disabled=""
																value="3306"></td>
															<td><select class="T-http">
																	<option>TCP</option>
																	<option>HTTP</option>
															</select></td>
															<td><i>动态生成</i></td>
															<td><a href="javascript:void(0)"
																onclick="_deletePortRow(this)" class="gray"> <i
																	class="fa fa-trash-o fa-lg"></i>
															</a></td>
														</tr>
														<tr class="plus-row">
															<td><input class="port" type="text"></td>
															<td><select class="T-http">
																	<option>TCP</option>
																	<option>HTTP</option>
															</select></td>
															<td><i>动态生成</i></td>
															<td><a href="javascript:void(0)"
																onclick="_deletePortRow(this)" class="gray"> <i
																	class="fa fa-trash-o fa-lg"></i>
															</a></td>
														</tr>
													</tbody>
												</table>
												<div class="createPort">
													<span><i class="fa fa-plus margin"></i></span>
												</div></li>
										</ul>
										<div style="height: 80px !important;"></div>
									</div>
								</form>
								<div id="environment-variable">
									<table class="table table-hover enabled" id="Path-table" style="width: 345px; margin: 5px 10px 5px 10px">
										<tbody id="Path-env">
											<c:forEach var = "templateName" items = "${templateNames }">
												<tr>
													<td class="vals vals-env">${templateName }
														<span class="vals-path hide"><i class="fa fa-check"></i></span>
														<input type="hidden" class="templateName" value="${templateName }" />
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
								<!-- 环境变量另存为模板 -->
								<div id="environment-template">
									<div style="width: 345px; margin: 5px 10px 5px 10px">
										<span>模板名称：</span><input type="text" id="envTemplateName" style="width:77%" autofocus="autofocus" />
									</div>
								</div>
							</div>
						</div>
						<div class="createPadding">
							<button class=" btn btn-default go_backs">上一步</button>
							<!-- <button class="btn btn-success two_step hide">高级设置</button> -->
							<button id="createButton"
								class="pull-right btn btn-primary pull_confirm">创建</button>
						</div>

					</div>