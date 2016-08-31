<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>快速构建</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<script type="text/javascript" src="<%=path%>/js/template/env-add.js"></script>
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
                                    <li class="radius_step action" style="width:100%">新增环境变量</li>
                                </ul>
                            </div>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/template/env/build" method="post">
								<div class="container" style="width: 90%">
									<li class="hide-set"><span class="ve_top">环境变量：</span>
										<ol>
											<li class="hide-select"><input type="text"
												placeholder="name" id="envName"> <input type="text"
												placeholder="value" id="envValue"> <a id="crateEnvVariate"><i
													class="fa fa-plus"></i>添加</a>
												<div style="float: right">
													<span id="importBtn" class=" btn-info btn-sm"
														>导入模板</span>
												</div></li>
											<li>
												<table class="table table-hover enabled" id="env-variate">
													<thead>
														<tr>
															<th style="width: 45%">键</th>
															<th style="width: 45%">值</th>
															<th style="width: 10%">操作</th>
														</tr>
													</thead>
													<tbody id="env-oper1">
														<input type="hidden" id="arrayKey" value="" />
													</tbody>
												</table>
											</li>
										</ol></li>
								</div>
								<div class="container" style="width: 90%">
									<div class="list-item-description" style="padding-top: 100px;">
										<a href="<%=path%>/template/env"><span
											class="btn btn-default" style="margin-right: 30px;">返回</span></a>
										<span id="buildStorage" class="btn btn-primary pull-right">保存</span>
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