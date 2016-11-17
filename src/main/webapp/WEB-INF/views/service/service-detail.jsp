<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
<script type="text/javascript"
	src="<%=path%>/js/service/service-detail.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/service/laydate/laydate.js"></script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">服务</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width:200px">${service.serviceName }</li>
					</ol>
					<input hidden="true" value="${service.id }" id="serId"/>
					<input hidden="true" value="${service.status }" id="serStatus"/>
         <input hidden="true" value="${service.serviceType }" id="serType"/>	
         <input hidden="true" value="${service.volName }" id="serVolName"/>
         <input hidden="true" value="${service.mountPath }" id="serMonPath"/>
             				
				</div>
				<div class="contentMain">
					<section class="detail-succeed">
						<div class="icon-img">
							<div class="type-icon">
								<img src="<%=path%>/images/image-1.png" height="100%">
							</div>
						</div>
						<ul class="succeed-content pull-left">
							<c:if test="${service.status==1 }">
								<li>运行状态：未启动</li>
							</c:if>
							<c:if test="${service.status==2||service.status==3 }">
								<li>运行状态：正在运行</li>
							</c:if>
							<c:if test="${service.status==4 }">
								<li>运行状态：已停止</li>
							</c:if>
							<%-- <li>服务地址：<a
								href="${service.serviceAddr}/${service.proxyPath}"
								target="_blank">${service.serviceAddr}/${service.proxyPath}</a></li> --%>
							<li class="oldCon">服务地址：<a href="${service.serviceAddr}/${service.proxyPath}"target="_blank">
								<span id="oldServiceAddr">${service.serviceAddr}</span>/
								<span id="oldProxyPath">${service.proxyPath}</span>
								</a><i id="editServiceAddrBtn" style="margin-left:20px" class="fa fa-edit"></i></li>
							<li class="editCon">服务地址：
							  <prex id=addrPrex ></prex>
								<input id="editServiceAddr" type="text" value="">/
								<input id="editProxyPath" type="text" value="${service.proxyPath}">
								<i id="saveEdit" style="margin-left:20px" class="fa fa-save"></i>
								<i id="canclEdit" style="margin-left:6px" class="fa fa-times"></i>
								</li>
							<li>创建时间：${service.createDate }</li>
							<li>更新时间：${service.createDate }</li>
						</ul>
						<div class="applocation">
							<a href="${service.serviceAddr}/${service.proxyPath}"
								target="_blank" class="open">打开应用</a>
						</div>
					</section>
					
					<div class="baseInfo center-style">
						<ul class="nav navbar-nav">
							<li><a class="BASE btn-prim">基本信息</a></li>
							<li><a class="INSTANCES">容器实例</a></li>
							<li><a class="ENVS">环境变量</a></li>
							<li><a class="PORTS">端口</a></li>
							<li class="dropdown">
								<a class="dropdown-toggle" id="dropdown-log"
									data-toggle="dropdown"> 日志 <b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
								 	<c:forEach items="${podNameList}" var="pod" >
								 		<li class="LOG"><a podName="${pod.podName }" serviceid="${service.id }" value="2" onclick="dropdownLog(this)" 
								 			style="width: 150px;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;" title="${pod.podName }">${pod.podName }</a></li>
								 	</c:forEach>
									
								</ul>
							</li>
							<li><input id="editSerBtn" name="editSerBtn" type="button" value="修改"/></li>
             <li><input id="restSerBtn" name="restSerBtn" type="button" value="还原"/></li>
             <li><input id="saveSerBtn" name="saveSerBtn" type="button" hidden="true" value="保存"/></li>
             <li><input id="canclSerBtn" name="canclSerBtn" type="button" value="取消"/></li>
<%-- 							<li><a class="historyLOG">历史日志</a></li>
							<li class="dropdown">
                                <a class="execCommand dropdown-toggle" id="dropdown-log"
                                    data-toggle="dropdown"> 命令操作 <b class="caret"></b>
                                </a>
                                <ul class="dropdown-menu">
                                    <c:forEach items="${podNameList}" var="pod" >
                                        <li><a class="podName" podName="${pod.podName }" serviceid="${service.id }" namespace = "${namespace }" value="2" >${pod.podName }</a></li>
                                    </c:forEach>
                                </ul>
                            </li>
