<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/user.css"/>
    <script type="text/javascript" src="<%=path %>/js/user/user.js"></script>
</head>
<body>
    <jsp:include page="../frame/menu.jsp" flush="true">
        <jsp:param name="user" value=""/>
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
                    <aside class="aside-btn">
                        <div class="btns-group">
                            <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">租户管理</span></span>
                        </div>
                    </aside>
                    <div class="caption clearfix">
                        <ul class="toolbox clearfix">
                            <li><a href="javascript:window.location.reload(true);" id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
                            <li><a href="<%=path %>/user/add" id="userCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                            <li class="dropdown">
                                <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="javascript:tenement_detail()">
                                            <i class="fa fa-play"></i>
                                            <span class="ic_left">查看</span>
                                        </a>
                                    </li>
                                <%--<li>
                                        <a href="javascript:changeContainerConf();">
                                            <i class="fa fa-cog"></i>
                                            <span class="ic_left">修改</span>
                                        </a>
                                    </li>--%>
                                    <li>
                                        <a href="javascript:delTenement()">
                                            <i class="fa fa-trash"></i>
                                            <span class="ic_left">删除</span>
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <form id="search_form" class="form-inline" action="<%=path %>/user/searchByCondition" method="post">
                        	<div class="searchFun" style=" float: left; text-align: center;margin-left: 160px" align="right" >
                                <label style="line-height: 35px;">姓名:</label>
                                <input name="search_userName" type="text" class="form-control" style="display: inline;width:60%" placeholder="搜索姓名关键字">
                            </div>
                            <div class="searchFun" style="float: left; text-align: center" align="right">
                                <label style="line-height: 35px">省份:</label>
                                <%--<input type="hidden" class="form-control" style="display: inline; width: 60%" placeholder="搜索省份关键字"
                                       name="search_province">--%>
                                <select name="search_province" id="search_province" style="height: 30px;display: inline;">
                                    <option name="search_province" ></option>
                                    <option name="search_province" value="1">北京</option>
                                    <option name="search_province" value="2">天津</option>
                                    <option name="search_province" value="3">上海</option>
                                    <option name="search_province" value="4">河北</option>
                                    <option name="search_province" value="5">河南</option>
                                    <option name="search_province" value="6">山西</option>
                                    <option name="search_province" value="7">内蒙古</option>
                                    <option name="search_province" value="8">辽宁</option>
                                    <option name="search_province" value="9">吉林</option>
                                    <option name="search_province" value="10">黑龙江</option>
                                    <option name="search_province" value="11">江苏</option>
                                    <option name="search_province" value="12">浙江</option>
                                    <option name="search_province" value="13">安徽</option>
                                    <option name="search_province" value="14">福建</option>
                                    <option name="search_province" value="15">江西</option>
                                    <option name="search_province" value="16">山东</option>
                                    <option name="search_province" value="17">湖南</option>
                                    <option name="search_province" value="18">湖北</option>
                                    <option name="search_province" value="19">广东</option>
                                    <option name="search_province" value="20">广西</option>
                                    <option name="search_province" value="21">海南</option>
                                    <option name="search_province" value="22">重庆</option>
                                    <option name="search_province" value="23">四川</option>
                                    <option name="search_province" value="24">贵州</option>
                                    <option name="search_province" value="25">云南</option>
                                    <option name="search_province" value="26">西藏</option>
                                    <option name="search_province" value="27">陕西</option>
                                    <option name="search_province" value="28">甘肃</option>
                                    <option name="search_province" value="29">青海</option>
                                    <option name="search_province" value="30">宁夏</option>
                                    <option name="search_province" value="31">新疆</option>
                                </select>
                            </div>
                            <div class="searchFun" style="float: left; text-align: center" align="right">
                                <label style="line-height: 35px">公司:</label>
                                <input type="text" class="form-control" style="display: inline; width: 60%" placeholder="搜索公司关键字"
                                       name="search_company">
                            </div>
                            <div class="searchFun" class="form-control" align="right" style="float: left; text-align: center">
                                <label style="line-height: 40px">权限:</label>
                           		<input style="line-height: 40px" type="checkbox"  name="search_autority" value="1">管理员
                               	<input style="line-height: 40px" type="checkbox" name="search_autority" value="2">租户
                                <input type="hidden" name="search_autority">
                            </div>
           					<div class="searchs" align="right" style="float: left; text-align: center;margin-left: 15px">
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
                        <table class="table user-table">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table-head">
                                        <table class="table"  style="margin: 0;">
                                            <thead>
                                            <tr>
                                                <th style="width: 5%;text-indent: 30px;">
                                                    <input type="checkbox" class="chkAll"/>
                                                </th>
                                                <th style="width: 15%;padding-left: 5px;">登录账号</th>
                                                <th style="width: 15%;">姓名</th>
                                                <th style="width: 15%;">省份</th>
                                                <th style="width: 15%;">公司</th>
                                                <th style="width: 15%;">部门</th>
                                                <th style="width: 13%;text-indent: 8px;">工号</th>
                                                <th style="width: 10%;">角色权限</th>
                                            </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
	                        <tr>
	                            <td>
	                                <div class="content-table">
	                                    <table class="table tables">
	                                        <tbody>
	                                        <c:forEach items="${userList }" var="user">
	                                        <c:if test="${user.id == null || user.id == 0}">
	                                                	<c:set var="cursorClass" value="cursor-no-drop"></c:set>
	                                        </c:if>
						                    <c:if test="${cur_user.id != user.id}">
		                                        <tr class="userTr" id="${user.id }">
		                                            <td style="width: 5%; text-indent: 30px;">
		                                            	<input type="checkbox" class="chkItem" name="ids" value="${user.id }" >
		                                            </td>
		                                            <td style="width: 15%;">
                                                        <a href="<%=path %>/user/detail/${user.id }" title="查看详细信息" onmousemove="style.textDecoration='underline'"
                                                           onmouseout="style.textDecoration='none'">${user.userName }</a>
                                                    </td>
		                                            <td style="width: 15%; text-indent: 0;">${user.user_realname }</td>
                                                    <td style="width: 15%; text-indent: 0;" id="user.user_province" name="user.user_province">
                                                        <input type="hidden" id="user_province_hidden"
                                                               value="${user.user_province}">
                                                        <select class="hidden" name="user_province" id="user_province">
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
                                                        </select>
                                                    </td>
		                                            <td style="width: 15%; text-indent: 0;">${user.company }</td>
		                                            <td style="width: 15%; text-indent: 0;">${user.user_department }</td>
		                                            <td style="width: 13%; text-indent: 0;">${user.user_employee_id}</td>
		                                            <td style="width: 10%; text-indent: 0;" id="user.user_autority" name="user.user_autority">
                                                        <input type="hidden" id="user_autority_hidden" value="${user.user_autority}">
                                                        <select class="hidden" id="user_autority" name="user_autority">
                                                            <option name="user_autority" value="2">租户</option>
                                                            <option name="user_autority" value="1">管理员</option>
                                                        </select>
		                                            </td>
		                                        </tr>
	                                        </c:if>
	                                        </c:forEach>
	                                        </tbody>
	                                    </table>
	                                </div>
	                            </td>
	                        </tr>
	                        </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </article>
    </div>
    <input id="create_flag" value="${creatFlag}" type="hidden">
    <input id="update_flag" value="${updateFlag}" type="hidden">

<script type="text/javascript" >
	$(document).ready(function(){
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
