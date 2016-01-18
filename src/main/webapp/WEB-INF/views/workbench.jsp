<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
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
                    <ul class="nav navbar-nav navbar-right navbar-info">
                        <li class="dropdown">
                            <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                ${cur_user.userName }
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="user/detail/${cur_user.id }/a"><i class="fa fa-user"></i>&nbsp;&nbsp;基本信息</a></li>
                                <li><a href="user/detail/${cur_user.id }/b"><i class="fa fa-pencil"></i>&nbsp;&nbsp修改密码</a></li>
                                <li class="logout">
                                    <a href="loginout/${cur_user.id }"><i class="fa fa-power-off"></i>&nbsp;&nbsp退出登录</a>
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
                                    <span><img src="images/applist3.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">镜像中心</div>
                            </div>
                        </a>
                    </li>
                    <c:if test="${cur_user.user_autority == 2}">
                    <li>
                        <a class="icon-view" href="javascript:void(0);" action="/user/manage/list">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/user.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">用户管理</div>
                            </div>
                        </a>
                    </li>
                    </c:if>

                    <c:if test="${cur_user.user_autority == 1}">
                    <li>
                        <a class="icon-view" href="javascript:void(0);" action="/user/list">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/user.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">租户管理</div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a class="icon-view" href="javascript:void(0);" action="/cluster/resource">
                            <div class="icon-wrapper">
                                <div class="icon-img">
                                    <span><img src="images/server.svg" alt=""/></span>
                                </div>
                                <div class="icon-name">集群管理</div>
                            </div>
                        </a>
                    </li>
                    </c:if>

                </ul>
            </div>
        </div>
    </div>
    <div class="wallpaper"></div>
<c:if test="${msg != null}">
    <input type="hidden" id="errorMsg" value="${msg}">
    <script type="text/javascript">
    $(function(){
    	var errorMsg = $("#errorMsg").val();
        layer.alert(errorMsg);
    });
        
    </script>
</c:if>
</body>
</html>