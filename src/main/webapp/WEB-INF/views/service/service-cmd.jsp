<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>终端</title>
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
<script type="text/javascript" src="<%=path%>/js/service/service-cmd.js" defer ></script>
</head>
<body>

	<input type="hidden" value="${dockerServerURL }" id="nodeIP">
	<input type="hidden" value="${entryHost }" id="entryHost">
	<input type="hidden" value="${dockerIOPort }" id="dockerIOPort">
	<input type="hidden" value="${containerid }" id="containerid">

	<!-- CMD -->
	<div class="containerCMD">
		<div class="weblogtitle" style="width: 100%">
			<div class="pull_left">
				<span class="circle red"></span> <span class="circle blue"></span> <span
					class="circle green"></span>
			</div>
			<div class="pull_right">
				<i id="saveImage" class="fa fa-save cursor" style="color: #2FBA66"
					title="保存镜像"></i>
			</div>
		</div>
		<div id="terminal-container"
			style="width: 100%; height: 100%; margin: 0 auto;"></div>
	</div>
	<!-- 保存镜像 -->
	<div id="saveImageCon" style="display: none">
		<input id="containerId" type="hidden" value="${containerid }">
		<input id="containerIp" type="hidden" value="${dockerServerURL }">
		<ul class="popWin">
			<li class="line-h-3 c-ser">
				<div class="">
					<span class="">镜像名称：</span>
					<input id="imageName" class="needImageInfo" id="confServiceName" type="text" value="">
				</div>
			</li>
			<li class="line-h-3 c-ser">
				<div class="">
					<span class="">镜像版本：</span>
					<input id="version" type="text" class="needImageInfo" value="">
				</div>
			</li>
			<li class="line-h-3 c-ser">
				<div class="">
					<span class="">启动命令：</span>
					<input id="cmdString" type="text" class="needImageInfo" value="">
				</div>
			</li>
		</ul>
	</div>
	<!--进度条 -->
	<div class="modal fade container" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 30%">
		<div class="progress progress-striped active" id="loading" style="margin-top: 87%;">
			<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;font-size:130%;"></div>
		</div>
	</div>

</body>
</html>
