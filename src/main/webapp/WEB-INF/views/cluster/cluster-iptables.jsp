<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster.js"></script>
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
                    <li class="active">iptables</li>
                </ol>
            </div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>iptables
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

					<div class="caption clearfix" style="padding-bottom: 0px">
						<ul class="toolbox clearfix hide">
							<li><a id="updateCluster" style="cursor: pointer"> <i
									class="fa fa-repeat"></i>
							</a></li>
						</ul>
						<form id="search_form" class="form-inline" action="" method="post">
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">服务:</label> <select
									name="search_service" id="search_service" onchange="searchService()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option value="">test11</option>
								</select>
							</div>
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">租户:</label> <select
									name="search_users" id="search_users" onchange="searchUsers()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option value="">testjiang</option>
								</select>
							</div>
							
						</form>
						<div class="" style="margin-bottom:15px">
							<table class="table table-striped table-hover sameTable">
								<thead>
									<tr><th colspan="3"><h4>iptables对比结构相同的节点：<h4></th></tr>
									<tr class="u-line">
										<th colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;节点IP:&nbsp;&nbsp;<span>192.168.0.10/192.168.0.11</span></th>
									</tr>
								</thead>
								<tbody id="sameTableList">
									<tr>
										<td rowspan="6">&nbsp;&nbsp;&nbsp;&nbsp;nat</td>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
									</tr>
									<tr class="u-line">
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
									</tr>
									
									<tr>
										<td rowspan="3">&nbsp;&nbsp;&nbsp;&nbsp;filter</td>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
									</tr>
									
								</tbody>
							</table>
							<table class="table table-striped table-hover differentTable">
								<thead>
									<tr><th colspan="3"><h4>iptables对比结构不同之处：<h4></th></tr>
									<tr class="u-line">
										<th>&nbsp;</th>
										<th>192.168.0.10</th>
										<th>192.168.0.11</th>
									</tr>
								</thead>
								<tbody id="routeList">
									<tr>
										<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;nat</td>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
										
									</tr>
									<tr class="u-line">
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
										<td>:PREROUTING ACCEPT [291303:18397761]</td>
										
									</tr>
									<tr>
										<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;filter</td>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
										<td>:PREROUTING ACCEPT [291303:18397761]2222</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
										<td>:PREROUTING ACCEPT [291303:18397761]2222</td>
									</tr>
								</tbody>
							</table>
						</div>
						
					</div>
					

				</div>
			</div>
    </article>
</div>


</body>
</html>
