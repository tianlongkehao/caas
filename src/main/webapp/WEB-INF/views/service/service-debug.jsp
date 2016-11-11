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
				<div style="width: 100%; float: right; background-color: #fff;">
					<div class="row">
						<div class="col-sm-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>
										<span>全部文件</span>
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
												<th style="width: 25%; text-indent: 30px;">文件名</th>
												<th style="width: 20%; text-indent: 5px;">大小</th>
												<th style="width: 25%; text-indent: 8px;">修改日期</th>
												<th style="width: 10%; text-indent: 80px;">操作</th>
											</tr>
										</thead>
										<tbody id="mybody" style="overflow-y: auto; height: 400px; display: block; width: 100%">

										</tbody>
										<tfoot class="hide">
											<tr>
												<td colspan="5">
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
				<div class="contentMain">
					<iframe id="shellinabox" src="https://192.168.0.75:4200/ssh" width="100%" height="500px"></iframe>
				</div>
			</div>
<script>
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