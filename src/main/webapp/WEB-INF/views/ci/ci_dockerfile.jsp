<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>上传构建</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/ci.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/codemirror.css" />
<script type="text/javascript" src="<%=path%>/js/ci/ci_dockerfile.js"></script>
<script type="text/javascript" src="<%=path%>/js/plugins/codemirror.js"></script>
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
								action="<%=path%>/ci/addResourceCi.do" method="post"
								enctype="multipart/form-data">
								<div class="row depot-name">
									<div class="form-group col-md-7">
										<div class="" style="margin-top:25px">
											<span class="name-note">${username } /&nbsp;</span> <input
												name="imgNameFirst" type="hidden" value="${dockerfilename }">
											<input id="imgNameLast" name="imgNameLast" type="text"
												class="name-input" value=""> : <input
												id="imgNameVersion" name="imgNameVersion" type="text"
												value="latest" class="name-input">
										</div>
									</div>
									<div class="form-group col-md-5">
	                                    <label>性质</label>
	                                    <div>
	                                        <span class="btn-groups btn-imageType">
	                                            <a case="public" class="btns active" onclick="javascript:$('#imgType').val(1)">公有</a>
	                                            <a case="private" class="btns" onclick="javascript:$('#imgType').val(2)">私有</a>
	                                        </span>
	                                        <input type="hidden" name="imgType" id="imgType" value="1">
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
											name="sourceCode" id="sourceCode">
									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<label>编写dockerfile</label> <span id="docImportBtn"
											class=" btn-info btn-sm" style="cursor: pointer">导入模板</span>
										<span id="docExportBtn" class=" btn-info btn-sm"
											style="cursor: pointer">另存为模板</span>
									</div>
									<!-- <div class="wrap-node">
										<textarea id="code" name="code"
											style="min-height: 280px; display: none;"></textarea>
										<div class="CodeMirror cm-s-mdn-like CodeMirror-wrap">
											<div
												style="overflow: hidden; position: relative; width: 3px; height: 0px; top: 4px; left: 43px;">
												<textarea autocorrect="off" autocapitalize="off"
													spellcheck="false"
													style="position: absolute; padding: 0px; width: 1000px; height: 1em; outline: none;"
													tabindex="0"></textarea>
											</div>
											<div class="CodeMirror-vscrollbar" cm-not-content="true">
												<div style="min-width: 1px; height: 0px;"></div>
											</div>
											<div class="CodeMirror-hscrollbar" cm-not-content="true">
												<div style="height: 100%; min-height: 1px; width: 0px;"></div>
											</div>
											<div class="CodeMirror-scrollbar-filler"
												cm-not-content="true"></div>
											<div class="CodeMirror-gutter-filler" cm-not-content="true"></div>
											<div class="CodeMirror-scroll" tabindex="-1">
												<div class="CodeMirror-sizer"
													style="margin-left: 39px; margin-bottom: -10px; border-right-width: 20px; min-height: 28px; padding-right: 0px; padding-bottom: 0px;">
													<div style="position: relative; top: 0px;">
														<div class="CodeMirror-lines">
															<div style="position: relative; outline: none;">
																<div class="CodeMirror-measure">
																	<pre>x</pre>
																</div>
																<div class="CodeMirror-measure"></div>
																<div style="position: relative; z-index: 1;"></div>
																<div class="CodeMirror-cursors" style="">
																	<div class="CodeMirror-cursor"
																		style="left: 4px; top: 0px; height: 20px;">&nbsp;</div>
																</div>
																<div class="CodeMirror-code">
																	<div style="position: relative;">
																		<div class="CodeMirror-gutter-wrapper"
																			style="left: -39px;">
																			<div
																				class="CodeMirror-linenumber CodeMirror-gutter-elt"
																				style="left: 6px; width: 21px;">1</div>
																		</div>
																		<pre class=" CodeMirror-line ">
																			<span style="padding-right: 0.1px;"><span
																				cm-text="">​</span></span>
																		</pre>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
												<div
													style="position: absolute; height: 20px; width: 1px; top: 28px;"></div>
												<div class="CodeMirror-gutters" style="height: 320px;">
													<div class="CodeMirror-gutter CodeMirror-linenumbers"
														style="width: 32px;"></div>
												</div>
											</div>
										</div>
									</div> -->

									<div class="form-group col-md-12">
										<textarea id="dockerfile"
											style="background-color: black; color: #37fc34; border: 0; width: 100%; height: 230px"></textarea>
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
								<!-- <section class="registryinfo">
                                <table class="table registry">
                                    <tbody>
                                    <tr>
                                        <td class="first-info"><b style="font-weight: 500;">编写dockerfile</b></td>
                                    </tr>
                                    <tr>
                                        
                                    </tr>
                                    </tbody>
                                </table>
                            </section> -->
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
						style="width: 326px; margin: 5px 10px 5px 10px;">
						<tbody id="dockerfile-body">
							<tr>
								<td class="vals vals-doc">demo1<span class="doc-tr hide"><i
										class="fa fa-check"></i></span></td>
							</tr>
							<tr>
								<td class="vals vals-env">demo2<span class="doc-tr hide"><i
										class="fa fa-check"></i></span></td>
							</tr>
							
						</tbody>
					</table>
				</div>
				<!-- dockerfile另存为模板 -->
				<div id="dockerfile-export">
					<div style="width: 345px; margin: 5px 10px 5px 10px">
						<span>模板名称：</span><input type="text" id="envTemplateName"
							style="width: 77%" autofocus="autofocus" />
					</div>
				</div>
			</div>
		</article>
	</div>

</body>
</html>