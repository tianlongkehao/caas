<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>nginx</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/nginx.css" />
<script type="text/javascript" src="<%=path%>/js/nginx/k8snginxcfg.js"></script>
</head>
<body>
	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">nginx</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
                    <div class="col-md-12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>租户
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="userReloadBtn"><i class="fa fa-repeat" title="刷新"></i></a>
									</div>
                            </div>
                            <div class="ibox-content">
                                <table class="table table-striped table-hover dataTables-example">
                                    <thead>
                                        <tr>
											<th style="width: 5%; text-indent: 30px;">
												<input type="checkbox" class="chkAll" id="checkallbox" /></th>
											<th style="width: 30%;">登录账号</th>
											<th style="width: 10%;" class="del-operation">操作</th>
										</tr>
                                    </thead>
                                    <tbody id="userList">
											<c:forEach items="${userList }" var="user">
												<c:if test="${user.id == null || user.id == 0}">
													<c:set var="cursorClass" value="cursor-no-drop"></c:set>
												</c:if>
												<c:if test="${cur_user.id != user.id}">
													<tr class="userTr" id="${user.id }">
														<td style="width: 5%; text-indent: 30px;"><input
															type="checkbox" class="chkItem" name="ids"
															value="${user.id }"></td>
														<td style="width: 30%;">
															${user.userName }
														</td>
														<td style="width: 10%;"><a class="k8snginxcfgButton" title="k8snginxcfg"
															class="no-drop" href="<%=path%>/nginx/k8snginxcfg"
															style="margin-left: 10px"> <i class="fa fa-gears"></i>
														</a></td>
													</tr>
												</c:if>
											</c:forEach> 
										</tbody>
                                    	<tfoot class="hide">
											<tr>
												<td colspan="3">
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
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 2 ]
			} ],
			//"aaSorting": [[ 5, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc"); 
	</script>
</body>
</html> 