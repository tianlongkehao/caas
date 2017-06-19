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
										<a title="创建dns" onclick="createDns()"><i
											class="fa fa-plus"></i></a>
										<a title="创建定时监控任务监控" onclick="createDNSMonitor()"><i
											class="fa fa-history"></i></a>
										<a title="定时日志" onclick="dnsHistory()"><i
											class="fa fa-file-text-o"></i></a>
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
										<th style="text-indent:10px">域名</th>
										<th>解析IP</th>
										<th>创建时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="dnsList">
									
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
		<select class="form-control" style="width:80%" id="dnsTime">
			<option value="0">定时检查时间间隔</option>
			<option value="5">5m</option>
			<option value="10">10m</option>
			<option value="30">30m</option>
			<option value="60">60m</option>
		</select></div>
		<div style="margin-top:10px">
			<span style="width:15%;float:left;line-height:34px;">服务域名：</span>
			<div class="domainTable" style="width:80%;height:300px">
				<table class="table table-bordered table-hover table-striped" style="width:100%">
					<tbody id="timeTaskList">
						
					</tbody>
					<!-- <tfoot>
						<tr onclick="addDnsHost(this)"><td><i class="fa fa-plus"></i>添加</td></tr>
					</tfoot> -->
				</table>
			</div>
			<span><i class="fa fa-warning" style="color:#e8504f;margin-top:15px;margin-left:15%"></i>最多选5个定时检查的域名</span>
		</div>
	</div>
</div>
<div class="createDnsInfo" style="display:none">
	<div style="padding:15px">
		<!-- <div style="height:50px">
			<span style="width:15%;float:left;line-height:34px;">服务名称：</span>
			<input style="width:80%;float:left;" class="form-control" id="serviceName" placeholder="服务名必须是小写字母开头，且是小写字母加数字的4-20个字符组成" value="">
		</div> -->
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">域名：</span>
			<input style="width:80%;float:left;" class="form-control" id="address" placeholder="域名就是 网址+端口，例如:192.168.0.1:8080" value="">
		</div>
	</div>
</div>
<div class="dnsResultInfo" style="display:none">
	<div style="padding:15px">
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">服务名称：</span>
			<input style="width:80%;float:left;" class="form-control" id="serviceNameInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">域名：</span>
			<input style="width:80%;float:left;" class="form-control" id="addressInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">服务状态：</span>
			<input style="width:80%;float:left;" class="form-control" id="resultInfo" value="">
		</div>
		<div style="height:40px">
			<span style="width:15%;float:left;line-height:34px;">解析IP：</span>
			<input style="width:80%;float:left;" class="form-control" id="ipInfo" value="">
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
		<input type="hidden" id="thisDnsId" value="">
			<div id="hisrotyInfos" style="margin-top:15px;"></div>
		</div>
		
	</div>
</div>
<script type="text/javascript">
	
</script>

</body>
</html>
