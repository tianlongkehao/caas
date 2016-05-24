<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path=request.getContextPath(); %>
<html>
<head>
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
            <ul class="nav navbar-nav navbar-right navbar-info">
                <li class="dropdown">
                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button"
                       aria-haspopup="true" aria-expanded="false">
                       ${cur_user.userName }
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="<%=path %>/user/detail/${cur_user.id }/a"><i class="fa fa-user"></i>&nbsp;&nbsp;基本信息</a></li>
                        <li><a href="<%=path %>/user/detail/${cur_user.id }/b"><i class="fa fa-pencil"></i>&nbsp;&nbsp修改密码</a></li>
                        <li class="logout">
	                        <c:choose>
		                       	<c:when test="${ssoConfig.enable}">
		                       		<a href="${ssoConfig.serverLogoutUrl}"><i class="fa fa-power-off"></i>&nbsp;&nbsp退出登录</a>
		                       	</c:when>
		                       	<c:otherwise>
		                       		<a href="<%=path %>/loginout/${cur_user.id }"><i class="fa fa-power-off"></i>&nbsp;&nbsp退出登录</a>
		                       	</c:otherwise>
		                  	</c:choose>
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
            <a href="<%=path %>/workbench">
                <span class="glyphicon glyphicon-th nav-icon" aria-hidden="true"></span>
                <span class="nav-title">控制台</span>
            </a>
        </li>
        <li id="menu_service">
            <a href="<%=path %>/service">
                <img class="nav-icon" src="<%=path %>/images/service.svg" alt=""/>
                <span class="nav-title">服务</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuService" action="">
                    <a href="<%=path %>/service">
                        服务
                    </a>
                </li>
                 <li>
                    <a href="<%=path %>/service/storage">
                        存储与备份
                    </a>
                </li>
            </ul>
        </li>
        <li id="menu_ci">
            <a href="<%=path %>/ci">
                <img class="nav-icon" src="<%=path %>/images/factory_new.svg" alt=""/>
                <span class="nav-title">构建</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuCi" action="">
                    <a href="<%=path %>/ci">
                        构建
                    </a>
                </li>
            </ul>
        </li>
        <li id="menu_registry">
            <a href="<%=path %>/registry/0">
                <img class="nav-icon" src="<%=path %>/images/applist3.svg" alt=""/>
                <span class="nav-title">镜像</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuRegistry">
                    <a href="<%=path %>/registry/0">
                        镜像中心
                    </a>
                </li>
                <li id="menuRegistry1">
                    <a href="<%=path %>/registry/1">
                        我的镜像
                    </a>
                </li>
                <li id="menuRegistry2">
                    <a href="<%=path %>/registry/2">
                        我的收藏
                    </a>
                </li>
            </ul>
        </li>
        <c:if test="${cur_user.user_autority == 2}">
        <li id="menu_usermanage" class="user-admin">
            <a href="<%=path %>/user/manage/list/${cur_user.id }">
                <img class="nav-icon" src="<%=path %>/images/user.svg" alt=""/>
                <span class="nav-title">用户</span>
            </a>
            <ul class="nav-item-hover">
                <li id="menuUserUser" action="">
                    <a href="<%=path %>/user/manage/list/${cur_user.id }">
                        用户管理
                    </a>
                </li>
            </ul>
        </li>
        </c:if>

        <c:if test="${cur_user.user_autority == 1}">
            <li id="menu_user" class="user-admin">
                <a href="<%=path %>/user/list">
                    <img class="nav-icon" src="<%=path %>/images/user.svg" alt=""/>
                    <span class="nav-title">租户</span>
                </a>
                <ul class="nav-item-hover">
                    <li id="menuUser" action="">
                        <a href="<%=path %>/user/list">
                            租户管理
                        </a>
                    </li>
                </ul>
            </li>

            <li id="menu_cluster">
                <a href="<%=path %>/cluster/resource">
                    <img class="nav-icon" src="<%=path %>/images/server.svg" alt=""/>
                    <span class="nav-title">集群</span>
                </a>
                <ul class="nav-item-hover">
                    <li id="resourceCluster" action="">
                        <a href="<%=path %>/cluster/resource">
                            集群监控
                        </a>
                    </li>
                    <li id="resourceContainers" action="">
                        <a href="<%=path %>/cluster/containers">
                            容器监控
                        </a>
                    </li>
                    <li id="manageCluster" action="">
                        <a href="<%=path %>/cluster/management">
                            集群管理
                        </a>
                    </li>
                </ul>
            </li>
        </c:if>
        <c:if test="${cur_user.user_autority == 2}">
        <li id="menu_cluster">
            <a href="<%=path %>/cluster/containers">
                <img class="nav-icon" src="<%=path %>/images/server.svg" alt=""/>
                <span class="nav-title">集群</span>
            </a>
            <ul class="nav-item-hover">
                <li id="" action="">
                    <a href="<%=path %>/cluster/containers">
                        容器监控
                    </a>
                </li>
            </ul>
        </li>
        </c:if>



    </ul>
</aside>
</body>

<script type="text/javascript">

    $(function(){

        var menu_flag = '${menu_flag}';

        $("#menu_"+menu_flag).addClass("item-click");

        // 菜单效果
        $(".nav-menu>li").on("mouseenter",function(){
            $(".nav-menu>li").css("background-color","transparent");
            $(".nav-menu>li").children(".nav-item-hover").css("display","none");
            $(this).css("background-color","#3c81e0");
            $(this).children(".nav-item-hover").css("display","block");
        });

        $(".nav-menu>li").on("mouseleave",function(){
            $(this).css("background-color","transparent");
            $(this).children(".nav-item-hover").css("display","none");
            $(".item-click").css("background-color","#3c81e0");
        });

    });




</script>

</html>
