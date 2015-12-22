<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>BONC-Paas</title>
    <%@include file="frame/dashboard-header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/dashboard.css">
    <script type="text/javascript" src="/js/customer/workbench.js"></script>
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
                    <%String name= (String) session.getAttribute("user.userName");%>
                    <ul class="nav navbar-nav navbar-right navbar-info">
                        <li class="dropdown">
                            <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                <%=session.getAttribute("CUR_USER") %>
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="user/detail/${user.id }"><i class="fa fa-user"></i>&nbsp;&nbsp;基本信息</a></li>
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
                        <a class="icon-view" href="javascript:void(0);" action="/service">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/service.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">服务集成</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);" action="/ci">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">代码构建</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);" action="/registry/0">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">镜像中心</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">API中心</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">应用中心</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);" action="/user">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/factory_new.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">用户中心</div>
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