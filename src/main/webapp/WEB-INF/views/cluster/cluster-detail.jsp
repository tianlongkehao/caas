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

                <div class="setTab">
                    <a id="resourceinfo" class="Record action">172.16.71.146</a>
                    <a id="pwd" class="Record">172.16.71.233</a>
                </div>

                <div class="account_table">

                    <div id="resourceinfo_wrap" class="tab_wrap ">
                        <div class="detail-info">
                            <div class="info-list" style="margin-top: 0px">
                                <table class="table" id="table-listing">
                                    <thead>
                                    <tr>
                                        <th colspan="6" class="detail-rows">资源使用情况</th>
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
                                        <td style="width:10%"><span id="detailCpu">-</span>（核）</td>
                                        <%--<td>网络（M）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block network"></div>
                                            </div>
                                        </td>
                                        <td>10M</td>--%>
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
                                    <tr>
                                        <td>内存（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailMemory"></div>
                                            </div>
                                        </td>
                                        <td><span id="detailMemory">-</span>/<span id="totalMemory">-</span>（G）</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>


                    <div id="pwd_wrap" class="tab_wrap hide">
                        <div class="detail-info">
                            <div class="info-list" style="margin-top:0px;">
                                22222222222222222222222222222222222
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