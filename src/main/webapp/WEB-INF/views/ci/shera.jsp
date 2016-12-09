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
	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="user" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">shera管理</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
                    <div class="col-md-12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>shera管理
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="userReloadBtn"><i class="fa fa-repeat" title="刷新"></i></a> 
										<a id="createSheraBtn" title="创建用户"><i
											class="fa fa-plus"></i></a> 
										<a href="javascript:delShera()" title="删除"><i
											class="fa fa-trash"></i></a>
									</div>
                            </div>
                            <div class="ibox-content">
                                <table class="table table-striped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" class="chkAll" id="checkallbox" /></th>
												<th style="width: 10%;">IP</th>
												<th style="width: 20%;">账号</th>
												<th style="width: 20%;">密码</th>
												<th style="width: 20%;">描述</th>
												<th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="secretKeyList">

											<tr class="userTr" id="${key.id }">
												<td style="width: 5%; text-indent: 30px;"><input
													type="checkbox" class="chkItem" name="ids"
													value="${key.id }"></td>
												<td style="width: 10%;cursor:pointer"><a title="查看详细信息" onclick="sheraDetail(this)" keyName="name"
													onmousemove="style.textDecoration='underline'"
													onmouseout="style.textDecoration='none'">ip</a></td>
												<td style="width: 20%;">name</td>
												<td style="width: 20%;">password</td>
												<td style="width: 20%;">remark</td>
												<td style="width: 10%;"><a id="deleteKeyBtn"
													class="no-drop"
													href="javascript:delOneTenement(${key.id })"
													style="margin-left: 10px"> <i class="fa fa-trash"></i>
												</a></td>
											</tr>

										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="6">
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
				<span class="labelCred">IP：</span> <input type="text"
					class="form-control conCred" id="sheraIp" name="sheraIp"
					value="">
			</div>
			<div class="infoCred">
				<span class="labelCred">账号：</span> <input type="text"
					class="form-control conCred" id="shreaName" name="shreaName"
					value="">
			</div>
			<div class="infoCred">
				<span class="labelCred">密码：</span> <input type="password"
					class="form-control conCred" id="shreaPassword" name="shreaPassword"
					value="">
			</div>
			<div class="infoCred">
				<span class="labelCred">JDK：</span>
				<table class="table enabled conCred jdkCon">
					<thead>
						<tr>
							<th style="width: 35%">name</th>
							<th style="width: 35%">path</th>
							<th style="vertical-align: middle; width: 10%">操作</th>
						</tr>
					</thead>
					<tbody class="jdktbody">
						<tr class="plus-row">
							<td><input class="jdkName" type="text" value="home"></td>
							<td><input class="jdkPath" type="text" value="/opt"></td>
							<td><a
								onclick="deleteRow(this)" class="gray"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a></td>
						</tr>
					</tbody>
				</table>
				<div class="createjdk" style="background: #fafafa">
					<span id="createjdk"><i class="fa fa-plus margin"></i>添加JDK</span>
				</div>
				
			</div>
			<br>
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
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,5] }]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		
		
	});
	</script>
</body>
</html>