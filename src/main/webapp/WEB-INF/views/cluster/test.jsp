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
					<!-- <div class="clusterTestBtns hide">
						<span id="deployBtn" class="btn btn-primary btn-color pull-left"
							style="margin-right: 10px; cursor: pointer">部署</span>
						<span id="excuteBtn" class="btn btn-primary btn-color pull-left"
							style="margin-right: 10px; cursor: pointer">执行</span>
						<span id="deleteBtn" class="btn btn-primary btn-color pull-left"
							style="margin-right: 10px; cursor: pointer">清除部署</span>
					</div> -->

					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>集群测试
									</h5>
									<div class="ibox-tools">
										<a id="deployBtn" style="cursor: pointer"><i>部署</i></a>
										<a onclick="testNodes()" style="cursor: pointer"><i>执行</i></a>
										<a id="deleteBtn" style="cursor: pointer"><i>清除部署</i></a>
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
										<th style="width:12%;">集群节点</th>
										<th style="width:70%;">测试进度</th>
										<th style="width:12%;text-indent:20px">测试结果</th>
									</tr>
								</thead>
								<tbody id="routeList">
									<tr>
										<td>数据加载中...</td>
									</tr>
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
					<table class="table clusterTestTable" style="width: 570px; padding: 5px; margin: 10px">
						<tbody>
							<tr>
								<th colspan="2" style="padding-left:5px">
									<label style="float:left"><input id="selectitem" type="checkbox" value="" style="float:left">全选</label>
								</th>
							</tr>
							<tr>
								<th class="testItemTit">
									<input name="item" class="checkItem" type="checkbox" value="pingitem" id="ping">&nbsp;ping地址：
								</th>
								<td>
									<input type="text" style="width: 80%" id="pingip" placeholder="主机IP" value="" />
								</td>
							</tr>
							<tr>
								<th class="testItemTit testItemTitNoCheckbox">平均响应时间：</th>
								<td>
									<input type="number" class="testItemCon"
									value="10" class="number" min="1" autocomplete="off" max=""
									 id="pingtime"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									name="instanceNum"><span class="s-unit">ms</span>
								</td>
							</tr>
							<tr>
								<th class="testItemTit"><input name="item" class="checkItem" type="checkbox" value="traceitem" id="trace">&nbsp;trace地址：</th>
								<td>
									<input type="text" class="testItemCon" id="tracepathip" placeholder="主机IP" value="" />
								</td>
							</tr>
							<tr>
								<th class="testItemTit testItemTitNoCheckbox">平均响应时间：</th>
								<td>
									<input type="number" value="2" class="number testItemCon" min="1"
									autocomplete="off" max=""  id="tracepathtime"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									name="instanceNum"><span class="s-unit">s</span>
								</td>
							</tr>
							<tr>
								<th class="testItemTit">
									<input name="item" class="checkItem" type="checkbox" value="curlitem" id="curl">&nbsp;curl响应时间：
								</th>
								<td style="width: 80%">
									<input type="number" value="2" class="number testItemCon"
									min="1" autocomplete="off" max=""  id="curltime"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									name="instanceNum"><span class="s-unit">s</span>
								</td>
							</tr>
							<tr>
								<th class="testItemTit">
									<input name="item" class="checkItem" type="checkbox" value="qperfitem" id="qperf">&nbsp;qperf带宽：
								</th>
								<td>
									<input type="number" value="2000" class="number testItemCon"
											min="1" autocomplete="off" max=""  id="speed"
											onkeyup="this.value=this.value.replace(/\D/g,'')"
											name="instanceNum"><span class="s-unit">MB</span>
								</td>
							</tr>
							<tr>
								<th class="testItemTit testItemTitNoCheckbox">延迟：</th>
								<td>
									<input type="number" value="10" class="number testItemCon" min="1"
										autocomplete="off" max=""  id="qperftime"
										onkeyup="this.value=this.value.replace(/\D/g,'')"
										name="instanceNum"><span class="s-unit">ms</span>
								</td>
							</tr>
								<tr>
									<th colspan="2" class="testItemTit">
										<input name="item" class="checkItem" type="checkbox" value="dockeritem" id="docker">&nbsp;Docker
									</th>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Pool Blocksize：</th>
									<td>
										<input type="number" value="60" class="number testItemCon" min="1"
											autocomplete="off" max="" id="PoolBlocksizeTarget"
											onkeyup="this.value=this.value.replace(/\D/g,'')"
											name="instanceNum"><span class="s-unit">KB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Base Device Size：</th>
									<td>
										<input type="number" value="100" class="number testItemCon" min="1"
											autocomplete="off" max="" id="BaseDeviceSizeTarget"
											onkeyup="this.value=this.value.replace(/\D/g,'')"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Backing Filesystem：</th>
									<td>
										<input type="text" value="xfs" class="number testItemCon" min="1"
											autocomplete="off" max="" id="BackingFilesystemTarget"
											name="instanceNum">
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Data file：</th>
									<td>
										<input type="text" value="/dev/loop0" class="number testItemCon" min="1"
											autocomplete="off" max="" id="DatafileTarget"
											name="instanceNum">
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Data Space Used：</th>
									<td>
										<input type="number" value="0" class="number testItemCon" min="1"
											autocomplete="off" max="" id="DataSpaceUsedTarget"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Data Space Total：</th>
									<td>
										<input type="number" value="100" class="number testItemCon" min="1"
											autocomplete="off" max="" id="DataSpaceTotalTarget"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Data Space Available：</th>
									<td>
										<input type="number" value="100" class="number testItemCon" min="1"
											autocomplete="off" max="" id="DataSpaceAvailableTarget"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Metadata file：</th>
									<td>
										<input type="text" value="/dev/loop1" class="number testItemCon" min="1"
											autocomplete="off" max="" id="MetadatafileTarget"
											name="instanceNum">
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Meta Space Used：</th>
									<td>
										<input type="number" value="0" class="number testItemCon" min="1"
											autocomplete="off" max="" id="MetaSpaceUsedTarget"
											name="instanceNum"><span class="s-unit">MB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Meta Space Total：</th>
									<td>
										<input type="number" value="2" class="number testItemCon" min="1"
											autocomplete="off" max="" id="MetaSpaceTotalTarget"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Meta Space Available：</th>
									<td>
										<input type="number" value="2" class="number testItemCon" min="1"
											autocomplete="off" max="" id="MetaSpaceAvailableTarget"
											name="instanceNum"><span class="s-unit">GB</span>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Deferred Removal Enable：</th>
									<td>
										<select id="DeferredRemovalEnableTarget" class="testItemCon">
											<option value="false">false</option>
											<option value="true">true</option>
										</select>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Udev Sync Supported：</th>
									<td>
										<select id="UdevSyncSupportedTarget" class="testItemCon">
											<option value="false">false</option>
											<option value="true">true</option>
										</select>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Deferred Deletion Enable：</th>
									<td>
										<select id="DeferredDeletionEnableTarget" class="testItemCon">
											<option value="false">false</option>
											<option value="true">true</option>
										</select>
									</td>
								</tr>
								<tr>
									<th class="testItemTit testItemTitNoCheckbox">Deferred Deleted Device Count：</th>
									<td>
										<input type="number" value="0" class="number testItemCon" min="1"
											autocomplete="off" max="" id="DeferredDeletedDeviceCountTarget"
											name="instanceNum">
									</td>
								</tr>
							<tr>
								<th colspan="2" style="width: 25%">
									<input name="item" class="checkItem" type="checkbox" value="dnsitem" id="dns">&nbsp;dns
								</th>
							</tr>
						</tbody>
					</table>
                </div>

				<div id="detail" style="display: none; text-align: center">
					<ul id="myTab" class="nav nav-tabs">
					</ul>
					<div id="myTabContent" class="tab-content">
					</div>
				</div>
			</div>
		</article>
	</div>
<script type="text/javascript">
	
</script>
</body>
</html>
