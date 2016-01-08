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
    <jsp:param name="service" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">服务</li>
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
                                    <a href="javascript:upGradeContainer()">
                                        <i class="fa fa-arrows"></i>
                                        <span class="ic_left">弹性伸缩</span>
                                    </a>
                                </li>
                               <!--<li>
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
                                </li>-->
                                <li>
                                    <a href="javascript:changeContainerConf();">
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
                    <div id="upgrade" style="display:none">
                    	<ul class="popWin">
                          <li class="line-h-3">
                            <span class="edit-name-c">服务名称：</span>
                            <input id="upgradeServiceName" disabled="disabled" style="margin-top: 5px;width: 165px;" type="text" value="">
                          </li>
                          <li class="line-h-3" id="instsizeChange">
                            <div class="param-set">
                              <span class="edit-name-c" style="margin-top: 5px;">实例数量：</span>
                              <input value="1" id="numberChange" class="" min="1" max="${leftpod }" style="margin-top: 10px;width: 165px;" type="number">
                              <span class="unit">个</span>
							  <span  style="color: grey;margin-left: 50px;">可用实例数量：<label id="leftpod" ></label></span>
                            </div>
                          </li>
                        </ul>
                    </div>
                    <div id="changeConf" style="display:none">
                    	<ul class="popWin">
                            <li class="line-h-3">
                                <span>服务名称：</span>
                                <input class="" id="confServiceName" disabled="disabled" style="margin-top: 5px;width: 165px;" type="text" value="">
                            </li>
                            <li class="line-h-3">
                                <div class="param-set">
                                    <span>CPU数量：</span>
                                    <input type="number" value="1" style="margin-top: 10px;width: 165px;" min="1" max="${leftcpu }" autocomplete="off"
                                           placeholder="1" id="confCpu" name="confCpu">
                                    <span class="unit">个</span>
                                    <span style="color: grey;margin-left: 50px;">当前可用cpu数量：<label id="leftcpu" ></label></span>
                                </div>
                            </li>
                            <li class="line-h-3">
                                <div class="param-set">
                                    <span>内存：</span>
                                    <input id="confRamSlider" data-slider-id='confRamSliderData' type="text" data-slider-min="0" data-slider-step="1" />
                                    <input type="text" left="${leftram }" value="" id="confRamSlider_input" name="confRam">
                                    <span>M</span>
                                    <span style="color: grey;">当前可用ram：<label id="leftram" ></label>M</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="searchs">
                        
                            <div class="form-group">
                                <div class="input-group">
                                    <input id="searchName" type="text" class="form-control" placeholder="搜索">
                                    <span class="input-group-btn">
                                        <button id="serviceSearch" class="btn btn-primary" type="button">
                                            <span class="glyphicon glyphicon-search"></span>
                                        </button>
                                    </span>
                                </div>
                            </div>
                        
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
                                                        <input type="checkbox" class="chkAll"/>
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
                                            <c:forEach items="${serviceList}" var="service" >
                                            	<c:choose>
                                            		<c:when test="${service.status == 1 }">
                                            			<c:set var="statusName" value="未启动"></c:set>
                                                    	<c:set var="statusClassName" value="fa_stop"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${service.status == 2 }">
                                            			<c:set var="statusName" value="启动中"></c:set>
                                                    	<c:set var="statusClassName" value="fa_success"></c:set>
                                                    	<c:set var="loadingImgShowClass" value=""></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${service.status == 3 }">
                                            			<c:set var="statusName" value="运行中"></c:set>
                                                    	<c:set var="statusClassName" value="fa_run"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${service.status == 4 }">
                                            			<c:set var="statusName" value="已停止"></c:set>
                                                    	<c:set var="statusClassName" value="fa_stop"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                                    <c:when test="${service.status == 5 }">
                                            			<c:set var="statusName" value="启动失败"></c:set>
                                                    	<c:set var="statusClassName" value="fa_stop"></c:set>
                                                    	<c:set var="loadingImgShowClass" value="hide"></c:set>
                                                    </c:when>
                                           	 	</c:choose>
                                           	 	<c:if test="${service.id == null || service.id == 0}">
                                                	<c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                           		</c:if>

                                            <tr id="inst_${service.id }" memory="512MB" class="show-tr">
                                                <td>
                                                    <div class="contents-table">
                                                        <table class="table">
                                                            <tbody>

                                           		<tr class="clusterId" containerName="${service.serviceName }">
                                                        <td style="width:5%;text-indent: 30px;">
                                                            <input type="checkbox" class="chkItem" name="chkItem" value="${service.id }" serviceName="${service.serviceName }" serviceNum="${service.instanceNum }" confRam="${service.ram }"status="" imagename="" imagetag="" confCpu="${service.cpuNum }"/>
                                                        </td>
                                                        <td style="width:20%;white-space:nowrap;">
                                                            <b class="caret margin" style="transform: rotate(-90deg);"></b>
                                                            <a href="/service/detail/${service.id}" class="cluster_mirrer_name">${service.serviceName}</a>
                                                            <span  class="number-node">${service.instanceNum }</span>
                                                            <span class="margin cursor console-code-modal" data-id="#console-code-modal">
                                                                <i class="fa fa-desktop" onclick="_showConsole('mysql');"></i>
                                                            </span>
                                                        </td>
                                                        <td style="width:10%" class="cStatusColumn" id="containerStatus">
                                                            <i class="${statusClassName}"></i> ${statusName}
                                                   	 		<img src="/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                        </td>
                                                        <td style="width:20%;">
                                                            <span class="cluster_mirrer">
                                                                <a title="点击查看镜像" target="_blank" href="../registry/detail/${service.imgID }">${service.imgName }</a>
                                                            </span>
                                                        </td>
                                                        <td style="width:34%" id="mysqlurl">
                                                            <span class="url">
                                                            	<c:if test="${service.serviceAddr!=null&&service.serviceAddr!='' }">
                                                                	<a href="${service.serviceAddr }:${service.portSet}${service.serviceLink}" target="_blank">${service.serviceAddr }:${service.portSet}${service.serviceLink}</a>
                                                                </c:if>
                                                            </span>
                                                        </td>
                                                        <td style="width:10%" class="tdTimeStrap">
                                                            <input type="hidden" class="timeStrap" value="">
                                                            <i class="fa_time"></i>
                                                            <span>${service.createDate }</span>
                                                        </td>
                                                    </tr>

                                                    <tr style="border-left:1px solid #eee;" class="hide">
                                                        <td colspan="8"><div class="align-center">
                                                            <table class="table">
                                                                <thead style="background: #FAFAFA;border-top:1px solid #EDECEC;">
                                                                <c:forEach items="${containerList}" var="container">
                                                                   <c:if test="${container.serviceid == service.id}">
                                                                    <tr class="tr-row">
                                                                        <td style="width:5%">&nbsp;</td>
                                                                        <td style="width:20%;">
                                                                            <a style="margin-left: 19px;" href="javascript:void(0)">${container.containerName }</a>
                                                                        </td>
                                                                        <td colspan="2" style="width:30%">
                                                                            <i class="${statusClassName}"></i> ${statusName}
                                                   	 						<img src="/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                                        </td>
                                                                        <td style="width:34%"></td>
                                                                        <td style="width:10%">
                                                                            
                                                                        </td>
                                                                    </tr>
                                                                 	</c:if>
                                                                </c:forEach>
                                                                </thead>
                                                            </table>
                                                        </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </td>
                                                </tr>
                                            
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

<c:if test="${msg} != null">
    <input type="hidden" id="errorMsg" value="${msg}">
    <script type="text/javascript">
        var errorMsg = $("#errorMsg").val();
        layer.alert(errorMsg);
    </script>
</c:if>

</body>
</html>