<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/cluster.css"/>
    <script type="text/javascript" src="/js/cluster/cluster.js"></script>
</head>
<body>
    <jsp:include page="../frame/menu.jsp" flush="true">
        <jsp:param name="user" value=""/>
    </jsp:include>

    <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentTitle">
                    <ol class="breadcrumb">
                        <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                                id="nav1">控制台</span></a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active" id="nav2">集群</li>
                    </ol>
                </div>
                <div class="contentMain">


                    <aside class="aside-btn">
                        <div class="btns-group">
                            <a id="clusterResource" class="Record"><span class="btn btn-defaults btn-white"><span class="ic_left">集群资源</span></span></a>
                            <a id="clusterManage" class="Record action"><span class="btn btn-defaults btn-white"><span class="ic_left">集群管理</span></span></a>
                        </div>
                    </aside>

                <div>
                    <div id="clusterManage_wrap" class="tab_wrap hide">
                    <div class="caption clearfix">
                        <ul class="toolbox clearfix">
                            <li><a href="javascript:void(0);" id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
                            <li><a href="/cluster/add" id="userCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                            <li class="dropdown">
                                <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="/cluster/detail">
                                            <i class="fa fa-play"></i>
                                            <span class="ic_left" id="detailInfo">查看</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="javascript:changeContainerConf();">
                                            <i class="fa fa-cog"></i>
                                            <span class="ic_left">修改</span>
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
                                        <input value="1" id="numberChange" class="" min="1" style="margin-top: 10px;width: 165px;" type="number">
                                        <span class="unit">个</span>
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
                                        <input type="number" value="1" style="margin-top: 10px;width: 165px;" min="1" autocomplete="off"
                                               placeholder="1" id="" name="">
                                        <span class="unit">个</span>
                                    </div>
                                </li>
                                <li class="line-h-3">
                                    <div class="param-set">
                                        <span>内存：</span>
                                        <input id="confRamSlider" data-slider-id='confRamSlider' type="text" data-slider-min="0" data-slider-max="2024" data-slider-step="1" data-slider-value="256"/>
                                        <input type="text" value="256" id="confRam" name="confRam">
                                        <span>M</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="searchs">
                            <form class="form-inline" action="">
                                <div class="form-group">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="搜索">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary" type="button">
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
                                                    <input type="checkbox" class="chkAll" />
                                                </th>
                                                <th style="width: 20%;padding-left: 5px;">IP</th>
                                                <th style="width: 20%;text-indent: 8px;">CPU</th>
                                                <th style="width: 20%;">内存</th>
                                                <th style="width: 20%;">网络</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <th style="width: 5%;text-indent: 30px;">
                                                    <input type="checkbox" class="chkAll"/>
                                                </th>
                                                <th style="width: 20%;padding-left: 5px;">172.16.71.146</th>
                                                <th style="width: 20%;text-indent: 8px;">111</th>
                                                <th style="width: 20%;">222</th>
                                                <th style="width: 20%;">333</th>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </th>
                            </tr>
                            </thead>
                            <tbody id = "serviceBody">
                            <tr>

                            </tr>
                            </tbody>
                        </table>
                    </div>
                    </div>

                    <div id="clusterResource_wrap" class="tab_wrap">
                    <div class="detail-info">
                        <div class="info-list">
                            <table class="table" id="table-listing">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">集群资源使用情况</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">CPU（核）</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailCpu">-</span>/<span id="totalCpu">-（核）</span></td>
                                </tr>
                                <tr>
                                    <td>内存（G）</td>
                                    <td>
                                        <div class="slider_bj">
                                            <div class="slider_block detailMemory"></div>
                                        </div>
                                    </td>
                                    <td><span id="detailMemory">-</span>/<span id="totalMemory">-</span>（G）</td>
                                </tr>
                                <tr>
                                    <td style="width:15%">网络（M）</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailNetwork"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailNetwork">-</span>10M</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <%--cpu--%>
                    <div class="detail-info" style="float:left;width: 50%">
                        <div class="info-list" style="margin-top: 0px;;width: 93%;">
                            <div class="row-title">CPU资源占用情况</div>
                            <table class="table" id="masterCpuResourceInfo" style="width: 90%;margin-top: 10px;">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">Master</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">127.16.15.12</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailCpuMaster">-</span>/<span id="totalCpuMaster">-（核）</span></td>
                                </tr>

                                </tbody>
                            </table>

                            <table class="table" id="etcdCpuResourceInfo" style="width: 90%;margin-top: 10px;">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">Etcd</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">127.16.15.16</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailCpuEtcd">-</span>/<span id="totalCpuEtcd">-（核）</span></td>
                                </tr>

                                </tbody>
                            </table>

                            <table class="table" id="slaveCpuResourceInfo" style="width: 90%;margin-top: 10px;">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">Slave</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">127.16.15.16</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailCpuSlave">-</span>/<span id="totalCpuSlave">-（核）</span></td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                    </div>

                    <%--内存资源--%>
                    <div class="detail-info" style="float: right;width: 50%">
                        <div class="info-list" style="margin-top: 0px;;width: 100%">
                            <div class="row-title">内存资源占用情况</div>
                            <table class="table" id="masterMemResourceInfo" style="width: 90%;margin-top: 10px;">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">Master</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">127.16.15.12</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailMemMaster">-</span>/<span id="totalMemMaster">-（G）</span></td>
                                </tr>

                                </tbody>
                            </table>

                            <table class="table" id="etcdMemResourceInfo" style="width: 90%;margin-top: 10px;">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">Etcd</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">127.16.15.16</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailMemEtcd">-</span>/<span id="totalMemEtcd">-（G）</span></td>
                                </tr>

                                </tbody>
                            </table>

                            <table class="table" id="slaveMemResourceInfo" style="width: 90%;margin-top: 10px;">
                                <thead>
                                <tr>
                                    <th colspan="6" class="detail-rows">Slave</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="width:15%">127.16.15.16</td>
                                    <td style="width:25%">
                                        <div class="slider_bj">
                                            <div class="slider_block detailCpu"></div>
                                        </div>
                                    </td>
                                    <td style="width:10%"><span id="detailMemSlave">-</span>/<span id="totalMemSlave">-（G）</span></td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                    </div>
                    </div>
                </div>


                </div>
            </div>
        </article>
    </div>

</body>
</html>