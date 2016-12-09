<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>创建租户</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/user.css" />
<script type="text/javascript" src="<%=path%>/js/user/user_create.js"></script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="user" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">创建租户</li>
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
							<form id="add_tenement" name="add_tenement" action=""
								method="post">
								<div class="step-inner" style="left: 0%;">

									<%--基本信息--%>
									<div class="host_step1">
										<div class="blankapp" style="text-align: center">
											<div class="row">
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">登录账号:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="text" 
														class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="userName" name="userName">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="text" 
														class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="user_realname" name="user_realname">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">权限选择:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <select class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="user_autority" name="user_autority">
														<option name="user_autority" value="2">租户</option>
														<option name="user_autority" value="1">管理员</option>
													</select>
												</div>
											</div>

											<div class="row" style="margin-top: 15px">
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <select class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="province" name="user_province">
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
													<label style="width: 20%; float: left; line-height: 35px">公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司:</label>
													<input type="text" class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="company" name="company">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">所属部门:</label>
													<input type="text" class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="user_department" name="user_department">
												</div>
											</div>
											<div class="row" style="margin-top: 15px">
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
													<input type="text" class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="user_employee_id" name="user_employee_id">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">手机号码:</label>
													<input type="text" class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="user_cellphone" name="user_cellphone">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">固定电话:</label>
													<input type="text" class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="user_phone" name="user_phone">
												</div>
											</div>

											<div class="row" style="margin-top: 15px">
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">电子邮箱:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="text" 
														class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="email" name="email">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">登陆密码:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="password" 
														class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="pwd" name="password">
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 20%; float: left; line-height: 35px">确认密码:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="password" 
														class="form-control"
														style="width: 75%; display: inline; float: right;"
														id="confirm_pwd">
												</div>
											</div>
										</div>
										<div class="list-item-description" style="padding-top: 100px;">
											<a href="<%=path%>/user/list"><span
												class="btn btn-default go_user" style="margin-right: 30px;">返回</span></a>
											<span class="next2 pull-right btn btn-primary pull_confirm"
												data-attr="tenxcloud/mysql" id="user_create_next2">下一步</span>
										</div>
									</div>

									<%--资源配额--%>
									<div class="host_step2">
										<div class="blankapp" style="text-align: center">
											<div class="row">
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">CPU数量:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="number" value="1"
														class="number form-control" min="1" autocomplete="off"
														placeholder="1" id="cpu_account" name="cpu_account"
														style="width: 75%; display: inline; float: right;">
													<span class="resource-unit">个</span>
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;存:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="number" value="1"
														class="number form-control" min="1" autocomplete="off"
														placeholder="1" id="ram" name="ram"
														style="width: 75%; display: inline; float: right;">
													<span class="resource-unit">GB</span>
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">卷组容量:</label>
													<label style="width: 2%; float: left;"><font
														color="red">*</font></label> <input type="number" value="1"
														class="number form-control" min="1" autocomplete="off"
														placeholder="1" id="vol" name="vol"
														style="width: 75%; display: inline; float: right;">
													<span class="resource-unit">GB</span>

												</div>
											</div>
										    <div class="row" style="margin-top: 15px">
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 21%; float: left; line-height: 35px">镜像个数:</label>
                                                    <input type="number" value="50" class="number form-control"
                                                        min="1" autocomplete="off" placeholder="1" id="image_count"
                                                        name="image_count"
                                                        style="width: 75%; display: inline; float: right;">
                                                        <span class="resource-unit">个</span>
                                                </div>
                                                <div class="col-md-4" align="left">
                                                    <label style="width: 21%; float: left; line-height: 35px">Shera环境:</label>
                                                    <select class="form-control" style="width: 75%; display: inline; float: right;"></select>
                                                </div>
                                            </div>
