<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/css/core/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/service.css" />
<script type="text/javascript" src="<%=path%>/js/plugins/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/service/service-debug.js"></script>
</head>
<body onunload="alert('The onunload event was triggered')">

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>
	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="#"><i class="fa fa-home"></i>&nbsp;&nbsp;控制台</a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">服务</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width: inherit;">${service.serviceName }(${podip})</li>
					</ol>
				</div>
				
				<div id="tabs">
					<ul>
						<li><a href="#doc">文件</a></li>
						<li><a href="#ftp">命令行</a></li>
						<li><a href="#export">导出</a></li>
					</ul>
					<input hidden="true" value="${containerId }" id="containerId"/>
					<input hidden="true" value="${imageName }" id="imageName"/>
					<div id="doc" >
						<div class="row">
							<div class="scpDoc">
								<div class="ibox float-e-margins" style="margin-bottom:0px">
									<div class="ibox-title">
										<h5>
											<span>${podip }<input type="text" id="scp-path" onkeypress="EnterPress(event)" onkeydown="EnterPress()" value=""></span>
										</h5>
	
										<div class="ibox-tools">
											<a href="javascript:creatable(null,'./');" id="volReloadBtn" title="刷新"> <i class="fa fa-repeat"></i></a>
											<a href="javascript:createdir()" id="adddir" title="新建"> <i class="fa fa-plus"></i></a>
											<a href="javascript:fileUpload()" id="fileUpload" title="上传文件"> <i class="fa fa-upload"></i></a>
	<!-- 										<a href="javascript:download()" id="fileDownloadFiles" title="导出文件"> <i class="fa fa-download"></i></a> -->
											<a href="javascript:delfiles()" id="deleteButton" title="删除"> <i class="fa fa-trash"></i></a>
										</div>
									</div>
									<div class="ibox-content">
										<table style="border-collapse: collapse; margin: 0 auto;" class="table">
											<thead style="display: block;">
												<tr>
													<th style="width: 5%; text-indent: 14px;"><input type="checkbox" class="chkAll"></th>
													<th style="width: 25%; text-indent: 25px;">文件名</th>
													<th style="width: 15%; text-indent: 61px;">大小</th>
													<th style="width: 30%; text-indent: 151px;">修改日期</th>
													<th style="width: 15%; text-indent: 131px;">操作</th>
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
					<div class="contentMain" id="ftp">
						<iframe id="shellinabox" src="${sshhost}" width="100%" height="500px"></iframe>
					</div>
					<div id="export">
						<div class="">
							<span class="name-note">${shrotImageName }</span> <input
								name="imgNameFirst" type="hidden" value="${shrotImageName }">
							: <input id="version" name="version"
								type="text" value="debug" class="name-input">
						</div>
						<a href="javascript:saveAsImage('${containerId}','${nodeName }')" class="btn">保存容器为镜像</a>
					</div>
				</div>
				
				<div id="upload-template" hidden="true">
					<div style="width: 345px; margin: 5px 10px 5px 10px">
						<form method="POST" enctype="multipart/form-data" action="upload" id="form1" name="form1">
							<p>
								文件：<input type="file" multiple="multiple" name="file" id="file" />
							</p>
							<input type="hidden" name="path" value="" id="path" />
						</form>
					</div>
				</div>
				<!--进度条 -->
				<div class="modal fade container" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 30%">
					<div class="progress progress-striped active" id="loading" style="margin-top: 87%;">
						<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;"></div>
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
<script>
$(document).ready(function () {
	//tab页切换
	$( "#tabs" ).tabs();
	//拖动
	$(".sortable-list").sortable({
		connectWith: ".connectList"
	}).disableSelection();
	//缩放
	$( ".localDoc" ).resizable({
  		autoHide: true,
		containment: ".row",
		maxHeight: 535,
		maxWidth:770,
		minWidth:413,
		resize: function( event, ui ) {
			var localDocWidth = $(".localDoc").width();
			var totalWidth = $(".localDoc").parent().width();
			var scpDocWidth = totalWidth - localDocWidth - 10;
			$(".scpDoc").width(scpDocWidth+"px");
		}
	});
});
</script>
		</article>
	</div>
</body>
</html>