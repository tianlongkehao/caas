<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>集群</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/cluster.css" />
<script type="text/javascript" src="<%=path%>/js/cluster/node.js"></script>
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
						<li class="active">节点管理</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>节点管理
									</h5>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<table class="table">
							<thead>
								<tr>
									<th style="width: 3%; text-indent: 30px;">
										<%--<input type="checkbox" class="chkAll"/>--%>
									</th>
									<th style="width: 20%; padding-left: 5px;">节点名称</th>
									<th style="width: 20%; padding-left: 5px;">IP</th>
									<th style="width: 20%;">集群状态</th>
									<th style="width: 20%;">节点状态</th>
									<th style="width: 20%; text-indent: 8px;">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${nodeList}" var="node">
									<tr class="clusterTr" id="${node.metadata.name }">
										<td style="width: 3%; text-indent: 30px;"></td>
										<td style="width: 20%; padding-left: 5px;"
											value="${node.metadata.name }"><a href="javascript:void(0)"
											onclick="nodedetail(this)" nodename="${node.metadata.name }"
											title="查看详细信息" onmousemove="style.textDecoration='underline'"
											onmouseout="style.textDecoration='none'">${node.metadata.name }</a>
										</td>
										<td style="width: 20%; text-indent: 8px;">${node.status.addresses[0].address}</td>
										<td style="width: 20%;"><c:choose>
												<c:when test="${node.spec.unschedulable==true}">隔离</c:when>
												<c:otherwise>正常</c:otherwise>
											</c:choose></td>
										<td style="width: 20%;"><c:choose>
												<c:when test="${node.status.conditions[1].status==true}">Ready</c:when>
												<c:otherwise>NotReady</c:otherwise>
											</c:choose></td>
										<td style="width: 20%; text-indent: 8px;"><c:choose>
												<c:when test="${node.spec.unschedulable==true}">
													<a class="no-drop a-oper" href="javascript:void(0)"
														onclick="addNode(this)" title="加入"
														nodename="${node.metadata.name}"> <i
														class="fa fa-reply"></i>
													</a>
												</c:when>
												<c:otherwise>
													<a class="no-drop a-oper" href="javascript:void(0)"
														onclick="deleteNode(this)" title="隔离"
														nodename="${node.metadata.name}"> <i
														class="fa fa-trash"></i>
													</a>
												</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div></div>
						<div id="nodedetail" style="display: none;">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="nodeTable">
									<tr>
										<th style="width: 25%">节点名称：</th>
										<td><input class="" type="text" id="nodename" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">IP地址：</th>
										<td><input class="" type="text" id="ip" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">集群状态：</th>
										<td><input class="" type="text" id="status" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">节点状态：</th>
										<td><input class="" type="text" id="nodestatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">CPU：</th>
										<td><input class="" type="text" id="cpu" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">Memory：</th>
										<td><input class="" type="text" id="memory" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">Pod数量上限：</th>
										<td><input class="" type="text" id="pod" readOnly
											value=""></td>
									</tr>
								</tbody>
							</table>
							<div class="podTable">
							    <table  class="table enabled" style="width: 580px; padding: 5px; margin: 10px">
								<thead>
								<tr>
									<th style="width: 30%; padding-left: 5px;">Pod名称</th>
									<th style="width: 30%; padding-left: 5px;">NameSpace</th>
									<th style="width: 30%;">状态</th>
								</tr>
							    </thead>
								<tbody id="podTable">
								</tbody>
							    </table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>

</body>
</html>