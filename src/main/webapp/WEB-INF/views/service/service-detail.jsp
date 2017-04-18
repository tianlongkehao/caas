<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href= "<%=path%>/plugins/datetimepicker/css/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
	
<link rel="stylesheet" href="<%=path%>/plugins/xterm/build/xterm.css" />
<link rel="stylesheet" href="<%=path%>/plugins/xterm/build/addons/fullscreen/fullscreen.css" />
<link rel="stylesheet" href="<%=path%>/css/mod/xtermStyle.css" />
      	
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/xterm.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/addons/attach/attach.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/addons/fit/fit.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/addons/fullscreen/fullscreen.js" ></script>
<%-- <script type="text/javascript" src="<%=path%>/plugins/xterm/build/jquery-1.11.3.min.js"></script> --%>
<script type="text/javascript" src="<%=path%>/plugins/datetimepicker/js/jquery-ui-slide.min.js"></script>
<script type="text/javascript" src="<%=path%>/plugins/datetimepicker/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="<%=path%>/js/service/service-detail.js"></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/main.js" defer ></script>
<%-- <script type="text/javascript" src="<%=path%>/plugins/xterm/app.js" defer ></script> --%>
</head>
<body>

	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i><span id="nav1">&nbsp;&nbsp;控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li><a href="<%=path %>/service"><span id="nav2">服务管理</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width:200px">${service.serviceName }</li>
					</ol>
					<input hidden="true" value="${service.id }" id="serId"/>
					<input hidden="true" value="${service.serviceName }" id="serName"/>
					<input hidden="true" value="${service.status }" id="serStatus"/>
			        <input hidden="true" value="${service.serviceType }" id="serType"/>
				</div>
				<div class="contentMain">
					<section class="detail-succeed">
						<div class="icon-img">
							<div class="type-icon">
								<img src="<%=path%>/images/podSimple.png" height="100%">
							</div>
						</div>
						<ul class="succeed-content pull-left">
							<c:if test="${service.status==1 }">
								<li>运行状态：未启动</li>
							</c:if>
							<c:if test="${service.status==2||service.status==3||service.status==6 }">
								<li>运行状态：正在运行</li>
							</c:if>
							<c:if test="${service.status==4 }">
								<li>运行状态：已停止</li>
							</c:if>
							<%-- <li>服务地址：<a
								href="${service.serviceAddr}/${service.proxyPath}"
								target="_blank">${service.serviceAddr}/${service.proxyPath}</a></li> --%>
							<li class="oldCon">服务地址：<a href="${service.serviceAddr}/${service.proxyPath}"target="_blank">
								<span id="oldServiceAddr">${service.serviceAddr}</span>/<span id="oldProxyPath">${service.proxyPath}</span>
								</a>
								<a id="editServiceAddrBtn" style="margin-left:20px" class="fa fa-edit"></a>
							</li>
							<li class="editCon">服务地址：
							  <prex id=addrPrex ></prex>
