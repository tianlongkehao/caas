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
					<aside class="aside-btn hide">
						<div class="btns-group">
							<span class="btn btn-defaults btn-white"><i
								class="fa fa-map-marker"></i><span class="ic_left">用户管理</span></span>
						</div>
					</aside>
					<div class="caption clearfix hide">
						<ul class="toolbox clearfix">
							<li><a href="javascript:window.location.reload(true);"
								id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
							<li><a href="<%=path %>/user/manage/add/${cur_user.id }"
								id="userCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
							<li class="dropdown"><a data-toggle="dropdown"
								href="javascript:void(0);">更多操作&nbsp;&nbsp;<i
									class="fa fa-caret-down"></i></a>
								<ul class="dropdown-menu">
									<%--<li>
                                    <a href="javascript:changeContainerConf();">
                                        <i class="fa fa-cog"></i>
                                        <span class="ic_left">修改</span>
                                    </a>
                                </li>--%>
									<li><a href="javascript:delUser()"> <i
											class="fa fa-trash"></i> <span class="ic_left">删除</span>
									</a></li>
								</ul></li>
						</ul>
						<form id="search_form" class="form-inline"
							action="<%=path %>/user/manage/searchByCondition/${cur_user.id }"
							method="post">
							<div class="searchFun"
								style="float: left; text-align: center; margin-left: 210px;">
								<label style="line-height: 35px">姓名:</label> <input
									name="search_userName" type="text" class="form-control"
									style="display: inline; width: 70%" placeholder="搜索姓名关键字">
							</div>
							<div class="searchFun" style="float: left; text-align: center;">
								<label style="line-height: 35px">公司:</label> <input type="text"
									class="form-control" style="display: inline; width: 70%"
									placeholder="搜索公司关键字" name="search_company">
							</div>
							<%--<div class="searchFun" style="float: left; text-align: center" >
                            <label style="line-height: 35px">部门:</label>
                            <input type="text" class="form-control" style="display: inline; width: 70%" placeholder="搜索部门关键字"
                                   name="search_department">
                        </div>--%>
							<div class="searchFun" style="float: left; text-align: center">
								<label style="line-height: 40px">权限:</label> <input
									style="line-height: 40px" type="checkbox"
									name="search_autority" value="3">普通用户 <input
									style="line-height: 40px" type="checkbox"
									name="search_autority" value="4">超级用户 <input
									type="hidden" name="search_autority">
							</div>
							<div class="searchs"
								style="float: left; text-align: center; margin-left: 15px">
								<div class="form-group">
									<div class="input-group">
										<span class="input-group-btn">
											<button class="btn btn-primary" type="submit">
												<span class="glyphicon glyphicon-search"></span>
											</button>
										</span>
									</div>
								</div>
							</div>


						</form>
					</div>
					<div class="itemTable">
						<div class="row">
							<div class="col-sm-12">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>
											<i class="fa fa-map-marker" style="margin-right: 6px;"></i>租户管理
										</h5>

										<div class="ibox-tools">
											<a href="javascript:window.location.reload(true);"
												id="userReloadBtn"><i class="fa fa-repeat" title="刷新"></i></a>
											<a href="<%=path %>/user/manage/add/${cur_user.id }"
												id="userCreateBtn" title="创建用户"><i class="fa fa-plus"></i></a>
											<a id="SearchBtn" title="搜索"><i class="fa fa-search"></i></a>
											<a href="javascript:delUser()" title="删除"><i
												class="fa fa-trash"></i></a>
										</div>
									</div>
									<div class="ibox-content">
										<input type="text" class="form-control input-sm m-b-xs"
											id="filter" placeholder="搜索表格...">

										<table class="footable table table-stripped"
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
																href="javascript:delUser()" title="删除"><i
																	class="fa fa-trash"></i></a></td>
														</tr>
													</c:if>
												</c:forEach>
											</tbody>
											<tfoot>
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
    	$(".footable").footable();
		$("#checkallbox").next().addClass("hide");
		$(".del-operation").children("span").addClass("hide");
    	
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