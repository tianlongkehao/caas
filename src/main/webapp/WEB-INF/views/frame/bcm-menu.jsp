<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path=request.getContextPath(); %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/bcm-menu.css">
    <script type="text/javascript" src="<%=path %>/js/plugins/bcm-menu.js"></script>
</head>
<body>

<header class="header">
    <div class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a href="<%=path %>/home">
                    <h2>BCM</h2>
                </a>
            </div>
            <ul class="nav navbar-nav navbar-right navbar-info">
                <li class="dropdown">
                	<c:if test="${cas_enable}">
		                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button"
		                   aria-haspopup="true" aria-expanded="false" style="margin-right:57px">
		                   <i class="fa fa-user"></i>
		                   </span>
		                </a>
                    </c:if>
                    <c:if test="${!cas_enable}">
	                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button"
	                       aria-haspopup="true" aria-expanded="false">
	                       ${cur_user.userName }
	                        <span class="caret"></span>
	                    </a>
                    </c:if>
                    <ul class="dropdown-menu">
                    	
                        <li><a href="<%=path %>/user/detail/${cur_user.id }/a"><i class="fa fa-user"></i>&nbsp;&nbsp;基本信息</a></li>
                     
                        <c:if test="${!cas_enable}">
                        	<li><a href="<%=path %>/user/detail/${cur_user.id }/b"><i class="fa fa-unlock-alt"></i>&nbsp;&nbsp修改密码</a></li>
                        
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
                        </c:if>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</header>
	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<ul class="nav" id="side-menu">
				<li class="nav-header hide">
					<div class="dropdown profile-element">
						<span><img alt="image" class="img-circle"
							src="<%=path%>/images/dockerui.png" /></span> <a
							data-toggle="dropdown" class="dropdown-toggle" href="#"> <span
							class="clear"> <span class="block m-t-xs"><strong
									class="font-bold">Jiang</strong></span> <span
								class="text-muted text-xs block">租户<b class="caret"></b></span>
						</span>
						</a>
						<ul class="dropdown-menu animated fadeInRight m-t-xs">

							<li><a class="J_menuItem" href="profile.html">个人资料</a></li>
							<li><a class="J_menuItem" href="contacts.html">联系我们</a></li>
							<li class="divider"></li>
							<li><a href="login.html">安全退出</a></li>
						</ul>
					</div>

				</li>
				<li id="menu_bcm"><a href="<%=path %>/bcm/${cur_user.id }" class="first-a"> <i class="fa fa-tachometer fa-nav-icon"></i>
						<span class="nav-label">总览</span>

				</a></li>
				<li id="menu_service"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/service.svg" /> <span
						class="nav-label">服务</span> <span class="fa arrow"></span>
				</a>
					<ul class="nav nav-second-level">
						<li id="li_service"><a class="J_menuItem" href="<%=path %>/service"><i class="fa fa-circle"></i>服务管理</a></li>
						<li id="li_storage"><a class="J_menuItem" href="<%=path %>/service/storage"><i class="fa fa-circle"></i>存储与备份</a></li>
						<li id="li_import"><a class="J_menuItem" href="<%=path %>/service/import"><i class="fa fa-circle"></i>引入外部服务</a></li>
					</ul></li>
				<li id="menu_ci"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/factory_new.svg" /> <span
						class="nav-label">构建</span> <span class="fa arrow"></span>
				</a>
					<ul class="nav nav-second-level">
						<li id="li_ci"><a class="J_menuItem" href="<%=path %>/ci"><i class="fa fa-circle"></i>镜像构建</a></li>
						<c:if test="${cur_user.user_autority == 1}">
						<li id="li_shera"><a class="J_menuItem" href="<%=path %>/user/shera"><i class="fa fa-circle"></i>Shera管理</a></li>
						</c:if>
					</ul></li>
				<li id="menu_registry"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/applist3.svg" /> <span
						class="nav-label">镜像</span> <span class="fa arrow"></span>
				</a>
					<ul class="nav nav-second-level">
						<li id="li_registry0"><a class="J_menuItem" href="<%=path %>/registry/0"><i class="fa fa-circle"></i>镜像中心</a></li>
						<li id="li_registry1"><a class="J_menuItem" href="<%=path %>/registry/1"><i class="fa fa-circle"></i>我的镜像</a></li>
						<li id="li_registry2"><a class="J_menuItem" href="<%=path %>/registry/2"><i class="fa fa-circle"></i>我的收藏</a></li>
					</ul></li>
				<c:if test="${!cas_enable}">
					<c:if test="${cur_user.user_autority == 1}">
						<li id="menu_user"><a href="#" class="first-a"> <img alt="image" class="tit-image"
								src="<%=path%>/images/user.svg" /> <span class="nav-label">租户</span>
								<span class="fa arrow"></span>
						</a>
							<ul class="nav nav-second-level">
								<li id="li_user"><a class="J_menuItem" href="<%=path %>/user/list"><i class="fa fa-circle"></i>租户管理</a></li>
							</ul></li>
					</c:if>
		        	<c:if test="${cur_user.user_autority == 2}">
						<li id="menu_usermanage"><a href="#"> <img alt="image" class="tit-image"
								src="<%=path%>/images/user.svg" /> <span class="nav-label">用户</span>
								<span class="fa arrow"></span>
						</a>
							<ul class="nav nav-second-level">
								<li id="li_manage"><a class="J_menuItem" href="<%=path %>/user/manage/list/${cur_user.id }"><i class="fa fa-circle"></i>用户管理</a></li>
								<li id="li_credential"><a class="J_menuItem" href="<%=path %>/secret/Credential"><i class="fa fa-circle"></i>密钥管理</a></li>
							</ul></li>
					</c:if>
				</c:if>
				<li id="menu_cluster"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/server.svg" /> <span class="nav-label">监控</span>
						<span class="fa arrow"></span>
				</a>
					<ul class="nav nav-second-level">
						<c:if test="${cur_user.user_autority == 1}">
						<li id="li_cluster"><a class="J_menuItem" href="<%=path %>/cluster/resource"><i class="fa fa-circle"></i>集群监控</a></li></c:if>
						<li id="li_container"><a class="J_menuItem" href="<%=path %>/cluster/containers"><i class="fa fa-circle"></i>容器监控</a></li>
						<c:if test="${cur_user.user_autority == 1}">
						<li id="li_management"><a class="J_menuItem" href="<%=path %>/cluster/management"><i class="fa fa-circle"></i>集群管理</a></li></c:if>
						<li id="li_topo"><a class="J_menuItem" href="<%=path %>/cluster/topo"><i class="fa fa-circle"></i>集群拓扑</a></li>
					</ul></li>
				<li id="menu_template"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/tem-image.png" /> <span
						class="nav-label">模板管理</span> <span class="fa arrow"></span>
				</a>
					<ul class="nav nav-second-level">
						<li id="li_dockerfile"><a class="J_menuItem" href="<%=path %>/template/dockerfile"><i class="fa fa-circle"></i>dockerfile</a></li>
						<li id="li_env"><a class="J_menuItem" href="<%=path %>/template/env"><i class="fa fa-circle"></i>环境变量</a></li>
					</ul></li>
				<%-- <li id="menu_product"><a href="<%=path %>/product/help" target="_blank"> <img alt="image" class="tit-image"
						src="<%=path%>/images/help-red.png"
						style="width: 14px; margin-left: 3px;" /> <span class="nav-label">帮助文档</span>
				</a></li> --%>
				
			</ul>
			<div class="helpDoc"><a href="<%=path %>/product/help" target="_blank"><input type="button" class="btn help-btn" value="新手入门"></a></div>
		</div>
	</nav>
	
