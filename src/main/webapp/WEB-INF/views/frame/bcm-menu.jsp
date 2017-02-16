<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path=request.getContextPath(); %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/bcm-menu.css">
    <script type="text/javascript" src="<%=path %>/js/customer/bcm-menu.js"></script>
    <title>BCM</title>
</head>
<body>
<header class="header">
    <div class="navbar navbar-fixed-top">
        <div class="container">
        	<div class="navbar-header">
                <span><a href="<%=path %>/home"><h1>BCM</h1></a></span>
            </div>
            <div class="navbar-tab">
                <ul>
                	<li><a class="navTab first" href="<%=path %>/home">首页</a></li>
                	<li><a class="navTab" href="<%=path %>/registry/imageShow">镜像广场</a></li>
                	<li class="active"><a class="navTab" href="<%=path %>/bcm/${cur_user.id }">控制台</a></li>
                </ul>
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
                        </c:if>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    
</header>

<div class="">
	<c:if test="${!cas_enable}">
	<div class="nav-title hide" >
		<ul class="nav" >
				<li class="nav-bcm hide">
					<span><a href="<%=path %>/home"><h1></h1></a></span>
				</li>
				<li class="nav-header ">
					<div class="dropdown profile-element">
						<div style="float:left">
							<img alt="image" class="img-circle" src="<%=path%>/images/dockerui.png" />
						</div> 
						<div class="userNameTit">
							<c:if test="${!cas_enable}"><span class="t-username"><strong>${cur_user.userName }</strong></span></c:if>
							<br>
						<span>
							<a href="<%=path %>/user/detail/${cur_user.id }/a" title="基本信息">&nbsp;<i class="fa fa-user"></i>&nbsp;</a>
							<c:if test="${!cas_enable}">
								<a href="<%=path %>/user/detail/${cur_user.id }/b" title="修改密码">&nbsp;<i class="fa fa-unlock-alt"></i>&nbsp;</a>
								<c:choose>
							        <c:when test="${ssoConfig.enable}">
							        	<a href="${ssoConfig.serverLogoutUrl}" title="退出登录">&nbsp;<i class="fa fa-power-off"></i>&nbsp;</a>
							        </c:when>
							        <c:otherwise>
							        	<a href="<%=path %>/loginout/${cur_user.id }" title="退出登录">&nbsp;<i class="fa fa-power-off"></i>&nbsp;</a>
							        </c:otherwise>
						        </c:choose>
							</c:if>
						</span>
					</div>
				</div>
			</li>
		</ul>
	</div>
	</c:if>
	<div ><nav class="navbar-default navbar-static-side" role="navigation" id="nav-side">
		<div class="sidebar-collapse">
			<ul class="nav" id="side-menu">
				<li id="menu_bcm" onclick="changeIconAll()"><a href="<%=path %>/bcm/${cur_user.id }" class="first-a"> 
						<img alt="image" class="tit-image" src="<%=path%>/images/u54.png" />
						<span class="nav-label">总览</span>

				</a></li>
				<li id="menu_service" onclick="changeIconService()"><a href="#" class="first-a"> 
					<img alt="image" class="tit-image" src="<%=path%>/images/u35.png" /> 
					<span class="nav-label">服务</span> 
				</a>
					<ul class="nav nav-second-level">
						<li id="li_service"><a class="J_menuItem" href="<%=path %>/service"><i class="fa fa_circle"></i>服务管理</a></li>
						<li id="li_storage"><a class="J_menuItem" href="<%=path %>/service/storage"><i class="fa fa_circle"></i>存储与备份</a></li>
						<li id="li_import"><a class="J_menuItem" href="<%=path %>/service/import"><i class="fa fa_circle"></i>引入外部服务</a></li>
					</ul></li>
				<li id="menu_ci" onclick="changeIconCi()"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/u21.png" /> <span
						class="nav-label">构建</span> 
				</a>
					<ul class="nav nav-second-level">
						<li id="li_ci"><a class="J_menuItem" href="<%=path %>/ci"><i class="fa fa_circle"></i>镜像构建</a></li>
						<c:if test="${cur_user.user_autority == 1}">
						<li id="li_shera"><a class="J_menuItem" href="<%=path %>/user/shera"><i class="fa fa_circle"></i>Shera管理</a></li>
						</c:if>
					</ul></li>
				<li id="menu_registry" onclick="changeIconImage()"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/u28.png" /> <span
						class="nav-label">镜像</span> 
				</a>
					<ul class="nav nav-second-level">
						<li id="li_registry0"><a class="J_menuItem" href="<%=path %>/registry/0"><i class="fa fa_circle"></i>镜像中心</a></li>
						<li id="li_registry1"><a class="J_menuItem" href="<%=path %>/registry/1"><i class="fa fa_circle"></i>我的镜像</a></li>
						<li id="li_registry2"><a class="J_menuItem" href="<%=path %>/registry/2"><i class="fa fa_circle"></i>我的收藏</a></li>
					</ul></li>
				<c:if test="${!cas_enable}">
					<c:if test="${cur_user.user_autority == 1}">
						<li id="menu_user" onclick="changeIconUser()"><a href="#" class="first-a"> <img alt="image" class="tit-image"
								src="<%=path%>/images/u60.svg" /> <span class="nav-label">租户</span>
								
						</a>
							<ul class="nav nav-second-level">
								<li id="li_user"><a class="J_menuItem" href="<%=path %>/user/list"><i class="fa fa_circle"></i>租户管理</a></li>
							</ul></li>
					</c:if>
		        	<c:if test="${cur_user.user_autority == 2}">
						<li id="menu_usermanage" onclick="changeIconUser()"><a href="#"> <img alt="image" class="tit-image"
								src="<%=path%>/images/u60.svg" /> <span class="nav-label">用户</span>
								
						</a>
							<ul class="nav nav-second-level">
								<li id="li_manage"><a class="J_menuItem" href="<%=path %>/user/manage/list/${cur_user.id }"><i class="fa fa_circle"></i>用户管理</a></li>
								<li id="li_credential"><a class="J_menuItem" href="<%=path %>/secret/Credential"><i class="fa fa_circle"></i>密钥管理</a></li>
							</ul></li>
					</c:if>
				</c:if>

                <c:if test="${cas_enable}">
                    <c:if test="${cur_user.user_autority == 1}">
                        <li id="menu_user" onclick="changeIconUser()"><a href="#" class="first-a"> <img alt="image" class="tit-image"
                                src="<%=path%>/images/u60.svg" /> <span class="nav-label">租户</span>
                                
                        </a>
                            <ul class="nav nav-second-level">
                                <li id="li_user"><a class="J_menuItem" href="<%=path %>/user/list"><i class="fa fa_circle"></i>租户管理</a></li>
                            </ul></li>
                    </c:if>
                    <c:if test="${cur_user.user_autority == 2}">
                        <li id="menu_usermanage"><a href="#"> <img alt="image" class="tit-image"
                                src="<%=path%>/images/user.svg" /> <span class="nav-label">密钥</span>
                                
                        </a>
                            <ul class="nav nav-second-level">
                                <li id="li_credential"><a class="J_menuItem" href="<%=path %>/secret/Credential"><i class="fa fa_circle"></i>密钥管理</a></li>
                            </ul></li>
                    </c:if>
                </c:if>


				<li id="menu_cluster" onclick="changeIconCluster()"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/u40.png" /> <span class="nav-label">监控</span>
						
				</a>
					<ul class="nav nav-second-level">
						<c:if test="${cur_user.user_autority == 1}">
						<li id="li_cluster"><a class="J_menuItem" href="<%=path %>/cluster/resource"><i class="fa fa_circle"></i>集群监控</a></li></c:if>
						<li id="li_container"><a class="J_menuItem" href="<%=path %>/cluster/containers"><i class="fa fa_circle"></i>容器监控</a></li>
						<c:if test="${cur_user.user_autority == 1}">
						<li id="li_management"><a class="J_menuItem" href="<%=path %>/cluster/management"><i class="fa fa_circle"></i>集群管理</a></li></c:if>
						<li id="li_topo"><a class="J_menuItem" href="<%=path %>/cluster/topo"><i class="fa fa_circle"></i>集群拓扑</a></li>
						<%-- <c:if test="${cur_user.user_autority == 1}">
						<li id="li_route"><a class="J_menuItem" href="<%=path %>/cluster/route"><i class="fa fa_circle"></i>route</a></li>
						<li id="li_iptables"><a class="J_menuItem" href="<%=path %>/cluster/iptables"><i class="fa fa_circle"></i>iptables</a></li>
						</c:if> --%>
					</ul></li>
				<li id="menu_template" onclick="changeIconTemplate()"><a href="#" class="first-a"> <img alt="image" class="tit-image"
						src="<%=path%>/images/u47.png" /> <span
						class="nav-label">模板</span> 
				</a>
					<ul class="nav nav-second-level">
						<li id="li_dockerfile"><a class="J_menuItem" href="<%=path %>/template/dockerfile"><i class="fa fa_circle"></i>dockerfile</a></li>
						<li id="li_env"><a class="J_menuItem" href="<%=path %>/template/env"><i class="fa fa_circle"></i>环境变量</a></li>
					</ul></li>
				<c:if test="${cur_user.user_autority == 1}">
				<li id="menu_log" onclick="changeIconLog()"><a href="#" class="first-a"> <img alt="log" class="tit-image"
						src="<%=path%>/images/u48.png" /> <span
						class="nav-label">日志</span> 
				</a>
					<ul class="nav nav-second-level">
						<li id="li_logService"><a class="J_menuItem" href="<%=path %>/logServices"><i class="fa fa_circle"></i>服务操作日志</a></li>
						<li id="li_logCommon"><a class="J_menuItem" href="<%=path %>/logCommon"><i class="fa fa_circle"></i>通用操作日志</a></li>
					</ul></li>
				</c:if>
				<%-- <li id="menu_product"><a href="<%=path %>/product/help" target="_blank"> <img alt="image" class="tit-image"
						src="<%=path%>/images/help-red.png"
						style="width: 14px; margin-left: 3px;" /> <span class="nav-label">帮助文档</span>
				</a></li> --%>
				
			</ul>
		</div>
	</nav></div>
	<div class="helpDoc hide">
		<ul class="nav" >
			<li class="nav-footer">
				<span><a href="<%=path %>/product/help" target="_blank"><input type="button" class="btn help-btn" value="新手入门"></a></span>
			</li>
		</ul>
	</div>
	<div class="helpDoc hide"><a href="<%=path %>/product/help" target="_blank"><input type="button" class="btn help-btn" value="新手入门"></a></div>
	<div class="sideBoxBtn hide" onclick="navHide()"><i class="fa fa-step-backward"></i><div class="btn-background"></div></div>
