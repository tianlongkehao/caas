<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BONC-PaaS</title>
    <link rel="stylesheet" type="text/css" href="/plugins/bootstrap-3.3.5/dist/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/plugins/Font-Awesome-master/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="/css/core/base.css">
    <link rel="stylesheet" type="text/css" href="/css/mod/dashboard.css">
    <script type="text/javascript" src="/js/plugins/jquery-1.11.3.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-3.3.5/dist/js/bootstrap.js" ></script>
</head>
<body>
    <div class="dashboard-wrapper">
        <header class="dashboard-header">
            <nav class="navbar dashboard-nav">
                <div class="container">
                    <div class="navbar-header navbar-logo">
                        <a href="javascript:void(0);">
                            <h1>BONC-PaaS</h1>
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
            </nav>
        </header>
        <div class="dashboard-main">
            <div class="container">
                <ul class="app-list">
                    <li>
                        <a class="icon-view" href="service.html">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/service.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">服务集成</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="/menu?flag=ci">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">代码构建</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="docker-registry.html">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">镜像服务</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">代码构建</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">代码构建</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">代码构建</div>
                            </div>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

    </div>
    <div class="wallpaper"></div>
</body>

</html>