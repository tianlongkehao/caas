<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/service.css" />
<script type="text/javascript" src="<%=path%>/js/service/service-debug.js"></script>
</head>
<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/home"><i class="fa fa-home"></i><span id="nav1">&nbsp;&nbsp;控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li><a href="<%=path %>/service"><span id="nav2">服务管理</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: inherit;">${service.serviceName }(${podip})</li>
					</ol>
				</div>
				
				<div class="contentMain">
					<div class="baseInfo center-style">
						<ul class="nav navbar-nav">
							<li><a class="DOC btn-prim serDetail">文件</a></li>
							<li><a class="CMD serDetail">命令行</a></li>
							<li><a class="EXPORT serDetail">导出</a></li>
						</ul>
					</div>
					
					<div class="docInfo container">
						<div id="doc" >
							<div class="row">
								<div class="scpDoc">
									<div class="ibox float-e-margins" style="margin-bottom:0px">
										<div class="ibox-title">
											<h5>
												<span>${podip }<input type="text" id="scp-path" onkeypress="EnterPress(event)" onkeydown="EnterPress()" value=""></span>
											</h5>
		
											<div class="ibox-tools">
												<a href="javascript:creatable(null,'./');" id="volReloadBtn" title="刷新"> <i class="fa fa-repeat fa-i"></i></a>
												<a href="javascript:createdir()" id="adddir" title="新建"> <i class="fa fa-plus fa-i"></i></a>
												<a href="javascript:fileUpload()" id="fileUpload" title="上传文件"> <i class="fa fa-upload fa-i"></i></a>
		<!-- 										<a href="javascript:download()" id="fileDownloadFiles" title="导出文件"> <i class="fa fa-download"></i></a> -->
												<a href="javascript:delfiles()" id="deleteButton" title="删除"> <i class="fa fa-trash fa-i"></i></a>
											</div>
										</div>
										<div class="ibox-content">
											<table style="border-collapse: collapse; margin: 0 auto;" class="table">
												<thead style="display: block;">
													<tr>
														<th style="width: 61px;"><input type="checkbox" class="chkAll"></th>
														<th style="width: 307px;">文件名</th>
														<th style="width: 183px;">大小</th>
														<th style="width: 369px;">修改日期</th>
														<th style="width: 184px;">操作</th>
													</tr>
												</thead>
												<tbody id="mybody" style="overflow-y: auto; height: 400px; display: block; width: 100%" class="sortable-list connectList agile-list">
		
												</tbody>
												
											</table>
											<ul class="sortable-list connectList"></ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div class="cmdInfo hide">
						<div class="contentMain" id="ftp">
							<iframe id="shellinabox" src="" value="" width="100%" height="500px"></iframe>
						</div>
					</div>
					
					<div class="exportInfo container hide">
						<div id="export">
							<div class="">
								<span class="name-note" style="float:left;line-height:34px">${shrotImageName }：</span> <input
									name="imgNameFirst" type="hidden" value="${shrotImageName }">
								<input id="version" name="version" style="width:20%;"
									type="text" value="debug" class="name-input form-control">
							</div>
							<a href="javascript:saveAsImage('${containerId}','${nodeIP }')" type="button" class="btn btn-primary" style="margin-top:10px;color:white;">保存容器为镜像</a>
						</div>
					</div>
				</div>
					
				<input hidden="true" value="${containerId }" id="containerId"/>
				<input hidden="true" value="${imageName }" id="imageName"/>
				<input hidden="true" value="${sshhost}" id="ssh_host"/>
				
				<div id="upload-template" hidden="true">
					<div style="width: 345px; margin: 5px 10px 5px 10px">
						<form method="POST" enctype="multipart/form-data" action="upload" id="form1" name="form1">
							<p>
								文件：<input type="file" multiple="multiple" name="file" id="file" />
							</p>
							<input type="hidden" name="path" value="" id="path" />
							<input type="hidden" name="uuid" value="" id="uuid"/>
							<input hidden="true" value="${hostkey }" name="hostkey" id="hostkey"/>
						</form>
					</div>
				</div>
				<!--进度条 -->
				<div class="modal fade container" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 30%">
					<div class="progress progress-striped active" id="loading" style="margin-top: 87%;">
						<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;font-size:130%;"></div>
					</div>
				</div>
				<div id="createdir-templat" hidden="true">
					<div style="width: 345px; margin: 5px 10px 5px 10px">
						<p>
							新建文件夹名字：<input type="text" name="newdir" id="newdir" />
						</p>
					</div>
				</div>
				
			</div>
		</article>
	</div>
</body>
</html>