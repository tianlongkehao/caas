<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>BCM</title>
    <%@include file="frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/home.css">
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
                	<li class="active"><a class="navTab first" href="<%=path %>/home">首页</a></li>
                	<li><a class="navTab" href="<%=path %>/registry/imageShow">镜像广场</a></li>
                	<li><a class="navTab" href="<%=path %>/bcm/${cur_user.id }">控制台</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>
<div class="funs">
	<div class="head-img">
		<img id="backgroundImg" src="<%=path %>/images/head-img1.png">
		<a href="<%=path %>/bcm/${cur_user.id }" class="pandectBtn"><i class="fa fa-home icon-home"></i><span>&nbsp;控制台</span></a>
	</div>
	<div class="head-title"><span><h2>容器云平台</h2></span></div>
	<div class="head-fun ">
		<div class="funs-list">
            <ul class="app-list">
                <li>
                   <a class="icon-view" href="<%=path %>/service">
                       	<div class="icon-wrapper">
                           <div class="icon-img" id="icon-service">
                                <span class="img "><img src="<%=path %>/images/new-service.png" alt=""/></span>
                                <span class="text hide">毫秒级启动容器，高度可扩展的高性能容器服务</span>
                           </div>
                       	   <div class="icon-name">服务集成</div>
                       </div>
                    </a>
                 </li>
                 <li>
                   <a class="icon-view" href="<%=path %>/ci;">
                       	<div class="icon-wrapper">
                           <div class="icon-img" id="icon-ci">
                                <span class="img"><img src="<%=path %>/images/new-ci.png" alt=""/></span>
                                <span class="text hide">实现应用的编译、测试、打包、发布流程的自动化</span>
                           </div>
                       	   <div class="icon-name">镜像构建</div>
                       </div>
                    </a>
                 </li>
                 <li>
                   <a class="icon-view" href="<%=path %>/registry/0">
                       	<div class="icon-wrapper">
                           <div class="icon-img" id="icon-image">
                                <span class="img"><img src="<%=path %>/images/new-image.png" alt=""/></span>
                                <span class="text hide">完全托管的docker容器注册表，轻松存储、管理和部署docker容器镜像</span>
                           </div>
                       	   <div class="icon-name">镜像中心</div>
                       </div>
                    </a>
                 </li>
                 <c:if test="${cur_user.user_autority == 1}">
                 <li>
                   <a class="icon-view" href="<%=path %>/cluster/resource">
                       	<div class="icon-wrapper">
                           <div class="icon-img" id="icon-cluster">
                                <span class="img"><img src="<%=path %>/images/new-cluster.png" alt=""/></span>
                                <span class="text hide">提供集群的部署、管理和监控的一整套解决方案</span>
                           </div>
                       	   <div class="icon-name">集群管理</div>
                       </div>
                    </a>
                 </li>
                 </c:if>
                 <c:if test="${cur_user.user_autority != 1}">
                 <li>
                   <a class="icon-view" href="<%=path %>/cluster/containers">
                       	<div class="icon-wrapper">
                           <div class="icon-img" id="icon-container">
                                <span class="img"><img src="<%=path %>/images/new-cluster.png" alt=""/></span>
                                <span class="text hide">提供该租户下的所有容器的监控和集群拓扑</span>
                           </div>
                       	   <div class="icon-name">监控信息</div>
                       </div>
                    </a>
                 </li>
                 </c:if>
                 <li>
                   <a class="icon-view" href="<%=path %>/template/dockerfile">
                       	<div class="icon-wrapper">
                           <div class="icon-img" id="icon-template">
                                <span class="img"><img src="<%=path %>/images/new-template.png" alt=""/></span>
                                <span class="text hide">统一管理模板，便于快速镜像构建</span>
                           </div>
                       	   <div class="icon-name">模板管理</div>
                       </div>
                    </a>
                 </li>
             </ul>
        </div>
		<%-- <div class="funCon service">
			<img src="<%=path%>/images/service-red.svg">
			<span class="service-tit"><h4>服务集成</h4></span>
		</div>
		<div class="funCon registry">
			<img src="<%=path%>/images/factory_new-red.svg">
			<span class="service-tit"><h4>镜像构建</h4></span>
		</div>
		<div class="funCon imageCenter">
			<img src="<%=path%>/images/applist3-red.svg">
			<span class="service-tit"><h4>镜像中心</h4></span>
		</div>
		<div class="funCon cluster">
			<img src="<%=path%>/images/server-red.svg">
			<span class="service-tit"><h4>集群管理</h4></span>
		</div>
		<div class="funCon template">
			<img src="<%=path%>/images/tem-image-red.png">
			<span class="service-tit"><h4>模板管理</h4></span>
		</div> --%>
	</div>

	<div class="head-title left"><span><h2>我们的优势</h2></span></div>
	<div class="funTab left">
		<div class="funTabRight left">
			<!-- <div class="tabBtn span-active" id="applyBtn"><span>应用部署</span></div>
			<div class="tabBtn" id="integrationBtn"><span>持续集成</span></div>
			<div class="tabBtn" id="microServieBtn"><span>微服务架构</span></div>
			<div class="tabBtn" id="saasBtn"><span>SAAS私有部署</span></div> -->

			<div class="tabBtn span-active" id="clusterBtn"><span>实时集群管理</span></div>
			<div class="tabBtn" id="usersBtn"><span>多租户管理</span></div>
			<div class="tabBtn" id="localCiBtn"><span>本地代码构建</span></div>
			<div class="tabBtn" id="imagesBtn"><span>丰富的镜像服务</span></div>
			<div class="tabBtn" id="autoOprationBtn"><span>自动化运维方案</span></div>
		</div>
		<div class="funTabLeft left">
			<!-- <div class="tabCon" id="applyCon"><span><h4>应用部署解决方案</h4></span><br>将自有大数据能力开放平台的工具应用、微服务应用、一线应用迁移到容器，支持租户一键部署，有效共享底层硬件资源，提高交付效率。利用容器资源隔离、分布式部署、水平动态扩展、负载均衡、高可用等核心技术为公共、专业、创新等各类应用的快速部署提供快速支撑</div>
			<div class="tabCon hide" id="integrationCon"><span><h4>持续集成解决方案</h4></span><br>研发了新的持续集成工具She-Ra，通过提供REST接口，与现有应用平台相结合，通过版本控制工具一站式持续集成，全程可视化管控，为用户提供简单、灵活的持续部署方式，抛弃Jenkins，实现更加一体化的持续集成环境。</div>
			<div class="tabCon hide" id="microServieCon"><span><h4>微服务架构解决方案</h4></span><br>基于容器云平台可以轻松构建微服务架构系统。微服务架构是一种架构模式，它提倡将单块架构的应用划分成一组小的服务，服务之间互相协调、互相配合，为用户提供最终价值。每个服务运行在其独立的进程中，服务与服务间采用轻量级的通信机制互相沟通。容器云平台现将存量经营平台、北十工作台拆分成微服务应用，实现一键增量部署。</div>
			<div class="tabCon hide" id="saasCon"><span><h4>SAAS私有部署解决方案</h4></span><br>容器云实现了应用快速部署及便捷管理，租户将部署包或者镜像包提交到能力开放平台，一键部署并运行，支持选择联通办公网、互联网等多种网络接入方式，提供内部应用和对外应用访问的图形化配置，运维人员不再接触任何物理机器，开发人员可以直接完成应用的部署，体现了DevOps中运维开发一体化的思想。</div> -->

			<div class="tabCon" id="clusterCon"><span><h4>实时集群管理</h4></span><br>通过自身平台的技术和优势为用户提供集群的部署、管理和监控的一整套解决方案。通过集群化的管理用户自己的实体主机、虚拟机或者 云主机上的资源，合理规划和充分利用现有的计算和存储资源，并在自己的私有集群上尝试、运用镜像和容器技术，通过这些新技术推进自己的产品 在开发、运维、部署、交付等各个环节上的变革及创新，逐渐搭建和形成自己的私有云架构。</div>
			<div class="tabCon hide" id="usersCon"><span><h4>多租户管理</h4></span><br>Kubernetes通过用户空间（namespace）来实现了一个简单的多租户模型，然后为每一个用户空间指定一定的配额，所有用户按照租户进行逻辑上的隔 离，Namespace能够帮助不同的租户共享同一个k8s集群。</div>
			<div class="tabCon hide" id="localCiCon"><span><h4>本地代码构建</h4></span><br>如果您的代码没有托管到GitHub或者BitBucket等代码托管平台上，只有本地的代码或者可部署的应用，BONC-PaaS云平台同样支持从代码到镜像的构建。可以支持 Windows、Linux和 Mac 三种平台，无需关联代码托管服务，如同使用本地 docker 一样的体验，不需要打包源代码文件，保证您的代码安全。</div>
			<div class="tabCon hide" id="imagesCon"><span><h4>丰富的镜像服务</h4></span><br>提供了涵盖云主机、云数据库、Web服务器和博客应用等多样化镜像服务，并进行了系统的分类，满足用户快速上手使用和高级定制化的各类需求。现已将自有大数据能力开放平台的工具应用、微服务应用、一线应用迁移到容器，第三方也逐步将应用部署到能力开放平台容器云上。
    <br>&nbsp;&nbsp;&nbsp;&nbsp;工具应用：元数据管理平台、数据质量管理平台、BPM流程调度平台、BI构建、BDI分布式ETL
   <br> &nbsp;&nbsp;&nbsp;&nbsp;基础应用：单点登录服务集成、数据服务平台、资源管理平台
   <br> &nbsp;&nbsp;&nbsp;&nbsp;一线应用：存量经营平台，拆分为将近20个微服务应用，实现一键增量部署</div>
			<div class="tabCon hide" id="autoOprationCon"><span><h4>自动化运维方案</h4></span><br>从代码到部署运维的一整套自动化方案，通过集成代码托管服务（支持GitHub、BitBucket、GitCafe、Coding等主要代码仓库），构建Docker 镜像；并一键部署到容器服务平台；动态调整服务的配置、对各个服务进行横向扩展，轻松实现复杂均衡、可自由伸缩的互联网应用框架。</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$(document).ready(function(){
	//$(".funTabLeft>div('.tabCon')").addClass("hide");
	$("#clusterBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#clusterCon").removeClass("hide");
	    $("#clusterBtn").addClass("span-active");
	});
	$("#usersBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#usersCon").removeClass("hide");
	    $("#usersBtn").addClass("span-active");
	});
	$("#localCiBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#localCiCon").removeClass("hide");
	    $("#localCiBtn").addClass("span-active");
	});
	$("#imagesBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#imagesCon").removeClass("hide");
	    $("#imagesBtn").addClass("span-active");
	});
	$("#autoOprationBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#autoOprationCon").removeClass("hide");
	    $("#autoOprationBtn").addClass("span-active");
	});

	$("#icon-service").mouseover(function(){
		$(this).find(".img").addClass("hide");
		$(this).find(".text").removeClass("hide");
	});
	$("#icon-service").mouseout(function(){
		$(this).find(".img").removeClass("hide");
		$(this).find(".text").addClass("hide");
	});
	$("#icon-ci").mouseover(function(){
		$(this).find(".img").addClass("hide");
		$(this).find(".text").removeClass("hide");
	});
	$("#icon-ci").mouseout(function(){
		$(this).find(".img").removeClass("hide");
		$(this).find(".text").addClass("hide");
	});
	$("#icon-image").mouseover(function(){
		$(this).find(".img").addClass("hide");
		$(this).find(".text").removeClass("hide");
	});
	$("#icon-image").mouseout(function(){
		$(this).find(".img").removeClass("hide");
		$(this).find(".text").addClass("hide");
	});
	$("#icon-cluster").mouseover(function(){
		$(this).find(".img").addClass("hide");
		$(this).find(".text").removeClass("hide");
	});
	$("#icon-cluster").mouseout(function(){
		$(this).find(".img").removeClass("hide");
		$(this).find(".text").addClass("hide");
	});
	$("#icon-container").mouseover(function(){
		$(this).find(".img").addClass("hide");
		$(this).find(".text").removeClass("hide");
	});
	$("#icon-container").mouseout(function(){
		$(this).find(".img").removeClass("hide");
		$(this).find(".text").addClass("hide");
	});
	$("#icon-template").mouseover(function(){
		$(this).find(".img").addClass("hide");
		$(this).find(".text").removeClass("hide");
	});
	$("#icon-template").mouseout(function(){
		$(this).find(".img").removeClass("hide");
		$(this).find(".text").addClass("hide");
	});

	adjustBtn();
})
window.onresize=function(){
	adjustBtn();
}
function adjustBtn(){
	var backgroundImgHeight = $("#backgroundImg").height();
    var pandectBtnPostion = backgroundImgHeight*0.92;
    $(".pandectBtn").css("top",pandectBtnPostion);
}
</script>
</body>
</html>
