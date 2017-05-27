<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>云存储</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/storage.css" />
<script type="text/javascript"
	src="<%=path%>/js/storage/storage-snap.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i
								class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">云存储</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">快照</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>快照
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table
										class="table table-stripped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="width: 10%; text-indent: 20px;">快照名称</th>
												<th style="width: 10%">磁盘名称</th>
												<th style="width: 10%;">创建时间</th>
												<th style="width: 10%;">快照描述</th>
												<th style="text-indent: 5px;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="snapList">
											<c:forEach items="${cephSnaps}" var="snap">
												<tr>
													<td style="width: 10%; text-indent: 20px;">${snap.name}</td>
													<td style="width: 10%">${snap.imgname}</td>
													<td style="width: 10%;">${snap.createDate}</td>
													<td style="width: 10%;">${snap.snapdetail}</td>
													<td style="text-indent: 5px;" class="del-operation"><a
														onclick="storageRollBack(this)" rbd="${snap.imgname}" snap="${snap.name}" title="回滚磁盘"><i
															class="fa fa-history"></i></a></td>
												</tr>
											</c:forEach>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="11">
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

	<script type="text/javascript">
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 10 ]
			} ],
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
	</script>

</body>
</html>