</div><!-- navAll -->

<div class="navbar-downSide-show hide">
	<nav class="navbar-downSide" ></nav>
	<div class="sideBoxBtn sideBoxBtnShow" onclick="navShow()"><i class="fa fa-step-forward"></i><div class="btn-background"></div></div>
</div>
	

</body>

<script type="text/javascript">

    $(function(){

        var menu_flag = '${menu_flag}';
        var li_flag = '${li_flag}';
        $("#menu_"+menu_flag).addClass("active");
        $("#li_"+li_flag).addClass("li-active");
        $("#menu_"+menu_flag).find('ul').addClass("in");
        $("#menu_"+menu_flag).find('ul').attr("aria-expanded","ture");
        
        var oldSrc = $("#menu_"+menu_flag+".active").find("img").attr("src");
        var newSrc = oldSrc.substring(0,11)+"w"+oldSrc.substring(11,15);
        $("#menu_"+menu_flag+".active").find("img").attr("src",newSrc);
        //$("#menu_"+menu_flag).find("img").attr("src",oldSrc);
		
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
    
	function changeIconAll(){
		$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54w.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    function changeIconService(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35w.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    function changeIconCi(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21w.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    function changeIconImage(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28w.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    function changeIconUser(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60w.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60w.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    
    function changeIconCluster(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40w.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    function changeIconTemplate(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47w.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48.png");
    }
    function changeIconLog(){
    	$("#menu_bcm").find("img").attr("src","<%=path%>/images/u54.png");
    	$("#menu_service").find("img").attr("src","<%=path%>/images/u35.png");
    	$("#menu_ci").find("img").attr("src","<%=path%>/images/u21.png");
    	$("#menu_registry").find("img").attr("src","<%=path%>/images/u28.png");
    	$("#menu_user").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_usermanage").find("img").attr("src","<%=path%>/images/u60.svg");
    	$("#menu_cluster").find("img").attr("src","<%=path%>/images/u40.png");
    	$("#menu_template").find("img").attr("src","<%=path%>/images/u47.png");
    	$("#menu_log").find("img").attr("src","<%=path%>/images/u48w.png");
    }
    
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
