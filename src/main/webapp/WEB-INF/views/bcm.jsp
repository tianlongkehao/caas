<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>BCM</title>
   <%@include file="frame/dashboard-header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/bcm.css">
    <script type="text/javascript" src="<%=path %>/js/plugins/bcm.js"></script>
</head>
<body>
<nav class="navbar-default navbar-static-side" role="navigation">
            
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                	<li class="nav-header">
                        <div class="dropdown profile-element">
                            <span><img alt="image" class="img-circle" src="<%=path %>/images/dockerui.png" /></span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                	<span class="block m-t-xs"><strong class="font-bold">Jiang</strong></span>
                                	<span class="text-muted text-xs block">租户<b class="caret"></b></span>
                                </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                
                                <li><a class="J_menuItem" href="profile.html">个人资料</a>
                                </li>
                                <li><a class="J_menuItem" href="contacts.html">联系我们</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="login.html">安全退出</a>
                                </li>
                            </ul>
                        </div>
                        
                    </li>
                	<li>
                		<a href="#">
                            <i class="fa fa-tachometer fa-nav-icon"></i>
                            <span class="nav-label">总览</span>
                            
                        </a>
                	</li>
                    <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/service-red.svg" />
                            <span class="nav-label">服务</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="" >服务管理</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="">存储与备份</a>
                            </li>
                          	<li>
                                <a class="J_menuItem" href="">引入外部服务</a>
                            </li>
						</ul>
                    </li>
                    <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/factory_new-red.svg" />
                            <span class="nav-label">构建</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="" >镜像构建</a>
                            </li>
                            
						</ul>
                    </li>
                    <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/applist3-red.svg" />
                            <span class="nav-label">镜像</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="" >镜像中心</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="" >我的镜像</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="" >我的收藏</a>
                            </li>
						</ul>
                    </li>
                   <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/user-red.svg" />
                            <span class="nav-label">用户</span>
                            <span class="fa arrow"></span>
                        </a>
						<ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="" >用户管理</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="" >密钥管理</a>
                            </li>
						</ul>
                    </li>
                    <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/server-red.svg" />
                            <span class="nav-label">监控</span>
                            <span class="fa arrow"></span>
                        </a>
						<ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="" >容器监控</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="" >集群拓扑</a>
                            </li>
						</ul>
                    </li>
                    <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/tem-image-red.png" />
                            <span class="nav-label">模板管理</span>
                            <span class="fa arrow"></span>
                        </a>
						<ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="" >dockerfile</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="" >环境变量</a>
                            </li>
						</ul>
                    </li>
                    <li>
                        <a href="#">
                            <img alt="image" class="tit-image" src="<%=path %>/images/help-red.png" style="width: 14px;margin-left: 3px;"/>
                            <span class="nav-label">帮助文档</span>
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

<div style="margin-top:70px;display:none">
    <div class="funMenu">
    	<div class="icon-img">
            <span><img src="<%=path %>/images/service.svg" alt=""/></span>
        </div>
    	<p>aaa</p>
    	<p>vv</p>
    </div>
</div>

<script type="text/javascript">

</script>
</body>
</html>