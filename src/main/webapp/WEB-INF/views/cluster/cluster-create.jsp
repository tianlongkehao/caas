<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/cluster.css"/>
    <script type="text/javascript" src="/js/cluster/cluster-create.js"></script>
</head>
<body>

<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="user" value=""/>
</jsp:include>
<input type="hidden" id="checkedClusters">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2" style="text-align: center">集群</li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav3">创建集群</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="modalCrealApp">
                    <div class="steps-main">
                        <div class="progre">
                            <ul style="padding: 0 4rem;">
                                <li class="radius_step action"><span>1</span> 选择集群节点</li>
                                <li class="radius_step"><span>2</span> 提供SSH登陆凭证</li>
                                <li class="radius_step"><span>3</span> 配置集群节点</li>
                                <li class="radius_step"><span>4</span> 集群节点安装</li>
                            </ul>
                        </div>
                        <div class="step-inner" style="left: 0%;">
                            <%--选择集群节点--%>
                            <div class="host_step1">
                                <div class="blankapp" style="width: 90%; text-align: center">
                                    <div class="row" style="margin-left: 54px;">
                                        <div>
                                            <form class="search-group-inner"
                                                  style="width:60%;margin: 0 auto;position: relative;"
                                                  action="/cluster/getClusters" method="post">
                                                <%--<input type="text" name="ipRange" class="form-control" placeholder="请输入要查找的IP"
                                                       style="width: 78%;display: inline" value="${ipRange}">--%>
                                                <input type="text" name="ipRange" class="form-control"
                                                       placeholder="请输入要查找的IP"
                                                       style="width: 78%;display: inline" value="172.16.71.[171-173]">
                                                <button type="submit" class="btn btn-primary btn-send">查找</button>
                                            </form>
                                        </div>
                                        <div>
                                            <table class="table table-hover enabled" style="margin-top: 25px">
                                                <thead>
                                                <tr style="text-align: center">
                                                    <th style="width: 5%;text-indent: 30px;">
                                                        <input type="checkbox" class="chkAll"/>
                                                    </th>
                                                    <th style="width: 33%;text-align: center">IP</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${lstClusters}" var="Cluster">
                                                    <tr>
                                                        <td style="text-align: center">
                                                            <input type="checkbox" name="checked"/>
                                                            <input type="hidden" name="aaa" value="${Cluster.host}">
                                                        </td>
                                                        <td style="text-align: center">${Cluster.host}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="list-item-description" style="padding-top: 100px">
                                    <a href="/cluster/list"><span class="btn btn-default go_user"
                                                                  style="margin-right: 30px;">返回</span></a>
                                        <span class="nextTwo pull-right btn btn-primary pull_confirm"
                                              data-attr="tenxcloud/mysql">下一步</span>
                                </div>
                            </div>

                            <%--2.提供SSH登陆凭证--%>
                            <div class="host_step2">

                                <ul class="safeSet" style="margin-left: 100px">
                                    <li style="margin-bottom: 30px">
                                        <span>此安装需要对主机的根访问权限，此安装程序将通过SSH连接到您的主机，然后直接以root用户身份登录（登录所有主机，主机接收相同密码）。</span>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">root：</span>
                                            <input type="text" class="form-control" placeholder="root"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">输入密码：</span>
                                            <input type="text" class="form-control"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">确认密码：</span>
                                            <input type="text" class="form-control"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                    <li class="line-h-3">
                                        <div class="param-set">
                                            <span class="number-title">SSH端口：</span>
                                            <input type="text" class="form-control" placeholder="22"
                                                   style="width: 78%;display: inline">
                                        </div>
                                    </li>
                                </ul>
                                <div class="" style="padding-top: 100px">
                                    <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                        <span id="checkBtn"
                                              class="checkBtn pull-right btn btn-primary pull_confirm">下一步</span>
                                </div>
                            </div>

                            <%--3.配置集群节点--%>
                            <div class="host_step3" style="float:left">
                                <div>
                                    <input type="hidden" id="bbb">
                                    <table class="table table-hover enabled">
                                        <thead>
                                        <tr style="text-align: center">
                                            <th style="width: 20%;text-align: center">IP</th>
                                            <th style="width: 33%;text-align: center">Master</th>
                                            <th style="width: 33%;text-align: center">slave</th>
                                            <th style="width: 33%;text-align: center">etcd</th>
                                        </tr>
                                        </thead>
                                        <tbody id='divId'>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="" style="padding-top: 100px">
                                    <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                        <span id="installBtn"
                                              class="installBtn pull-right btn btn-primary pull_confirm">安装</span>
                                </div>
                            </div>
                            <%--4.配置集群节点--%>
                            <div class="host_step4" style="height: auto;float: right">
                                <div>
                                    <table class="table table-hover enabled">
                                        <thead>
                                        <tr style="text-align: center">
                                            <th style="width: 20%;text-align: center">IP</th>
                                            <th style="width: 33%;text-align: center">安装进度</th>
                                            <th style="width: 33%;text-align: center">状态</th>
                                            <th style="width: 33%;text-align: center">详细信息</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <th style="text-align: center">172.16.71.111</th>
                                            <td style="text-align: center"></td>
                                            <td style="text-align: center"></td>
                                            <td style="text-align: center"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="" style="padding-top: 100px">
                                    <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                    <a href="/cluster/list"><span id="finishBtn"
                                                                  class="pull-right btn btn-primary pull_confirm">完成</span></a>
                                </div>
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