--%>
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
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody class="BORDER">
								<tr>
									<td>服务名称：
									<span id="oldSerName" class="oldBaseCon">${service.serviceName }</span>
									<span id="editSerName" hidden="true" class="editBaseCon">
									   <input id="newSerName" name="serviceName" type="text" value=${service.serviceName } />
									</span>
									</td>
									<c:if test="${service.status==1 }">
										<td>运行状态：未启动</td>
									</c:if>
									<c:if test="${service.status==2||service.status==3 }">
										<td>运行状态：正在运行</td>
									</c:if>
									<c:if test="${service.status==4 }">
										<td>运行状态：已停止</td>
									</c:if>
								</tr>
								<tr>
									<td>镜像名称：${service.imgName } : ${service.imgVersion }</td>
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
									<c:if test="${service.startCommand == '' }">
									<td>启动命令：
									<span id="oldStartComm" class="oldBaseCon">默认</span>
                 <span id="editStartComm" hidden="true" class="editBaseCon">
                 <input id="newStartComm" name="startCommand" type="text" value="${service.startCommand }" />
                 </span>
									</td>
									</c:if>
									<c:if test="${service.startCommand != '' }">
									<td>启动命令：
									<span id="oldStartComm" class="oldBaseCon">${service.startCommand }</span>
                   <span id="editStartComm" hidden="true" class="editBaseCon">
                      <input id="newStartComm" name="startCommand" type="text" value="${service.startCommand }" />
                   </span>
									</td>
									</c:if>
									<td >服务访问路径：<span class="oldBaseCon_Run oldBaseCon">${service.servicePath }</span>
									<span id="editSerPath" hidden="true" class="editBaseCon editBaseCon_Run">
                   <input id="newSerPath" name="servicePath" type="text" value="${service.servicePath }" />
                </span>
									</td>
								</tr>
								<tr>
									<c:if test="${service.proxyZone == '' }">
									<td>nginx代理区域：
									<span class="oldBaseCon_Run oldBaseCon">未设置</span>
									 <span id="editProxyZone" hidden="true" class="editBaseCon_Run editBaseCon">
                  <label class="checkbox-inline"> <input
                      type="checkbox"  name="proxyZone"
                      value="dmz"> DMZ区
                  </label> <label class="checkbox-inline"> <input
                      type="checkbox"  name="proxyZone"
                      value="user"> USER区
                </label>
                </span>
									</td>
									</c:if>
									<c:if test="${service.proxyZone != '' }">
									<td>nginx代理区域:
									<span class="oldBaseCon_Run oldBaseCon">${service.proxyZone }</span>
									 <span id="editProxyZone" hidden="true" class="editBaseCon_Run editBaseCon">
                  <label class="checkbox-inline"> <input
                      type="checkbox"  name="proxyZone"
                      value="dmz"> DMZ区
                  </label> <label class="checkbox-inline"> <input
                      type="checkbox"  name="proxyZone"
                      value="user"> USER区
                </label>
                </span>
