<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/user.css"/>
    <script type="text/javascript" src="/js/user/user.js"></script>
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
                        <li class="active" id="nav2">租户</li>
                    </ol>
                </div>
                <div class="contentMain">


                    <aside class="aside-btn">
                        <div class="btns-group">
                            <span class="btn btn-defaults btn-white"><i class="icon-map-marker"></i><span class="ic_left">租户管理</span></span>
                        </div>
                    </aside>
                    <div class="caption clearfix">
                        <ul class="toolbox clearfix">
                            <li><a href="javascript:void(0);" id="userReloadBtn"><i class="fa fa-repeat"></i></a></li>
                            <li><a href="/user/add" id="userCreateBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;创建</a></li>
                            <li class="dropdown">
                                <a data-toggle="dropdown" href="javascript:void(0);">更多操作&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="javascript:createContainer()">
                                            <i class="fa fa-play"></i>
                                            <span class="ic_left">查看</span>
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
                                                    <input type="checkbox" class="chkAll"/>
                                                </th>
                                                <th style="width: 20%;padding-left: 5px;">登录账号</th>
                                                <th style="width: 18%;text-indent: 8px;">工号</th>
                                                <th style="width: 18%;">姓名</th>
                                                <th style="width: 18%;">部门</th>
                                                <th style="width: 18%;">角色权限</th>
                                            </tr>
                                            </thead>
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
            </div>
        </article>
    </div>


<%--
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>
<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">用户</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="setTab">
                    <a id="baseinfo" class="Record active">基本信息</a>
                    <a id="pwd" class="Record">修改密码</a>
                </div>

                <div class="account_table" userID="${user.id }">

                    <div id="baseinfo_wrap" class="tab_wrap">
                        <form class="form-horizontal user_info_form" id="editUser" name="editUser" action="/user/edit.do" method="post">
                            <div class="form-group">
                                <label for="username" class="col-md-offset-1 col-md-3">用户名：</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control" id="userName" name="userName" value="${user.userName }" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="col-md-offset-1 col-md-3">邮箱：</label>
                                <div class="col-md-5">
                                    <input type="email" class="form-control" id="email" name="email" value="${user.email }">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="company" class="col-md-offset-1 col-md-3">公司：</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control" id="company" name="company" value="${user.company }">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-2">
                                    <button type="button" class="btn btn-primary" id="basicInfo">保存</button>
                                </div>
                            </div>

                        </form>
                    </div>

                    <div id="pwd_wrap" class="tab_wrap hide">

                        <form class="edit_pwd_form form-horizontal" action="user/userModifyPsw.do" id="pswSave" name="pswSave" method="post">
                            <div class="form-group">
                                <label for="originalPwd" class="col-md-offset-1 col-md-3">原密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="originalPwd" name="originalPwd" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="newPwd" class="col-md-offset-1 col-md-3">新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="newPwd" name="newPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="confirmNewPwd" class="col-md-offset-1 col-md-3">确认新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-3">
                                    <button type="button" class="btn btn-primary" id="passwordInfo">提交修改</button>
                                </div>
                            </div>

                        </form>

                    </div>

                </div>


            </div>
        </div>
    </article>
</div>--%>

</body>
</html>