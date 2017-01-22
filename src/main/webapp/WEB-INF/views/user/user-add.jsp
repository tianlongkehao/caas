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

<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>
<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">增加用户</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="account_table">
                    <form class="form-horizontal user_info_form" id="addUser" name="addUser" action="<%=path %>/user/add.do" method="post">
                        <div class="form-group">
                            <label for="username" class="col-md-offset-1 col-md-3">用户名：</label>
                            <div class="col-md-5">
                                <input type="text" class="form-control" id="userName" name="userName" value="" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-md-offset-1 col-md-3">邮箱：</label>
                            <div class="col-md-5">
                                <input type="email" class="form-control" id="email" name="email" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="company" class="col-md-offset-1 col-md-3">公司：</label>
                            <div class="col-md-5">
                                <input type="text" class="form-control" id="company" name="company" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="newPwd" class="col-md-offset-1 col-md-3">新密码：</label>
                            <div class="col-md-5">
                                <input type="password" class="form-control" id="password" name="password" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="confirmNewPwd" class="col-md-offset-1 col-md-3">确认新密码：</label>
                            <div class="col-md-5">
                                <input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-offset-4 col-md-2">
                                <button type="button" class="btn btn-primary btn-color" id="userSave">保存</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </article>
</div>

</body>
</html>