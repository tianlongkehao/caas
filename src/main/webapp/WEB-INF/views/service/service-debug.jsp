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
<body >

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
						<li class="active">${service.serviceName }</li>
					</ol>
				</div>
				
				<div id="tabs">
					<ul>
						<li><a href="#doc">文件</a></li>
						<li><a href="#ftp">ftp</a></li>
					</ul>
					<div id="doc" >
						<div class="row">
							<div class="col-sm-6 localDoc">
								<div class="ibox float-e-margins" style="margin-bottom:0px">
									<div class="ibox-title">
										<h5>
											<span>本地文件</span>
										</h5>
	
										<div class="ibox-tools">
											<a href="javascript:creatable(null,'./');" id="volReloadBtn" title="刷新"> <i class="fa fa-repeat"></i></a>
											
										</div>
									</div>
									<div class="ibox-content">
										<table style="border-collapse: collapse; margin: 0 auto;" class="table">
											<thead style="display: block;">
												<tr>
													<th style="width: 5%; text-indent: 14px;"><input type="checkbox" class="chkAll"></th>
													<th style="width: 25%; ">文件名</th>
													<th style="width: 15%; ">大小</th>
													<th style="width: 30%; ">修改日期</th>
													<th style="width: 15%; ">操作</th>
												</tr>
											</thead>
											<tbody id="mybody1" style="overflow-y: auto; height: 400px; display: block; width: 100%" class="sortable-list connectList agile-list">
	
											</tbody>
											<tfoot class="hide">
												<tr>
													<td colspan="5">
														<ul class="pagination pull-right"></ul>
													</td>
												</tr>
											</tfoot>
										</table>
										<ul class="sortable-list connectList"></ul>
									</div>
								</div>
							</div>
							<div class="col-sm-6 scpDoc">
								<div class="ibox float-e-margins" style="margin-bottom:0px">
									<div class="ibox-title">
										<h5>
											<span>${podip }<input type="text" id="scp-path" value=""></span>
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
													<th style="width: 25%; text-indent: 10px;">文件名</th>
													<th style="width: 15%; text-indent: 0px;">大小</th>
													<th style="width: 30%; text-indent: 0px;">修改日期</th>
													<th style="width: 15%; text-indent: 10px;">操作</th>
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
						<iframe id="shellinabox" src="https://192.168.0.75:4200/ssh" width="100%" height="500px"></iframe>
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
// //创建ifame对象
// var iframe = document.createElement("shellinabox");
// //设置ifame对象src属性
// iframe.src = "https://192.168.0.75:4200/ssh";

// if (!/*@cc_on!@*/0) { //如果不是ie
//     iframe.onload = function(){ 加载
//         alert("Local iframe is now loaded.");
//     };
// } else {
//     iframe.onreadystatechange = function(){
//         if (iframe.readyState == "complete"){ 完成状态判断
//             alert("Local iframe is now loaded.");
//         }
//     };
// }

// fillseo_window.onbeforeunload=function(){
// alert("弹弹弹！");
// };
</script>
		</article>
	</div>
</body>
</html>