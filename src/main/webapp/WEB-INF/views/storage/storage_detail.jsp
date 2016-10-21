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
								
								<input hidden="true" value="" id="downfilepath"/>
							</li>
						</ul>
						<!--进度条 -->
				    <div class="modal fade container" id="myModal" tabindex="-1" role="dialog"
				        aria-labelledby="myModalLabel" aria-hidden="true" style="width:30%">
				        <div class="progress progress-striped active" id="loading"
				            style="margin-top: 87%;">
				            <div class="progress-bar progress-bar-info" role="progressbar"
				                aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
				                style="width: 100%;">
				            </div>
				        </div>
				    </div>		
						<div class="pull-right parameter-list">
							<canvas id="parameter" width="120px" height="120px"></canvas>
							<div class="param-text">
								<p class="param-used">
									<span class="i"></span>已使用：<span id="hasUsed"></span>M
								</p>
								<p class="param-sum">
									<span class="i" ></span>总&nbsp;&nbsp;&nbsp;&nbsp;量：<span id="totalSize" >${storage.storageSize }</span>
									M
								</p>
							</div>
							<input type="hidden" id="sizeUsedPersent" value="0.01"> <input
								type="hidden" id="volumeSize" value="512">
						</div>
						<div style="width: 83%; float:right; background-color: #fff;">
							<div class="row">
								<div class="col-sm-12">
									<div class="ibox float-e-margins">
										<div class="ibox-title">
											<h5>
												<div class="" id="allboxs"><span >全部文件</span></div>
											</h5>

											<div class="ibox-tools">
												<a href="javascript:refreshtable()"
													id="volReloadBtn" title="刷新"><i class="fa fa-repeat"></i></a>
												<a href="javascript:createdir()" id="adddir" title="新建"><i
	                                          class="fa fa-plus"></i></a>  
												<a  id="fileUpload" title="上传文件"><i
													class="fa fa-upload"></i></a> <a id="fileDownload"
													title="导出文件"><i class="fa fa-download"></i>
													<input hidden="true" value="" id="downfilepath"/>
													</a> 
												<a id="deleteButton" class="no-drop"
													href="javascript:delfiles()" title="删除"> <i
													id="deleteButtonfile" class="fa fa-trash self_a"></i>
												</a>
												<a id="unzip" href="javascript:unzipFile()" title="解压zip文件"><i class="fa fa-hdd-o"></i></a>
											</div>
										</div>
										<div class="ibox-content" >
											<table style="border-collapse:collapse;margin:0 auto;"
												class="table table-stripped table-hover dataTables-example">
												<thead style="display:block;">
													<tr>
														<th style="width: 5%;text-indent: 14px;"><input type="checkbox" class="chkAll"></th>
														<th style="width: 25%; text-indent: 30px;">文件名</th>
														<th style="width: 20%; text-indent: 15px;">大小</th>
														<th style="width: 25%; text-indent: 8px;">修改日期</th>
														<th style="width: 10%; text-indent: 100px;"
															class="del-operation">操作</th>
													</tr>
												</thead>
												<tbody id="mybody" style="overflow-y:auto; height:400px;display:block;width:100%">

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
						<div id="environment-templat" hidden="true">
                <div style="width: 345px; margin: 5px 10px 5px 10px">
                   <form method="POST" enctype="multipart/form-data" action="upload" id="form1" name="form1"> 
                     <p>文件：<input type="file" multiple="multiple" name="file" id="file" /></p>
                     <input type="hidden" name="path" value="sfasf" id="path" />
                     <input type ="hidden" name="storageName" value=${storage.storageName }  />
                     <input type ="hidden" name="id" value=${storage.id }  >
                   </form>
                 </div>
            </div>
            <div id="createdir-templat" hidden="true">
                <div style="width: 345px; margin: 5px 10px 5px 10px">
                     <p>新建文件夹名字：<input type="text" name="newdir" id="newdir" /></p>
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
	        if(""==downfiles){
	        	   alert("请选择需要下载的文件");
	        	   return;
	                  }
	        location.href = ctx + "/media?directory=" + directory +"&"+ downfiles;
	    }
	</script>
</body>
</html>