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
                    <li class="active" id="nav2">集群</li>
                </ol>
            </div>
            <div class="contentMain">


                <aside class="aside-btn">
                    <div class="btns-group">
                        <a id="clusterResource" class="Record action"><span class="btn btn-defaults btn-white"><span
                                class="ic_left">集群资源</span></span></a>
                    </div>
                </aside>

                <div>

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
                                        <td style="width:10%"><span id="detailCpu">-</span>/<span
                                                id="totalCpu">-（核）</span></td>
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
                                        <td style="width:10%"><span id="detailCpuMaster">-</span>/<span
                                                id="totalCpuMaster">-（核）</span></td>
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
                                        <td style="width:10%"><span id="detailCpuEtcd">-</span>/<span id="totalCpuEtcd">-（核）</span>
                                        </td>
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
                                        <td style="width:10%"><span id="detailCpuSlave">-</span>/<span
                                                id="totalCpuSlave">-（核）</span></td>
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
                                        <td style="width:10%"><span id="detailMemMaster">-</span>/<span
                                                id="totalMemMaster">-（G）</span></td>
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
                                        <td style="width:10%"><span id="detailMemEtcd">-</span>/<span id="totalMemEtcd">-（G）</span>
                                        </td>
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
                                        <td style="width:10%"><span id="detailMemSlave">-</span>/<span
                                                id="totalMemSlave">-（G）</span></td>
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