<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>dockerfile</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/codemirror.css" />
	<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/ambiance.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<script type="text/javascript" src="<%=path%>/js/plugins/codemirror.js"></script>
<script type="text/javascript" src="<%=path%>/js/template/dockerfile-edit.js"></script>
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
						<li class="active" id="nav2">模板</li>
						<li style="margin-left: -44px;"><i class="fa fa-angle-right"></i></li>
						<li class="active" style="width:108px;">编辑dockerfile</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action" style="width:100%">编辑dockerfile</li>
                                </ul>
                            </div>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/template/dockerfile/build" method="post">
								<div class="container" style="width: 90%">
									<div class="row">
                                    <div class="form-group col-md-12">
                                        <span style="float:left;line-height:34px">模板名称：</span>
                                         <input type="hidden" id="dockerFileId" value = "${dockerFileTemp.id }" />
                                        <input type="text" class="form-control" id="dockerFileTemplateName" style="width: 93%" disabled value = "${dockerFileTemp.templateName }" />
                                    </div>
									<div class="form-group col-md-12">
										<textarea id="dockerFile" name="dockerFile">${dockerFileTemp.dockerFile }</textarea>
									</div>
								</div>
									
								</div>
								<div class="container" style="width: 90%">
									<div class="list-item-description" style="padding-top: 100px;">
										<a href="<%=path%>/template/dockerfile"><span
											class="btn btn-default" style="margin-right: 30px;">返回</span></a>
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