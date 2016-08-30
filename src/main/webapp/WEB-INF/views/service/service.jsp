<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>服务</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/service.css"/>
    <script type="text/javascript" src="<%=path %>/js/service/service.js"></script>
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
                        <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">BCM</span></span>
                    </div>
                </aside>
                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="serviceReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <li><a href="<%=path %>/service/add" id="serviceCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                            <ul class="dropdown-menu serviceOperation">
                                <li>
                                    <a id="startContainer" class="no-drop" href="javascript:createContainer()">
                                        <i class="fa fa-play"></i>
                                        <span class="ic_left">启动</span>
                                    </a>
                                </li>
                                <li>
                                    <a id="stopContainer" class="no-drop" href="javascript:stopContainer()">
                                        <i class="fa fa-power-off"></i>
                                        <span class="ic_left">停止</span>
                                    </a>
                                </li>
                                  <li>
                                    <a id="scaleCluster" class="no-drop" href="javascript:upGradeContainer()">
                                        <i class="fa fa-arrows"></i>
                                        <span class="ic_left">弹性伸缩</span>
                                    </a>
                                </li>
                               <li>
                                    <a id="upgradeCluster" class="no-drop" href="javascript:versionUpgrade()"  >
                                        <i class="fa fa-arrow-up"></i>
                                        <span class="ic_left">版本升级</span>
                                    </a>
                                </li>
                               <!-- <li>
                                    <a id="redeployContainer" class="no-drop" href="">
                                        <i class="fa fa-undo"></i>
                                        <span class="ic_left">重新部署</span>
                                    </a>
                                </li>-->
                                <li>
                                    <a id="changeConfiguration" class="no-drop" href="javascript:changeContainerConf();">
                                        <i class="fa fa-cog"></i>
                                        <span class="ic_left">更改配置</span>
                                    </a>
                                </li>
                                 
                                <li>
                                    <a id="deleteButton" class="no-drop" href="javascript:delContainer()">
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
                              <input value="1" id="numberChange" class="" min="1" max="" style="margin-top: 10px;width: 165px;" type="number">
                              <span class="unit">个</span>
							  <!-- <span  style="color: grey;margin-left: 50px;">可用实例数量：<label id="leftpod" ></label></span> -->
                            </div>
                          </li>
                        </ul>
                    </div>
                    <div id="versionUpgrade" style="display:none">
                    	<ul class="popWin">
                          <li class="line-h-3">
                            <span class="edit-name-c">服务名称：</span>
                            <input id="upgradeVersionServiceName" disabled="disabled" style="margin-top: 5px;width: 165px;" type="text" value="">
                          </li>
                          <li class="line-h-3">
                            <span class="edit-name-c">镜像名称：</span>
                            <input id="upgradeimgName" disabled="disabled" style="margin-top: 5px;width: 165px;" type="text" value="">
                          </li>
                          <li class="line-h-3" id="instsizeChange">
                            <div class="param-set">
                              <span class="edit-name-c" style="margin-top: 5px;">镜像版本：</span>
                              <!--<select class="form-control" style="margin-top: 10px;width: 165px;display: inline; float: right;" id="imgVersionName" name="user_autority">
                                     <option name="user_autority" value="2">租户</option>
                                     <option name="user_autority" value="1">管理员</option>
                                                    </select>  -->
                              <select id="imgVersionName" name="imgVersionName" style="margin-top: 10px;width: 165px;">
                              
                              </select>
                             <!--  <input value="" id="imgVersionName" class=""  style="margin-top: 10px;width: 165px;" type="text"> -->
							  <!-- <span  style="color: grey;margin-left: 50px;">可用实例数量：<label id="leftpod" ></label></span> -->
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
                                    <input type="number" value="" style="margin-top: 10px;width: 165px;" min="${cpumin }" max="${cpumax }" autocomplete="off" step="0.1"
                                           placeholder="" id="confCpu" name="confCpu">
                                    <span class="unit">个</span>
                                   <!-- <span style="color: grey;margin-left: 50px;">当前可用cpu数量：<label id="leftcpu" ></label></span> -->
                                </div>
                            </li>
                            <li class="line-h-3">
                                <div class="param-set">
                                    <span>内存：</span>
                                    <input id="confRamSlider" data-slider-id='confRamSliderData' type="text" data-slider-min="${memorymin }" data-slider-max="${memorymax }" data-slider-step="1" />
                                    <input type="text" left="" value="" id="confRamSlider_input" name="confRam">
                                    <span>M</span>
                                   <!-- <span style="color: grey;">当前可用ram：<label id="leftram" ></label>M</span>-->
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="searchs">
                    	<form id="search_form" class="form-inline" action="<%=path %>/service/findservice" method="post">
                            <div class="form-group">
                                <div class="input-group">
                                    <input id="searchNames" name="searchNames" type="text" class="form-control" placeholder="搜索">
                                    <span class="input-group-btn">
                                        <button id="serviceSearch" class="btn btn-primary" type="submit">
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
                                                        <input type="checkbox" autocomplete="off" class="chkAll"/>
                                                    </th>
                                                    <th style="width: 20%;padding-left: 5px;">名称</th>
                                                    <th style="width: 10%;text-indent: 8px;">运行状态</th>
                                                    <th style="width: 20%;">镜像</th>
                                                    <th style="width: 24%;">服务地址</th>
                                                    <th style="width: 12%;">创建于</th>
                                                    <th style="width: 8%;">操作</th>
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
                                                            <input type="checkbox" class="chkItem" name="chkItem" autocomplete="off" id="checkboxID" value="${service.id }" serviceName="${service.serviceName }" serviceNum="${service.instanceNum }" confRam="${service.ram }"status="${service.status }" imagename="${service.imgName }" imageversion="${service.imgVersion }" confCpu="${service.cpuNum }"/>
                                                        </td>
                                                        <td style="width:20%;white-space:nowrap;">
                                                            <b class="caret margin" style="transform: rotate(-90deg);"></b>
                                                            <a href="<%=path %>/service/detail/${service.id}" class="cluster_mirrer_name">${service.serviceName}</a>
                                                            <span  class="number-node">${service.instanceNum }</span>
                                                            <span class="margin cursor console-code-modal" data-id="#console-code-modal">
                                                                <i class="fa fa-desktop" onclick="_showConsole('mysql');"></i>
                                                            </span>
                                                        </td>
                                                        <td style="width:10%" class="cStatusColumn" id="containerStatus">
                                                            <i class="${statusClassName}"></i> ${statusName}
                                                   	 		<img src="<%=path %>/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
                                                        </td>
                                                        <td style="width:20%;">
                                                            <span class="cluster_mirrer">
                                                                <a title="点击查看镜像" target="_blank" href="<%=path %>../registry/detail/${service.imgID }">${service.imgName }</a>
                                                            </span>
                                                        </td>
                                                        <td style="width:24%" id="mysqlurl">
                                                            <span class="url">
                                                            	<c:if test="${service.serviceAddr!=null&&service.serviceAddr!='' }">
                                                                	<a href="${service.serviceAddr}/${service.proxyPath}" target="_blank">${service.serviceAddr}/${service.proxyPath}</a>
                                                                </c:if>
                                                            </span>
                                                        </td>
                                                        <td style="width:12%" class="tdTimeStrap">
                                                            <input type="hidden" class="timeStrap" value="">
                                                            <i class="fa_time"></i>
                                                            <span>${service.createDate }</span>
                                                        </td>
                                                        <td style="width:8%">
                                                            <a id="${service.id}_start" class="startContainer_a" href="javascript:oneStartContainer(${service.id},${service.status})">
					                                        			<i class="fa fa-play"></i>
						                                    	  </a>
						                                    	  <a id="${service.id}_stop" class="stopContainer_a" href="javascript:oneStopContainer(${service.id},${service.status})" style="margin-left:10px">
						                                        		<i class="fa fa-power-off"></i>
						                                    	  </a>
						                                    	  <a id="${service.id}_del" class="deleteButton_a" href="javascript:oneDeleteContainer(${service.id})" style="margin-left:10px">
						                                        		<i class="fa fa-trash"></i>
						                                        </a>
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
                                                   	 						<img src="<%=path %>/images/loading4.gif" alt="" class="${loadingImgShowClass}" />
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
				<div id="pagination"></div>
            </div>
        </div>
    </article>
</div>
     <!--版本升级进度条 -->
    <div class="modal fade container" id="myModal" tabindex="-1" role="dialog"
        aria-labelledby="myModalLabel" aria-hidden="true" style="width:30%">
        <div class="progress progress-striped active" id="loading"
            style="margin-top: 87%;">
            <div class="progress-bar progress-bar-info" role="progressbar"
                aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
                style="width: 100%;">
            </div>
        </div>
    </div>


${msg}
<c:if test="${msg!= null} ">
    <input type="hidden" id="errorMsg" value="${msg}">
    <script type="text/javascript">
    	
        //var errorMsg = $("#errorMsg").val();
        var errorMsg = '${msg}';
        alert(errorMsg);
    </script>
</c:if>

</body>
</html>