<%--                        --%>
									</td>
									</c:if>
									<td>nginx代理路径：
									<span class="oldBaseCon_Run oldBaseCon">${service.proxyPath }</span>
                   <span id="editProxyPath" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newProxyPath" name="proxyPath" type="text" value="${service.proxyPath }" />
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
                   <option name="sessionAffinity" value="ClientIP">ClientIP</option>
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
                 </c:if>
									</span>
                   <span id="editNodAffy" hidden="true" class="editBaseCon_Run editBaseCon">
                   <%-- <input id="newNodAffy" name="nodeIpAffinity" type="text" value=${service.nodeIpAffinity } /> --%>
                   <select  id="newNodAffy" name="nodeIpAffinity" >
                   <option name="nodeIpAffinity" value="" >NONE</option>
                   <option name="nodeIpAffinity" value="nodeIpAffinity">nodeIpAffinity</option>
                   </select>
                </span>
									</td>
								</tr>
								<c:if test="${service.checkPath=='' }">
                 <tr hidden="true" class="editBaseCon_Run editBaseCon">
                 <td>检查状态：
                   <span id="editCheckPath" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newCheckPath" name="checkPath" type="text" value="${service.checkPath }"  />
                </span>
                  </td>
                  <td>检测延迟：
                  <span id="editInitDelay" hidden="true" class="editBaseCon_Run editBaseCon">
                  <input id="newInitDelay" name="initialDelay" type="text" value="${service.initialDelay }"  />
                </span>
		              s</td>
		          </tr>
		          <tr hidden="true" class="editBaseCon_Run editBaseCon">
		              <td>检测超时：
                   <span id="editTiOut" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newTiOut" name="timeoutDetction" type="text" value="${service.timeoutDetction }" />
                </span>
                   s</td>
                   <td>检测频率：
                   <span id="editPeriod" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newPeriod" name="periodDetction" type="text" value="${service.periodDetction }" />
                </span>
                  s</td>
              </tr>
								</c:if>
								<c:if test="${service.checkPath!='' }">
								<tr>
									<td>检查状态：
									<span class="oldBaseCon_Run oldBaseCon">${service.checkPath }</span>
                   <span id="editCheckPath" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newCheckPath" name="checkPath" type="text" value="${service.checkPath }" />
                </span>
									</td>
									<td>检测延迟：
									<span class="oldBaseCon_Run oldBaseCon">${service.initialDelay }</span>
                   <span id="editInitDelay" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newInitDelay" name="initialDelay" type="text" value="${service.initialDelay }" />
                </span>
									s</td>
								</tr>
								<tr>
									<td>检测超时：
									<span class="oldBaseCon_Run oldBaseCon">${service.timeoutDetction }</span>
                   <span id="editTiOut" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newTiOut" name="timeoutDetction" type="text" value="${service.timeoutDetction }" />
                </span>
									s</td>
									<td>检测频率：
									<span class="oldBaseCon_Run oldBaseCon">${service.periodDetction }</span>
                   <span id="editPeriod" hidden="true" class="editBaseCon_Run editBaseCon">
                   <input id="newPeriod" name="periodDetction" type="text" value="${service.periodDetction }" />
                </span>
									s</td>
								</tr>
								</c:if>
								<tr id="newSerType" hidden="true" hidden="true" class="editBaseCon" >
									<td>
									<span>挂载地址：</span>
									<span><input type="text" id="newMonPath" name="mountPath" value="${service.mountPath }" />
									</span>
									<span>存储卷：</span>
									<span>
									<select id="selSerType" name="volName">
									 <option value="">请选择一个卷组</option>
	               </select>
									</span>
									</td>
								</tr>
								<tr class="oldBaseCon">
									<c:if test="${service.serviceType==1 }">
									<td>服务类型：有状态服务</td>
									<td>挂载地址：${ service.mountPath }</td>
									</c:if>
									<c:if test="${service.serviceType==2 }">
									<td>服务类型：无状态服务</td>
									</c:if>
								</tr>
								<c:if test="${service.serviceType==1 }">
								<tr class="oldBaseCon">
									<td>存储卷：${service.volName }</td>
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
										<c:if test="${service.status==4 }">
											<td>waiting</td>
										</c:if>
										<c:if test="${service.status==2||service.status==3 }">
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
					<div class="envMapping hide" style="min-height: 500px;">
						<section class="paddingrow">
							<div class="envlabel hide"><label class="forEnvName">环境变量模板名称：</label>
								<input type="text" class="form-control envName" value="${envTemplate.templateName }" readonly>
							</div>
							<table class="table table-normal" style="border: 1px solid #eee;">
								<thead style="background: #F5F6F6;">
									<tr style="height: 40px;">
										<th style="width:40%;text-indent: 15px;">键</th>
										<th>值</th>
										
									</tr>
								</thead>
								<tbody>
	                                <c:forEach items="${envVariableList }" var="envVariable">
	                                    <tr>
	                                        <td style="width:40%;text-indent: 15px;"> ${envVariable.envKey }</td>
	                                        <td> ${envVariable.envValue }</td>
	                                    </tr>
	                                </c:forEach>
									
								</tbody>
							</table>
						</section>
					</div>
					<div class="portMapping hide" style="min-height: 500px;">
						<section class="paddingrow">
							<table class="table table-normal" style="border: 1px solid #eee;">
								<thead style="background: #F5F6F6;">
									<tr style="height: 40px;">
										<td style="width:10%;text-indent: 15px;">名称</td>
										<td>容器端口</td>
										<td>协议</td>
										<td>映射端口</td>
										<td>服务地址</td>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${portConfigList }" var="portConfig">
										<tr>
											<td style="width:10%;text-indent: 15px;">${service.serviceName }</td>
											<td>${portConfig.containerPort }</td>
											<td>${portConfig.protocol }</td>
											<td>${portConfig.mapPort }</td>
											<td><a
												href="${service.serviceAddr}/${service.proxyPath}"
												target="_blank">
													${service.serviceAddr}/${service.proxyPath} </a></td>
										</tr>
									</c:forEach>
									<%-- 									<tr>
										<td>${service.serviceName }</td>
										<td>8080</td>
										<td>TCP</td>
										<td>${service.portSet }</td>
										<td><a
											href="${service.serviceAddr}/${service.proxyPath}"
											target="_blank">${service.serviceAddr}/${service.proxyPath}</a></td>
									</tr> --%>
								</tbody>
							</table>
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
								<input id="date_log1" type="text" value="" readonly>
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
						<div id="containerlogList" class="weblog">
							 
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