<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>BONC-Paas</title>
    <link rel="stylesheet" type="text/css" href="/plugins/bootstrap-3.3.5/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/plugins/Font-Awesome-master/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="/plugins/layer/skin/layer.css"/>
    <link rel="stylesheet" type="text/css" href="/css/core/base.css"/>
    <link rel="stylesheet" type="text/css" href="/css/core/layout.css"/>
    <script type="text/javascript" src="/js/plugins/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="/plugins/bootstrap-3.3.5/dist/js/bootstrap.js" ></script>
    <script type="text/javascript" src="/plugins/layer/layer.js"></script>
    <script type="text/javascript">
    	var flag = "${flag}";
    </script>
    <script type="text/javascript" src="/js/customer/custom.js"></script>
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
            <ul class="nav navbar-nav navbar-right navbar-user">
                <li class="dropdown">
                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        Admin
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0);"><i class="fa fa-user"></i>&nbsp;&nbsp;基本信息</a></li>
                        <li><a href="javascript:void(0);"><i class="fa fa-pencil"></i>&nbsp;&nbsp修改密码</a></li>
                        <li class="logout">
                            <a href="javascript:void(0);"><i class="fa fa-power-off"></i>&nbsp;&nbsp退出登录</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</header>
<aside class="page-sidebar">
    <ul class="nav-menu">
        <li class="first-nav-li">
            <a href="/">
                <span class="glyphicon glyphicon-th nav-icon" aria-hidden="true"></span>
                <span class="nav-title">控制台</span>
            </a>
        </li>
        <li >
            <a href="javascript:void(0)">
                <img class="nav-icon" src="images/service.svg" alt=""/>
                <span class="nav-title">服务</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuService" action="/service">
                    <a href="javascript:void(0)">
                        	服务
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        	存储与备份
                    </a>
                </li>
            </ul>
        </li>
        <li>
            <a href="javascript:void(0)">
                <img class="nav-icon" src="images/service.svg" alt=""/>
                <span class="nav-title">构建</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuCi" action="/ci">
                    <a href="javascript:void(0)">
                        	构建
                    </a>
                </li>
            </ul>
        </li>
        <li>
            <a href="javascript:void(0)">
                <img class="nav-icon" src="images/service.svg" alt=""/>
                <span class="nav-title">镜像</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuRegistry" action="/registry/0">
                    <a href="javascript:void(0)">
                        	镜像中心
                    </a>
                </li>
                <li id="menuRegistry1" action="/registry/1">
                    <a href="javascript:void(0)">
                        	我的镜像
                    </a>
                </li>
                <li id="menuRegistry2" action="/registry/2">
                    <a href="javascript:void(0)">
                        	我的收藏
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</aside>
<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2"></li>
                </ol>
            </div>
            <div class="contentMain">
            </div>
        </div>
    </article>
</div>
</body>
</html>