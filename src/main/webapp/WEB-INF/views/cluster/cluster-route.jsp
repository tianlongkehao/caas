<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster-route.js"></script>
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
                    <li class="active">路由监控</li>
                </ol>
            </div>
				<div class="contentMain">
					<div class="row">
						<div class="col-md-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>路由监控
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
								<label style="line-height: 35px">node:</label> <select
									name="search_routeNode" id="search_routeNode" onchange="searchRouteNode()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<c:forEach items="${nodeList }" var="nodeList">
										<option value="${nodeList.nodeIp }">${nodeList.nodeName }</option>
									</c:forEach>
								</select>
							</div>
						</form>
						<div class="" style="margin-bottom:15px">
							<table class="table table-striped table-hover dataTables-example">
								<thead>
									<tr>
										<th>&nbsp;</th>
										<th>targetIP</th>
										<th>是否成功</th>
									</tr>
								</thead>
								<tbody id="routeList">
									
								</tbody>
								<!-- <tfoot class="hide">
									<tr>
										<td colspan="3">
											<ul class="pagination pull-right"></ul>
										</td>
									</tr>
								</tfoot> -->
							</table>
						</div>
						
					</div>
					

				</div>
			</div>
    </article>
</div>
<script type="text/javascript">
	/* $('.dataTables-example').dataTable({
	    "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0] }],
	    "searching":false
	    //"aaSorting": [[ 2, "desc" ]]
	}); */
	//$("#checkallbox").parent().removeClass("sorting_asc");
</script>

</body>
</html>
