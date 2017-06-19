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
						<li class="active">密钥管理</li>
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
										<a id="createKeyBtn" title="创建密钥"><i
											class="fa fa-plus"></i></a> 
										<a href="javascript:delSecretKey()" title="删除"><i
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
												<th style="width: 15%;">名称</th>
												<th style="width: 15%;text-indent:20px">仓库类型</th>
												<th style="width: 10%;">认证方式</th>
												<th style="width: 15%;">创建时间</th>
												<th style="width: 10%;" class="del-operation">操作</th>
											</tr>
										</thead>
										<tbody id="secretKeyList">
										    <c:forEach items="${creList }" var = "cre">
												<tr class="userTr" id="${cre.id }">
													<td style="width: 5%; text-indent: 30px;"><input
														type="checkbox" class="chkItem" name="ids"
														value="${cre.id }"></td>
													<td style="width: 15%;cursor:pointer"><a title="查看详细信息" onclick="keyDetail(this)" id = "${cre.id }" keyType="${cre.type }"
														onmousemove="style.textDecoration='underline'"
														onmouseout="style.textDecoration='none'">${cre.userName }  (${cre.remark })</a></td>
	                                                <c:if test="${cre.codeType == 1}">
                                                        <td style="width: 10%; text-indent: 20px;"
                                                            id="key-codeType" name="key-codeType">Git
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${cre.codeType == 2}">
                                                        <td style="width: 10%; text-indent: 20px;"
                                                            id="key-codeType" name="key-codeType">SVN
                                                        </td>
                                                    </c:if>
	                                                <c:if test="${cre.type == 1}">
														<td style="width: 10%;"
															id="key-type" name="key-type">HTTP
													    </td>
	                                                </c:if>
	                                                <c:if test="${cre.type == 2}">
                                                        <td style="width: 10%;"
                                                            id="key-type" name="key-type">SSH
                                                        </td>
                                                    </c:if>
                                                    <td style="width: 15%;">${cre.createDate }</td>
													<td style="width: 10%;"><a id="deleteKeyBtn"
														class="no-drop"
														href="javascript:delOneSecretKey(${cre.id })"
														style="margin-left: 10px"> <i class="fa fa-trash"></i>
													</a></td>
												</tr>
											</c:forEach>
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
	<input type="hidden" id="userAutority" value="${cur_user.user_autority}">
	<div id="createKeyCon" style="display: none">
		<div style="margin: 15px 15px">
		    <div class="infoCred">
                <span class="labelCred">仓库：</span> 
                <select class="form-control conCred" id="codeType" name="codeType">
                    <option value="1">Git</option>
                    <option value="2">SVN</option>
                </select>
            </div>
			<div class="infoCred userLimit">   
				<span class="labelCred">认证：</span> 
				<select class="form-control conCred" id="CredentialsType" name="type" disabled>
					<option value="1">用户名和密码</option>
				</select>
			</div>
			<div class="infoCred adminLimit">   
				<span class="labelCred">认证：</span> 
				<select class="form-control conCred" id="CredentialsType" name="type" disabled>
					<option value="2">SSH用户名和密钥</option>
				</select>
			</div>
			<div class="infoCred">
				<span class="labelCred">用户名：<font color="red">*</font></span> <input type="text"
					class="form-control conCred" id="userNameCred" name="userName"
					value="">
			</div>
			<div class="infoCred userLimit">
				<span class="labelCred">密码：<font color="red">*</font></span> <input type="password"
					class="form-control conCred" id="passwordCred" name="password"
					value="">
			</div>
			<div class="infoCred">
				<span class="labelCred">描述：<font color="red">*</font></span>
				<textarea type="text" class="form-control conCred"
					id="keyRemark" name="keyRemark" rows="4" value=""></textarea>
			</div>
			<div class="infoCred adminLimit">
				<span class="labelCred">代理：</span>
				<div class="conCred"><input type="checkbox" id="proxy"></div>
			</div>
			<div class="infoCred adminLimitproxy">
				<span class="labelCred">host：<font color="red">*</font></span> 
				<input type="text" class="form-control conCred" id="host" name="host"
					value="">
			</div>
			<div class="infoCred adminLimitproxy">
				<span class="labelCred">IP：<font color="red">*</font></span> 
				<input type="text" class="form-control conCred" id="ip" name="ip"
					value="">
			</div>
			<div class="infoCred adminLimitproxy">
				<span class="labelCred">port：<font color="red">*</font></span> 
				<input type="text" class="form-control conCred" id="port" name="port"
					value="">
			</div>
			<div class="infoCred adminLimitproxy">
				<span class="labelCred">policy：</span> 
				<input type="text" class="form-control conCred" id="policy" name="policy"
					value="">
			</div>
			<div class="infoCred adminLimitproxy">
				<span class="labelCred">identifyFile：</span> 
				<input type="text" class="form-control conCred" id="identifyFile" name="identifyFile"
					value="">
			</div>
		</div>
	</div>
	<div id="secretKeyDetail" style="display: none">
		<div style="margin: 15px 15px">
		    <div class="infoCred">
                <span class="labelCred">仓库：</span> 
                <select class="form-control conCred" id="codeTypeDetail" name="codeType" disabled>
                    <option value="1">Git</option>
                    <option value="2">SVN</option>
                </select>
            </div>
			<div class="infoCred">   
				<span class="labelCred">认证：</span> 
				<select class="form-control conCred" id="CredentialsTypeDetail" name="type" disabled>
					<option value="1">用户名和密码</option>
					<option value="2">SSH用户名和密钥</option>
				</select>
			</div>
			<div class="infoCred">
				<span class="labelCred">用户名：</span> <input type="text"
					class="form-control conCred" id="userNameCredDetail" name="userName"
					value="" readonly="readonly">
			</div>
			<div class="infoCred normal">
				<span class="labelCred">密码：</span> <input type="password"
					class="form-control conCred" id="passwordCredDetail" name="password"
					value="" readonly="readonly">
			</div>
			<div class="infoCred">
				<span class="labelCred">描述：</span>
				<textarea type="text" class="form-control conCred"
					id="keyRemarkDetail" name="keyRemark" rows="2" value="" readonly="readonly"></textarea>
			</div>
			<div class="infoCred ssh">
				<span class="labelCred">公钥：<br><i class="fa fa-clipboard" onclick="copySshPwd(this)" style="margin-left:10px;cursor:pointer;color:#36C"></i>&nbsp;(复制)</span> 
				<textarea type="text" class="form-control conCred"
					id="SSHpasswordCredDetail" rows="4" value="" readonly="readonly"></textarea>
			</div>
		</div>
	</div>
	<!-- ssh认证密钥 -->
	<div id="sshPwdInfo" style="display:none">
		<div style="width: 90%; margin: 0 auto;margin-top:10px">
			<span>认证已经生成，请添加下面的公钥到对应代码托管平台。<i class="fa fa-clipboard" onclick="copySshPwd(this)" style="margin-left:10px;cursor:pointer;color:#36C"></i>&nbsp;(复制)</span>
			<textarea rows="8" id="sshPassword" style="width:100%;margin-top:10px;border:1px solid #ddd"></textarea>
		</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		$('.dataTables-example').dataTable({
	        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ,5] }],
	        "aaSorting": [[ 4, "desc" ]]
		});
		$("#checkallbox").parent().removeClass("sorting_asc");
		
		
	});
	</script>
</body>
</html>
