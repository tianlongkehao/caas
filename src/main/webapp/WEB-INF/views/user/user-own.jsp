<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/user.css"/>
    <link rel="icon" type="image/x-icon" href="<%=path %>/images/favicon.ico">
    <script type="text/javascript" src="<%=path %>/js/user/user-own.js"></script>
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
                    <li class="active">用户信息</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="setTab">
                    <a id="baseinfo" class="Record active">基本信息</a>
                    <a id="resourceinfo" class="Record">资源信息</a>
                    <a id="pwd" class="Record hide">修改密码</a>
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
                                    <label style="line-height: 34px;margin-bottom: 0px">权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限:</label>
                                    <input type="hidden" id="user_autority_hidden" value="${user.user_autority}">
                                    <select class="form-control" style="width: 78%;display: inline; float: right;margin-right: 17px"
                                            name="user_autority" id="user_autority" disabled>
                                        <option name="option" value="1" >管理员</option>
                                        <option name="option" value="2" >租户</option>
                                        <option name="option" value="3" >普通用户</option>
                                    </select>
                                </div>
                                <%--<div class="col-md-4">
                                    <label>权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限:</label>
                                    <c:if test="${user.user_autority=='1' }">
	                                    <input type="text" class="form-control" id="user_autority" name="user_autority"
	                                           value="管理员" style="width: 78%;display: inline" readonly="readonly">
                                    </c:if>
                                    <c:if test="${user.user_autority=='2' }">
	                                    <input type="text" class="form-control" id="user_autority" name="user_autority"
	                                           value="普通用户 " style="width: 78%;display: inline" readonly="readonly">
                                    </c:if>
                                </div>--%>
                            </div>
                            <div class="row" style="margin-top: 15px">
                                <div class="col-md-4">
                                    <label style="line-height: 34px;margin-bottom: 0px">省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:</label>
                                    <input type="hidden" id="user_province_hidden" value="${user.user_province}">
                                    <select class="form-control" style="width: 78%;display: inline; float: right;margin-right: 17px"
                                            name="province" id="user_province" disabled>
                                        <option name="user_province" value="1">北京</option>
                                        <option name="user_province" value="2">天津</option>
                                        <option name="user_province" value="3">上海</option>
                                        <option name="user_province" value="4">河北</option>
                                        <option name="user_province" value="5">河南</option>
                                        <option name="user_province" value="6">山西</option>
                                        <option name="user_province" value="7">内蒙古</option>
                                        <option name="user_province" value="8">辽宁</option>
                                        <option name="user_province" value="9">吉林</option>
                                        <option name="user_province" value="10">黑龙江</option>
                                        <option name="user_province" value="11">江苏</option>
                                        <option name="user_province" value="12">浙江</option>
                                        <option name="user_province" value="13">安徽</option>
                                        <option name="user_province" value="14">福建</option>
                                        <option name="user_province" value="15">江西</option>
                                        <option name="user_province" value="16">山东</option>
                                        <option name="user_province" value="17">湖南</option>
                                        <option name="user_province" value="18">湖北</option>
                                        <option name="user_province" value="19">广东</option>
                                        <option name="user_province" value="20">广西</option>
                                        <option name="user_province" value="21">海南</option>
                                        <option name="user_province" value="22">重庆</option>
                                        <option name="user_province" value="23">四川</option>
                                        <option name="user_province" value="24">贵州</option>
                                        <option name="user_province" value="25">云南</option>
                                        <option name="user_province" value="26">西藏</option>
                                        <option name="user_province" value="27">陕西</option>
                                        <option name="user_province" value="28">甘肃</option>
                                        <option name="user_province" value="29">青海</option>
                                        <option name="user_province" value="30">宁夏</option>
                                        <option name="user_province" value="31">新疆</option>
                                    </select>
                                </div>
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
                            </div>
                            <div class="row" style="margin-top: 15px">
                                <div class="col-md-4">
                                    <label>工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号 :</label>
                                    <input type="text" class="form-control" id="user_employee_id"
                                           name="user_employee_id" value="${user.user_employee_id }"
                                           style="width: 78%;display: inline" >
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
                            </div>
                            <c:if test="${!cas_enable}">
                            <div style="margin-top: 25px">
                                <span class="pull-right btn btn-primary" id="basicInfo">保存</span>
                            </div>
                            </c:if>
                        </form>
                    </div>

                    <div id="resourceinfo_wrap" class="tab_wrap hide">
                        <section class="container-count">
                        <div class="padding">
                            <div class="row-title">服务详情</div>
                            <ul class="server-list">
                                <li>
                                    <a href="<%=path %>/containers?0" data-permalink onclick="_permalink(this)">
                                      <span class="server-info-icon"><i class="fa_icon server_icon_1"></i>
                                        <span>服务个数：</span>
                                      </span>
                                      <span class="pull-right big green"><span>${usedServiceNum}</span>/<span>未限制${servServiceNum}</span>&nbsp;个</span>
                                      	<%--<c:choose>
                                      		<c:when test="${servServiceNum==''}">
	                                      		<span class="pull-right big green"><span id="clusterNum"></span>&nbsp;个</span>
                                      		</c:when>
                                      		<c:otherwise>
		                                        <span class="pull-right big green"><span id="clusterNum1">${servServiceNum }</span>&nbsp;个</span>
                                      		</c:otherwise>
                                      	</c:choose>--%>
                                    </a>
                                </li>
                                <li>
                                    <a href="<%=path %>/ci?0" data-permalink onclick="permalink(this)">
                                          <span class="server-info-icon"><i class="fa_icon server_icon_2"></i>
                                            <span>实例个数：</span>
                                          </span>
                                        <span class="pull-right big blue"><span>${usedPodNum}</span>/<span>未限制${servPodNum}</span>&nbsp;个</span>
                                    </a>
                                </li>
                                <%--<li>
                                    <a href="<%=path %>/docker-registry?0" data-permalink onclick="_permalink(this)">
                                          <span class="server-info-icon"><i class="fa_icon server_icon_3"></i>
                                            <span>副本控制器：</span>
                                          </span>
                                        <span class="pull-right big yellow"><span>${usedControllerNum}</span>/<span>未限制${servControllerNum}</span>&nbsp;个</span>
                                    </a>
                                </li>--%>
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
                                        <td style="width:15%">CPU（个）</td>
                                        <td style="width:25%">
                                            <div class="slider_bj">
                                                <div class="slider_block detailCpu" id="usedCpu"></div>
                                            </div>
                                        </td>
                                        <td style="width:10%">
                                            <span id="detailCpu">${usedCpuNum}</span>/<span id="totalCpu">${servCpuNum}</span>（个）
                                        	<%--<c:choose>
                                        		<c:when test="${usedCpuNum==''}">
			                                        <span id="detailCpu">&nbsp;</span>/<span id="totalCpu">&nbsp;</span>（个）
                                        		</c:when>
                                        		<c:otherwise>
			                                        <span id="detailCpu">${usedCpuNum}</span>/<span id="totalCpu">${servCpuNum}</span>（个）
                                        		</c:otherwise>
                                        	</c:choose>--%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>内存（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailMemory" id="usedMemory"></div>
                                            </div>
                                        </td>
                                        <td>
                                            <span id="detailMemory">${usedMemoryNum}</span>/<span id="totalMemory">${servMemoryNum}</span>（G）
                                        	<%--<c:choose>
                                        		<c:when test="${usedMemoryNum==''}">
			                                        <span id="detailMemory">&nbsp;</span>/<span id="totalMemory">&nbsp;</span>（G）
                                        		</c:when>
                                        		<c:otherwise>
			                                        <span id="detailMemory">${usedMemoryNum}</span>/<span id="totalMemory">${servMemoryNum}</span>（G）
                                        		</c:otherwise>
                                        	</c:choose>--%>
                                        </td>
                                    </tr>
                                    <%--<tr>
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
                                    </tr>--%>
                                    </tbody>
                                </table>
                            </div>
                        </div>

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