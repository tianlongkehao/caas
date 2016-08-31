<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>租户</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/user.css" />
<script type="text/javascript" src="<%=path%>/js/user/user.js"></script>
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
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">租户管理</li>
					</ol>
				</div>
				<div class="contentMain">
					
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
										<a href="<%=path%>/user/add" id="userCreateBtn" title="创建用户"><i
											class="fa fa-plus"></i></a> 
										<a id="SearchBtn" title="搜索"><i
											class="fa fa-search"></i></a> 
										<a href="javascript:delTenement()" title="删除"><i
											class="fa fa-trash"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<input type="text" class="form-control input-sm m-b-xs"
										id="filter" placeholder="搜索表格...">

									<table class="footable table table-stripped" data-page-size="8"
										data-filter=#filter>
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;">
													<input type="checkbox" class="chkAll" id="checkallbox" /></th>
												<th style="width: 15%; padding-left: 5px;">登录账号</th>
												<th style="width: 15%;">姓名</th>
												<th style="width: 10%;">省份</th>
												<th style="width: 15%;">公司</th>
												<th style="width: 15%;">部门</th>
												<th style="width: 10%; text-indent: 8px;">工号</th>
												<th style="width: 10%;">角色权限</th>
												<th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody>
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
														<td style="width: 10%; text-indent: 0;"
															id="user.user_province" name="user.user_province"><input
															type="hidden" id="user_province_hidden"
															value="${user.user_province}"> <select
															class="hidden" name="user_province" id="user_province">
																<option name="user_province" value="1">北京</option>
																<option name="user_province" value="2">天津</option>
																<option name="user_province" value="3">上海</option>
																<option name="user_province" value="4">河北</option>
																<option name="user_province" value="5">河南</option>
																<option name="user_province" value="6">山西</option>
																<option name="user_province" value="7">内蒙古</option>
																<option name="user_province" value="8">辽宁</option>
																<option name="user_province" value="9">吉林</option>
																<option name="user_province" value="10">黑龙江</option>
																<option name="user_province" value="11">江苏</option>
																<option name="user_province" value="12">浙江</option>
																<option name="user_province" value="13">安徽</option>
																<option name="user_province" value="14">福建</option>
																<option name="user_province" value="15">江西</option>
																<option name="user_province" value="16">山东</option>
																<option name="user_province" value="17">湖南</option>
																<option name="user_province" value="18">湖北</option>
																<option name="user_province" value="19">广东</option>
																<option name="user_province" value="20">广西</option>
																<option name="user_province" value="21">海南</option>
																<option name="user_province" value="22">重庆</option>
																<option name="user_province" value="23">四川</option>
																<option name="user_province" value="24">贵州</option>
																<option name="user_province" value="25">云南</option>
																<option name="user_province" value="26">西藏</option>
																<option name="user_province" value="27">陕西</option>
																<option name="user_province" value="28">甘肃</option>
																<option name="user_province" value="29">青海</option>
																<option name="user_province" value="30">宁夏</option>
																<option name="user_province" value="31">新疆</option>
														</select></td>
														<td style="width: 15%; text-indent: 0;">${user.company }</td>
														<td style="width: 15%; text-indent: 0;">${user.user_department }</td>
														<td style="width: 10%; text-indent: 0;">${user.user_employee_id}</td>
														<td style="width: 10%; text-indent: 0;"
															id="user.user_autority" name="user.user_autority"><input
															type="hidden" id="user_autority_hidden"
															value="${user.user_autority}"> <select
															class="hidden" id="user_autority" name="user_autority">
																<option name="user_autority" value="2">租户</option>
																<option name="user_autority" value="1">管理员</option>
														</select></td>
														<td style="width: 10%;"><a id="deleteButton"
															class="no-drop" href="javascript:delOneTenement()"
															style="margin-left: 10px"> <i class="fa fa-trash"></i>
														</a></td>
													</tr>
												</c:if>
											</c:forEach>
										</tbody>
										<tfoot>
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
			/*if(confirm("创建用户成功！")) {
				location.href="<%=path %>/user/list";
			}
			//window.location.reload;
			else{
				$("#create_flag").attr("value", '');
			}
			return;*/
            location.href="<%=path %>/user/list";
            layer.alert("创建用户成功！");
		}
		if(create_flag == '400'){
			/*if(confirm("创建用户失败！")) {
				location.href="<%=path %>/user/list";
			}
			else {
				$("#create_flag").attr("value", '');
			}
			return;*/
            location.href="<%=path %>/user/list";
            layer.alert("创建用户失败！");
		}
		
		if(update_flag === '400'){
			/*if(confirm("用户信息更新失败！")) {
				location.href="<%=path %>/user/list";
			}
			else {
				$("#update_flag").attr("value", '');
			}
			return;*/
            location.href="<%=path %>/user/list";
            layer.alert("用户信息更新失败！");
		}
		if(update_flag === '200'){
			/*if(confirm("用户信息更新成功！")) {
				location.href="<%=path %>/user/list";
			}
			else {
				$("#update_flag").attr("value", '');			
			}
			return;*/
            location.href="<%=path %>/user/list";
            layer.alert("用户信息更新成功！");
		}

			
		});
	</script>
</body>
</html>
