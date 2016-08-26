<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>服务</title>
<%@include file="../frame/header.jsp"%>
<script type="text/javascript" src="<%=path %>/js/storage/storage_detail.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/storage.css" />

</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="service" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">服务</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active">存储和备份</li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="storageName">${storage.storageName }</li>
					</ol>

				</div>
				<div class="contentMain">

					<aside class="aside-btn">
						<div class="btns-group">
							<span class="btn btn-defaults btn-white"><i
								class="icon-map-marker"></i><span class="ic_left">BCM</span></span>
						</div>
					</aside>

					<section class="detail-succeed">
						<div class="icon-img" id="storageUsed">
							<div class="type-icon">
								<i class="fa fa-hdd-o"></i>
							</div>
						</div>
						<ul class="succeed-content pull-left">
							<li>卷组名称：&nbsp;&nbsp;&nbsp;${storage.storageName }</li>

							<li>使用状态：&nbsp;&nbsp;&nbsp; <c:if
									test="${storage.useType ==1 }">
                         	未使用
                        </c:if> <c:if test="${storage.useType ==2 }">
                         	使用
                        </c:if>
							</li>

							<li>大&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;小：&nbsp;&nbsp;&nbsp;${storage.storageSize }
								M</li>
							<li>格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;式：&nbsp;&nbsp;&nbsp;${storage.format }</li>
							<li>创建时间：&nbsp;&nbsp;&nbsp;${storage.createDate }</li>
							<li>内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容：&nbsp;&nbsp;&nbsp;
								<span class="btn btn-primary upload" id="fileUpload"> 上传文件 </span> <span
								class="btn btn-primary download" id="fileDownload"> 导出文件 </span>
								<input hidden="true" value="" id="downfilepath"/>
							</li>
						</ul>
						
						<div class="pull-right parameter-list">
							<canvas id="parameter" width="120px" height="120px"></canvas>
							<div class="param-text">
								<p class="param-used">
									<span class="i"></span>已使用：<span id="sizeUsed">0</span>M
								</p>
								<p class="param-sum">
									<span class="i"></span>总&nbsp;&nbsp;&nbsp;&nbsp;量：${storage.storageSize }
									M
								</p>
							</div>
							<input type="hidden" id="sizeUsedPersent" value="0.01"> <input
								type="hidden" id="volumeSize" value="512">
						</div>
						<div style="width: 83%; float:right; background-color: #fff;">
							<div style="padding-left:15px">
								<div class="" style="line-height:40px" id="allboxs"><span >全部文件</span></div>
								<div class="" style="line-height:40px; display:none" id="nextboxs">
									<ul style="margin:0px">
										<li style="float:left"><a href="">返回上一级</a><span style="padding:5px">|</span><a href="">全部文件</a><span style="padding:5px">></span></li>
										<li style="float:left">{当前文件名称}</li>
									</ul>
								</div>
							</div>
							<table class="table table-hover table-bordered"
								style="margin: 0px; background-color: #fff; width: 98%; margin:0 auto; margin-bottom:20px">
								<thead style="background-color: #F5F6F6">
									<tr class="vol_list">
										<th style="text-indent: 14px;"><input type="checkbox" class="chkAll"></th>
										<th style="width: 40%;">文件名</th>
										<th style="width: 30%;">大小</th>
										<th style="width: 26%;">修改日期</th>
									</tr>
								</thead>
								<tbody id ="mybody">
									<tr class="vol_list" style="cursor:pointer">
										<td style="text-indent: 14px;"><input type="checkbox" class="chkItem" name="vol_chk" value="" ></td>
										<td style="width: 40%;"><a hrer=""><img src="/images/img-file.png" ><span onclick="creatable()" style="margin-left:5px">文件夹3</span></a></td>
										<td style="width: 30%;">33</td>
										<td style="width: 26%;"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div id="environment-templat" hidden="true">
                <div style="width: 345px; margin: 5px 10px 5px 10px">
                   <form method="POST" enctype="multipart/form-data" action="upload" id="form1" name="form1"> 
                     <p>文件：<input type="file" name="file" id="file" /></p>
                     <input type="hidden" name="path" value="sfasf" id="path">
                   </form>
                 </div>
            </div>
					</section>

				</div>
			</div>
		</article>
	</div>
	<script type="text/javascript">
		document.getElementById('fileDownload').onclick = function(){
	        var directory = document.getElementById('downfilepath').value;
	        var downfiles = $("input[name='downfiles']:checked").serialize();
	        location.href = ctx + "/media?directory=" + directory +"&"+ downfiles;
	    }
	</script>
</body>
</html>