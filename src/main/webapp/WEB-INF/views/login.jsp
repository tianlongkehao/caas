<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>用户登录</title>
   <%@include file="frame/dashboard-header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/core/layout.css">
</head>
<body>

<header class="header">
    <div class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a href="javascript:void(0);">
                    <h2>BONC-PaaS</h2>
                </a>
            </div>
        </div>
    </div>
</header>
<div class="container">
    <div class="login-container">
        <div class="sign-container">
            <h3>登录</h3>
            <div class="sign-form">
                <form id="loginForm" class="form" action="/signin" method="post">
                    <div class="form-group">
                        <div class="input-group">
                            <span class="input-group-addon">
                                <i class="fa fa-user"></i>
                            </span>
                            <input type="text" class="form-control" id="userName" name="userName" placeholder="请输入用户名">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <span class="input-group-addon">
                                <i class="fa fa-lock"></i>
                            </span>
                            <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码">
                        </div>
                    </div>
                    <div class="form-group">
                        <button id="btn-signin" class="btn btn-primary btn-block" type="button" disabled="disabled">登录</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(function(){

        $("#userName").keyup(function(){
            var checkFlag = checkEnter();
            if(checkFlag){
                $("#btn-signin").removeAttr("disabled");
            }else{
                $("#btn-signin").attr("disabled", "disabled");
            }
            return false;
        });

        $("#password").keyup(function(){
            var checkFlag = checkEnter();
            if(checkFlag){
                $("#btn-signin").removeAttr("disabled");
            }else{
                $("#btn-signin").attr("disabled", "disabled");
            }
            return false;
        });

        function checkEnter(){
            if($("#userName").val() && $("#password").val()){
                return true;
            }
            return false;
        }

        $("#btn-signin").click(function(){
            $("#loginForm").submit();
        });

    });

</script>
</body>
</html>