<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />
<script type="text/javascript"
	src="<%=path%>/js/service/service-import.js"></script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2" style="width: 102px">引入外部服务</li>
					</ol>
				</div>
				<div class="contentMain">


					<aside class="aside-btn">
						<div class="btns-group">
							<span class="btn btn-defaults btn-white"><i
								class="icon-map-marker"></i><span class="ic_left">BCM</span></span>
						</div>
					</aside>
					<div class="caption clearfix">
						<ul class="toolbox clearfix">
							<li><a href="javascript:void(0);" id="serviceReloadBtn"><i
									class="fa fa-repeat"></i></a></li>
							<li><a id="importServiceBtn"><i class="fa fa-plus"></i>&nbsp;&nbsp;新建</a></li>
							<li class="dropdown"><a data-toggle="dropdown"
								href="javascript:void(0);">更多操作&nbsp;&nbsp;<i
									class="fa fa-caret-down"></i></a>
								<ul class="dropdown-menu serviceOperation">
									<li><a id="deleteButton" class="no-drop"
										href="javascript:void()" onclick="delImportSers()"> <i class="fa fa-trash"></i>
											<span class="ic_left">删除</span>
									</a></li>
								</ul></li>
						</ul>
					</div>
					<div class="itemTable">
						<table class="table service-table">
							<thead>
								<tr>
									<th>
										<div class="table-head">
											<table class="table">
												<thead>
													<tr>
														<th style="width: 5%; text-indent: 30px;"><input
															type="checkbox" autocomplete="off" class="chkAll" /></th>
														<th style="width: 18%; padding-left: 5px;">服务名称</th>
														<th style="width: 20%; text-indent: 8px;">服务访问地址</th>
														<th style="width: 20%;">外部服务地址</th>
														<th style="width: 14%;">可见域</th>
														<th style="width: 10%;">操作</th>
													</tr>

												</thead>
											</table>
										</div>
									</th>
								</tr>
							</thead>
							<tbody id="serviceBody">
								<tr>
									<td>
										<div>
											<table class="table">
												<tbody id="importSerList">
													
												</tbody>
											</table>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

					</div>

				</div>
			</div>
		</article>
	</div>
	<div id="import-service" style="display: none;">
		<form>
			<table class="table enabled"
				style="width: 345px; padding: 5px; margin: 10px">
				<tbody class="BORDER">
					<tr>
						<th style="width: 35%">服务名称：</th>
						<td><input class="" type="text" id="improt-ser-name" value=""></td>
					</tr>
					<tr>
						<th style="width: 35%">服务访问地址：</th>
						<td><input class="" type="text" id="improt-ser-in" value=""></td>
					</tr>
					<tr>
						<th style="width: 35%">外部服务地址：</th>
						<td><input class="" type="text" id="improt-ser-out" value=""></td>
					</tr>
					<tr>
						<th style="width: 35%">可见域：</th>
						<td><select class="" id="improt-ser-visibility">
								<option>仅本租户可见</option>
								<option>所有租户可见</option>
						</select></td>
					</tr>

				</tbody>
			</table>
		</form>
	</div>

	${msg}
	<c:if test="${msg!= null} ">
		<input type="hidden" id="errorMsg" value="${msg}">
		<script type="text/javascript">
			//var errorMsg = $("#errorMsg").val();
			var errorMsg = '${msg}';
			alert(errorMsg);
		</script>
	</c:if>

</body>
</html>