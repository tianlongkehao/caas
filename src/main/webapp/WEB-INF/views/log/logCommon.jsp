<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>模板</title>
	<%@include file="../frame/header.jsp"%>
	<script type="text/javascript" src="<%=path%>/js/log/logCommon.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="template" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: 110px">通用操作日志</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>通用操作日志
									</h5>
									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content">
									<table
										class="table table-striped table-hover dataTables-example"
										data-filter=#filter>
										<thead>
											<tr>
												<th>&nbsp;</th>
												<th>操作人</th>
												<th>操作模块</th>
												<th>操作内容</th>
	                                            <th>操作类型</th>
	                                            <th>操作时间</th>
											</tr>
										</thead>
										<tbody id="logCommonList">
											
											<%-- 	<tr>
													<td style="width: 13%;text-indent:10px;">11</td>
														<td style="width: 50%">22</td>
														<td style="width: 20%">33</td>
			                                            <td style="width: 15%;height:40px;">44
			                                                 <fmt:formatDate value="${dockerfile.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                                            </td>
												</tr> --%>
											
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="4">
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
/* 		$('.dataTables-example').dataTable({
	        "aaSorting": [[ 3, "desc" ]]
		}); */
	</script>

</body>
</html>