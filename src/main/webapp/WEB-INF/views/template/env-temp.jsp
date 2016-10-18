<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>模板</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/footable/footable。core.css" />
<script type="text/javascript"
	src="<%=path%>/js/template/env-temp.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/customer/custom.js"></script>
</head>
<body>
	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="template" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">模板</li>
						<li style="margin-left: -44px;"><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: 110px">环境变量模板</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>环境变量模板
									</h5>
									<div class="ibox-tools">
										<a href="<%=path%>/template/env/add" id="envVariableAdd"
											title="新建环境变量模板"><i class="fa fa-plus"></i>
										</a> 
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i>
										</a>
										<a id="deleteButton" class="no-drop"
											href="javascript:delEnvTemplates()" title="删除">
											<i class="fa fa-trash"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content">
									<table
										class="table table-striped table-hover dataTables-example"
										data-filter=#filter>
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;">
													<input type="checkbox" autocomplete="off" class="chkAll" id="checkallbox"/>
												</th>
												<th style="width: 15%; text-indent: 30px;">名称</th>
												<th style="width: 15%; text-indent: 15px;">主鍵</th>
												<th style="width: 30%; text-indent: 8px;">值</th>
												<th style="width: 20%;">创建时间</th>
												<th style="width: 10%; text-indent: 10px;"
													class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="envTemplateList">
											<c:forEach items="${envTemplateList}" var="envTemplate">
												<tr>
													<td style="width: 5%; text-indent: 30px;">
														<input type="checkbox" class="chkItem" name="chkItem"
															autocomplete="off" id="checkboxID" value="${envTemplate.id }" />
													</td>
													<td style="width: 15%;text-indent:30px;">${envTemplate.templateName }</td>
		                                            <td style="width: 15%;text-indent: 15px;">${envTemplate.envKey }</td>
		                                            <td style="width: 30%;text-indent: 8px;">${envTemplate.envValue }</td>
		                                            <td style="width: 20%;">${envTemplate.createDate }</td>
		                                            <td style="width: 10%;text-indent: 10px;" >
														<a id="deleteButton" class="no-drop" href="javascript:oneDeleteEnvTemplate(${envTemplate.id })" title="删除"></a>
														<i class="fa fa-trash"></i>
		                                            </td>
												</tr>
											</c:forEach>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="6">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>

	<script type="text/javascript">
		$('.dataTables-example').dataTable({
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,5] }]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
	</script>

</body>
</html>