<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/cluster.css"/>
    <script type="text/javascript" src="/js/cluster/cluster-management.js"></script>
</head>
<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="cluster" value=""/>
</jsp:include>
<input type="hidden" id="checkedHosts">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">集群管理</li>
                </ol>
            </div>
            <div class="contentMain">

                <aside class="aside-btn">
                    <div class="btns-group">
                        <a id="clusterManage" class="Record "><span class="btn btn-defaults btn-white"><span
                                class="ic_left">集群管理</span></span></a>
                    </div>
                </aside>

                <div>
                    <div id="clusterManage_wrap" class="tab_wrap">
                        <div class="caption clearfix">
                            <ul class="toolbox clearfix">
                                <li><a href="javascript:void(0);" id="userReloadBtn"><i class="fa fa-repeat"></i></a>
                                </li>
                                <li><a href="/cluster/add" id="userCreateBtn"><i
                                        class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                                <%--<li class="dropdown">
                                    <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i
                                            class="fa fa-caret-down"></i></a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a href="javascript:resource_detail();">
                                                <i class="fa fa-play"></i>
                                                <span class="ic_left">删除</span>
                                            </a>
                                        </li>
                                    </ul>
                                </li>--%>
                            </ul>
                            <div id="upgrade" style="display:none">
                                <ul class="popWin">
                                    <li class="line-h-3">
                                        <span class="edit-name-c">服务名称：</span>
                                        <input id="upgradeServiceName" disabled="disabled"
                                               style="margin-top: 5px;width: 165px;" type="text" value="">
                                    </li>
                                    <li class="line-h-3" id="instsizeChange">
                                        <div class="param-set">
                                            <span class="edit-name-c" style="margin-top: 5px;">实例数量：</span>
                                            <input value="1" id="numberChange" class="" min="1"
                                                   style="margin-top: 10px;width: 165px;" type="number">
                                            <span class="unit">个</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <div id="changeConf" style="display:none">
                                <ul class="popWin">
                                    <li class="line-h-3">
                                        <span>服务名称：</span>
                                        <input class="" id="confServiceName" disabled="disabled"
                                               style="margin-top: 5px;width: 165px;" type="text" value="">
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span>CPU数量：</span>
                                            <input type="number" value="1" style="margin-top: 10px;width: 165px;"
                                                   min="1" autocomplete="off"
                                                   placeholder="1" id="" name="">
                                            <span class="unit">个</span>
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span>内存：</span>
                                            <input id="confRamSlider" data-slider-id='confRamSlider' type="text"
                                                   data-slider-min="0" data-slider-max="2024" data-slider-step="1"
                                                   data-slider-value="256"/>
                                            <input type="text" value="256" id="confRam" name="confRam">
                                            <span>M</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <div class="searchs">
                                <form class="form-inline" action="/cluster/searchCluster" method="post">
                                    <div class="form-group">
                                        <div class="input-group">
                                            <input type="text" class="form-control" placeholder="搜索IP地址关键字" name="searchIP">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary" type="submit">
                                            <span class="glyphicon glyphicon-search"></span>
                                        </button>
                                    </span>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div class="itemTable">
                            <table class="table cluster-table">
                                <thead>
                                <tr>
                                    <th>
                                        <div class="table-head">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th style="width: 3%;text-indent: 30px;">
                                                        <%--<input type="checkbox" class="chkAll"/>--%>
                                                    </th>
                                                    <th style="width: 20%;padding-left: 5px;">IP</th>
                                                    <th style="width: 20%;text-indent: 8px;">节点类型</th>
                                                    <th style="width: 20%;">状态</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${lstClusters}" var="cluster">
                                                    <c:if test="${cluster.host == null || cluster.host == '0'}">
                                                        <c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                                    </c:if>
                                                    <c:if test="${cur_cluster.host != cluster.host}">
                                                        <tr class="clusterTr" id="${cluster.host }">
                                                            <td style="width: 3%;text-indent: 30px;">
                                                                <%--<input type="checkbox" class="chkItem" name="hosts"
                                                                       value="${cluster.id}">--%>
                                                            </td>
                                                            <td style="width: 20%;padding-left: 5px;" value="${cluster.host }">
                                                                <a href="/cluster/detail?hostIps=${cluster.host }" title="查看详细信息"
                                                                   onmousemove="style.textDecoration='underline'"
                                                                   onmouseout="style.textDecoration='none'">${cluster.host }</a>
                                                            </td>
                                                            <td style="width: 20%;text-indent: 8px;">${cluster.hostType}</td>
                                                            <td style="width: 20%;">连接正常</td>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </article>
</div>

</body>
</html>