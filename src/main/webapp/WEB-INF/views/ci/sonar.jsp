<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>shera</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<script type="text/javascript" src="<%=path%>/js/ci/shera.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="user" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/bcm/${cur_user.id }"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">sonar管理</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
                    <div class="col-md-12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>sonar管理
									</h5>

									<div class="ibox-tools">
										<a id="createSheraBtn" title="创建sonar"><i
											class="fa fa-plus"></i></a> 
										<a href="javascript:delSheras()" title="删除"><i
											class="fa fa-trash"></i></a>
										<a href="javascript:window.location.reload(true);"
											id="userReloadBtn"><i class="fa fa-repeat" title="刷新"></i></a> 
									</div>
                            </div>
                            <div class="ibox-content">
                                <table class="table table-striped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" class="chkAll" id="checkallbox" /></th>
												<th style="width: 40%;">网址</th>
												<th style="width: 40%;">描述</th>
												<th style="width: 8%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="secretKeyList">
										    <%-- <c:forEach items="${sonarList }" var="sonar"> --%>
												<tr class="userTr" id="${shera.id }">
													<td style="width: 5%; text-indent: 30px;"><input
														type="checkbox" class="chkItem" name="ids"
														value="${shera.id }"></td>
													<td style="width: 40%;cursor:pointer"><a title="查看详细信息" onclick="sheraDetail(${shera.id})" keyName="name"
														onmousemove="style.textDecoration='underline'"
														onmouseout="style.textDecoration='none'">dddd</a></td>
													<td style="width: 40%;">ddd</td>
													<td style="width: 8%;"><a id="deleteKeyBtn"
														class="no-drop"
														href="javascript:delOneTenement(${shera.id })"
														style="margin-left: 10px"> <i class="fa fa-trash"></i></a>
													</td>
												</tr>
                                            <%-- </c:forEach> --%>
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
	<div id="createSheraCon" style="display: none">
		<div style="margin: 15px 20px">
			<div class="infoCred">
				<span class="labelCred">网址：</span> <input type="text"
					class="form-control conCred" id="sheraIp" name="sheraIp" placeholder="192.168.0.76"
					value="">
			</div>
			<div class="infoCred">
                <span class="labelCred">描述：</span> 
                <textarea class="form-control conCred" style="height:100px"
                    id="sheraRemark" name="sheraRemark" row="8" value=""></textarea>
            </div>
		</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		$('.dataTables-example').dataTable({
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,6] }]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		
		
	});
	</script>
</body>
</html>
