<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>集群</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/cluster.css" />
<script type="text/javascript" src="<%=path%>/js/cluster/test.js"></script>
</head>

<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="cluster" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i
								class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">集群测试</li>
					</ol>
				</div>
				<div class="contentMain" style="height: 400px;">
					<div class="clusterTestBtns">
						<span id="deployBtn" class="btn btn-primary btn-color pull-left"
							style="margin-right: 10px; cursor: pointer">部署</span>
						<span id="excuteBtn" class="btn btn-primary btn-color pull-left"
							style="margin-right: 10px; cursor: pointer">执行</span> 
						<span id="deleteBtn" class="btn btn-primary btn-color pull-left"
							style="margin-right: 10px; cursor: pointer">清除部署</span> 
					</div>
					
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>集群测试
									</h5>
									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);" title="刷新"><i
											class="fa fa-repeat"></i></a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content" style="padding: 0px"></div>
					<div class="caption clearfix"
						style="width: 100%; height: 100%; float: left;">
						<div class="" style="margin-bottom:15px">
							<table class="table table-striped table-hover dataTables-example">
								<thead>
									<tr>
										<th style="width:5%;text-indent:20px">
											<input type="checkbox" class="chkAll" id="checkallbox" />
										</th>
										<th style="width:15%;">集群节点</th>
										<th style="width:40%;">测试进度</th>
										<th style="width:20%;text-indent:20px">测试结果</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="routeList">
									<c:forEach items="${nodeList}" var="node">
										<tr>
											<td style="width:5%;text-indent:20px">
												<input class="chkItem" name="node" type="checkbox" value="${node.metadata.name }">
											</td>
											<td style="width:15%;">${node.metadata.name }</td>
											<td style="width:40%;" nodeName="${node.metadata.name }">
									        	<div class="progress nodeProgress" style="margin:0 auto">
									        		<div class="progress-bar" role="progressbar"
														 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">
													</div>
													<!-- <div class="progress-bar progress-bar-warning" role="progressbar"
														 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
														 style="width: 15%;">
														<span >部署完成</span>
													</div>
													
													<div class="progress-bar progress-bar-success" role="progressbar"
														 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
														 style="width: 85%;">
														<span>执行完成</span>
													</div> -->
												</div>
									        </td>
											
											<td class="clusterTestOpr" style="width:20%;text-indent:20px">
												<a id="${node.metadata.name }"  nodename="${node.metadata.name }"
													onclick="detail(this)" title="查看详细信息">
													
												</a>
											</td>
											<td class="clusterTestOprBtns">
												<a><i>部署</i></a>
												<a><i>执行</i></a>
												<a><i>清理部署</i></a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						
					</div>
					<!-- <div class="caption clearfix"
						style="width: 75%; float: left; height: 100%;">
						<div>
							<input id="selectitem" type="checkbox" value="">检测项
						</div>
						<div>
						    <input name="item" type="checkbox" value="pingitem">
							ping <input type="text" id="pingip" placeholder="主机IP"
								value="192.168.0.75" /> 平均响应时间小于:<input type="number"
								value="10" class="number" min="1" autocomplete="off" max=""
								placeholder="1" id="pingtime"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								name="instanceNum"><span class="s-unit">ms</span>
						</div>
						<div>
						<input name="item" type="checkbox" value="traceitem">
							time tracepath <input type="text" id="tracepathip"
								placeholder="主机IP" value="192.168.0.75" /> 平均响应时间小于:<input
								type="number" value="2" class="number" min="1"
								autocomplete="off" max="" placeholder="1" id="tracepathtime"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								name="instanceNum"><span class="s-unit">s</span>
						</div>
						<div>
						  <input name="item" type="checkbox" value="curlitem">
							time curl 响应时间小于:<input type="number" value="2" class="number"
								min="1" autocomplete="off" max="" placeholder="1" id="curltime"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								name="instanceNum"><span class="s-unit">s</span>
						</div>
						<div>
						   <input name="item" type="checkbox" value="qperfitem">
							qperf 带宽大于 <input type="number" value="2000" class="number"
								min="1" autocomplete="off" max="" placeholder="1" id="qperf"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								name="instanceNum"><span class="s-unit">MB</span> 延迟小于<input
								type="number" value="10" class="number" min="1"
								autocomplete="off" max="" placeholder="1" id="qperftime"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								name="instanceNum"><span class="s-unit">ms</span>
						</div>
						<div>
						   <input name="item" type="checkbox" value="dockeritem">
							docker 磁盘空间
							<input type="number" value="15" class="number" min="1"
								autocomplete="off" max="" placeholder="1" id="docker"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								name="instanceNum"><span class="s-unit">GB</span>
						</div>
						<div>
						   <input name="item" type="checkbox" value="dnsitem">
							dns
						</div>
					</div> -->
					<input type="hidden" id="deployednodes" value="${deployedpod}">
					
				</div>

                <div id="chkitem" style="display: none; text-align: center">
					<table class="table clusterTestTable" style="width: 580px; padding: 5px; margin: 10px">
						<tbody>
							<tr>
								<th colspan="4" style="padding-left:5px">
									<label style="float:left"><input id="selectitem" type="checkbox" value="" style="float:left">全选</label>
								</th>
							</tr>
							<tr>
								<th style="width: 20%">
									<input name="item" type="checkbox" value="pingitem">&nbsp;ping地址：
								</th>
								<td>
									<input type="text" id="pingip" placeholder="主机IP" value="192.168.0.75" />
								</td>
								<th style="width: 20%">平均响应时间：</th>
								<td>
									<input type="number" style="width: 80%"
									value="10" class="number" min="1" autocomplete="off" max=""
									placeholder="1" id="pingtime"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									name="instanceNum"><span class="s-unit">ms</span>
								</td>
							</tr>
							<tr>
								<th style="width: 20%"><input name="item" type="checkbox" value="traceitem">&nbsp;trace地址：</th>
								<td>
									<input type="text" id="tracepathip" placeholder="主机IP" value="192.168.0.75" />
								</td>
								<th style="width: 20%">平均响应时间：</th>
								<td>
									<input style="width: 80%"
									type="number" value="2" class="number" min="1"
									autocomplete="off" max="" placeholder="1" id="tracepathtime"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									name="instanceNum"><span class="s-unit">s</span>
								</td>
							</tr>
							<tr>
								<th style="width: 20%">
									<input name="item" type="checkbox" value="curlitem">&nbsp;curl响应时间：
								</th>
								<td colspan="3" style="width: 80%">
									<input type="number" style="width: 80%;" value="2" class="number"
									min="1" autocomplete="off" max="" placeholder="1" id="curltime"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									name="instanceNum"><span class="s-unit">s</span>
								</td>
							</tr>
							<tr>
								<th style="width: 20%">
									<input name="item" type="checkbox" value="qperfitem">&nbsp;qperf带宽：
								</th>
								<td>
									<input style="width: 80%" type="number" value="2000" class="number"
											min="1" autocomplete="off" max="" placeholder="1" id="qperf"
											onkeyup="this.value=this.value.replace(/\D/g,'')"
											name="instanceNum"><span class="s-unit">MB</span>
								</td>
								<th style="width: 20%">延迟：</th>
								<td>
									<input style="width: 80%"
										type="number" value="10" class="number" min="1"
										autocomplete="off" max="" placeholder="1" id="qperftime"
										onkeyup="this.value=this.value.replace(/\D/g,'')"
										name="instanceNum"><span class="s-unit">ms</span>
								</td>
								</tr>
								<tr>
									<th style="width: 25%">
										<input name="item" type="checkbox" value="dockeritem">&nbsp;docker磁盘大小：
									</th>
									<td colspan="3" style="width: 75%">
										<input style="width: 80%" type="number" value="15" class="number" min="1"
											autocomplete="off" max="" placeholder="1" id="docker"
											onkeyup="this.value=this.value.replace(/\D/g,'')"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th style="width: 25%">
										<input name="item" type="checkbox" value="dockeritem">&nbsp;docker磁盘大小：
									</th>
									<td colspan="3" style="width: 75%">
										<input style="width: 80%" type="number" value="15" class="number" min="1"
											autocomplete="off" max="" placeholder="1" id="docker"
											onkeyup="this.value=this.value.replace(/\D/g,'')"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th colspan="4" style="width: 25%">
										<input name="item" type="checkbox" value="dnsitem">&nbsp;dns
									</th>
								</tr>
						</tbody>
					</table>
                </div>

				<div id="detail" style="display: none; text-align: center">
					<ul id="myTab" class="nav nav-tabs">
						<li class="active"><a href="#pingTab" data-toggle="tab">Ping</a></li>
						<li><a href="#traceTab" data-toggle="tab">Trace</a></li>
						<li><a href="#qperfTab" data-toggle="tab">Qperf</a></li>
						<li><a href="#curlTab" data-toggle="tab">Curl</a></li>
						<li><a href="#dockerTab" data-toggle="tab">Docker</a></li>
						<li><a href="#dnsTab" data-toggle="tab">Dns</a></li>
					</ul>
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="pingTab">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="pingTable">
									<tr>
										<th style="width: 25%">ping操作：</th>
										<td><input class="" type="text" id="pingstatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">检测状态：</th>
										<td><input class="" type="text" id="pingpass" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">平均响应时间：</th>
										<td><input class="" type="text" id="pingavg" readOnly
											value=""></td>
									</tr>
									<tr>
										<td colspan="2"><textarea id="pingdetail"
												style="width: 100%; height: 100px;"></textarea></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="tab-pane fade" id="traceTab">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="traceTable">
									<tr>
										<th style="width: 25%">trace操作：</th>
										<td><input class="" type="text" id="tracestatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">检测状态：</th>
										<td><input class="" type="text" id="tracepass" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">平均响应时间：</th>
										<td><input class="" type="text" id="tracetime" readOnly
											value=""></td>
									</tr>
									<tr>
										<td colspan="2"><textarea id="tracedetail"
												style="width: 100%; height: 100px;"></textarea></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="tab-pane fade" id="qperfTab">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="qperfTable">
									<tr>
										<th style="width: 25%">qperf操作：</th>
										<td><input class="" type="text" id="qperfstatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">检测状态：</th>
										<td><input class="" type="text" id="qperfpass" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">带宽：</th>
										<td><input class="" type="text" id="speed" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">延迟：</th>
										<td><input class="" type="text" id="latency" readOnly
											value=""></td>
									</tr>
									<tr>
										<td colspan="2"><textarea id="qperfdetail"
												style="width: 100%; height: 100px;"></textarea></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="tab-pane fade" id="curlTab">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="curlTable">
									<tr>
										<th style="width: 25%">curl操作：</th>
										<td><input class="" type="text" id="curlstatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">检测状态：</th>
										<td><input class="" type="text" id="curlpass" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">响应时间：</th>
										<td><input class="" type="text" id="curlavg" readOnly
											value=""></td>
									</tr>
									<tr>
										<td colspan="2"><textarea id="curldetail"
												style="width: 100%; height: 100px;"></textarea></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="tab-pane fade" id="dockerTab">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="dockerTable">
									<tr>
										<th style="width: 25%">cpu：</th>
										<td><input class="" type="text" id="cpu" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">memory：</th>
										<td><input class="" type="text" id="memory" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">检测状态：</th>
										<td><input class="" type="text" id="dockerpass" readOnly
											value=""></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="tab-pane fade" id="dnsTab">
							<table class="table enabled"
								style="width: 580px; padding: 5px; margin: 10px">
								<tbody class="curlTable">
								    <tr>
										<th style="width: 25%">检测状态：</th>
										<td><input class="" type="text" id="dnspass" readOnly
											value=""></td>
									</tr>
									<tr>
										<th style="width: 25%">主dns操作：</th>
										<td><input class="" type="text" id="masterstatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<td colspan="2"><textarea id="masterdetail"
												style="width: 100%; height: 100px;"></textarea></td>
									</tr>
									<tr>
										<th style="width: 25%">备dns操作：</th>
										<td><input class="" type="text" id="standbystatus" readOnly
											value=""></td>
									</tr>
									<tr>
										<td colspan="2"><textarea id="standbydetail"
												style="width: 100%; height: 100px;"></textarea></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>
	<script type="text/javascript">
	$('.dataTables-example').dataTable({
	    "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,3] }],
	    //"searching":false
	    //"aaSorting": [[ 2, "desc" ]]
	}); 
	$("#checkallbox").parent().removeClass("sorting_asc");
</script>
</body>
</html>
