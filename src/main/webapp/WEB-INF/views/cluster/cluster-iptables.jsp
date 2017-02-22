<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>集群</title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/cluster.css"/>
    <script type="text/javascript" src="<%=path %>/js/cluster/cluster-iptable.js"></script>
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
						<input type="hidden" id="userAutority" value="${cur_user.user_autority}">
						<input type="hidden" id="userName" value="${cur_user.userName }">
						<form >
							<c:if test="${cur_user.user_autority == 1}">
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">服务:</label> <select
									name="search_service" id="search_service" onchange="searchServiceShowIptables()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option value="">-----请选择-----</option>
                                    
								</select>
							</div>
							
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">租户:</label> <select
									name="search_users" id="search_users" onchange="changeNamespace()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
                                    <option value="noNamespace">-----请选择-----</option>
                                    <c:forEach items="${users}" var="user">
                                    	<option value="${user.namespace }">${user.namespace }</option>
                                	</c:forEach>
                                    <%-- <c:forEach items=${users } var="user">
                                    	<option value="${user.namespace }">${user.namespace }</option>
                                    </c:forEach> --%> 
								</select>
							</div>
							</c:if>
							
							<c:if test="${cur_user.user_autority != 1}">
							<div class="searchFun"
								style="float: left; text-align: center; margin: 0px 10px; float: right"
								align="right">
								<label style="line-height: 35px">服务:</label> <select
									name="search_service" id="search_service" onchange="searchServiceDiff()"
									style="height: 30px; display: inline; width: 140px; border-radius: 5px;">
									<option value="">-----请选择-----</option>
									<%-- <option value="">${user }</option> --%>
									
                                    <c:forEach items="${services }" var="service">
                                    	<option value="${service.metadata.name }">${service.metadata.name }</option>
                                    </c:forEach>
								</select>
							</div>
							</c:if>
						</form>
						<div class="" style="margin-bottom:15px">
							<table class="table table-hover sameTable">
								<thead>
									<tr><th colspan="3"><h4>iptables对比结果相同的节点：<h4></th></tr>
									<tr class="u-line">
										<th colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;节点IP:&nbsp;&nbsp;<span id="sameNodes">请选择需要对比的服务</span></th>
									</tr>
								</thead>
								<tbody id="sameTableList">
									<!-- <tr>
										<td rowspan="6">&nbsp;&nbsp;&nbsp;&nbsp;nat</td>
										<td>wwwwwwwwww</td>
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
									</tr> -->
									
									<!-- <tr>
										<td rowspan="3">&nbsp;&nbsp;&nbsp;&nbsp;filter</td>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
									</tr>
									<tr>
										<td>:PREROUTING ACCEPT [291303:18397761]222222</td>
									</tr> -->
									
								</tbody>
							</table>
							<div class="diffTitle"><span><h4>iptables对比结果不同之处：<h4></span></div>
							<div id="diffTable">
								<!-- <table class="table table-striped table-hover differentTable">
									<thead class="">
										<tr class="u-line">
											<th colspan="2">相对比的两个节点</th>
											<th>nodeName(192.168.0.10)</th>
											<th>nodeName(192.168.0.11)</th>
										</tr>
									</thead>
									<tbody id="routeList">
										<tr class="u-line">
											<td rowspan="5">&nbsp;&nbsp;&nbsp;&nbsp;nat</td>
											<td rowspan="3">kubesep</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										<tr class="u-line">
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										<tr class="u-line">
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										
										<tr class="u-line">
											<td rowspan="2">kubeServices</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										<tr class="u-line">
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										
										
									</tbody>
								</table> -->
								
								<!-- <table class="table table-striped table-hover differentTable">
									<thead>
										<tr class="u-line">
											<th colspan="2">相对比的两个节点</th>
											<th>nodeName(192.168.0.10)</th>
											<th>nodeName(192.168.0.11)</th>
										</tr>
									</thead>
									<tbody id="routeList">
										<tr class="u-line">
											<td rowspan="5">&nbsp;&nbsp;&nbsp;&nbsp;nat</td>
											<td rowspan="3">sep</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											
										</tr>
										<tr class="u-line">
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										<tr class="u-line">
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										
										<tr class="u-line">
											<td rowspan="2">services</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										<tr class="u-line">
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
											<td>:PREROUTING ACCEPT [291303:18397761]</td>
										</tr>
										
										
									</tbody>
								</table> -->
							</div>
						</div>
					</div>

				</div>
			</div>
    </article>
</div>


</body>
</html>