<%-- 							  	<input id="editServiceAddrValue" type="hidden" value="${service.serviceAddr}"> --%>
								<input id="editServiceAddr" type="text" value="${service.serviceAddr}">/
								<input id="editProxyPath" type="text" value="${service.proxyPath}">
								<i id="saveEdit" style="margin-left:20px" class="fa fa-save"></i>
								<i id="canclEdit" style="margin-left:6px" class="fa fa-times"></i>
								</li>
							<li class="oldCon1">责任人：${service.responsiblePerson } ${service.responsiblePersonTelephone }
								<a id="editResponseBtn" style="margin-left:20px" class="fa fa-edit"></a></li>
							<li class="editCon1">责任人：
								<input id="editResponsiblePerson" type="text" value="${service.responsiblePerson}">/
								<input id="editResponsiblePersonTelephone" type="text" value="${service.responsiblePersonTelephone}">
								<i id="saveEdit1" style="margin-left:20px" class="fa fa-save"></i>
								<i id="canclEdit1" style="margin-left:6px" class="fa fa-times"></i>
								</li>
							<li>创建时间：${service.createDate }</li>
							<li>更新时间：${service.updateDate }</li>
						</ul>
						<div class="applocation">
							<a href="${service.serviceAddr}/${service.proxyPath}"
								target="_blank" class="open">打开应用</a>
						</div>
					</section>

					<div class="baseInfo center-style">
						<ul class="nav navbar-nav">
							<li><a class="BASE btn-prim serDetail">基本信息</a></li>
							<li><a class="INSTANCES serDetail">容器实例</a></li>
							<li><a class="SERVOL serDetail">挂载地址</a></li>
							<li><a class="ENVS serDetail">环境变量</a></li>
							<li><a class="PORTS serDetail">端口</a></li>
							<li class="dropdown">
								<a class="dropdown-toggle serDetail" id="dropdown-log"
									data-toggle="dropdown"> 日志 <b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
								 	<c:forEach items="${podNameList}" var="pod" >
								 		<li class="LOG"><a class="dropdown-pod" podName="${pod.podName }" serviceid="${service.id }" value="2" onclick="dropdownLog(this)"
								 			style="width: 100%;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;" title="${pod.podName }">${pod.podName }</a></li>
								 	</c:forEach>

								</ul>
							</li>
							<li class="dropdown">
								<a class="dropdown-toggle serDetail" id="dropdown-log"
									data-toggle="dropdown"> 终端 <b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
								 	<c:forEach items="${podNameList}" var="pod" >
								 		<li class="CMD"><a class="dropdown-pod" podName="${pod.podName }" serviceid="${service.id }" value="2" onclick="dropdownCMD(this)"
								 			style="width: 100%;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;" title="${pod.podName }">${pod.podName }</a></li>
								 	</c:forEach>

								</ul>
							</li>
						</ul>
					</div>

					<div id="edit_serAddr" hidden="true">
		                <div style="width: 345px; margin: 5px 10px 5px 10px">
		                     <p>新服务地址：<p1 id="SerAddrPrex">http://${service.serviceName }.</p1><input type="text" name="newSerAddr" id="newSerAddr" /></p>
		                </div>
         			</div>
         			<!-- Dlag -->

					<div class="containerInfo">
					<form id="BaseSerForm" name="BaseSerForm"
					 action="<%=path%>/service/detail/editBaseSerForm.do" >
					  <input hidden="true" value="${service.id }"  name="id"/>
						<table class="table w50">
							<thead>
								<tr>
									<th>基本信息</th>
									<th>
										<div style="float:right; margin-right:30px; color:#337ab7; font-size:19px">
											<c:if test="${service.status==1 or service.status==4}">
												<i class="fa fa-edit" id="editSerBtn" name="editSerBtn" title="修改"></i>
											</c:if>
											<i class="fa fa-reply" id="restSerBtn" name="restSerBtn" title="还原"></i>
											<i class="fa fa-save" id="saveSerBtn" name="saveSerBtn" title="保存"></i>
											<i class="fa fa-times" id="canclSerBtn" name="canclSerBtn" title="取消"></i>
										</div>
									</th>
								</tr>
							</thead>
							<tbody class="BORDER">
								<tr>
									<td>服务名称：
									<span id="oldSerName" class="oldBaseCon">${service.serviceName }</span>
									<span id="editSerName" hidden="true" class="editBaseCon">
									   <input id="serviceName" name="serviceName" type="text" value=${service.serviceName } />
									</span>
									</td>
									<c:if test="${service.serviceChName == '' }">
									<td>服务中文名称：未设置</td>
									</c:if>
									<c:if test="${service.serviceChName != '' }">
									<td>服务中文名称：${service.serviceChName }</td>
									</c:if>
								</tr>
								<tr>
									<c:if test="${service.status==1 }">
										<td>运行状态：未启动</td>
									</c:if>
									<c:if test="${service.status==2||service.status==3 }">
										<td>运行状态：正在运行</td>
									</c:if>
									<c:if test="${service.status==4 }">
										<td>运行状态：已停止</td>
									</c:if>
									<c:if test="${service.status==6 }">
										<td>运行状态：调试中</td>
									</c:if>
									<td>镜像名称：${service.imgName } : ${service.imgVersion }</td>
								</tr>
								<tr>
									<td>创建时间：${service.createDate }</td>
								</tr>
							</tbody>
						</table>
						<table class="table basicInfo w50">
							<thead>
								<tr>
									<th>配置信息</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody class="BORDER">
								<tr>
									<td>CPU：${service.cpuNum }</td>
									<td>内存：${service.ram }MB</td>
								</tr>
								<tr>
									<td>启动命令：
										<c:if test="${service.startCommand == '' }">
											<span id="oldStartComm" class="oldBaseCon">默认</span>
										</c:if>
										<c:if test="${service.startCommand != '' }">
											<span id="oldStartComm" class="oldBaseCon">${service.startCommand }</span>
										</c:if>
						                <span id="editStartComm" hidden="true" class="editBaseCon">
						                     <input id="startCommand_input" name="startCommand" type="text" value="${service.startCommand }" />
						                </span>
									</td>
									<td >服务访问路径：
										<span class="oldBaseCon_Run oldBaseCon">${service.servicePath }</span>
										<span id="editSerPath" hidden="true" class="editBaseCon editBaseCon_Run">
	                   					<input id="webPath" name="servicePath" type="text" value="${service.servicePath }" />
	                					</span>
									</td>
								</tr>
								<tr>
									<c:if test="${service.proxyZone == '' ||service.proxyZone == null}">
									<td>nginx代理区域：
										<span class="oldBaseCon_Run oldBaseCon">未设置</span>
										<span id="editProxyZone" hidden="true" class="editBaseCon_Run editBaseCon">
						                  <c:if test="${fn:contains(service.proxyZone,'dmz')==true}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz" checked="checked"> DMZ区
						                   </label>
						                  </c:if>
						                  <c:if test="${fn:contains(service.proxyZone,'dmz')==false}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz"> DMZ区
						                   </label>
						                  </c:if>

						                  <c:if test="${fn:contains(service.proxyZone,'dmz1')==true}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz1" checked="checked"> DMZ1区
						                   </label>
						                  </c:if>
						                  <c:if test="${fn:contains(service.proxyZone,'dmz1')==false}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz1"> DMZ1区
						                   </label>
						                  </c:if>

						                  <c:if test="${fn:contains(service.proxyZone,'user')==true}">
						                  <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="user" checked="checked"> USER区
						                	</label>
						                  </c:if>
						                  <c:if test="${fn:contains(service.proxyZone,'user')==false}">
						                  <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="user"> USER区
						                	</label>
						                  </c:if>

						                </span>
									</td>
									</c:if>
									<c:if test="${service.proxyZone != '' && service.proxyZone != null}">
									<td>nginx代理区域:
										<span class="oldBaseCon_Run oldBaseCon">${service.proxyZone }</span>
									 	<span id="editProxyZone" hidden="true" class="editBaseCon_Run editBaseCon">
						                  <c:if test="${fn:contains(service.proxyZone,'dmz')==true}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz" checked="checked"> DMZ区
						                   </label>
						                  </c:if>
						                  <c:if test="${fn:contains(service.proxyZone,'dmz')==false}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz"> DMZ区
						                   </label>
						                  </c:if>

						                  <c:if test="${fn:contains(service.proxyZone,'dmz1')==true}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz1" checked="checked"> DMZ1区
						                   </label>
						                  </c:if>
						                  <c:if test="${fn:contains(service.proxyZone,'dmz1')==false}">
						                   <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="dmz1"> DMZ1区
						                   </label>
						                  </c:if>

						                  <c:if test="${fn:contains(service.proxyZone,'user')==true}">
						                  <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="user" checked="checked"> USER区
						                </label>
						                  </c:if>
						                  <c:if test="${fn:contains(service.proxyZone,'user')==false}">
						                  <label class="checkbox-inline"> <input
						                      type="checkbox"  name="proxyZone"
						                      value="user"> USER区
						                </label>
						                  </c:if>
						                </span>
									</td>
									</c:if>
									<td>nginx代理路径：
										<span id="oldProxyPath" class="oldBaseCon_Run oldBaseCon">${service.proxyPath }</span>
					                   <span id="editProxyPath" hidden="true" class="editBaseCon_Run editBaseCon">
						                   <input id="nginxPath" name="proxyPath" type="text" value="${service.proxyPath }" />
						                </span>
									</td>
								</tr>
								<tr>
									<td>会话黏连方式：
										<span class="oldBaseCon_Run oldBaseCon">
										<c:if test="${service.sessionAffinity == '' || service.sessionAffinity == null}">
										未配置
					                    </c:if>
					                    <c:if test="${service.sessionAffinity == 'ClientIP'}">
					                    ${service.sessionAffinity }
					                    </c:if></span>
					                    <span id="editSeAffy" hidden="true" class="editBaseCon_Run editBaseCon">
					                  <%--  <input id="newSeAffy" name="sessionAffinity" type="text" value=${service.sessionAffinity } /> --%>
					                    <select id="newSeAffy" name="sessionAffinity">

						                    <option name="sessionAffinity" value="" >NONE</option>
						                    <option name="sessionAffinity" value="ClientIP" <c:if test="${service.sessionAffinity == 'ClientIP'}">selected</c:if>>ClientIP</option>

					                    </select>
					                	</span>
									</td>
									<td>NodeIp黏连方式：
										<span class="oldBaseCon_Run oldBaseCon">
										<c:if test="${service.nodeIpAffinity == '' || service.nodeIpAffinity == null}">
										未配置
										</c:if>
										<c:if test="${service.nodeIpAffinity == 'nodeIpAffinity' }">
						                  ${service.nodeIpAffinity }
						                 </c:if></span>
						                   <span id="editNodAffy" hidden="true" class="editBaseCon_Run editBaseCon">
						                   <%-- <input id="newNodAffy" name="nodeIpAffinity" type="text" value=${service.nodeIpAffinity } /> --%>
						                   <select  id="newNodAffy" name="nodeIpAffinity" >
						                   <option name="nodeIpAffinity" value="" >NONE</option>
						                   <option name="nodeIpAffinity" value="nodeIpAffinity" <c:if test="${service.nodeIpAffinity == 'nodeIpAffinity' }">selected</c:if> >nodeIpAffinity</option>
						                   </select>
						                </span>
									</td>
								</tr>
								<c:if test="${service.checkPath=='' }">
                 				<tr  class="editBaseCon_Run editBaseCon">
									<td>检查状态：
                                       <span class="oldBaseCon_Run oldBaseCon">${service.checkPath }</span>
					                   <span id="editCheckPath" hidden="true" class="editBaseCon_Run editBaseCon">
					                   <input id="checkSerStatus_input" name="checkPath" type="text" value="${service.checkPath }" />
					                </span>
                                    </td>
                                    <td>检测延迟：
                                       <span class="oldBaseCon_Run oldBaseCon">${service.initialDelay }</span>
					                   <span id="editInitDelay" hidden="true" class="editBaseCon_Run editBaseCon">
												<input type="number" value="" id="initialDelay"
														onkeyup="this.value=this.value.replace(/\D/g,'')" min="0" name="initialDelay">
					                   </span>s
					                </td>
		          				</tr>
					            <tr  class="editBaseCon_Run editBaseCon">
					                <td>检测超时：
	                                    <span class="oldBaseCon_Run oldBaseCon">${service.timeoutDetction }</span>
					                    <span id="editTiOut" hidden="true" class="editBaseCon_Run editBaseCon">
												<input type="number" value="" id="timeoutDetction"
													onkeyup="this.value=this.value.replace(/\D/g,'')" min="0" name="timeoutDetction">
					                    </span>s
				                    </td>
                                    <td>检测频率：
	                                    <span class="oldBaseCon_Run oldBaseCon">${service.periodDetction }</span>
						                   <span id="editPeriod" hidden="true"
												class="editBaseCon_Run editBaseCon">
												<input type="number" value="" id="periodDetction"
													onkeyup="this.value=this.value.replace(/\D/g,'')" min="1" name="periodDetction">
											</span>s
											</td>
             					</tr>
								</c:if>
								<c:if test="${service.checkPath!='' }">
									<tr>
										<td>检查状态：
											<span class="oldBaseCon_Run oldBaseCon">${service.checkPath }</span>
						                    <span id="editCheckPath" hidden="true" class="editBaseCon_Run editBaseCon">
						                    <input id="checkSerStatus_input" name="checkPath" type="text" value="${service.checkPath }" />
						                    </span>
										</td>
										<td>检测延迟：
											<span class="oldBaseCon_Run oldBaseCon">${service.initialDelay }</span>
						                    <span id="editInitDelay" hidden="true" class="editBaseCon_Run editBaseCon">
												<input type="number" value="" id="initialDelay" placeholder="${service.initialDelay }"
														onkeyup=" this.value=this.value.replace(/\D/g,'')" min="0" name="initialDelay">
											</span>s
						                </td>
									</tr>
									<tr>
										<td>检测超时：
											<span class="oldBaseCon_Run oldBaseCon">${service.timeoutDetction }</span>
						                   	<span id="editTiOut" hidden="true" class="editBaseCon_Run editBaseCon">
												<input type="number" value="" id="timeoutDetction" placeholder="${service.timeoutDetction }"
													onkeyup="this.value=this.value.replace(/\D/g,'')" min="0" name="timeoutDetction">
											</span>s
					                    </td>
										<td>检测频率：
											<span class="oldBaseCon_Run oldBaseCon">${service.periodDetction }</span>
							                   <span id="editPeriod" hidden="true" class="editBaseCon_Run editBaseCon">
												<input type="number" value="" id="periodDetction" placeholder="${service.periodDetction }"
													onkeyup="this.value=this.value.replace(/\D/g,'')" min="1" name="periodDetction">
							                </span>s
						                </td>
									</tr>
								</c:if>
							</tbody>
						</table>
						</form>
					</div>

					<div class="containerInstances hide" style="min-height: 300px;">
						<table class="table">
							<thead>
								<tr style="white-space: nowrap;">
									<th>名称</th>
									<th>运行状态</th>
									<th>镜像</th>
									<th>内网地址</th>
									<th>外网地址</th>
									<th>创建时间</th>
								</tr>
							</thead>
							<tbody class="BORDER-TR">
								<c:forEach items="${containerList }" var="container">
									<c:choose>
										<c:when test="${service.status == 2 }">
											<c:set var="statusClass" value="fa_run"></c:set>
											<c:set var="eventStatusClass" value="fa-check"></c:set>
											<c:set var="statusName" value="成功"></c:set>
											<c:set var="eventStatus" value="success"></c:set>
										</c:when>
										<c:when test="${service.status == 4 }">
											<c:set var="statusClass" value="fa_stop"></c:set>
											<c:set var="eventStatusClass" value="fa-times"></c:set>
											<c:set var="statusName" value="失败"></c:set>
											<c:set var="eventStatus" value="error"></c:set>
										</c:when>
									</c:choose>
									<tr>
										<td><a href="">${container.containerName }</a></td>
										<c:if test="${service.status==1 }">
											<td>waiting</td>
										</c:if>
										<c:if test="${service.status==2||service.status==3||service.status==6 }">
											<td>Running</td>
										</c:if>
										<c:if test="${service.status==4 }">
											<td>stopping</td>
										</c:if>

										<td>${service.imgName }</td>
										<td>bonc:8080</td>
										<td><a href="${service.serviceAddr}/${service.proxyPath}"
											target="_blank">${service.serviceAddr}/${service.proxyPath}</a></td>
										<td>${service.createDate }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="bindDomain hide" style="min-height: 500px;">
						<section class="paddingrow">
							<div class="orgmain">
								<aside class="after-info">TenxCloud
									只允许备案过的域名绑定。为了完成域名绑定，你还需要在您的域名服务商处，将指定域名的CNAME指向我们为您生成的域名绑定专用地址。</aside>
							</div>
							<table class="table table-normal">
								<thead style="background: #F5F6F6;">
									<tr style="height: 40px;">
										<th class="w10">服务端口</th>
										<th class="w45">域名</th>
										<th class="w30">CNAME地址</th>
										<th class="w15">审核状态</th>
									</tr>
								</thead>
								<tbody id="addorg">
									<tr>
										<td class="w10"><span id="selectedPort"><span
												id="bindingPort" align="center">3306</span></span></td>
										<td class="first-td w45">
											<!-- <input id="domainname" type="text" placeholder='输入域名'> -->
											<div class="input-group">
												<input type="text" class="form-control" id="domainname"
													placeholder="输入域名"> <span class="input-group-addon"
													id="basic-addon"><i class="fa_btn_plus_gray pushOrg"></i></span>
											</div>
											<div class="input-group deletelines" style="margin-top: 5px;">
												<input type="text" class="form-control" placeholder="输入域名"
													value="127.0.0.1" disabled="disabled"> <span
													class="input-group-addon"><a
													href="javascript:void(0)" onclick="deleteline(this)"><i
														class="fa fa-trash-o fa-lg" style="margin-right: 3px;"></i></a></span>
											</div>
										</td>
										<td class="w30"><span id="cname">mysql-lynnxu.tenxapp.com:25314</span></td>
										<td class="w15"><span id="reviewStatus">已审核</span></td>
									</tr>
								</tbody>
							</table>
							<div class="bind-status" style="display: none;">添加成功！</div>
						</section>
					</div>
					<div class="serVolMapping hide" style="min-height: 500px;">
						<table class="table">
							<thead>
								<tr>
									<c:if test="${service.serviceType==1 }">
									<th>服务类型  ( 有状态服务 ) </th>
									<th>&nbsp;</th>
									</c:if>
									<c:if test="${service.serviceType==2 }">
									<th>服务类型  ( 无状态服务 ) </th>
									<th>&nbsp;</th>
									</c:if>

								</tr>

							</thead>
							<tbody class="BORDER-vol">
								<c:if test="${service.serviceType==1 }">
									<c:forEach items="${storageList }" var="storage">
										<tr>
											<td>挂载地址：${storage.mountPoint }</td>
											<td>存储卷：${storage.storageName }</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>
					<div class="envMapping hide" style="min-height: 500px;">
						<section class="paddingrow">
							<div class="envlabel hide"><label class="forEnvName">环境变量模板名称：</label>
								<input type="text" class="form-control envName" value="${envTemplate.templateName }" readonly>
							</div>
							<table class="table table-stripped table-hover" style="border: 1px solid #eee;">
								<thead style="background: #F5F6F6;">
									<tr style="height: 40px;">
										<th style="width:40%;text-indent: 15px;">键</th>
										<th>值</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="editEnvBody">
					               <c:forEach items="${envVariableList }" var="envVariable">
					                   <tr>
					                       <td style="width:40%;text-indent: 15px;">
					                       <span id="key${envVariable.envId}" class="oldEnv">${envVariable.envKey }</span>
					                       <span class="editEnv" hidden="true"><input class="envKey" type="text" name="envKey" value="${envVariable.envKey }"  /></span>
					                       <input class="envId" hidden="true" value="${envVariable.envId}" />
					                       </td>
					                       <td>
					                       <span id="value${envVariable.envId}" class="oldEnv">${envVariable.envValue }</span>
					                        <span class="editEnv" hidden="true"><input class="envValue" type="text" name="envValue" value="${envVariable.envValue }"  /></span>
					                       </td>
					                       <c:if test="${service.status==1 or service.status==4}">
					                          <c:if test="${envVariable.envKey!='APM' and envVariable.envKey!='namespace' and envVariable.envKey!='service'}">
						                          <td style="width:10%;" class="editBtn">
						                              <i onclick="editEnvBtn(this)"  type="button" value="修改"  class="fa fa-edit oldEnvConfig editEnvBtn"></i>
						                              <i onclick="saveEnvEdit(this)" hidden=true type="button" value="提交"  class="fa fa-save editEnv saveEnv"></i>
						                              <i onclick="canclEnvEdit(this)" hidden=true type="button" value="取消"  class="fa fa-times editEnv"></i>
						                              <i onclick="delEnvEdit(this)" type="button" value="删除"  class="fa fa-trash editEnvBtn"></i>
						                          </td>
					                          </c:if>
					                        </c:if>
					                   </tr>
					               </c:forEach>
								</tbody>
							</table>
							<c:if test="${service.status==1 or service.status==4}">
							<div class="createPort" style="background: #fafafa">
								<span id="addEnvBtn" onclick="addEnvClick()" value="添加环境变量"><i class="fa fa-plus margin"></i>添加环境变量</span>
							</div>
							</c:if>
							<div id="createEnv-templat" hidden="true">
			                	<div>
			                    	<p><span class="newEnvLab">Key：</span><input type="text" name="newKey" id="newKey" class="newEnvInput form-control" /></p>
			                    	<p><span class="newEnvLab">Value：</span><input type="text" name="newValue" id="newValue" class="newEnvInput form-control" /></p>
			                 	</div>
			            	</div>
						</section>
					</div>
					<div class="portMapping hide" style="min-height: 500px;">
						<section class="paddingrow">
							<table class="table table-normal" style="border: 1px solid #eee;">
								<thead style="background: #F5F6F6;">
									<tr style="height: 40px;">
										<td style="width:10%;text-indent: 15px;">名称</td>
										<td style="width:10%;">容器端口</td>
										<td style="width:10%;">协议</td>
										<td style="width:10%;">映射端口</td>
										<td style="width:50%;">服务地址</td>
										<td style="width:10%;">操作</td>
									</tr>
								</thead>
								<tbody id="editPortCfgBody">
									<c:forEach items="${portConfigList }" var="portConfig">
										<tr>
											<td style="width:10%;text-indent: 15px;">${service.serviceName }</td>
										  <td style="width:10%;" class="portConfig"><span class="oldPortConfig">${portConfig.containerPort }</span>
											<span class="editPortConfig"><input class="containerPort" type="text" value="${portConfig.containerPort }" name="containerPort"/></span>
											<input class="portId" hidden="true" value="${portConfig.portId} " />
											</td>
											<td style="width:10%;">${portConfig.protocol }</td>
											<td style="width:10%;">${portConfig.mapPort }</td>
											<td style="width:50%;"><a
												href="${service.serviceAddr}/${service.proxyPath}"
												target="_blank">
													${service.serviceAddr}/${service.proxyPath} </a></td>
											<c:if test="${service.status==1 or service.status==4}">
											<td style="width:10%;" class="editBtn">
												<i onclick="editPortAddrBtn(this)"  type="button" value="修改"  class="fa fa-edit oldPortConfig editPortAddrBtn"></i>
												<i onclick="savePortEdit(this)" hidden=true type="button" value="提交"  class="fa fa-save editPortConfig savePortEdit"></i>
           										<i onclick="canclPortEdit(this)" hidden=true type="button" value="取消"  class="fa fa-times editPortConfig"></i>
                    							<i onclick="delPortEdit(this)" type="button" value="删除"  class="fa fa-trash editPortBtn"></i>
                    						</td>
                    						</c:if>
										</tr>
									</c:forEach>
								</tbody>
							</table>
				            <c:if test="${service.status==1 or service.status==4}">
				            	<div class="createPort" style="background: #fafafa">
									<span value="添加端口信息"  id="addPortCfgBtn" onclick="addPortCfgClick()"><i class="fa fa-plus margin"></i>添加端口信息</span>
								</div>
				            </c:if>
				            <div id="createCfg-template" hidden="true">
					            <div style="width: 345px; margin: 5px 10px 5px 10px">
					              	<p class="portLabel"><span>容器端口：</span><input type="text" name="containerPort" id="containerPort" class="form-control" /></p>
					                <p class="portLabel"><span>协议：</span><select class="T-http form-control" name="protocol" id="protocol">
					                          <option>TCP</option>
					                          <option>UDP</option>
					                       </select>
					                </p>
					            </div>
				            </div>
						</section>
					</div>
					<div class="monitorInfo hide">
						<div class="data-analysis"></div>
					</div>
					<!-- 日志 -->
					<div class="containerLog hide" style="min-height: 500px;">
					<!-- <li ></li> -->
					<!-- <li ><a id="getCurrentPodlogs" href="javascript:clearLog()">获取实时日志</a></li> -->
						<div class="weblogtitle">
							<div class="pull_left">
								<span class="circle red"></span> <span class="circle blue"></span>
								<span class="circle green"></span>
							</div>
							<div class="pull_right">
								<!--<div class="input-append date form_datetime" style="display: inline-block;">-->
