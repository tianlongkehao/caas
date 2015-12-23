<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户管理</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/user.css"/>
    <script type="text/javascript" src="/js/user/user-management.js"></script>
</head>
<body>

<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
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

                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="user/add" id="userAdd">增加用户</a></li>
                        <li><a href="javascript:void(0);">删除</a></li>
                        <li><a href="javascript:void(0);">全部删除</a></li>
                    </ul>
                </div>

                <div class="itemTable">
                    <table class="table user-table">
                        <thead>
                        <tr>
                            <th>
                                <div class="table-head">
                                    <table class="table" style="margin: 0;">
                                        <thead>
                                        <tr>
                                            <th style="width: 10%; text-indent: 30px;"><input type="checkbox" class="chkAll"></th>
                                            <th style="width: 20%; text-indent: 20px;">用户名</th>
                                            <th style="width: 20%; text-indent: 0;">邮箱</th>
                                            <th style="width: 25%; text-indent: 0;">公司</th>
                                            <th style="width: 25%; text-indent: 0;">操作</th>
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
                                        <tr class="userTr" id="${user.id }">
                                            <td style="width: 10%; text-indent: 30px;"><input type="checkbox" class="chkItem"></td>
                                            <td href="user/detail/${user.id }" style="width: 20%; text-indent: 20px;">${user.userName }</td>
                                            <td style="width: 20%; text-indent: 0;">${user.email }</td>
                                            <td style="width: 25%; text-indent: 0;">${user.company }</td>
                                            <td style="width: 25%; text-indent: 0;">
                                                <a href="user/detail/${user.id }">信息修改</a>
                                                <a href="user/del/${user.id }">删除</a>
                                            </td>
                                        </tr>
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