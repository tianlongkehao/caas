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
										<a title="定时" onclick="timedTask()"><i
											class="fa fa-clock-o"></i></a> 
										<a title="定时日志" onclick="timedLog()"><i
											class="fa fa-history"></i></a> 
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
										<th style="text-indent:10px">域名</th>
										<th>解析IP</th>
									</tr>
								</thead>
								<tbody id="routeList">
									<tr>
										<td style="text-indent:10px">portal.clyxys.svc.cluster.local</td>
										<td>192.168.0.1</td>
									</tr>
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
<script type="text/javascript">
	$('.dataTables-example').dataTable({
	    //"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0,3] }],
	    //"searching":false
	    //"aaSorting": [[ 2, "desc" ]]
	}); 
	//$("#checkallbox").parent().removeClass("sorting_asc");
</script>

</body>
</html>