<!-- 								<input type="text" id="dateTimePicker" value=""/> -->
								<!-- <input id="date_log1" type="text" value="" readonly> -->
								<!--<i id="datePicker" class="fa fa-calendar margin cursor" data-toggle="tooltip" data-placement="top" title="" data-original-title="选择日期"></i>-->
								<%-- <i id="datePicker1" class="fa fa-calendar margin cursor" serviceid="${service.id }"></i> --%>
								<a id="getPodlogFile" href="" style="color:#2FBA66"><i id="download" class="fa fa-download margin cursor" ></i></a>
								<input type="hidden" id="podName" name="podName" value=""></input>
 								<!--<input type="hidden" id="serviceid" name="serviceid" value=""></input> -->
									<!--</div>-->
								<i id="refreshLog1" class="fa fa-refresh margin cursor" title="获取实时日志" ></i>
								<i id="fullScreen" class="fa fa-expand margin cursor" title="满屏"></i>
							</div>
						</div>
<!--                         <div id="containerlogList" class="weblog">

                        </div> -->
                        <div class='containerlogList weblog' style='overflow: auto;margin-top:10px;background-color:black;color: #37fc34'>
                            <pre class="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px; overflow: hidden; float: left;">
                                   <span class='printLogSpan' style="overflow: hidden; float: left;"></span>
                            </pre>
                        </div>


						<input id="serviceInstances" type="hidden" value="">
						<input id="creationTime" type="hidden" value="${service.createDate }">
					</div>
