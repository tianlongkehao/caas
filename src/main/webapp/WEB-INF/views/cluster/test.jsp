<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>集群</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/cluster.css" />
<script type="text/javascript"
	src="<%=path%>/js/cluster/test.js"></script>
</head>

<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="cluster" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i
								class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">集群测试</li>
					</ol>
				</div>
				<div class="contentMain" style="height:400px;">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>集群测试
									</h5>
									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);" title="刷新"><i
											class="fa fa-repeat"></i></a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content" style="padding: 0px"></div>
					<div class="caption clearfix"
						style="overflow:auto;width: 25%;height:100%;float: left;">
						<%-- <c:if test="${not empty nodeList}"> --%>
							<div class="checkbox">
								<input id="selectnode" type="checkbox" value="" >集群节点
							</div>
						<%-- </c:if> --%>
						<c:forEach items="${nodeList}" var="node">
							<div class="checkbox">
								<input name="node" type="checkbox" checked value="${node.metadata.name }">${node.metadata.name }
							</div>
						</c:forEach>
					</div>
					<div class="caption clearfix"
						style="width: 75%;float: left;height:100%;">
						<div>
							ping
							<input type="text" id="pingip" placeholder="主机IP" value = "" />
							平均响应时间小于:<input type="number" value="10" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="pingtime"
														onkeyup="this.value=this.value.replace(/\D/g,'')"
														name="instanceNum"><span class="s-unit">ms</span>
							<a id="pinglink" style="float:right" href="javascript:void(0)" onclick="pingdetail()"
									title="查看详细信息" onmousemove="style.textDecoration='underline'"
									onmouseout="style.textDecoration='none'"></a>
						</div>
						<div>
							time tracepath
							<input type="text" id="tracepathip" placeholder="主机IP" value = "" />
							平均响应时间小于:<input type="number" value="2" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="tracepathtime"
														onkeyup="this.value=this.value.replace(/\D/g,'')"
														name="instanceNum"><span class="s-unit">s</span>
							<a id="tracepathlink" style="float:right" href="javascript:void(0)" onclick="tracepathdetail()"
									title="查看详细信息" onmousemove="style.textDecoration='underline'"
									onmouseout="style.textDecoration='none'"></a>
						</div>
						<div>
							time curl 响应时间小于:<input type="number" value="2" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="curltime"
														onkeyup="this.value=this.value.replace(/\D/g,'')"
														name="instanceNum"><span class="s-unit">s</span>
								<a id="curllink" style="float:right" href="javascript:void(0)" onclick="curldetail()"
									title="查看详细信息" onmousemove="style.textDecoration='underline'"
									onmouseout="style.textDecoration='none'"></a>
						</div>
						<div>
							qperf 性能数值大于
							<!-- <input type="text" id="qperf" placeholder="性能数值" value = "" /> -->
							<input type="number" value="2000" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="qperf"
														onkeyup="this.value=this.value.replace(/\D/g,'')"
														name="instanceNum">
							<a id="qperflink" style="float:right" href="javascript:void(0)" onclick="qperfdetail()"
									title="查看详细信息" onmousemove="style.textDecoration='underline'"
									onmouseout="style.textDecoration='none'"></a>
						</div>
						<div>
							docker 磁盘空间
							<!-- <input type="text" id="docker" placeholder="磁盘空间" value = "" /> -->
							<input type="number" value="10" class="number" min="1"
														autocomplete="off" max="" placeholder="1" id="docker"
														onkeyup="this.value=this.value.replace(/\D/g,'')"
														name="instanceNum"><span class="s-unit">GB</span>
							<a id="dockerlink" style="float:right" href="javascript:void(0)" onclick="dockerdetail()"
									title="查看详细信息" onmousemove="style.textDecoration='underline'"
									onmouseout="style.textDecoration='none'"></a>
						</div>
						<div>
							dns
							<a id="dnslink" style="float:right" href="javascript:void(0)" onclick="dnsdetail()"
									title="查看详细信息" onmousemove="style.textDecoration='underline'"
									onmouseout="style.textDecoration='none'"></a>
									<!-- FF0033 红色 33CC33 绿色  <font color="#33CC33" style="font-weight:bold">通过</font>-->
						</div>
					</div>
					<input type="hidden" id="deployednodes" value=""></input>
					<span id="deleteBtn" class="btn btn-primary btn-color pull-right" style="margin-right: 10px;cursor: pointer">清除部署</span>
					<span id="excuteBtn" class="btn btn-primary btn-color pull-right" style="margin-right: 10px;cursor: pointer">执行</span>
					<span id="deployBtn" class="btn btn-primary btn-color pull-right" style="margin-right: 10px;cursor: pointer">部署</span>
				</div>
				<div id="pingdetail" style="display: none;">
				</div>
				<div id="tracepathdetail" style="display: none;">
				</div>
				<div id="curldetail" style="display: none;">
				</div>
				<div id="qperfdetail" style="display: none;">
				</div>
				<div id="dockerdetail" style="display: none;">
				</div>
				<div id="dnsdetail" style="display: none;">
				</div>
			</div>
		</article>
	</div>
</body>
</html>
