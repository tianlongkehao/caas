<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/css/mod/service.css" />
<script type="text/javascript" src="/js/service/service-detail.js"></script>
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
						<li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">服务</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">${service.serviceName }</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">${service.serviceName }</li>
					</ol>
				</div>
				<div class="contentMain">
					<section class="detail-succeed">
						<div class="icon-img">
							<div class="type-icon">
								<img src="<%=path %>/images/image-1.png" height="100%">
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
							<li>服务地址：<a
								href="${service.serviceAddr}/${service.proxyPath}"
								target="_blank">${service.serviceAddr}/${service.proxyPath}</a></li>
							<li>创建时间：${service.createDate }</li>
							<li>更新时间：${service.createDate }</li>
						</ul>
						<div class="applocation">
							<a
								href="${service.serviceAddr}/${service.proxyPath}"
								target="_blank" class="open">打开应用</a>
						</div>
					</section>
					<div class="baseInfo center-style">
						<a class="BASE btn-prim">容器</a>
						<!-- <a class="INSTANCES">容器实例</a>
						 <a class="DOMAIN">绑定域名</a>  -->
						<!--<a class="PORTS">端口</a>
						<!--<a class="MONITOR">监控</a>-->
						<a class="LOG">日志</a>
		 				<!--<a class="EVENT">事件</a> -->
					</div>
					<div class="containerInfo">
						<table class="table w50">
							<thead>
								<tr>
									<th>基本信息</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody class="BORDER">
								<tr>
									<td>名称：${service.serviceName }</td>
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
									<td>镜像名称：${service.imgName }</td>
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
								<!-- <tr>
					<td>带宽：10 MB（公有网络）</td>
					<td>系统盘：10GB</td>
				</tr> -->
								<tr>
									<td>CPU：${service.cpuNum }</td>
									<td>内存：${service.ram }MB</td>
								</tr>
							</tbody>
						</table>
						<!-- <table class="table basicInfo w50">
			<thead>
				<tr>
					<th>环境变量</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody class="BORDER">
				<tr>
					<td>变量名</td>
					<td>变量值</td>
				</tr>
			</tbody>
		</table> -->
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
										<td><a
											href="${service.serviceAddr}/${service.proxyPath}"
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
					<div class="portMapping hide" style="min-height: 500px;">
						<section class="paddingrow">
							<table class="table table-normal" style="border: 1px solid #eee;">
								<thead style="background: #F5F6F6;">
									<tr style="height: 40px;">
										<td>名称</td>
										<td>容器端口</td>
										<td>协议</td>
										<td>映射端口</td>
										<td>服务地址</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>${service.serviceName }</td>
										<td>8080</td>
										<td>TCP</td>
										<td>${service.portSet }</td>
										<td><a
											href="${service.serviceAddr}/${service.proxyPath}"
											target="_blank">${service.serviceAddr}/${service.proxyPath}</a></td>
									</tr>
								</tbody>
							</table>
						</section>
					</div>
					<div class="monitorInfo hide">
						<div class="data-analysis"></div>
					</div>
					<div class="containerLog hide" style="min-height: 500px;">
						<div class="weblogtitle">
							<div class="pull_left">
								<span class="circle red"></span> <span class="circle blue"></span>
								<span class="circle green"></span>
							</div>
							<div class="pull_right">
								<!--<div class="input-append date form_datetime" style="display: inline-block;">-->
								<input id="date_log" type="text" value="历史记录" readonly>
								<!--<i id="datePicker" class="fa fa-calendar margin cursor" data-toggle="tooltip" data-placement="top" title="" data-original-title="选择日期"></i>-->
								<i id="datePicker" class="fa fa-calendar margin cursor"></i>
								<!--</div>-->
								<i id="refreshLog" class="fa fa-refresh margin cursor"
									data-toggle="tooltip" data-placement="top" title=""
									data-original-title="刷新日志"></i> <i id="fullScreen"
									class="fa fa-expand margin cursor" title="满屏"></i>
							</div>
						</div>
						<div class="weblog">
							<pre id="serviceLogs" style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">${logList }</pre>
							<input id="serviceInstances" type="hidden" value=""> 
							<input id="creationTime" type="hidden" value="2015-11-30 10:23:27">
						</div>
					</div>
					<div class="containerEvent hide" style="min-height: 500px;">
						<div class="containerEvent"
							style="min-height: 500px; display: block;">
							<div class="event">
								<div class="event-line">
									<div class="event-status success">
										<i class="fa fa-check note"></i>
									</div>
									<div class="time-line-content">
										<div class="time-line-reason event-title live">
											<div class="title-name success">创建成功</div>
											<div class="time-line-time">
												<div class="event-sign live">
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
		</article>
	</div>
</body>
</html>