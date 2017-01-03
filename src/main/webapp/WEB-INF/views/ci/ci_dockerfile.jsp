<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>Dockerfile构建</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<script type="text/javascript" src="<%=path%>/js/ci/ci_dockerfile.js"></script>
</head>
<body>

	<jsp:include page="../frame/menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2" style="width: 110px">Dockerfile构建</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="item-obj">
						<div class="container">
							<h4>Dockerfile构建</h4>

							<form id="buildForm" name="buildForm"
								action="<%=path%>/ci/addDockerFileCi.do" method="post"
								enctype="multipart/form-data">

								<div class="row depot-name">
									<div class="form-group col-md-7">
										<div class="" style="margin-top: 25px">
											<span class="name-note">${username } /&nbsp;</span> <input
												name="imgNameFirst" id = "imgNameFirst" type="hidden" value="${username }">
											<input id="imgNameLast" name="imgNameLast" type="text"
												class="name-input" value=""> : <input
												id="imgNameVersion" name="imgNameVersion" type="text"
												value="latest" class="name-input">
										</div>
									</div>
									<div class="form-group col-md-5">
										<label>性质</label>
										<div>
											<span class="btn-groups btn-imageType"> <a
												case="public" class="btns active"
												onclick="javascript:$('#imgType').val(1)">公有</a> <a
												case="private" class="btns"
												onclick="javascript:$('#imgType').val(2)">私有</a>
											</span> <input type="hidden" name="imgType" id="imgType" value="1">
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<label>简介</label> <input id="description" name="description"
											class="form-control" type="text" required="">
									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<label for="sourceCode">上传程序包</label> <input type="file"
											class="files" name="sourceCode" id="sourceCode"
											multiple="multiple" onchange="selectFile(this)">
									</div>
								</div>
								<div class="row" id="input-con">
									
								</div>
								
								<div class="row">
									<div class="form-group col-md-12">
										<label>编写dockerfile</label> <span id="docImportBtn"
											class=" btn-info btn-sm" style="cursor: pointer">导入模板</span>
										<span id="docExportBtn" class=" btn-info btn-sm"
											style="cursor: pointer">另存为模板</span>
									</div>

									<div class="form-group col-md-12" id="dockerFiles">
										<textarea id="dockerFile" name="dockerFile"></textarea>
									</div>
								</div>
								<section class="registryinfo">
									<table class="table registry">
										<tbody>
											<tr>
												<td class="first-info"><b style="font-weight: 500;">基本信息</b></td>
											</tr>
											<tr>
												<td><span class="reg-text">项目名称</span> <input
													id="projectName" name="projectName" type="text" value=""
													class="reg-input"></td>
											</tr>
										</tbody>
									</table>
								</section>
								<br>
								<div class="pull-right">
									<span id="buildBtn" class="btn btn-primary">创建</span>
								</div>
							</form>
						</div>
					</div>
				</div>


				<!--dockerfile导入模板 -->
                <div id="dockerfile-import" style="max-height:170px;overflow-y:scroll;overflow-x:hidden;">
                    <table class="table table-hover enabled" id="Path-table-doc"
                        style="width: 326px; margin: 5px 10px 5px 10px">
                        <tbody id="dockerfile-body">
                           
                        </tbody>
                    </table>
                </div>
                <!-- dockerfile另存为模板 -->
                <div id="dockerfile-export">
                    <div style="width: 345px; margin: 5px 10px 5px 10px">
                        <span>模板名称：</span><input type="text" id="dockerFileTemplateName"
                            style="width: 77%" autofocus="autofocus" />
                    </div>
                </div>
			</div>
		</article>
	</div>
</body>
</html>