<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户管理</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/user.css"/>
    <script type="text/javascript" src="<%=path %>/js/user/user_create.js"></script>
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
                    <li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">用户管理</li>
                </ol>
            </div>


            <div class="contentMain">


                <aside class="aside-btn">
                    <div class="btns-group">
                        <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">用户管理</span></span>
                    </div>
                </aside>
                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:window.location.reload(true);" id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <li><a href="<%=path %>/user/manage/add/${cur_user.id }" id="userCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="javascript:delUser()">
                                        <i class="fa fa-trash"></i>
                                        <span class="ic_left">删除</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <form id="search_form" class="form-inline" action="<%=path %>/user/manage/searchByCondition/${cur_user.id }" method="post">
                        <div class="col-md-2 " style="width: 25%; " align="right">
                            <label style="line-height: 35px">姓名:</label>
                            <input name="search_userName" type="text" class="form-control" style="float: right;width:70%" placeholder="搜索姓名关键字">
                        </div>
                        <%--<div class="col-md-2 " style="width: 25%; " align="right">
                            <label style="line-height: 35px">公司:</label>
                            <input type="text" class="form-control" style="display: inline; width: 70%" placeholder="搜索公司关键字"
                                   name="search_company">
                        </div>--%>
                        <div class="col-md-2" style="width: 25%; " align="right">
                            <label style="line-height: 35px">部门:</label>
                            <input type="text" class="form-control" style="display: inline; width: 70%"
                                   name="search_department">
                        </div>
                        <div class="col-md-2" class="form-control" align="right">
                            <label style="line-height: 40px; margin-left: -10px">权限:</label>
                            <input style="line-height: 40px" type="checkbox" name="search_autority" value="3">普通用户
                            <input style="line-height: 40px" type="checkbox" name="search_autority" value="4">超级用户
                            <input type="hidden" name="search_autority">
                        </div>
                        <div class="searchs" align="right">
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
                                            <th style="width: 18%;">公司</th>
                                            <th style="width: 15%;">部门</th>
                                            <th style="width: 15%;text-indent: 8px;">工号</th>
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
                                        <c:forEach items="${userManageList }" var="user">
                                            <%--<c:if test="${user.id == null || user.id == 0}">
                                                <c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                            </c:if>--%>
                                            <c:if test="${cur_user.id != user.id}">
                                                <tr class="userTr" id="${user.id }">
                                                    <td style="width: 5%; text-indent: 30px;">
                                                        <input type="checkbox" class="chkItem" name="ids" value="${user.id }" >
                                                    </td>
                                                    <td style="width: 15%;">
                                                        <a href="<%=path %>/user/manage/detail/${user.id }" title="查看详细信息" onmousemove="style.textDecoration='underline'"
                                                           onmouseout="style.textDecoration='none'">${user.userName }</a>
                                                    </td>
                                                    <td style="width: 15%; text-indent: 0;">${user.user_realname }</td>
                                                    <td style="width: 18%; text-indent: 0;">${user.company }</td>
                                                    <td style="width: 15%; text-indent: 0;">${user.user_department }</td>
                                                    <td style="width: 15%; text-indent: 0;">${user.user_employee_id}</td>
                                                    <td style="width: 10%; text-indent: 0;">
                                                        <c:if test='${user.user_autority == "3"}'>普通用户</c:if>
                                                        <c:if test='${user.user_autority == "4"}'>超级用户</c:if>
                                                    </td>
                                                    <%--<td style="width: 10%; text-indent: 0;">
                                                        <c:if test='${user.user_autority == "4"}'>超级用户</c:if>
                                                    </td>--%>
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

</body>
</html>