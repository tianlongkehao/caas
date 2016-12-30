<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>快速构建</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/storage.css" />
<script type="text/javascript"
	src="<%=path%>/js/storage/storage_add.js"></script>
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
						<li><a href="<%=path %>/home"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">服务</li>
						<li style="margin-left: -44px;"><i class="fa fa-angle-right"></i></li>
						<li class="active">创建备份</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre">
								<ul style="padding: 0 4rem;">
									<li class="radius_step action" style="width: 100%">基本信息</li>
								</ul>
							</div>

							<%--基本信息--%>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/service/storage/build" method="post">
								<div class="container" style="width:80%">
								<div class="row">
									<div class="form-group col-md-12">
										<label class="col-md-2 s-title">名称：</label>
										<div class="col-md-6" style="line-height:34px">
											<input id="storageName" name="storageName"
												class="form-control" type="text" required=""
												placeholder="3到5个字符，可由字母，数字，下划线组成">
										</div>

									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<label class="col-md-2 s-title">存储大小：</label>
										<div class="col-md-8 storageSizes" style="line-height:34px">
											<input type="radio" name="storageSize" class="storageSize" id="size20"
												value="20480"><label for="size20">20<span class="s-unit">G</span></label> 
											<input type="radio" name="storageSize" class="storageSize" id="size50" 
												value="51200"><label for="size50">50<span class="s-unit">G</span></label>
											<input type="radio" name="storageSize" class="storageSize" id="size100"
												value="102400"><label for="size100">100<span class="s-unit">G</span></label> 
											<input type="radio" name="storageSize" class="storageSize" id="defVolNum">
											<label for="defVolNum"><input id="defVol" type="number"
												placeholder="自定义大小"><span class="is-unit">G</span></label> 
											<span style="color: #1E90FF; padding-left: 15px">总量：<span
												id="totalVol">${cur_user.vol_size}</span>G
											</span> <span style="color: #1E90FF; padding-left: 15px">剩余：<span
												id="restVol">${leftstorage }</span>G 可用
											</span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<label class="col-md-2 s-title">读写权限：</label>
										<div class="col-md-4" style="line-height:34px">
											<input type="checkbox" id="readOnlyOp1" name="isVolumeRead"
												class="isVolumeReadonly"> <label for="readOnlyOp1"
												style="margin-bottom: 0px;">只读</label>
										</div>
									</div>
								</div>
								<div class="row" style="display: none">
									<div class="form-group col-md-12">
										<label class="col-md-2">存储格式：</label>
										<div class="col-md-4">
											<select id="format" name="format" class="form-control"
												style="width: 189px;">
												<option value="3">ext4</option>
												<option value="2">reiserfs</option>
												<option value="1">xfs</option>
											</select>
										</div>
									</div>
								</div></div>
								<div class="container" style="width:90%">
								<div class="list-item-description" style="padding-top: 100px;">
                                            <a href="<%=path %>/service/storage"><span class="btn btn-default" style="margin-right: 30px;">返回</span></a>
                                            <span id="buildStorage" class="btn btn-primary pull-right">保存</span>
                                        </div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>

</body>
</html>