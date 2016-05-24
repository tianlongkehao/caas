<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
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
                    <li class="active" id="nav2">集群资源</li>
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
                                        <th colspan="6" class="detail-rows">集群资源总览</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td style="width:10%">CPU</td>
                                        <td style="width:30%">
                                            <div class="slider_bj">
                                                <div class="slider_block detailCpu" id="clusterCpu" style="width: 0%"></div>
                                            </div>
                                        </td>
                                        <td style="width:10%"><span id="detailCpu">${allClusterCpuUse}</span>/<span
                                                id="totalCpu">${allClusterCpuLimit}</span>（ms）</td>
                                    </tr>
                                    <tr>
                                        <td>内存</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailMemory" id="clusterMem" style="width: 0%"></div>
                                            </div>
                                        </td>
                                        <td><span id="detailMemory">${allClusterMemUse}</span>/<span
                                                id="totalMemory">${allClusterMemLimit}</span>（M）
                                        </td>
                                    </tr>
                                    <%--<tr>
                                        <td style="width:15%">网络（M）</td>
                                        <td style="width:25%">
                                            <div class="slider_bj">
                                                <div class="slider_block detailNetwork"></div>
                                            </div>
                                        </td>
                                        <td style="width:10%"><span id="detailNetwork">-</span>10M</td>
                                    </tr>--%>
                                    </tbody>
                                </table>
                                <table class="table" id="cluster-listing" style="margin-top: 20px">
                                    <thead>
                                    <tr>
                                        <th class="detail-rows" style="width:13%">集群主机资源一览</th>
                                        <th  class="detail-rows" style="text-align: center;width:33%">CPU</th>
                                        <th class="detail-rows" style="text-align: center;width:33%">内存</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${lstClusters}" var="clusterUse">
                                        <%--<c:if test="${clusterUse.host == null || clusterUse.host == '0'}">
                                            <c:set var="cursorClass" value="cursor-no-drop"></c:set>
                                        </c:if>--%>
                                        <c:if test="${cur_cluster.host != clusterUse.host}">
                                            <tr name="clusterUseInfo">
                                                <td>${clusterUse.host}</td>
                                                <td style="border-right: 1px solid #ddd;border-left: 1px solid #ddd;">
                                                    <div class="slider_bj" style="width:50%; float: left;margin-left: 40px">
                                                        <div class="slider_block detailCpu clusterCpuUse" style="width: 0%"></div>
                                                    </div>
                                                    <div><span id="clusterDetailCpu" class="clusterDetCpuUse">${clusterUse.cpuUse}</span>/<span
                                                            id="clusterTotalCup" class="clusterTotCpuLimit">${clusterUse.cpuLimit}</span>（ms）</div>
                                                </td>
                                                <td>
                                                    <div class="slider_bj" style="width:50%; float: left;margin-left: 40px">
                                                        <div class="slider_block detailMemory clusterMemUse" style="width: 0%"></div>

                                                    </div>
                                                    <div><span id="clusterDetailMemory" class="clusterDetMemUse">${clusterUse.memUse}</span>/<span
                                                            id="clustertotalMemory" class="clusterTotMemLimit">${clusterUse.memLimit}</span>（M）</div>
                                                </td>
                                            </tr>
                                        </c:if>

                                    </c:forEach>
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