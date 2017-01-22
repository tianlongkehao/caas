<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>用户</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/user.css"/>
    <script type="text/javascript" src="<%=path %>/js/user/user-own.js"></script>
</head>
<body>

<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="user" value=""/>
</jsp:include>
<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i><span id="nav1">&nbsp;&nbsp;控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">用户信息</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="setTab">
                    <a id="baseinfo" class="Record active">基本信息</a>
                    <!-- <a id="resourceinfo" class="Record">资源信息</a> -->
                    <a id="preferenceinfo" class="Record">偏好设置</a>
                    <a id="pwd" class="Record hide">修改密码</a>
                </div>

                <div class="account_table" userID="${user.id }">

                    <div id="baseinfo_wrap" class="tab_wrap">
                        <form class="form-horizontal user_info_form" id="editUser" name="editUser"
                              action="" method="post">
                            <div class="row" style="margin-top: 10px">
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="登录账号">登录账号:</label>
                                    <input type="text" class="form-control userOwnCon" id="userName" name="userName"
                                           value="${user.userName }"  style="width: 78%;display: inline" readonly="readonly">
                                    <input type="hidden" name="id" id="user_id" value="${user.id }">
                                </div>
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="姓名">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
                                    <input type="text" class="form-control userOwnCon" id="user_realname" name="user_realname"
                                           value="${user.user_realname }" style="width: 78%;display: inline" readonly="readonly">
                                </div>

                                <div class="col-md-4">
                                    <label class="userOwnTitle" style="line-height: 34px;margin-bottom: 0px" title="权限">权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限:</label>
                                    <input type="hidden" id="user_autority_hidden" value="${user.user_autority}">
                                    <select class="form-control" style="width:78%; display: inline;"
                                            name="user_autority" id="user_autority" disabled>
                                        <option name="option" value="1" >管理员</option>
                                        <option name="option" value="2" >租户</option>
                                        <option name="option" value="3" >普通用户</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row" style="margin-top: 20px">
                                <div class="col-md-4">
                                    <label class="userOwnTitle" style="line-height: 34px;margin-bottom: 0px" title="省份">省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:</label>
                                    <input type="hidden" id="user_province_hidden" value="${user.user_province}">
                                    <select class="form-control" style="width:78%; display: inline;"
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
                                    <label class="userOwnTitle" title="公司">公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
                                    <input type="text" class="form-control userOwnCon" id="company" name="company"
                                           value="${user.company}" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="所属部门">所属部门:</label>
                                    <input type="text" class="form-control userOwnCon" id="user_department" name="user_department"
                                           value="${user.user_department }" style="width: 78%;display: inline">
                                </div>
                            </div>
                            <div class="row" style="margin-top: 20px">
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="工号">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号 :</label>
                                    <input type="text" class="form-control userOwnCon" id="user_employee_id"
                                           name="user_employee_id" value="${user.user_employee_id }"
                                           style="width: 78%;display: inline" >
                                </div>
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="手机号码">手机号码:</label>
                                    <input type="text" class="form-control userOwnCon" id="user_cellphone" name="user_cellphone"
                                           value="${user.user_cellphone }" style="width: 78%;display: inline">
                                </div>
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="固定电话">固定电话:</label>
                                    <input type="text" class="form-control userOwnCon" id="user_phone" name="user_phone"
                                           value="${user.user_phone }" style="width: 78%;display: inline">
                                </div>
                            </div>
                            <div class="row" style="margin-top: 20px">
                                <div class="col-md-4">
                                    <label class="userOwnTitle" title="邮箱地址">邮箱地址:</label>
                                    <input type="text" class="form-control userOwnCon" id="email" name="email"
                                           value="${user.email}" style="width: 78%;display: inline">
                                </div>
                                <c:if test="${user.user_autority != 3 }">
	                                <div class="col-md-4" align="left">
	                                    <label class="userOwnTitle" style="width: 21%; float: left; line-height: 35px" title="Shera环境">Shera环境:</label>
	                                    <select class="form-control" name = "sheraId" style="width: 78%; display: inline; float: right; " disabled>
	                                        <c:if test="${userShera != null }">
	                                            <option  value="${userShera.id }">${userShera.sheraUrl }</option>
	                                        </c:if>
	                                        <c:if test="${userShera == null }">
	                                            <option  value="0">没有添加Shera</option>
	                                        </c:if>
	                                    </select>
	                                </div>
                                </c:if>
                            </div>
                            <c:if test="${!cas_enable}">
                            <div class="basicInfoSaveBtn">
                                <span class="pull-right btn btn-primary btn-color" id="basicInfo">保存</span>
                            </div>
                            </c:if>
                        </form>
                    </div>
					<div id="preferenceinfo_wrap" class="tab_wrap hide">
						<div class="container-prefer">
	                        <div class="panel-group" id="accordion">
								<div class="panel panel-info">
									<div class="panel-heading">
										<h4 class="panel-title">
											<a data-toggle="collapse" data-parent="#accordion" 
											   href="#collapseOne">
												监控设置
											</a>
										</h4>
									</div>
									<div id="collapseOne" class="panel-collapse collapse in">
										<div class="panel-body">
											<div class="preferInfo">
												<span>默认值设置：</span>
											</div>
											<div class="preferInfo">
												<span>监控设置：</span>
												<label for="preferenceChk">
												<c:choose>
													<c:when test="${userFavor.monitor == 0 }">
														<input type="checkbox" id="PinpointChk">
													</c:when>
													<c:when test="${userFavor.monitor == 1 }">
														<input type="checkbox" id="PinpointChk" checked="1">
													</c:when>
													<c:otherwise>
														<input type="checkbox" id="PinpointChk" checked="1">
													</c:otherwise>
												</c:choose>
												Pinpoint监控</label>
											</div>
											<div class="form-group">
					                            <div class="col-md-offset-10 col-md-2">
					                                <button type="button" class="btn btn-primary btn-color" id="preferSave">保存</button>
					                            </div>
					                        </div>
										</div>
									</div>
								</div>
							</div>
							<div class="panel panel-success">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a data-toggle="collapse" data-parent="#accordion" 
										   href="#collapseTwo">
											主题颜色设置
										</a>
									</h4>
								</div>
								<div id="collapseTwo" class="panel-collapse collapse">
									<div class="panel-body">
										<div class="setColor preferInfo">
				                        	<div>皮肤颜色：
				                        		<i id="redColor" class="fa fa-circle" style="color:#e8504f;font-size:20px;cursor:pointer"></i>
				                        		<i id="blueColor" class="fa fa-circle" style="color:#418ac7;font-size:20px;cursor:pointer"></i>
				                        	</div>
				                        </div>
									</div>
								</div>
							</div>
						</div>
						
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
                                    <tr>
                                        <td>卷组容量（G）</td>
                                        <td>
                                            <div class="slider_bj">
                                                <div class="slider_block detailVolume" id="usedVolume"></div>
                                            </div>
                                        </td>
                                        <td>
                                            <span id="detailVolume">${usedstorage}</span>/<span id="totalVolume">${userResource.vol_size}</span>（G）
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
                    	<div class="col-md-4 pwd-img"><img src="/images/pwd.png" alt="修改密码"></div>
						<div class="col-md-8 pwd-con">
                        <form class="edit_pwd_form form-horizontal" action="" id="pswSave"
                              name="pswSave" method="post">
                            <div class="form-group">
                                <label for="originalPwd" class="col-md-2" style="width: 128px">原密码：</label>
                                <div class="col-md-7">
                                    <input type="password" class="form-control" id="originalPwd" name="originalPwd"
                                           value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="newPwd" class="col-md-2" style="width: 128px">新密码：</label>
                                <div class="col-md-7">
                                    <input type="password" class="form-control" id="newPwd" name="newPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="confirmNewPwd" class="col-md-2" style="width: 128px">确认新密码：</label>
                                <div class="col-md-7">
                                    <input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-6 col-md-3">
<!--                                     <button type="button" class="pull-right btn btn-primary" id="modifyPwd">提交修改</button> -->
                                    <span class="pull-right btn btn-primary btn-color" id="modifyPwd">提交修改</span>
                                </div>
                            </div>

                        </form>
						</div>
                    </div>

                </div>


            </div>
        </div>
    </article>
</div>

</body>
</html>