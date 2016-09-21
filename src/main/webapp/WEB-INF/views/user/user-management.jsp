<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>用户管理</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/user.css" />
<script type="text/javascript" src="<%=path%>/js/user/user_create.js"></script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="user" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">用户管理</li>
					</ol>
				</div>
				
				<div class="contentMain">
					<div class="itemTable">
						<div class="row">
							<div class="col-sm-12">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>
											<i class="fa fa-map-marker" style="margin-right: 6px;"></i>租户管理
										</h5>
										<div class="ibox-tools">
											<a href="<%=path %>/user/manage/add/${cur_user.id }"
												id="userCreateBtn" title="创建用户"><i class="fa fa-plus"></i></a>
											<a id="SearchBtn" title="搜索"><i class="fa fa-search"></i></a>
											<a href="javascript:delUser()" title="删除"><i
												class="fa fa-trash"></i></a>
											<a href="javascript:window.location.reload(true);"
												id="userReloadBtn"><i class="fa fa-repeat" title="刷新"></i></a>
										</div>
									</div>
									<div class="ibox-content">
										<table class="table table-striped table-hover dataTables-example"
											data-page-size="8" data-filter=#filter>
											<thead>
												<tr>
													<th style="width: 5%; text-indent: 30px;"><input
														type="checkbox" class="chkAll" id="checkallbox" /></th>
													<th style="width: 15%; padding-left: 5px;">登录账号</th>
													<th style="width: 15%;">姓名</th>
													<th style="width: 15%;">公司</th>
													<th style="width: 15%;">部门</th>
													<th style="width: 15%; text-indent: 8px;">工号</th>
													<th style="width: 10%;">角色权限</th>
													<th style="width: 8%;" class="del-operation">操作</th>
												</tr>

											</thead>
											<tbody>
												<c:forEach items="${userManageList }" var="user">
													<%--<c:if test="${user.id == null || user.id == 0}">
                                                <c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                            </c:if>--%>
													<c:if test="${cur_user.id != user.id}">
														<tr class="userTr" id="${user.id }">
															<td style="width: 5%; text-indent: 30px;"><input
																type="checkbox" class="chkItem" name="ids"
																value="${user.id }"></td>
															<td style="width: 15%;"><a
																href="<%=path %>/user/manage/detail/${user.id }"
																title="查看详细信息"
																onmousemove="style.textDecoration='underline'"
																onmouseout="style.textDecoration='none'">${user.userName }</a>
															</td>
															<td style="width: 15%; text-indent: 0;">${user.user_realname }</td>
															<td style="width: 15%; text-indent: 0;">${user.company }</td>
															<td style="width: 15%; text-indent: 0;">${user.user_department }</td>
															<td style="width: 15%; text-indent: 0;">${user.user_employee_id}</td>
															<td style="width: 10%; text-indent: 0;"><c:if
																	test='${user.user_autority == "3"}'>普通用户</c:if> <c:if
																	test='${user.user_autority == "4"}'>超级用户</c:if></td>
															<td style="width: 8%; text-indent: 4px;"><a
																href="javascript:delUserById(${user.id })" title="删除"><i
																	class="fa fa-trash"></i></a></td>
														</tr>
													</c:if>
												</c:forEach>
											</tbody>
											<tfoot class="hide">
												<tr>
													<td colspan="8">
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


			</div>
		</article>
	</div>
	<input id="create_flag" value="${creatFlag}" type="hidden">
	<input id="update_flag" value="${updateFlag}" type="hidden">
	<script type="text/javascript">
    $(document).ready(function(){
    	$('.dataTables-example').dataTable({
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,7] }]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
    	
        var create_flag = $.trim($("#create_flag").val());
        var update_flag = $.trim($("#update_flag").val());

        if(create_flag == '200'){
            location.href="<%=path%>/user/manage/list/${cur_user.id }";
            layer.alert("创建用户成功！",50000);
        }
        if(create_flag == '400'){
            location.href="<%=path%>/user/manage/list/${cur_user.id }";
            layer.alert("创建用户失败！");
        }

        if(update_flag === '400'){
            location.href="<%=path%>/user/manage/list/${cur_user.id }";
            layer.alert("用户信息更新失败！");
        }
        if(update_flag === '200'){
            location.href="<%=path%>/user/manage/list/${cur_user.id }";
				layer.alert("用户信息更新成功！");
			}
		});
	</script>
</body>
</html>