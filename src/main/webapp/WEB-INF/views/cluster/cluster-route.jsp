<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster-route.js"></script>
</head>

<body>
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="cluster" value=""/>
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
                    <li class="active">路由监控</li>
                </ol>
            </div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>路由监控
									</h5>
									<div class="ibox-tools">
										<a title="批量测试" onclick="testRoutes()"><img src="<%=path %>/images/cluster-test.png" alt="cluster-test" class="clusterImg"></a>
										<a href="javascript:window.location.reload(true);" title="刷新"><i
											class="fa fa-repeat"></i></a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content" style="padding: 0px"></div>

					<div class="caption clearfix" style="padding-bottom: 0px">
						<ul class="toolbox clearfix hide">
							<li><a id="updateCluster" style="cursor: pointer"> <i
									class="fa fa-repeat"></i>
							</a></li>
						</ul>
						<div class="" style="margin-bottom:15px">
							<table class="table table-striped table-hover dataTables-example">
								<thead>
									<tr>
										<th style="width:40%">集群节点</th>
										<th style="width:15%">&nbsp;</th>
										<th style="width:15%">&nbsp;</th>
										<th>是否成功</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="routeList">
									<%-- <c:forEach items="${nodeList }" var="nodeList">
										<tr>
											<td><input type="checkbox" class="chkItem" nodeIp="${nodeList.nodeIp }" nodeName="${nodeList.nodeName }"/></td>
											<td colspan="3">${nodeList.nodeName }（${nodeList.nodeIp }）</td>
											<c:if test="${nodeList.problem == 'unknown' }">
											<td>未知</td>
											</c:if>
											<c:if test="${nodeList.problem == 'false' }">
											<td>成功</td>
											</c:if>
											<c:if test="${nodeList.problem == 'true' }">
											<td>失败</td>
											</c:if>
											<td class="routeBtns">
												<a class="fa-caret" nodeIp="${nodeList.nodeIp }" onclick="nodeTargetIPDetail(this)"><i class="fa fa-caret-right" flag="1">测试</i></a>
												<a onclick="recoverOneRoute(this)" nodeIp="${nodeList.nodeIp }" nodeName="${nodeList.nodeName }"><i>恢复</i></a>
											</td>
										</tr>
									</c:forEach> --%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
    </article>
</div>

</body>
</html>
