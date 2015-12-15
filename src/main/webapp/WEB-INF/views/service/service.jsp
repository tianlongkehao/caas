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