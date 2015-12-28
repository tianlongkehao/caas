<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/user.css"/>
    <link rel="icon" type="image/x-icon" href="/images/favicon.ico">
    <script type="text/javascript" src="/js/user/user-own.js"></script>
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
                    <li class="active">用户</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="setTab">
                    <a id="baseinfo" class="Record active">基本信息</a>
                    <a id="resourceinfo" class="Record">资源信息</a>
                    <a id="pwd" class="Record">修改密码</a>
                </div>

                <div class="account_table" userID="${user.id }">

                    <div id="baseinfo_wrap" class="tab_wrap">
                        <form class="form-horizontal user_info_form" id="editUser" name="editUser"
                              action="/user/edit.do" method="post">
                            <div class="row">
                                <div class="col-md-4">
                                    <label>登录账号:</label>
                                    <input type="text" class="form-control" id="userName" name="userName"
                                           value="${user.userName }" disabled style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
                                    <input type="text" class="form-control" id="user_realname" name="user_realname"
                                           value="${user.user_realname }" style="width: 78%;display: inline" disabled>
                                </div>
                                <div class="col-md-4">
                                    <label>工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
                                    <input type="text" class="form-control" id="user_employee_id"
                                           name="user_employee_id" value="${user.user_employee_id }"
                                           style="width: 78%;display: inline" disabled>
                                </div>
                            </div>
                            <div class="row" style="margin-top: 15px">
                                <div class="col-md-4">
                                    <label>所属部门:</label>
                                    <input type="text" class="form-control" id="user_department" name="user_department"
                                           value="${user.user_department }" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>手机号码:</label>
                                    <input type="text" class="form-control" id="user_cellphone" name="user_cellphone"
                                           value="${user.user_cellphone }" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>固定电话:</label>
                                    <input type="text" class="form-control" id="user_phone" name="user_phone"
                                           value="${user.user_phone }" style="width: 78%;display: inline">
                                </div>
                            </div>
                            <div class="row" style="margin-top: 15px">
                                <div class="col-md-4">
                                    <label>邮箱地址:</label>
                                    <input type="text" class="form-control" id="email" name="email"
                                           value="${user.email}" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
                                    <input type="text" class="form-control" id="company" name="company"
                                           value="${user.company}" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>权限选择:</label>
                                    <select class="form-control" id="user_autority" name="user_autority"
                                            value="${user.user_autority}" style="width: 78%;display: inline">
                                        <option value=1>管理员</option>
                                        <option value=2>用户</option>
                                    </select>
                                </div>
                            </div>
                            <div style="margin-top: 25px">
                                <button type="button" class="pull-right btn btn-primary" id="basicInfo">保存</button>
                            </div>

                            <%-- <div class="form-group">
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
                             </div>--%>

                        </form>
                    </div>

                    <div id="resourceinfo_wrap" class="tab_wrap hide">
                        <section class="container-count">
                        <div class="padding">
                            <div class="row-title">服务详情</div>
                            <ul class="server-list">
                                <li>
                                    <a href="/containers?0" data-permalink onclick="_permalink(this)">
                                      <span class="server-info-icon"><i class="fa_icon server_icon_1"></i>
                                        <span>服务个数：</span>
                                      </span>
                                        <span class="pull-right big green"><span id="clusterNum">-</span>&nbsp;个</span></a>
                                </li>
                                <li>
                                    <a href="/ci?0" data-permalink onclick="_permalink(this)">
                                          <span class="server-info-icon"><i class="fa_icon server_icon_2"></i>
                                            <span>项目个数：</span>
                                          </span>
                                        <span class="pull-right big blue"><span id="ciNum">-</span>&nbsp;个</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="/docker-registry?0" data-permalink onclick="_permalink(this)">
                                          <span class="server-info-icon"><i class="fa_icon server_icon_3"></i>
                                            <span>镜像个数：</span>
                                          </span>
                                        <span class="pull-right big yellow"><span id="imageNum">-</span>&nbsp;个</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="/hosting?0" data-permalink onclick="_permalink(this)">
                                      <span class="server-info-icon"><i class="fa_icon server_icon_hosting"></i>
                                        <span>主机个数：</span>
                                      </span>
                                        <span class="pull-right big purple"><span id="hostingNum">-</span>&nbsp;个</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        </section>

                        <div class="detail-info">
                            <div class="info-list">
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
                                        <td>网络（M）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block network"></div>
                                            </div>
                                        </td>
                                        <td>10M</td>
                                    </tr>
                                    <tr>
                                        <td>内存（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailMemory"></div>
                                            </div>
                                        </td>
                                        <td><span id="detailMemory">-</span>/<span id="totalMemory">-</span>（G）</td>
                                        <td>存储（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailVolume"></div>
                                            </div>
                                        </td>
                                        <td>
                                            <span id="detailVolume">-</span>/<span id="totalVolume">-</span>（G）
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>集群（个）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailHostingClusterNum"></div>
                                            </div>
                                        </td>
                                        <td><span id="detailHostingClusterNum">-</span>（个）</td>
                                        <td style="width:15%">主机（个）</td>
                                        <td style="width:25%">
                                            <div class="slider_bj">
                                                <div class="slider_block detailHostingNum"></div>
                                            </div>
                                        </td>
                                        <td style="width:10%"><span id="detailHostingNum">-</span>/5（个）</td>
                                    </tr>
                                    <%--<tr>
                                        <td>Stack（个）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailStackNum"></div>
                                            </div>
                                        </td>
                                        <td><span id="detailStackNum">-</span>（个）</td>
                                        <td colspan="3"></td>
                                    </tr>--%>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <%--<div>
                            <canvas id="pieChart" class="pieChart" width="200" height="200"></canvas>
                            <div style="margin-top: 15px">
                                <canvas id="greenPie" width="15" height="15"></canvas>运行中
                                <canvas id="redPie" width="15" height="15"></canvas>停止
                                <canvas id="grayPie" width="15" height="15"></canvas>其他
                            </div>
                        </div>--%>

                    </div>


                    <div id="pwd_wrap" class="tab_wrap hide">

                        <form class="edit_pwd_form form-horizontal" action="user/userModifyPsw.do" id="pswSave"
                              name="pswSave" method="post">
                            <div class="form-group">
                                <label for="originalPwd" class="col-md-offset-2 col-md-2">原密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="originalPwd" name="originalPwd"
                                           value="${user.originalPwd}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="newPwd" class="col-md-offset-2 col-md-2">新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="newPwd" name="newPwd"
                                           value="${user.newPwd}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="confirmNewPwd" class="col-md-offset-2 col-md-2">确认新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd"
                                           value="${user.confirmNewPwd}">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-6 col-md-3">
                                    <button type="button" class="pull-right btn btn-primary" id="passwordInfo">提交修改
                                    </button>
                                </div>
                            </div>

                        </form>

                    </div>

                </div>


            </div>
        </div>
    </article>
</div>

</body>
</html>