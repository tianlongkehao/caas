<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster-dns.js"></script>
</head>

<body>
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="cluster" value=""/>
</jsp:include>
<input type="hidden" id="checkedHosts">

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active">集群DNS</li>
                </ol>
            </div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>集群DNS
									</h5>
									<div class="ibox-tools">
										<a title="创建监控" onclick="createDNSMonitor()"><i
											class="fa fa-plus"></i></a>
										<a title="定时" onclick="timedTask()"><i
											class="fa fa-clock-o"></i></a> 
										<a title="定时日志" onclick="timedLog()"><i
											class="fa fa-history"></i></a> 
										<a title="删除" onclick="delDns()"><i
											class="fa fa-trash"></i></a>
										<a href="javascript:window.location.reload(true);" title="刷新"><i
											class="fa fa-repeat"></i></a>
									</div>
								</div>

							</div>
						</div>
					</div>
					<div class="ibox-content" style="padding: 0px"></div>

					<div class="caption clearfix" style="padding-bottom: 0px">
						<ul class="toolbox clearfix hide">
							<li><a id="updateCluster" style="cursor: pointer"> <i
									class="fa fa-repeat"></i>
							</a></li>
						</ul>
						
						<div class="" style="margin-bottom:15px">
							<table class="table table-striped table-hover dataTables-example">
								<thead>
									<tr>	
										<th style="width:5%;text-indent:20px">
											<input type="checkbox" class="chkAll" id="checkallbox" />
										</th>
										<th style="text-indent:10px">服务名称</th>
										<th style="text-indent:10px">域名</th>
										<th>解析IP</th>
										<th>创建时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="dnsList">
									<c:forEach items="${DNSServiceList }" var="dns">
										<tr>
											<td style="width:5%;text-indent:20px">
												<input class="chkItem" type="checkbox" id="${dns.id }">
											</td>
											<td style="text-indent:10px"><a class="link" onclick="getDnsResult(this,${dns.id })" serviceName="${dns.serviceName }">${dns.serviceName }</a></td>
											<td style="text-indent:10px">${dns.address }</td>
											<td></td>
											<td>${dns.createDate }</td>
											<td><a onclick="delOneDns(${dns.id })"><i class="fa fa-trash"></i></a>
											<a onclick="dnsOneHistory(${dns.id })"><i class="fa fa-history"></i></a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						
					</div>
					

				</div>
			</div>
    </article>
</div>
<div class="timedTaskInfo" style="display:none">
	<div style="padding:15px">
		<div><span style="width:15%;float:left;line-height:34px;">时间间隔：</span>
		<select class="form-control" style="width:80%">
			<option value="0">定时检查时间间隔</option>
			<option value="5">5m</option>
			<option value="10">10m</option>
			<option value="30">30m</option>
			<option value="60">60m</option>
		</select></div>
		<div style="margin-top:10px">
			<span style="width:15%;float:left;line-height:34px;">服务域名：</span>
			<div class="domainTable" style="width:80%">
				<table class="table table-bordered table-hover table-striped" style="width:100%">
					<tr onclick="checkServerDomain(this)"><td>dsdsfsfsfsfd<i class="fa fa-check" domain="dsdsfsfsfsfd"></i></td></tr>
					<tr onclick="checkServerDomain(this)"><td>1111112222<i class="fa fa-check" domain="1111112222"></i></td></tr>
					<tr onclick="checkServerDomain(this)"><td>dsdsfsfsfsfd<i class="fa fa-check" domain="dsdsfsfsfsfd"></i></td></tr>
					<tr onclick="checkServerDomain(this)"><td>1111112222<i class="fa fa-check" domain="1111112222"></i></td></tr>
					<tr onclick="checkServerDomain(this)"><td>dsdsfsfsfsfd<i class="fa fa-check" domain="dsdsfsfsfsfd"></i></td></tr>
					<tr onclick="checkServerDomain(this)"><td>1111112222<i class="fa fa-check" domain="1111112222"></i></td></tr>
				</table>
			</div>
			<span><i class="fa fa-warning" style="color:#e8504f;margin-top:15px;margin-left:15%"></i>最多选5个定时检查的域名</span>
		</div>
	</div>
</div>
<div class="createDnsInfo" style="display:none">
	<div style="padding:15px">
		<div style="height:50px">
			<span style="width:15%;float:left;line-height:34px;">服务名称：</span>
			<input style="width:80%;float:left;" class="form-control" id="serviceName" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">域名：</span>
			<input style="width:80%;float:left;" class="form-control" id="address" value="">
		</div>
	</div>
</div>
<div class="dnsResultInfo" style="display:none">
	<div style="padding:15px">
		<div style="height:50px">
			<span style="width:15%;float:left;line-height:34px;">服务名称：</span>
			<input style="width:80%;float:left;" class="form-control" id="serviceNameInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">域名：</span>
			<input style="width:80%;float:left;" class="form-control" id="addressInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">解析结果：</span>
			<input style="width:80%;float:left;" class="form-control" id="resultInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">解析IP：</span>
			<input style="width:80%;float:left;" class="form-control" id="ipInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">时间间隔：</span>
			<input style="width:80%;float:left;" class="form-control" id="timeInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">日志：</span>
			<textarea style="width:80%;float:left;" class="form-control" id="logInfo" value="" rows="4"></textarea>
		</div>
	</div>
</div>
<div class="dnshistoryInfo" style="display:none">
	<div style="padding:15px">
		<div style="height:50px">
			<p><span style="width:15%;float:left;line-height:34px;">时间间隔：</span>
				<select>
					<option>1m</option>
					<option>3m</option>
					<option>5m</option>
					<option>10m</option>
				</select>
			</p>
			<div id="hisrotyInfos"></div>
		</div>
		
	</div>
</div>
<script type="text/javascript">
	$('.dataTables-example').dataTable({
	    "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,5] }],
	    //"searching":false
	    "aaSorting": [[ 4, "desc" ]]
	}); 
	$("#checkallbox").parent().removeClass("sorting_asc");
</script>

</body>
</html>
