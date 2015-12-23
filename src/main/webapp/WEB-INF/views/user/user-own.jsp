<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/user.css"/>
    <script type="text/javascript" src="/js/user/user.js"></script>
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
                    <li class="active">用户</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="setTab">
                    <a id="baseinfo" class="Record active">基本信息</a>
                    <a id="pwd" class="Record">修改密码</a>
                </div>

                <div class="account_table" userID="${user.id }">

                    <div id="baseinfo_wrap" class="tab_wrap">
                        <form class="form-horizontal user_info_form" id="editUser" name="editUser" action="/user/edit.do" method="post">
                            <div class="form-group">
                                <label for="username" class="col-md-offset-1 col-md-3">用户名：</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control" id="userName" name="userName" value="${user.userName }" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="col-md-offset-1 col-md-3">邮箱：</label>
                                <div class="col-md-5">
                                    <input type="email" class="form-control" id="email" name="email" value="${user.email }">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="company" class="col-md-offset-1 col-md-3">公司：</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control" id="company" name="company" value="${user.company }">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-2">
                                    <button type="button" class="btn btn-primary" id="basicInfo">保存</button>
                                </div>
                            </div>

                        </form>
                    </div>

                    <div id="pwd_wrap" class="tab_wrap hide">

                        <form class="edit_pwd_form form-horizontal" action="user/userModifyPsw.do" id="pswSave" name="pswSave" method="post">
                            <div class="form-group">
                                <label for="originalPwd" class="col-md-offset-1 col-md-3">原密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="originalPwd" name="originalPwd" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="newPwd" class="col-md-offset-1 col-md-3">新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="newPwd" name="newPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="confirmNewPwd" class="col-md-offset-1 col-md-3">确认新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-3">
                                    <button type="button" class="btn btn-primary" id="passwordInfo">提交修改</button>
                                </div>
                            </div>

                        </form>

                    </div>

                </div>


            </div>
        </div>
    </article>
</div>

</body>
</html>