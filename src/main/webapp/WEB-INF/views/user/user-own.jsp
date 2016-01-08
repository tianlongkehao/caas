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
                              action="" method="post">
                            <div class="row">
                                <div class="col-md-4">
                                    <label>登录账号:</label>
                                    <input type="text" class="form-control" id="userName" name="userName"
                                           value="${user.userName }"  style="width: 78%;display: inline" readonly="readonly">
                                    <input type="hidden" name="id" id="user_id" value="${user.id }">
                                </div>
                                <div class="col-md-4">
                                    <label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
                                    <input type="text" class="form-control" id="user_realname" name="user_realname"
                                           value="${user.user_realname }" style="width: 78%;display: inline" readonly="readonly">
                                </div>
                                <div class="col-md-4">
                                    <label>权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限:</label>
                                    <c:if test="${user.user_autority=='1' }">
	                                    <input type="text" class="form-control" id="user_autority" name="user_autority"
	                                           value="管理员" style="width: 78%;display: inline" readonly="readonly">
                                    </c:if>
                                    <c:if test="${user.user_autority=='2' }">
	                                    <input type="text" class="form-control" id="user_autority" name="user_autority"
	                                           value="普通用户 " style="width: 78%;display: inline" readonly="readonly">
                                    </c:if>
                                </div>
                            </div>
                            <div class="row" style="margin-top: 15px">
                                <div class="col-md-4">
                                    <label>公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
                                    <input type="text" class="form-control" id="company" name="company"
                                           value="${user.company}" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>所属部门:</label>
                                    <input type="text" class="form-control" id="user_department" name="user_department"
                                           value="${user.user_department }" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label>工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
                                    <input type="text" class="form-control" id="user_employee_id"
                                           name="user_employee_id" value="${user.user_employee_id }"
                                           style="width: 78%;display: inline" >
                                </div>
                            </div>
                            <div class="row" style="margin-top: 15px">
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
                                <div class="col-md-4">
                                    <label>邮箱地址:</label>
                                    <input type="text" class="form-control" id="email" name="email"
                                           value="${user.email}" style="width: 78%;display: inline">
                                </div>
                            </div>
                            <div style="margin-top: 25px">
                                <span class="pull-right btn btn-primary" id="basicInfo">保存</span>
                            </div>
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
                                      	<c:choose>
                                      		<c:when test="${servServiceNum==''}">
	                                      		<span class="pull-right big green"><span id="clusterNum">-</span>&nbsp;个</span>
                                      		</c:when>
                                      		<c:otherwise>
		                                        <span class="pull-right big green"><span id="clusterNum">${servServiceNum }</span>&nbsp;个</span>
                                      		</c:otherwise>
                                      	</c:choose>
                                      	</a>
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
                                        <td style="width:10%">
                                        	<c:choose>
                                        		<c:when test="${usedCpuNum==''}">
			                                        <span id="detailCpu">-</span>（核）
                                        		</c:when>
                                        		<c:otherwise>
			                                        <span id="detailCpu">${usedCpuNum }</span>（核）
                                        		</c:otherwise>
                                        	</c:choose>
                                        </td>
                                        <td>网络（M）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block network"></div>
                                            </div>
                                        </td>
                                        <td>10M</td>
                                    </tr>
                                    <tr>
                                        <td>内存（M）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailMemory"></div>
                                            </div>
                                        </td>
                                        <td>
                                        	<c:choose>
                                        		<c:when test="${usedMemoryNum==''}">
			                                        <span id="detailMemory">-</span>/<span id="totalMemory">-</span>（M）
                                        		</c:when>
                                        		<c:otherwise>
			                                        <span id="detailMemory">${usedMemoryNum}</span>/<span id="totalMemory">-</span>（M）
                                        		</c:otherwise>
                                        	</c:choose>
                                        </td>
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
                                        <td><span id="detailHostingClusterNum">3</span>（个）</td>
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

                        <form class="edit_pwd_form form-horizontal" action="" id="pswSave"
                              name="pswSave" method="post">
                            <div class="form-group">
                                <label for="originalPwd" class="col-md-offset-2 col-md-2">原密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="originalPwd" name="originalPwd"
                                           value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="newPwd" class="col-md-offset-2 col-md-2">新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="newPwd" name="newPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="confirmNewPwd" class="col-md-offset-2 col-md-2">确认新密码：</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-6 col-md-3">
<!--                                     <button type="button" class="pull-right btn btn-primary" id="modifyPwd">提交修改</button> -->
                                    <span class="pull-right btn btn-primary" id="modifyPwd">提交修改</span>
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