<!-- 											<div class="row" style="margin-top: 15px">
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">Pod数量:</label>
													<input type="number" value="0" class="number form-control"
														min="1" autocomplete="off" placeholder="1" id="pod_count"
														name="pod_count"
														style="width: 75%; display: inline; float: right;"
														disabled> <span class="resource-unit">个</span>
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">副本控制器:</label>
													<input type="number" value="0" class="number form-control"
														min="1" autocomplete="off" placeholder="1"
														id="image_control" name="image_control"
														style="width: 75%; display: inline; float: right;"
														disabled> <span class="resource-unit">个</span>
												</div>
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">服&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务:</label>
													<input type="number" value="0" class="number form-control"
														min="1" autocomplete="off" placeholder="1"
														id="server_count" name="server_count"
														style="width: 75%; display: inline; float: right;"
														disabled> <span class="resource-unit">个</span>

												</div>
											</div>
											<div class="row" style="margin-top: 15px">
												<div class="col-md-4" align="left">
													<label style="width: 21%; float: left; line-height: 35px">卷组挂载量:</label>
													<input type="number" value="0" class="number form-control"
														min="1" autocomplete="off" placeholder="1" id="vol_count"
														name="vol_count"
														style="width: 75%; display: inline; float: right;"
														disabled> <span class="resource-unit">个</span>
												</div>
											</div> -->
										</div>

										<div class="" style="padding-top: 180px">
											<span class="btn btn-default last_step"
												style="margin-right: 30px;">上一步</span> <span
												id="user_save_finishBtn"
												class="next3 pull-right btn btn-primary pull_confirm">完成</span>
										</div>

									</div>
									<!-- host_step2 -->

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
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="pod_cpu_default" id="pod_cpu_default" value="0.5"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="pod_memory_default" id="pod_memory_default" value="1"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">上限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="pod_cpu_max" id="pod_cpu_max"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="pod_memory_max" id="pod_memory_max"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">下限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="pod_cpu_min" id="pod_cpu_min" value="0.2" /></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="pod_memory_min" id="pod_memory_min" value="0.5"/></td>
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
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="container_cpu_default" id="container_cpu_default" value="0.5"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="container_memory_default" id="container_memory_default" value="0.5"/></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">上限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="container_cpu_max" id="container_cpu_max" /></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="container_memory_max" id="container_memory_max" /></td>
                                            </tr>
                                            <tr>
                                                <th style="text-align: center">下限</th>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="container_cpu_min" id="container_cpu_min" value="0.1"/></td>
                                                <td style="text-align: center"><input data-slider-id='volSlider' type="text"  style="width:350px" class="restrictionVal" name="container_memory_min" id="container_memory_min" value="0.5"/></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="" style="padding-top: 100px">
                                        <span class="btn btn-default last_step" style="margin-right: 30px;">上一步</span>
                                        <span id="user_save_finishBtn" class="pull-right btn btn-primary pull_confirm">完成</span>
                                    </div>
                                </div>--%>
								</div>
							</form>

						</div>

					</div>

				</div>
			</div>
		</article>
	</div>
	<%--<script type="text/javascript" >
	$(document).ready(function(){
		$("#userName").blur(function(){
			var username = $.trim($("#userName").val());
				var un = username.toLowerCase();
				console.info(un);
				$("#userName").val(un);
				//console.info("name: "+username);
				 $.get(
					 "<%=path %>/user/checkUsername/"+un,
					 function(data,status){
				    	console.info("Data: " + data + "\nStatus: " + status);
				    	var data = eval("(" + data + ")");
						if(data.status=="400"){
                            layer.tips('登陆帐号已经被使用，请输入新的帐号！','#userName',{
                                tips: [1, '#0FA6D8']
                            });
                            $('#userName').focus();
                            return false;
//                            $("#userName").focus();
//							layer.alert("登陆帐号已经被使用，请输入新的帐号！");

					 	}
						if(data.status=="300"){
							layer.alert("k8s已经建立此名称的namespace，请输入新的帐号！");
                            closeBtn(0);
							$("#userName").focus();
					 	}
					});

		});
	});
</script>--%>
</body>
</html>
