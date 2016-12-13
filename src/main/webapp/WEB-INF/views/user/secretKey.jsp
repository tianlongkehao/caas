<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>密钥</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/secretKey.css" />
<script type="text/javascript" src="<%=path%>/js/user/secretKey.js"></script>
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
						<li class="active" id="nav2">密钥管理</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="row">
                    <div class="col-md-12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>
										<i class="fa fa-map-marker" style="margin-right: 6px;"></i>密钥管理
									</h5>

									<div class="ibox-tools">
										<a href="javascript:window.location.reload(true);"
											id="userReloadBtn"><i class="fa fa-repeat" title="刷新"></i></a> 
										<a id="createKeyBtn" title="创建用户"><i
											class="fa fa-plus"></i></a> 
										<a href="javascript:delSecretKey()" title="删除"><i
											class="fa fa-trash"></i></a>
									</div>
                            </div>
                            <div class="ibox-content">
                                <table class="table table-striped table-hover dataTables-example">
										<thead>
											<tr>
												<th style="width: 5%; text-indent: 30px;"><input
													type="checkbox" class="chkAll" id="checkallbox" /></th>
												<th style="width: 15%;">名称</th>
												<th style="width: 10%;">类型</th>
												<th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="secretKeyList">

											<tr class="userTr" id="${key.id }">
												<td style="width: 5%; text-indent: 30px;"><input
													type="checkbox" class="chkItem" name="ids"
													value="${key.id }"></td>
												<td style="width: 15%;cursor:pointer"><a title="查看详细信息" onclick="keyDetail(this)" keyName="name"
													onmousemove="style.textDecoration='underline'"
													onmouseout="style.textDecoration='none'">name</a></td>

												<td style="width: 10%; text-indent: 0;"
													id="key-type" name="key-type"><input
													type="hidden" id="key-type-hidden"
													value="http">http <select
													class="hidden" id="secretType" name="secretType">
														<option value="1">HTTP</option>
														<option value="2">SSH</option>
												</select></td>
												<td style="width: 10%;"><a id="deleteKeyBtn"
													class="no-drop"
													href="javascript:delOneSecretKey(${key.id })"
													style="margin-left: 10px"> <i class="fa fa-trash"></i>
												</a></td>
											</tr>

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
	<div id="createKeyCon" style="display: none">
		<div style="width:345px;margin: 15px 15px">
			<div class="infoCred">
				<span class="labelCred">类型：</span> <select
					class="form-control conCred" id="CredentialsType" name="type">
					<option value="1">用户名和密码</option>
					<option value="2">SSH用户名和密钥</option>
				</select>
			</div>
			<div class="infoCred">
				<span class="labelCred">用户名：</span> <input type="text"
					class="form-control conCred" id="userNameCred" name="userName"
					value="">
			</div>
			<div class="infoCred normal">
				<span class="labelCred">密码：</span> <input type="password"
					class="form-control conCred" id="passwordCred" name="password"
					value="">
			</div>
			<div class="infoCred ssh">
				<span class="labelCred">密钥：</span>
				<textarea type="text" class="form-control conCred" style="height:100px"
					id="SSHpasswordCred" name="privateKey" row="8" value=""></textarea>
			</div>
			<div class="infoCred">
				<span class="labelCred">描述：</span>
				<textarea type="text" class="form-control conCred" style="height:100px"
					id="keyRemark" name="keyRemark" row="8" value=""></textarea>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		$('.dataTables-example').dataTable({
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,3] }]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		
		
	});
	</script>
</body>
</html>