</body>

<script type="text/javascript">

    $(function(){

        var menu_flag = '${menu_flag}';
        var li_flag = '${li_flag}';
        $("#menu_"+menu_flag).addClass("active");
        $("#li_"+li_flag).addClass("li-active");
        $("#menu_"+menu_flag).find('ul').addClass("in");
        $("#menu_"+menu_flag).find('ul').attr("aria-expanded","ture");
		
        // 菜单效果
        /* $(".nav-menu>li").on("mouseenter",function(){
            $(".nav-menu>li").css("background-color","transparent");
            $(".nav-menu>li").children(".nav-item-hover").css("display","none");
            $(this).css("background-color","#e86a6a");
            $(this).css("font-size","110%");
            $(this).children(".nav-item-hover").css("display","block");
        });

        $(".nav-menu>li").on("mouseleave",function(){
            $(this).css("background-color","transparent");
            $(this).children(".nav-item-hover").css("display","none");
            $(".item-click").css("background-color","#e86a6a");
            
        }); */

    });
    
    //分页插件中的时间格式的转换;
    function calendarFormat(data){
    	 var date = new Date(data);
         var y = date.getFullYear();  
         var m = date.getMonth() + 1;  
         m = m < 10 ? ('0' + m) : m;  
         var d = date.getDate();  
         d = d < 10 ? ('0' + d) : d;  
         var h = date.getHours();  
         h=h < 10 ? ('0' + h) : h;  
         var minute = date.getMinutes();  
         minute = minute < 10 ? ('0' + minute) : minute;  
         var second=date.getSeconds();  
         second=second < 10 ? ('0' + second) : second;  
         return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; s
    }
    


</script>

</html>
