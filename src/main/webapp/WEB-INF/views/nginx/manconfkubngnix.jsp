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
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>租户管理
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
												<th style="width: 15%; padding-left: 5px;">登录账号</th>
												<th style="width: 15%;">姓名</th>
												
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
														<td style="width: 15%;"><a
															href="<%=path %>/user/detail/${user.id }" title="查看详细信息"
															onmousemove="style.textDecoration='underline'"
															onmouseout="style.textDecoration='none'">${user.userName }</a>
														</td>
														<td style="width: 15%; text-indent: 0;">${user.user_realname }</td>
														
														<td style="width: 10%;"><a id="deleteButton"
															class="no-drop" href="javascript:delOneTenement(${user.id })"
															style="margin-left: 10px"> <i class="fa fa-trash"></i>
														</a></td>
													</tr>
												</c:if>
											</c:forEach> 
										</tbody>
                                    	<tfoot class="hide">
											<tr>
												<td colspan="9">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
				
				<a href="<%=path%>/nginx/k8snginxcfg"><span type="button" class="btn btn-default" value="k8snginxcfg">k8snginxcfg</span></a>
				
				</div>
			</div>
		</article>
	</div>
</body>
</html> 