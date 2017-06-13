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
					    <div class="searchs">
							<form id="search_form" class="form-inline" action="" method="post">

							<div class="searchFun"
								style="float: right; text-align: center; margin: 0px 10px"
								align="right">
								<label style="line-height: 35px">节点状态:</label> <select
									name="node_status" id="node_status" onchange="conditonchanged()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option value="0">-----请选择-----</option>
									<option value="1">Ready</option>
									<option value="2">NotReady</option>
								</select>
							</div>

							<div class="searchFun"
								style="text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">集群状态:</label> <select
									name="cluster_status" id="cluster_status" onchange="conditonchanged()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option value="0" selected >-----请选择-----</option>
									<option value="1">正常</option>
									<option value="2">隔离</option>
								</select>
							</div>

							<div class="searchFun"
								style="text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">IP:</label>
								<input type="text" name="ip_content" id="ip_content" oninput="conditonchanged()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;"/>
							</div>
						</form>
						</div>
						<table class="table table-striped table-hover dataTables-example">
							<thead>
								<tr>
									<th style="width: 20%;text-indent:30px">节点名称</th>
									<th style="width: 20%;">IP</th>
									<th style="width: 20%;">集群状态</th>
									<th style="width: 20%;">节点状态</th>
									<th style="width: 20%;">操作</th>
								</tr>
							</thead>
							<tbody id="nodelisttable">
								<c:forEach items="${nodeList}" var="node">
									<tr class="clusterTr" id="${node.metadata.name }">
										<td style="width: 20%;text-indent:30px"
											value="${node.metadata.name }"><a class="link" href="javascript:void(0)"
											onclick="nodedetail(this)" nodename="${node.metadata.name }"
											title="查看详细信息" onmousemove="style.textDecoration='underline'"
											onmouseout="style.textDecoration='none'">${node.metadata.name }</a>
										</td>
										<td style="width: 20%;">${node.status.addresses[0].address}</td>
										<td style="width: 20%;"><c:choose>
												<c:when test="${node.spec.unschedulable==true}">隔离</c:when>
												<c:otherwise>正常</c:otherwise>
											</c:choose></td>
										<td style="width: 20%;"><c:choose>
												<c:when test="${node.status.conditions[1].status==true}">Ready</c:when>
												<c:otherwise>NotReady</c:otherwise>
											</c:choose></td>
										<td style="width: 20%;"><c:choose>
												<c:when test="${node.spec.unschedulable==true}">
													<a class="no-drop a-oper" href="javascript:void(0)"
														onclick="addNode(this)" title="加入"
														nodename="${node.metadata.name}"> <i
														class="fa fa-reply"></i>
													</a>
												</c:when>
												<c:otherwise>
													<a class="no-drop a-oper" href="javascript:void(0)"
														onclick="partdeleteNode(this)" title="软隔离"
														nodename="${node.metadata.name}"> <i
														class="fa fa-chain-broken"></i>
													</a>
													<a class="no-drop a-oper" href="javascript:void(0)"
														onclick="deleteNode(this)" title="强制隔离"
														nodename="${node.metadata.name}"> <i
														class="fa fa-trash"></i>
													</a>
												</c:otherwise>
											</c:choose>
										</td>
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
									<th style="width: 30%; text-align: center;">Pod名称</th>
									<th style="width: 30%; text-align: center;">NameSpace</th>
									<th style="width: 30%; text-align: center;">状态</th>
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
<script type="text/javascript">
	$('.dataTables-example').dataTable({
	    "aoColumnDefs": [ { "bSortable": false, "aTargets": [4] }],
	    "searching":false
	    //"aaSorting": [[ 2, "desc" ]]
	});
	$("#checkallbox").parent().removeClass("sorting_asc");
</script>
</body>
</html>