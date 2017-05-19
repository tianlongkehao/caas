<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>模板</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/footable/footable.core.css" />
<script type="text/javascript"
	src="<%=path%>/js/template/env-temp.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/customer/custom.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="template" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
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
										<a id="deleteButton" class="no-drop"
											href="javascript:delEnvTemplates()" title="删除">
											<i class="fa fa-trash"></i>
										</a> 
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i>
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
												<th style="width: 60%; text-indent: 30px;">名称</th>
												<th style="width: 20%;">创建时间</th>
												<th style="width: 10%; text-indent: 10px;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="envTemplateList">
											<c:forEach items="${envTemplateList}" var="envTemplate">
												<tr>
													<td style="width: 5%; text-indent: 30px;">
														<input type="checkbox" class="chkItem" name="chkItem"
															autocomplete="off" id="checkboxID" value="${envTemplate.templateName }" />
													</td>
													<td style="width: 60%;text-indent:30px;">
														<a class="link" href="<%=path %>/template/env/detail/${envTemplate.templateName }" style="float:left;line-height:40px"
                                                            title="查看${envTemplate.templateName }详细信息" 
                                                            onmousemove="style.textDecoration='underline'"
                                                            onmouseout="style.textDecoration='none'">${envTemplate.templateName }</a>
													</td>
		                                            <td style="width: 20%;">
		                                                   <fmt:formatDate value="${envTemplate.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                                            </td>
		                                            <td style="width: 10%;text-indent: 10px;" >
														<a id="deleteButton" class="no-drop" href="javascript:oneDeleteEnvTemplate('${envTemplate.templateName }')" title="删除" style="float:left;line-height:40px">
														<i class="fa fa-trash"></i></a>
		                                            </td>
												</tr>
											</c:forEach>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="4">
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
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,3] }],
	        "aaSorting": [[ 2, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
	</script>

</body>
</html>