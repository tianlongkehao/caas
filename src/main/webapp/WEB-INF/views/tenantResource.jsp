<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>集群</title>
<%@include file="frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/cluster.css" />
</head>
<body>
	<div class="page-container">
		<article>
			<div class="ibox-content">
				<table class="table table-striped table-hover dataTables-example">
					<thead>
						<tr>
							<th style="width: 13%; text-indent: 30px">租户</th>
							<th style="width: 10%;">数据库Cpu</th>
							<th style="width: 10%;">数据库Mem(G)</th>
							<th style="width: 10%;">QuotaCpu</th>
							<th style="width: 10%;">QuotaMem</th>
							<th style="width: 10%;">QuotaCpuUsed</th>
							<th style="width: 10%;">QuotaMemUsed</th>
							<th style="width: 27%;">操作</th>
						</tr>
					</thead>
					<tbody id="data">
						<c:forEach items="${tenantResources}" var="tenantResource">
							<tr tenantId=${tenantResource.id }>
								<td style="width: 13%; text-indent: 30px">${tenantResource.name}</td>
								<td style="width: 10%;">${tenantResource.cpu}</td>
								<td style="width: 10%;">${tenantResource.memory}</td>
								<td style="width: 10%;">${tenantResource.quotaCpu}</td>
								<td style="width: 10%;">${tenantResource.quotaMem}</td>
								<td style="width: 10%;">${tenantResource.quotaCpuUsed}</td>
								<td style="width: 10%;">${tenantResource.quotaMemUsed}</td>
								<td style="width: 27%;">
									<button onclick="rc(this)">调整RC</button>
									<button onclick="pod(this)">删除Pod</button>
									<button onclick="quota(this)">调整Quota</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</article>
	</div>
	<script type="text/javascript">
		$('.dataTables-example').dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 4 ]
			} ],
			"searching" : false
		});
		$("#checkallbox").parent().removeClass("sorting_asc");

		function rc(obj) {
			var tenantId = $(obj).parent().parent().attr("tenantId");
			$.ajax({
				url : "" + ctx + "/quota/updateRc.do?tenantId=" + tenantId,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("Rc调整成功！", {
							icon : 6
						});
					} else {
						layer.alert("Rc调整失败！");
					}
				}
			});
		}

		function pod(obj) {
			var tenantId = $(obj).parent().parent().attr("tenantId");
			$.ajax({
				url : "" + ctx + "/quota/deletePods.do?tenantId=" + tenantId,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("Pod调整成功！", {
							icon : 6
						});
						location.reload();
					} else {
						layer.alert("Pod调整失败！");
					}
				}
			});
		}

		function quota(obj) {
			var tenantId = $(obj).parent().parent().attr("tenantId");
			$.ajax({
				url : "" + ctx + "/quota/updateQuota.do?tenantId=" + tenantId,
				success : function(data) {
					data = eval("(" + data + ")");
					if (data.status == "200") {
						layer.msg("Quota调整成功！", {
							icon : 6
						});
						location.reload();
					} else {
						layer.alert("Quota调整失败！");
					}
				}
			});
		}
	</script>
</body>
</html>