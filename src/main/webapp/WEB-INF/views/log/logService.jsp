<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>日志</title>
    <%@include file="../frame/header.jsp"%>
   	<script type="text/javascript" src="<%=path%>/js/log/logService.js"></script>
</head>
<body>
<jsp:include page="../frame/bcm-menu.jsp" flush="true">
    <jsp:param name="template" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="<%=path %>/home"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" style="width:110px">服务操作日志</li>
                </ol>
            </div>
            <div class="contentMain">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>服务操作日志
									</h5>
									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
									</div>
								</div>
								<div class="ibox-content">
									<table class="table table-striped table-hover dataTables-example" data-page-size="8"
										data-filter=#filter style="margin-left: 30px">
										<thead>
											<tr>
												<th style="width: 5%;">&nbsp;</th>
												<th style="width: 13%;">操作人</th>
												<th style="width: 45%;">操作内容</th>
	                                            <th style="width: 20%;">操作类型</th>
	                                            <th style="width: 15%;">操作时间</th>
											</tr>
										</thead>
										<tbody id="logServiceList">
									<%-- 		  <c:forEach items = "${logServiceList }" var = "logService">
													<tr>
														<td style="width: 13%;text-indent:10px;">${logService.createUserName }</td>
														<td style="width: 50%">${logService.serviceName }</td>
														<td style="width: 20%">${logServiceOprationMaps[logService.operationType]}</td>
			                                            <td style="width: 15%;height:40px;">
			                                                 <fmt:formatDate value="${logService.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                                                </td>
													</tr>
											  </c:forEach> --%>
										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="3">
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
</body>
</html>