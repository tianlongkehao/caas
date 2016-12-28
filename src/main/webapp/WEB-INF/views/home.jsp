<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>BCM</title>
   <%@include file="frame/dashboard-header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/core/layout.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/home.css">
</head>
<body>

<header class="header">
    <div class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a href="javascript:void(0);">
                    <h2>BCM</h2>
                </a>
            </div>
            <div class="navbar-login">
                <a href="<%=path %>/login" type="button" class="btn btn-default">登录</a>
            </div>
        </div>
    </div>
</header>
<div class="funs">
	<div class="head-img"><img src="<%=path %>/images/head-img1.png"></div>
	<div class="head-title"><span><h2>容器云平台</h2></span></div>
	<div class="head-title cloud-tit"><span>C-cloud</span></div>
	<div class="head-fun ">
		<div class="funCon service">
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
		</div>
	</div>
	
	<div class="head-title left"><span><h2>解决方案</h2></span></div>
	<div class="funTab left">
		<div class="funTabRight left">
			<div class="tabBtn span-active" id="applyBtn"><span>应用部署</span></div>
			<div class="tabBtn" id="integrationBtn"><span>持续集成</span></div>
			<div class="tabBtn" id="microServieBtn"><span>微服务架构</span></div>
			<div class="tabBtn" id="saasBtn"><span>SAAS私有部署</span></div>
		</div>
		<div class="funTabLeft left">
			<div class="tabCon" id="applyCon"><span><h4>应用部署解决方案</h4></span><br>将自有大数据能力开放平台的工具应用、微服务应用、一线应用迁移到容器，支持租户一键部署，有效共享底层硬件资源，提高交付效率。利用容器资源隔离、分布式部署、水平动态扩展、负载均衡、高可用等核心技术为公共、专业、创新等各类应用的快速部署提供快速支撑</div>
			<div class="tabCon hide" id="integrationCon"><span><h4>持续集成解决方案</h4></span><br>研发了新的持续集成工具She-Ra，通过提供REST接口，与现有应用平台相结合，通过版本控制工具一站式持续集成，全程可视化管控，为用户提供简单、灵活的持续部署方式，抛弃Jenkins，实现更加一体化的持续集成环境。</div>
			<div class="tabCon hide" id="microServieCon"><span><h4>微服务架构解决方案</h4></span><br>基于容器云平台可以轻松构建微服务架构系统。微服务架构是一种架构模式，它提倡将单块架构的应用划分成一组小的服务，服务之间互相协调、互相配合，为用户提供最终价值。每个服务运行在其独立的进程中，服务与服务间采用轻量级的通信机制互相沟通。容器云平台现将存量经营平台、北十工作台拆分成微服务应用，实现一键增量部署。</div>
			<div class="tabCon hide" id="saasCon"><span><h4>SAAS私有部署解决方案</h4></span><br>容器云实现了应用快速部署及便捷管理，租户将部署包或者镜像包提交到能力开放平台，一键部署并运行，支持选择联通办公网、互联网等多种网络接入方式，提供内部应用和对外应用访问的图形化配置，运维人员不再接触任何物理机器，开发人员可以直接完成应用的部署，体现了DevOps中运维开发一体化的思想。</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$(document).ready(function(){
	//$(".funTabLeft>div('.tabCon')").addClass("hide");
	$("#applyBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#applyCon").removeClass("hide");
	    $("#applyBtn").addClass("span-active");
	});
	$("#integrationBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#integrationCon").removeClass("hide");
	    $("#integrationBtn").addClass("span-active");
	});
	$("#microServieBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#microServieCon").removeClass("hide");
	    $("#microServieBtn").addClass("span-active");
	});
	$("#saasBtn").mouseover(function(){
	    $(".funTabLeft>div").addClass("hide");
	    $(".funTabRight>div").removeClass("span-active");
	    $("#saasCon").removeClass("hide");
	    $("#saasBtn").addClass("span-active");
	});
})

</script>
</body>
</html>