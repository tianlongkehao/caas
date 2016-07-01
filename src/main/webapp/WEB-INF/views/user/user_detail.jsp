<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>租户信息</title>
    <%@include file="../frame/header.jsp" %> 
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/user.css"/>
    <script type="text/javascript" src="<%=path %>/js/user/user_detail.js"></script>
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
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">租户详情</li>
                </ol>
            </div>
            <div class="contentMain">
                <div class="modalCrealApp">
                        <div class="steps-main">
                            <div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action"><span>1</span> 基本信息</li>
                                    <li class="radius_step"><span>2</span> 资源配额</li>
                                    <%--<li class="radius_step"><span>3</span> 资源限制</li>--%>
                                </ul>
                            </div>
                            <div class="step-inner" style="left: 0%;">

                                <%--基本信息--%>
			                    <form id="update_tenement" name="update_tenement" action="" method="post" >
                                    <div class="host_step1" >
                                        <div class="blankapp" style=" text-align: center">
                                        
                                            <div class="row">
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">登录账号:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="userName" name="userName" value="${user.userName }" readonly="readonly">
													<input type="hidden" id="user_id" name="id" value="${user.id}">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_realname" name="user_realname" value="${user.user_realname }" readonly="readonly">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="hidden" id="user_autority_hidden" name="user_autority" value="${user.user_autority}">
                                                    <select class="form-control" style="width: 75%;display: inline; float: right;"
                                                    			 id="user_autority" disabled>
                                                        <option name="option" value="2" >租户</option>
                                                        <option name="option" value="1" >管理员</option>
                                                    </select>
                                                </div>
                                       
                                            </div>

                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="hidden" id="user_province_hidden" name="user_province" value="${user.user_province}">
                                                    <select class="form-control" style="width: 75%;display: inline; float: right;"
                                                             id="user_province" readonly="readonly">
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
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="company" name="company" value="${user.company }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">所属部门:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_department" name="user_department" value="${user.user_department }">
                                                </div>
                                            </div>
                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="user_employee_id" name="user_employee_id" value="${user.user_employee_id }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">手机号码:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_cellphone" name="user_cellphone" value="${user.user_cellphone }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">固定电话:</label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="user_phone" name="user_phone" value="${user.user_phone }">
                                                </div>
                                            </div>
                                            <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">电子邮箱:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;"
                                                           id="email" name="email" value="${user.email }">
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label class="stepLabel">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</label>
                                                    <label style="width: 2%; float: left ;"><font color="red">*</font></label>
                                                    <input type="text" class="form-control" style="width: 75%;display: inline; float: right;" 
                                                    		id="pwd" name="password"  value="${user.password}" readonly="readonly">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="list-item-description" style="padding-top: 100px">
                                            <a href="<%=path %>/user/list"><span class="btn btn-default go_user" style="margin-right: 30px;">返回</span></a>
                                            <span class="next2 pull-right btn btn-primary pull_confirm" data-attr="tenxcloud/mysql" id="user_create_next2">下一步</span>
                                        </div>
                                    </div>
                               

                                <%--资源配额--%>
                                <div class="host_step2" >
                                    <ul class="safeSet" style="margin-left: 270px">
                                        <li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">CPU数量：</span>
                                                <input type="text" value="${resource.cpu_account}" class="number" min="1" autocomplete="off"
                                                       placeholder="1" id="cpu_account" name="cpu_account" style="width:350px">
                                                <span class="unit">个</span>
                                            </div>
                                        </li>
                                        <li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">内存：</span>
                                                <input type="text" class="number" min="1" autocomplete="off"
                                                       placeholder="1" id="ram" name="ram" style="width:350px" value="${resource.ram }">
                                                <span class="unit">GB</span>
                                            </div>
                                        </li>
                                        <li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">卷组容量：</span>
                                                <input type="text" class="number" min="1" autocomplete="off"
                                                       placeholder="1" id="vol" name="vol" style="width:350px" value="${user.vol_size }">
                                                <span class="unit">GB</span>
                                            </div>
                                        </li>
                                        <li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">Pod数量：</span>
                                                <input type="text" value="${resource.pod_count}" class="number" min="1" autocomplete="off" disabled="disabled"
                                                       placeholder="1" id="pod_count" name="pod_count" style="width:350px"> <span class="unit">个</span>
                                            </div>
                                        </li>
                                        <li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">副本控制器：</span>
                                                <input type="text" value="${resource.image_control}" class="number" min="1" autocomplete="off" disabled="disabled"
                                                       placeholder="1" id="image_control" name="image_control" style="width:350px"> <span class="unit">个</span>
                                            </div>
                                        </li>
                                        <li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">服务：</span>
                                                <input type="text" value="${resource.server_count}" class="number" min="1" autocomplete="off" disabled="disabled"
                                                       placeholder="1" id="server_count" name="server_count"  style="width:350px"> <span class="unit">个</span>
                                            </div>
                                        </li>
										<li class="line-h-3">
                                            <div class="param-set">
                                                <span class="number-title">卷组挂载数量：</span>
                                                <input type="text" value="1" class="number" min="1" autocomplete="off" disabled="disabled"
                                                       placeholder="1" id="vol_count" name="vol_count" style="width:350px" > 
                                                <span class="unit">个</span>
                                            </div>
                                        </li>
                                        
                                    </ul>
                                    <div class="" style="padding-top: 100px">
                                        <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                        <span id="user_update_finishBtn" class="pull-right btn btn-primary pull_confirm">修改</span>
                                    </div>
                                    <%--<div class="" style="padding-top: 100px">
                                        <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                        <span id="nextBtn3" class="next3 pull-right btn btn-primary pull_confirm">下一步</span>
                                    </div>--%>
                                </div>

                                <%--3.资源限制--%>
                                <%--<div class="host_step3" style="height: auto;" >
                                    <div>
                                        <div style="padding: 10px;text-align: center"><span >Pod资源限制值</span></div>
                                        <table class="table table-hover enabled" id="pod" >
                                            <thead>
                                            <tr style="text-align: center">
                                                <th style="width: 20%;text-align: center">Pod</th>
                                                <th style="width: 33%;text-align: center">CPU（个）</th>
                                                <th style="width: 33%;text-align: center">内存（G）</th>
                                            </tr>
                                            </thead>
                                            <tbody id="pod-limit">
                                            <tr>
                                                <th style="text-align: center">默认值</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                									name="pod_cpu_default" id="pod_cpu_default" value="${restriction.pod_cpu_default }"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                									name="pod_memory_default" id="pod_memory_default" value="${restriction.pod_memory_default}"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">上限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                									name="pod_cpu_max" id="pod_cpu_max" value="${restriction.pod_cpu_max}"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                									name="pod_memory_max" id="pod_memory_max" value="${restriction.pod_memory_max }"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">下限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                									name="pod_cpu_min" id="pod_cpu_min" value="${restriction.pod_cpu_min}"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                									name="pod_memory_min" id="pod_memory_min" value="${restriction.pod_memory_min}"/></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div>
                                        <div style="padding: 10px;text-align: center"><span >Container资源限制值</span></div>
                                        <table class="table table-hover enabled" id="container" >
                                            <thead>
                                            <tr style="text-align: center">
                                                <th style="width: 20%;text-align: center">Container</th>
                                                <th style="width: 33%;text-align: center">CPU（个）</th>
                                                <th style="width: 33%;text-align: center">内存（G）</th>
                                            </tr>
                                            </thead>
                                            <tbody id="container-limit">
                                            <tr>
                                                <th style="text-align: center">默认值</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                										name="container_cpu_default" id="container_cpu_default" value="${restriction.container_cpu_default}"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                										name="container_memory_default" id="container_memory_default" value="${restriction.container_memory_default}"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">上限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                										name="container_cpu_max" id="container_cpu_max" value="${restriction.container_cpu_max}"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                										name="container_memory_max" id="container_memory_max" value="${restriction.container_memory_max}"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">下限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                										name="container_cpu_min" id="container_cpu_min" value="${restriction.container_cpu_min}"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal"
                                                										name="container_memory_min" id="container_memory_min" value="${restriction.container_memory_min}"/></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="" style="padding-top: 100px">
                                        <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                        <span id="user_update_finishBtn" class="pull-right btn btn-primary pull_confirm">修改</span>
                                    </div>
                                </div>--%>
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
