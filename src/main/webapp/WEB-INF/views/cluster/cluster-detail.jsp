<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/cluster.css"/>
    <link rel="icon" type="image/x-icon" href="/images/favicon.ico">
    <script type="text/javascript" src="/js/cluster/cluster-detail.js"></script>

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
                    <li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">集群</li>
                </ol>
            </div>
            <div class="contentMain">

                <%--<div class="setTab" id='resourceTab'>
                    <c:forEach items="${lstClusters}" var="cluster">
                        <a name="hosts" class="Record action active" value="${cluster.host}">${cluster.host}</a>
                    </c:forEach>
                </div>--%>

                <div class="account_table">
                    <c:forEach items="${lstClustersUse}" var="clusterUse">
                        <div class="tab_wrap" name="clusterHost">
                            <div class="detail-info">
                                <div class="info-list" style="margin-top: 0px">
                                    <table class="table" id="table-listing">
                                        <thead>
                                        <tr>
                                            <th colspan="6" class="detail-rows">${clusterUse.host}</th>
                                        </tr>
                                        </thead>
                                        <tbody id='divResultInfo'>
                                        <tr>
                                            <td style="width:15%">CPU（Usage）</td>
                                            <td style="width:25%">
                                                <div class="slider_bj">
                                                    <div class="slider_block detailCpu cpuPercents" style="width:0%"></div>
                                                </div>
                                            </td>
                                            <td style="width:10%"><span id="detailCpu" class="detailCpus">${clusterUse.cpuUse}</span>/<span
                                                    id="totalCpu" class="totalCpus">${clusterUse.cpuLimit}</span>（ms）
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
                                        <tr>
                                            <td>内存（Usage）</td>
                                            <td>
                                                <div class="slider_bj">
                                                    <div class="slider_block detailMemory memPercents" style="width: 0%"></div>
                                                </div>
                                            </td>
                                            <td><span id="detailMemoryUse" class="detailMems">${clusterUse.memUse}</span>/<span
                                                    id="totalMemoryUse" class="totalMems">${clusterUse.memLimit}</span>（M）
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>内存（Working Set）</td>
                                            <td>
                                                <div class="slider_bj">
                                                    <div class="slider_block detailMemory workDetailMems" style="width: 0%"></div>
                                                </div>
                                            </td>
                                            <td><span id="detailMemorySet" class="detailMemsSet">${clusterUse.memSet}</span>/<span
                                                    id="totalMemorySet" class="totalMemsSet">${clusterUse.memLimit}</span>（M）
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>

            </div>
        </div>
    </article>
</div>

</body>

<script type="text/javascript">
    debugger
    /*var str = '<a name="hosts" class="Record action active">  </a>';*/
    var aaa = document.getElementsByName('hosts').length;
    for (var i = 0; i < aaa; i++) {
        var bbb = document.getElementsByName('hosts')[i].getAttribute("value");
        document.getElementsByName('hosts')[i].id = bbb.substring(10, 13);
        document.getElementsByName('clusterHost')[i].id = bbb.substring(10, 13) + '_wrap';
    }
</script>
</html>