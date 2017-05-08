<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>shera</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<script type="text/javascript" src="<%=path%>/js/ci/shera.js"></script>
<script type="text/javascript" src="<%=path%>/js/ci/shera-detail.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="user" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i
								class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">shera管理</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre">
								<ul style="padding: 0 4rem;">
									<li class="radius_step action">基本信息</li>
								</ul>
							</div>
						</div>
						<div class="step-inner" style="left: 0%;margin-bottom:60px">
							<%-- 基本信息 --%>
							<div class="host_step1">
								<div>
									<%-- <c:if test="${shera}" var="shera"> --%>
									<div class="infoCred">
										<span class="labelspan">IP：<font color="red">*</font></span> <input type="text"
											class="form-control conCred1" id="sheraIp" name="sheraIp"
											placeholder="192.168.0.76" value="${shera.sheraUrl}">
									</div>
									<div class="infoCred">
										<span class="labelspan">端口：<font color="red">*</font></span> <input type="text"
											class="form-control conCred1" id="port" name="port"
											placeholder="" value="${shera.port}">
									</div>
									<div class="infoCred">
										<span class="labelspan">账号：<font color="red">*</font></span> <input type="text"
											class="form-control conCred1" id="shreaName" name="shreaName"
											value="${shera.userName }">
									</div>
									<div class="infoCred">
										<span class="labelspan">密码：<font color="red">*</font></span> <input type="password"
											class="form-control conCred1" id="shreaPassword"
											name="shreaPassword" value="${shera.password }">
									</div>
									<div class="infoCred">
										<span class="labelspan">描述：</span>
										<textarea class="form-control conCred1" style="height: 100px" id="sheraRemark" rows="8" value="${shera.remark }"></textarea>
									</div>
									<%-- </c:if> --%>
									<div class="infoCred">
										<span class="labelspan">JDK：</span>
										<table class="table enabled conCred1 jdkCon">
											<thead>
												<tr>
													<th style="width: 35%">name</th>
													<th style="width: 35%">path</th>
													<th style="vertical-align: middle; width: 10%">操作</th>
												</tr>
											</thead>
											<tbody class="jdktbody">
												<c:forEach items="${allJdk.items }" var="jdk">
													<tr class="plus-row">
													<td>${jdk.version}</td>
													<td>${jdk.path}</td>
													<td><a onclick="deleteRow(this)" class="gray"><i class="fa fa-trash-o fa-lg"></i></a></td>
													</tr>
												</c:forEach>
											</tbody>
											<tfoot>
												<tr>
													<td colspan="3" id="jdk" class="create"><i class="fa fa-plus margin"></i>添加</td>
												</tr>
											</tfoot>
										</table>
										<input type="hidden" id="arrayJdk" value="" />
									</div>
									<div class="infoCred">
										<span class="labelspan">sonar：</span>
										<div class="sonarsCon row conCred1 conCredIbox sonarIbox">
											<div class="form-group col-md-12">
												<label class="labelspan spantest">sonar版本:</label>
												<input type="text" class="form-control conCred1 sonarVersion" value="">
											</div>
											<div class="form-group col-md-12">
												<span class="labelspan spantest">环境变量：</span>
												<table class="table enabled conCred1 jdkCon">
													<thead>
														<tr>
															<th style="width: 35%">key</th>
															<th style="width: 35%">val</th>
															<th style="vertical-align: middle; width: 10%">操作</th>
														</tr>
													</thead>
													<tbody class="sonartbody">
														
													</tbody>
													<tfoot>
														<tr>
															<td colspan="3" id="sonar" class="create"><i
																class="fa fa-plus margin"></i>添加</td>
														</tr>
													</tfoot>
												</table>
											</div>
										</div>
									</div>
									<div class="infoCred">
										<span class="labelspan">Maven：</span>
										<input type="button" class="btn btn-info" value="添加 Maven" onclick="addMaven()">
										<div class="mavens">

										</div>
									</div>
									<div class="infoCred">
										<span class="labelspan">ant：</span>
										<input type="button" class="btn btn-info" value="添加 Ant" onclick="addAnt()">
										<input type="hidden" value="${antConfig }" id="antConfig">
										<div class="ants">
											<%-- <c:forEach items="${antConfig }" var="antConfig">
												<input type="hidden" value="${antConfig.env }" id="antEnv">
												<div class="row conCred conCredIbox antIbox">
													<div class="">
														<div class="ibox float-e-margins">
															<div class="ibox-title">
																<h5>ant</h5>
																<div class="ibox-tools">
																	<a class="collapse-link"> <i
																		class="fa fa-chevron-up"></i>
																	</a> <a class="close-link"> <i
																		class="fa fa-times fa-delete"></i>
																	</a>
																</div>
															</div>
															<div class="ibox-content">
																<div class="row ant-config">
																	<div class="form-group col-md-12">
																		<label class="labelspan spantest">ant版本:</label> <input
																			type="text" class="form-control conCred antVersion"
																			value="${antConfig.version }">
																	</div>
																	<div class="form-group col-md-12">
																		<span class="labelspan spantest spantest">环境变量：</span>
																		<table class="table enabled conCred jdkCon">
																			<thead>
																				<tr>
																					<th style="width: 35%">key</th>
																					<th style="width: 35%">value</th>
																					<th style="vertical-align: middle; width: 10%">操作</th>
																				</tr>
																			</thead>
																			<tbody class="anttbody">
																				
																			</tbody>
																			<tfoot>
																				<tr>
																					<td colspan="3" id="ant" class="create"><i
																						class="fa fa-plus margin"></i>添加</td>
																				</tr>
																			</tfoot>
																		</table>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
											</c:forEach> --%>
										</div>
									</div>

								</div>
							</div>
							<div class="lastBtns">
								<a type="button" class="btn btn-default" href="<%=path %>/user/shera">返回</a>
								<button id="saveSheraBtn" class="pull-right btn btn-primary btn-color pull_confirm">修改</button>
							</div>
						</div>
					</div>
				</div>
			</div>

		</article>
	</div>


	<script type="text/javascript">
	$(document).ready(function(){
		//antEnv
		var antConfig = $("#antConfig").val();
		var addAntDetailHtml ="";
		var antEnvHtml = "";
		for(var antNum = 0; antNum<antConfig.length; antNum++){
			addAntDetailHtml += '<div class="row conCred conCredIbox antIbox">'
				+'<div class="">'
				+'<div class="ibox float-e-margins">'
				+'<div class="ibox-title">'
				+'<h5>ant</h5>'
				+'<div class="ibox-tools">'
				+'<a class="collapse-link"> <i class="fa fa-chevron-up"></i>'
				+'</a> <a class="close-link"> <i class="fa fa-times fa-delete"></i>'
				+'</a>'
				+'</div>'
				+'</div>'
				+'<div class="ibox-content">'
				+'<div class="row ant-config">'
				+'<div class="form-group col-md-12">'
				+'<label class="labelspan spantest">ant版本:</label>'
				+'<input type="text" class="form-control conCred antVersion" value="'+antConfig[antNum].version+'">'
				+'</div>'
				+'<div class="form-group col-md-12">'
				+'<span class="labelspan spantest spantest">环境变量：</span>'
				+'<table class="table enabled conCred jdkCon">'
				+'<thead>'
				+'<tr>'
				+'<th style="width: 35%">key</th>'
				+'<th style="width: 35%">val</th>'
				+'<th style="vertical-align: middle; width: 10%">操作</th>'
				+'</tr>'
				+'</thead>'
				+'<tbody class="anttbody">';
			for(var key in antConfig.env){
				antEnvHtml += '<tr class="plus-row">'
								+'<td>'+key+'</td>'
								+'<td>'+antConfig.env[key]+'</td>'
								+'<td><a onclick="deleteRow(this)" class="gray"><i class="fa fa-trash-o fa-lg"></i></a></td>'
								+'</tr>';
			}
			addAntDetailHtml += antEnvHtml
				+'</tbody>'
				+'<tfoot>'
				+'<tr>'
				+'<td colspan="3" id="ant" class="create"><i class="fa fa-plus margin"></i>添加</td>'
				+'</tr>'
				+'</tfoot>'
				+'</table>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>';
		}
		$(".ants").append(addAntDetailHtml);

	});
	</script>
</body>
</html>
