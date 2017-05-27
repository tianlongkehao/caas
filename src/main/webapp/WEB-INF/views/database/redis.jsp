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
												<th style="">redis内存(G)</th>
												<th style="">CPU(个)</th>
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
													<td style="">12</td>
													<td style="">2</td>
													<td style="">运行中</td>
													<td style="">2017-02-01 12:12</td>
		                                            <td style="width: 10%;" >
		                                            	<a class="no-drop" title="启动"><i class="fa fa-play"></i></a>
		                                            	<a class="no-drop" title="停止"><i class="fa fa-power-off"></i></a>
		                                            	<a href="<%=path %>/db/redis/create" class="no-drop" title="查看配置"><i class="fa fa-cog"></i></a>
														<a onclick="changeCfg(this)" clusterType="1" redisMem="32" class="no-drop" title="修改"><i class="fa fa-edit"></i></a>
														<a id="deleteButton" class="no-drop" title="删除" href="javascript:oneDeleteEnvTemplate('${envTemplate.templateName }')" title="删除" >
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
	<!-- 日志 -->
	<div class="oneNodeLogsInfo" style="display: none">
		<textarea></textarea>
	</div>
	<!-- 修改集群配置 内存 cpu port 内存消除策略 -->
	<div class="changeCfgInfo" style="display: none">
		<div style="margin: 25px 15px" class="createRedisStep1">
			<div class="infoCred">
				<span class="labelCred">redis内存：</span> 
				<div id="changeRedisMemSec"></div>
			</div>
			<div class="infoCred">
				<span class="labelCred">端口：</span> <input type="text"
					class="form-control conCred" id="" name=""
						value="">
			</div>
			<div class="infoCred">   
				<span class="labelCred">内存消除策略：</span> 
					<select class="form-control conCred" id="" name="" >
						<option value="1">allkeys_lru</option>
						<option value="2">Volatile_lruG</option>
						<option value="1">Volatile_random</option>
						<option value="2">allkeys_random</option>
						<option value="1">Volatile_ttl</option>
						<option value="2">noeviction</option>
					</select>
			</div>
			<div class="infoCred">
				<p class="pExplain">内存消除策略，默认allkeys-lru(优先移除主键中最近未使用的key), noeviction(不清除，超出最大内存报错),
				volatile-lru(优先移除过期键中最近未使用的key),allkeys-random(随机移除主键中某个key),
				volatile-random(随机移除过期键中某个key),volatile-ttl(过期键中，优先移除过期时间更早的key)</p>
			</div>					   
		</div>
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