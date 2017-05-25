<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>redis</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/redis.css" />
<script type="text/javascript" src="<%=path%>/js/database/redis.js"></script>
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
						<li class=""><a href="<%=path %>/db/redis"><span id="nav2">数据库服务</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: 110px">redis</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>redis
									</h5>
									<div class="ibox-tools">
										<a id="clusterAdd" href="<%=path %>/db/redis/create"
											title="新建集群"><i class="fa fa-plus"></i>
										</a>
										<a id="deleteButton" class="no-drop"
											href="javascript:delEnvTemplates()" title="删除">
											<i class="fa fa-trash"></i>
										</a> 
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
												<th style="width: 5%; text-indent: 30px;">
													<input type="checkbox" autocomplete="off" class="chkAll" id="checkallbox"/>
												</th>
												<th style="">集群名称</th>
												<th style="">使用者</th>
												<th style="">节点个数</th>
												<th style="">集群模式</th>
												<th style="">redis内存/容器内存</th>
												<th style="">CPU资源</th>
												<th style="">集群状态</th>
												<th style="">创建时间</th>
												<th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="clustersList">
											
												<tr>
													<td style="width: 5%; text-indent: 30px;">
														<input type="checkbox" class="chkItem" name="chkItem"
															value="" />
													</td>
													<td style=""><a class="link" onclick="clusterDetail(this)">abd</a></td>
													<td style="">me</td>
													<td style="">2</td>
													<td style="">集群</td>
													<td style="">2/12</td>
													<td style="">10</td>
													<td style="">运行中</td>
													<td style="">2017-02-01 12:12</td>
		                                            <td style="width: 10%;" >
		                                            	<a class="no-drop"><i class="fa fa-play"></i></a>
		                                            	<a class="no-drop"><i class="fa fa-power-off"></i></a>
		                                            	<a onclick="changeCluster()" class="no-drop"><i class="fa fa-edit"></i></a>
		                                            	<a onclick="cfgCluster()" class="no-drop"><i class="fa fa-cog"></i></a>
														<a id="deleteButton" class="no-drop" href="javascript:oneDeleteEnvTemplate('${envTemplate.templateName }')" title="删除" >
														<i class="fa fa-trash"></i></a>
		                                            </td>
												</tr>
											
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
	<!-- 集群详情弹窗 -->
	<div class="clusterDetailInfo" style="display: none">
		<div class="tableInfo">
			<table class="table" style="margin-top:20px;margin-bottom:4px;">
				<thead>
					<tr>
						<th style="width:17%;padding-left:20px">节点名称</th>
						<th style="width:10%;">节点类型</th>
						<th style="width:10%;">IP</th>
						<th style="width:10%;">端口</th>
						<th style="width:15%;">创建时间</th>
						<th style="width:15%;">更新时间</th>
						<th style="width:10%;">操作</th>
					</tr>			
				</thead>
			</table>
			<div class="clusterInfoDiv">
				<table class="table table-hover table-striped">
					<tbody id="clusterInfoList">
						<tr>
							<td style="width:17%;padding-left:20px">cluster-1 </td>
							<td style="width:10%;">主节点</td>
							<td style="width:10%;">192.168.2.50</td>
							<td style="width:10%;">2255</td>
							<td style="width:15%;">2017-01-02 15:15</td>
							<td style="width:15%;">2017-01-02 15:15</td>
							<td style="width:10%;">
								<a class="link"><i>重启</i></a>
								<a class="link" onclick="oneNodeLogs()"><i>日志</i></a>
								<a class="link"><i>终端</i></a>
							</td>
						</tr>
						<tr>
							<td style="width:17%;padding-left:20px">cluster-2 </td>
							<td style="width:10%;">从节点</td>
							<td style="width:10%;">192.168.2.50</td>
							<td style="width:10%;">2255</td>
							<td style="width:15%;">2017-01-02 15:15</td>
							<td style="width:15%;">2017-01-02 15:15</td>
							<td style="width:10%;">
								<a class="link"><i>重启</i></a>
								<a class="link" onclick="oneNodeLogs()"><i>日志</i></a>
								<a class="link"><i>终端</i></a>
							</td>
						</tr> 
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 创建集群 -->
	<div class="oneNodeLogsInfo" style="display: none">
		<textarea style="margin:20px 15px;width:95%;height:500px"></textarea>
	</div>
	<script type="text/javascript">
		$('.dataTables-example').dataTable({
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,5,6,9] }],
	        "aaSorting": [[ 8, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
	</script>
</body>
</html>