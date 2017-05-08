<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>快速构建</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<script type="text/javascript" src="<%=path%>/js/template/configmap-edit.js"></script>
</head>
<body>

	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: 110px"><a href="<%=path %>/configmap/list"><span id="nav2">配置文件模板</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: 110px">编辑模板</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action" style="width:100%">编辑配置文件模板</li>
                                </ul>
                            </div>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/configmap/createOrUpdateConfigMap" method="post">
								<div class="container" style="width: 90%">
									<div class="form-group ">
                                        <span style="float:left;line-height:34px">模板名称：</span>
                                    	<input type="hidden" name="configmapId" id="configmapId"  value = "${configmapId }" />
                                        <input type="text" class="form-control" name="configMapName" id="configMapName" style="width: 72%;float:left"  readOnly value = "${configMapName }" />
                                    	<div class="form-group" style="float:right;margin-top:7px;">
											<span id="crateEnvVariate" class=" btn-info btn-sm">添加配置文件</span>
										</div>
                                    </div>
									<li class="hide-set">
										<ol>
											<li>
												<table class="table table-hover enabled" id="env-variate">
													<thead>
														<tr>
															<th style="width: 20%">文件名</th>
															<th style="width: 70%">文件内容</th>
															<th style="width: 10%">操作</th>
														</tr>
													</thead>
													<tbody id="env-oper1">
														<c:forEach items="${configmapDataList }" var="configmapData">
															<tr>
																<td class="keys"><input type="text" style="width: 98%" value="${configmapData.key }"></td>
																<td class="vals"><textarea  style="width: 98%;height:200px">${configmapData.value }</textarea></td>
																<td class="func">
																	<a href="javascript:void(0)" onclick="deleteRow(this)" class="gray">
																	<i class="fa fa-trash-o fa-lg"></i></a>
																	<input type="hidden" class="oldValue" value="">
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</li>
										</ol>
									</li>
								</div>
								<div class="container" style="width: 90%">
									<div class="list-item-description" >
										<a href="<%=path%>/configmap/list"><span
											class="btn btn-default" style="margin-right: 30px;">返回</span></a>
										<span id="exportBtn" class="btn btn-primary btn-color pull-right" style="cursor: pointer">保存</span>
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