<%-- 					<!-- 历史日志 -->
					<div class="historycontainerLog hide" style="margin-bottom:30px;">
						<div class="weblogtitle">
							<div class="pull_right" style="width:99%">
								<input id="date_log" type="text" value="" readonly>
								<i id="datePicker" class="fa fa-calendar margin cursor"
									serviceid="${service.id }" serviceName = "${service.serviceName }"></i>
								<i id="refreshLog" class="fa fa-refresh margin cursor"
									data-toggle="tooltip" data-placement="top" title=""
									data-original-title="刷新日志"></i>
							</div>
						</div>
						<div id="hisLogList"><div>
					</div> --%>
					<div class="containerEvent hide" style="min-height: 500px;">
						<div class="containerEvent" style="min-height: 500px; display: block;">
							<div class="event">
								<div class="event-line">
									<div class="event-status success">
										<i class="fa fa-check note"></i>
									</div>
									<div class="time-line-content">
										<div class="time-line-reason event-title live">
											<div class="title-name success">创建成功</div>
											<div class="time-line-time">
												<div class="event-sign">
													<i class="fa fa-angle-right fa_caret"></i>
												</div>
												<div class="datetimes">${service.createDate }</div>
											</div>
											<div style="display: block;" class="time-line-message">
												<p class="list-times">时间：${service.createDate }</p>
												<p class="list-conent">信息：${service.serviceName }</p>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div
							style="text-align: center; margin-top: 40px; font-size: 12px;">结果为空</div>
					</div>
					<!-- CMD -->
					<div class="containerCMD hide" style="min-height: 500px;">
						<div id="terminal-container" style="width:98%"></div>
				        <!-- <div>
				          <h2>Options</h2>
				          <label><input type="checkbox" id="option-cursor-blink" checked> cursorBlink</label>
				          <div>
				          	<h3>Size</h3>
				            <div>
				              <div style="display: inline-block; margin-right: 16px;">
				                <label for="cols">Columns</label>
				                <input type="number" id="cols" value="81" />
				              </div>
				              <div style="display: inline-block; margin-right: 16px;">
				                <label for="rows">Rows</label>
				                <input type="number" id="rows" />
				              </div>
				            </div>
				          </div>
				        </div> -->
                        
						
					</div>
				</div>
			</div>

<%-- 			         <!-- 命令操作 -->
                    <div class="containerLog hide" id = "containerexec" style="min-height: 500px;">
                        <div class="weblogtitle">
                            <div class="pull_right" style="width:99%">
                                <input id="execText" type="text" value="" style="width:90%;background: none;border:0;text-align: right;">
                                <a id="execcmd" >运行</a>
                                <input type="hidden" id="podName" name="podName" value=""></input>
                                <input type="hidden" id="serviceid" name="serviceid" value=""></input>
                            </div>
                        </div>
                        <div id="containerlogList2" class="weblog">

                        </div>
                        <input id="serviceInstances2" type="hidden" value="">
                        <input id="creationTime2" type="hidden" value="${service.createDate }">
                    </div>
 --%>
		</article>
	</div>
</body>
</html>
