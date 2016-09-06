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
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>引入外部服务
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
										<a href="javascript:void(0)" id="importServiceBtn" title="新建"><i
											class="fa fa-plus"></i></a>  <a id="SearchBtn1" title="搜索"><i
											class="fa fa-search"></i></a>
											<a href="" id="delImportSers"
											onclick="delImportSers()" title="删除"><i
											class="fa fa-trash"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<input type="text" class="form-control input-sm m-b-xs"
										id="filter1" placeholder="搜索表格...">

									<table class="footable table table-stripped" data-page-size="8"
										data-filter=#filter1>
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" autocomplete="off" class="chkAll"
													id="checkallbox" /></th>
												<th style="width: 18%; padding-left: 5px;">服务名称</th>
												<th style="width: 20%; text-indent: 8px;">服务访问地址</th>
												<th style="width: 20%;">外部服务地址</th>
												<th style="width: 14%;">可见域</th>
												<th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="storageList">

										</tbody>
										<tfoot>
											<tr>
												<td colspan="6">
													<ul class="pagination pull-right"></ul>
												</td>
											</tr>
										</tfoot>
									</table>
								</div>
							</div>
						</div>
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
	<script type="text/javascript">
		$(".footable").footable();
		$(".del-operation").children("span").addClass("hide");
		$("#checkallbox").next().addClass("hide");
	</script>
</body>
</html>