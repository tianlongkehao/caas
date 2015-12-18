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
                        <li class="active">${container.containerName }</li>
                    </ol>
                </div>
                <div class="contentMain">
	<section class="detail-succeed">
		<div class="icon-img">
			<div class="type-icon">
				<img src="images/tenxcloud_mysql.png" height="100%">
			</div>
		</div>
		<ul class="succeed-content pull-left">
			<li>运行状态：运行中</li>
			<li>服务地址：<a href="http://mysql-lynnxu.tenxapp.com:25314"
				target="_blank">bonc.com:25314</a></li>
			<li>创建时间：${container.createDate }</li>
			<li>更新时间：${container.createDate }</li>
		</ul>
		<div class="applocation">
			<a href="http://mysql-lynnxu.tenxapp.com:25314" target="_blank"
				class="open">打开应用</a>
		</div>
	</section>
	<div class="baseInfo center-style">
		<a class="BASE btn-prim">基本信息</a> <a class="INSTANCES">容器实例</a> <a
			class="DOMAIN">绑定域名</a> <a class="PORTS">端口</a> <a class="MONITOR">监控</a>
		<a class="LOG">日志</a> <a class="EVENT">事件</a>
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
					<td>名称：${container.containerName }</td>
					<td>运行状态：正在运行</td>
				</tr>
				<tr>
					<td>镜像名称：${container.imageName }</td>
					<td>创建时间：${container.createDate }</td>
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
					<td>带宽：10 MB（公有网络）</td>
					<td>系统盘：10GB</td>
				</tr>
				<tr>
					<td>CPU：125m</td>
					<td>内存：512MB</td>
				</tr>
			</tbody>
		</table>
		<table class="table basicInfo w50">
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
		</table>
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
	<c:forEach items="${serviceList }" var="service" >
		<c:choose>
			<c:when test="${service.status == 1 }">
				<c:set var="statusClass" value="fa_run"></c:set>
                <c:set var="eventStatusClass" value="fa-check"></c:set>
                <c:set var="statusName" value="成功"></c:set>
                <c:set var="eventStatus" value="success"></c:set>
			</c:when>
			<c:when test="${service.status == 2 }">
				<c:set var="statusClass" value="fa_stop"></c:set>
                <c:set var="eventStatusClass" value="fa-times"></c:set>
                <c:set var="statusName" value="失败"></c:set>
                <c:set var="eventStatus" value="error"></c:set>
			</c:when>
		</c:choose>
				<tr>
                  <td><a href="/containers/tenx_district2/instances/hello-bq2vo">${service.serviceName }</a></td>
                  <td>Running</td>
                  <td>${service.imgName }</td>
                  <td>bonc:8080</td>
                  <td><a href="http://hello-christ.tenxapp.com" target="_blank">bonc.com</a></td>
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
									class="input-group-addon"><a href="javascript:void(0)"
									onclick="deleteline(this)"><i class="fa fa-trash-o fa-lg"
										style="margin-right: 3px;"></i></a></span>
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
						<td>mysql</td>
						<td>3306</td>
						<td>TCP</td>
						<td>25314</td>
						<td><a href="http://mysql-lynnxu.tenxapp.com:25314"
							target="_blank">mysql-lynnxu.tenxapp.com:25314</a></td>
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
				<span class="circle red"></span> <span class="circle blue"></span> <span
					class="circle green"></span>
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
			<pre id="serviceLogs"
				style="background: none repeat scroll 0 0 black; color: #37fc34; border: 0; font-size: 12px;">今天没有日志产生。</pre>
			<input id="serviceInstances" type="hidden" value=""> <input
				id="creationTime" type="hidden" value="2015-11-30 10:23:27">
		</div>
	</div>
	<div class="containerEvent hide" style="min-height: 500px;">
		<div style="text-align: center; margin-top: 40px; font-size: 12px;">结果为空</div>
	</div>
	         </div>
            </div>
        </article>
    </div>
</body>
</html>