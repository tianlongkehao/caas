<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>文件</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href= "<%=path%>/plugins/datetimepicker/css/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/service.css" />

<link rel="stylesheet" href="<%=path%>/plugins/xterm/build/xterm.css" />
<link rel="stylesheet" href="<%=path%>/plugins/xterm/build/addons/fullscreen/fullscreen.css" />

<script type="text/javascript" src="<%=path%>/plugins/xterm/build/xterm.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/addons/attach/attach.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/addons/fit/fit.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/xterm/build/addons/fullscreen/fullscreen.js" ></script>
<script type="text/javascript" src="<%=path%>/plugins/datetimepicker/js/jquery-ui-slide.min.js"></script>
<script type="text/javascript" src="<%=path%>/plugins/datetimepicker/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="<%=path%>/js/service/service-file.js" defer ></script>

</head>
<body style="overflow:hidden">

	<input type="hidden" value="${dockerServerURL }" id="nodeIP">
	<input type="hidden" value="${entryHost }" id="entryHost">
	<input type="hidden" value="${dockerIOPort }" id="dockerIOPort">
	<input type="hidden" value="${containerid }" id="containerid">

	<!-- 文件 -->
	<div class="containerFile" style="height:100%;min-height: 520px;">
		<input id="containerFileId" type="hidden" value=""> <input
			id="containerFileIp" type="hidden" value="">
		<div id="file-container"
			style="width: 98%; min-height: 500px; height:100%;margin: 0 auto;overflow: hidden">
			<div class="row">
				<div class="col-sm-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>
								<span>路径：<input id="path" type="text" value="" disabled></span>
							</h5>

							<div class="ibox-tools">
								<a href="javascript:sendChangeDirMsg('../')" id="back"
									title="返回上层"><i class="fa fa-mail-reply"></i></a> <a
									href="javascript:sendPwdMsg()" id="volReloadBtn" title="刷新"><i
									class="fa fa-repeat"></i></a> <a href="javascript:sendMkdirMsg()"
									id="adddir" title="新建"><i class="fa fa-plus"></i></a> <a
									href="javascript:fileUpload()" id="fileUpload" title="上传文件"><i
									class="fa fa-upload"></i></a>
							</div>
						</div>
						<div class="ibox-content">
							<table style="width: 100%">
								<thead>
									<tr>
										<th style="width: 5%; text-indent: 14px;"><input
											type="checkbox" class="chkAll"></th>
										<th style="width: 45%;">文件名</th>
										<th style="width: 40%;" class="del-operation">操作</th>
									</tr>
								</thead>
							</table>
							<div class="tableBody"
								style="overflow-y: auto; height: 100%; display: block; width: 100%">
								<table style="border-collapse: collapse; margin: 0 auto;"
									class="table table-stripped table-hover dataTables-example">

									<tbody id="fileBody">

									</tbody>

								</table>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!--新建文件夹 -->
	<div id="createdir-templat" hidden="true">
		<div style="width: 345px; margin: 5px 10px 5px 10px">
			<p>
				新建文件夹名字：<input type="text" name="newdir" id="newdir" />
			</p>
		</div>
	</div>
	<!--上传文件 -->
	<div id="upload-template" hidden="true">
		<div style="width: 345px; margin: 5px 10px 5px 10px">
			<form method="POST" enctype="multipart/form-data" action="upload" id="form1" name="form1">
				<input id="currentContainerId" name="currentContainerId" type="hidden" value="">
				<input id="currentContainerIp" name="currentContainerIp" type="hidden" value="">
				<input id="currentFilePath" name="currentFilePath" type="hidden" value="">
				<p>
					文件：<input type="file" multiple="multiple" name="file" id="file" />
				</p>
			</form>
		</div>
	</div>
	<!--进度条 -->
	<div class="modal fade container" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 30%">
		<div class="progress progress-striped active" id="loading" style="margin-top: 87%;">
			<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;font-size:130%;"></div>
		</div>
	</div>
</body>
</html>
