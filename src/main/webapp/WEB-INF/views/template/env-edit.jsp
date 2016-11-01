<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>快速构建</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<script type="text/javascript" src="<%=path%>/js/template/env-edit.js"></script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">模板</li>
						<li style="margin-left: -44px;"><i class="fa fa-angle-right"></i></li>
						<li class="active">环境变量</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action" style="width:100%">编辑环境变量</li>
                                </ul>
                            </div>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/template/env/build" method="post">
								<div class="container" style="width: 90%">
									<div class="form-group ">
                                        <span style="float:left;line-height:34px">模板名称：</span>
                                         <input type="hidden" id="envId" value = "" />
                                        <input type="text" class="form-control" id="envTemplateName" style="width: 72%;float:left" disabled value = "${templateName }" />
                                    </div>
									<div class="form-group" style="float:right">
										<span id="crateEnvVariate" class=" btn-info btn-sm">添加环境变量</span>
										<span id="importBtn" class=" btn-info btn-sm">导入模板</span>
									</div>
									<li class="hide-set">
										<ol>
											<li>
												<table class="table table-hover enabled" id="env-variate">
													<thead>
														<tr>
															<th style="width: 45%">键</th>
															<th style="width: 45%">值</th>
															<th style="width: 10%">操作</th>
														</tr>
													</thead>
													<tbody id="envList">
														<c:forEach items="${envTemplateList }" var="envTemplateList">
															<tr>
																<td class="keys"><input type="text" style="width: 98%" value="${envTemplateList.envKey }"></td>
																<td class="vals"><input type="text" style="width: 98%" value="${envTemplateList.envValue }"></td>
																<td class="func">
																	<a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">
																	<i class="fa fa-trash-o fa-lg"></i></a>
																	<input type="hidden" class="oldValue" value="">
																</td>
															</tr>
														</c:forEach>
													</tbody>
													<input type="hidden" id="arrayKey" value="" />
												</table>
											</li>
										</ol>
									</li>
								</div>
								<div class="container" style="width: 90%">
									<div class="list-item-description" >
										<a href="<%=path%>/template/env"><span
											class="btn btn-default" style="margin-right: 30px;">返回</span></a>
										<span id="exportBtn" class="btn btn-primary pull-right" style="cursor: pointer">保存</span>
									</div>
								</div>
								<!-- 环境变量导入模板 -->
								<div id="environment-variable" style="display:none; max-height:360px;overflow-y:scroll;overflow-x:hidden;">
									<table class="table table-hover enabled" id="Path-table"
										style="width: 326px; margin: 5px 10px 5px 10px">
										<tbody id="Path-env">
										
										</tbody>
									</table>
								</div>
								<!-- 环境变量另存为模板 -->
								<div id="environment-template" style="display:none; max-height:170px;overflow-y:scroll;overflow-x:hidden;">
									<div style="width: 326px; margin: 5px 10px 5px 10px">
										<span>模板名称：</span><input type="text" id="envTemplateName"
											style="width: 77%" autofocus="autofocus" />
									</div>
								</div>
								<input type="hidden" id="envVariable" name="envVariable"
									value=""></input>
							</form>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>

</body>
</html>