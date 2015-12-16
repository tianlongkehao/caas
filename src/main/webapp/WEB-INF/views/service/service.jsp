<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>服务</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/service.css"/>
    <script type="text/javascript" src="/js/service/service.js"></script>
</head>
<body>

<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">快速构建</li>
                </ol>
            </div>
            <div class="contentMain">


                <aside class="aside-btn">
                    <div class="btns-group">
                        <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">BONC PAAS</span></span>
                    </div>
                </aside>
                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="serviceReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <li><a href="/service/add" id="serviceCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="javascript:createContainer()">
                                        <i class="fa fa-play"></i>
                                        <span class="ic_left">启动</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:stopContainer()">
                                        <i class="fa fa-power-off"></i>
                                        <span class="ic_left">停止</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="">
                                        <i class="fa fa-arrows"></i>
                                        <span class="ic_left">弹性伸缩</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="">
                                        <i class="fa fa-arrow-up"></i>
                                        <span class="ic_left">灰度升级</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="">
                                        <i class="fa fa-undo"></i>
                                        <span class="ic_left">重新部署</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="">
                                        <i class="fa fa-cog"></i>
                                        <span class="ic_left">更改配置</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:delContainer()">
                                        <i class="fa fa-trash"></i>
                                        <span class="ic_left">删除</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <div class="searchs">
                        <form class="form-inline" action="">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="搜索">
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" type="button">
                                            <span class="glyphicon glyphicon-search"></span>
                                        </button>
                                    </span>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="itemTable">
                    <table class="table service-table">
                        <thead>
                            <tr>
                                <th>
                                    <div class="table-head">
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th style="width: 5%;text-indent: 30px;">
                                                        <input type="checkbox" id="btnCheckAll"/>
                                                    </th>
                                                    <th style="width: 20%;padding-left: 5px;">名称</th>
                                                    <th style="width: 10%;text-indent: 8px;">运行状态</th>
                                                    <th style="width: 20%;">镜像</th>
                                                    <th style="width: 34%;">服务地址</th>
                                                    <th style="width: 10%;">创建于</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </th>
                            </tr>
                        </thead>
                        <tbody id = "serviceBody">
                            <tr>
                                <td>
                                    <div>
                                        <table class="table">
                                            <tbody id="serviceList">
                                            <c:forEach items="${containerList}" var="container" >
                                            	<c:choose>
                                            		<c:when test="${container.containerStatus == 1 }">
                                            			<c:set var="statusName" value="未启动"></c:set>
                                                    	<c:set var="statusClassName" value="fa_stop"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${container.containerStatus == 2 }">
                                            			<c:set var="statusName" value="启动中"></c:set>
                                                    	<c:set var="statusClassName" value="fa_success"></c:set>
                                                    	<c:set var="loadingImgShowClass" value=""></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${container.containerStatus == 3 }">
                                            			<c:set var="statusName" value="运行中"></c:set>
                                                    	<c:set var="statusClassName" value="fa_success"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${container.containerStatus == 4 }">
                                            			<c:set var="statusName" value="已停止"></c:set>
                                                    	<c:set var="statusClassName" value="fa_stop"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                           	 	</c:choose>
                                           	 	<c:if test="${container.id == null || container.id == 0}">
                                                	<c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                           		</c:if>
                                           		<tr class="clusterId">
                                                        <td style="width:5%;text-indent: 30px;">
                                                            <input type="checkbox" name="chkItem" value="${container.id }" status="" imagename="" imagetag="" />
                                                        </td>
                                                        <td style="width:20%;white-space:nowrap;">
                                                            <b class="caret margin" style="transform: rotate(0deg);"></b>
                                                            <a href="/service/detail/${container.id}" class="cluster_mirrer_name">${container.containerName}</a>
                                                            <span class="number-node">${container.serviceNum }</span>
                                                            <span class="margin cursor console-code-modal" data-id="#console-code-modal">
                                                                <i class="fa fa-desktop" onclick="_showConsole('mysql');"></i>
                                                            </span>
                                                        </td>
                                                        <td style="width:10%" class="cStatusColumn">
                                                            <i class="${statusClassName}"></i> ${statusName}
                                                   	 		<img src="/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                        </td>
                                                        <td style="width:20%;">
                                                            <img src="https://dn-tenxstore.qbox.me/tenxcloud_mysql.png?imageView2/2/h/20" style="max-height:20px;max-width:40px;">
                                                            <span class="cluster_mirrer">
                                                                <a title="点击查看镜像" target="_blank" href="https://hub.tenxcloud.com/repos/tenxcloud/mysql">${container.imageName }</a>
                                                            </span>
                                                        </td>
                                                        <td style="width:34%" id="mysqlurl">
                                                            <span class="url">
                                                                <a href="http://mysql-lynnxu.tenxapp.com:25314" target="_blank">bonc</a>
                                                            </span>
                                                        </td>
                                                        <td style="width:10%" class="tdTimeStrap">
                                                            <input type="hidden" class="timeStrap" value="2015-11-30T02:23:28.000Z">
                                                            <i class="fa_time"></i>
                                                            <span>${container.createDate }</span>
                                                        </td>
                                                    </tr>
                                                    <c:forEach items="${serviceList}" var="service">
                                                    	<c:if test="${container.id == service.containerID}">
                                                    		<tr style="border-left:1px solid #eee;">
                                                        <td colspan="8"><div class="align-center">
                                                            <table class="table">
                                                                <thead style="background: #FAFAFA;border-top:1px solid #EDECEC;">
                                                                    <tr class="tr-row">
                                                                        <td style="width:5%">&nbsp;</td>
                                                                        <td style="width:20%;">
                                                                            <a style="margin-left: 19px;" href="/containers/tenx_district2/instances/mysql-s49b2">${service.serviceName }</a>
                                                                        </td>
                                                                        <td colspan="2" style="width:30%">
                                                                            <i class="${statusClassName}"></i> ${statusName}
                                                   	 						<img src="/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                                        </td>
                                                                        <td style="width:34%">bonc&nbsp; （内网）</td>
                                                                        <td style="width:10%">
                                                                            <i class="fa_time"></i>${service.createDate }
                                                                        </td>
                                                                    </tr>
                                                                </thead>
                                                            </table>
                                                        </div>
                                                        </td>
                                                    </tr>
                                                    	</c:if>
                                                    </c:forEach>
                                            
                                            </c:forEach>
                                            
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </article>
</div>

</body>
</html>